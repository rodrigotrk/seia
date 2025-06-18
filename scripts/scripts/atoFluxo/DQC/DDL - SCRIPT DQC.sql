--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA ELEMENTO INTERVENÇÃO QUEIMA CONTROLADA ------------------------
CREATE SEQUENCE public.elemento_intervencao_qc_ide_elemento_intervencao_qc_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE elemento_intervencao_queima_controlada
(
	ide_elemento_intervencao_queima_controlada INTEGER NOT NULL DEFAULT nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass),
	dtc_criacao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL,
	dtc_exclusao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NULL,
	ind_excluido BOOLEAN NOT NULL,
	nom_elemento_intervencao VARCHAR(100) NOT NULL,
	
	CONSTRAINT pk_elemento_intervencao_queima_controlada PRIMARY KEY (ide_elemento_intervencao_queima_controlada)
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA OBJETIVO QUEIMA CONTROLADA ------------------------------------
CREATE SEQUENCE public.objetivo_qc_ide_objetivo_queima_controlada_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE objetivo_queima_controlada
(
	ide_objetivo_queima_controlada INTEGER NOT NULL DEFAULT nextval('objetivo_qc_ide_objetivo_queima_controlada_seq'::regclass),
	des_objetivo_queima_controlada VARCHAR(200) NOT NULL,
	dtc_criacao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL,
	dtc_exclusao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NULL,
	ind_excluido BOOLEAN NOT NULL,
	ind_possui_area_prevista BOOLEAN NOT NULL DEFAULT TRUE,
	
	CONSTRAINT pk_objetivo_queima_controlada PRIMARY KEY (ide_objetivo_queima_controlada)
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC TÉCNICA QUEIMA CONTROLADA ---------------------------------
CREATE SEQUENCE public.tecnica_qc_ide_tecnica_queima_controlada_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tecnica_queima_controlada
(
	ide_tecnica_queima_controlada INTEGER NOT NULL DEFAULT nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass),
	nom_tecnica_queima_controlada VARCHAR(100) NOT NULL,
	dtc_criacao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL,
	dtc_exclusao TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NULL,
	ind_excluido BOOLEAN NOT NULL,
	
	CONSTRAINT pk_tecnica_queima_controlada PRIMARY KEY (ide_tecnica_queima_controlada)
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC -----------------------------------------------------------
CREATE SEQUENCE public.dqc_ide_declaracao_queima_controlada_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE declaracao_queima_controlada
(
	ide_declaracao_queima_controlada INTEGER NOT NULL DEFAULT nextval('dqc_ide_declaracao_queima_controlada_seq'::regclass),
	ind_ciente_termo_compromisso BOOLEAN NOT NULL,
	ind_aceite_responsabilidade BOOLEAN NULL,
	ide_ato_declaratorio INTEGER NOT NULL,
	
	CONSTRAINT pk_declaracao_queima_controlada PRIMARY KEY (ide_declaracao_queima_controlada),
	CONSTRAINT fk_declaracao_queima_controlada_ide_ato_declaratorio FOREIGN KEY (ide_ato_declaratorio) REFERENCES ato_declaratorio (ide_ato_declaratorio) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC ELEMENTO INTERVENÇÃO --------------------------------------
CREATE SEQUENCE public.dqc_elemento_intervencao_ide_dqc_elemento_intervencao_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE declaracao_queima_controlada_elemento_intervencao
(
	ide_declaracao_queima_controlada_elemento_intervencao INTEGER NOT NULL DEFAULT nextval('dqc_elemento_intervencao_ide_dqc_elemento_intervencao_seq'::regclass),
	val_distancia NUMERIC(13,4),
	ide_declaracao_queima_controlada INTEGER NOT NULL,
	ide_elemento_intervencao_queima_controlada INTEGER NOT NULL,
	
	CONSTRAINT pk_declaracao_queima_controlada_elemento_intervencao PRIMARY KEY (ide_declaracao_queima_controlada_elemento_intervencao),
	CONSTRAINT fk_declaracao_queima_controlada_elemento_intervencao_ide_declaracao_queima_controlada FOREIGN KEY (ide_declaracao_queima_controlada) REFERENCES declaracao_queima_controlada (ide_declaracao_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_declaracao_queima_controlada_elemento_intervencao_ide_elemento_intervencao_queima_controlada FOREIGN KEY (ide_elemento_intervencao_queima_controlada) REFERENCES elemento_intervencao_queima_controlada (ide_elemento_intervencao_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC IMÓVEL ----------------------------------------------------
CREATE SEQUENCE public.dqc_imovel_ide_declaracao_queima_controlada_imovel_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE declaracao_queima_controlada_imovel
(
	ide_declaracao_queima_controlada_imovel INTEGER NOT NULL DEFAULT nextval('dqc_imovel_ide_declaracao_queima_controlada_imovel_seq'::regclass),
	ind_arrendado BOOLEAN NOT NULL,
	ide_declaracao_queima_controlada INTEGER NOT NULL,
	ide_localizacao_geografica INTEGER NULL,
	ide_imovel INTEGER NOT NULL,
	
	CONSTRAINT pk_declaracao_queima_controlada_imovel PRIMARY KEY (ide_declaracao_queima_controlada_imovel),
	CONSTRAINT fk_declaracao_queima_controlada_imovel_ide_declaracao_queima_controlada FOREIGN KEY (ide_declaracao_queima_controlada) REFERENCES declaracao_queima_controlada (ide_declaracao_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_declaracao_queima_controlada_imovel_ide_localizacao_geografica FOREIGN KEY (ide_localizacao_geografica) REFERENCES localizacao_geografica (ide_localizacao_geografica) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_declaracao_queima_controlada_imovel_ide_imovel FOREIGN KEY (ide_imovel) REFERENCES imovel (ide_imovel) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC RESPONSÁVEL TÉCNICO ---------------------------------------
CREATE SEQUENCE public.dqc_responsavel_tecnico_ide_dqc_responsavel_tecnico_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE declaracao_queima_controlada_responsavel_tecnico
(
	ide_declaracao_queima_controlada_responsavel_tecnico INTEGER NOT NULL DEFAULT nextval('dqc_responsavel_tecnico_ide_dqc_responsavel_tecnico_seq'::regclass),
	ide_declaracao_queima_controlada INTEGER NOT NULL,
	ide_pessoa_fisica INTEGER NOT NULL,
	
	CONSTRAINT pk_declaracao_queima_controlada_responsavel_tecnico PRIMARY KEY (ide_declaracao_queima_controlada_responsavel_tecnico),
	CONSTRAINT fk_declaracao_queima_controlada_responsavel_tecnico_ide_declaracao_queima_controlada FOREIGN KEY (ide_declaracao_queima_controlada) REFERENCES declaracao_queima_controlada (ide_declaracao_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_declaracao_queima_controlada_responsavel_tecnico_ide_pessoa_fisica FOREIGN KEY (ide_pessoa_fisica) REFERENCES pessoa_fisica (ide_pessoa_fisica) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC TÉCNICA UTILIZADA -----------------------------------------
CREATE SEQUENCE public.dqc_tecnica_utilizada_ide_dqc_tecnica_utilizada_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE declaracao_queima_controlada_tecnica_utilizada
(
	ide_declaracao_queima_controlada_tecnica_utilizada INTEGER NOT NULL DEFAULT nextval('dqc_tecnica_utilizada_ide_dqc_tecnica_utilizada_seq'::regclass),
	ide_declaracao_queima_controlada INTEGER NOT NULL,
	ide_tecnica_queima_controlada INTEGER NOT NULL,
	
	CONSTRAINT pk_declaracao_queima_controlada_tecnica_utilizada PRIMARY KEY (ide_declaracao_queima_controlada_tecnica_utilizada),
	CONSTRAINT fk_declaracao_queima_controlada_tecnica_utilizada_ide_declaracao_queima_controlada FOREIGN KEY (ide_declaracao_queima_controlada) REFERENCES declaracao_queima_controlada (ide_declaracao_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_declaracao_queima_controlada_tecnica_utilizada_ide_tecnica_queima_controlada FOREIGN KEY (ide_tecnica_queima_controlada) REFERENCES tecnica_queima_controlada (ide_tecnica_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- TABELA DQC IMÓVEL OBJETIVO QUEIMA ------------------------------------
CREATE SEQUENCE public.dqci_objetivo_qc_ide_dqci_objetivo_qc_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE declaracao_queima_controlada_imovel_objetivo_queima_controlada
(
	ide_dqc_imovel_objetivo_queima_controlada INTEGER NOT NULL DEFAULT nextval('dqci_objetivo_qc_ide_dqci_objetivo_qc_seq'::regclass),
	val_area_prevista_queima NUMERIC(13,4) NULL,
	ide_declaracao_queima_controlada_imovel INTEGER NOT NULL,
	ide_objetivo_queima_controlada INTEGER NOT NULL,
	
	CONSTRAINT pk_dqc_imovel_objetivo_queima_controlada PRIMARY KEY (ide_dqc_imovel_objetivo_queima_controlada),
	CONSTRAINT fk_dqc_imovel_objetivo_queima_controlada_ide_declaracao_queima_controlada_imovel FOREIGN KEY (ide_declaracao_queima_controlada_imovel) REFERENCES declaracao_queima_controlada_imovel (ide_declaracao_queima_controlada_imovel) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_dqc_imovel_objetivo_queima_controlada_ide_objetivo_queima_controlada FOREIGN KEY (ide_objetivo_queima_controlada) REFERENCES objetivo_queima_controlada (ide_objetivo_queima_controlada) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
);

--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- SCRIPT REMOVER DQC ---------------------------------------------------

-- DROP FUNCTION remover_ato_declaratorio_dqc_by_requerimento(n_requerimento character varying);

CREATE OR REPLACE FUNCTION remover_ato_declaratorio_dqc_by_requerimento(n_requerimento character varying) RETURNS character AS $BODY$
	DECLARE
    	n_requerimento ALIAS FOR $1;
    	dqc RECORD;
	
	BEGIN
	
		FOR dqc IN (SELECT * FROM declaracao_queima_controlada  WHERE ide_ato_declaratorio IN 
				(SELECT ide_ato_declaratorio FROM ato_declaratorio  WHERE ide_requerimento IN 
					(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = n_requerimento)))
		LOOP
			DELETE FROM declaracao_queima_controlada_imovel_objetivo_queima_controlada WHERE ide_declaracao_queima_controlada_imovel IN 
				(SELECT ide_declaracao_queima_controlada_imovel FROM declaracao_queima_controlada_imovel WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada);
			
			DELETE FROM declaracao_queima_controlada_tecnica_utilizada WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada;
			DELETE FROM declaracao_queima_controlada_elemento_intervencao WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada;
			DELETE FROM declaracao_queima_controlada_responsavel_tecnico WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada;
			DELETE FROM declaracao_queima_controlada_imovel WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada;
			DELETE FROM declaracao_queima_controlada WHERE ide_declaracao_queima_controlada = dqc.ide_declaracao_queima_controlada;
			DELETE FROM ato_declaratorio WHERE ide_ato_declaratorio = dqc.ide_ato_declaratorio;
			
			RETURN 'DQC - Deletado';
		END LOOP;	
	END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
  
ALTER FUNCTION remover_ato_declaratorio_dqc_by_requerimento(character varying) OWNER TO seia_sema;
