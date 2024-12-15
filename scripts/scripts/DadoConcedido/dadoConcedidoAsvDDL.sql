CREATE SEQUENCE public.processo_ato_concedido_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE processo_ato_concedido
(
	ide_processo_ato_concedido INTEGER DEFAULT nextval(('processo_ato_concedido_seq'::text)::regclass) NOT NULL,
	ide_processo_ato     INTEGER NOT NULL,
	ide_tipologia INTEGER NULL,
	ide_tipo_area_concedida INTEGER NOT NULL,
	ide_localizacao_geografica INTEGER NULL,
	val_atividade        NUMERIC(10,4) NULL,
	ide_imovel           INTEGER NULL
);

ALTER TABLE processo_ato_concedido
ADD PRIMARY KEY (ide_processo_ato_concedido);

CREATE TABLE tipo_area_concedida
(
	ide_tipo_area_concedida INTEGER NOT NULL,
	des_tipo_area_concedida VARCHAR(20) NOT NULL
);

ALTER TABLE tipo_area_concedida
ADD PRIMARY KEY (ide_tipo_area_concedida);

ALTER TABLE processo_ato_concedido
ADD CONSTRAINT fk_processo_ato_concedido_processo_ato FOREIGN KEY (ide_processo_ato) REFERENCES processo_ato (ide_processo_ato);

ALTER TABLE processo_ato_concedido
ADD CONSTRAINT fk_processo_ato_concedido_tipologia FOREIGN KEY (ide_tipologia) REFERENCES tipologia (ide_tipologia);

ALTER TABLE processo_ato_concedido
ADD CONSTRAINT fk_processo_ato_concedido_tipo_area FOREIGN KEY (ide_tipo_area_concedida) REFERENCES tipo_area_concedida (ide_tipo_area_concedida);

ALTER TABLE processo_ato_concedido
ADD CONSTRAINT fk_processo_ato_concedido_localizacao_geografica FOREIGN KEY (ide_localizacao_geografica) REFERENCES localizacao_geografica (ide_localizacao_geografica);

ALTER TABLE processo_ato_concedido
ADD CONSTRAINT fk_processo_ato_concedido_imovel FOREIGN KEY (ide_imovel) REFERENCES imovel (ide_imovel);