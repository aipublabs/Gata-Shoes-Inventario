// Entidades base
export interface Categoria {
  idCategoria: number;
  nombreCategoria: string;
}

export interface Color {
  idColor: number;
  nombreColor: string;
}

export interface Talla {
  idTalla: number;
  numero: string;
}

export interface Producto {
  idProducto: number;
  nombre: string;
  descripcion: string | null;
  precio: number;
  urlImagen: string | null;
  categoria: Categoria | null;
}

export interface Inventario {
  idInventario: number;
  stock: number;
  producto: Producto;
  talla: Talla;
  color: Color;
}

export interface CategoriaStock {
  nombreCategoria: string;
  stock: number;
}

export interface InventarioResumen {
  totalVariantes: number;
  totalStock: number;
  alertasStockBajo: number;
  topCategoriasStock: CategoriaStock[];
  novedades: Inventario[];
  topStock: Inventario[];
}

// Auth
export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface LoginResponse {
  accessToken: string;
  idAdmin: number;
  nombre: string;
  correo: string;
}

export interface AuthUser {
  idAdmin: number;
  nombre: string;
  correo: string;
  accessToken: string;
}
