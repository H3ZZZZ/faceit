// src/pages/IconGallery.tsx
const ICONS = [
  "trash",
  "skull",
  "donk",
  "star",
  "fire",
  "trophy",
  "headshot",
  "poop",
  "bait"
];

export default function IconGallery() {
  return (
    <main className="min-h-screen bg-[#121212] p-10 text-white">
      <h1 className="text-2xl font-bold mb-6 text-center">Icon Preview</h1>
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-6 place-items-center">
        {ICONS.map((icon) => (
          <div key={icon} className="flex flex-col items-center gap-2">
            <img
              src={`/src/assets/icons/${icon}.png`}
              alt={icon}
              className="w-12 h-12 object-contain"
            />
            <span className="text-sm">{icon}</span>
          </div>
        ))}
      </div>
    </main>
  );
}
