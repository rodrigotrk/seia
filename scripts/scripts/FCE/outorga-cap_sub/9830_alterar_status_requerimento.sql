-- Function: alterar_status_requerimento(character varying, integer, integer)

-- DROP FUNCTION alterar_status_requerimento(character varying, integer, integer);

CREATE OR REPLACE FUNCTION alterar_status_requerimento(
    prequerimento character varying,
    idstatus integer,
    ide_pessoa integer)
  RETURNS text AS
$BODY$		
		DECLARE pRequerimento ALIAS FOR $1;
		DECLARE idStatus ALIAS FOR $2; 
		DECLARE ide_pessoa ALIAS FOR $3; 
		
		BEGIN
			--AGUARDANDO ENQUADRAMENTO--
			IF(idStatus = 1)
			THEN RETURN (SELECT aguardando_enquadramento(pRequerimento, ide_pessoa));
			
			--VALIDADO--
			ELSIF(idStatus = 5)
			THEN RETURN (SELECT requerimento_validado(pRequerimento, ide_pessoa));
			
			END IF;
		END;
	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alterar_status_requerimento(character varying, integer, integer)
  OWNER TO postgres;
