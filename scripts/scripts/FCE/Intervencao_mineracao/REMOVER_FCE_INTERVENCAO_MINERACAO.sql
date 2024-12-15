CREATE OR REPLACE FUNCTION remover_fce_intervencao_mineracao_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
BEGIN
--------------------------------------------------------------------
DELETE from fce_intervencao_tipo_caract_extracao where ide_fce_intervencao_caracteristica_extracao = (
	SELECT ide_fce_intervencao_caracteristica_extracao from fce_intervencao_caracteristica_extracao where ide_fce_intervencao_mineracao = (
		SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce = (
			SELECT ide_fce FROM FCE where ide_requerimento in(
				SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
			) and ide_documento_obrigatorio = 3002
		)
	)
);
--------------------------------------------------------------------
DELETE from fce_intervencao_caracteristica_extracao where ide_fce_intervencao_mineracao = (
	SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce = (
		SELECT ide_fce FROM FCE where ide_requerimento in(
			SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
		) and ide_documento_obrigatorio = 3002
	)
);
--------------------------------------------------------------------
DELETE from lancamento_efluente_localizacao_geografica where ide_fce_intervencao_mineracao = (
	SELECT ide_fce_intervencao_mineracao from fce_intervencao_mineracao where ide_fce = (
		SELECT ide_fce FROM FCE where ide_requerimento in(
			SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
		) and ide_documento_obrigatorio = 3002
	)
);
--------------------------------------------------------------------
DELETE from fce_intervencao_mineracao where ide_fce = (
	SELECT ide_fce FROM FCE where ide_requerimento in(
		SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1
	) and ide_documento_obrigatorio = 3002
);

RETURN 'FCE - Intervenção Mineração para fins de Recurso Hídrico excluído!';    
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_intervencao_mineracao_by_requerimento(character varying)
  OWNER TO seia_sema;