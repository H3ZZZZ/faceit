import type { PlayerStatsFull } from "../../types/PlayerStatsFull";
import { getPlayerIcons } from "../../utils/icons";

type Props = {
  data: PlayerStatsFull;
};

const calculateCurrentWinStreak = (results: string[]) => {
  let streak = 0;
  for (let i = results.length - 1; i >= 0; i--) {
    if (results[i] === "W") streak++;
    else break;
  }
  return streak;
};

export default function LifetimeStatsCard({ data }: Props) {
  const currentStreak = calculateCurrentWinStreak(data.last5Results);

  const icons = getPlayerIcons({
    averageKr: data.avgKr,
    averageKd: data.avgKd,
    winRate: data.winrate,
    averageHsPercent: data.avgHsPercent,
    winstreak: currentStreak,
  });

  return (
    <div className="bg-[#1e1e1e] text-white p-5 rounded-xl shadow-md hover:shadow-lg transition flex flex-col gap-4">
      {/* Header */}
      <div className="flex justify-between items-center">
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
            <div className="text-xs text-gray-400 flex items-center gap-2 mt-0.5">
              ELO: {data.faceitElo}
              <img
                src={`/faceit-levels/${data.skillLevel}.png`}
                alt={`Level ${data.skillLevel}`}
                className="inline w-5 h-5 align-middle"
              />
            </div>
          </div>
        </div>

        {/* Icons */}
        <div className="flex gap-1">
          {icons.map((icon, i) => (
            <img
              key={i}
              src={`/icons/${icon}.png`}
              alt={icon}
              title={icon}
              className="w-6 h-6"
            />
          ))}
        </div>
      </div>

      <div className="border-t border-gray-700" />

      {/* Stats */}
      <div className="grid grid-cols-2 md:grid-cols-3 gap-y-1 text-sm">
        <div>
          Matches:{" "}
          <span className="font-semibold text-white">{data.matchesPlayed}</span>
        </div>
        <div>
          Winrate:{" "}
          <span className="font-semibold text-white">{data.winrate}%</span>
        </div>
        <div>
          CS2 Elo Δ:{" "}
          <span
            className={`font-semibold ${
              data.totalEloGain >= 0 ? "text-green-500" : "text-red-500"
            }`}
          >
            {data.totalEloGain >= 0 ? "+" : ""}
            {data.totalEloGain}
          </span>
        </div>
        <div>
          K/D:{" "}
          <span className="font-semibold text-white">
            {data.avgKd.toFixed(2)}
          </span>
        </div>
        <div>
          K/R:{" "}
          <span className="font-semibold text-white">
            {data.avgKr.toFixed(2)}
          </span>
        </div>
        <div>
          ADR:{" "}
          <span className="font-semibold text-white">
            {data.avgAdr.toFixed(1)}
          </span>
        </div>
        <div>
          HS:{" "}
          <span className="font-semibold text-white">{data.avgHsPercent}%</span>
        </div>
        <div>
          K/A/D:{" "}
          <span className="font-semibold text-white">
            {data.kavg} / {data.aavg} / {data.davg}
          </span>
        </div>
        <div>
          Current Streak:{" "}
          <span className="font-semibold text-white">{currentStreak}</span>
        </div>
      </div>

      {/* Last 5 Results */}
      <div className="mt-2 flex gap-1">
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
  );
}
