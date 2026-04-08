// src/context/PlayerDataContext.tsx
import { createContext, useContext, useEffect, useRef, useState } from "react";
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
  const hasStartedFetch = useRef(false);

  useEffect(() => {
    if (hasStartedFetch.current) return;

    hasStartedFetch.current = true;

    fetchAllPlayerStats()
      .then(setData)
      .catch((error) => {
        console.error("Failed to fetch player stats:", error);
        setData(null);
      })
      .finally(() => setLoading(false));
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
