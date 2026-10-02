import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { inr } from "../lib/money";
import { findApplication, loadApplications, loanName, saveApplications, statusLabel } from "../lib/store";

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
    <section className="page">
      <p className="kicker">Track</p>
      <h1>Where is the file?</h1>
      <p className="lede narrow">
        Sample references already on this browser: HW-24018, HW-23880, HW-24102. A request you submit is stored here too.
      </p>
      <form className="search" onSubmit={onSearch}>
        <label>
          Reference
          <input value={query} onChange={(event) => { setQuery(event.target.value); setMissing(false); }} placeholder="HW-24018" />
        </label>
        <button className="button" type="submit">Open</button>
      </form>
      {missing && <p className="errors">No file matches {query.trim() || "that reference"}.</p>}

      <div className="file-list">
        {files.map((file) => (
          <Link key={file.reference} to={`/track/${file.reference}`} className="file-row">
            <div>
              <strong>{file.reference}</strong>
              <p>
                {file.fullName} · {loanName(file.loanType)} · {file.cityInIndia}
              </p>
            </div>
            <div>
              <span className={`pill pill-${file.status}`}>{statusLabel(file.status)}</span>
              <p>{inr(file.amountInr)}</p>
            </div>
          </Link>
        ))}
      </div>
      <button type="button" className="text-button" onClick={resetSamples}>
        Restore the three sample files
      </button>
    </section>
  );
}
