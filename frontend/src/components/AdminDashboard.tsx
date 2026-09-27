import { useState } from "react";
import { AdminAnalytics } from "./AdminAnalytics";
import { AdminServices } from "./AdminServices";
import { AdminStaff } from "./AdminStaff";

type Tab = "analytics" | "services" | "staff";

const TABS: { id: Tab; label: string }[] = [
  { id: "analytics", label: "Analytics" },
  { id: "services", label: "Services" },
  { id: "staff", label: "Staff" },
];

const AdminDashboard = () => {
  const [tab, setTab] = useState<Tab>("analytics");

  return (
    <div>
      <h1 className="text-2xl font-bold">Admin dashboard</h1>

      <div className="mt-6 flex gap-1 border-b border-ink/10">
        {TABS.map((t) => (
          <button
            key={t.id}
            onClick={() => setTab(t.id)}
            className={`px-4 py-2 text-sm font-medium ${
              tab === t.id ? "border-b-2 border-navy text-navy" : "text-ink/50 hover:text-ink"
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      <div className="mt-6">
        {tab === "analytics" && <AdminAnalytics />}
        {tab === "services" && <AdminServices />}
        {tab === "staff" && <AdminStaff />}
      </div>
    </div>
  );
};

export default AdminDashboard;
