import { useMemo, useState } from "react";
import { emi, inr, inrCompact } from "../lib/money";

export function EmiPlanner() {
  const [amount, setAmount] = useState(8_500_000);
  const [rate, setRate] = useState(7.6);
  const [years, setYears] = useState(20);

  const monthly = useMemo(() => emi(amount, rate, years), [amount, rate, years]);
  const total = monthly * years * 12;
  const interest = total - amount;

  return (
    <section className="planner" id="planner">
      <div className="planner-copy">
        <p className="kicker">Repayment sketch</p>
        <h2>What the EMI looks like before a bank says yes.</h2>
        <p>
          This is arithmetic, not an offer. NRI tenures are often capped by retirement age, and the actual rate moves with the country on your visa.
        </p>
      </div>
      <form className="planner-card" onSubmit={(event) => event.preventDefault()}>
        <label>
          Loan amount
          <strong>{inrCompact(amount)}</strong>
          <input
            type="range"
            min={500000}
            max={50000000}
            step={100000}
            value={amount}
            onChange={(event) => setAmount(Number(event.target.value))}
          />
        </label>
        <label>
          Interest
          <strong>{rate.toFixed(2)}% a year</strong>
          <input
            type="range"
            min={7}
            max={14}
            step={0.05}
            value={rate}
            onChange={(event) => setRate(Number(event.target.value))}
          />
        </label>
        <label>
          Tenure
          <strong>{years} years</strong>
          <input
            type="range"
            min={1}
            max={30}
            step={1}
            value={years}
            onChange={(event) => setYears(Number(event.target.value))}
          />
        </label>
        <dl className="planner-out">
          <div>
            <dt>Monthly EMI</dt>
            <dd>{inr(monthly)}</dd>
          </div>
          <div>
            <dt>Interest</dt>
            <dd>{inr(interest)}</dd>
          </div>
          <div>
            <dt>Total paid</dt>
            <dd>{inr(total)}</dd>
          </div>
        </dl>
      </form>
    </section>
  );
}
