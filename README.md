# Evidencia GA7-220501096-AA5-EV02

## API: pruebas de registro e inicio de sesión con Postman

## 1. Información general

- **Evidencia:** GA7-220501096-AA5-EV02
- **Tipo de evidencia:** Producto
- **Nombre:** API
- **Proyecto:** Gata Shoes, Sistema de Inventario
- **Programa:** Análisis y Desarrollo de Software
- **Backend:** Spring Boot 3.3.5 y Java 17
- **Base de datos:** MySQL 8
- **Herramienta de pruebas:** Postman
- **Seguridad:** Spring Security, JWT y BCrypt
- **Rama:** `feature/GA7-220501096-AA5-EV02`

## 2. Objetivo

Realizar y documentar las pruebas funcionales de los servicios web de registro e inicio de sesión desarrollados en la evidencia GA7-220501096-AA5-EV01, utilizando Postman para comprobar las respuestas exitosas, las validaciones de entrada y el manejo de errores de autenticación.

## 3. Alcance

Esta evidencia evalúa exclusivamente los siguientes servicios web:

- `POST /api/v1/auth/registro`
- `POST /api/v1/auth/login`

Las pruebas de otras API del proyecto, como categorías, inventario, productos, colores, tallas y alertas, no forman parte del alcance de esta evidencia.

## 4. Criterios de evaluación

| N.º | Criterio de evaluación | Evidencia de cumplimiento | Estado |
|---:|---|---|---|
| 1 | Realiza el test de la API utilizando Postman | Colección con siete solicitudes ejecutadas y pruebas automáticas aprobadas | Cumplido |
| 2 | Realiza el video solicitado mostrando las pruebas de la API | Video con la ejecución y explicación de las pruebas | Cumplido |
| 3 | Realiza la documentación de la API | Documento de pruebas con descripción, resultados y pantallazos | Cumplido |
| 4 | Entrega los endpoints de las API desarrolladas | Archivo independiente con la documentación de registro y login | Cumplido |

## 5. Servicios web evaluados

### 5.1 Registro de administrador

- **Método HTTP:** `POST`
- **Endpoint:** `/api/v1/auth/registro`
- **URL local:** `http://localhost:8081/api/v1/auth/registro`
- **Autenticación requerida:** No
- **Content-Type:** `application/json`

El servicio permite registrar un administrador mediante nombre, correo y contraseña.

El proceso incluye:

- Validación de campos obligatorios.
- Validación del formato del correo.
- Validación de la longitud de la contraseña.
- Verificación de correos duplicados.
- Normalización del nombre y del correo.
- Cifrado de la contraseña mediante BCrypt.
- Persistencia del administrador en MySQL.
- Respuesta sin contraseña ni hash.

#### Solicitud de ejemplo

```json
{
  "nombre": "Usuario Prueba EV02",
  "correo": "usuario.prueba@example.com",
  "contrasena": "ClaveSegura123"
}
```

#### Respuesta exitosa

Código HTTP:

```text
201 Created
```

```json
{
  "idAdmin": 3,
  "nombre": "Usuario Prueba EV02",
  "correo": "usuario.prueba@example.com"
}
```

#### Posibles respuestas

| Código HTTP | Descripción |
|---:|---|
| `201 Created` | Administrador registrado correctamente |
| `400 Bad Request` | Los datos enviados no cumplen las validaciones |
| `409 Conflict` | El correo ya se encuentra registrado |
| `500 Internal Server Error` | Se produjo un error no controlado en el servidor |

### 5.2 Inicio de sesión

- **Método HTTP:** `POST`
- **Endpoint:** `/api/v1/auth/login`
- **URL local:** `http://localhost:8081/api/v1/auth/login`
- **Autenticación requerida:** No
- **Content-Type:** `application/json`

El servicio valida el correo y la contraseña del administrador. Cuando la autenticación es correcta, genera un access token JWT y devuelve los datos públicos del usuario.

#### Solicitud de ejemplo

```json
{
  "correo": "usuario.prueba@example.com",
  "contrasena": "ClaveSegura123"
}
```

#### Respuesta exitosa

Código HTTP:

```text
200 OK
```

```json
{
  "accessToken": "[TOKEN JWT OCULTO POR SEGURIDAD]",
  "idAdmin": 3,
  "nombre": "Usuario Prueba EV02",
  "correo": "usuario.prueba@example.com"
}
```

#### Posibles respuestas

