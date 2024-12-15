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
-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
CREATE OR REPLACE FUNCTION public.reenquadramento_aguardando_enquadramento(n_reenquadramento character varying)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ 
DECLARE n_reenquadramento ALIAS FOR $1; 
		n_processo_reenquadramento integer;
		n_enquadramento_ato_ambiental integer;
		n_enquadramento integer;
BEGIN

	PERFORM reenquadramento_validado($1);
	 
	-- BUSCA DO ID DO PROCESSO REENQUADRAMENTO PELO PROCESSO
	SELECT INTO n_processo_reenquadramento ide_processo_reenquadramento 
	FROM processo_reenquadramento 
	WHERE num_processo = $1;
	
	-- BUSCA O NÚMERO DO ENQUADRAMENTO
	SELECT INTO n_enquadramento ide_enquadramento 
	FROM enquadramento 
	WHERE ide_processo_reenquadramento in (n_processo_reenquadramento);
	
	-- BUSCA O NUMERO DO ENQUADRAMENTO ATO AMBIENTAL
	SELECT INTO n_enquadramento_ato_ambiental ide_enquadramento_ato_ambiental 
	FROM enquadramento_ato_ambiental 
	WHERE ide_enquadramento IN  (n_enquadramento);
	
	-- DELETE'S
	DELETE
	FROM documento_obrigatorio_reenquadramento 
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);
	
	DELETE
	FROM enquadramento_finalidade_uso_agua
	WHERE ide_enquadramento_ato_ambiental IN (n_enquadramento_ato_ambiental);
	
	DELETE
	FROM bioma_enquadramento_ato_ambiental
	WHERE ide_enquadramento_ato_ambiental in (n_enquadramento_ato_ambiental);
	
	DELETE
	FROM processo_reenquadramento_hist_ato
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);
	
	
	DELETE
	FROM enquadramento_ato_ambiental
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE
	FROM enquadramento_documento_obrigatorio 
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE 
	FROM enquadramento_documento_ato 
	WHERE ide_enquadramento in (n_enquadramento);
	
	DELETE
	FROM enquadramento 
	WHERE ide_processo_reenquadramento IN (n_processo_reenquadramento);

	
END	
$function$
;

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

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- REMOVE FORMULÁRIO PARA REMOVER O FORMULÁRIO DE ABASTECIMENTO HUMANO POR REENQUADRAMENTO
CREATE OR REPLACE FUNCTION public.remover_fce_abastecimento_humano_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1;
		n_reenquadramento integer;
BEGIN

-- BUSCA DO ID DO PROCESSO REENQUADRAMENTO PELO NUMERO DO PROCESSO
SELECT INTO n_reenquadramento ide_processo_reenquadramento 
FROM processo_reenquadramento WHERE num_processo =$1 ;
	
-- DELETE'S		
DELETE 
FROM fce_abastecimento_humano 
WHERE ide_fce 
IN (
	SELECT ide_fce 
	FROM fce 
	WHERE ide_processo_reenquadramento 
	IN (n_reenquadramento)and (ide_documento_obrigatorio = 1419));

DELETE 
FROM fce 
WHERE ide_processo_reenquadramento IN (n_reenquadramento)	
AND (ide_documento_obrigatorio = 1419);

DELETE
FROM documento_obrigatorio_reenquadramento  
WHERE ide_processo_reenquadramento in (n_reenquadramento) 
AND ide_documento_obrigatorio = 1419;

RETURN 'OK!';    
END; 
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
CREATE OR REPLACE FUNCTION public.remover_fce_abastecimento_industrial_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
		n_reenquadramento integer;
BEGIN
-- BUSCA O IDE DO PROCESSO DO REENQUADRAMENTO POR NUMERO DO PROCESSO
SELECT INTO n_reenquadramento ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1;

