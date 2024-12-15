-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- REMOVER FORMULÁRIO DE CANAIS POR IDE_PROCESSO_REENQUADRAMENTO
CREATE OR REPLACE FUNCTION public.remover_fce_canais_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
		n_processo_reenquadramento integer;
begin
select into n_processo_reenquadramento ide_processo_reenquadramento FROM processo_reenquadramento WHERE ide_processo_reenquadramento = $1;
--------------------------------------------------------------------------------------
DELETE from fce_canal_municipio where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_processo_reenquadramento in (
			n_processo_reenquadramento
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------------------------
DELETE from fce_canal_tipo_finalidade_uso_agua where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_processo_reenquadramento in (
			n_processo_reenquadramento
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_caracteristica where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_processo_reenquadramento in (
			n_processo_reenquadramento
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho_tipo_revestimento where ide_fce_canal_trecho in (
	SELECT ide_fce_canal_trecho FROM fce_canal_trecho WHERE ide_fce_canal in(
		SELECT ide_fce_canal FROM fce_canal WHERE ide_fce in(
			SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento in(
				n_processo_reenquadramento
			) and ide_documento_obrigatorio = 10070
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho_secao_geometrica where ide_fce_canal_trecho in (
	SELECT ide_fce_canal_trecho FROM fce_canal_trecho where ide_fce_canal in (
		select ide_fce_canal from fce_canal where ide_fce in(
			SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
				n_processo_reenquadramento
			) and ide_documento_obrigatorio = 10070
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho where ide_fce_canal in(
	SELECT ide_fce_canal FROM fce_canal WHERE ide_fce in(
		SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
			n_processo_reenquadramento
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal where ide_fce = (
	SELECT ide_fce FROM FCE where ide_processo_reenquadramento in(
		n_processo_reenquadramento
	) and ide_documento_obrigatorio = 10070
);

RETURN 'FCE - Canais excluído!';    
END;
$function$
;
