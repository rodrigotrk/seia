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
