
-- Drop table

-- DROP TABLE public.comunicacao_status;

CREATE TABLE public.comunicacao_status (
	ide_comunicacao_status int4 NOT NULL,
	dsc_status varchar(100) NULL,
	CONSTRAINT comunicacao_status_pk PRIMARY KEY (ide_comunicacao_status)
);

-- Drop table

-- DROP TABLE public.comunicacao;
CREATE SEQUENCE comunicacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.comunicacao (
	ide_comunicacao int4 NOT NULL,
	dsc_titulo varchar(100) NOT NULL,
	ide_comunicacao_status int4 NOT NULL,
	ind_ativa bool NOT NULL,
	dtc_periodo_inicio date NULL,
	dtc_periodo_fim date NULL,
	ide_pessoa_fisica int4 NULL,
	txt_conteudo text(1200) NULL,
	tp_comunicacao varchar(1) NULL,
	dtc_criacao date NULL,
	CONSTRAINT comunicacao_pk PRIMARY KEY (ide_comunicacao)
);

ALTER TABLE public.comunicacao ADD CONSTRAINT comunicacao_fk_1 FOREIGN KEY (ide_comunicacao_status) REFERENCES comunicacao_status(ide_comunicacao_status);


-- Drop table

-- DROP TABLE public.comunicacao_perfil;

CREATE TABLE public.comunicacao_perfil (
	ide_comunicacao int4 NULL,
	ide_perfil int4 NULL
);

ALTER TABLE public.comunicacao_perfil ADD CONSTRAINT comunicacao_perfil_fk FOREIGN KEY (ide_comunicacao) REFERENCES comunicacao(ide_comunicacao);
ALTER TABLE public.comunicacao_perfil ADD CONSTRAINT perfil_fk FOREIGN KEY (ide_perfil) REFERENCES perfil(ide_perfil);


INSERT INTO public.funcionalidade
(ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
VALUES(96, 1, 'Comunicação', false, '2019-09-04 14:46:57.818', NULL);

INSERT INTO public.funcionalidade
(ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
VALUES(97, 3, 'Visualizar Comunicações', false, '2019-09-05 00:00:00.000', NULL);

INSERT INTO public.funcionalidade_url
(ide_funcionalidade, dsc_url, ind_principal)
VALUES(96, '/paginas/comunicacao/manter-comunicacao.xhtml', false);
INSERT INTO public.funcionalidade_url
(ide_funcionalidade, dsc_url, ind_principal)
VALUES(96, '/paginas/comunicacao/consulta.xhtml', false);

INSERT INTO public.funcionalidade_url
(ide_funcionalidade, dsc_url, ind_principal)
VALUES(97, '/paginas/comunicacao/consulta-usuario.xhtml', false);
INSERT INTO public.funcionalidade_url
(ide_funcionalidade, dsc_url, ind_principal)
VALUES(97, '/paginas/comunicacao/consulta-usuario.xhtml?faces-redirect=true', false);


INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(96, 1, 1);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1, 1);


INSERT INTO public.comunicacao_status
(ide_comunicacao_status, dsc_status)
VALUES(1, 'Novo');
INSERT INTO public.comunicacao_status
(ide_comunicacao_status, dsc_status)
VALUES(2, 'Enviado');
INSERT INTO public.comunicacao_status
(ide_comunicacao_status, dsc_status)
VALUES(3, 'Arquivado');
INSERT INTO public.comunicacao_status
(ide_comunicacao_status, dsc_status)
VALUES(4, 'Cancelado');

INSERT INTO public.funcionalidade_acao
(ide_funcionalidade_acao, ide_funcionalidade, ide_acao)
VALUES(275, 96, 1);
INSERT INTO public.funcionalidade_acao
(ide_funcionalidade_acao, ide_funcionalidade, ide_acao)
VALUES(276, 97, 1);

INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,1);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,2);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,4);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,5);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,6);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,7);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,8);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,9);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,10);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,3);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,11);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,12);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,13);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,14);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,15);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,16);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,19);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,20);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,21);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,24);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,23);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,26);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,28);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,29);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,30);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,31);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,33);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,32);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,34);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,35);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,36);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,37);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,38);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,39);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,41);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,44);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,45);
INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,54);

-------------JOB--------------------------

INSERT INTO parametro (ide_parametro, nom_parametro, dsc_valor) VALUES(44, 'Horário de execução da rotina de comunicação com os usuários', '0 0 3 * * ?')
