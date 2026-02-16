-- =====================================================
-- Script de Creación de Base de Datos
-- Sistema de Gestión de Usuarios y Tarjetas
-- =====================================================

-- Eliminar secuencias si existen (para poder recrear)
DROP SEQUENCE USUARIO_SEQ;
DROP SEQUENCE TARJETA_SEQ;

-- Eliminar tablas si existen (para poder recrear)
DROP TABLE TARJETAS CASCADE CONSTRAINTS;
DROP TABLE USUARIOS CASCADE CONSTRAINTS;

-- =====================================================
-- TABLA: USUARIOS
-- Descripción: Almacena la información de los usuarios del sistema
-- =====================================================
CREATE TABLE USUARIOS (
    ID NUMBER(19) NOT NULL,
    NOMBRE VARCHAR2(100) NOT NULL,
    APELLIDO VARCHAR2(100) NOT NULL,
    EMAIL VARCHAR2(150) NOT NULL,
    TELEFONO VARCHAR2(10) NOT NULL,
    ACTIVO NUMBER(1) DEFAULT 1 NOT NULL,
    CONSTRAINT PK_USUARIOS PRIMARY KEY (ID),
    CONSTRAINT UK_USUARIOS_EMAIL UNIQUE (EMAIL),
    CONSTRAINT CHK_USUARIOS_ACTIVO CHECK (ACTIVO IN (0, 1))
);

-- Comentarios de la tabla USUARIOS
COMMENT ON TABLE USUARIOS IS 'Tabla que almacena la información de los usuarios del sistema';
COMMENT ON COLUMN USUARIOS.ID IS 'Identificador único del usuario';
COMMENT ON COLUMN USUARIOS.NOMBRE IS 'Nombre del usuario';
COMMENT ON COLUMN USUARIOS.APELLIDO IS 'Apellido del usuario';
COMMENT ON COLUMN USUARIOS.EMAIL IS 'Correo electrónico del usuario (único)';
COMMENT ON COLUMN USUARIOS.TELEFONO IS 'Teléfono del usuario (10 dígitos)';
COMMENT ON COLUMN USUARIOS.ACTIVO IS 'Indica si el usuario está activo (1) o inactivo (0)';

-- =====================================================
-- TABLA: TARJETAS
-- Descripción: Almacena las tarjetas bancarias de los usuarios
-- Utiliza herencia con discriminador para diferentes tipos
-- =====================================================
CREATE TABLE TARJETAS (
    ID NUMBER(19) NOT NULL,
    TIPO_TARJETA VARCHAR2(20) NOT NULL,
    NUMERO_TARJETA VARCHAR2(16) NOT NULL,
    TITULAR VARCHAR2(100) NOT NULL,
    FECHA_VENCIMIENTO DATE NOT NULL,
    SALDO NUMBER(15,2) DEFAULT 0 NOT NULL,
    ACTIVA NUMBER(1) DEFAULT 1 NOT NULL,
    USUARIO_ID NUMBER(19) NOT NULL,
    -- Campos específicos para Tarjeta de Crédito
    LIMITE_CREDITO NUMBER(15,2),
    TASA_INTERES NUMBER(5,2),
    -- Campos específicos para Tarjeta de Débito
    CUENTA_ASOCIADA VARCHAR2(20),
    PERMITE_SOBREGIRO NUMBER(1),
    LIMITE_SOBREGIRO NUMBER(15,2),
    -- Campos específicos para Tarjeta de Nómina
    EMPRESA VARCHAR2(100),
    NUMERO_EMPLEADO VARCHAR2(50),
    DEPOSITO_MENSUAL NUMBER(15,2),
    CONSTRAINT PK_TARJETAS PRIMARY KEY (ID),
    CONSTRAINT UK_TARJETAS_NUMERO UNIQUE (NUMERO_TARJETA),
    CONSTRAINT FK_TARJETAS_USUARIO FOREIGN KEY (USUARIO_ID) 
        REFERENCES USUARIOS(ID) ON DELETE CASCADE,
    CONSTRAINT CHK_TARJETAS_TIPO CHECK (TIPO_TARJETA IN ('CREDITO', 'DEBITO', 'NOMINA')),
    CONSTRAINT CHK_TARJETAS_ACTIVA CHECK (ACTIVA IN (0, 1)),
    CONSTRAINT CHK_TARJETAS_SALDO CHECK (SALDO >= 0 OR TIPO_TARJETA = 'DEBITO'),
    CONSTRAINT CHK_TARJETAS_SOBREGIRO CHECK (PERMITE_SOBREGIRO IN (0, 1))
);

