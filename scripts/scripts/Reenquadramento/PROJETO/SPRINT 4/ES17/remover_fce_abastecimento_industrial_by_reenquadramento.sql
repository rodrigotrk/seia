-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
CREATE OR REPLACE FUNCTION public.remover_fce_abastecimento_industrial_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
		n_reenquadramento integer;
BEGIN
-- BUSCA O IDE DO PROCESSO DO REENQUADRAMENTO POR NUMERO DO PROCESSO
SELECT INTO n_reenquadramento ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1;

--DELETE'S
DELETE 
FROM fce_abastecimento_industrial 
WHERE ide_fce IN
	(SELECT ide_fce 
	FROM fce 
	WHERE ide_processo_reenquadramento 
	IN (n_reenquadramento)
	AND (ide_documento_obrigatorio = 1432));

DELETE
FROM fce 
WHERE ide_processo_reenquadramento in (n_reenquadramento)
AND (ide_documento_obrigatorio = 1432);

DELETE 
FROM documento_obrigatorio_reenquadramento 
WHERE ide_processo_reenquadramento in (n_reenquadramento)
AND (ide_documento_obrigatorio = 1432 OR ide_documento_obrigatorio = 9988);

RETURN 'OK!';    
END; 
$function$
;
