import { FormEvent, useEffect, useState } from "react";
import { apiRequest, ApiError } from "../lib/api";
import { useAuth } from "../lib/auth";
import type { ServiceItem } from "../types";

export function AdminServices() {
  const { user } = useAuth();
  const [services, setServices] = useState<ServiceItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [duration, setDuration] = useState(10);
  const [creating, setCreating] = useState(false);

  async function load() {
    setLoading(true);
    try {
      const data = await apiRequest<{ services: ServiceItem[] }>("/admin/services", { token: user?.token });
      setServices(data.services);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't load services.");
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
    if (!name.trim()) return;

    setCreating(true);
    setError(null);
    try {
      // Note: description is required by the backend for creation
      await apiRequest("/admin/services", {
        method: "POST",
        token: user?.token,
        body: { name, description, estimatedDuration: duration },
      });
      setName("");
      setDescription("");
      setDuration(10);
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't create service.");
    } finally {
      setCreating(false);
    }
  }

  async function toggleActive(service: ServiceItem) {
    try {
      // partial update - only send what's changing
      await apiRequest(`/admin/services/${service.id}`, {
        method: "PUT",
        token: user?.token,
        body: { isActive: !service.isActive },
      });
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't update service.");
    }
  }

  async function handleDelete(service: ServiceItem) {
    if (!confirm(`Delete "${service.name}"? This can't be undone.`)) return;
    try {
      await apiRequest(`/admin/services/${service.id}`, { method: "DELETE", token: user?.token });
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't delete service.");
    }
  }

  return (
    <div className="grid gap-6 md:grid-cols-3">
      <form onSubmit={handleCreate} className="h-fit rounded-lg border border-ink/10 bg-white p-5">
        <p className="font-medium">New service</p>
        <div className="mt-3 space-y-3">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Service name"
            className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy"
          />
          <input
            type="text"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Description"
            className="w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy"
          />
          <div>
            <label className="block text-xs text-ink/60">Estimated duration (minutes)</label>
            <input
              type="number"
              min={1}
              value={duration}
              onChange={(e) => setDuration(Number(e.target.value))}
              className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 text-sm focus:border-navy"
            />
          </div>
          <button
            type="submit"
            disabled={creating}
            className="w-full rounded-md bg-navy px-4 py-2 text-sm font-medium text-paper hover:bg-navy-light disabled:opacity-50"
          >
            {creating ? "Adding…" : "Add service"}
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
        ) : services.length === 0 ? (
          <p className="text-ink/40">No services yet — add one on the left.</p>
        ) : (
          <ul className="divide-y divide-ink/10 rounded-lg border border-ink/10 bg-white">
            {services.map((s) => (
              <li key={s.id} className="flex items-center justify-between px-5 py-4">
                <div>
                  <p className="font-medium">{s.name}</p>
                  <p className="text-sm text-ink/60">
                    {s.description || "No description"} · ~{s.estimatedDuration} min
                  </p>
                </div>
                <div className="flex items-center gap-3">
                  <span
                    className={`rounded-full px-2 py-0.5 text-xs font-medium ${
                      s.isActive ? "bg-served/10 text-served" : "bg-ink/5 text-ink/50"
                    }`}
                  >
                    {s.isActive ? "Active" : "Inactive"}
                  </span>
                  <button
                    onClick={() => toggleActive(s)}
                    className="rounded-md border border-ink/15 px-3 py-1.5 text-sm hover:bg-ink/5"
                  >
                    {s.isActive ? "Deactivate" : "Activate"}
                  </button>
                  <button
                    onClick={() => handleDelete(s)}
                    className="rounded-md border border-cancelled/30 px-3 py-1.5 text-sm text-cancelled hover:bg-cancelled/5"
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
