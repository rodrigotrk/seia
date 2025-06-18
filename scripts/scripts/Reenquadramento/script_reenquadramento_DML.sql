
--INSERT INTO motivo_notificacao (ide_motivo_notificacao, nom_motivo_notificacao, ind_notificacao_prazo, ind_notificacao_comunicacao, ind_notificacao_homologacao, ind_envio_shape) 
--    VALUES (21, 'Reenquadramento de processo', false, true, false, false);

INSERT INTO status_fluxo(ide_status_fluxo, dsc_status_fluxo, ind_excluido, dtc_criacao, dtc_exclusao, dsc_status_fluxo_externo, ind_visivel_historico)
    VALUES (21, 'Aguardando reenquadramento do processo', false, now(), null, '', true);
INSERT INTO status_fluxo(ide_status_fluxo, dsc_status_fluxo, ind_excluido, dtc_criacao, dtc_exclusao, dsc_status_fluxo_externo, ind_visivel_historico)
    VALUES (22, 'Processo Reenquadrado', false, now(), null, '', true);
    
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (1, 'ALTERACAO_ATOS_AUTORIZATIVOS', true, now(), null);
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (2, 'INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS', true, now(), null);
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (3, 'ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE', true, now(), null);
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (4, 'ALTERACAO_CLASSE_EMPREENDIMENTO', true, now(), null);
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (5, 'ALTERACAO_TIPOLOGIA', true, now(), null);
INSERT INTO finalidade_reenquadramento(ide_finalidade_reenquadramento, nom_motivo_reenquadramento, ind_ativo, dtc_criacao, dtc_exclusao)
    VALUES (6, 'CORRECAO_PORTE_EMPREENDIMENTO', true, now(), null);


INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (1, 'Aguardando o reenquadramento do processo', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (2, 'Aguardando o envio da documentação', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (3, 'Em validação prévia', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (4, 'Pendência de Envio de Documentação', now(), true, null);     
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (5, 'Pendência de validação da documentação', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (6, 'Validado', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (7, 'Boleto de pagamento liberado', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (8, 'Comprovante enviado', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (9, 'Pendência de validação do comprovate de pagamento', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (10, 'Reenquadrado', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (11, 'Encaminhado para o gestor', now(), true, null);
INSERT INTO status_reenquadramento(ide_status_reenquadramento, nom_status_reenquadramento, dtc_criacao, ind_ativo, dtc_exclusao)
    VALUES (12, 'Boleto Validado', now(), true, null);
    
    
INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (5, 'Reenquadramento de processo', false, now(), null);

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml', true);
    
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6);
    
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 29);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6, 5);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6, 4);
 
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 29, 4);
    

INSERT INTO tipo_boleto_pagamento(ide_tipo_boleto_pagamento, nom_tipo_boleto_pagamento, ind_requerimento, ind_processo, ind_ativo)
    VALUES (7, 'Reenquadramento de processo', false, true, true);
    
 insert into funcionalidade_acao (ide_funcionalidade, ide_acao)
	values (82, 29);

	insert into tipo_status_processo_ato (ide_tipo_status_processo_ato, nom_tipo_status_processo_ato) VALUES
	(9, 'Alterado');

	
	insert into dado_origem (ide_dado_origem, nom_dado_origem) values (3, 'Reenquadramento');
	
	
	/*ES21 Comunicação*/
	
	

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
