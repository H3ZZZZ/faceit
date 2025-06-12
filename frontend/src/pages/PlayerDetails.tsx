import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchPlayerByNickname } from "../api/api";
import type { PlayerStatsFull } from "../types/PlayerStatsFull";
import LifetimeStatsCard from "../components/PlayerDetails/LifetimeStatsCard";
import PerformanceHighlights from "../components/PlayerDetails/PerformanceHighlights";

export default function PlayerDetails() {
  const { nickname } = useParams();
  const [data, setData] = useState<PlayerStatsFull | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!nickname) return;

    fetchPlayerByNickname(nickname)
      .then(setData)
      .catch(() => setError("Player not found or error loading data."));
  }, [nickname]);

  if (error)
    return (
      <div className="text-red-500 text-center mt-10 text-lg">{error}</div>
    );
  if (!data)
    return (
      <div className="text-white text-center mt-10 text-lg">Loading...</div>
    );

  return (
    <div className="text-white max-w-5xl mx-auto mt-10 px-4 pb-10">
      {/* Page Title */}
      <h1 className="text-3xl font-bold mb-6 text-center text-orange-400">
        All-Time CS2 Stats
      </h1>

      <LifetimeStatsCard data={data} />
      <PerformanceHighlights data={data} />

      {/* Notice about data limitations */}
      <div className="mt-6 text-sm text-gray-400 text-center max-w-2xl mx-auto">
        ⚠️ Note: Due to ELO resets, smurf activity, or missing match data, small
        discrepancies may occur in the total ELO calculations. We aim for
        accuracy but cannot guarantee 100%.
      </div>

      {/* Footer Callout */}
      <div className="mt-10 text-center">
        <div className="inline-block bg-yellow-500 text-black px-4 py-2 rounded shadow-md text-sm font-semibold animate-pulse">
          🚧 More stats are coming soon – stay tuned!
        </div>
      </div>
    </div>
  );
}
