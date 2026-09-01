# Evidencia GA7-220501096-AA5-EV03

## Diseño y desarrollo de servicios web del proyecto Gata Shoes

## 1. Objetivo de la evidencia

La evidencia documenta las API REST necesarias para el proyecto completo de Gata Shoes, con un enfoque en la implementación de servicios web para la gestión del inventario, catálogo y administración del sistema. En esta rama se incorporó específicamente el endpoint de ajuste manual de stock:

- PATCH /api/v1/inventario/{id}/stock

Este ajuste forma parte del conjunto de servicios REST del proyecto, pero no representa el único alcance de la evidencia, sino una ampliación funcional dentro de la estructura general del backend.

## 2. Inventario actualizado de endpoints reales del backend

### 2.1 Autenticación y registro

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Autenticación | POST | /api/v1/auth/login | Iniciar sesión de administrador y emitir tokens JWT | LoginRequest | LoginResponse | 200 OK | 400 Bad Request, 401 Unauthorized | Público | correo y contraseña obligatorios; credenciales válidas |
| Autenticación | POST | /api/v1/auth/refresh | Renovar access token usando el refresh token almacenado en cookie | Sin DTO | Object con accessToken | 200 OK | 401 Unauthorized | Público | refresh token válido y no expirado |
| Autenticación | POST | /api/v1/auth/logout | Cerrar sesión y limpiar la cookie del refresh token | Sin DTO | Sin cuerpo | 200 OK | — | Público | cierre de sesión exitoso |
| Registro | POST | /api/v1/auth/registro | Registrar un nuevo administrador | RegistroRequest | AdministradorResponse | 201 Created | 400 Bad Request, 409 Conflict | Público | nombre obligatorio; correo válido; contraseña con longitud válida; correo duplicado rechazado |

#### DTO de entrada
- LoginRequest
  - correo: String
  - contrasena: String

- RegistroRequest
  - nombre: String
  - correo: String
  - contrasena: String

#### DTO de salida
- LoginResponse
  - accessToken
  - idAdmin
  - nombre
  - correo

- AdministradorResponse
  - idAdmin
  - nombre
  - correo

#### Códigos HTTP del servicio de registro
- HTTP 201 para registro correcto.
- HTTP 400 para datos inválidos.
- HTTP 409 para correo duplicado.

### 2.2 Administradores

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Administradores | GET | /api/v1/administradores | Listar administradores | Sin DTO | List<AdministradorResponse> | 200 OK | — | Protegido por JWT | consulta del conjunto administrativo |
| Administradores | GET | /api/v1/administradores/{id} | Obtener administrador por ID | Sin DTO | AdministradorResponse | 200 OK | 404 Not Found | Protegido por JWT | ID existente |
| Administradores | DELETE | /api/v1/administradores/{id} | Eliminar administrador | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | administrador existente |

### 2.3 Categorías

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Categorías | GET | /api/v1/categorias | Listar categorías | Sin DTO | List<CategoriaResponse> | 200 OK | — | Protegido por JWT | consulta general |
| Categorías | GET | /api/v1/categorias/{id} | Obtener categoría por ID | Sin DTO | CategoriaResponse | 200 OK | 404 Not Found | Protegido por JWT | id válido |
| Categorías | POST | /api/v1/categorias | Crear categoría | CategoriaRequest | CategoriaResponse | 201 Created | 400 Bad Request, 404 Not Found | Protegido por JWT | nombreCategoria obligatorio |
| Categorías | PUT | /api/v1/categorias/{id} | Actualizar categoría | CategoriaRequest | CategoriaResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | categoría existente y nombre válido |
| Categorías | DELETE | /api/v1/categorias/{id} | Eliminar categoría | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | categoría existente |

#### DTO de entrada
- CategoriaRequest
  - nombreCategoria: String

#### DTO de salida
- CategoriaResponse
  - idCategoria
  - nombreCategoria

