import { Link, useParams } from "react-router-dom";
import { useState } from "react";
import { loanById } from "../data/catalog";
import { inr } from "../lib/money";
import { findApplication, selectOffer, statusLabel } from "../lib/store";
import type { Application } from "../types";

const accountCopy = { yes: "NRE or NRO already open", opening: "Account still to be opened", no: "No NRE or NRO yet" };

export function ApplicationPage() {
  const { reference = "" } = useParams();
  const [file, setFile] = useState<Application | undefined>(() => findApplication(reference));

  if (!file) {
    return (
      <section className="page">
        <h1>No file called {reference}</h1>
        <p className="lede narrow">Check the reference, or go back to the list. Sample files use HW-24018, HW-23880, and HW-24102.</p>
        <Link className="button" to="/track">All files</Link>
      </section>
    );
  }

  function choose(lender: string) {
    const next = selectOffer(file!.reference, lender);
    if (next) setFile(next);
  }

  const loan = loanById(file.loanType);

  return (
    <section className="page file">
      <p className="kicker">File {file.reference}</p>
      <div className="file-title">
        <h1>{file.fullName}</h1>
        <span className={`pill pill-${file.status}`}>{statusLabel(file.status)}</span>
      </div>
      <p className="lede narrow">
        {loan.name} of {inr(file.amountInr)} over {file.tenureYears} years, for {file.cityInIndia}. Income is earned in {file.cityAbroad}, {file.country}.
      </p>

      <dl className="facts">
        <div><dt>Purpose</dt><dd>{file.purpose}</dd></div>
        <div><dt>Work</dt><dd>{file.employment} · {file.employer}</dd></div>
        <div><dt>Residency</dt><dd>{file.residency}, {file.yearsAbroad} years abroad</dd></div>
        <div><dt>Repayment account</dt><dd>{accountCopy[file.hasNreNro]}</dd></div>
        <div><dt>Co-applicant</dt><dd>{file.coApplicant ? file.coApplicantRelation : "None"}</dd></div>
        <div><dt>Power of attorney</dt><dd>{file.needsPoa ? "Needed for the India signature" : "Applicant can travel"}</dd></div>
      </dl>

      <h2>Timeline</h2>
      <ol className="timeline">
        {file.timeline.map((event) => (
          <li key={event.id} className={event.state}>
            <span />
            <div>
              <strong>{event.label}</strong>
              <p>{event.detail}</p>
            </div>
          </li>
        ))}
      </ol>

      <h2>Indicative offers</h2>
      {file.offers.length === 0 ? (
        <p className="aside">Offers appear after the document check. This file is still on that step.</p>
      ) : (
        <div className="offers">
          {file.offers.map((offer) => (
            <article key={offer.lenderId} className={file.chosenLender === offer.lender ? "chosen" : ""}>
              <header>
                <h3>{offer.lender}</h3>
                <strong>{offer.rate.toFixed(2)}%</strong>
              </header>
              <p>{inr(offer.emi)} a month · {offer.tenureYears} years</p>
              <p className="note">Fee {offer.processingFee}. {offer.note}</p>
              {file.chosenLender === offer.lender ? (
                <p className="chosen-label">Selected for sanction</p>
              ) : (
                <button type="button" className="button" onClick={() => choose(offer.lender)} disabled={file.status === "disbursed"}>
                  Proceed with {offer.lender}
                </button>
              )}
            </article>
          ))}
        </div>
      )}
      <p className="fine">These offers are calculated in the browser from the mock rate card. They are not a sanction from the named bank.</p>
      <Link to="/track">Back to all files</Link>
    </section>
  );
}
