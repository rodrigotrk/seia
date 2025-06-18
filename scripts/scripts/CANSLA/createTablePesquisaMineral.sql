-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE tipo_atividade_nao_sujeita_licenciamento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE tipo_atividade_nao_sujeita_licenciamento
(
	ide_tipo_atividade_nao_sujeita_licenciamento INTEGER DEFAULT nextval(('tipo_atividade_nao_sujeita_licenciamento_seq'::TEXT)::regclass) NOT NULL,
	nom_atividade        VARCHAR(180) NULL,
	ind_ativo BOOLEAN
);

ALTER TABLE tipo_atividade_nao_sujeita_licenciamento ADD CONSTRAINT pk_tipo_atividade_nao_sujeita_licenciamento
	PRIMARY KEY (ide_tipo_atividade_nao_sujeita_licenciamento);
	
-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE atividade_nao_sujeita_licenciamento_documento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE atividade_nao_sujeita_licenciamento_documento
(
	ide_atividade_nao_sujeita_licenciamento_documento INTEGER DEFAULT nextval(('atividade_nao_sujeita_licenciamento_documento_seq'::TEXT)::regclass) NOT NULL,
	ide_documento_obrigatorio INTEGER NOT NULL,
	ide_tipo_atividade_nao_sujeita_licenciamento INTEGER NOT NULL
);

ALTER TABLE atividade_nao_sujeita_licenciamento_documento ADD CONSTRAINT pk_atividade_nao_sujeita_licenciamento_documento
	PRIMARY KEY (ide_atividade_nao_sujeita_licenciamento_documento);

ALTER TABLE atividade_nao_sujeita_licenciamento_documento
ADD CONSTRAINT fk_atividade_nao_sujeita_licenciamento_documento_documento_obrigatorio FOREIGN KEY (ide_documento_obrigatorio)
REFERENCES documento_obrigatorio(ide_documento_obrigatorio)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE atividade_nao_sujeita_licenciamento_documento
ADD CONSTRAINT fk_atividade_nao_sujeita_licenciamento_documento_tipo_atividade_nao_sujeita_licenciamento FOREIGN KEY (ide_tipo_atividade_nao_sujeita_licenciamento)
REFERENCES tipo_atividade_nao_sujeita_licenciamento(ide_tipo_atividade_nao_sujeita_licenciamento)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_nao_sujeita_lic_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_nao_sujeita_lic
(
	ide_cadastro_atividade_nao_sujeita_lic INTEGER DEFAULT nextval(('cadastro_atividade_nao_sujeita_lic_seq'::TEXT)::regclass) NOT NULL,
	ide_pessoa_requerente INTEGER NOT NULL,
	ide_pessoa_fisica_cadastro INTEGER NOT NULL,
	dtc_cadastro         TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	ind_excluido         BOOLEAN NOT NULL,
	ide_tipo_atividade_nao_sujeita_licenciamento INTEGER NOT NULL,
	num_cadastro VARCHAR(25),
	ide_empreendimento   INTEGER NOT NULL,
	ide_certificado INTEGER
);

ALTER TABLE cadastro_atividade_nao_sujeita_lic ADD CONSTRAINT pk_cadastro_atividade_nao_sujeita_lic
	PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE cadastro_atividade_nao_sujeita_lic
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_pessoa_requerente FOREIGN KEY (ide_pessoa_requerente)
REFERENCES pessoa (ide_pessoa)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_pessoa_fisica_cadastro FOREIGN KEY (ide_pessoa_fisica_cadastro)
REFERENCES pessoa_fisica(ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_tipo_atividade_nao_sujeita_licenciamento FOREIGN KEY (ide_tipo_atividade_nao_sujeita_licenciamento)
REFERENCES tipo_atividade_nao_sujeita_licenciamento(ide_tipo_atividade_nao_sujeita_licenciamento)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_empreendimento FOREIGN KEY (ide_empreendimento)
REFERENCES empreendimento(ide_empreendimento)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_certificado FOREIGN KEY (ide_certificado)
REFERENCES certificado(ide_certificado)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_nao_sujeita_licenciamento_comunicacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_nao_sujeita_lic_comunicacao
(
	ide_cadastro_atividade_nao_sujeita_lic_comunicacao INTEGER DEFAULT nextval(('cadastro_atividade_nao_sujeita_licenciamento_comunicacao_seq'::TEXT)::regclass) NOT NULL,
	dtc_comunicacao      TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	dsc_mensagem         TEXT NOT NULL,
	dsc_assunto          VARCHAR(150) NOT NULL,
	ind_enviado          BOOLEAN NOT NULL,
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL
);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_comunicacao ADD CONSTRAINT pk_cadastro_atividade_nao_sujeita_lic_comunicacao
	PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic_comunicacao);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_comunicacao
