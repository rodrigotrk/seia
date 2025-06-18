--Criando a sequence
CREATE SEQUENCE DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ TO consulta;


﻿--Criando Tabela
CREATE TABLE DISPOSICAO_FINAL_RESIDUO
(
	IDE_DISPOSICAO_FINAL_RESIDUO INTEGER NOT NULL DEFAULT nextval(('DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ'::text)::regclass),
	DES_DISPOSICAO_FINAL_RESIDUO VARCHAR(80) NOT NULL,
	DTC_CRIACAO          timestamp NOT NULL,
	DTC_EXCLUSAO         timestamp NULL,
	IND_EXCLUIDO         boolean NOT NULL
);

ALTER TABLE DISPOSICAO_FINAL_RESIDUO
ADD PRIMARY KEY (ide_disposicao_final_residuo);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ TO consulta;


--drop table declaracao_transporte;
CREATE TABLE declaracao_transporte
(
	ide_declaracao_transporte INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ'::text)::regclass),
	ind_aceite_responsabilidade boolean NULL,
	ide_requerimento     INTEGER NOT NULL
);

ALTER TABLE declaracao_transporte
ADD PRIMARY KEY (ide_declaracao_transporte);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_destinatario_residuo
(
	ide_declaracao_transporte_destinatario_residuo INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ'::text)::regclass),
	ide_pessoa_juridica  INTEGER NULL,
	ide_declaracao_transporte INTEGER NULL
);

ALTER TABLE declaracao_transporte_destinatario_residuo
ADD PRIMARY KEY (ide_declaracao_transporte_destinatario_residuo);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_disposicao_final_residuo
(
	ide_declaracao_transporte_disposicao_final_residuo INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_IDE_DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ'::text)::regclass),
	ide_disposicao_final_residuo INTEGER NULL,
	ide_declaracao_transporte INTEGER NULL
);

ALTER TABLE declaracao_transporte_disposicao_final_residuo
ADD PRIMARY KEY (ide_declaracao_transporte_disposicao_final_residuo);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_IDE_DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_IDE_DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_IDE_DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_IDE_DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ TO consulta;

CREATE TABLE declaracao_transporte_entidade_transportadora
(
	ide_declaracao_transporte_entidade_transportadora INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_IDE_DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ'::text)::regclass),
	num_processo_licenciamento VARCHAR(20) NULL,
	ide_pessoa           INTEGER  NOT NULL,
	ide_declaracao_transporte INTEGER NOT NULL
);

ALTER TABLE declaracao_transporte_entidade_transportadora
ADD PRIMARY KEY (ide_declaracao_transporte_entidade_transportadora);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_gerador_residuo
(
	ide_declaracao_transporte_gerador_residuo INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ'::text)::regclass),
	ide_declaracao_transporte INTEGER NOT NULL,
	ide_pessoa           INTEGER NOT NULL
);

ALTER TABLE declaracao_transporte_gerador_residuo
ADD PRIMARY KEY (ide_declaracao_transporte_gerador_residuo);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_RESIDUO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_residuo
(
	ide_declaracao_transporte INTEGER NULL,
	ide_declaracao_transporte_residuo INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_RESIDUO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_SEQ'::text)::regclass),
	ide_residuo          INTEGER NULL,
	qtd_transportada     NUMERIC(10,2) NOT NULL,
	ide_estado_fisico    INTEGER NULL,
	ide_tipo_veiculo     INTEGER NULL,
	ide_acondicionamento INTEGER NULL
);

ALTER TABLE declaracao_transporte_residuo
ADD PRIMARY KEY (ide_declaracao_transporte_residuo);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_ROTEIRO_IDE_DECLARACAO_TRANSPORTE_ROTEIRO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_ROTEIRO_IDE_DECLARACAO_TRANSPORTE_ROTEIRO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_ROTEIRO_IDE_DECLARACAO_TRANSPORTE_ROTEIRO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_ROTEIRO_IDE_DECLARACAO_TRANSPORTE_ROTEIRO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_roteiro
(
	ide_declaracao_transporte_roteiro INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_ROTEIRO_IDE_DECLARACAO_TRANSPORTE_ROTEIRO_SEQ'::text)::regclass),
	ide_declaracao_transporte INTEGER NULL,
	ide_localizacao_geografica INTEGER NULL
);

ALTER TABLE declaracao_transporte_roteiro
ADD PRIMARY KEY (ide_declaracao_transporte_roteiro);

CREATE SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_IDE_DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ TO consulta;

CREATE TABLE declaracao_transporte_residuo_endereco
(
	ide_declaracao_transporte_residuo_endereco INTEGER NOT NULL DEFAULT nextval(('DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_IDE_DECLARCAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ'::text)::regclass),
	ide_declaracao_transporte INTEGER NULL,
	ide_endereco         INTEGER NULL,
	ide_tipo_endereco    INTEGER NULL,
	ind_possui_licenca_autorizacao BOOLEAN NULL,
	num_processo_licenca_autorizacao VARCHAR(20) NULL
);

ALTER TABLE declarAcao_transporte_residuo_endereco
ADD PRIMARY KEY (ide_declaracao_transporte_residuo_endereco);

CREATE SEQUENCE ACONDICIONAMENTO_IDE_ACONDICIONAMENTO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE ACONDICIONAMENTO_IDE_ACONDICIONAMENTO_SEQ
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE ACONDICIONAMENTO_IDE_ACONDICIONAMENTO_SEQ TO seia_sema;
GRANT SELECT ON SEQUENCE ACONDICIONAMENTO_IDE_ACONDICIONAMENTO_SEQ TO consulta;

