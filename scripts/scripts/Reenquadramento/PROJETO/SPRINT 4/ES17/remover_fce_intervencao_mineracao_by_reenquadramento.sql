-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Intervenção em Mineração por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_intervencao_mineracao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
--------------------------------------------------------------------
DELETE from fce_intervencao_tipo_caract_extracao where ide_fce_intervencao_caracteristica_extracao in (
	SELECT ide_fce_intervencao_caracteristica_extracao from fce_intervencao_caracteristica_extracao where ide_fce_intervencao_mineracao = (
		SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce in (
			SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
				SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1
			) and ide_documento_obrigatorio = 3002
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_intervencao_caracteristica_extracao where ide_fce_intervencao_mineracao in (
	SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce in (
		SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
			SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1
		) and ide_documento_obrigatorio = 3002
	)
);
--------------------------------------------------------------------
DELETE from lancamento_efluente_localizacao_geografica where ide_fce_intervencao_mineracao in (
	SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce in (
		SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
			SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1
		) and ide_documento_obrigatorio = 3002
	)
);
--------------------------------------------------------------------
DELETE from fce_intervencao_mineracao where ide_fce in (
	SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
		SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1
	) and ide_documento_obrigatorio = 3002
);

RETURN 'FCE - Intervenção Mineração para fins de Recurso Hídrico excluído!';    
END; 
$function$
;
