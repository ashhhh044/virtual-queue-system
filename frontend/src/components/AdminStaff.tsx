import { FormEvent, useEffect, useState } from "react";
import { apiRequest, ApiError } from "../lib/api";
import { useAuth } from "../lib/auth";
import type { Staff } from "../types";

export function AdminStaff() {
  const { user } = useAuth();
  const [staffList, setStaffList] = useState<Staff[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [employeeId, setEmployeeId] = useState("");
  const [department, setDepartment] = useState("");
  const [creating, setCreating] = useState(false);

  async function load() {
    setLoading(true);
    try {
      const data = await apiRequest<{ staff: Staff[] }>("/admin/staff", { token: user?.token });
      setStaffList(data.staff);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't load staff.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  async function handleCreate(e: FormEvent) {
    e.preventDefault();
    if (!name.trim() || !email.trim() || !password || !employeeId.trim()) {
      setError("Name, email, password, and employee ID are required.");
      return;
    }
    // backend requires an 8+ character password
    if (password.length < 8) {
      setError("Password must be at least 8 characters.");
      return;
    }

    setCreating(true);
    setError(null);
    try {
      await apiRequest("/admin/staff", {
        method: "POST",
        token: user?.token,
        body: { name, email, phone, password, employeeId, department, counterNumber: 1 },
      });
      setName("");
      setEmail("");
      setPhone("");
      setPassword("");
      setEmployeeId("");
      setDepartment("");
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't create staff.");
    } finally {
      setCreating(false);
    }
  }

  async function handleDelete(staff: Staff) {
    if (!confirm(`Remove ${staff.name} from staff?`)) return;
    try {
      await apiRequest(`/admin/staff/${staff.id}`, { method: "DELETE", token: user?.token });
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't delete staff.");
    }
  }

  return (
    <div className="grid gap-6 md:grid-cols-3">
      <form onSubmit={handleCreate} className="h-fit rounded-lg border border-ink/10 bg-white p-5">
        <p className="font-medium">New staff member</p>
        <div className="mt-3 space-y-3">
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Full name" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="Phone" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Temporary password (8+ chars)" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <input type="text" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} placeholder="Employee ID" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} placeholder="Department" className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy" />
          <button type="submit" disabled={creating} className="w-full rounded-md bg-navy px-4 py-2 text-sm font-medium text-paper hover:bg-navy-light disabled:opacity-50">
            {creating ? "Adding…" : "Add staff member"}
          </button>
        </div>
      </form>

      <div className="md:col-span-2">
        {error && (
          <p className="mb-4 rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
            {error}
          </p>
        )}

        {loading ? (
          <p className="text-ink/50">Loading…</p>
        ) : staffList.length === 0 ? (
          <p className="text-ink/40">No staff yet — add one on the left.</p>
        ) : (
          <ul className="divide-y divide-ink/10 rounded-lg border border-ink/10 bg-white">
            {staffList.map((s) => (
              <li key={s.id} className="flex items-center justify-between px-5 py-4">
                <div>
                  <p className="font-medium">{s.name}</p>
                  <p className="text-sm text-ink/60">
                    {s.email} · {s.department || "No department"} · #{s.employeeId}
                  </p>
                </div>
                <button
                  onClick={() => handleDelete(s)}
                  className="rounded-md border border-cancelled/30 px-3 py-1.5 text-sm text-cancelled hover:bg-cancelled/5"
                >
                  Remove
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
