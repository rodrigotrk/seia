
CREATE SEQUENCE fce_energia_solar_ide_fce_energia_solar_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_solar_ide_fce_energia_solar_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_solar_ide_fce_energia_solar_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_solar_ide_fce_energia_solar_seq TO consulta;


CREATE SEQUENCE fce_energia_solar_usina_ide_fce_energia_solar_usina_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_solar_usina_ide_fce_energia_solar_usina_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_solar_usina_ide_fce_energia_solar_usina_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_solar_usina_ide_fce_energia_solar_usina_seq TO consulta;



CREATE SEQUENCE fce_energia_eolica_ide_fce_energia_eolica_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_eolica_ide_fce_energia_eolica_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_eolica_ide_fce_energia_eolica_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_eolica_ide_fce_energia_eolica_seq TO consulta;


CREATE SEQUENCE fce_energia_eolica_licenca_previa_ide_fce_energia_eolica_licenca_previa_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_eolica_licenca_previa_ide_fce_energia_eolica_licenca_previa_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_eolica_licenca_previa_ide_fce_energia_eolica_licenca_previa_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_eolica_licenca_previa_ide_fce_energia_eolica_licenca_previa_seq TO consulta;



CREATE SEQUENCE fce_energia_eolica_parque_ide_fce_energia_eolica_parque_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_eolica_parque_ide_fce_energia_eolica_parque_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_eolica_parque_ide_fce_energia_eolica_parque_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_eolica_parque_ide_fce_energia_eolica_parque_seq TO consulta;


CREATE SEQUENCE fce_energia_eolica_parque_aerogerador_ide_fce_energia_eolica_aerogerador_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_eolica_parque_aerogerador_ide_fce_energia_eolica_aerogerador_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_eolica_parque_aerogerador_ide_fce_energia_eolica_aerogerador_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_eolica_parque_aerogerador_ide_fce_energia_eolica_aerogerador_seq TO consulta;


CREATE SEQUENCE fce_energia_termoeletrica_ide_fce_energia_termoeletrica_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_termoeletrica_ide_fce_energia_termoeletrica_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_termoeletrica_ide_fce_energia_termoeletrica_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_termoeletrica_ide_fce_energia_termoeletrica_seq TO consulta;


CREATE SEQUENCE fce_energia_termoeletrica_unidade_ide_fce_energia_termoeletrica_unidade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_termoeletrica_unidade_ide_fce_energia_termoeletrica_unidade_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_termoeletrica_unidade_ide_fce_energia_termoeletrica_unidade_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_termoeletrica_unidade_ide_fce_energia_termoeletrica_unidade_seq TO consulta;


CREATE SEQUENCE fce_energia_termoeletrica_unidade_combustivel_ide_fce_energia_termoeletrica_unidade_combustivel_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_termoeletrica_unidade_combustivel_ide_fce_energia_termoeletrica_unidade_combustivel_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_termoeletrica_unidade_combustivel_ide_fce_energia_termoeletrica_unidade_combustivel_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_termoeletrica_unidade_combustivel_ide_fce_energia_termoeletrica_unidade_combustivel_seq TO consulta;


CREATE SEQUENCE fce_energia_termoeletrica_unidade_gerador_ide_fce_energia_termoeletrica_unidade_gerador_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE fce_energia_termoeletrica_unidade_gerador_ide_fce_energia_termoeletrica_unidade_gerador_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE fce_energia_termoeletrica_unidade_gerador_ide_fce_energia_termoeletrica_unidade_gerador_seq TO seia_sema;
GRANT SELECT ON SEQUENCE fce_energia_termoeletrica_unidade_gerador_ide_fce_energia_termoeletrica_unidade_gerador_seq TO consulta;


CREATE SEQUENCE FCE_ENERGIA_IDE_FCE_ENERGIA_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE FCE_ENERGIA_IDE_FCE_ENERGIA_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE FCE_ENERGIA_IDE_FCE_ENERGIA_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE FCE_ENERGIA_IDE_FCE_ENERGIA_SEQ TO consulta;


CREATE SEQUENCE FINALIDADE_GERACAO_ENERGIA_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE FINALIDADE_GERACAO_ENERGIA_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE FINALIDADE_GERACAO_ENERGIA_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE FINALIDADE_GERACAO_ENERGIA_SEQ TO consulta;


