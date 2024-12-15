CREATE SEQUENCE ide_documento_requerimento_empreendimento_seq;

CREATE TABLE documento_requerimento_empreendimento
(
  ide_documento_requerimento_empreendimento integer NOT NULL DEFAULT nextval(('ide_documento_requerimento_empreendimento_seq'::text)::regclass),
  ide_empreendimento_requerimento integer NOT NULL,
  nom_documento character varying(500) NOT NULL,
  ide_pessoa_fisica integer NOT NULL,
  ind_documento_validado boolean NOT NULL DEFAULT false,
  dtc_validacao timestamp(6) without time zone,
  dsc_caminho_arquivo character varying(500),
  ide_pessoa_validacao integer,
  CONSTRAINT pk_documento_requerimento_empreendimento PRIMARY KEY (ide_documento_requerimento_empreendimento),
  CONSTRAINT documento_requerimento_empreendimento_ide_pessoa_validacao_fkey FOREIGN KEY (ide_pessoa_validacao)
      REFERENCES pessoa (ide_pessoa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT documento_requerimento_empreendimento_ide_requerimento_fkey FOREIGN KEY (ide_empreendimento_requerimento)
      REFERENCES empreendimento_requerimento (ide_empreendimento_requerimento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


