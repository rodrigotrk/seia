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

