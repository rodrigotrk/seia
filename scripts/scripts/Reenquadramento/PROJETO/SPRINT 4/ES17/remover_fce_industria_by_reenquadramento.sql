-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Industria por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_industria_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
DELETE FROM fce_industria_destINo_residuo WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_emissao_atmosferica WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_origem_energia WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_residuo_gerado WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_sistema_tratamento WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_substancia WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_tipo_app WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
			(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_residuo_civil WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
			(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria WHERE ide_fce IN
	(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458);

DELETE FROM fce WHERE ide_processo_reenquadramento IN 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458;

DELETE FROM documento_obrigatorio_reenquadramento WHERE ide_processo_reenquadramento in 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458;
RETURN 'OK!';    
END; 
$function$
;

