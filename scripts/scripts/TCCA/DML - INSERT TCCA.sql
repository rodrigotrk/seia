----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------- INSERT FUNCIONALIDADES ---------------------------------------------------------------------------

-- ALTER SEQUENCE funcionalidade_ide_funcionalidade_seq RESTART WITH 67;
-- INSERT INTO funcionalidade VALUES (nextval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 'Termo de Compromisso Ambiental - TCCA', false, now(), null); --67

-- ALTER SEQUENCE Acao_IDE_ACAO_seq RESTART WITH 64;
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Listar Projetos do TCCA', false, now(), null); --64
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Cadastrar Projeto do TCCA', false, now(), null); --65
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Visualizar Projeto do TCCA', false, now(), null); --66
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Editar Projeto do TCCA', false, now(), null); --67
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Excluir Projeto do TCCA', false, now(), null); --68
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Cancelar Projeto do TCCA', false, now(), null); --69
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Executar Projeto do TCCA', false, now(), null); --70
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Movimentação Financeira no Projeto', false, now(), null); --71
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Movimentação Financeira no Tcca', false, now(), null); --72
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Duplicar TCCA', false, now(), null); --73
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Reajustar Valor do TCCA', false, now(), null); --74
-- INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Renovar Prazo de Validade do TCCA', false, now(), null); --75
-- 
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 1);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 2);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 3);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 4);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 6);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 51);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 64);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 65);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 66);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 67);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 68);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 69);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 70);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 71);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 72);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 73);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 74);
-- INSERT INTO funcionalidade_acao VALUES (nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 67, 75);

INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/consultaTcca.xhtml', false);
INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/cadastroTcca.xhtml', false);
INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/cadastroMovimentacaoFinanceiraTcca.xhtml', false);

INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/consultaProjeto.xhtml', false);
INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/cadastroProjeto.xhtml', false);
INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/execucaoProjeto.xhtml', false);
INSERT INTO funcionalidade_url VALUES (67, '/paginas/manter-tcca/cadastroMovimentacaoFinanceiraProjeto.xhtml', false);

INSERT INTO tcca_projeto_tipo_status VALUES (1, 'Cadastro Incompleto');
INSERT INTO tcca_projeto_tipo_status VALUES (2, 'Vigente');
INSERT INTO tcca_projeto_tipo_status VALUES (3, 'Previsto');
INSERT INTO tcca_projeto_tipo_status VALUES (4, 'Em Execução');
INSERT INTO tcca_projeto_tipo_status VALUES (5, 'Expirado');
INSERT INTO tcca_projeto_tipo_status VALUES (6, 'Cancelado');
INSERT INTO tcca_projeto_tipo_status VALUES (7, 'Remanejado');
INSERT INTO tcca_projeto_tipo_status VALUES (8, 'Concluído');

INSERT INTO tipo_origem_destino VALUES (1, 'Saldo disponível do TCCA');
INSERT INTO tipo_origem_destino VALUES (2, 'Saldo de outro projeto');
INSERT INTO tipo_origem_destino VALUES (3, 'Saldo de outro TCCA');
INSERT INTO tipo_origem_destino VALUES (4, 'Saldo do produto');
INSERT INTO tipo_origem_destino VALUES (5, 'Saldo suplementado');

INSERT INTO tcca_projeto_operacao VALUES (1, 'Reajuste', FALSE);
INSERT INTO tcca_projeto_operacao VALUES (2, 'Suplementação', FALSE);
INSERT INTO tcca_projeto_operacao VALUES (3, 'Remanejamento', FALSE);
INSERT INTO tcca_projeto_operacao VALUES (4, 'Destinação Incial', TRUE);
INSERT INTO tcca_projeto_operacao VALUES (5, 'Destinação', FALSE);
INSERT INTO tcca_projeto_operacao VALUES (6, 'Execução', FALSE);
INSERT INTO tcca_projeto_operacao VALUES (7, 'Devolução', FALSE);

-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,1,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,2,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,3,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,4,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,6,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,51,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,64,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,65,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,66,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,67,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,68,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,69,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,70,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,71,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,72,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,73,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,74,5);
-- INSERT INTO rel_grupo_perfil_funcionalidade VALUES (67,75,5);