import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Home() {
  const [nickname, setNickname] = useState("");
  const navigate = useNavigate();

  const handleSearch = () => {
    if (nickname.trim()) {
      navigate(`/player/${nickname.trim()}`);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <div className="text-white text-center mt-20 px-4">
      <h1 className="text-3xl font-bold mb-4">
        Welcome to GOGO Tracker by H3ZZ
      </h1>
      <p className="text-sm text-gray-400 mb-8">
        This site provides FACEIT stats, tools, and trackers – all in one place.
      </p>

      {/* Search Input */}
      <div className="flex justify-center items-center gap-2 mb-10 max-w-md mx-auto">
        <input
          type="text"
          placeholder="Enter FACEIT nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          onKeyDown={handleKeyPress}
          className="w-full px-4 py-2 rounded-lg bg-white text-black placeholder-gray-600 border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-orange-500"
        />

        <button
          onClick={handleSearch}
          className="bg-orange-500 text-black px-4 py-2 rounded hover:bg-orange-600 font-medium"
        >
          Search
        </button>
      </div>

      {/* Navigation Grid */}
      <p>Check out some of the other pages:</p>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 max-w-3xl mx-auto mt-5">
        <Link
          to="/shared-stats"
          className="bg-gray-700 text-white px-6 py-3 rounded hover:bg-gray-600 font-medium"
        >
          🤝 Played Together
        </Link>

        <Link
          to="/gogo-tracker"
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
        If the features don't work, the server might be down.
      </p>
    </div>
  );
}
