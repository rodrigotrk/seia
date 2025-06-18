--Scripts em caráter prioritário
--Data de geração 29/11/2023
--Versão 4.29.6

BEGIN;

-- [35800] - Imóvel Rural sumiu do sistema

	INSERT
	        INTO
	        pessoa_imovel
	(ide_pessoa_imovel,
	        ide_pessoa,
	        ide_imovel,
	        dtc_criacao,
	        ind_excluido,
	        ide_tipo_vinculo_imovel,
	        dtc_exclusao,
	        ide_tipo_vinculo_pct,
	        dsc_tipo_vinculo_pct_outros)
	VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'),
	1111754,
	436213,
	now(),
	FALSE,
	1,
	NULL,
	NULL,
	NULL);
	UPDATE
	        imovel_rural
	SET
	        ide_requerente_cadastro = 1111754
	WHERE
	        ide_imovel_rural = 436213;
	        
-- [35711] - Erro ao realizar análise técnica

	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 1052788
	WHERE
	        ide_arquivo_processo = 252163;
	        
	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 1052788
	WHERE
	        ide_arquivo_processo = 256657;
	        
-- [35799] - Imóvel rural sumiu do sistema 

	UPDATE
	        imovel  
	SET
	        ind_excluido = FALSE 
	WHERE
	        ide_imovel  = 800246;

-- [35763] - Erro ao gerar Declaração de Inexibilidade
	        
	UPDATE
		logradouro
	SET
		ide_bairro = 2355850
	WHERE
		ide_logradouro = 3938736;
		
-- [35804] - Transferencia de Pauta
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 691318;

INSERT
	INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
VALUES 
		(82963,
		12,
		38,
		now(),
		TRUE,
		NULL,
		'Solicitação de remanejamento de processo através do chamado Redmine 35804',
		211,
		FALSE,
		1241,
		NULL);

-- [35573] Solicitação de Melhoria - Isenção de Boleto

	INSERT INTO motivo_isencao_boleto
	(ide_motivo_isencao_boleto, dsc_motivo_isencao_boleto, ind_ativo, ind_requerimento, ind_reenquadramento)
	VALUES(nextval('MOTIVO_ISENCAO_BOLETO_IDE_MOTIVO_ISENCAO_BOLETO_seq'), 'Demais casos a serem justificados no processo', TRUE, TRUE, FALSE);		
		
-- [35668] - Transferencia de Pauta

		UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 702532;

INSERT
	INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
VALUES 
		(82877,
		4,
		38,
		now(),
		TRUE,
		NULL,
		'Solicitação de remanejamento de processo através do chamado Redmine 35668',
		211,
		FALSE,
		1241,
		NULL);
 
-- [35795] Processo duplicado

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao IN (699652);
		
