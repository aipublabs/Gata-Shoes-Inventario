/**
 * data.js - Motor de gestión de datos para Gata Shoes
 * Ecosistema completo: Inventario, Categorías, Atributos y Administradores.
 */

const STORAGE_KEY_INVENTARIO = 'gata_shoes_inventario';
const STORAGE_KEY_SESION = 'gata_shoes_sesion_activa';
const STORAGE_KEY_CATEGORIAS = 'gata_shoes_categorias';
const STORAGE_KEY_ATRIBUTOS = 'gata_shoes_atributos';
const STORAGE_KEY_ADMINS = 'gata_shoes_admins';

// --- 1. DATOS INICIALES (Mock Data) ---
const datosIniciales = [
    { id: 1, sku: 'GS-001-NIKE', nombre: "Nike Air Max", categoria: "Deportivo", talla: 40, stock: 15, precio: 120.00, color: "Rojo/Blanco", imagen: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&q=80" },
    { id: 2, sku: 'GS-002-MOC', nombre: "Oxford Ejecutivo", categoria: "Formal", talla: 42, stock: 8, precio: 85.50, color: "Cuero Marrón", imagen: "https://images.unsplash.com/photo-1537832816519-689ad163238b?w=500&q=80" },
    { id: 3, sku: 'GS-003-VANS', nombre: "Vans Old Skool", categoria: "Urbano", talla: 39, stock: 20, precio: 65.00, color: "Negro/Blanco", imagen: "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=500&q=80" }
];

const categoriasIniciales = [
    { id: 1, nombre: "Deportivo", descripcion: "Calzado para alto rendimiento y atletismo." },
    { id: 2, nombre: "Formal", descripcion: "Zapatos elegantes de cuero y oficina." },
    { id: 3, nombre: "Urbano", descripcion: "Estilo casual para el día a día." },
    { id: 4, nombre: "Sandalias", descripcion: "Calzado abierto para verano y playa." }
];

const atributosIniciales = {
    colores: [
        { id: 1, nombre: "Rojo Clásico", hex: "#FF0000" },
        { id: 2, nombre: "Negro Ónyx", hex: "#000000" },
        { id: 3, nombre: "Cuero Marrón", hex: "#5D4037" },
        { id: 4, nombre: "Azul Rey", hex: "#0000FF" }
    ],
    tallas: [36, 37, 38, 39, 40, 41, 42, 43, 44]
};

const adminsIniciales = [
    { id: 1, iniciales: 'AA', nombre: 'Admin Atelier', email: 'admin@gata.shoes', rol: 'Superuser', estado: 'Activo', pass: 'gata2024' },
    { id: 2, iniciales: 'AM', nombre: 'Adrián Mendoza', email: 'a.mendoza@gata.shoes', rol: 'Inventory Manager', estado: 'Activo', pass: '123456' }
];

// --- 2. INICIALIZACIÓN ---
function inicializarDatos() {
    if (!sessionStorage.getItem(STORAGE_KEY_INVENTARIO)) sessionStorage.setItem(STORAGE_KEY_INVENTARIO, JSON.stringify(datosIniciales));
    if (!sessionStorage.getItem(STORAGE_KEY_CATEGORIAS)) sessionStorage.setItem(STORAGE_KEY_CATEGORIAS, JSON.stringify(categoriasIniciales));
    if (!sessionStorage.getItem(STORAGE_KEY_ATRIBUTOS)) sessionStorage.setItem(STORAGE_KEY_ATRIBUTOS, JSON.stringify(atributosIniciales));
    if (!sessionStorage.getItem(STORAGE_KEY_ADMINS)) sessionStorage.setItem(STORAGE_KEY_ADMINS, JSON.stringify(adminsIniciales));
}

// --- 3. LÓGICA DE NEGOCIO ---
function obtenerInventario() { return JSON.parse(sessionStorage.getItem(STORAGE_KEY_INVENTARIO) || '[]'); }
function guardarInventario(inv) { sessionStorage.setItem(STORAGE_KEY_INVENTARIO, JSON.stringify(inv)); }
function obtenerCategorias() { return JSON.parse(sessionStorage.getItem(STORAGE_KEY_CATEGORIAS) || '[]'); }
function guardarCategorias(cats) { sessionStorage.setItem(STORAGE_KEY_CATEGORIAS, JSON.stringify(cats)); }
function obtenerAtributos() { return JSON.parse(sessionStorage.getItem(STORAGE_KEY_ATRIBUTOS) || '{}'); }
function obtenerAdmins() { return JSON.parse(sessionStorage.getItem(STORAGE_KEY_ADMINS) || '[]'); }
function guardarAdmins(admins) { sessionStorage.setItem(STORAGE_KEY_ADMINS, JSON.stringify(admins)); }

function agregarProducto(p) {
    const inv = obtenerInventario();
    p.id = inv.length > 0 ? Math.max(...inv.map(x => x.id)) + 1 : 1;
    inv.push(p);
    guardarInventario(inv);
}

function eliminarProducto(id) {
    const inv = obtenerInventario().filter(p => p.id !== id);
    guardarInventario(inv);
}

function actualizarStock(id, nuevoStock) {
    const inv = obtenerInventario();
    const idx = inv.findIndex(p => p.id === id);
    if (idx !== -1) {
        inv[idx].stock = nuevoStock < 0 ? 0 : nuevoStock;
        guardarInventario(inv);
    }
}

// --- 4. AUTENTICACIÓN ---
function intentarLogin(email, password) {
    const admins = obtenerAdmins();
    const adminValido = admins.find(a => a.email === email && a.pass === password);
    if (adminValido && adminValido.estado === 'Activo') {
        sessionStorage.setItem(STORAGE_KEY_SESION, JSON.stringify({
            nombre: adminValido.nombre,
            email: adminValido.email,
            iniciales: adminValido.iniciales,
            rol: adminValido.rol
        }));
        return true;
    }
    return false;
}

function verificarSesion() {
    const sesion = sessionStorage.getItem(STORAGE_KEY_SESION);
    const path = window.location.pathname;
    const pag = path.substring(path.lastIndexOf('/') + 1) || 'Login.jsp';
    if (!sesion && pag !== 'Login.jsp') window.location.href = 'Login.jsp';
    else if (sesion && pag === 'Login.jsp') window.location.href = 'Resumen.jsp';
}

function cerrarSesion() {
    sessionStorage.removeItem(STORAGE_KEY_SESION);
    window.location.href = 'Login.jsp';
}

inicializarDatos();
verificarSesion();