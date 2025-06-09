export function getPlayerIcons(stats: {
  averageKr: number;
  averageKd: number;
  winRate: number;
  averageHsPercent: number;
  winstreak: number;
}): string[] {
  const icons: string[] = [];

  if (stats.averageKr <= 0.5) icons.push("trash");
  if (stats.averageKr >= 0.9) icons.push("skull");
  if (stats.averageKr >= 1.0) icons.push("donk");
  if (stats.averageKd >= 1.4) icons.push("star");
  if (stats.winstreak >= 5) icons.push("fire");
  if (stats.winRate >= 70) icons.push("trophy");
  if (stats.averageHsPercent >= 60) icons.push("headshot");
  if (stats.winRate <= 30) icons.push("poop");
  if (stats.averageKd > 1.2 && stats.averageKr < 0.7) icons.push("bait");

  return icons;
}
