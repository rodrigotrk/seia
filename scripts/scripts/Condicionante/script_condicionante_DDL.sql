CREATE SEQUENCE public.segmento_ide_segmento_seq;

CREATE TABLE public.segmento (
                ide_segmento INTEGER NOT NULL DEFAULT nextval('public.segmento_ide_segmento_seq'),
                nom_segmento VARCHAR(100) NOT NULL,
                ind_excluido BOOLEAN,
                CONSTRAINT ide_segmento PRIMARY KEY (ide_segmento)
);


CREATE SEQUENCE public.condicionante_ambiental_ide_condicionante_seq;

CREATE TABLE public.condicionante_ambiental (
                ide_condicionante INTEGER NOT NULL DEFAULT nextval('public.condicionante_ambiental_ide_condicionante_seq'),
                nom_condicionante VARCHAR(100) NOT NULL,
                ind_excluido BOOLEAN,
                ide_segmento INTEGER NOT NULL,
                CONSTRAINT ide_condicionante PRIMARY KEY (ide_condicionante)
);

CREATE TABLE public.condicionante_analise_tecnica (
                ide_analise_tecnica INTEGER DEFAULT nextval('analise_tecnica_seq'::regclass) NOT NULL,
                ide_condicionante INTEGER NOT NULL
);

CREATE TABLE condicionante_ato_ambiental
(
  ide_ato_ambiental integer NOT NULL DEFAULT nextval(('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq'::text)::regclass),
  ide_condicionante integer NOT NULL,
  CONSTRAINT ato_ambiental_condicionante_ato_ambiental_fk FOREIGN KEY (ide_ato_ambiental)
      REFERENCES ato_ambiental (ide_ato_ambiental) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT condicionante_ambiental_condicionante_ato_ambiental_fk FOREIGN KEY (ide_condicionante)
      REFERENCES condicionante_ambiental (ide_condicionante) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT condicionante_ato_ambiental_ide_ato_ambiental_key UNIQUE (ide_ato_ambiental, ide_condicionante)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.condicionante_ambiental ADD CONSTRAINT segmento_condicionante_ambiental_fk
FOREIGN KEY (ide_segmento)
REFERENCES public.segmento (ide_segmento);

ALTER TABLE public.condicionante_analise_tecnica ADD CONSTRAINT condicionante_ambiental_condicionante_analise_tecnica_fk
FOREIGN KEY (ide_condicionante)
REFERENCES public.condicionante_ambiental (ide_condicionante);

ALTER TABLE public.condicionante_analise_tecnica ADD CONSTRAINT analise_tecnica_condicionante_analise_tecnica_fk
FOREIGN KEY (ide_analise_tecnica)
REFERENCES public.analise_tecnica (ide_analise_tecnica);

ALTER TABLE secao
   ALTER COLUMN dsc_secao TYPE character varying(25);
