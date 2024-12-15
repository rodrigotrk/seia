-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Localização Geográfica por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_loc_geo_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

delete from dado_geografico where ide_localizacao_geografica in 
	(select ide_localizacao_geografica from fce_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in  
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1439));

UPDATE localizacao_geografica SET ind_excluido = true, dtc_exclusao = (SELECT now()) where ide_localizacao_geografica in 
	(select ide_localizacao_geografica from fce_localizacao_geografica where ide_fce in 
 		(select ide_fce from fce where ide_processo_reenquadramento in 
 			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
 			and ide_documento_obrigatorio = 1439));
	
delete from fce_localizacao_geografica where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
		and ide_documento_obrigatorio = 1439);

delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1439;

delete from documento_obrigatorio_requerimento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
	and (ide_documento_obrigatorio = 1439 or ide_documento_obrigatorio = 9981);

RETURN 'OK!';    
END; 
$function$
;
