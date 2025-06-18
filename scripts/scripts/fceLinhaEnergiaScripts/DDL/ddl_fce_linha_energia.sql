BEGIN;
CREATE SEQUENCE public.ide_tipo_subestacao_seq;
CREATE TABLE public.tipo_subestacao (
                ide_tipo_subestacao INTEGER NOT NULL DEFAULT nextval('public.ide_tipo_subestacao_seq'),
                dsc_tipo_subestacao VARCHAR(50) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT tipo_subestacao_pk PRIMARY KEY (ide_tipo_subestacao)
);



/*CREATE TABLE public.tipo_residuo_gerado (
                ide_tipo_residuo_gerado INTEGER NOT NULL,
                dsc_tipo_residuo_gerado VARCHAR(40) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT pk_tipo_residuo_gerado PRIMARY KEY (ide_tipo_residuo_gerado)
);*/

CREATE SEQUENCE public.ide_tipo_energia_seq;
CREATE TABLE public.tipo_energia (
                ide_tipo_energia INTEGER NOT NULL DEFAULT nextval('public.ide_tipo_energia_seq'),
                dsc_tipo_energia VARCHAR(50) NOT NULL,
                ind_ativo BOOLEAN NOT NULL,
                CONSTRAINT pk_tipo_energia PRIMARY KEY (ide_tipo_energia)
);

--ALTER SEQUENCE public.tipo_energia_ide_tipo_energia_seq OWNED BY public.tipo_energia.ide_tipo_energia;

CREATE UNIQUE INDEX uq_tipo_energia_dsc_tipo_energia
 ON public.tipo_energia USING BTREE
 ( dsc_tipo_energia );

CREATE SEQUENCE public.fce_linha_energia_seq;
CREATE TABLE public.fce_linha_energia (
                ide_fce_linha_energia INTEGER NOT NULL DEFAULT nextval('public.fce_linha_energia_seq'),
                val_extensao_total_linha NUMERIC(10,2) NOT NULL,
                val_largura_faixa_servidao NUMERIC(10,2) NOT NULL,
                val_tensao_operacao NUMERIC(10,2) NOT NULL,
                val_area_total_faixa_servidao NUMERIC(10,4) NOT NULL,
                ide_fce INTEGER NOT NULL,
                ind_alternativa_locacional BOOLEAN DEFAULT FALSE NOT NULL,
                obs_alternativa_locacional VARCHAR(500),
                CONSTRAINT fce_linha_energia_pk PRIMARY KEY (ide_fce_linha_energia)
);


--ALTER SEQUENCE public.fce_linha_energia_ide_fce_linha_energia_seq OWNED BY public.fce_linha_energia.ide_fce_linha_energia;
CREATE SEQUENCE public.ide_fce_linha_energia_localizacao_geografica_seq;
CREATE TABLE public.fce_linha_energia_localizacao_geografica (
		ide_fce_linha_energia_localizacao_geografica INTEGER NOT NULL DEFAULT nextval('public.ide_fce_linha_energia_localizacao_geografica_seq'),
                ide_fce_linha_energia INTEGER NOT NULL,
                ind_preferencial BOOLEAN DEFAULT TRUE NOT NULL,
                ide_localizacao_geografica INTEGER NOT NULL,
                ind_objeto_concedido BOOLEAN DEFAULT FALSE NOT NULL,
		CONSTRAINT fce_linha_energia_localizacao_geografica_pk PRIMARY KEY (ide_fce_linha_energia_localizacao_geografica)	
);


CREATE SEQUENCE public.fce_linha_energia_tipo_subestacao_seq;
CREATE TABLE public.fce_linha_energia_tipo_subestacao (
                ide_fce_linha_energia_tipo_subestacao INTEGER NOT NULL DEFAULT nextval('public.fce_linha_energia_tipo_subestacao_seq'),
                ide_fce_linha_energia INTEGER NOT NULL,
                dsc_subestacao VARCHAR NOT NULL,
                ide_tipo_subestacao INTEGER NOT NULL,
                CONSTRAINT fce_linha_energia_tipo_subestacao_pk PRIMARY KEY (ide_fce_linha_energia_tipo_subestacao)
);


--ALTER SEQUENCE public.fce_linha_energia_tipo_subestacao_ide_subestacao_seq OWNED BY public.fce_linha_energia_tipo_subestacao.ide_subestacao;
CREATE SEQUENCE public.ide_fce_linha_energia_tipo_energia_seq;
CREATE TABLE public.fce_linha_energia_tipo_energia (
		ide_fce_linha_energia_tipo_energia INTEGER NOT NULL DEFAULT nextval('public.ide_fce_linha_energia_tipo_energia_seq'),
                ide_fce_linha_energia INTEGER NOT NULL,
                ide_tipo_energia INTEGER NOT NULL,
                CONSTRAINT fce_linha_energia_tipo_energia_pk PRIMARY KEY (ide_fce_linha_energia_tipo_energia)
);

CREATE SEQUENCE public.ide_fce_linha_energia_destino_residuo_seq;
CREATE TABLE public.fce_linha_energia_destino_residuo (
		ide_fce_linha_energia_destino_residuo INTEGER NOT NULL DEFAULT nextval('public.ide_fce_linha_energia_destino_residuo_seq'),
                ide_fce_linha_energia INTEGER NOT NULL,
                ide_destino_residuo INTEGER NOT NULL,
                CONSTRAINT fce_linha_energia_destino_residuo_pk PRIMARY KEY (ide_fce_linha_energia_destino_residuo)
);

