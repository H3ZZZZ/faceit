// src/pages/GogoPage.tsx
import React, { useEffect, useState } from 'react';
// @ts-ignore
import axios from 'axios';

interface PlayerStatBlock {
    gamesCount: number;
    wins: number;
    losses: number;
    winRate: number;
    eloDiff: number;
    avgKills: number;
    avgDeaths: number;
    avgAssists: number;
    avgKD: number;
    avgKR: number;
    avgADR: number;
    avgHS: number;
}

interface PlayerStatsResponse {
    nickname: string;
    stats10: PlayerStatBlock;
    stats30: PlayerStatBlock;
    stats50: PlayerStatBlock;
    stats100: PlayerStatBlock;
}

const PLAYER_IDS = [
    "1d51deb3-3f4a-4d9e-8494-bf8d5e280341",
    "86df81a4-ecd1-439c-a717-7efff943f3b2",
];

const GAME_COUNTS = [10, 30, 50, 100];

const GogoPage: React.FC = () => {
    const [selectedGames, setSelectedGames] = useState(30);
    const [stats, setStats] = useState<PlayerStatsResponse[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchStats = async () => {
        setLoading(true);
        try {
            const responses = await Promise.all(
                PLAYER_IDS.map((id) =>
                    axios.get<PlayerStatsResponse>(`http://localhost:8080/api/stats/${id}`)
                )
            );
            const allStats = responses.map((res) => res.data);
            setStats(allStats);
        } catch (err) {
            console.error('Failed to fetch stats:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">Gogo Page</h1>
            <div className="flex gap-2 mb-4">
                {GAME_COUNTS.map((count) => (
                    <button
                        key={count}
                        className={`px-4 py-2 rounded ${selectedGames === count ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
                        onClick={() => setSelectedGames(count)}
                    >
                        Last {count} games
                    </button>
                ))}
            </div>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {stats.map((player) => {
                        const selectedStats = player[`stats${selectedGames}` as keyof PlayerStatsResponse] as PlayerStatBlock;
                        return (
                            <div key={player.nickname} className="border p-4 rounded shadow">
                                <h2 className="text-xl font-semibold mb-2">{player.nickname}</h2>
                                <p>Games: {selectedStats.gamesCount}</p>
                                <p>Wins: {selectedStats.wins}</p>
                                <p>Losses: {selectedStats.losses}</p>
                                <p>Winrate: {selectedStats.winRate.toFixed(1)}%</p>
                                <p>ELO: {selectedStats.eloDiff}</p>
                                <p>Kills: {selectedStats.avgKills}</p>
                                <p>Deaths: {selectedStats.avgDeaths}</p>
                                <p>Assists: {selectedStats.avgAssists}</p>
                                <p>K/D: {selectedStats.avgKD.toFixed(2)}</p>
                                <p>K/R: {selectedStats.avgKR.toFixed(2)}</p>
                                <p>ADR: {selectedStats.avgADR.toFixed(2)}</p>
                                <p>HS%: {selectedStats.avgHS.toFixed(1)}%</p>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
};

export default GogoPage;