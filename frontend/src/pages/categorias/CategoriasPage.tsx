import { useEffect, useMemo, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import Modal from "../../components/ui/Modal";
import PageHeader from "../../components/ui/PageHeader";
import {
  createCategoria,
  deleteCategoria,
  getCategorias,
  updateCategoria,
} from "../../api/axiosClient";
import type { Categoria } from "../../types";

const CategoriasPage = () => {
  // Componente para gestionar el catálogo de categorías.
  // Permite crear, editar y eliminar categorías desde una vista única.
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingCategoria, setEditingCategoria] = useState<Categoria | null>(null);
  const [formNombre, setFormNombre] = useState("");
  const [error, setError] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadCategorias = async () => {
    setIsLoading(true);
    try {
      const response = await getCategorias();
      console.log('[CategoriasPage] Datos recibidos:', response.data);
      setCategorias(response.data);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('[CategoriasPage] Error al cargar:', err);
      setError("No se pudo cargar la lista de categorías.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadCategorias();
  }, []);

  const filteredCategorias = useMemo(() => {
    if (!searchTerm.trim()) return categorias;
    return categorias.filter((categoria) =>
      categoria.nombreCategoria.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [categorias, searchTerm]);

  const openNewCategoria = () => {
    setEditingCategoria(null);
    setFormNombre("");
    setError("");
    setModalOpen(true);
  };

  const openEditCategoria = (categoria: Categoria) => {
    setEditingCategoria(categoria);
    setFormNombre(categoria.nombreCategoria);
    setError("");
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditingCategoria(null);
    setFormNombre("");
    setError("");
  };

  /*
    Guarda una categoría nueva o actualiza una existente.
    Valida que el nombre no esté vacío y luego recarga la lista
    para reflejar los cambios inmediatamente.
  */
  const handleSave = async () => {
    const nombre = formNombre.trim();
    if (!nombre) {
      setError("El nombre de la categoría es obligatorio.");
      return;
    }

    setIsSubmitting(true);
    try {
      if (editingCategoria) {
        await updateCategoria(editingCategoria.idCategoria, { nombreCategoria: nombre });
      } else {
        await createCategoria({ nombreCategoria: nombre });
      }
      await loadCategorias();
      closeModal();
    } catch {
      setError("No se pudo guardar la categoría. Intenta nuevamente.");
    } finally {
      setIsSubmitting(false);
    }
  };

  /*
    Elimina la categoría seleccionada tras confirmar con el usuario.
    Después de borrar, recarga el catálogo para que la interfaz muestre
    la información actualizada sin necesidad de refrescar la página.
  */
  const handleDelete = async (categoriaId: number) => {
    const confirmed = window.confirm("¿Desea eliminar esta categoría?");
    if (!confirmed) return;

    try {
      await deleteCategoria(categoriaId);
      await loadCategorias();
    } catch {
      setError("No se pudo eliminar la categoría. Intenta nuevamente.");
    }
  };

  return (
    <MainLayout searchPlaceholder="Buscar categorías..." onSearch={setSearchTerm}>
      <div className="space-y-8">
        <PageHeader
          title="Gestión de Categorías"
          description="Administración y control de categorías"
          actionLabel="Nueva Categoría"
          actionIcon="add"
          onAction={openNewCategoria}
        />

        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b border-slate-100">
            <h2 className="text-2xl font-black text-slate-800">Listado de Categorías</h2>
            <p className="text-xs text-slate-400 mt-1">CRUD completo de categorías registradas.</p>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-sm">
              <thead className="bg-slate-50 text-slate-500 font-bold border-b">
                <tr>
                  <th className="text-left p-4">ID</th>
                  <th className="text-left p-4">Nombre Categoría</th>
                  <th className="text-center p-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {isLoading ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      Cargando categorías...
                    </td>
                  </tr>
                ) : filteredCategorias.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      No se encontraron categorías.
                    </td>
                  </tr>
                ) : (
                  filteredCategorias.map((categoria) => (
                    <tr key={categoria.idCategoria} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-bold text-slate-600">{categoria.idCategoria}</td>
                      <td className="p-4 font-semibold text-slate-800">{categoria.nombreCategoria}</td>
                      <td className="p-4 text-center">
                        <div className="inline-flex flex-wrap items-center justify-center gap-2">
                          <button
                            type="button"
                            onClick={() => openEditCategoria(categoria)}
                            className="flex items-center gap-1 bg-amber-100 text-amber-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-amber-200 transition-all"
                          >
                            <span className="material-symbols-outlined text-sm">edit</span>
                            Editar
                          </button>
                          <button
                            type="button"
                            onClick={() => handleDelete(categoria.idCategoria)}
                            className="flex items-center gap-1 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-red-200 transition-all"
                          >
                            <span className="material-symbols-outlined text-sm">delete</span>
                            Eliminar
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
          title={editingCategoria ? "Editar Categoría" : "Nueva Categoría"}
        >
          <div className="space-y-6">
            <div>
              <label htmlFor="categoriaNombre" className="block text-sm font-semibold text-slate-700 mb-2">
                Nombre Categoría
              </label>
              <input
                id="categoriaNombre"
                type="text"
                value={formNombre}
                onChange={(event) => setFormNombre(event.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                placeholder="Nombre de la categoría"
              />
              {error ? <p className="mt-2 text-sm text-red-600">{error}</p> : null}
            </div>

            <div className="flex flex-wrap justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={closeModal}
                className="rounded-xl border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-700 hover:bg-slate-50 transition-all"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleSave}
                disabled={isSubmitting}
                className="rounded-xl bg-violet-600 px-6 py-3 text-sm font-bold text-white hover:bg-violet-700 transition-all disabled:cursor-not-allowed disabled:opacity-60"
              >
                Guardar
              </button>
            </div>
          </div>
        </Modal>
      </div>
    </MainLayout>
  );
};

export default CategoriasPage;
