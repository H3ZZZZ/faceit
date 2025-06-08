import type { PlayerStats } from '../types/PlayerStats';
import { getPlayerIcons } from '../utils/icons';

export default function PlayerCard({
  nickname,
  wins,
  losses,
  winRate,
  eloChange,
  averageKd,
  averageKr,
  averageAdr,
  averageHsPercent,
  kavg,
  aavg,
  davg,
}: PlayerStats) {
  const winstreak = 0; // Placeholder – will be added to backend later
  const icons = getPlayerIcons({
    averageKr,
    averageKd: parseFloat(averageKd),
    winRate,
    averageHsPercent,
    winstreak,
  });

  return (
    <div className="relative bg-[#1e1e1e] text-white p-4 rounded-xl shadow-md hover:scale-105 transition">
      {/* Icons */}
      <div className="absolute top-2 right-2 flex gap-1 text-xl">
        {icons.map((icon, i) => (
          <span key={i} title={icon}>{icon}</span>
        ))}
      </div>

      {/* Player name */}
      <h2 className="text-xl font-semibold mb-3">{nickname}</h2>

      {/* Stats grid */}
      <div className="grid grid-cols-2 text-sm gap-y-1">
        <div>
          Wins/Losses: <span className="text-green-400">{wins}</span> / <span className="text-red-400">{losses}</span>
        </div>
        <div>Win Rate: {winRate}%</div>
        <div>
          Elo:{" "}
          <span className={eloChange >= 0 ? "text-green-400" : "text-red-400"}>
            {eloChange >= 0 ? "+" : ""}
            {eloChange}
          </span>
        </div>
        <div>K/D: {averageKd}</div>
        <div>ADR: {averageAdr.toFixed(1)}</div>
        <div>HS%: {averageHsPercent}%</div>
        <div>K/R: {averageKr}</div>
        <div>K/A/D: {kavg} / {aavg} / {davg}</div>
      </div>
    </div>
  );
}
