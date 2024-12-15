
insert into funcionalidade_url values(69, '/paginas/manter-atividade-nao-sujeita-licenciamento/torres-anenometricas/cadastro.xhtml',false);
--update  funcionalidade set ide_secao = 2 where ide_funcionalidade = 69;
update tipo_atividade_nao_sujeita_licenciamento set ind_ativo = true where nom_atividade = 'Instalação de torres anemométricas';

/*Inserindo valores iniciais na tipo_natureza_torre pois torres_anemometrias faz uma referência não nula para a tipo_natureza_torre.
Obs.: valores observados no protótipo*/
insert into public.tipo_natureza_torre values(nextval('tipo_natureza_torre_ide_tipo_natureza_torre_seq'), 'Provisória');
insert into public.tipo_natureza_torre values(nextval('tipo_natureza_torre_ide_tipo_natureza_torre_seq'), 'Permanente');

insert into localizacao_atividade_torre values(nextval('localizacao_atividade_torre_ide_localizacao_atividade_torre_seq'), 'Unidade de Conservação');
insert into localizacao_atividade_torre values(nextval('localizacao_atividade_torre_ide_localizacao_atividade_torre_seq'), 'Zona de amortecimento');

insert into tipo_unidade_conservacao_torre values(nextval('tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq'), 'Unidade de Uso Sustentável');
insert into tipo_unidade_conservacao_torre values(nextval('tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq'), 'Unidade de Proteção Integral');

--permissões atendente
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 3 and ide_perfil = 5;
insert into rel_grupo_perfil_funcionalidade values(69, 3, 5);
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 2 and ide_perfil = 5;
insert into rel_grupo_perfil_funcionalidade values(69, 2, 5);
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 76 and ide_perfil = 5;
insert into rel_grupo_perfil_funcionalidade values(69, 76, 5);

--permissões usuário externo
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 3 and ide_perfil = 2;
insert into rel_grupo_perfil_funcionalidade values(69, 3, 2);
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 2 and ide_perfil = 2;
insert into rel_grupo_perfil_funcionalidade values(69, 2, 2);
delete from rel_grupo_perfil_funcionalidade where ide_funcionalidade = 69 and ide_acao = 76 and ide_perfil = 2;
insert into rel_grupo_perfil_funcionalidade values(69, 76, 2);

insert into tipo_certificado values(10, 'TORRES');

CREATE SEQUENCE tipo_certificado_cta_seq;