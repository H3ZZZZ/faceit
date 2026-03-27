import { useEffect, useMemo, useRef, useState } from "react";
import dayjs from "dayjs";
import { FaCheck, FaCopy, FaTimes } from "react-icons/fa";

import { getIconRule, getPlayerIcons } from "../utils/icons";
import { fetchAdvancedEvent, searchFaceitPlayers } from "../api/api";
import type {
  EventDayStats,
  EventMapSummary,
  EventPlayerStats,
  EventQueueCombo,
  GogoLanEvent,
} from "../types/GogoLanEvent";
import type { FaceitPlayerSuggestion } from "../types/FaceitPlayerSuggestion";

const CACHE_KEY = "gogo_lan_event_cache_v2";
const CACHE_DURATION = 5 * 60 * 1000;

type RankByOption = "elo" | "kd" | "kr" | "adr" | "winrate" | "matches";

const RANK_BY_OPTIONS: Array<{ value: RankByOption; label: string; helper: string }> = [
  { value: "elo", label: "ELO gain", helper: "ELO gain, then K/D" },
  { value: "kd", label: "K/D", helper: "K/D, then K/R" },
  { value: "kr", label: "K/R", helper: "K/R, then K/D" },
  { value: "adr", label: "ADR", helper: "ADR, then K/R" },
  { value: "winrate", label: "Win rate", helper: "Win rate, then matches" },
  { value: "matches", label: "Matches played", helper: "Matches, then ELO gain" },
];

