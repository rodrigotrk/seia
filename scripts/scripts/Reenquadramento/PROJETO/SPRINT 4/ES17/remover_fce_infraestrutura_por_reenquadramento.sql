-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Infraestrutura por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_infraestrutura_por_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

DELETE FROM fce_outorga_tipo_infraestrutura_utilizada where ide_fce_outorga_tipo_infraestrutura_utilizada in 
	(SELECT ide_fce_outorga_tipo_infraestrutura_utilizada FROM fce_outorga_tipo_infraestrutura_utilizada WHERE ide_fce_outorga_infraestrutura in 
		(SELECT ide_fce_outorga_infraestrutura FROM fce_outorga_infraestrutura WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070)));				

DELETE FROM fce_outorga_infraestrutura where ide_fce_outorga_infraestrutura in 
		(SELECT ide_fce_outorga_infraestrutura FROM fce_outorga_infraestrutura WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070));

DELETE FROM fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070;

RETURN 'FCE - Infraestrutura removido!';
END;
$function$
;
