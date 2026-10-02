import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";

const links = [
  { to: "/loans", label: "Loans" },
  { to: "/apply", label: "Apply" },
  { to: "/track", label: "Track" },
];

export function Layout() {
  const [open, setOpen] = useState(false);

  return (
    <div className="shell">
      <header className="top">
        <NavLink to="/" className="mark" onClick={() => setOpen(false)}>
          <span className="mark-seal" aria-hidden="true" />
          Homeward
        </NavLink>
        <button className="menu" type="button" aria-expanded={open} onClick={() => setOpen((value) => !value)}>
          {open ? "Close" : "Menu"}
        </button>
        <nav className={open ? "nav open" : "nav"}>
          {links.map((link) => (
            <NavLink key={link.to} to={link.to} onClick={() => setOpen(false)}>
              {link.label}
            </NavLink>
          ))}
          <NavLink to="/apply" className="nav-cta" onClick={() => setOpen(false)}>
            Start a request
          </NavLink>
        </nav>
      </header>
      <main>
        <Outlet />
      </main>
      <footer className="foot">
        <div>
          <strong>Homeward</strong>
          <p>A mock loan desk for Indians living abroad. Not a bank, not a sanction, and not affiliated with Loans Got Easy.</p>
        </div>
        <p className="fine">
          Rates are invented illustrations for this demo, loosely shaped like published Indian home-loan ranges. A real lender prices your file. EMI is paid from an NRE or NRO account. Agricultural land is not financed.
        </p>
      </footer>
    </div>
  );
}
