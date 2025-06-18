--Scripts em caráter prioritário
--Data de geração 15/05/2024
--Versão 4.31.9

BEGIN;

--[36338] - Processo sumiu do sistema

update 
	controle_tramitacao
set 
	ind_fim_da_fila = true
where 
	ide_controle_tramitacao = 728061;
	
--[36283] - Requerimento sumiu do sistema
insert 
	into
	tramitacao_requerimento(ide_status_requerimento, 
							ide_requerimento, 
							dtc_movimentacao, 
							ide_pessoa)
values(14, 
1326595, 
'2024-04-16 10:40:36.868',
51336);

update 
	requerimento
set
	dtc_finalizacao = '2024-04-16 10:40:36.868'
where 
	ide_requerimento = 1326595;

insert
	into
	requerimento_pessoa (ide_requerimento,
	ide_pessoa,
	ide_tipo_pessoa_requerimento,
	ind_solicitante,
	ind_usuario_logado)
values(1326595,
51336,
1,
'true',
'true');

insert
	into
	empreendimento_requerimento (ide_requerimento,
	ide_empreendimento,
	ide_fase_empreendimento,
	dtc_fase_empreendimento,
	ide_porte,
	ide_classe,
	ind_dla,
	num_processo_ana,
	num_portaria_ana,
	num_vazao_total,
	ide_programa_governo)
values(1326595,
203357,
3,
null,
2,
1,
false,
null,
null,
null,
null);

-- [36351] Processo sumiu do sistema
update
		controle_tramitacao
set
		ind_fim_da_fila = true
where
		ide_controle_tramitacao = 733317;

-- [35974] - Erro na finalização CEFIR

UPDATE
	imovel_rural
SET
	ide_requerente_cadastro = 1103367
WHERE
	ide_imovel_rural = 1218060;
	
-- [36333] - Cadastro de espécies na lista de espécies nativas do RFP

INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Astronium urundeuva / Aroreira-do-sertão', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Astronium urundeuva / Aroreira-do-sertão');

INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Cedrela odorata / Cedro-Cheiroso', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Cedrela odorata / Cedro-Cheiroso');

INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Plathymenia reticulata / Vinhático-do-campo', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Plathymenia reticulata / Vinhático-do-campo');

-- [36331] Processo Duplicado

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao IN (763220, 763181, 763180);
		
-- [36319] Transferência de Pauta
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (766495, 763460, 763471, 763418, 763421);

-- [36315] - Alteração de perfil de usuário interno para externo
UPDATE
	usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE
WHERE
	ide_pessoa_fisica = 719034;
	
-- [36311] - Alteração de perfil de usuário interno para externo
update 
	usuario
set
	ide_perfil = 2, 
	ind_tipo_usuario = false
where
	ide_pessoa_fisica = 1238345;

update 
	usuario
set
	ide_perfil = 2, 
	ind_tipo_usuario = false
where
	ide_pessoa_fisica = 149997;
	
--[36311] Alteração de perfil de usuário interno para externo
update 
        usuario
set
        ide_perfil = 2, 
        ind_tipo_usuario = false 
where
        ide_pessoa_fisica = 1238345;
update 
        usuario
set
        ide_perfil = 2, 
        ind_tipo_usuario = false 
where
        ide_pessoa_fisica = 149997;
        
--[36303] - Alteração de perfil de usuário interno para externo
update 
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false
where 
        ide_pessoa_fisica = 18370;
update 
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false
where 
        ide_pessoa_fisica = 1231664;
update
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false 
where 
        ide_pessoa_fisica = 8163;
update         
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false 
where 
        ide_pessoa_fisica = 1022025;
update 
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false 
where 
        ide_pessoa_fisica = 1214189;
        
--[36291] Retorno de status da RL;
update 
        reserva_legal
set 
        ide_status = 4
where
        ide_reserva_legal = 72907;
