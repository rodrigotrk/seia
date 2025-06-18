ALTER TABLE processo_ato ADD COLUMN ide_processo_reenquadramento INTEGER NULL;

CREATE SEQUENCE processo_reenquadramento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE processo_reenquadramento
(
	ide_processo_reenquadramento INTEGER NOT NULL DEFAULT nextval(('processo_reenquadramento_seq'::text)::regclass),
	num_processo VARCHAR(50) NOT NULL,
	ind_excluido BOOLEAN NOT NULL,
	dtc_exclusao TIMESTAMP NULL,
	dtc_formacao TIMESTAMP NOT NULL,
	ide_processo INTEGER NULL
);
ALTER TABLE processo_reenquadramento
ADD PRIMARY KEY (ide_processo_reenquadramento);

CREATE SEQUENCE finalidade_reenquadramento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE finalidade_reenquadramento
(
	ide_finalidade_reenquadramento INTEGER NOT NULL DEFAULT nextval(('finalidade_reenquadramento_seq'::text)::regclass),
	nom_motivo_reenquadramento VARCHAR(50) NOT NULL,
	ind_ativo BOOLEAN NOT NULL,
	dtc_criacao TIMESTAMP NOT NULL,
	dtc_exclusao TIMESTAMP NULL
);
ALTER TABLE finalidade_reenquadramento
ADD PRIMARY KEY (ide_finalidade_reenquadramento);

CREATE SEQUENCE reenquadramento_processo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE reenquadramento_processo
(
	ide_reenquadramento_processo INTEGER NOT NULL DEFAULT nextval(('reenquadramento_processo_seq'::text)::regclass),
	ide_notificacao INTEGER NOT NULL,
	ide_classe_empreendimento_inicial INTEGER NULL,
	ide_nova_classe_empreendimento INTEGER NULL,
	ide_porte_empreendimento_inicial INTEGER NULL,
	ide_novo_porte_empreendimento INTEGER NULL,
	ide_finalidade_reenquadramento INTEGER
);
ALTER TABLE reenquadramento_processo
ADD PRIMARY KEY (ide_reenquadramento_processo);

CREATE SEQUENCE reenquadramento_processo_ato_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE reenquadramento_processo_ato
(
	ide_reenquadramento_processo_ato INTEGER NOT NULL DEFAULT nextval(('reenquadramento_processo_ato_seq'::text)::regclass),
	ide_processo_ato INTEGER NULL,
	ide_novo_ato_ambiental INTEGER NOT NULL,
	ide_nova_tipologia INTEGER,
	ide_reenquadramento_processo INTEGER
);
ALTER TABLE reenquadramento_processo_ato
ADD PRIMARY KEY (ide_reenquadramento_processo_ato);

CREATE SEQUENCE reenquadramento_potencial_poluicao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE reenquadramento_potencial_poluicao
(
	ide_reenquadramento_potencial_poluicao INTEGER NOT NULL DEFAULT nextval(('reenquadramento_potencial_poluicao_seq'::text)::regclass),
	ide_reenquadramento_processo INTEGER NOT NULL,
	ide_requerimento_tipologia INTEGER NOT NULL,
	ide_potencial_poluicao_novo INTEGER NULL,
	ide_potencial_poluicao_inicial INTEGER NULL
);
ALTER TABLE reenquadramento_potencial_poluicao
ADD PRIMARY KEY (ide_reenquadramento_potencial_poluicao);

CREATE SEQUENCE reenquadramento_tipologia_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE reenquadramento_tipologia
(
	ide_reenquadramento_tipologia INTEGER NOT NULL DEFAULT nextval(('reenquadramento_tipologia_seq'::text)::regclass),
	ide_reenquadramento_processo INTEGER NOT NULL,
	ide_tipologia INTEGER NOT NULL
);
ALTER TABLE reenquadramento_tipologia
ADD PRIMARY KEY (ide_reenquadramento_tipologia);

CREATE SEQUENCE status_reenquadramento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE status_reenquadramento
(
	ide_status_reenquadramento INTEGER NOT NULL DEFAULT nextval(('status_reenquadramento_seq'::text)::regclass),
	nom_status_reenquadramento VARCHAR(50) NOT NULL,
	dtc_criacao TIMESTAMP NOT NULL,
	ind_ativo BOOLEAN NOT NULL,
	dtc_exclusao TIMESTAMP NULL
);
ALTER TABLE status_reenquadramento
ADD PRIMARY KEY (ide_status_reenquadramento);

