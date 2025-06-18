--Scripts em caráter prioritário
--Data de geração 28/06/2023
--Versão 4.20.0

BEGIN;

-- [35424] Retorno de Status
	INSERT
	        INTO
	        tramitacao_requerimento(ide_status_requerimento,
	        ide_requerimento,
	        dtc_movimentacao,
	        ide_pessoa)
	values
	        (1,
	        1012886,
	        now(),
	        5919);
        
-- [35401] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1488378;
		
-- [35338] - Requerimento sem número

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
					r.ide_requerimento DESC
				   LIMIT 1),
				  '.',
				   3),
				   '/',
				   1) AS INTEGER) + 4 ) AS VARCHAR) || 
			          CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
			   ),
		dtc_finalizacao = (
				   SELECT
					  dtc_movimentacao
				   FROM
					  tramitacao_requerimento
				   WHERE
					  ide_requerimento = 1220389
				   ORDER BY
					  dtc_movimentacao DESC
				   LIMIT 1
				  )
	 WHERE
		ide_requerimento = 1220389;
		
-- [34890] Erro na finalizaçao CEFIR

	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural = 1208155;

--- [34685] Erro na finalização do CEFIR

	UPDATE
	        requerimento
	SET
	        ind_excluido = TRUE
	WHERE
	        ide_requerimento = 1165548;
	    
	UPDATE
	        requerimento_imovel
	SET
	        ind_excluido = TRUE
	WHERE
	        ide_requerimento = 1165548;

-- [35443] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 579138;	        
-- [35438] - Erro ao remover pendencia do imóvel

	UPDATE
		historico_suspensao_cadastro
	SET
		dtc_retirada_suspensao = NULL
	WHERE
		ide_suspensao_cadastro = 178;
-- [35162] Erro na etapa 6 do requerimento

	UPDATE
		fauna
	SET
		ind_excluido = TRUE
	WHERE
		ide_fauna = 83738;	        
-- [35423] - Erro ao solicitar DTRP
	UPDATE
	        bairro
	SET
	        ide_municipio = 691
	WHERE
	        ide_bairro = 807530;
	        
-- [34736] Erro ao salvar alteração no cadastro de empreendimento

	INSERT INTO imovel
				(ide_imovel, ide_tipo_imovel, ide_endereco, dtc_criacao, ind_excluido, dtc_exclusao, ide_usuario_exclusao)
	VALUES(nextval('IMOVEL_IDE_IMOVEL_seq'), 3, 38606, now(), FALSE, NULL, NULL);

	INSERT INTO imovel_empreendimento
				(ide_imovel, ide_empreendimento, dtc_associacao)
	VALUES (currval('IMOVEL_IDE_IMOVEL_seq'), 11185, now());

	UPDATE
		empreendimento
	SET
		ind_cessionario = TRUE
	WHERE
		ide_empreendimento = 11185;	        
	        
-- [35419] - Correção relatório DTRP
	
	UPDATE
		declaracao_transporte
	SET
		ind_excluido = TRUE
	WHERE
		ide_declaracao_transporte = 5631;
	        
	        
-- [35382] - Requerimento sem número

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
					  ide_requerimento = 1233585
				   ORDER BY
					  dtc_movimentacao DESC
				   LIMIT 1
				  )
	WHERE
		ide_requerimento = 1233585;			
	        
	        
-- [35340] - Remoção endereço inválido

	UPDATE
		endereco_pessoa
	SET
		ide_pessoa = 1
	WHERE
		ide_endereco_pessoa = 35220;      
	        
-- [34787] Erro ao salvar dados básicos do imóvel

-- Deleção de dados de "outros_passivos_ambientais" idênticos e que estavam duplicados
	DELETE
	FROM
		cronograma_etapa
	WHERE
		ide_cronograma_etapa = 793674;

	DELETE
	FROM
		cronograma_recuperacao
	WHERE
		ide_cronograma_recuperacao = 147946;

	DELETE
	FROM
		outros_passivos_ambientais
	WHERE
		ide_outros_passivos_ambientais = 1648;

COMMIT;