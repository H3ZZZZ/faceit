// src/components/LoadError.tsx
export default function LoadError({ message }: { message?: string }) {
  return (
    <div className="text-red-500 p-6 text-center">
      {message || "Failed to load data."}
    </div>
  );
}
