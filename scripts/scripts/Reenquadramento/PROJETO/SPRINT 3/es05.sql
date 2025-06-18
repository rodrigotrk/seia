CREATE TABLE reenquadramento_processo_ato_tipo_atividade_fauna
(
  ide_reenquadramento_processo_ato integer NOT NULL,
  ide_tipo_atividade_fauna integer NOT NULL,
 
  CONSTRAINT reenquadramento_processo_ato_tipo_atividade_fauna_pk PRIMARY KEY (ide_reenquadramento_processo_ato, ide_tipo_atividade_fauna),
  CONSTRAINT reenquadramento_processo_ato_tipo_atividade_fauna_fk1 FOREIGN KEY (ide_reenquadramento_processo_ato)
      REFERENCES reenquadramento_processo_ato (ide_reenquadramento_processo_ato) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT reenquadramento_processo_ato_tipo_atividade_fauna_fk2 FOREIGN KEY (ide_tipo_atividade_fauna)
      REFERENCES tipo_atividade_fauna (ide_tipo_atividade_fauna) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE reenquadramento_processo_ato_objetivo_atividade_manejo
(
  ide_reenquadramento_processo_ato integer NOT NULL,
  ide_objetivo_atividade_manejo integer NOT NULL,
 
  CONSTRAINT reenquadramento_processo_ato_objetivo_atividade_manejo_pk PRIMARY KEY (ide_reenquadramento_processo_ato, ide_objetivo_atividade_manejo),
  CONSTRAINT reenquadramento_processo_ato_objetivo_atividade_manejo_fk1 FOREIGN KEY (ide_reenquadramento_processo_ato)
      REFERENCES reenquadramento_processo_ato (ide_reenquadramento_processo_ato) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT reenquadramento_processo_ato_objetivo_atividade_manejo_fk2 FOREIGN KEY (ide_objetivo_atividade_manejo)
      REFERENCES objetivo_atividade_manejo (ide_objetivo_atividade_manejo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE comunicacao_reenquadramento_processo ALTER COLUMN des_mensagem TYPE text;
ALTER TABLE comunicacao_processo ALTER COLUMN des_mensagem TYPE text;