export function inr(amount: number) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(Math.round(amount));
}

export function inrCompact(amount: number) {
  if (amount >= 10_000_000) return `₹${(amount / 10_000_000).toFixed(2)} Cr`;
  if (amount >= 100_000) return `₹${(amount / 100_000).toFixed(1)} L`;
  return inr(amount);
}

export function emi(principal: number, annualPercent: number, years: number) {
  const months = Math.max(1, Math.round(years * 12));
  const monthly = annualPercent / 12 / 100;
  if (monthly === 0) return principal / months;
  const factor = (1 + monthly) ** months;
  return (principal * monthly * factor) / (factor - 1);
}

export function toInr(amount: number, perInr: number) {
  return amount * perInr;
}
