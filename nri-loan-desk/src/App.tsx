import { Route, Routes } from "react-router-dom";
import { Layout } from "./components/Layout";
import { ApplicationPage } from "./pages/ApplicationPage";
import { ApplyPage } from "./pages/ApplyPage";
import { HomePage } from "./pages/HomePage";
import { LoansPage } from "./pages/LoansPage";
import { TrackPage } from "./pages/TrackPage";

export function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<HomePage />} />
        <Route path="loans" element={<LoansPage />} />
        <Route path="apply" element={<ApplyPage />} />
        <Route path="track" element={<TrackPage />} />
        <Route path="track/:reference" element={<ApplicationPage />} />
      </Route>
    </Routes>
  );
}
