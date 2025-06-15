export type SharedMatch = {
  matchId: string;
  date: number;
  map: string;
  result: string;
  eloGain: number;
  kd: number;
  kr: number;
  adr: number;
};

export type SharedStatsPlayer = {
  nickname: string;
  matches: number;
  totalEloGain: number;
  winrate: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
};

export type SharedStatsResponse = {
  matchCount: number;
  perPlayer: SharedStatsPlayer[];
  global: {
    winrate: number;
    avgKd: number;
    avgKr: number;
    avgAdr: number;
    totalEloGain: number;
  };
};
