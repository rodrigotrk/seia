--Scripts em caráter prioritário
--Data de geração 13/12/2023
--Versão 4.29.8

BEGIN;

-- [35853] - Pagamento REPFLOR

	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento = 1579447;
-- [35849] - Transferência de Titularidade

	UPDATE 
	        requerimento
	SET
	        nom_contato = 'Brasil Refinarias LTDA'
	WHERE 
	        ide_requerimento = 43211;
	        
	UPDATE 
	        requerimento_pessoa
	SET
	        ide_pessoa = 10491
	WHERE 
	        ide_requerimento = 43211 AND ide_tipo_pessoa_requerimento = 1;
-- [35846] - Imóvel não encontrado

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE 
	WHERE
	        ide_pessoa_imovel = 265099;
	        
-- [35842] Transferência de Titularidade

	UPDATE 
		requerimento
	SET
		nom_contato = 'Agrifirma Bahia Agropecuária Ltda'
	WHERE 
		ide_requerimento = 967193;
	        
	UPDATE 
		requerimento_pessoa
	SET
		ide_pessoa = 80145
	WHERE 
		ide_requerimento = 967193 AND ide_tipo_pessoa_requerimento = 1;
		
-- [35838] - Erro na finalização CEFIR
 
	DELETE FROM cronograma_recuperacao
	WHERE ide_cronograma_recuperacao=96331;
	
	DELETE FROM app
	WHERE ide_app=136354;
	
	DELETE FROM cronograma_recuperacao
	WHERE ide_cronograma_recuperacao=96337;
	
	DELETE FROM app
	WHERE ide_app=136359;
	
-- [35829] - Transferência de pauta
	
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 702881;
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
	VALUES (77937,
	4,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703635;
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
	VALUES (80005,
	12,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703118;
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
	VALUES (88684,
	4,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703427;
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
	VALUES (89037,
	12,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703434;
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
	VALUES (89478,
	4,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703619;
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
	VALUES (89531,
	4,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 701764;
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
	VALUES (92974,
	19,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 703552;
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
	VALUES (94611,
	4,
	45,
	now(),
	TRUE,
	NULL,
	' Solicitação de remanejamento de processo através DO chamado Redmine 35829 ',
	2180,
	FALSE,
	4509,
	NULL);

-- [35820] - Erro ao editar cadastro do empreendimento

	UPDATE
	        localizacao_geografica
	SET
	        ide_classificacao_secao = 2
	WHERE
	        ide_localizacao_geografica = 16935;
	        

-- [35793] Transferência de Titularidade

	UPDATE 
	        requerimento
	SET
	        nom_contato = 'Agrifirma Bahia Agropecuária Ltda'
	WHERE 
	        ide_requerimento = 967944;
	        
	UPDATE 
	        requerimento_pessoa
	SET
	        ide_pessoa = 80145
	WHERE 
	        ide_requerimento = 967944 AND ide_tipo_pessoa_requerimento = 1;
	        
-- [35741] - Erro na finalização CEFIR

	INSERT
		INTO
		pct_familia (
		ide_pct,
		ide_pessoa,
		dtc_cadastro,
		dtc_exclusao,
		ind_excluido,
		ide_pessoa_associada)
	VALUES (447,
	494389,
	NOW(),
	NULL,
	FALSE,
	NULL);

-- [35856] - Pagamento REPFLOR
	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento = 1572010;
	        
--[35845] - Erro ao anexar shapes a notificação

	INSERT
		INTO
		public.motivo_notificacao_imovel
	(ide_motivo_notificacao_imovel,
		ide_notificacao_motivo_notificacao,
		ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'::text::regclass),
	82748,
	1221788);
	
-- [35873] Transferência de Titularidade
	UPDATE
		requerimento
	SET
		nom_contato = 'Enel Brasil S.A'
	WHERE
		ide_requerimento = 937991;
	
	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 1067846
	WHERE
		ide_requerimento = 937991
		AND ide_tipo_pessoa_requerimento = 1;
	
-- [35811] Erro ao tentar finalizar o CEFIR
	
	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural = 1217940;	
	
-- [35844] - Erro ao realizar análise técnica

	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 8306
	WHERE
	        ide_arquivo_processo = 204938;
	INSERT
	        INTO
	        public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao,
	        ide_imovel)
	VALUES (54112,
	8306);
	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 8306
	WHERE
	        ide_arquivo_processo = 204948;
	INSERT
	        INTO
	        public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao,
	        ide_imovel)
	VALUES (54111,
	8306);
	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 8306
	WHERE
	        ide_arquivo_processo = 257810;
	INSERT
	        INTO
	        public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao,
	        ide_imovel)
	VALUES (87129,
	8306);
	
-- [35826] - Pagamento REPFLOR

    UPDATE
            tramitacao_requerimento
    SET
            ide_status_requerimento = 8
    WHERE
            ide_tramitacao_requerimento = 1615780;
            
--[35892] - Inclusão de Espécies

-- nome cientifico: Handroanthus umbellatus
-- nome popular: Ipê-branco 
INSERT
        INTO
        especie_supressao
(nom_especie_supressao,
        ind_ativo)
VALUES
('Handroanthus umbellatus',
TRUE);
INSERT
        INTO
        nome_popular_especie
(nom_popular_especie)
VALUES
('Ipê-branco');
INSERT
        INTO
        especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
        currval ('especie_supressao_seq')
));
-- nome cientifico: Lycium martii
-- nome popular: Pau-de-tamanco 
INSERT
        INTO
        especie_supressao
(nom_especie_supressao,
        ind_ativo)
VALUES
('Lycium martii',
TRUE);
INSERT
        INTO
        nome_popular_especie
(nom_popular_especie)
VALUES
('Pau-de-tamanco');
INSERT
        INTO
        especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
        currval ('especie_supressao_seq')
));
-- nome cientifico: Sarcomphalus joazeiro
-- nome popular: Juá 
INSERT
        INTO
        especie_supressao
