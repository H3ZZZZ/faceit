import type { PlayerStatsFull } from "../../types/PlayerStatsFull";

type Props = {
  data: PlayerStatsFull;
};

export default function PlayerHeader({ data }: Props) {
  return (
    <div className="flex items-center gap-4 mb-4">
      <img
        src={data.avatar}
        alt="avatar"
        className="w-14 h-14 rounded-full border"
      />
      <div>
        <h2 className="text-xl font-bold">{data.nickname}</h2>
        <p className="text-sm text-gray-400">
          ELO: {data.faceitElo} (lvl {data.skillLevel})
        </p>
      </div>
    </div>
  );
}
