// src/pages/GogoTracker.tsx
import { useState } from "react";
import { usePlayerData } from "../context/PlayerDataContext";
import PlayerCard from "../components/PlayerCard";

export default function GogoTracker() {
  const { data, loading } = usePlayerData();
  const [segmentKey, setSegmentKey] = useState<
    "last10" | "last30" | "last50" | "last100"
  >("last30");

  return (
    <main className="min-h-screen p-6 overflow-x-hidden overflow-y-auto">
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
        <div className="text-center text-white">
          Loading stats... Server is sometimes a little slow
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
          {data!.map((player) => (
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
