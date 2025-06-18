BEGIN;

INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Incineração',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Utilização em forno industrial (exceto em fornos de cimento)',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Coprocessamento em fornos de cimento',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Utilização em caldeira',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Formulação de “blend” de resíduos',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Reprocessamento de solventes',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Reprocessamento de óleo',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Reutilização/Reciclagem/Recuperação',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Tratamento fisico-químico',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Tratamento biológico',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Tratamento térmico sem combustão (autoclave, microondas, ETD)',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Tratamento térmico: dessorção térmica',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Landfarming',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Descontaminação',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Encapsulamento/fixação química ou Solidificação',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Processamento a Plasma Térmico',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Aterro Industrial',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Estocagem temporaria',current_timestamp,null, false);
INSERT INTO DISPOSICAO_FINAL_RESIDUO (des_disposicao_final_residuo,dtc_criacao,dtc_exclusao,ind_excluido) VALUES ('Outras destinações (comportamento padrão utilizado nos FCEs)',current_timestamp,null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E01', 'Tambor de 200 L', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E02', 'A granel', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E03', 'Caçamba (contêiner)', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E04', 'Tanque', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E05', 'Tambores de outros tamanhos e bombonas', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E06', 'Fardos', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E07', 'Sacos plásticos', current_timestamp, null, false);

INSERT INTO ACONDICIONAMENTO(cod_acondicionamento, des_acondicionamento, dtc_criacao, dtc_exclusao, ind_excluido)
VALUES ('E08', 'Outras formas', current_timestamp, null, false);


INSERT INTO estado_fisico(nom_estado_fisico) VALUES ('Sólido');
INSERT INTO estado_fisico(nom_estado_fisico) VALUES ('Líquido');
INSERT INTO estado_fisico(nom_estado_fisico) VALUES ('Gases Contidos');


update documento_obrigatorio set nom_documento_obrigatorio = 'Anuência da empresa receptora' where ide_documento_obrigatorio = 1326;
update documento_obrigatorio set nom_documento_obrigatorio = 'Licença de operação da empresa geradora (se passível de licenciamento) ou declaração de que a atividade realizada não é passível de licenciamento;' where ide_documento_obrigatorio = 1324;
update documento_obrigatorio set nom_documento_obrigatorio = 'Licença de operação da empresa receptora' where ide_documento_obrigatorio = 1325;
update documento_obrigatorio set nom_documento_obrigatorio = 'Fichas de emergências dos resíduos, conforme norma NBR 7503 da ABNT;' where ide_documento_obrigatorio = 1328;
update documento_obrigatorio set ind_ativo=false where ide_documento_obrigatorio = 1327;

insert into documento_obrigatorio (nom_documento_obrigatorio,num_tamanho,ind_formulario,dsc_caminho_arquivo,ind_publico,ide_tipo_documento_obrigatorio,ind_ativo)
values ('Documento que comprove a qualidade de representante legal da empresa geradora (procuração, contrato social, etc.)',0,false,null,true,1,true);
insert into documento_ato (ide_Documento_obrigatorio,ide_ato_ambiental,ide_tipologia,ide_tipo_finalidade_uso_agua,ind_ativo) values (currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),45,null,null,true);

insert into documento_obrigatorio (nom_documento_obrigatorio,num_tamanho,ind_formulario,dsc_caminho_arquivo,ind_publico,ide_tipo_documento_obrigatorio,ind_ativo)
values ('Procuração – Este documento só deverá ser listado se o requerimento for aberto por procurador.',0,false,null,true,1,true);
insert into documento_ato (ide_Documento_obrigatorio,ide_ato_ambiental,ide_tipologia,ide_tipo_finalidade_uso_agua,ind_ativo) values (currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),45,null,null,true);

insert into documento_obrigatorio (nom_documento_obrigatorio,num_tamanho,ind_formulario,dsc_caminho_arquivo,ind_publico,ide_tipo_documento_obrigatorio,ind_ativo)
values ('Licença(s) da(s) empresa(s) responsável(is) pelo transporte do resíduo.',0,false,null,true,1,true);
insert into documento_ato (ide_Documento_obrigatorio,ide_ato_ambiental,ide_tipologia,ide_tipo_finalidade_uso_agua,ind_ativo) values (currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),45,null,null,true);



insert into funcionalidade (ide_secao, dsc_funcionalidade, ind_excluido,dtc_criacao) values (4,'Identificar Tipo de Solicitação',false,current_timestamp);
INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal) VALUES (currval('funcionalidade_ide_funcionalidade_seq'),'/paginas/identificar-tipo-solicitacao/identificar-tipo-solicitacao.xhtml', false);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 2);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 3);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 4);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 6);
insert into funcionalidade (ide_secao, dsc_funcionalidade, ind_excluido,dtc_criacao) values (4,'Declaração de Transporte de Resíduos Perigosos',false,current_timestamp);
INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal) VALUES (currval('funcionalidade_ide_funcionalidade_seq'),'/paginas/manter-dtrp/declaracao-transporte-residuo-perigoso-incluir.xhtml', false);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 1);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 4);
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao) VALUES (currval('funcionalidade_ide_funcionalidade_seq'), 6);
insert into tipo_endereco (nom_tipo_endereco) values ('Geração Resíduo');
insert into tipo_endereco (nom_tipo_endereco) values ('Destinação Resíduo');
update parametro_calculo set ind_ativo = false where ide_ato_ambiental = 45;
INSERT INTO parametro_calculo(ide_ato_ambiental, valor_taxa, dtc_criacao, ind_boleto, ind_ativo) VALUES (45, '250.00', now(),true,true);

/*permissão perfis atendentes e usuário externo*/
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,1,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,3,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,4,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,6,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,1,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,3,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,4,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (70,6,5);

insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,1,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,3,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,4,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,6,2);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,1,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,3,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,4,5);
insert into rel_grupo_perfil_funcionalidade (ide_funcionalidade,ide_acao,ide_perfil) values (71,6,5);

/*Tipos de veiculos*/
INSERT INTO tipo_veiculo(ide_tipo_veiculo, dsc_tipo_veiculo, ind_ativo) VALUES (1, 'Tanque', TRUE);
INSERT INTO tipo_veiculo(ide_tipo_veiculo, dsc_tipo_veiculo, ind_ativo) VALUES (2, 'Carga', TRUE);

COMMIT;