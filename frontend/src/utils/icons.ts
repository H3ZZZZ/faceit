export function getPlayerIcons(stats: {
  averageKr: number;
  averageKd: number;
  winRate: number;
  averageHsPercent: number;
  winstreak: number;
}): string[] {
  const icons: string[] = [];

  if (stats.averageKr <= 0.5) icons.push("trash");
  if (stats.averageKr < 1 && stats.averageKr >= 0.9) icons.push("skull");
  if (stats.averageKr >= 1.0) icons.push("donk");
  if (stats.averageKd >= 1.4) icons.push("star");
  if (stats.winstreak >= 5) icons.push("fire");
  if (stats.winRate >= 70) icons.push("trophy");
  if (stats.averageHsPercent >= 60) icons.push("headshot");
  if (stats.winRate <= 30) icons.push("poop");
  if (stats.averageKd > 1.2 && stats.averageKr < 0.7) icons.push("bait");

  return icons;
}

export const ICON_RULES: { icon: string; label: string; rule: string }[] = [
  {
    icon: "trash",
    label: "Trash",
    rule: "KR ≤ 0.50 — low impact per round (qAYKE level)",
  },
  { icon: "skull", label: "Skull", rule: "KR ≥ 0.90 — strong kill output" },
  {
    icon: "donk",
    label: "Donk",
    rule: "KR ≥ 1.00 — top-tier impact (Donk-level)",
  },
  { icon: "star", label: "Star", rule: "KD ≥ 1.40 — high kill/death ratio" },
  {
    icon: "fire",
    label: "On Fire",
    rule: "Win streak ≥ 5 — currently on fire",
  },
  { icon: "trophy", label: "Trophy", rule: "Win rate ≥ 70% — winner mindset" },
  { icon: "headshot", label: "Headshot", rule: "HS% ≥ 60% — headshot machine" },
  {
    icon: "poop",
    label: "Poop",
    rule: "Win rate ≤ 30% — rough results lately",
  },
  {
    icon: "bait",
    label: "Bait",
    rule: "KD > 1.2 and KR < 0.7 — possible baiter",
  },
];

export const getIconRule = (icon: string) =>
  ICON_RULES.find((r) => r.icon === icon) ?? {
    icon,
    label: icon,
    rule: "No description available",
  };
