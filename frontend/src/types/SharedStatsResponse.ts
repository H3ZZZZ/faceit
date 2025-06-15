export type SharedMapStatsDTO = {
  map: string;
  matches: number;
  wins: number;
  losses: number;
  winrate: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
  totalEloGain: number;
  longestWinStreak: number;
  longestLossStreak: number;
  last5Results: string[]; // e.g. ["W", "L", "W", "W", "L"]
};
export interface SharedPlayerStatsDTO {
  nickname: string;
  avatar: string;
  skillLevel: number;
  faceitElo: number;
  country: string;
  totalMatches: number;
  wins: number;
  losses: number;
  winrate: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
  totalEloGain: number;
  mapStats: SharedMapStatsDTO[];
  longestWinStreak: number;
  longestLossStreak: number;
  last5Results: string[]; // e.g. ["W", "L", "W", "W", "L"]
}
