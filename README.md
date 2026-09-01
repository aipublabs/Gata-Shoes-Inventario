# Evidencia GA7-220501096-AA5-EV04

## API del proyecto Gata Shoes

## 1. Información general

- Evidencia: GA7-220501096-AA5-EV04.
- Tipo de evidencia: Producto.
- Nombre: API del Proyecto.
- Proyecto: Gata Shoes, Sistema de Inventario.
- Programa: Análisis y Desarrollo de Software.
- Backend: Spring Boot 3.3.5 y Java 17.
- Frontend: React y TypeScript.
- Base de datos: MySQL 8.
- Herramienta de pruebas: Postman.
- Seguridad: Spring Security, JWT y BCrypt.
- Rama: feature/GA7-220501096-AA5-EV04.

## 2. Objetivo

El objetivo fue probar, documentar y presentar las API REST del proyecto Gata Shoes mediante Postman. Las pruebas cubrieron los servicios del backend relacionados con:

- autenticación y registro;
- administradores;
- categorías;
- colores;
- tallas;
- productos;
- inventario;
- ajuste manual de stock;
- resumen;
- alertas;
- limpieza de datos temporales.

## 3. Alcance

La colección de Postman prueba los servicios REST disponibles en el backend del proyecto Gata Shoes. Para esta evidencia se utilizaron datos temporales, los identificadores se almacenaron en variables dinámicas, las solicitudes se ejecutaron en orden y los datos temporales fueron eliminados al finalizar. La ejecución no modificó permanentemente los registros reales del proyecto.

## 4. Criterios de evaluación

| N.º | Criterio de evaluación | Evidencia de cumplimiento | Estado |
|---:|---|---|---|
| 1 | Realiza el test de las API del proyecto utilizando Postman. | Colección ejecutada con 40 solicitudes, 80 pruebas aprobadas, 0 fallidas y 0 errores. | Cumplido |
| 2 | Realiza el video solicitado mostrando las pruebas de las API del proyecto. | Video con la ejecución del Collection Runner y explicación de los resultados. | Cumplido |
| 3 | Realiza la documentación de las API del proyecto. | Documento con descripción de pruebas, resultados y pantallazos. | Cumplido |
| 4 | Entrega los endpoints de las API del proyecto desarrolladas. | Archivo ENDPOINTS_AA5_EV04.txt con los 35 endpoints documentados. | Cumplido |

## 5. Colección de Postman

La colección utilizada para esta evidencia se denomina:

GA7-220501096-AA5-EV04 - API del Proyecto Gata Shoes

La colección contiene 40 solicitudes organizadas en nueve carpetas:

1. 01 - Autenticación y registro: 3 solicitudes.
2. 02 - Administradores: 2 solicitudes.
3. 03 - Categorías: 4 solicitudes.
4. 04 - Colores: 4 solicitudes.
5. 05 - Tallas: 4 solicitudes.
6. 06 - Productos: 4 solicitudes.
7. 07 - Inventario y ajuste de stock: 10 solicitudes.
8. 08 - Resumen y alertas: 2 solicitudes.
9. 09 - Limpieza de datos temporales: 7 solicitudes.

La colección utiliza:

- scripts Pre-request;
- scripts Post-response;
- variables dinámicas;
- autenticación Bearer Token;
- encadenamiento de solicitudes;
- validaciones automáticas;
- limpieza de datos temporales.

## 6. Variables de la colección

| Variable | Propósito |
|---|---|
| baseUrl | URL base de la API. |
| marcaTiempoEV04 | Identificador dinámico de la ejecución. |
| correoAdminEV04 | Correo temporal del administrador. |
| contrasenaAdminEV04 | Contraseña temporal de pruebas. |
| accessTokenEV04 | Token utilizado por las solicitudes protegidas. |
| idAdminEV04 | Identificador del administrador temporal. |
| nombreCategoriaEV04 | Nombre dinámico de categoría. |
| idCategoriaEV04 | Identificador de categoría. |
| nombreColorEV04 | Nombre dinámico del color. |
| idColorEV04 | Identificador del color. |
| numeroTallaEV04 | Número dinámico de talla. |
| idTallaEV04 | Identificador de talla. |
| nombreProductoEV04 | Nombre dinámico del producto. |
| idProductoEV04 | Identificador del producto. |
| idInventarioEV04 | Identificador del inventario. |

Los valores sensibles no deben mostrarse en el README, el documento ni el video.

## 7. Módulos probados

