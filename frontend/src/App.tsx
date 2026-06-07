import { BrowserRouter, Routes, Route, Navigate, Outlet } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import LoginPage from "./pages/login/LoginPage";
import ResumenPage from "./pages/resumen/ResumenPage";
import InventarioPage from "./pages/inventario/InventarioPage";
import CategoriasPage from "./pages/categorias/CategoriasPage";
import AlertasPage from "./pages/alertas/AlertasPage";
import { useAuth } from "./hooks/useAuth";

const ProtectedRoute = () => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Cargando...
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Ruta pública */}
          <Route path="/login" element={<LoginPage />} />

          {/* Rutas protegidas */}
          <Route element={<ProtectedRoute />}>
            <Route path="/resumen"    element={<ResumenPage />} />
            <Route path="/inventario" element={<InventarioPage />} />
            <Route path="/categorias" element={<CategoriasPage />} />
            <Route path="/alertas"    element={<AlertasPage />} />
          </Route>

          {/* Redirecciones */}
          <Route path="/"  element={<Navigate to="/resumen" replace />} />
          <Route path="*"  element={<Navigate to="/login"   replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;