-- ============================================================
--  NATYMOO - BASE DE DATOS PARA TIENDA DE ROPA (PostgreSQL)
--  Diseñada para integrarse con Spring Boot + JPA
-- ============================================================

-- ============================================================
-- 1. ROLES Y USUARIOS
-- ============================================================

CREATE TABLE roles (
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE  -- ADMIN, USER, EMPLEADO
);

CREATE TABLE usuarios (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,       -- BCrypt
    telefono        VARCHAR(20),
    rol_id          INTEGER NOT NULL REFERENCES roles(id),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro  TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE direcciones_usuario (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id          BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    alias               VARCHAR(50),              -- "Casa", "Trabajo"
    destinatario        VARCHAR(150) NOT NULL,
    telefono_contacto   VARCHAR(20),
    calle               VARCHAR(200) NOT NULL,
    numero              VARCHAR(20),
    ciudad              VARCHAR(100) NOT NULL,
    departamento        VARCHAR(100),
    codigo_postal       VARCHAR(20),
    pais                VARCHAR(100) NOT NULL DEFAULT 'Colombia',
    es_principal        BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 2. CATÁLOGO: CATEGORÍAS, MARCAS, PRODUCTOS Y VARIANTES
-- ============================================================

CREATE TABLE categorias (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         VARCHAR(255),
    categoria_padre_id  INTEGER REFERENCES categorias(id),  -- permite subcategorías
    activo              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE productos (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre              VARCHAR(150) NOT NULL,
    descripcion         TEXT,
    categoria_id        INTEGER NOT NULL REFERENCES categorias(id),
    precio_base         NUMERIC(12,2) NOT NULL CHECK (precio_base >= 0),
    precio_oferta       NUMERIC(12,2) CHECK (precio_oferta >= 0),
    genero              VARCHAR(20) CHECK (genero IN ('HOMBRE','MUJER','UNISEX','NINO','NINA')),
    material            VARCHAR(150),
    activo              BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion      TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Cada combinación de talla/color es una variante con su propio stock y SKU.
CREATE TABLE variantes_producto (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id         BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    sku                 VARCHAR(50) NOT NULL UNIQUE,
    talla               VARCHAR(10) NOT NULL,   -- XS, S, M, L, XL, XXL
    color               VARCHAR(50) NOT NULL,
    stock               INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    precio_adicional    NUMERIC(12,2) NOT NULL DEFAULT 0, -- ajuste sobre precio_base si aplica
    activo              BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (producto_id, talla, color)
);

CREATE TABLE imagenes_producto (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id     BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    variante_id     BIGINT REFERENCES variantes_producto(id) ON DELETE CASCADE, -- NULL = imagen genérica del producto
    url             VARCHAR(500) NOT NULL,
    orden           INTEGER NOT NULL DEFAULT 0,
    es_principal    BOOLEAN NOT NULL DEFAULT FALSE
);

-- ============================================================
-- 3. CARRITO DE COMPRAS
-- ============================================================

CREATE TABLE carritos_compra (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id      BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    estado          VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO','CONVERTIDO','ABANDONADO')),
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE detalle_carrito (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    carrito_id      BIGINT NOT NULL REFERENCES carritos_compra(id) ON DELETE CASCADE,
    variante_id     BIGINT NOT NULL REFERENCES variantes_producto(id),
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL,  -- precio congelado al momento de agregar
    UNIQUE (carrito_id, variante_id)
);

-- ============================================================
-- 4. CUPONES Y DESCUENTOS
-- ============================================================

CREATE TABLE cupones (
    id              INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo          VARCHAR(30) NOT NULL UNIQUE,
    tipo_descuento  VARCHAR(20) NOT NULL CHECK (tipo_descuento IN ('PORCENTAJE','MONTO_FIJO')),
    valor           NUMERIC(12,2) NOT NULL CHECK (valor > 0),
    monto_minimo    NUMERIC(12,2) DEFAULT 0,
    fecha_inicio    TIMESTAMP NOT NULL,
    fecha_fin       TIMESTAMP NOT NULL,
    uso_maximo      INTEGER,               -- NULL = ilimitado
    usos_actuales   INTEGER NOT NULL DEFAULT 0,
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    CHECK (fecha_fin > fecha_inicio)
);

-- ============================================================
-- 5. ÓRDENES DE COMPRA Y PAGOS
-- ============================================================

CREATE TABLE ordenes_compra (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id          BIGINT NOT NULL REFERENCES usuarios(id),
    direccion_id        BIGINT NOT NULL REFERENCES direcciones_usuario(id),
    cupon_id            INTEGER REFERENCES cupones(id),
    subtotal            NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0),
    descuento           NUMERIC(12,2) NOT NULL DEFAULT 0,
    costo_envio         NUMERIC(12,2) NOT NULL DEFAULT 0,
    total               NUMERIC(12,2) NOT NULL CHECK (total >= 0),
    estado              VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                        CHECK (estado IN ('PENDIENTE','PAGADO','EN_PREPARACION','ENVIADO','ENTREGADO','CANCELADO')),
    fecha_creacion      TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE detalle_orden (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orden_id        BIGINT NOT NULL REFERENCES ordenes_compra(id) ON DELETE CASCADE,
    variante_id     BIGINT NOT NULL REFERENCES variantes_producto(id),
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal        NUMERIC(12,2) NOT NULL
);

-- Historial de estados: útil para tracking del pedido en el frontend
CREATE TABLE historial_estado_orden (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orden_id    BIGINT NOT NULL REFERENCES ordenes_compra(id) ON DELETE CASCADE,
    estado      VARCHAR(20) NOT NULL,
    comentario  VARCHAR(255),
    fecha       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pagos (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orden_id                BIGINT NOT NULL REFERENCES ordenes_compra(id) ON DELETE CASCADE,
    metodo_pago             VARCHAR(30) NOT NULL CHECK (metodo_pago IN ('TARJETA','PSE','EFECTIVO','TRANSFERENCIA','NEQUI','DAVIPLATA','NU')),
    monto                   NUMERIC(12,2) NOT NULL CHECK (monto >= 0),
    estado                  VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE','APROBADO','RECHAZADO','REEMBOLSADO')),
    referencia_transaccion  VARCHAR(150),
    fecha_pago              TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 6. RESEÑAS Y LISTA DE DESEOS
-- ============================================================

CREATE TABLE resenas_producto (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id     BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    usuario_id      BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    calificacion    SMALLINT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    comentario      TEXT,
    fecha           TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (producto_id, usuario_id)  -- un usuario reseña un producto una sola vez
);

CREATE TABLE lista_deseos (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id      BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    producto_id     BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    fecha_agregado  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (usuario_id, producto_id)
);

-- ============================================================
-- 6.5. NOVEDADES (reportes de incidencias de los usuarios)
-- ============================================================

CREATE TABLE novedades (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id          BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    orden_id            BIGINT REFERENCES ordenes_compra(id) ON DELETE SET NULL,
    asunto              VARCHAR(150) NOT NULL,
    descripcion         TEXT NOT NULL,
    estado              VARCHAR(20) NOT NULL DEFAULT 'ABIERTA'
                        CHECK (estado IN ('ABIERTA','EN_PROCESO','RESUELTA')),
    respuesta           TEXT,
    fecha_creacion      TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 7. ÍNDICES PARA RENDIMIENTO
-- ============================================================

CREATE INDEX idx_productos_categoria     ON productos(categoria_id);
CREATE INDEX idx_variantes_producto      ON variantes_producto(producto_id);
CREATE INDEX idx_detalle_carrito_carrito ON detalle_carrito(carrito_id);
CREATE INDEX idx_ordenes_usuario         ON ordenes_compra(usuario_id);
CREATE INDEX idx_detalle_orden_orden     ON detalle_orden(orden_id);
CREATE INDEX idx_pagos_orden             ON pagos(orden_id);
CREATE INDEX idx_resenas_producto        ON resenas_producto(producto_id);
CREATE INDEX idx_direcciones_usuario     ON direcciones_usuario(usuario_id);
CREATE INDEX idx_novedades_usuario       ON novedades(usuario_id);

-- ============================================================
-- 8. TRIGGERS: actualizar fecha_actualizacion automáticamente
-- ============================================================

CREATE OR REPLACE FUNCTION actualizar_fecha_actualizacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_usuarios_update
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

CREATE TRIGGER trg_productos_update
    BEFORE UPDATE ON productos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

CREATE TRIGGER trg_carritos_update
    BEFORE UPDATE ON carritos_compra
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

CREATE TRIGGER trg_ordenes_update
    BEFORE UPDATE ON ordenes_compra
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

CREATE TRIGGER trg_novedades_update
    BEFORE UPDATE ON novedades
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

-- ============================================================
-- 9. DATOS INICIALES (SEED)
-- ============================================================

INSERT INTO roles (nombre) VALUES ('ADMIN'), ('USER'), ('EMPLEADO');

INSERT INTO categorias (nombre, descripcion) VALUES
('Pijamas', 'Ropa de dormir'),
('Ropa interior', 'Ropa íntima'),
('Batas', 'Batas de baño y estar en casa'),
('Accesorios', 'Complementos varios');

-- Producto de ejemplo con variantes
INSERT INTO productos (nombre, descripcion, categoria_id, precio_base, genero, material)
VALUES ('Pijama Manga Larga Ositos', 'Pijama de algodón suave con estampado de ositos', 1, 89900, 'MUJER', 'Algodón 100%');

INSERT INTO variantes_producto (producto_id, sku, talla, color, stock)
VALUES
(1, 'PIJ-OSITOS-S-ROSA', 'S', 'Rosa', 15),
(1, 'PIJ-OSITOS-M-ROSA', 'M', 'Rosa', 20),
(1, 'PIJ-OSITOS-L-ROSA', 'L', 'Rosa', 10),
(1, 'PIJ-OSITOS-S-AZUL', 'S', 'Azul', 12);

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