CREATE SEQUENCE tipo_combustivel_ide_tipo_combustivel_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE tipo_combustivel_ide_tipo_combustivel_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE tipo_combustivel_ide_tipo_combustivel_seq TO seia_sema;
GRANT SELECT ON SEQUENCE tipo_combustivel_ide_tipo_combustivel_seq TO consulta;


CREATE SEQUENCE FCE_ENERGIA_FINALIDADE_IDE_FCE_ENERGIA_FINALIDADE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE FCE_ENERGIA_FINALIDADE_IDE_FCE_ENERGIA_FINALIDADE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE FCE_ENERGIA_FINALIDADE_IDE_FCE_ENERGIA_FINALIDADE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE FCE_ENERGIA_FINALIDADE_IDE_FCE_ENERGIA_FINALIDADE_SEQ TO consulta;



CREATE TABLE FCE_ENERGIA
(
	IDE_FCE_ENERGIA      INTEGER NOT NULL,
	DES_IDENTIFICACAO_EMPREENDIMENTO VARCHAR(100) NOT NULL,
	VAL_AREA             NUMERIC(10,4) NOT NULL,
	NUM_PROCESSO_OUTORGA VARCHAR(50) NULL,
	NUM_PROCESSO_ASV     VARCHAR(50) NULL,
	IDE_FCE              INTEGER NOT NULL,
	IDE_LOCALIZACAO_GEOGRAFICA INTEGER NULL,
	IND_ASV              BOOLEAN NOT NULL,
	IND_CAPTACAO         BOOLEAN NOT NULL
);

ALTER TABLE FCE_ENERGIA
ADD PRIMARY KEY (IDE_FCE_ENERGIA);


ALTER TABLE FCE_ENERGIA
ADD CONSTRAINT FK_FCE_ENERGIA_FCE FOREIGN KEY (IDE_FCE) REFERENCES FCE (IDE_FCE);



ALTER TABLE FCE_ENERGIA
ADD CONSTRAINT FK_FCE_ENERGIA_LOCALIZACAO_GEOGRAFICA FOREIGN KEY (IDE_LOCALIZACAO_GEOGRAFICA) REFERENCES LOCALIZACAO_GEOGRAFICA (IDE_LOCALIZACAO_GEOGRAFICA);



CREATE TABLE FCE_ENERGIA_EOLICA
(
	IDE_FCE_ENERGIA_EOLICA INTEGER NOT NULL,
	IDE_FCE_ENERGIA      INTEGER NULL,
	IND_LICENCA_PREVIA   BOOLEAN NOT NULL
);


ALTER TABLE FCE_ENERGIA_EOLICA
ADD PRIMARY KEY (IDE_FCE_ENERGIA_EOLICA);

ALTER TABLE FCE_ENERGIA_EOLICA
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_FCE_ENERGIA FOREIGN KEY (IDE_FCE_ENERGIA) REFERENCES FCE_ENERGIA (IDE_FCE_ENERGIA);




CREATE TABLE FCE_ENERGIA_SOLAR
(
	IDE_FCE_ENERGIA_SOLAR INTEGER NOT NULL,
	IDE_FCE_ENERGIA      INTEGER NULL
);



ALTER TABLE FCE_ENERGIA_SOLAR
ADD PRIMARY KEY (IDE_FCE_ENERGIA_SOLAR);

ALTER TABLE FCE_ENERGIA_SOLAR
ADD CONSTRAINT FK_FCE_ENERGIA_SOLAR_FCE_ENERGIA FOREIGN KEY (IDE_FCE_ENERGIA) REFERENCES FCE_ENERGIA (IDE_FCE_ENERGIA);





CREATE TABLE FCE_ENERGIA_TERMOELETRICA
(
	IDE_FCE_ENERGIA_TERMOELETRICA INTEGER NOT NULL,
	IDE_FCE_ENERGIA      INTEGER NULL
);



ALTER TABLE FCE_ENERGIA_TERMOELETRICA
ADD PRIMARY KEY (IDE_FCE_ENERGIA_TERMOELETRICA);

ALTER TABLE FCE_ENERGIA_TERMOELETRICA
ADD CONSTRAINT FK_FCE_ENERGIA_TERMOELETRICA_FCE_ENERGIA FOREIGN KEY (IDE_FCE_ENERGIA) REFERENCES FCE_ENERGIA (IDE_FCE_ENERGIA);




