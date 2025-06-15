import type {
  SharedPlayerStatsDTO,
  SharedMapStatsDTO,
} from "../types/SharedStatsResponse";
import { getPlayerIcons, getIconRule } from "../utils/icons";

interface Props {
  players: SharedPlayerStatsDTO[];
}

const currentMapPool = [
  "de_inferno",
  "de_train",
  "de_mirage",
  "de_nuke",
  "de_dust2",
  "de_anubis",
  "de_ancient",
];

const formatMapName = (map: string) =>
  map.replace("de_", "").replace("_rbcs", "").replace("_", " ").toUpperCase();

export default function SharedMapComparisonGrid({ players }: Props) {
  const allMaps = currentMapPool.filter((map) =>
    players.some((p) => p.mapStats.some((m) => m.map === map))
  );

  return (
    <div className="mt-12">
      <h2 className="text-2xl font-bold mb-6">Map Comparison</h2>
      {allMaps.map((map) => (
        <div key={map} className="mb-10">
          <h3 className="text-xl font-semibold mb-4 text-orange-400">
            {formatMapName(map)}
          </h3>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
            {players.map((player) => {
              const mapStats = player.mapStats.find((m) => m.map === map);
              if (!mapStats) return null;

              const icons = getPlayerIcons({
                averageKr: mapStats.avgKr,
                averageKd: mapStats.avgKd,
                winRate: mapStats.winrate,
                averageHsPercent: mapStats.avgHsPercent,
                winstreak:
                  mapStats.last5Results?.length === 5 &&
                  mapStats.last5Results.every((r) => r === "W")
                    ? 5
                    : 0,
              });

              const eloColor =
                mapStats.totalEloGain > 0
                  ? "text-green-400"
                  : mapStats.totalEloGain < 0
                  ? "text-red-400"
                  : "text-white";

              return (
                <div
                  key={player.nickname}
                  className="bg-[#1e1e1e] rounded-xl p-4 border border-gray-700 shadow w-full"
                >
                  <div className="flex items-center gap-4 mb-4">
                    <img
                      src={player.avatar}
                      alt={player.nickname}
                      className="w-12 h-12 rounded-full"
                    />
                    <div>
                      <h3 className="text-lg font-semibold flex items-center gap-2">
                        {player.nickname}
                        <img
                          src={`https://flagcdn.com/24x18/${player.country.toLowerCase()}.png`}
                          alt={player.country}
                          className="w-4 h-3 object-cover rounded-sm mt-[1px]"
                        />
                      </h3>
                      <div className="text-xs text-gray-400 flex items-center gap-2 mt-0.5">
                        ELO: {player.faceitElo}
                        <img
                          src={`/faceit-levels/${player.skillLevel}.png`}
                          alt={`Level ${player.skillLevel}`}
                          className="inline w-5 h-5 align-middle"
                        />
                      </div>
                    </div>
                  </div>

                  <div className="grid grid-cols-2 gap-1 text-sm text-gray-200 mb-3">
                    <p>
                      Matches:{" "}
                      <span className="text-white">{mapStats.matches}</span>
                    </p>
                    <p>
                      Win/Loss:{" "}
                      <span className="text-green-400 font-medium">
                        {mapStats.wins}
                      </span>{" "}
                      /{" "}
                      <span className="text-red-400 font-medium">
                        {mapStats.losses}
                      </span>
                    </p>
                    <p>
                      K/D:{" "}
                      <span className="text-white">
                        {mapStats.avgKd.toFixed(2)}
                      </span>
                    </p>
                    <p>
                      KR:{" "}
                      <span className="text-white">
                        {mapStats.avgKr.toFixed(2)}
                      </span>
                    </p>
                    <p>
                      ADR:{" "}
                      <span className="text-white">
                        {mapStats.avgAdr.toFixed(1)}
                      </span>
                    </p>
                    <p>
                      HS:{" "}
                      <span className="text-white">
                        {mapStats.avgHsPercent}%
                      </span>
                    </p>
                    <p>
                      Winrate:{" "}
                      <span className="text-white">{mapStats.winrate}%</span>
                    </p>
                    <p>
                      ELO Δ:{" "}
                      <span className={`${eloColor} font-semibold`}>
                        {mapStats.totalEloGain >= 0 ? "+" : ""}
                        {mapStats.totalEloGain}
                      </span>
                    </p>
                  </div>

                  <div className="text-sm text-gray-300 mb-1">
                    <span className="mr-4">
                      Best Win Streak:{" "}
                      <span className="text-green-400">
                        {mapStats.longestWinStreak}
                      </span>
                    </span>
                    <span>
                      Worst Loss Streak:{" "}
                      <span className="text-red-400">
                        {mapStats.longestLossStreak}
                      </span>
                    </span>
                  </div>

                  <div className="text-sm text-gray-300 flex items-center gap-2 mb-2">
                    {mapStats.last5Results?.map((res, idx) => (
                      <span
                        key={idx}
                        className={`w-5 h-5 flex items-center justify-center text-xs font-bold rounded ${
                          res === "W" ? "bg-green-500" : "bg-red-500"
                        } text-white`}
                      >
                        {res}
                      </span>
                    ))}
                  </div>

                  <div className="flex justify-end gap-2">
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
                </div>
              );
            })}
          </div>
        </div>
      ))}
    </div>
  );
}
