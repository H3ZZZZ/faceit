import { useEffect, useState } from 'react';
import { fetchAllPlayerStats } from './api/api';
import PlayerCard from './components/PlayerCard';
import type { PlayerStats } from './types/PlayerStats';

function App() {
  const [data, setData] = useState<PlayerStats[]>([]);

  useEffect(() => {
    fetchAllPlayerStats().then(setData);
  }, []);

  return (
    <main className="bg-[#121212] min-h-screen p-6">
      <h1 className="text-3xl text-white font-bold mb-6">Player Stats</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {data.map(player => (
          <PlayerCard key={player.playerId} {...player} />
        ))}
      </div>
    </main>
  );
}

export default App;
