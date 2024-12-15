------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------ Script da funcionalidade Registro de floresta plantada -------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

DROP SEQUENCE IF EXISTS registro_floresta_producao_seq;
CREATE SEQUENCE registro_floresta_producao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS registro_floresta_producao_responsavel_tecnico_seq ;
CREATE SEQUENCE registro_floresta_producao_responsavel_tecnico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS registro_floresta_producao_imovel_seq;
CREATE SEQUENCE registro_floresta_producao_imovel_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS registro_floresta_producao_imovel_seq;
CREATE SEQUENCE registro_floresta_producao_imovel_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS especie_floresta_seq;
CREATE SEQUENCE especie_floresta_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS natureza_especie_floresta_seq;
CREATE SEQUENCE natureza_especie_floresta_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE if exists registro_floresta_producao_imovel_plantio_seq;
CREATE SEQUENCE registro_floresta_producao_imovel_plantio_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS registro_floresta_producao_especie_producao_seq;
CREATE SEQUENCE registro_floresta_producao_especie_producao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP SEQUENCE IF EXISTS registro_floresta_producao_imovel_especie_producao_seq;
CREATE SEQUENCE registro_floresta_producao_imovel_especie_producao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

  
---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE registro_floresta_producao
(
	ide_registro_floresta_producao INTEGER DEFAULT nextval('registro_floresta_producao_seq'::text::regclass) NOT NULL,
	ind_ciente_termo_compromisso BOOLEAN NOT NULL,
	ind_aceite_responsabilidade BOOLEAN NULL,
	dt_prevista_ultimo_corte DATE NULL,
	ide_ato_declaratorio    INTEGER NOT NULL
);

ALTER TABLE registro_floresta_producao
ADD PRIMARY KEY (ide_registro_floresta_producao);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE registro_floresta_producao_responsavel_tecnico
(
	ide_registro_floresta_producao_responsavel_tecnico integer DEFAULT nextval('registro_floresta_producao_responsavel_tecnico_seq'::text::regclass) NOT NULL,
	ide_registro_floresta_producao INTEGER NULL,
	ide_pessoa_fisica    integer NULL
);

ALTER TABLE registro_floresta_producao_responsavel_tecnico
ADD PRIMARY KEY (ide_registro_floresta_producao_responsavel_tecnico);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE registro_floresta_producao_imovel
(
	ide_registro_floresta_producao_imovel INTEGER DEFAULT nextval('registro_floresta_producao_imovel_seq'::text::regclass) NOT NULL,
	ind_arrendado        BOOLEAN NOT NULL,
	ide_imovel           INTEGER NOT NULL,
	ide_registro_floresta_producao INTEGER NOT NULL
);

ALTER TABLE registro_floresta_producao_imovel
ADD PRIMARY KEY (ide_registro_floresta_producao_imovel);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE registro_floresta_producao_imovel_plantio
(
	ide_registro_floresta_imovel_plantio INTEGER DEFAULT nextval('registro_floresta_producao_imovel_plantio_seq'::text::regclass) NOT NULL,
	val_area_plantio     NUMERIC(13,4) NOT NULL,
	ide_localizacao_geografica INTEGER ,
	ide_registro_floresta_producao_imovel INTEGER NULL,
	dt_inicio_prevista_implantacao DATE NULL,
	dt_fim_prevista_implantacao DATE NULL,
	dt_inicio_periodo_implantacao DATE NULL,
	dt_fim_periodo_implantacao DATE NULL,
	ide_situacao_atual_floresta_producao INTEGER NOT NULL
);

ALTER TABLE registro_floresta_producao_imovel_plantio
ADD PRIMARY KEY (ide_registro_floresta_imovel_plantio);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE situacao_atual_floresta_producao
(
	ide_situacao_atual_floresta_producao INTEGER DEFAULT nextval('situacao_atual_floresta_producao_seq'::text::regclass) NOT NULL,
	des_situacao_floresta_producao VARCHAR(30) NOT NULL,
	dtc_criacao          DATE NULL,
	dtc_exclusao         CHAR(18) NULL,
	ind_excluido         BOOLEAN NOT NULL
);

ALTER TABLE situacao_atual_floresta_producao
ADD PRIMARY KEY (ide_situacao_atual_floresta_producao);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE registro_floresta_producao_imovel_especie_producao
(
	ide_registro_floresta_producao_imovel_especie_producao INTEGER DEFAULT nextval('registro_floresta_producao_imovel_especie_producao_seq'::text::regclass) NOT NULL,
	ide_registro_floresta_producao_imovel INTEGER NULL,
	ide_especie_floresta INTEGER NULL,
	val_ima              NUMERIC(13,4) NOT NULL,
	val_ciclo_corte      NUMERIC(13,4) NOT NULL,
	val_espacamento      NUMERIC(13,4) NOT NULL,
	val_estimativa_volume_total_final NUMERIC(13,4) NOT NULL
);

