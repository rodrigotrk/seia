-- Table: usuario_bloqueio

-- DROP TABLE usuario_bloqueio;
CREATE SEQUENCE usuario_bloqueio_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 11
  CACHE 1;
ALTER TABLE usuario_bloqueio_id_seq
  OWNER TO postgres;
  
CREATE TABLE usuario_bloqueio
(
  id integer NOT NULL,
  dsc_login character varying,
  dsc_endereco_ip character varying,
  dt_acesso timestamp without time zone,
  CONSTRAINT primary_key PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuario_bloqueio
  OWNER TO postgres;
GRANT ALL ON TABLE usuario_bloqueio TO postgres;
GRANT ALL ON TABLE usuario_bloqueio TO public;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON usuario_bloqueio TO public;