CREATE TABLE FINALIDADE_GERACAO_ENERGIA
(
	IDE_FINALIDADE_GERACAO_ENERGIA INTEGER NOT NULL,
	DES_FINALIDADE_GERACAO_ENERGIA VARCHAR(20) NOT NULL,
	IND_ATIVO            BOOLEAN NOT NULL,
	DTC_CRIACAO          DATETIME NOT NULL,
	DTC_EXCLUSAO         DATETIME NULL
);



ALTER TABLE FINALIDADE_GERACAO_ENERGIA
ADD PRIMARY KEY (IDE_FINALIDADE_GERACAO_ENERGIA);



CREATE TABLE FCE_ENERGIA_FINALIDADE
(
	IDE_FCE_ENERGIA      INTEGER NOT NULL,
	IDE_FINALIDADE_GERACAO_ENERGIA INTEGER NOT NULL,
	IDE_FCE_ENERGIA_FINALIDADE INTEGER NOT NULL
);



ALTER TABLE FCE_ENERGIA_FINALIDADE
ADD PRIMARY KEY (IDE_FCE_ENERGIA_FINALIDADE,IDE_FCE_ENERGIA,IDE_FINALIDADE_GERACAO_ENERGIA);


ALTER TABLE FCE_ENERGIA_FINALIDADE
ADD CONSTRAINT FK_FCE_ENERGIA_FINALIDADE_FCE_ENERGIA FOREIGN KEY (IDE_FCE_ENERGIA) REFERENCES FCE_ENERGIA (IDE_FCE_ENERGIA);



ALTER TABLE FCE_ENERGIA_FINALIDADE
ADD CONSTRAINT FK_FCE_ENERGIA_FINALIDADE_FINALIDADE_GERACAO_ENERGIA FOREIGN KEY (IDE_FINALIDADE_GERACAO_ENERGIA) REFERENCES FINALIDADE_GERACAO_ENERGIA (IDE_FINALIDADE_GERACAO_ENERGIA);

alter table FCE_ENERGIA_FINALIDADE alter column ide_fce_energia_finalidade set DEFAULT nextval(('FCE_ENERGIA_FINALIDADE_IDE_FCE_ENERGIA_FINALIDADE_SEQ'::text)::regclass);


CREATE TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE
(
	IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE INTEGER NOT NULL,
	DES_IDENTIFICACAO_UNIDADE VARCHAR(20) NOT NULL,
	IND_SISTEMA_CONTROLE_EMISSAO BOOLEAN NOT NULL,
	VAL_EFICIENCIA_CONTROLE_EMISSAO NUMERIC(10,2) NULL,
	IDE_FCE_ENERGIA_TERMOELETRICA INTEGER NULL,
	IDE_LOCALIZACAO_GEOGRAFICA INTEGER NULL,
	VAL_AREA_UNIDADE     NUMERIC(10,4) NOT NULL
);



ALTER TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE
ADD PRIMARY KEY (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE);


ALTER TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE
ADD CONSTRAINT FK_FCE_ENERGIA_TERMOELETRICA_UNIDADE_FCE_ENERGIA_TERMOELETRICA FOREIGN KEY (IDE_FCE_ENERGIA_TERMOELETRICA) REFERENCES FCE_ENERGIA_TERMOELETRICA (IDE_FCE_ENERGIA_TERMOELETRICA);



ALTER TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE
ADD CONSTRAINT FK_FCE_ENERGIA_TERMOELETRICA_UNIDADE_LOCALIZACAO_GEOGRAFICA FOREIGN KEY (IDE_LOCALIZACAO_GEOGRAFICA) REFERENCES LOCALIZACAO_GEOGRAFICA (IDE_LOCALIZACAO_GEOGRAFICA);




CREATE TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR
(
	IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR INTEGER NOT NULL,
	DES_IDENTIFICACAO_GERADOR VARCHAR(20) NULL,
	VAL_POTENCIA_GERADOR NUMERIC(10,2) NULL,
	IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE INTEGER NULL
);



ALTER TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR
ADD PRIMARY KEY (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR);


ALTER TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR
ADD CONSTRAINT FK_FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR_FCE_UNIDADE FOREIGN KEY (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE) REFERENCES FCE_ENERGIA_TERMOELETRICA_UNIDADE (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE);

