import type { PlayerStats, StatSegment } from "../types/PlayerStats";
import { getPlayerIcons } from "../utils/icons";
import { useState } from "react";

export default function PlayerCard(player: PlayerStats) {
  const [segmentKey, setSegmentKey] = useState<
    "last10" | "last30" | "last50" | "last100"
  >("last30");
  const segment: StatSegment = player[segmentKey];

  const icons = getPlayerIcons({
    averageKr: segment.averageKr,
    averageKd: segment.averageKd,
    winRate: segment.winRate,
    averageHsPercent: segment.averageHsPercent,
    winstreak: segment.winStreakCount,
  });

  const segmentOptions = ["last10", "last30", "last50", "last100"] as const;

  return (
    <div className="relative bg-[#1e1e1e] text-white p-4 rounded-xl shadow-md hover:scale-[1.02] transition flex flex-col gap-3">
      {/* Icons */}
      <div className="absolute top-2 right-2 flex gap-1 text-xl">
        {icons.map((icon, i) => (
          <span key={i}>{icon}</span>
        ))}
      </div>

      {/* Header */}
      <div className="flex items-center gap-4">
        <img
          src={player.avatar}
          alt="avatar"
          className="w-12 h-12 rounded-full"
        />
        <div>
          <div className="flex items-center gap-2">
            <h2 className="text-xl font-semibold">{player.nickname}</h2>
            <img
              src={`https://flagcdn.com/24x18/${player.country.toLowerCase()}.png`}
              alt={player.country}
              className="w-6 h-4 object-cover rounded-sm"
            />
            <div className="w-6 h-6 bg-orange-500 text-black text-xs font-bold flex items-center justify-center rounded-full">
              {player.skillLevel}
            </div>
          </div>
          <div className="text-sm text-orange-400">ELO: {player.faceitElo}</div>
        </div>
      </div>

      {/* Segment buttons */}
      <div className="flex gap-2">
        {segmentOptions.map((key) => (
          <button
            key={key}
            className={`text-xs px-2 py-1 rounded ${
              key === segmentKey ? "bg-orange-500 text-black" : "bg-gray-700"
            }`}
            onClick={() => setSegmentKey(key)}
          >
            {key.replace("last", "Last ")}
          </button>
        ))}
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 text-sm gap-y-1">
        <div>
          Wins/Losses: <span className="text-green-400">{segment.wins}</span> /{" "}
          <span className="text-red-400">{segment.losses}</span>
        </div>
        <div>Win Rate: {segment.winRate}%</div>
        <div>
          Elo Δ:{" "}
          <span
            className={
              segment.eloChange >= 0 ? "text-green-400" : "text-red-400"
            }
          >
            {segment.eloChange >= 0 ? "+" : ""}
            {segment.eloChange}
          </span>
        </div>
        <div>K/D: {segment.averageKd.toFixed(2)}</div>
        <div>K/R: {segment.averageKr.toFixed(2)}</div>
        <div>ADR: {segment.averageAdr.toFixed(1)}</div>
        <div>HS%: {segment.averageHsPercent}%</div>
        <div>
          K/A/D: {segment.kavg} / {segment.aavg} / {segment.davg}
        </div>
      </div>

      {/* Last 5 results */}
      <div className="flex gap-1 mt-2">
        {segment.last5Results.map((r, i) => (
          <span
            key={i}
            className={`px-2 py-0.5 text-xs font-bold rounded ${
              r === "W" ? "bg-green-600" : "bg-red-600"
            }`}
          >
            {r}
          </span>
        ))}
      </div>
    </div>
  );
}
