import { countryByName, lenders, loanById } from "../data/catalog";
import { emi, toInr } from "./money";
import type { Application, AppStatus, Draft, Offer, TimelineEvent } from "../types";

const KEY = "homeward.applications.v1";

const statusCopy: Record<AppStatus, string> = {
  received: "Request received",
  documents: "Documents in review",
  offers: "Offers ready",
  selected: "Offer selected",
  sanction: "Sanction in progress",
  disbursed: "Disbursed",
};

export function statusLabel(status: AppStatus) {
  return statusCopy[status];
}

function timelineFor(status: AppStatus, createdAt: string, lender: string | null): TimelineEvent[] {
  const order: AppStatus[] = ["received", "documents", "offers", "selected", "sanction", "disbursed"];
  const index = order.indexOf(status);
  const rows: { id: AppStatus; label: string; detail: string }[] = [
    { id: "received", label: "Request received", detail: "The desk has the country, income, and property city." },
    { id: "documents", label: "Document list sent", detail: "Passport, visa, salary trail, and India-side papers. One upload, not one per bank." },
    { id: "offers", label: "Indicative offers", detail: "Lenders that accept income from this country, with EMI and fee." },
    { id: "selected", label: "Lender chosen", detail: lender ? `${lender} is the file we will take forward.` : "You pick one offer. The others stay on the record." },
    { id: "sanction", label: "Sanction", detail: "The lender’s credit team, not Homeward, issues the sanction letter." },
    { id: "disbursed", label: "Disbursed", detail: "Funds move in India. EMI starts from the NRE or NRO account." },
  ];
  return rows.map((row, rowIndex) => ({
    id: row.id,
    label: row.label,
    detail: row.detail,
    at: rowIndex <= index ? createdAt : "",
    state: rowIndex < index ? "done" : rowIndex === index ? "current" : "upcoming",
  }));
}

function buildOffers(draft: {
  country: string;
  loanType: Application["loanType"];
  amountInr: number;
  tenureYears: number;
}): Offer[] {
  return lenders
    .filter((lender) => lender.rates[draft.loanType] != null)
    .filter((lender) => lender.countries === "all" || lender.countries.includes(draft.country))
    .map((lender) => {
      const base = lender.rates[draft.loanType] ?? 9;
      const loading = draft.country === "United States" || draft.country === "Canada" ? 0.15 : 0;
      const rate = Math.round((base + loading) * 100) / 100;
      return {
        lenderId: lender.id,
        lender: lender.name,
        rate,
        tenureYears: draft.tenureYears,
        emi: emi(draft.amountInr, rate, draft.tenureYears),
        processingFee: lender.fee,
        note: lender.note,
      };
    })
    .sort((a, b) => a.rate - b.rate)
    .slice(0, 4);
}

function reference() {
  const n = Math.floor(10000 + Math.random() * 89999);
  return `HW-${n}`;
}

export function createApplication(draft: Draft): Application {
  const createdAt = new Date().toISOString();
  const amountInr = Number(draft.amountInr);
  const tenureYears = Number(draft.tenureYears);
  const application: Application = {
    reference: reference(),
    createdAt,
    status: "offers",
    fullName: draft.fullName.trim(),
    email: draft.email.trim(),
    phone: draft.phone.trim(),
    country: draft.country,
    cityAbroad: draft.cityAbroad.trim(),
    yearsAbroad: Number(draft.yearsAbroad),
    residency: draft.residency,
    loanType: draft.loanType,
    amountInr,
    tenureYears,
    cityInIndia: draft.cityInIndia.trim(),
    purpose: draft.purpose.trim(),
    incomeCurrency: draft.incomeCurrency,
    annualIncome: Number(draft.annualIncome),
    employer: draft.employer.trim(),
    employment: draft.employment,
    coApplicant: draft.coApplicant === "yes",
    coApplicantRelation: draft.coApplicantRelation.trim(),
    hasNreNro: draft.hasNreNro,
    needsPoa: draft.needsPoa === "yes",
    chosenLender: null,
    timeline: [],
    offers: buildOffers({
      country: draft.country,
      loanType: draft.loanType,
      amountInr,
      tenureYears,
    }),
  };
  application.timeline = timelineFor("offers", createdAt, null);
  return application;
}