### 2.4 Colores

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Colores | GET | /api/v1/colores | Listar colores | Sin DTO | List<ColorResponse> | 200 OK | — | Protegido por JWT | consulta general |
| Colores | GET | /api/v1/colores/{id} | Obtener color por ID | Sin DTO | ColorResponse | 200 OK | 404 Not Found | Protegido por JWT | id válido |
| Colores | POST | /api/v1/colores | Crear color | ColorRequest | ColorResponse | 201 Created | 400 Bad Request | Protegido por JWT | nombreColor obligatorio |
| Colores | PUT | /api/v1/colores/{id} | Actualizar color | ColorRequest | ColorResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | color existente y nombre válido |
| Colores | DELETE | /api/v1/colores/{id} | Eliminar color | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | color existente |

#### DTO de entrada
- ColorRequest
  - nombreColor: String

#### DTO de salida
- ColorResponse
  - idColor
  - nombreColor

### 2.5 Tallas

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Tallas | GET | /api/v1/tallas | Listar tallas | Sin DTO | List<TallaResponse> | 200 OK | — | Protegido por JWT | consulta general |
| Tallas | GET | /api/v1/tallas/{id} | Obtener talla por ID | Sin DTO | TallaResponse | 200 OK | 404 Not Found | Protegido por JWT | id válido |
| Tallas | POST | /api/v1/tallas | Crear talla | TallaRequest | TallaResponse | 201 Created | 400 Bad Request | Protegido por JWT | numero obligatorio |
| Tallas | PUT | /api/v1/tallas/{id} | Actualizar talla | TallaRequest | TallaResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | talla existente y valor válido |
| Tallas | DELETE | /api/v1/tallas/{id} | Eliminar talla | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | talla existente |

#### DTO de entrada
- TallaRequest
  - numero: String

#### DTO de salida
- TallaResponse
  - idTalla
  - numero

### 2.6 Productos

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Productos | GET | /api/v1/productos | Listar productos | Sin DTO | List<ProductoResponse> | 200 OK | — | Protegido por JWT | consulta general |
| Productos | GET | /api/v1/productos/{id} | Obtener producto por ID | Sin DTO | ProductoResponse | 200 OK | 404 Not Found | Protegido por JWT | id válido |
| Productos | POST | /api/v1/productos | Crear producto | ProductoRequest | ProductoResponse | 201 Created | 400 Bad Request, 404 Not Found | Protegido por JWT | nombre obligatorio; precio positivo; idCategoria existente |
| Productos | PUT | /api/v1/productos/{id} | Actualizar producto | ProductoRequest | ProductoResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | producto existente y categoría válida |
| Productos | DELETE | /api/v1/productos/{id} | Eliminar producto | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | producto existente |

#### DTO de entrada
- ProductoRequest
  - nombre: String
  - descripcion: String
  - precio: BigDecimal
  - urlImagen: String
  - idCategoria: Integer

#### DTO de salida
- ProductoResponse
  - idProducto
  - nombre
  - descripcion
  - precio
  - urlImagen
  - categoria: CategoriaResponse

### 2.7 Inventario

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Inventario | GET | /api/v1/inventario | Listar inventario | Sin DTO | List<InventarioResponse> | 200 OK | — | Protegido por JWT | consulta general, paginación opcional |
| Inventario | GET | /api/v1/inventario/{id} | Obtener detalle de una variante por ID | Sin DTO | InventarioResponse | 200 OK | 404 Not Found | Protegido por JWT | ID válido |
| Inventario | POST | /api/v1/inventario | Crear registro de inventario | InventarioRequest | InventarioResponse | 201 Created | 400 Bad Request, 404 Not Found | Protegido por JWT | producto, talla y color existentes; stock >= 0 |
| Inventario | PUT | /api/v1/inventario/{id} | Actualizar registro de inventario | InventarioRequest | InventarioResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | variante existente; relaciones válidas |
| Inventario | PATCH | /api/v1/inventario/{id}/stock | Ajustar stock manualmente según operación | AjusteStockRequest | InventarioResponse | 200 OK | 400 Bad Request, 404 Not Found | Protegido por JWT | validación de operación, cantidad y stock final |
| Inventario | DELETE | /api/v1/inventario/{id} | Eliminar una variante del inventario | Sin DTO | Sin cuerpo | 204 No Content | 404 Not Found | Protegido por JWT | variante existente |