--DELETE'S
DELETE 
FROM fce_abastecimento_industrial 
WHERE ide_fce IN
	(SELECT ide_fce 
	FROM fce 
	WHERE ide_processo_reenquadramento 
	IN (n_reenquadramento)
	AND (ide_documento_obrigatorio = 1432));

DELETE
FROM fce 
WHERE ide_processo_reenquadramento in (n_reenquadramento)
AND (ide_documento_obrigatorio = 1432);

DELETE 
FROM documento_obrigatorio_reenquadramento 
WHERE ide_processo_reenquadramento in (n_reenquadramento)
AND (ide_documento_obrigatorio = 1432 OR ide_documento_obrigatorio = 9988);

RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Caracterização do Empreendimento - ASV
CREATE OR REPLACE FUNCTION public.remover_fce_asv_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1;
		n_reenquadramento integer;
		n_fce integer;
		n_fce_asv integer;
begin
	-- BUSCA DO ID DO PROCESSO REENQUADRAMENTO PELO NUMERO DO PROCESSO
	SELECT INTO n_reenquadramento ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1;
	-- BUSCA DO ID DO FCE PELO ID DO PROCESSO REENQUADRAMENTO
	SELECT INTO n_fce ide_fce FROM fce WHERE ide_processo_reenquadramento IN (n_reenquadramento) and ide_documento_obrigatorio = 2000;

	SELECT INTO n_fce_asv ide_fce_asv FROM fce_asv WHERE ide_fce IN (n_fce);
	
	DELETE 
	FROM produto_supressao_destino 
	WHERE ide_produto_supressao IN 
		(SELECT ide_produto_supressao 
		FROM produto_supressao 
		WHERE ide_fce_asv 
		IN (n_fce_asv));

	DELETE 
	FROM produto_supressao 
	WHERE ide_fce_asv 
	IN (n_fce_asv);

	DELETE 
	FROM fce_asv_ocorrencia_area 
	WHERE ide_fce_asv 
	IN (n_fce_asv);

	DELETE 
	FROM fce_asv_justificativa_supressao 
	WHERE ide_fce_asv 
	IN (n_fce_asv);

	DELETE 
	FROM fce_asv_obj_supressao 
	WHERE ide_fce_asv
	IN (n_fce_asv);

	DELETE 
	FROM fce_asv_classi_vegetacao 
	WHERE ide_fce_asv 
	IN (n_fce_asv);
				
	DELETE 
	FROM especie_supressao_aut_destino_socio_economico 
	WHERE ide_especie_supressao_autorizacao 
	IN (SELECT ide_especie_supressao_autorizacao 
		FROM especie_supressao_autorizacao 
		WHERE ide_fce_asv 
		IN (n_fce_asv));

	DELETE FROM especie_supressao_autorizacao WHERE ide_fce_asv IN
		(n_fce_asv);
	
	DELETE FROM fce_asv WHERE ide_fce IN 
		(n_fce);

	DELETE 
	FROM fce 
	WHERE ide_processo_reenquadramento 
	IN (n_reenquadramento) 
	and ide_documento_obrigatorio = 2000;

	DELETE FROM documento_obrigatorio_reenquadramento WHERE ide_documento_obrigatorio IN 
		(n_reenquadramento) 
			and ide_documento_obrigatorio = 2000;		
			
RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Autorização de Mineração 
CREATE OR REPLACE FUNCTION public.remover_fce_autorizacao_mineracao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1;
		n_reenquadramento integer;
BEGIN

delete from fce_pesquisa_mineral_destino_residuo where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from fce_pesquisa_mineral_tipo_residuo where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from fce_pesquisa_mineral_metodo_recuperacao where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from fce_pesquisa_mineral_substancia_mineral_tipologia where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from fce_pesquisa_prospeccao_geofisica where ide_fce_pesquisa_mineral_prospeccao in (
	select ide_fce_pesquisa_mineral_prospeccao from fce_pesquisa_mineral_prospeccao where ide_fce_pesquisa_mineral in (
		SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
			(select ide_fce from fce where ide_processo_reenquadramento in 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 3001)
		)
	)
);

