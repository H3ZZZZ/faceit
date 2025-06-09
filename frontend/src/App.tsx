// src/App.tsx
import { Routes, Route } from 'react-router-dom';
import GogoTracker from './pages/GogoTracker';
import Home from './pages/Home';
import Navbar from './components/Navbar';

export default function App() {
  return (
    <main className="bg-[#121212] min-h-screen p-6">
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/GogoTracker" element={<GogoTracker />} />
      </Routes>
    </main>
  );
}
