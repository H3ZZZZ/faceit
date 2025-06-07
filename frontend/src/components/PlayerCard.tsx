import type {PlayerStats} from '../types/PlayerStats';

export default function PlayerCard({ player }: { player: PlayerStats }) {
    return (
        <div className="bg-white shadow p-4 rounded-xl w-full md:w-[300px]">
            <h2 className="text-xl font-bold mb-2">{player.nickname}</h2>
            <p>ELO Change: {player.eloChange}</p>
            <p>Winrate: {player.winRate}%</p>
            <p>K/D: {player.averageKd}</p>
            <p>K/R: {player.averageKr}</p>
            <p>ADR: {player.averageAdr}</p>
            <p>HS%: {player.averageHsPercent}%</p>
            <p>K/A/D: {player.kavg}/{player.aavg}/{player.davg}</p>
        </div>
    );
}
