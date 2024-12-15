ALTER TABLE motivo_notificacao ADD COLUMN ind_envio_shape boolean default false;

CREATE SEQUENCE notificacao_motivo_notificacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

ALTER TABLE notificacao_motivo_notificacao ADD COLUMN ide_notificacao_motivo_notificacao INTEGER; 

CREATE OR REPLACE FUNCTION atualizar_ide_notificacao_motivo_notificacao()  RETURNS void AS
$BODY$
DECLARE
	item RECORD;
BEGIN
	FOR item IN select * from notificacao_motivo_notificacao
	LOOP 
		update notificacao_motivo_notificacao 
		set ide_notificacao_motivo_notificacao = (nextval(('notificacao_motivo_notificacao_seq'::text)::regclass))
		where ide_notificacao = item.ide_notificacao and ide_motivo_notificacao = item.ide_motivo_notificacao;
        END LOOP;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

SELECT atualizar_ide_notificacao_motivo_notificacao();

ALTER TABLE notificacao_motivo_notificacao DROP CONSTRAINT pk_notificacao_motivo_notificacao;
ALTER TABLE notificacao_motivo_notificacao ADD CONSTRAINT pk_notificacao_motivo_notificacao PRIMARY KEY (ide_notificacao_motivo_notificacao);

CREATE SEQUENCE motivo_notificacao_imovel_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE motivo_notificacao_imovel
(
  ide_motivo_notificacao_imovel integer NOT NULL DEFAULT nextval(('motivo_notificacao_imovel_seq'::text)::regclass),
  ide_notificacao_motivo_notificacao integer,
  ide_imovel integer,
  CONSTRAINT pk_motivo_notificacao_imovel PRIMARY KEY (ide_motivo_notificacao_imovel),
  CONSTRAINT motivo_notificacao_imovel_ide_notificacao_motivo_notificacao_fk FOREIGN KEY (ide_notificacao_motivo_notificacao) 
	REFERENCES notificacao_motivo_notificacao (ide_notificacao_motivo_notificacao) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT motivo_notificacao_imovel_ide_imovel_fk FOREIGN KEY (ide_imovel) 
	REFERENCES imovel (ide_imovel) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE arquivo_processo 
	ADD COLUMN ide_imovel INTEGER;
ALTER TABLE arquivo_processo 
	ADD CONSTRAINT arquivo_processo_ide_imovel_fk
	FOREIGN KEY (ide_imovel) 
	REFERENCES imovel (ide_imovel) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE origem_fce RENAME ide_origem_fce  TO ide_dado_origem;
ALTER TABLE origem_fce RENAME nom_origem_fce  TO nom_dado_origem;
ALTER TABLE origem_fce RENAME TO dado_origem;
ALTER SEQUENCE origem_fce_seq RENAME TO dado_origem_seq;

ALTER TABLE reserva_legal ALTER COLUMN ide_imovel_rural DROP NOT NULL;

ALTER TABLE reserva_legal ADD COLUMN ide_dado_origem INTEGER;
ALTER TABLE reserva_legal
	ADD CONSTRAINT reserva_legal_ide_dado_origem_fk
	FOREIGN KEY (ide_dado_origem) 
	REFERENCES dado_origem (ide_dado_origem) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE reserva_legal ADD COLUMN ide_notificacao INTEGER;
ALTER TABLE reserva_legal
	ADD CONSTRAINT reserva_legal_ide_notificacao_fk
	FOREIGN KEY (ide_notificacao) 
	REFERENCES notificacao (ide_notificacao) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE reserva_legal ADD COLUMN ide_processo_ato INTEGER;
ALTER TABLE reserva_legal
	ADD CONSTRAINT reserva_legal_ide_processo_ato_fk
	FOREIGN KEY (ide_processo_ato) 
	REFERENCES processo_ato (ide_processo_ato) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE reserva_legal ADD COLUMN ide_reserva_legal_pai INTEGER;
ALTER TABLE reserva_legal
	ADD CONSTRAINT reserva_legal_ide_reserva_legal_pai_fk
	FOREIGN KEY (ide_reserva_legal_pai) 
	REFERENCES reserva_legal (ide_reserva_legal) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE reserva_legal
	DROP CONSTRAINT unq_reserva_legal_ide_imovel_rural;
ALTER TABLE reserva_legal
	ADD CONSTRAINT unq_reserva_legal_ide_imovel_rural UNIQUE (ide_imovel_rural,ide_reserva_legal_pai);