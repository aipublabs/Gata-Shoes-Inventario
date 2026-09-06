# Gata Shoes - Sistema de Inventario

## Evidencia GA8-220501096-AA1-EV01

**Desarrollar software a partir de la integración de sus módulos componentes**

- **Programa:** Análisis y Desarrollo de Software
- **Fase:** Ejecución
- **Rama:** `feature/GA8-220501096-AA1-EV01`
- **Repositorio:** https://github.com/aipublabs/Gata-Shoes-Inventario

## 1. Descripción

Gata Shoes es una aplicación web para administrar productos, categorías, variantes, existencias, alertas y acceso administrativo. La solución integra una interfaz desarrollada con React y TypeScript, servicios implementados con Spring Boot y Java 17, persistencia mediante Spring Data JPA y una base de datos MySQL.

La evidencia comprende la integración de los módulos, la aplicación de buenas prácticas de codificación, el uso del entorno de desarrollo, el control de versiones y la ejecución de pruebas unitarias.

## 2. Objetivo

Integrar y comprobar los módulos de Gata Shoes de acuerdo con los requerimientos del sistema, aplicando buenas prácticas de codificación, utilizando adecuadamente el entorno de desarrollo, registrando los cambios mediante Git y verificando las reglas principales mediante pruebas unitarias.

## 3. Arquitectura de la solución

```text
Interfaz React
    -> Comunicación con servicios
    -> API REST Spring Boot
    -> Servicios de negocio
    -> Repositorios Spring Data
    -> MySQL
```

### Capas

| Capa | Componentes | Responsabilidad |
|---|---|---|
| Presentación | React, páginas y componentes | Interacción con el administrador |
| Comunicación | Cliente de servicios | Envío y recepción de información |
| Exposición | Controladores REST | Recepción de operaciones del frontend |
| Negocio | Servicios Spring Boot | Aplicación de reglas funcionales |
| Persistencia | Repositorios Spring Data | Acceso a la información almacenada |
| Datos | MySQL | Almacenamiento del sistema |
| Seguridad | Spring Security, credenciales digitales y protección de contraseñas | Control de acceso y protección de información |

## 4. Módulos integrados

| Módulo | Funciones principales | Estado |
|---|---|---|
| Autenticación y sesión | Ingreso, protección de rutas y cierre de sesión | Interfaz y backend integrados |
| Tablero | Distribución por categoría, nuevos ingresos y Top 3 | Interfaz y backend integrados |
| Productos | Registro y mantenimiento de referencias | Formulario y servicios disponibles |
| Categorías | Listado, creación, actualización y eliminación | Interfaz y backend integrados |
| Colores y tallas | Catálogos asociados con las variantes | Servicios consumidos por los formularios |
| Inventario | Consulta y control de existencias | Interfaz y backend integrados |
| Ajuste de stock | Agregar, restar, fijar y eliminar al llegar a cero | Integración completada |
| Alertas | Consulta de referencias con existencias bajas | Interfaz y backend integrados |
| Administradores | Registro, consulta y eliminación | Servicios del backend |

## 5. Historias de usuario

| Código | Historia de usuario | Módulo relacionado |
|---|---|---|
| HU-01 | Autenticación de usuario | Autenticación |
| HU-02 | Tablero de control | Resumen |
| HU-03 | Registro de nuevas variantes | Productos e inventario |
| HU-04 | Consulta de ingresos recientes | Resumen |
| HU-05 | Ajuste manual de stock | Inventario |
| HU-06 | Administración de categorías | Categorías |
| HU-07 | Administración de colores | Colores |
| HU-08 | Administración de tallas | Tallas |
| HU-09 | Administración de productos | Productos |
| HU-10 | Consulta de alertas de stock | Alertas |
| HU-11 | Administración de usuarios administradores | Administradores |
| HU-12 | Cierre y continuidad de sesión | Sesión |

## 6. Casos de uso principales

