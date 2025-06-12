import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

type Props = {
  data: PlayerStatsFull;
};

export default function PlayerExtremes({ data }: Props) {
  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold mb-2">Notable Performances</h3>
      <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 text-sm">
        <p>Most Kills: {data.mostKills}</p>
        <p>Fewest Kills: {data.fewestKills}</p>
        <p>Highest K/D: {data.highestKd}</p>
        <p>Lowest K/D: {data.lowestKd}</p>
        <p>Highest K/R: {data.highestKr}</p>
        <p>Lowest K/R: {data.lowestKr}</p>
        <p>Win Streak: {data.longestWinStreak}</p>
        <p>Loss Streak: {data.longestLossStreak}</p>
      </div>
    </div>
  );
}
