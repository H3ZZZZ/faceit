// src/App.tsx
import { Routes, Route } from "react-router-dom";
import GogoTracker from "./pages/GogoTracker";
import Home from "./pages/Home";
import Navbar from "./components/Navbar";
import IconGallery from "./pages/IconGallery";

export default function App() {
  return (
    <main className=""> 
      <Navbar />
      <div className="px-6 pb-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/GogoTracker" element={<GogoTracker />} />
          <Route path="/icons" element={<IconGallery />} />
        </Routes>
      </div>
    </main>
  );
}