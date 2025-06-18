CREATE SEQUENCE tipo_pessoa_juridica_pct_seq;
CREATE TABLE tipo_pessoa_juridica_pct
(
  ide_tipo_pessoa_juridica_pct integer NOT NULL DEFAULT nextval(('tipo_pessoa_juridica_pct_seq'::text)::regclass),
  dsc_tipo_pessoa_juridica_pct character varying(100) NOT NULL,
  dtc_criacao timestamp without time zone NOT NULL,
  ind_excluido boolean NOT NULL,
  dtc_exclusao timestamp without time zone,
  CONSTRAINT pk_tipo_pessoa_juridica_pct PRIMARY KEY (ide_tipo_pessoa_juridica_pct)
);

CREATE SEQUENCE tipo_vinculo_pct_seq;
CREATE TABLE tipo_vinculo_pct
(
  ide_tipo_vinculo_pct integer NOT NULL DEFAULT nextval(('tipo_vinculo_pct_seq'::text)::regclass),
  dsc_tipo_vinculo_pct character varying(100) NOT NULL,
  ind_excluido boolean NOT NULL,
  dtc_criacao timestamp without time zone NOT NULL,
  dtc_exclusao timestamp without time zone,
  CONSTRAINT pk_tipo_vinculo_pct PRIMARY KEY (ide_tipo_vinculo_pct)
);


ALTER TABLE pessoa_imovel ADD COLUMN ide_tipo_vinculo_pct integer;
ALTER TABLE pessoa_imovel ADD COLUMN dsc_tipo_vinculo_pct_outros character varying(100);

