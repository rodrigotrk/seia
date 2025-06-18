insert into funcionalidade (ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao)values (2, 'Identificar papel', false, now());
insert into funcionalidade_url values (95, '/paginas/identificar-papel/identificar-papel-solicitacao.xhtml' ,false);
insert into funcionalidade_acao (ide_funcionalidade, ide_acao) values (95, 1);
insert into rel_grupo_perfil_funcionalidade values (95, 1, 5);