CREATE SEQUENCE tramitacao_reenquadramento_processo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE tramitacao_reenquadramento_processo
(
	ide_tramitacao_reenquadramento_processo INTEGER NOT NULL DEFAULT nextval(('tramitacao_reenquadramento_processo_seq'::text)::regclass),
	dtc_movimentacao TIMESTAMP NOT NULL,
	ide_pessoa INTEGER NOT NULL,
	ide_status_reenquadramento INTEGER NOT NULL,
	ide_processo_reenquadramento INTEGER NOT NULL
);
ALTER TABLE tramitacao_reenquadramento_processo
ADD PRIMARY KEY (ide_tramitacao_reenquadramento_processo);

CREATE SEQUENCE comunicacao_reenquadramento_processo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE comunicacao_reenquadramento_processo
(
	ide_comunicacao_reenquadramento_processo INTEGER NOT NULL DEFAULT nextval(('comunicacao_reenquadramento_processo_seq'::text)::regclass),
	dtc_comunicacao TIMESTAMP NOT NULL,
	des_mensagem VARCHAR(500) NOT NULL,
	ide_processo_reenquadramento INTEGER NOT NULL
);
ALTER TABLE comunicacao_reenquadramento_processo
ADD PRIMARY KEY (ide_comunicacao_reenquadramento_processo);

ALTER TABLE comunicacao_reenquadramento_processo
ADD CONSTRAINT comunicacao_reenquadramento_processo_processo_reenquadramento_fk FOREIGN KEY (ide_processo_reenquadramento) REFERENCES processo_reenquadramento (ide_processo_reenquadramento);

CREATE TABLE finalidade_reenquadramento_processo
(
	ide_finalidade_reenquadramento INTEGER,
	ide_reenquadramento_processo INTEGER
);
ALTER TABLE finalidade_reenquadramento_processo
ADD PRIMARY KEY (ide_finalidade_reenquadramento, ide_reenquadramento_processo);

ALTER TABLE finalidade_reenquadramento_processo
ADD CONSTRAINT finalidade_reenquadramento_processo_finalidade_reenquadramento_fk FOREIGN KEY (ide_finalidade_reenquadramento) REFERENCES finalidade_reenquadramento (ide_finalidade_reenquadramento);

ALTER TABLE finalidade_reenquadramento_processo
ADD CONSTRAINT finalidade_reenquadramento_processo_reenquadramento_processo_fk FOREIGN KEY (ide_reenquadramento_processo) REFERENCES reenquadramento_processo (ide_reenquadramento_processo);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_notificacao_fk FOREIGN KEY (ide_notificacao) REFERENCES notificacao (ide_notificacao);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_porte1_fk FOREIGN KEY (ide_porte_empreendimento_inicial) REFERENCES porte (ide_porte);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_porte2_fk FOREIGN KEY (ide_novo_porte_empreendimento) REFERENCES porte (ide_porte);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_classe1_fk FOREIGN KEY (ide_classe_empreendimento_inicial) REFERENCES classe (ide_classe);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_classe2_fk FOREIGN KEY (ide_nova_classe_empreendimento) REFERENCES classe (ide_classe);

ALTER TABLE reenquadramento_processo
ADD CONSTRAINT reenquadramento_processo_finalidade_reenquadramento_fk FOREIGN KEY (ide_finalidade_reenquadramento) REFERENCES finalidade_reenquadramento (ide_finalidade_reenquadramento);


ALTER TABLE reenquadramento_processo_ato
ADD CONSTRAINT reenquadramento_processo_ato_reenquadramento_processo_fk FOREIGN KEY (ide_reenquadramento_processo) REFERENCES reenquadramento_processo (ide_reenquadramento_processo);

ALTER TABLE reenquadramento_processo_ato
ADD CONSTRAINT reenquadramento_processo_ato_processo_ato_fk FOREIGN KEY (ide_processo_ato) REFERENCES processo_ato (ide_processo_ato);

ALTER TABLE reenquadramento_processo_ato
ADD CONSTRAINT reenquadramento_processo_ato_ato_ambiental_fk FOREIGN KEY (ide_novo_ato_ambiental) REFERENCES ato_ambiental (ide_ato_ambiental);

