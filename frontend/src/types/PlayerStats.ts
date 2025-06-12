export type StatSegment = {
  averageKd: number;
  averageKr: number;
  averageAdr: number;
  averageHsPercent: number;
  winRate: number;
  matchesPlayed: number;
  wins: number;
  losses: number;
  eloChange: number;
  winStreakCount: number;
  last5Results: string[];
  kavg: number;
  aavg: number;
  davg: number;
};

export type PlayerStats = {
  playerId: string;
  nickname: string;
  avatar: string;
  country: string;
  skillLevel: number;
  faceitElo: number;
  lastActive: number;
  last10: StatSegment;
  last30: StatSegment;
  last50: StatSegment;
  last100: StatSegment;
};
