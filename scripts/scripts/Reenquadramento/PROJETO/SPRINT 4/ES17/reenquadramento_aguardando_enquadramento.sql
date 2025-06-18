-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
CREATE OR REPLACE FUNCTION public.reenquadramento_aguardando_enquadramento(n_reenquadramento character varying)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ 
DECLARE n_reenquadramento ALIAS FOR $1; 
		n_processo_reenquadramento integer;
		n_enquadramento_ato_ambiental integer;
		n_enquadramento integer;
BEGIN

	PERFORM reenquadramento_validado($1);
	 
	-- BUSCA DO ID DO PROCESSO REENQUADRAMENTO PELO PROCESSO
	SELECT INTO n_processo_reenquadramento ide_processo_reenquadramento 
	FROM processo_reenquadramento 
	WHERE num_processo = $1;
	
	-- BUSCA O NÚMERO DO ENQUADRAMENTO
	SELECT INTO n_enquadramento ide_enquadramento 
	FROM enquadramento 
	WHERE ide_processo_reenquadramento in (n_processo_reenquadramento);
	
	-- BUSCA O NUMERO DO ENQUADRAMENTO ATO AMBIENTAL
	SELECT INTO n_enquadramento_ato_ambiental ide_enquadramento_ato_ambiental 
	FROM enquadramento_ato_ambiental 
	WHERE ide_enquadramento IN  (n_enquadramento);
	
	-- DELETE'S
	DELETE
	FROM documento_obrigatorio_reenquadramento 
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);
	
	DELETE
	FROM enquadramento_finalidade_uso_agua
	WHERE ide_enquadramento_ato_ambiental IN (n_enquadramento_ato_ambiental);
	
	DELETE
	FROM bioma_enquadramento_ato_ambiental
	WHERE ide_enquadramento_ato_ambiental in (n_enquadramento_ato_ambiental);
	
	DELETE
	FROM processo_reenquadramento_hist_ato
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);
	
	
	DELETE
	FROM enquadramento_ato_ambiental
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE
	FROM enquadramento_documento_obrigatorio 
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE 
	FROM enquadramento_documento_ato 
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE
	FROM enquadramento 
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);

	
END	
$function$
;