import { createContext, useContext, useMemo, useState, type ReactNode } from "react";
import type { AuthUser } from "../types";
import { login as loginApi } from "../api/axiosClient";

export interface AuthContextType {
  user: AuthUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (correo: string, contrasena: string) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Contexto de autenticación que mantiene el usuario actual y controla el estado de sesión.
// Este contexto permite que toda la aplicación sepa si el usuario está autenticado.
const isTokenExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
};

/*
  Comprueba si el token guardado en LocalStorage está vencido.
  Si el token expiró, se usa esta función para impedir accesos con credenciales caducas.
*/
const getStoredUser = (): AuthUser | null => {
  const token = localStorage.getItem("accessToken");
  const storedUser = localStorage.getItem("authUser");
  if (!token || !storedUser) return null;

  if (isTokenExpired(token)) {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("authUser");
    // eslint-disable-next-line no-console
    console.log('[Auth] accessToken expirado. Limpiando localStorage.');
    return null;
  }

  try {
    // Debugeo inicial para saber si ya había sesión almacenada al arrancar.
    // eslint-disable-next-line no-console
    console.log('[Auth] getStoredUser - token:', token ? 'SÍ' : 'NO', 'storedUser:', storedUser ? 'SÍ' : 'NO');
    return { ...JSON.parse(storedUser), accessToken: token } as AuthUser;
  } catch {
    return null;
  }
};

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<AuthUser | null>(() => getStoredUser());
  const [isLoading, setIsLoading] = useState(false);

  const isAuthenticated = Boolean(user?.accessToken);

  /*
    Ejecuta el proceso de login del usuario.

    1. Marca la carga como activa.
    2. Llama a la API de login con correo y contraseña.
    3. Si la respuesta es exitosa, guarda el accessToken y los datos del usuario en localStorage.
    4. Actualiza el estado global del contexto para que la app reconozca al usuario autenticado.
    5. Siempre desactiva el estado de carga al finalizar.
  */
  const login = async (correo: string, contrasena: string) => {
    setIsLoading(true);
    try {
      const response = await loginApi(correo, contrasena);
      const data = response.data;
      const authUser: AuthUser = {
        idAdmin: data.idAdmin,
        nombre: data.nombre,
        correo: data.correo,
        accessToken: data.accessToken,
      }; 

      localStorage.setItem("accessToken", data.accessToken);
      localStorage.setItem("authUser", JSON.stringify({
        idAdmin: data.idAdmin,
        nombre: data.nombre,
        correo: data.correo,
      }));
      // Debug: confirm token saved
      // eslint-disable-next-line no-console
      console.log('[Auth] Guardado accessToken:', data.accessToken ? 'SÍ' : 'NO');
      setUser(authUser);
    } finally {
      setIsLoading(false);
    }
  };

  // Cierra la sesión actual, elimina credenciales locales y fuerza el redireccionamiento a login.
  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("authUser");
    setUser(null);
    window.location.href = "/login";
  };

  const value = useMemo(
    () => ({ user, isAuthenticated, isLoading, login, logout }),
    [user, isAuthenticated, isLoading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuthContext = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
};
