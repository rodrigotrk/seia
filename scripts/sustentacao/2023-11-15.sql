--Scripts em caráter prioritário
--Data de geração 01/11/2023
--Versão 4.29.4

BEGIN;
-- [35742] - Solicitação de Melhoria - CEP
	ALTER TABLE public.logradouro ADD nom_origem_api char(2) NULL;
	COMMENT ON COLUMN public.logradouro.nom_origem_api IS 'Verifica se foi carregado pela api dos correios (CO) ou pelo viacep (VI)';
	COMMENT ON COLUMN public.logradouro.ind_origem_api IS 'Identifica se foi carregada via api';

-- [35691] - Processo sumiu

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 578441;
		

-- [35690] - Processo sumiu

    UPDATE
            controle_tramitacao
    SET
            ind_fim_da_fila = TRUE
    WHERE
            ide_controle_tramitacao = 577971;
            
--[35685] Imóvel Rural sumiu do sistema

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1817438;

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
		1182780,
		1183930,
		now(),
		FALSE,
		1,
		NULL,
		NULL,
		NULL);

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 1182780
	WHERE
		ide_imovel_rural = 1183930;
		
-- [35582] Transferência de Pauta (COASP)

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao IN (465630,
	470708,
	473558,
	478124,
	478125,
	574954,
	574955,
	574956,
	574957,
	574958,
	594194,
	597151,
	600814,
	611742,
	656000,
	671558,
	671559);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 3930, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 7543, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 50526, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 54630, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 55576, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 69342, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 71517, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 79046, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 79708, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 80023, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 81479, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35582', 205, FALSE, 1243, NULL);
	
-- [35689] Inclusão de espécie florestal (RFP)
-- 1 - Handroanthus crhysotricha / Ipê amarelo
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Handroanthus crhysotricha / Ipê amarelo', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Handroanthus crhysotricha / Ipê amarelo');
-- 2 - Paraptadenia rigida / Angico-cangalha
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Paraptadenia rigida / Angico-cangalha', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Paraptadenia rigida / Angico-cangalha');
-- 3 - Espécie: Protium aracouchini / Amescla
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Protium aracouchini / Amescla', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Protium aracouchini / Amescla');
-- 4 - Espécie: Senna alata / Fedegoso
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Senna alata / Fedegoso', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Senna alata / Fedegoso');
-- 5 - Espécie: Sparattanthelium botocudorum / Agasalho-de-anum
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Sparattanthelium botocudorum / Agasalho-de-anum', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Sparattanthelium botocudorum / Agasalho-de-anum');
-- 6 - Espécie: Solanum granulosoleprosum / Fumo-bravo
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Solanum granulosoleprosum / Fumo-bravo', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Solanum granulosoleprosum / Fumo-bravo');
-- 7 - Espécie: Heliocarpus popayanensis / Algodoeiro
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Heliocarpus popayanensis / Algodoeiro', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Heliocarpus popayanensis / Algodoeiro');
-- 8 - Espécie: Albizia niopoides / Farinha - seca
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Albizia niopoides / Farinha - seca', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Albizia niopoides / Farinha - seca');
-- 9 - Espécie: Citharexylum myrianthum
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Citharexylum myrianthum', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Citharexylum myrianthum');
-- 10 - Espécie: Eschweilera ovata
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Eschweilera ovata', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Eschweilera ovata');
-- 11 - Espécie: Spondias mombin
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Spondias mombin', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Spondias mombin');
-- 12 - Espécie: Aegiphila integrifolia
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Aegiphila integrifolia', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Aegiphila integrifolia');
-- 13 - Espécie: Cedrela
	INSERT INTO
	        especie_floresta (ide_especie_floresta,
	        nom_especie_floresta,
	        ide_natureza_especie_floresta,
	        dtc_criacao,
	        dtc_exclusao,
	        ind_excluido)
	SELECT nextval('especie_floresta_seq'), 'Cedrela', 1, now(), NULL, FALSE
	WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Cedrela');
	
--[35700] - Imóvel Rural sumiu do sistema

        UPDATE
                pessoa_imovel
        SET
                ind_excluido = FALSE
        WHERE
                ide_pessoa_imovel = 1817438;
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
                361935,
                432725,
                now(),
                FALSE,
                1,
                NULL,
                NULL,
                NULL);
        UPDATE
                imovel_rural
        SET
                ide_requerente_cadastro = 361935
        WHERE
                ide_imovel_rural = 432725;

-- [35707] Transferência de Pauta

-- Removendo as duas últimas linhas do histórico do processo:
	DELETE FROM controle_tramitacao
	WHERE ide_controle_tramitacao IN (152406, 152405);

-- Vinculando o processo para a pauta da equipe técnica:
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (151912, 151911);

-- Removendo a notificação de arquivamento que iria ser aprovada antes:        

	DELETE FROM notificacao_motivo_notificacao
	WHERE ide_notificacao_motivo_notificacao = 11696;

	DELETE FROM public.notificacao
	WHERE ide_notificacao = 7952;	
	
-- [35715] Transferência de Pauta (DIFIM)

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 677874;
	INSERT INTO controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 46280, 4, 10, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35715', 674, FALSE, 6764, NULL);


-- [35661] Erro ao aprovar notificação
-- Removendo a notificação que não foi aprovada:

	DELETE FROM notificacao_motivo_notificacao
	WHERE ide_notificacao_motivo_notificacao = 49536;
	DELETE FROM notificacao
	WHERE ide_notificacao = 30277;
	

-- [35696]- Retorno de status da RL

	UPDATE
	        reserva_legal
	SET
	        ide_imovel_rural = NULL
	WHERE
	        ide_reserva_legal IN (5495, 5523, 5492, 5522, 3119, 2967, 2964, 2960, 2956);

COMMIT;