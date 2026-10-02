import type { LoanTypeId } from "../types";

export const countries = [
  { name: "United Arab Emirates", currency: "AED", perInr: 22.9, cities: ["Dubai", "Abu Dhabi", "Sharjah"] },
  { name: "United States", currency: "USD", perInr: 83.4, cities: ["New Jersey", "Dallas", "Bay Area", "New York"] },
  { name: "United Kingdom", currency: "GBP", perInr: 106, cities: ["London", "Manchester", "Birmingham"] },
  { name: "Singapore", currency: "SGD", perInr: 62.4, cities: ["Singapore"] },
  { name: "Canada", currency: "CAD", perInr: 61.2, cities: ["Toronto", "Vancouver"] },
  { name: "Australia", currency: "AUD", perInr: 54.8, cities: ["Sydney", "Melbourne"] },
  { name: "Germany", currency: "EUR", perInr: 90.5, cities: ["Berlin", "Munich", "Frankfurt"] },
  { name: "Qatar", currency: "QAR", perInr: 22.9, cities: ["Doha"] },
  { name: "Saudi Arabia", currency: "SAR", perInr: 22.2, cities: ["Riyadh", "Jeddah"] },
] as const;

export const loanTypes: {
  id: LoanTypeId;
  name: string;
  from: number;
  blurb: string;
  nri: string;
}[] = [
  {
    id: "home",
    name: "Home loan",
    from: 7.15,
    blurb: "Buy a ready flat or house in India without flying back for every bank visit.",
    nri: "Repaid from an NRE or NRO account. Many lenders cap LTV around 75–80% for non-residents.",
  },
  {
    id: "plot",
    name: "Plot and build",
    from: 7.5,
    blurb: "Finance the land and the construction that follows, released in stages.",
    nri: "The site has to be an approved residential plot. Farmhouses and agricultural land are out.",
  },
  {
    id: "construction",
    name: "Construction",
    from: 7.5,
    blurb: "You already hold the plot. The loan pays the build.",
    nri: "Drawdowns follow engineer certificates. A resident relative can sign stage papers with a POA.",
  },
  {
    id: "balance",
    name: "Balance transfer",
    from: 7.2,
    blurb: "Move an Indian home loan you already service onto a lower NRI rate.",
    nri: "Useful when the original loan was taken as a resident and the pricing never caught up.",
  },
  {
    id: "lap",
    name: "Loan against property",
    from: 8.99,
    blurb: "Raise funds against a residential property you already own in India.",
    nri: "End use is checked. Lenders are stricter on commercial property for non-residents.",
  },
  {
    id: "education",
    name: "Education loan",
    from: 8.25,
    blurb: "Fund a degree, with an NRI parent or the student as the earning applicant.",
    nri: "Admission letter, fee schedule, and the co-applicant’s overseas income are the usual file.",
  },
];

export type Lender = {
  id: string;
  name: string;
  highlight: string;
  rates: Partial<Record<LoanTypeId, number>>;
  countries: string[] | "all";
  fee: string;
  note: string;
};

