# Evidencia GA7-220501096-AA5-EV01

## 1. Información general

- **Evidencia:** GA7-220501096-AA5-EV01
- **Nombre:** Diseño y desarrollo de servicios web, caso
- **Proyecto:** Gata Shoes, Sistema de Inventario
- **Backend:** Spring Boot 3.3.5 y Java 17
- **Base de datos:** MySQL 8
- **Seguridad:** Spring Security, JWT y BCrypt
- **Rama:** feature/GA7-220501096-AA5-EV01
- **Commit principal de implementación:** e64372a

## 2. Objetivo

El objetivo es implementar y verificar servicios web reutilizables para:

- Registrar administradores con validación de datos y cifrado de contraseñas
- Iniciar sesión de administradores validando credenciales
- Validar los datos de entrada mediante Bean Validation
- Asegurar las contraseñas utilizando BCrypt
- Controlar errores mediante excepciones específicas y códigos HTTP estándar
- Utilizar Git y GitHub para versionar el código durante todo el proceso de desarrollo

## 3. Criterios de evaluación

| Criterio | Estado | Descripción |
|----------|--------|-------------|
| Servicio web para registro | Cumplido | Se implementó endpoint POST /api/v1/auth/registro que permite registrar administradores con validación de datos y cifrado de contraseña. La respuesta HTTP es 201 Created y no expone la contraseña. |
| Servicio web para inicio de sesión | Cumplido | Se implementó endpoint POST /api/v1/auth/login que autentica administradores validando correo y contraseña. La respuesta HTTP es 200 OK y retorna access token, idAdmin, nombre y correo. |
| Validaciones de verificación | Cumplido | Se implementaron validaciones mediante Bean Validation (@NotBlank, @Email, @Size) y verificación de duplicados mediante existsByCorreoIgnoreCase(). Códigos HTTP: 400 para datos inválidos, 409 para correo duplicado, 401 para credenciales inválidas. |
| Herramientas de versionamiento | Cumplido | Se utilizó Git como sistema de control de versiones y GitHub como repositorio remoto. Todos los cambios fueron desarrollados en la rama feature/GA7-220501096-AA5-EV01 y consolidados en el commit e64372a. |

## 4. Servicio web de registro

### Especificación

