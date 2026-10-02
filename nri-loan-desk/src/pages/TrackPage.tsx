import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { inr } from "../lib/money";
import { findApplication, loadApplications, loanName, saveApplications, statusLabel } from "../lib/store";
import { btn, field, h1, kicker, label, lede, page, pill } from "../ui";

export function TrackPage() {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [missing, setMissing] = useState(false);
  const [files, setFiles] = useState(() => loadApplications());

  function onSearch(event: FormEvent) {
    event.preventDefault();
    const found = findApplication(query);
    if (!found) {
      setMissing(true);
      return;
    }
    navigate(`/track/${found.reference}`);
  }

  function resetSamples() {
    localStorage.removeItem("homeward.applications.v1");
    const fresh = loadApplications();
    saveApplications(fresh);
    setFiles(fresh);
    setMissing(false);
  }

  return (
    <section className={page}>
      <p className={kicker}>Track</p>
      <h1 className={h1}>Where is the file?</h1>
      <p className={lede}>
        Sample references already on this browser: HW-24018, HW-23880, HW-24102. A request you submit is stored here too.
      </p>
      <form className="mt-6 flex max-w-xl items-end gap-3" onSubmit={onSearch}>
        <label className={`${label} flex-1`}>
          Reference
          <input className={field} value={query} onChange={(event) => { setQuery(event.target.value); setMissing(false); }} placeholder="HW-24018" />
        </label>
        <button className={btn} type="submit">Open</button>
      </form>
      {missing && <p className="mt-3 rounded-xl bg-red-50 px-3 py-2 text-sm text-red-800">No file matches {query.trim() || "that reference"}.</p>}

      <div className="mt-6 grid gap-3">
        {files.map((file) => (
          <Link key={file.reference} to={`/track/${file.reference}`} className="flex items-center justify-between gap-4 rounded-2xl border border-emerald-100 bg-white px-5 py-4 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md">
            <div>
              <strong className="text-lg">{file.reference}</strong>
              <p className="text-sm text-emerald-800/80">{file.fullName} · {loanName(file.loanType)} · {file.cityInIndia}</p>
            </div>
            <div className="text-right">
              <span className={pill(file.status)}>{statusLabel(file.status)}</span>
              <p className="mt-1 text-sm text-emerald-800">{inr(file.amountInr)}</p>
            </div>
          </Link>
        ))}
      </div>
      <button type="button" className="mt-4 text-sm font-medium text-emerald-800 underline" onClick={resetSamples}>
        Restore the three sample files
      </button>
    </section>
  );
}
