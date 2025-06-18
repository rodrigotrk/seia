CREATE TABLE ato_sinaflor
(
	ide_ato_sinaflor INTEGER NOT NULL,
	nom_ato_sinaflor VARCHAR(200) NOT NULL,
	CONSTRAINT pk_ato_sinaflor PRIMARY KEY (ide_ato_sinaflor)
	
);

ALTER TABLE ato_ambiental ADD COLUMN ide_ato_sinaflor INTEGER;
ALTER TABLE ato_ambiental ADD CONSTRAINT fk_ato_ambiental_ato_sinaflor FOREIGN KEY (ide_ato_sinaflor) REFERENCES ato_sinaflor (ide_ato_sinaflor) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE SEQUENCE processo_sinaflor_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE TABLE processo_sinaflor
(
	ide_processo_sinaflor INTEGER NOT NULL DEFAULT nextval(('processo_sinaflor_seq'::text)::regclass),
	token VARCHAR(200),
	dtc_sincronizacao TIMESTAMP NOT NULL,
	ind_concluido BOOLEAN NOT NULL,
	dsc_log TEXT NOT NULL,
	ide_processo INTEGER NOT NULL,
	CONSTRAINT pk_processo_sinaflor PRIMARY KEY (ide_processo_sinaflor),
	CONSTRAINT fk_processo_sinaflor_processo FOREIGN KEY (ide_processo) REFERENCES processo (ide_processo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
	
);