ALTER TABLE registro_floresta_producao_imovel_especie_producao
ADD PRIMARY KEY (ide_registro_floresta_producao_imovel_especie_producao);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE natureza_especie_floresta
(
	ide_natureza_especie_floresta INTEGER NOT NULL,
	nom_natureza_especie_floresta VARCHAR(50) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_excluido         BOOLEAN NOT NULL
);

ALTER TABLE natureza_especie_floresta
ADD PRIMARY KEY (ide_natureza_especie_floresta);

---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE especie_floresta
(
	ide_especie_floresta INTEGER DEFAULT nextval('especie_floresta_seq'::text::regclass) NOT NULL,
	nom_especie_floresta VARCHAR(50) NOT NULL,
	ide_natureza_especie_floresta INTEGER NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_excluido         BOOLEAN NOT NULL
);

ALTER TABLE especie_floresta
ADD PRIMARY KEY (ide_especie_floresta);

---------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE registro_floresta_producao
ADD CONSTRAINT fk_registro_floresta_producao_ide_ato_declaratorio FOREIGN KEY (ide_ato_declaratorio) REFERENCES ato_declaratorio (ide_ato_declaratorio);

ALTER TABLE registro_floresta_producao_responsavel_tecnico
ADD CONSTRAINT fk_registro_floresta_producao_responsavel_tecnico_ide_registro_f FOREIGN KEY (ide_registro_floresta_producao) REFERENCES registro_floresta_producao (ide_registro_floresta_producao);

ALTER TABLE registro_floresta_producao_responsavel_tecnico
ADD CONSTRAINT fk_registro_floresta_producao_responsavel_tecnico_ide_pessoa_fis FOREIGN KEY (ide_pessoa_fisica) REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE registro_floresta_producao_imovel
ADD CONSTRAINT fk_registro_floresta_producao_imovel_ide_imovel FOREIGN KEY (ide_imovel) REFERENCES imovel (ide_imovel);

ALTER TABLE registro_floresta_producao_imovel
ADD CONSTRAINT fk_registro_floresta_producao_imovel_ide_registro_floresta_produ FOREIGN KEY (ide_registro_floresta_producao) REFERENCES registro_floresta_producao (ide_registro_floresta_producao);

ALTER TABLE registro_floresta_producao_imovel_especie_producao
ADD CONSTRAINT fk_registro_floresta_producao_imovel_especie_producao_ide_regist FOREIGN KEY (ide_registro_floresta_producao_imovel) REFERENCES registro_floresta_producao_imovel (ide_registro_floresta_producao_imovel);

ALTER TABLE especie_floresta
ADD CONSTRAINT fk_especie_floresta_ide_natureza FOREIGN KEY (ide_natureza_especie_floresta) REFERENCES natureza_especie_floresta (ide_natureza_especie_floresta);

ALTER TABLE registro_floresta_producao_imovel_especie_producao
ADD CONSTRAINT fk_registro_floresta_producao_imovel_especie_producao_ide_especi FOREIGN KEY (ide_especie_floresta) REFERENCES especie_floresta (ide_especie_floresta);

ALTER TABLE registro_floresta_producao_imovel_plantio
ADD CONSTRAINT fk_registro_floresta_producao_imovel_plantio_ide_localizacao_geo FOREIGN KEY (ide_localizacao_geografica) REFERENCES localizacao_geografica (ide_localizacao_geografica);

ALTER TABLE registro_floresta_producao_imovel_plantio
ADD CONSTRAINT fk_registro_floresta_producao_imovel_plantio_ide_registro_flores FOREIGN KEY (ide_registro_floresta_producao_imovel) REFERENCES registro_floresta_producao_imovel (ide_registro_floresta_producao_imovel);

ALTER TABLE registro_floresta_producao_imovel_plantio
ADD CONSTRAINT fk_registro_floresta_producao_imovel_plantio_ide_situacao_atual_ FOREIGN KEY (ide_situacao_atual_floresta_producao) REFERENCES situacao_atual_floresta_producao (ide_situacao_atual_floresta_producao);


---------------------------------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION remover_ato_declaratorio_rfp_by_requerimento(n_requerimento character varying)
  RETURNS character AS
$BODY$
DECLARE
    n_requerimento ALIAS FOR $1;
    req RECORD;
BEGIN

