// src/components/Navbar.tsx
import { Link, useLocation } from 'react-router-dom';

const pageTitles: Record<string, string> = {
  '/': 'Home',
  '/GogoTracker': 'GOGO Tracker',
};

export default function Navbar() {
  const { pathname } = useLocation();
  const currentTitle = pageTitles[pathname] || '';

  return (
    <nav className="flex items-center justify-between mb-6 text-white">
      {/* Left: Links */}
      <div className="flex gap-4 text-sm">
        <Link
          to="/"
          className={pathname === '/' ? 'text-orange-400' : 'hover:text-orange-400'}
        >
          Home
        </Link>
        <Link
          to="/GogoTracker"
          className={pathname === '/GogoTracker' ? 'text-orange-400' : 'hover:text-orange-400'}
        >
          GOGO Tracker
        </Link>
      </div>

      {/* Center: Page title */}
      <div className="absolute left-1/2 transform -translate-x-1/2 text-xl font-bold text-white">
        {currentTitle}
      </div>

      {/* Right: empty to balance flex layout */}
      <div className="w-24" />
    </nav>
  );
}
