// src/context/PlayerDataContext.tsx
import { createContext, useContext, useState, useEffect } from "react";
import { fetchAllPlayerStats } from "../api/api";
import type { PlayerStats } from "../types/PlayerStats";

type PlayerDataContextType = {
  data: PlayerStats[] | null;
  loading: boolean;
};

const PlayerDataContext = createContext<PlayerDataContextType>({
  data: null,
  loading: true,
});

export function PlayerDataProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [data, setData] = useState<PlayerStats[] | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!data) {
      fetchAllPlayerStats()
        .then(setData)
        .catch((error) => {
          console.error("Failed to fetch player stats:", error);
          setData(null);
        })
        .finally(() => setLoading(false)); // ✅ make sure this runs even on error
    }
  }, []);

  return (
    <PlayerDataContext.Provider value={{ data, loading }}>
      {children}
    </PlayerDataContext.Provider>
  );
}

export function usePlayerData() {
  return useContext(PlayerDataContext);
}