export const lenders: Lender[] = [
  {
    id: "bajaj",
    name: "Bajaj Housing",
    highlight: "Keen starting rate",
    rates: { home: 7.15, plot: 7.4, construction: 7.45, balance: 7.25, lap: 9.1 },
    countries: "all",
    fee: "0.25% + GST",
    note: "Fast on salaried Gulf and Singapore files with a resident co-applicant.",
  },
  {
    id: "canara",
    name: "Canara Bank",
    highlight: "PSU NRI desk",
    rates: { home: 7.15, plot: 7.55, construction: 7.55, balance: 7.3, education: 8.4, lap: 9.2 },
    countries: "all",
    fee: "0.50% up to a cap",
    note: "Comfortable with NRE salary credits from the Gulf.",
  },
  {
    id: "bob",
    name: "Bank of Baroda",
    highlight: "Strong in UAE and UK",
    rates: { home: 7.2, plot: 7.6, balance: 7.35, education: 8.35, lap: 9.15 },
    countries: ["United Arab Emirates", "United Kingdom", "United States", "Qatar", "Singapore"],
    fee: "0.25% for salaried NRI",
    note: "Branch network overlaps the corridors where most of our files start.",
  },
  {
    id: "sbi",
    name: "State Bank of India",
    highlight: "Widest NRI coverage",
    rates: { home: 7.5, plot: 7.7, construction: 7.7, balance: 7.55, education: 8.5, lap: 9.4 },
    countries: "all",
    fee: "0.35% + GST",
    note: "The default when the country of residence is outside the usual five.",
  },
  {
    id: "icici",
    name: "ICICI Bank",
    highlight: "Pre-approved salaried",
    rates: { home: 7.5, plot: 7.85, construction: 7.8, balance: 7.6, lap: 9.25, education: 8.65 },
    countries: "all",
    fee: "0.50% negotiable",
    note: "Prefers category employers and two years of overseas continuity.",
  },
  {
    id: "hdfc",
    name: "HDFC Bank",
    highlight: "Quick sanction",
    rates: { home: 7.75, construction: 7.9, balance: 7.7, lap: 9.35 },
    countries: ["United Arab Emirates", "United States", "United Kingdom", "Singapore", "Canada", "Australia"],
    fee: "Up to 0.50%",
    note: "Asks for a relationship account. Disbursal is often the fastest of the private banks.",
  },
  {
    id: "axis",
    name: "Axis Bank",
    highlight: "Joint income friendly",
    rates: { home: 8.0, plot: 8.15, balance: 7.9, lap: 9.5, education: 8.9 },
    countries: "all",
    fee: "0.50%",
    note: "Useful when a resident spouse’s Indian income is added to the overseas salary.",
  },
  {
    id: "kotak",
    name: "Kotak Mahindra",
    highlight: "US and Canada files",
    rates: { home: 7.6, balance: 7.65, lap: 9.3, education: 8.7 },
    countries: ["United States", "Canada", "United Kingdom", "Singapore", "United Arab Emirates"],
    fee: "0.40%",
    note: "Reads W-2 and T4 income without forcing an Indian ITR for the NRI applicant.",
  },
];

export const faqs = [
  {
    q: "Can I apply without visiting India?",
    a: "Yes. The request, document chase, and lender follow-up happen over email and a call in your time zone. A power of attorney covers the in-person registration step. You do not book a flight to start.",
  },
  {
    q: "Which income proofs do lenders actually read?",
    a: "Gulf residents usually send a salary certificate, six months of bank statements, and a passport with the residence visa. US files use W-2s and pay stubs. UK files use P60s and contracts. The exact list depends on the lender we shortlist.",
  },
  {
    q: "How is the EMI paid?",
    a: "From an NRE or NRO account in India, not by a standing order from your bank abroad. If you do not have one yet, opening it is part of the file.",
  },
  {
    q: "Will this check hurt my CIBIL score?",
    a: "Filing a request on Homeward does not pull a bureau. A lender’s own credit enquiry, once you accept an offer, can. We say so before that step.",
  },
  {
    q: "Do you lend the money?",
    a: "No. Homeward is a desk that compares partner lenders and keeps one file. The bank or housing company sanctions, prices, and disburses. Rates on this site are illustrative mock figures, not live offers.",
  },
  {
    q: "What cannot be financed?",
    a: "Agricultural land and most farmhouses are refused for NRI borrowers. Under-construction projects need a lender-approved builder. Cash salary with no trail is a hard stop.",
  },
];

export const steps = [
  { n: "01", title: "Tell us the country and the house", body: "Two minutes. No PDFs yet. We only need where you live, what you want in India, and a rough amount." },
  { n: "02", title: "One advisor, your time zone", body: "A person calls within one working day. Same person stays on the file through sanction." },
  { n: "03", title: "Documents, once", body: "Passport, visa, overseas income, and the India-side papers. We do not ask you to upload the same set to eight banks." },
  { n: "04", title: "Offers you can compare", body: "Indicative rates, EMI, and fees from lenders that already take NRI income from your country." },
  { n: "05", title: "POA, then disbursement", body: "If a signature is needed in India, we draft the power of attorney. Funds move to the seller from the lending bank." },
];

export function countryByName(name: string) {
  return countries.find((country) => country.name === name) ?? countries[0];
}

export function loanById(id: LoanTypeId) {
  return loanTypes.find((loan) => loan.id === id) ?? loanTypes[0];
}
