INSERT INTO funcionalidade(ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (93, 4, 'Cumprimento de Reposição Florestal', false, now(), null);

INSERT INTO funcionalidade(ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (93, 4, 'Cumprimento de Reposição Florestal', false, now(), null);
    
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
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-CRF/cumprimentoReposicaoFlorestal.xhtml', false);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 1, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 2, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 3, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 4, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 5, 1);            

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 1, 5);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 2, 5);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 3, 5);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 4, 5);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 5, 5);    

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 1, 19);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 2, 19);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 3, 19);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 4, 19);
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (93, 5, 19); 
    
-----------------------------------------------------------------------------------------------------------------------------    
    
    
INSERT INTO funcionalidade(ide_funcionalidade, ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (94, 4, 'DAE', false, now(), null);

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (94, 14);
    
INSERT INTO acao (dsc_acao,ind_excluido,dtc_criacao,dtc_exclusao)
	VALUES ('Baixar', false, now(), null); 

INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (94, currval(('Acao_IDE_ACAO_seq'::text)::regclass));

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (94, '/paginas/manter-cerh/baixar-dae/baixar-dae.xhtml', false);
    
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (94, currval(('Acao_IDE_ACAO_seq'::text)::regclass), 1);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (94, currval(('Acao_IDE_ACAO_seq'::text)::regclass), 5);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (94, currval(('Acao_IDE_ACAO_seq'::text)::regclass), 19);


-----------------------------------------------------------------------------------------------------------------------------



insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal)
	values ('Detentor de ato autorizativo');
insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal)
	values ('Consumidor de matéria prima florestal de origem nativa');
insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal)
	values ('Devedor de reposição florestal por desmatamento sem autorização');

insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal, ide_pagamento_reposicao_florestal_pai)
	values ('Autorização de supressão vegetação (ASV) emitida por orgão competente', 1);
insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal, ide_pagamento_reposicao_florestal_pai)
	values ('Aproveitamento de material lenhoso (AML)', 1);
insert into pagamento_reposicao_florestal (nom_pagamento_reposicao_florestal, ide_pagamento_reposicao_florestal_pai)
	values ('Reconhecimento de volume florestal remanescente (RVFR)', 1);



insert into tipo_volume_florestal_remanescente (nom_tipo_volume_florestal_remanescente)
	values('Rendimento de material lenhoso superior ao concedido pelo ato autorizativo original (ASV ou AML)');

insert into tipo_volume_florestal_remanescente (nom_tipo_volume_florestal_remanescente)
	values('Madeira não transportada ou não explorada dentro do prazo de vigência do ato autorizativo');


insert into ato_ambiental (ide_ato_ambiental, ide_tipo_ato, sgl_ato_ambiental, nom_ato_ambiental, ind_declaratorio, ind_ativo, ind_automatico, ind_visivel_solicitacao_tla) 
	values(128, 7, 'CRF', 'Cumprimento de Reposição Florestal', true, true, true, false);

insert into documento_obrigatorio (ide_documento_obrigatorio, nom_documento_obrigatorio, ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo, num_tamanho)
	values (10081, 'Auto da infração', false, false, 1, true, 0);

insert into documento_obrigatorio (ide_documento_obrigatorio, nom_documento_obrigatorio, ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo, num_tamanho)
	values (10082, 'Cópia da portaria do ato autorizativo florestal', false, false, 1, true, 0);

insert into documento_obrigatorio(ide_documento_obrigatorio, nom_documento_obrigatorio, ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo, num_tamanho) 
	values(10083, 'Cópia da portaria do ato autorizativo florestal da ASV ou AML', false, false, 1, true, 0);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
	values (10081, 128, true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
	values (10082, 128, true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
	values (10083, 128, true);

insert into orgao_emissor_auto (nom_orgao_emissor_auto)
	values('INEMA');
insert into orgao_emissor_auto (nom_orgao_emissor_auto)
	values('IBAMA');
insert into orgao_emissor_auto (nom_orgao_emissor_auto)
	values('Município');
insert into orgao_emissor_auto (nom_orgao_emissor_auto)
	values('ICMBio');


ALTER TABLE bioma ADD COLUMN metros_cubicos numeric(12,2) ;

insert into parametro_calculo (ide_ato_ambiental, valor_taxa, dtc_criacao, ind_boleto, ind_ativo, fator_multiplicador)
	values (128, 18.00, now(), false, true, 1.20);


update bioma set metros_cubicos = 100 where ide_bioma = 1;
update bioma set metros_cubicos = 60 where ide_bioma = 2;
update bioma set metros_cubicos = 80 where ide_bioma = 3;


insert into sefaz_codigo_receita (num_codigo_receita, dsc_codigo_receita, ind_ativo)
	values (2248, 'TPP Reposição Florestal - Sec Meio Ambiente', true);


insert into indice_cobranca (dsc_indice_cobranca, sgl_indice_cobranca)
	values ('Índice Geral de Preços do Mercado', 'IGPM');

insert into indice_cobranca (dsc_indice_cobranca, sgl_indice_cobranca)
	values ('Sistema Especial de Liquidação e de Custódia', 'SELIC');

insert into tipo_certificado(ide_tipo_certificado, dsc_tipo_certificado) 
	values (8, 'CRF');
	


insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2018-09-07',true,false,'Dia da Pátria');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2018-10-12',true,false,'Nossa Senhora Aparecida');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2018-11-02',true,false,'Finados');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2018-11-15',true,false,'Proclamação da República');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2018-12-25',true,false,'Natal');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-01-01',true,false,'Reveillon');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-03-05',true,false,'Carnaval');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-04-19',true,false,'Sexta-Feria Santa');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-04-21',true,false,'Tiradentes');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-05-01',true,false,'Dia do Trabalhador');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-06-20',true,false,'Corpus Christi');

insert into calendario(dtc_feriado,ind_feriado,ind_pt_facultativo,dsc_feriado)
values
('2019-07-02',true,false,'Independência da Bahia');


insert into parametro (ide_parametro, nom_parametro, dsc_valor)
	values (40, 'Multa de atraso do Cumprimento de Reposição Florestal', '10.00');

	
insert into unidade_medida (ide_unidade_medida, cod_unidade_medida, nom_unidadade_medida)
	values (87, 'MDC', 'Metro de Carvão');

insert into unidade_medida (ide_unidade_medida, cod_unidade_medida, nom_unidadade_medida)
	values (88, 'ST', 'Metro Estéreo');