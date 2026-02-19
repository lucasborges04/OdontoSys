CREATE DATABASE IF NOT EXISTS odontosys;
USE odontosys;

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nivel_permissao VARCHAR(50) NOT NULL COMMENT 'Enum: ADMINISTRADOR, RECEPCIONISTA, DENTISTA'
);

CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE dentista (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100) NOT NULL,
    cro VARCHAR(20) NOT NULL UNIQUE COMMENT 'Conselho Regional de Odontologia',
    telefone VARCHAR(20)
);

CREATE TABLE tratamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL
);

CREATE TABLE consulta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_consulta DATE NOT NULL,
    horario TIME NOT NULL,
    status VARCHAR(50) NOT NULL COMMENT 'Enum: AGENDADA, REALIZADA, CANCELADA, NAO_COMPARECEU',
    paciente_id BIGINT NOT NULL,
    dentista_id BIGINT NOT NULL,

    CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_consulta_dentista FOREIGN KEY (dentista_id) REFERENCES dentista(id)
);

CREATE TABLE consulta_tratamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    consulta_id BIGINT NOT NULL,
    tratamento_id BIGINT NOT NULL,

    CONSTRAINT fk_ct_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id),
    CONSTRAINT fk_ct_tratamento FOREIGN KEY (tratamento_id) REFERENCES tratamento(id)
);

CREATE TABLE pagamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    valor DECIMAL(10, 2) NOT NULL,
    data_pagamento DATETIME NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL COMMENT 'Enum: DINHEIRO, CARTAO_CREDITO, PIX...',
    consulta_id BIGINT NOT NULL,

    CONSTRAINT fk_pagamento_consulta FOREIGN KEY (consulta_id) REFERENCES consulta(id)
);