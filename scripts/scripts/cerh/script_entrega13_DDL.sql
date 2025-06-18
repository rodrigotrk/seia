ALTER TABLE cerh ADD COLUMN ide_cerh_pai INTEGER;
ALTER TABLE cerh ADD COLUMN ind_historico BOOLEAN;
ALTER TABLE cerh ADD COLUMN ide_cerh_status INTEGER;

ALTER TABLE cerh ADD FOREIGN KEY (ide_cerh_pai) REFERENCES public.cerh (ide_cerh) 
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE cerh ADD CONSTRAINT fk_cerh_cerh_status FOREIGN KEY (ide_cerh_status) REFERENCES cerh_status (ide_cerh_status)
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE cerh_status RENAME TO cerh_status_historico;
ALTER SEQUENCE cerh_status_seq RENAME TO cerh_status_historico_seq;

ALTER TABLE cerh_tipo_status RENAME ide_cerh_tipo_status  TO ide_cerh_status;
ALTER TABLE cerh_tipo_status RENAME dsc_tipo_status TO dsc_status;
ALTER TABLE cerh_tipo_status RENAME TO cerh_status;
ALTER SEQUENCE cerh_tipo_status_seq RENAME TO cerh_status_seq;
