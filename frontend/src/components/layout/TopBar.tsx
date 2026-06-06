import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/useAuth";

interface TopBarProps {
  searchPlaceholder?: string;
  onSearch?: (term: string) => void;
}

const TopBar = ({ searchPlaceholder, onSearch }: TopBarProps) => {
  const { user } = useAuth();
  const [searchTerm, setSearchTerm] = useState("");

  const displayName = user?.nombre ?? "Administrador";
  const initials = displayName
    .split(" ")
    .filter(Boolean)
    .map((part) => part[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

  useEffect(() => {
    if (onSearch) {
      onSearch(searchTerm);
    }
  }, [searchTerm, onSearch]);

  return (
    <header className="bg-white flex justify-between items-center w-full px-6 py-4 sticky top-0 z-40 shadow-sm border-b border-slate-100">
      <div className="flex items-center gap-4 flex-1">
        <div className="relative w-full max-w-md">
          <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-lg">
            search
          </span>
          <input
            type="text"
            value={searchTerm}
            onChange={(event) => setSearchTerm(event.target.value)}
            placeholder={searchPlaceholder ?? "Buscar..."}
            className="w-full bg-slate-100 border-none rounded-full py-3 pl-10 pr-4 text-sm focus:ring-2 focus:ring-violet-300 focus:outline-none"
          />
        </div>
      </div>

      <div className="flex items-center gap-4">
        <div className="text-right hidden sm:block">
          <p className="text-xs font-bold text-slate-800">Administrador</p>
          <p className="text-[10px] text-slate-500">Usuario autenticado</p>
        </div>

        <div className="w-10 h-10 rounded-full bg-violet-100 flex items-center justify-center text-violet-700 font-bold text-sm border border-violet-200">
          {initials || "GS"}
        </div>
      </div>
    </header>
  );
};

export default TopBar;
