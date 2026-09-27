import { FormEvent, useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { apiRequest, ApiError } from "../lib/api";
import type { Customer, CustomerUpdateMessage } from "../types";
import { TokenTicket } from "./TokenTicket";
import { useQueueSocket } from "../lib/useQueueSocket";

const CheckStatus = () => {
  const [searchParams] = useSearchParams();
  const [accessKey, setAccessKey] = useState(searchParams.get("key") ?? "");
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [cancelling, setCancelling] = useState(false);

  async function lookup(key: string) {
    setError(null);
    setLoading(true);
    try {
      const data = await apiRequest<{ customer: Customer }>(`/customer/status/${key}`);
      setCustomer(data.customer);
    } catch (err) {
      setCustomer(null);
      setError(err instanceof ApiError ? err.message : "Couldn't find that ticket.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    const key = searchParams.get("key");
    if (key) lookup(key);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useQueueSocket<CustomerUpdateMessage>(
    customer ? `/topic/customer/${customer.accessKey}` : null,
    (update) => {
      setCustomer((prev) =>
        prev ? { ...prev, position: update.position, eta: update.eta, status: update.status } : prev
      );
    }
  );

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (accessKey.trim()) lookup(accessKey.trim());
  }

  async function handleCancel() {
    if (!customer) return;
    if (!confirm("Cancel your spot in the queue?")) return;

    setCancelling(true);
    try {
      await apiRequest(`/customer/cancel/${customer.accessKey}`, { method: "DELETE" });
      setCustomer({ ...customer, status: "no-show", position: null, eta: null });
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Couldn't cancel.");
    } finally {
      setCancelling(false);
    }
  }

  return (
    <div className="mx-auto max-w-md">
      <h1 className="text-2xl font-bold">Check your status</h1>
      <p className="mt-1 text-ink/70">Enter the access key you received when you joined the queue.</p>

      <form onSubmit={handleSubmit} className="mt-6 flex gap-2">
        <input
          type="text"
          value={accessKey}
          onChange={(e) => setAccessKey(e.target.value.toUpperCase())}
          placeholder="ABC123XY"
          className="flex-1 rounded-md border border-ink/15 px-3 py-2 font-mono focus:border-navy"
        />
        <button
          type="submit"
          disabled={loading}
          className="rounded-md bg-navy px-5 py-2 font-medium text-paper hover:bg-navy-light disabled:opacity-50"
        >
          {loading ? "Looking…" : "Look up"}
        </button>
      </form>

      {error && (
        <p className="mt-4 rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
          {error}
        </p>
      )}

      {customer && (
        <div className="mt-6">
          <TokenTicket customer={customer} />

          {customer.status === "waiting" && (
            <button
              onClick={handleCancel}
              disabled={cancelling}
              className="mt-4 w-full rounded-md border border-cancelled/40 px-5 py-3 font-medium text-cancelled hover:bg-cancelled/5 disabled:opacity-50"
            >
              {cancelling ? "Cancelling…" : "Cancel my spot"}
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default CheckStatus;
