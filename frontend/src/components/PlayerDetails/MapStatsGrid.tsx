import type { PlayerStatsFull, MapStats } from "../../types/PlayerStatsFull";
import { getPlayerIcons } from "../../utils/icons";
import { getIconRule } from "../../utils/icons";

type Props = {
  data: PlayerStatsFull;
};

const currentMapPool = [
  "de_inferno",
  "de_train",
  "de_mirage",
  "de_nuke",
  "de_dust2",
  "de_overpass",
  "de_ancient",
];

const calculateCurrentWinStreak = (results: string[]) => {
  let streak = 0;
  for (const r of results.reverse()) {
    if (r === "W") streak++;
    else break;
  }
  return streak;
};

export default function MapStatsGrid({ data }: Props) {
  const currentMaps = data.mapStats.filter((m) =>
    currentMapPool.includes(m.map)
  );
  const otherMaps = data.mapStats.filter(
    (m) => !currentMapPool.includes(m.map)
  );

  const renderMapCards = (maps: MapStats[]) => (
    <div className="grid sm:grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
      {maps.map((map) => {
        const currentWinStreak = calculateCurrentWinStreak([
          ...map.last5Results,
        ]);

        const icons = getPlayerIcons({
          averageKr: map.avgKr,
          averageKd: map.avgKd,
          winRate: map.winrate,
          averageHsPercent: map.avgHsPercent,
          winstreak: currentWinStreak,
        });

        return (
          <div
            key={map.map}
            className="bg-[#1e1e1e] p-4 rounded-lg shadow-md hover:shadow-lg transition relative"
          >
            {/* Image */}
            <img
              src={`/map-images/${map.map}.jpg`}
              alt={map.map}
              onError={(e) =>
                ((e.target as HTMLImageElement).style.display = "none")
              }
              className="rounded w-full h-28 object-cover mb-2"
            />

            {/* Icons row with descriptive tooltip */}
            <div className="flex justify-end gap-2 mb-2">
              {icons.map((icon, i) => {
                const { label, rule } = getIconRule(icon);
                return (
                  <div key={i} className="relative group">
                    <img
                      src={`/icons/${icon}.png`}
                      alt={label}
                      className="w-5 h-5"
                    />
                    <div className="absolute bottom-full mb-1 left-1/2 -translate-x-1/2 bg-black text-white text-[11px] rounded px-2 py-1 opacity-0 group-hover:opacity-100 transition pointer-events-none z-10 w-max max-w-xs text-center whitespace-pre-line">
                      <strong className="block mb-0.5 text-orange-400">
                        {label}
                      </strong>
                      {rule}
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Top row: Map name, Elo, Matches */}
            <div className="flex justify-between items-center mb-2">
              <h4 className="text-lg font-semibold capitalize">
                {map.map
                  .replace("de_", "")
                  .replace("_rbcs", "")
                  .replace("_", " ")}
              </h4>
              <div className="text-base text-center">
                <span className="text-white mr-1">Elo Δ:</span>
                <span
                  className={`font-semibold ${
                    map.totalEloGain >= 0 ? "text-green-500" : "text-red-500"
                  }`}
                >
                  {map.totalEloGain >= 0 ? "+" : ""}
                  {map.totalEloGain}
                </span>
              </div>

              <span className="text-sm text-gray-400">
                {map.matches} matches
              </span>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-2 text-sm gap-y-1 mb-3">
              <div>
                Wins:{" "}
                <span className="font-semibold text-green-400">{map.wins}</span>
              </div>
              <div>
                Losses:{" "}
                <span className="font-semibold text-red-400">{map.losses}</span>
              </div>
              <div>
                Winrate:{" "}
                <span className="font-semibold text-white">{map.winrate}%</span>
              </div>
              <div>
                K/D:{" "}
                <span className="font-semibold text-white">
                  {map.avgKd.toFixed(2)}
                </span>
              </div>
              <div>
                K/R:{" "}
                <span className="font-semibold text-white">
                  {map.avgKr.toFixed(2)}
                </span>
              </div>
              <div>
                ADR:{" "}
                <span className="font-semibold text-white">
                  {map.avgAdr.toFixed(1)}
                </span>
              </div>
              <div>
                HS:{" "}
                <span className="font-semibold text-white">
                  {map.avgHsPercent}%
                </span>
              </div>
              <div>
                K/A/D:{" "}
                <span className="font-semibold text-white">
                  {map.kavg} / {map.aavg} / {map.davg}
                </span>
              </div>
              <div>
                Win Streak:{" "}
                <span className="font-semibold text-white">
                  {map.longestWinStreak}
                </span>
              </div>
              <div>
                Loss Streak:{" "}
                <span className="font-semibold text-white">
                  {map.longestLossStreak}
                </span>
              </div>
            </div>

            {/* Last 5 Results */}
            <div className="flex gap-1 mt-2">
              {map.last5Results.map((r, i) => (
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
      })}
    </div>
  );

  return (
    <div className="mt-10">
      <h3 className="text-xl font-bold mb-4">Map Breakdown</h3>

      {/* Current Map Pool */}
      {currentMaps.length > 0 && (
        <>
          <h4 className="text-lg font-semibold mb-2 text-orange-400">
            Current Map Pool
          </h4>
          {renderMapCards(currentMaps)}
        </>
      )}

      {/* Other Maps */}
      {otherMaps.length > 0 && (
        <>
          <h4 className="text-lg font-semibold mb-4 mt-8 text-gray-300">
            Other Maps
          </h4>
          {renderMapCards(otherMaps)}
        </>
      )}
    </div>
  );
}