-- Comentarios de la tabla TARJETAS
COMMENT ON TABLE TARJETAS IS 'Tabla que almacena las tarjetas bancarias utilizando herencia (CREDITO, DEBITO, NOMINA)';
COMMENT ON COLUMN TARJETAS.ID IS 'Identificador único de la tarjeta';
COMMENT ON COLUMN TARJETAS.TIPO_TARJETA IS 'Discriminador de tipo: CREDITO, DEBITO o NOMINA';
COMMENT ON COLUMN TARJETAS.NUMERO_TARJETA IS 'Número de la tarjeta (16 dígitos, único)';
COMMENT ON COLUMN TARJETAS.TITULAR IS 'Nombre del titular de la tarjeta';
COMMENT ON COLUMN TARJETAS.FECHA_VENCIMIENTO IS 'Fecha de vencimiento de la tarjeta';
COMMENT ON COLUMN TARJETAS.SALDO IS 'Saldo o monto disponible en la tarjeta';
COMMENT ON COLUMN TARJETAS.ACTIVA IS 'Indica si la tarjeta está activa (1) o inactiva (0)';
COMMENT ON COLUMN TARJETAS.USUARIO_ID IS 'ID del usuario propietario de la tarjeta';
COMMENT ON COLUMN TARJETAS.LIMITE_CREDITO IS 'Límite de crédito (solo para CREDITO)';
COMMENT ON COLUMN TARJETAS.TASA_INTERES IS 'Tasa de interés anual (solo para CREDITO)';
COMMENT ON COLUMN TARJETAS.CUENTA_ASOCIADA IS 'Número de cuenta bancaria asociada (solo para DEBITO)';
COMMENT ON COLUMN TARJETAS.PERMITE_SOBREGIRO IS 'Indica si permite sobregiro (solo para DEBITO)';
COMMENT ON COLUMN TARJETAS.LIMITE_SOBREGIRO IS 'Límite de sobregiro permitido (solo para DEBITO)';
COMMENT ON COLUMN TARJETAS.EMPRESA IS 'Empresa empleadora (solo para NOMINA)';
COMMENT ON COLUMN TARJETAS.NUMERO_EMPLEADO IS 'Número de empleado (solo para NOMINA)';
COMMENT ON COLUMN TARJETAS.DEPOSITO_MENSUAL IS 'Monto del depósito mensual de nómina (solo para NOMINA)';

-- =====================================================
-- SECUENCIAS para generación de IDs
-- =====================================================
CREATE SEQUENCE USUARIO_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE TARJETA_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

COMMENT ON SEQUENCE USUARIO_SEQ IS 'Secuencia para generar IDs de usuarios';
COMMENT ON SEQUENCE TARJETA_SEQ IS 'Secuencia para generar IDs de tarjetas';

-- =====================================================
-- ÍNDICES para mejorar el rendimiento
-- =====================================================
CREATE INDEX IDX_TARJETAS_USUARIO ON TARJETAS(USUARIO_ID);
CREATE INDEX IDX_TARJETAS_TIPO ON TARJETAS(TIPO_TARJETA);
CREATE INDEX IDX_TARJETAS_ACTIVA ON TARJETAS(ACTIVA);
CREATE INDEX IDX_USUARIOS_EMAIL ON USUARIOS(EMAIL);
CREATE INDEX IDX_USUARIOS_ACTIVO ON USUARIOS(ACTIVO);

COMMENT ON INDEX IDX_TARJETAS_USUARIO IS 'Índice para búsquedas de tarjetas por usuario';
COMMENT ON INDEX IDX_TARJETAS_TIPO IS 'Índice para búsquedas de tarjetas por tipo';
COMMENT ON INDEX IDX_TARJETAS_ACTIVA IS 'Índice para búsquedas de tarjetas activas';
COMMENT ON INDEX IDX_USUARIOS_EMAIL IS 'Índice para búsquedas de usuarios por email';
COMMENT ON INDEX IDX_USUARIOS_ACTIVO IS 'Índice para búsquedas de usuarios activos';

-- =====================================================
-- COMMIT de los cambios
-- =====================================================
COMMIT;

SELECT 'Base de datos creada exitosamente' AS RESULTADO FROM DUAL;