CREATE SEQUENCE public.ide_fce_linha_energia_residuo_gerado_seq;
CREATE TABLE public.fce_linha_energia_residuo_gerado (
		ide_fce_linha_energia_residuo_gerado INTEGER NOT NULL DEFAULT nextval('public.ide_fce_linha_energia_residuo_gerado_seq'),
                ide_fce_linha_energia INTEGER NOT NULL,
                ide_tipo_residuo_gerado INTEGER NOT NULL,
                CONSTRAINT fce_linha_energia_residuo_gerado_pk PRIMARY KEY (ide_fce_linha_energia_residuo_gerado)
);



ALTER TABLE public.fce_linha_energia_localizacao_geografica ADD CONSTRAINT fce_linha_localizacao_geografica_localizacao_geografica_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_tipo_subestacao ADD CONSTRAINT subestacao_tipo_subestacao_fk
FOREIGN KEY (ide_tipo_subestacao)
REFERENCES public.tipo_subestacao (ide_tipo_subestacao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_destino_residuo ADD CONSTRAINT destino_residuo_fce_linha_destino_residuo_fk
FOREIGN KEY (ide_destino_residuo)
REFERENCES public.destino_residuo (ide_destino_residuo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_residuo_gerado ADD CONSTRAINT tipo_residuo_gerado_fce_linha_residuo_gerado_fk
FOREIGN KEY (ide_tipo_residuo_gerado)
REFERENCES public.tipo_residuo_gerado (ide_tipo_residuo_gerado)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_tipo_energia ADD CONSTRAINT tipo_origem_energia_fce_linha_origem_energia_fk
FOREIGN KEY (ide_tipo_energia)
REFERENCES public.tipo_energia (ide_tipo_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia ADD CONSTRAINT fce_fce_linha_transmissao_distribuicao_fk
FOREIGN KEY (ide_fce)
REFERENCES public.fce (ide_fce)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_residuo_gerado ADD CONSTRAINT fce_linha_transmissao_distribuicao_fce_linha_residuo_gerado_fk
FOREIGN KEY (ide_fce_linha_energia)
REFERENCES public.fce_linha_energia (ide_fce_linha_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_destino_residuo ADD CONSTRAINT fce_linha_transmissao_distribuicao_fce_linha_destino_residuo_fk
FOREIGN KEY (ide_fce_linha_energia)
REFERENCES public.fce_linha_energia (ide_fce_linha_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_tipo_energia ADD CONSTRAINT fce_linha_origem_energia_fce_linha_transmissao_distribuicao_fk
FOREIGN KEY (ide_fce_linha_energia)
REFERENCES public.fce_linha_energia (ide_fce_linha_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_tipo_subestacao ADD CONSTRAINT fce_linha_transmissao_distribuicao_subestacao_fk
FOREIGN KEY (ide_fce_linha_energia)
REFERENCES public.fce_linha_energia (ide_fce_linha_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_linha_energia_localizacao_geografica ADD CONSTRAINT fce_linha_transmissao_distribuicao_fce_linha_transmissao_dis879
FOREIGN KEY (ide_fce_linha_energia)
REFERENCES public.fce_linha_energia (ide_fce_linha_energia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

alter table tipo_area_concedida ALTER column des_tipo_area_concedida Type varchar(100);


CREATE OR REPLACE FUNCTION remover_fce_linha_energia_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
DECLARE ide_loc_geo_array  integer[] ;
BEGIN
ide_loc_geo_array := ARRAY( (SELECT ide_localizacao_geografica FROM fce_linha_energia_localizacao_geografica WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073))));

DELETE FROM fce_linha_energia_tipo_energia  where ide_fce_linha_energia_tipo_energia in 
	(SELECT ide_fce_linha_energia_tipo_energia FROM fce_linha_energia_tipo_energia WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_tipo_subestacao  where ide_fce_linha_energia_tipo_subestacao in 
	(SELECT ide_fce_linha_energia_tipo_subestacao FROM fce_linha_energia_tipo_subestacao WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_residuo_gerado  where ide_fce_linha_energia_residuo_gerado in 
	(SELECT ide_fce_linha_energia_residuo_gerado FROM fce_linha_energia_residuo_gerado WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_destino_residuo  where ide_fce_linha_energia_destino_residuo in 
	(SELECT ide_fce_linha_energia_destino_residuo FROM fce_linha_energia_destino_residuo WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia_localizacao_geografica  where ide_fce_linha_energia_localizacao_geografica in 
	(SELECT ide_fce_linha_energia_localizacao_geografica FROM fce_linha_energia_localizacao_geografica WHERE ide_fce_linha_energia  in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073)));

DELETE FROM fce_linha_energia  where ide_fce_linha_energia in 
		(SELECT ide_fce_linha_energia FROM fce_linha_energia WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073));

IF array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE FROM dado_geografico where ide_dado_geografico in (
	select ide_dado_geografico from dado_geografico  where ide_localizacao_geografica  in (ide_loc_geo_array[i]));
 END LOOP;
END IF;

IF  array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE from processo_ato_concedido where ide_localizacao_geografica  in  (ide_loc_geo_array[i]);
 END LOOP;
END IF;

IF array_length(ide_loc_geo_array, 1) > 0
THEN
 FOR i IN 1.. array_upper(ide_loc_geo_array,1)
 LOOP
       DELETE FROM localizacao_geografica where ide_localizacao_geografica in (ide_loc_geo_array[i]);
 END LOOP;
END IF;

DELETE FROM fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10073;
RETURN 'FCE - Linha de transmissão e distribuição de energia excluído!';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_linha_energia_by_requerimento(character varying)
  OWNER TO postgres;


COMMIT;