- **Método:** POST
- **Endpoint:** /api/v1/auth/registro
- **Acceso:** Público mediante /api/v1/auth/**
- **Content-Type:** application/json

### Solicitud

```json
{
  "nombre": "Usuario Prueba",
  "correo": "usuario.prueba@example.com",
  "contrasena": "ClaveSegura123"
}
```

### Respuesta exitosa (HTTP 201)

```json
{
  "idAdmin": 2,
  "nombre": "Usuario Prueba",
  "correo": "usuario.prueba@example.com"
}
```

### Comportamiento

- **HTTP 201 Created:** Registro exitoso
- **HTTP 400 Bad Request:** Datos de entrada inválidos (nombre vacío, correo con formato inválido, contraseña menor a 8 caracteres)
- **HTTP 409 Conflict:** Correo duplicado
- **Seguridad:** La respuesta no contiene contraseña, password ni hash
- **Cifrado:** La contraseña se cifra utilizando PasswordEncoder con algoritmo BCrypt existente
- **Normalización:** 
  - El nombre se normaliza eliminando espacios externos con `.trim()`
  - El correo se normaliza eliminando espacios externos y convirtiéndolo a minúsculas con `.toLowerCase(Locale.ROOT)`
- **Validación de duplicados:** Se utiliza `existsByCorreoIgnoreCase()` para verificar que el correo no esté registrado
- **Delegación:** El endpoint delega la lógica completa de registro en `AdministradorService`

### Consideración técnica identificada

Bean Validation se ejecuta antes de la normalización del servicio. Por esta razón, el correo de entrada debe tener un formato válido y no debe incluir espacios externos. Esta es una consideración técnica identificada durante la validación. El correo debe enviarse sin espacios al inicio o al final, debido a que Bean Validation se ejecuta antes de la normalización realizada por el servicio.

## 5. Servicio web de inicio de sesión

### Especificación

- **Método:** POST
- **Endpoint:** /api/v1/auth/login
- **Acceso:** Público
- **Content-Type:** application/json

### Solicitud

```json
{
  "correo": "usuario.prueba@example.com",
  "contrasena": "ClaveSegura123"
}
```

### Respuesta exitosa (HTTP 200)

- **HTTP 200 OK:** Autenticación exitosa
- **Contenido:** access token (no se muestra en esta documentación), idAdmin, nombre y correo
- **Seguridad:** La respuesta no contiene la contraseña

### Comportamiento de credenciales inválidas

- **HTTP 401 Unauthorized:** Correo inexistente o contraseña incorrecta
- **Mensaje:** "Credenciales inválidas"
- **Seguridad:** El servicio no revela si falló el correo o la contraseña, brindando igual seguridad para ambos casos

### Proceso de verificación

- La contraseña se verifica utilizando `PasswordEncoder.matches()` con BCrypt
- Se mantiene compatibilidad con contraseñas sin cifrar de migraciones anteriores, cifrándolas automáticamente al verificar éxito

## 6. Validaciones implementadas

| Escenario | Resultado verificado | Observaciones |
|-----------|----------------------|--------------|
| **Registro exitoso** | HTTP 201 | Devuelve idAdmin, nombre y correo sin exponer contraseña |
| **Inicio de sesión exitoso** | HTTP 200 | Devuelve los datos del administrador y confirma la existencia del access token (no mostrado) |
| **Correo duplicado** | HTTP 409 Conflict | Mensaje: "El correo ya se encuentra registrado" |
| **Correo con formato inválido** | HTTP 400 Bad Request | Valor utilizado: "correo-invalido" → Mensaje: "El correo debe tener un formato válido" |
| **Contraseña menor a 8 caracteres** | HTTP 400 Bad Request | Valor utilizado: "Abc123" → Mensaje: "La contraseña debe tener entre 8 y 255 caracteres" |
| **Campos vacíos** | HTTP 400 Bad Request | Se recibieron mensajes para nombre, correo y contraseña obligatorios. Una contraseña vacía puede producir simultáneamente los mensajes de @NotBlank y @Size, porque incumple ambas reglas. |
| **Credenciales incorrectas** | HTTP 401 Unauthorized | Error: "No Autorizado" → Mensaje: "Credenciales inválidas" |
| **Cifrado de contraseña** | Verificado en MySQL | Prefijo observado: $2a$ → Longitud observada: 60 caracteres → Esto confirma el almacenamiento mediante BCrypt |
| **Limpieza de datos de prueba** | Confirmado | El usuario temporal utilizado en la validación fue eliminado de MySQL. Se confirmó que quedaron cero registros para ese usuario. |

## 7. Archivos relacionados con la evidencia

El commit e64372a modificó o creó exactamente estos ocho archivos:

1. **inventario/src/main/java/com/gatashoes/inventario/api/controller/AuthRestController.java**
   - Controlador REST que expone los endpoints `/login`, `/refresh`, `/logout` y `/registro`
   - Recibe solicitudes, delega en servicios y retorna respuestas con códigos HTTP apropiados

2. **inventario/src/main/java/com/gatashoes/inventario/api/dto/request/RegistroRequest.java**
   - DTO para recibir datos de registro
   - Incluye validaciones mediante Bean Validation: @NotBlank, @Email, @Size
   - Todos los mensajes de validación están en español

3. **inventario/src/main/java/com/gatashoes/inventario/api/exception/CorreoDuplicadoException.java**
   - Excepción específica para correos duplicados
   - Permite retornar HTTP 409 Conflict de forma diferenciada

4. **inventario/src/main/java/com/gatashoes/inventario/api/exception/CredencialesInvalidasException.java**
   - Excepción específica para credenciales inválidas
   - Permite retornar HTTP 401 Unauthorized de forma diferenciada

5. **inventario/src/main/java/com/gatashoes/inventario/api/exception/GlobalExceptionHandler.java**
   - Manejador centralizado de excepciones mediante @RestControllerAdvice
   - Convierte excepciones en respuestas ErrorResponse con códigos HTTP estándar
   - Maneja: ResourceNotFoundException, MethodArgumentNotValidException, CorreoDuplicadoException, CredencialesInvalidasException, Exception

6. **inventario/src/main/java/com/gatashoes/inventario/api/security/AuthService.java**
   - Servicio que realiza la autenticación
   - Valida correo y contraseña
   - Lanza CredencialesInvalidasException sin revelar cuál dato falló
   - Mantiene compatibilidad con contraseñas sin cifrar, cifrándolas automáticamente

7. **inventario/src/main/java/com/gatashoes/inventario/repository/AdministradorRepository.java**
   - Repositorio JPA para Administrador
   - Agrega método `existsByCorreoIgnoreCase()` para verificación de duplicados case-insensitive

8. **inventario/src/main/java/com/gatashoes/inventario/service/AdministradorService.java**
   - Servicio que contiene la lógica de negocio para administradores
   - Agrega método `registrarAdministrador()` que normaliza datos, verifica duplicados, cifra contraseña y persiste

## 8. Flujo del servicio de registro

```
Cliente HTTP
    ↓
AuthRestController (recibe solicitud, valida anotación @Valid)
    ↓
RegistroRequest y Bean Validation (valida formato, obligatoriedad, tamaños)
    ↓
AdministradorService.registrarAdministrador() (normaliza, verifica, cifra, persiste)
    ↓
AdministradorRepository (accede a base de datos MySQL)
    ↓
MySQL (almacena registro)
    ↓
AdministradorMapper (convierte entidad a DTO sin contraseña)
    ↓
AdministradorResponse (retorna al cliente)
    ↓
Cliente HTTP (recibe HTTP 201 con datos públicos)
```

### Responsabilidades por capa

- **AuthRestController:** Recibe solicitudes HTTP, valida con @Valid, delega en servicios, retorna ResponseEntity con código HTTP y cuerpo
- **Bean Validation:** Valida formato, obligatoriedad, rangos de valores antes de ser procesados por el servicio
- **AdministradorService:** Normaliza datos (trim, toLowerCase), verifica duplicados, cifra contraseña, captura excepciones específicas
- **AdministradorRepository:** Accede a base de datos, ejecuta consultas, persiste cambios
- **GlobalExceptionHandler:** Convierte excepciones de negocio en respuestas HTTP con ErrorResponse estandarizado
- **AdministradorMapper:** Convierte entidades JPA a DTOs de respuesta, excluye datos sensibles

## 9. Códigos HTTP

- **200 OK:** Inicio de sesión correcto. Contiene access token, idAdmin, nombre y correo
- **201 Created:** Registro correcto. Contiene idAdmin, nombre y correo (sin contraseña)
- **400 Bad Request:** Datos de entrada inválidos. Causa: formato de correo inválido, contraseña menor a 8 caracteres, campos vacíos. Mensaje en español de la validación que falló
- **401 Unauthorized:** Credenciales inválidas. Causa: correo inexistente o contraseña incorrecta. Mensaje: "Credenciales inválidas"
- **409 Conflict:** Correo duplicado. Causa: el correo ya está registrado. Mensaje: "El correo ya se encuentra registrado"
- **500 Internal Server Error:** Error no controlado en el servidor

## 10. Comentarios y estándares de codificación

Se conservaron los estándares existentes del proyecto Gata Shoes:

- **Convención de nombres:** Clases en PascalCase, métodos y variables en camelCase, paquetes en minúsculas
- **Arquitectura:** Organización por capas (controller → service → repository)
- **DTOs:** Uso de records de Java para entrada (RegistroRequest, LoginRequest) y salida (AdministradorResponse, LoginResponse)
- **Manejo de excepciones:** Centralizado mediante @RestControllerAdvice con GlobalExceptionHandler
- **Persistencia:** Repositorios JPA extendiendo JpaRepository
- **Inyección de dependencias:** PasswordEncoder existente inyectado mediante @Autowired
- **Documentación:** JavaDoc y comentarios en español
- **Seguridad:** Respuestas que nunca exponen contraseña, hash ni información sensible

## 11. Versionamiento

- **Sistema de control:** Git
- **Repositorio remoto:** GitHub
- **Rama de desarrollo:** feature/GA7-220501096-AA5-EV01
- **Commit de implementación:** e64372a
- **Mensaje del commit:** "feat: implementar servicio web de registro y validaciones de autenticación"
- **Enlace a la rama:** [Rama feature/GA7-220501096-AA5-EV01](https://github.com/aipublabs/Gata-Shoes-Inventario/tree/feature/GA7-220501096-AA5-EV01)

## 12. Conclusión

Los cuatro criterios de evaluación quedaron implementados y verificados:

1. **Registro:** Servicio web POST /api/v1/auth/registro que registra administradores con validación de datos, normalización de campos y cifrado de contraseña. Retorna HTTP 201 con datos públicos (sin contraseña).

2. **Inicio de sesión:** Servicio web POST /api/v1/auth/login que autentica administradores validando correo y contraseña. Retorna HTTP 200 con access token y datos públicos.

3. **Validaciones:** Implementadas mediante Bean Validation (@NotBlank, @Email, @Size) en RegistroRequest, verificación de duplicados mediante existsByCorreoIgnoreCase(), y excepciones específicas (CorreoDuplicadoException, CredencialesInvalidasException) que retornan códigos HTTP estándar (400, 401, 409).

4. **Versionamiento:** Todos los cambios fueron desarrollados en la rama feature/GA7-220501096-AA5-EV01 y consolidados en el commit e64372a, permitiendo rastreabilidad y reversibilidad.

La solución se construyó sobre la arquitectura existente de Gata Shoes sin eliminar ni afectar los demás módulos del proyecto, manteniendo los estándares de codificación, seguridad y manejo de excepciones ya establecidos.
