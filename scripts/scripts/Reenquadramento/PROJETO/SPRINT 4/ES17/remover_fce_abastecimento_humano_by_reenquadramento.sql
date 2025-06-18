-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- REMOVE FORMULÁRIO PARA REMOVER O FORMULÁRIO DE ABASTECIMENTO HUMANO POR REENQUADRAMENTO
CREATE OR REPLACE FUNCTION public.remover_fce_abastecimento_humano_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1;
		n_reenquadramento integer;
BEGIN

-- BUSCA DO ID DO PROCESSO REENQUADRAMENTO PELO NUMERO DO PROCESSO
SELECT INTO n_reenquadramento ide_processo_reenquadramento 
FROM processo_reenquadramento WHERE num_processo =$1 ;
	
-- DELETE'S		
DELETE 
FROM fce_abastecimento_humano 
WHERE ide_fce 
IN (
	SELECT ide_fce 
	FROM fce 
	WHERE ide_processo_reenquadramento 
	IN (n_reenquadramento)and (ide_documento_obrigatorio = 1419));

DELETE 
FROM fce 
WHERE ide_processo_reenquadramento IN (n_reenquadramento)	
AND (ide_documento_obrigatorio = 1419);

DELETE
FROM documento_obrigatorio_reenquadramento  
WHERE ide_processo_reenquadramento in (n_reenquadramento) 
AND ide_documento_obrigatorio = 1419;

RETURN 'OK!';    
END; 
$function$
;
