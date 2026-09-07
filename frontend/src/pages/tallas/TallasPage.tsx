import { useEffect, useMemo, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import Modal from "../../components/ui/Modal";
import PageHeader from "../../components/ui/PageHeader";
import {
  createTalla,
  deleteTalla,
  getTallas,
  updateTalla,
} from "../../api/axiosClient";
import type { Talla } from "../../types";

const TallasPage = () => {
  const [tallas, setTallas] = useState<Talla[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingTalla, setEditingTalla] = useState<Talla | null>(null);
  const [formNumero, setFormNumero] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [pageError, setPageError] = useState("");
  const [formError, setFormError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadTallas = async () => {
    setIsLoading(true);
    try {
      const response = await getTallas();
      setTallas(response.data ?? []);
      setPageError("");
    } catch {
      setPageError("No se pudo cargar la lista de tallas. Intenta nuevamente.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadTallas();
  }, []);

  const filteredTallas = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();
    if (!normalizedSearch) return tallas;

    return tallas.filter((talla) =>
      talla.numero.toLowerCase().includes(normalizedSearch)
    );
  }, [tallas, searchTerm]);

  const openNewTalla = () => {
    setEditingTalla(null);
    setFormNumero("");
    setFormError("");
    setModalOpen(true);
  };

  const openEditTalla = (talla: Talla) => {
    setEditingTalla(talla);
    setFormNumero(talla.numero);
    setFormError("");
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditingTalla(null);
    setFormNumero("");
    setFormError("");
  };

  const handleSave = async () => {
    const numero = formNumero.trim();

    if (!numero) {
      setFormError("La talla es obligatoria.");
      return;
    }

    if (numero.length > 10) {
      setFormError("La talla no puede superar los 10 caracteres.");
      return;
    }

    setIsSubmitting(true);
    setFormError("");
    try {
      if (editingTalla) {
        await updateTalla(editingTalla.idTalla, { numero });
      } else {
        await createTalla({ numero });
      }

      await loadTallas();
      closeModal();
    } catch {
      setFormError("No se pudo guardar la talla. Intenta nuevamente.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async (tallaId: number) => {
    const confirmed = window.confirm("¿Desea eliminar esta talla?");
    if (!confirmed) return;

    try {
      await deleteTalla(tallaId);
      await loadTallas();
    } catch {
      setPageError("No se pudo eliminar la talla. Intenta nuevamente.");
    }
  };

  return (
    <MainLayout searchPlaceholder="Buscar tallas..." onSearch={setSearchTerm}>
      <div className="space-y-8">
        <PageHeader
          title="Gestión de Tallas"
          description="Administración y control de tallas"
          actionLabel="Nueva Talla"
          actionIcon="add"
          onAction={openNewTalla}
        />

        {pageError ? (
          <div className="rounded-2xl border border-red-200 bg-red-50 px-5 py-4 text-sm text-red-700">
            {pageError}
          </div>
        ) : null}

        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b border-slate-100">
            <h2 className="text-2xl font-black text-slate-800">Listado de Tallas</h2>
            <p className="text-xs text-slate-400 mt-1">CRUD completo de tallas registradas.</p>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-sm">
              <thead className="bg-slate-50 text-slate-500 font-bold border-b">
                <tr>
                  <th className="text-left p-4">ID</th>
                  <th className="text-left p-4">Talla</th>
                  <th className="text-center p-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 bg-white">
                {isLoading ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      Cargando tallas...
                    </td>
                  </tr>
                ) : filteredTallas.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="p-6 text-center text-slate-500">
                      {tallas.length === 0
                        ? "No hay tallas registradas. Crea la primera talla."
                        : "No se encontraron tallas que coincidan con la búsqueda."}
                    </td>
                  </tr>
                ) : (
                  filteredTallas.map((talla) => (
                    <tr key={talla.idTalla} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-bold text-slate-600">{talla.idTalla}</td>
                      <td className="p-4 font-semibold text-slate-800">{talla.numero}</td>
                      <td className="p-4 text-center">
                        <div className="inline-flex flex-wrap items-center justify-center gap-2">
                          <button
                            type="button"
                            onClick={() => openEditTalla(talla)}
                            className="flex items-center gap-1 bg-amber-100 text-amber-700 px-4 py-2 rounded-lg text-xs font-bold hover:bg-amber-200 transition-all"
                          >
                            <span className="material-symbols-outlined text-sm">edit</span>
                            Editar
                          </button>
                          <button
                            type="button"
                            onClick={() => handleDelete(talla.idTalla)}
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
          title={editingTalla ? "Editar Talla" : "Nueva Talla"}
        >
          <div className="space-y-6">
            <div>
              <label htmlFor="tallaNumero" className="block text-sm font-semibold text-slate-700 mb-2">
                Talla
              </label>
              <input
                id="tallaNumero"
                type="text"
                value={formNumero}
                maxLength={10}
                onChange={(event) => {
                  setFormNumero(event.target.value);
                  setFormError("");
                }}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none focus:ring-2 focus:ring-violet-300"
                placeholder="Número o representación de la talla"
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

export default TallasPage;