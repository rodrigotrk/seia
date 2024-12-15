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
