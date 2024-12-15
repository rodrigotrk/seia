CREATE TABLE forma_realocacao_rl
(
   ide_forma_realocacao_rl integer NOT NULL, 
   dsc_tipo_realocacao_rl character varying(60) NOT NULL, 
   ind_excluido boolean NOT NULL DEFAULT false, 
   dtc_criacao timestamp without time zone NOT NULL, 
   dtc_exclusao timestamp without time zone, 
   CONSTRAINT pk_ide_forma_realocacao_rl PRIMARY KEY (ide_forma_realocacao_rl)
) 
WITH (
  OIDS = FALSE
)
;

ALTER TABLE reserva_legal ADD COLUMN ide_forma_realocacao_rl integer;

ALTER TABLE reserva_legal
  ADD CONSTRAINT fk_ide_forma_realocacao_rl FOREIGN KEY (ide_forma_realocacao_rl) REFERENCES forma_realocacao_rl (ide_forma_realocacao_rl)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ide_forma_realocacao_rl
  ON reserva_legal(ide_forma_realocacao_rl);
