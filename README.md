# GameTracker

API REST para gestionar una colección personal de videojuegos: registrar títulos, indicar plataforma, género, año de salida y el estado en el que se encuentra cada juego (pendiente, jugando, terminado, abandonado).

Proyecto construido con **Java 21** y **Spring Boot 3** siguiendo una arquitectura por capas, con DTOs, validaciones, manejo global de errores y persistencia en MySQL.

---

## Stack

- Java 21 (LTS)
- Spring Boot 3.5.13
- Spring Web, Spring Data JPA, Spring Validation
- Hibernate
- MySQL 8
- Lombok
- Maven 3.9

---

## Arquitectura

```
com.sergioroldan.gametracker
├── controller    → Endpoints REST
├── service       → Lógica de negocio + mapeo Entity ↔ DTO
├── repository    → Acceso a datos (JpaRepository)
├── model         → Entidades JPA y enums
├── dto           → Data Transfer Objects con validaciones
└── exception     → Excepciones personalizadas + GlobalExceptionHandler
```

Separación clara de responsabilidades: el controlador no conoce la base de datos, el servicio no conoce HTTP, y el repositorio solo sabe de entidades.

---

## Modelo de datos

**Entidad `Game`**

| Campo         | Tipo         | Notas                                      |
|---------------|--------------|--------------------------------------------|
| `id`          | `Long`       | Clave primaria autogenerada                |
| `title`       | `String`     | Obligatorio, único                         |
| `platform`    | `String`     | Obligatorio                                |
| `genre`       | `String`     | Opcional                                   |
| `releaseYear` | `Integer`    | Mínimo 1958                                |
| `status`      | `GameStatus` | Enum: `PENDING`, `PLAYING`, `FINISHED`, `DROPPED` |

---

## Endpoints

Base URL: `http://localhost:8080/games`

| Método   | Ruta           | Descripción                                              |
|----------|----------------|----------------------------------------------------------|
| `GET`    | `/games`       | Lista todos los juegos. Admite filtros por query params. |
| `GET`    | `/games/{id}`  | Devuelve un juego por su id.                             |
| `POST`   | `/games`       | Crea un nuevo juego.                                     |
| `PUT`    | `/games/{id}`  | Actualiza un juego existente.                            |
| `DELETE` | `/games/{id}`  | Elimina un juego.                                        |

### Filtros en `GET /games`

- `?status=PLAYING` — filtra por estado
- `?platform=PS5` — filtra por plataforma
- `?status=FINISHED&platform=Switch` — combina ambos

### Ejemplo de cuerpo JSON

```json
{
  "title": "Hollow Knight",
  "platform": "PC",
  "genre": "Metroidvania",
  "releaseYear": 2017,
  "status": "FINISHED"
}
```

---

## Validaciones

**De formato** (en el DTO, disparadas con `@Valid`):

- `title` no puede estar vacío
- `platform` no puede estar vacía
- `releaseYear` no puede ser anterior a 1958
- `status` es obligatorio

**De negocio** (en el servicio):

- No puede existir más de un juego con el mismo título (`GameAlreadyExistsException`)
- El año de salida no puede ser futuro salvo que el estado sea `PENDING` (`InvalidGameDataException`)

---

## Manejo de errores

Las excepciones se centralizan en `GlobalExceptionHandler` con `@RestControllerAdvice`. Las respuestas siempre llegan al cliente en formato JSON uniforme:

| Excepción                        | HTTP | Significado                          |
|----------------------------------|------|--------------------------------------|
| `GameNotFoundException`          | 404  | El juego solicitado no existe        |
| `GameAlreadyExistsException`     | 409  | Ya hay un juego con ese título       |
| `InvalidGameDataException`       | 422  | Violación de regla de negocio        |
| `MethodArgumentNotValidException`| 400  | Fallo de validación del DTO          |
| `Exception` (genérica)           | 500  | Error inesperado del servidor        |

Ejemplo de respuesta de error:

```json
{
  "timestamp": "2026-04-13T17:22:14.123",
  "status": 404,
  "error": "Not Found",
  "message": "No se ha encontrado el juego con id 42"
}
```

---

## Puesta en marcha

### Requisitos

- Java 21
- Maven 3.9 (o usar el wrapper incluido `./mvnw`)
- MySQL 8 corriendo en local

### Configuración de la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE gametracker;
```

Las credenciales de MySQL no están hardcodeadas en `application.properties` por seguridad. Se leen desde variables de entorno:

```properties
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}
```

Antes de arrancar la aplicación hay que definir ambas variables. En Linux/macOS:

```bash
export MYSQL_USER=root
export MYSQL_PASSWORD=tu_password
```

En Windows (PowerShell):

```powershell
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="tu_password"
```

En IntelliJ IDEA, se pueden añadir en **Run/Debug Configurations → Environment variables**.

### Arrancar la aplicación

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### Compilar y ejecutar tests

```bash
./mvnw clean compile
./mvnw test
```

---

## Ejemplos con curl

Crear un juego:

```bash
curl -X POST http://localhost:8080/games \
  -H "Content-Type: application/json" \
  -d '{"title":"Elden Ring","platform":"PS5","genre":"Souls","releaseYear":2022,"status":"PLAYING"}'
```

Listar juegos filtrando por estado:

```bash
curl "http://localhost:8080/games?status=PLAYING"
```

Actualizar un juego:

```bash
curl -X PUT http://localhost:8080/games/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Elden Ring","platform":"PS5","genre":"Souls","releaseYear":2022,"status":"FINISHED"}'
```

Eliminar un juego:

```bash
curl -X DELETE http://localhost:8080/games/1
```

---

## Autor

Sergio Roldán — proyecto de aprendizaje para reforzar fundamentos de backend con Java y Spring Boot.
