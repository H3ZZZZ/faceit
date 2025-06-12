import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

const highlight = (label: string, value: string | number, matchId: string) => (
  <div className="text-sm">
    <span className="text-gray-300">{label}: </span>
    <span className="text-orange-400">{value}</span>{" "}
    <a
      href={`https://www.faceit.com/en/cs2/room/${matchId}/scoreboard`}
      target="_blank"
      rel="noopener noreferrer"
      className="text-blue-400 hover:underline text-xs"
    >
      (view match)
    </a>
  </div>
);

type Props = {
  data: PlayerStatsFull;
};

export default function PerformanceHighlights({ data }: Props) {
  return (
    <div className="mt-8 p-4 bg-[#1a1a1a] rounded-lg">
      <h3 className="text-lg font-semibold mb-4">Performance Highlights</h3>
      <div className="grid sm:grid-cols-2 gap-4">
        {highlight("Most Kills", data.mostKills, data.mostKillsMatchId)}
        {highlight("Fewest Kills", data.fewestKills, data.fewestKillsMatchId)}
        {highlight("Highest K/D", data.highestKd, data.highestKdMatchId)}
        {highlight("Lowest K/D", data.lowestKd, data.lowestKdMatchId)}
        {highlight("Highest K/R", data.highestKr, data.highestKrMatchId)}
        {highlight("Lowest K/R", data.lowestKr, data.lowestKrMatchId)}
        <div>Longest Win Streak: {data.longestWinStreak}</div>
        <div>Longest Loss Streak: {data.longestLossStreak}</div>
      </div>
    </div>
  );
}
