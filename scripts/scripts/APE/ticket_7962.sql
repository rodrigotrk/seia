update pergunta 
set cod_pergunta = 'NR_A3_P14', 
dsc_pergunta ='1.4. O empreendimento é passível de plano de suprimento sustentável?' 
where cod_pergunta = 'NR_A3_P13';

update pergunta 
set cod_pergunta = 'NR_A3_P13', 
dsc_pergunta ='1.3. Será necessária intervenção em área(s) protegida(s)?' 
where cod_pergunta = 'NR_A3_P12';

update pergunta 
set cod_pergunta = 'NR_A3_P12', 
dsc_pergunta='1.2. O empreendimento ou atividade está em processo de licenciamento pelo município ou união?' 
where cod_pergunta = 'NR_A3_P11';

update pergunta 
set cod_pergunta = 'NR_A3_P12_N',
dsc_pergunta='1.2. O empreendimento ou atividade foi licenciada pelo município ou união?'
where cod_pergunta = 'NR_A3_P11_N';

alter table pergunta alter column dsc_pergunta type varchar(500);

insert into pergunta(dsc_pergunta, ind_agrupador, tipo_classificacao_secao_pergunta, ind_ativo, ide_pergunta_pai, dtc_criacao, cod_pergunta)
values ('1.1 O empreendimento ou atividade necessita  de uma autorização por procedimento especial de licenciamento de acordo com o previsto no Decreto 16.963/2016 aplicáveis a agricultura de sequeiro, pecuária extensiva ou agricultura irrigada?', false, 0, true, null, now(), 'NR_A3_P11');

insert into pergunta(dsc_pergunta, ind_agrupador, tipo_classificacao_secao_pergunta, ind_ativo, ide_pergunta_pai, dtc_criacao, cod_pergunta)
values ('1.1.1 A atividade ou empreendimento possui supressão de vegetação nativa autorizada pelo órgão ambiental competente?', false, 0, true, null, now(), 'NR_A3_P111');

insert into pergunta(dsc_pergunta, ind_agrupador, tipo_classificacao_secao_pergunta, ind_ativo, ide_pergunta_pai, dtc_criacao, cod_pergunta)
values ('1.1.1.1 A supressão da vegetação nativa foi anterior a 22 de Julho de 2008?', false, 0, true, null, now(), 'NR_A3_P1111');

insert into pergunta(dsc_pergunta, ind_agrupador, tipo_classificacao_secao_pergunta, ind_ativo, ide_pergunta_pai, dtc_criacao, cod_pergunta)
values ('1.1.2 O empreendimento faz captação de água de corpo ou manancial hídrico para atividade de agricultura ou pecuária extensiva?', false, 0, true, null, now(), 'NR_A3_P112');

insert into pergunta(dsc_pergunta, ind_agrupador, tipo_classificacao_secao_pergunta, ind_ativo, ide_pergunta_pai, dtc_criacao, cod_pergunta)
values ('1.1.2.1 Existe processo de dispensa e/ou processo de outorga autorizado pelo órgão ambiental competente?', false, 0, true, null, now(), 'NR_A3_P1121');