import axios from "axios";
import type {
  Categoria,
  Color,
  Talla,
  Producto,
  Inventario,
  InventarioResumen,
  LoginResponse,
} from "../types";

const axiosClient = axios.create({
  baseURL: "http://localhost:8081/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  // Debug logs to verify token presence and request URL
  console.log('[Axios] Token enviado:', token ? 'SÍ' : 'NO');
  console.log('[Axios] Request a:', config.url);
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("accessToken");
      localStorage.removeItem("authUser");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosClient;

// Auth
export const login = (correo: string, contrasena: string) =>
  axiosClient.post<LoginResponse>("/auth/login", { correo, contrasena });

// Categorias
export const getCategorias = () => axiosClient.get<Categoria[]>("/categorias");
export const createCategoria = (data: { nombreCategoria: string }) =>
  axiosClient.post<Categoria>("/categorias", data);
export const updateCategoria = (id: number, data: { nombreCategoria: string }) =>
  axiosClient.put<Categoria>(`/categorias/${id}`, data);
export const deleteCategoria = (id: number) =>
  axiosClient.delete(`/categorias/${id}`);

// Colores
export const getColores = () => axiosClient.get<Color[]>("/colores");
export const createColor = (data: { nombreColor: string }) =>
  axiosClient.post<Color>("/colores", data);
export const updateColor = (id: number, data: { nombreColor: string }) =>
  axiosClient.put<Color>(`/colores/${id}`, data);
export const deleteColor = (id: number) => axiosClient.delete(`/colores/${id}`);

// Tallas
export const getTallas = () => axiosClient.get<Talla[]>("/tallas");
export const createTalla = (data: { numero: string }) =>
  axiosClient.post<Talla>("/tallas", data);
export const updateTalla = (id: number, data: { numero: string }) =>
  axiosClient.put<Talla>(`/tallas/${id}`, data);
export const deleteTalla = (id: number) => axiosClient.delete(`/tallas/${id}`);

// Productos
export const getProductos = () => axiosClient.get<Producto[]>("/productos");
export const createProducto = (data: Omit<Producto, "idProducto">) =>
  axiosClient.post<Producto>("/productos", data);
export const updateProducto = (id: number, data: Partial<Producto>) =>
  axiosClient.put<Producto>(`/productos/${id}`, data);
export const deleteProducto = (id: number) => axiosClient.delete(`/productos/${id}`);

// Inventario
export const getInventario = () => axiosClient.get<Inventario[]>("/inventario");
export const createInventario = (data: {
  idProducto: number;
  idTalla: number;
  idColor: number;
  stock: number;
}) => axiosClient.post<Inventario>("/inventario", data);
export const updateInventario = (id: number, data: {
  idProducto: number;
  idTalla: number;
  idColor: number;
  stock: number;
}) => axiosClient.put<Inventario>(`/inventario/${id}`, data);
export const deleteInventario = (id: number) =>
  axiosClient.delete(`/inventario/${id}`);

// Resumen
export const getResumen = () => axiosClient.get<InventarioResumen>("/resumen");

// Alertas
export const getAlertas = () => axiosClient.get<Inventario[]>("/alertas");
