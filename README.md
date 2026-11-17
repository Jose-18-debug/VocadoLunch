# VocadoLunch

Aplicación de escritorio escolar para administrar desayunos, clientes, pedidos, repartidores, pagos y ventas. Está desarrollada en **Java 17**, **Swing**, **Maven**, **JDBC** y **MySQL**.

## 1. Requisitos

- JDK 17 (o una versión posterior compatible).
- IntelliJ IDEA.
- MySQL Server 8.x y MySQL Workbench.
- Maven (IntelliJ puede utilizar el Maven Wrapper/instalación integrada).

## 2. Preparar MySQL

1. Inicia MySQL Server y abre MySQL Workbench.
2. Conéctate a tu servidor local.
3. Para una instalación NUEVA, abre y ejecuta `database/crear_vocado_lunch_db.sql`. Este script crea las tablas en el orden correcto (usuarios, desayunos, pedidos y detalles).
4. Si ya tienes una base con datos, conserva tu esquema y ejecuta únicamente `database/actualizar_fase5.sql`.
5. Verifica las tablas y columnas:

```sql
USE vocado_lunch_db;
SHOW TABLES;
DESCRIBE usuarios;
DESCRIBE desayuno;
DESCRIBE pedidos;
DESCRIBE detalle_pedido;
```

La migración agrega a `pedidos` los campos `metodo_pago` y `pagado`, necesarios para Fase 5. El script puede ejecutarse más de una vez sin duplicar esas columnas.

La tabla `usuarios` debe contener usuarios con `rol='Cliente'` y `rol='Repartidor'`. Puedes registrarlos desde el módulo Clientes (clientes) o insertar un repartidor de prueba con SQL. Ejemplo:

```sql
INSERT INTO usuarios(nombre,telefono,direccion,rol,correo,`contraseña`)
VALUES ('Repartidor de prueba',5551234567,'Zona centro','Repartidor','repartidor@prueba.local','');
```

No uses IDs inventados en pedidos: el módulo carga clientes existentes y guarda su ID real.

## 3. Configurar la conexión local

La conexión está centralizada en `src/main/java/conexion/Conexion.java`. Por seguridad, la contraseña no se almacena en el repositorio.

Configura estas variables de entorno en la configuración de ejecución de IntelliJ:

- `VOCADO_DB_URL` = `jdbc:mysql://localhost:3306/vocado_lunch_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8`
- `VOCADO_DB_USER` = tu usuario local de MySQL (por ejemplo `root`)
- `VOCADO_DB_PASSWORD` = la contraseña local de ese usuario

Si no defines `VOCADO_DB_URL`, el programa usa la URL local predeterminada. No subas contraseñas a Git ni compartas las de otros integrantes.

## 4. Abrir y ejecutar en IntelliJ IDEA

1. Abre la carpeta raíz `VocadoLunch` (la que contiene `pom.xml`).
2. Espera a que IntelliJ importe/sincronice Maven.
3. Configura Project SDK en JDK 17.
4. Configura las variables de entorno indicadas arriba en **Run > Edit Configurations**.
5. Ejecuta primero `conexion.PruebaConexion` para comprobar el acceso a MySQL.
6. Si la prueba es correcta, ejecuta **`vista.VocadoLunchApp`**. Esta es la única clase que abre la aplicación.

`PruebaConexion` y `PruebaIntegracionBD` son utilidades de diagnóstico; no son el inicio normal del sistema. No ejecutes pruebas CRUD contra datos importantes sin revisar su código.

## 5. Módulos

### Inicio
Muestra el panel de bienvenida.

### Desayunos
- Consulta los desayunos ordenados por ID ascendente.
- Agrega y edita desayunos.
- La columna N.º es una numeración visual consecutiva (1, 2, 3...), mientras que ID es la clave real de MySQL.
- El botón Desactivar aplica una baja lógica (`disponible=0`). No renumera ni reutiliza IDs y conserva productos referenciados por pedidos anteriores.

### Clientes
Permite consultar clientes y registrar nuevos usuarios con rol `Cliente`.

### Pedidos
- Carga clientes existentes y desayunos disponibles desde MySQL.
- Permite agregar productos y cantidades al carrito.
- Calcula subtotales y total.
- Registra el pedido y sus detalles dentro de una transacción.
- Guarda método de pago y deja el pago inicialmente como pendiente.
- El folio es el ID autogenerado de `pedidos`.

### Repartidores
Permite consultar pedidos, cambiar su estado y asignar un usuario con rol `Repartidor`. La selección usa el ID real del objeto Usuario, por lo que no intenta convertir el texto de presentación a entero.

### Pagos y ventas
- Consulta el historial de pedidos.
- Permite confirmar el pago de un pedido.
- Calcula pedidos pagados e ingresos del día actual.

**Nota:** la confirmación de pago es administrativa/manual. El sistema no procesa transferencias bancarias ni se conecta con bancos o pasarelas de pago.

## 6. Reglas importantes de datos

- No renumeres claves primarias `AUTO_INCREMENT`. Si se elimina/desactiva un desayuno, los IDs posteriores no cambian.
- El orden de presentación se controla con `ORDER BY id_desayuno ASC`.
- Los pedidos requieren un `id_cliente` existente cuyo rol sea `Cliente`.
- Los repartidores asignados deben existir en `usuarios` con rol `Repartidor`.
- El total se vuelve a calcular desde los precios actuales de los desayunos en el momento de guardar el pedido.
- El pedido y sus detalles se guardan en una transacción: si falla una parte, se ejecuta `ROLLBACK`.

## 7. Estructura principal

```text
src/main/java/
  conexion/
    Conexion.java
  dao/
    DesayunoDAO.java
    PedidoDAO.java
    UsuarioDAO.java
  modelo/
    Desayuno.java
    DetallePedido.java
    Pedido.java
    Usuario.java
  vista/
    Estilo.java
    FormularioDesayuno.java
    PanelInicio.java
    PanelDesayunos.java
    PanelPedidos.java
    PanelClientes.java
    PanelRepartidores.java
    PanelVentas.java
    VentanaPrincipal.java
    VocadoLunchApp.java
src/main/resources/
  logo.png (opcional; colocar aquí cuando el equipo lo tenga)
database/
  actualizar_fase5.sql
```

## 8. Pruebas finales sugeridas

1. Ejecutar `PruebaConexion` y comprobar que las cuatro tablas existen.
2. Registrar un cliente en Clientes.
3. Registrar al menos dos desayunos disponibles.
4. Crear un pedido con más de un producto.
5. Revisar en Workbench que exista un registro en `pedidos` y sus registros en `detalle_pedido`.
6. Abrir Repartidores y asignar un usuario cuyo rol sea `Repartidor`.
7. Cambiar el estado del pedido.
8. Abrir Pagos y ventas y confirmar el pago.
9. Actualizar la pantalla y verificar que cambie a PAGADO y que el total vendido del día se actualice.

## 9. Limitaciones conocidas del proyecto escolar

- No incluye autenticación/roles de acceso completos.
- No procesa pagos reales ni valida comprobantes de transferencia.
- No tiene seguimiento GPS ni notificaciones automáticas.
- Los reportes de ventas se calculan con la fecha local del servidor MySQL y consideran pedidos marcados como pagados.
- El campo `fecha_hora` de la base original es texto; para un sistema de producción convendría migrarlo a `DATETIME`.

## 10. Git

No agregues credenciales locales, archivos `.idea` personales ni `target/`. Incluye `pom.xml`, `src/`, `database/` y este README. Revisa `git status` antes de hacer push.
