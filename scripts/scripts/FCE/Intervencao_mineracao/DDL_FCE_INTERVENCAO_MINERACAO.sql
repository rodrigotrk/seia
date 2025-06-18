---

CREATE SEQUENCE public.fce_intervencao_mineracao_seq;

CREATE TABLE public.fce_intervencao_mineracao (
                ide_fce_intervencao_mineracao INTEGER NOT NULL DEFAULT nextval('public.fce_intervencao_mineracao_seq'),
                ide_fce INTEGER NOT NULL,
                dsc_tratamento_efluente VARCHAR(500),
                CONSTRAINT fce_intervencao_mineracao_pk PRIMARY KEY (ide_fce_intervencao_mineracao)
);


CREATE SEQUENCE public.lancamento_efluente_localizacao_geografica_seq;

CREATE TABLE public.lancamento_efluente_localizacao_geografica (
                ide_lancamento_efluente_localizacao_geografica INTEGER NOT NULL DEFAULT nextval('public.lancamento_efluente_localizacao_geografica_seq'),
                ide_fce_intervencao_mineracao INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER NOT NULL,
                CONSTRAINT lancamento_efluente_localizacao_geografica_pk PRIMARY KEY (ide_lancamento_efluente_localizacao_geografica)
);


CREATE SEQUENCE public.tipo_mineral_extraido_seq;

CREATE TABLE public.tipo_mineral_extraido (
                ide_tipo_mineral_extraido INTEGER NOT NULL DEFAULT nextval('public.tipo_mineral_extraido_seq'),
                nom_tipo_mineral_extraido VARCHAR(100) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT tipo_mineral_extraido_pk PRIMARY KEY (ide_tipo_mineral_extraido)
);

INSERT INTO public.tipo_mineral_extraido (nom_tipo_mineral_extraido) VALUES ('Areia');
INSERT INTO public.tipo_mineral_extraido (nom_tipo_mineral_extraido) VALUES ('Outros');


CREATE SEQUENCE public.fce_intervencao_caracteristica_extracao_seq;

CREATE TABLE public.fce_intervencao_caracteristica_extracao (
                ide_fce_intervencao_caracteristica_extracao INTEGER NOT NULL DEFAULT nextval('public.fce_intervencao_caracteristica_extracao_seq'),
                ide_localizacao_geografica_ini INTEGER NOT NULL,
                ide_localizacao_geografica_fim INTEGER NOT NULL,
                ide_fce_intervencao_mineracao INTEGER NOT NULL,
                nom_manancial VARCHAR(100) NOT NULL,
                nom_coordenada VARCHAR(100) NOT NULL,
                val_periodo_mineracao INTEGER NOT NULL,
                val_vazao_mineral_extraido_polpa NUMERIC(10,2) NOT NULL,
                val_vazao_agua_polpa NUMERIC(10,2) NOT NULL,
                val_vazao_polpa NUMERIC(10,2) NOT NULL,
                val_teor_umidade NUMERIC(10,2) NOT NULL,
                val_area_lavra NUMERIC(12,4) NOT NULL,
                val_producao_maxima_mensal NUMERIC(10,2) NOT NULL,
                val_qtde_dias_producao_mes INTEGER NOT NULL,
                val_vazao_lancamento_manancial NUMERIC(10,2) NOT NULL,
                ide_tipo_mineral_extraido INTEGER NOT NULL,
                CONSTRAINT fce_intervencao_caracteristica_extracao_pk PRIMARY KEY (ide_fce_intervencao_caracteristica_extracao)
);


CREATE SEQUENCE public.tipo_caracteristica_extracao_seq;

CREATE TABLE public.tipo_caracteristica_extracao (
                ide_tipo_caracteristica_extracao INTEGER NOT NULL DEFAULT nextval('public.tipo_caracteristica_extracao_seq'),
                nom_caracteristica_extracao VARCHAR(150) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT tipo_caracteristica_extracao_pk PRIMARY KEY (ide_tipo_caracteristica_extracao)
);


INSERT INTO public.tipo_caracteristica_extracao (nom_caracteristica_extracao) VALUES ('Leito Seco');
INSERT INTO public.tipo_caracteristica_extracao (nom_caracteristica_extracao) VALUES ('Dragagem');
INSERT INTO public.tipo_caracteristica_extracao (nom_caracteristica_extracao) VALUES ('Outros');


CREATE SEQUENCE public.fce_intervencao_tipo_caract_extracao_seq;

CREATE TABLE public.fce_intervencao_tipo_caract_extracao (
                ide_fce_intervencao_tipo_caract_extracao INTEGER NOT NULL DEFAULT nextval('public.fce_intervencao_tipo_caract_extracao_seq'),
                ide_tipo_caracteristica_extracao INTEGER NOT NULL,
                ide_fce_intervencao_caracteristica_extracao INTEGER NOT NULL,
                CONSTRAINT fce_intervencao_tipo_caract_extracao_pk PRIMARY KEY (ide_fce_intervencao_tipo_caract_extracao)
);


ALTER TABLE public.lancamento_efluente_localizacao_geografica ADD CONSTRAINT localizacao_geografica_lancamento_efluente_local_geografica_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_caracteristica_extracao ADD CONSTRAINT localizacao_geografica_fce_intervencao_mineracao_local_geogr948
FOREIGN KEY (ide_localizacao_geografica_ini)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_caracteristica_extracao ADD CONSTRAINT localizacao_geografica_fce_intervencao_mineracao_local_geogr949
FOREIGN KEY (ide_localizacao_geografica_fim)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_mineracao ADD CONSTRAINT fce_fce_intervencao_recursos_hidricos_mineracao_fk
FOREIGN KEY (ide_fce)
REFERENCES public.fce (ide_fce)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.lancamento_efluente_localizacao_geografica ADD CONSTRAINT fce_intervencao_recursos_hidricos_mineracao_lancamento_eflue319
FOREIGN KEY (ide_fce_intervencao_mineracao)
REFERENCES public.fce_intervencao_mineracao (ide_fce_intervencao_mineracao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_caracteristica_extracao ADD CONSTRAINT fce_intervencao_mineracao_fce_intervencao_mineracao_local_ge706
FOREIGN KEY (ide_fce_intervencao_mineracao)
REFERENCES public.fce_intervencao_mineracao (ide_fce_intervencao_mineracao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_caracteristica_extracao ADD CONSTRAINT tipo_mineral_extraido_fce_intervencao_mineracao_localizacao_632
FOREIGN KEY (ide_tipo_mineral_extraido)
REFERENCES public.tipo_mineral_extraido (ide_tipo_mineral_extraido)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_tipo_caract_extracao ADD CONSTRAINT fce_intervencao_caracteristica_extracao_fce_caracteristicas_81
FOREIGN KEY (ide_fce_intervencao_caracteristica_extracao)
REFERENCES public.fce_intervencao_caracteristica_extracao (ide_fce_intervencao_caracteristica_extracao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_intervencao_tipo_caract_extracao ADD CONSTRAINT caracteristica_extracao_fce_intervencao_recursos_hidricos_ca950
FOREIGN KEY (ide_tipo_caracteristica_extracao)
REFERENCES public.tipo_caracteristica_extracao (ide_tipo_caracteristica_extracao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;