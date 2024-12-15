-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Licenciamento Aquicultura por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_licenciamento_aquicultura_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
DELETE FROM fce_localizacao_geografica WHERE ide_fce  IN 
	(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		AND ide_documento_obrigatorio = 9997);

DELETE FROM fce_aquicultura_licenca_tipo_atividade_sistema_cultivo WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce in 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento in
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_organismo WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_tipo_Instalacao WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
		(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN
			(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				AND ide_documento_obrigatorio = 9997)));

DELETE FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_tipo_localizacao_cultivo WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_localizacao_geografica WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_localizacao_geografica WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca WHERE ide_fce IN 
	(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 9997);

DELETE FROM fce  WHERE ide_processo_reenquadramento IN 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
	AND ide_documento_obrigatorio = 9997;

RETURN 'OK!';    
END; 
$function$
;
