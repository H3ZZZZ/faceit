import type { PlayerStats } from "../types/PlayerStats";
import type { PlayerStatsFull } from "../types/PlayerStatsFull";
import type { SladeshSimple } from "../types/SladeshSimple";

const API_BASE = `${import.meta.env.VITE_API_BASE_URL}`;

const PLAYER_IDS = [
  "1d51deb3-3f4a-4d9e-8494-bf8d5e280341", // H3ZZ
  "86df81a4-ecd1-439c-a717-7efff943f3b2", // Sinzey
  "a0595bec-7399-4f27-a124-9a0886dbb59d", // Thomsen
  "26e6f329-cf09-4a99-8f95-6e89cdca076b", // Camilla
  "cabee4bd-4c52-4ef6-80d9-ceced8206e12", // Cav
  "d0494eda-13bf-4d0a-801b-69a8aad4ded4", // Lugi
  "a63130d8-d99a-4699-947e-8404b7ab2722", // Clawr
  "f69d1f29-c0ea-42b9-80b4-3c5640a8be15", // Tylle
  "14ba6328-38e5-4716-a981-21ecf703116c", // AnkjaerL
  "2202482c-eec3-4fa8-9286-aa573cb6dc34", // Jipsi
  "3bbbd6ea-449f-4a8c-8de0-10e5aa259718", // pace
  "49ec6cd2-96ee-4952-b723-cd5c8c97ff31", // qAYKE
  // "36b83767-a43b-46fa-bf35-6a406ac8ec27", // Dog2020
  // "e7893da0-ee0f-4e7f-ba10-65f0d2b965d8", // Harty
  // "efb4bfe3-5f08-4ac0-890b-0d0d8e5de38f", // Skitzoo
  "59adcce7-fd9f-4d91-bb3d-08a3a71aeadb", // Raller
  // "259ef4a5-e636-4e17-b6a4-ff2e1a728426", // Kasper
  // "91e5164d-866b-4797-95cc-5d6799b4dd5f", // Skejs
  // "e377361b-c4fc-4439-8435-cee579d5fc96", // BacH
  // "17d8ecb7-01b6-4cb9-b1db-7555c878ce6d", // Carlsson
];

export async function fetchAllPlayerStats(): Promise<PlayerStats[]> {
  const responses = await Promise.all(
    PLAYER_IDS.map((id) =>
      fetch(`${API_BASE}/stats/${id}`).then((res) => res.json())
    )
  );
  return responses;
}

export async function fetchPlayerByNickname(
  nickname: string
): Promise<PlayerStatsFull> {
  const res = await fetch(`${API_BASE}/lifetime-stats/by-nickname/${nickname}`);
  if (!res.ok) throw new Error("Player not found");
  return await res.json();
}

export async function fetchSladeshSimple(): Promise<SladeshSimple> {
  const res = await fetch(`${API_BASE}/stats/sladesh/simple`);
  if (!res.ok) throw new Error("Failed to fetch Sladesh simple data");
  return await res.json();
}

export async function fetchSharedStats(nicknames: string[]) {
  const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/squad-stats`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(nicknames),
  });

  if (!res.ok) throw new Error("Failed to fetch shared stats");

  return res.json();
}
