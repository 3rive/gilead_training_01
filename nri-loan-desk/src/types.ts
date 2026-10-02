export type LoanTypeId =
  | "home"
  | "plot"
  | "construction"
  | "balance"
  | "lap"
  | "education";

export type AppStatus =
  | "received"
  | "documents"
  | "offers"
  | "selected"
  | "sanction"
  | "disbursed";

export type TimelineEvent = {
  id: string;
  label: string;
  detail: string;
  at: string;
  state: "done" | "current" | "upcoming";
};

export type Offer = {
  lenderId: string;
  lender: string;
  rate: number;
  tenureYears: number;
  emi: number;
  processingFee: string;
  note: string;
};

export type Application = {
  reference: string;
  createdAt: string;
  status: AppStatus;
  fullName: string;
  email: string;
  phone: string;
  country: string;
  cityAbroad: string;
  yearsAbroad: number;
  residency: string;
  loanType: LoanTypeId;
  amountInr: number;
  tenureYears: number;
  cityInIndia: string;
  purpose: string;
  incomeCurrency: string;
  annualIncome: number;
  employer: string;
  employment: string;
  coApplicant: boolean;
  coApplicantRelation: string;
  hasNreNro: "yes" | "no" | "opening";
  needsPoa: boolean;
  chosenLender: string | null;
  timeline: TimelineEvent[];
  offers: Offer[];
};

export type Draft = {
  country: string;
  cityAbroad: string;
  yearsAbroad: string;
  residency: string;
  loanType: LoanTypeId;
  amountInr: string;
  tenureYears: string;
  cityInIndia: string;
  purpose: string;
  incomeCurrency: string;
  annualIncome: string;
  employer: string;
  employment: string;
  coApplicant: "yes" | "no";
  coApplicantRelation: string;
  hasNreNro: "yes" | "no" | "opening";
  needsPoa: "yes" | "no";
  fullName: string;
  email: string;
  phone: string;
};