CREATE TABLE FCE_ENERGIA_TERMOELETRICA_UNIDADE_COMBUSTIVEL
(
   IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_COMBUSTIVEL integer NOT NULL, 
   IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE integer, 
   IDE_TIPO_COMBUSTIVEL integer, 
   CONSTRAINT pk_fce_energia_termoeletrica_unidade_combustivel PRIMARY KEY (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_COMBUSTIVEL), 
   CONSTRAINT fk_termo_unidade FOREIGN KEY (IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE) REFERENCES fce_energia_termoeletrica_unidade (ide_fce_energia_termoeletrica_unidade) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT fk_termo_combustivel FOREIGN KEY (IDE_TIPO_COMBUSTIVEL) REFERENCES tipo_combustivel (ide_tipo_combustivel) ON UPDATE NO ACTION ON DELETE NO ACTION
) 


CREATE TABLE TIPO_COMBUSTIVEL
(
	IDE_TIPO_COMBUSTIVEL INTEGER NOT NULL,
	DES_TIPO_COMBUSTIVEL CHAR(18) NULL,
	IND_ATIVO            BOOLEAN NOT NULL,
	DTC_CRIACAO          DATETIME NOT NULL,
	DTC_EXCLUSAO         DATETIME NULL
);


ALTER TABLE TIPO_COMBUSTIVEL
ADD PRIMARY KEY (IDE_TIPO_COMBUSTIVEL);




CREATE TABLE FCE_ENERGIA_SOLAR_USINA
(
	IDE_FCE_ENERGIA_SOLAR_USINA INTEGER NOT NULL,
	DES_IDENTIFICACAO_USINA VARCHAR(20) NULL,
	VAL_POTENCIA_USINA   NUMERIC(10,2) NULL,
	IDE_FCE_ENERGIA_SOLAR INTEGER NULL,
	IDE_LOCALIZACAO_GEOGRAFICA INTEGER NULL,
	VAL_AREA_USINA       NUMERIC(10,4) NULL
);


ALTER TABLE FCE_ENERGIA_SOLAR_USINA
ADD PRIMARY KEY (IDE_FCE_ENERGIA_SOLAR_USINA);

ALTER TABLE FCE_ENERGIA_SOLAR_USINA
ADD CONSTRAINT FK_FCE_ENERGIA_SOLAR_USINA_FCE_ENERGIA_SOLAR FOREIGN KEY (IDE_FCE_ENERGIA_SOLAR) REFERENCES FCE_ENERGIA_SOLAR (IDE_FCE_ENERGIA_SOLAR);

ALTER TABLE FCE_ENERGIA_SOLAR_USINA
ADD CONSTRAINT FK_FCE_ENERGIA_SOLAR_USINA_LOCALIZACAO_GEOGRAFICA FOREIGN KEY (IDE_LOCALIZACAO_GEOGRAFICA) REFERENCES LOCALIZACAO_GEOGRAFICA (IDE_LOCALIZACAO_GEOGRAFICA);



CREATE TABLE FCE_ENERGIA_EOLICA_LICENCA_PREVIA
(
	IDE_FCE_ENERGIA_EOLICA INTEGER NOT NULL,
	NUM_PARQUES          INTEGER NOT NULL,
	QTD_GERAL_AEROGERADOR INTEGER NULL,
	VAL_POTENCIA_AEROGERADOR CHAR(18) NULL
);



ALTER TABLE FCE_ENERGIA_EOLICA_LICENCA_PREVIA
ADD PRIMARY KEY (IDE_FCE_ENERGIA_EOLICA);



ALTER TABLE FCE_ENERGIA_EOLICA_LICENCA_PREVIA
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_LP_FCE_ENERGIA_EOLICA FOREIGN KEY (IDE_FCE_ENERGIA_EOLICA) REFERENCES FCE_ENERGIA_EOLICA (IDE_FCE_ENERGIA_EOLICA);



