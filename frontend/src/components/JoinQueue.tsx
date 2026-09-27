import { FormEvent, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { apiRequest, ApiError } from "../lib/api";
import type { Customer, Priority, ServiceItem } from "../types";
import { TokenTicket } from "./TokenTicket";

const JoinQueue = () => {
  const [services, setServices] = useState<ServiceItem[]>([]);
  const [servicesError, setServicesError] = useState<string | null>(null);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [serviceType, setServiceType] = useState("");
  const [priority, setPriority] = useState<Priority>("normal");

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [joined, setJoined] = useState<Customer | null>(null);

  useEffect(() => {
    apiRequest<{ services: ServiceItem[] }>("/customer/services")
      .then((data) => {
        setServices(data.services);
        if (data.services.length > 0) setServiceType(data.services[0].name);
      })
      .catch((err: ApiError) => setServicesError(err.message));
  }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);

    if (!name.trim() || !email.trim() || !phone.trim() || !serviceType) {
      setError("Please fill in every field.");
      return;
    }

    setSubmitting(true);
    try {
      const data = await apiRequest<{ customer: Customer }>("/customer/join", {
        method: "POST",
        body: { name, email, phone, serviceType, priority },
      });
      setJoined(data.customer);
    } catch (err) {
      setError(
        err instanceof ApiError ? err.message : "Couldn't join the queue.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  if (joined) {
    return (
      <div className="mx-auto max-w-md">
        <h1 className="text-2xl font-bold">You're in line</h1>
        <p className="mt-1 text-ink/70">
          Save your access key — you'll need it to check your status later.
        </p>

        <div className="mt-6">
          <TokenTicket customer={joined} />
        </div>

        <div className="mt-4 rounded-md border border-amber/40 bg-amber/10 px-4 py-3 text-sm">
          Access key:{" "}
          <span className="font-mono font-semibold">{joined.accessKey}</span>
        </div>

        <Link
          to={`/status?key=${joined.accessKey}`}
          className="mt-6 inline-block rounded-md bg-navy px-5 py-3 font-medium text-paper hover:bg-navy-light"
        >
          Track my status
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-md">
      <h1 className="text-2xl font-bold">Join a queue</h1>
      <p className="mt-1 text-ink/70">
        Enter your details and pick a service to get a ticket.
      </p>

      {servicesError && (
        <p className="mt-4 rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
          Couldn't load services: {servicesError}
        </p>
      )}

      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <div>
          <label className="block text-sm font-medium">Full name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 focus:border-navy"
            placeholder="Your Name"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 focus:border-navy"
            placeholder="user@example.com"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Phone</label>
          <input
            type="tel"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 px-3 py-2 focus:border-navy"
            placeholder="555-0100"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Service</label>
          <select
            value={serviceType}
            onChange={(e) => setServiceType(e.target.value)}
            className="mt-1 w-full rounded-md border border-ink/15 bg-white px-3 py-2 focus:border-navy"
          >
            {services.length === 0 && (
              <option value="">No services available</option>
            )}
            {services.map((s) => (
              <option key={s.id} value={s.name}>
                {s.name} (~{s.estimatedDuration} min)
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium">Priority</label>
          <select
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
            className="mt-1 w-full rounded-md border border-ink/15 bg-white px-3 py-2 focus:border-navy"
          >
            <option value="normal">Normal</option>
            <option value="high">High (elderly / disability)</option>
            <option value="emergency">Emergency</option>
          </select>
        </div>

        {error && (
          <p className="rounded-md border border-cancelled/30 bg-cancelled/10 px-4 py-3 text-sm text-cancelled">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={submitting || services.length === 0}
          className="w-full rounded-md bg-navy px-5 py-3 font-medium text-paper hover:bg-navy-light disabled:opacity-50"
        >
          {submitting ? "Joining…" : "Get my ticket"}
        </button>
      </form>
    </div>
  );
};

export default JoinQueue;
