# Wiki Backend

Proyecto de presentación del equipo — Wiki Backend. Aplicación web desarrollada con Spring Boot para gestionar y mostrar información de estudiantes y grupos de trabajo.

## Características

-  Motor de plantillas Thymeleaf para renderizado dinámico
-  Página principal con la información de los miembros del equipo
-  Páginas individuales para cada miembro
-  Formulario de contacto accesible desde `/grupo` mediante el botón **Contáctanos**
-  Persistencia de datos mediante JPA (Hibernate)
-  Base de datos PostgreSQL
-  Datos iniciales cargados automáticamente mediante `data.sql`
-  Estilos CSS básicos para las vistas
-  Configuración mediante Docker Compose para ejecutar la aplicación junto con PostgreSQL

## Tecnologías

- **Java 25**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Thymeleaf**
- **PostgreSQL**
- **Maven**
- **Docker**
- **Docker Compose**
- **HTML5**
- **CSS3**

## Requisitos

Para ejecutar el proyecto mediante Docker:

- Docker Desktop instalado y en ejecución
- Docker Compose (incluido actualmente en Docker Desktop)
- No es necesario instalar PostgreSQL ni Maven en el equipo si se utiliza Docker

## Inicio Rápido con Docker

### 1. Clona el repositorio

```bash
git clone <repo-url>
cd wiki-backend
```

### 2. Levanta la aplicación y la base de datos

```bash
docker compose up --build
```

Este comando crea y levanta automáticamente:

- Un contenedor para la aplicación Spring Boot
- Un contenedor para PostgreSQL
- La base de datos `wiki`
- Las tablas necesarias mediante JPA
- Los datos iniciales definidos en `data.sql`

### 3. Abre la aplicación en el navegador

```
http://localhost:8080/grupo
```

### 4. Para detener los servicios

```
Ctrl + C
```

## Base de Datos

La aplicación utiliza **PostgreSQL** como sistema gestor de base de datos. Al utilizar Docker Compose, PostgreSQL se ejecuta en un contenedor independiente de la instalación local del equipo.

### Formulario de contacto

Se agregó un acceso desde la vista principal para llegar a `/contacto`, donde el usuario puede enviar nombre, correo, asunto y mensaje. El formulario queda visible desde la pantalla `/grupo` con el botón **Contáctanos**.

Los mensajes se guardan en PostgreSQL en la tabla `contactos` mediante la entidad `Contacto` y el `ContactoRepository`.

### Consulta de mensajes de contacto en PostgreSQL

La consulta para revisarlos es:

```sql
SELECT id, nombre, email, asunto, mensaje, created_at
FROM contactos
ORDER BY created_at DESC;
```

Y si solo quieres filtrar por un correo concreto:

```sql
SELECT *
FROM contactos
WHERE email = 'usuario@correo.com';
```

### Estructura de la Base de Datos

```
Docker Compose
├── wiki-app
│   └── Spring Boot
└── wiki-db
    └── PostgreSQL
        └── wiki
            └── estudiantes
```

### Configuración de Conexión

- **Host**: `postgres:5432`
- **Base de datos**: `wiki`
- **Datos iniciales**: `src/main/resources/data.sql`

## Acceso Manual a la Base de Datos

Si necesitas ejecutar consultas SQL directamente, primero asegúrate de que los servicios estén ejecutándose:

```bash
docker exec -it wiki-db psql -U postgres -d wiki
```

Una vez dentro de PostgreSQL, puedes ejecutar consultas como:

```sql
SELECT * FROM estudiantes;
```

Para salir:

```
\q
```

## Persistencia de la Base de Datos

PostgreSQL utiliza un volumen de Docker (`postgres_data`) para almacenar los datos. Esto permite conservar los datos aunque el contenedor de PostgreSQL sea detenido.

## Ejecución sin Docker

También es posible ejecutar la aplicación directamente con Maven, siempre que exista una instalación de PostgreSQL disponible y correctamente configurada.

### Con el Maven Wrapper

**En Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**En Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

En este caso, PostgreSQL debe estar ejecutándose localmente y la configuración de conexión debe coincidir con la definida en `application.properties`.

## Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/grupo` | Vista principal del equipo |
| GET | `/miembro1` | Perfil del miembro 1 |
| GET | `/miembro2` | Perfil del miembro 2 |
| GET | `/miembro3` | Perfil del miembro 3 |
| GET | `/miembro4` | Perfil del miembro 4 |

## Estructura de Directorios

```
src/
├── main/
│   ├── java/com/javeriana/wiki/
│   │   ├── controller/          → Controladores Spring
│   │   ├── entities/            → Entidades JPA
│   │   └── repository/          → Repositorios Spring Data JPA
│   │
│   └── resources/
│       ├── templates/           → Plantillas Thymeleaf
│       ├── static/
│       │   ├── css/             → Estilos CSS
│       │   └── images/          → Imágenes
│       ├── data.sql             → Datos iniciales de la base de datos
│       └── application.properties → Configuración de Spring Boot
│
├── Dockerfile                   → Imagen de la aplicación
├── docker-compose.yml           → Configuración de aplicación y PostgreSQL
├── .dockerignore                → Archivos excluidos del contexto de Docker
└── pom.xml                      → Dependencias y configuración de Maven
```

## Arquitectura

La aplicación sigue una arquitectura basada en **Spring Boot**, **JPA**, **PostgreSQL** y **Thymeleaf**.

```
PostgreSQL
    ↓
Spring Data JPA
    ↓
Repository
    ↓
Controller
    ↓
Thymeleaf
    ↓
HTML
    ↓
Navegador
```

Los datos de los estudiantes no se encuentran definidos directamente en las vistas. Los controladores consultan la información almacenada en PostgreSQL mediante los repositorios JPA y posteriormente la envían a las plantillas Thymeleaf para su renderizado dinámico.
## Documentación de Bizagi2

La página `/documentacion` (botón **Documentación** en `/grupo`) presenta el proyecto Bizagi2 y un visor con los
8 documentos de la entrega final. Los archivos viven en `src/main/resources/static/docs/` en dos formatos:
`.pdf` (lo que muestra el visor) y `.docx` (editable). Para actualizar un documento basta con reemplazar ambos
archivos con el mismo nombre. Los diagramas están en `src/main/resources/static/images/diagramas/`.

## Despliegue en el servidor

El workflow `.github/workflows/ci.yml` compila, prueba y publica la imagen `ghcr.io/jhony-hacku/wiki-dearrollo`
en cada push a `main`. En el servidor de producción de Bizagi2 (una sola vez):

```bash
sudo mkdir -p /opt/wiki && sudo chown deploy:deploy /opt/wiki && cd /opt/wiki
# copiar deploy/compose.yml y deploy/.env.example (scp), luego:
cp .env.example .env && sed -i "s/^DB_PASSWORD=.*/DB_PASSWORD=$(openssl rand -hex 24)/" .env && chmod 600 .env
docker compose pull && docker compose up -d
sudo tailscale funnel --bg --https=8443 http://127.0.0.1:8081
```

La wiki queda en `https://<servidor>.ts.net:8443`. Para actualizarla: `docker compose pull && docker compose up -d`.
Si el paquete de GHCR es privado, hay que marcarlo como público en GitHub (Packages → wiki-dearrollo →
Package settings → Change visibility) o iniciar sesión en el servidor con `docker login ghcr.io`.
