import type { SharedPlayerStatsDTO } from "../types/SharedStatsResponse";
import { getPlayerIcons, getIconRule } from "../utils/icons";

export default function SharedPlayerCard({
  player,
}: {
  player: SharedPlayerStatsDTO;
}) {
  const eloColor =
    player.totalEloGain > 0
      ? "text-green-400"
      : player.totalEloGain < 0
      ? "text-red-400"
      : "text-white";

  const icons = getPlayerIcons({
    averageKr: player.avgKr,
    averageKd: player.avgKd,
    winRate: player.winrate,
    averageHsPercent: player.avgHsPercent,
    winstreak:
      player.last5Results?.length === 5 &&
      player.last5Results.every((r) => r === "W")
        ? 5
        : 0,
  });

  return (
    <div className="bg-[#1e1e1e] rounded-xl p-4 border border-gray-700 shadow w-full max-w-xs mx-auto">
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
          Matches: <span className="text-white">{player.totalMatches}</span>
        </p>
        <p>
          Win/Loss:{" "}
          <span className="text-green-400 font-medium">{player.wins}</span>/{""}
          <span className="text-red-400 font-medium">{player.losses}</span>
        </p>
        <p>
          K/D: <span className="text-white">{player.avgKd}</span>
        </p>
        <p>
          KR: <span className="text-white">{player.avgKr}</span>
        </p>
        <p>
          ADR: <span className="text-white">{player.avgAdr}</span>
        </p>
        <p>
          HS: <span className="text-white">{player.avgHsPercent}%</span>
        </p>
        <p>
          Winrate: <span className="text-white">{player.winrate}%</span>
        </p>
        <p>
          ELO Δ:{" "}
          <span className={`${eloColor} font-semibold`}>
            {player.totalEloGain >= 0 ? "+" : ""}
            {player.totalEloGain}
          </span>
        </p>
      </div>

      <div className="text-sm text-gray-300 mb-1">
        <span className="mr-4">
          Best Win Streak:{" "}
          <span className="text-green-400">{player.longestWinStreak}</span>
        </span>
        <span>
          Worst Loss Streak:{" "}
          <span className="text-red-400">{player.longestLossStreak}</span>
        </span>
      </div>

      <div className="text-sm text-gray-300 flex items-center gap-2 mb-2">
        {player.last5Results?.map((res, idx) => (
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
              <img src={`/icons/${icon}.png`} alt={label} className="w-5 h-5" />
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
}
