-- Inserir dados iniciais de subestações se ainda não existirem
INSERT INTO TB_SUBESTACAO (ID_SUBESTACAO, CODIGO, NOME, LATITUDE, LONGITUDE)
SELECT 1, 'AML', 'Subestação A', -23.2744134389987, -49.4702838173763
WHERE NOT EXISTS (SELECT 1 FROM TB_SUBESTACAO WHERE CODIGO = 'AML');

INSERT INTO TB_SUBESTACAO (ID_SUBESTACAO, CODIGO, NOME, LATITUDE, LONGITUDE)
SELECT 2, 'MKP', 'Subestação B', -22.6999266804592, -46.996111878849
WHERE NOT EXISTS (SELECT 1 FROM TB_SUBESTACAO WHERE CODIGO = 'MKP');

INSERT INTO TB_SUBESTACAO (ID_SUBESTACAO, CODIGO, NOME, LATITUDE, LONGITUDE)
SELECT 3, 'ZFA', 'Subestação C', -23.0917377538889, -48.9241617522699
WHERE NOT EXISTS (SELECT 1 FROM TB_SUBESTACAO WHERE CODIGO = 'ZFA');

-- Inserir alguns dados de exemplo para as redes MT
INSERT INTO TB_REDE_MT (ID_REDE_MT, ID_SUBESTACAO, CODIGO, NOME, TENSAO_NOMINAL)
SELECT 1, 1, 'AML01', 'Rede MT 01 - AML', 13.8
WHERE NOT EXISTS (SELECT 1 FROM TB_REDE_MT WHERE CODIGO = 'AML01');

INSERT INTO TB_REDE_MT (ID_REDE_MT, ID_SUBESTACAO, CODIGO, NOME, TENSAO_NOMINAL)
SELECT 2, 1, 'AML02', 'Rede MT 02 - AML', 13.8
WHERE NOT EXISTS (SELECT 1 FROM TB_REDE_MT WHERE CODIGO = 'AML02');

INSERT INTO TB_REDE_MT (ID_REDE_MT, ID_SUBESTACAO, CODIGO, NOME, TENSAO_NOMINAL)
SELECT 3, 2, 'MKP01', 'Rede MT 01 - MKP', 23.1
WHERE NOT EXISTS (SELECT 1 FROM TB_REDE_MT WHERE CODIGO = 'MKP01');

INSERT INTO TB_REDE_MT (ID_REDE_MT, ID_SUBESTACAO, CODIGO, NOME, TENSAO_NOMINAL)
SELECT 4, 3, 'ZFA01', 'Rede MT 01 - ZFA', 34.5
WHERE NOT EXISTS (SELECT 1 FROM TB_REDE_MT WHERE CODIGO = 'ZFA01');