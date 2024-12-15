--update no numero do convenio e numero de carteira;
UPDATE dado_bancario SET convenio ='2851908', num_carteira ='17/019' WHERE ide_dado_bancario =1;

--inserts relativos as permissoes de acesso a tela e aos botoes;
INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao) VALUES (3, 'Consultar Lotes de Boleto', false, now(), null);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4);
INSERT INTO funcionalidade_url (ide_funcionalidade, dsc_url) VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-boleto/consultaLoteBoleto.xhtml');
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)  VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 6);

INSERT INTO acao(dsc_acao, ind_excluido, dtc_criacao, dtc_exclusao) VALUES ('Confirmar Lotes', false, now(), null);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass));
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)  VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass), 6);

--create table tipo_lote_boleto
CREATE TABLE tipo_lote_boleto
(
	ide_tipo_lote_boleto INTEGER NOT NULL,
	dsc_tipo_lote_boleto VARCHAR(20) NOT NULL
);
ALTER TABLE tipo_lote_boleto
ADD PRIMARY KEY (ide_tipo_lote_boleto);
ALTER TABLE tipo_lote_boleto OWNER TO seia_sema;

--insercao dos dois tipos de lote: "remessa" e "retorno"
INSERT INTO tipo_lote_boleto(ide_tipo_lote_boleto, dsc_tipo_lote_boleto) VALUES (1, 'Remessa');
INSERT INTO tipo_lote_boleto(ide_tipo_lote_boleto, dsc_tipo_lote_boleto) VALUES (2, 'Retorno');

--create table lote_boleto;
CREATE TABLE lote_boleto
(
	ide_lote_boleto INTEGER NOT NULL DEFAULT nextval(('lote_boleto_ide_lote_boleto_seq'::text)::regclass),
	ide_tipo_lote_boleto INTEGER NOT NULL,
	dtc_criacao          timestamp(6) without time zone NOT NULL,
	num_lote_boleto      VARCHAR(20) NOT NULL,
	CONSTRAINT fk_lote_boleto_ide_tipo_lote_boleto FOREIGN KEY (ide_tipo_lote_boleto) REFERENCES tipo_lote_boleto (ide_tipo_lote_boleto)
);
ALTER TABLE lote_boleto
ADD PRIMARY KEY (ide_lote_boleto);
ALTER TABLE lote_boleto OWNER TO seia_sema;

--sequence para ide_lote_boleto
CREATE SEQUENCE lote_boleto_ide_lote_boleto_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE lote_boleto_ide_lote_boleto_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE lote_boleto_ide_lote_boleto_seq TO seia_sema;

--create table lote_remessa_boleto;
CREATE TABLE lote_remessa_boleto
(
	ide_lote_remessa_boleto INTEGER NOT NULL DEFAULT nextval(('lote_remessa_boleto_ide_lote_remessa_boleto_seq'::text)::regclass),
	ide_lote_boleto      INTEGER NULL,
	dtc_envio_remessa    timestamp(6) without time zone, --data de confirmacao de integracao com o bb cobranca
	ide_pessoa_geracao INTEGER NOT NULL, --pessoa que exporta o arquivo de remessa do seia.
	ide_pessoa_confirmacao INTEGER NULL, --pessoa que confirma a integracao da remessa com o bb cobranca.
	CONSTRAINT fk_lote_remessa_boleto_ide_lote_boleto FOREIGN KEY (ide_lote_boleto) REFERENCES lote_boleto (ide_lote_boleto),
	CONSTRAINT fk_lote_remessa_boleto_ide_pessoa_geracao FOREIGN KEY (ide_pessoa_geracao) REFERENCES pessoa (ide_pessoa),
        CONSTRAINT fk_lote_remessa_boleto_ide_pessoa_confirmacao FOREIGN KEY (ide_pessoa_confirmacao) REFERENCES pessoa (ide_pessoa)
);
ALTER TABLE lote_remessa_boleto
ADD PRIMARY KEY (ide_lote_remessa_boleto);
ALTER TABLE lote_remessa_boleto OWNER TO seia_sema;
--sequence para lote_remessa_boleto
CREATE SEQUENCE lote_remessa_boleto_ide_lote_remessa_boleto_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE lote_remessa_boleto_ide_lote_remessa_boleto_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE lote_remessa_boleto_ide_lote_remessa_boleto_seq TO seia_sema;

--create table lote_retorno_boleto;
CREATE TABLE lote_retorno_boleto
(
	ide_lote_retorno_boleto INTEGER NOT NULL DEFAULT nextval(('lote_retorno_boleto_ide_lote_retorno_boleto_seq'::text)::regclass),
	ide_lote_boleto      INTEGER NOT NULL,
	des_caminho_arquivo  VARCHAR(100) NOT NULL,
	dtc_importacao_retorno  timestamp(6) without time zone NOT NULL, --data de importacao do arquivo de retorno
	ide_pessoa 	     INTEGER NOT NULL, --pessoa que importa o arquivo de retorno no sistema
	CONSTRAINT fk_lote_retorno_boleto_ide_lote_boleto FOREIGN KEY (ide_lote_boleto) REFERENCES lote_boleto (ide_lote_boleto),
	CONSTRAINT fk_lote_retorno_boleto_ide_pessoa FOREIGN KEY (ide_pessoa) REFERENCES pessoa (ide_pessoa)
);
ALTER TABLE lote_retorno_boleto
ADD PRIMARY KEY (ide_lote_retorno_boleto);
ALTER TABLE lote_retorno_boleto OWNER TO seia_sema;

--sequence para lote_retorno_boleto
CREATE SEQUENCE lote_retorno_boleto_ide_lote_retorno_boleto_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE lote_retorno_boleto_ide_lote_retorno_boleto_seq
  OWNER TO seia_sema;
GRANT ALL ON SEQUENCE lote_retorno_boleto_ide_lote_retorno_boleto_seq TO seia_sema;

--alter table boleto;
ALTER TABLE boleto_pagamento_requerimento ADD COLUMN ide_lote_remessa_boleto integer null;
ALTER TABLE boleto_pagamento_requerimento ADD COLUMN ide_lote_retorno_boleto integer null;
ALTER TABLE boleto_pagamento_requerimento ADD CONSTRAINT fk_lote_remessa_boleto_ide_lote_remessa_boleto FOREIGN KEY (ide_lote_remessa_boleto) REFERENCES lote_remessa_boleto (ide_lote_remessa_boleto);
ALTER TABLE boleto_pagamento_requerimento ADD CONSTRAINT fk_lote_retorno_boleto_ide_lote_retorno_boleto FOREIGN KEY (ide_lote_retorno_boleto) REFERENCES lote_retorno_boleto (ide_lote_retorno_boleto);
ALTER TABLE boleto_pagamento_requerimento ADD COLUMN ind_boleto_registrado boolean null;
UPDATE boleto_pagamento_requerimento SET ind_boleto_registrado = false;

--inclusão do status "boleto em processamento"
INSERT into status_requerimento(ide_status_requerimento, nom_status_requerimento) VALUES (18, 'Boleto em Processamento');
