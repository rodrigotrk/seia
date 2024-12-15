
CREATE SEQUENCE ATIVIDADE_INEXIGIVEL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE ATIVIDADE_INEXIGIVEL_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE ATIVIDADE_INEXIGIVEL_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE ATIVIDADE_INEXIGIVEL_SEQ TO consulta;
	
CREATE TABLE atividade_inexigivel
(
	ide_atividade_inexigivel INTEGER NOT NULL DEFAULT nextval(('ATIVIDADE_INEXIGIVEL_SEQ'::text)::regclass),
	nom_atividade_inexigivel varchar(255) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_ativo            BOOLEAN NOT NULL,
	ide_tipo_atividade_inexigivel INTEGER NOT NULL 
);

ALTER TABLE atividade_inexigivel ADD PRIMARY KEY (ide_atividade_inexigivel);

CREATE SEQUENCE DECLARACAO_INEXIGIBILIDADE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_INEXIGIBILIDADE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade
(
	ide_declaracao_inexigibilidade INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_INEXIGIBILIDADE_SEQ'::text)::regclass),
	ide_atividade_inexigivel INTEGER NOT NULL,
	ide_requerimento     INTEGER NULL,
	ind_ciente_recomendacao BOOLEAN NULL
);

ALTER TABLE declaracao_inexigibilidade ADD PRIMARY KEY (ide_declaracao_inexigibilidade);


CREATE SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_ABASTECIMENTO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_INEXIGIBILIDADE_INFO_ABASTECIMENTO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_ABASTECIMENTO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_ABASTECIMENTO_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_abastecimento
(
	ide_declaracao_inexigibilidade_info_abastecimento  INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_INEXIGIBILIDADE_INFO_ABASTECIMENTO_SEQ'::text)::regclass),
	val_vazao            NUMERIC(10,2) NOT NULL,
	nom_sistema          VARCHAR(80) NOT NULL,
	ide_localizacao_geografica INTEGER NULL,
	ide_endereco         INTEGER NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL
);

ALTER TABLE declaracao_inexigibilidade_info_abastecimento
ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_abastecimento,ide_declaracao_inexigibilidade);


CREATE SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_BUEIRO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_INEXIGIBILIDADE_INFO_BUEIRO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_BUEIRO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_BUEIRO_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_bueiro 
(
	ide_declaracao_inexigibilidade_info_bueiro INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_INEXIGIBILIDADE_INFO_BUEIRO_SEQ'::text)::regclass),
	des_trajeto_via      VARCHAR(255) NOT NULL,
	num_bueiro           INTEGER NOT NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL
);

ALTER TABLE declaracao_inexigibilidade_info_bueiro
ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_bueiro,ide_declaracao_inexigibilidade);


CREATE SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_GERAL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_INEXIGIBILIDADE_INFO_GERAL_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_GERAL_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_GERAL_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_geral
(
	ide_declaracao_inexigibilidade_info_geral INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_INEXIGIBILIDADE_INFO_GERAL_SEQ'::text)::regclass),
	ind_luz_para_todos   BOOLEAN NULL,
	ind_asv_atos_autorizativos boolean NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL,
	ide_tipo_local_atividade_inexigivel INTEGER NULL,
	ide_tipo_rio_intervencao INTEGER NULL,
	ind_sistema_simplificado_abastecimento BOOLEAN NULL,
	ide_endereco         INTEGER NULL
);

ALTER TABLE declaracao_inexigibilidade_info_geral ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_geral,ide_declaracao_inexigibilidade);


CREATE SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PONTE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PONTE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PONTE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PONTE_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_ponte
(
	ide_declaracao_inexigibilidade_info_ponte INTEGER NOT NULL DEFAULT nextval(('IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PONTE_SEQ'::text)::regclass),
	nom_ponte            VARCHAR(80) NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL
);

ALTER TABLE declaracao_inexigibilidade_info_ponte ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_ponte,ide_declaracao_inexigibilidade);


CREATE SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PROJETO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PROJETO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PROJETO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PROJETO_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_projeto
(
	ide_declaracao_inexigibilidade_info_projeto INTEGER NOT NULL DEFAULT nextval(('IDE_DECLARACAO_INEXIGIBILIDADE_INFO_PROJETO_SEQ'::text)::regclass),
	nom_projeto          varchar(80) NOT NULL,
	ide_endereco         INTEGER NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL
);

ALTER TABLE declaracao_inexigibilidade_info_projeto ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_projeto,ide_declaracao_inexigibilidade);


CREATE SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_UNIDADE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_INEXIGIBILIDADE_INFO_UNIDADE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_UNIDADE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_INEXIGIBILIDADE_INFO_UNIDADE_SEQ TO consulta;

CREATE TABLE declaracao_inexigibilidade_info_unidade
(
	ide_declaracao_inexigibilidade_info_unidade INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_INEXIGIBILIDADE_INFO_UNIDADE_SEQ'::text)::regclass),
	nom_unidade          VARCHAR(80) NOT NULL,
	val_area             NUMERIC(10,2) NULL,
	val_area_inundada    NUMERIC(14,4) NULL,
	ide_endereco         INTEGER NULL,
	ide_declaracao_inexigibilidade INTEGER NOT NULL
);

