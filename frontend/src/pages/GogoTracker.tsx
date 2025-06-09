// src/pages/GogoTracker.tsx
import { useEffect, useState } from "react";
import { fetchAllPlayerStats } from "../api/api";
import PlayerCard from "../components/PlayerCard";
import type { PlayerStats } from "../types/PlayerStats";

export default function GogoTracker() {
  const [data, setData] = useState<PlayerStats[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAllPlayerStats().then((data) => {
      setData(data);
      setLoading(false);
    });
  }, []);

  return (
    <main className="bg-[#121212] min-h-screen p-6">
      {loading ? (
        <div className="text-center text-white">Loading stats...</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
          {data.map((player) => (
            <PlayerCard key={player.playerId} {...player} />
          ))}
        </div>
      )}
    </main>
  );
}
