insert into funcionalidade (ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao) values(nextval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass),1,'Versão', false, now(), null);
insert into funcionalidade_url values(currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/versao/versao.xhtml', false);
insert into rel_grupo_perfil_funcionalidade values(currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 6,1);
