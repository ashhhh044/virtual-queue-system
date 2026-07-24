import { createContext } from "react";

interface User {
  id: number;
  name: string;
  email: string;
  role: "ADMIN" | "STAFF";
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);
