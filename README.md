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

Los servicios ya están configurados para conectarse a MySQL en `localhost:3306`, con
usuario `root` y contraseña vacía (valores predeterminados habituales de XAMPP). Al
iniciar, Hibernate crea o actualiza las tablas automáticamente. Si cambiaste el puerto
o las credenciales de MySQL, define `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USER` y
`MYSQL_PASSWORD` en el entorno antes de iniciar los servicios. `ddl-auto=update` está
destinado al desarrollo local.

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

Por ejemplo, el inicio de sesión se realiza mediante `POST http://localhost:8080/api/auth/login`
y las solicitudes al catálogo usan `http://localhost:8080/api/productos`.

Los puertos de los servicios se pueden cambiar con la variable `PORT`; el puerto del gateway,
con `GATEWAY_PORT`. Las direcciones de destino del gateway también se pueden configurar con
`AUTENTIFICADOR_URL`, `PRODUCTO_CATALOGO_URL`, `PEDIDO_ORDENES_URL` y `PAGO_URL`.

El gateway requiere un JWT válido en `Authorization: Bearer <token>` para acceder a los
endpoints, excepto `POST /api/auth/registro` y `POST /api/auth/login`. La clave de firma debe
ser la misma en el gateway y en autenticación; ambos leen `JWT_SECRET`. Si no se configura,
se usa la clave predeterminada de desarrollo. En producción, define una clave segura de al
menos 32 bytes en ambos servicios.

Después de iniciar sesión, envía el token recibido en las solicitudes protegidas:

```http
Authorization: Bearer <token>
```
