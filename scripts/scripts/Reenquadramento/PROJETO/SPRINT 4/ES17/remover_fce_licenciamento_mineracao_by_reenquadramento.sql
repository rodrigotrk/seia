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