| Código HTTP | Descripción |
|---:|---|
| `200 OK` | Inicio de sesión correcto |
| `400 Bad Request` | Los campos obligatorios están vacíos |
| `401 Unauthorized` | El correo no existe o la contraseña es incorrecta |
| `500 Internal Server Error` | Se produjo un error no controlado en el servidor |

El mensaje utilizado para las credenciales incorrectas es:

```text
Credenciales inválidas
```

El servicio no revela si el error corresponde al correo o a la contraseña.

## 6. Colección de Postman

Para esta evidencia se creó la colección:

```text
GA7-220501096-AA5-EV02 - Registro y Login
```

La colección contiene siete solicitudes con scripts automáticos de validación:

1. `EV02-01 - Registro exitoso`
2. `EV02-02 - Inicio de sesión exitoso`
3. `EV02-03 - Correo duplicado`
4. `EV02-04 - Correo inválido`
5. `EV02-05 - Contraseña corta`
6. `EV02-06 - Campos vacíos en registro`
7. `EV02-07 - Credenciales incorrectas`

### Variables de la colección

| Variable | Descripción |
|---|---|
| `baseUrl` | URL base de la API |
| `correoPruebaEV02` | Correo dinámico generado para cada ejecución |
| `contrasenaPruebaEV02` | Contraseña temporal utilizada en las pruebas |
| `idAdminPruebaEV02` | Identificador retornado al registrar el administrador |

La solicitud `EV02-01 - Registro exitoso` genera dinámicamente un correo mediante `Date.now()`.

Las solicitudes posteriores reutilizan el correo, la contraseña y el identificador almacenados en las variables de la colección.

## 7. Pruebas ejecutadas

| ID | Caso de prueba | Endpoint | Resultado | Estado |
|---|---|---|---:|---|
| EV02-01 | Registro exitoso | `POST /api/v1/auth/registro` | HTTP 201 | Aprobada |
| EV02-02 | Inicio de sesión exitoso | `POST /api/v1/auth/login` | HTTP 200 | Aprobada |
| EV02-03 | Registro con correo duplicado | `POST /api/v1/auth/registro` | HTTP 409 | Aprobada |
| EV02-04 | Registro con correo inválido | `POST /api/v1/auth/registro` | HTTP 400 | Aprobada |
| EV02-05 | Registro con contraseña menor de ocho caracteres | `POST /api/v1/auth/registro` | HTTP 400 | Aprobada |
| EV02-06 | Registro con campos obligatorios vacíos | `POST /api/v1/auth/registro` | HTTP 400 | Aprobada |
| EV02-07 | Inicio de sesión con credenciales incorrectas | `POST /api/v1/auth/login` | HTTP 401 | Aprobada |

Todas las verificaciones automáticas configuradas en la colección de Postman finalizaron con estado `Passed`.

## 8. Resultados principales

### 8.1 Registro exitoso

Se verificó que:

- El servicio retorna HTTP `201 Created`.
- La respuesta tiene formato JSON.
- La respuesta contiene `idAdmin`, `nombre` y `correo`.
- El nombre retornado corresponde al nombre enviado.
- El correo retornado corresponde al correo dinámico generado.
- La respuesta no contiene `contrasena`, `password` ni `hash`.
- El identificador generado queda disponible para las siguientes pruebas.

### 8.2 Inicio de sesión exitoso

Se verificó que:

- El servicio retorna HTTP `200 OK`.
- La respuesta tiene formato JSON.
- La respuesta contiene un access token.
- Los datos corresponden al administrador previamente registrado.
- La respuesta contiene `idAdmin`, `nombre` y `correo`.
- La respuesta no expone la contraseña.
- El access token se mantiene oculto en la documentación y en el video.

### 8.3 Correo duplicado

Se verificó que:

- El servicio retorna HTTP `409 Conflict`.
- La respuesta tiene formato JSON.
- El error es identificado como `Conflicto`.
- El mensaje indica que el correo ya se encuentra registrado.
- La respuesta contiene la ruta `/api/v1/auth/registro`.

Ejemplo de respuesta:

```json
{
  "status": 409,
  "error": "Conflicto",
  "message": "El correo ya se encuentra registrado",
  "path": "/api/v1/auth/registro"
}
```

### 8.4 Correo inválido

Se verificó que:

- El servicio retorna HTTP `400 Bad Request`.
- La respuesta tiene formato JSON.
- El error es identificado como `Solicitud Inválida`.
- La respuesta indica que el correo debe tener un formato válido.

Ejemplo de respuesta:

```json
{
  "status": 400,
  "error": "Solicitud Inválida",
  "message": "correo: El correo debe tener un formato válido",
  "path": "/api/v1/auth/registro"
}
```

