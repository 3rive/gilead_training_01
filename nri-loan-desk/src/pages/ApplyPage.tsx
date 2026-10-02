import { FormEvent, useMemo, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { countries, countryByName, loanTypes } from "../data/catalog";
import { inr, toInr } from "../lib/money";
import { createApplication, upsertApplication } from "../lib/store";
import type { Draft, LoanTypeId } from "../types";
import { btn, btnGhost, field, h1, kicker, label, lede, page } from "../ui";

const steps = ["Where you live", "The loan", "Income", "India side", "You"];

function initialType(value: string | null): LoanTypeId {
  return loanTypes.some((loan) => loan.id === value) ? (value as LoanTypeId) : "home";
}

export function ApplyPage() {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [errors, setErrors] = useState<string[]>([]);
  const [draft, setDraft] = useState<Draft>({
    country: "United Arab Emirates",
    cityAbroad: "Dubai",
    yearsAbroad: "5",
    residency: "Employment visa",
    loanType: initialType(params.get("type")),
    amountInr: "7500000",
    tenureYears: "20",
    cityInIndia: "Hyderabad",
    purpose: "",
    incomeCurrency: "AED",
    annualIncome: "",
    employer: "",
    employment: "Salaried",
    coApplicant: "no",
    coApplicantRelation: "",
    hasNreNro: "yes",
    needsPoa: "yes",
    fullName: "",
    email: "",
    phone: "",
  });

  const country = countryByName(draft.country);
  const incomeInr = useMemo(() => {
    const annual = Number(draft.annualIncome);
    if (!annual) return 0;
    return toInr(annual, country.perInr);
  }, [country.perInr, draft.annualIncome]);

  function patch(partial: Partial<Draft>) {
    setDraft((current) => ({ ...current, ...partial }));
  }

  function validate(index: number) {
    const next: string[] = [];
    if (index === 0) {
      if (!draft.cityAbroad.trim()) next.push("City abroad is required.");
      if (Number(draft.yearsAbroad) <= 0) next.push("Years abroad should be at least 1.");
    }
    if (index === 1) {
      const amount = Number(draft.amountInr);
      if (amount < 500000) next.push("Amount should be at least ₹5 lakh.");
      if (!draft.cityInIndia.trim()) next.push("City in India is required.");
      if (draft.purpose.trim().length < 12) next.push("Say what the money is for, in a sentence.");
    }
    if (index === 2) {
      if (Number(draft.annualIncome) <= 0) next.push("Annual income is required.");
      if (!draft.employer.trim()) next.push("Employer or practice name is required.");
    }
    if (index === 3 && draft.coApplicant === "yes" && !draft.coApplicantRelation.trim()) {
      next.push("Name the co-applicant relationship.");
    }
    if (index === 4) {
      if (draft.fullName.trim().length < 3) next.push("Full name is required.");
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(draft.email)) next.push("A real email is required so the file can be tracked.");
      if (draft.phone.trim().length < 8) next.push("Phone with country code is required.");
    }
    setErrors(next);
    return next.length === 0;
  }

  function onContinue(event: FormEvent) {
    event.preventDefault();
    if (!validate(step)) return;
    setStep((current) => current + 1);
  }

  function onSubmit(event: FormEvent) {
    event.preventDefault();
    if (!validate(4)) return;
    const application = createApplication(draft);
    upsertApplication(application);
    navigate(`/track/${application.reference}`);
  }

  return (
    <section className={`${page} grid items-start gap-8 lg:grid-cols-[0.85fr_1fr]`}>
      <div>
        <p className={kicker}>New request</p>
        <h1 className={h1}>Raise a loan file from where you live.</h1>
        <p className={lede}>No documents on this step. The reference you get at the end is how you track the file.</p>
        <ol className="mt-6 flex flex-wrap gap-2">
          {steps.map((name, index) => (
            <li
              key={name}
              className={`border-b-2 px-1 pb-1 text-sm ${index === step ? "border-emerald-700 font-semibold text-emerald-950" : index < step ? "border-emerald-400 text-emerald-700" : "border-emerald-100 text-emerald-800/60"}`}
            >
              {name}
            </li>
          ))}
        </ol>
      </div>

      <form className="grid gap-4 rounded-2xl border border-emerald-100 bg-white p-6 shadow-sm" onSubmit={step === 4 ? onSubmit : onContinue}>
        {errors.length > 0 && (
          <div className="rounded-xl bg-red-50 px-3 py-2 text-sm text-red-800" role="alert">
            {errors.map((error) => (
              <p key={error}>{error}</p>
            ))}
          </div>
        )}

        {step === 0 && (
          <>
            <label className={label}>
              Country of residence
              <select className={field}
                value={draft.country}
                onChange={(event) => {
                  const next = countryByName(event.target.value);
                  patch({ country: next.name, incomeCurrency: next.currency, cityAbroad: next.cities[0] });
                }}
              >
                {countries.map((item) => (
                  <option key={item.name}>{item.name}</option>
                ))}
              </select>
            </label>
            <label className={label}>
              City
              <input className={field} value={draft.cityAbroad} onChange={(event) => patch({ cityAbroad: event.target.value })} />
            </label>
            <label className={label}>
              Years abroad
              <input className={field}
                inputMode="decimal"
                value={draft.yearsAbroad}
                onChange={(event) => patch({ yearsAbroad: event.target.value })}
              />
            </label>
            <label className={label}>
              Residency
              <select className={field} value={draft.residency} onChange={(event) => patch({ residency: event.target.value })}>
                {["Employment visa", "Permanent residence", "Citizen (OCI / PIO)", "Student visa", "Dependent visa"].map((item) => (
                  <option key={item}>{item}</option>
                ))}
              </select>
            </label>
          </>
        )}

        {step === 1 && (
          <>
            <label className={label}>
              Loan
              <select className={field} value={draft.loanType} onChange={(event) => patch({ loanType: event.target.value as LoanTypeId })}>
                {loanTypes.map((loan) => (
                  <option key={loan.id} value={loan.id}>
                    {loan.name}
                  </option>
                ))}
              </select>
            </label>
            <label className={label}>
              Amount in rupees
              <input className={field} inputMode="numeric" value={draft.amountInr} onChange={(event) => patch({ amountInr: event.target.value })} />
            </label>
            <label className={label}>
              Tenure in years
              <input className={field} inputMode="numeric" value={draft.tenureYears} onChange={(event) => patch({ tenureYears: event.target.value })} />
            </label>
            <label className={label}>
              City in India
              <input className={field} value={draft.cityInIndia} onChange={(event) => patch({ cityInIndia: event.target.value })} />
            </label>
            <label className={label}>
              What is this for?
              <textarea className={field} value={draft.purpose} onChange={(event) => patch({ purpose: event.target.value })} rows={4} />
            </label>
          </>
        )}

        {step === 2 && (
          <>
            <label className={label}>
              Work
              <select className={field} value={draft.employment} onChange={(event) => patch({ employment: event.target.value })}>
                {["Salaried", "Self-employed", "Professional practice", "Business owner"].map((item) => (
                  <option key={item}>{item}</option>
                ))}
              </select>
            </label>
            <label className={label}>
              Employer or practice
              <input className={field} value={draft.employer} onChange={(event) => patch({ employer: event.target.value })} />
            </label>
            <label className={label}>
              Annual income ({draft.incomeCurrency})
              <input className={field} inputMode="decimal" value={draft.annualIncome} onChange={(event) => patch({ annualIncome: event.target.value })} />
            </label>
            <p className="text-sm text-emerald-800/80">
              {incomeInr > 0
                ? `About ${inr(incomeInr)} a year at the demo rate of 1 ${draft.incomeCurrency} = ₹${country.perInr}. Lenders still read the foreign-currency statements.`
                : "We convert this only so the desk can sketch eligibility. The lender reads the original currency."}
            </p>
          </>
        )}

        {step === 3 && (
          <>
            <fieldset className="rounded-xl border border-emerald-200 p-3">
              <legend className="px-1 text-sm font-medium">Resident co-applicant in India?</legend>
              <label className="flex items-center gap-2 py-1 text-sm">
                <input className="accent-emerald-700" type="radio" name="co" checked={draft.coApplicant === "yes"} onChange={() => patch({ coApplicant: "yes" })} />
                Yes, a close relative
              </label>
              <label className="flex items-center gap-2 py-1 text-sm">
                <input className="accent-emerald-700" type="radio" name="co" checked={draft.coApplicant === "no"} onChange={() => patch({ coApplicant: "no" })} />
                No
              </label>
            </fieldset>
            {draft.coApplicant === "yes" && (
              <label className={label}>
                Relationship
                <input className={field}
                  value={draft.coApplicantRelation}
                  onChange={(event) => patch({ coApplicantRelation: event.target.value })}
                  placeholder="Spouse, parent, sibling"
                />
              </label>
            )}
            <label className={label}>
              NRE or NRO account
              <select className={field} value={draft.hasNreNro} onChange={(event) => patch({ hasNreNro: event.target.value as Draft["hasNreNro"] })}>
                <option value="yes">Already open</option>
                <option value="opening">Need to open one</option>
                <option value="no">Not yet, and I am unsure</option>
              </select>
            </label>
            <label className={label}>
              Power of attorney for the India visit
              <select className={field} value={draft.needsPoa} onChange={(event) => patch({ needsPoa: event.target.value as Draft["needsPoa"] })}>
                <option value="yes">Yes, someone there should sign</option>
                <option value="no">I can travel for registration</option>
              </select>
            </label>
          </>
        )}

        {step === 4 && (
          <>
            <label className={label}>
              Full name
              <input className={field} value={draft.fullName} onChange={(event) => patch({ fullName: event.target.value })} />
            </label>
            <label className={label}>
              Email
              <input className={field} type="email" value={draft.email} onChange={(event) => patch({ email: event.target.value })} />
            </label>
            <label className={label}>
              Phone with country code
              <input className={field} value={draft.phone} onChange={(event) => patch({ phone: event.target.value })} />
            </label>
            <div className="rounded-xl bg-emerald-50 p-3 text-sm">
              <p>
                {draft.fullName || "You"} · {draft.cityAbroad}, {draft.country}
              </p>
              <p>
                {loanTypes.find((loan) => loan.id === draft.loanType)?.name} of {inr(Number(draft.amountInr) || 0)} in {draft.cityInIndia}
              </p>
              <p>{draft.purpose}</p>
            </div>
          </>
        )}

        <div className="flex justify-between gap-3">
          {step > 0 ? (
            <button type="button" className={btnGhost} onClick={() => { setErrors([]); setStep((current) => current - 1); }}>
              Back
            </button>
          ) : (
            <Link className={btnGhost} to="/loans">
              All loans
            </Link>
          )}
          <button className={btn} type="submit">
            {step === 4 ? "Submit request" : "Continue"}
          </button>
        </div>
      </form>
    </section>
  );
}
