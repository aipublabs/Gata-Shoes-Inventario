import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const LoginPage = () => {
  // Componente de inicio de sesión que controla el acceso al sistema.
  // Solo permite continuar si el usuario proporciona credenciales válidas.
  const navigate = useNavigate();
  const { login, isAuthenticated, isLoading } = useAuth();

  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [errors, setErrors] = useState({ correo: "", contrasena: "" });

  // Si ya existe sesión activa, no debe mostrarse el login.
  // Redirige automáticamente al dashboard para evitar volver a iniciar sesión.
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/resumen");
    }
  }, [isAuthenticated, navigate]);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError("");
    setErrors({ correo: "", contrasena: "" });

    // Validación manual en español
    if (!correo.trim()) {
      setErrors((e) => ({ ...e, correo: "El correo electrónico es obligatorio." }));
      return;
    }
    
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(correo.trim())) {
      setErrors((e) => ({ ...e, correo: "Ingresa un correo electrónico válido." }));
      return;
    }
    
    if (!contrasena.trim()) {
      setErrors((e) => ({ ...e, contrasena: "La contraseña es obligatoria." }));
      return;
    }
    
    try {
      /*
        Envía las credenciales al backend.
        Si el login es correcto, el contexto de autenticación guardará el token
        y la app navegará al dashboard.
      */
      await login(correo.trim(), contrasena);
      navigate("/resumen");
    } catch (loginError) {
      setError("Credenciales incorrectas. Inténtelo de nuevo.")
    }
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center px-4 py-10"
      style={{
        background: "radial-gradient(circle at top right, #f0dbff 0%, #faf9fc 50%)",
      }}
    >
      <div className="w-full max-w-xl rounded-xl bg-white p-8 md:p-12 shadow-xl shadow-slate-200">
        <div className="flex items-center gap-4 mb-8">
          <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-primary p-2.5 text-on-primary">
            <span className="material-symbols-outlined text-2xl">settings_accessibility</span>
          </div>
          <div>
            <h1 className="text-4xl font-black italic tracking-tight text-slate-900">Gata Shoes</h1>
            <p className="mt-1 text-sm text-slate-600">Acceso al sistema de gestión</p>
          </div>
        </div>

        <form className="space-y-5" onSubmit={handleSubmit}>
          <div className="space-y-2">
            <label htmlFor="correo" className="block text-sm font-medium text-slate-700">
              Correo electrónico
            </label>
            <div className="flex items-center gap-3 rounded-xl bg-surface-container-low px-4 py-3 ring-1 ring-outline-variant/20">
              <span className="material-symbols-outlined text-slate-500">mail</span>
              <input
                id="correo"
                type="text"
                value={correo}
                onChange={(event) => {
                  setCorreo(event.target.value);
                  setErrors((e) => ({ ...e, correo: "" }));
                }}
                placeholder="correo@ejemplo.com"
                className="w-full border-none bg-transparent p-0 text-sm text-slate-900 outline-none placeholder:text-slate-400"
              />
            </div>
            {errors.correo && (
              <p className="text-red-500 text-xs mt-1 ml-1">{errors.correo}</p>
            )}
          </div>

          <div className="space-y-2">
            <label htmlFor="contrasena" className="block text-sm font-medium text-slate-700">
              Contraseña
            </label>
            <div className="flex items-center gap-3 rounded-xl bg-surface-container-low px-4 py-3 ring-1 ring-outline-variant/20">
              <span className="material-symbols-outlined text-slate-500">lock_open</span>
              <input
                id="contrasena"
                type={showPassword ? "text" : "password"}
                value={contrasena}
                onChange={(event) => {
                  setContrasena(event.target.value);
                  setErrors((e) => ({ ...e, contrasena: "" }));
                }}
                placeholder="Ingresa tu contraseña"
                className="w-full border-none bg-transparent p-0 text-sm text-slate-900 outline-none placeholder:text-slate-400"
              />
              <button
                type="button"
                onClick={() => setShowPassword((prev) => !prev)}
                className="text-slate-500 transition hover:text-slate-700"
                aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
              >
                <span className="material-symbols-outlined">
                  {showPassword ? "visibility_off" : "visibility"}
                </span>
              </button>
            </div>
          </div>

          {error ? (
            <div className="rounded-xl border border-red-300 bg-red-100 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          ) : null}

          <button
            type="submit"
            disabled={isLoading}
            className="flex w-full items-center justify-center gap-2 rounded-xl bg-gradient-to-br from-primary to-primary-container px-5 py-3 text-sm font-semibold text-white shadow-sm transition hover:opacity-95 disabled:cursor-not-allowed disabled:opacity-60"
          >
            <span className="material-symbols-outlined">lock</span>
            {isLoading ? "Ingresando..." : "Ingresar"}
          </button>

          {errors.contrasena && (
            <p className="text-red-500 text-xs mt-1 ml-1">{errors.contrasena}</p>
          )}
        </form>

        <div className="mt-10 border-t border-slate-200 pt-5 text-center text-sm text-slate-500">
          © 2026 Gata Shoes Inventario · v2.4.0
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
