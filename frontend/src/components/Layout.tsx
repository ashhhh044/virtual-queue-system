import { Link, useNavigate } from "react-router-dom";
import type { ReactNode } from "react";
import { useAuth } from "../lib/auth";

export function Layout({ children }: { children: ReactNode }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <div className="min-h-screen bg-paper">
      <header className="border-b border-ink/10 bg-white">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-6 py-4">
          <Link to="/" className="flex items-center gap-2">
            <span className="font-mono text-lg font-bold text-navy">QUEUE</span>
            <span className="text-sm text-ink/50"> management</span>
          </Link>

          <nav className="flex items-center gap-5 text-sm">
            {!user && (
              <>
                <Link to="/join" className="text-ink/70 hover:text-ink">
                  Join a queue
                </Link>
                <Link to="/status" className="text-ink/70 hover:text-ink">
                  Check status
                </Link>
                <Link
                  to="/login"
                  className="rounded-md bg-navy px-3 py-1.5 font-medium text-paper hover:bg-navy-light"
                >
                  Staff / Admin login
                </Link>
              </>
            )}
            {user && (
              <>
                <span className="text-ink/70">
                  {user.name} · <span className="text-ink/40">{user.role}</span>
                </span>
                <button
                  onClick={handleLogout}
                  className="rounded-md border border-ink/15 px-3 py-1.5 font-medium hover:bg-ink/5"
                >
                  Log out
                </button>
              </>
            )}
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-6 py-10">{children}</main>
    </div>
  );
}