ALTER TABLE reenquadramento_processo_ato
ADD CONSTRAINT reenquadramento_processo_ato_tipologia_fk FOREIGN KEY (ide_nova_tipologia) REFERENCES tipologia (ide_tipologia);


ALTER TABLE reenquadramento_potencial_poluicao
ADD CONSTRAINT reenquadramento_potencial_poluicao_reenquadramento_processo_fk FOREIGN KEY (ide_reenquadramento_processo) REFERENCES reenquadramento_processo (ide_reenquadramento_processo);

ALTER TABLE reenquadramento_potencial_poluicao
ADD CONSTRAINT reenquadramento_potencial_poluicao_requerimento_tipologia_fk FOREIGN KEY (ide_requerimento_tipologia) REFERENCES requerimento_tipologia (ide_requerimento_tipologia);

ALTER TABLE reenquadramento_potencial_poluicao
ADD CONSTRAINT reenquadramento_potencial_poluicao_potencial_poluicao1_fk FOREIGN KEY (ide_potencial_poluicao_inicial) REFERENCES potencial_poluicao (ide_potencial_poluicao);

ALTER TABLE reenquadramento_potencial_poluicao
ADD CONSTRAINT reenquadramento_potencial_poluicao_potencial_poluicao2_fk FOREIGN KEY (ide_potencial_poluicao_novo) REFERENCES potencial_poluicao (ide_potencial_poluicao);


ALTER TABLE reenquadramento_tipologia
ADD CONSTRAINT reenquadramento_tipologia_reenquadramento_processo_fk FOREIGN KEY (ide_reenquadramento_processo) REFERENCES reenquadramento_processo (ide_reenquadramento_processo);

ALTER TABLE reenquadramento_tipologia
ADD CONSTRAINT reenquadramento_tipologia_tipologia_fk FOREIGN KEY (ide_tipologia) REFERENCES tipologia (ide_tipologia);


ALTER TABLE tramitacao_reenquadramento_processo
ADD CONSTRAINT tramitacao_reenquadramento_processo_pessoa_fk FOREIGN KEY (ide_pessoa) REFERENCES pessoa (ide_pessoa);

ALTER TABLE tramitacao_reenquadramento_processo
ADD CONSTRAINT tramitacao_reenquadramento_processo_status_reenquadramento_fk FOREIGN KEY (ide_status_reenquadramento) REFERENCES status_reenquadramento (ide_status_reenquadramento);

ALTER TABLE tramitacao_reenquadramento_processo
ADD CONSTRAINT tramitacao_reenquadramento_processo_processo_reenquadramento_fk FOREIGN KEY (ide_processo_reenquadramento) REFERENCES processo_reenquadramento (ide_processo_reenquadramento);


ALTER TABLE processo_ato
ADD CONSTRAINT processo_ato_processo_reenquadramento_fk FOREIGN KEY (ide_processo_reenquadramento) REFERENCES processo_reenquadramento (ide_processo_reenquadramento);


ALTER TABLE processo_reenquadramento
ADD CONSTRAINT processo_reenquadramento_processo_fk FOREIGN KEY (ide_processo) REFERENCES processo (ide_processo);

