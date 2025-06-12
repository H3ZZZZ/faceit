import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchPlayerByNickname } from "../api/api";
import type { PlayerStatsFull } from "../types/PlayerStatsFull";

export default function PlayerDetails() {
  const { nickname } = useParams();
  const [data, setData] = useState<PlayerStatsFull | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!nickname) return;

    fetchPlayerByNickname(nickname)
      .then(setData)
      .catch(() => setError("Player not found or error loading data."));
  }, [nickname]);

  if (error) return <div className="text-red-500 p-4">{error}</div>;
  if (!data) return <div className="text-white p-4">Loading...</div>;

  return (
    <div className="text-white max-w-4xl mx-auto mt-10 p-4 bg-[#1e1e1e] rounded-lg">
      <div className="flex items-center gap-4 mb-4">
        <img
          src={data.avatar}
          alt="avatar"
          className="w-14 h-14 rounded-full border"
        />
        <div>
          <h2 className="text-xl font-bold">{data.nickname}</h2>
          <p className="text-sm text-gray-400">
            ELO: {data.faceitElo} (lvl {data.skillLevel})
          </p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 text-sm">
        <p>Matches: {data.matchesPlayed}</p>
        <p>Winrate: {data.winrate}%</p>
        <p>K/D: {data.avgKd.toFixed(2)}</p>
        <p>K/R: {data.avgKr.toFixed(2)}</p>
        <p>ADR: {data.avgAdr.toFixed(1)}</p>
        <p>HS%: {data.avgHsPercent}%</p>
        <p>K/A/D: {data.kavg} / {data.aavg} / {data.davg}</p>
        <p>Elo Δ: {data.eloChange >= 0 ? "+" : ""}{data.eloChange}</p>
      </div>

      <div className="mt-6">
        <h3 className="text-lg font-semibold mb-2">Last 5 Results</h3>
        <div className="flex gap-1">
          {data.last5Results.map((r, i) => (
            <span
              key={i}
              className={`px-2 py-1 text-xs rounded font-bold ${
                r === "W" ? "bg-green-600" : "bg-red-600"
              }`}
            >
              {r}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}
