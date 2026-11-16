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
