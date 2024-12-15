/*
DROP TABLE IF EXISTS tipo_revestimento CASCADE;
DROP TABLE IF EXISTS tipo_canal CASCADE;
DROP TABLE IF EXISTS fce_canal_trecho CASCADE;
DROP TABLE IF EXISTS fce_canal_trecho_tipo_revestimento CASCADE;
DROP TABLE IF EXISTS secao_geometrica CASCADE;
DROP TABLE IF EXISTS fce_canal_trecho_secao_geometrica CASCADE;
DROP TABLE IF EXISTS tipo_rio CASCADE;
DROP TABLE IF EXISTS fce_canal CASCADE;
DROP TABLE IF EXISTS fce_canal_municipio CASCADE;
DROP TABLE IF EXISTS caracteristica_canal CASCADE;
DROP TABLE IF EXISTS fce_canal_caracteristica CASCADE;
DROP SEQUENCE public.fce_canais_seq
*/
-- -----------------------------------------------------
-- Table tipo_revestimento
-- -----------------------------------------------------
CREATE SEQUENCE public.tipo_revestimento_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE tipo_revestimento (
  ide_tipo_revestimento INTEGER DEFAULT nextval(('tipo_revestimento_seq'::text)::regclass) NOT NULL,
  dsc_tipo_revestimento VARCHAR(100) NOT NULL,
  PRIMARY KEY (ide_tipo_revestimento));

-- -----------------------------------------------------
-- Table tipo_canal
-- -----------------------------------------------------
CREATE SEQUENCE public.tipo_canal_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  tipo_canal (
  ide_tipo_canal INTEGER DEFAULT nextval(('tipo_canal_seq'::text)::regclass) NOT NULL,
  dsc_tipo_canal VARCHAR(100) NOT NULL,
  PRIMARY KEY (ide_tipo_canal));
-- -----------------------------------------------------
-- Table Trecho_canal
-- -----------------------------------------------------  
CREATE SEQUENCE public.fce_canal_trecho_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
CREATE TABLE  fce_canal_trecho (
  ide_fce_canal_trecho INTEGER DEFAULT nextval(('fce_canal_trecho_seq'::text)::regclass) NOT NULL,
  ide_localizacao_geografica_inicio INTEGER NOT NULL,
  ide_localizacao_geografica_fim INTEGER NOT NULL,
  val_comprimento DECIMAL(10,2) NULL,
  ide_tipo_canal INTEGER NULL,
  ide_fce_canal INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal_trecho),
  CONSTRAINT fk_fce_canal_trecho_tipo_canal
    FOREIGN KEY (ide_tipo_canal)
    REFERENCES tipo_canal (ide_tipo_canal)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table fce_canal_trecho_tipo_revestimento
-- -----------------------------------------------------
CREATE SEQUENCE public.fce_canal_trecho_tipo_revestimento_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  fce_canal_trecho_tipo_revestimento (
  ide_fce_canal_trecho_tipo_revestimento INTEGER DEFAULT nextval(('fce_canal_trecho_tipo_revestimento_seq'::text)::regclass) NOT NULL,
  ide_tipo_revestimento INTEGER NOT NULL,
  ide_fce_canal_trecho INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal_trecho_tipo_revestimento),
  CONSTRAINT fk_tipo_revestimento_has_fce_canal_trecho_tipo_revestimento1
    FOREIGN KEY (ide_tipo_revestimento)
    REFERENCES tipo_revestimento (ide_tipo_revestimento)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_tipo_revestimento_has_fce_canal_trecho_fce_canal_trecho1
    FOREIGN KEY (ide_fce_canal_trecho)
    REFERENCES fce_canal_trecho (ide_fce_canal_trecho)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table secao_geometrica
-- -----------------------------------------------------
CREATE SEQUENCE public.secao_geometrica_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  secao_geometrica (
  ide_secao_geometrica INTEGER DEFAULT nextval(('secao_geometrica_seq'::text)::regclass) NOT NULL,
  dsc_secao_geometrica VARCHAR(100) NOT NULL,
  PRIMARY KEY (ide_secao_geometrica));



-- -----------------------------------------------------
-- Table fce_canal_trecho_secao_geometrica
-- -----------------------------------------------------
CREATE SEQUENCE public.fce_canal_trecho_secao_geometrica_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  fce_canal_trecho_secao_geometrica (
  ide_fce_canal_trecho_secao_geometrica INTEGER DEFAULT nextval(('fce_canal_trecho_secao_geometrica_seq'::text)::regclass) NOT NULL,
  val_base_maior DECIMAL(10,2) NULL,
  val_base_menor DECIMAL(10,2) NULL,
  val_altura DECIMAL(10,2) NULL,
  val_largura DECIMAL(10,2) NULL,
  val_diametro DECIMAL(10,2) NULL,
  ide_fce_canal_trecho INTEGER NOT NULL,
  ide_secao_geometrica INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal_trecho_secao_geometrica),
  CONSTRAINT fk_secao_geometrica_has_fce_canal_trecho_secao_geometrica1
    FOREIGN KEY (ide_secao_geometrica)
    REFERENCES secao_geometrica (ide_secao_geometrica)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_secao_geometrica_has_fce_canal_trecho_fce_canal_trecho1
    FOREIGN KEY (ide_fce_canal_trecho)
    REFERENCES fce_canal_trecho (ide_fce_canal_trecho)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table tipo_rio
