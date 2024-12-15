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
