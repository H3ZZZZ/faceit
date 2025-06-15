// src/pages/Home.tsx
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="text-white text-center mt-20 px-4">
      <h1 className="text-3xl font-bold mb-4">
        Welcome to GOGO Tracker by H3ZZ
      </h1>
      <p className="text-sm text-gray-400 mb-8">
        This site provides FACEIT stats, tools, and trackers - all in one place.
      </p>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 max-w-3xl mx-auto">
        <Link
          to="/search"
          className="bg-orange-500 text-black px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          🔍 Search Player
        </Link>

        <Link
          to="/shared-stats"
          className="bg-gray-700 text-white px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          🤝 Played Together
        </Link>

        <Link
          to="/GogoTracker"
          className="bg-gray-700 text-white px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          📊 GOGO Tracker
        </Link>

        <Link
          to="/sladesh-tracker"
          className="bg-gray-700 text-white px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          🕵️ Sladesh Tracker
        </Link>

        <Link
          to="/icons"
          className="bg-gray-700 text-white px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          ⭐ Icon List
        </Link>
      </div>
      <p className="text-sm text-gray-400 mt-8">
        If the features doesn't work, the server might be down.
      </p>
    </div>
  );
}
