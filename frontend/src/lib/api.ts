import { AUTH_STORAGE_KEY } from "./auth";
import type { AuthUser } from "../types";

const API_URL = import.meta.env.VITE_API_URL;

export class ApiError extends Error {
  status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

interface RequestOptions {
  method?: "GET" | "POST" | "PUT" | "DELETE";
  body?: unknown;
  token?: string | null;
  // internal - prevents an infinite retry loop if refresh itself 401s
  _isRetry?: boolean;
}

function getStoredUser(): AuthUser | null {
  const raw = localStorage.getItem(AUTH_STORAGE_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as AuthUser;
  } catch {
    return null;
  }
}

async function tryRefreshToken(): Promise<string | null> {
  const user = getStoredUser();
  if (!user?.refreshToken) return null;

  try {
    const res = await fetch(`${API_URL}/auth/refresh`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ refreshToken: user.refreshToken }),
    });
    const data = await res.json();
    if (!res.ok || !data.token) return null;

    // keep everything else the same, just swap in the new access token
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify({ ...user, token: data.token }));
    return data.token as string;
  } catch {
    return null;
  }
}

export async function apiRequest<T>(
  path: string,
  { method = "GET", body, token, _isRetry = false }: RequestOptions = {}
): Promise<T> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  let response: Response;
  try {
    response = await fetch(`${API_URL}${path}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new ApiError("Couldn't reach the server. Is the backend running?", 0);
  }

  // Access token expired mid-session (they're short-lived, 15 min) - try to
  // silently get a new one and retry this request exactly once.
  if (response.status === 401 && token && !_isRetry) {
    const newToken = await tryRefreshToken();
    if (newToken) {
      return apiRequest<T>(path, { method, body, token: newToken, _isRetry: true });
    }
    // refresh failed too - the session is genuinely over
    localStorage.removeItem(AUTH_STORAGE_KEY);
  }

  let data: any;
  try {
    data = await response.json();
  } catch {
    throw new ApiError(`Unexpected response from server (${response.status})`, response.status);
  }

  if (!response.ok || data.success === false) {
    throw new ApiError(data.message || "Something went wrong", response.status);
  }

  return data as T;
}
