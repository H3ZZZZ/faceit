import { useState } from "react";
import { fetchSharedStats } from "../api/api";
import type { SharedPlayerStatsDTO } from "../types/SharedStatsResponse";
import SharedPlayerCard from "../components/SharedPlayerCard";
import SharedMapComparisonGrid from "../components/SharedMapComparisonGrid";

export default function SharedStatsPage() {
  const [playerCount, setPlayerCount] = useState(2);
  const [nicknames, setNicknames] = useState<string[]>(["", ""]);
  const [players, setPlayers] = useState<SharedPlayerStatsDTO[]>([]);
  const [loading, setLoading] = useState(false);

  const handleCountChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const count = parseInt(e.target.value);
    setPlayerCount(count);

    const updated = [...nicknames];
    while (updated.length < count) updated.push("");
    while (updated.length > count) updated.pop();
    setNicknames(updated);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (nicknames.filter((n) => n.trim()).length < 2) {
      alert("Select 2–5 players");
      return;
    }

    setLoading(true);
    try {
      const res = await fetchSharedStats(nicknames);
      setPlayers(res);
    } catch (err) {
      alert("Failed to fetch shared stats");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="text-white">
      <h1 className="text-2xl font-bold mb-4">Shared Stats Comparison</h1>

      <form onSubmit={handleSubmit} className="space-y-4 mb-6">
        <div className="flex gap-4 items-center">
          <label htmlFor="playerCount" className="text-gray-300">
            Number of players:
          </label>
          <select
            id="playerCount"
            value={playerCount}
            onChange={handleCountChange}
            className="bg-[#1e1e1e] border border-gray-600 rounded px-3 py-2 text-white"
          >
            {[2, 3, 4, 5].map((n) => (
              <option key={n} value={n}>
                {n}
              </option>
            ))}
          </select>
        </div>

        <div className="space-y-2">
          {nicknames.map((name, idx) => (
            <input
              key={idx}
              type="text"
              value={name}
              onChange={(e) => {
                const updated = [...nicknames];
                updated[idx] = e.target.value;
                setNicknames(updated);
              }}
              placeholder={`Nickname #${idx + 1}`}
              className="bg-[#1e1e1e] border border-gray-600 rounded px-3 py-2 w-full"
            />
          ))}
        </div>

        <button
          type="submit"
          disabled={loading || nicknames.filter((n) => n.trim()).length < 2}
          className="bg-orange-500 hover:bg-orange-600 px-6 py-2 rounded text-white"
        >
          {loading ? "Fetching..." : "Compare"}
        </button>

        <p className="text-red-400 text-sm mt-2">
          Note: ELO calculation may not be 100% accurate due to smurf accounts,
          ELO resets, and occasional faulty data from FACEIT matchrooms. Use this as an estimate only.
          <br />
          All other stats are accurate.  
        </p>
      </form>

      {players.length > 0 && (
        <>
          <h2 className="text-2xl font-bold mb-6">
            Overall Stats Played Together
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 mb-10">
            {players.map((player) => (
              <SharedPlayerCard key={player.nickname} player={player} />
            ))}
          </div>

          <SharedMapComparisonGrid players={players} />
        </>
      )}
    </div>
  );
}