const CREW_PLAYERS: FaceitPlayerSuggestion[] = [
  { playerId: "1d51deb3-3f4a-4d9e-8494-bf8d5e280341", nickname: "H3ZZ", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "86df81a4-ecd1-439c-a717-7efff943f3b2", nickname: "Sinzey", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "a0595bec-7399-4f27-a124-9a0886dbb59d", nickname: "TeggaaFxD", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "cabee4bd-4c52-4ef6-80d9-ceced8206e12", nickname: "sladesh", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "d0494eda-13bf-4d0a-801b-69a8aad4ded4", nickname: "Kirzten", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "a63130d8-d99a-4699-947e-8404b7ab2722", nickname: "Clawr", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "f69d1f29-c0ea-42b9-80b4-3c5640a8be15", nickname: "Tylle", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "14ba6328-38e5-4716-a981-21ecf703116c", nickname: "AnkjaerL", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "2202482c-eec3-4fa8-9286-aa573cb6dc34", nickname: "Jipsii", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "3bbbd6ea-449f-4a8c-8de0-10e5aa259718", nickname: "pace", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "49ec6cd2-96ee-4952-b723-cd5c8c97ff31", nickname: "qAYKE", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "59adcce7-fd9f-4d91-bb3d-08a3a71aeadb", nickname: "RalleR", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "259ef4a5-e636-4e17-b6a4-ff2e1a728426", nickname: "RhoODo", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "91e5164d-866b-4797-95cc-5d6799b4dd5f", nickname: "Skejs", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "e377361b-c4fc-4439-8435-cee579d5fc96", nickname: "BacH-_-", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
  { playerId: "a322c7b9-48c3-48f1-93b3-d1e6b91e984b", nickname: "VireZ", avatar: null, country: "", skillLevel: 0, faceitElo: 0 },
];


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
  const icons =
    player.matchesPlayed > 0
      ? getPlayerIcons({
          averageKr: player.avgKr,
          averageKd: player.avgKd,
          winRate: player.winrate,
          averageHsPercent: player.avgHsPercent,
          winstreak:
            player.last5Results.length === 5 &&
            player.last5Results.every((result) => result === "W")
              ? 5
              : 0,
        })
      : [];

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
            {icons.length > 0 ? (
              <div className="mt-2 flex flex-wrap items-center gap-2">
                {icons.map((icon) => {
                  const { label, rule } = getIconRule(icon);

                  return (
                    <div
                      key={`${player.playerId}-${icon}`}
                      className="rounded-full border border-white/8 bg-white/4 p-1"
                      title={`${label}: ${rule}`}
                    >
                      <img src={`/icons/${icon}.png`} alt={label} className="h-5 w-5" />
                    </div>
                  );
                })}
              </div>
            ) : null}
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
                  {dayjs(day.date).format("ddd D MMM")}
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
            Map Stats
          </h4>
          <div className="mt-3 text-sm text-[#d7d7d7]">
            {player.mapStats.length > 0 ? (
              <div className="space-y-2">
                {player.mapStats.map((mapStat) => (
                  <div
                    key={mapStat.map}
                    className="rounded-xl border border-white/8 bg-white/4 px-3 py-2"
                  >
                    <div className="flex items-center justify-between gap-3">
                      <span className="font-semibold text-white">{mapStat.map}</span>
                      <span
                        className={
                          mapStat.totalEloGain >= 0
                            ? "text-emerald-300"
                            : "text-rose-300"
                        }
                      >
                        {signed(mapStat.totalEloGain)} ELO
                      </span>
                    </div>
                    <div className="mt-1 text-xs text-[#bdbdbd]">
                      {mapStat.matches} played | {mapStat.wins}-{mapStat.losses} | {mapStat.winrate}% WR | {mapStat.avgKd.toFixed(2)} K/D | {mapStat.avgKr.toFixed(2)} K/R | {mapStat.avgAdr.toFixed(1)} ADR | {Math.round(mapStat.avgHsPercent)}% HS
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              "No map data"
            )}
          </div>
        </div>
      </div>

      <div className="mt-4 rounded-2xl border border-[#f59e0b]/10 bg-black/15 p-4">
        <div className="mb-3 flex items-center justify-between">
          <h4 className="text-sm font-semibold uppercase tracking-[0.24em] text-[#d1a55b]">
            Custom Squad Match Stats
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
  winners,
  value,
  description,
}: GogoLanEvent["awards"][number]) {
  const displayWinners = winners?.length ? winners : [{ playerId: winner, nickname: winner, avatar: null }];

  return (
    <article className="rounded-[24px] border border-[#c98d2d]/15 bg-[#16120d] p-4">
      <div className="text-[11px] uppercase tracking-[0.24em] text-[#d7a04b]">
        {title}
      </div>
      <div className="mt-3 flex flex-wrap gap-3">
        {displayWinners.map((awardWinner) => (
          <div
            key={awardWinner.playerId}
            className="flex items-center gap-3 rounded-2xl border border-white/8 bg-white/4 px-3 py-2"
          >
            {awardWinner.avatar ? (
              <img
                src={awardWinner.avatar}
                alt={awardWinner.nickname}
                className="h-10 w-10 rounded-xl object-cover"
              />
            ) : (
              <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-[#312519] text-sm font-semibold text-[#ffcf8a]">
                {awardWinner.nickname.slice(0, 2).toUpperCase()}
              </div>
            )}
            <div className="text-sm font-semibold text-white">
              {awardWinner.nickname}
            </div>
          </div>
        ))}
      </div>
      <div className="mt-3 text-sm font-medium text-[#ffd18a]">{value}</div>
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
        <h3 className="text-lg font-semibold text-white">Map Meta</h3>
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

export default function AdvancedPage() {
  const today = dayjs().format("YYYY-MM-DD");
  const thirtyDaysAgo = dayjs().subtract(29, "day").format("YYYY-MM-DD");
  const initialLoadDone = useRef(false);

  const initialState = useMemo(() => {
    const params = typeof window !== "undefined"
      ? new URLSearchParams(window.location.search)
      : new URLSearchParams();
    const urlStart = params.get("start") ?? thirtyDaysAgo;
    const urlEnd = params.get("end") ?? today;
    const playerIds = (params.get("players") ?? "")
      .split(",")
      .map((value) => value.trim())
      .filter(Boolean);

    const players = playerIds.map((playerId) => {
      const preset = CREW_PLAYERS.find((player) => player.playerId === playerId);
      return (
        preset ?? {
          playerId,
          nickname: playerId.slice(0, 8),
          avatar: null,
          country: "",
          skillLevel: 0,
          faceitElo: 0,
        }
      );
    });

    return {
      startDate: urlStart,
      endDate: urlEnd,
      players,
      shouldAutoload: players.length > 0,
    };
  }, [thirtyDaysAgo, today]);

  const [selectedPlayers, setSelectedPlayers] = useState<FaceitPlayerSuggestion[]>(initialState.players);
  const [playerQuery, setPlayerQuery] = useState("");
  const [suggestions, setSuggestions] = useState<FaceitPlayerSuggestion[]>([]);
  const [event, setEvent] = useState<GogoLanEvent | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [copied, setCopied] = useState(false);
  const [rankBy, setRankBy] = useState<RankByOption>("elo");
  const [startDate, setStartDate] = useState(initialState.startDate);
  const [endDate, setEndDate] = useState(initialState.endDate);

  useEffect(() => {
    const trimmed = playerQuery.trim();
    if (trimmed.length < 2) {
      setSuggestions([]);
      return;
    }

    const timeout = window.setTimeout(() => {
      searchFaceitPlayers(trimmed)
        .then((results) => {
          const selectedIds = new Set(selectedPlayers.map((player) => player.playerId));
          setSuggestions(results.filter((player) => !selectedIds.has(player.playerId)));
        })
        .catch(() => setSuggestions([]));
    }, 250);

    return () => window.clearTimeout(timeout);
  }, [playerQuery, selectedPlayers]);

  useEffect(() => {
    if (typeof window === "undefined") return;
    const params = new URLSearchParams();
    if (selectedPlayers.length > 0) {
      params.set(
        "players",
        selectedPlayers.map((player) => player.playerId).join(","),
      );
    }
    params.set("start", startDate);
    params.set("end", endDate);
    const query = params.toString();
    const nextUrl = `${window.location.pathname}${query ? `?${query}` : ""}`;
    window.history.replaceState(null, "", nextUrl);
  }, [endDate, selectedPlayers, startDate]);

  const queueSizes = useMemo(() => {
    if (!event) return [];
    return Array.from(
      new Set(event.queueCombos.map((combo) => combo.lineupSize)),
    ).sort((a, b) => a - b);
  }, [event]);

  const leaderboardPlayers = useMemo(() => {
    if (!event) return [];

    const valueFor = (player: EventPlayerStats, key: RankByOption) => {
      switch (key) {
        case "kd":
          return player.avgKd;
        case "kr":
          return player.avgKr;
        case "adr":
          return player.avgAdr;
        case "winrate":
          return player.winrate;
        case "matches":
          return player.matchesPlayed;
        case "elo":
        default:
          return player.totalEloGain;
      }
    };

    const tieBreakers: RankByOption[] = (() => {
      switch (rankBy) {
        case "kd":
          return ["kr", "elo", "matches"];
        case "kr":
          return ["kd", "elo", "matches"];
        case "adr":
          return ["kr", "kd", "elo"];
        case "winrate":
          return ["matches", "elo", "kd"];
        case "matches":
          return ["elo", "kd", "kr"];
        case "elo":
        default:
          return ["kd", "kr", "matches"];
      }
    })();

    return [...event.players].sort((left, right) => {
      const primary = valueFor(right, rankBy) - valueFor(left, rankBy);
      if (primary !== 0) return primary;

      for (const tieBreaker of tieBreakers) {
        const delta = valueFor(right, tieBreaker) - valueFor(left, tieBreaker);
        if (delta !== 0) return delta;
      }

      return left.nickname.localeCompare(right.nickname);
    });
  }, [event, rankBy]);

  const rankByLabel =
    RANK_BY_OPTIONS.find((option) => option.value === rankBy)?.helper ??
    "ELO gain, then K/D";

  const availableCrewPlayers = useMemo(() => {
    const selectedIds = new Set(selectedPlayers.map((player) => player.playerId));
    return CREW_PLAYERS.filter((player) => !selectedIds.has(player.playerId));
  }, [selectedPlayers]);

  const syncSelectedPlayers = (payload: GogoLanEvent) => {
    setSelectedPlayers(
      payload.players.map((player) => ({
        playerId: player.playerId,
        nickname: player.nickname,
        avatar: player.avatar,
        country: player.country,
        skillLevel: player.skillLevel,
        faceitElo: player.faceitElo,
      })),
    );
  };

  const addPlayer = (player: FaceitPlayerSuggestion) => {
    setSelectedPlayers((current) =>
      current.some((entry) => entry.playerId === player.playerId)
        ? current
        : [...current, player],
    );
    setPlayerQuery("");
    setSuggestions([]);
  };

  const removePlayer = (playerId: string) => {
    setSelectedPlayers((current) => current.filter((player) => player.playerId !== playerId));
  };

  const applyPreset = (preset: "7d" | "30d" | "month") => {
    const end = dayjs().format("YYYY-MM-DD");
    if (preset === "7d") {
      setStartDate(dayjs().subtract(6, "day").format("YYYY-MM-DD"));
      setEndDate(end);
      return;
    }
    if (preset === "30d") {
      setStartDate(dayjs().subtract(29, "day").format("YYYY-MM-DD"));
      setEndDate(end);
      return;
    }
    setStartDate(dayjs().startOf("month").format("YYYY-MM-DD"));
    setEndDate(end);
  };

  const loadAdvancedPage = async (
    playersOverride = selectedPlayers,
    startOverride = startDate,
    endOverride = endDate,
  ) => {
    if (playersOverride.length === 0) {
      setError("Add at least one player first.");
      return;
    }
    if (!startOverride || !endOverride) {
      setError("Choose both a start date and an end date.");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const payload = await fetchAdvancedEvent({
        playerIds: playersOverride.map((player) => player.playerId),
        startDate: startOverride,
        endDate: endOverride,
        name: "Custom Squad",
      });
      setEvent(payload);
      syncSelectedPlayers(payload);
    } catch {
      setError("Failed to load Custom Squad data from the backend.");
      setEvent(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (initialLoadDone.current) return;
    initialLoadDone.current = true;
    if (initialState.shouldAutoload) {
      void loadAdvancedPage(initialState.players, initialState.startDate, initialState.endDate);
    }
  }, [initialState]);

  const copyLink = async () => {
    if (typeof window === "undefined") return;
    await navigator.clipboard.writeText(window.location.href);
    setCopied(true);
    window.setTimeout(() => setCopied(false), 1800);
  };

  return (
    <main className="space-y-6 text-white">
      <section className="overflow-visible rounded-[32px] border border-[#e79b34]/15 bg-[radial-gradient(circle_at_top_left,_rgba(245,158,11,0.22),_transparent_28%),linear-gradient(135deg,_#17120d,_#0d0d0d_60%)] p-6">
        <div className="grid gap-6 lg:grid-cols-[1.2fr_1fr]">
          <div>
            <div className="text-[11px] uppercase tracking-[0.28em] text-[#e0b46a]">
              Custom Event Builder
            </div>
            <h1 className="mt-2 text-4xl font-semibold tracking-tight">
              Custom Squad
            </h1>
            <p className="mt-3 max-w-2xl text-sm leading-6 text-[#c7c7c7]">
              Build a custom FACEIT event page by choosing your own players and time window.
              It uses the same rich leaderboard, awards, map trends, and played-together stats as GOGO LAN.
            </p>
          </div>

          <div className="grid gap-3 sm:grid-cols-2">
            <StatChip label="Selected" value={`${selectedPlayers.length} players`} />
            <StatChip label="Window" value={`${startDate} to ${endDate}`} />
            <StatChip label="Typeahead" value="FACEIT nickname search" />
            <StatChip label="Share" value="URL + copy link" />
          </div>
        </div>

        <div className="mt-6 rounded-[28px] border border-white/8 bg-black/20 p-5">
          <div className="grid gap-4 lg:grid-cols-[1.4fr_0.6fr_0.6fr_auto] lg:items-end">
            <div className="relative">
              <label className="text-[11px] uppercase tracking-[0.22em] text-[#8d8d8d]">
                Add players
              </label>
              <input
                type="text"
                value={playerQuery}
                onChange={(event) => setPlayerQuery(event.target.value)}
                placeholder="Search FACEIT nickname"
                className="mt-2 w-full rounded-2xl border border-white/10 bg-[#111111] px-4 py-3 text-white outline-none transition focus:border-[#e79b34]/50"
              />
              {suggestions.length > 0 ? (
                <div className="absolute z-20 mt-2 w-full rounded-2xl border border-white/10 bg-[#111111] p-2 shadow-[0_20px_40px_rgba(0,0,0,0.35)]">
                  {suggestions.map((player) => (
                    <button
                      key={player.playerId}
                      type="button"
                      onClick={() => addPlayer(player)}
                      className="flex w-full items-center gap-3 rounded-xl px-3 py-2 text-left transition hover:bg-white/6"
                    >
                      {player.avatar ? (
                        <img src={player.avatar} alt={player.nickname} className="h-9 w-9 rounded-xl object-cover" />
                      ) : (
                        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-[#312519] text-sm font-semibold text-[#ffcf8a]">
                          {player.nickname.slice(0, 2).toUpperCase()}
                        </div>
                      )}
                      <div className="min-w-0">
                        <div className="truncate text-sm font-semibold text-white">{player.nickname}</div>
                        <div className="text-xs text-[#9b9b9b]">ELO {player.faceitElo} | Level {player.skillLevel}</div>
                      </div>
                    </button>
                  ))}
                </div>
              ) : null}
            </div>

            <div>
              <label className="text-[11px] uppercase tracking-[0.22em] text-[#8d8d8d]">
                Start date
              </label>
              <input
                type="date"
                value={startDate}
                onChange={(event) => setStartDate(event.target.value)}
                className="mt-2 w-full rounded-2xl border border-white/10 bg-[#111111] px-4 py-3 text-white outline-none transition focus:border-[#e79b34]/50"
              />
            </div>

            <div>
              <label className="text-[11px] uppercase tracking-[0.22em] text-[#8d8d8d]">
                End date
              </label>
              <input
                type="date"
                value={endDate}
                onChange={(event) => setEndDate(event.target.value)}
                className="mt-2 w-full rounded-2xl border border-white/10 bg-[#111111] px-4 py-3 text-white outline-none transition focus:border-[#e79b34]/50"
              />
            </div>

            <div className="flex gap-2 lg:flex-col">
              <button
                type="button"
                onClick={() => void copyLink()}
                className="flex items-center justify-center gap-2 rounded-2xl border border-white/10 bg-white/5 px-5 py-3 font-semibold text-white transition hover:bg-white/10"
              >
                {copied ? <FaCheck size={14} /> : <FaCopy size={14} />}
                {copied ? "Copied" : "Copy link"}
              </button>
              <button
                type="button"
                onClick={() => void loadAdvancedPage()}
                className="rounded-2xl bg-[#e79b34] px-5 py-3 font-semibold text-black transition hover:bg-[#f0ac4f]"
              >
                Build page
              </button>
            </div>
          </div>

          <div className="mt-4 flex flex-wrap gap-2">
            <span className="self-center text-[11px] uppercase tracking-[0.22em] text-[#8d8d8d]">
              Date presets
            </span>
            <button type="button" onClick={() => applyPreset("7d")} className="rounded-full border border-white/10 bg-white/5 px-3 py-2 text-sm text-white transition hover:bg-white/10">Last 7 days</button>
            <button type="button" onClick={() => applyPreset("30d")} className="rounded-full border border-white/10 bg-white/5 px-3 py-2 text-sm text-white transition hover:bg-white/10">Last 30 days</button>
            <button type="button" onClick={() => applyPreset("month")} className="rounded-full border border-white/10 bg-white/5 px-3 py-2 text-sm text-white transition hover:bg-white/10">This month</button>
          </div>

          <div className="mt-4 flex flex-wrap gap-2">
            <span className="self-center text-[11px] uppercase tracking-[0.22em] text-[#8d8d8d]">
              Quick add crew
            </span>
            {availableCrewPlayers.map((player) => (
              <button
                key={player.playerId}
                type="button"
                onClick={() => addPlayer(player)}
                className="rounded-full border border-[#e79b34]/20 bg-[#20150b] px-3 py-2 text-sm text-[#ffd18a] transition hover:bg-[#2c1d0e]"
              >
                {player.nickname}
              </button>
            ))}
          </div>

          <div className="mt-4 flex flex-wrap gap-2">
            {selectedPlayers.length > 0 ? (
              selectedPlayers.map((player) => (
                <button
                  key={player.playerId}
                  type="button"
                  onClick={() => removePlayer(player.playerId)}
                  className="flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-3 py-2 text-sm text-white transition hover:bg-white/10"
                >
                  <span>{player.nickname}</span>
                  <FaTimes size={12} className="text-[#9b9b9b]" />
                </button>
              ))
            ) : (
              <div className="text-sm text-[#9b9b9b]">No players selected yet.</div>
            )}
          </div>

          {error ? <div className="mt-4 text-sm text-rose-300">{error}</div> : null}
        </div>
      </section>

      {loading ? <div className="p-6 text-white">Loading Custom Squad...</div> : null}
      {!loading && !event ? (
        <div className="rounded-[28px] border border-white/8 bg-[#131313] p-6 text-sm text-[#bcbcbc]">
          Pick your players and date range above, then build the page.
        </div>
      ) : null}

      {!loading && event ? (
        <>
          <section className="space-y-4">
            <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
              <div>
                <h2 className="text-2xl font-semibold">Custom Squad Leaderboard</h2>
                <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
                  Ranked by {rankByLabel}
                </div>
              </div>
              <label className="flex items-center gap-3 self-start rounded-2xl border border-white/8 bg-[#131313] px-4 py-2 text-sm text-[#d7d7d7] sm:self-auto">
                <span className="uppercase tracking-[0.18em] text-[#8d8d8d]">Rank by</span>
                <select
                  value={rankBy}
                  onChange={(event) => setRankBy(event.target.value as RankByOption)}
                  className="bg-transparent text-white outline-none"
                >
                  {RANK_BY_OPTIONS.map((option) => (
                    <option key={option.value} value={option.value} className="bg-[#111111]">
                      {option.label}
                    </option>
                  ))}
                </select>
              </label>
            </div>
            <div className="space-y-4">
              {leaderboardPlayers.map((player, index) => (
                <PlayerLeaderboardCard key={player.playerId} player={player} rank={index + 1} />
              ))}
            </div>
          </section>

          <section className="rounded-[28px] border border-white/8 bg-[#131313] p-5">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-semibold">Custom Squad Awards</h2>
              <div className="text-xs uppercase tracking-[0.24em] text-[#7f7f7f]">
                Generated from your selected players
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
                Every duo, trio and stack in your custom event
              </div>
            </div>
            {queueSizes.map((size) => (
              <QueueSection key={size} combos={event.queueCombos} size={size} />
            ))}
          </section>
        </>
      ) : null}
    </main>
  );
}









