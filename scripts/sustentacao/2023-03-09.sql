--Scripts em caráter prioritário
--Data de geração 01/03/2023
--Versão 4.26.8

BEGIN;
-- [35086] Inclusão de espécies florestais no SEIA
-- Nome científico: Peritassa campestris
-- Nome popular: Manquinha
	INSERT INTO 
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES 
		('Peritassa campestris', TRUE);
       
	INSERT INTO 
		nome_popular_especie (nom_popular_especie)
	VALUES 
		('Manquinha');
       
	INSERT INTO 
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES 
		(currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
		

-- [35081] Processo sumiu 
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (492400, 492401);
		
-- [35078] Falha ao inserir CEP do Empreendimento 
	UPDATE
		bairro
	SET
		ide_municipio = 450
	WHERE
		ide_bairro = 44354;
	UPDATE
		logradouro
	SET
		ide_bairro = 89851
	WHERE
		ide_logradouro = 4222308;
		
-- [35072] - Retorno de Status da rl
	UPDATE
		reserva_legal
	SET
		ide_status = 3
	WHERE
		ide_reserva_legal = 650;
                
-- HIST_HISTORICO                
        INSERT INTO
                hist_historico (data_historico,
                acao_historico,
                ip_historico,
                ide_usuario)
        VALUES
        (now(),
        'UPD',
        '177.25.175.190',
        4404);
        
-- HIST_VALOR
        INSERT INTO
                hist_valor (val_valor,
                ide_campo,
                ide_registro,
                ide_historico)
        VALUES 
        (3,
        195,
        650,
        currval('historico_ide_historico_seq'));
		
-- [35069] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 573992;
       
-- HIST_HISTORICO                
        INSERT INTO
                hist_historico (data_historico,
                acao_historico,
                ip_historico,
                ide_usuario)
        VALUES
        (now(),
        'UPD',
        '138.59.184.34',
        450);
        
-- HIST_VALOR
        INSERT INTO
                hist_valor (val_valor,
                ide_campo,
                ide_registro,
                ide_historico)
        VALUES 
        (4,
        195,
        573992,
        currval('historico_ide_historico_seq'));
		
-- [35017] Transferência de pauta
-- Processo 2022.001.005182/INEMA/LIC-05182
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (579780);
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
VALUES (80498,
        12,
        3,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35017',
        233,
        TRUE,
        972702,
        NULL);
-- Processo 2021.001.006624/INEMA/LIC-06624
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao IN (592661, 592662);
INSERT INTO
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
VALUES (72049,
        12,
        3,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35017',
        233,
        TRUE,
        972702,
        NULL);
-- Processo 2022.001.004795/INEMA/LIC-04795
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao IN (597926,
	597925,
	597924,
	597923,
	597922,
	597921,
	597920,
	597919,
	597918);
INSERT INTO
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
VALUES (80097,
        12,
        3,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35017',
        233,
        FALSE,
        972702,
        NULL);
        
-- Alterando o perfil de Marcus para 'Usuário Externo', e inativando seu login:
	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE,
		ind_excluido = TRUE 
	WHERE
		ide_pessoa_fisica = 972702;

-- [35102] Solicitação de 'zerar' Reserva Legal

	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal = 29516;		
-- [35099] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1405777;		
		
-- [35098] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 614158;

-- [35077] Imóvel sumiu do sistema

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1688796;		
		
COMMIT;