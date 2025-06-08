export function getPlayerIcons(stats: {
  averageKr: number;
  averageKd: number;
  winRate: number;
  averageHsPercent: number;
  winstreak: number;
}): string[] {
  const icons: string[] = [];

  if (stats.averageKr <= 0.5) icons.push("🗑️");
  if (stats.averageKr >= 0.9) icons.push("💀");
  if (stats.averageKr >= 1.0) icons.push("🐐");
  if (stats.averageKd >= 1.4) icons.push("⭐");
  if (stats.winstreak >= 5) icons.push("🔥");
  if (stats.winRate >= 70) icons.push("🏆");
  if (stats.averageHsPercent >= 60) icons.push("🎯");
  if (stats.winRate <= 30) icons.push("💩");
  if (stats.averageKd > 1.2 && stats.averageKr < 0.7) icons.push("🪝");

  return icons;
}
