import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest, ApiError } from "../lib/api";
import { useAuth } from "../lib/auth";
import type { Role } from "../types";

interface LoginResponse {
  token: string;
  refreshToken: string;
  role: Role;
  name: string;
  staffId?: number;
  adminId?: number;
}

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);

    if (!email.trim() || !password) {
      setError("Enter your email and password.");
      return;
    }

    setSubmitting(true);
    try {
      const data = await apiRequest<LoginResponse>("/auth/login", {
        method: "POST",
        body: { email, password },
      });

      login({
        token: data.token,
        refreshToken: data.refreshToken,
        role: data.role,
        name: data.name,
        id: data.role === "ADMIN" ? data.adminId! : data.staffId!,
      });

      navigate(data.role === "ADMIN" ? "/admin" : "/staff");
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Login failed.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="mx-auto max-w-sm">
      <h1 className="text-2xl font-bold">Staff / Admin login</h1>
      <p className="mt-1 text-ink/70">Sign in with your work email.</p>

      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <div>
          <label className="block text-sm font-medium">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 focus:border-navy"
            placeholder="staff@queue.com"
            autoComplete="username"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 focus:border-navy"
            autoComplete="current-password"
          />
        </div>

        {error && (
          <p className="rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={submitting}
          className="w-full rounded-md bg-navy px-5 py-3 font-medium text-paper hover:bg-navy-light disabled:opacity-50"
        >
          {submitting ? "Signing in…" : "Sign in"}
        </button>
      </form>

      <div className="mt-6 rounded-md border border-ink/10 bg-white px-4 py-3 text-sm text-ink/60">
        <p className="font-medium text-ink/80">Default accounts (dev only)</p>
        <p className="mt-1 font-mono">admin@queue.com / admin123</p>
        <p className="font-mono">staff@queue.com / staff123</p>
      </div>
    </div>
  );
};

export default Login;