CREATE TABLE acondicionamento
(
	ide_acondicionamento INTEGER NOT NULL DEFAULT nextval(('ACONDICIONAMENTO_IDE_ACONDICIONAMENTO_SEQ'::text)::regclass),
	cod_acondicionamento VARCHAR(20) NOT NULL,
	des_acondicionamento VARCHAR(40) NOT NULL,
	dtc_criacao          timestamp NOT NULL,
	dtc_exclusao         timestamp NULL,
	ind_excluido         boolean NOT NULL
);

ALTER TABLE acondicionamento
ADD PRIMARY KEY (ide_acondicionamento);

ALTER TABLE declaracao_transporte
ADD CONSTRAINT fk_declaracao_transporte_ide_ato_declaratorio FOREIGN KEY (ide_ato_declaratorio) REFERENCES ato_declaratorio (ide_ato_declaratorio);



ALTER TABLE declaracao_transporte_destinatario_residuo
ADD CONSTRAINT fk_declaracao_transporte_destinatario_residuo_ide_pessoa_juridic FOREIGN KEY (ide_pessoa_juridica) REFERENCES pessoa_juridica (ide_pessoa_juridica);



ALTER TABLE declaracao_transporte_destinatario_residuo
ADD CONSTRAINT fk_declaracao_transporte_destinatario_residuo_ide_declaracao_tra FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_disposicao_final_residuo
ADD CONSTRAINT fk_declaracao_transporte_disposicao_final_residuo_ide_disposicao FOREIGN KEY (ide_disposicao_final_residuo) REFERENCES disposicao_final_residuo (ide_disposicao_final_residuo);



ALTER TABLE declaracao_transporte_disposicao_final_residuo
ADD CONSTRAINT fk_declaracao_transporte_disposicao_final_residuo_ide_declaracao FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_entidade_transportadora
ADD CONSTRAINT fk_declaracao_transporte_gerador_residuo_ide_pessoa FOREIGN KEY (ide_pessoa) REFERENCES pessoa (ide_pessoa);



ALTER TABLE declaracao_transporte_entidade_transportadora
ADD CONSTRAINT fk_declaracao_transporte_entidade_transportadora_ide_declaracao_ FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_gerador_residuo
ADD CONSTRAINT fk_declaracao_transporte_gerador_residuo_ide_declaracao_transpor FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_gerador_residuo
ADD CONSTRAINT fk_declaracao_transporte_gerador_residuo_ide_pessoa FOREIGN KEY (ide_pessoa) REFERENCES pessoa (ide_pessoa);



ALTER TABLE declaracao_transporte_residuo
ADD CONSTRAINT fk_declaracao_transporte_residuo_ide_declaracao_transporte FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_residuo
ADD CONSTRAINT fk_declaracao_transporte_residuo_ide_residuo FOREIGN KEY (ide_residuo) REFERENCES residuo (ide_residuo);



ALTER TABLE declaracao_transporte_residuo
ADD CONSTRAINT fk_declaracao_transporte_residuo_ide_estado_fisico FOREIGN KEY (ide_estado_fisico) REFERENCES estado_fisico (ide_estado_fisico);



ALTER TABLE declaracao_transporte_residuo
ADD CONSTRAINT fk_declaracao_transporte_residuo_ide_tipo_veiculo FOREIGN KEY (ide_tipo_veiculo) REFERENCES tipo_veiculo (ide_tipo_veiculo);



ALTER TABLE declaracao_transporte_residuo
ADD CONSTRAINT fk_declaracao_transporte_residuo_ide_acondicionamento FOREIGN KEY (ide_acondicionamento) REFERENCES acondicionamento (ide_acondicionamento);



ALTER TABLE declaracao_transporte_roteiro
ADD CONSTRAINT fk_declaracao_transporte_roteiro_ide_declaracao_transporte FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_roteiro
ADD CONSTRAINT fk_declaracao_transporte_roteiro_ide_localizacao_geografica FOREIGN KEY (ide_localizacao_geografica) REFERENCES localizacao_geografica (ide_localizacao_geografica);



ALTER TABLE declaracao_transporte_residuo_endereco
ADD CONSTRAINT fk_declaracao_transporte_residuo_endereco_ide_declaracao_transpo FOREIGN KEY (ide_declaracao_transporte) REFERENCES declaracao_transporte (ide_declaracao_transporte);



ALTER TABLE declaracao_transporte_residuo_endereco
ADD CONSTRAINT fk_declaracao_transporte_residuo_endereco_ide_endereco FOREIGN KEY (ide_endereco) REFERENCES endereco (ide_endereco);



ALTER TABLE declaracao_transporte_residuo_endereco
ADD CONSTRAINT fk_declaracao_transporte_residuo_endereco_ide_tipo_endereco FOREIGN KEY (ide_tipo_endereco) REFERENCES tipo_endereco (ide_tipo_endereco);

ALTER TABLE declaracao_transporte
ADD CONSTRAINT fk_declaracao_transporte_ide_ato_declaratorio FOREIGN KEY (ide_ato_declaratorio) REFERENCES ato_declaratorio (ide_ato_declaratorio);

--caso ja exista a tabela declaracao transporte, executar esse trecho para trocar o nome da coluna.
--alter table declaracao_transporte rename column ide_requerimento to ide_ato_declaratorio;


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

--removendo obrigatoriedade do documento obrigatorio no ato_declaratorio
alter table ato_declaratorio alter column ide_documento_obrigatorio drop not null;

