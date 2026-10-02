import { useMemo, useState } from "react";
import { emi, inr, inrCompact } from "../lib/money";
import { h2, kicker } from "../ui";

export function EmiPlanner() {
  const [amount, setAmount] = useState(8_500_000);
  const [rate, setRate] = useState(7.6);
  const [years, setYears] = useState(20);

  const monthly = useMemo(() => emi(amount, rate, years), [amount, rate, years]);
  const total = monthly * years * 12;
  const interest = total - amount;

  return (
    <section className="mx-auto grid w-full max-w-6xl items-center gap-8 px-5 py-14 lg:grid-cols-[0.8fr_1.2fr]" id="planner">
      <div>
        <p className={kicker}>Repayment sketch</p>
        <h2 className={h2}>What the EMI looks like before a bank says yes.</h2>
        <p className="text-emerald-900/80">
          This is arithmetic, not an offer. NRI tenures are often capped by retirement age, and the actual rate moves with the country on your visa.
        </p>
      </div>
      <form className="grid gap-4 rounded-2xl border border-emerald-100 bg-white p-6 shadow-sm" onSubmit={(event) => event.preventDefault()}>
        <label className="grid gap-1 text-sm font-medium">
          Loan amount
          <strong className="text-2xl font-semibold text-emerald-950">{inrCompact(amount)}</strong>
          <input className="accent-emerald-700" type="range" min={500000} max={50000000} step={100000} value={amount} onChange={(event) => setAmount(Number(event.target.value))} />
        </label>
        <label className="grid gap-1 text-sm font-medium">
          Interest
          <strong className="text-2xl font-semibold text-emerald-950">{rate.toFixed(2)}% a year</strong>
          <input className="accent-emerald-700" type="range" min={7} max={14} step={0.05} value={rate} onChange={(event) => setRate(Number(event.target.value))} />
        </label>
        <label className="grid gap-1 text-sm font-medium">
          Tenure
          <strong className="text-2xl font-semibold text-emerald-950">{years} years</strong>
          <input className="accent-emerald-700" type="range" min={1} max={30} step={1} value={years} onChange={(event) => setYears(Number(event.target.value))} />
        </label>
        <dl className="grid gap-3 sm:grid-cols-3">
          {[
            ["Monthly EMI", inr(monthly)],
            ["Interest", inr(interest)],
            ["Total paid", inr(total)],
          ].map(([label, value]) => (
            <div key={label} className="rounded-xl bg-emerald-800 p-3 text-emerald-50">
              <dt className="text-xs uppercase tracking-wide text-emerald-100">{label}</dt>
              <dd className="mt-1 text-lg font-semibold">{value}</dd>
            </div>
          ))}
        </dl>
      </form>
    </section>
  );
}
