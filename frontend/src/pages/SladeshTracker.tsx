// src/pages/SladeshTracker.tsx
import { useEffect, useState } from "react";
import type { SladeshSimple } from "../types/SladeshSimple";
import { fetchSladeshSimple } from "../api/api";

const CACHE_KEY = "sladesh_simple_cache";
const CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

export default function SladeshTracker() {
  const [data, setData] = useState<SladeshSimple | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const cached = localStorage.getItem(CACHE_KEY);
    if (cached) {
      const { timestamp, payload } = JSON.parse(cached);
      if (Date.now() - timestamp < CACHE_DURATION) {
        setData(payload);
        setLoading(false);
        return;
      }
    }

    fetchSladeshSimple()
      .then((res) => {
        setData(res);
        localStorage.setItem(CACHE_KEY, JSON.stringify({ timestamp: Date.now(), payload: res }));
      })
      .catch(() => setData(null))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="text-white p-6">Loading...</div>;

  if (!data)
    return <div className="text-red-500 p-6">Failed to load Sladesh data.</div>;

  const isLevel10 = data.level === 10;

  return (
    <main className="min-h-screen flex flex-col items-center justify-center text-white">
      <h1 className="text-2xl font-semibold mb-4">Is sladesh level 10?</h1>
      <div
        className={`text-3xl font-bold ${
          isLevel10 ? "text-green-400" : "text-red-500"
        }`}
      >
        {isLevel10 ? "✅ Yes" : "❌ No"}
      </div>
      <div className="mt-2 text-sm text-gray-300">
        Current ELO: <span className="font-mono">{data.elo}</span>
      </div>
    </main>
  );
}
