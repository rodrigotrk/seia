--Scripts em caráter prioritário
--Data de geração 07/08/2024
--Versão 4.33.3

BEGIN;

--[36573] - Alteração de Usuário

UPDATE
	usuario
SET
	ide_perfil = 4,
	ind_excluido = FALSE
WHERE
	ide_pessoa_fisica = 33997;

-- [36601] Erro ao anexar shapes a notificação
UPDATE
        motivo_notificacao_imovel
SET
        ide_imovel = 1329950
WHERE
        ide_motivo_notificacao_imovel = 2765;
        
-- [36581] Pagamento REPFLOR

	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1334121, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1339415, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1345787, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1343698, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1349708, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1349956, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1352161, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1348168, now(), 1);
	INSERT INTO tramitacao_requerimento (ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES (8,1352155, now(), 1);
	
--[36580] - Bloqueio de Edição de CEFIR - Fazenda Missio

UPDATE 
	imovel_rural 
SET 
	ind_bloqueio_limite = true
WHERE 
	ide_imovel_rural = 15980;
	
-- [36535] - Erro ao finalizar requerimento
UPDATE
	requerimento
SET
	num_requerimento = (
		SELECT
			CAST(
				'2024.001.0' AS VARCHAR
			) || 
                                  CAST(
				(
					CAST(
						split_part(
							split_part(
								(
									SELECT
										r.num_requerimento
									FROM
										requerimento r
									WHERE
										r.num_requerimento IS NOT NULL
									ORDER BY
										r.num_requerimento DESC
									LIMIT 1
								),
								'.',
								3
							),
							'/',
							1
						) AS INTEGER
					) + 1
				) AS VARCHAR
			) || 
                                  CAST(
				'/INEMA/REQ' AS VARCHAR
			) AS NUM_REQUERIMENTO
	),
	dtc_finalizacao = (
		SELECT
			dtc_movimentacao
		FROM
			tramitacao_requerimento
		WHERE
			ide_requerimento = 1350589
		ORDER BY
			dtc_movimentacao DESC
		LIMIT 1
	)
WHERE
	ide_requerimento = 1350589;
	
--[36424] Erro no CEP na solicitação do ato florestal

UPDATE 
	municipio
SET 
	num_cep = 42816200,
	ind_estado_emergencia = FALSE,
	ind_bloqueio_dqc = FALSE,
	ind_excluido = FALSE
WHERE 
	ide_municipio = 985;
	
-- [36375] - Duplicidade no restaurador
UPDATE
        municipio
SET
        ind_excluido = TRUE
WHERE
        ide_municipio = 10363;
        
-- [36602] - Solicitação de remanejamento de processos SEIA
UPDATE
        usuario
SET
        ide_perfil = 2, ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 2403;
        
        
-- script de tramitação
        
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 784689;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (78596, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 757483;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (100839, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 757495;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (101744, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775079;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96314, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 782078;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (101288, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 767157;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (85256, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 735722;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97581, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 735731;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97702, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 768821;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (98716, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 760332;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94148, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 779645;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (72049, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 735629;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96739, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 735559;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94814, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 785390;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86709, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 767346;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (84704, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775997;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91523, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 777080;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91553, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 781874;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87726, 12, (3), now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine #36602', (233), FALSE, 1, NULL);

-- [36622] - Processo sumiu do sistema
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 786942;
-- [36618] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1098611;
        
-- [36613] - Retorno de status da RL

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
663441,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 663441;

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
873550,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 873550;
	
-- [36610] Cadastro de espécies no SEIA
-- Espécie: Aralia sp
-- Nome popular: Aralia
INSERT
        INTO
        especie_supressao (nom_especie_supressao,
        ind_ativo)
VALUES ('Aralia sp',
TRUE);
INSERT
        INTO
        nome_popular_especie (nom_popular_especie)
VALUES ('Arália');
INSERT
        INTO
        especie_supressao_nome_popular_especie (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES (currval ('nome_popular_especie_seq'),
currval ('especie_supressao_seq'));
-- Espécie: Kielmera neglecta
-- Nome popular: Caga matéria
INSERT
        INTO
        especie_supressao (nom_especie_supressao,
        ind_ativo)
VALUES ('Kielmera neglecta',
TRUE);
INSERT
        INTO
        nome_popular_especie (nom_popular_especie)
VALUES ('Caga matéria');
INSERT
        INTO
        especie_supressao_nome_popular_especie (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES (currval ('nome_popular_especie_seq'),
currval ('especie_supressao_seq'));


-- [36603] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 23939;
        
-- [36597] Erro na etapa 6

	UPDATE
		fauna
	SET
		ind_excluido = TRUE
	WHERE
		ide_fauna = 85595;
		
-- [36590] - [PROCESSO] Transferência de Titularidade    
UPDATE
	requerimento_pessoa
SET
	ide_pessoa = 1041907
WHERE
	ide_requerimento = 14254
	AND ide_pessoa IN ('1724', '5462');

UPDATE
	requerimento
SET
	nom_contato = 'Energizzi Energias do Brasil'
WHERE
	ide_requerimento = 14254;
	
-- [36562] Erro ao enquadrar requerimento

	UPDATE
		enquadramento
	SET
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 176046;

COMMIT;