delete from fce_prospeccao where ide_fce_pesquisa_mineral_prospeccao in (
	select ide_fce_pesquisa_mineral_prospeccao from fce_pesquisa_mineral_prospeccao where ide_fce_pesquisa_mineral in (
		SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
			(select ide_fce from fce where ide_processo_reenquadramento in 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 3001)
		)
	)
);

delete from fce_pesquisa_mineral_prospeccao where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from fce_pesquisa_mineral_origem_energia where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);



delete from processo_dnpm where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete from outorga_mineracao where ide_fce_pesquisa_mineral in (
	SELECT ide_fce_pesquisa_mineral FROM fce_pesquisa_mineral where ide_fce in (
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001)
	)
);

delete FROM fce_pesquisa_mineral where ide_fce in (
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 3001)
);

DELETE FROM fce WHERE ide_fce IN 
	(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento in 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3001);
RETURN 'FCE - Autorização para Mineração excluído!';    
END; 
$function$
;



-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Captação Subterranea
CREATE OR REPLACE FUNCTION public.remover_fce_cap_subterranea_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

delete from fce_captacao_subterranea where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 1435);
	
delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) and ide_documento_obrigatorio = 1435;

delete from documento_obrigatorio_reenquadramento 	where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
	and (ide_documento_obrigatorio = 1435 or ide_documento_obrigatorio = 9991);
RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Captação Superficial
CREATE OR REPLACE FUNCTION public.remover_fce_cap_superficial_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN


delete from fce_captacao_superficial_cnae where ide_fce_captacao_superficial in
(select ide_fce_captacao_superficial from fce_captacao_superficial where ide_fce_outorga_localizacao_geografica in
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1436)));

delete from fce_captacao_superficial where ide_fce_outorga_localizacao_geografica in
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
			and ide_documento_obrigatorio = 1436));

delete from fce_outorga_localizacao_geografica where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1436);
	
delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1436;

delete from documento_obrigatorio_reenquadramento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and (ide_documento_obrigatorio = 1436 or ide_documento_obrigatorio = 9998);

RETURN 'OK!';    
END; 
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Formulário de Desedentacao Animal
CREATE OR REPLACE FUNCTION public.remover_fce_dessedentacao_animal_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

delete from fce_dessedentacao_animal_atividade where ide_fce_dessedentacao_animal in 
	(select ide_fce_dessedentacao_animal from fce_dessedentacao_animal where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982));

delete from fce_dessedentacao_animal where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982);

delete from fce_abastecimento_humano where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
		and ide_documento_obrigatorio = 9982);