insert
        into
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
values (now(),
'UPD',
'168.90.77.39',
1
);
insert
        into
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
values (4,
195,
72907,
currval('historico_ide_historico_seq'));
update
        reserva_legal
set
        ide_status = 4
where
        ide_reserva_legal = 79005;
insert
        into
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
values (now(),
'UPD',
'168.90.77.39',
1
);
insert
        into
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
values (4,
195,
79005,
currval('historico_ide_historico_seq'));
update 
        reserva_legal
set
        ide_status = 4
where
        ide_reserva_legal = 79080;
insert
        into
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
values (now(),
'UPD',
'168.90.77.39',
1
);
insert
        into
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
values (4,
195,
79080,
currval('historico_ide_historico_seq'));
update 
        reserva_legal
set 
        ide_status = 4
where
        ide_reserva_legal = 80940;
insert
        into
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
values (now(),
'UPD',
'168.90.77.39',
1
);
insert
        into
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
values (4,
195,
80940,
currval('historico_ide_historico_seq'));

-- [36257] - Desbloqueio de todos os imóveis do tipo "Assentamento"

	UPDATE
		imovel_rural
	SET
		ind_bloqueio_limite = false
	WHERE
		ind_bloqueio_limite = true
		AND ide_tipo_cadastro_imovel_rural = 2
		AND ide_imovel_rural NOT IN(1075588,
		1098328,
		738915,
		656516,
		653686,
		746384,
		1107447,
		82360,
		656095,
		1280631,
		728425,
		1153121,
		943416,
		657391,
		657402,
		657406,
		728429,
		646689,
		650021,
		650662,
		653752,
		653760,
		654138,
		654415,
		654804,
		824123,
		828896,
		828897,
		828902,
		828907,
		654745,
		21067,
		655716,
		656114,
		924400,
		655876,
		657973,
		819630,
		731634,
		737535,
		658167,
		658177,
		658214,
		658218,
		787951,
		841034,
		880322,
		887958,
		658055,
		658072,
		693250,
		730247,
		1006166,
		943414,
		985409,
		1007299,
		1012592,
		1280616,
		657981,
		1084728,
		844636,
		996565,
		1092726,
		1280832,
		943394,
		981904,
		730275,
		731232,
		734967,
		738329,
		743614,
		745856,
		747385,
		657380,
		657400,
		1122800,
		1280634,
		992895,
		1280668,
		657378,
		657395,
		655774,
		656165,
		654323,
		774191,
		885811,
		1280672,
		658186,
		996569,
		791189,
		868735,
		877583,
		731244,
		731263,
		731266,
		655803,
		655862,
		655896,
		656079,
		656087,
		656152,
		656178,
		656182,
		654164,
		774165,
		774171,
		774176,
		843268,
		653757,
		653842,
		1317641,
		774224,
		657407,
		654409,
		1280836,
		787062,
		887031,
		977769,
		167113,
		650401,
		650642,
		653850,
		654788,
		654795,
		654828,
		1000565,
		1000610,
		1000949,
		19008,
		656158,
		656517,
		657388,
		657398,
		731277,
		735732,
		1003354,
		1317711,
		983670,
		983869,
		1003789,
		1280613,
		749010,
		749014,
		1280649,
		20253,
		840909,
		1280833,
		998067,
		656655,
		884675,
		654155,
		654346,
		896794,
		1280834,
		646866,
		648783,
		947866,
		774253,
		824479,
		947511,
		947662,
		653840,
		774276,
		658165,
		658207,
		1280837,
		654398,
		1116092,
		1128725,
		731284,
		998272,
		1280623,
		650651,
		654123,
		654401,
		654774,
		774291,
		656626,
		657375,
		657377,
		657382,
		650017,
		836876,
		836984,
		836995,
		912116,
		1188935,
		771081,
		648809,
		654111,
		654707,
		1000065,
		1090061,
		921221,
		922995,
		924651,
		857431,
		872096,
		920433,
		1154494,
		1005644,
		657987,
		658117,
		924219,
		21824,
		773723,
		774314,
		896986,
		735635,
		1076372,
		656526,
		9401,
		656628,
		656634,
		656638,
		1082892,
		774513,
		959403,
		996582,
		996588,
		996599,
		996888,
		21210,
		654392,
		1280655,
		656522,
		656528,
		657381,
		657396,
		657403,
		667011,
		27978,
		29461,
		829613,
		18018,
		655732,
		655812,
		656071,
		656176,
		658096,
		943432,
		943433,
		943435,
		947682,
		947767,
		921740,
		921965,
		924159,
		924395,
		1276093,
		738230,
		739061,
		745839,
		747407,
		996919,
		996922,
		996923,
		835750,
		690807,
		743606,
		1085829,
		922222,
		922225,
		996924,
		996926,
		996928,
		1021708,
		1061975,
		1280665,
		868102,
		868109,
		1019628,
		1280658,
		819020,
		1280667,
		20397,
		809437,
		761770,
		774875,
		933125,
		941045,
		977264,
		1303599,
		655920,
		656068,
		657947,
		657954,
		658100,
		1280677,
		656082,
		658065,
		654645,
		25408,
		726779,
		726790,
		727512,
		727519,
		727528,
		727538,
		727566,
		727582,
		727587,
		26027,
		650022,
		654462,
		1280628,
		658210,
		807447,
		881209,
		882874,
		883575,
		883670,
		884399,
		1003696,
		1011195,
		1101901,
		1109357,
		942972,
		995917,
		995920,
		996004,
		996010);
		