| Código | Caso de uso | Resultado principal |
|---|---|---|
| CU-01 | Autenticar administrador | Permite o rechaza el acceso de forma controlada |
| CU-02 | Consultar tablero e ingresos recientes | Presenta indicadores, novedades y Top 3 |
| CU-03 | Gestionar categorías | Consulta y mantiene el catálogo |
| CU-04 | Registrar producto y variante | Incorpora una referencia con talla, color y stock |
| CU-05 | Consultar inventario | Presenta variantes y existencias |
| CU-06 | Ajustar stock | Agrega, resta, fija o elimina al llegar a cero |
| CU-07 | Consultar alertas | Identifica referencias con existencias bajas |
| CU-08 | Cerrar sesión | Finaliza el acceso a funciones protegidas |
| CU-09 | Renovar sesión | Mantiene el acceso autorizado o solicita un nuevo ingreso |

## 7. Criterios funcionales principales

### Autenticación y sesión

- Los campos obligatorios deben validarse.
- Las credenciales correctas permiten el acceso.
- Las credenciales incorrectas se rechazan mediante un mensaje general.
- Las funciones administrativas requieren una sesión válida.
- El cierre de sesión restringe nuevamente el acceso.

### Productos y variantes

- El producto requiere nombre, precio y categoría.
- La variante requiere talla, color y stock.
- El precio debe ser mayor que cero.
- El stock inicial no puede ser negativo.
- La categoría, la talla y el color deben estar disponibles.

### Ajuste de stock

- Añadir incrementa las existencias.
- Restar disminuye las existencias.
- Fijar reemplaza la cantidad actual.
- El stock no puede quedar negativo.
- Una variante se elimina cuando el stock llega a cero.

### Categorías

- Se pueden consultar, crear, actualizar y eliminar.
- El nombre es obligatorio.
- Una categoría inexistente genera una respuesta controlada.

### Alertas

- Las variantes con tres unidades o menos aparecen como alertas.
- Las variantes fuera del umbral no se incluyen.
- La consulta no modifica el inventario.

### Tablero

- Presenta información consolidada del inventario.
- Muestra nuevos ingresos.
- Presenta el Top 3 con Mayor Stock.
- Mantiene resultados válidos cuando no existen registros.

## 8. Integración del ajuste manual de stock

La pantalla de inventario consume el servicio especializado:

```text
PATCH /api/v1/inventario/{id}/stock
```

La interfaz envía únicamente el tipo de ajuste y la cantidad. El backend calcula el resultado y aplica las reglas de negocio.

Operaciones disponibles:

- `AGREGAR`
- `RESTAR`
- `FIJAR`

Reglas aplicadas:

- `AGREGAR` y `RESTAR` requieren una cantidad mayor que cero.
- `FIJAR` permite una cantidad igual a cero.
- El resultado no puede ser negativo.
- Una variante se elimina cuando el resultado llega a cero.
- La configuración de comunicación permite solicitudes `PATCH`.
- El campo visual de motivo fue retirado porque el sistema no lo almacenaba.
- El cálculo definitivo del stock permanece centralizado en el backend.

## 9. Validaciones funcionales

| Módulo | Validación | Resultado |
|---|---|---|
| Autenticación | Ingresar sin contraseña | Solicita el dato obligatorio |
| Autenticación | Ingresar con credenciales válidas o inválidas | Permite el acceso o rechaza mediante un mensaje general |
| Sesión | Acceder y cerrar sesión | Habilita o restringe las funciones protegidas |
| Tablero | Consultar el panel | Presenta distribución, nuevos ingresos y Top 3 |
| Productos | Registrar una referencia válida | La referencia queda disponible en catálogo e inventario |
| Productos | Usar datos incompletos o stock negativo | Solicita corregir la información |
| Categorías | Consultar, crear, editar y eliminar | El listado refleja las operaciones realizadas |
| Inventario | Fijar de 5 a 9 y restaurar a 5 | Actualizaciones reflejadas en interfaz y MySQL |
| Inventario | Agregar 2 y restar 2 | El stock cambia de 5 a 7 y regresa a 5 |
| Inventario | Usar cero o producir un resultado negativo | La operación es rechazada |
| Inventario | Fijar en cero una variante con una unidad | La variante se elimina |
| Alertas | Consultar inventario crítico | Presenta referencias bajo el umbral y su clasificación |

## 10. Organización del proyecto

### Frontend

```text
frontend/src/
├── api
├── components
├── contexts
├── hooks
├── pages
└── types
```

### Backend

