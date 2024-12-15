BEGIN;

CREATE SEQUENCE public.ide_obras_infra_complementares_seq;

CREATE TABLE public.obras_infra_complementares (
                ide_obras_infra_complementares INTEGER NOT NULL DEFAULT nextval('public.ide_obras_infra_complementares_seq'),
                nom_obras_infra_complementares VARCHAR(150) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT obras_infra_complementares_pk PRIMARY KEY (ide_obras_infra_complementares)
);


CREATE SEQUENCE public.ide_material_utilizado_seq;

CREATE TABLE public.material_utilizado (
                ide_material_utilizado INTEGER NOT NULL DEFAULT nextval('public.ide_material_utilizado_seq'),
                nom_material_utilizado VARCHAR(100) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT material_utilizado_pk PRIMARY KEY (ide_material_utilizado)
);


CREATE SEQUENCE public.ide_uso_reservatorio_seq;

CREATE TABLE public.uso_reservatorio (
                ide_uso_reservatorio INTEGER NOT NULL DEFAULT nextval('public.ide_uso_reservatorio_seq'),
                nom_uso_reservatorio VARCHAR(100) NOT NULL,
                ind_ativo BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT ide_uso_reservatorio_pk PRIMARY KEY (ide_uso_reservatorio)
);


CREATE SEQUENCE public.ide_fce_barragem_seq;

CREATE TABLE public.fce_barragem (
                ide_fce_barragem INTEGER NOT NULL DEFAULT nextval('public.ide_fce_barragem_seq'),
                ide_localizacao_geografica INTEGER NOT NULL,
                ide_fce INTEGER NOT NULL,
                val_vazao_maxima NUMERIC(10,2) NOT NULL,
                val_vazao_regularizada NUMERIC(10,2) NOT NULL,
                val_descarga_fundo NUMERIC(10,2) NOT NULL,
                val_altura_maxima NUMERIC(10,2) NOT NULL,
                val_volume_maximo_acumulado NUMERIC(10,2) NOT NULL,
                val_cota_maxima NUMERIC(10,2) NOT NULL,
                val_cota_minima NUMERIC(10,2) NOT NULL,
                val_cota_media_diarias NUMERIC(10,2) NOT NULL,
                CONSTRAINT ide_fce_barragem_pk PRIMARY KEY (ide_fce_barragem)
);


CREATE SEQUENCE public.ide_fce_barragem_licenciamento_seq;

CREATE TABLE public.fce_barragem_licenciamento (
                ide_fce_barragem_licenciamento INTEGER NOT NULL DEFAULT nextval('public.ide_fce_barragem_licenciamento_seq'),
                ide_fce_barragem INTEGER NOT NULL,
                ide_poligonal_app INTEGER NOT NULL,
                ide_poligonal_area_suprimida INTEGER NOT NULL,
                CONSTRAINT fce_barragem_licenciamento_pk PRIMARY KEY (ide_fce_barragem_licenciamento)
);


CREATE TABLE public.fce_barrag_licenc_loca_geo (
                ide_localizacao_geografica INTEGER NOT NULL,
                ide_fce_barragem_licenciamento INTEGER NOT NULL,
                nom_rio VARCHAR(80) NOT NULL,
                CONSTRAINT fce_barrag_licenc_loca_geo_pk PRIMARY KEY (ide_localizacao_geografica, ide_fce_barragem_licenciamento)
);


CREATE TABLE public.fce_barragem_obras_infra (
                ide_fce_barragem_licenciamento INTEGER NOT NULL,
                ide_obras_infra_complementares INTEGER NOT NULL,
                CONSTRAINT fce_barragem_obras_infra_pk PRIMARY KEY (ide_fce_barragem_licenciamento, ide_obras_infra_complementares)
);


CREATE TABLE public.fce_barragem_material_utilizado (
                ide_material_utilizado INTEGER NOT NULL,
                ide_fce_barragem_licenciamento INTEGER NOT NULL,
                CONSTRAINT fce_barragem_material_utilizado_pk PRIMARY KEY (ide_material_utilizado, ide_fce_barragem_licenciamento)
);


CREATE TABLE public.fce_barragem_uso_reservatorio (
                ide_uso_reservatorio INTEGER NOT NULL,
                ide_fce_barragem INTEGER NOT NULL,
                CONSTRAINT ide_fce_barragem_uso_reservatorio_pk PRIMARY KEY (ide_uso_reservatorio, ide_fce_barragem)
);


