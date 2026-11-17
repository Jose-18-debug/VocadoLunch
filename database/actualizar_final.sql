-- VocadoLunch - migración de Fase 5
-- Ejecutar en MySQL Workbench conectado al servidor local.
-- Agrega columnas de metodo/estado de pago sin borrar registros.
USE vocado_lunch_db;

SET @existe_metodo = (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pedidos' AND COLUMN_NAME = 'metodo_pago'
);
SET @sql_metodo = IF(@existe_metodo = 0,
  'ALTER TABLE pedidos ADD COLUMN metodo_pago VARCHAR(20) NULL',
  'SELECT ''La columna metodo_pago ya existe'' AS mensaje');
PREPARE stmt_metodo FROM @sql_metodo;
EXECUTE stmt_metodo;
DEALLOCATE PREPARE stmt_metodo;

SET @existe_pagado = (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'pedidos' AND COLUMN_NAME = 'pagado'
);
SET @sql_pagado = IF(@existe_pagado = 0,
  'ALTER TABLE pedidos ADD COLUMN pagado TINYINT(1) NOT NULL DEFAULT 0',
  'SELECT ''La columna pagado ya existe'' AS mensaje');
PREPARE stmt_pagado FROM @sql_pagado;
EXECUTE stmt_pagado;
DEALLOCATE PREPARE stmt_pagado;

-- Comprobación
SHOW COLUMNS FROM pedidos;
