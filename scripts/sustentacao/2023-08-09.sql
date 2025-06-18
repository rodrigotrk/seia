--Scripts em caráter prioritário
--Data de geração 12/07/2023
--Versão 4.28.2

BEGIN;
		

-- [35502] Imóvel rural sumiu do sistema 
       
	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1926161;
	        
-- [35500] Erro ao tentar realizar alterações
	
	UPDATE
	        localizacao_geografica
	SET
	        ide_classificacao_secao = 2
	WHERE
	        ide_localizacao_geografica IN (511168, 511174);
	       
-- [35112] Erro ao editar Reserva legal

	UPDATE
	        localizacao_geografica
	SET
	        ide_classificacao_secao = 2
	WHERE
	        ide_localizacao_geografica = 3066926;

		
-- [35491] - Processo sumiu

	UPDATE
	                controle_tramitacao
	SET
	                ind_fim_da_fila = TRUE
	WHERE
	                ide_controle_tramitacao = 580041;

-- [35490] - Processo sumiu

	UPDATE
	                controle_tramitacao
	SET
	                ind_fim_da_fila = TRUE
	WHERE
	                ide_controle_tramitacao = 607965;

-- [35512] Atualização de taxas - Reposição Florestal

	UPDATE
		parametro_calculo
	SET
		ind_ativo = FALSE
	WHERE
		ide_parametro_calculo = 32545;

	INSERT INTO parametro_calculo
	(ide_parametro_calculo, ide_ato_ambiental, ide_tipologia, ide_tipo_finalidade_uso_agua, valor_taxa, area_minima, area_maxima, ide_classe, num_ufir, dtc_criacao, ind_boleto, ind_ativo, fator_multiplicador, vazao_minima, vazao_maxima, ide_tipo_criadouro_fauna, ide_bioma)
	VALUES(nextval('PARAMETRO_CALCULO_IDE_PARAMETRO_CALCULO_seq'), 128, NULL, NULL, 31.83, NULL, NULL, NULL, NULL, now(), FALSE, TRUE, 1.20, NULL, NULL, NULL, NULL);
	
--[35484] Imóvel Rural sumiu do sistema
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
1162904,
679251,
now(),
FALSE,
2,
NULL,
NULL,
NULL);

UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 1162904
WHERE
        ide_imovel_rural = 679251;

-- [33042] - Correção acentuação caminho de arquivo

	UPDATE
	        arquivo_processo
	SET
	        dsc_caminho_arquivo = '/opt/ARQUIVOS/PROCESSO/PT OUTORGA LI SES IRECE.pdf'
	WHERE
	        ide_arquivo_processo = 13258;

-- [34795] - Correção acentuação caminho de arquivo
	       
	UPDATE
	        arquivo_processo
	SET
	        dsc_caminho_arquivo = '/opt/ARQUIVOS/PROCESSO/PT_ECOVILLLE_MUCUGE.pdf'
	WHERE
	        ide_arquivo_processo = 9761;
	       
-- [35306] Erro na finalização do CEFIR

	UPDATE
		requerimento
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 935285;

	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 935285;
	
-- [35333] Processo sumiu do sistema

	UPDATE 
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE 
		ide_controle_tramitacao = 643421;
	
-- [35387] - Correção erro análise técnica

	UPDATE
		fce
	SET
		ide_origem_fce = 2
	WHERE
		ide_fce = 31089;
	                
	
-- [35498] Erro na etapa 6 do requerimento

	UPDATE
	        fauna 
	SET
	        ind_excluido  = TRUE 
	WHERE
	        ide_fauna = 84254;
COMMIT;