CREATE TABLE FCE_ENERGIA_EOLICA_PARQUE
(
	IDE_FCE_ENERGIA_EOLICA_PARQUE INTEGER NOT NULL,
	DES_IDENTIFICADOR_PARQUE VARCHAR(20) NOT NULL,
	VAL_AREA_PARQUE      NUMERIC(10,4) NOT NULL,
	IDE_LOCALIZACAO_GEOGRAFICA INTEGER NULL,
	IDE_FCE_ENERGIA_EOLICA INTEGER NULL
);



ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE
ADD PRIMARY KEY (IDE_FCE_ENERGIA_EOLICA_PARQUE);

ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_PARQUE_LOCALIZACAO_GEOGRAFICA FOREIGN KEY (IDE_LOCALIZACAO_GEOGRAFICA) REFERENCES LOCALIZACAO_GEOGRAFICA (IDE_LOCALIZACAO_GEOGRAFICA);


ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_PARQUE_FCE_ENERGIA_EOLICA FOREIGN KEY (IDE_FCE_ENERGIA_EOLICA) REFERENCES FCE_ENERGIA_EOLICA (IDE_FCE_ENERGIA_EOLICA);




CREATE TABLE FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR
(
	IDE_FCE_ENERGIA_EOLICA_AEROGERADOR INTEGER NOT NULL,
	DES_IDENTIFICADOR_AEROGERADOR VARCHAR(20) NOT NULL,
	VAL_POTENCIA_AEROGERADOR NUMERIC(10,2) NOT NULL,
	VAL_ALTURA           NUMERIC(10,2) NOT NULL,
	IDE_FCE_ENERGIA_EOLICA_PARQUE INTEGER NULL,
	IDE_LOCALIZACAO_GEOGRAFICA INTEGER NULL
);



ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR
ADD PRIMARY KEY (IDE_FCE_ENERGIA_EOLICA_AEROGERADOR);



ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR_FCE_ENERGIA_EOLICA_PARQUE FOREIGN KEY (IDE_FCE_ENERGIA_EOLICA_PARQUE) REFERENCES FCE_ENERGIA_EOLICA_PARQUE (IDE_FCE_ENERGIA_EOLICA_PARQUE);



ALTER TABLE FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR
ADD CONSTRAINT FK_FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR_LOCALIZACAO_GEOGRAFICA FOREIGN KEY (IDE_LOCALIZACAO_GEOGRAFICA) REFERENCES LOCALIZACAO_GEOGRAFICA (IDE_LOCALIZACAO_GEOGRAFICA);



INSERT INTO tipo_combustivel (ide_tipo_combustivel, des_tipo_combustivel, ind_ativo, dtc_criacao, dtc_exclusao) VALUES (1, 'Gás Natural', true, current_date, null);
INSERT INTO tipo_combustivel (ide_tipo_combustivel, des_tipo_combustivel, ind_ativo, dtc_criacao, dtc_exclusao) VALUES (2, 'Óleo Combustível', true, current_date, null);
INSERT INTO tipo_combustivel (ide_tipo_combustivel, des_tipo_combustivel, ind_ativo, dtc_criacao, dtc_exclusao) VALUES (3, 'Biomassa', true, current_date, null);
INSERT INTO tipo_combustivel (ide_tipo_combustivel, des_tipo_combustivel, ind_ativo, dtc_criacao, dtc_exclusao) VALUES (4, 'Outros', true, current_date, null);


INSERT INTO documento_obrigatorio(nom_documento_obrigatorio, ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo, num_tamanho)
  VALUES ('Formulário de Caracterização do Empreendimento - Geração de Energia',  true, true, 1,  true, 0.000);

INSERT INTO documento_ato(ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia, ind_ativo)
VALUES 
(10070,1, 218, true),(10070,1, 219, true),(10070,1, 223, true),(10070,1, 366, true),(10070,1, 367, true),(10070,1, 426, true),
(10070,2, 218, true),(10070,2, 219, true),(10070,2, 223, true),(10070,2, 366, true),(10070,2, 367, true),(10070,2, 426, true),
(10070,3, 218, true),(10070,3, 219, true),(10070,3, 223, true),(10070,3, 366, true),(10070,3, 367, true),(10070,3, 426, true),
(10070,6, 218, true),(10070,6, 219, true),(10070,6, 223, true),(10070,6, 366, true),(10070,6, 367, true),(10070,6, 426, true),
(10070,7, 218, true),(10070,7, 219, true),(10070,7, 223, true),(10070,7, 366, true),(10070,7, 367, true),(10070,7, 426, true),
(10070,8, 218, true),(10070,8, 219, true),(10070,8, 223, true),(10070,8, 366, true),(10070,8, 367, true),(10070,8, 426, true),
(10070,48, 218, true),(10070,48, 219, true),(10070,48, 223, true),(10070,48, 366, true),(10070,48, 367, true),(10070,48, 426, true)

