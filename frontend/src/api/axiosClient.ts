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

/*
  axiosClient es el cliente HTTP centralizado de la aplicación.
  Se encarga de comunicarse con el backend en la URL base del API
  y de manejar de forma automática los encabezados comunes y el estado de sesión.
*/
const axiosClient = axios.create({
  baseURL: "http://localhost:8081/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

/*
  Interceptor de request: se ejecuta antes de cada llamada.
  Agrega el token JWT al header Authorization para que el backend
  puede validar la sesión en los endpoints protegidos.
*/
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

/*
  Interceptor de response: intercepta errores globales.
  Si el backend devuelve 401, significa que la sesión no es válida
  y se limpia el localStorage para forzar un nuevo login.
*/
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
/*
  Llama al endpoint de autenticación para obtener un token de acceso.
  Este token se almacenará en localStorage y será usado por los interceptores.
*/
export const login = (correo: string, contrasena: string) =>
  axiosClient.post<LoginResponse>("/auth/login", { correo, contrasena });

// Categorias
/*
  Recupera todas las categorías disponibles para llenar selects y tablas.
*/
export const getCategorias = () => axiosClient.get<Categoria[]>("/categorias");

/*
  Crea una nueva categoría en el backend.
  Se usa en el modal de creación/edición de categorías.
*/
export const createCategoria = (data: { nombreCategoria: string }) =>
  axiosClient.post<Categoria>("/categorias", data);

/*
  Actualiza una categoría existente por su id.
  Permite mantener el catálogo de categorías sincronizado.
*/
export const updateCategoria = (id: number, data: { nombreCategoria: string }) =>
  axiosClient.put<Categoria>(`/categorias/${id}`, data);

/*
  Elimina una categoría por id.
  Se llama cuando el administrador confirma la eliminación.
*/
export const deleteCategoria = (id: number) =>
  axiosClient.delete(`/categorias/${id}`);

// Colores
/*
  Recupera la lista de colores disponibles para el inventario.
*/
export const getColores = () => axiosClient.get<Color[]>("/colores");

/*
  Crea un nuevo color que se puede asociar a variantes en inventario.
*/
export const createColor = (data: { nombreColor: string }) =>
  axiosClient.post<Color>("/colores", data);

/*
  Actualiza el nombre de un color existente.
*/
export const updateColor = (id: number, data: { nombreColor: string }) =>
  axiosClient.put<Color>(`/colores/${id}`, data);

/*
  Elimina un color del catálogo si ya no es necesario.
*/
export const deleteColor = (id: number) => axiosClient.delete(`/colores/${id}`);

// Tallas
/*
  Recupera todas las tallas disponibles para el formulario de inventario.
*/
export const getTallas = () => axiosClient.get<Talla[]>("/tallas");

/*
  Crea una nueva talla que puede ser usada en variantes de producto.
*/
export const createTalla = (data: { numero: string }) =>
  axiosClient.post<Talla>("/tallas", data);

/*
  Actualiza el valor de una talla existente.
*/
export const updateTalla = (id: number, data: { numero: string }) =>
  axiosClient.put<Talla>(`/tallas/${id}`, data);

/*
  Elimina una talla del catálogo si ya no se usa.
*/
export const deleteTalla = (id: number) => axiosClient.delete(`/tallas/${id}`);

// Productos
/*
  Recupera el listado de productos para mostrar en tablas y selects.
*/
export const getProductos = () => axiosClient.get<Producto[]>("/productos");

/*
  Crea un producto nuevo sin el id, que luego se asociará a inventario.
*/
export const createProducto = (data: Omit<Producto, "idProducto">) =>
  axiosClient.post<Producto>("/productos", data);

export const updateProducto = (id: number, data: Partial<Producto>) =>
  axiosClient.put<Producto>(`/productos/${id}`, data);
export const deleteProducto = (id: number) => axiosClient.delete(`/productos/${id}`);

// Inventario
/*
  Recupera todas las variantes de inventario para la gestión diaria.
*/
export const getInventario = () => axiosClient.get<Inventario[]>("/inventario");

/*
  Crea una entrada en inventario para un producto existente.
  Se usa junto a createProducto cuando se agrega una nueva variante.
*/
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
/*
  Obtiene los datos del dashboard: totales, alertas, novedades y top stock.
*/
export const getResumen = () => axiosClient.get<InventarioResumen>("/resumen");

// Alertas
/*
  Recupera solo las variantes con stock bajo para la pantalla de alertas.
*/
export const getAlertas = () => axiosClient.get<Inventario[]>("/alertas");
