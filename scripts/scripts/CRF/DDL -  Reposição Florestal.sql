CREATE SEQUENCE declaracao_parcial_dae_ide_seq;

CREATE SEQUENCE pagamento_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE cumprimento_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE tipo_volume_florestal_remanescente_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE detentor_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE consumidor_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE numero_car_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE devedor_reposicao_florestal_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE ORGAO_EMISSOR_AUTO_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE MEMORIA_CALCULO_DAE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE INDICE_COBRANCA_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE SEQUENCE TAXA_INDICE_COBRANCA_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE TABLE pagamento_reposicao_florestal
(
  ide_pagamento_reposicao_florestal integer NOT NULL DEFAULT nextval(('pagamento_reposicao_florestal_seq'::text)::regclass),
  nom_pagamento_reposicao_florestal character varying(150) NOT NULL,
  ide_pagamento_reposicao_florestal_pai integer,
  CONSTRAINT pagamento_reposicao_florestal_pkey PRIMARY KEY (ide_pagamento_reposicao_florestal),
  CONSTRAINT fk44e656d984794eaa FOREIGN KEY (ide_pagamento_reposicao_florestal_pai)
      REFERENCES pagamento_reposicao_florestal (ide_pagamento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE cumprimento_reposicao_florestal
(
  ide_cumprimento_reposicao_florestal integer NOT NULL DEFAULT nextval(('cumprimento_reposicao_florestal_seq'::text)::regclass),
  ind_ciente boolean NOT NULL,
  ide_pagamento_reposicao_florestal integer NOT NULL,
  ide_requerimento integer NOT NULL,
  vlr_pecuniario numeric(12,2),
  CONSTRAINT cumprimento_reposicao_florestal_pkey PRIMARY KEY (ide_cumprimento_reposicao_florestal),
  CONSTRAINT fkcf6c49f834e010d1 FOREIGN KEY (ide_pagamento_reposicao_florestal)
      REFERENCES pagamento_reposicao_florestal (ide_pagamento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkcf6c49f8b57dd5a5 FOREIGN KEY (ide_requerimento)
      REFERENCES requerimento (ide_requerimento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE tipo_volume_florestal_remanescente
(
  ide_tipo_volume_florestal_remanescente integer NOT NULL DEFAULT nextval(('tipo_volume_florestal_remanescente_seq'::text)::regclass),
  nom_tipo_volume_florestal_remanescente character varying(255) NOT NULL,
  CONSTRAINT tipo_volume_florestal_remanescente_pkey PRIMARY KEY (ide_tipo_volume_florestal_remanescente)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE detentor_reposicao_florestal
(
  ide_detentor_reposicao_florestal integer NOT NULL DEFAULT nextval(('detentor_reposicao_florestal_seq'::text)::regclass),
  data_publicacao_portaria timestamp without time zone NOT NULL,
  num_portaria character varying(100) NOT NULL,
  num_processo character varying(100) NOT NULL,
  num_processo_asv_aml character varying(100),
  volume_autorizado numeric(12,4) NOT NULL,
  ide_cumprimento_reposicao_florestal integer NOT NULL,
  ide_tipo_volume_florestal_remanescente integer,
  ide_unidade_medida integer NOT NULL,
  CONSTRAINT detentor_reposicao_florestal_pkey PRIMARY KEY (ide_detentor_reposicao_florestal),
  CONSTRAINT fkb1c7ef0ed8d6ba0f FOREIGN KEY (ide_cumprimento_reposicao_florestal)
      REFERENCES cumprimento_reposicao_florestal (ide_cumprimento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkb1c7ef0efeef3218 FOREIGN KEY (ide_tipo_volume_florestal_remanescente)
      REFERENCES tipo_volume_florestal_remanescente (ide_tipo_volume_florestal_remanescente) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT detentor_reposicao_florestal_unidade_medida_fk FOREIGN KEY (ide_unidade_medida)
      REFERENCES unidade_medida (ide_unidade_medida) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE consumidor_reposicao_florestal
(
  ide_consumidor_reposicao_florestal integer NOT NULL DEFAULT nextval(('consumidor_reposicao_florestal'::text)::regclass),
  data_publicacao_portaria timestamp without time zone NOT NULL,
  ind_possui_numero_car boolean NOT NULL,
  num_portaria_ato_adquirido character varying(100) NOT NULL,
  num_processo_original character varying(100) NOT NULL,
  vlm_material_lenhoso_consumido numeric(12,4) NOT NULL,
  ide_cumprimento_reposicao_florestal integer NOT NULL,
  ide_unidade_medida integer NOT NULL,
  CONSTRAINT consumidor_reposicao_florestal_pkey PRIMARY KEY (ide_consumidor_reposicao_florestal),
  CONSTRAINT fkfd05f27ad8d6ba0f FOREIGN KEY (ide_cumprimento_reposicao_florestal)
      REFERENCES cumprimento_reposicao_florestal (ide_cumprimento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT detentor_reposicao_florestal_unidade_medida_fk FOREIGN KEY (ide_unidade_medida)
      REFERENCES unidade_medida (ide_unidade_medida) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE numero_car_reposicao_florestal
(
  ide_numero_car_reposicao_florestal integer NOT NULL DEFAULT nextval(('numero_car_reposicao_florestal'::text)::regclass),
  num_car character varying(50) NOT NULL,
  ide_consumidor_reposicao_florestal integer NOT NULL,
  CONSTRAINT numero_car_reposicao_florestal_pkey PRIMARY KEY (ide_numero_car_reposicao_florestal),
  CONSTRAINT fk6e53f964e674908d FOREIGN KEY (ide_consumidor_reposicao_florestal)
      REFERENCES consumidor_reposicao_florestal (ide_consumidor_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE orgao_emissor_auto
(
  ide_orgao_emissor_auto integer NOT NULL DEFAULT nextval(('ORGAO_EMISSOR_AUTO_SEQ'::text)::regclass),
  nom_orgao_emissor_auto character varying(100) NOT NULL,
  CONSTRAINT orgao_emissor_auto_pkey PRIMARY KEY (ide_orgao_emissor_auto)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE devedor_reposicao_florestal
(
  ide_devedor_reposicao_florestal integer NOT NULL DEFAULT nextval(('devedor_reposicao_florestal_seq'::text)::regclass),
  ide_cumprimento_reposicao_florestal integer NOT NULL,
  ide_orgao_emissor_auto integer NOT NULL,
  ide_bioma integer NOT NULL,
  ide_municipio integer,
  num_auto_infracao character varying(50) NOT NULL,
  vlr_area_suprimida numeric(12,4) NOT NULL,
  CONSTRAINT devedor_reposicao_florestal_pkey PRIMARY KEY (ide_devedor_reposicao_florestal),
  CONSTRAINT devedor_reposicao_florestal_consumidor_fk FOREIGN KEY (ide_cumprimento_reposicao_florestal)
      REFERENCES cumprimento_reposicao_florestal (ide_cumprimento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT orgao_emissor_auto_consumidor_fk FOREIGN KEY (ide_orgao_emissor_auto)
      REFERENCES orgao_emissor_auto (ide_orgao_emissor_auto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bioma_consumidor_fk FOREIGN KEY (ide_bioma)
      REFERENCES bioma (ide_bioma) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT devedor_reposicao_florestal_municioio_fk FOREIGN KEY (ide_municipio)
      REFERENCES municipio (ide_municipio) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE memoria_calculo_dae
(
  ide_memoria_calculo_dae integer NOT NULL DEFAULT nextval(('SEQUENCE MEMORIA_CALCULO_DAE_SEQ'::text)::regclass),
  ide_cumprimento_reposicao_florestal integer NOT NULL,
  ide_parametro_calculo integer NOT NULL,
  valor_total numeric(12,2) NOT NULL,
  qtd_parcelas integer NOT NULL,
  ind_parcelado boolean NOT NULL,
  CONSTRAINT memoria_calculo_dae_pkey PRIMARY KEY (ide_memoria_calculo_dae),
  CONSTRAINT memoria_calculo_dae_cumprimento_fk FOREIGN KEY (ide_cumprimento_reposicao_florestal)
      REFERENCES cumprimento_reposicao_florestal (ide_cumprimento_reposicao_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT memoria_calculo_dae_parametro_calculo_fk FOREIGN KEY (ide_parametro_calculo)
      REFERENCES parametro_calculo (ide_parametro_calculo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE memoria_calculo_dae_parcela
(
  ide_memoria_calculo_dae_parcela integer NOT NULL DEFAULT nextval(('SEQUENCE MEMORIA_CALCULO_DAE_SEQ'::text)::regclass),
  ide_memoria_calculo_dae integer NOT NULL,
  ide_dae integer,
  valor numeric(12,4) NOT NULL,
  num_parcela_referencia integer NOT NULL,
  dtc_exclusao date,
  ind_excluido boolean NOT NULL,
  ind_parcela_unica boolean NOT NULL,
  CONSTRAINT memoria_calculo_dae_parcela_pkey PRIMARY KEY (ide_memoria_calculo_dae_parcela),
  CONSTRAINT memoria_calculo_dae_situacao_calculo_dae_fk FOREIGN KEY (ide_memoria_calculo_dae)
      REFERENCES memoria_calculo_dae (ide_memoria_calculo_dae) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT memoria_calculo_dae_situacao_dae_fk FOREIGN KEY (ide_dae)
      REFERENCES dae (ide_dae) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE indice_cobranca
(
  ide_indice_cobranca integer NOT NULL DEFAULT nextval(('INDICE_COBRANCA_SEQ'::text)::regclass),
  dsc_indice_cobranca varchar(100) NOT NULL,
  sgl_indice_cobranca varchar(10) NOT NULL,
  CONSTRAINT indice_cobranca_pkey PRIMARY KEY (ide_indice_cobranca)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE taxa_indice_cobranca
(
  ide_taxa_indice_cobranca integer NOT NULL DEFAULT nextval(('TAXA_INDICE_COBRANCA_SEQ'::text)::regclass),
  ide_indice_cobranca integer NOT NULL,
  valor numeric(12,2) NOT NULL,
  ind_ativo boolean NOT NULL,
  dtc_criacao DATE NOT NULL,
  dtc_exclusao DATE,
  ide_pessoa_fisica  integer NOT NULL,
  mes_referencia integer NOT NULL,
  ano_referencia integer NOT NULL,
  CONSTRAINT taxa_indice_cobranca_pkey PRIMARY KEY (ide_taxa_indice_cobranca),
  CONSTRAINT taxa_indice_cobranca_indice_cobranca_fk FOREIGN KEY (ide_indice_cobranca)
      REFERENCES indice_cobranca (ide_indice_cobranca) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT taxa_indice_cobranca_pessoa_fisica_fk FOREIGN KEY (ide_pessoa_fisica)
      REFERENCES pessoa_fisica (ide_pessoa_fisica) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE declaracao_parcial_dae
(
  ide_declaracao_parcial_dae integer NOT NULL DEFAULT nextval(('declaracao_parcial_dae_ide_seq'::text)::regclass),
  dtc_emissao_declaracao_parcial_dae timestamp(6) without time zone NOT NULL,
  ide_ato_ambiental integer,
  ide_orgao integer NOT NULL,
  ide_requerimento integer,
  ide_memoria_calculo_dae_parcela integer not null,
  num_declaracao_parcial_dae character varying(50),
  num_token character varying(32),
  ide_tipo_certificado integer,
  CONSTRAINT pk_declaracao_parcial_dae PRIMARY KEY (ide_declaracao_parcial_dae),
  CONSTRAINT declaracao_parcial_dae_ide_ato_ambiental_fkey FOREIGN KEY (ide_ato_ambiental)
      REFERENCES ato_ambiental (ide_ato_ambiental) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT certificado_ide_orgao_fkey FOREIGN KEY (ide_orgao)
      REFERENCES orgao (ide_orgao) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT declaracao_parcial_dae_ide_requerimento_fkey FOREIGN KEY (ide_requerimento)
      REFERENCES requerimento (ide_requerimento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT declaracao_parcial_dae_ide_parcela_fkey FOREIGN KEY (ide_memoria_calculo_dae_parcela)
      REFERENCES memoria_calculo_dae_parcela (ide_memoria_calculo_dae_parcela) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT declaracao_parcial_dae_ide_tipo_certificado_fkey FOREIGN KEY (ide_tipo_certificado)
      REFERENCES tipo_certificado (ide_tipo_certificado) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE SEQUENCE tipo_certificado_crf_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  
DROP TABLE calendario;
  
CREATE TABLE calendario(
  ide_calendario integer NOT NULL DEFAULT nextval(('CALENDARIO_IDE_CALENDARIO_seq'::text)::regclass),
  dtc_feriado date NOT NULL,
  ind_feriado boolean NOT NULL,
  ind_pt_facultativo boolean NOT NULL,
  dsc_feriado character varying(100) NOT NULL,
  CONSTRAINT pk_calendario PRIMARY KEY (ide_calendario)
);