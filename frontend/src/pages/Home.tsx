import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

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
    <div className="mt-12 px-4 text-center text-white sm:mt-20">
      <h1 className="mb-4 text-3xl font-bold">Welcome to GOGO Tracker by H3ZZ</h1>
      <p className="mb-8 text-sm text-gray-400">
        This site provides FACEIT stats, tools, and trackers all in one place.
      </p>

      <div className="mx-auto mb-10 flex max-w-md items-center justify-center gap-2">
        <input
          type="text"
          placeholder="Enter FACEIT nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          onKeyDown={handleKeyPress}
          className="w-full rounded-lg border border-gray-300 bg-white px-4 py-2 text-black placeholder-gray-600 shadow-sm focus:outline-none focus:ring-2 focus:ring-orange-500"
        />

        <button
          onClick={handleSearch}
          className="rounded bg-orange-500 px-4 py-2 font-medium text-black hover:bg-orange-600"
        >
          Search
        </button>
      </div>

      <p>Check out some of the other pages:</p>
      <div className="mx-auto mt-5 grid max-w-4xl grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <Link
          to="/gogo-lan"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          GOGO LAN
        </Link>

        <Link
          to="/advanced"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          Custom Squad
        </Link>

        <Link
          to="/shared-stats"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          Played Together
        </Link>

        <Link
          to="/gogo-tracker"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          GOGO Tracker
        </Link>

        <Link
          to="/sladesh-tracker"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          Sladesh Tracker
        </Link>

        <Link
          to="/icons"
          className="rounded bg-gray-700 px-6 py-3 font-medium text-white hover:bg-gray-600"
        >
          Icon List
        </Link>
      </div>

      <p className="mt-8 text-sm text-gray-400">
        If the features don't work, the server might be down.
      </p>
    </div>
  );
}

