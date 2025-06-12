const ICON_RULES: { icon: string; label: string; rule: string }[] = [
  {
    icon: "trash",
    label: "Trash",
    rule: "KR ≤ 0.50 — low impact per round (qAYKE level)",
  },
  {
    icon: "skull",
    label: "Skull",
    rule: "KR ≥ 0.90 — strong kill output",
  },
  {
    icon: "donk",
    label: "Donk",
    rule: "KR ≥ 1.00 — top-tier impact (Donk-level)",
  },
  {
    icon: "star",
    label: "Star",
    rule: "KD ≥ 1.40 — high kill/death ratio",
  },
  {
    icon: "fire",
    label: "On Fire",
    rule: "Win streak ≥ 5 — currently on fire",
  },
  {
    icon: "trophy",
    label: "Trophy",
    rule: "Win rate ≥ 70% — winner mindset",
  },
  {
    icon: "headshot",
    label: "Headshot",
    rule: "HS% ≥ 60% — headshot machine",
  },
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

export default function IconGallery() {
  return (
    <main className="min-h-screen bg-[#121212] p-10 text-white">
      <h1 className="text-2xl font-bold mb-6 text-center">Teamspeak icon rules</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {ICON_RULES.map(({ icon, label, rule }) => (
          <div
            key={icon}
            className="bg-[#1e1e1e] p-4 rounded-lg shadow flex items-start gap-4"
          >
            <img
              src={`/icons/${icon}.png`}
              alt={label}
              className="w-12 h-12 object-contain"
            />
            <div>
              <h2 className="font-semibold text-lg">{label}</h2>
              <p className="text-sm text-gray-300">{rule}</p>
            </div>
          </div>
        ))}
      </div>
    </main>
  );
}
