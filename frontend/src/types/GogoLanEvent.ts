export type EventDayStats = {
  date: string;
  matchesPlayed: number;
  wins: number;
  losses: number;
  winrate: number;
  totalEloGain: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
  kavg: number;
  aavg: number;
  davg: number;
  sessionHours: number;
  lastResults: string[];
};

export type EventMapStats = {
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
  kavg: number;
  aavg: number;
  davg: number;
  longestWinStreak: number;
  longestLossStreak: number;
  last5Results: string[];
};

export type EventPlayerStats = {
  playerId: string;
  nickname: string;
  avatar: string | null;
  country: string;
  skillLevel: number;
  faceitElo: number;
  matchesPlayed: number;
  wins: number;
  losses: number;
  winrate: number;
  totalEloGain: number;
  eloChange: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
  kavg: number;
  aavg: number;
  davg: number;
  longestWinStreak: number;
  longestLossStreak: number;
  last5Results: string[];
  totalSessionHours: number;
  lastMatchAt: string | null;
  mostPlayedMap: string | null;
  bestMap: string | null;
  worstMap: string | null;
  bestDay: EventDayStats | null;
  worstDay: EventDayStats | null;
  dailyStats: EventDayStats[];
  mapStats: EventMapStats[];
};

export type EventAward = {
  title: string;
  winner: string;
  value: string;
  description: string;
};

export type EventQueueCombo = {
  playerNicknames: string[];
  lineupSize: number;
  matchesPlayed: number;
  wins: number;
  losses: number;
  winrate: number;
  totalEloGain: number;
};

export type EventMapSummary = {
  map: string;
  matchesPlayed: number;
  wins: number;
  losses: number;
  winrate: number;
  avgKd: number;
  avgKr: number;
  avgAdr: number;
  avgHsPercent: number;
  totalEloGain: number;
};

export type GogoLanEvent = {
  key: string;
  name: string;
  zoneId: string;
  startDate: string;
  endDate: string;
  playerCount: number;
  totalUniqueMatches: number;
  players: EventPlayerStats[];
  awards: EventAward[];
  queueCombos: EventQueueCombo[];
  mapLeaderboard: EventMapSummary[];
};
