import type { Customer } from "../types";
import { StatusBadge } from "./StatusBadge";

export function TokenTicket({ customer }: { customer: Customer }) {
  return (
    <div className="overflow-hidden rounded-lg border border-ink/10 bg-white shadow-sm">
      <div className="bg-navy px-6 py-5 text-paper">
        <p className="text-sm uppercase tracking-wide text-paper/70">Your token</p>
        <p className="mt-1 font-mono text-5xl font-bold tracking-tight">{customer.tokenNumber}</p>
      </div>

      <div className="grid grid-cols-2 divide-x divide-ink/10 border-b border-ink/10">
        <div className="px-6 py-4">
          <p className="text-sm text-ink/60">Position in line</p>
          <p className="font-mono text-2xl font-semibold">{customer.position ?? "—"}</p>
        </div>
        <div className="px-6 py-4">
          <p className="text-sm text-ink/60">Estimated wait</p>
          <p className="font-mono text-2xl font-semibold">
            {customer.eta != null ? `${customer.eta} min` : "—"}
          </p>
        </div>
      </div>

      <div className="flex items-center justify-between px-6 py-4">
        <div>
          <p className="text-sm text-ink/60">Name</p>
          <p className="font-medium">{customer.name}</p>
        </div>
        <StatusBadge status={customer.status} />
      </div>
    </div>
  );
}
