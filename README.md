# Microservicios de la tienda

## Base de datos MySQL con XAMPP

Inicia **MySQL** desde el panel de XAMPP. En phpMyAdmin (`http://localhost/phpmyadmin`),
abre la pestaña **SQL** y crea una base para cada microservicio:

```sql
CREATE DATABASE tienda_auth CHARACTER SET utf8mb4;
CREATE DATABASE tienda_productos CHARACTER SET utf8mb4;
CREATE DATABASE tienda_pedidos CHARACTER SET utf8mb4;
CREATE DATABASE tienda_pagos CHARACTER SET utf8mb4;
```

Los servicios se conectan a MySQL en `localhost:3306`, con usuario `root` y contraseña
vacía (valores predeterminados habituales de XAMPP). Hibernate crea o actualiza las tablas
al iniciar. Si cambiaste el puerto o las credenciales de MySQL, define `MYSQL_HOST`,
`MYSQL_PORT`, `MYSQL_USER` y `MYSQL_PASSWORD` en el entorno. `ddl-auto=update` es para
desarrollo local.

Los pedidos guardan sus productos en `pedido_items`, relacionada con `pedidos` mediante
JPA (`Pedido` uno-a-muchos `PedidoDetalle`). `productoId` se conserva como identificador
porque el catálogo pertenece a otro microservicio y otra base de datos; no se crean
relaciones JPA entre bases de microservicios diferentes.

## API Gateway

El gateway centraliza el acceso a los cuatro microservicios en `http://localhost:8080`.
Conserva las rutas que cada servicio ya expone:

| Servicio | Ruta | Puerto predeterminado |
| --- | --- | --- |
| Autenticación | `/api/auth/**` | 8081 |
| Catálogo de productos | `/api/productos/**` | 8082 |
| Pedidos | `/api/pedidos/**` | 8083 |
| Pagos | `/api/pagos/**` | 8084 |

Inicia cada aplicación en una terminal distinta desde la raíz del repositorio:

```powershell
.\autentificador\mvnw.cmd -f .\autentificador\pom.xml spring-boot:run
.\producto-catalogo\mvnw.cmd -f .\producto-catalogo\pom.xml spring-boot:run
.\pedido-ordenes\mvnw.cmd -f .\pedido-ordenes\pom.xml spring-boot:run
.\pago\mvnw.cmd -f .\pago\pom.xml spring-boot:run
.\autentificador\mvnw.cmd -f .\gateway\pom.xml spring-boot:run
```

Los puertos de los servicios se pueden cambiar con `PORT`; el puerto del gateway, con
`GATEWAY_PORT`. Las direcciones de destino del gateway se pueden configurar con
`AUTENTIFICADOR_URL`, `PRODUCTO_CATALOGO_URL`, `PEDIDO_ORDENES_URL` y `PAGO_URL`.

## Endpoints

En Postman, registra e inicia sesión primero. El login devuelve un JWT en `token`; para
el resto de las rutas configura **Authorization → Bearer Token** con ese valor. Solo
`POST /api/auth/registro` y `POST /api/auth/login` son públicos.

| Servicio | Método | Ruta | Operación |
| --- | --- | --- | --- |
| Autenticación | `POST` | `/api/auth/registro` | Registrar usuario |
| Autenticación | `POST` | `/api/auth/login` | Obtener JWT |
| Autenticación | `GET` | `/api/auth/usuario/{id}` | Consultar usuario |
| Autenticación | `PUT` | `/api/auth/usuario/{id}` | Actualizar usuario |
| Autenticación | `DELETE` | `/api/auth/usuario/{id}` | Eliminar usuario |
| Productos | `GET` | `/api/productos` | Listar productos |
| Productos | `GET` | `/api/productos/{id}` | Consultar producto |
| Productos | `POST` | `/api/productos` | Crear producto |
| Productos | `PUT` | `/api/productos/{id}` | Actualizar producto |
| Productos | `DELETE` | `/api/productos/{id}` | Eliminar producto |
| Pedidos | `GET` | `/api/pedidos` | Listar pedidos |
| Pedidos | `GET` | `/api/pedidos/{id}` | Consultar pedido |
| Pedidos | `GET` | `/api/pedidos/usuario/{usuarioId}` | Listar pedidos del usuario |
| Pedidos | `POST` | `/api/pedidos` | Crear pedido |
| Pedidos | `PUT` | `/api/pedidos/{id}` | Actualizar pedido |
| Pedidos | `DELETE` | `/api/pedidos/{id}` | Eliminar pedido |
| Pagos | `GET` | `/api/pagos` | Listar pagos |
| Pagos | `GET` | `/api/pagos/{id}` | Consultar pago |
| Pagos | `GET` | `/api/pagos/pedido/{pedidoId}` | Consultar pago de un pedido |
| Pagos | `POST` | `/api/pagos` | Crear/procesar pago |
| Pagos | `PUT` | `/api/pagos/{id}` | Actualizar pago |
| Pagos | `DELETE` | `/api/pagos/{id}` | Eliminar pago |

Ejemplo de creación de pedido con productos:

```json
{
  "usuarioId": 1,
  "total": 19990,
  "estado": "PENDIENTE",
  "items": [
    {
      "productoId": 1,
      "cantidad": 1,
      "precioUnitario": 19990
    }
  ]
}
```

Las solicitudes inválidas responden `400 Bad Request`, los recursos inexistentes `404 Not
Found`, los emails duplicados `409 Conflict` y las credenciales incorrectas
`401 Unauthorized`.

## Clonar, probar y empaquetar

Requisitos: JDK 21, Git y XAMPP con MySQL iniciado y las cuatro bases creadas. Desde
PowerShell:

```powershell
git clone https://github.com/cegardini-hue/Cesare_Gardini_010V.git
cd Cesare_Gardini_010V
```

Ejecuta pruebas y genera los JAR. Los tests usan H2 en memoria y no necesitan MySQL activo:

```powershell
.\autentificador\mvnw.cmd -f .\autentificador\pom.xml clean test package
.\producto-catalogo\mvnw.cmd -f .\producto-catalogo\pom.xml clean test package
.\pedido-ordenes\mvnw.cmd -f .\pedido-ordenes\pom.xml clean test package
.\pago\mvnw.cmd -f .\pago\pom.xml clean test package
.\autentificador\mvnw.cmd -f .\gateway\pom.xml clean test package
```

Los `.jar` se generan en la carpeta `target` de cada módulo. Para probar los endpoints
localmente, inicia las cinco aplicaciones usando los comandos de la sección API Gateway,
cada una en una terminal distinta, y envía solicitudes desde Postman.

Para mantener el historial solicitado en la pauta, trabajen en `feature/` y luego integren
los cambios a `develop` y `main`. Después de clonar, creen las ramas así:

```powershell
git switch -c develop
git switch -c feature/ep2-crud
```

Usen commits descriptivos en la rama de funcionalidad y súbanla al remoto para compartirla.
