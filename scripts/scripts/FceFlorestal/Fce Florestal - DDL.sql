CREATE SEQUENCE public.fce_florestal_ide_fce_florestal_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE public.fce_florestal
(
   ide_fce_florestal integer NOT NULL DEFAULT nextval(('fce_florestal_ide_fce_florestal_seq'::text)::regclass), 
   ide_fce integer NOT NULL, 
   num_area_app numeric(12,4), 
   num_area_supressao numeric(12,4), 
   num_area_suprimida numeric(5,2), 
   val_area numeric(12,4), 
   val_area_und_producao numeric(12,4), 
   ind_app boolean, 
   ind_material_lenhoso boolean, 
   ind_aceite boolean, 

   CONSTRAINT pk_fce_florestal_ide_fce_florestal PRIMARY KEY (ide_fce_florestal), 
   CONSTRAINT fk_fce_ide_fce FOREIGN KEY (ide_fce) REFERENCES fce (ide_fce) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.tipo_especie_florestal_ide_tipo_especie_florestal_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE public.tipo_especie_florestal
(
   ide_tipo_especie_florestal integer NOT NULL DEFAULT nextval(('tipo_especie_florestal_ide_tipo_especie_florestal_seq'::text)::regclass), 
   dsc_tipo_especie_florestal VARCHAR(50),
   ind_ativo boolean,

   CONSTRAINT pk_tipo_especie_florestal_ide_tipo_especie_florestal PRIMARY KEY (ide_tipo_especie_florestal)
);

CREATE SEQUENCE public.especie_florestal_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE public.especie_florestal
(
   ide_especie_florestal integer NOT NULL DEFAULT nextval(('especie_florestal_seq'::text)::regclass), 
   ide_tipo_especie_florestal integer NOT NULL,
   nom_especie_florestal VARCHAR(50),
   ind_ativo boolean,

   CONSTRAINT pk_especie_florestal_ide_especie_florestal PRIMARY KEY (ide_especie_florestal),
   CONSTRAINT fk_tipo_especie_florestal_ide_tipo_especie_florestal FOREIGN KEY (ide_tipo_especie_florestal) REFERENCES tipo_especie_florestal (ide_tipo_especie_florestal) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE especie_florestal_nome_popular_especie
(
  ide_especie_florestal integer NOT NULL,
  ide_nome_popular_especie integer NOT NULL,
  
  CONSTRAINT pk_especie_florestal_nome_popular_especie PRIMARY KEY (ide_especie_florestal, ide_nome_popular_especie),
  CONSTRAINT fk_esp_flo_nom_pop_esp_nome_popular_especie FOREIGN KEY (ide_nome_popular_especie)
      REFERENCES nome_popular_especie (ide_nome_popular_especie) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_nom_pop_esp_especie_florestal FOREIGN KEY (ide_especie_florestal)
      REFERENCES especie_florestal (ide_especie_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.especie_florestal_autorizacao_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE especie_florestal_autorizacao
(
  ide_especie_florestal_autorizacao integer NOT NULL DEFAULT nextval(('especie_florestal_autorizacao_seq'::text)::regclass),
  volume_total_fora_app numeric(10,2),
  volume_total_em_app numeric(10,2),
  ide_fce_florestal integer,
  ide_processo_ato integer,
  ide_processo_ato_concedido integer,
  ide_produto integer,
  ide_especie_florestal integer,
  ide_nome_popular_especie integer,
  
  CONSTRAINT pk_especie_florestal_autorizacao PRIMARY KEY (ide_especie_florestal_autorizacao),
  CONSTRAINT fk_esp_flo_aut_fce_florestal FOREIGN KEY (ide_fce_florestal)
      REFERENCES fce_florestal (ide_fce_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_especie_florestal FOREIGN KEY (ide_especie_florestal)
      REFERENCES especie_florestal (ide_especie_florestal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_ide_processo_ato_especie FOREIGN KEY (ide_processo_ato)
      REFERENCES processo_ato (ide_processo_ato) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_nome_popular_especie FOREIGN KEY (ide_nome_popular_especie)
      REFERENCES nome_popular_especie (ide_nome_popular_especie) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_processo_ato_concedido_especie FOREIGN KEY (ide_processo_ato_concedido)
      REFERENCES processo_ato_concedido (ide_processo_ato_concedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_ide_produto FOREIGN KEY (ide_produto)
      REFERENCES produto (ide_produto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.especie_florestal_aut_destino_socio_economico_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE especie_florestal_aut_destino_socio_economico
(
  ide_especie_florestal_aut_destino_socio_economico integer NOT NULL DEFAULT nextval(('especie_florestal_aut_destino_socio_economico_seq'::text)::regclass),
  ide_especie_florestal_autorizacao integer NOT NULL,
  ide_destino_socioeconomico integer NOT NULL,

  CONSTRAINT pk_especie_florestal_aut_destino_socio_economico PRIMARY KEY (ide_especie_florestal_aut_destino_socio_economico),
  CONSTRAINT fk_esp_flo_aut_dest_socio_eco_destino_socioeconomico FOREIGN KEY (ide_destino_socioeconomico)
      REFERENCES destino_socioeconomico (ide_destino_socioeconomico) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esp_flo_aut_dest_socio_eco_especie_florestal_autorizacao FOREIGN KEY (ide_especie_florestal_autorizacao)
      REFERENCES especie_florestal_autorizacao (ide_especie_florestal_autorizacao) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
