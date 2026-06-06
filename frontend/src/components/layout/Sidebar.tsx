import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const navItems = [
  { label: "Dashboard", path: "/resumen", icon: "dashboard" },
  { label: "Inventario", path: "/inventario", icon: "inventory_2" },
  { label: "Categorías", path: "/categorias", icon: "category" },
  { label: "Alertas Stock", path: "/alertas", icon: "warning", iconClass: "text-red-400" },
];

const Sidebar = () => {
  const location = useLocation();
  const { user, logout } = useAuth();

  const currentPath = location.pathname;
  const isActive = (path: string) => currentPath === path || (path !== "/resumen" && currentPath.startsWith(path));

  const userName = user?.nombre ?? "Administrador";

  return (
    <aside className="hidden lg:flex flex-col h-screen w-72 fixed left-0 top-0 bg-slate-900 shadow-2xl z-50 py-6 overflow-y-auto">
      <div className="px-6 mb-10 flex items-center gap-3">
        <div className="w-10 h-10 rounded-xl bg-violet-600 flex items-center justify-center">
          <span className="material-symbols-outlined text-white">diamond</span>
        </div>

        <div>
          <h2 className="text-white text-xl font-black tracking-tight">Gata Shoes</h2>
          <p className="text-violet-200 text-xs opacity-70">{userName}</p>
        </div>
      </div>

      <nav className="flex-1 space-y-1 px-2">
        {navItems.map((item) => {
          const active = isActive(item.path);
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center gap-3 text-sm px-6 py-3 transition-all ${
                active
                  ? "bg-violet-600/20 text-white border-r-4 border-violet-400 font-bold"
                  : "text-slate-300 hover:bg-white/5"
              }`}
            >
              <span className={`material-symbols-outlined ${item.iconClass ?? ""}`}>{item.icon}</span>
              {item.label}
            </Link>
          );
        })}
      </nav>

      <div className="mt-auto px-6 pt-4">
        <button
          type="button"
          onClick={logout}
          className="w-full text-slate-300 px-4 py-3 flex items-center justify-center gap-3 hover:bg-white/10 rounded-xl transition-all text-sm"
        >
          <span className="material-symbols-outlined">logout</span>
          Cerrar Sesión
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