```text
inventario/src/main/java/com/gatashoes/inventario/
├── api
│   ├── controller
│   ├── dto
│   ├── mapper
│   ├── exception
│   └── security
├── config
├── model
├── repository
└── service
```

## 11. Tecnologías y frameworks

| Capa | Tecnología | Propósito |
|---|---|---|
| Interfaz | React | Construcción de pantallas y componentes |
| Interfaz | TypeScript | Definición de tipos |
| Navegación | React Router | Control de rutas |
| Comunicación | Axios | Comunicación con los servicios |
| Construcción frontend | Vite | Ejecución y distribución |
| Backend | Spring Boot y Java 17 | Servicios web y reglas funcionales |
| Seguridad | Spring Security | Protección de recursos |
| Persistencia | Spring Data JPA | Acceso a datos |
| Validación | Bean Validation | Validación de entradas |
| Base de datos | MySQL Connector | Conexión con MySQL |
| Pruebas | JUnit 5 | Ejecución de pruebas unitarias |
| Pruebas | Mockito | Simulación de dependencias |
| Pruebas | AssertJ | Verificación de resultados |

## 12. Buenas prácticas aplicadas

- Arquitectura por capas.
- Separación de responsabilidades.
- Patrón repositorio.
- Objetos de transferencia para datos de entrada y salida.
- Mapeo controlado entre entidades y respuestas.
- Inyección de dependencias.
- Manejo centralizado de excepciones.
- Protección de solicitudes administrativas.
- Tipado del frontend.
- Componentes visuales reutilizables.
- Pruebas unitarias aisladas.
- Commits separados por propósito.
- Revisión automática de calidad del frontend finalizada sin errores.

## 13. Componentes reutilizables

| Componente | Uso |
|---|---|
| `MainLayout` | Estructura común de las páginas administrativas |
| `Sidebar` | Menú principal y acceso a módulos |
| `TopBar` | Barra superior, búsqueda e identificación del usuario |
| `Modal` | Presentación de formularios y operaciones emergentes |
| `MetricCard` | Presentación de indicadores del tablero |
| `PageHeader` | Encabezados uniformes |
| Componentes de formulario | Campos, botones y controles compartidos |

## 14. Metodología aplicada

El desarrollo se realizó de manera iterativa e incremental:

1. Revisión de requerimientos.
2. Diagnóstico de integración.
3. Implementación del cambio mínimo.
4. Validación funcional.
5. Creación y ejecución de pruebas unitarias.
6. Compilación y empaquetado.
7. Registro y publicación mediante Git.
8. Actualización de la documentación.

## 15. Ambientes

### Desarrollo

- Windows.
- Visual Studio Code.
- Java 17.
- Maven Wrapper.
- Node.js y npm.
- React y TypeScript.
- Spring Boot.
- MySQL.
- Git y GitHub.

### Pruebas unitarias

- JUnit 5.
- Mockito.
- AssertJ.
- Repositorios simulados.
- Sin conexión a MySQL.
- Sin inicio completo de Spring Boot.
- Sin solicitudes HTTP.

### Pruebas funcionales

- Frontend React.
- Backend Spring Boot.
- Navegador web.
- MySQL.
- Datos controlados.

## 16. Configuración y ejecución

### Requisitos generales

- Java 17.
- Node.js y npm.
- MySQL.
- Git.

Los datos sensibles deben suministrarse mediante variables de entorno. No deben almacenarse contraseñas, claves o credenciales digitales en el repositorio.

### Iniciar el backend

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml spring-boot:run
```

### Instalar dependencias del frontend

```powershell
npm --prefix frontend install
```

### Iniciar el frontend

```powershell
npm --prefix frontend run dev
```

## 17. Pruebas unitarias

| Clase | Cantidad | Cobertura principal |
|---|---:|---|
| `InventarioServiceTest` | 24 | Consultas, persistencia, alertas, novedades, Top 3 y ajuste de stock |
| `AdministradorServiceTest` | 14 | Consultas, registro, normalización, protección de contraseña, duplicados y eliminación |
| `AuthServiceTest` | 8 | Credenciales válidas e inválidas y tratamiento de contraseñas |
| `CategoriaServiceTest` | 10 | Listado, consulta, creación, actualización, eliminación e inexistentes |
| `ColorServiceTest` | 10 | Listado, consulta, creación, actualización, eliminación e inexistentes |
| `ProductoServiceTest` | 10 | Listado, consulta, creación, actualización, eliminación e inexistentes |
| `ResumenServiceTest` | 7 | Totales, alertas, categorías, novedades, Top 3 e inventario vacío |
| `JwtServiceTest` | 7 | Generación, lectura, validación y rechazo de credenciales alteradas |
| `TallaServiceTest` | 10 | Listado, consulta, creación, actualización, eliminación e inexistentes |
| **Total** | **100** | Cobertura consolidada de la solución |

Resultado consolidado:

```text
Tests run: 100
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