delete from fce_outorga_localizacao_geografica where ide_fce  in
	(select ide_fce from fce where ide_processo_reenquadramento 
		in (select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 9982);

delete from fce where ide_processo_reenquadramento in
	(select ide_requerimento from requerimento where num_requerimento = $1) and ide_documento_obrigatorio = 9982;

delete from documento_obrigatorio_reenquadramento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 9982;
RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Industria por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_industria_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
DELETE FROM fce_industria_destINo_residuo WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_emissao_atmosferica WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_origem_energia WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_residuo_gerado WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_sistema_tratamento WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_substancia WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
		(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_tipo_app WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
			(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria_residuo_civil WHERE ide_fce_industria IN
	(SELECT ide_fce_industria FROM fce_industria WHERE ide_fce IN 
			(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 1458));

DELETE FROM fce_industria WHERE ide_fce IN
	(SELECT ide_fce FROM fce WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458);

DELETE FROM fce WHERE ide_processo_reenquadramento IN 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458;

DELETE FROM documento_obrigatorio_reenquadramento WHERE ide_processo_reenquadramento in 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	AND ide_documento_obrigatorio = 1458;
RETURN 'OK!';    
END; 
$function$
;



-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Infraestrutura por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_infraestrutura_por_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

DELETE FROM fce_outorga_tipo_infraestrutura_utilizada where ide_fce_outorga_tipo_infraestrutura_utilizada in 
	(SELECT ide_fce_outorga_tipo_infraestrutura_utilizada FROM fce_outorga_tipo_infraestrutura_utilizada WHERE ide_fce_outorga_infraestrutura in 
		(SELECT ide_fce_outorga_infraestrutura FROM fce_outorga_infraestrutura WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070)));				

DELETE FROM fce_outorga_infraestrutura where ide_fce_outorga_infraestrutura in 
		(SELECT ide_fce_outorga_infraestrutura FROM fce_outorga_infraestrutura WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070));

DELETE FROM fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10070;

RETURN 'FCE - Infraestrutura removido!';
END;
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- REMOVER FORMULÁRIO INTERVENÇÃO BARRAGEM POR REENQUADRAMENTO
CREATE OR REPLACE FUNCTION public.remover_fce_intervencao_barragem_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1;
        v_fce integer;
	v_reenquadramento integer;
	v_localizacao_geografica integer;
	v_fce_barragem integer;
	v_fce_barragem_licenciamento integer;
BEGIN

select into v_reenquadramento proc_req.ide_processo_reenquadramento from processo_reenquadramento proc_req where proc_req.num_processo = $1;
select into v_fce ide_fce from fce where ide_processo_reenquadramento = v_reenquadramento and (ide_documento_obrigatorio = 1437 or ide_documento_obrigatorio = 97);
select into v_localizacao_geografica ide_localizacao_geografica from fce_barragem where ide_fce = v_fce;
select into v_fce_barragem ide_fce_barragem from fce_barragem where ide_fce = v_fce;
select into v_fce_barragem_licenciamento ide_fce_barragem_licenciamento from fce_barragem_licenciamento where ide_fce_barragem = v_fce_barragem;

delete from fce_barragem_material_utilizado where ide_fce_barragem_licenciamento = v_fce_barragem_licenciamento;
delete from fce_barrag_licenc_loca_geo where ide_fce_barragem_licenciamento = v_fce_barragem_licenciamento;
delete from fce_barragem_obras_infra where ide_fce_barragem_licenciamento = v_fce_barragem_licenciamento;

delete from fce_barragem_uso_reservatorio where ide_fce_barragem = v_fce_barragem;
delete from fce_intervencao_barragem where ide_fce = v_fce;
delete from fce_barragem_licenciamento where ide_fce_barragem = v_fce_barragem;
delete from fce_barragem where ide_fce = v_fce;
delete from dado_geografico where ide_localizacao_geografica = v_localizacao_geografica;
delete from localizacao_geografica where ide_localizacao_geografica = v_localizacao_geografica;

delete from fce where ide_fce = v_fce;

delete from documento_obrigatorio_reenquadramento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento where num_processo = $1)
	and (ide_documento_obrigatorio = 1437 or ide_documento_obrigatorio = 97 or ide_documento_obrigatorio = 9999);
RETURN 'OK!';    
END; 
$function$
;

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

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Irrigação por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_irrigacao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_irrigacao where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) and ide_documento_obrigatorio = 1433);

delete from fce_atividade_area where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) and ide_documento_obrigatorio = 1433);

delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1433;
RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Lançamento de Efluente por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_lancamento_efluente_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_lancamento_efluente_caracterizacao where ide_fce_lancamento_efluente in
	(select ide_fce_lancamento_efluente from fce_lancamento_efluente where ide_fce_outorga_localizacao_geografica in 
		(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
			(select ide_fce from fce where ide_processo_reenquadramento in 
				(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 1440)));

delete from fce_lancamento_efluente_caracteristica where ide_fce_lancamento_efluente in
	(select ide_fce_lancamento_efluente from fce_lancamento_efluente where ide_fce_outorga_localizacao_geografica in 
		(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
			(select ide_fce from fce where ide_processo_reenquadramento in
				(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 1440)));

delete from fce_lancamento_efluente where ide_fce_outorga_localizacao_geografica in 
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 1440));

