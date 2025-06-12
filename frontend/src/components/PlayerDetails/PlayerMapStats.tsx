import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

type Props = {
  data: PlayerStatsFull;
};

export default function PlayerMapStats({ data }: Props) {
  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold mb-2">Per-Map Stats</h3>
      <div className="overflow-x-auto">
        <table className="table-auto w-full text-sm text-left text-white border-collapse">
          <thead className="bg-gray-800">
            <tr>
              <th className="p-2">Map</th>
              <th className="p-2">Matches</th>
              <th className="p-2">Winrate</th>
              <th className="p-2">K/D</th>
              <th className="p-2">K/R</th>
              <th className="p-2">ADR</th>
              <th className="p-2">HS%</th>
              <th className="p-2">K/A/D</th>
            </tr>
          </thead>
          <tbody>
            {data.mapStats.map((map) => (
              <tr key={map.map} className="border-t border-gray-700">
                <td className="p-2">{map.map}</td>
                <td className="p-2">{map.matches}</td>
                <td className="p-2">{map.winrate}%</td>
                <td className="p-2">{map.avgKd.toFixed(2)}</td>
                <td className="p-2">{map.avgKr.toFixed(2)}</td>
                <td className="p-2">{map.avgAdr.toFixed(1)}</td>
                <td className="p-2">{map.avgHsPercent}%</td>
                <td className="p-2">{map.kavg} / {map.aavg} / {map.davg}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