### Ejecutar las 100 pruebas unitarias

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml "-Dtest=InventarioServiceTest,AdministradorServiceTest,AuthServiceTest,CategoriaServiceTest,ProductoServiceTest,ResumenServiceTest,JwtServiceTest,ColorServiceTest,TallaServiceTest" test
```

## 18. Validaciones de calidad y compilación

### Revisión automática del frontend

```powershell
npm --prefix frontend run lint
```

La revisión finalizó sin errores.

### Compilar el frontend

```powershell
npm --prefix frontend run build
```

TypeScript compiló correctamente y Vite generó la distribución de producción.

### Compilar el backend

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml clean compile
```

### Empaquetar el backend

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml "-Dtest=InventarioServiceTest,AdministradorServiceTest,AuthServiceTest,CategoriaServiceTest,ProductoServiceTest,ResumenServiceTest,JwtServiceTest,ColorServiceTest,TallaServiceTest" package
```

Resultado:

- Archivo: `inventario-0.0.1-SNAPSHOT.jar`
- Tamaño verificado: `54.494.035 bytes`
- Estado: `BUILD SUCCESS`

## 19. Control de versiones

- **Rama:** `feature/GA8-220501096-AA1-EV01`
- **Repositorio remoto:** GitHub
- **Enlace:** https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA8-220501096-AA1-EV01

### Commits principales

| Commit | Propósito |
|---|---|
| `cf82654` | Integrar ajuste de stock entre frontend y backend |
| `ea496b7` | Corregir errores de calidad del frontend |
| `444fe96` | Agregar pruebas del servicio de inventario |
| `c4d9e58` | Agregar pruebas del servicio de administradores |
| `adc6592` | Completar pruebas de servicios principales |
| `97b1697` | Estabilizar validación de credencial alterada |
| `ec35a81` | Documentar integración y pruebas de EV01 |

## 20. Cumplimiento de la lista de chequeo

| Criterio | Evidencia | Estado |
|---|---|---|
| Codificación de módulos, 40 % | Módulos integrados, arquitectura, requerimientos, trazabilidad, validaciones y compilación | Cumplido |
| Buenas prácticas, 15 % | Capas, paquetes, patrones, tipos, reutilización, excepciones y metodología | Cumplido |
| Manejo del entorno de desarrollo, 15 % | Estructura, terminales, ejecución, compilación y pruebas desde Visual Studio Code | Cumplido |
| Control de versiones, 15 % | Rama específica, commits separados, sincronización y repositorio remoto | Cumplido |
| Pruebas unitarias, 15 % | Nueve clases y 100 pruebas aprobadas, sin fallos ni errores | Cumplido |

## 21. Seguridad

- No publicar contraseñas reales.
- No publicar claves o credenciales digitales completas.
- No publicar hashes.
- Utilizar variables de entorno.
- Proteger las contraseñas almacenadas.
- Restringir las funciones administrativas a sesiones válidas.
- No utilizar datos reales en las pruebas unitarias.
- Mantener las configuraciones locales sensibles fuera del repositorio.

## 22. Conclusiones

Gata Shoes integra React, Spring Boot y MySQL para atender autenticación, sesión, tablero, productos, catálogos, inventario y alertas. La estructura por capas, los paquetes, los patrones y los componentes reutilizables respaldan las buenas prácticas de codificación.

Las validaciones funcionales comprobaron los flujos principales, las 100 pruebas unitarias finalizaron correctamente, el frontend y el backend compilaron, el backend fue empaquetado y los cambios quedaron versionados y publicados en GitHub.
