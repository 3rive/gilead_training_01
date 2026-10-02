import { Link } from "react-router-dom";
import { EmiPlanner } from "../components/EmiPlanner";
import { countries, faqs, lenders, loanTypes, steps } from "../data/catalog";
import { inr } from "../lib/money";
import { loanName, loadApplications, statusLabel } from "../lib/store";
import { btn, btnGhost, card, h2, kicker, pill } from "../ui";

export function HomePage() {
  const sample = loadApplications()[0];

  return (
    <>
      <section className="mx-auto grid w-full max-w-6xl items-center gap-10 px-5 py-12 lg:grid-cols-[1.2fr_0.9fr]">
        <div>
          <p className={kicker}>For Indians outside India</p>
          <h1 className="mt-3 text-5xl font-semibold leading-none tracking-tight text-emerald-950 sm:text-6xl">
            Buy the house in India. Stay where you are.
          </h1>
          <p className="mt-5 max-w-xl text-lg leading-relaxed text-emerald-900/80">
            Homeward is a loan desk for NRIs. One request covers the lenders that already read a Dubai salary, a US W-2, or a Singapore Employment Pass. You track the file from the first call to disbursement.
          </p>
          <div className="mt-6 flex flex-wrap gap-3">
            <Link className={btn} to="/apply">Raise a request</Link>
            <Link className={btnGhost} to="/track/HW-24018">See a live file</Link>
          </div>
          <ul className="mt-6 flex flex-wrap gap-2 text-sm text-emerald-900">
            {["Home loans from 7.15% in this demo", "Gulf, US, UK, Singapore, and more", "POA so you do not fly for registration"].map((item) => (
              <li key={item} className="rounded-full border border-emerald-200 bg-white px-3 py-1.5">{item}</li>
            ))}
          </ul>
        </div>
        <div className="rounded-3xl bg-gradient-to-br from-emerald-800 to-emerald-950 p-5 text-emerald-50 shadow-xl">
          <div className="mb-4 flex items-center gap-3 text-lg font-medium text-emerald-100" aria-hidden="true">
            <span>Dubai</span>
            <span className="h-px flex-1 bg-emerald-300/70" />
            <span>Hyderabad</span>
          </div>
          {sample && (
            <Link className="block rounded-2xl bg-white p-5 text-emerald-950 shadow-sm" to={`/track/${sample.reference}`}>
              <p className={kicker}>Sample file on the desk</p>
              <strong className="mt-2 block text-3xl font-semibold tracking-tight">{sample.reference}</strong>
              <p className="mt-2 text-emerald-800">{sample.fullName} · {sample.cityAbroad}</p>
              <p className="text-emerald-800">{loanName(sample.loanType)} · {inr(sample.amountInr)} · {sample.cityInIndia}</p>
              <span className={`mt-4 ${pill(sample.status)}`}>{statusLabel(sample.status)}</span>
            </Link>
          )}
        </div>
      </section>

      <section className="mx-auto grid w-full max-w-6xl gap-3 px-5 sm:grid-cols-2 lg:grid-cols-4">
        {[
          ["8", "lenders in the mock book"],
          ["9", "countries of residence"],
          ["24h", "advisor callback in the story"],
          ["1", "file, not eight bank portals"],
        ].map(([value, label]) => (
          <div key={label} className="rounded-2xl border border-emerald-100 bg-white px-4 py-3">
            <strong className="block text-2xl font-semibold text-emerald-800">{value}</strong>
            <span className="text-sm text-emerald-900/70">{label}</span>
          </div>
        ))}
      </section>

      <section className="mx-auto flex w-full max-w-6xl flex-col gap-3 px-5 py-8 sm:flex-row sm:items-center">
        <p className="min-w-40 text-sm text-emerald-800">Where the income is earned</p>
        <ul className="flex flex-wrap gap-2">
          {countries.map((country) => (
            <li key={country.name} className="rounded-full border border-emerald-200 bg-white px-3 py-1 text-sm">{country.name}</li>
          ))}
        </ul>
      </section>

      <section className="mx-auto w-full max-w-6xl px-5 py-10">
        <p className={kicker}>Loan types</p>
        <h2 className={h2}>The products NRIs actually ask for.</h2>
        <div className="mt-6 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {loanTypes.map((loan) => (
            <article key={loan.id} className={card}>
              <p className="text-xs font-semibold uppercase tracking-wide text-emerald-700">from {loan.from.toFixed(2)}%</p>
              <h3 className="text-xl font-semibold">{loan.name}</h3>
              <p className="text-emerald-900/80">{loan.blurb}</p>
              <p className="text-sm text-emerald-800/70">{loan.nri}</p>
              <Link className="mt-auto font-medium text-emerald-800 underline-offset-4 hover:underline" to={`/apply?type=${loan.id}`}>Start with this</Link>
            </article>
          ))}
        </div>
      </section>

      <section className="mx-auto grid w-full max-w-6xl gap-8 px-5 py-8 lg:grid-cols-[0.8fr_1.2fr]">
        <div>
          <p className={kicker}>How a file moves</p>
          <h2 className={h2}>You submit once. The desk talks to the banks.</h2>
        </div>
        <ol className="grid gap-3">
          {steps.map((step) => (
            <li key={step.n} className="grid grid-cols-[auto_1fr] gap-3 rounded-2xl border border-emerald-100 bg-white p-4">
              <span className="text-lg font-semibold text-emerald-700">{step.n}</span>
              <div>
                <h3 className="font-semibold">{step.title}</h3>
                <p className="text-sm text-emerald-900/75">{step.body}</p>
              </div>
            </li>
          ))}
        </ol>
      </section>

      <section className="mx-auto w-full max-w-6xl px-5 py-10">
        <p className={kicker}>Illustrative desk</p>
        <h2 className={h2}>Lenders in this mock book.</h2>
        <p className="max-w-2xl text-emerald-900/75">Starting rates for a well-paid salaried NRI home loan. Your number will differ.</p>
        <div className="mt-5 overflow-x-auto rounded-2xl border border-emerald-100 bg-white shadow-sm">
          <table className="w-full border-collapse text-left text-sm">
            <thead className="bg-emerald-800 text-emerald-50">
              <tr>
                <th className="px-4 py-3 font-medium">Lender</th>
                <th className="px-4 py-3 font-medium">Home</th>
                <th className="px-4 py-3 font-medium">Why it shows up</th>
              </tr>
            </thead>
            <tbody>
              {lenders.map((lender) => (
                <tr key={lender.id} className="border-t border-emerald-100">
                  <td className="px-4 py-3">
                    <strong className="block">{lender.name}</strong>
                    <span className="text-emerald-700">{lender.highlight}</span>
                  </td>
                  <td className="px-4 py-3 font-medium">{lender.rates.home ? `${lender.rates.home.toFixed(2)}%` : "—"}</td>
                  <td className="px-4 py-3 text-emerald-900/80">{lender.note}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      <EmiPlanner />

      <section className="mx-auto w-full max-w-6xl px-5 py-10">
        <p className={kicker}>Questions</p>
        <h2 className={h2}>What changes because you do not live in India.</h2>
        <div className="mt-4 grid gap-3">
          {faqs.map((faq) => (
            <details key={faq.q} className="rounded-2xl border border-emerald-100 bg-white px-4 py-3">
              <summary className="cursor-pointer font-medium">{faq.q}</summary>
              <p className="mt-2 text-sm text-emerald-900/80">{faq.a}</p>
            </details>
          ))}
        </div>
      </section>
    </>
  );
}