### 8.5 Contraseña corta

Se verificó que:

- El servicio retorna HTTP `400 Bad Request`.
- La respuesta tiene formato JSON.
- La respuesta indica que la contraseña debe tener entre 8 y 255 caracteres.

El valor utilizado para realizar la prueba fue:

```text
Abc123
```

Ejemplo de respuesta:

```json
{
  "status": 400,
  "error": "Solicitud Inválida",
  "message": "contrasena: La contraseña debe tener entre 8 y 255 caracteres",
  "path": "/api/v1/auth/registro"
}
```

### 8.6 Campos vacíos en registro

Se verificó que:

- El servicio retorna HTTP `400 Bad Request`.
- La respuesta tiene formato JSON.
- La respuesta contiene mensajes para nombre, correo y contraseña obligatorios.
- No se crea un administrador cuando los campos obligatorios están vacíos.

Una contraseña vacía puede producir simultáneamente los mensajes correspondientes a `@NotBlank` y `@Size`, debido a que el valor incumple ambas validaciones.

Ejemplo de respuesta:

```json
{
  "status": 400,
  "error": "Solicitud Inválida",
  "message": "correo: El correo es obligatorio; contrasena: La contraseña es obligatoria; nombre: El nombre es obligatorio; contrasena: La contraseña debe tener entre 8 y 255 caracteres",
  "path": "/api/v1/auth/registro"
}
```

### 8.7 Credenciales incorrectas

Se verificó que:

- El servicio retorna HTTP `401 Unauthorized`.
- La respuesta tiene formato JSON.
- El error es identificado como `No Autorizado`.
- El mensaje es `Credenciales inválidas`.
- La respuesta no revela si el dato incorrecto corresponde al correo o a la contraseña.

Ejemplo de respuesta:

```json
{
  "status": 401,
  "error": "No Autorizado",
  "message": "Credenciales inválidas",
  "path": "/api/v1/auth/login"
}
```

## 9. Limpieza de datos

Después de ejecutar las pruebas se eliminó de MySQL el administrador temporal creado por la colección.

La eliminación se realizó utilizando el identificador y el correo del usuario temporal, con el propósito de evitar eliminar un registro diferente.

Posteriormente se verificó mediante una consulta que no quedaron registros asociados al usuario temporal utilizado en las pruebas.

## 10. Evidencias generadas

La entrega de esta actividad incluye:

- Código fuente del proyecto.
- Documento de pruebas con los pantallazos de Postman.
- Video con la ejecución y explicación de las pruebas.
- Colección Postman con siete solicitudes y scripts automáticos.
- Archivo independiente con la documentación de los endpoints.
- Archivo con el enlace al repositorio.
- README específico de la rama de la evidencia.

Los archivos principales de soporte son:

- `GA7-220501096-AA5-EV02_DOCUMENTO_PRUEBAS.docx` o su versión final en PDF.
- `GA7-220501096-AA5-EV02_VIDEO.mp4`.
- `GA7-220501096-AA5-EV02.postman_collection.json`.
- `ENDPOINTS_AA5_EV02.txt`.
- `ENLACE_REPOSITORIO_AA5_EV02.txt`.
- `README.md`.

## 11. Versionamiento

- **Sistema de control de versiones:** Git
- **Repositorio remoto:** GitHub
- **Rama de la evidencia:** `feature/GA7-220501096-AA5-EV02`

Enlace a la rama:

https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA7-220501096-AA5-EV02

La rama fue creada a partir de la versión terminada de la evidencia GA7-220501096-AA5-EV01, conservando los servicios web de registro e inicio de sesión que se probaron en esta actividad.

## 12. Consideraciones de seguridad

Durante la creación de las evidencias se aplicaron las siguientes medidas:

- No se incluyeron contraseñas reales.
- No se publicaron hashes completos.
- No se mostraron tokens JWT completos.
- El access token fue ocultado en el documento y en el video.
- Los usuarios temporales fueron eliminados después de las pruebas.
- Las respuestas del servicio no exponen contraseñas.
- El mensaje de credenciales inválidas no indica cuál dato falló.
- Las variables temporales de la colección se utilizaron únicamente para las pruebas.
- El archivo con los endpoints no contiene secretos.
- El archivo con el enlace al repositorio contiene únicamente información pública de la rama.

15. Verifica que no se haya modificado ningún archivo diferente de README.md.
16. No ejecutes comandos.
17. No hagas commit.
18. No hagas push.
19. Detente después de actualizar README.md.
