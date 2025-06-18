CREATE SEQUENCE auditoria_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE TABLE auditoria
(
  ide_auditoria integer NOT NULL,
  dtc_alteracao timestamp without time zone,
  ide_pessoa_fisica integer,
  num_ip character varying,
  caminho_requisicao character varying,
  CONSTRAINT auditoria_pkey PRIMARY KEY (ide_auditoria)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE pessoa_aud
(
  ide_pessoa integer NOT NULL,
  dtc_criacao timestamp(6) without time zone NOT NULL,
  ind_excluido boolean NOT NULL,
  des_email character varying(70),
  dtc_exclusao timestamp(6) without time zone,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE pessoa_fisica_aud
(
  ide_pessoa_fisica integer NOT NULL,
  ide_pais integer,
  nom_pessoa character varying(200) NOT NULL,
  dtc_nascimento date,
  des_naturalidade character varying(70),
  nom_mae character varying(200),
  num_cpf character varying(11) NOT NULL,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE pessoa_juridica_aud
(
  ide_pessoa_juridica integer NOT NULL,
  ide_natureza_juridica integer,
  nom_razao_social character varying(200) NOT NULL,
  nom_nome_fantasia character varying(200),
  dtc_abertura timestamp(6) without time zone,
  num_cnpj character varying(14) NOT NULL,
  num_inscricao_municipal character varying(30),
  num_inscricao_estadual character varying(30),
  dsc_caminho_arquivo character varying(1000),
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE procurador_pessoa_fisica_aud
(
  ide_procurador_pessoa_fisica integer NOT NULL,
  ide_procurador integer NOT NULL,
  ide_pessoa_fisica integer NOT NULL,
  dtc_inicio timestamp(6) without time zone NOT NULL,
  ind_excluido boolean NOT NULL DEFAULT false,
  dtc_exclusao timestamp(6) without time zone,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE procurador_representante_aud
(
  ide_procurador_representante integer NOT NULL,
  ide_procurador integer NOT NULL,
  dtc_inicio timestamp(6) without time zone NOT NULL,
  ind_excluido boolean NOT NULL DEFAULT false,
  dtc_exclusao timestamp(6) without time zone,
  ide_pessoa_juridica integer NOT NULL,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE usuario_aud
(
  ide_pessoa_fisica integer NOT NULL,
  ide_perfil integer NOT NULL,
  dsc_login character varying(25) NOT NULL,
  dsc_senha character varying(32) NOT NULL,
  ind_excluido boolean NOT NULL,
  dtc_criacao timestamp(6) without time zone,
  dtc_exclusao timestamp(6) without time zone,
  ind_tipo_usuario boolean NOT NULL,
  ind_validacao boolean NOT NULL,
  dtc_ultimo_login timestamp(6) without time zone,
  dtc_ultima_senha timestamp(6) without time zone,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);

CREATE TABLE representante_legal_aud
(
  ide_representante_legal integer NOT NULL,
  ide_pessoa_fisica integer NOT NULL,
  ide_pessoa_juridica integer NOT NULL,
  dtc_inicio timestamp(6) without time zone NOT NULL,
  ind_excluido boolean NOT NULL,
  dsc_caminho_representacao character varying(1000) NOT NULL,
  dtc_inicio_representacao timestamp(6) without time zone,
  dtc_fim_representacao timestamp(6) without time zone,
  dtc_exclusao timestamp(6) without time zone,
  ind_assinatura_obrigatoria boolean NOT NULL,
  rev integer NOT NULL,
  revtype integer
)
WITH (
  OIDS=FALSE
);
