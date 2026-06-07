import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { getAlertas, getInventario } from "../../api/axiosClient";
import type { Inventario } from "../../types";

const AlertasPage = () => {
  // Componente que muestra alertas de stock y métricas generales del inventario.
  // Combina la vista de productos críticos con indicadores de inventario total.
  const [alertas, setAlertas] = useState<Inventario[]>([]);
  const [todosInventario, setTodosInventario] = useState<Inventario[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  /*
    Carga los datos de alertas y del inventario completo en paralelo.
    Se usan dos endpoints porque las alertas son un subconjunto de productos
    con stock bajo, mientras que el inventario completo permite calcular métricas.
  */
  const fetchData = async () => {
    setIsLoading(true);
    try {
      const [alertasResp, inventarioResp] = await Promise.all([
        getAlertas(),
        getInventario(),
      ]);
      setAlertas(alertasResp.data ?? []);
      setTodosInventario(inventarioResp.data ?? []);
    } catch (err) {
      console.error("Error cargando datos:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Métricas que resumen el estado del inventario completo.
  // Estas cifras ayudan a priorizar acciones de reposición y control.
  const totalUnidades = todosInventario.reduce((acc, i) => acc + i.stock, 0);
  const alertasCriticas = alertas.length;
  const skusActivos = todosInventario.length;
  const valorInventario = todosInventario.reduce(
    (acc, i) => acc + i.stock * i.producto.precio,
    0
  );

  const todosConNivel = todosInventario.filter((item) => {
    if (!searchTerm) return true;
    const term = searchTerm.toLowerCase();
    return (
      item.producto.nombre.toLowerCase().includes(term) ||
      item.color.nombreColor.toLowerCase().includes(term) ||
      item.talla.numero.toLowerCase().includes(term)
    );
  });

  /*
    Clasifica el nivel de stock según reglas de negocio.
    - Stock Crítico: 0 a 3 unidades.
    - Bajo Stock: de 4 a 10 unidades.
    - Stock Saludable: más de 10 unidades.
    Esta función se usa para cambiar el color y la etiqueta de cada fila.
  */
  const getNivel = (stock: number) => {
    if (stock <= 3)
      return {
        label: "Stock Crítico",
        className: "bg-red-600 text-white",
        rowClass: "bg-secondary-container/30 hover:bg-secondary-container/50",
        amountClass: "text-error font-bold",
      };
    if (stock <= 10)
      return {
        label: "Bajo Stock",
        className: "bg-amber-100 text-amber-700",
        rowClass: "bg-white hover:bg-surface-container-low",
        amountClass: "text-amber-600 font-bold",
      };
    return {
      label: "Stock Saludable",
      className: "bg-green-100 text-green-700",
      rowClass: "bg-white hover:bg-surface-container-low",
      amountClass: "text-on-surface font-bold",
    };
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <span className="text-slate-600">Cargando alertas...</span>
      </div>
    );
  }

  return (
    <MainLayout
      searchPlaceholder="Buscar producto por nombre o color..."
      onSearch={setSearchTerm}
    >
      <div className="space-y-6">

        {/* Banner alerta crítica */}
        {alertasCriticas > 0 && (
          <div className="bg-error-container text-on-error-container px-6 py-4 rounded-xl flex items-center gap-4 shadow-sm">
            <div className="w-10 h-10 bg-error/10 rounded-full flex items-center justify-center flex-shrink-0">
              <span
                className="material-symbols-outlined text-error"
                style={{ fontVariationSettings: "'FILL' 1" }}
              >
                warning
              </span>
            </div>
            <div>
              <h2 className="font-headline font-bold text-lg leading-none">
                ¡Atención! Inventario Crítico
              </h2>
              <p className="text-sm opacity-80 mt-1">
                Hay <span className="font-bold">{alertasCriticas} SKUs</span> con
                existencias por debajo del umbral mínimo de seguridad (≤ 3 unidades).
              </p>
            </div>
          </div>
        )}

        {/* Header */}
        <div>
          <h3 className="font-headline text-3xl font-extrabold text-on-surface tracking-tight">
            Inventario de Productos
          </h3>
          <p className="text-on-surface-variant mt-1">
            Gestión detallada de existencias y niveles de stock por variante.
          </p>
        </div>

        {/* Tabla */}
        <div className="bg-surface-container-lowest rounded-2xl overflow-hidden shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead className="bg-on-secondary-fixed-variant text-white">
                <tr>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider">
                    ID
                  </th>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider">
                    Producto
                  </th>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider text-center">
                    Talla
                  </th>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider">
                    Color
                  </th>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider text-center">
                    Cantidad
                  </th>
                  <th className="px-6 py-4 font-headline text-xs font-bold uppercase tracking-wider">
                    Nivel
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-outline-variant/10">
                {todosConNivel.length === 0 ? (
                  <tr>
                    <td
                      colSpan={6}
                      className="px-6 py-12 text-center text-slate-400 font-medium"
                    >
                      No hay productos para mostrar.
                    </td>
                  </tr>
                ) : (
                  todosConNivel.map((item) => {
                    const nivel = getNivel(item.stock);
                    return (
                      <tr
                        key={item.idInventario}
                        className={`transition-colors ${nivel.rowClass}`}
                      >
                        {/* ID */}
                        <td className="px-6 py-4 text-xs font-mono text-slate-400">
                          #{item.idInventario}
                        </td>

                        {/* Producto */}
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-3">
                            <div className="w-12 h-12 rounded-lg bg-surface-variant overflow-hidden flex items-center justify-center flex-shrink-0">
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
                            <div>
                              <p className="font-semibold text-sm text-on-surface">
                                {item.producto.nombre}
                              </p>
                              <p className="text-[11px] text-on-surface-variant">
                                {item.producto.categoria?.nombreCategoria ?? "Sin categoría"}
                              </p>
                            </div>
                          </div>
                        </td>

                        {/* Talla */}
                        <td className="px-6 py-4 text-center font-medium text-sm">
                          {item.talla.numero}
                        </td>

                        {/* Color */}
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2">
                            <span className="w-3 h-3 rounded-full bg-slate-400 flex-shrink-0" />
                            <span className="text-sm">{item.color.nombreColor}</span>
                          </div>
                        </td>

                        {/* Cantidad */}
                        <td className={`px-6 py-4 text-center text-sm ${nivel.amountClass}`}>
                          {item.stock}
                        </td>

                        {/* Nivel */}
                        <td className="px-6 py-4">
                          <span
                            className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-tighter ${nivel.className}`}
                          >
                            {nivel.label}
                          </span>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Métricas footer */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 pt-2">

          {/* Unidades Totales */}
          <div className="bg-white p-5 rounded-2xl shadow-sm flex flex-col gap-1">
            <span className="text-on-surface-variant text-xs font-bold uppercase tracking-wider">
              Unidades Totales
            </span>
            <div className="flex items-end justify-between">
              <span className="font-headline text-3xl font-extrabold text-on-surface">
                {totalUnidades.toLocaleString("es-CO")}
              </span>
              <span className="material-symbols-outlined text-primary">
                inventory_2
              </span>
            </div>
          </div>

          {/* Valor Inventario */}
          <div className="bg-white p-5 rounded-2xl shadow-sm flex flex-col gap-1">
            <span className="text-on-surface-variant text-xs font-bold uppercase tracking-wider">
              Valor Inventario
            </span>
            <div className="flex items-end justify-between">
              <span className="font-headline text-3xl font-extrabold text-on-surface">
                ${(valorInventario / 1_000_000).toFixed(1)}M
              </span>
              <span className="text-slate-400 text-xs font-bold">COP</span>
            </div>
          </div>

          {/* Alertas Críticas */}
          <div className="bg-secondary-container/40 p-5 rounded-2xl shadow-sm flex flex-col gap-1 ring-1 ring-secondary-container">
            <span className="text-secondary text-xs font-bold uppercase tracking-wider">
              Alertas Críticas
            </span>
            <div className="flex items-end justify-between">
              <span className="font-headline text-3xl font-extrabold text-error">
                {alertasCriticas}
              </span>
              <span
                className="material-symbols-outlined text-error"
                style={{ fontVariationSettings: "'FILL' 1" }}
              >
                error
              </span>
            </div>
          </div>

          {/* SKUs Activos */}
          <div className="bg-white p-5 rounded-2xl shadow-sm flex flex-col gap-1">
            <span className="text-on-surface-variant text-xs font-bold uppercase tracking-wider">
              SKUs Activos
            </span>
            <div className="flex items-end justify-between">
              <span className="font-headline text-3xl font-extrabold text-on-surface">
                {skusActivos}
              </span>
              <span className="material-symbols-outlined text-primary">
                check_circle
              </span>
            </div>
          </div>

        </div>
      </div>
    </MainLayout>
  );
};

export default AlertasPage;