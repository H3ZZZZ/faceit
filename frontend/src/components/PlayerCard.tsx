import type { PlayerStats, StatSegment } from "../types/PlayerStats";
import { getPlayerIcons } from "../utils/icons";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
dayjs.extend(relativeTime);

type Props = PlayerStats & {
  segmentKey: "last10" | "last30" | "last50" | "last100";
};

export default function PlayerCard({ segmentKey, ...player }: Props) {
  const segment: StatSegment = player[segmentKey];

  const icons = getPlayerIcons({
    averageKr: segment.averageKr,
    averageKd: segment.averageKd,
    winRate: segment.winRate,
    averageHsPercent: segment.averageHsPercent,
    winstreak: segment.winStreakCount,
  });

  return (
    <div className="bg-[#1e1e1e] text-white p-4 rounded-xl shadow-md transition hover:shadow-lg hover:scale-[1.015] flex flex-col gap-3 relative">
      {/* Icons (Top Right) */}
      <div className="absolute top-3 right-2 flex gap-1 text-xl items-end">
        {icons.map((icon, i) => (
          <img
            key={i}
            src={`/icons/${icon}.png`}
            alt={icon}
            className="w-8 h-8 object-contain"
          />
        ))}
      </div>

      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <img
            src={player.avatar}
            alt="avatar"
            className="w-10 h-10 rounded-full"
          />
          <div>
            <div className="flex items-center gap-2">
              <h2 className="font-semibold text-lg text-orange-400">
                {player.nickname}
              </h2>
              <img
                src={`https://flagcdn.com/24x18/${player.country.toLowerCase()}.png`}
                alt={player.country}
                className="w-4 h-3 object-cover rounded-sm mt-[1px]"
              />
            </div>
            <div className="flex items-center gap-2 text-xs text-gray-400">
              <span>ELO: {player.faceitElo}</span>
              <img
                src={`/faceit-levels/${player.skillLevel}.png`}
                alt={`Level ${player.skillLevel}`}
                className="w-5 h-5 object-contain"
              />
            </div>
          </div>
        </div>
      </div>

      {/* Divider */}
      <div className="border-t border-gray-700" />

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
        <div>HS: {segment.averageHsPercent}%</div>
        <div>
          K/A/D: {segment.kavg} / {segment.aavg} / {segment.davg}
        </div>
      </div>

      {/* Last 5 results */}
      <div className="flex items-center justify-between">
        <div className="flex gap-1">
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
        <div className="text-xs text-gray-400 whitespace-nowrap mr-26">
          Last played: {dayjs(player.lastActive).fromNow()}
        </div>
      </div>
    </div>
  );
}
