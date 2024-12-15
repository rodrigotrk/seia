--Scripts em caráter prioritário
--Data de geração 07/02/2024
--Versão 4.30.5

BEGIN;

-- [35733] - Erro na finalização CEFIR

	DELETE
	FROM
	        cronograma_recuperacao
	WHERE
	        ide_cronograma_recuperacao = 55146;
        
-- [35614] - Erro na finalização do CEFIR

	UPDATE
	        requerimento
	SET
	        ind_excluido = TRUE
	WHERE
	        ide_requerimento = 1265448;
	UPDATE
	        requerimento_imovel
	SET
	        ind_excluido = TRUE
	WHERE
	        ide_requerimento = 1265448;
        
-- [35848] - Erro na finalizaçao CEFIR

	UPDATE
		requerimento
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 1291039;
		
	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento_imovel = 1113133;
		
-- [36008] - Imóvel rural sumiu do sistema

	INSERT
	        INTO
	        public.pessoa_imovel
	        (ide_pessoa_imovel,
	        ide_pessoa,
	        ide_imovel,
	        dtc_criacao,
	        ind_excluido,
	        ide_tipo_vinculo_imovel,
	        dtc_exclusao,
	        ide_tipo_vinculo_pct,
	        dsc_tipo_vinculo_pct_outros)
	VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'::text::regclass),
	1235015,
	436716,
	now(),
	FALSE,
	1,
	NULL,
	NULL,
	NULL);
	UPDATE
	        imovel_rural
	SET
	        ide_requerente_cadastro = 1235015
	WHERE
	        ide_imovel_rural = 436716;
        
-- [36032] Processo sumiu da pauta técnica

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 687276;
        
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(84943, 8, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36032', 115, FALSE, 1, NULL);

-- [36035] - Imóvel rural sumiu do sistema

	UPDATE
		imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_imovel = 91756;
		
-- [36039] - Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 662126;
		
COMMIT;