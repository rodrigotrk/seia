--Scripts em caráter prioritário
--Data de geração 05/09/2023
--Versão 4.28.9

BEGIN;

-- [35577] - Processo sumiu

	UPDATE
	      controle_tramitacao
	SET
	      ind_fim_da_fila = TRUE
	WHERE
	      ide_controle_tramitacao = 608940;
                
-- [35540] Alteração de Titularidade de processo e requerimento

	UPDATE 
		requerimento
	SET
		nom_contato = 'REFINARIA DE MATARIPE S.A.'
	WHERE 
		ide_requerimento IN (48937, 1044589);

	UPDATE 
		requerimento_pessoa
	SET
		ide_pessoa = 965765
	WHERE 
		ide_requerimento IN (48937, 1044589) AND ide_tipo_pessoa_requerimento = 1;
		
-- [34949] - Retorno de status para ajuste de documentação

    UPDATE
          imovel_rural
    SET
          status_cadastro = 0
    WHERE
          ide_imovel_rural = 1217916;
          
-- [34872] - Erro na finalizaçao CEFIR

	UPDATE
	        imovel_rural
	SET
	        status_cadastro = 0
	WHERE
	        ide_imovel_rural = 1217905;
	        
--[35569] - Erro na finalização CEFIR	 

	UPDATE
		cronograma_recuperacao
	SET
		ide_app = null
	WHERE
		ide_cronograma_recuperacao = 88649;
	
	UPDATE
		cronograma_recuperacao
	SET
		ide_app = null
	WHERE
		ide_cronograma_recuperacao = 88650;
		        
-- [35567] - Requerimento sem número

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
	                                  ide_requerimento = 1261622
	                           ORDER BY
	                                  dtc_movimentacao DESC
	                           LIMIT 1
	                          )
	WHERE
	        ide_requerimento = 1261622;
	       
-- [34951] - Retorno de status para ajuste de documentação

	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural = 1217920;

COMMIT;