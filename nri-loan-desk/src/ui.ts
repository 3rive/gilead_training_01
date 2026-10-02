import type { AppStatus } from "./types";

export const kicker = "text-xs font-semibold uppercase tracking-[0.16em] text-emerald-700";
export const page = "mx-auto w-full max-w-6xl px-5 py-10";
export const h1 = "mt-2 text-4xl font-semibold tracking-tight text-emerald-950 sm:text-5xl";
export const h2 = "mt-1 text-3xl font-semibold tracking-tight text-emerald-950";
export const lede = "mt-3 max-w-2xl text-lg leading-relaxed text-emerald-900/80";
export const btn =
  "inline-flex items-center justify-center rounded-full bg-emerald-800 px-4 py-2.5 text-sm font-medium text-white shadow-sm transition hover:bg-emerald-900 disabled:cursor-not-allowed disabled:opacity-50";
export const btnGhost =
  "inline-flex items-center justify-center rounded-full border border-emerald-800 bg-white px-4 py-2.5 text-sm font-medium text-emerald-900 transition hover:bg-emerald-50";
export const card =
  "flex flex-col gap-2 rounded-2xl border border-emerald-100 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md";
export const field =
  "mt-1 w-full rounded-lg border border-emerald-200 bg-white px-3 py-2.5 text-emerald-950 outline-none ring-emerald-200 focus:border-emerald-700 focus:ring-2";
export const label = "grid gap-1 text-sm font-medium text-emerald-950";

export function pill(status: AppStatus) {
  const tone =
    status === "documents" || status === "received"
      ? "bg-lime-100 text-lime-900"
      : status === "disbursed"
        ? "bg-emerald-200 text-emerald-950"
        : "bg-emerald-100 text-emerald-900";
  return `inline-flex w-fit rounded-full px-2.5 py-1 text-xs font-medium ${tone}`;
}
