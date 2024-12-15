--Scripts em caráter prioritário
--Data de geração 07/02/2024
--Versão 4.30.5

BEGIN;

--[36150] Requerimento sem número
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
                                  ide_requerimento = 1312201
                           ORDER BY
                                  dtc_movimentacao DESC
                           LIMIT 1
                          )
WHERE
        ide_requerimento = 1312201;
        
-- [36129] - Retorno de status da Reserva Legal
UPDATE
	reserva_legal
SET
	ide_status = 3,
	ide_dado_origem = NULL
WHERE
	ide_reserva_legal = 15311;
	
INSERT INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
'UPD',
'186.244.93.45',
1);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
195,
15311,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3,
	ide_dado_origem = NULL
WHERE
	ide_reserva_legal = 15258;
	
INSERT INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
'UPD',
'186.244.93.45',
1);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
195,
15258,
currval('historico_ide_historico_seq'));	

UPDATE
	reserva_legal
SET
	ide_status = 3,
	ide_dado_origem = NULL
WHERE
	ide_reserva_legal = 11630;
	
INSERT INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
'UPD',
'186.244.93.45',
1);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
195,
11630,
currval('historico_ide_historico_seq'));

-- [36112] - Zerar RL
UPDATE
        reserva_legal
SET
        ide_imovel_rural = NULL
WHERE
        ide_reserva_legal = 4236;

-- [36108] - Requerimento sem número
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
                                  ide_requerimento = 1317092
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1317092;
        
-- [36105] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1493763, 1491244);

-- [36099] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1633981, 1634570, 1617850);
    
-- [36098] Pagamento REPFLOR        
    UPDATE
            tramitacao_requerimento
    SET
            ide_status_requerimento = 8
    WHERE
            ide_tramitacao_requerimento = 1490854;
            
 
-- [36095] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1430469);
    
 
-- [36094] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1419100);
    
-- [36090] - Correção imovel notificação

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 1284158
	WHERE
		ide_arquivo_processo = 266377;
		
-- [35730] - Retorno de status
UPDATE
        imovel_rural
SET
        status_cadastro = 3
WHERE
        ide_imovel_rural = 19447;
        
--[36133] Imóvel Rural sumiu do sistema
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
1220776,
1285220,
now(),
FALSE,
1,
NULL,
NULL,
NULL);

UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 1220776
WHERE
        ide_imovel_rural = 1285220

-- [36125] Transferência de Pauta

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 749750;
	        
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93887, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36125', 205, FALSE, 1, NULL);        
        
COMMIT;