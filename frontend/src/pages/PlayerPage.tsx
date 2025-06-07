import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

type PlayerData = {
    nickname: string;
    avatar: string;
    country: string;
    elo: number;
    level: number;
};

function PlayerPage() {
    const { nickname } = useParams();
    const [data, setData] = useState<PlayerData | null>(null);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch(`http://localhost:8080/api/player/${nickname}`)
            .then((res) => {
                if (!res.ok) throw new Error('Player not found');
                return res.json();
            })
            .then(setData)
            .catch((err) => setError(err.message));
    }, [nickname]);

    if (error) return <div>Error: {error}</div>;
    if (!data) return <div>Loading...</div>;

    return (
        <div style={{ padding: '2rem' }}>
            <h2>Player: {data.nickname}</h2>
            <img src={data.avatar} alt="Avatar" width={100} />
            <p>Country: {data.country}</p>
            <p>ELO: {data.elo}</p>
            <p>Level: {data.level}</p>
        </div>
    );
}

export default PlayerPage;
