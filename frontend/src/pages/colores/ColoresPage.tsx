import { useEffect, useMemo, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import Modal from "../../components/ui/Modal";
import PageHeader from "../../components/ui/PageHeader";
import {
  createColor,
  deleteColor,
  getColores,
  updateColor,
} from "../../api/axiosClient";
import type { Color } from "../../types";

const ColoresPage = () => {
  const [colores, setColores] = useState<Color[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingColor, setEditingColor] = useState<Color | null>(null);
  const [formNombre, setFormNombre] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [pageError, setPageError] = useState("");
  const [formError, setFormError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadColores = async () => {
    setIsLoading(true);
    try {
      const response = await getColores();
      setColores(response.data ?? []);
      setPageError("");
    } catch {
      setPageError("No se pudo cargar la lista de colores. Intenta nuevamente.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadColores();
  }, []);

  const filteredColores = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();
    if (!normalizedSearch) return colores;

    return colores.filter((color) =>
      color.nombreColor.toLowerCase().includes(normalizedSearch)
    );
  }, [colores, searchTerm]);

  const openNewColor = () => {
    setEditingColor(null);
    setFormNombre("");
    setFormError("");
    setModalOpen(true);
  };

  const openEditColor = (color: Color) => {
    setEditingColor(color);
    setFormNombre(color.nombreColor);
    setFormError("");
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditingColor(null);
    setFormNombre("");
    setFormError("");
  };

  const handleSave = async () => {
    const nombre = formNombre.trim();

    if (!nombre) {
      setFormError("El nombre del color es obligatorio.");
      return;
    }

    if (nombre.length > 30) {
      setFormError("El nombre del color no puede superar los 30 caracteres.");
      return;
    }

    setIsSubmitting(true);
    setFormError("");
    try {
      if (editingColor) {
        await updateColor(editingColor.idColor, { nombreColor: nombre });
      } else {
        await createColor({ nombreColor: nombre });
      }

      await loadColores();
      closeModal();
    } catch {
      setFormError("No se pudo guardar el color. Intenta nuevamente.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async (colorId: number) => {
    const confirmed = window.confirm("¿Desea eliminar este color?");
    if (!confirmed) return;

    try {
      await deleteColor(colorId);
      await loadColores();
    } catch {
      setPageError("No se pudo eliminar el color. Intenta nuevamente.");
    }
  };

  return (
    <MainLayout searchPlaceholder="Buscar colores..." onSearch={setSearchTerm}>
      <div className="space-y-8">
        <PageHeader
          title="Gestión de Colores"
          description="Administración y control de colores"
          actionLabel="Nuevo Color"
          actionIcon="add"
          onAction={openNewColor}
        />

        {pageError ? (
          <div className="rounded-2xl border border-red-200 bg-red-50 px-5 py-4 text-sm text-red-700">
            {pageError}
          </div>
        ) : null}

        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b border-slate-100">
            <h2 className="text-2xl font-black text-slate-800">Listado de Colores</h2>
            <p className="text-xs text-slate-400 mt-1">CRUD completo de colores registrados.</p>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-sm">
              <thead className="bg-slate-50 text-slate-500 font-bold border-b">
                <tr>
                  <th className="text-left p-4">ID</th>
                  <th className="text-left p-4">Nombre Color</th>
                  <th className="text-center p-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {isLoading ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      Cargando colores...
                    </td>
                  </tr>
                ) : filteredColores.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      {colores.length === 0
                        ? "No hay colores registrados. Crea el primer color."
                        : "No se encontraron colores que coincidan con la búsqueda."}
                    </td>
                  </tr>
                ) : (
                  filteredColores.map((color) => (
                    <tr key={color.idColor} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-bold text-slate-600">{color.idColor}</td>
                      <td className="p-4 font-semibold text-slate-800">{color.nombreColor}</td>
                      <td className="p-4 text-center">
                        <div className="inline-flex flex-wrap items-center justify-center gap-2">
                          <button
                            type="button"
                            onClick={() => openEditColor(color)}
                            className="flex items-center gap-1 bg-amber-100 text-amber-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-amber-200 transition-all"
                          >
                            <span className="material-symbols-outlined text-sm">edit</span>
                            Editar
                          </button>
                          <button
                            type="button"
                            onClick={() => handleDelete(color.idColor)}
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
          title={editingColor ? "Editar Color" : "Nuevo Color"}
        >
          <div className="space-y-6">
            <div>
              <label htmlFor="colorNombre" className="block text-sm font-semibold text-slate-700 mb-2">
                Nombre Color
              </label>
              <input
                id="colorNombre"
                type="text"
                value={formNombre}
                maxLength={30}
                onChange={(event) => {
                  setFormNombre(event.target.value);
                  setFormError("");
                }}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                placeholder="Nombre del color"
              />
              {formError ? <p className="mt-2 text-sm text-red-600">{formError}</p> : null}
            </div>

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
                disabled={isSubmitting}
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

export default ColoresPage;