// src/components/Navbar.tsx
import { Link, useLocation } from "react-router-dom";

const pageTitles: Record<string, string> = {
  "/": "Home",
  "/search": "Search",
  "/GogoTracker": "GOGO Tracker",
  "/icons": "Icon Gallery",
  "/player": "Player Stats", // for dynamic player pages
};

export default function Navbar() {
  const { pathname } = useLocation();

  // Match exact or prefix path (e.g., /player/Skejs)
  const currentTitle =
    Object.entries(pageTitles).find(
      ([key]) => pathname === key || pathname.startsWith(key + "/")
    )?.[1] || "";

  return (
    <nav className="px-6 py-4 flex items-center justify-between text-white">
      {/* Left: Links */}
      <div className="flex gap-4 text-sm">
        <Link
          to="/"
          className={
            pathname === "/" ? "text-orange-400" : "hover:text-orange-400"
          }
        >
          Home
        </Link>
        <Link
          to="/search"
          className={
            pathname === "/search" ? "text-orange-400" : "hover:text-orange-400"
          }
        >
          Search
        </Link>
        <Link
          to="/GogoTracker"
          className={
            pathname === "/GogoTracker"
              ? "text-orange-400"
              : "hover:text-orange-400"
          }
        >
          GOGO Tracker
        </Link>
        <Link
          to="/icons"
          className={
            pathname === "/icons" ? "text-orange-400" : "hover:text-orange-400"
          }
        >
          Icons
        </Link>
      </div>
    </nav>
  );
}
