import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

type Props = {
  data: PlayerStatsFull;
};

export default function PlayerStatsOverview({ data }: Props) {
  return (
    <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 text-sm">
      <p>Matches: {data.matchesPlayed}</p>
      <p>Winrate: {data.winrate}%</p>
      <p>K/D: {data.avgKd.toFixed(2)}</p>
      <p>K/R: {data.avgKr.toFixed(2)}</p>
      <p>ADR: {data.avgAdr.toFixed(1)}</p>
      <p>HS%: {data.avgHsPercent}%</p>
      <p>K/A/D: {data.kavg} / {data.aavg} / {data.davg}</p>
      <p>
        Elo Δ:{" "}
        <span className={data.eloChange >= 0 ? "text-green-400" : "text-red-400"}>
          {data.eloChange >= 0 ? "+" : ""}
          {data.eloChange}
        </span>
      </p>
    </div>
  );
}
