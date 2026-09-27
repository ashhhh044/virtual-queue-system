import { useEffect, useState } from "react";
import { apiRequest, ApiError } from "../lib/api";
import { useAuth } from "../lib/auth";
import { useQueueSocket } from "../lib/useQueueSocket";
import { StatusBadge } from "./StatusBadge";
import type { Customer, QueueUpdateMessage, ServiceItem } from "../types";

const StaffDashboard = () => {
  const { user } = useAuth();

  const [services, setServices] = useState<ServiceItem[]>([]);
  const [serviceType, setServiceType] = useState<string | null>(null);
  const [queue, setQueue] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [calledCustomer, setCalledCustomer] = useState<Customer | null>(null);
  const [actionLoading, setActionLoading] = useState(false);

  useEffect(() => {
    apiRequest<{ services: ServiceItem[] }>("/customer/services")
      .then((data) => {
        setServices(data.services);
        if (data.services.length > 0) setServiceType(data.services[0].name);
      })
      .catch((err: ApiError) => setError(err.message));
  }, []);

  async function loadQueue(type: string) {
    setLoading(true);
    setError(null);
    try {
      const data = await apiRequest<{ customers: Customer[] }>(
        `/staff/queue/${type}`,
        {
          token: user?.token,
        },
      );
      setQueue(data.customers);
    } catch (err) {
      setError(
        err instanceof ApiError ? err.message : "Couldn't load the queue.",
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (serviceType) loadQueue(serviceType);
    setCalledCustomer(null);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [serviceType]);

  useQueueSocket<QueueUpdateMessage>(
    serviceType ? `/topic/queue/${serviceType}` : null,
    (update) => setQueue(update.customers),
  );

  async function handleCallNext() {
    if (!serviceType) return;
    setActionLoading(true);
    setError(null);
    try {
      const data = await apiRequest<{ customer: Customer }>(
        `/staff/call-next/${serviceType}`,
        {
          method: "POST",
          token: user?.token,
        },
      );
      setCalledCustomer(data.customer);
      if (serviceType) loadQueue(serviceType);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "No one left to call.");
    } finally {
      setActionLoading(false);
    }
  }

  async function handleComplete() {
    if (!calledCustomer) return;
    setActionLoading(true);
    setError(null);
    try {
      await apiRequest(`/staff/complete/${calledCustomer.id}`, {
        method: "PUT",
        token: user?.token,
      });
      setCalledCustomer(null);
      if (serviceType) loadQueue(serviceType);
    } catch (err) {
      setError(
        err instanceof ApiError ? err.message : "Couldn't complete service.",
      );
    } finally {
      setActionLoading(false);
    }
  }

  return (
    <div>
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Staff dashboard</h1>

        {services.length > 0 && (
          <select
            value={serviceType ?? ""}
            onChange={(e) => setServiceType(e.target.value)}
            className="rounded-md border border-ink/15 bg-white px-3 py-2 text-sm focus:border-navy"
          >
            {services.map((s) => (
              <option key={s.id} value={s.name}>
                {s.name}
              </option>
            ))}
          </select>
        )}
      </div>

      {error && (
        <p className="mt-4 rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
          {error}
        </p>
      )}

      <div className="mt-6 grid gap-6 md:grid-cols-3">
        <div className="rounded-lg border border-ink/10 bg-white p-5">
          <p className="text-sm text-ink/60">Now calling</p>
          {calledCustomer ? (
            <>
              <p className="mt-1 font-mono text-3xl font-bold text-navy">
                {calledCustomer.tokenNumber}
              </p>
              <p className="mt-1 text-ink/70">{calledCustomer.name}</p>
              <button
                onClick={handleComplete}
                disabled={actionLoading}
                className="mt-4 w-full rounded-md bg-served px-4 py-2 font-medium text-white hover:opacity-90 disabled:opacity-50"
              >
                {actionLoading ? "…" : "Mark completed"}
              </button>
            </>
          ) : (
            <p className="mt-1 text-ink/40">No one currently being served</p>
          )}

          <button
            onClick={handleCallNext}
            disabled={actionLoading || !serviceType || !!calledCustomer}
            className="mt-3 w-full rounded-md bg-navy px-4 py-2 font-medium text-paper hover:bg-navy-light disabled:opacity-50"
          >
            Call next
          </button>
        </div>

        <div className="rounded-lg border border-ink/10 bg-white p-5 md:col-span-2">
          <p className="text-sm text-ink/60">
            Waiting ({queue.length}) {loading && "· loading…"}
          </p>

          {queue.length === 0 && !loading ? (
            <p className="mt-6 text-center text-ink/40">Queue is empty.</p>
          ) : (
            <ul className="mt-3 divide-y divide-ink/10">
              {queue.map((c, idx) => (
                <li
                  key={c.id}
                  className="flex items-center justify-between py-3"
                >
                  <div className="flex items-center gap-3">
                    <span className="font-mono text-sm text-ink/40">
                      #{idx + 1}
                    </span>
                    <div>
                      <p className="font-medium">{c.name}</p>
                      <p className="font-mono text-sm text-ink/50">
                        {c.tokenNumber}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    {c.priority !== "normal" && (
                      <span className="rounded-full bg-amber/15 px-2 py-0.5 text-xs font-medium text-amber-dark">
                        {c.priority}
                      </span>
                    )}
                    <StatusBadge status={c.status} />
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
};

export default StaffDashboard;
