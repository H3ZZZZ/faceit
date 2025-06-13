import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Home from "./pages/Home";
import GogoTracker from "./pages/GogoTracker";
import IconGallery from "./pages/IconGallery";
import PlayerDetails from "./pages/PlayerDetails";
import SearchPage from "./pages/SearchPage";
import SladeshTracker from "./pages/SladeshTracker";

function App() {
  return (
    <div className="flex">
      <Sidebar />
      <main className="ml-52 w-full bg-[#181818] min-h-screen px-6 py-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/GogoTracker" element={<GogoTracker />} />
          <Route path="/icons" element={<IconGallery />} />
          <Route path="/player/:nickname" element={<PlayerDetails />} />
          <Route path="/sladesh-tracker" element={<SladeshTracker />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