ADD CONSTRAINT fk_cadastro_atividade_nao_sujeita_lic_comunicacao_cadastro FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_documento_identificacao_representacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_documento_identificacao_representacao
(
	ide_cadastro_atividade_documento_identificacao_representacao INTEGER DEFAULT nextval(('cadastro_atividade_documento_identificacao_representacao_seq'::TEXT)::regclass) NOT NULL,
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL,
	ind_documento_validado BOOLEAN,
	dtc_documento_validado TIMESTAMP(6) WITHOUT TIME ZONE NULL DEFAULT now(),
	ide_procurador       INTEGER NULL,
	dsc_url_documento    VARCHAR(1000) NULL,
	ide_representante_legal INTEGER NULL,
	ide_pessoa_fisica_validacao INTEGER NULL,
	ide_pessoa_fisica_envio INTEGER NULL,
	dtc_envio_documento  TIMESTAMP(6) WITHOUT TIME ZONE NULL DEFAULT now(),
	ide_procurador_rep_empreendimento INTEGER NULL,
	ide_documento_identificacao INTEGER NULL
);

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD PRIMARY KEY (ide_cadastro_atividade_documento_identificacao_representacao);

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_cadastro FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_pr FOREIGN KEY (ide_procurador)
REFERENCES procurador_representante (ide_procurador_representante)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_representante FOREIGN KEY (ide_representante_legal)
REFERENCES representante_legal (ide_representante_legal)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_pfv FOREIGN KEY (ide_pessoa_fisica_validacao)
REFERENCES pessoa_fisica (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_pfe FOREIGN KEY (ide_pessoa_fisica_envio)
REFERENCES pessoa_fisica (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_pre FOREIGN KEY (ide_procurador_rep_empreendimento)
REFERENCES procurador_rep_empreendimento (ide_procurador_rep_empreendimento)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_documento_identificacao_representacao
ADD CONSTRAINT fk_cadastro_atividade_doc_identificacao_rep_doc FOREIGN KEY (ide_documento_identificacao)
REFERENCES documento_identificacao (ide_documento_identificacao)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_nao_sujeita_lic_doc_apensado_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado
(
	ide_cadastro_atividade_nao_sujeita_lic_doc_apensado INTEGER DEFAULT nextval(('cadastro_atividade_nao_sujeita_lic_doc_apensado_seq'::TEXT)::regclass) NOT NULL,
	dtc_envio_documento  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	dtc_validado_documento TIMESTAMP(6) WITHOUT TIME ZONE NULL DEFAULT now(),
	ide_pessoa_fisica_validacao INTEGER NULL,
	ide_pessoa_fisica_envio INTEGER NOT NULL,
	url_documento        VARCHAR(1000) NOT NULL,
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NULL,
	ide_documento_obrigatorio INTEGER NULL,
	ind_validado boolean 
);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado ADD CONSTRAINT pk_cad_ativ_nao_sujeita_lic_documento_apensado
	PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic_doc_apensado);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_documento_apensado_pessoa_fisica_validacao FOREIGN KEY (ide_pessoa_fisica_validacao)
REFERENCES pessoa_fisica(ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_documento_apensado_pessoa_fisica_envio FOREIGN KEY (ide_pessoa_fisica_envio)
REFERENCES pessoa_fisica(ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_documento_apensado_cad_ativ_nao_sujeita_lic FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic(ide_cadastro_atividade_nao_sujeita_lic)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic_doc_apensado
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_documento_apensado_documento_obrigatorio FOREIGN KEY (ide_documento_obrigatorio)
REFERENCES documento_obrigatorio(ide_documento_obrigatorio)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_nao_sujeita_lic_tipo_status_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_nao_sujeita_lic_tipo_status
(
	ide_cadastro_atividade_nao_sujeita_lic_tipo_status INTEGER DEFAULT nextval(('cadastro_atividade_nao_sujeita_lic_tipo_status_seq'::TEXT)::regclass) NOT NULL,
	nom_tipo_status      VARCHAR(80) NOT NULL
);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_tipo_status ADD CONSTRAINT pk_cadastro_atividade_nao_sujeita_lic_tipo_status
	PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic_tipo_status);

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE cadastro_atividade_nao_sujeita_lic_status_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE cadastro_atividade_nao_sujeita_lic_status
(
	ide_cadastro_atividade_nao_sujeita_lic_status INTEGER DEFAULT nextval(('cadastro_atividade_nao_sujeita_lic_status_seq'::TEXT)::regclass) NOT NULL,
	dtc_status           TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	ide_pessoa_fisica    INTEGER NOT NULL,
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NULL,
	ide_cadastro_atividade_nao_sujeita_lic_tipo_status INTEGER NULL
);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_status ADD CONSTRAINT pk_cadastro_atividade_nao_sujeita_lic_status
	PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic_status);

ALTER TABLE cadastro_atividade_nao_sujeita_lic_status
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_status_pf FOREIGN KEY (ide_pessoa_fisica)
REFERENCES pessoa_fisica(ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic_status
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_status_cad_ativ_nao_sujeita_lic FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic(ide_cadastro_atividade_nao_sujeita_lic)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE cadastro_atividade_nao_sujeita_lic_status
ADD CONSTRAINT fk_cad_ativ_nao_sujeita_lic_status_cad_ativ_nao_sujeita_lic_tipo_status FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic_tipo_status)
REFERENCES cadastro_atividade_nao_sujeita_lic_tipo_status(ide_cadastro_atividade_nao_sujeita_lic_tipo_status)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
	
-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE pesquisa_mineral_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pesquisa_mineral
(
	ide_pesquisa_mineral INTEGER DEFAULT nextval(('pesquisa_mineral_seq'::TEXT)::regclass) NOT NULL,
	ind_utiliza_agua     BOOLEAN,
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL
);

ALTER TABLE pesquisa_mineral ADD CONSTRAINT pk_pesquisa_mineral
	PRIMARY KEY (ide_pesquisa_mineral);

ALTER TABLE pesquisa_mineral
ADD CONSTRAINT fk_pesquisa_mineral_cadastro_atividade_nao_sujeita_lic FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES cadastro_atividade_nao_sujeita_lic(ide_cadastro_atividade_nao_sujeita_lic)
ON DELETE NO ACTION
ON UPDATE NO ACTION;


ALTER TABLE processo_dnpm ADD COLUMN ide_pesquisa_mineral integer;
ALTER TABLE processo_dnpm 
ADD CONSTRAINT fk_pesquisa_mineral_processo_dnpm FOREIGN KEY (ide_pesquisa_mineral) 
REFERENCES pesquisa_mineral (ide_pesquisa_mineral) 
ON UPDATE NO ACTION 
ON DELETE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE formacao_profissional_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE formacao_profissional
(
	ide_formacao_profissional INTEGER DEFAULT nextval(('formacao_profissional_seq'::TEXT)::regclass) NOT NULL,
	nom_formacao_profissional     VARCHAR(40) NOT NULL
);

ALTER TABLE formacao_profissional ADD CONSTRAINT pk_formacao_profissional
	PRIMARY KEY (ide_formacao_profissional);
	
-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE pesquisa_mineral_responsavel_tecnico_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pesquisa_mineral_responsavel_tecnico
(
	ide_pesquisa_mineral_responsavel_tecnico INTEGER DEFAULT nextval(('pesquisa_mineral_responsavel_tecnico_seq'::TEXT)::regclass) NOT NULL,
	ide_pessoa_fisica_responsavel_tecnico INTEGER NULL,
	ide_pesquisa_mineral INTEGER NOT NULL,
	ide_formacao_profissional INTEGER NOT NULL
);

ALTER TABLE pesquisa_mineral_responsavel_tecnico ADD CONSTRAINT pk_pesquisa_mineral_responsavel_tecnico
	PRIMARY KEY (ide_pesquisa_mineral_responsavel_tecnico);

ALTER TABLE pesquisa_mineral_responsavel_tecnico
ADD CONSTRAINT fk_pesquisa_mineral_responsavel_tecnico_pessoa_fisica FOREIGN KEY (ide_pessoa_fisica_responsavel_tecnico)
REFERENCES pessoa_fisica(ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE pesquisa_mineral_responsavel_tecnico
ADD CONSTRAINT fk_pesquisa_mineral_responsavel_tecnico_pesquisa_mineral FOREIGN KEY (ide_pesquisa_mineral)
REFERENCES pesquisa_mineral(ide_pesquisa_mineral)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE pesquisa_mineral_responsavel_tecnico
ADD CONSTRAINT fk_pesquisa_mineral_responsavel_tecnico_formacao_profissional FOREIGN KEY (ide_formacao_profissional)
REFERENCES formacao_profissional(ide_formacao_profissional)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE pesquisa_mineral_substancia_mineral_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pesquisa_mineral_substancia_mineral
(
	ide_pesquisa_mineral_substancia_mineral INTEGER DEFAULT nextval(('pesquisa_mineral_substancia_mineral_seq'::TEXT)::regclass) NOT NULL,
	ide_pesquisa_mineral INTEGER NOT NULL,
	ide_substancia_mineral INTEGER NOT NULL
);

ALTER TABLE pesquisa_mineral_substancia_mineral ADD CONSTRAINT pk_pesquisa_mineral_substancia_mineral
	PRIMARY KEY (ide_pesquisa_mineral_substancia_mineral);

ALTER TABLE pesquisa_mineral_substancia_mineral
ADD CONSTRAINT fk_pesquisa_mineral_substancia_mineral_pesquisa_mineral FOREIGN KEY (ide_pesquisa_mineral)
REFERENCES pesquisa_mineral(ide_pesquisa_mineral)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE pesquisa_mineral_substancia_mineral
ADD CONSTRAINT fk_pesquisa_mineral_substancia_mineral_substancia_mineral FOREIGN KEY (ide_substancia_mineral)
REFERENCES substancia_mineral(ide_substancia_mineral)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE pesquisa_mineral_documento_captacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pesquisa_mineral_documento_captacao
(
	ide_pesquisa_mineral_documento_captacao INTEGER DEFAULT nextval(('pesquisa_mineral_documento_captacao_seq'::TEXT)::regclass) NOT NULL,
	nom_documento      VARCHAR(80) NOT NULL
);

ALTER TABLE pesquisa_mineral_documento_captacao ADD CONSTRAINT pk_pesquisa_mineral_documento_captacao
	PRIMARY KEY (ide_pesquisa_mineral_documento_captacao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE pesquisa_mineral_uso_da_agua_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pesquisa_mineral_uso_da_agua
(
	ide_pesquisa_mineral_uso_da_agua INTEGER DEFAULT nextval(('pesquisa_mineral_uso_da_agua_seq'::TEXT)::regclass) NOT NULL,
	ide_tipo_captacao INTEGER NOT NULL,
	ide_pesquisa_mineral INTEGER NOT NULL,
	ind_fonte_embasa     BOOLEAN NULL,
	ide_pesquisa_mineral_documento_captacao  INTEGER,
	num_documento        VARCHAR(20) NULL,
	ind_fonte_saae       BOOLEAN NULL
);

ALTER TABLE pesquisa_mineral_uso_da_agua ADD CONSTRAINT pk_pesquisa_mineral_uso_da_agua
	PRIMARY KEY (ide_pesquisa_mineral_uso_da_agua);

ALTER TABLE pesquisa_mineral_uso_da_agua
ADD CONSTRAINT fk_pesquisa_mineral_uso_da_agua_tipo_captacao FOREIGN KEY (ide_tipo_captacao)
REFERENCES tipo_captacao(ide_tipo_captacao)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE pesquisa_mineral_uso_da_agua
ADD CONSTRAINT fk_pesquisa_mineral_uso_da_agua_pesquisa_mineral FOREIGN KEY (ide_pesquisa_mineral)
REFERENCES pesquisa_mineral(ide_pesquisa_mineral)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE pesquisa_mineral_uso_da_agua
ADD CONSTRAINT fk_pesquisa_mineral_uso_da_agua_documento_captacao FOREIGN KEY (ide_pesquisa_mineral_documento_captacao)
REFERENCES pesquisa_mineral_documento_captacao(ide_pesquisa_mineral_documento_captacao)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE processo_dnpm_prospecao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE processo_dnpm_prospecao
(
	ide_processo_dnpm_prospecao INTEGER DEFAULT nextval(('processo_dnpm_prospecao_seq'::TEXT)::regclass) NOT NULL,
	ide_processo_dnpm INTEGER NOT NULL,
	ide_metodo_prospeccao INTEGER NOT NULL
);

ALTER TABLE processo_dnpm_prospecao ADD CONSTRAINT pk_processo_dnpm_prospecao
	PRIMARY KEY (ide_processo_dnpm_prospecao);

ALTER TABLE processo_dnpm_prospecao
ADD CONSTRAINT fk_processo_dnpm_prospecao_processo_dnpm FOREIGN KEY (ide_processo_dnpm)
REFERENCES processo_dnpm(ide_processo_dnpm)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE processo_dnpm_prospecao
ADD CONSTRAINT fk_processo_dnpm_prospecao_metodo_prospeccao FOREIGN KEY (ide_metodo_prospeccao)
REFERENCES metodo_prospeccao(ide_metodo_prospeccao)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE prospecao_detalhamento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE prospecao_detalhamento
(
	ide_prospeccao_detalhamento INTEGER DEFAULT nextval(('prospecao_detalhamento_seq'::TEXT)::regclass) NOT NULL,
	nom_identificacao   VARCHAR(20) NOT NULL,
	nom_imovel_rural     VARCHAR(180) NOT NULL,
	nom_identificacao_alvo VARCHAR(20) NOT NULL,
	num_profundidade     NUMERIC(10,2) NOT NULL,
	num_diametro         NUMERIC(10,2) NULL,
	num_area_praca       NUMERIC(10,2) NULL,
	num_largura          NUMERIC(10,2) NULL,
	num_comprimento      NUMERIC(10,2) NULL,
	ide_processo_dnpm_prospecao INTEGER NOT NULL,
	ide_localizacao_geografica INTEGER NOT NULL
);

ALTER TABLE prospecao_detalhamento ADD CONSTRAINT pk_prospecao_detalhamento
	PRIMARY KEY (ide_prospeccao_detalhamento);

ALTER TABLE prospecao_detalhamento
ADD CONSTRAINT fk_prospecao_detalhamento_processo_dnpm_prospecao FOREIGN KEY (ide_processo_dnpm_prospecao)
REFERENCES processo_dnpm_prospecao(ide_processo_dnpm_prospecao)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE prospecao_detalhamento
ADD CONSTRAINT fk_prospecao_detalhamento_localizacao_geografica FOREIGN KEY (ide_localizacao_geografica)
REFERENCES localizacao_geografica(ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
