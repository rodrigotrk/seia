-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Captação Superficial
CREATE OR REPLACE FUNCTION public.remover_fce_cap_superficial_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN


delete from fce_captacao_superficial_cnae where ide_fce_captacao_superficial in
(select ide_fce_captacao_superficial from fce_captacao_superficial where ide_fce_outorga_localizacao_geografica in
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1436)));

delete from fce_captacao_superficial where ide_fce_outorga_localizacao_geografica in
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
			and ide_documento_obrigatorio = 1436));

delete from fce_outorga_localizacao_geografica where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1436);
	
delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1436;

delete from documento_obrigatorio_reenquadramento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and (ide_documento_obrigatorio = 1436 or ide_documento_obrigatorio = 9998);

RETURN 'OK!';    
END; 
$function$
;