ALTER TABLE enquadramento ADD COLUMN ide_processo_reenquadramento integer;
ALTER TABLE enquadramento 
ADD CONSTRAINT fK_ide_processo_reenquadramento_processo_reenquadramento FOREIGN KEY (ide_processo_reenquadramento) REFERENCES processo_reenquadramento (ide_processo_reenquadramento) 
ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE SEQUENCE documento_obrigatorio_reenquadramento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE documento_obrigatorio_reenquadramento
(
  ide_documento_obrigatorio_reenquadramento integer NOT NULL DEFAULT nextval(('documento_obrigatorio_reenquadramento_seq'::text)::regclass),
  ide_processo_reenquadramento integer NOT NULL,
  ide_enquadramento_ato_ambiental integer NOT NULL,
  ide_documento_obrigatorio integer NOT NULL,
  ide_documento_ato integer NOT NULL, 	
  dsc_caminho_arquivo character varying(1000),
  ind_documento_validado boolean NOT NULL DEFAULT false,
  dtc_validacao timestamp(6) without time zone,
  ide_pessoa_upload integer,
  ide_pessoa_validacao integer,
  ind_sigiloso boolean NOT NULL DEFAULT false
);

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT pk_documento_obrigatorio_reenquadramento 
	PRIMARY KEY (ide_documento_obrigatorio_reenquadramento);

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_pro_reenq_fkey 
	FOREIGN KEY (ide_processo_reenquadramento) 
	REFERENCES processo_reenquadramento (ide_processo_reenquadramento) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_enq_ato_amb_fkey 
	FOREIGN KEY (ide_enquadramento_ato_ambiental) 
	REFERENCES enquadramento_ato_ambiental (ide_enquadramento_ato_ambiental) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_doc_obr_fkey 
	FOREIGN KEY (ide_documento_obrigatorio) 
	REFERENCES documento_obrigatorio (ide_documento_obrigatorio) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_doc_ato_fkey 
	FOREIGN KEY (ide_documento_ato) 
	REFERENCES documento_ato (ide_documento_ato) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_pessoa_upload_fkey 
	FOREIGN KEY (ide_pessoa_upload) 
	REFERENCES pessoa (ide_pessoa) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE documento_obrigatorio_reenquadramento 
	ADD CONSTRAINT documento_obrigatorio_reenquadramento_ide_pessoa_validacao_fkey 
	FOREIGN KEY (ide_pessoa_validacao) 
	REFERENCES pessoa (ide_pessoa) 
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE boleto_dae_requerimento ADD COLUMN ide_tipo_boleto_pagamento integer;