delete from fce_outorga_localizacao_geografica where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 1440);
	
delete from fce where ide_processo_reenquadramento in 
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1440;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in 
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
	and (ide_documento_obrigatorio = 1440 or ide_documento_obrigatorio = 9978);
RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Licenciamento Aquicultura por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_licenciamento_aquicultura_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
DELETE FROM fce_localizacao_geografica WHERE ide_fce  IN 
	(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		AND ide_documento_obrigatorio = 9997);

DELETE FROM fce_aquicultura_licenca_tipo_atividade_sistema_cultivo WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce in 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento in
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_organismo WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_tipo_Instalacao WHERE ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade_tipo_producao FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
		(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
			(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
				(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
					AND ide_documento_obrigatorio = 9997))));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_tipo_producao WHERE ide_fce_aquicultura_licenca_tipo_atividade IN 
	(SELECT ide_fce_aquicultura_licenca_tipo_atividade FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
		(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN
			(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
				(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				AND ide_documento_obrigatorio = 9997)));

DELETE FROM fce_aquicultura_licenca_tipo_atividade WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_tipo_localizacao_cultivo WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_localizacao_geografica WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca_tipo_atividade_localizacao_geografica WHERE ide_fce_aquicultura_licenca IN 
	(SELECT ide_fce_aquicultura_licenca FROM fce_aquicultura_licenca WHERE ide_fce IN 
		(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			AND ide_documento_obrigatorio = 9997));

DELETE FROM fce_aquicultura_licenca WHERE ide_fce IN 
	(SELECT ide_fce FROM fce  WHERE ide_processo_reenquadramento IN 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		AND ide_documento_obrigatorio = 9997);

DELETE FROM fce  WHERE ide_processo_reenquadramento IN 
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
	AND ide_documento_obrigatorio = 9997;

RETURN 'OK!';    
END; 
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Licenciamento Mineração - Produção por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_licenciamento_mineracao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
DELETE FROM fce_licenciamento_mineral_producao_produto  where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_servidao_mineraria where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_transporte_minerio where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_tipo_residuo where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_tecnica_lavra where ide_fce_licenciamento_mineral  IN
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_substancia_mineral_tipologia where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_sistema_tratamento where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_metodo_lavra where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_emissao_atmosferica where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_destino_residuo where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_metodo_recuperacao where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_exploracao_regime_exploracao where ide_fce_licenciamento_mineral  IN 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_origem_energia where ide_fce_licenciamento_mineral  IN
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from fce_licenciamento_mineral_tipo_app where ide_fce_licenciamento_mineral  IN
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from forma_disposicao_rejeitos where ide_fce_licenciamento_mineral  IN
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from processo_dnpm where ide_fce_licenciamento_mineral  IN
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));

delete from outorga_mineracao where ide_fce_licenciamento_mineral  in 
	(select ide_fce_licenciamento_mineral from fce_licenciamento_mineral  where ide_fce  IN 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000));
		
delete from fce_licenciamento_mineral  where ide_fce  in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000);

delete from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
		and ide_documento_obrigatorio = 3000;
RETURN 'FCE - Licenciamento para Mineração excluído!';    
END; 
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Licenciamento Mineração - Produção por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_linha_energia_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
DECLARE ide_loc_geo_array  integer[] ;
BEGIN
ide_loc_geo_array := ARRAY( (SELECT ide_localizacao_geografica FROM fce_linha_energia_localizacao_geografica WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073))));

