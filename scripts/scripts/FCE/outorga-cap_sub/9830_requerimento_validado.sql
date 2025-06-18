-- Function: requerimento_validado(character varying, integer)

-- DROP FUNCTION requerimento_validado(character varying, integer);

CREATE OR REPLACE FUNCTION requerimento_validado(
    n_requerimento character varying,
    p_ide_pessoa integer)
  RETURNS void AS
$BODY$ 

  DECLARE n_requerimento ALIAS for $1;
	  p_ide_pessoa ALIAS for $2;
	  BOLETO RECORD;

  BEGIN
  
DELETE
  FROM   comprovante_pagamento_dae
  WHERE  ide_boleto_dae_requerimento in
         (
                SELECT ide_boleto_dae_requerimento
                FROM   boleto_dae_requerimento
                WHERE  ide_requerimento=
                       (
                              SELECT ide_requerimento
                              FROM   requerimento
                              WHERE  num_requerimento = $1));

  DELETE
  FROM   boleto_dae_historico
  WHERE  ide_boleto_dae_requerimento IN
         (
                SELECT ide_boleto_dae_requerimento
                FROM   boleto_dae_requerimento
                WHERE  ide_requerimento=
                       (
                              SELECT ide_requerimento
                              FROM   requerimento
                              WHERE  num_requerimento = $1));
  
  DELETE
  FROM   boleto_dae_requerimento
  WHERE  ide_requerimento in
         (
                SELECT ide_requerimento
                FROM   requerimento
                WHERE  num_requerimento = $1);

  
  FOR BOLETO IN
	SELECT bp.ide_boleto_pagamento_requerimento from boleto_pagamento_requerimento bp where bp.ide_boleto_pagamento_requerimento not in (
		SELECT b.ide_boleto_pagamento_requerimento FROM boleto_pagamento_requerimento b LEFT JOIN boleto_pagamento_historico h on b.ide_boleto_pagamento_requerimento = h.ide_boleto_pagamento WHERE b.ide_tipo_boleto_pagamento = 1 AND h.ide_status_boleto_pagamento = 5 )
		and bp.ide_requerimento= (SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1)
	LOOP
		 INSERT INTO boleto_pagamento_historico(ide_boleto_pagamento_historico, ide_boleto_pagamento, ide_status_boleto_pagamento, dtc_tramitacao, ide_pessoa, ide_motivo_cancelamento_boleto, dsc_observacao)
		 VALUES (nextval(('boleto_pagamento_historico_ide_boleto_pagamento_historico_seq'::text)::regclass), 
			BOLETO.IDE_BOLETO_PAGAMENTO_REQUERIMENTO, 5, now(),  $2, null, null);
	END LOOP;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION requerimento_validado(character varying, integer)
  OWNER TO postgres;
