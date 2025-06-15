import { useState } from "react";
import { fetchSharedStats } from "../api/api";
import type { SharedStatsResponse } from "../types/SharedStatsResponse";

export default function SharedStatsPage() {
  const [nicknames, setNicknames] = useState(["", ""]);
  const [data, setData] = useState<SharedStatsResponse | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (nicknames.length < 2) return alert("Select 2–5 players");
    const res = await fetchSharedStats(nicknames);
    setData(res);
  };

  return (
    <div className="text-white">
      <h1 className="text-2xl font-bold mb-4">Shared Stats Comparison</h1>
      <form onSubmit={handleSubmit} className="space-y-6 mb-6 max-w-xl">
        {/* Selector for how many players */}
        <div className="flex items-center gap-3">
          <label htmlFor="playerCount" className="font-medium">
            Number of players:
          </label>
          <select
            id="playerCount"
            value={nicknames.length}
            onChange={(e) => {
              const count = parseInt(e.target.value);
              const updated = [...nicknames];
              while (updated.length < count) updated.push("");
              while (updated.length > count) updated.pop();
              setNicknames(updated);
            }}
            className="bg-[#1e1e1e] border border-gray-600 rounded px-3 py-2"
          >
            {[2, 3, 4, 5].map((count) => (
              <option key={count} value={count}>
                {count} Players
              </option>
            ))}
          </select>
        </div>

        {/* Inputs for player nicknames */}
        <div className="space-y-3">
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
              placeholder={`Player ${idx + 1} nickname`}
              className="bg-[#1e1e1e] border border-gray-600 rounded px-3 py-2 w-full"
            />
          ))}
        </div>

        <button
          type="submit"
          disabled={nicknames.filter((n) => n.trim()).length < 2}
          className="bg-orange-500 hover:bg-orange-600 px-6 py-2 rounded text-white w-full"
        >
          Compare
        </button>
      </form>

      {data && (
        <div>
          <h2 className="text-xl font-semibold mb-2">Shared Stats</h2>
          <p>Matches Played Together: {data.matchCount}</p>
          <p>Avg K/D: {data.global.avgKd}</p>
          <p>Winrate: {data.global.winrate}%</p>
          <p>ELO Gained: {data.global.totalEloGain}</p>

          <table className="mt-4 w-full text-left border border-gray-700">
            <thead>
              <tr className="bg-[#2a2a2a]">
                <th className="p-2">Nickname</th>
                <th className="p-2">Matches</th>
                <th className="p-2">Winrate</th>
                <th className="p-2">K/D</th>
                <th className="p-2">KR</th>
                <th className="p-2">ADR</th>
                <th className="p-2">ELO Δ</th>
              </tr>
            </thead>
            <tbody>
              {data.perPlayer.map((p) => (
                <tr key={p.nickname}>
                  <td className="p-2">{p.nickname}</td>
                  <td className="p-2">{p.matches}</td>
                  <td className="p-2">{p.winrate}%</td>
                  <td className="p-2">{p.avgKd}</td>
                  <td className="p-2">{p.avgKr}</td>
                  <td className="p-2">{p.avgAdr}</td>
                  <td className="p-2">{p.totalEloGain}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
