--Scripts em caráter prioritário
--Data de geração 12/07/2023
--Versão 4.28.2

BEGIN;

-- [35471] - Requerimento sem número

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
	                                  ide_requerimento = 1234706
	                           ORDER BY
	                                  dtc_movimentacao DESC
	                           LIMIT 1
	                          )
	WHERE
	        ide_requerimento = 1234706;
	        
-- [35463] - Processo sumiu

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 652215
	        
-- [35427] Imóvel Rural sumiu do sistema

	INSERT INTO bairro
	(ide_bairro, ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api)
	VALUES(nextval('BAIRRO_IDE_BAIRRO_seq'), 994, 'ZONA RURAL', FALSE, FALSE);

	INSERT INTO logradouro
	(ide_logradouro, ide_tipo_logradouro, ide_bairro, nom_logradouro, ind_origem_correio, ide_municipio, num_cep, ind_origem_api)
	VALUES(nextval('LOGRADOURO_IDE_LOGRADOURO_seq'), 3, currval('BAIRRO_IDE_BAIRRO_seq'), 'POV. DE PEDRA BRANCA', FALSE, 994, 44890000, NULL);

	UPDATE
		endereco
	SET
		des_complemento = '2,5kM DO CENTRO DE CANARANA',
		ide_logradouro = currval('LOGRADOURO_IDE_LOGRADOURO_seq')
	WHERE
		ide_endereco = 2550319;
		
-- [35252] Erro ao realizar análise técnica

	ALTER SEQUENCE fce_intervencao_barragem_ide_fce_intervencao_barragem_seq RESTART 366;
	
-- [35188] Requerimento sem número do "Processo Formado"

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 4
	WHERE
		ide_tramitacao_requerimento = 1461735;
		
-- [35185] - Erro ao gerar DAE

	UPDATE 
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento = 1466525;
	        
-- [35114] - Processo formado

	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 4
	WHERE
	        ide_tramitacao_requerimento = 840209;
	        
--[34647] - Boleto com valor duvergente.

	UPDATE
		public.outorga_localizacao_geografica_finalidade
	SET
		ind_excluido = true
	WHERE
		ide_outorga_localizacao_geografica_finalidade = 70331;
		
		
--[35482] - Todos os requerimentos sem numero

	CREATE OR REPLACE FUNCTION resolver() 
	RETURNS void
	LANGUAGE plpgsql 
	AS $function$
	    DECLARE
	    t record;
	BEGIN
	    FOR t IN 
	        (
	           SELECT r.ide_requerimento
	                    FROM tramitacao_requerimento tr 
	                    INNER  JOIN requerimento r  
	                    ON r.ide_requerimento = tr.ide_requerimento
	                    WHERE tr.ide_status_requerimento != 14 
	                    AND tr.ide_status_requerimento != 9
	                    AND r.num_requerimento IS NULL
	                    AND r.dtc_finalizacao IS NULL
	                    ORDER BY r.num_requerimento DESC
	        )
	           
	        LOOP 
	                           UPDATE
	                                        requerimento r1
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
	                                        dtc_finalizacao = (SELECT dtc_movimentacao
	                                                       FROM tramitacao_requerimento tr2 
	                                                       WHERE tr2.ide_requerimento = t.ide_requerimento
	                                                       ORDER BY dtc_movimentacao 
	                                                       DESC LIMIT 1)
	                                WHERE
	                                        r1.ide_requerimento = (SELECT r.ide_requerimento
	                                                                    FROM tramitacao_requerimento tr 
	                                                                    INNER  JOIN requerimento r  
	                                                                    ON r.ide_requerimento = tr.ide_requerimento
	                                                                    WHERE tr.ide_status_requerimento != 14 
	                                                                    AND tr.ide_status_requerimento != 9
	                                                                    AND r.num_requerimento IS NULL
	                                                                    AND r.dtc_finalizacao IS NULL
	                                                                    ORDER BY r.num_requerimento DESC
	                                                                           LIMIT 1);
	                                        END LOOP;
	                                    END;
	$function$
	;
	SELECT * FROM resolver();
	DROP FUNCTION resolver();

-- [35478] Retorno de status da RL
 
        UPDATE
                reserva_legal
        SET
                ide_status = 4
        WHERE
                ide_reserva_legal = 4535;
       
-- HIST_HISTORICO                
        INSERT
                INTO
                hist_historico (data_historico,
                acao_historico,
                ip_historico,
                ide_usuario)
        VALUES
        (now(),
        'UPD',
        '172.16.10.81',
        2406);
        
-- HIST_VALOR
        INSERT
                INTO
                hist_valor (val_valor,
                ide_campo,
                ide_registro,
                ide_historico)
        VALUES 
        (4,
        195,
        4535,
        currval('historico_ide_historico_seq'));
        
-- [35475] Reserva Legal

	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal = 10288;
		
-- [35474] Cadastro de espécies no SEIA
	-- Nome científico: Handroanthus crhysotricha
	-- Nome popular: Ipê amarelo
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Handroanthus crhysotricha', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Ipê amarelo');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Paraptadenia rigida
	-- Nome popular: Angico-cangalha
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Paraptadenia rigida', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Angico-cangalha');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Protium aracouchini
	-- Nome popular: Amescla
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Protium aracouchini', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Amescla');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Senna alata
	-- Nome popular: Fedegoso
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Senna alata', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Fedegoso');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Sparattanthelium botocudorum
	-- Nome popular: Agasalho-de-anum
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Sparattanthelium botocudorum', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Agasalho-de-anum');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Solanum granulosoleprosum
	-- Nome popular: Fumo-bravo
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Solanum granulosoleprosum', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Fumo-bravo');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Heliocarpus popayanensis
	-- Nome popular: Algodoeiro
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Heliocarpus popayanensis', TRUE);
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Algodoeiro');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	-- Nome científico: Albizia niopoides
	-- Nome popular: Farinha-seca
	UPDATE
	        especie_supressao
	SET
	        nom_especie_supressao = 'Albizia niopoides'
	WHERE
	        ide_especie_supressao = 202;
	       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Farinha-seca');
	       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), 202);
	
-- [35468] - Requerimento sem número

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
	                                  ide_requerimento = 1232447
	                           ORDER BY
	                                  dtc_movimentacao DESC
	                           LIMIT 1
	                          )
	WHERE
	        ide_requerimento = 1232447;
	        
-- [35430] - CEP divergente da localidade

	UPDATE
		logradouro
	SET
		ind_origem_api = FALSE
	WHERE
		num_cep = '48330000'
		AND ide_municipio != 868;
	
-- [35412] - Correção ind_api de CEP

	UPDATE
	        logradouro
	SET
	        ind_origem_api = FALSE
	WHERE
	        num_cep = '48590970'
	AND 	ide_municipio != 700
	OR 		ide_logradouro = 4296150;
	
-- [35224] Erro ao carregar o FCE.
	
	UPDATE
		fce
	SET
		ide_origem_fce = 3,
		ide_processo_reenquadramento = 1137
	WHERE
		ide_fce = 59965;

-- [34531] - Retorno de status

	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 4
	WHERE
	        ide_tramitacao_requerimento = 842965;
		
COMMIT;