-- =====================================================================
-- NEXUS ACADÊMICO 
-- =====================================================================

CREATE DATABASE IF NOT EXISTS nexus_academico
	CHARACTER SET utf8mb4
	COLLATE utf8mb4_unicode_ci;
USE nexus_academico;


-- =====================================================================
-- 1. TABELAS
-- =====================================================================

-- 1.1 USUÁRIO | [ESPECIALIZAÇÃO - TABELA PAI]

CREATE TABLE usuario (
    id_usuario    INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    senha_hash    VARCHAR(255) NOT NULL,
	ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    data_cadastro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 1.2 ALUNO | [ESPECIALIZAÇÃO - TABELA FILHO]

CREATE TABLE aluno (
    id_aluno INT PRIMARY KEY,
    ra       VARCHAR(20)  NOT NULL UNIQUE,
    curso    VARCHAR(100) NOT NULL,
    periodo  VARCHAR(20)  NOT NULL,
    turma    CHAR(1)      NOT NULL,
	
    CONSTRAINT fk_aluno_usuario
        FOREIGN KEY (id_aluno) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.3 ORGANIZADOR | [ESPECIALIZAÇÃO - TABELA FILHO]

CREATE TABLE organizador (
    id_organizador INT PRIMARY KEY,
    vinculo        ENUM('professor', 'coordenador', 'membro_liga') NOT NULL,
    cargo          VARCHAR(100),
    
    CONSTRAINT fk_organizador_usuario
        FOREIGN KEY (id_organizador) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.4 MODERADOR | [ESPECIALIZAÇÃO - TABELA FILHO]

CREATE TABLE moderador (
    id_moderador INT PRIMARY KEY,
    nivel_acesso ENUM('basico', 'adm') NOT NULL,
    
    CONSTRAINT fk_moderador_usuario
        FOREIGN KEY (id_moderador) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.5 TIPO_EVENTO

CREATE TABLE tipo_evento(
    id_tipo_evento INT AUTO_INCREMENT PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL UNIQUE,
    descricao      VARCHAR(200)
);

-- 1.6 EVENTO

CREATE TABLE evento (
    id_evento      INT AUTO_INCREMENT PRIMARY KEY,
    id_organizador INT          NOT NULL,
    id_tipo_evento INT          NOT NULL,
    titulo         VARCHAR(100) NOT NULL,
    descricao      TEXT,
    local          VARCHAR(100),
    vagas          INT          NOT NULL,
    data_inicio    DATE         NOT NULL,
    data_fim       DATE         NOT NULL,
    carga_horaria  INT          NOT NULL,
    modalidade     ENUM('presencial', 'online', 'hibrido')   NOT NULL,
    status         ENUM('pendente', 'aprovado', 'rejeitado', 'cancelado') NOT NULL DEFAULT 'pendente',
    data_criacao   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_evento_organizador
        FOREIGN KEY (id_organizador) REFERENCES organizador (id_organizador)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_evento_tipo
        FOREIGN KEY (id_tipo_evento) REFERENCES tipo_evento (id_tipo_evento)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_evento_vagas CHECK (vagas > 0),
    CONSTRAINT chk_evento_horas CHECK (carga_horaria > 0),
    CONSTRAINT chk_evento_datas CHECK (data_fim >= data_inicio)
);

-- 1.7 INSCRICAO

CREATE TABLE inscricao (
    id_inscricao   INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno       INT      NOT NULL, 
    id_evento      INT      NOT NULL,
    data_inscricao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status         ENUM('confirmada', 'cancelada', 'concluida', 'ausente') NOT NULL DEFAULT 'confirmada',

    CONSTRAINT uq_inscricao_aluno_evento UNIQUE (id_aluno, id_evento),
    CONSTRAINT fk_inscricao_aluno
        FOREIGN KEY (id_aluno) REFERENCES aluno (id_aluno)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_inscricao_evento
        FOREIGN KEY (id_evento) REFERENCES evento (id_evento)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 1.8 AVALIACAO_EVENTO

CREATE TABLE avaliacao_evento (
    id_avaliacao   INT AUTO_INCREMENT PRIMARY KEY,
    id_moderador   INT      NOT NULL,
    id_evento      INT      NOT NULL,
    decisao        ENUM('aprovado', 'rejeitado') NOT NULL,
    data_avaliacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_avaliacao_moderador 
        FOREIGN KEY (id_moderador) REFERENCES moderador (id_moderador)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_avaliacao_evento
        FOREIGN KEY (id_evento) REFERENCES evento (id_evento)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 1.9 ENCONTRO_EVENTO

CREATE TABLE encontro_evento (
    id_encontro   INT AUTO_INCREMENT PRIMARY KEY,
    id_evento     INT  NOT NULL,
    data_encontro DATE NOT NULL,
    hora_inicio   TIME NOT NULL,
    hora_fim      TIME NOT NULL,

    CONSTRAINT fk_encontro_evento
        FOREIGN KEY (id_evento) REFERENCES evento (id_evento)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_encontro_horas CHECK (hora_fim > hora_inicio)
);

-- 1.10 PRESENCA

CREATE TABLE presenca (
    id_presenca   INT AUTO_INCREMENT PRIMARY KEY,
    id_inscricao  INT      NOT NULL,
    id_encontro   INT      NOT NULL,
    presente      BOOLEAN  NOT NULL DEFAULT FALSE,
    data_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uq_presenca_inscricao_encontro UNIQUE (id_inscricao, id_encontro),
    CONSTRAINT fk_presenca_inscricao
        FOREIGN KEY (id_inscricao) REFERENCES inscricao (id_inscricao)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_presenca_encontro
        FOREIGN KEY (id_encontro) REFERENCES encontro_evento (id_encontro)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.11 CERTIFICADO

CREATE TABLE certificado (
    id_certificado      INT AUTO_INCREMENT PRIMARY KEY,
    id_inscricao        INT         NOT NULL,
    codigo_verificacao  VARCHAR(64) NOT NULL UNIQUE,
    carga_horaria_total INT         NOT NULL,
    data_emissao        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    arquivo_url         VARCHAR(255),
    status              ENUM('emitido', 'cancelado') NOT NULL DEFAULT 'emitido',
    
    CONSTRAINT fk_certificado_inscricao
        FOREIGN KEY (id_inscricao) REFERENCES inscricao (id_inscricao)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_certificado_horas CHECK (carga_horaria_total >= 0)
);

-- 1.12 VALIDACAO_CERTIFICADO

CREATE TABLE validacao_certificado (
    id_validacao   INT AUTO_INCREMENT PRIMARY KEY,
    id_certificado INT         NOT NULL,
    data_validacao DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_origem      VARCHAR(45) NOT NULL,
    resultado      ENUM('valido', 'invalido') NOT NULL,
    
    CONSTRAINT fk_validacao_certificado
        FOREIGN KEY (id_certificado) REFERENCES certificado (id_certificado)
        ON DELETE CASCADE ON UPDATE CASCADE
);