-- -----------------------------------------------------
CREATE SEQUENCE public.tipo_rio_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  tipo_rio (
  ide_tipo_rio INTEGER DEFAULT nextval(('tipo_rio_seq'::text)::regclass) NOT NULL,
  dsc_tipo_rio VARCHAR(100) NOT NULL,
  PRIMARY KEY (ide_tipo_rio));



-- -----------------------------------------------------
-- Table fce_canal
-- -----------------------------------------------------
CREATE SEQUENCE public.fce_canais_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  fce_canal (
  ide_fce_canal INTEGER DEFAULT nextval(('fce_canais_seq'::text)::regclass) NOT NULL,
  val_vazao DECIMAL(10,2) NOT NULL,
  val_area_ocupada DECIMAL(10,2) NOT NULL,
  ide_tipo_rio INTEGER NULL,
  nom_rio VARCHAR(100) NULL,
  ide_fce INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal),
  CONSTRAINT fk_fce_canal_tipo_rio1
    FOREIGN KEY (ide_tipo_rio)
    REFERENCES tipo_rio (ide_tipo_rio)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_fce1
    FOREIGN KEY (ide_fce)
    REFERENCES fce (ide_fce)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table fce_canal_municipio
-- -----------------------------------------------------
CREATE SEQUENCE public.fce_canal_municipio_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  fce_canal_municipio (
  ide_canal_municipio INTEGER DEFAULT nextval(('fce_canal_municipio_seq'::text)::regclass) NOT NULL,
  ide_fce_canal INTEGER NOT NULL,
  ide_municipio INTEGER NOT NULL,
  PRIMARY KEY (ide_canal_municipio),
  CONSTRAINT fk_fce_canal_municipio_fce_canal1
    FOREIGN KEY (ide_fce_canal)
    REFERENCES fce_canal (ide_fce_canal)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,  
  CONSTRAINT fk_municipio1
    FOREIGN KEY (ide_municipio)
    REFERENCES municipio (ide_municipio)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table caracteristica_canal
-- -----------------------------------------------------
CREATE SEQUENCE public.caracteristica_canal_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  caracteristica_canal (
  ide_caracteristica_canal INTEGER DEFAULT nextval(('caracteristica_canal_seq'::text)::regclass) NOT NULL,
  dsc_caracteristica_canal VARCHAR(100) NOT NULL,
  PRIMARY KEY (ide_caracteristica_canal));



-- -----------------------------------------------------
-- Table fce_canal_caracteristica
-- -----------------------------------------------------
CREATE SEQUENCE public.fce_canal_caracteristica_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE  fce_canal_caracteristica (
  ide_fce_canal_caracteristica INTEGER DEFAULT nextval(('fce_canal_caracteristica_seq'::text)::regclass) NOT NULL,
  ide_fce_canal INTEGER NOT NULL,
  ide_caracteristica_canal INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal_caracteristica),
  CONSTRAINT fk_fce_canal_caracteristica_fce_canal1
    FOREIGN KEY (ide_fce_canal)
    REFERENCES fce_canal (ide_fce_canal)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_fce_canal_caracteristica_caracteristica_canal1
    FOREIGN KEY (ide_caracteristica_canal)
    REFERENCES caracteristica_canal (ide_caracteristica_canal)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    

CREATE SEQUENCE public.fce_canal_tipo_finalidade_uso_agua_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
  
CREATE TABLE fce_canal_tipo_finalidade_uso_agua (
  ide_fce_canal_tipo_finalidade_uso_agua INTEGER DEFAULT nextval(('fce_canal_tipo_finalidade_uso_agua_seq'::text)::regclass) NOT NULL,
  ide_fce_canal INTEGER NOT NULL,
  ide_tipo_finalidade_uso_agua INTEGER NOT NULL,
  PRIMARY KEY (ide_fce_canal_tipo_finalidade_uso_agua),
	CONSTRAINT fk_fce_canal_tipo_finalidade_uso_agua
    FOREIGN KEY (ide_fce_canal)
    REFERENCES fce_canal (ide_fce_canal)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
	CONSTRAINT fk_ide_tipo_finalidade_uso_agua
    FOREIGN KEY (ide_tipo_finalidade_uso_agua)
    REFERENCES tipo_finalidade_uso_agua (ide_tipo_finalidade_uso_agua)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );