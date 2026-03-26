import { useEffect, useMemo, useState } from "react";
import dayjs from "dayjs";
import { useSearchParams } from "react-router-dom";
import { fetchGogoLanEvent } from "../api/api";
import type {
  EventDayStats,
  EventMapSummary,
  EventPlayerStats,
  EventQueueCombo,
  GogoLanEvent,
} from "../types/GogoLanEvent";

const CACHE_KEY = "gogo_lan_event_cache";
const CACHE_DURATION = 5 * 60 * 1000;

function signed(value: number) {
  return value > 0 ? `+${value}` : `${value}`;
}

function dailyTone(day: EventDayStats) {
  if (day.matchesPlayed === 0) return "bg-[#2b2517] text-[#9f8a5b]";
  if (day.totalEloGain > 0) return "bg-emerald-500/20 text-emerald-300";
  if (day.totalEloGain < 0) return "bg-rose-500/20 text-rose-300";
  return "bg-slate-500/20 text-slate-300";
}

function stackLabel(size: number) {
  if (size === 2) return "Duos";
  if (size === 3) return "Trios";
  if (size === 4) return "4-Stacks";
  if (size === 5) return "5-Stacks";
  return `${size}-Stacks`;
}

function StatChip({
  label,
  value,
  tone = "text-white",
}: {
  label: string;
  value: string;
  tone?: string;
}) {
  return (
    <div className="rounded-2xl border border-white/8 bg-white/4 px-3 py-2">
      <div className="text-[10px] uppercase tracking-[0.24em] text-[#8d8d8d]">
        {label}
      </div>
      <div className={`mt-1 text-sm font-semibold ${tone}`}>{value}</div>
    </div>
  );
}

function AdvancedChip({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="rounded-2xl border border-[#f59e0b]/10 bg-[#1a140d] px-3 py-2">
      <div className="text-[10px] uppercase tracking-[0.22em] text-[#8d8d8d]">
        {label}
      </div>
      <div className="mt-1 text-sm font-semibold text-[#ffd089]">{value}</div>
    </div>
  );
}