ALTER TABLE declaracao_inexigibilidade_info_unidade ADD PRIMARY KEY (ide_declaracao_inexigibilidade_info_unidade,ide_declaracao_inexigibilidade);


CREATE SEQUENCE TIPO_ATIVIDADE_INEXIGIVEL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE TIPO_ATIVIDADE_INEXIGIVEL_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE TIPO_ATIVIDADE_INEXIGIVEL_SEQ TO seia_sema;

GRANT SELECT ON SEQUENCE TIPO_ATIVIDADE_INEXIGIVEL_SEQ TO consulta;
CREATE TABLE tipo_atividade_inexigivel
(
	ide_tipo_atividade_inexigivel INTEGER NOT NULL DEFAULT nextval(('TIPO_ATIVIDADE_INEXIGIVEL_SEQ'::text)::regclass),
	des_tipo_atividade_inexigivel VARCHAR(100) NOT NULL,
	dtc_criacao          timestamp NOT NULL,
	dtc_exclusao         timestamp NULL,
	ind_ativo            BOOLEAN NOT NULL
);

ALTER TABLE tipo_atividade_inexigivel ADD PRIMARY KEY (ide_tipo_atividade_inexigivel);

CREATE SEQUENCE TIPO_LOCAL_ATIVIDADE_INEXIGIVEL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE TIPO_LOCAL_ATIVIDADE_INEXIGIVEL_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE TIPO_LOCAL_ATIVIDADE_INEXIGIVEL_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE TIPO_LOCAL_ATIVIDADE_INEXIGIVEL_SEQ TO consulta;

CREATE TABLE tipo_local_atividade_inexigivel
(
	ide_tipo_local_atividade_inexigivel INTEGER NOT NULL DEFAULT nextval(('TIPO_LOCAL_ATIVIDADE_INEXIGIVEL_SEQ'::text)::regclass),
	des_tipo_local_atividade VARCHAR(100) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_ativo            BOOLEAN NOT NULL
);

ALTER TABLE tipo_local_atividade_inexigivel ADD PRIMARY KEY (ide_tipo_local_atividade_inexigivel);

CREATE SEQUENCE TIPO_RIO_INTERVENCAO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE TIPO_RIO_INTERVENCAO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE TIPO_RIO_INTERVENCAO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE TIPO_RIO_INTERVENCAO_SEQ TO consulta;

CREATE TABLE tipo_rio_intervencao
(
	ide_tipo_rio_intervencao INTEGER NOT NULL DEFAULT nextval(('TIPO_RIO_INTERVENCAO_SEQ'::text)::regclass),
	des_tipo_rio_intervencao VARCHAR(20) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_ativo            BOOLEAN NOT NULL
);

ALTER TABLE tipo_rio_intervencao ADD PRIMARY KEY (ide_tipo_rio_intervencao);

ALTER TABLE atividade_inexigivel ADD CONSTRAINT fk_atividade_inexigivel_tipo_atividade FOREIGN KEY (ide_tipo_atividade_inexigivel) REFERENCES tipo_atividade_inexigivel (ide_tipo_atividade_inexigivel);

ALTER TABLE declaracao_inexigibilidade ADD CONSTRAINT fk_declaracao_inexigibilidade_atividade_inexigivel FOREIGN KEY (ide_atividade_inexigivel) REFERENCES atividade_inexigivel (ide_atividade_inexigivel);

ALTER TABLE declaracao_inexigibilidade ADD CONSTRAINT fk_declaracao_inexigibilidade_requerimento FOREIGN KEY (ide_requerimento) REFERENCES requerimento (ide_requerimento);

ALTER TABLE declaracao_inexigibilidade_info_abastecimento ADD CONSTRAINT fk_declaracao_inexigibilidade_info_abastecimento_loc_geo FOREIGN KEY (ide_localizacao_geografica) REFERENCES localizacao_geografica (ide_localizacao_geografica);

ALTER TABLE declaracao_inexigibilidade_info_abastecimento ADD CONSTRAINT fk_declaracao_inexigibilidade_info_abastecimento_endereco FOREIGN KEY (ide_endereco) REFERENCES endereco (ide_endereco);

ALTER TABLE declaracao_inexigibilidade_info_abastecimento ADD CONSTRAINT fk_declaracao_inexigibilidade_info_abastecimento_decalracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);

ALTER TABLE declaracao_inexigibilidade_info_bueiro ADD CONSTRAINT fk_declaracao_inexigibilidade_info_bueiro_declaracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);

ALTER TABLE declaracao_inexigibilidade_info_geral ADD CONSTRAINT fk_declaracao_inexigibilidade_info_geral_declaracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);