FOR req IN SELECT ide_requerimento FROM requerimento WHERE num_requerimento = n_requerimento
	LOOP
		delete from registro_floresta_producao_imovel_plantio rfpip 
			where rfpip.ide_registro_floresta_producao_imovel in (
				select ide_registro_floresta_producao_imovel 
					FROM registro_floresta_producao_imovel rfpi
					inner join registro_floresta_producao rfp on rfpi.ide_registro_floresta_producao = rfp.ide_registro_floresta_producao
					inner join  ato_declaratorio at_ on at_.ide_ato_declaratorio = rfp.ide_ato_declaratorio
					WHERE at_.ide_requerimento = req.ide_requerimento
			);
		
		delete from registro_floresta_producao_imovel_especie_producao ep 
			where ep.ide_registro_floresta_producao_imovel in (
				select ide_registro_floresta_producao_imovel 
					FROM registro_floresta_producao_imovel rfpi
					inner join registro_floresta_producao rfp on rfpi.ide_registro_floresta_producao = rfp.ide_registro_floresta_producao
					inner join  ato_declaratorio at_ on at_.ide_ato_declaratorio = rfp.ide_ato_declaratorio
					WHERE at_.ide_requerimento = req.ide_requerimento
			);
		
		delete from registro_floresta_producao_imovel imovel
			where imovel.ide_registro_floresta_producao in (
				select ide_registro_floresta_producao 
					FROM registro_floresta_producao rfp
					inner join  ato_declaratorio at_ on at_.ide_ato_declaratorio = rfp.ide_ato_declaratorio
					WHERE at_.ide_requerimento = req.ide_requerimento
			);
		
		delete from registro_floresta_producao_responsavel_tecnico resp
			where resp.ide_registro_floresta_producao in (
				select ide_registro_floresta_producao 
					FROM registro_floresta_producao rfp
					inner join  ato_declaratorio at_ on at_.ide_ato_declaratorio = rfp.ide_ato_declaratorio
					WHERE at_.ide_requerimento = req.ide_requerimento
			);
		
		delete from registro_floresta_producao rfp 
			where rfp.ide_registro_floresta_producao in (
				select ide_registro_floresta_producao from registro_floresta_producao rfp
					inner join ato_declaratorio at_ on at_.ide_ato_declaratorio = rfp.ide_ato_declaratorio
					where ide_requerimento = req.ide_requerimento
		);
			
		DELETE FROM ato_declaratorio WHERE ide_requerimento IN
		   (req.ide_requerimento) AND (ide_documento_obrigatorio = 10041);
		   	   
	  END LOOP;    
     RETURN 'RFP - Deletado';    
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
ALTER FUNCTION remover_ato_declaratorio_rfp_by_requerimento(character varying)
  OWNER TO seia_sema;

---------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO situacao_atual_floresta_producao(ide_situacao_atual_floresta_producao,des_situacao_floresta_producao,dtc_criacao,ind_excluido) VALUES (1,'Em Projeto', now(), false);
INSERT INTO situacao_atual_floresta_producao(ide_situacao_atual_floresta_producao,des_situacao_floresta_producao,dtc_criacao,ind_excluido) VALUES (2,'Em Implantação', now(), false);
INSERT INTO situacao_atual_floresta_producao(ide_situacao_atual_floresta_producao,des_situacao_floresta_producao,dtc_criacao,ind_excluido) VALUES (3,'Efetivamente Implantada', now(), false);

---------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho,
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo) VALUES ('Formulário de Registro de Floresta Plantada', 0.000, true,
true, 1,
true);

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo) VALUES (
	(SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio LIKE 'Formulário de Registro de Floresta Plantada'), 
	(SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental LIKE 'RFP'), 
	TRUE
);


---------------------------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO natureza_especie_floresta(ide_natureza_especie_floresta, nom_natureza_especie_floresta, dtc_criacao, ind_excluido, dtc_exclusao)
VALUES (1, 'Nativa',now(),  false, now());

INSERT INTO natureza_especie_floresta(ide_natureza_especie_floresta, nom_natureza_especie_floresta, dtc_criacao, ind_excluido, dtc_exclusao)
VALUES (2, 'Exótica',now(),  false, now());

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (1, 'Acacia mangium / Acácia', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (2, 'Azadirachta indica / Neem', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (3, 'Eucalyptus camaldulensis / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (4, 'Eucalyptus citriodora / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (5, 'Eucalyptus cloeziana / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (6, 'Eucalyptus globulus / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (7, 'Eucalyptus grancam (híbrido) / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (8, 'Eucalyptus grandis / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (9, 'Eucalyptus microcorys / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (10, 'Eucalyptus pellita / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (11, 'Eucalyptus saligna / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (12, 'Eucalyptus urocam (híbrido) / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (13, 'Eucalyptus urograndis (híbrido) / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (14, 'Eucalyptus urophylla / Eucalipto', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (15, 'Khaya ivorensis / Mogno Africano', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (16, 'Leucaena leucocephala / Leucena', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (17, 'Pinus caribaea var. bahamensis / Pinheiro', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (18, 'Pinus caribaea var. caribaea / Pinheiro', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (19, 'Pinus caribaea var. hondurensis / Pinheiro', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (20, 'Pinus elliotti  / Pinheiro', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (21, 'Pinus oocarpa / Pinheiro', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (22, 'Prosopis juliflora / Algaroba', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (23, 'Tectona grandis / Teca', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (24, 'Toona ciliata / Cedro-australiano', 2, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (25, 'Calophyllum brasiliensis / Guanandi', 1, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (26, 'Hevea brasiliensis / Seringueira', 1, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (27, 'Mimosa caesalpiniifolia / Sabiá', 1, now(),  null,   false);

INSERT INTO especie_floresta(ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES (28, 'Swietenia macrophylla / Mogno', 1, now(),  null,   false);


