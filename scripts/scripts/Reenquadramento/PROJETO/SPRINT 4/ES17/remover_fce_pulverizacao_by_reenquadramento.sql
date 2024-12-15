-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Pulverização por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_pulverizacao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_pulverizacao where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1434);

delete from fce_atividade_area where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1434);

delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
	and ide_documento_obrigatorio = 1434;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
	and (ide_documento_obrigatorio = 1434 or ide_documento_obrigatorio = 9987);
RETURN 'OK!';    
END; 
$function$
;
