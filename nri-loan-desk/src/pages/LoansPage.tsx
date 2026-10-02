import { Link } from "react-router-dom";
import { lenders, loanTypes } from "../data/catalog";

export function LoansPage() {
  return (
    <section className="page">
      <p className="kicker">Compare</p>
      <h1>Six products. One file.</h1>
      <p className="lede narrow">
        Pick the job the money has to do. On the next screen you add the country you live in, and the desk only keeps lenders that take that income.
      </p>
      <div className="cards">
        {loanTypes.map((loan) => {
          const names = lenders
            .filter((lender) => lender.rates[loan.id] != null)
            .map((lender) => lender.name);
          return (
            <article key={loan.id} className="card">
              <p className="from">from {loan.from.toFixed(2)}%</p>
              <h2>{loan.name}</h2>
              <p>{loan.blurb}</p>
              <p className="note">{loan.nri}</p>
              <p className="lenders">{names.join(" · ")}</p>
              <Link className="button" to={`/apply?type=${loan.id}`}>
                Apply
              </Link>
            </article>
          );
        })}
      </div>
    </section>
  );
}
