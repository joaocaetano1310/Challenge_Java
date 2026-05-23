-- ==============================
-- Dados iniciais para testes
-- ==============================

-- Usuários
INSERT INTO TB_USUARIO (nome, email, senha, cpf, telefone) VALUES ('Joao Silva', 'joao@email.com', '123456', '123.456.789-00', '(11) 99999-0001');
INSERT INTO TB_USUARIO (nome, email, senha, cpf, telefone) VALUES ('Maria Souza', 'maria@email.com', '123456', '987.654.321-00', '(11) 99999-0002');

-- Pets
INSERT INTO TB_PET (nome, especie, raca, idade, tamanho, peso, usuario_id) VALUES ('Rex', 'CAO', 'Labrador', 3, 'Grande', 25.5, 1);
INSERT INTO TB_PET (nome, especie, raca, idade, tamanho, peso, usuario_id) VALUES ('Mia', 'GATO', 'Persa', 2, 'Pequeno', 4.2, 1);
INSERT INTO TB_PET (nome, especie, raca, idade, tamanho, peso, usuario_id) VALUES ('Bolinha', 'COELHO', NULL, 1, 'Pequeno', 1.8, 2);

-- Vacinas
INSERT INTO TB_VACINA (nome_vacina, data_aplicacao, proxima_dose, local_aplicacao, pet_id) VALUES ('Antirrabica', '2025-01-10', '2027-01-10', 'Clinica VetCare', 1);
INSERT INTO TB_VACINA (nome_vacina, data_aplicacao, proxima_dose, local_aplicacao, pet_id) VALUES ('V10', '2025-03-15', '2027-03-15', 'PetShop Central', 1);
INSERT INTO TB_VACINA (nome_vacina, data_aplicacao, proxima_dose, local_aplicacao, pet_id) VALUES ('FELV', '2025-02-20', '2027-02-20', 'Clinica VetCare', 2);

-- Consultas
INSERT INTO TB_CONSULTA (tipo_consulta, data, hora, local, pet_id) VALUES ('Check-up', '2026-06-10', '09:00:00', 'Clinica VetCare', 1);
INSERT INTO TB_CONSULTA (tipo_consulta, data, hora, local, pet_id) VALUES ('Vacinacao', '2026-06-15', '14:30:00', 'PetShop Central', 2);
INSERT INTO TB_CONSULTA (tipo_consulta, data, hora, local, pet_id) VALUES ('Retorno', '2026-07-01', '10:00:00', 'Clinica VetCare', 3);
