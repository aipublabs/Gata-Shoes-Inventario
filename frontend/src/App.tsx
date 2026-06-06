import { BrowserRouter, Routes, Route, Navigate, Outlet } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import LoginPage from "./pages/login/LoginPage";
import ResumenPage from "./pages/resumen/ResumenPage";
import InventarioPage from "./pages/inventario/InventarioPage";
import CategoriasPage from "./pages/categorias/CategoriasPage";
import ColoresPage from "./pages/colores/ColoresPage";
import TallasPage from "./pages/tallas/TallasPage";
import ProductosPage from "./pages/productos/ProductosPage";
import { useAuth } from "./hooks/useAuth";

const ProtectedRoute = () => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <div className="min-h-screen flex items-center justify-center">Cargando...</div>;
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
          <Route path="/login" element={<LoginPage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/resumen" element={<ResumenPage />} />
            <Route path="/inventario" element={<InventarioPage />} />
            <Route path="/categorias" element={<CategoriasPage />} />
            <Route path="/colores" element={<ColoresPage />} />
            <Route path="/tallas" element={<TallasPage />} />
            <Route path="/productos" element={<ProductosPage />} />
          </Route>
          <Route path="/" element={<Navigate to="/resumen" replace />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
