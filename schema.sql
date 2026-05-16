-- ============================================================
-- Plataforma Fitness - Schema MySQL
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS PROGRESSO_DESAFIOS;
DROP TABLE IF EXISTS DESAFIOS_EXERCICIOS;
DROP TABLE IF EXISTS DESAFIOS;
DROP TABLE IF EXISTS FICHAS_EXERCICIO;
DROP TABLE IF EXISTS FICHAS_TREINO;
DROP TABLE IF EXISTS EXERCICIOS;
DROP TABLE IF EXISTS USUARIOS;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------
-- Tabela: USUARIOS
-- ------------------------------------------------------------
CREATE TABLE USUARIOS (
    usuario         INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,

    CONSTRAINT pk_usuarios_usuario        PRIMARY KEY (usuario),
    CONSTRAINT uq_usuarios_email          UNIQUE      (email)
);

-- ------------------------------------------------------------
-- Tabela: EXERCICIOS
-- ------------------------------------------------------------
CREATE TABLE EXERCICIOS (
    exercicio       INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    descricao       TEXT            NULL,

    CONSTRAINT pk_exercicios_exercicio    PRIMARY KEY (exercicio)
);

-- ------------------------------------------------------------
-- Tabela: DESAFIOS
-- ------------------------------------------------------------
CREATE TABLE DESAFIOS (
    desafio         INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    descricao       TEXT            NULL,

    CONSTRAINT pk_desafios_desafio PRIMARY KEY (desafio)
);

-- ------------------------------------------------------------
-- Tabela: DESAFIOS_EXERCICIOS
-- ------------------------------------------------------------
CREATE TABLE DESAFIOS_EXERCICIOS (
    desafio         INT             NOT NULL,
    exercicio       INT             NOT NULL,

    CONSTRAINT pk_desafios_exercicios PRIMARY KEY (desafio, exercicio),
    CONSTRAINT fk_desafios_exercicios_desafio FOREIGN KEY (desafio)
        REFERENCES DESAFIOS (desafio)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_desafios_exercicios_exercicio FOREIGN KEY (exercicio)
        REFERENCES EXERCICIOS (exercicio)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- Tabela: PROGRESSO_DESAFIOS
-- ------------------------------------------------------------
CREATE TABLE PROGRESSO_DESAFIOS (
    usuario         INT             NOT NULL,
    desafio         INT             NOT NULL,
    exercicio       INT             NOT NULL,
    concluido       BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_progresso_desafios PRIMARY KEY (usuario, desafio, exercicio),
    CONSTRAINT fk_progresso_desafios_usuario FOREIGN KEY (usuario)
        REFERENCES USUARIOS (usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_progresso_desafios_desafio FOREIGN KEY (desafio)
        REFERENCES DESAFIOS (desafio)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_progresso_desafios_desafio_exercicio FOREIGN KEY (desafio, exercicio)
        REFERENCES DESAFIOS_EXERCICIOS (desafio, exercicio)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
