-- Esquema local de VocadoLunch.
-- No elimina ni reemplaza tablas existentes.

CREATE DATABASE IF NOT EXISTS vocado_lunch_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vocado_lunch_db;

-- usuarios se crea primero porque pedidos depende de ella.
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) DEFAULT NULL,
  telefono INT DEFAULT NULL,
  direccion VARCHAR(100) DEFAULT NULL,
  rol VARCHAR(12) DEFAULT NULL,
  correo VARCHAR(100) DEFAULT NULL,
  `contraseña` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS desayuno (
  id_desayuno INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) DEFAULT NULL,
  precio FLOAT DEFAULT NULL,
  descripcion VARCHAR(250) DEFAULT NULL,
  ruta VARCHAR(100) DEFAULT NULL,
  url_imagen VARCHAR(100) DEFAULT NULL,
  disponible TINYINT(1) DEFAULT NULL,
  PRIMARY KEY (id_desayuno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- pedidos se crea antes que detalle_pedido.
CREATE TABLE IF NOT EXISTS pedidos (
  id_pedidos INT NOT NULL AUTO_INCREMENT,
  id_cliente INT DEFAULT NULL,
  id_repartidor INT DEFAULT NULL,
  fecha_hora VARCHAR(100) DEFAULT NULL,
  total FLOAT DEFAULT NULL,
  estado VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (id_pedidos),
  KEY idx_pedidos_cliente (id_cliente),
  KEY idx_pedidos_repartidor (id_repartidor),
  CONSTRAINT fk_pedidos_cliente
    FOREIGN KEY (id_cliente) REFERENCES usuarios (id_usuario),
  CONSTRAINT fk_pedidos_repartidor
    FOREIGN KEY (id_repartidor) REFERENCES usuarios (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS detalle_pedido (
  id_detalle_pedido INT NOT NULL AUTO_INCREMENT,
  id_pedido INT DEFAULT NULL,
  id_desayuno INT DEFAULT NULL,
  cantidad INT DEFAULT NULL,
  subtotal FLOAT DEFAULT NULL,
  PRIMARY KEY (id_detalle_pedido),
  KEY idx_detalle_pedido_pedido (id_pedido),
  KEY idx_detalle_pedido_desayuno (id_desayuno),
  CONSTRAINT fk_detalle_pedido_pedido
    FOREIGN KEY (id_pedido) REFERENCES pedidos (id_pedidos),
  CONSTRAINT fk_detalle_pedido_desayuno
    FOREIGN KEY (id_desayuno) REFERENCES desayuno (id_desayuno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SHOW TABLES;
