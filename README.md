# Evidencia GA8-220501096-AA1-EV01

## Desarrollo de software a partir de la integración de sus módulos componentes

## 1. Información general

- Proyecto: Gata Shoes, Sistema de Inventario.
- Programa: Análisis y Desarrollo de Software.
- Fase: Ejecución.
- Evidencia: GA8-220501096-AA1-EV01.
- Tipo de evidencia: Desempeño.
- Rama: `feature/GA8-220501096-AA1-EV01`.
- Repositorio: https://github.com/aipublabs/Gata-Shoes-Inventario

## 2. Objetivo

La evidencia busca integrar los módulos de la solución web, validar su funcionamiento conjunto, aplicar buenas prácticas, utilizar control de versiones y comprobar las reglas principales mediante pruebas unitarias.

## 3. Arquitectura de la solución

- Frontend desarrollado con React y TypeScript.
- Comunicación entre la interfaz y los servicios mediante Axios.
- Backend desarrollado con Spring Boot y Java 17.
- Persistencia mediante Spring Data JPA.
- Base de datos MySQL.
- Seguridad mediante Spring Security, JWT y BCrypt.

Flujo de la solución:

```text
Interfaz React
→ comunicación con servicios
→ API REST Spring Boot
→ servicios de negocio
→ repositorios
→ MySQL
```

## 4. Módulos integrados

| Módulo | Funciones principales | Frontend | Backend | Estado |
|---|---|---|---|---|
| Autenticación | Inicio de sesión y control de sesión | Pantalla de login y contexto de autenticación | AuthService, JWT y endpoints de autenticación | Integrado |
| Administradores | Registro, consulta y eliminación | No hay pantalla dedicada identificada | AdministradorService y AdministradorRepository | Disponible mediante servicios backend |
| Resumen | Indicadores, novedades y mayor stock | Pantalla de resumen | ResumenService e InventarioRepository | Integrado |
| Categorías | Listar, crear, actualizar y eliminar | Pantalla de categorías | CategoriaService y CategoriaRepository | Integrado |
| Colores | CRUD de colores | Funciones Axios para consultas y CRUD; no hay pantalla dedicada | ColorService y ColorRepository | Disponible mediante servicios backend |
| Tallas | CRUD de tallas | Funciones Axios para consultas y CRUD; no hay pantalla dedicada | TallaService y TallaRepository | Disponible mediante servicios backend |
| Productos | Crear producto y operaciones CRUD | Creación desde el resumen; no hay pantalla CRUD dedicada | ProductoService y ProductoRepository | Parcialmente integrado |
| Inventario | Consultar y registrar variantes | Pantalla de inventario y modal de creación | InventarioService e InventarioRepository | Integrado |
| Ajuste manual de stock | Agregar, restar y fijar existencias | Formulario de inventario | Endpoint especializado y regla de negocio en InventarioService | Integrado |
| Alertas | Consultar stock bajo | Pantalla de alertas | ResumenRestController e InventarioService | Integrado |

## 5. Integración del ajuste manual de stock

Durante EV01 se integró la pantalla React de inventario con:

```text
PATCH /api/v1/inventario/{id}/stock
```

La integración soporta:

- `AGREGAR`.
- `RESTAR`.
- `FIJAR`.
- El frontend envía el tipo de ajuste y la cantidad.
- El backend calcula el resultado.
- El backend impide stock negativo.
- `AGREGAR` y `RESTAR` requieren una cantidad mayor que cero.
- `FIJAR` permite una cantidad igual a cero.
- Una variante se elimina cuando el stock llega a cero.
- Se agregó `PATCH` a los métodos permitidos por CORS.
- Se eliminó de la pantalla el campo `motivo` porque no era almacenado por el sistema.
- Se eliminó el cálculo local utilizado como fuente principal del stock.

## 6. Validaciones funcionales realizadas

1. `FIJAR` actualizó el inventario 3 de stock 5 a stock 9.
2. MySQL confirmó el stock 9.
3. `FIJAR` restauró el inventario 3 de stock 9 a stock 5.
4. MySQL confirmó la restauración a stock 5.
5. `AGREGAR` cambió el stock de 5 a 7.
6. `RESTAR` cambió el stock de 7 a 5.
7. MySQL confirmó nuevamente el stock 5.
8. `AGREGAR` con cantidad cero fue rechazado.
9. `RESTAR` una cantidad superior al stock fue rechazado.
10. Después de ambos rechazos, el stock se conservó en 5.
11. Se creó una variante temporal con identificador 32 y stock 1.
12. `FIJAR` la variante temporal en cero eliminó el registro.
13. La consulta posterior confirmó que la variante temporal ya no existía.

