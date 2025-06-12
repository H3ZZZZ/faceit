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
      <h1 className="text-2xl font-bold mb-4 text-center">Search Faceit Player</h1>
      <form onSubmit={handleSubmit} className="flex gap-2">
        <input
          type="text"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          placeholder="Enter nickname"
          className="w-full px-3 py-2 rounded bg-[#222] text-white border border-gray-600"
        />
        <button
          type="submit"
          className="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
        >
          Search
        </button>
      </form>
    </div>
  );
}
