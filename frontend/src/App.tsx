import { Routes, Route } from "react-router-dom";
import GogoTracker from "./pages/GogoTracker";
import Home from "./pages/Home";
import Navbar from "./components/Navbar";
import IconGallery from "./pages/IconGallery";
import SearchPage from "./pages/SearchPage";
import PlayerDetails from "./pages/PlayerDetails";

export default function App() {
  return (
    <main>
      <Navbar />
      <div className="px-6 pb-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/GogoTracker" element={<GogoTracker />} />
          <Route path="/icons" element={<IconGallery />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/player/:nickname" element={<PlayerDetails />} />
        </Routes>
      </div>
    </main>
  );
}
