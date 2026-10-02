import { Link, useParams } from "react-router-dom";
import { useState } from "react";
import { loanById } from "../data/catalog";
import { inr } from "../lib/money";
import { findApplication, selectOffer, statusLabel } from "../lib/store";
import type { Application } from "../types";
import { btn, h1, h2, kicker, lede, page, pill } from "../ui";

const accountCopy = { yes: "NRE or NRO already open", opening: "Account still to be opened", no: "No NRE or NRO yet" };

export function ApplicationPage() {
  const { reference = "" } = useParams();
  const [file, setFile] = useState<Application | undefined>(() => findApplication(reference));

  if (!file) {
    return (
      <section className={page}>
        <h1 className={h1}>No file called {reference}</h1>
        <p className={lede}>Check the reference, or go back to the list. Sample files use HW-24018, HW-23880, and HW-24102.</p>
        <Link className={`${btn} mt-4`} to="/track">All files</Link>
      </section>
    );
  }

  function choose(lender: string) {
    const next = selectOffer(file!.reference, lender);
    if (next) setFile(next);
  }

  const loan = loanById(file.loanType);

  return (
    <section className={page}>
      <p className={kicker}>File {file.reference}</p>
      <div className="mt-2 flex flex-wrap items-end justify-between gap-3">
        <h1 className="text-4xl font-semibold tracking-tight sm:text-5xl">{file.fullName}</h1>
        <span className={pill(file.status)}>{statusLabel(file.status)}</span>
      </div>
      <p className={lede}>
        {loan.name} of {inr(file.amountInr)} over {file.tenureYears} years, for {file.cityInIndia}. Income is earned in {file.cityAbroad}, {file.country}.
      </p>

      <dl className="mt-6 grid gap-3 sm:grid-cols-2">
        {[
          ["Purpose", file.purpose],
          ["Work", `${file.employment} · ${file.employer}`],
          ["Residency", `${file.residency}, ${file.yearsAbroad} years abroad`],
          ["Repayment account", accountCopy[file.hasNreNro]],
          ["Co-applicant", file.coApplicant ? file.coApplicantRelation : "None"],
          ["Power of attorney", file.needsPoa ? "Needed for the India signature" : "Applicant can travel"],
        ].map(([term, detail]) => (
          <div key={term} className="rounded-2xl border border-emerald-100 bg-white p-4">
            <dt className="text-xs font-semibold uppercase tracking-wide text-emerald-700">{term}</dt>
            <dd className="mt-1">{detail}</dd>
          </div>
        ))}
      </dl>

      <h2 className={`${h2} mt-10`}>Timeline</h2>
      <ol className="mt-4">
        {file.timeline.map((event, index) => (
          <li key={event.id} className="grid grid-cols-[22px_1fr] gap-3">
            <span className="relative">
              <span className={`mt-1 block h-3.5 w-3.5 rounded-full border-2 ${event.state === "upcoming" ? "border-emerald-200 bg-white" : "border-emerald-700 bg-emerald-700"}`} />
              {index < file.timeline.length - 1 && <span className="absolute left-[6px] top-4 h-full w-px bg-emerald-200" />}
            </span>
            <div className="pb-4">
              <strong>{event.label}</strong>
              <p className="text-sm text-emerald-900/75">{event.detail}</p>
            </div>
          </li>
        ))}
      </ol>

      <h2 className={h2}>Indicative offers</h2>
      {file.offers.length === 0 ? (
        <p className="text-emerald-900/75">Offers appear after the document check. This file is still on that step.</p>
      ) : (
        <div className="mt-4 grid gap-4 md:grid-cols-2">
          {file.offers.map((offer) => {
            const chosen = file.chosenLender === offer.lender;
            return (
              <article key={offer.lenderId} className={`rounded-2xl border bg-white p-5 shadow-sm ${chosen ? "border-emerald-600 bg-emerald-50" : "border-emerald-100"}`}>
                <header className="flex items-baseline justify-between gap-3">
                  <h3 className="text-lg font-semibold">{offer.lender}</h3>
                  <strong className="text-2xl text-emerald-800">{offer.rate.toFixed(2)}%</strong>
                </header>
                <p className="mt-2">{inr(offer.emi)} a month · {offer.tenureYears} years</p>
                <p className="mt-1 text-sm text-emerald-900/75">Fee {offer.processingFee}. {offer.note}</p>
                {chosen ? (
                  <p className="mt-3 font-medium text-emerald-800">Selected for sanction</p>
                ) : (
                  <button type="button" className={`${btn} mt-4`} onClick={() => choose(offer.lender)} disabled={file.status === "disbursed"}>
                    Proceed with {offer.lender}
                  </button>
                )}
              </article>
            );
          })}
        </div>
      )}
      <p className="mt-4 text-sm text-emerald-900/70">These offers are calculated in the browser from the mock rate card. They are not a sanction from the named bank.</p>
      <Link className="mt-3 inline-block font-medium text-emerald-800 underline" to="/track">Back to all files</Link>
    </section>
  );
}
