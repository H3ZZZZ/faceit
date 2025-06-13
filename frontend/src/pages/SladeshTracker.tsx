import { useEffect, useState } from "react";

type SladeshSimple = {
  nickname: string;
  elo: number;
  level: number;
};

export default function SladeshTracker() {
  const [data, setData] = useState<SladeshSimple | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/stats/sladesh/simple")
      .then((res) => res.json())
      .then(setData)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <div className="text-white p-6">Loading...</div>;
  }

  if (!data) {
    return <div className="text-red-500 p-6">Failed to load Sladesh data.</div>;
  }

  const isLevel10 = data.level === 10;

  return (
    <main className="min-h-screen flex flex-col items-center justify-center bg-[#111827] text-white">
      <h1 className="text-2xl font-semibold mb-4">Is Sladesh level 10?</h1>
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
