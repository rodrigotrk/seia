/*
 * 22/12/2021 - Scripts criados por Kelson Borges para atualização das funções 
 * sp_get_lista_imovel_sobreposto_acima_tolerancia e sp_valida_imovel_sobreposicao no banco de dados, 
 * que estavam falhando na questão de verificação de tolerância ou faltando compatibilidade em certos aspectos.
 * */


-- Function: sp_valida_imovel_sobreposicao(geometry, integer, boolean, geometry)

-- DROP FUNCTION sp_valida_imovel_sobreposicao(geometry, integer, boolean, geometry);

CREATE OR REPLACE FUNCTION sp_valida_imovel_sobreposicao(
    the_geom_imovel geometry,
    ide_imovel_rural integer,
    ind_volta boolean,
    the_geom_temp geometry)
  RETURNS boolean AS
$BODY$
DECLARE
	V_LISTA_SOBREPOSICAO integer[];
	V_GEOM_UNION geometry;
	V_AREA_IMOVEL numeric;
	V_AREA_SOBREPOSICAO numeric;
	V_PERCENTUAL numeric;
	V_VALIDA_VOLTA boolean;
	V_IDE_IMOVEL_SOBREP integer;
	V_GEOM_IMOVEL_SOBREP geometry;
BEGIN
	RAISE NOTICE 'ID IMOVEL: %',ide_imovel_rural;

	--Gera uma lista contendo os identificadores dos imóveis rurais em banco que fazem sobreposição com o imóvel passado por parâmetro (excluindo o próprio)
	V_LISTA_SOBREPOSICAO := sp_get_imovel_sobreposicao(the_geom_imovel, ide_imovel_rural);

	RAISE NOTICE 'LISTA SOBREPOSICAO: %',V_LISTA_SOBREPOSICAO;
	
	--Obtém a geometria unificada de todos os limites dos imóveis rurais da lista de sobreposição
	IF (array_length(V_LISTA_SOBREPOSICAO, 1) > 0) THEN
		V_GEOM_UNION := sp_get_geom_imovel(V_LISTA_SOBREPOSICAO);
	END IF;

	--Se a geometria temporária for passada, acrescenta a geometria unificada de sobreposição
	IF ((the_geom_temp IS NOT NULL) AND (ST_IsEmpty(the_geom_temp) IS FALSE)) THEN
		IF (ST_IsEmpty(V_GEOM_UNION) IS FALSE) THEN
			V_GEOM_UNION := sp_get_uniao(V_GEOM_UNION, the_geom_temp);
		ELSE
			V_GEOM_UNION := the_geom_temp;
		END IF;
	END IF;
	
	--Se houver geometria(s) sobreposta(s), é realizada a análise de percentual
	IF (ST_IsEmpty(V_GEOM_UNION) IS FALSE) THEN
		--Calcula a área do limite do imóvel rural passado por parâmetro
		V_AREA_IMOVEL := ST_Area(the_geom_imovel);
		--Calcula a área de interseção entre a geometria dos imóveis rurais e a geometria do imóvel passado por parâmetro
		V_AREA_SOBREPOSICAO := ST_Area(sp_get_intersecao(the_geom_imovel, V_GEOM_UNION));
		--Calcula a proporção da área comprometida do imóvel rural passado por parâmetro
		V_PERCENTUAL := ROUND((V_AREA_SOBREPOSICAO/V_AREA_IMOVEL)*100,1);

		RAISE NOTICE 'AREA SOBREPOSICAO: % / AREA TOTAL: % / PERCENTUAL: % / TOLERANCIA: %',V_AREA_SOBREPOSICAO,V_AREA_IMOVEL,V_PERCENTUAL,sp_get_tolerancia_sobreposicao(ide_imovel_rural);

		--Se o percentual estiver dentro da margem de tolerância de sobreposição, verifica se é necessário realizar a análise de "volta".
		IF (V_PERCENTUAL <= sp_get_tolerancia_sobreposicao(ide_imovel_rural)) THEN
			--Se o indicador (flag) da "volta" for verdadeiro, é realizada também a validação de sobreposição para cada um dos imóveis rurais listados e já cadastrados em banco.
			IF (ind_volta) THEN
				FOR i IN 1..array_length(V_LISTA_SOBREPOSICAO, 1) LOOP
					--Armazena o identificador do imóvel rural com sobreposição
					V_IDE_IMOVEL_SOBREP := V_LISTA_SOBREPOSICAO[i];
					--Obtém a geometria do limite do imóvel rural com sobreposição
					V_GEOM_IMOVEL_SOBREP := sp_get_geom_imovel(ARRAY[V_IDE_IMOVEL_SOBREP]);
					--Calcula a área do limite do imóvel rural com sobreposição
					V_AREA_IMOVEL := ST_Area(V_GEOM_IMOVEL_SOBREP);
					--Calcula a área de interseção entre a geometria do imóvel passado por parâmetro e do imóvel rural com sobreposição
					V_AREA_SOBREPOSICAO := ST_Area(sp_get_intersecao(the_geom_imovel, V_GEOM_IMOVEL_SOBREP));
					--Calcula a proporção da área comprometida do imóvel rural com sobreposição
					V_PERCENTUAL := ROUND((V_AREA_SOBREPOSICAO/V_AREA_IMOVEL)*100,2);

					RAISE NOTICE 'ID IMOVEL SOBREPOSICAO: % / AREA SOBREPOSICAO: % / AREA TOTAL: % / PERCENTUAL: %',V_IDE_IMOVEL_SOBREP,V_AREA_SOBREPOSICAO,V_AREA_IMOVEL,V_PERCENTUAL;

					--Se o percentual for a partir de 0,50 aplica a mesma validação para o imóvel rural com sobreposição
					IF (V_PERCENTUAL >= 0.50) THEN
						V_VALIDA_VOLTA := sp_valida_imovel_sobreposicao(V_GEOM_IMOVEL_SOBREP, V_IDE_IMOVEL_SOBREP, FALSE, the_geom_imovel);
						--Se a validação de um dos imóveis rurais já cadastrados em banco retornar "FALSO", o imóvel rural passado por parâmetro não será considerado válido para finalizar.
						--Caso contrário, se todos os imóveis rurais listados continuarem válidos, o imóvel rural passado por parâmetro é considerado válido para finalizar.
						IF (V_VALIDA_VOLTA IS FALSE) THEN
							RETURN FALSE;
						END IF;
					END IF;
				END LOOP;
			END IF;

			RETURN TRUE;
		ELSE
			RETURN FALSE;
		END IF;
	ELSE
		RETURN TRUE;
	END IF;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_valida_imovel_sobreposicao(geometry, integer, boolean, geometry)
  OWNER TO seia_sema;
