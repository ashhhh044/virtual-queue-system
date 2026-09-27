import type { CustomerStatus } from "../types";

const STYLES: Record<CustomerStatus, string> = {
  waiting: "bg-amber/15 text-amber-dark border-amber/40",
  called: "bg-navy/10 text-navy border-navy/30",
  served: "bg-served/10 text-served border-served/30",
  "no-show": "bg-cancelled/10 text-cancelled border-cancelled/30",
};

const LABELS: Record<CustomerStatus, string> = {
  waiting: "Waiting",
  called: "Called",
  served: "Served",
  "no-show": "Cancelled",
};

export function StatusBadge({ status }: { status: CustomerStatus }) {
  return (
    <span className={`inline-flex items-center rounded-full border px-3 py-1 text-sm font-medium ${STYLES[status]}`}>
      {LABELS[status]}
    </span>
  );
}
