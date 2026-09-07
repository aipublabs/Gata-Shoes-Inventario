import axios from "axios";
import { useEffect, useMemo, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import Modal from "../../components/ui/Modal";
import PageHeader from "../../components/ui/PageHeader";
import {
  createProducto,
  deleteProducto,
  getCategorias,
  getProductos,
  updateProducto,
} from "../../api/axiosClient";
import type { Categoria, Producto, ProductoRequest } from "../../types";

interface ApiErrorResponse {
  message?: string;
}

const getApiErrorMessage = (error: unknown) => {
  if (axios.isAxiosError<ApiErrorResponse>(error)) {
    return error.response?.data?.message;
  }

  return undefined;
};

const ProductosPage = () => {
  const [productos, setProductos] = useState<Producto[]>([]);
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingProducto, setEditingProducto] = useState<Producto | null>(null);
  const [formNombre, setFormNombre] = useState("");
  const [formDescripcion, setFormDescripcion] = useState("");
  const [formPrecio, setFormPrecio] = useState<number | "">("");
  const [formUrlImagen, setFormUrlImagen] = useState("");
  const [formIdCategoria, setFormIdCategoria] = useState<number | "">("");
  const [searchTerm, setSearchTerm] = useState("");
  const [pageError, setPageError] = useState("");
  const [categoriesError, setCategoriesError] = useState("");
  const [formError, setFormError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const loadData = async () => {
    setIsLoading(true);
    const [productosResult, categoriasResult] = await Promise.allSettled([
      getProductos(),
      getCategorias(),
    ]);

    if (productosResult.status === "fulfilled") {
      setProductos(productosResult.value.data ?? []);
      setPageError("");
    } else {
      setPageError("No se pudo cargar la lista de productos. Intenta nuevamente.");
    }

    if (categoriasResult.status === "fulfilled") {
      const categoriasRecibidas = categoriasResult.value.data ?? [];
      setCategorias(categoriasRecibidas);
      setCategoriesError(
        categoriasRecibidas.length === 0
          ? "No hay categorías disponibles. Crea una categoría antes de registrar productos."
          : ""
      );
    } else {
      setCategorias([]);
      setCategoriesError("No se pudieron cargar las categorías para el formulario.");
    }

    setIsLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  const filteredProductos = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();
    if (!normalizedSearch) return productos;

    return productos.filter((producto) => {
      const categoria = producto.categoria?.nombreCategoria ?? "";
      const descripcion = producto.descripcion ?? "";
      return [producto.nombre, descripcion, categoria].some((value) =>
        value.toLowerCase().includes(normalizedSearch)
      );
    });
  }, [productos, searchTerm]);

  const resetForm = () => {
    setFormNombre("");
    setFormDescripcion("");
    setFormPrecio("");
    setFormUrlImagen("");
    setFormIdCategoria("");
    setFormError("");
  };

  const openNewProducto = () => {
    setEditingProducto(null);
    resetForm();
    setModalOpen(true);
  };

  const openEditProducto = (producto: Producto) => {
    setEditingProducto(producto);
    setFormNombre(producto.nombre);
    setFormDescripcion(producto.descripcion ?? "");
    setFormPrecio(producto.precio);
    setFormUrlImagen(producto.urlImagen ?? "");
    setFormIdCategoria(producto.categoria?.idCategoria ?? "");
    setFormError("");
    setModalOpen(true);
  };

  const closeModal = () => {
    if (isSubmitting) return;
    setModalOpen(false);
    setEditingProducto(null);
    resetForm();
  };

  const handleSave = async () => {
    const nombre = formNombre.trim();
    const descripcion = formDescripcion.trim() || null;
    const urlImagen = formUrlImagen.trim() || null;

    if (!nombre) {
      setFormError("El nombre del producto es obligatorio.");
      return;
    }

    if (nombre.length > 100) {
      setFormError("El nombre del producto no puede superar los 100 caracteres.");
      return;
    }

    if (formPrecio === "" || formPrecio <= 0) {
      setFormError("El precio es obligatorio y debe ser mayor que cero.");
      return;
    }

    if (formIdCategoria === "") {
      setFormError("La categoría es obligatoria.");
      return;
    }

    const request: ProductoRequest = {
      nombre,
      descripcion,
      precio: formPrecio,
      urlImagen,
      idCategoria: formIdCategoria,
    };

    setIsSubmitting(true);
    setFormError("");
    try {
      if (editingProducto) {
        await updateProducto(editingProducto.idProducto, request);
      } else {
        await createProducto(request);
      }

      await loadData();
      setModalOpen(false);
      setEditingProducto(null);
      resetForm();
    } catch (error: unknown) {
      setFormError(
        getApiErrorMessage(error) ?? "No se pudo guardar el producto. Intenta nuevamente."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async (producto: Producto) => {
    const confirmed = window.confirm(`¿Desea eliminar el producto "${producto.nombre}"?`);
    if (!confirmed) return;

    setDeletingId(producto.idProducto);
    setPageError("");
    try {
      await deleteProducto(producto.idProducto);
      await loadData();
    } catch (error: unknown) {
      const backendMessage = getApiErrorMessage(error);
      const detail = backendMessage ? ` Detalle: ${backendMessage}` : "";
      setPageError(
        `No se pudo eliminar el producto. Verifica que no tenga referencias de inventario.${detail}`
      );
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <MainLayout searchPlaceholder="Buscar productos..." onSearch={setSearchTerm}>
      <div className="space-y-8">
        <PageHeader
          title="Gestión de Productos"
          description="Administración y control de productos"
          actionLabel="Nuevo Producto"
          actionIcon="add"
          onAction={openNewProducto}
        />

        {pageError ? (
          <div className="rounded-2xl border border-red-200 bg-red-50 px-5 py-4 text-sm text-red-700">
            {pageError}
          </div>
        ) : null}

        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b border-slate-100">
            <h2 className="text-2xl font-black text-slate-800">Listado de Productos</h2>
            <p className="text-xs text-slate-400 mt-1">CRUD completo de productos registrados.</p>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-sm">
              <thead className="bg-slate-50 text-slate-500 font-bold border-b">
                <tr>
                  <th className="text-left p-4">ID</th>
                  <th className="text-left p-4">Imagen</th>
                  <th className="text-left p-4">Nombre</th>
                  <th className="text-left p-4">Descripción</th>
                  <th className="text-left p-4">Precio</th>
                  <th className="text-left p-4">Categoría</th>
                  <th className="text-center p-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {isLoading ? (
                  <tr>
                    <td colSpan={7} className="p-6 text-center text-slate-500">
                      Cargando productos...
                    </td>
                  </tr>
                ) : filteredProductos.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="p-6 text-center text-slate-500">
                      {productos.length === 0
                        ? "No hay productos registrados. Crea el primer producto."
                        : "No se encontraron productos que coincidan con la búsqueda."}
                    </td>
                  </tr>
                ) : (
                  filteredProductos.map((producto) => (
                    <tr key={producto.idProducto} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-bold text-slate-600">{producto.idProducto}</td>
                      <td className="p-4">
                        {producto.urlImagen ? (
                          <img
                            src={producto.urlImagen}
                            alt={producto.nombre}
                            className="h-16 w-16 rounded-xl border border-slate-200 object-cover"
                          />
                        ) : (
                          <div className="flex h-16 w-16 items-center justify-center rounded-xl bg-slate-100 text-slate-400">
                            <span className="material-symbols-outlined">image_not_supported</span>
                          </div>
                        )}
                      </td>
                      <td className="p-4 font-semibold text-slate-800">{producto.nombre}</td>
                      <td className="max-w-xs p-4 text-slate-600" title={producto.descripcion ?? "Sin descripción"}>
                        <span className="line-clamp-2">{producto.descripcion ?? "Sin descripción"}</span>
                      </td>
                      <td className="p-4 font-black text-violet-600">
                        ${producto.precio.toLocaleString("es-CO")}
                      </td>
                      <td className="p-4 text-slate-600">
                        {producto.categoria?.nombreCategoria ?? "Sin categoría"}
                      </td>
                      <td className="p-4 text-center">
                        <div className="inline-flex flex-wrap items-center justify-center gap-2">
                          <button
                            type="button"
                            onClick={() => openEditProducto(producto)}
                            disabled={deletingId !== null}
                            className="flex items-center gap-1 bg-amber-100 text-amber-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-amber-200 transition-all disabled:cursor-not-allowed disabled:opacity-60"
                          >
                            <span className="material-symbols-outlined text-sm">edit</span>
                            Editar
                          </button>
                          <button
                            type="button"
                            onClick={() => handleDelete(producto)}
                            disabled={deletingId !== null}
                            className="flex items-center gap-1 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-red-200 transition-all disabled:cursor-not-allowed disabled:opacity-60"
                          >
                            <span className="material-symbols-outlined text-sm">delete</span>
                            {deletingId === producto.idProducto ? "Eliminando..." : "Eliminar"}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        <Modal
          isOpen={modalOpen}
          onClose={closeModal}
          title={editingProducto ? "Editar Producto" : "Nuevo Producto"}
          maxWidth="max-w-4xl"
        >
          <div className="space-y-6">
            {categoriesError ? (
              <div className="rounded-xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-700">
                {categoriesError}
              </div>
            ) : null}

            <div className="grid gap-6 md:grid-cols-2">
              <div>
                <label htmlFor="productoNombre" className="mb-2 block text-sm font-semibold text-slate-700">
                  Nombre del producto
                </label>
                <input
                  id="productoNombre"
                  type="text"
                  value={formNombre}
                  maxLength={100}
                  onChange={(event) => {
                    setFormNombre(event.target.value);
                    setFormError("");
                  }}
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                  placeholder="Nombre del producto"
                />
              </div>

              <div>
                <label htmlFor="productoCategoria" className="mb-2 block text-sm font-semibold text-slate-700">
                  Categoría
                </label>
                <select
                  id="productoCategoria"
                  value={formIdCategoria}
                  onChange={(event) => {
                    setFormIdCategoria(event.target.value ? Number(event.target.value) : "");
                    setFormError("");
                  }}
                  disabled={categorias.length === 0}
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  <option value="">Selecciona una categoría</option>
                  {categorias.map((categoria) => (
                    <option key={categoria.idCategoria} value={categoria.idCategoria}>
                      {categoria.nombreCategoria}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label htmlFor="productoPrecio" className="mb-2 block text-sm font-semibold text-slate-700">
                  Precio (COP)
                </label>
                <input
                  id="productoPrecio"
                  type="number"
                  min="0.01"
                  step="0.01"
                  value={formPrecio}
                  onChange={(event) => {
                    setFormPrecio(event.target.value ? Number(event.target.value) : "");
                    setFormError("");
                  }}
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                  placeholder="0.00"
                />
              </div>

              <div>
                <label htmlFor="productoUrlImagen" className="mb-2 block text-sm font-semibold text-slate-700">
                  URL de imagen
                </label>
                <input
                  id="productoUrlImagen"
                  type="url"
                  value={formUrlImagen}
                  onChange={(event) => setFormUrlImagen(event.target.value)}
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                  placeholder="https://ejemplo.com/imagen.jpg"
                />
              </div>
            </div>

            <div>
              <label htmlFor="productoDescripcion" className="mb-2 block text-sm font-semibold text-slate-700">
                Descripción
              </label>
              <textarea
                id="productoDescripcion"
                value={formDescripcion}
                onChange={(event) => setFormDescripcion(event.target.value)}
                rows={4}
                className="w-full resize-none rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                placeholder="Descripción del producto (opcional)"
              />
            </div>

            {formError ? <p className="text-sm text-red-600">{formError}</p> : null}

            <div className="flex flex-wrap justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={closeModal}
                disabled={isSubmitting}
                className="rounded-xl border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-700 hover:bg-slate-50 transition-all disabled:cursor-not-allowed disabled:opacity-60"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleSave}
                disabled={isSubmitting || categorias.length === 0}
                className="rounded-xl bg-violet-600 px-6 py-3 text-sm font-bold text-white hover:bg-violet-700 transition-all disabled:cursor-not-allowed disabled:opacity-60"
              >
                {isSubmitting ? "Guardando..." : "Guardar"}
              </button>
            </div>
          </div>
        </Modal>
      </div>
    </MainLayout>
  );
};

export default ProductosPage;
