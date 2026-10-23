# Microservicios de la tienda

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