-- [36020] Erro ao consultar Requerimento

	UPDATE
		fauna
	SET
		ind_excluido = TRUE
	WHERE
		ide_fauna = 84605;
		
-- [36016] - Erro na finalizaçao CEFIR
DELETE
FROM
	cronograma_recuperacao cr
WHERE
	ide_app = 97689;
DELETE
FROM
	APP
WHERE
	ide_app = 97689;

-- [36284] Erro ao realizar análise técnica - Tela sem informações

	UPDATE
	        integrante_equipe
	SET
	        ind_lider_equipe = FALSE
	WHERE
	        ide_integrante_equipe = 71500;
	       
	INSERT INTO integrante_equipe
	(ide_integrante_equipe, ide_pessoa_fisica, ind_lider_equipe, ide_area, ide_area_responsavel, ide_equipe)
	VALUES(nextval('integrante_equipe_seq'), 19476, TRUE, NULL, NULL, 39690);
	INSERT INTO processo_ato_integrante_equipe 
	(ide_equipe_integrante, ide_processo_ato)
	VALUES(currval('integrante_equipe_seq'), 87295);

-- [36359] Erro ao realizar análise técnica - O parecer da análise técnica é de preenchimento obrigatório
INSERT INTO equipe
(ide_equipe, dtc_formacao_equipe, dtc_exclusao_equipe, ind_ativo, ide_area, ide_area_responsavel, ide_processo)
VALUES(nextval('equipe_seq'), now(), NULL, TRUE, 76, NULL, 95078);
INSERT INTO integrante_equipe
(ide_pessoa_fisica, ind_lider_equipe, ide_integrante_equipe, ide_area, ide_area_responsavel, ide_equipe)
VALUES(19476, TRUE, nextval('integrante_equipe_seq'), 76, 76, currval('equipe_seq'));
INSERT INTO processo_ato_integrante_equipe 
(ide_equipe_integrante, ide_processo_ato)
VALUES(currval('integrante_equipe_seq'), 104104);

-- [36310] - Alteração de perfil de usuário interno para externo
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 700131;
INSERT
        INTO
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_externo,
        dsc_comentario_interno,
        ide_pauta,
        ind_responsavel,
        ide_pessoa_fisica,
        ind_area_secundaria)
VALUES (76242,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36310 ',
2942,
FALSE,
1104205,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 648955;
INSERT
        INTO
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_externo,
        dsc_comentario_interno,
        ide_pauta,
        ind_responsavel,
        ide_pessoa_fisica,
        ind_area_secundaria)
