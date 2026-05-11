-- ============================================================
-- Plataforma Fitness - Schema MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS desafio_fitness
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE desafio_fitness;

-- ------------------------------------------------------------
-- Tabela: USUARIOS
-- ------------------------------------------------------------
CREATE TABLE USUARIOS (
    usuario         INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    senha           VARCHAR(255)    NOT NULL,
    data_nascimento DATE            NULL,
    peso            DECIMAL(5,2)    NULL,
    altura          DECIMAL(4,2)    NULL,
    data_cadastro   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios_usuario        PRIMARY KEY (usuario),
    CONSTRAINT uq_usuarios_email          UNIQUE      (email),
    CONSTRAINT chk_usuarios_peso          CHECK       (peso > 0),
    CONSTRAINT chk_usuarios_altura        CHECK       (altura > 0)
);

-- ------------------------------------------------------------
-- Tabela: EXERCICIOS
-- ------------------------------------------------------------
CREATE TABLE EXERCICIOS (
    exercicio       INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    descricao       TEXT            NULL,
    grupo_muscular  VARCHAR(50)     NULL,
    equipamento     VARCHAR(100)    NULL,

    CONSTRAINT pk_exercicios_exercicio    PRIMARY KEY (exercicio),
    CONSTRAINT uq_exercicio_nome            UNIQUE      (nome)
);

-- ------------------------------------------------------------
-- Tabela: FICHA_TREINO
-- ------------------------------------------------------------
CREATE TABLE FICHAS_TREINO (
    ficha        INT             NOT NULL AUTO_INCREMENT,
    usuario      INT             NOT NULL,
    nome_ficha      VARCHAR(100)    NOT NULL,
    objetivo        VARCHAR(255)    NULL,
    data_criacao    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_fichastreino_ficha          PRIMARY KEY (ficha),
    CONSTRAINT fk_fichastreino_usuario        FOREIGN KEY (usuario)
        REFERENCES USUARIOS (usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- Tabela: FICHA_EXERCICIO
-- ------------------------------------------------------------
CREATE TABLE FICHAS_EXERCICIO (
    ficha_exercicio  INT             NOT NULL AUTO_INCREMENT,
    ficha               INT             NOT NULL,
    exercicio           INT             NOT NULL,
    series              INT             NOT NULL,
    repeticoes          INT             NOT NULL,
    carga_kg            DECIMAL(5,2)    NULL,
    tempo_descanso_seg  INT             NULL,
    ordem               INT             NOT NULL DEFAULT 1,
    observacoes         TEXT            NULL,

    CONSTRAINT pk_fichasexercicio_ficha_exercicio PRIMARY KEY (ficha_exercicio),
    CONSTRAINT fk_fichasexercicio_ficha           FOREIGN KEY (ficha)
        REFERENCES FICHAS_TREINO (ficha)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_fichasexercicio_exercicio       FOREIGN KEY (exercicio)
        REFERENCES EXERCICIOS (exercicio)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT chk_fichasexercicio_series            CHECK (series > 0),
    CONSTRAINT chk_fichasexercicio_repeticoes        CHECK (repeticoes > 0),
    CONSTRAINT chk_fichasexercicio_carga_kg          CHECK (carga_kg >= 0),
    CONSTRAINT chk_fichasexercicio_tempo_descanso_seg CHECK (tempo_descanso_seg >= 0),
    CONSTRAINT chk_fichasexercicio_ordem             CHECK (ordem > 0)
);
