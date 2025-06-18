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
