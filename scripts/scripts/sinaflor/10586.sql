ALTER TABLE especie_supressao_autorizacao
ADD COLUMN ide_processo_ato integer;



ALTER TABLE especie_supressao_autorizacao ADD CONSTRAINT fk_esp_sup_ide_processo_ato_especie FOREIGN KEY (ide_processo_ato)
      REFERENCES processo_ato (ide_processo_ato) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
