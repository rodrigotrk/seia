CREATE SEQUENCE public.detalhamento_boleto_finalidade_seq 
INCREMENT 1 
MINVALUE 1 
MAXVALUE 9223372036854775807 
START 1 
CACHE 1;

CREATE TABLE public.detalhamento_boleto_finalidade
(
	ide_detalhamento_boleto_finalidade INTEGER DEFAULT nextval(('detalhamento_boleto_finalidade_seq'::text)::regclass) NOT NULL,
	ide_detalhamento_boleto integer NOT NULL,
	ide_tipo_finalidade_uso_agua integer NOT NULL,
	num_area_dessedentacao_animal numeric(10,2),
	num_area_empreendimento numeric(10,2),
	ind_abastecimento_em_distrito_industrial boolean,

	CONSTRAINT ide_detalhamento_boleto_finalidade_pk PRIMARY KEY (ide_detalhamento_boleto_finalidade), 

	CONSTRAINT detalhamento_boleto_finalidade_ide_detalhamento_boleto 
	FOREIGN KEY (ide_detalhamento_boleto) REFERENCES detalhamento_boleto (ide_detalhamento_boleto) ON UPDATE NO ACTION ON DELETE NO ACTION, 

	CONSTRAINT detalhamento_boleto_finalidade_ide_tipo_finalidade_uso_agua 
	FOREIGN KEY (ide_tipo_finalidade_uso_agua) REFERENCES tipo_finalidade_uso_agua (ide_tipo_finalidade_uso_agua) ON UPDATE NO ACTION ON DELETE NO ACTION
);