#### DTO de entrada
- InventarioRequest
  - idProducto: Integer
  - idTalla: Integer
  - idColor: Integer
  - stock: Integer

- AjusteStockRequest
  - tipo: TipoAjusteStock
  - cantidad: Integer

#### DTO de salida
- InventarioResponse
  - idInventario
  - stock
  - producto
  - talla
  - color

### 2.8 Resumen

| Módulo | Método | Ruta completa | Propósito | DTO de entrada | DTO de salida | Código HTTP exitoso | Códigos HTTP de error | Público / protegido por JWT | Validaciones principales |
|---|---|---|---|---|---|---|---|---|---|
| Resumen | GET | /api/v1/resumen | Obtener resumen del inventario y métricas operativas | Sin DTO | InventarioResumenResponse | 200 OK | — | Protegido por JWT | cálculo de métricas del inventario |
| Alertas | GET | /api/v1/alertas | Obtener variantes con stock bajo o crítico | Sin DTO | List<InventarioResponse> | 200 OK | — | Protegido por JWT | stock <= 3 |

#### DTO de salida
- InventarioResumenResponse
  - totalVariantes
  - totalStock
  - alertasStockBajo
  - topCategoriasStock
  - novedades
  - topStock

## 3. API incorporada en EV03

### 3.1 Endpoint
PATCH /api/v1/inventario/{id}/stock

### 3.2 Propósito
Permite ajustar manualmente el stock de una variante del inventario mediante una operación de negocio controlada por backend. La operación centraliza la validación y evita que el cliente pueda introducir inconsistencias en existencias y stock.

### 3.3 Seguridad JWT
El endpoint está protegido por JWT y debe invocarse con un usuario autenticado con permisos adecuados para operar sobre inventario.

### 3.4 AjusteStockRequest
DTO de entrada para el ajuste de stock:

- tipo: TipoAjusteStock
- cantidad: Integer

Validaciones principales:
- tipo obligatorio
- cantidad obligatoria
- cantidad no puede ser negativa
- en AGREGAR y RESTAR la cantidad debe ser mayor que cero

### 3.5 TipoAjusteStock
Enum con las operaciones permitidas:
- AGREGAR
- RESTAR
- FIJAR

### 3.6 InventarioResponse
DTO de salida que devuelve el inventario actualizado cuando la operación queda en una cantidad válida.

### 3.7 Operaciones
- AGREGAR: incrementa stock
- RESTAR: decrementa stock
- FIJAR: reemplaza el stock con el valor indicado

### 3.8 Respuestas HTTP
- HTTP 200 OK si queda stock mayor que cero
- HTTP 204 No Content si el stock llega a cero y se elimina la variante
- HTTP 400 Bad Request para operaciones inválidas
- HTTP 404 Not Found si el inventario no existe

### 3.9 Reglas de negocio documentadas
- No se permite stock final negativo.
- No se permite cantidad cero para AGREGAR o RESTAR.
- Si el stock resultante llega a cero, la variante es eliminada.
- Si la operación es inválida, la API responde con 400.
- Si la variante no existe, la API responde con 404.

## 4. Validaciones funcionales realmente ejecutadas

Se realizaron las siguientes comprobaciones de negocio en la evidencia EV03:

