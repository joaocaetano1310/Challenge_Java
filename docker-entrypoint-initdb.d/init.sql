-- ============================================================
-- FutureVet API - Inicialização do banco MySQL
-- Executado automaticamente pelo container na primeira subida
-- ============================================================

CREATE DATABASE IF NOT EXISTS `db-futurevet`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `db-futurevet`;

-- ============================================================
-- TABELAS
-- ============================================================

CREATE TABLE IF NOT EXISTS tb_usuario (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(100) NOT NULL,
    email     VARCHAR(100) NOT NULL,
    senha     VARCHAR(255) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL,
    telefone  VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuario_email (email),
    UNIQUE KEY uq_usuario_cpf   (cpf)
);

CREATE TABLE IF NOT EXISTS tb_pet (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(80)  NOT NULL,
    especie     VARCHAR(20)  NOT NULL,
    raca        VARCHAR(80),
    idade       INT          NOT NULL,
    tamanho     VARCHAR(30)  NOT NULL,
    peso        DOUBLE       NOT NULL,
    usuario_id  BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pet_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE,
    CONSTRAINT ck_pet_especie CHECK (especie IN ('CAO','GATO','COELHO','OUTRO'))
);

CREATE TABLE IF NOT EXISTS tb_vacina (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nome_vacina      VARCHAR(100) NOT NULL,
    data_aplicacao   DATE         NOT NULL,
    proxima_dose     DATE         NOT NULL,
    local_aplicacao  VARCHAR(150) NOT NULL,
    pet_id           BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_vacina_pet FOREIGN KEY (pet_id) REFERENCES tb_pet(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_consulta (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_consulta  VARCHAR(100) NOT NULL,
    data           DATE         NOT NULL,
    hora           TIME         NOT NULL,
    local          VARCHAR(150) NOT NULL,
    pet_id         BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_consulta_pet FOREIGN KEY (pet_id) REFERENCES tb_pet(id) ON DELETE CASCADE
);

-- ============================================================
-- DADOS INICIAIS
-- ============================================================

INSERT INTO tb_usuario (nome, email, senha, cpf, telefone) VALUES
    ('Joao Silva',  'joao@email.com',  '123456', '123.456.789-00', '(11) 99999-0001'),
    ('Maria Souza', 'maria@email.com', '123456', '987.654.321-00', '(11) 99999-0002');

INSERT INTO tb_pet (nome, especie, raca, idade, tamanho, peso, usuario_id) VALUES
    ('Rex',     'CAO',    'Labrador', 3, 'Grande',  25.5, 1),
    ('Mia',     'GATO',   'Persa',    2, 'Pequeno',  4.2, 1),
    ('Bolinha', 'COELHO', NULL,       1, 'Pequeno',  1.8, 2);

INSERT INTO tb_vacina (nome_vacina, data_aplicacao, proxima_dose, local_aplicacao, pet_id) VALUES
    ('Antirrabica', '2025-01-10', '2027-01-10', 'Clinica VetCare',  1),
    ('V10',         '2025-03-15', '2027-03-15', 'PetShop Central',  1),
    ('FELV',        '2025-02-20', '2027-02-20', 'Clinica VetCare',  2);

INSERT INTO tb_consulta (tipo_consulta, data, hora, local, pet_id) VALUES
    ('Check-up',  '2026-06-10', '09:00:00', 'Clinica VetCare',  1),
    ('Vacinacao', '2026-06-15', '14:30:00', 'PetShop Central',  2),
    ('Retorno',   '2026-07-01', '10:00:00', 'Clinica VetCare',  3);