El inventario real 3 quedó restaurado en stock 5. La variante temporal 32 fue eliminada y no quedaron datos temporales de esa validación.

## 7. Pruebas unitarias

Las pruebas unitarias verifican servicios y reglas específicas sin iniciar toda la aplicación ni conectarse a MySQL.

| Clase | Pruebas |
|---|---:|
| `InventarioServiceTest` | 24 |
| `AdministradorServiceTest` | 14 |
| `AuthServiceTest` | 8 |
| `CategoriaServiceTest` | 10 |
| `ColorServiceTest` | 10 |
| `ProductoServiceTest` | 10 |
| `ResumenServiceTest` | 7 |
| `JwtServiceTest` | 7 |
| `TallaServiceTest` | 10 |

Resultado consolidado:

- Pruebas ejecutadas: 100.
- Pruebas aprobadas: 100.
- Pruebas fallidas: 0.
- Errores: 0.
- Pruebas omitidas: 0.
- Resultado: `BUILD SUCCESS`.

Se utilizaron JUnit 5, Mockito y AssertJ. Los repositorios fueron simulados. No se utilizó MySQL, no se modificaron datos reales, no se realizaron solicitudes HTTP y no se inició Spring Boot para las pruebas unitarias. `InventarioApplicationTests` no se incluyó en la ejecución focalizada de las 100 pruebas.

## 8. Cobertura funcional de las pruebas

| Servicio | Funciones verificadas |
|---|---|
| `InventarioService` | Consultas, guardado y actualización, eliminación, alertas, novedades, mayor stock, `AGREGAR`, `RESTAR`, `FIJAR`, cantidades inválidas, stock negativo y eliminación al llegar a cero. |
| `AdministradorService` | Consultas, registro, normalización, protección de contraseña, correo duplicado y eliminación. |
| `AuthService` | Credenciales válidas e inválidas, contraseñas protegidas, migración de contraseñas sin protección y mensaje general de acceso inválido. |
| `CategoriaService` | Listado, consulta, guardado, actualización, eliminación y recursos inexistentes. |
| `ColorService` | Listado, consulta, guardado, actualización, eliminación y recursos inexistentes. |
| `TallaService` | Listado, consulta, guardado, actualización, eliminación y recursos inexistentes. |
| `ProductoService` | Listado, consulta, guardado, actualización, eliminación y recursos inexistentes. |
| `ResumenService` | Total de variantes, stock total, alertas, distribución por categoría, novedades, mayor stock e inventario vacío. |
| `JwtService` | Generación de tokens, extracción de correo, validación y rechazo de tokens malformados o alterados. |

## 9. Buenas prácticas aplicadas

- Separación por capas.
- Uso de DTO.
- Uso de servicios.
- Uso de repositorios.
- Validaciones centralizadas en el backend.
- Tipos definidos en TypeScript.
- Eliminación del uso de tipos genéricos que reducían la seguridad del código.
- Eliminación de variables sin utilizar.
- Revisión automática de calidad del código frontend finalizada sin errores.
- Manejo funcional de errores.
- Pruebas independientes.
- Repositorios simulados.
- Ausencia de datos reales en las pruebas.
- Commits separados por propósito.

## 10. Compilación y empaquetado

### Frontend

- TypeScript compiló correctamente.
- Vite generó la distribución de producción.
- Resultado exitoso.

### Backend

- Java 17.
- Maven compiló correctamente.
- Las 100 pruebas fueron ejecutadas durante el empaquetado.
- Resultado: `BUILD SUCCESS`.
- Archivo generado: `inventario-0.0.1-SNAPSHOT.jar`.
- Tamaño verificado: `54494035` bytes.

La advertencia de API obsoleta en `JwtService` no impidió la compilación.

## 11. Ambiente de desarrollo y pruebas

