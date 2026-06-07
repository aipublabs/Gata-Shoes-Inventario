# Gata Shoes — Sistema de Inventario

Sistema web de gestión de inventario para Gata Shoes con arquitectura REST API + Frontend React.

## Stack Tecnológico

### Backend
- **Java 17** + **Spring Boot 3.3.5**
- **Spring Data JPA** + **Hibernate 6.5.3**
- **MySQL 8.0**
- Maven para gestión de dependencias

### Frontend
- **React 18** + **TypeScript**
- **Vite** (build tool)
- **Tailwind CSS** para estilos
- **Axios** para llamadas API
- **React Router** para navegación

## Instalación y Ejecución

### Requisitos previos
- Java 17+
- Node.js 18+ / npm 10+
- MySQL 8.0 (Docker recomendado)
- Git

### Backend

```bash
cd inventario
.\mvnw.cmd spring-boot:run
```

Backend estará disponible en: **http://localhost:8081**

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend estará disponible en: **http://localhost:5173**

## URLs y Acceso

| Componente | URL |
|-----------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8081/api/v1 |

### Credenciales de Prueba
- **Correo**: admin@gatashoes.com
- **Contraseña**: 123456

## Endpoints REST Principales

### Autenticación
- `POST /api/v1/auth/login` — Login

### Categorías
- `GET /api/v1/categorias` — Listar
- `POST /api/v1/categorias` — Crear
- `PUT /api/v1/categorias/{id}` — Actualizar
- `DELETE /api/v1/categorias/{id}` — Eliminar

### Colores
- `GET /api/v1/colores` — Listar
- `POST /api/v1/colores` — Crear
- `PUT /api/v1/colores/{id}` — Actualizar
- `DELETE /api/v1/colores/{id}` — Eliminar

### Tallas
- `GET /api/v1/tallas` — Listar
- `POST /api/v1/tallas` — Crear
- `PUT /api/v1/tallas/{id}` — Actualizar
- `DELETE /api/v1/tallas/{id}` — Eliminar

### Productos
- `GET /api/v1/productos` — Listar
- `POST /api/v1/productos` — Crear
- `PUT /api/v1/productos/{id}` — Actualizar
- `DELETE /api/v1/productos/{id}` — Eliminar

### Inventario
- `GET /api/v1/inventario` — Listar variantes
- `POST /api/v1/inventario` — Crear variante
- `PUT /api/v1/inventario/{id}` — Actualizar
- `DELETE /api/v1/inventario/{id}` — Eliminar

### Resumen y Alertas
- `GET /api/v1/resumen` — Dashboard (totales, novedades, top stock)
- `GET /api/v1/alertas` — Stock bajo (≤ 3 unidades)

## Estructura del Proyecto

```
Gata-Shoes-Inventario/
├── inventario/                          # Backend (Spring Boot)
│   ├── src/main/java/com/gatashoes/inventario/
│   │   ├── api/
│   │   │   ├── controller/             # REST Controllers
│   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── security/               # JWT & Security
│   │   │   └── exception/              # Global error handling
│   │   ├── config/                     # Configuration (Security, CORS)
│   │   ├── model/                      # JPA Entities
│   │   ├── repository/                 # Data Access Layer
│   │   └── service/                    # Business Logic
│   ├── pom.xml
│   └── mvnw / mvnw.cmd                 # Maven wrapper
│
├── frontend/                            # Frontend (React)
│   ├── src/
│   │   ├── pages/                      # Page components
│   │   ├── components/                 # Reusable components
│   │   ├── api/                        # Axios client & endpoints
│   │   ├── contexts/                   # Auth context
│   │   ├── hooks/                      # Custom hooks
│   │   ├── types/                      # TypeScript types
│   │   └── App.tsx                     # Routing & layout
│   ├── package.json
│   ├── vite.config.ts
│   └── tailwind.config.js
│
└── README.md                            # Este archivo
```

## Características Principales

### Módulos Implementados
- ✅ Autenticación JWT
- ✅ Dashboard (Resumen con métricas, gráficos, últimas novedades)
- ✅ Gestión de Categorías
- ✅ Gestión de Productos
- ✅ Alertas de Stock Bajo
- ✅ Interfaz responsive con Tailwind CSS

## Seguridad

- **CORS**: Configurado para `http://localhost:5173`
- **JWT**: Token Bearer en header `Authorization`
- **Session**: STATELESS (sin sesiones de servidor)
- **Endpoints protegidos**: `/api/v1/**` requiere autenticación
- **Endpoints públicos**: `/api/v1/auth/**`

## Desarrollo

### Scripts útiles

#### Backend
```bash
# Compilar
./mvnw clean compile

# Tests
./mvnw test

# Empaquetar JAR
./mvnw clean package

# Ejecutar JAR
java -jar target/inventario-0.0.1-SNAPSHOT.jar
```

#### Frontend
```bash
# Dev server
npm run dev

# Build para producción
npm run build

# Preview build
npm run preview

# Linting (TypeScript)
npm run build  # Incluye verificación tsc
```

## Contribuciones

Para contribuir:
1. Fork el repositorio
2. Crea una rama (`git checkout -b feature/nueva-funcion`)
3. Commit cambios (`git commit -m 'Agrega nueva función'`)
4. Push a la rama (`git push origin feature/nueva-funcion`)
5. Abre un Pull Request

## Licencia

Propietario — Gata Shoes

## Contacto

Para soporte o preguntas sobre el proyecto, contacta al equipo de desarrollo.

---

**Última actualización**: Junio 2026