(nom_especie_supressao,
        ind_ativo)
VALUES
('Sarcomphalus joazeiro',
TRUE);
INSERT
        INTO
        nome_popular_especie
(nom_popular_especie)
VALUES
('Juá');
INSERT
        INTO
        especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
        currval ('especie_supressao_seq')
));
-- nome cientifico: Sparattosperma sp
-- nome popular: Coroa-branca 
INSERT
        INTO
        especie_supressao
(nom_especie_supressao,
        ind_ativo)
VALUES
('Sparattosperma sp',
TRUE);
INSERT
        INTO
        nome_popular_especie
(nom_popular_especie)
VALUES
('Coroa-branca');
INSERT
        INTO
        especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
        currval ('especie_supressao_seq')
));
-- nome cientifico: Varronia globosa
-- nome popular: Maria-preta 
INSERT
        INTO
        especie_supressao
(nom_especie_supressao,
        ind_ativo)
VALUES
('Varronia globosa',
TRUE);
INSERT
        INTO
        nome_popular_especie
(nom_popular_especie)
VALUES
('Maria-preta');
INSERT
        INTO
        especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
        currval ('especie_supressao_seq')
));

-- [35865] Transferência de Pauta - Processos da COMIN

-- Processos encaminhados para a pauta de Lilia Costa Macêdo [COMIN]:

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao IN (634878,670325,390323,390324,390325,670326,623833,623828,623829,623831,623832,670330,454434,454436,
									454437,454438,670328,670327,694150,694151,694152,694153,694154,694155,694156,694157,694158,694159);
	
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(2519, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(13456, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(56130, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(64634, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(65258, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(80097, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35865', 233, FALSE, 1, NULL);
	
-- Correção dos processos que haviam sido encaminhados para a pauta de Altamirano Vaz Lordello Neto [NOUT] erroneamente:
	
	UPDATE
		controle_tramitacao
	SET
		ide_status_fluxo = 12,
		ide_pessoa_fisica = 1,
		dsc_comentario_interno = 'Solicitação de remanejamento de processo através do chamado Redmine 35865'
	WHERE
		ide_controle_tramitacao IN (703488, 703490, 703491, 703492, 703493, 703494, 703495, 703496, 703497);            

-- [35897] Alteração de Usuário

-- Alteração do usuário de Natalia:

	UPDATE
	        usuario
	SET
	        ide_perfil = 9
	WHERE
	        ide_pessoa_fisica = 1225659;
	        
	UPDATE
	        pauta
	SET
	        ide_tipo_pauta = 4,
	        ide_area = 2
	WHERE
	        ide_pauta = 2884;
	UPDATE
	        area
	SET
	        ide_pessoa_fisica = 1225659
	WHERE
	        ide_area = 2;
        
-- Alteração do usuário de Luiz:

	UPDATE
	        usuario
	SET
	        ide_perfil = 1
	WHERE
	        ide_pessoa_fisica = 6389;
	UPDATE
	        funcionario
	SET
	        ide_area = 80
	WHERE
	        ide_pessoa_fisica = 6389;
        
-- Transferência de processos de Luiz para Natalia:

	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728446;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (875, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728447;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (5891, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728435;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (18541, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728306;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (40167, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728397;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (48505, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 712192;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65018, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 710028;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65462, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 704994;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65463, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 723551;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65465, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727123;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65720, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728338;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71279, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728299;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73665, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728060;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73689, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728486;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (77444, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 708548;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (77747, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728331;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80039, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728037;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80329, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728483;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81065, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 717974;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81644, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727760;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82225, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727767;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82535, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 709063;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86034, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728116;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86632, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728005;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86995, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728300;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (87226, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727931;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (87295, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728041;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88314, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728148;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88490, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728464;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88608, 4, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728407;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (89991, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728437;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90503, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727996;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91012, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 723226;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91044, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727923;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92836, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727925;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93524, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728055;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93713, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728051;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93769, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728146;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94538, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728139;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95190, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728149;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95203, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727782;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95285, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728475;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95406, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728141;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96434, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728143;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96564, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728474;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96923, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728449;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (97646, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728440;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (97669, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728453;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (97678, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728438;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (97865, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', 2884, FALSE, 1, NULL);             

-- [35590] Erro ao tentar finalizar o CEFIR

	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural = 1217932;            
            
-- [35789] - Erro ao realizar análise técnica

	INSERT
	        INTO
	        equipe(dtc_formacao_equipe,
	        ind_ativo,
	        ide_area,
	        ide_processo)
	VALUES (now(),
	TRUE,
	76,
	92133);
	INSERT
	        INTO
	        integrante_equipe(ide_pessoa_fisica,
	        ind_lider_equipe,
	        ide_area,
	        ide_area_responsavel,
	        ide_equipe)
	VALUES (1166004,
	TRUE,
	76,
	76,
	(
	SELECT
	        currval('equipe_seq')));
	INSERT
	        INTO
	        processo_ato_integrante_equipe
	VALUES ((
	SELECT
	        currval('integrante_equipe_seq')),
	100828);

-- [35855] Solicitação de Permissão - Acesso GeoBahia

INSERT
	INTO
	perfil_autorizacao_geobahia (ide_perfil,
	ind_autorizacao)
	SELECT
	70,
	TRUE
WHERE
	NOT EXISTS (
	SELECT
		1
	FROM
		perfil_autorizacao_geobahia
	WHERE
		ide_perfil = 70);

-- [35863] Cadastro de espécies no SEIA
-- Espécie: Acosmium Diffusissimum
-- Nome popular: Lombo-preto
INSERT
        INTO
        especie_supressao (nom_especie_supressao,
        ind_ativo)
VALUES ('Acosmium Diffusissimum',
TRUE);
INSERT
        INTO
        nome_popular_especie (nom_popular_especie)
VALUES ('Lombo-preto');
INSERT
        INTO
        especie_supressao_nome_popular_especie (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES (currval ('nome_popular_especie_seq'),
currval ('especie_supressao_seq'));

-- [35875] - Transferência de Titularidade

	UPDATE
	        requerimento
	SET
	        nom_contato = 'Enel Brasil S.A'
	WHERE
	        ide_requerimento = 937995;
	UPDATE
	        requerimento_pessoa
	SET
	        ide_pessoa = 1067846
	WHERE
	        ide_requerimento = 937995
	        AND ide_tipo_pessoa_requerimento = 1;

-- [35876] Transferência de Titularidade

	UPDATE
	        requerimento
	SET
	        nom_contato = 'Enel Brasil S.A'
	WHERE
	        ide_requerimento = 936128;
	UPDATE
	        requerimento_pessoa
	SET
	        ide_pessoa = 1067846
	WHERE
	        ide_requerimento = 936128
	        AND ide_tipo_pessoa_requerimento = 1;
        
--[35850] Erro ao gerar certificado CPP

-- Corrigindo o tipo da localidade "errada":

	UPDATE
		municipio
	SET
		ide_tipo_municipio = 3
	WHERE
		ide_municipio = 10363; 
            
COMMIT;