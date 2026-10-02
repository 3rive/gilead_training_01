import { Link } from "react-router-dom";
import { lenders, loanTypes } from "../data/catalog";
import { btn, card, h1, kicker, lede, page } from "../ui";

export function LoansPage() {
  return (
    <section className={page}>
      <p className={kicker}>Compare</p>
      <h1 className={h1}>Six products. One file.</h1>
      <p className={lede}>
        Pick the job the money has to do. On the next screen you add the country you live in, and the desk only keeps lenders that take that income.
      </p>
      <div className="mt-8 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {loanTypes.map((loan) => {
          const names = lenders.filter((lender) => lender.rates[loan.id] != null).map((lender) => lender.name);
          return (
            <article key={loan.id} className={card}>
              <p className="text-xs font-semibold uppercase tracking-wide text-emerald-700">from {loan.from.toFixed(2)}%</p>
              <h2 className="text-2xl font-semibold">{loan.name}</h2>
              <p className="text-emerald-900/80">{loan.blurb}</p>
              <p className="text-sm text-emerald-800/70">{loan.nri}</p>
              <p className="text-sm text-emerald-800">{names.join(" · ")}</p>
              <Link className={`${btn} mt-auto w-fit`} to={`/apply?type=${loan.id}`}>Apply</Link>
            </article>
          );
        })}
      </div>
    </section>
  );
}
