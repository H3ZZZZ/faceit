// src/pages/GogoTracker.tsx
import { useEffect, useState } from "react";
import { fetchAllPlayerStats } from "../api/api";
import PlayerCard from "../components/PlayerCard";
import type { PlayerStats } from "../types/PlayerStats";

export default function GogoTracker() {
  const [data, setData] = useState<PlayerStats[]>([]);
  const [loading, setLoading] = useState(true);
  const [segmentKey, setSegmentKey] = useState<
    "last10" | "last30" | "last50" | "last100"
  >("last30");

  useEffect(() => {
    fetchAllPlayerStats().then((data) => {
      setData(data);
      setLoading(false);
    });
  }, []);

  return (
    <main className="bg-[#121212] min-h-screen p-6">
      {/* Global segment selector */}
      <div className="flex justify-center gap-2 mb-6">
        {["last10", "last30", "last50", "last100"].map((key) => (
          <button
            key={key}
            onClick={() => setSegmentKey(key as typeof segmentKey)}
            className={`text-xs px-3 py-1 rounded font-medium transition-all duration-150 transform ${
              segmentKey === key
                ? "bg-orange-500 text-black scale-105 shadow"
                : "bg-gray-700 text-white hover:bg-gray-600 hover:scale-105"
            }`}
          >
            {key.replace("last", "Last ")}
          </button>
        ))}
      </div>

      {/* Stats grid */}
      {loading ? (
        <div className="text-center text-white">Loading stats...</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
          {data.map((player) => (
            <PlayerCard
              key={player.playerId}
              {...player}
              segmentKey={segmentKey}
            />
          ))}
        </div>
      )}
    </main>
  );
}
