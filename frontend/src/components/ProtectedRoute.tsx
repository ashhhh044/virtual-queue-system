import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";
import { useAuth } from "../lib/auth";
import type { Role } from "../types";

export function ProtectedRoute({ allow, children }: { allow: Role[]; children: ReactNode }) {
  const { user } = useAuth();

  if (!user) return <Navigate to="/login" replace />;
  if (!allow.includes(user.role)) return <Navigate to="/" replace />;

  return <>{children}</>;
}
