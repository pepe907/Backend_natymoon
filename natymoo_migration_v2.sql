-- ============================================================
-- MIGRACION v2 - Natymoo
-- Ejecutar SOLO si tu base de datos ya existia con el script
-- original (natymoo_database.sql) y quieres actualizarla sin
-- perder datos. Si vas a crear la base desde cero, no necesitas
-- este archivo: usa natymoo_database.sql directamente, ya viene
-- actualizado.
-- ============================================================

-- 1) Agregar Nequi, Daviplata y Nu como metodos de pago validos
ALTER TABLE pagos DROP CONSTRAINT IF EXISTS pagos_metodo_pago_check;
ALTER TABLE pagos ADD CONSTRAINT pagos_metodo_pago_check
    CHECK (metodo_pago IN ('TARJETA','PSE','EFECTIVO','TRANSFERENCIA','NEQUI','DAVIPLATA','NU'));

-- 2) Tabla de novedades (reportes de incidencias de los usuarios)
CREATE TABLE IF NOT EXISTS novedades (
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

CREATE INDEX IF NOT EXISTS idx_novedades_usuario ON novedades(usuario_id);

CREATE TRIGGER trg_novedades_update
    BEFORE UPDATE ON novedades
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion();

-- Nota: el trigger de arriba fallara si ya existe (Postgres no soporta
-- "CREATE TRIGGER IF NOT EXISTS" antes de la version 17). Si te marca
-- error de "trigger ya existe", ignoralo tranquilamente, significa que
-- ya estaba creado.