function AdvancedGridChip({
  label,
  items,
}: {
  label: string;
  items: Array<{ label: string; value: string }>;
}) {
  return (
    <div className="rounded-2xl border border-[#f59e0b]/10 bg-[#1a140d] px-3 py-2">
      <div className="text-[10px] uppercase tracking-[0.22em] text-[#8d8d8d]">
        {label}
      </div>
      <div className="mt-2 grid grid-cols-5 gap-2 text-center">
        {items.map((item) => (
          <div key={item.label} className="min-w-0">
            <div className="text-[10px] uppercase tracking-[0.12em] text-[#8d8d8d]">
              {item.label}
            </div>
            <div className="mt-1 text-sm font-semibold text-[#ffd089]">
              {item.value}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

function PlayerLeaderboardCard({
  player,
  rank,
}: {
  player: EventPlayerStats;
  rank: number;
}) {
  return (
    <article className="rounded-[28px] border border-[#f59e0b]/15 bg-[linear-gradient(180deg,rgba(30,30,30,0.95),rgba(17,17,17,0.92))] p-5 shadow-[0_20px_60px_rgba(0,0,0,0.35)]">
      <div className="flex items-start justify-between gap-4">
        <div className="flex items-center gap-4">
          <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#2a2116] text-lg font-bold text-[#ffb44c]">
            #{rank}
          </div>
          {player.avatar ? (
            <img
              src={player.avatar}
              alt={player.nickname}
              className="h-14 w-14 rounded-2xl object-cover"
            />
          ) : (
            <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-[#312519] text-lg font-semibold text-[#ffcf8a]">
              {player.nickname.slice(0, 2).toUpperCase()}
            </div>
          )}
          <div>
            <div className="flex items-center gap-2">
              <h3 className="text-xl font-semibold text-white">
                {player.nickname}
              </h3>
              {player.country ? (
                <img
                  src={`https://flagcdn.com/24x18/${player.country.toLowerCase()}.png`}
                  alt={player.country}
                  className="h-3 w-4 rounded-[2px] object-cover"
                />
              ) : null}
            </div>
            <div className="mt-1 flex items-center gap-2 text-xs text-[#b4b4b4]">
              <span>ELO {player.faceitElo}</span>
              <img
                src={`/faceit-levels/${player.skillLevel}.png`}
                alt={`Level ${player.skillLevel}`}
                className="h-5 w-5 object-contain"
              />
              <span>{player.matchesPlayed} matches</span>
            </div>
          </div>
        </div>
        <div
          className={`rounded-full px-3 py-1 text-sm font-semibold ${
            player.totalEloGain >= 0
              ? "bg-emerald-500/15 text-emerald-300"
              : "bg-rose-500/15 text-rose-300"
          }`}
        >
          {signed(player.totalEloGain)} ELO
        </div>
      </div>

      <div className="mt-5 grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
        <StatChip label="Record" value={`${player.wins}-${player.losses}`} />
        <StatChip label="Win Rate" value={`${player.winrate}%`} />
        <StatChip label="K/D" value={player.avgKd.toFixed(2)} />
        <StatChip label="ADR" value={player.avgAdr.toFixed(1)} />
        <StatChip label="K/R" value={player.avgKr.toFixed(2)} />
        <StatChip label="HS" value={`${Math.round(player.avgHsPercent)}%`} />
        <StatChip
          label="Session Time"
          value={`${player.totalSessionHours.toFixed(1)}h`}
        />
        <StatChip
          label="Maps"
          value={`${player.mostPlayedMap ?? "-"} / ${player.bestMap ?? "-"}`}
        />
      </div>

      <div className="mt-5 grid gap-4 lg:grid-cols-[1.4fr_1fr]">
        <div className="rounded-2xl border border-white/8 bg-black/20 p-4">
          <div className="mb-3 flex items-center justify-between">
            <h4 className="text-sm font-semibold uppercase tracking-[0.24em] text-[#d1a55b]">
              Daily Run
            </h4>
            <div className="text-xs text-[#8d8d8d]">
              {player.lastMatchAt
                ? `Last game ${dayjs(player.lastMatchAt).format("MMM D HH:mm")}`
                : "No matches"}
            </div>
          </div>
          <div className="grid gap-2 sm:grid-cols-3 xl:grid-cols-6">
            {player.dailyStats.map((day) => (
              <div
                key={day.date}
                className={`rounded-2xl px-3 py-3 ${dailyTone(day)}`}
              >
                <div className="text-[11px] uppercase tracking-[0.16em]">
                  {dayjs(day.date).format("ddd D")}
                </div>
                <div className="mt-2 text-xl font-semibold">
                  {day.matchesPlayed}
                </div>
                <div className="text-xs">
                  {day.matchesPlayed === 0
                    ? "No games"
                    : `${day.wins}-${day.losses} | ${signed(day.totalEloGain)} ELO`}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="rounded-2xl border border-white/8 bg-black/20 p-4">
          <h4 className="text-sm font-semibold uppercase tracking-[0.24em] text-[#d1a55b]">
            Player Notes
          </h4>
          <div className="mt-3 space-y-3 text-sm text-[#d7d7d7]">
            <div>
              <span className="text-[#8d8d8d]">Best day:</span>{" "}
              {player.bestDay
                ? `${dayjs(player.bestDay.date).format("MMM D")} (${signed(
                    player.bestDay.totalEloGain,
                  )} ELO)`
                : "No data"}
            </div>
            <div>
              <span className="text-[#8d8d8d]">Worst day:</span>{" "}
              {player.worstDay
                ? `${dayjs(player.worstDay.date).format("MMM D")} (${signed(
                    player.worstDay.totalEloGain,
                  )} ELO)`
                : "No data"}
            </div>
            <div>
              <span className="text-[#8d8d8d]">Best map:</span>{" "}
              {player.bestMap ?? "No data"}
            </div>
            <div>
              <span className="text-[#8d8d8d]">Worst map:</span>{" "}
              {player.worstMap ?? "No data"}
            </div>
            <div>
              <span className="text-[#8d8d8d]">K/A/D:</span> {player.kavg}/
              {player.aavg}/{player.davg}
            </div>
            <div className="flex gap-1 pt-1">
              {player.last5Results.length > 0 ? (
                player.last5Results.map((result, index) => (
                  <span
                    key={`${player.playerId}-${index}`}
                    className={`rounded px-2 py-1 text-xs font-bold ${
                      result === "W"
                        ? "bg-emerald-500/20 text-emerald-300"
                        : "bg-rose-500/20 text-rose-300"
                    }`}
                  >
                    {result}
                  </span>
                ))
              ) : (
                <span className="text-xs text-[#8d8d8d]">No recent results</span>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="mt-4 rounded-2xl border border-[#f59e0b]/10 bg-black/15 p-4">
        <div className="mb-3 flex items-center justify-between">
          <h4 className="text-sm font-semibold uppercase tracking-[0.24em] text-[#d1a55b]">
            Advanced LAN Stats
          </h4>
          <div className="text-xs text-[#8d8d8d]">
            Match-level v4 stats
          </div>
        </div>
        <div className="grid gap-3 sm:grid-cols-2 xl:grid-cols-5">
          <AdvancedChip
            label="Entries"
            value={`${player.entryWins}/${player.entryCount} won | ${Math.round(
              player.entrySuccessRate,
            )}% win | ${Math.round(player.entryAttemptRate)}% att.`}
          />
          <AdvancedChip
            label="Clutches"
            value={`${player.clutchWins} wins | ${player.clutchKills} kills`}
          />
          <AdvancedChip
            label="MVP / AWP"
            value={`${player.mvps} MVPs | ${player.sniperKills} AWP`}
          />
          <AdvancedGridChip
            label="Pistols / Total Multi Kills"
            items={[
              { label: "Pistol", value: `${player.pistolKills}` },
              { label: "2K", value: `${player.doubleKills}` },
              { label: "3K", value: `${player.tripleKills}` },
              { label: "4K", value: `${player.quadroKills}` },
              { label: "ACE", value: `${player.pentaKills}` },
            ]}
          />
          <AdvancedChip
            label="Flash / Util"
            value={`${player.enemiesFlashedPerRound.toFixed(
              2,
            )} flashed/r | ${player.utilityDamagePerRound.toFixed(
              2,
            )} util/r`}
          />
        </div>
      </div>
    </article>
  );
}

function AwardCard({
  title,
  winner,
  value,
  description,
}: GogoLanEvent["awards"][number]) {
  return (
    <article className="rounded-[24px] border border-[#c98d2d]/15 bg-[#16120d] p-4">
      <div className="text-[11px] uppercase tracking-[0.24em] text-[#d7a04b]">
        {title}
      </div>
      <div className="mt-2 text-lg font-semibold text-white">{winner}</div>
      <div className="mt-1 text-sm font-medium text-[#ffd18a]">{value}</div>
      <p className="mt-2 text-sm text-[#a7a7a7]">{description}</p>
    </article>
  );
}

function QueueSection({
  combos,
  size,
}: {
  combos: EventQueueCombo[];
  size: number;
}) {
  const filtered = combos.filter((combo) => combo.lineupSize === size).slice(0, 6);
  if (filtered.length === 0) return null;

  return (
    <section className="rounded-[28px] border border-white/8 bg-[#131313] p-5">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold text-white">{stackLabel(size)}</h3>
        <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
          Shared queue records
        </div>
      </div>
      <div className="mt-4 grid gap-3 lg:grid-cols-2">
        {filtered.map((combo) => (
          <div
            key={combo.playerNicknames.join("|")}
            className="rounded-2xl border border-white/6 bg-white/3 p-4"
          >
            <div className="text-sm font-semibold text-white">
              {combo.playerNicknames.join(" + ")}
            </div>
            <div className="mt-2 grid grid-cols-4 gap-2 text-sm">
              <StatChip label="Matches" value={`${combo.matchesPlayed}`} />
              <StatChip label="Record" value={`${combo.wins}-${combo.losses}`} />
              <StatChip label="Win%" value={`${combo.winrate}%`} />
              <StatChip
                label="ELO"
                value={signed(combo.totalEloGain)}
                tone={
                  combo.totalEloGain >= 0 ? "text-emerald-300" : "text-rose-300"
                }
              />
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

function MapLeaderboard({ maps }: { maps: EventMapSummary[] }) {
  return (
    <section className="rounded-[28px] border border-white/8 bg-[#131313] p-5">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold text-white">LAN Map Meta</h3>
        <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
          All player matches combined
        </div>
      </div>
      <div className="mt-4 overflow-x-auto">
        <table className="min-w-full text-left text-sm">
          <thead className="text-[#8d8d8d]">
            <tr className="border-b border-white/8">
              <th className="pb-3 pr-4">Map</th>
              <th className="pb-3 pr-4">Matches</th>
              <th className="pb-3 pr-4">Record</th>
              <th className="pb-3 pr-4">Win%</th>
              <th className="pb-3 pr-4">K/D</th>
              <th className="pb-3 pr-4">K/R</th>
              <th className="pb-3 pr-4">ADR</th>
              <th className="pb-3">ELO</th>
            </tr>
          </thead>
          <tbody>
            {maps.map((map) => (
              <tr key={map.map} className="border-b border-white/5 text-white">
                <td className="py-3 pr-4 font-medium">{map.map}</td>
                <td className="py-3 pr-4">{map.matchesPlayed}</td>
                <td className="py-3 pr-4">
                  {map.wins}-{map.losses}
                </td>
                <td className="py-3 pr-4">{map.winrate}%</td>
                <td className="py-3 pr-4">{map.avgKd.toFixed(2)}</td>
                <td className="py-3 pr-4">{map.avgKr.toFixed(2)}</td>
                <td className="py-3 pr-4">{map.avgAdr.toFixed(1)}</td>
                <td
                  className={`py-3 ${
                    map.totalEloGain >= 0 ? "text-emerald-300" : "text-rose-300"
                  }`}
                >
                  {signed(map.totalEloGain)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}

export default function GogoLanPage() {
  const [searchParams] = useSearchParams();
  const [event, setEvent] = useState<GogoLanEvent | null>(null);
  const [loading, setLoading] = useState(true);
  const start = searchParams.get("start") ?? undefined;
  const end = searchParams.get("end") ?? undefined;
  const cacheKey = `${CACHE_KEY}:${start ?? "default"}:${end ?? "default"}`;

  useEffect(() => {
    const cached = localStorage.getItem(cacheKey);
    if (cached) {
      const { timestamp, payload } = JSON.parse(cached);
      if (Date.now() - timestamp < CACHE_DURATION) {
        setEvent(payload);
        setLoading(false);
        return;
      }
    }

    fetchGogoLanEvent(start, end)
      .then((payload) => {
        setEvent(payload);
        localStorage.setItem(
          cacheKey,
          JSON.stringify({ timestamp: Date.now(), payload }),
        );
      })
      .catch(() => setEvent(null))
      .finally(() => setLoading(false));
  }, [cacheKey, end, start]);

  const queueSizes = useMemo(() => {
    if (!event) return [];
    return Array.from(
      new Set(event.queueCombos.map((combo) => combo.lineupSize)),
    ).sort((a, b) => a - b);
  }, [event]);

  if (loading) {
    return <div className="p-6 text-white">Loading Gogo LAN...</div>;
  }

  if (!event) {
    return (
      <div className="p-6 text-red-400">
        Failed to load Gogo LAN data from the backend.
      </div>
    );
  }

  return (
    <main className="space-y-6 text-white">
      <section className="overflow-hidden rounded-[32px] border border-[#e79b34]/15 bg-[radial-gradient(circle_at_top_left,_rgba(245,158,11,0.22),_transparent_28%),linear-gradient(135deg,_#17120d,_#0d0d0d_60%)] p-6">
        <div className="grid gap-6 lg:grid-cols-[1.3fr_0.9fr]">
          <div>
            <div className="text-[11px] uppercase tracking-[0.28em] text-[#e0b46a]">
              Event Page
            </div>
            <h1 className="mt-2 text-4xl font-semibold tracking-tight">
              {event.name}
            </h1>
            <p className="mt-3 max-w-2xl text-sm leading-6 text-[#c7c7c7]">
              LAN-only FACEIT tracking for {event.playerCount} players across{" "}
              {dayjs(event.startDate).format("MMMM D")} to{" "}
              {dayjs(event.endDate).format("MMMM D, YYYY")}. The page shows a
              live leaderboard, daily runs, map trends, awards, and who actually
              played together during the event.
            </p>
            {(start || end) && (
              <p className="mt-3 text-xs uppercase tracking-[0.2em] text-[#e0b46a]">
                Test window override active
              </p>
            )}
          </div>

          <div className="grid gap-3 sm:grid-cols-2">
            <StatChip label="Players" value={`${event.playerCount}`} />
            <StatChip label="Unique Matches" value={`${event.totalUniqueMatches}`} />
            <StatChip
              label="Window"
              value={`${dayjs(event.startDate).format("MMM D")} - ${dayjs(
                event.endDate,
              ).format("MMM D")}`}
            />
            <StatChip label="Timezone" value={event.zoneId} />
          </div>
        </div>
      </section>

      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold">LAN Leaderboard</h2>
          <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
            Ranked by ELO gain, then K/D
          </div>
        </div>
        <div className="space-y-4">
          {event.players.map((player, index) => (
            <PlayerLeaderboardCard
              key={player.playerId}
              player={player}
              rank={index + 1}
            />
          ))}
        </div>
      </section>

      <section className="rounded-[28px] border border-white/8 bg-[#131313] p-5">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold">LAN Awards</h2>
          <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
            Event-specific callouts
          </div>
        </div>
        <div className="mt-4 grid gap-4 md:grid-cols-2 xl:grid-cols-4">
          {event.awards.map((award) => (
            <AwardCard key={award.title} {...award} />
          ))}
        </div>
      </section>

      <MapLeaderboard maps={event.mapLeaderboard} />

      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold">Played Together</h2>
          <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
            Every duo, trio and stack that shared matches
          </div>
        </div>
        {queueSizes.map((size) => (
          <QueueSection key={size} combos={event.queueCombos} size={size} />
        ))}
      </section>
    </main>
  );
}