1. AGREGAR cambió el stock del inventario 3 de 5 a 7.
2. RESTAR cambió el stock de 7 a 5.
3. FIJAR cambió el stock de 5 a 6.
4. FIJAR restauró el stock de 6 a 5.
5. RESTAR 6 cuando el stock era 5 devolvió HTTP 400.
6. Después del error, el inventario conservó el stock 5.
7. AGREGAR cantidad cero devolvió HTTP 400.
8. El tipo SUMAR devolvió HTTP 400.
9. El inventario 999999 devolvió HTTP 404.
10. Se creó un inventario temporal con ID 28 y stock 1.
11. RESTAR una unidad al inventario temporal devolvió HTTP 204.
12. La consulta posterior del inventario 28 devolvió HTTP 404.

Se aclara que:
- el inventario temporal fue eliminado;
- el inventario real con ID 3 quedó restaurado con stock 5.

## 5. Criterios de evaluación de EV03

1. Realiza los servicios según requerimientos del proyecto.
   La implementación cumple con los requerimientos funcionales del proyecto al ofrecer servicios REST para los módulos principales y para la gestión del inventario.

2. Realiza API REST según necesidades del proyecto.
   Se expusieron endpoints REST para autenticación, administración, catálogo, inventario, resumen y alertas, manteniendo la estructura de la aplicación y la lógica del negocio en backend.

3. Realiza las validaciones de verificación correctamente.
   Las operaciones de inventario se validan en backend para evitar stock negativo, cantidades inválidas, tipos no permitidos y referencias inexistentes.

4. Utiliza herramientas de versionamiento para la creación del proyecto.
   El desarrollo se realizó en el repositorio del proyecto Gata Shoes utilizando Git y GitHub, con la rama y el commit de implementación registrados en el historial del repositorio.

## 6. Relación general entre las API y las historias de usuario

Las API REST del proyecto están alineadas con las historias de usuario y con la gestión operativa del sistema. Los módulos principales permiten:

- registrar y autenticar administradores;
- gestionar categorías, colores y tallas;
- mantener el catálogo de productos;
- operar el inventario real de existencias;
- consultar informes de resumen y alertas;
- ajustar stocks con validación de negocio en el backend.

La solución no depende de la capa visual para validar reglas de existencias y stock, sino que centraliza esa lógica en el backend, reforzando la integridad del sistema.

## 7. Arquitectura del proyecto

La arquitectura del backend se mantiene basada en la separación por responsabilidades:

Controller REST
→ DTO request
→ Service
→ Repository
→ Entidad JPA
→ Mapper
→ DTO response

Flujo general:
- El controller REST recibe la petición.
- El DTO request valida los datos de entrada.
- El service contiene la lógica de negocio.
- El repository accede a los datos.
- La entidad JPA representa el modelo persistente.
- El mapper transforma la entidad al DTO de respuesta.
- El DTO response devuelve la estructura final al cliente.

## 8. Uso de Git y GitHub

El desarrollo se realizó en el repositorio del proyecto Gata Shoes utilizando Git y GitHub.

- Rama: feature/GA7-220501096-AA5-EV03
- Commit de implementación: b2cada5

Enlace de la rama:

[Rama feature/GA7-220501096-AA5-EV03](https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA7-220501096-AA5-EV03)

## 9. Consideraciones de seguridad

La solución contempla una capa de seguridad basada en JWT para proteger los servicios del sistema y asegurar que solo usuarios autenticados puedan operar sobre los recursos. Se debe mantener este criterio en todas las ejecuciones del backend:

- autenticación pública para login y registro;
- acceso controlado para operaciones del sistema y del inventario;
- validación de entrada en servidor;
- no exposición de contraseñas, hashes, tokens completos ni secretos en el repositorio o en la documentación.

## 10. Conclusión

La solución dispone de API REST para los módulos principales del proyecto Gata Shoes, incluyendo autenticación, administración, catálogo, inventario y resumen. El ajuste de stock quedó centralizado en el backend, con validaciones que impiden operaciones inválidas y con una arquitectura coherente que conserva la estructura existente del proyecto.

La implementación mantiene la separación de responsabilidades entre capas y asegura que la lógica del inventario se ejecute de forma consistente, segura y verificable. Además, se utilizó Git y GitHub para el versionamiento del desarrollo de la evidencia.
