import { Route, Routes } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import GogoLanPage from "./pages/GogoLanPage";
import GogoTracker from "./pages/GogoTracker";
import Home from "./pages/Home";
import IconGallery from "./pages/IconGallery";
import PlayerDetails from "./pages/PlayerDetails";
import SearchPage from "./pages/SearchPage";
import SharedStatsPage from "./pages/SharedStatsPage";
import SladeshTracker from "./pages/SladeshTracker";

function App() {
  return (
    <div className="flex bg-[#181818]">
      <Sidebar />
      <main className="min-h-screen w-full bg-[#181818] px-4 pb-6 pt-20 lg:ml-52 lg:px-6 lg:py-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/gogo-tracker" element={<GogoTracker />} />
          <Route path="/icons" element={<IconGallery />} />
          <Route path="/player/:nickname" element={<PlayerDetails />} />
          <Route path="/sladesh-tracker" element={<SladeshTracker />} />
          <Route path="/shared-stats" element={<SharedStatsPage />} />
          <Route path="/gogo-lan" element={<GogoLanPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
