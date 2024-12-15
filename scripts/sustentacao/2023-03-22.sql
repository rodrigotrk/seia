--Scripts em caráter prioritário
--Data de geração 08/03/2023
--Versão 4.27.1

BEGIN;

-- [35133] Imóvel Rural sumiu do sistema

-- Vinculando o imóvel ao requerente atual:

	INSERT INTO pessoa_imovel (ide_pessoa_imovel,
							   ide_pessoa,
							   ide_imovel,
							   dtc_criacao,
							   ind_excluido,
							   ide_tipo_vinculo_imovel,
							   dtc_exclusao,
							   ide_tipo_vinculo_pct,
							   dsc_tipo_vinculo_pct_outros)
	SELECT nextval('pessoa_imovel_ide_pessoa_imovel_seq'),
					1107496,
					354229,
					now(),
					FALSE,
					2,
					NULL,
					NULL,
					NULL
	WHERE NOT EXISTS (SELECT 1 FROM pessoa_imovel WHERE ide_pessoa = 1107496);
	
-- Atualizando o cadastro do imóvel ao novo vínculo:

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 1107496
	WHERE
		ide_imovel_rural = 354229;

-- [35132] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 3
	WHERE
		ide_reserva_legal = 817314;

-- Inserindo a linha de tramitação no "Histórico Alteração" do CEFIR:
        
	INSERT
		INTO
		hist_historico (data_historico,
		acao_historico,
		ip_historico,
		ide_usuario)
	VALUES (now(),
		'UPD',
		'172.16.10.72',
		2903);

	INSERT
		INTO
		hist_valor (val_valor,
		ide_campo,
		ide_registro,
		ide_historico)
	VALUES (3,
		195,
		817314,
		currval('historico_ide_historico_seq'));
-- [35130] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 544820;
-- [35121] Transferência de pauta

-- Processo 2022.001.008673/INEMA/LIC-08673
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 615946;

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
	VALUES (84190,
		12,
		50,
		now(),
		TRUE,
		NULL, 
		'Solicitação de remanejamento de processo através do chamado Redmine 35121',
		408,
		FALSE,
		1026761,
		NULL);
		
-- Desativando o perfil de Fábio Bandeira:
	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE,
		ind_excluido = TRUE
	WHERE
		ide_pessoa_fisica = 1026761;

-- Alterando o perfil de Fábio Rodrigues para "Coordenador(a)":
	UPDATE
		usuario	
	SET
		ide_perfil = 4
	WHERE
		ide_pessoa_fisica = 7171;

	UPDATE
		area
	SET
		ide_pessoa_fisica = 7171
	WHERE
		ide_area = 50;
	
	UPDATE
		pauta
	SET
		ide_area = 50,
		ide_tipo_pauta = 2
	WHERE
		ide_pessoa_fisica = 7171;

-- [35120] Solicitação de 'zerar' Reserva Legal

	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal IN (10303, 10307);
-- [35109] Transferência de pauta 

-- Processo 2022.001.007151/INEMA/LIC-07151

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 618965;

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
	VALUES (82619,
        4,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35109',
        2316,
        FALSE,
        937718,
        NULL);
        
-- Processo 2018.001.002955/INEMA/LIC-02955

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 620084;

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
	VALUES (31191,
        4,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35109',
        2316,
        FALSE,
        937718,
        NULL);
        
-- Processo 2021.001.005204/INEMA/LIC-05204

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 614808;

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
	VALUES (70547,
        4,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35109',
        2316,
        FALSE,
        937718,
        NULL);
        
-- Processo 2021.001.007189/INEMA/LIC-07189
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 617252;

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
	VALUES (72627,
        4,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35109',
        2316,
        FALSE,
        937718,
        NULL);
        
-- Processo 2022.001.009057/INEMA/LIC-09057

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 614762;
	
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
	VALUES (84586,
        5,
        42,
        now(),
        TRUE,
        NULL, 
        'Solicitação de remanejamento de processo através do chamado Redmine 35109',
        112,
        FALSE,
        937718,
        NULL);
        
-- Desativando o perfil de Rafael:

	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE,
		ide_perfil = 2,
		ind_excluido = TRUE
	WHERE
		ide_pessoa_fisica = 937718;
		
-- Alterando o perfil de Alvaro para "Coordenador(a)":
        
	UPDATE
		usuario
	SET
		ide_perfil = 4
	WHERE
		ide_pessoa_fisica = 596786;

	UPDATE
		area
	SET
		ide_pessoa_fisica = 596786
	WHERE
		ide_area = 42;

	UPDATE
		pauta
	SET
		ide_area = 42,
		ide_tipo_pauta = 2
	WHERE
		ide_pessoa_fisica = 596786;
	
	UPDATE
		pauta
	SET
		ide_area = NULL
	WHERE
		ide_pessoa_fisica IN (7118, 937718);

-- [35139] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1451798;		
-- [35140] Solicitação de 'zerar' Reserva Legal

	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal = 700389;		
		
-- [34751] - Imóvel Rural sumiu do sistema 

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 533837;

	UPDATE
		pessoa_imovel
	SET 
		ind_excluido = TRUE 
	WHERE 
		ide_pessoa_imovel = 1798452;

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 298271
	WHERE
		ide_imovel_rural = 356435;

COMMIT;