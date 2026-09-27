import { Link } from "react-router-dom";

const Landing = () => {
  return (
    <div className="grid gap-10 md:grid-cols-2 md:items-center">
      <div>
        <p className="font-mono text-sm uppercase tracking-wide text-amber-dark">
          No more standing in line
        </p>
        <h1 className="mt-3 text-4xl font-bold leading-tight text-ink md:text-5xl">
          Take a ticket, watch your position move.
        </h1>
        <p className="mt-4 max-w-md text-ink/70">
          Join a queue from your phone, see your live position and estimated wait time, and get
          called when it's your turn.
        </p>

        <div className="mt-8 flex flex-wrap gap-3">
          <Link to="/join" className="rounded-md bg-navy px-5 py-3 font-medium text-paper hover:bg-navy-light">
            Join a queue
          </Link>
          <Link to="/status" className="rounded-md border border-ink/15 bg-white px-5 py-3 font-medium hover:bg-ink/5">
            Check my status
          </Link>
        </div>
      </div>

      <div className="rounded-lg border border-ink/10 bg-white p-6 shadow-sm">
        <p className="text-sm text-ink/60">Preview</p>
        <div className="mt-3 rounded-md bg-navy px-5 py-4 text-paper">
          <p className="text-xs uppercase tracking-wide text-paper/70">Your token</p>
          <p className="mt-1 font-mono text-4xl font-bold">T014</p>
        </div>
        <div className="mt-4 flex justify-between text-sm">
          <div>
            <p className="text-ink/60">Position</p>
            <p className="font-mono text-xl font-semibold">3</p>
          </div>
          <div>
            <p className="text-ink/60">Est. wait</p>
            <p className="font-mono text-xl font-semibold">12 min</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Landing;