DELETE FROM fce_linha_energia_tipo_energia  where ide_fce_linha_energia_tipo_energia in 
	(SELECT ide_fce_linha_energia_tipo_energia FROM fce_linha_energia_tipo_energia WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_tipo_subestacao  where ide_fce_linha_energia_tipo_subestacao in 
	(SELECT ide_fce_linha_energia_tipo_subestacao FROM fce_linha_energia_tipo_subestacao WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_residuo_gerado  where ide_fce_linha_energia_residuo_gerado in 
	(SELECT ide_fce_linha_energia_residuo_gerado FROM fce_linha_energia_residuo_gerado WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_destino_residuo  where ide_fce_linha_energia_destino_residuo in 
	(SELECT ide_fce_linha_energia_destino_residuo FROM fce_linha_energia_destino_residuo WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_localizacao_geografica  where ide_fce_linha_energia_localizacao_geografica in 
	(SELECT ide_fce_linha_energia_localizacao_geografica FROM fce_linha_energia_localizacao_geografica WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia  where ide_fce_linha_energia in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073));

IF array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE FROM dado_geografico where ide_dado_geografico in (
	select ide_dado_geografico from dado_geografico  where ide_localizacao_geografica  in (ide_loc_geo_array[i]));
 END LOOP;
END IF;

IF  array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE from processo_ato_concedido where ide_localizacao_geografica  in  (ide_loc_geo_array[i]);
 END LOOP;
END IF;

IF array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE FROM localizacao_geografica where ide_localizacao_geografica in (ide_loc_geo_array[i]);
 END LOOP;
END IF;

DELETE FROM fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10073;
RETURN 'FCE - Linha de transmissão e distribuição de energia excluído!';
END;
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Localização Geográfica por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_loc_geo_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN

delete from dado_geografico where ide_localizacao_geografica in 
	(select ide_localizacao_geografica from fce_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in  
			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1439));

UPDATE localizacao_geografica SET ind_excluido = true, dtc_exclusao = (SELECT now()) where ide_localizacao_geografica in 
	(select ide_localizacao_geografica from fce_localizacao_geografica where ide_fce in 
 		(select ide_fce from fce where ide_processo_reenquadramento in 
 			(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
 			and ide_documento_obrigatorio = 1439));
	
delete from fce_localizacao_geografica where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
		and ide_documento_obrigatorio = 1439);

delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1439;

delete from documento_obrigatorio_requerimento  where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)	
	and (ide_documento_obrigatorio = 1439 or ide_documento_obrigatorio = 9981);

RETURN 'OK!';    
END; 
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Outorga Aquicultura por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_outorga_aquicultura_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_outorga_localizacao_aquicultura where ide_fce_outorga_localizacao_geografica in 
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in
		(select ide_fce from fce where ide_processo_reenquadramento in
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)	
			and ide_documento_obrigatorio = 1444));

delete from fce_lancamento_efluente_caracteristica where ide_fce_lancamento_efluente in
	(select ide_fce_lancamento_efluente from fce_aquicultura where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			and ide_documento_obrigatorio = 1444) and ide_tipo_aquicultura = 2);

delete from fce_aquicultura_especie where ide_fce_aquicultura in 
	(select ide_fce_aquicultura from fce_aquicultura where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
			and ide_documento_obrigatorio = 1444));

delete from fce_aquicultura_localizacao_geografica where ide_fce_aquicultura in 
	(select ide_fce_aquicultura from fce_aquicultura where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1444));

delete from fce_aquicultura where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)	
		and ide_documento_obrigatorio = 1444);

delete from fce_lancamento_efluente where ide_fce_outorga_localizacao_geografica in 
	(select ide_fce_outorga_localizacao_geografica from fce_outorga_localizacao_geografica where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 1444));

delete from fce_outorga_localizacao_geografica where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	and ide_documento_obrigatorio = 1444);

delete from fce where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
	and ide_documento_obrigatorio = 1444;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
	and (ide_documento_obrigatorio = 1444 or ide_documento_obrigatorio = 9990);
RETURN 'OK!';    
END; 
$function$
;


-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Pulverização por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_pulverizacao_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_pulverizacao where ide_fce in 
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1434);

delete from fce_atividade_area where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 1434);

