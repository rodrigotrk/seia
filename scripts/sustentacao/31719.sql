BEGIN;

UPDATE
	requerimento
SET
	num_requerimento = (
	SELECT
		CAST('2021.001.1' AS VARCHAR) || 
		CAST((CAST(split_part(split_part((
			SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
		CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
	dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1036256 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
	ide_requerimento = 1036256;

COMMIT;

END;
