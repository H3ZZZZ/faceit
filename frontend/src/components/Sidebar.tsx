import { useEffect, useMemo, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import type { IconType } from "react-icons";
import {
  FaBars,
  FaChartLine,
  FaHome,
  FaSearch,
  FaSlidersH,
  FaStar,
  FaTimes,
  FaTrophy,
  FaUserSecret,
  FaUsers,
} from "react-icons/fa";

type NavItem = {
  to: string;
  label: string;
  icon: IconType;
};

const NAV_ITEMS: NavItem[] = [
  { to: "/", label: "Home", icon: FaHome },
  { to: "/gogo-tracker", label: "GOGO Tracker", icon: FaChartLine },
  { to: "/gogo-lan", label: "GOGO LAN", icon: FaTrophy },
  { to: "/advanced", label: "Custom Squad", icon: FaSlidersH },
  { to: "/sladesh-tracker", label: "Sladesh Tracker", icon: FaUserSecret },
  { to: "/shared-stats", label: "Played Together", icon: FaUsers },
  { to: "/search", label: "Search Player", icon: FaSearch },
  { to: "/icons", label: "Icons", icon: FaStar },
];

function SidebarNav({
  pathname,
  onNavigate,
}: {
  pathname: string;
  onNavigate?: () => void;
}) {
  const navLinks = useMemo(
    () =>
      NAV_ITEMS.map(({ to, label, icon: Icon }) => {
        const active = pathname === to;

        return (
          <Link
            key={to}
            to={to}
            onClick={onNavigate}
            className={`flex w-full items-center gap-3 rounded-xl px-4 py-2.5 text-left transition ${
              active
                ? "bg-[#2a2a2a] text-orange-400"
                : "text-white hover:bg-[#2a2a2a]"
            }`}
          >
            <Icon size={16} />
            <span>{label}</span>
          </Link>
        );
      }),
    [onNavigate, pathname],
  );

  return <div className="flex flex-col items-start gap-1">{navLinks}</div>;
}

export default function Sidebar() {
  const { pathname } = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    setMobileOpen(false);
  }, [pathname]);

  return (
    <>
      <header className="fixed inset-x-0 top-0 z-40 flex items-center justify-between border-b border-white/8 bg-[#121212]/95 px-4 py-3 backdrop-blur lg:hidden">
        <Link to="/" className="flex items-center gap-3">
          <img
            src="/nyGOGO_transparent.png"
            alt="GoGo logo"
            className="h-9 w-9 object-contain"
          />
          <div>
            <div className="text-sm font-semibold text-white">GOGO Tracker</div>
            <div className="text-[10px] uppercase tracking-[0.22em] text-[#8d8d8d]">
              Navigation
            </div>
          </div>
        </Link>
        <button
          type="button"
          onClick={() => setMobileOpen((open) => !open)}
          className="rounded-xl border border-white/10 bg-white/5 p-2 text-white transition hover:bg-white/10"
          aria-label={mobileOpen ? "Close menu" : "Open menu"}
          aria-expanded={mobileOpen}
        >
          {mobileOpen ? <FaTimes size={18} /> : <FaBars size={18} />}
        </button>
      </header>

      <div
        className={`fixed inset-0 z-30 bg-black/55 transition lg:hidden ${
          mobileOpen ? "pointer-events-auto opacity-100" : "pointer-events-none opacity-0"
        }`}
        onClick={() => setMobileOpen(false)}
        aria-hidden="true"
      />

      <aside
        className={`fixed inset-y-0 left-0 z-40 flex w-72 max-w-[82vw] flex-col border-r border-white/8 bg-[#121212] px-4 py-6 transition-transform lg:hidden ${
          mobileOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <Link to="/" className="flex items-center gap-3 px-2">
          <img
            src="/nyGOGO_transparent.png"
            alt="GoGo logo"
            className="h-10 w-10 object-contain"
          />
          <div>
            <div className="text-sm font-semibold text-white">GOGO Tracker</div>
            <div className="text-[10px] uppercase tracking-[0.22em] text-[#8d8d8d]">
              Menu
            </div>
          </div>
        </Link>

        <div className="mt-6">
          <SidebarNav pathname={pathname} onNavigate={() => setMobileOpen(false)} />
        </div>
      </aside>

      <aside className="fixed left-0 top-0 hidden min-h-screen w-52 flex-col items-center gap-4 bg-[#121212] py-6 lg:flex">
        <Link to="/">
          <img
            src="/nyGOGO_transparent.png"
            alt="GoGo logo"
            className="w-10"
          />
        </Link>

        <div className="mt-4 w-full px-4">
          <SidebarNav pathname={pathname} />
        </div>
      </aside>
    </>
  );
}

