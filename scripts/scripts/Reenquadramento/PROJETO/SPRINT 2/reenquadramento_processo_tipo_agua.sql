CREATE TABLE public.reenquadramento_tipo_finalidade_uso_agua (
	ide_reenquadramento_processo_ato integer NOT NULL,
	ide_tipo_finalidade_uso_agua integer NOT NULL,
	CONSTRAINT reenquadramento_tipo_finalidade_uso_agua_fk FOREIGN KEY (ide_tipo_finalidade_uso_agua) REFERENCES public.tipo_finalidade_uso_agua(ide_tipo_finalidade_uso_agua),
	CONSTRAINT reenquadramento_processo_ato_fk FOREIGN KEY (ide_reenquadramento_processo_ato) REFERENCES public.reenquadramento_processo_ato(ide_reenquadramento_processo_ato) 
);

