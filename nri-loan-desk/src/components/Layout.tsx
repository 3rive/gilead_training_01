import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import { btn } from "../ui";

const links = [
  { to: "/loans", label: "Loans" },
  { to: "/apply", label: "Apply" },
  { to: "/track", label: "Track" },
];

export function Layout() {
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-screen bg-emerald-50 text-emerald-950">
      <header className="sticky top-0 z-30 border-b border-emerald-100 bg-emerald-50/90 backdrop-blur">
        <div className="mx-auto flex w-full max-w-6xl items-center justify-between gap-4 px-5 py-3">
          <NavLink to="/" className="flex items-center gap-2 text-lg font-semibold tracking-tight" onClick={() => setOpen(false)}>
            <span className="grid h-8 w-8 place-items-center rounded-lg bg-emerald-800 text-sm text-emerald-50" aria-hidden="true">H</span>
            Homeward
          </NavLink>
          <button
            className="rounded-full border border-emerald-200 bg-white px-3 py-1.5 text-sm md:hidden"
            type="button"
            aria-expanded={open}
            onClick={() => setOpen((value) => !value)}
          >
            {open ? "Close" : "Menu"}
          </button>
          <nav className={`${open ? "flex" : "hidden"} absolute left-4 right-4 top-16 flex-col gap-1 rounded-2xl border border-emerald-100 bg-white p-3 shadow-lg md:static md:flex md:flex-row md:items-center md:border-0 md:bg-transparent md:p-0 md:shadow-none`}>
            {links.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={() => setOpen(false)}
                className={({ isActive }) =>
                  `rounded-full px-3 py-2 text-sm font-medium ${isActive ? "bg-emerald-100 text-emerald-950" : "text-emerald-900 hover:bg-emerald-100/70"}`
                }
              >
                {link.label}
              </NavLink>
            ))}
            <NavLink to="/apply" className={btn} onClick={() => setOpen(false)}>
              Start a request
            </NavLink>
          </nav>
        </div>
      </header>
      <main>
        <Outlet />
      </main>
      <footer className="mx-auto mt-8 w-full max-w-6xl border-t border-emerald-100 px-5 py-8 text-sm text-emerald-900/70">
        <strong className="text-emerald-950">Homeward</strong>
        <p className="mt-2">A mock loan desk for Indians living abroad. Not a bank, not a sanction, and not affiliated with Loans Got Easy.</p>
        <p className="mt-2">
          Rates are invented illustrations for this demo, loosely shaped like published Indian home-loan ranges. A real lender prices your file. EMI is paid from an NRE or NRO account. Agricultural land is not financed.
        </p>
      </footer>
    </div>
  );
}
