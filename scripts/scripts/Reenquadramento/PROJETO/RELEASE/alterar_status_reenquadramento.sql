-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN

CREATE OR REPLACE FUNCTION public.alterar_status_reenquadramento(preenquadramento character varying, idstatusreenquadramento integer)
 RETURNS text
 LANGUAGE plpgsql
AS $function$		
		DECLARE pReenquadramento ALIAS FOR $1;
		DECLARE idStatusReenquadramento ALIAS FOR $2; 
		
		BEGIN
			--AGUARDANDO EDIÇÃO DO REENQUADRAMENTO--
			IF(idStatusReenquadramento = 14)
			THEN RETURN (SELECT reenquadramento_aguardando_enquadramento(pReenquadramento));
			
			--VALIDADO--
			ELSIF(idStatusReenquadramento = 6)
			THEN RETURN (SELECT reenquadramento_validado(pReenquadramento));
			
			END IF;
		END;
	$function$
;