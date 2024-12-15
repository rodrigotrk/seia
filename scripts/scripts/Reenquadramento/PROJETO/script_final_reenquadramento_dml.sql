----------------------------------
-- SRPINT 2
----------------------------------

INSERT INTO public.status_reenquadramento
(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
VALUES(14, 'Aguardando edição do reenquadramento', '2019-07-24 09:43:00.000', true, NULL);

INSERT INTO public.status_reenquadramento
(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
VALUES(15, 'Pendência de reenquadramento', '2019-07-24 09:43:00.000', true, NULL);

----------------------------------
-- SRPINT 3
----------------------------------

INSERT INTO categoria_documento (nom_categoria) VALUES('JUSTIFICATIVA PARA NÃO REENQUADRAR');

INSERT INTO public.status_reenquadramento
(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
VALUES(16, 'Não Reenquadrado', '2019-08-09 09:44:00.000', true, NULL);

INSERT INTO status_fluxo (ide_status_fluxo,dsc_status_fluxo,ind_excluido,dtc_criacao,ind_visivel_historico)
	VALUES (26,'Editado aguardando reenquadramento',false,'2019-08-14 13:52:00.000',true);
	
INSERT INTO status_fluxo (ide_status_fluxo,dsc_status_fluxo,ind_excluido,dtc_criacao,ind_visivel_historico)
	VALUES (27,'Reenquadramento em trâmite',false,'2019-08-14 13:52:00.000',true);
	
INSERT INTO status_fluxo (ide_status_fluxo,dsc_status_fluxo,ind_excluido,dtc_criacao,ind_visivel_historico)
	VALUES (28,'Reenquadrado Aguardando Distribuição',false,'2019-08-14 13:52:00.000',true);

----------------------------------
-- SRPINT 4
----------------------------------

INSERT INTO status_reenquadramento
(ide_status_reenquadramento ,nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
VALUES(17,'Retorno de Status', '2019-08-29 11:00:00.000', false, NULL);

ALTER TABLE processo_reenquadramento ADD COLUMN ind_aceite_requerente BOOLEAN;
ALTER TABLE processo_reenquadramento ADD COLUMN dtc_aceite_requerente TIMESTAMP;

INSERT INTO status_fluxo (ide_status_fluxo,dsc_status_fluxo,ind_excluido,dtc_criacao,ind_visivel_historico)
	VALUES (23,'Não Reenquadrado',false,'2019-08-14 13:52:00.000',true);
	
----------------------------------
-- SRPINT 5
----------------------------------

INSERT INTO categoria_documento (ide_categoria_documento, nom_categoria) VALUES (11, 'RESUMO DE REQUERIMENTO DE REENQUADRAMENTO DE PROCESSO');

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
VALUES(nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 96, 1);
INSERT INTO public.funcionalidade_acao
(ide_funcionalidade_acao, ide_funcionalidade, ide_acao)
VALUES(nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 97, 1);

/*INSERT INTO public.rel_grupo_perfil_funcionalidade
(ide_funcionalidade, ide_acao, ide_perfil)
VALUES(97, 1,1);*/

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

INSERT INTO parametro (ide_parametro, nom_parametro, dsc_valor) VALUES(44, 'Horário de execução da rotina de comunicação com os usuários', '0 0 3 * * ?');

----------------------------------
-- SRPINT 6
----------------------------------

UPDATE documento_obrigatorio SET ind_formulario_digital=TRUE WHERE ind_formulario AND dsc_caminho_arquivo IS NULL;