| Módulo | Cantidad de solicitudes | Operaciones principales | Seguridad |
|---|---:|---|---|
| Autenticación y registro | 3 | Registro, login y rechazo de credenciales incorrectas | Endpoints públicos |
| Administradores | 2 | Listar y consultar administrador temporal | Protegidos por JWT |
| Categorías | 4 | Crear, consultar, actualizar y listar | Protegidas por JWT |
| Colores | 4 | Crear, consultar, actualizar y listar | Protegidos por JWT |
| Tallas | 4 | Crear, consultar, actualizar y listar | Protegidas por JWT |
| Productos | 4 | Crear, consultar, actualizar y listar | Protegidos por JWT |
| Inventario y stock | 10 | Crear, consultar, listar, actualizar, agregar stock, restar stock, fijar stock y validar operaciones inválidas | Protegido por JWT |
| Resumen y alertas | 2 | Consultar métricas y alertas | Protegidos por JWT |
| Limpieza | 7 | Eliminar datos temporales y limpiar variables | Protegida por JWT |

## 8. Resultados de la ejecución

La ejecución se realizó con Collection Runner y se registraron los siguientes resultados:

- Fuente: Collection Runner.
- Iteraciones: 1.
- Solicitudes ejecutadas: 40.
- Pruebas ejecutadas: 80.
- Pruebas aprobadas: 80.
- Pruebas fallidas: 0.
- Solicitudes omitidas: 0.
- Errores: 0.
- Duración de la ejecución validada: 4 segundos y 726 milisegundos.
- Tiempo promedio de respuesta: 32 milisegundos.

Los códigos HTTP 400, 401, 404 y 409 mostrados en algunos casos corresponden a resultados esperados de pruebas negativas y no representan fallos de la colección.

Para la grabación del video se utilizó un delay en Collection Runner para facilitar la visualización del avance.

## 9. Pruebas del ajuste manual de stock

El endpoint probado fue:

PATCH /api/v1/inventario/{id}/stock

Las operaciones documentadas fueron:

- AGREGAR.
- RESTAR.
- FIJAR.

Las validaciones comprobadas fueron:

- HTTP 200 cuando el stock resultante es mayor que cero.
- HTTP 204 cuando el stock llega a cero y la variante se elimina.
- HTTP 400 cuando se intenta obtener stock negativo.
- HTTP 400 cuando AGREGAR o RESTAR recibe cantidad cero.
- HTTP 400 para un tipo de ajuste no válido.
- HTTP 404 cuando el inventario no existe.

Estas reglas se centralizan en el backend para garantizar la integridad del inventario.

## 10. Limpieza de los datos temporales

La colección elimina los datos en orden inverso a su creación:

1. Inventario.
2. Producto.
3. Talla.
4. Color.
5. Categoría.
6. Administrador.

Se aclara que:

- el inventario se elimina mediante FIJAR con cantidad cero;
- se verifica posteriormente que el inventario no exista;
- las variables temporales se limpian al finalizar;
- los registros reales del proyecto no son eliminados.

## 11. Endpoints documentados

El archivo:

ENDPOINTS_AA5_EV04.txt

documenta 35 endpoints distribuidos así:

- Autenticación y registro: 4.
- Administradores: 3.
- Categorías: 5.
- Colores: 5.
- Tallas: 5.
- Productos: 5.
- Inventario: 6.
- Resumen y alertas: 2.

Total: 35 endpoints.

Para cada endpoint se documentan:

- método HTTP;
- ruta;
- propósito;
- seguridad;
- DTO de entrada;
- DTO de salida;
- códigos HTTP;
- validaciones principales.

## 12. Evidencias generadas

La entrega de esta evidencia incluye:

- Código fuente del proyecto.
- Documento de pruebas con pantallazos.
- Video mostrando la ejecución.
- Colección Postman exportada.
- Archivo con todos los endpoints.
- Archivo con el enlace de la rama.
- README específico de EV04.

Los archivos documentados son:

- GA7-220501096-AA5-EV04.postman_collection.json
- ENDPOINTS_AA5_EV04.txt
- ENLACE_REPOSITORIO_AA5_EV04.txt
- README.md

## 13. Versionamiento

- Sistema de control: Git.
- Repositorio remoto: GitHub.
- Rama de la evidencia: feature/GA7-220501096-AA5-EV04.

Enlace de la rama:

[Rama feature/GA7-220501096-AA5-EV04](https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA7-220501096-AA5-EV04)

## 14. Consideraciones de seguridad

Durante la ejecución de la colección se aplicaron las siguientes medidas:

- No publicar contraseñas reales.
- No mostrar tokens JWT completos.
- No mostrar hashes.
- Proteger las variables temporales y credenciales.
- Usar JWT para las solicitudes protegidas.
- Eliminar los administradores y datos temporales.
- No modificar registros reales.
- No incluir secretos en el repositorio.
- No mostrar credenciales en pantallazos o video.

## 15. Conclusiones

Las API del proyecto fueron probadas mediante Postman y la colección ejecutó 40 solicitudes. Las 80 pruebas finalizaron correctamente, sin fallos ni errores. Se comprobaron tanto respuestas válidas como inválidas, se validó la seguridad JWT y se probaron las operaciones CRUD del proyecto. También se validó el ajuste manual de stock y se documentaron los 35 endpoints. Los datos temporales fueron eliminados al terminar la ejecución y los cuatro criterios de evaluación quedaron cubiertos.
