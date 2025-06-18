BEGIN;

UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2020.001.0' AS VARCHAR) || 
                CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 6 ) AS VARCHAR) || 
                CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 978959 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
        ide_requerimento = 978959;
       
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2021.001.0' AS VARCHAR) || 
              	CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 5 ) AS VARCHAR) || 
                CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1079667 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
        ide_requerimento = 1079667;
       
COMMIT;

END;
