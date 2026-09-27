import { useEffect, useState } from "react";
import { apiRequest, ApiError } from "../lib/api";
import { useAuth } from "../lib/auth";
import type { Analytics } from "../types";

export function AdminAnalytics() {
  const { user } = useAuth();
  const [data, setData] = useState<Analytics | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiRequest<{ analytics: Analytics }>("/admin/stats", { token: user?.token })
      .then((res) => setData(res.analytics))
      .catch((err: ApiError) => setError(err.message))
      .finally(() => setLoading(false));
  }, [user?.token]);

  if (loading) return <p className="text-ink/50">Loading…</p>;
  if (error) {
    return (
      <p className="rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
        {error}
      </p>
    );
  }
  if (!data) return null;

  const stats = [
    { label: "Total customers", value: data.totalCustomers },
    { label: "Currently waiting", value: data.waitingCustomers },
    { label: "Served today", value: data.servedToday },
    { label: "Avg. wait (min)", value: Math.round(data.averageWaitTime) },
  ];

  const distributionEntries = Object.entries(data.serviceDistribution);
  const maxCount = Math.max(1, ...distributionEntries.map(([, count]) => count));

  return (
    <div>
      <div className="grid grid-cols-2 gap-4 md:grid-cols-4">
        {stats.map((s) => (
          <div key={s.label} className="rounded-lg border border-ink/10 bg-white p-5">
            <p className="text-sm text-ink/60">{s.label}</p>
            <p className="mt-1 font-mono text-3xl font-bold text-navy">{s.value}</p>
          </div>
        ))}
      </div>

      <div className="mt-6 rounded-lg border border-ink/10 bg-white p-5">
        <p className="text-sm font-medium text-ink/80">Completed by service</p>
        {distributionEntries.length === 0 ? (
          <p className="mt-3 text-ink/40">No completed services yet.</p>
        ) : (
          <ul className="mt-4 space-y-3">
            {distributionEntries.map(([name, count]) => (
              <li key={name}>
                <div className="flex justify-between text-sm">
                  <span>{name}</span>
                  <span className="font-mono text-ink/60">{count}</span>
                </div>
                <div className="mt-1 h-2 rounded-full bg-ink/5">
                  <div className="h-2 rounded-full bg-amber" style={{ width: `${(count / maxCount) * 100}%` }} />
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
