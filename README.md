# VocadoLunch - Interfaz Java Swing

Proyecto inicial de escritorio con Java Swing, Maven y MySQL

## Estructura

- `conexion`: conexión JDBC y prueba de conexión
- `modelo`: clase Desayuno
- `dao`: operaciones CRUD de desayunos
- `vista`: ventana principal y paneles Swing
- `src/main/resources/logo.png`: logo del sistema de VocadoLunch

## Requisitos

- JDK 17 o superior
- IntelliJ IDEA
- MySQL Server en ejecución
- Base de datos `vocado_lunch_db` con la tabla `desayuno`

## Configurar la conexión

La clase `conexion.Conexion` utiliza por defecto:
- URL: `jdbc:mysql://localhost:3306/vocado_lunch_db`
- Usuario: `root`
- Contraseña: vacía (aún por escoger)

Se debe ajustar las variables `VOCADO_DB_USER` y `VOCADO_DB_PASSWORD` en tu entorno e desarrollo, o modificar los valores predeterminados en `Conexion.java` para que coincidan con el MySQL de tu computadora local. No usar la contraseña de otra computadora

## Ejecutar

1. Abre la carpeta del proyecto en IntelliJ IDEA como proyecto Maven
2. Espera a que Maven descargue MySQL Connector/J
3. Comprueba que la base de datos y la tabla `desayuno` existan
4. Ejecuta `conexion.PruebaConexion`
5. Ejecuta `vista.VocadoLunchApp`

La ventana principal puede abrir aunque MySQL no esté disponible. El módulo Desayunos necesita conexión para realizar operaciones reales

## Importante sobre el alcance

En esta primera entrega:
- El dashboard es una vista inicial; sus indicadores son marcadores y todavía no consultan ventas
- El módulo Desayunos sí tiene operaciones de listar, insertar, editar y eliminar contra la tabla `desayuno`
- Pedidos, Clientes, Repartidores y Ventas son pantallas preparadas visualmente, todavía sin persistencia

El código usa únicamente columnas presentes en la tabla `desayuno` del SQL compartido. Antes de implementar pedidos y pagos, hay que revisar el esquema completo y el orden de creación de las tablas y claves foráneas


VOCADOLUNCH - FASE 2: INTEGRACION Y PRUEBAS
===========================================

OBJETIVO
--------
Conectar Java con un servidor MySQL local y comprobar la base de datos.

ARCHIVOS
-------
Conexion.java: conexion JDBC. Lee VOCADO_DB_URL, VOCADO_DB_USER y
VOCADO_DB_PASSWORD. Valores por defecto: localhost, root y password vacia.
PruebaConexion.java: comprueba conexion, tablas y lectura de desayuno.
PruebaIntegracionBD.java: prueba INSERT/SELECT/UPDATE/DELETE en desayuno
dentro de una transaccion que termina con ROLLBACK.
database/vocado_lunch_db.sql: crea las cuatro tablas del esquema recibido.

REQUISITOS
----------
- MySQL Server instalado y ejecutandose.
- MySQL Workbench instalado.
- Maven con MySQL Connector/J 9.5.0 en pom.xml.
- JDK compatible con el source/target configurado en Maven.

CREAR LA BASE DE DATOS
----------------------
1. Abrir MySQL Workbench y conectar a la instancia local.
2. Introducir la contrasena del usuario local configurado.
3. Abrir database/vocado_lunch_db.sql.
4. Ejecutar con el icono del rayo.
5. Actualizar SCHEMAS y verificar vocado_lunch_db y sus tablas.

Workbench es el cliente grafico. MySQL Server es el servicio que almacena
y procesa los datos. Instalar solo Workbench no basta.

CONFIGURACION JAVA
------------------
Por defecto Conexion usa:
URL: jdbc:mysql://localhost:3306/vocado_lunch_db
USER: root
PASSWORD: vacia

Si asignaste contrasena a root, configura VOCADO_DB_USER y
VOCADO_DB_PASSWORD como variables de entorno. No subas contrasenas reales
a Git. VOCADO_DB_URL permite cambiar la URL.

PRUEBAS EN ORDEN
----------------
1. Recargar Maven en IntelliJ.
2. Ejecutar conexion.PruebaConexion. Debe mostrar CONEXION EXITOSA y
   las cuatro tablas como ENCONTRADA.
3. Ejecutar conexion.PruebaIntegracionBD. Debe mostrar INSERT OK,
   SELECT OK, UPDATE OK, DELETE OK y CRUD VERIFICADO.
4. Ejecutar vista.VocadoLunchApp.
5. En Desayunos, probar listar, agregar, editar y eliminar desde la interfaz.

NOTA DEL ESQUEMA
----------------
Se respetan los nombres de tablas y columnas del SQL compartido. Se corrigio
el orden de creacion: usuarios, desayuno, pedidos, detalle_pedido.
CREATE TABLE IF NOT EXISTS no modifica una tabla que ya existia con otras
columnas. Si ya hay tablas previas, revisar su estructura antes de ejecutar.

LOGO
----
Cuando recibas el archivo, colocarlo como:
src/main/resources/logo.png
No es necesario modificar estos archivos para agregarlo.
