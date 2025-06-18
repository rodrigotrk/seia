--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 28/05/2025 10:51:17
--Versão 4.36.0

BEGIN;

UPDATE
	logradouro l
SET
	ide_bairro = 1628,
	nom_logradouro = 'R. dos Industriários, Quadra D - lote 17',
	num_cep = 44010565,
	ide_municipio = 691
WHERE
	ide_logradouro = 4862183;
UPDATE
	requerimento_imovel
SET
	ind_excluido = TRUE
WHERE
	ide_requerimento = 1432019;
UPDATE
	requerimento ri
SET
	dtc_exclusao = now(),
	ind_excluido = TRUE
WHERE
	ide_requerimento IN (1411686, 1411685, 1411683);
UPDATE
	requerimento_imovel ri
SET
	dtc_exclusao = now(),
	ind_excluido = TRUE
WHERE
	ide_requerimento IN (1411686, 1411685, 1411683);
UPDATE
	requerimento_imovel
SET
	ide_requerimento = 1411687
WHERE
	ide_imovel = 1448041
	AND ide_tipo_imovel_requerimento = 2
	AND ide_requerimento IN (1411686, 1411685, 1411683);
INSERT
	INTO
	hist_historico (data_historico,
	acao_historico,
	ip_historico,
	ide_usuario)
VALUES (now(),
        'UPD',
        '201.50.135.226',
        1);
INSERT
	INTO
	hist_valor (val_valor,
	ide_campo,
	ide_registro,
	ide_historico)
VALUES (3,
        195,
        140205,
        currval('historico_ide_historico_seq'));
UPDATE
	public.reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 140205;
INSERT
	INTO
	especie_supressao (nom_especie_supressao,
	ind_ativo)
VALUES ('Enterolobium glaziovii',
TRUE);
INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('vinhático-cabeleira');
INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));
INSERT
	INTO
	especie_floresta (
    ide_especie_floresta,
	nom_especie_floresta,
	ide_natureza_especie_floresta,
	dtc_criacao,
	dtc_exclusao,
	ind_excluido)
SELECT
	nextval('especie_floresta_seq'),
	'Enterolobium glaziovii / vinhático-cabeleira',
	1,
	now(),
	NULL,
	FALSE
WHERE
	NOT EXISTS (
	SELECT
		1
	FROM
		especie_floresta
	WHERE
		nom_especie_floresta = 'Enterolobium glaziovii / vinhático-cabeleira');
INSERT
	INTO
	especie_supressao (nom_especie_supressao,
	ind_ativo)
VALUES ('Rauvolfia grandiflora',
TRUE);
INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('canudo-de-pito');
INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));
INSERT
	INTO
	especie_floresta (
    ide_especie_floresta,
	nom_especie_floresta,
	ide_natureza_especie_floresta,
	dtc_criacao,
	dtc_exclusao,
	ind_excluido)
SELECT
	nextval('especie_floresta_seq'),
	'Rauvolfia grandiflora / canudo-de-pito',
	1,
	now(),
	NULL,
	FALSE
WHERE
	NOT EXISTS (
	SELECT
		1
	FROM
		especie_floresta
	WHERE
		nom_especie_floresta = 'Rauvolfia grandiflora / canudo-de-pito');
INSERT
	INTO
	especie_supressao (nom_especie_supressao,
	ind_ativo)
VALUES ('Croton uruguayensis',
TRUE);
INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('Sangra-d''água');
INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));
INSERT
	INTO
	especie_floresta (
    ide_especie_floresta,
	nom_especie_floresta,
	ide_natureza_especie_floresta,
	dtc_criacao,
	dtc_exclusao,
	ind_excluido)
SELECT
	nextval('especie_floresta_seq'),
	'Croton uruguayensis / Sangra-d''água',
	1,
	now(),
	NULL,
	FALSE
WHERE
	NOT EXISTS (
	SELECT
		1
	FROM
		especie_floresta
	WHERE
		nom_especie_floresta = 'Croton uruguayensis / Sangra-d''água');
INSERT
	INTO
	especie_supressao (nom_especie_supressao,
	ind_ativo)
VALUES ('Sloanea granulosa',
TRUE);
INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('cabeça-de-negro');
INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));
INSERT
	INTO
	especie_floresta (
    ide_especie_floresta,
	nom_especie_floresta,
	ide_natureza_especie_floresta,
	dtc_criacao,
	dtc_exclusao,
	ind_excluido)
SELECT
	nextval('especie_floresta_seq'),
	'Sloanea granulosa / cabeça-de-negro',
	1,
	now(),
	NULL,
	FALSE
WHERE
	NOT EXISTS (
	SELECT
		1
	FROM
		especie_floresta
	WHERE
		nom_especie_floresta = 'Sloanea granulosa / cabeça-de-negro');
UPDATE
	requerimento_imovel
SET
	ind_excluido = TRUE
WHERE
	ide_requerimento = 1431758;
UPDATE
	logradouro l
SET
	ide_bairro = 1256,
	nom_logradouro = 'Via Matoim',
	num_cep = 43813000,
	ide_municipio = 999
WHERE
	ide_logradouro = 1585103;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 819367;
UPDATE
	requerimento_imovel
SET
	ind_excluido = TRUE,
	dtc_exclusao = now()
WHERE
	ide_requerimento_imovel = 1222081;
UPDATE
	requerimento
SET
	ind_excluido = TRUE,
	dtc_exclusao = now()
WHERE
	ide_requerimento = 1434998;
UPDATE
	enquadramento
SET
	ide_requerimento = NULL
WHERE
	ide_enquadramento = 228535;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 824393;

COMMIT;