ALTER TABLE public.fce_barragem_obras_infra ADD CONSTRAINT obras_infra_complementares_obras_infra_fce_barragem_fk
FOREIGN KEY (ide_obras_infra_complementares)
REFERENCES public.obras_infra_complementares (ide_obras_infra_complementares)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_barragem_material_utilizado ADD CONSTRAINT material_obrigatorio_material_utilizado_fce_barragem_fk
FOREIGN KEY (ide_material_utilizado)
REFERENCES public.material_utilizado (ide_material_utilizado)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_barrag_licenc_loca_geo ADD CONSTRAINT localizacao_geografica_fce_barrag_licenc_loca_geo_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_licenciamento ADD CONSTRAINT localizacao_geografica_fce_barragem_licenciamento__fk
FOREIGN KEY (ide_poligonal_app)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_licenciamento ADD CONSTRAINT localizacao_geografica_fce_barragem_licenciamento__fk1
FOREIGN KEY (ide_poligonal_area_suprimida)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem ADD CONSTRAINT localizacao_geografica_fce_barragem_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_uso_reservatorio ADD CONSTRAINT uso_reservatorio_fce_barragem_uso_reservatorio_fk
FOREIGN KEY (ide_uso_reservatorio)
REFERENCES public.uso_reservatorio (ide_uso_reservatorio)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem ADD CONSTRAINT fce_fce_barragem_fk
FOREIGN KEY (ide_fce)
REFERENCES public.fce (ide_fce)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_uso_reservatorio ADD CONSTRAINT fce_barragem_fce_barragem_uso_reservatorio_fk
FOREIGN KEY (ide_fce_barragem)
REFERENCES public.fce_barragem (ide_fce_barragem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_licenciamento ADD CONSTRAINT fce_barragem_fce_barragem_licenciamento__fk
FOREIGN KEY (ide_fce_barragem)
REFERENCES public.fce_barragem (ide_fce_barragem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_material_utilizado ADD CONSTRAINT fce_barragem_licenciamento__material_utilizado_fce_barragem_fk
FOREIGN KEY (ide_fce_barragem_licenciamento)
REFERENCES public.fce_barragem_licenciamento (ide_fce_barragem_licenciamento)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barragem_obras_infra ADD CONSTRAINT fce_barragem_licenciamento__obras_infra_fce_barragem_fk
FOREIGN KEY (ide_fce_barragem_licenciamento)
REFERENCES public.fce_barragem_licenciamento (ide_fce_barragem_licenciamento)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.fce_barrag_licenc_loca_geo ADD CONSTRAINT fce_barragem_licenciamento__fce_barrag_licenc_loc_geo_fk
FOREIGN KEY (ide_fce_barragem_licenciamento)
REFERENCES public.fce_barragem_licenciamento (ide_fce_barragem_licenciamento)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

-- RELACIOANMENTO FCE INTERVENCAO BARRAGEM
ALTER TABLE public.fce_intervencao_barragem
ADD ide_fce_barragem INTEGER;

ALTER TABLE public.fce_intervencao_barragem ADD CONSTRAINT fce_barragem_intervencao_barragem__fk1
FOREIGN KEY (ide_fce_barragem)
REFERENCES public.fce_barragem (ide_fce_barragem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

-- INSERTS DAS TABELAS

-- // Uso do Reservatório //
INSERT INTO public.uso_reservatorio (nom_uso_reservatorio, ind_ativo) VALUES ('Abastecimento Humano', true);
INSERT INTO public.uso_reservatorio (nom_uso_reservatorio, ind_ativo) VALUES ('Irrigação', true);
INSERT INTO public.uso_reservatorio (nom_uso_reservatorio, ind_ativo) VALUES ('Aquicultura', true);
INSERT INTO public.uso_reservatorio (nom_uso_reservatorio, ind_ativo) VALUES ('Outros', true);

-- // Material Utilizado //
INSERT INTO public.material_utilizado (nom_material_utilizado, ind_ativo) VALUES ('Terra', true);
INSERT INTO public.material_utilizado (nom_material_utilizado, ind_ativo) VALUES ('CCR', true);
INSERT INTO public.material_utilizado (nom_material_utilizado, ind_ativo) VALUES ('Concreto Armado', true);
INSERT INTO public.material_utilizado (nom_material_utilizado, ind_ativo) VALUES ('Outros', true);

-- // Obras Infraestruturas Complementares //
INSERT INTO public.obras_infra_complementares (nom_obras_infra_complementares, ind_ativo) VALUES ('Obras de acesso e controle de entrada aos locais das obras', true);
INSERT INTO public.obras_infra_complementares (nom_obras_infra_complementares, ind_ativo) VALUES ('Controle e operação tais como: rede viária, guarita, estacionamento interno, casa dos operadores, dentre outros', true);
INSERT INTO public.obras_infra_complementares (nom_obras_infra_complementares, ind_ativo) VALUES ('Edificações para residência do pessoal permanente', true);
INSERT INTO public.obras_infra_complementares (nom_obras_infra_complementares, ind_ativo) VALUES ('Outros', true);

COMMIT;

