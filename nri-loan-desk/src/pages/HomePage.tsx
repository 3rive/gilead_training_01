import { Link } from "react-router-dom";
import { EmiPlanner } from "../components/EmiPlanner";
import { countries, faqs, lenders, loanTypes, steps } from "../data/catalog";
import { inr } from "../lib/money";
import { loanName, loadApplications, statusLabel } from "../lib/store";

export function HomePage() {
  const sample = loadApplications()[0];

  return (
    <>
      <section className="hero">
        <div>
          <p className="kicker">For Indians outside India</p>
          <h1>Buy the house in India. Stay where you are.</h1>
          <p className="lede">
            Homeward is a loan desk for NRIs. One request covers the lenders that already read a Dubai salary, a US W-2, or a Singapore Employment Pass. You track the file from the first call to disbursement.
          </p>
          <div className="hero-actions">
            <Link className="button" to="/apply">
              Raise a request
            </Link>
            <Link className="button ghost" to="/track/HW-24018">
              See a live file
            </Link>
          </div>
          <ul className="hero-facts">
            <li>Home loans from 7.15% in this demo</li>
            <li>Gulf, US, UK, Singapore, and more</li>
            <li>POA so you do not fly for registration</li>
          </ul>
        </div>
        {sample && (
          <Link className="file-card" to={`/track/${sample.reference}`}>
            <p className="kicker">Sample file</p>
            <strong>{sample.reference}</strong>
            <p>
              {sample.fullName} · {sample.cityAbroad}
            </p>
            <p>
              {loanName(sample.loanType)} · {inr(sample.amountInr)} · {sample.cityInIndia}
            </p>
            <span className={`pill pill-${sample.status}`}>{statusLabel(sample.status)}</span>
          </Link>
        )}
      </section>

      <section className="band">
        <p>Where the income is earned</p>
        <ul>
          {countries.map((country) => (
            <li key={country.name}>{country.name}</li>
          ))}
        </ul>
      </section>

      <section className="section">
        <div className="section-head">
          <p className="kicker">Loan types</p>
          <h2>The products NRIs actually ask for.</h2>
        </div>
        <div className="cards">
          {loanTypes.map((loan) => (
            <article key={loan.id} className="card">
              <p className="from">from {loan.from.toFixed(2)}%</p>
              <h3>{loan.name}</h3>
              <p>{loan.blurb}</p>
              <p className="note">{loan.nri}</p>
              <Link to={`/apply?type=${loan.id}`}>Start with this</Link>
            </article>
          ))}
        </div>
      </section>

      <section className="section split">
        <div>
          <p className="kicker">How a file moves</p>
          <h2>You submit once. The desk talks to the banks.</h2>
        </div>
        <ol className="steps">
          {steps.map((step) => (
            <li key={step.n}>
              <span>{step.n}</span>
              <div>
                <h3>{step.title}</h3>
                <p>{step.body}</p>
              </div>
            </li>
          ))}
        </ol>
      </section>

      <section className="section">
        <div className="section-head">
          <p className="kicker">Illustrative desk</p>
          <h2>Lenders in this mock book.</h2>
          <p>Starting rates for a well-paid salaried NRI home loan. Your number will differ.</p>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Lender</th>
                <th>Home</th>
                <th>Why it shows up</th>
              </tr>
            </thead>
            <tbody>
              {lenders.map((lender) => (
                <tr key={lender.id}>
                  <td>
                    <strong>{lender.name}</strong>
                    <span>{lender.highlight}</span>
                  </td>
                  <td>{lender.rates.home ? `${lender.rates.home.toFixed(2)}%` : "—"}</td>
                  <td>{lender.note}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      <EmiPlanner />

      <section className="section">
        <div className="section-head">
          <p className="kicker">Questions</p>
          <h2>What changes because you do not live in India.</h2>
        </div>
        <div className="faqs">
          {faqs.map((faq) => (
            <details key={faq.q}>
              <summary>{faq.q}</summary>
              <p>{faq.a}</p>
            </details>
          ))}
        </div>
      </section>
    </>
  );
}
