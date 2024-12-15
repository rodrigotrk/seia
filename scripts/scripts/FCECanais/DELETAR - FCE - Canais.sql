-- Function: remover_fce_canais_by_requerimento(character varying)

--DROP FUNCTION remover_fce_canais_by_requerimento(character varying);

CREATE OR REPLACE FUNCTION remover_fce_canais_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
BEGIN
--------------------------------------------------------------------------------------
DELETE from fce_canal_municipio where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_requerimento in (
			select ide_requerimento from requerimento where num_requerimento = $1
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------------------------
DELETE from fce_canal_tipo_finalidade_uso_agua where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_requerimento in (
			select ide_requerimento from requerimento where num_requerimento = $1
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_caracteristica where ide_fce_canal in (
	SELECT ide_fce_canal FROM fce_canal where ide_fce in (
		select ide_fce from fce where ide_requerimento in (
			select ide_requerimento from requerimento where num_requerimento = $1
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho_tipo_revestimento where ide_fce_canal_trecho in (
	SELECT ide_fce_canal_trecho FROM fce_canal_trecho WHERE ide_fce_canal in(
		SELECT ide_fce_canal FROM fce_canal WHERE ide_fce in(
			SELECT ide_fce FROM fce WHERE ide_requerimento in(
				SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
			) and ide_documento_obrigatorio = 10070
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho_secao_geometrica where ide_fce_canal_trecho in (
	SELECT ide_fce_canal_trecho FROM fce_canal_trecho where ide_fce_canal in (
		select ide_fce_canal from fce_canal where ide_fce in(
			SELECT ide_fce FROM FCE where ide_requerimento in(
				SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
			) and ide_documento_obrigatorio = 10070
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_canal_trecho where ide_fce_canal in(
	SELECT ide_fce_canal FROM fce_canal WHERE ide_fce in(
		SELECT ide_fce FROM FCE where ide_requerimento in(
			SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
		) and ide_documento_obrigatorio = 10070
	)
);
--------------------------------------------------------------------
DELETE from fce_canal where ide_fce = (
	SELECT ide_fce FROM FCE where ide_requerimento in(
		SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
	) and ide_documento_obrigatorio = 10070
);

RETURN 'FCE - Canais excluído!';    
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_autorizacao_mineracao_by_requerimento(character varying)
  OWNER TO seia_sema;