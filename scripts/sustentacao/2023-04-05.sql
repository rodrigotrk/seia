--Scripts em caráter prioritário
--Data de geração 22/03/2023
--Versão 4.27.2

BEGIN;

-- [35167] Solicitação de 'zerar' Reserva Legal
	
	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal = 5260;
-- [35165] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1448475;
-- [35164] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 623940;
-- [35155] Alteração de Usuário

	UPDATE
		usuario
	SET
		ide_perfil = 2
	WHERE
		ide_pessoa_fisica = 2766;
-- [35144] Transferência de pauta

-- Processo 2020.001.002207/INEMA/LIC-02207
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 621194;
       
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
	VALUES (59819,
        12,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35144',
        2316,
        FALSE,
        937718,
        NULL);
        
-- Processo 2020.001.005023/INEMA/LIC-05023
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 621992;
       
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
	VALUES (62816,
        4,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35144',
        2316,
        FALSE,
        937718,
        NULL); 
        
-- Processo 2022.001.002230/INEMA/LIC-02230
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 622477;
       
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
	VALUES (77333,
        12,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35144',
        2316,
        FALSE,
        937718,
        NULL); 
-- [35141] Troca de usuário interno para externo
	
	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE
	WHERE
		ide_pessoa_fisica = 594957;
-- [34920] Erro ao Cadastrar Pessoa Jurídica

ALTER TABLE auditoria.pessoa_juridica_aud ALTER COLUMN ide_natureza_juridica DROP NOT NULL;


-- [35180] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 606304;
-- [35174] Alteração de Usuário

	UPDATE
		usuario
	SET
		ide_perfil = 2,
		ind_tipo_usuario = FALSE,
		ind_excluido = TRUE
	WHERE
		ide_pessoa_fisica = 59282;
-- [35171] Retorno de status da RL

	UPDATE
		reserva_legal
	SET
		ide_status = 3
	WHERE
		ide_reserva_legal = 20009;
        
-- Inserindo a linha de tramitação no "Histórico Alteração" do CEFIR:
        
	INSERT
		INTO
		hist_historico (data_historico,
		acao_historico,
		ip_historico,
		ide_usuario)
	VALUES (now(),
		'UPD',
		'186.227.222.163',
		38518);
		
	INSERT
		INTO
		hist_valor (val_valor,
		ide_campo,
		ide_registro,
		ide_historico)
	VALUES (3,
		195,
		20009,
		currval('historico_ide_historico_seq'));
-- [35170] Erro de CEP

-- CEP '46.460-000':

	UPDATE
		logradouro
	SET
		ide_municipio = 653,
		num_cep = '46430000'
	WHERE
		ide_logradouro IN (4001595, 4010315);

-- CEP '44.780.000':

	UPDATE
		logradouro
	SET
		ide_municipio = 476,
		num_cep = '44790000'
	WHERE
		ide_logradouro = 4253952;

	UPDATE
		logradouro
	SET
		num_cep = '44790000'
	WHERE
		ide_logradouro IN (3907375,
		4282418,
		4282970,
		4283241,
		4283257,
		4283260,
		4307770,
		4310830);

COMMIT;