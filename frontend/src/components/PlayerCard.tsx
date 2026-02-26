import type { PlayerStats, StatSegment } from "../types/PlayerStats";
import { getPlayerIcons, getIconRule } from "../utils/icons";
import { Link } from "react-router-dom";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
dayjs.extend(relativeTime);

type Props = PlayerStats & {
  segmentKey: "last10" | "last30" | "last50" | "last100";
};

export default function PlayerCard({ segmentKey, ...player }: Props) {
  const segment: StatSegment = player[segmentKey];
  if (!segment) {
    return null;
  }

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
      <div className="absolute top-3 right-2 flex gap-1 items-end">
        {icons.map((icon, i) => (
          <div key={i} className="relative group">
            <img
              src={`/icons/${icon}.png`}
              alt={icon}
              className="w-8 h-8 object-contain"
            />
            <div className="absolute bottom-full mb-1 left-1/2 -translate-x-1/2 bg-black text-white text-xs rounded px-2 py-0.5 opacity-0 group-hover:opacity-100 transition pointer-events-none z-10 whitespace-nowrap max-w-[200px] text-center">
              {getIconRule(icon).rule}
            </div>
          </div>
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
              <Link
                to={`/player/${player.nickname}`}
                className="font-semibold text-lg text-orange-400 hover:underline"
              >
                {player.nickname}
              </Link>
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
        <div>K/D: {segment.averageKd.toFixed(2)}</div>
        <div>K/R: {segment.averageKr.toFixed(2)}</div>
        <div>ADR: {segment.averageAdr.toFixed(1)}</div>
        <div>HS: {segment.averageHsPercent}%</div>
        <div>
          K/A/D: {segment.kavg} / {segment.aavg} / {segment.davg}
        </div>
        <div>New K/D: {segment.newKD?.toFixed(2) ?? "0.00"}</div>
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
        <div className="text-xs text-gray-400 whitespace-nowrap mr-10">
          Last played: {dayjs(player.lastActive).fromNow()}
        </div>
      </div>
    </div>
  );
}