| Componente | Uso |
|---|---|
| Windows | Sistema operativo de desarrollo |
| Visual Studio Code | IDE de desarrollo |
| Java 17 | Lenguaje del backend |
| Spring Boot 3.3.5 | Framework del backend |
| Maven Wrapper | Construcción, pruebas y empaquetado |
| React | Interfaz frontend |
| TypeScript | Tipado del frontend |
| Vite | Desarrollo y construcción frontend |
| Node.js y npm | Dependencias y scripts frontend |
| MySQL | Persistencia de datos |
| Git | Control de versiones |
| GitHub | Repositorio remoto |
| Navegador web | Ejecución y validación funcional del frontend |

Puertos comprobados:

- Frontend: `5173`.
- Backend: `8081`.

## 12. Ejecución del proyecto

Configurar MySQL con la base de datos del proyecto y definir la contraseña mediante una variable de entorno segura. No almacenar credenciales directamente en la documentación ni en el repositorio.

Iniciar el backend desde la carpeta del proyecto Java:

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml spring-boot:run
```

Instalar las dependencias del frontend e iniciar Vite:

```powershell
Set-Location .\frontend
npm install
npm run dev
```

Acceder desde el navegador a:

```text
http://localhost:5173
```

## 13. Ejecución de las pruebas

Ejecutar las nueve clases unitarias:

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml "-Dtest=InventarioServiceTest,AdministradorServiceTest,AuthServiceTest,CategoriaServiceTest,ProductoServiceTest,ResumenServiceTest,JwtServiceTest,ColorServiceTest,TallaServiceTest" test
```

Ejecutar la revisión automática de calidad del frontend:

```powershell
npm --prefix frontend run lint
```

Compilar el frontend:

```powershell
npm run build
```

Empaquetar el backend:

```powershell
.\inventario\mvnw.cmd -f .\inventario\pom.xml package
```

## 14. Control de versiones

- Rama: `feature/GA8-220501096-AA1-EV01`.
- Repositorio remoto: GitHub.

Commits de la evidencia:

- `cf82654`: integrar ajuste de stock entre frontend y backend.
- `ea496b7`: corregir errores de calidad del código frontend.
- `444fe96`: agregar pruebas unitarias del servicio de inventario.
- `c4d9e58`: agregar pruebas unitarias del servicio de administradores.
- `adc6592`: completar pruebas unitarias de los servicios principales.
- `97b1697`: estabilizar validación de token JWT alterado.

Enlace de la rama:

[feature/GA8-220501096-AA1-EV01](https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA8-220501096-AA1-EV01)

## 15. Cumplimiento de los criterios de evaluación

| Criterio | Evidencia de cumplimiento | Estado |
|---|---|---|
| 1. Codifica los módulos de acuerdo con los requerimientos del sistema en el lenguaje seleccionado. | Módulos React, API REST Spring Boot, servicios, repositorios e integración del ajuste de stock. | Cumplido |
| 2. Aplica buenas prácticas de codificación. | Separación por capas, DTO, validaciones backend, tipos TypeScript y pruebas aisladas. | Cumplido |
| 3. Maneja de manera adecuada el IDE de desarrollo. | Desarrollo y organización del proyecto en Visual Studio Code. | Cumplido |
| 4. Aplica el control de versiones del código escrito. | Rama de evidencia y commits separados por propósito en Git y GitHub. | Cumplido |
| 5. Aplica pruebas unitarias. | 100 pruebas unitarias ejecutadas con 100 aprobadas, 0 fallidas y 0 errores. | Cumplido |

## 16. Consideraciones de seguridad

- No publicar contraseñas reales.
- No publicar tokens completos.
- No publicar claves JWT.
- No publicar hashes.
- Utilizar variables de entorno.
- Proteger las contraseñas.
- Limitar el acceso a las funciones administrativas.
- No usar datos reales en las pruebas unitarias.
- No incluir secretos en la documentación.

## 17. Conclusiones

Los módulos principales trabajan de forma integrada. React se comunica con Spring Boot y Spring Boot administra la información en MySQL. El ajuste de stock quedó centralizado en el backend, con validaciones para las operaciones `AGREGAR`, `RESTAR` y `FIJAR`.

Las validaciones funcionales fueron aprobadas. Las 100 pruebas unitarias fueron aprobadas. El frontend y el backend compilaron correctamente, y el backend fue empaquetado correctamente en `inventario-0.0.1-SNAPSHOT.jar`.

El trabajo fue versionado mediante Git y GitHub. Con la evidencia disponible, los criterios de EV01 quedaron cubiertos.
