--Scripts em caráter prioritário
--Data de geração 18/10/2023
--Versão 4.29.4

BEGIN;

-- [35617] Erro na movimentação de processos
-- 2022.001.000167/INEMA/JUR-00167

	INSERT INTO bairro (ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES (nextval('BAIRRO_IDE_BAIRRO_seq'), 1039, 'Zona Rural', FALSE, FALSE);

	UPDATE
		logradouro
	SET
		ide_bairro = currval('BAIRRO_IDE_BAIRRO_seq')
	WHERE
		ide_logradouro = 4142274;

-- 2022.001.000168/INEMA/JUR-00168

	INSERT INTO bairro (ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES (nextval('BAIRRO_IDE_BAIRRO_seq'), 547, 'Zona Rural', FALSE, FALSE);

	UPDATE
		logradouro
	SET
		ide_bairro = currval('BAIRRO_IDE_BAIRRO_seq')
	WHERE
		ide_logradouro = 4142295;

-- 2022.001.000169/INEMA/JUR-00169

	INSERT INTO bairro (ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES (nextval('BAIRRO_IDE_BAIRRO_seq'), 547, 'Zona Rural', FALSE, FALSE);

	UPDATE
		logradouro
	SET
		ide_bairro = currval('BAIRRO_IDE_BAIRRO_seq')
	WHERE
		ide_logradouro = 4142303;

-- 2022.001.000170/INEMA/JUR-00170

	INSERT INTO bairro (ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES (nextval('BAIRRO_IDE_BAIRRO_seq'), 547, 'Zona Rural', FALSE, FALSE);

	UPDATE
		logradouro
	SET
		ide_bairro = currval('BAIRRO_IDE_BAIRRO_seq')
	WHERE
		ide_logradouro = 4142309;

	INSERT INTO bairro (ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES (nextval('BAIRRO_IDE_BAIRRO_seq'), 837, 'Zona Rural', FALSE, FALSE);

	UPDATE
		logradouro
	SET
		ide_bairro = currval('BAIRRO_IDE_BAIRRO_seq')
	WHERE
		ide_logradouro = 4142310;
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 585082;
		
-- [35647] - Pagamento REPFLOR

    UPDATE
            tramitacao_requerimento
    SET
            ide_status_requerimento = 8
    WHERE
            ide_tramitacao_requerimento IN (1532956, 1539936, 1545022, 1549627, 1549539, 1547775, 1524343, 1552403);

-- [35612] - Empreendimento - CEP divergente da localidade

	UPDATE 
	        logradouro
	SET 
	        ind_origem_api = FALSE
	WHERE
	        ide_logradouro IN 
	(4142386,
	4538938,
	4473028,
	3997158,
	4338371,
	4338373);
	

-- [35648] Imóvel rural sumiu do sistema 

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1674763;
	
-- [35646] Imóvel rural sumiu do sistema 

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1926161;
	        
 
-- [35660] - Empreendimento - CEP divergente da localidade

	UPDATE 
	        logradouro
	SET 
	        ind_origem_api = FALSE
	WHERE
	        ide_logradouro IN (4447463, 3728338, 4383845);
	        
-- [35662] - Processo sumiu

    UPDATE
            controle_tramitacao
    SET
            ind_fim_da_fila = TRUE
    WHERE
            ide_controle_tramitacao = 684679;

 
-- [35664] - Empreendimento - CEP divergente da localidade
	UPDATE 
	        logradouro
	SET 
	        ind_origem_api = FALSE
	WHERE
	        ide_logradouro IN (3914932, 4479025);
	        
-- [35675] Cadastro de espécies no SEIA
-- Nome científico: Eugenia punicifolia
-- Nome popular: Goiabinha

	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Eugenia punicifolia', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Goiabinha');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));

-- [35674] Imóvel rural sumiu do sistema

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1958457;


-- [35671] Erro ao realizar análise técnica

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 1048870
	WHERE
		ide_arquivo_processo = 239099;
	
	INSERT INTO motivo_notificacao_imovel
	(ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 77071, 1048870);	  
	
--[35458/35456] - Erro ao gerar termo de compromisso.

	UPDATE
		reserva_legal
	SET
		ide_tipo_arl = 3
	WHERE
		ide_reserva_legal in (2850, 9462);

	        
COMMIT;