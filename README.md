# Natymoo Backend

Backend en Spring Boot 3.3 + PostgreSQL para la tienda de ropa Natymoo. Autenticación con JWT
guardado en **cookie httpOnly** (no en localStorage), y CRUD completo para las 16 tablas del
esquema.

## 1. Requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL corriendo localmente (o remoto)

## 2. Preparar la base de datos

1. Crea la base de datos:
   ```sql
   CREATE DATABASE natymoo_db;
   ```
2. Ejecuta el script `natymoo_database.sql` (el que ya tienes) contra esa base:
   ```
   psql -U postgres -d natymoo_db -f natymoo_database.sql
   ```

## 3. Configurar credenciales

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/natymoo_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

Y en producción, cambia:
```properties
app.jwt.secret=UNA_CLAVE_LARGA_Y_SECRETA_DE_PRODUCCION
app.jwt.cookie-secure=true
```

## 4. Correr el proyecto

```bash
mvn clean install
mvn spring-boot:run
```

El backend queda en `http://localhost:8080`.

## 5. Documentación interactiva (Swagger)

Con el backend corriendo, abre:

```
http://localhost:8080/swagger-ui.html
```

Ahí ves los 18 grupos de endpoints (Autenticación, Catálogo, Carrito, Compras,
Administración), cada uno con sus campos de entrada, ejemplos y la opción de
probarlos directo desde el navegador con "Try it out".

**Para probar rutas protegidas:** abre el grupo "Autenticación", usa
`POST /api/auth/login` con "Try it out" (o `/register` si no tienes cuenta
aún). En cuanto responda 200, el navegador ya guardó la cookie del JWT —
cualquier otro endpoint que pruebes desde esa misma pestaña queda autenticado
automáticamente, sin que tengas que copiar ningún token a mano.

La especificación cruda en formato OpenAPI (JSON) queda en
`http://localhost:8080/v3/api-docs`, por si quieres importarla en Postman u
otra herramienta.

## 6. Autenticación (JWT en cookie)

| Endpoint             | Método | Descripción                                    |
|-----------------------|--------|-------------------------------------------------|
| `/api/auth/register`  | POST   | Crea usuario, inicia sesión y setea la cookie   |
| `/api/auth/login`     | POST   | Verifica credenciales y setea la cookie         |
| `/api/auth/logout`    | POST   | Borra la cookie                                 |
| `/api/auth/me`        | GET    | Devuelve el usuario autenticado (según cookie)  |

Ejemplo de registro:
```json
POST /api/auth/register
{
  "nombre": "Natalia",
  "apellido": "Moreno",
  "email": "nati@correo.com",
  "password": "12345678",
  "telefono": "3001234567"
}
```

El backend responde con los datos del usuario y setea la cookie `natymoo_token` (httpOnly,
no accesible desde JavaScript). En cada request posterior el navegador la envía solo si
haces las peticiones **con credenciales**. Desde el frontend (fetch/axios), siempre incluye:

```js
fetch("http://localhost:8080/api/productos", {
  credentials: "include"
})
```

o con axios:
```js
axios.defaults.withCredentials = true;
```

Y en `application.properties` ajusta `app.cors.allowed-origins` con la URL exacta de tu
frontend (por defecto tiene `localhost:5173` y `localhost:3000`).

## 7. CRUD disponible

Cada una de las 16 tablas tiene su propio CRUD REST bajo `/api/...`:

| Tabla                     | Endpoint base                     |
|---------------------------|------------------------------------|
| roles                     | `/api/roles`                       |
| usuarios                  | `/api/usuarios`                    |
| direcciones_usuario       | `/api/direcciones-usuario`         |
| categorias                | `/api/categorias`                  |
| productos                 | `/api/productos`                   |
| variantes_producto        | `/api/variantes-producto`          |
| imagenes_producto         | `/api/imagenes-producto`           |
| carritos_compra           | `/api/carritos-compra`             |
| detalle_carrito           | `/api/detalle-carrito`             |
| cupones                   | `/api/cupones`                     |
| ordenes_compra            | `/api/ordenes-compra`              |
| detalle_orden             | `/api/detalle-orden`               |
| historial_estado_orden    | `/api/historial-estado-orden`      |
| pagos                     | `/api/pagos`                       |
| resenas_producto          | `/api/resenas-producto`            |
| lista_deseos              | `/api/lista-deseos`                |

Cada uno soporta:
- `GET /api/{recurso}` → listar todos
- `GET /api/{recurso}/{id}` → obtener uno
- `POST /api/{recurso}` → crear
- `PUT /api/{recurso}/{id}` → actualizar
- `DELETE /api/{recurso}/{id}` → eliminar

### Ejemplo: crear un producto
```json
POST /api/productos
{
  "nombre": "Pijama Manga Larga Ositos",
  "descripcion": "Pijama de algodon suave",
  "categoria": { "id": 1 },
  "precioBase": 89900,
  "genero": "MUJER",
  "material": "Algodon 100%"
}
```

Nota importante: cuando el JSON tiene una relación (como `categoria`, `producto`, `usuario`,
`rol`, `variante`, etc.), solo necesitas enviar el objeto con el `id` — el backend resuelve
la referencia completa contra la base de datos.

## 8. Reglas de acceso

- **Público** (sin login): ver productos, categorías, variantes, imágenes y reseñas (`GET`).
- **Autenticado** (cualquier usuario logueado): todo lo demás (carrito, órdenes, direcciones,
  reseñas propias, etc.).
- **Solo ADMIN**: gestionar roles, cupones, y crear/editar/borrar productos y categorías.

## 9. Notas de diseño

- `ddl-auto=validate`: Hibernate **no** crea ni modifica tablas, solo valida que las
  entidades coincidan con el esquema. Esto evita que Hibernate te dañe los `CHECK`
  constraints o triggers que ya definiste en el script SQL.
- El PATCH de actualización (`PUT`) usa `BeanUtils.copyProperties` para no tener que repetir
  campo por campo en cada servicio — copia todo excepto el `id`.
- Las contraseñas se guardan con **BCrypt** (`passwordHash`), nunca en texto plano.
- El JWT vive en una cookie `httpOnly` + `SameSite=Lax`, protegida contra robo por XSS. Para
  producción con HTTPS, cambia `app.jwt.cookie-secure=true`.

## 10. Siguiente paso sugerido

Conectar el frontend (React/Vite) usando `fetch`/`axios` con `credentials: "include"` en
cada llamada, y probar el flujo completo: registro → login → crear producto (como admin) →
agregar al carrito → crear orden → pagar.
