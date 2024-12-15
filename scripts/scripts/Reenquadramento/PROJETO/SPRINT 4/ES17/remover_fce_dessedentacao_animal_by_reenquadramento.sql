-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Desedentacao Animal
CREATE OR REPLACE FUNCTION public.remover_fce_dessedentacao_animal_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

delete from fce_dessedentacao_animal_atividade where ide_fce_dessedentacao_animal in 
	(select ide_fce_dessedentacao_animal from fce_dessedentacao_animal where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982));

delete from fce_dessedentacao_animal where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982);

delete from fce_abastecimento_humano where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
		and ide_documento_obrigatorio = 9982);

delete from fce_outorga_localizacao_geografica where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento 
		in (select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982);

delete from fce where ide_processo_reenquadramento in
	(select ide_requerimento from requerimento where num_requerimento = $1) and ide_documento_obrigatorio = 9982;

delete from documento_obrigatorio_reenquadramento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 9982;
RETURN 'OK!';    
END; 
$function$
;
