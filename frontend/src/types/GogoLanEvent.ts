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
  sessionMinutes?: number;
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
  totalSessionMinutes?: number;
  lastMatchAt: string | null;
  mostPlayedMap: string | null;
  bestMap: string | null;
  worstMap: string | null;
  entryCount: number;
  entryWins: number;
  entrySuccessRate: number;
  entryAttemptRate: number;
  clutchWins: number;
  clutch1v1Wins: number;
  clutch1v2Wins: number;
  clutchKills: number;
  mvps: number;
  mvpsPerGame: number;
  sniperKills: number;
  sniperKillsPerGame: number;
  pistolKills: number;
  pistolKillsPerGame: number;
  doubleKills: number;
  tripleKills: number;
  quadroKills: number;
  pentaKills: number;
  multiKillRounds: number;
  multiKillRoundsPerGame: number;
  flashSuccesses: number;
  enemiesFlashed: number;
  enemiesFlashedPerRound: number;
  flashSuccessRate: number;
  utilityDamage: number;
  utilitySuccesses: number;
  utilityCount: number;
  roundsPlayed: number;
  utilityDamagePerRound: number;
  bestDay: EventDayStats | null;
  worstDay: EventDayStats | null;
  dailyStats: EventDayStats[];
  mapStats: EventMapStats[];
};

export type EventAwardWinner = {
  playerId: string;
  nickname: string;
  avatar: string | null;
};

export type EventAward = {
  title: string;
  winner: string;
  winners?: EventAwardWinner[];
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
