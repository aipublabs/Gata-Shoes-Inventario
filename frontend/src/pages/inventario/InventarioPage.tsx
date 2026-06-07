import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { getInventario, updateInventario, deleteInventario } from "../../api/axiosClient";
import type { Inventario } from "../../types";

type AdjustType = "add" | "sub" | "fix";

const InventarioPage = () => {
  const [inventario, setInventario] = useState<Inventario[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [selected, setSelected] = useState<Inventario | null>(null);
  const [adjustType, setAdjustType] = useState<AdjustType>("add");
  const [cantidad, setCantidad] = useState<number | "">("");
  const [motivo, setMotivo] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const fetchInventario = async () => {
    setIsLoading(true);
    try {
      const resp = await getInventario();
      const data = resp.data ?? [];
      setInventario(data);
      if (data.length > 0 && !selected) {
        setSelected(data[0]);
      }
    } catch (err) {
      console.error("Error cargando inventario:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchInventario();
  }, []);

  const filteredInventario = inventario.filter((item) => {
    if (!searchTerm) return true;
    const term = searchTerm.toLowerCase();
    return (
      item.producto.nombre.toLowerCase().includes(term) ||
      item.color.nombreColor.toLowerCase().includes(term) ||
      item.talla.numero.toLowerCase().includes(term)
    );
  });

  const calcPreview = (): number => {
    if (!selected) return 0;
    const qty = Number(cantidad) || 0;
    if (adjustType === "add") return selected.stock + qty;
    if (adjustType === "sub") return Math.max(0, selected.stock - qty);
    return qty;
  };

  const stockBadgeClass = (stock: number) => {
    if (stock <= 3) return "bg-error-container text-on-error-container";
    if (stock <= 10) return "bg-primary-fixed text-on-primary-fixed-variant";
    return "bg-secondary-container text-on-secondary-container";
  };

  const handleSelectItem = (item: Inventario) => {
    setSelected(item);
    setCantidad("");
    setMotivo("");
    setAdjustType("add");
    setSuccessMsg("");
    setErrorMsg("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  if (!selected) return;

  // ═══════════════════════════════════════════════════
  // VALIDACIONES
  // ═══════════════════════════════════════════════════

  if (cantidad === "" || Number(cantidad) < 0) {
    setErrorMsg("Ingresa una cantidad válida.");
    return;
  }

  // Validación: restar no puede superar el stock actual
  if (adjustType === "sub" && Number(cantidad) > selected.stock) {
    setErrorMsg(
      `No puedes restar ${cantidad} unidades. El stock actual es de ${selected.stock} unidades.`
    );
    return;
  }

  // Validación: fijar total no puede ser negativo
  if (adjustType === "fix" && Number(cantidad) < 0) {
    setErrorMsg("El stock fijado no puede ser negativo.");
    return;
  }

  const nuevoStock = calcPreview();
  setIsSaving(true);
  setErrorMsg("");
  setSuccessMsg("");

  try {
    // ═══════════════════════════════════════════════════
    // SI EL NUEVO STOCK ES 0 → ELIMINAR DEL INVENTARIO
    // El producto se conserva en la tabla productos
    // ═══════════════════════════════════════════════════
    if (nuevoStock === 0) {
      await deleteInventario(selected.idInventario);

      setSuccessMsg(
        `✅ Stock llegó a 0. La variante "${selected.producto.nombre} · ${selected.color.nombreColor} · Talla ${selected.talla.numero}" fue eliminada del inventario.`
      );

      // Recargar lista y limpiar selección
      const resp = await getInventario();
      const data = resp.data ?? [];
      setInventario(data);
      setSelected(data.length > 0 ? data[0] : null);

    } else {
      // ═══════════════════════════════════════════════════
      // STOCK > 0 → ACTUALIZAR NORMALMENTE
      // ═══════════════════════════════════════════════════
      await updateInventario(selected.idInventario, {
        idProducto: selected.producto.idProducto,
        idTalla: selected.talla.idTalla,
        idColor: selected.color.idColor,
        stock: nuevoStock,
      });

      setSuccessMsg(`✅ Stock actualizado a ${nuevoStock} unidades.`);

      // Recargar lista y actualizar seleccionado
      const resp = await getInventario();
      const data = resp.data ?? [];
      setInventario(data);
      const updated = data.find(
        (i) => i.idInventario === selected.idInventario
      );
      if (updated) setSelected(updated);
    }

    // Limpiar formulario
    setCantidad("");
    setMotivo("");
    setAdjustType("add");

  } catch (err) {
    console.error("Error actualizando stock:", err);
    setErrorMsg("No se pudo actualizar el stock. Intenta nuevamente.");
  } finally {
    setIsSaving(false);
  }
};

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <span className="text-slate-600">Cargando inventario...</span>
      </div>
    );
  }

  return (
    <MainLayout
      searchPlaceholder="Buscar SKU o modelo..."
      onSearch={setSearchTerm}
    >
      <div className="flex gap-8" style={{ height: "calc(100vh - 10rem)" }}>

        {/* ═══ PANEL IZQUIERDO — Lista inventario ═══ */}
        <section className="flex-[1.5] flex flex-col bg-surface-container-low rounded-3xl overflow-hidden shadow-sm">
          
          {/* Header panel */}
          <div className="p-6 bg-surface-container-high/50 flex justify-between items-center flex-shrink-0">
            <h3 className="font-headline font-bold text-lg text-on-surface">
              Catálogo de Calzado
            </h3>
            <button className="px-3 py-1.5 rounded-lg bg-surface-container-lowest text-xs font-semibold text-on-surface-variant shadow-sm flex items-center gap-2">
              <span className="material-symbols-outlined text-base">filter_list</span>
              Filtrar
            </button>
          </div>

          {/* Tabla */}
          <div className="flex-1 overflow-y-auto px-6 py-2">
            <table className="w-full text-left border-separate border-spacing-y-3">
              <thead className="sticky top-0 bg-surface-container-low z-10">
                <tr className="text-on-surface-variant text-xs font-bold tracking-widest uppercase">
                  <th className="pb-2 px-4">Producto</th>
                  <th className="pb-2 px-4">Color</th>
                  <th className="pb-2 px-4">Talla</th>
                  <th className="pb-2 px-4 text-right">Existencias</th>
                </tr>
              </thead>
              <tbody className="text-sm">
                {filteredInventario.length === 0 ? (
                  <tr>
                    <td colSpan={4} className="text-center py-10 text-slate-400">
                      No hay productos para mostrar.
                    </td>
                  </tr>
                ) : (
                  filteredInventario.map((item) => {
                    const isSelected =
                      selected?.idInventario === item.idInventario;
                    return (
                      <tr
                        key={item.idInventario}
                        onClick={() => handleSelectItem(item)}
                        className={`cursor-pointer transition-all ${
                          isSelected
                            ? "bg-surface-container-lowest shadow-[0_4px_20px_-10px_rgba(101,0,205,0.15)] ring-2 ring-primary/20"
                            : "hover:bg-surface-container-lowest"
                        } rounded-xl`}
                      >
                        {/* Producto */}
                        <td className="py-4 px-4 rounded-l-xl">
                          <div className="flex items-center gap-3">
                            <div className="h-10 w-10 rounded-full bg-surface-variant flex items-center justify-center overflow-hidden flex-shrink-0">
                              {item.producto.urlImagen ? (
                                <img
                                  src={item.producto.urlImagen}
                                  alt={item.producto.nombre}
                                  className="w-full h-full object-cover"
                                />
                              ) : (
                                <span className="material-symbols-outlined text-slate-400 text-sm">
                                  image_not_supported
                                </span>
                              )}
                            </div>
                            <span
                              className={`truncate max-w-[140px] ${
                                isSelected
                                  ? "font-bold text-on-surface"
                                  : "font-semibold text-on-surface"
                              }`}
                            >
                              {item.producto.nombre}
                            </span>
                          </div>
                        </td>

                        {/* Color */}
                        <td className="py-4 px-4 text-on-surface-variant">
                          {item.color.nombreColor}
                        </td>

                        {/* Talla */}
                        <td className="py-4 px-4 font-mono font-medium">
                          {item.talla.numero}
                        </td>

                        {/* Stock */}
                        <td className="py-4 px-4 text-right rounded-r-xl">
                          <span
                            className={`px-3 py-1 rounded-full font-bold text-xs ${stockBadgeClass(item.stock)}`}
                          >
                            {item.stock} uds
                          </span>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </section>

        {/* ═══ PANEL DERECHO — Ajuste de stock ═══ */}
        <section className="flex-1 flex flex-col bg-surface-container-lowest rounded-3xl overflow-hidden shadow-2xl shadow-violet-900/5 relative">
          
          {/* Gradiente decorativo */}
          <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-tertiary/5 pointer-events-none" />

          {selected ? (
            <div className="relative p-8 flex flex-col h-full z-10 overflow-y-auto gap-5">

              {/* Header detalle */}
              <header>
                <span className="text-primary font-bold text-xs tracking-widest uppercase mb-2 block">
                  Detalle de Selección
                </span>
                <div className="flex items-start justify-between gap-4">
                  <div className="flex-1 min-w-0">
                    <h3 className="text-3xl font-black text-on-surface tracking-tight leading-none truncate">
                      {selected.producto.nombre}
                    </h3>
                    <p className="text-on-surface-variant mt-1 text-sm">
                      {selected.producto.categoria?.nombreCategoria ?? "Sin categoría"}
                      {" · "}
                      {selected.color.nombreColor}
                      {" · "}
                      Talla {selected.talla.numero}
                    </p>
                  </div>
                  <div className="text-right flex-shrink-0">
                    <p className="text-xs font-bold text-on-surface-variant uppercase">
                      Stock Actual
                    </p>
                    <p
                      className={`text-4xl font-headline font-extrabold ${
                        selected.stock <= 3 ? "text-error" : "text-primary"
                      }`}
                    >
                      {selected.stock}
                    </p>
                  </div>
                </div>
              </header>

              {/* Preview imagen */}
              <div className="w-full aspect-square max-h-44 bg-surface-container-low rounded-2xl flex items-center justify-center overflow-hidden border border-outline-variant/20 shadow-inner group flex-shrink-0">
                {selected.producto.urlImagen ? (
                  <img
                    src={selected.producto.urlImagen}
                    alt={selected.producto.nombre}
                    className="w-4/5 h-auto object-contain transition-transform duration-500 group-hover:scale-110"
                  />
                ) : (
                  <div className="flex flex-col items-center gap-2 text-slate-400">
                    <span className="material-symbols-outlined text-4xl">
                      image_not_supported
                    </span>
                    <span className="text-xs">Sin imagen</span>
                  </div>
                )}
              </div>

              {/* Formulario */}
              <form className="flex flex-col gap-5 flex-1" onSubmit={handleSubmit}>

                {/* Tipo de ajuste */}
                <div>
                  <label className="text-sm font-bold text-on-surface-variant mb-3 block">
                    Tipo de Ajuste
                  </label>
                  <div className="grid grid-cols-3 gap-3">
                    {(
                      [
                        { value: "add", icon: "add_circle", label: "Añadir" },
                        { value: "sub", icon: "remove_circle", label: "Restar" },
                        { value: "fix", icon: "pin", label: "Fijar Total" },
                      ] as { value: AdjustType; icon: string; label: string }[]
                    ).map((opt) => (
                      <label key={opt.value} className="cursor-pointer group">
                        <input
                          type="radio"
                          name="adj_type"
                          value={opt.value}
                          checked={adjustType === opt.value}
                          onChange={() => {
                            setAdjustType(opt.value);
                            setCantidad("");
                          }}
                          className="sr-only peer"
                        />
                        <div className="py-3 px-2 text-center rounded-xl border border-outline-variant/30 bg-surface-container-low text-on-surface-variant font-semibold text-xs peer-checked:bg-primary-fixed peer-checked:border-primary peer-checked:text-on-primary-fixed-variant transition-all group-hover:border-primary/50">
                          <span className="material-symbols-outlined block mb-1 text-lg">
                            {opt.icon}
                          </span>
                          {opt.label}
                        </div>
                      </label>
                    ))}
                  </div>
                </div>

                {/* Cantidad + Previsión */}
                <div className="flex items-center gap-4">
                  <div className="flex-1">
                    <label className="text-sm font-bold text-on-surface-variant mb-2 block">
                      {adjustType === "fix" ? "Stock Final" : "Cantidad"}
                    </label>
                    <div className="relative">
                      <input
                        type="number"
                        min="0"
                        placeholder="0"
                        className="w-full py-4 px-6 bg-surface-container-low border-2 border-transparent rounded-2xl focus:border-primary focus:ring-0 text-2xl font-headline font-bold text-on-surface transition-all outline-none"
                        value={cantidad}
                        onChange={(e) => {
                          setCantidad(
                            e.target.value === "" ? "" : Number(e.target.value)
                          );
                          setSuccessMsg("");
                          setErrorMsg("");
                        }}
                      />
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 flex flex-col gap-1">
                        <button
                          type="button"
                          onClick={() =>
                            setCantidad((prev) =>
                              Math.max(0, (Number(prev) || 0) + 1)
                            )
                          }
                          className="text-primary hover:bg-primary-fixed p-1 rounded transition-colors"
                        >
                          <span className="material-symbols-outlined text-sm">
                            keyboard_arrow_up
                          </span>
                        </button>
                        <button
                          type="button"
                          onClick={() =>
                            setCantidad((prev) =>
                              Math.max(0, (Number(prev) || 0) - 1)
                            )
                          }
                          className="text-primary hover:bg-primary-fixed p-1 rounded transition-colors"
                        >
                          <span className="material-symbols-outlined text-sm">
                            keyboard_arrow_down
                          </span>
                        </button>
                      </div>
                    </div>
                  </div>

                  {/* Previsión */}
                  <div className="w-1/3 bg-tertiary-fixed/30 rounded-2xl p-4 flex flex-col justify-center items-center">
                    <span className="text-[10px] font-bold text-on-tertiary-fixed-variant uppercase opacity-70 mb-1">
                      Previsión
                    </span>
                    <span
                      className={`text-2xl font-black ${
                        calcPreview() <= 3 ? "text-error" : "text-tertiary"
                      }`}
                    >
                      {calcPreview()}
                    </span>
                    <span className="text-[10px] text-on-surface-variant mt-1">
                      unidades
                    </span>
                  </div>
                </div>

                {/* Motivo */}
                <div>
                  <label className="text-sm font-bold text-on-surface-variant mb-2 block">
                    Motivo de Ajuste
                  </label>
                  <textarea
                    rows={3}
                    placeholder="Ej: Recepción de pedido, Devolución cliente..."
                    className="w-full py-4 px-6 bg-surface-container-low border-2 border-transparent rounded-2xl focus:border-primary focus:ring-0 text-sm transition-all resize-none outline-none"
                    value={motivo}
                    onChange={(e) => setMotivo(e.target.value)}
                  />
                </div>

                {/* Mensajes feedback */}
                {successMsg && (
                  <div className="rounded-xl bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700 font-medium flex items-center gap-2">
                    <span className="material-symbols-outlined text-emerald-500 text-lg">
                      check_circle
                    </span>
                    {successMsg}
                  </div>
                )}
                {errorMsg && (
                  <div className="rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700 font-medium flex items-center gap-2">
                    <span className="material-symbols-outlined text-red-500 text-lg">
                      error
                    </span>
                    {errorMsg}
                  </div>
                )}

                {/* Botón submit */}
                <button
                  type="submit"
                  disabled={isSaving || cantidad === ""}
                  className="mt-auto w-full py-5 bg-gradient-to-r from-primary to-primary-container text-white rounded-2xl font-headline font-extrabold text-lg shadow-xl shadow-primary/25 hover:shadow-primary/40 active:scale-[0.98] transition-all flex items-center justify-center gap-3 disabled:opacity-60 disabled:cursor-not-allowed disabled:scale-100"
                >
                  <span className="material-symbols-outlined">
                    published_with_changes
                  </span>
                  {isSaving ? "Actualizando..." : "Actualizar Stock"}
                </button>

              </form>
            </div>
          ) : (
            /* Estado vacío — ningún producto seleccionado */
            <div className="relative z-10 flex flex-col items-center justify-center h-full text-center p-8">
              <div className="w-20 h-20 bg-primary-fixed rounded-full flex items-center justify-center mb-4">
                <span className="material-symbols-outlined text-primary text-4xl">
                  inventory_2
                </span>
              </div>
              <h3 className="text-xl font-bold text-on-surface mb-2">
                Selecciona un producto
              </h3>
              <p className="text-on-surface-variant text-sm">
                Haz click en cualquier producto de la lista para ajustar su stock.
              </p>
            </div>
          )}
        </section>

      </div>
    </MainLayout>
  );
};

export default InventarioPage;