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

