-- VocadoLunch: esquema completo para una instalación NUEVA.
-- Si ya tienes datos en vocado_lunch_db, NO ejecutes este archivo para reemplazar tablas.
-- En una instalación existente ejecuta database/actualizar_fase5.sql.
CREATE DATABASE IF NOT EXISTS vocado_lunch_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE vocado_lunch_db;

CREATE TABLE IF NOT EXISTS usuarios (
                                        id_usuario INT NOT NULL AUTO_INCREMENT,
                                        nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NULL,
    direccion VARCHAR(150) NULL,
    rol VARCHAR(20) NOT NULL,
    correo VARCHAR(100) NULL,
    `contraseña` VARCHAR(100) NULL,
    PRIMARY KEY (id_usuario)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS desayuno (
                                        id_desayuno INT NOT NULL AUTO_INCREMENT,
                                        nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0,
    descripcion VARCHAR(250) NULL,
    ruta VARCHAR(100) NULL,
    url_imagen VARCHAR(255) NULL,
    disponible TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_desayuno)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pedidos (
                                       id_pedidos INT NOT NULL AUTO_INCREMENT,
                                       id_cliente INT NOT NULL,
                                       id_repartidor INT NULL,
                                       fecha_hora VARCHAR(100) NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL DEFAULT 'Recibido',
    metodo_pago VARCHAR(20) NULL,
    pagado TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_pedidos),
    KEY idx_pedidos_cliente (id_cliente),
    KEY idx_pedidos_repartidor (id_repartidor),
    CONSTRAINT fk_pedidos_cliente FOREIGN KEY (id_cliente) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_pedidos_repartidor FOREIGN KEY (id_repartidor) REFERENCES usuarios(id_usuario)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalle_pedido (
                                              id_detalle_pedido INT NOT NULL AUTO_INCREMENT,
                                              id_pedido INT NOT NULL,
                                              id_desayuno INT NOT NULL,
                                              cantidad INT NOT NULL,
                                              subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_detalle_pedido),
    KEY idx_detalle_pedido (id_pedido),
    KEY idx_detalle_desayuno (id_desayuno),
    CONSTRAINT fk_detalle_pedido_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedidos),
    CONSTRAINT fk_detalle_pedido_desayuno FOREIGN KEY (id_desayuno) REFERENCES desayuno(id_desayuno)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
