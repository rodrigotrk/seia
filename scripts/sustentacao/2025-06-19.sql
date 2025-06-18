--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 05/06/2025 14:51:27
--Versão 4.36.0

BEGIN;

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
								  CAST('2025.001.' AS VARCHAR) || 
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

COMMIT;
