import { useEffect, useState } from 'react';
import { fetchAllPlayerStats } from '../services/api';
import type {PlayerStats} from '../types/PlayerStats';
import PlayerCard from '../components/PlayerCard';

export default function Home() {
    const [players, setPlayers] = useState<PlayerStats[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchAllPlayerStats().then(data => {
            setPlayers(data);
            setLoading(false);
        });
    }, []);

    if (loading) return <div className="p-4">Loading...</div>;

    return (
        <div className="p-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {players.map(player => (
                <PlayerCard key={player.playerId} player={player} />
            ))}
        </div>
    );
}
