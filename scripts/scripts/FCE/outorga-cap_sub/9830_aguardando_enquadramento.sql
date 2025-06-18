-- Function: aguardando_enquadramento(character varying, integer)

-- DROP FUNCTION aguardando_enquadramento(character varying, integer);

CREATE OR REPLACE FUNCTION aguardando_enquadramento(
    n_requerimento character varying,
    ide_pessoa integer)
  RETURNS void AS
$BODY$ 
DECLARE n_requerimento ALIAS
FOR $1;
ide_pessoa ALIAS for $2; BEGIN

PERFORM requerimento_validado($1, $2);

DELETE
FROM documento_obrigatorio_requerimento WHERE ide_requerimento=
    (SELECT ide_requerimento
     FROM requerimento
     WHERE num_requerimento = $1);


DELETE
FROM enquadramento_finalidade_uso_agua
WHERE ide_enquadramento_ato_ambiental IN
      (SELECT ide_enquadramento_ato_ambiental 
       FROM enquadramento_ato_ambiental 
       WHERE ide_enquadramento IN 
             (SELECT ide_enquadramento 
              FROM enquadramento 
              WHERE ide_requerimento = 
                    (SELECT ide_requerimento 
                     FROM requerimento 
                     WHERE num_requerimento =  $1 )));


--V1
DELETE
FROM bioma_enquadramento_ato_ambiental
WHERE ide_enquadramento_ato_ambiental in
	(SELECT ide_enquadramento_ato_ambiental
	 FROM enquadramento_ato_ambiental
	 WHERE ide_enquadramento in
		(SELECT ide_enquadramento
		 FROM enquadramento
		 WHERE ide_requerimento_unico =
			(SELECT ide_requerimento
			 FROM requerimento
			 WHERE num_requerimento = $1)));

--V2
DELETE
FROM bioma_enquadramento_ato_ambiental
WHERE ide_enquadramento_ato_ambiental in
	(SELECT ide_enquadramento_ato_ambiental
	 FROM enquadramento_ato_ambiental
	 WHERE ide_enquadramento in
		(SELECT ide_enquadramento
		 FROM enquadramento
		 WHERE ide_requerimento=
			(SELECT ide_requerimento
			 FROM requerimento
			 WHERE num_requerimento = $1)));

DELETE
FROM bioma_requerimento
WHERE ide_requerimento in
	(SELECT ide_requerimento
	 FROM requerimento
	 WHERE num_requerimento = $1);


--V1 
DELETE
FROM enquadramento_ato_ambiental
WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento_unico =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));
--V2
DELETE
FROM enquadramento_ato_ambiental
WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));
--V1	
  DELETE
  FROM enquadramento_documento_obrigatorio WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento_unico =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));

--V2
  DELETE
  FROM enquadramento_documento_obrigatorio WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));

 --V1
DELETE FROM enquadramento_documento_ato 
  WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento_unico =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));
--V2
DELETE FROM enquadramento_documento_ato 
  WHERE ide_enquadramento in
    (SELECT ide_enquadramento
     FROM enquadramento
     WHERE ide_requerimento =
         (SELECT ide_requerimento
          FROM requerimento
          WHERE num_requerimento = $1));
--V1      
  DELETE
  FROM enquadramento WHERE ide_requerimento_unico =
    (SELECT ide_requerimento
     FROM requerimento
     WHERE num_requerimento = $1);
--V2
  DELETE
  FROM enquadramento WHERE ide_requerimento =
    (SELECT ide_requerimento
     FROM requerimento
     WHERE num_requerimento = $1);
     
  UPDATE documento_identificacao_requerimento
	SET ind_documento_validado = false, 
	    dtc_validacao = null, 
	    ide_pessoa_validacao = null 
	WHERE ide_requerimento = 
	(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1);

  UPDATE documento_representacao_requerimento
	SET ind_documento_validado = false, 
	    dtc_validacao = null, 
	    ide_pessoa_validacao = null 
	WHERE ide_requerimento = 
	(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1);

  UPDATE requerimento 
	SET ide_area = null 
	WHERE ide_requerimento = 
	(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1);
	
END	
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION aguardando_enquadramento(character varying, integer)
  OWNER TO postgres;