ALTER TABLE fce_energia_solar_usina RENAME ide_fce_energia_solar_usinal  TO ide_fce_energia_solar_usina;





--Excluir FCE - GERAÇÃO DE ENERGIA
CREATE OR REPLACE FUNCTION remover_fce_geracao_energia_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
BEGIN		

DELETE FROM fce_energia_termoeletrica_unidade_gerador where ide_fce_energia_termoeletrica_unidade_gerador in
	(SELECT ide_fce_energia_termoeletrica_unidade_gerador FROM fce_energia_termoeletrica_unidade_gerador WHERE ide_fce_energia_termoeletrica_unidade in 
		(SELECT ide_fce_energia_termoeletrica_unidade FROM fce_energia_termoeletrica_unidade WHERE ide_fce_energia_termoeletrica in 
			(SELECT ide_fce_energia_termoeletrica FROM fce_energia_termoeletrica WHERE ide_fce_energia   in 
				(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
					(select ide_fce from fce where ide_requerimento in 
						(select ide_requerimento from requerimento where num_requerimento = $1) 
					and ide_documento_obrigatorio = 10070)))));
	
DELETE FROM fce_energia_termoeletrica_unidade_combustivel where ide_fce_energia_termoeletrica_unidade_combustivel in
	(SELECT ide_fce_energia_termoeletrica_unidade_combustivel FROM fce_energia_termoeletrica_unidade_combustivel WHERE ide_fce_energia_termoeletrica_unidade in 
		(SELECT ide_fce_energia_termoeletrica_unidade FROM fce_energia_termoeletrica_unidade WHERE ide_fce_energia_termoeletrica in 
			(SELECT ide_fce_energia_termoeletrica FROM fce_energia_termoeletrica WHERE ide_fce_energia   in 
				(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
					(select ide_fce from fce where ide_requerimento in 
						(select ide_requerimento from requerimento where num_requerimento = $1) 
					and ide_documento_obrigatorio = 10070)))));

DELETE FROM fce_energia_termoeletrica_unidade where ide_fce_energia_termoeletrica_unidade in 
	(SELECT ide_fce_energia_termoeletrica_unidade FROM fce_energia_termoeletrica_unidade WHERE ide_fce_energia_termoeletrica in 
		(SELECT ide_fce_energia_termoeletrica FROM fce_energia_termoeletrica WHERE ide_fce_energia   in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070))));

DELETE FROM fce_energia_termoeletrica where ide_fce_energia_termoeletrica in 
	(SELECT ide_fce_energia_termoeletrica FROM fce_energia_termoeletrica WHERE ide_fce_energia  in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070)));

DELETE FROM fce_energia_solar_usina where ide_fce_energia_solar_usina in 
	(SELECT ide_fce_energia_solar_usina FROM fce_energia_solar_usina WHERE ide_fce_energia_solar  in 
		(SELECT ide_fce_energia_solar FROM fce_energia_solar WHERE ide_fce_energia   in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070))));

DELETE FROM fce_energia_solar where ide_fce_energia_solar in 
	(SELECT ide_fce_energia_solar FROM fce_energia_solar WHERE ide_fce_energia  in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070)));

DELETE FROM fce_energia_eolica_licenca_previa where ide_fce_energia_eolica_licenca_previa in 
	(SELECT ide_fce_energia_eolica_licenca_previa FROM fce_energia_eolica_licenca_previa WHERE ide_fce_energia_eolica  in 
		(SELECT ide_fce_energia_eolica FROM fce_energia_eolica WHERE ide_fce_energia   in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070))));

DELETE FROM fce_energia_eolica_parque where ide_fce_energia_eolica_parque in 
	(SELECT ide_fce_energia_eolica_parque FROM fce_energia_eolica_parque WHERE ide_fce_energia_eolica  in 
		(SELECT ide_fce_energia_eolica FROM fce_energia_eolica WHERE ide_fce_energia   in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070))));
					
DELETE FROM fce_energia_eolica where ide_fce_energia_eolica in 
	(SELECT ide_fce_energia_eolica FROM fce_energia_eolica WHERE ide_fce_energia  in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070)));				


DELETE FROM fce_energia where ide_fce_energia in 
		(SELECT ide_fce_energia FROM fce_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070));

DELETE FROM fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10070;
RETURN 'FCE - Geração de Energia';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_geracao_energia_by_requerimento(character varying)
  OWNER TO postgres;