COMMENT ON FUNCTION sp_valida_imovel_sobreposicao(geometry, integer, boolean, geometry) IS 'Executa a validação de sobreposição do limite do imóvel rural passado por parâmetro, considerando os imóveis rurais já persistidos ou não. Retorna "TRUE" quando não há sobreposições além da tolerância permitida e "FALSE" caso contrário.';
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Function: sp_get_lista_imovel_sobreposto_acima_tolerancia(geometry, integer, geometry)

-- DROP FUNCTION sp_get_lista_imovel_sobreposto_acima_tolerancia(geometry, integer, geometry);

CREATE OR REPLACE FUNCTION sp_get_lista_imovel_sobreposto_acima_tolerancia(
    the_geom_imovel geometry,
    ide_imovel_rural integer,
    the_geom_temp geometry)
  RETURNS text AS
$BODY$
DECLARE
	V_LISTA_SOBREPOSICAO integer[];
	V_GEOM_UNION geometry;
	V_AREA_IMOVEL numeric;
	V_AREA_SOBREPOSICAO numeric;
	V_PERCENTUAL numeric;
	V_IDE_IMOVEL_SOBREP integer;
	V_GEOM_IMOVEL_SOBREP geometry;
	V_LISTA_IMOVEL_SOBREPOSTO integer[];
