--Scripts em caráter prioritário
--Data de geração 18/10/2023
--Versão 4.29.3

BEGIN;

--[35483] - Erro nos dados deRLAC

-- atribuindo uma lac ao requerimento
	INSERT INTO lac
	(ide_lac, ide_requerimento, num_certificado, dtc_criacao, ide_documento_obrigatorio)
	VALUES(nextval('LAC_IDE_LAC_seq'), 979256, NULL, now(), 1263);

-- associando a lac transporte
	INSERT INTO lac_transporte
	(ide_lac_transporte, ide_lac, ide_localizacao_geografica, ind_base_operacional, ind_produto, ind_atendimento_emergencia_empresa, ind_atendimento_emergencia_terceiro, ind_residuo, ide_pessoa_juridica)
	VALUES(nextval('lac_transporte_ide_lac_transporte_seq'), currval('LAC_IDE_LAC_seq'), NULL, false, true, false, true, false, 19111);

-- inserindo os produtos e suas respectivas classes
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 507, 5000.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 536, 3000.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 581, 6000.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 598, 500.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 627, 2000.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 1014, 1500.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 1931, 6000.00);
	INSERT INTO lac_transporte_produto
	(ide_lac_transporte, ide_produto, qtd_media_transporte_anual)
	VALUES(currval('lac_transporte_ide_lac_transporte_seq'), 2139, 33124.80);

-- [35565] Erro ao realizar a análise técnica da ASV

-- 2022.001.003995/INEMA/LIC-03995

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 62411
	WHERE
		ide_arquivo_processo IN (230098, 231480);

	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 72155, 62411);

	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 72957, 62411);

-- 2018.001.003031/INEMA/LIC-03031

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 71953
	WHERE
		ide_arquivo_processo = 240500;
	
	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 73058, 71953);
	
-- [35568] Erro ao realizar a análise técnica da ASV

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 1132758
	WHERE
		ide_arquivo_processo = 245879;
	
	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 79413, 1132758);
	
--[35619] Imóvel Rural sumiu do sistema

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
	58253,
	773852,
	now(),
	FALSE,
	1,
	NULL,
	NULL,
	NULL);
	UPDATE
	        imovel_rural
	SET
	        ide_requerente_cadastro = 58253
	WHERE
	        ide_imovel_rural = 773852;
	        
--[35624] Imóvel Rural sumiu do sistema   

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
	1182734,
	1221989,
	now(),
	FALSE,
	1,
	NULL,
	NULL,
	NULL);
	UPDATE
	               imovel_rural 
	        SET
	                ide_requerente_cadastro = 1182734
	        WHERE
	        ide_imovel_rural = 1221989;

-- [35628] Erro ao realizar análise técnica
-- Associando o imóvel válido ao empreendimento:
	INSERT INTO imovel_empreendimento (ide_imovel, ide_empreendimento, dtc_associacao)
	VALUES(1118318, 148779, now());
	
-- Vinculando o imóvel aos shapes:
	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 1118318
	WHERE
	        ide_arquivo_processo IN (251208, 251810);
	        
-- Vinculando a notificação, o motivo e o imóvel:        
	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 83781, 1118318);
	INSERT INTO motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 85159, 1118318);
	
-- [35630] - Processo sumiu
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = FALSE
        WHERE
                ide_controle_tramitacao = 614703;
        
--status "encaminhado para o gestor, usuário "maria.lins" 
       
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
	         (86181,
	12,
	1,
	now(),
	TRUE,
	NULL,
	NULL,
	156,
	FALSE,
	399,
	NULL);
	
-- [35641] - Retorno de status da RL
-- Fazenda Genebra/Santa Rosa
	UPDATE
	        reserva_legal
	SET
	        ide_status = 3
	WHERE
	        ide_reserva_legal = 8680;
        
-- Inserindo a linha de tramitação no "Histórico Alteração"
	INSERT
	        INTO
	        hist_historico (data_historico,
	        acao_historico,
	        ip_historico,
	        ide_usuario)
	VALUES (now(),
	'UPD',
	'201.50.135.226',
	13249);
	INSERT
	        INTO
	        hist_valor (val_valor,
	        ide_campo,
	        ide_registro,
	        ide_historico)
	VALUES (3,
	195,
	8680,
	currval('historico_ide_historico_seq'));
	
-- [35603] - Erro ao gerar Nº do requerimento

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
	                                  ide_requerimento = 1269126
	                           ORDER BY
	                                  dtc_movimentacao DESC
	                           LIMIT 1
	                          )
	WHERE
	        ide_requerimento = 1269126;
	        
-- [35603] - Erro ao gerar Nº do requerimento

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
	                                  ide_requerimento = 1269126
	                           ORDER BY
	                                  dtc_movimentacao DESC
	                           LIMIT 1
	                          )
	WHERE
	        ide_requerimento = 1269126;
	        
-- [35598] Retorno de status da RL
-- Imóvel I: Fazenda Poções
	UPDATE
	        reserva_legal
	SET
	        ide_status = 3
	WHERE
	        ide_reserva_legal = 859444;
	        
-- Inserindo a linha de tramitação no "Histórico Alteração"
	INSERT
	        INTO
	        hist_historico (data_historico,
	        acao_historico,
	        ip_historico,
	        ide_usuario)
	VALUES (now(),
	'UPD',
	'10.90.13.84',
	8517);
	INSERT
	        INTO
	        hist_valor (val_valor,
	        ide_campo,
	        ide_registro,
	        ide_historico)
	VALUES (3,
	195,
	859444,
	currval('historico_ide_historico_seq'));
	
-- Imóvel II: Fazenda Baixinha
	UPDATE
	        reserva_legal
	SET
	        ide_status = 3
	WHERE
	        ide_reserva_legal = 859439;
	        
-- Inserindo a linha de tramitação no "Histórico Alteração"
	INSERT
	        INTO
	        hist_historico (data_historico,
	        acao_historico,
	        ip_historico,
	        ide_usuario)
	VALUES (now(),
	'UPD',
	'10.90.13.84',
	8517);	UPDATE
		        reserva_legal
		SET
		        ide_status = 3
		WHERE
		        ide_reserva_legal = 859439;
	INSERT
	        INTO
	        hist_valor (val_valor,
	        ide_campo,
	        ide_registro,
	        ide_historico)
	VALUES (3,
	195,
	859439,
	currval('historico_ide_historico_seq'));
	
-- [35625] Ajustes no Perfil "Consulta Externa"

	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 73, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 73);

-- [35623] - Erro ao anexar shapes a notificação
-- Implantado dia 19/10/2023
	INSERT
		INTO
		public.motivo_notificacao_imovel
	
	(ide_motivo_notificacao_imovel,
		ide_notificacao_motivo_notificacao,
		ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'::text::regclass),
	85650,
	718344);

COMMIT;