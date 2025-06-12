// src/pages/Home.tsx
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="text-white text-center mt-20 px-4">
      <h1 className="text-3xl font-bold mb-4">
        Welcome to GOGO tracker by H3ZZ
      </h1>
      <p className="text-sm text-gray-400 mb-8">
        The website is currently under development. Please check back later for
        more updates.
      </p>
      <div className="flex justify-center gap-4">
        <Link
          to="/search"
          className="bg-orange-500 text-black px-4 py-2 rounded hover:bg-orange-400"
        >
          Search Player
        </Link>
        <Link
          to="/GogoTracker"
          className="bg-gray-700 text-white px-4 py-2 rounded hover:bg-gray-600"
        >
          GOGO Tracker
        </Link>
      </div>
    </div>
  );
}
