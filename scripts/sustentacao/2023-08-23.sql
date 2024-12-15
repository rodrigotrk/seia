--Scripts em caráter prioritário
--Data de geração 09/08/2023
--Versão 4.28.5

BEGIN;

-- [35534] Adaptação - Label Boleto

	UPDATE
		parametro_calculo
	SET
		ind_ativo = FALSE
	WHERE
		ide_parametro_calculo = 32681;
		
-- [35526] Solicitação de Permissão - Acesso GeoBahia

	UPDATE
		perfil_autorizacao_geobahia
	SET
		ind_autorizacao = TRUE
	WHERE
		ide_perfil = 30;
	
	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 63, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 63);

	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 64, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 64);
	
	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 65, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 65);

	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 66, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 66);

	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 67, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 67);

	INSERT INTO perfil_autorizacao_geobahia (ide_perfil, ind_autorizacao)
	SELECT 68, TRUE
	WHERE NOT EXISTS (SELECT 1 FROM perfil_autorizacao_geobahia WHERE ide_perfil = 68);
	
-- [35525] Imóvel Rural sumiu do sistema

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1777646;
	        
-- [35515] - Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1523549;
		
-- [35509] - Erro ao realizar a análise técnica da ASV

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 62956
	WHERE
		ide_arquivo_processo IN (231479, 230278);
	
	INSERT
		INTO
		motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao,
		ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 72953,
	62956);
	
	INSERT
		INTO
		motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao,
		ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 72179,
	62956);
	
-- [35496] Declaração de Inexigibilidade gerada com parte das informações incompleta

	UPDATE
		atividade_inexigivel
	SET
		ind_ativo = FALSE
	WHERE
		ide_atividade_inexigivel = 310;

	UPDATE
		declaracao_inexigibilidade
	SET
		ide_atividade_inexigivel = 318
	WHERE
		ide_atividade_inexigivel = 310;

-- [35516] - Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1482834;
		
-- [35538] - Processo sumiu
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 663495;

-- [35508] Empreendimento - CEP divergente da localidade

	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		ide_logradouro IN (4054917,
	4079943,
	4240474,
	4011423,
	4079837,
	4178188,
	4014613,
	3886533,
	4012045,
	4011623,
	4244247,
	4241001,
	4010240,
	4037453,
	4125803,
	4284510);		
		
-- [35542] - Solicitação de Permissão GeoBahia

	INSERT
		INTO
		perfil_autorizacao_geobahia (ide_perfil,
		ind_autorizacao)
	SELECT
		69,
		TRUE
	WHERE
		NOT EXISTS (
		SELECT
			1
		FROM
			perfil_autorizacao_geobahia
		WHERE
			ide_perfil = 69);

-- [35368] - Erro ao salvar empreendimento

	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		ide_logradouro = 4056278;
	
	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		ide_logradouro = 4129013;
	
	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		ide_logradouro = 4142550;
	
	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		ide_logradouro = 3895058;

COMMIT;