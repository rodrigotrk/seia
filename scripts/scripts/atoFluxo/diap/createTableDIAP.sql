CREATE SEQUENCE ato_declaratorio_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE ato_declaratorio
(
	ide_ato_declaratorio INTEGER DEFAULT nextval(('ato_declaratorio_seq'::text)::regclass) NOT NULL,
	ide_requerimento INTEGER NOT NULL,
	ide_documento_obrigatorio INTEGER NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	ind_concluido         boolean NOT NULL
);

ALTER TABLE ato_declaratorio
ADD PRIMARY KEY (ide_ato_declaratorio);

ALTER TABLE ato_declaratorio
ADD CONSTRAINT fk_ato_declaratorio_ide_requerimento 
FOREIGN KEY (ide_requerimento) 
REFERENCES requerimento (ide_requerimento);

ALTER TABLE ato_declaratorio
ADD CONSTRAINT fk_ato_declaratorio_ide_documento_obrigatorio 
FOREIGN KEY (ide_documento_obrigatorio) 
REFERENCES documento_obrigatorio (ide_documento_obrigatorio);

CREATE SEQUENCE atividade_intervencao_app_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE atividade_intervencao_app
(
	ide_atividade_intervencao_app INTEGER DEFAULT nextval(('atividade_intervencao_app_seq'::text)::regclass) NOT NULL,
	des_atividade_intervencao_app VARCHAR(500) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_excluido         boolean NOT NULL
);

ALTER TABLE atividade_intervencao_app
ADD PRIMARY KEY (ide_atividade_intervencao_app);

---------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE caracteristica_intervencao_app_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE caracteristica_intervencao_app
(
	ide_caracteristica_intervencao_app INTEGER DEFAULT nextval(('caracteristica_intervencao_app_seq'::text)::regclass) NOT NULL,
	nom_caracteristica_intervencao VARCHAR(50) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_excluido         boolean NOT NULL
);

ALTER TABLE caracteristica_intervencao_app
ADD PRIMARY KEY (ide_caracteristica_intervencao_app);

---------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE declaracao_intervencao_app_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE declaracao_intervencao_app
(
	ide_declaracao_intervencao_app integer DEFAULT nextval(('declaracao_intervencao_app_seq'::text)::regclass) NOT NULL,
	ind_ciente_portaria  BOOLEAN NOT NULL,
	ind_aceite_responsabilidade BOOLEAN NULL,
	des_objetivo_intervencao_app VARCHAR(250) NULL,
	ide_ato_declaratorio     INTEGER NOT NULL,
	ide_localizacao_geografica INTEGER NULL
);

ALTER TABLE declaracao_intervencao_app
ADD PRIMARY KEY (ide_declaracao_intervencao_app);

ALTER TABLE declaracao_intervencao_app
ADD CONSTRAINT fk_declaracao_intervencao_app_ide_ato_declaratorio 
FOREIGN KEY (ide_ato_declaratorio) 
REFERENCES ato_declaratorio (ide_ato_declaratorio);

ALTER TABLE declaracao_intervencao_app
ADD CONSTRAINT fk_declaracao_intervencao_app_ide_localizacao_geografica 
FOREIGN KEY (ide_localizacao_geografica) 
REFERENCES localizacao_geografica (ide_localizacao_geografica);

---------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE caracteristica_atividade_intervencao_app_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE caracteristica_atividade_intervencao_app
(
	ide_caracteristica_atividade_intervencao_app INTEGER DEFAULT nextval(('caracteristica_atividade_intervencao_app_seq'::text)::regclass) NOT NULL,
	ide_caracteristica_intervencao_app INTEGER NOT NULL,
	ide_atividade_intervencao_app INTEGER NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ind_excluido         boolean NOT NULL
);

ALTER TABLE caracteristica_atividade_intervencao_app
ADD PRIMARY KEY (ide_caracteristica_atividade_intervencao_app);


ALTER TABLE caracteristica_atividade_intervencao_app
ADD CONSTRAINT fk_carcteristica_atividade_intervencao_app_ide_caracteristica_in 
FOREIGN KEY (ide_caracteristica_intervencao_app) 
REFERENCES caracteristica_intervencao_app (ide_caracteristica_intervencao_app);

ALTER TABLE caracteristica_atividade_intervencao_app
ADD CONSTRAINT fk_caracteristica_atividade_intervencao_app_ide_atividade_interv 
FOREIGN KEY (ide_atividade_intervencao_app) 
REFERENCES atividade_intervencao_app (ide_atividade_intervencao_app);

---------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE declaracao_intervencao_app_caracteristca_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE declaracao_intervencao_app_caracteristca
(
	ide_declaracao_intervencao_app_caracteristica INTEGER DEFAULT nextval(('declaracao_intervencao_app_caracteristca_seq'::text)::regclass) NOT NULL,
	ide_declaracao_intervencao_app integer NOT NULL,
	ide_caracteristica_atividade_intervencao_app INTEGER NULL,
	des_caminho_arquivo_decreto VARCHAR(1000) NULL
);

ALTER TABLE declaracao_intervencao_app_caracteristca
ADD PRIMARY KEY (ide_declaracao_intervencao_app_caracteristica);


ALTER TABLE declaracao_intervencao_app_caracteristca
ADD CONSTRAINT fk_declaracao_intervencao_app_ide_declaracao_intervencao_app_car 
FOREIGN KEY (ide_declaracao_intervencao_app) 
REFERENCES declaracao_intervencao_app (ide_declaracao_intervencao_app);

ALTER TABLE declaracao_intervencao_app_caracteristca
ADD CONSTRAINT fk_declaracao_intervencao_app_caracteristica_ide_caracteristica_ 
FOREIGN KEY (ide_caracteristica_atividade_intervencao_app) 
REFERENCES caracteristica_atividade_intervencao_app (ide_caracteristica_atividade_intervencao_app);



CREATE OR REPLACE FUNCTION remover_ato_declaratorio_diap_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
BEGIN

DELETE FROM declaracao_intervencao_app_caracteristca WHERE ide_declaracao_intervencao_app IN
	(SELECT ide_declaracao_intervencao_app FROM declaracao_intervencao_app WHERE ide_ato_declaratorio IN
		(SELECT ide_ato_declaratorio FROM ato_declaratorio WHERE ide_requerimento IN
			(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1)	AND (ide_documento_obrigatorio = 10048)
		)
	);

DELETE FROM declaracao_intervencao_app  WHERE ide_declaracao_intervencao_app IN
	(SELECT ide_declaracao_intervencao_app FROM declaracao_intervencao_app WHERE ide_ato_declaratorio IN
		(SELECT ide_ato_declaratorio FROM ato_declaratorio WHERE ide_requerimento IN
			(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1)	AND (ide_documento_obrigatorio = 10048)
		)
	);

DELETE FROM ato_declaratorio WHERE ide_requerimento IN
	(SELECT ide_requerimento FROM requerimento WHERE num_requerimento = $1)	AND (ide_documento_obrigatorio = 10048);

RETURN 'OK!';    
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_ato_declaratorio_diap_by_requerimento(character varying)
  OWNER TO seia_sema;