function seed(): Application[] {
  const meera = createApplication({
    country: "United Arab Emirates",
    cityAbroad: "Dubai",
    yearsAbroad: "6",
    residency: "Employment visa",
    loanType: "home",
    amountInr: "18500000",
    tenureYears: "20",
    cityInIndia: "Hyderabad",
    purpose: "Ready 3BHK in Kokapet for parents, and for us in a few years.",
    incomeCurrency: "AED",
    annualIncome: "420000",
    employer: "Emirates NBD",
    employment: "Salaried",
    coApplicant: "yes",
    coApplicantRelation: "Mother, resident in Hyderabad",
    hasNreNro: "yes",
    needsPoa: "yes",
    fullName: "Meera Iyer",
    email: "meera@example.com",
    phone: "+971 50 000 0000",
  });
  meera.reference = "HW-24018";
  meera.createdAt = "2026-08-12T09:30:00.000Z";
  meera.status = "selected";
  meera.chosenLender = "Bajaj Housing";
  meera.timeline = timelineFor("selected", meera.createdAt, meera.chosenLender);

  const arjun = createApplication({
    country: "United States",
    cityAbroad: "Jersey City",
    yearsAbroad: "9",
    residency: "H-1B",
    loanType: "balance",
    amountInr: "6400000",
    tenureYears: "12",
    cityInIndia: "Pune",
    purpose: "Transfer the 2019 SBI home loan. Current rate never moved after I left.",
    incomeCurrency: "USD",
    annualIncome: "168000",
    employer: "A payments company",
    employment: "Salaried",
    coApplicant: "no",
    coApplicantRelation: "",
    hasNreNro: "yes",
    needsPoa: "no",
    fullName: "Arjun Deshpande",
    email: "arjun@example.com",
    phone: "+1 201 555 0148",
  });
  arjun.reference = "HW-23880";
  arjun.createdAt = "2026-07-02T14:10:00.000Z";
  arjun.status = "documents";
  arjun.offers = [];
  arjun.timeline = timelineFor("documents", arjun.createdAt, null);

  const leela = createApplication({
    country: "Singapore",
    cityAbroad: "Singapore",
    yearsAbroad: "4",
    residency: "Employment Pass",
    loanType: "education",
    amountInr: "4500000",
    tenureYears: "8",
    cityInIndia: "Bengaluru",
    purpose: "Master’s fees for my sister. I am the earning co-applicant.",
    incomeCurrency: "SGD",
    annualIncome: "120000",
    employer: "A regional bank",
    employment: "Salaried",
    coApplicant: "yes",
    coApplicantRelation: "Sister, the student",
    hasNreNro: "opening",
    needsPoa: "no",
    fullName: "Leela Nair",
    email: "leela@example.com",
    phone: "+65 9000 0000",
  });
  leela.reference = "HW-24102";
  leela.createdAt = "2026-09-20T04:00:00.000Z";
  leela.status = "disbursed";
  leela.chosenLender = "Bank of Baroda";
  leela.timeline = timelineFor("disbursed", leela.createdAt, leela.chosenLender);

  return [meera, arjun, leela];
}

export function loadApplications(): Application[] {
  const raw = localStorage.getItem(KEY);
  if (!raw) {
    const initial = seed();
    localStorage.setItem(KEY, JSON.stringify(initial));
    return initial;
  }
  return JSON.parse(raw) as Application[];
}

export function saveApplications(applications: Application[]) {
  localStorage.setItem(KEY, JSON.stringify(applications));
}

export function upsertApplication(application: Application) {
  const all = loadApplications().filter((item) => item.reference !== application.reference);
  saveApplications([application, ...all]);
}

export function findApplication(reference: string) {
  return loadApplications().find((item) => item.reference.toLowerCase() === reference.trim().toLowerCase());
}

export function selectOffer(reference: string, lender: string) {
  const current = findApplication(reference);
  if (!current) return;
  const next: Application = {
    ...current,
    status: "selected",
    chosenLender: lender,
    timeline: timelineFor("selected", current.createdAt, lender),
  };
  upsertApplication(next);
  return next;
}

export function incomeInInr(annual: number, currency: string) {
  const match = countryByName(
    currency === "AED"
      ? "United Arab Emirates"
      : currency === "USD"
        ? "United States"
        : currency === "GBP"
          ? "United Kingdom"
          : currency === "SGD"
            ? "Singapore"
            : currency === "CAD"
              ? "Canada"
              : currency === "AUD"
                ? "Australia"
                : currency === "EUR"
                  ? "Germany"
                  : currency === "QAR"
                    ? "Qatar"
                    : "Saudi Arabia",
  );
  return toInr(annual, match.perInr);
}

export function loanName(id: Application["loanType"]) {
  return loanById(id).name;
}