delete from fce where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
	and ide_documento_obrigatorio = 1434;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in
	(select ide_processo_reenquadramento from processo_reenquadramento WHERE num_processo =$1)
	and (ide_documento_obrigatorio = 1434 or ide_documento_obrigatorio = 9987);
RETURN 'OK!';    
END; 
$function$
;



-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Abastececimento de Água por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_saa_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN			

DELETE FROM eta_tipo_composicao where ide_eta_tipo_composicao in 
	(SELECT ide_eta_tipo_composicao FROM eta_tipo_composicao WHERE ide_fce_saa_localizacao_geografica_eta  in 
		(SELECT ide_fce_saa_localizacao_geografica_eta FROM fce_saa_localizacao_geografica_eta WHERE ide_fce_saa   in 
			(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
					(select ide_fce from fce where ide_processo_reenquadramento in 
						(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
					and ide_documento_obrigatorio = 10071))));
				

DELETE FROM fce_saa_localizacao_geografica_eta  where ide_fce_saa_localizacao_geografica_eta in 
	(SELECT ide_fce_saa_localizacao_geografica_eta FROM fce_saa_localizacao_geografica_eta WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071)));
				
DELETE FROM fce_saa_localizacao_geografica_reservatorio where ide_fce_saa_localizacao_geografica_reservatorio in 
	(SELECT ide_fce_saa_localizacao_geografica_reservatorio FROM fce_saa_localizacao_geografica_reservatorio WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071)));
				
DELETE FROM fce_saa_localizacao_geografica_elevatoria_bruta where ide_fce_saa_localizacao_geografica_elevatoria_bruta in 
	(SELECT ide_fce_saa_localizacao_geografica_elevatoria_bruta FROM fce_saa_localizacao_geografica_elevatoria_bruta WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071)));

DELETE FROM fce_saa_localizacao_geografica_elevatoria_tratada where ide_fce_saa_localizacao_geografica_elevatoria_tratada in 
	(SELECT ide_fce_saa_localizacao_geografica_elevatoria_tratada FROM fce_saa_localizacao_geografica_elevatoria_tratada WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071)));

DELETE FROM fce_saa_localizacao_geografica_dados_concedidos where ide_fce_saa_localizacao_geografica_dados_concedidos in 
	(SELECT ide_fce_saa_localizacao_geografica_dados_concedidos FROM fce_saa_localizacao_geografica_dados_concedidos WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071)));				


DELETE FROM fce_saa where ide_fce_saa in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071));

DELETE FROM fce where ide_processo_reenquadramento in 
					(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 10071;
RETURN 'FCE - Sistema de abastecimento de água!';
END;
$function$
;

-- SEMA - SEIA - PROJETO REENQUADRAMENTO
-- CREATED AT: 2019-08-27
-- AUTHOR: DIEGO RAIAN
-- Remover Formulário de Turismo por Reenquadramento  
CREATE OR REPLACE FUNCTION public.remover_fce_turismo_by_reenquadramento(preenquadramento character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$ 
DECLARE pReenquadramento ALIAS FOR $1; 
BEGIN
delete from fce_turismo_localizacao_geografica where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_categoria_turismo where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_turismo_origem_energia where ide_fce_turismo in
	(select ide_fce_turismo from fce_turismo where ide_fce in 
		(select ide_fce from fce where ide_processo_reenquadramento in 
			(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
				and ide_documento_obrigatorio = 9989));

delete from fce_turismo where ide_fce in
	(select ide_fce from fce where ide_processo_reenquadramento in 
		(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1) 
			and ide_documento_obrigatorio = 9989);

delete from fce where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		and ide_documento_obrigatorio = 9989;

delete from documento_obrigatorio_reenquadramento where ide_processo_reenquadramento in
	(SELECT ide_processo_reenquadramento FROM processo_reenquadramento WHERE num_processo =$1)
		and (ide_documento_obrigatorio = 9989 or ide_documento_obrigatorio = 9986);
RETURN 'OK!';    
END; 
$function$
;


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
