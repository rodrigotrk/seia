-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Turismo por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_turismo_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_turismo_localizacao_geografica where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_categoria_turismo where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_turismo_origem_energia where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_turismo where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 9989);

delete from fce where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 9989;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		and (ide_documento_obrigatorio = 9989 or ide_documento_obrigatorio = 9986);
RETURN 'OK!';    
END; 
$function$
;
