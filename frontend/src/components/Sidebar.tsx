import { Link, useLocation, useNavigate } from "react-router-dom";
import {
  FaHome,
  FaSearch,
  FaChartLine,
  FaStar,
  FaUserSecret,
} from "react-icons/fa";

export default function Sidebar() {
  const { pathname } = useLocation();

  const navLink = (to: string, label: string, Icon: React.ElementType) => (
    <Link
      to={to}
      className={`flex items-center gap-2 px-4 py-2 rounded hover:bg-[#2a2a2a] transition w-full text-left ${
        pathname === to ? "bg-[#2a2a2a] text-orange-400" : "text-white"
      }`}
    >
      <Icon size={16} />
      {label}
    </Link>
  );

  return (
    <aside className="bg-[#121212] w-52 min-h-screen flex flex-col items-center py-6 gap-4 fixed left-0 top-0">
      {/* Logo */}
      <Link to="/">
        <img
          src="/nyGOGO_transparent.png"
          alt="GoGo logo"
          className="w-10 mb-4"
        />
      </Link>

      {/* Navigation */}
      <div className="flex flex-col items-start w-full px-4 mt-4 space-y-1">
        {navLink("/", "Home", FaHome)}
        {navLink("/search", "Search", FaSearch)}
        {navLink("/GogoTracker", "GOGO Tracker", FaChartLine)}
        {navLink("/icons", "Icons", FaStar)}
        {navLink("/sladesh-tracker", "Sladesh Tracker", FaUserSecret)}
      </div>
    </aside>
  );
}
