alter SEQUENCE residuo_ide_residuo_seq RESTART WITH 240;

alter table residuo ALTER column dsc_periculosidade type varchar(200);

ALTER table residuo add column dtc_exclusao date;

INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (1, 'Resíduos Perigosos', false, now(), null);

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1);

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2);

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3);

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4);    

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6);       

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-residuos-perigosos/residuosList.xhtml', false);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 1);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 1);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 1);  
  
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 1);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6, 1);    
