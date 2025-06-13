import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function SearchPage() {
  const [nickname, setNickname] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (nickname.trim()) {
      navigate(`/player/${nickname.trim()}`);
    }
  };

  return (
    <div className="text-white max-w-md mx-auto mt-20 px-4">
      <h1 className="text-3xl font-bold mb-4 text-center text-orange-400">
        Search Faceit Player
      </h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-2">
        <input
          type="text"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          placeholder="Enter exact nickname"
          className="w-full px-3 py-2 rounded bg-[#1e1e1e] text-white border border-gray-600 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-400"
        />
        <p className="text-xs text-gray-400">
          Nickname is <span className="text-orange-300">case-sensitive</span>. Type it exactly as on Faceit.
        </p>
        <button
          type="submit"
          className="mt-2 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
        >
          Search
        </button>
      </form>
    </div>
  );
}
