import { useState } from 'react';

type PlayerData = {
    nickname: string;
    avatar: string;
    country: string;
    elo: number;
    level: number;
};

function SearchPage() {
    const [nickname, setNickname] = useState('');
    const [player, setPlayer] = useState<PlayerData | null>(null);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setPlayer(null);
        setError('');

        try {
            const res = await fetch(`http://localhost:8080/api/player/${nickname}`);
            if (!res.ok) throw new Error('Player not found');
            const data = await res.json();
            setPlayer(data);
        } catch (err: any) {
            setError(err.message || 'Unknown error');
        }
    };

    return (
        <div style={{ padding: '2rem' }}>
            <h1>FACEIT Player Search</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    placeholder="Enter player nickname"
                />
                <button type="submit">Search</button>
            </form>

            {error && <p style={{ color: 'red' }}>Error: {error}</p>}

            {player && (
                <div style={{ marginTop: '2rem', border: '1px solid #ccc', padding: '1rem' }}>
                    <img src={player.avatar} alt="Avatar" width={100} />
                    <h2>{player.nickname}</h2>
                    <p>Country: {player.country}</p>
                    <p>ELO: {player.elo}</p>
                    <p>Level: {player.level}</p>
                </div>
            )}
        </div>
    );
}

export default SearchPage;