ALTER TABLE declaracao_inexigibilidade_info_geral ADD CONSTRAINT fk_declaracao_inexigibilidade_info_geral_tipo_local FOREIGN KEY (ide_tipo_local_atividade_inexigivel) REFERENCES tipo_local_atividade_inexigivel (ide_tipo_local_atividade_inexigivel);

ALTER TABLE declaracao_inexigibilidade_info_geral ADD CONSTRAINT fk_declaracao_inexigibilidade_info_geral_tipo_rio FOREIGN KEY (ide_tipo_rio_intervencao) REFERENCES tipo_rio_intervencao (ide_tipo_rio_intervencao);

ALTER TABLE declaracao_inexigibilidade_info_geral ADD CONSTRAINT fk_declaracao_inexigibilidade_info_geral_endereco FOREIGN KEY (ide_endereco) REFERENCES endereco (ide_endereco);

ALTER TABLE declaracao_inexigibilidade_info_ponte ADD CONSTRAINT fk_declaracao_inexigibilidade_info_ponte_declaracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);

ALTER TABLE declaracao_inexigibilidade_info_projeto ADD CONSTRAINT fk_declaracao_inexigibilidade_info_projeto_endereco FOREIGN KEY (ide_endereco) REFERENCES endereco (ide_endereco);

ALTER TABLE declaracao_inexigibilidade_info_projeto ADD CONSTRAINT fk_declaracao_inexigibilidade_info_projeto_declaracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);

ALTER TABLE declaracao_inexigibilidade_info_unidade ADD CONSTRAINT fk_declaracao_inexigibilidade_info_unidade_endereco FOREIGN KEY (ide_endereco) REFERENCES endereco (ide_endereco);

ALTER TABLE declaracao_inexigibilidade_info_unidade ADD CONSTRAINT fk_declaracao_inexigibilidade_info_unidade_declaracao FOREIGN KEY (ide_declaracao_inexigibilidade) REFERENCES declaracao_inexigibilidade (ide_declaracao_inexigibilidade);


ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_LOCAL_REALIZACAO BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_ENDERECO BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_PROJETO BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_QTD_BUEIROS BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_UNIDADE BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_ABASTECIMENTO BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_PONTE BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_LUZ_PARA_TODOS BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE atividade_inexigivel ADD COLUMN PERMITE_MUNICIPIO_EMERGENCIAL BOOLEAN NOT NULL DEFAULT(FALSE);


CREATE SEQUENCE RECOMENDACAO_INEXIGIBILIDADE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE RECOMENDACAO_INEXIGIBILIDADE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE RECOMENDACAO_INEXIGIBILIDADE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE RECOMENDACAO_INEXIGIBILIDADE_SEQ TO consulta;
	
CREATE TABLE recomendacao_inexigibilidade
(
  ide_recomendacao_inexigibilidade integer NOT NULL DEFAULT nextval(('RECOMENDACAO_INEXIGIBILIDADE_SEQ'::text)::regclass),
  des_recomendacao_inexigibilidade character varying NOT NULL,
  ind_ativo boolean NOT NULL
);

ALTER TABLE RECOMENDACAO_INEXIGIBILIDADE ADD PRIMARY KEY (IDE_RECOMENDACAO_INEXIGIBILIDADE);

CREATE SEQUENCE RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ TO consulta;

CREATE TABLE RECOMENDACAO_ATIVIDADE_INEXIGIVEL
(
	IDE_RECOMENDACAO_ATIVIDADE_INEXIGIVEL INTEGER NOT NULL DEFAULT nextval(('RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ'::text)::regclass),
	IDE_RECOMENDACAO_INEXIGIBILIDADE INTEGER NOT NULL,
	IDE_ATIVIDADE_INEXIGIVEL INTEGER NOT NULL,
	ind_ativo            BOOLEAN NOT NULL
);

ALTER TABLE RECOMENDACAO_ATIVIDADE_INEXIGIVEL ADD PRIMARY KEY (IDE_RECOMENDACAO_ATIVIDADE_INEXIGIVEL);

CREATE TABLE MODELO_CERTIFICADO_INEXIGIBILIDADE
(
	IDE_MODELO_CERTIFICADO_INEXIGIBILIDADE INTEGER NOT NULL,
	DSC_MODELO_CERTIFICADO_INEXIGIBILIDADE VARCHAR(255) NOT NULL,
	IND_ATIVO            BOOLEAN NOT NULL DEFAULT(TRUE)
);

ALTER TABLE MODELO_CERTIFICADO_INEXIGIBILIDADE ADD PRIMARY KEY (IDE_MODELO_CERTIFICADO_INEXIGIBILIDADE);

CREATE SEQUENCE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ TO consulta;

CREATE TABLE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE
(
	IDE_ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO INTEGER NOT NULL DEFAULT nextval(('ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ'::text)::regclass),
	IDE_MODELO_CERTIFICADO_INEXIGIBILIDADE INTEGER NOT NULL,
	IDE_ATIVIDADE_INEXIGIVEL INTEGER NOT NULL
);

ALTER TABLE ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE ADD PRIMARY KEY (IDE_ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO);