-- [35732] Levantamento de processos
-- Processos encaminhados para a pauta de Adriany Christina Pereira de Carvalho [COFAQ]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (645478, 645479, 645480, 645481, 687563, 687564, 687565, 687566, 687567);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(54095, 12, 9, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 354, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(59406, 12, 9, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 354, FALSE, 1, NULL);
-- Processos encaminhados para a pauta de Altamirano Vaz Lordello Neto [NOUT]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (26874,639333,29716,365627,365628,390259,473833,631062,631058,352802,674744,674745,674746,691362,691363,691364,691365,691366,716924);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2966, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(21771, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(49870, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(54784, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(58431, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(66349, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(68956, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(73950, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(75696, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(75902, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(77467, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2881, FALSE, 1, NULL);
-- Processos encaminhados para a pauta de Jose Antonio Almeida de Lacerda [COINE]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (193192,193193,140915,140921,12824,12825,16611,12539,12540,12541,12542,12538,359945,440001,
                                    689704,689705,689706,689707,689708,689709,689710,689711,694758,694759,694760,694761,694762);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2887, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(22023, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(41156, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(57785, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(58222, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(58222, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(993, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1062, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1063, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1101, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1131, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1133, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1134, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1135, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(13150, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 810, FALSE, 1, NULL);
-- Processos encaminhados para a pauta de José Paulo Novaes Mendes [COINS]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (573965,573966,573967,573968,573969,573970,26306,546606,546607,645864,645865,645866,645867,655312,
                                655313,655314,513389,635305,635306,635307,635308,635309,660297,660298,660299,660300,660301,660302,648027,
                                648028,685983,685984,685985,691362,691363,691364,691365,691366,703699,703698);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(8835, 4, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1348, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(4281, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(16430, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(45460, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(55051, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(64468, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(66122, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(73223, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(75696, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(76144, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(78636, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 215, FALSE, 1, NULL);
-- Processos encaminhados para a pauta de LUIS GUSTAVO TAVARES DA SILVA [COGED]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (636549,636518,636438,640381,77892,77895,77897,68682,685171,697708,696285,697902,35658,130461,84839,
                                    72913,65046,65047,44083,44070,44080,44085,44081,44078,44079,35657,278507,695822,526359,526360,695805,
                                    695806,695823,471439,471440,471441,471442,547351,547349,547350,547352,547357,547353,547354,547356,
                                    547340,547341,547342,547343,547347,547348,679122,679123,679124,679125,691318,704861);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(61857, 8, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(77999, 8, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(78137, 8, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(80329, 8, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3166, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3322, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3406, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(5205, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(7947, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(21284, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(33470, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(68448, 19, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(614, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(940, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(968, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1035, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1047, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1049, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1064, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1065, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1071, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(1072, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2044, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2766, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2919, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3066, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3132, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(3571, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(5800, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(7057, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(7156, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(8442, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(14835, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(37883, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(51851, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(80685, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(81315, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(82963, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(83766, 12, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 211, FALSE, 1, NULL);
-- Processos encaminhados para a pauta de MARCELO GUIMARÃES COSTA [COIND]:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN(696173,696174,695464,695465,695466,695467,88892,11988,678212,678213,
                                   678214,678215,678216,605834,680283,680284,680285);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(2728, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(8279, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(16208, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(69592, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(73689, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(78223, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(79079, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35732', 2180, FALSE, 1, NULL);


-- [35795] Processo duplicado

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao IN (699652);
	        
-- [35814] - Requerimento sem número
--FAZENDA CONJUNTO SANTA FÉ I
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                                  CAST('2023.001.0' AS VARCHAR) || 
                                  CAST((CAST(split_part(split_part((
                SELECT
                                        r.num_requerimento
                FROM
                                        requerimento r
                WHERE
                                        r.num_requerimento IS NOT NULL
                ORDER BY
                                        r.num_requerimento DESC
                LIMIT 1),
                                  '.',
                                   3),
                                   '/',
                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                           ),
        dtc_finalizacao = (
        SELECT
                                  dtc_movimentacao
        FROM
                                  tramitacao_requerimento
        WHERE
                                  ide_requerimento = 1279135
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1279135;
--FAZENDA CONJUNTO SANTA FÉ II
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                                  CAST('2023.001.0' AS VARCHAR) || 
                                  CAST((CAST(split_part(split_part((
                SELECT
                                        r.num_requerimento
                FROM
                                        requerimento r
                WHERE
                                        r.num_requerimento IS NOT NULL
                ORDER BY
                                        r.num_requerimento DESC
                LIMIT 1),
                                  '.',
                                   3),
                                   '/',
                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                           ),
        dtc_finalizacao = (
        SELECT
                                  dtc_movimentacao
        FROM
                                  tramitacao_requerimento
        WHERE
                                  ide_requerimento = 1279152
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1279152;
        

--[35833] Imóvel Rural sumiu do sistema
UPDATE
        pessoa_imovel
SET
        ind_excluido = FALSE
WHERE
        ide_pessoa_imovel = 508728;
INSERT
        INTO
        pessoa_imovel
(ide_pessoa_imovel,
        ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'),
670194,
342160,
now(),
FALSE,
2,
NULL,
NULL,
NULL);
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 670194
WHERE
        ide_imovel_rural = 342160;
        
--[35834] Imóvel Rural sumiu do sistema

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1948899; 

COMMIT;