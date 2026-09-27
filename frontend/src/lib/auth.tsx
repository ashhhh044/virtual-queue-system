import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import type { AuthUser } from "../types";

export const AUTH_STORAGE_KEY = "queue_auth_user";

interface AuthContextValue {
  user: AuthUser | null;
  login: (user: AuthUser) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) {
      try {
        setUser(JSON.parse(stored));
      } catch {
        localStorage.removeItem(AUTH_STORAGE_KEY);
      }
    }
  }, []);

  function login(newUser: AuthUser) {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(newUser));
    setUser(newUser);
  }

  function logout() {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) {
      try {
        const parsed = JSON.parse(stored) as AuthUser;
        // best-effort - tell the backend to invalidate the refresh token too,
        // don't block logging out locally if this fails
        fetch(`${import.meta.env.VITE_API_URL}/auth/logout`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ refreshToken: parsed.refreshToken }),
        }).catch(() => {});
      } catch {
        // ignore
      }
    }
    localStorage.removeItem(AUTH_STORAGE_KEY);
    setUser(null);
  }

  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