ALTER TABLE boleto_dae_requerimento
  ADD CONSTRAINT boleto_dae_requerimento_ide_tipo_boleto_pagamento_fkey FOREIGN KEY (ide_tipo_boleto_pagamento)
      REFERENCES tipo_boleto_pagamento (ide_tipo_boleto_pagamento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE boleto_dae_requerimento
   ALTER COLUMN ide_requerimento DROP NOT NULL;
   
ALTER TABLE boleto_dae_requerimento
  ADD COLUMN ide_processo integer;
  
  
ALTER TABLE boleto_dae_requerimento
  ADD CONSTRAINT boleto_dae_requerimento_ide_processo_fkey FOREIGN KEY (ide_processo)
      REFERENCES processo (ide_processo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

      
ALTER TABLE boleto_dae_requerimento ADD COLUMN ide_processo_reenquadramento integer;

ALTER TABLE boleto_dae_requerimento
  ADD CONSTRAINT boleto_dae_requerimento_ide_processo_reenquadramento FOREIGN KEY (ide_processo_reenquadramento)
      REFERENCES processo_reenquadramento (ide_processo_reenquadramento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
      
ALTER TABLE boleto_pagamento_requerimento ADD COLUMN ide_processo_reenquadramento integer;

ALTER TABLE boleto_pagamento_requerimento
  ADD CONSTRAINT boleto_pagamento_requerimento_ide_processo_reenquadramento FOREIGN KEY (ide_processo_reenquadramento)
      REFERENCES processo_reenquadramento (ide_processo_reenquadramento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
      
ALTER TABLE processo_ato
DROP CONSTRAINT processo_ato_ide_processo_key;

ALTER TABLE processo_ato
  ADD CONSTRAINT processo_ato_ide_processo_key UNIQUE(ide_processo, ide_ato_ambiental, ide_tipologia, ide_processo_reenquadramento);
  
--#9326 
ALTER TABLE enquadramento ADD COLUMN desc_caminho_rel_requerimento_reenq VARCHAR(100);
  
ALTER TABLE requerimento ADD COLUMN dtc_finalizacao_reenquadramento timestamp(6) without time zone;
ALTER TABLE requerimento ADD COLUMN des_caminho_resumo_original character varying(255);

CREATE SEQUENCE processo_reenquadramento_hist_ato_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE processo_reenquadramento_hist_ato
(
  ide_processo_reenquadramento_hist_ato integer NOT NULL DEFAULT nextval(('processo_reenquadramento_hist_ato_seq'::text)::regclass),
  ide_processo_reenquadramento integer NOT NULL,
  dtc_reenquadramento date,
  ide_ato_ambiental_original integer,
  ide_tipologia_original integer,
  ide_enquadramento_ato_ambiental_reenquadrado integer NOT NULL,
  CONSTRAINT processo_reenquadramento_hist_ato_processo_reenquadramento_pk PRIMARY KEY (ide_processo_reenquadramento_hist_ato),
  CONSTRAINT processo_reenquadramento_hist_ato_ato_ambiental_original_fk FOREIGN KEY (ide_ato_ambiental_original)
      REFERENCES ato_ambiental (ide_ato_ambiental) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT processo_reenquadramento_hist_ato_enquadramento_ato_ambiental_f FOREIGN KEY (ide_enquadramento_ato_ambiental_reenquadrado)
      REFERENCES enquadramento_ato_ambiental (ide_enquadramento_ato_ambiental) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT processo_reenquadramento_hist_ato_processo_reenquadramento_fk FOREIGN KEY (ide_processo_reenquadramento)
      REFERENCES processo_reenquadramento (ide_processo_reenquadramento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT processo_reenquadramento_hist_ato_tipologia_original_fk FOREIGN KEY (ide_tipologia_original)
      REFERENCES tipologia (ide_tipologia) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE processo_reenquadramento_hist_ato
  OWNER TO postgres;

CREATE INDEX fki_processo_reenquadramento_hist_ato_ato_ambiental_original_fk
  ON processo_reenquadramento_hist_ato
  USING btree
  (ide_ato_ambiental_original);

CREATE INDEX fki_processo_reenquadramento_hist_ato_enquadramento_ato_ambient
  ON processo_reenquadramento_hist_ato
  USING btree
  (ide_enquadramento_ato_ambiental_reenquadrado);

CREATE INDEX fki_processo_reenquadramento_hist_ato_ide_processo_reenquadrame
  ON processo_reenquadramento_hist_ato
  USING btree
  (ide_processo_reenquadramento);

CREATE INDEX fki_processo_reenquadramento_hist_ato_tipologia_original_fk
  ON processo_reenquadramento_hist_ato
  USING btree
  (ide_tipologia_original);


ALTER TABLE fce
   ADD COLUMN ide_processo_reenquadramento integer;
   
ALTER TABLE fce
  ADD CONSTRAINT fk_fce_ide_processo_reenquadramento FOREIGN KEY (ide_processo_reenquadramento)
      REFERENCES processo_reenquadramento (ide_processo_reenquadramento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE fce DROP CONSTRAINT unica_fce_documento_requerimento_uq;
      
CREATE UNIQUE INDEX unica_fce_documento_requerimento_uq 
	ON fce (ide_requerimento, ide_documento_obrigatorio, ide_origem_fce, COALESCE(ide_processo_reenquadramento, -1));
	
	
/*TABELAS DA FUNCIONALIDADE COMUNICACAO ES21*/

CREATE TABLE public.comunicacao_status (
	ide_comunicacao_status int4 NOT NULL,
	dsc_status varchar(100) NULL,
	CONSTRAINT comunicacao_status_pk PRIMARY KEY (ide_comunicacao_status)
);

-- Drop table

-- DROP TABLE public.comunicacao;
CREATE SEQUENCE comunicacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.comunicacao (
	ide_comunicacao int4 NOT NULL,
	dsc_titulo varchar(100) NOT NULL,
	ide_comunicacao_status int4 NOT NULL,
	ind_ativa bool NOT NULL,
	dtc_periodo_inicio date NULL,
	dtc_periodo_fim date NULL,
	ide_pessoa_fisica int4 NULL,
	txt_conteudo text(1200) NULL,
	tp_comunicacao varchar(1) NULL,
	dtc_criacao date NULL,
	CONSTRAINT comunicacao_pk PRIMARY KEY (ide_comunicacao)
);

ALTER TABLE public.comunicacao ADD CONSTRAINT comunicacao_fk_1 FOREIGN KEY (ide_comunicacao_status) REFERENCES comunicacao_status(ide_comunicacao_status);


-- Drop table

-- DROP TABLE public.comunicacao_perfil;

CREATE TABLE public.comunicacao_perfil (
	ide_comunicacao int4 NULL,
	ide_perfil int4 NULL
);

ALTER TABLE public.comunicacao_perfil ADD CONSTRAINT comunicacao_perfil_fk FOREIGN KEY (ide_comunicacao) REFERENCES comunicacao(ide_comunicacao);
ALTER TABLE public.comunicacao_perfil ADD CONSTRAINT perfil_fk FOREIGN KEY (ide_perfil) REFERENCES perfil(ide_perfil);
	
	