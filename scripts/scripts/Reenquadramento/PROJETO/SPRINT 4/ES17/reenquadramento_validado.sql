-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN

CREATE OR REPLACE FUNCTION public.reenquadramento_validado(n_reenquadramento character varying)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ 

  DECLARE n_reenquadramento ALIAS for $1;
  		n_processo_reenquadramento integer;
  		n_boleto_pagamento_requerimento integer;
  		n_boleto_dae integer;
  		TIPO_BOLETO constant NUMERIC := 7;
BEGIN
    -- Carrega o Ide do Processo do reenquadramento
	SELECT INTO n_processo_reenquadramento ide_processo_reenquadramento 
	FROM processo_reenquadramento 
  	WHERE num_processo = $1;
  	
  	-- Carrega o Ide do Boleto do reenquadramento
  	SELECT into n_boleto_pagamento_requerimento ide_boleto_pagamento_requerimento
    FROM   boleto_pagamento_requerimento
    WHERE  ide_processo_reenquadramento IN (n_processo_reenquadramento)
	AND ide_tipo_boleto_pagamento = TIPO_BOLETO;
	
	-- Carrega Ide do Boleto DAE	
	SELECT INTO  n_boleto_dae ide_boleto_dae_requerimento
    FROM   boleto_dae_requerimento
    WHERE  ide_processo_reenquadramento IN ( n_processo_reenquadramento);
	
    ---DELETE'S
 	DELETE
 	FROM   comprovante_pagamento_dae
    WHERE  ide_boleto_dae_requerimento IN ( n_boleto_dae );
  
	DELETE
	FROM   comprovante_pagamento
	WHERE  ide_boleto_pagamento_requerimento in (n_boleto_pagamento_requerimento);
  
  	DELETE
  	FROM   boleto_dae_historico
  	WHERE  ide_boleto_dae_requerimento IN (n_boleto_dae);
  
  	DELETE
  	FROM   boleto_dae_requerimento
  	WHERE  ide_processo_reenquadramento IN  (n_processo_reenquadramento);
  
  	DELETE
  	FROM   boleto_pagamento_historico
  	WHERE  ide_boleto_pagamento in  (n_boleto_pagamento_requerimento);
  
  	DELETE
  	FROM   detalhamento_boleto
  	WHERE  ide_boleto_pagamento_requerimento in (n_boleto_pagamento_requerimento);
  
  	DELETE
  	FROM   boleto_pagamento_requerimento
  	WHERE  ide_processo_reenquadramento IN (n_processo_reenquadramento)
  	AND ide_tipo_boleto_pagamento = TIPO_BOLETO;
	
END;
$function$
;