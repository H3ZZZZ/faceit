import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

type Props = {
  data: PlayerStatsFull;
};

export default function LifetimeStatsCard({ data }: Props) {
  return (
    <div className="bg-[#1e1e1e] text-white p-4 rounded-xl shadow-md transition hover:shadow-lg flex flex-col gap-3 relative">
      {/* Header */}
      <div className="flex items-center gap-3">
        <img
          src={data.avatar}
          alt="avatar"
          className="w-12 h-12 rounded-full"
        />
        <div>
          <div className="flex items-center gap-2">
            <h2 className="font-semibold text-lg">{data.nickname}</h2>
            <img
              src={`https://flagcdn.com/24x18/${data.country.toLowerCase()}.png`}
              alt={data.country}
              className="w-4 h-3 object-cover rounded-sm mt-[1px]"
            />
          </div>
          <div className="text-xs text-gray-400">
            ELO: {data.faceitElo}
            <img
              src={`/faceit-levels/${data.skillLevel}.png`}
              alt={`Level ${data.skillLevel}`}
              className="inline w-5 h-5 ml-2 align-middle"
            />
          </div>
        </div>
      </div>

      <div className="border-t border-gray-700" />

      {/* Stats */}
      <div className="grid grid-cols-2 text-sm gap-y-1">
        <div>Matches: {data.matchesPlayed}</div>
        <div>Winrate: {data.winrate}%</div>
        <div>K/D: {data.avgKd.toFixed(2)}</div>
        <div>K/R: {data.avgKr.toFixed(2)}</div>
        <div>ADR: {data.avgAdr.toFixed(1)}</div>
        <div>HS%: {data.avgHsPercent}%</div>
        <div>
          K/A/D: {data.kavg} / {data.aavg} / {data.davg}
        </div>
        <div>Total Elo: {data.totalEloGain}</div>
      </div>

      {/* Last 5 Results */}
      <div className="mt-4">
        <div className="flex gap-1">
          {data.last5Results.map((r, i) => (
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
    </div>
  );
}