BEGIN
	RAISE NOTICE 'ID IMOVEL: %',ide_imovel_rural;
	
	--Gera uma lista contendo os identificadores dos imóveis rurais em banco que fazem sobreposição com o imóvel passado por parâmetro (excluindo o próprio)
	V_LISTA_SOBREPOSICAO := sp_get_imovel_sobreposicao(the_geom_imovel, ide_imovel_rural);
	
	RAISE NOTICE 'LISTA SOBREPOSICAO: %',V_LISTA_SOBREPOSICAO;
	
	--Obtém a geometria unificada de todos os limites dos imóveis rurais da lista de sobreposição
	V_GEOM_UNION := sp_get_geom_imovel(V_LISTA_SOBREPOSICAO);

	--Se a geometria temporária for passada, acrescenta a geometria unificada de sobreposição
	IF ((the_geom_temp IS NOT NULL) AND (ST_IsEmpty(the_geom_temp) IS FALSE)) THEN
		V_GEOM_UNION := sp_get_uniao(V_GEOM_UNION, the_geom_temp);
	END IF;
	
	--Calcula a área do limite do imóvel rural passado por parâmetro
	V_AREA_IMOVEL := ST_Area(the_geom_imovel);
	--Calcula a área de interseção entre a geometria dos imóveis rurais e a geometria do imóvel passado por parâmetro
	V_AREA_SOBREPOSICAO := ST_Area(sp_get_intersecao(the_geom_imovel, V_GEOM_UNION));
	--Calcula a proporção da área comprometida do imóvel rural passado por parâmetro
	V_PERCENTUAL := ROUND((V_AREA_SOBREPOSICAO/V_AREA_IMOVEL)*100,1);

	RAISE NOTICE 'AREA SOBREPOSICAO: % / AREA TOTAL: % / PERCENTUAL: % / TOLERANCIA: %',V_AREA_SOBREPOSICAO,V_AREA_IMOVEL,V_PERCENTUAL,sp_get_tolerancia_sobreposicao(ide_imovel_rural);
	
	--Se o percentual não estiver dentro da margem de tolerância retorna V_LISTA_SOBREPOSICAO.
	IF (V_PERCENTUAL > sp_get_tolerancia_sobreposicao(ide_imovel_rural)) THEN
		RETURN array_to_string(V_LISTA_SOBREPOSICAO,',');
	END IF;
	
	--Se o percentual estiver dentro da margem de tolerância, é realizada a validação de sobreposição para cada um dos imóveis rurais listados e já cadastrados em banco.
	FOR i IN 1..array_length(V_LISTA_SOBREPOSICAO, 1) LOOP
		--Armazena o identificador do imóvel rural com sobreposição
		V_IDE_IMOVEL_SOBREP := V_LISTA_SOBREPOSICAO[i];
		--Obtém a geometria do limite do imóvel rural com sobreposição
		V_GEOM_IMOVEL_SOBREP := sp_get_geom_imovel(ARRAY[V_IDE_IMOVEL_SOBREP]);
		--Calcula a área do limite do imóvel rural com sobreposição
		V_AREA_IMOVEL := ST_Area(V_GEOM_IMOVEL_SOBREP);
		--Calcula a área de interseção entre a geometria do imóvel passado por parâmetro e do imóvel rural com sobreposição
		V_AREA_SOBREPOSICAO := ST_Area(sp_get_intersecao(the_geom_imovel, V_GEOM_IMOVEL_SOBREP));
		--Calcula a proporção da área comprometida do imóvel rural com sobreposição
		V_PERCENTUAL := ROUND((V_AREA_SOBREPOSICAO/V_AREA_IMOVEL)*100,2);
		
		RAISE NOTICE 'VOLTA - ID IMOVEL SOBREPOSICAO: % / AREA SOBREPOSICAO: % / AREA TOTAL: % / PERCENTUAL: %',V_IDE_IMOVEL_SOBREP,V_AREA_SOBREPOSICAO,V_AREA_IMOVEL,V_PERCENTUAL;
		
		--Se o percentual for a partir de 0,50 aplica a mesma validação para o imóvel rural com sobreposição
		IF (V_PERCENTUAL >= 0.50) THEN
			--Se a validação de um dos imóveis rurais já cadastrados em banco retornar "FALSO", o imóvel rural será adicionado na lista de retorno, 
			--porque este imóvel será invalidado se o imóvel rural passado por parâmetro sobrepor ele.
			IF (sp_valida_imovel_sobreposicao(V_GEOM_IMOVEL_SOBREP, V_IDE_IMOVEL_SOBREP, FALSE, the_geom_imovel) IS FALSE) THEN
				V_LISTA_IMOVEL_SOBREPOSTO := V_LISTA_IMOVEL_SOBREPOSTO || V_IDE_IMOVEL_SOBREP;
			END IF;
		END IF;
	END LOOP;

	RETURN array_to_string(V_LISTA_IMOVEL_SOBREPOSTO,',');
END
$BODY$
LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION sp_get_lista_imovel_sobreposto_acima_tolerancia(geometry, integer, geometry) OWNER TO seia_sema;
COMMENT ON FUNCTION sp_get_lista_imovel_sobreposto_acima_tolerancia(geometry, integer, geometry) IS 'Executa a validação de sobreposição do limite do imóvel rural passado por parâmetro, considerando os imóveis rurais já persistidos ou não. Retorna a lista de imóveis sobrepostos pela geometria passada por parâmetro que impede a finalização do imóvel passado por parametro, considerando as regra de percentual de sobreposição.';
