CREATE OR REPLACE FUNCTION remover_fce_florestal_by_requerimento(
    prequerimento character varying,
    pdocumentoobrigatorio integer)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
DECLARE pDocumentoObrigatorio ALIAS FOR $2; 
BEGIN
	delete from especie_florestal_aut_destino_socio_economico where ide_especie_florestal_autorizacao in
		(select ide_especie_florestal_autorizacao from especie_florestal_autorizacao where ide_fce_florestal in
			(select ide_fce_florestal from fce_florestal where ide_fce in
				(select ide_fce from fce where ide_requerimento in
					(select ide_requerimento from requerimento where num_requerimento  = $1) and ide_documento_obrigatorio = $2)));

	delete from especie_florestal_autorizacao where ide_fce_florestal in
		(select ide_fce_florestal from fce_florestal where ide_fce in
			(select ide_fce from fce where ide_requerimento in
				(select ide_requerimento from requerimento where num_requerimento  = $1) and ide_documento_obrigatorio = $2));
	
	delete from fce_florestal where ide_fce in 
		(select ide_fce from fce where ide_requerimento in
			(select ide_requerimento from requerimento where num_requerimento  = $1) and ide_documento_obrigatorio = $2);

	delete from fce where ide_requerimento in
		(select ide_requerimento from requerimento where num_requerimento  = $1) and ide_documento_obrigatorio = $2;

	delete from documento_obrigatorio_requerimento  where ide_requerimento in
		(select ide_requerimento from requerimento where num_requerimento  = $1) and ide_documento_obrigatorio = $2;
RETURN 'OK!';    
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_florestal_by_requerimento(character varying, integer)
  OWNER TO postgres;

  
  
CREATE OR REPLACE FUNCTION remover_fce_epmf_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE prequerimento ALIAS FOR $1; 
BEGIN
  PERFORM remover_fce_florestal_by_requerimento($1, 10081);
  RETURN 'OK!';    
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;