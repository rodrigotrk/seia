CREATE OR REPLACE FUNCTION atualizar_taxas_parametro_calculo(nomeAtoAmbiental character varying, nomeTipologia character varying, nomeFinalidade character varying, classe INTEGER, areaMinima numeric(10,2), areaMaxima numeric(10,2), valorTaxa numeric(10,2)) RETURNS void AS
	
	$BODY$ 
	DECLARE 
		nomeAtoAmbiental ALIAS FOR $1;
		nomeTipologia ALIAS FOR $2;
		nomeFinalidade ALIAS FOR $3;
		classe ALIAS FOR $4;
		areaMinima ALIAS FOR $5;
		areaMaxima ALIAS FOR $6;
		valorTaxa ALIAS FOR $7;
		
		recAtoAmbiental INTEGER;
		recTipologia INTEGER;
		recFinalidade INTEGER;
		recParametroCalculo INTEGER[];
		
	BEGIN
		--Busca alguns filtros
		if nomeAtoAmbiental is not null then
			recAtoAmbiental := (select ide_ato_ambiental from ato_ambiental where nom_ato_ambiental ILIKE nomeAtoAmbiental);
		end if;
		
		if nomeTipologia is not null then
			recTipologia := (select ide_tipologia from tipologia where des_tipologia ILIKE nomeTipologia 
				and ind_excluido = false and ind_possui_filhos = false);
		end if;
		
		if nomeFinalidade is not null then
			recFinalidade := (select ide_tipo_finalidade_uso_agua from tipo_finalidade_uso_agua 
				where nom_tipo_finalidade_uso_agua ILIKE nomeFinalidade and ind_ativo = true);
		end if;
		
		--Busca o parametro_calculo de acordo com os filtros passados:
		
		--Ato Ambiental + Tipologia + Finalidade + Area Maxima
		if recAtoAmbiental is not null and recTipologia is not null and recFinalidade is not null and areaMinima is null and areaMaxima is not null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua = recFinalidade
				and area_minima is null 
				and area_maxima = areaMaxima);
		end if;
		
		--Ato Ambiental + Tipologia + Finalidade + Ambas areas
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and recFinalidade is not null 
			and areaMinima is not null and areaMaxima is not null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua = recFinalidade
				and area_minima = areaMinima 
				and area_maxima = areaMaxima);
		end if;
		
		--Ato Ambiental + Tipologia + Finalidade + Area Minima
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and recFinalidade is not null 
			and areaMinima is not null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua = recFinalidade
				and area_minima = areaMinima 
				and area_maxima is null);
		end if;
		
		--Ato Ambiental + Tipologia + Finalidade + Classe
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and recFinalidade is not null 
			and classe is not null and areaMinima is null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua = recFinalidade
				and ide_classe = classe);
		end if;
		
		--Ato Ambiental + Tipologia + Finalidade
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and recFinalidade is not null 
			and areaMinima is null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua = recFinalidade);
		end if;
		
		--Ato Ambiental + Tipologia + Classe
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and classe is not null 
			and areaMinima is null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true 
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_classe = classe);
		end if;
		
		--Ato Ambiental + Tipologia
		if recParametroCalculo is null and recAtoAmbiental is not null and recTipologia is not null and areaMinima is null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia = recTipologia
				and ide_tipo_finalidade_uso_agua is null
				and ide_classe is null);
		end if;
		
		--Ato Ambiental
		if recParametroCalculo is null and recAtoAmbiental is not null and areaMinima is null and areaMaxima is null
		then
			recParametroCalculo := array(select ide_parametro_calculo from parametro_calculo where ind_ativo = true 
				and ide_ato_ambiental = recAtoAmbiental
				and ide_tipologia is null
				and ide_tipo_finalidade_uso_agua is null);
		end if;
		
		--Inativa o parametro_calculo encontrado
		if recParametroCalculo is not null
		then
			update parametro_calculo set ind_ativo = false where ide_parametro_calculo = any(recParametroCalculo);
		end if;
		
		--Insere o novo parametro_calculo
		INSERT INTO parametro_calculo(
		ide_ato_ambiental, 
		ide_tipologia, 
		ide_tipo_finalidade_uso_agua, 
		area_minima, 
		area_maxima, 
		ide_classe, 
		valor_taxa, 
		vazao_minima, 
		vazao_maxima, 
		num_ufir, 
		dtc_criacao, 
		ind_boleto, 
		ind_ativo, 
		fator_multiplicador, 
		ide_tipo_criadouro_fauna, 
		ide_bioma) 
		
		VALUES (
		recAtoAmbiental,
		recTipologia,
		recFinalidade,
		areaMinima,
		areaMaxima,
		classe,
		valorTaxa,
		null,
		null,
		null,
		now(),
		true,
		true,
		null,
		null,
		null);
	END;
	$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

ALTER FUNCTION atualizar_taxas_parametro_calculo(nomeAtoAmbiental character varying, nomeTipologia character varying, nomeFinalidade character varying, 
	classe INTEGER, areaMinima numeric(10,2), areaMaxima numeric(10,2), valorTaxa numeric(10,2)) 
OWNER TO seia_sema;
