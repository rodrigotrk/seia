--Scripts em caráter prioritário
--Data de geração 21/02/2024
--Versão 4.30.6

BEGIN;

-- [36074] Erro no valor do boleto - Vistoria
	UPDATE
	        public.parametro_calculo
	SET
	        area_minima = NULL
	WHERE
	        ide_parametro_calculo = 32695;
-- [36047] Erro no valor do boleto - Vistoria
    UPDATE
        public.parametro_calculo
SET
        area_minima = NULL
WHERE
        ide_parametro_calculo = 32701;

-- [35709] Erro ao responder FCE - ASV
UPDATE
        fce_asv
SET
        ide_fce = NULL
WHERE
        ide_fce_asv IN (8818, 8819, 8820, 8821, 8822, 8823, 8824, 8825, 8826, 8827);
        
-- [36049] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 731164;
--[ 35885 ] Erro ao gerar boleto do requerimento

	DELETE
		FROM
			processo
		WHERE
			processo.ide_requerimento = 1241535;

-- [35602] - Erro ao responder FCE - irrigação
INSERT
        INTO
        fce_irrigacao
VALUES ((
SELECT
        MAX(ide_fce_irrigacao)+ 1
FROM
        fce_irrigacao),
1,
1,
0,
0,
71745,
FALSE);

-- [35918] - Informações do cadastro de Imóvel Rural sumiu do sistema
UPDATE
	documento_imovel_rural_posse
SET
	ide_documento_imovel_rural = NULL
WHERE
	ide_documento_imovel_rural_posse = 1129263;
	
-- [36048] - Requerimento sem número
UPDATE
                requerimento
        SET
                num_requerimento = (
                SELECT
                        CAST('2024.001.0' AS VARCHAR) || 
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
                        ide_requerimento = 1306397
                ORDER BY
                        dtc_movimentacao DESC
                LIMIT 1
                                  )
        WHERE
                ide_requerimento = 1306397;
        
        UPDATE
                requerimento
        SET
                num_requerimento = (
                SELECT
                        CAST('2024.001.0' AS VARCHAR) || 
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
                        ide_requerimento = 1306170
                ORDER BY
                        dtc_movimentacao DESC
                LIMIT 1
                                  )
        WHERE
                ide_requerimento = 1306170;

-- [35966] Erro ao realizar análise técnica

	DELETE
	FROM
	        fce_barragem
	WHERE
	        ide_fce_barragem = 236;
	DELETE
	FROM
	        fce
	WHERE
	        ide_fce = 47153;
	       
	UPDATE
	        fce
	SET
	        ide_origem_fce = 2,
	        ide_analise_tecnica = 23603,
	        ind_concluido = NULL,
	        ide_processo_reenquadramento = 1214,
	        ide_pessoa_fisica = 155660
	WHERE
	        ide_fce IN (47283, 47152, 46974, 46975, 47154);                
-- [35900] Correção das informações no resumo do requerimento

	UPDATE
	        solicitacao_administrativo
	SET
	        num_portaria = 27.672
	WHERE
	        ide_solicitacao_administrativo = 6145;                
                
COMMIT;