ALTER TABLE pessoa_imovel
  ADD CONSTRAINT pessoa_imovel_tipo_vinculo_pct_fk FOREIGN KEY (ide_tipo_vinculo_pct)
      REFERENCES tipo_vinculo_pct (ide_tipo_vinculo_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE SEQUENCE tipo_territorio_pct_seq;
CREATE TABLE tipo_territorio_pct
(
  ide_tipo_territorio_pct integer NOT NULL DEFAULT nextval(('tipo_territorio_pct_seq'::text)::regclass),
  dsc_tipo_territorio_pct character varying(100) NOT NULL,
  ind_excluido boolean NOT NULL,
  dtc_criacao timestamp without time zone NOT NULL,
  dtc_exclusao timestamp without time zone,
  CONSTRAINT pk_ide_tipo_territorio_pct PRIMARY KEY (ide_tipo_territorio_pct)
);



CREATE SEQUENCE tipo_seguimento_pct_seq;
CREATE TABLE tipo_seguimento_pct
(
	ide_tipo_seguimento_pct INTEGER NOT NULL DEFAULT nextval(('tipo_seguimento_pct_seq'::text)::regclass),
	dsc_tipo_seguimento_pct VARCHAR(100) NULL,
	dtc_criacao          timestamp NOT NULL,
	dtc_exclusao         timestamp NULL,
	ind_excluido         Boolean NOT NULL
);
ALTER TABLE tipo_seguimento_pct ADD PRIMARY KEY (ide_tipo_seguimento_pct);

CREATE SEQUENCE pct_imovel_rural_seq;
CREATE TABLE pct_imovel_rural
(
	ide_pct              INTEGER NOT NULL DEFAULT nextval(('pct_imovel_rural_seq'::text)::regclass),
	num_familias         INTEGER NOT NULL,
	ide_imovel_rural     INTEGER NULL,
	ide_tipo_seguimento_pct INTEGER NULL,
	ide_tipo_territorio_pct INTEGER NULL,
	dsc_tipo_seguimento_pct_outros character varying(100) NULL,
	dsc_tipo_territorio_pct_outros character varying(100) NULL
);
ALTER TABLE pct_imovel_rural ADD PRIMARY KEY (ide_pct);
ALTER TABLE pct_imovel_rural ADD CONSTRAINT fk_pct_imovel_rual_ide_imovel_rural FOREIGN KEY (ide_imovel_rural) REFERENCES imovel_rural (ide_imovel_rural);
ALTER TABLE pct_imovel_rural ADD CONSTRAINT fk_pct_imovel_rural_ide_tipo_segmento_pct FOREIGN KEY (ide_tipo_seguimento_pct) REFERENCES tipo_seguimento_pct (ide_tipo_seguimento_pct);
ALTER TABLE pct_imovel_rural ADD CONSTRAINT fk_pct_imovel_rual_ide_tipo_territorio_pct FOREIGN KEY (ide_tipo_territorio_pct) REFERENCES tipo_territorio_pct (ide_tipo_territorio_pct);


--===========================================================================

ALTER TABLE tipo_documento_imovel_rural ADD COLUMN ide_tipo_territorio_pct integer;

ALTER TABLE tipo_documento_imovel_rural
  ADD CONSTRAINT tipo_documento_territorio_pct_fk FOREIGN KEY (ide_tipo_territorio_pct)
      REFERENCES tipo_territorio_pct (ide_tipo_territorio_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--===========================================================================

ALTER TABLE imovel_rural ADD COLUMN num_nirf character varying(50);

ALTER TABLE imovel_rural ADD COLUMN num_termo_autodeclaracao character varying(50);

ALTER TABLE imovel_rural ADD COLUMN ide_localizacao_geografica_pct integer;

ALTER TABLE imovel_rural
  ADD CONSTRAINT imovel_rural_ide_localizacao_geografica_pct_fkey FOREIGN KEY (ide_localizacao_geografica_pct)
      REFERENCES localizacao_geografica (ide_localizacao_geografica) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--============================================================================

CREATE SEQUENCE pct_familia_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE pct_familia
(
  ide_pct_familia integer NOT NULL DEFAULT nextval(('pct_familia_seq'::text)::regclass),
  ide_pct integer NOT NULL,
  ide_pessoa integer NOT NULL,
  dtc_cadastro timestamp without time zone NOT NULL,
  dtc_exclusao timestamp without time zone,
  ind_excluido boolean NOT NULL,
  ide_pessoa_associada integer,
  CONSTRAINT pk_pct_familia PRIMARY KEY (ide_pct_familia),
  CONSTRAINT fk_pct_familia_pct_imovel_rural FOREIGN KEY (ide_pct)
      REFERENCES pct_imovel_rural (ide_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pct_familia_pessoa FOREIGN KEY (ide_pessoa)
      REFERENCES pessoa (ide_pessoa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pct_familia_pessoa_associada FOREIGN KEY (ide_pessoa_associada)
      REFERENCES pessoa (ide_pessoa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE pct_imovel_rural_tipo_seguimento_pct
(
  ide_pct integer NOT NULL,
  ide_tipo_seguimento_pct integer NOT NULL,
  CONSTRAINT pk_pct_imovel_rural_tipo_seguimento_pct PRIMARY KEY (ide_pct, ide_tipo_seguimento_pct),
  CONSTRAINT pct_im_rural_tp_seg_pct_imovel_rural_fkey FOREIGN KEY (ide_pct)
      REFERENCES pct_imovel_rural (ide_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pct_im_rural_tp_seg_tipo_seguimento_pct_fkey FOREIGN KEY (ide_tipo_seguimento_pct)
      REFERENCES tipo_seguimento_pct (ide_tipo_seguimento_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


--===============================================================================================

ALTER TABLE pct_imovel_rural ADD COLUMN ind_aceite Boolean;

CREATE SEQUENCE pessoa_juridica_pct_seq;
CREATE TABLE pessoa_juridica_pct
(
  ide_pessoa_juridica_pct integer NOT NULL DEFAULT nextval(('pessoa_juridica_pct_seq'::text)::regclass),
  ide_pct integer NOT NULL,
  ide_pessoa_juridica integer NOT NULL,
  dtc_cadastro timestamp without time zone NOT NULL,
  dtc_exclusao timestamp without time zone,
  ind_excluido boolean NOT NULL,
  CONSTRAINT pk_pessoa_juridica_pct PRIMARY KEY (ide_pessoa_juridica_pct)
);

ALTER TABLE pessoa_juridica_pct
  ADD CONSTRAINT pessoa_juridica_pct_fk FOREIGN KEY (ide_pct)
      REFERENCES pct_imovel_rural (ide_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE pessoa_juridica_pct
  ADD CONSTRAINT pessoa_juridica_fk FOREIGN KEY (ide_pessoa_juridica)
      REFERENCES pessoa_juridica (ide_pessoa_juridica) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE pct_imovel_rural ADD COLUMN ind_associacao_comunidade Boolean;

ALTER TABLE pessoa_juridica_pct ADD COLUMN ide_tipo_pessoa_juridica_pct INTEGER NOT NULL;
ALTER TABLE pessoa_juridica_pct
  ADD CONSTRAINT pct_imovel_rural_ide_tipo_pessoa_juridica_pct_fk FOREIGN KEY (ide_tipo_pessoa_juridica_pct)
      REFERENCES tipo_pessoa_juridica_pct (ide_tipo_pessoa_juridica_pct) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--===============================================================================================
--====== sprint 5
--CREATE SEQUENCE pessoa_parentesco_seq;
--
--CREATE TABLE parentesco
--(
--  ide_parentesco integer NOT NULL DEFAULT nextval(('pessoa_parentesco_seq'::text)::regclass),
--  dsc_parentesco character varying(40) NOT NULL,
--  dtc_cadastro timestamp without time zone NOT NULL,
--  dtc_exclusao timestamp without time zone,
--  ind_excluido boolean NOT NULL,
--  CONSTRAINT pk_parentesco PRIMARY KEY (ide_parentesco)
--);
--
--ALTER TABLE pct_familia ADD COLUMN ide_parentesco Integer;
--
--ALTER TABLE pct_familia
--  ADD CONSTRAINT pct_familia_parentesco_fk FOREIGN KEY (ide_parentesco)
--      REFERENCES parentesco (ide_parentesco) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
--======
--===============================================================================================
ALTER TABLE tipo_documento_imovel_rural ALTER COLUMN ide_tipo_vinculo_imovel DROP NOT NULL;      

ALTER TABLE imovel_rural ADD COLUMN ide_localizacao_geografica_pct_limite_territorio integer;

ALTER TABLE imovel_rural ADD COLUMN num_sncr character varying(15);

ALTER TABLE imovel_rural
  ADD CONSTRAINT imovel_rural_ide_localizacao_geografica_pct_limite_territorio_fkey FOREIGN KEY (ide_localizacao_geografica_pct_limite_territorio)
      REFERENCES localizacao_geografica (ide_localizacao_geografica) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE pct_imovel_rural  ADD COLUMN ide_contrato_convenio integer;

ALTER TABLE pct_imovel_rural ADD CONSTRAINT fk_pct_imovel_rual_ide_contrato_convenio FOREIGN KEY (ide_contrato_convenio) REFERENCES contrato_convenio (ide_contrato_convenio);

CREATE INDEX idx_pct_imovel_rural_ide_imovel_rural ON pct_imovel_rural USING btree (ide_imovel_rural);
CREATE INDEX idx_pessoa_fisica_contrato_convenio_ide_contrato_convenio ON pessoa_fisica_contrato_convenio USING btree (ide_contrato_convenio);


--=============================================================================================

CREATE OR REPLACE FUNCTION sp_get_imovel_sobreposicao_pct(
    the_geom_imovel geometry,
    ide_imovel_exclusao integer)
  RETURNS SETOF integer[] AS
$BODY$
DECLARE

	V_IND_IMOVEL_BNDES boolean;
	V_CONDICAO varchar;
	V_SQL varchar;

BEGIN

	--Verifica se o imóvel é um cadastro do BNDES
	SELECT INTO V_IND_IMOVEL_BNDES (ide_tipo_cadastro_imovel_rural = 4) FROM imovel_rural WHERE ide_imovel_rural = ide_imovel_exclusao;

	IF V_IND_IMOVEL_BNDES IS TRUE THEN

		RETURN QUERY
			SELECT ARRAY_AGG(ide_imovel_rural) 
			FROM ( 
				SELECT DISTINCT ir.ide_imovel_rural 
				FROM ( 
					SELECT ir.ide_imovel_rural, dg.the_geom 
					FROM imovel_rural ir 
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural 
					LEFT JOIN imovel_rural_sicar irs ON irs.ide_imovel_rural = ir.ide_imovel_rural 
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica 
					WHERE 	
						i.ind_excluido IS FALSE 
						AND ir.ide_tipo_cadastro_imovel_rural = 5
						AND ir.ide_imovel_rural <> ide_imovel_exclusao
						AND dg.the_geom IS NOT NULL 
						AND dg.coord_geo_numerica IS NULL 
						AND (
							( 
								(
									ir.status_cadastro = 3 
									OR (
										ir.status_cadastro = 1 AND ind_deseja_completar_informacoes = true
									)
								) AND ( 
									ir.dtc_primeira_finalizacao::date >= '16-09-2014'::date 
									OR ir.dtc_finalizacao::date >= '16-09-2014'::date
									OR irs.dtc_criacao::date >= '16-09-2014'::date  
								)
							) OR ( 
								ir.status_cadastro = 0 
								AND irs.num_sicar IS NOT NULL 
							) OR ( 
								ir.ide_imovel_rural in (SELECT ide_imovel_rural FROM imovel_suspensao where ind_excluido = false)
							)
						)
				) ir
				JOIN ( 
					SELECT (ST_Dump(ST_Union(the_geom_imovel))).geom AS the_geom 
				) im ON ST_Intersects(im.the_geom, ir.the_geom) 
				ORDER BY ir.ide_imovel_rural
			) t;

	ELSE
		
		RETURN QUERY
			SELECT ARRAY_AGG(ide_imovel_rural) 
			FROM ( 
				SELECT DISTINCT ir.ide_imovel_rural 
				FROM ( 
					SELECT ir.ide_imovel_rural, dg.the_geom 
					FROM imovel_rural ir 
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural 
					LEFT JOIN imovel_rural_sicar irs ON irs.ide_imovel_rural = ir.ide_imovel_rural 
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica 
					WHERE 	
						i.ind_excluido IS FALSE
 						AND ir.ide_tipo_cadastro_imovel_rural = 5
						AND ir.ide_imovel_rural <> ide_imovel_exclusao
						AND dg.the_geom IS NOT NULL 
						AND dg.coord_geo_numerica IS NULL 
						AND (
							(
								ir.status_cadastro = 3 
								OR (
									ir.status_cadastro = 1 
									AND ind_deseja_completar_informacoes = true
								)
							) OR ( 
								ir.status_cadastro = 0 
								AND irs.num_sicar IS NOT NULL 
							) OR ( 
								ir.ide_imovel_rural in (SELECT ide_imovel_rural FROM imovel_suspensao where ind_excluido = false)
							)
						)
				) ir
				JOIN ( 
					SELECT (ST_Dump(ST_Union(the_geom_imovel))).geom AS the_geom 
				) im ON ST_Intersects(im.the_geom, ir.the_geom) 
				ORDER BY ir.ide_imovel_rural
			) t;			

	END IF;

  EXCEPTION
    WHEN OTHERS THEN
      --RAISE NOTICE 'Exception. [ide_imovel_exclusao]: %',ide_imovel_exclusao;
      RETURN QUERY SELECT sp_get_imovel_sobreposicao_old(the_geom_imovel, ide_imovel_exclusao);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION sp_get_imovel_sobreposicao_pct(geometry, integer)
  OWNER TO seia_sema;
COMMENT ON FUNCTION sp_get_imovel_sobreposicao_pct(geometry, integer) IS 'Retorna uma lista de imóveis rurais que sobrepõe a geometria passada por parâmetro, considerando o imóvel que deverá ser excluído da verificação.';


--==================================================================================================

CREATE OR REPLACE FUNCTION sp_valida_imovel_sobreposicao_pct(
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
	--Gera uma lista contendo os identificadores dos imóveis rurais em banco que fazem sobreposição com o imóvel passado por parâmetro (excluindo o próprio)
	V_LISTA_SOBREPOSICAO := sp_get_imovel_sobreposicao_pct(the_geom_imovel, ide_imovel_rural);

	RAISE NOTICE 'ID IMOVEL: %',ide_imovel_rural;
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
					V_PERCENTUAL := ROUND((V_AREA_SOBREPOSICAO/V_AREA_IMOVEL)*100,1);

					RAISE NOTICE 'ID IMOVEL SOBREPOSICAO: % / AREA SOBREPOSICAO: % / AREA TOTAL: % / PERCENTUAL: %',V_IDE_IMOVEL_SOBREP,V_AREA_SOBREPOSICAO,V_AREA_IMOVEL,V_PERCENTUAL;

					--Se o percentual for maior que 0 (zero) aplica a mesma validação para o imóvel rural com sobreposição
					IF (V_PERCENTUAL > 0.0) THEN
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
ALTER FUNCTION sp_valida_imovel_sobreposicao_pct(geometry, integer, boolean, geometry)
  OWNER TO seia_sema;
COMMENT ON FUNCTION sp_valida_imovel_sobreposicao_pct(geometry, integer, boolean, geometry) IS 'Executa a validação de sobreposição do limite do imóvel rural passado por parâmetro, considerando os imóveis rurais já persistidos ou não. Retorna "TRUE" quando não há sobreposições além da tolerância permitida e "FALSE" caso contrário.';

--================================================================================================================================================================================


DROP FUNCTION sp_validacao_geom_tema_temporario2(geometry,integer);

CREATE OR REPLACE FUNCTION sp_validacao_geom_tema_temporario2(
    IN pgeom geometry,
    IN tipo integer,
    OUT ide_loc_sobreposicao integer)
  RETURNS SETOF integer AS
$BODY$
	BEGIN
		IF (tipo = 1) THEN 
			--Imóvel
			RETURN QUERY 
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM imovel_rural ir
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) ir
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(ir.the_geom, t.the_geom);
		ELSIF (tipo = 2) THEN
			--Reserva Legal
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM reserva_legal rl
					JOIN imovel_rural ir ON ir.ide_imovel_rural = rl.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = rl.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
					AND ir.ide_tipo_cadastro_imovel_rural <> 5
				) rl
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(rl.the_geom, t.the_geom);				
		ELSIF (tipo = 3) THEN
			--Área de Preservação Permanente
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM app 
					JOIN imovel_rural ir ON ir.ide_imovel_rural = app.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = app.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND ir.ind_app IS TRUE
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) app
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(app.the_geom, t.the_geom);
		ELSIF (tipo = 4) THEN
			--Atividade Desenvolvida
			RETURN QUERY 
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM area_produtiva ad
					JOIN imovel_rural ir ON ir.ide_imovel_rural = ad.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ad.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND ir.ind_area_produtiva IS TRUE
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) ad
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(ad.the_geom, t.the_geom);
		ELSIF (tipo = 5) THEN
			--Vegetação Nativa
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM vegetacao_nativa vn
					JOIN imovel_rural ir ON ir.ide_imovel_rural = vn.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = vn.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND ir.ind_vegetacao_nativa IS TRUE
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) vn
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(vn.the_geom, t.the_geom);
		ELSIF (tipo = 12) THEN
			--Imóvel Rural Lote (INCRA)
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica_lote
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM imovel_rural ir 
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica_lote
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) irl
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(irl.the_geom, t.the_geom);
		ELSIF (tipo = 13) THEN
			--RPPN
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM imovel_rural_rppn rppn
					JOIN imovel_rural ir ON ir.ide_imovel_rural = rppn.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = rppn.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND ir.ind_rppn IS TRUE
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) rppn
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(rppn.the_geom, t.the_geom);
		ELSIF (tipo = 14) THEN
			--Área Rural Consolidada (INCRA)
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM area_rural_consolidada arc
					JOIN imovel_rural ir ON ir.ide_imovel_rural = arc.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = arc.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					--AND ir.ind_arc IS TRUE
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
				) arc
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(arc.the_geom, t.the_geom);
		ELSIF (tipo = 16) THEN
			--Reserva Legal PCT
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM imovel_rural ir
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = ir.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
					AND ir.ide_tipo_cadastro_imovel_rural = 5
				) rl
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(rl.the_geom, t.the_geom);
		ELSIF (tipo = 17) THEN
			--Comunidade reserva legal PCT
			RETURN QUERY
				SELECT DISTINCT ide_localizacao_geografica
				FROM (
					SELECT dg.ide_localizacao_geografica, the_geom
					FROM reserva_legal rl
					JOIN imovel_rural ir ON ir.ide_imovel_rural = rl.ide_imovel_rural
					JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural
					JOIN dado_geografico dg ON dg.ide_localizacao_geografica = rl.ide_localizacao_geografica
					WHERE i.ind_excluido IS FALSE
					AND ir.status_cadastro IN (1,3)
					AND dg.the_geom IS NOT NULL
					AND dg.coord_geo_numerica IS NULL
					AND ir.ide_tipo_cadastro_imovel_rural = 5
				) rl
				JOIN (
					SELECT  ST_Union(the_geom) AS the_geom
					FROM (
						SELECT (ST_Dump(ST_Union(pgeom))).geom AS the_geom
					) t
				) t
				ON ST_Intersects(rl.the_geom, t.the_geom);	
		ELSE
			RAISE EXCEPTION 'Valor desconhecido para o parâmetro TIPO';
		END IF;

		EXCEPTION
			WHEN OTHERS THEN
				--RAISE NOTICE 'Exception %',tipo;
				RETURN QUERY SELECT sp_validacao_geom_tema_temporario2_old(pgeom, tipo);
	END
	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION sp_validacao_geom_tema_temporario2(geometry, integer)
  OWNER TO seia_sema;
COMMENT ON FUNCTION sp_validacao_geom_tema_temporario2(geometry, integer) IS 'Retorna a lista de identificadores de localização geográfica que intersectam a localização geográfica passada por parâmetro, considerando seu tipo.';