VALUES (77753,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36310 ',
2942,
FALSE,
1104183,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 566105;
INSERT
        INTO
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_externo,
        dsc_comentario_interno,
        ide_pauta,
        ind_responsavel,
        ide_pessoa_fisica,
        ind_area_secundaria)
VALUES (79272,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36310 ',
2942,
FALSE,
980745,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 697480;
INSERT
        INTO
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_externo,
        dsc_comentario_interno,
        ide_pauta,
        ind_responsavel,
        ide_pessoa_fisica,
        ind_area_secundaria)
VALUES (80477,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36310 ',
2942,
FALSE,
980745,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 649011;
INSERT
        INTO
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_externo,
        dsc_comentario_interno,
        ide_pauta,
        ind_responsavel,
        ide_pessoa_fisica,
        ind_area_secundaria)
VALUES (87797,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36310 ',
2942,
FALSE,
1104205,
NULL);
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1079282;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 7110;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1051731;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1064088;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1181521;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 795271;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 185282;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1104573;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1102877;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 852778;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 830875;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 8527;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 407922;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 7112;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 114843;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 3911;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 904195;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1121318;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1128632;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 998344;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 998603;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 469311;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1065976;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 602700;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1078202;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1081344;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1050443;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 949987;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 13243;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1104205;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1104581;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1128050;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1104183;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1140193;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1161376;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 830879;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1256348;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1067467;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 16;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 980745;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1159952;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1207823;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 417763;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1045155;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 989969;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1104188;
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 507996;

-- [36309] - Transferência de Pauta
update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763189;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (93515,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763520;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99185,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763531;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99204,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763518;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99188,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763193;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99200,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763196;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99197,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763529;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99205,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763527;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99206,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763191;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99202,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763198;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99192,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763519;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99187,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763200;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99190,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763201;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99189,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763186;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (93183,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763190;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (93456,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763535;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99198,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763533;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99203,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763539;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99193,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763536;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99194,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 763192;

insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_externo,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica,
	ind_area_secundaria)
values (99201,
19,
76,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
2942,
false,
144783,
null);
	
-- [36205] Novos campos vinculado a funcionalidade de desbloqueio
	CREATE TABLE public.imovel_rural_desbloqueio (
		ide_imovel_rural_desbloqueio int4 DEFAULT nextval('DESBLOQUEIO_IMOVEL_RURAL_seq'::text::regclass) NOT NULL,
		ide_imovel_rural int4 NOT NULL,
		ide_documento_solicitacao int4 NOT NULL,
		ide_usuario int4 NOT NULL,
		observacao varchar(2000) NOT NULL,
		dtc_justificativa timestamp(6) DEFAULT now() NOT NULL,
		CONSTRAINT ide_imovel_rural_desbloqueio_pk PRIMARY KEY (ide_imovel_rural_desbloqueio)
	);

	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_documento_solicitacao_fk FOREIGN KEY (ide_documento_solicitacao) REFERENCES public.documento_imovel_rural(ide_documento_imovel_rural);
	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_imovel_rural_fk FOREIGN KEY (ide_imovel_rural) REFERENCES public.imovel_rural(ide_imovel_rural);
	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_usuario_fk FOREIGN KEY (ide_usuario) REFERENCES public.usuario(ide_pessoa_fisica);
	
	CREATE SEQUENCE public.DESBLOQUEIO_IMOVEL_RURAL_seq
		INCREMENT BY 1
		MINVALUE 1
		MAXVALUE 9223372036854775807
		START 1
		NO CYCLE;
	
	INSERT INTO public.hist_tabela
	(ide_tabela, desc_tabela)
	VALUES(nextval('tabela_ide_tabela_seq'), 'imovel_rural_desbloqueio');
	
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideImovelRuralDesbloqueio');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideImovelRural');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideDocumentoSolicitacao');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideUsuario');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'observacao');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'dtcJustificativa');

COMMIT;