-- Drop table

-- DROP TABLE public.reenquadramento_tipologia_empreendimento;

CREATE TABLE public.reenquadramento_tipologia_empreendimento (
	ide_reenquadramento_processo_ato INTEGER NOT NULL,
	ide_tipologia INTEGER NOT NULL,
	CONSTRAINT reenquadramento_processo_ato_fk FOREIGN KEY (ide_reenquadramento_processo_ato) REFERENCES reenquadramento_processo_ato(ide_reenquadramento_processo_ato) ,
	CONSTRAINT reenquadramento_tipologia_empreendimento_fk FOREIGN KEY (ide_tipologia) REFERENCES tipologia(ide_tipologia)
);
CREATE INDEX reenquadramento_empreedimento_tipologia_ide_reenquadramento_pro ON reenquadramento_tipologia_empreendimento USING btree (ide_reenquadramento_processo_ato);
