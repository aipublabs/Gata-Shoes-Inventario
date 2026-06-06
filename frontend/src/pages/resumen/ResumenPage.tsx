import { useEffect, useMemo, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import MetricCard from "../../components/ui/MetricCard";
import PageHeader from "../../components/ui/PageHeader";
import Modal from "../../components/ui/Modal";
import {
  getResumen,
  getCategorias,
  createProducto,
  createInventario,
  getTallas,
  getColores,
} from "../../api/axiosClient";
import type { InventarioResumen, Inventario, Talla, Color } from "../../types";

const ResumenPage = () => {
  const [resumen, setResumen] = useState<InventarioResumen | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  const fetchResumen = async () => {
    setIsLoading(true);
    try {
      const response = await getResumen();
      setResumen(response.data);
    } catch (err) {
      console.error("[ResumenPage] Error al cargar:", err);
      setError("No se pudo cargar el resumen. Intenta nuevamente más tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchResumen();
  }, []);

  // Modal estado
  const [modalProductoOpen, setModalProductoOpen] = useState(false);
  const [categorias, setCategorias] = useState<Array<{ idCategoria: number; nombreCategoria: string }>>([]);
  const [tallas, setTallas] = useState<Talla[]>([]);
  const [colores, setColores] = useState<Color[]>([]);

  // Campos producto
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState<number | "">("");
  const [urlImagen, setUrlImagen] = useState("");
  const [idCategoria, setIdCategoria] = useState<number | "">("");

  // Campos inventario
  const [idTalla, setIdTalla] = useState<number | "">("");
  const [idColor, setIdColor] = useState<number | "">("");
  const [stock, setStock] = useState<number | "">("");

  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    const loadSelects = async () => {
      try {
        const [catResp, tallaResp, colorResp] = await Promise.all([
          getCategorias(),
          getTallas(),
          getColores(),
        ]);
        setCategorias(catResp.data ?? []);
        setTallas(tallaResp.data ?? []);
        setColores(colorResp.data ?? []);
      } catch (err) {
        console.error("Error cargando selects:", err);
      }
    };

    if (modalProductoOpen) {
      loadSelects();
      // Reset form
      setNombre("");
      setDescripcion("");
      setPrecio("");
      setUrlImagen("");
      setIdCategoria("");
      setIdTalla("");
      setIdColor("");
      setStock("");
      setFormErrors({});
    }
  }, [modalProductoOpen]);

  const topCategorias = resumen?.topCategoriasStock ?? [];

  const novedades = useMemo(() => {
    const items = resumen?.novedades ?? [];
    if (!searchTerm) return items.slice(0, 5);
    return items
      .filter((item) =>
        item.producto.nombre.toLowerCase().includes(searchTerm.toLowerCase())
      )
      .slice(0, 5);
  }, [resumen, searchTerm]);

  const topStock = useMemo(() => {
    const items = resumen?.topStock ?? [];
    if (!searchTerm) return items;
    return items.filter((item) =>
      item.producto.nombre.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [resumen, searchTerm]);

  const maxStock = useMemo(() => {
    const stocks = topCategorias.map((c) => c.stock);
    return Math.max(...stocks, 1);
  }, [topCategorias]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validaciones
    const errors: Record<string, string> = {};
    if (!nombre.trim()) errors.nombre = "El nombre es obligatorio.";
    if (!precio || Number(precio) <= 0) errors.precio = "El precio debe ser mayor que 0.";
    if (!idCategoria) errors.idCategoria = "Selecciona una categoría.";
    if (!idTalla) errors.idTalla = "Selecciona una talla.";
    if (!idColor) errors.idColor = "Selecciona un color.";
    if (stock === "" || Number(stock) < 0) errors.stock = "El stock inicial debe ser 0 o mayor.";

    setFormErrors(errors);
    if (Object.keys(errors).length > 0) return;

    setIsSaving(true);
    try {
      // Paso 1 — Crear el producto
      const productoResp = await createProducto({
        nombre: nombre.trim(),
        descripcion: descripcion.trim() || null,
        precio: Number(precio),
        urlImagen: urlImagen.trim() || null,
        idCategoria: Number(idCategoria),
      } as any);

      const nuevoProductoId = productoResp.data.idProducto;

      // Paso 2 — Crear la entrada en inventario
      await createInventario({
        idProducto: nuevoProductoId,
        idTalla: Number(idTalla),
        idColor: Number(idColor),
        stock: Number(stock),
      });

      // Paso 3 — Cerrar modal y recargar
      setModalProductoOpen(false);
      await fetchResumen();

    } catch (err) {
      console.error("Error creando producto:", err);
      setFormErrors({ submit: "No se pudo crear el producto. Intenta nuevamente." });
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center p-6">
        <span className="text-slate-600">Cargando...</span>
      </div>
    );
  }

  return (
    <MainLayout searchPlaceholder="Buscar productos..." onSearch={setSearchTerm}>
      <div className="space-y-8">

        {/* Header */}
        <PageHeader
          title="Resumen del Sistema"
          description="Estado actual de la salud del inventario"
          actionLabel="Nuevo Producto"
          onAction={() => setModalProductoOpen(true)}
        />

        {/* Error */}
        {error && (
          <div className="rounded-2xl border border-red-200 bg-red-50 px-5 py-4 text-sm text-red-700">
            {error}
          </div>
        )}

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <MetricCard
            icon="inventory_2"
            label="Variantes en Inventario"
            value={resumen?.totalVariantes ?? 0}
            colorVariant="violet"
          />
          <MetricCard
            icon="analytics"
            label="Stock Total"
            value={resumen?.totalStock ?? 0}
            colorVariant="emerald"
          />
          <MetricCard
            icon="warning"
            label="Alertas Stock Bajo (<=3)"
            value={resumen?.alertasStockBajo ?? 0}
            colorVariant="amber"
          />
        </div>

        {/* Gráfico de barras */}
        <section className="bg-white p-8 rounded-2xl shadow-sm border border-slate-100">
          <h2 className="text-xl font-bold text-slate-900">
            Distribución de Stock por Categoría
          </h2>
          <p className="text-xs text-slate-400 mt-1">
            Proporción porcentual del Top 4 de categorías con mayor volumen.
          </p>
          <div className="mt-8 grid gap-5 xl:grid-cols-4">
            {topCategorias.length === 0 ? (
              <div className="text-slate-500 text-sm">No hay datos para mostrar.</div>
            ) : (
              topCategorias.map((categoria) => {
                const height = Math.max((categoria.stock / maxStock) * 100, 10);
                return (
                  <div key={categoria.nombreCategoria} className="flex flex-col items-center gap-3">
                    <div className="h-52 w-full rounded-3xl bg-slate-100 overflow-hidden flex items-end">
                      <div
                        className="w-full rounded-3xl bg-violet-600 transition-all duration-700"
                        style={{ height: `${height}%` }}
                      />
                    </div>
                    <div className="text-center">
                      <p className="text-sm font-semibold text-slate-700">
                        {categoria.nombreCategoria}
                      </p>
                      <p className="text-xs text-slate-400 mt-1">
                        {categoria.stock} unidades
                      </p>
                    </div>
                  </div>
                );
              })
            )}
          </div>
        </section>

        {/* Nuevos Ingresos */}
        <section className="space-y-4">
          <div>
            <h2 className="text-2xl font-black tracking-tight text-slate-800">
              Nuevos Ingresos
            </h2>
            <p className="text-slate-400 text-xs mt-1">
              Los últimos 5 artículos añadidos recientemente al catálogo.
            </p>
          </div>
          <div className="flex flex-row gap-4 overflow-x-auto pb-2">
            {novedades.length === 0 ? (
              <p className="text-slate-500 text-sm">No hay novedades para mostrar.</p>
            ) : (
              novedades.map((item) => (
                <article
                  key={item.idInventario}
                  className="flex-shrink-0 w-44 bg-white p-3 rounded-2xl border border-slate-100 shadow-sm hover:shadow-md transition-shadow cursor-pointer"
                >
                  {item.producto.urlImagen ? (
                    <img
                      src={item.producto.urlImagen}
                      alt={item.producto.nombre}
                      className="h-28 w-full object-cover rounded-xl"
                    />
                  ) : (
                    <div className="h-28 w-full bg-slate-100 flex items-center justify-center text-slate-400 rounded-xl">
                      <span className="material-symbols-outlined">image_not_supported</span>
                    </div>
                  )}
                  <div className="space-y-1 mt-2">
                    <p className="text-sm font-bold text-slate-800 truncate">
                      {item.producto.nombre}
                    </p>
                    <p className="text-xs text-slate-400">
                      {item.producto.categoria?.nombreCategoria ?? "Sin categoría"}
                    </p>
                    <div className="flex flex-wrap gap-1 text-xs text-slate-500">
                      <span>Talla {item.talla.numero}</span>
                      <span>·</span>
                      <span>{item.color.nombreColor}</span>
                    </div>
                    <p className="text-sm font-black text-violet-600">
                      ${item.producto.precio.toLocaleString("es-CO")}
                    </p>
                  </div>
                </article>
              ))
            )}
          </div>
        </section>

        {/* Top 3 con Mayor Stock */}
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b border-slate-100">
            <h2 className="text-2xl font-black text-slate-800">
              Top 3 con Mayor Stock
            </h2>
            <p className="text-xs text-slate-400 mt-1">
              Variantes físicas que lideran el volumen de almacenamiento.
            </p>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-sm">
              <thead className="bg-slate-50 text-slate-500 font-bold border-b">
                <tr>
                  <th className="text-left p-4">Imagen</th>
                  <th className="text-left p-4">ID</th>
                  <th className="text-left p-4">Nombre</th>
                  <th className="text-left p-4">Categoría</th>
                  <th className="text-left p-4">Color</th>
                  <th className="text-left p-4">Talla</th>
                  <th className="text-left p-4">Stock</th>
                  <th className="text-left p-4">Precio</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {topStock.length === 0 ? (
                  <tr>
                    <td colSpan={8} className="p-12 text-center text-slate-400 font-medium">
                      No hay productos registrados en el inventario actualmente.
                    </td>
                  </tr>
                ) : (
                  topStock.map((item: Inventario) => (
                    <tr key={item.idInventario} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4">
                        {item.producto.urlImagen ? (
                          <img
                            src={item.producto.urlImagen}
                            alt={item.producto.nombre}
                            className="w-16 h-16 object-cover rounded-xl border border-slate-200"
                          />
                        ) : (
                          <div className="w-16 h-16 rounded-xl bg-slate-100 flex items-center justify-center text-slate-400">
                            <span className="material-symbols-outlined text-sm">image_not_supported</span>
                          </div>
                        )}
                      </td>
                      <td className="p-4 font-bold text-slate-600">
                        #{item.producto.idProducto}
                      </td>
                      <td className="p-4 font-semibold text-slate-800">
                        {item.producto.nombre}
                      </td>
                      <td className="p-4 text-slate-600">
                        {item.producto.categoria?.nombreCategoria ?? "Sin categoría"}
                      </td>
                      <td className="p-4 text-slate-600">{item.color.nombreColor}</td>
                      <td className="p-4 text-slate-700 font-medium">{item.talla.numero}</td>
                      <td className={`p-4 font-bold ${item.stock <= 3 ? "text-red-500" : "text-slate-700"}`}>
                        {item.stock}
                      </td>
                      <td className="p-4 font-black text-violet-600">
                        ${item.producto.precio.toLocaleString("es-CO")}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* ✅ Modal Nuevo Producto */}
        <Modal
          isOpen={modalProductoOpen}
          onClose={() => setModalProductoOpen(false)}
          title=""
          maxWidth="max-w-4xl"
        >
          <div className="flex flex-col md:flex-row -m-6 rounded-3xl overflow-hidden">

            {/* Columna izquierda — Preview imagen */}
            <div className="md:w-5/12 bg-surface-container-low p-8 flex flex-col items-center gap-6 border-r border-outline-variant/10">
              <div className="w-full aspect-square bg-white rounded-xl border-2 border-dashed border-outline-variant flex flex-col items-center justify-center p-4 text-center overflow-hidden">
                {urlImagen ? (
                  <img
                    src={urlImagen}
                    alt="Vista previa"
                    className="w-full h-full object-cover rounded-xl"
                  />
                ) : (
                  <>
                    <div className="w-16 h-16 bg-primary-fixed rounded-full flex items-center justify-center mb-4">
                      <span className="material-symbols-outlined text-primary text-3xl">
                        add_a_photo
                      </span>
                    </div>
                    <p className="font-bold text-on-surface">Vista Previa</p>
                    <p className="text-sm text-on-surface-variant mt-2 px-4">
                      Pega una URL de imagen para previsualizar
                    </p>
                  </>
                )}
              </div>
              <div className="flex items-start gap-3 p-3 bg-white rounded-lg shadow-sm w-full">
                <span className="material-symbols-outlined text-slate-400">lightbulb</span>
                <p className="text-xs text-slate-600 leading-relaxed">
                  Usa fondos neutros para resaltar la silueta del zapato en el catálogo.
                </p>
              </div>
            </div>

            {/* Columna derecha — Formulario */}
            <div className="md:w-7/12 p-8 md:p-10 flex flex-col overflow-y-auto max-h-[80vh]">
              <div className="mb-6">
                <h2 className="text-3xl font-headline font-extrabold text-on-surface tracking-tight">
                  Añadir Nuevo Zapato
                </h2>
                <p className="text-sm text-on-surface-variant mt-1">
                  Registra una nueva SKU en el taller digital de Gata Shoes
                </p>
              </div>

              <form className="space-y-4 flex-1" onSubmit={handleSubmit}>

                {/* Nombre */}
                <div className="space-y-1">
                  <label className="text-sm font-semibold text-on-surface-variant px-1">
                    Nombre del Producto
                  </label>
                  <input
                    placeholder="Ej: Stellar Runner Pro V2"
                    className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 focus:ring-2 focus:ring-primary outline-none transition-all placeholder:text-outline"
                    value={nombre}
                    onChange={(e) => { setNombre(e.target.value); setFormErrors(f => ({ ...f, nombre: "" })); }}
                  />
                  {formErrors.nombre && <p className="text-red-500 text-xs">{formErrors.nombre}</p>}
                </div>

                {/* Precio + Categoría */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <label className="text-sm font-semibold text-on-surface-variant px-1">
                      Precio (COP)
                    </label>
                    <div className="relative">
                      <span className="absolute left-4 top-1/2 -translate-y-1/2 text-on-surface-variant font-bold">$</span>
                      <input
                        type="number"
                        step="0.01"
                        placeholder="0.00"
                        className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg pl-8 pr-4 py-3 focus:ring-2 focus:ring-primary outline-none transition-all"
                        value={precio}
                        onChange={(e) => { setPrecio(e.target.value === "" ? "" : Number(e.target.value)); setFormErrors(f => ({ ...f, precio: "" })); }}
                      />
                    </div>
                    {formErrors.precio && <p className="text-red-500 text-xs">{formErrors.precio}</p>}
                  </div>

                  <div className="space-y-1">
                    <label className="text-sm font-semibold text-on-surface-variant px-1">
                      Categoría
                    </label>
                    <div className="relative">
                      <select
                        className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 appearance-none focus:ring-2 focus:ring-primary outline-none transition-all cursor-pointer"
                        value={idCategoria}
                        onChange={(e) => { setIdCategoria(e.target.value === "" ? "" : Number(e.target.value)); setFormErrors(f => ({ ...f, idCategoria: "" })); }}
                      >
                        <option value="">Selecciona...</option>
                        {categorias.map((c) => (
                          <option key={c.idCategoria} value={c.idCategoria}>{c.nombreCategoria}</option>
                        ))}
                      </select>
                      <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-on-surface-variant text-sm">expand_more</span>
                    </div>
                    {formErrors.idCategoria && <p className="text-red-500 text-xs">{formErrors.idCategoria}</p>}
                  </div>
                </div>

                {/* Talla + Color */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <label className="text-sm font-semibold text-on-surface-variant px-1">
                      Talla
                    </label>
                    <div className="relative">
                      <select
                        className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 appearance-none focus:ring-2 focus:ring-primary outline-none transition-all cursor-pointer"
                        value={idTalla}
                        onChange={(e) => { setIdTalla(e.target.value === "" ? "" : Number(e.target.value)); setFormErrors(f => ({ ...f, idTalla: "" })); }}
                      >
                        <option value="">Selecciona...</option>
                        {tallas.map((t) => (
                          <option key={t.idTalla} value={t.idTalla}>{t.numero}</option>
                        ))}
                      </select>
                      <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-on-surface-variant text-sm">expand_more</span>
                    </div>
                    {formErrors.idTalla && <p className="text-red-500 text-xs">{formErrors.idTalla}</p>}
                  </div>

                  <div className="space-y-1">
                    <label className="text-sm font-semibold text-on-surface-variant px-1">
                      Color
                    </label>
                    <div className="relative">
                      <select
                        className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 appearance-none focus:ring-2 focus:ring-primary outline-none transition-all cursor-pointer"
                        value={idColor}
                        onChange={(e) => { setIdColor(e.target.value === "" ? "" : Number(e.target.value)); setFormErrors(f => ({ ...f, idColor: "" })); }}
                      >
                        <option value="">Selecciona...</option>
                        {colores.map((c) => (
                          <option key={c.idColor} value={c.idColor}>{c.nombreColor}</option>
                        ))}
                      </select>
                      <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-on-surface-variant text-sm">expand_more</span>
                    </div>
                    {formErrors.idColor && <p className="text-red-500 text-xs">{formErrors.idColor}</p>}
                  </div>
                </div>

                {/* Stock inicial */}
                <div className="space-y-1">
                  <label className="text-sm font-semibold text-on-surface-variant px-1">
                    Stock Inicial
                  </label>
                  <input
                    type="number"
                    min="0"
                    placeholder="Ej: 12"
                    className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 focus:ring-2 focus:ring-primary outline-none transition-all"
                    value={stock}
                    onChange={(e) => { setStock(e.target.value === "" ? "" : Number(e.target.value)); setFormErrors(f => ({ ...f, stock: "" })); }}
                  />
                  {formErrors.stock && <p className="text-red-500 text-xs">{formErrors.stock}</p>}
                </div>

                {/* URL Imagen */}
                <div className="space-y-1">
                  <label className="text-sm font-semibold text-on-surface-variant px-1">
                    URL de Imagen (opcional)
                  </label>
                  <input
                    placeholder="https://images.unsplash.com/..."
                    className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 focus:ring-2 focus:ring-primary outline-none transition-all"
                    value={urlImagen}
                    onChange={(e) => setUrlImagen(e.target.value)}
                  />
                </div>

                {/* Descripción */}
                <div className="space-y-1">
                  <label className="text-sm font-semibold text-on-surface-variant px-1">
                    Descripción (opcional)
                  </label>
                  <textarea
                    placeholder="Descripción del producto..."
                    rows={2}
                    className="w-full bg-surface-container-low border border-outline-variant/20 rounded-lg px-4 py-3 focus:ring-2 focus:ring-primary outline-none transition-all resize-none"
                    value={descripcion}
                    onChange={(e) => setDescripcion(e.target.value)}
                  />
                </div>

                {formErrors.submit && (
                  <p className="text-red-500 text-sm">{formErrors.submit}</p>
                )}

                {/* Botones */}
                <div className="pt-4 flex flex-col-reverse sm:flex-row items-center justify-end gap-4">
                  <button
                    type="button"
                    onClick={() => setModalProductoOpen(false)}
                    className="w-full sm:w-auto px-8 py-3 rounded-xl font-bold text-primary hover:bg-primary-fixed transition-all"
                    disabled={isSaving}
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    disabled={isSaving}
                    className="w-full sm:w-auto px-10 py-3 rounded-xl bg-gradient-to-br from-primary to-primary-container text-on-primary font-bold shadow-lg hover:scale-[1.02] active:scale-95 transition-all flex items-center justify-center gap-2 disabled:opacity-60 disabled:cursor-not-allowed"
                  >
                    <span className="material-symbols-outlined text-lg" style={{ fontVariationSettings: "'FILL' 1" }}>
                      save
                    </span>
                    {isSaving ? "Guardando..." : "Guardar"}
                  </button>
                </div>

              </form>
            </div>
          </div>
        </Modal>

      </div>
    </MainLayout>
  );
};

export default ResumenPage;