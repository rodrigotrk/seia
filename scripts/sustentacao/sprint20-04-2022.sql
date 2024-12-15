--[33789] - Processo sumiu do sistema.

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao IN (526420,526419);
-- [33642] - Transferência de pauta
BEGIN;
UPDATE controle_tramitacao  SET ind_fim_da_fila =FALSE WHERE ide_controle_tramitacao IN (383993,357561,503954,433328,434340,530070,530071,530072,530073,504054,504055,504056,521332);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(61451, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(8244, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(68541, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(66434, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(7707, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(68413, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(56130, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area,dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, 
dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES 
(18335, 12, 3,now(), true,'', 'Solicitação de remanejamento de processo através do chamado Redmine 33642', 233, false,null, false);
COMMIT;

-- [33473] -- Erro ao tentar finalizar questionário da solicitação

BEGIN;
UPDATE
        public.requerimento
SET
        ind_excluido = true
WHERE
        ide_requerimento = 1118845;
COMMIT;

BEGIN;
UPDATE
        public.requerimento
SET
        ind_excluido = true
WHERE
        ide_requerimento = 1116409;

--[33860] - [PROCESSO] Processo duplicado no sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = FALSE 
        WHERE 
                ide_controle_tramitacao = 524900;
COMMIT;       

--[33858] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 415368;
COMMIT;	

--[33850] - [PROCESSO] Processo sumiu do sistema

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao IN (535414,535415,535416,535417,535418);
COMMIT;	

--[33846] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 531301;
COMMIT;	
--[33833] - [PROCESSO] Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 538201;
COMMIT;        
        
        
--[33830] - Erro ao finalizar cadastro de cefir

 UPDATE public.localizacao_geografica
SET ide_classificacao_secao=2, dtc_criacao='2016-07-26 01:14:00.644', ind_excluido=false, dtc_exclusao=NULL, ide_sistema_coordenada=4, fonte_coordenada=NULL, des_localizacao_geografica='', dtc_atualizacao=NULL
WHERE ide_localizacao_geografica=398598;
        
-- [33769] - Documento da resposta de notificação.

BEGIN;

UPDATE arquivo_processo SET ide_imovel = 19973 WHERE ide_arquivo_processo IN (170401, 198940, 190963, 201706, 198957, 201707, 202418, 202419, 202420);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(38293, 19973);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(46190, 19973);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(52013, 19973);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(52014, 19973);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(55285, 19973);

INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES(55286, 19973);

COMMIT;

--[29742] - [Prodeb #140748] Erro de duplicidade e triplicidade na aba localidade

        --Criação da coluna ind_excluido na tabela "município".

ALTER TABLE municipio ADD ind_excluido boolean DEFAULT false;

        --Update do vinculo de um municipio para outro

UPDATE logradouro SET ide_municipio = 442 WHERE ide_municipio = 897;

UPDATE municipio SET num_cep = '48000970' WHERE ide_municipio = 442;

UPDATE logradouro SET ide_municipio = 9239 WHERE ide_municipio = 9240;

UPDATE logradouro SET ide_municipio = 3720 WHERE ide_municipio = 3721;

UPDATE logradouro SET ide_municipio = 623 WHERE ide_municipio = 847;

UPDATE logradouro SET ide_municipio = 8018 WHERE ide_municipio = 8019; 

UPDATE logradouro SET ide_municipio = 691 WHERE ide_municipio in (1127,460);

UPDATE logradouro SET ide_municipio = 6339 WHERE ide_municipio = 6340;

UPDATE logradouro SET ide_municipio = 519 WHERE ide_municipio = 739;

UPDATE logradouro SET ide_municipio = 767 WHERE ide_municipio = 431;

UPDATE logradouro SET ide_municipio = 6886 WHERE ide_municipio = 6887;

UPDATE logradouro SET ide_municipio = 10263 WHERE ide_municipio = 10264;

UPDATE logradouro SET ide_municipio = 3725 WHERE ide_municipio = 2947;

UPDATE logradouro SET ide_municipio = 859 WHERE ide_municipio = 1178;

UPDATE logradouro SET ide_municipio = 862 WHERE ide_municipio = 1126;

UPDATE logradouro SET ide_municipio = 5356 WHERE ide_municipio = 5169;

UPDATE logradouro SET ide_municipio = 8616 WHERE ide_municipio = 8617;

UPDATE logradouro SET ide_municipio = 6256 WHERE ide_municipio = 6257;

UPDATE municipio SET num_cep = '87111011' WHERE ide_municipio = 6256;

UPDATE logradouro SET ide_municipio = 7829 WHERE ide_municipio = 8672;

UPDATE logradouro SET ide_municipio = 7082 WHERE ide_municipio = 6305; 	

UPDATE logradouro SET ide_municipio = 7844 WHERE ide_municipio = 7845;	

UPDATE logradouro SET ide_municipio = 8420 WHERE ide_municipio = 8421;

UPDATE endereco_empreendimento_municipio SET ide_municipio = 442 WHERE ide_municipio = 897;

UPDATE endereco_empreendimento_municipio SET ide_municipio = 623 WHERE ide_municipio = 847;

UPDATE endereco_empreendimento_municipio SET ide_municipio =691 WHERE ide_municipio IN (460,1127);

        --Excluindo lógicamente os municipios duplicados não utilizados.

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 897; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 9240; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 3721; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 847; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 8019; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio in (460,1127); 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 6340; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 739; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 431; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 6887; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 10264; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 10107;

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 2947; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 1178; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 1126; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 5169; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 5425; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 8617;

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 6257; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 8672; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 7845; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 6305; 

UPDATE municipio SET ind_excluido = TRUE WHERE ide_municipio = 8421; 

UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8293;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8292;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8291;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5423;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6969;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6932;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6933;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8074;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6247;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6245;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6248;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6246;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7148;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6720;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6719;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6257;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6181;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8375;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8421;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1254;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1178;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6267;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6268;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8369;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=61; 
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1235;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6294;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1354;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8070;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8636;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6078;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1010;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6540;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8366;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5362;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8621;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8622;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6305;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1238;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6679;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6562;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5421;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6116;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8706;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1576;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1294;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1575;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8161;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1367;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=104;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=739;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6808;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=82;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8617;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=3541;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=9240;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1349;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4181;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1692;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1691;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6767;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8158;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=9110;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7092;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4980;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1702;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4638;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=101;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6985;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6984;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6460;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6406;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6405;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8726;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4331;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6507;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1380;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1910;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1419;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6606;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1425;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1351;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5169;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4459;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1647;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8583;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7019;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1687;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6579;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8593;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8610;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1855;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8497;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5075;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6085;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6200;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1463;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1464;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1797;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7030;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6841;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7924;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1362;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7990;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=3654;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5296;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1684;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1579;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8246;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8247;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5513;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7889;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7768;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1263;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8465;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1905;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5508;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6416;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1792;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6176;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1232;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1392;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6327;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1233;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8459;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1225;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1467;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6157;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6499;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=847;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7790;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7789;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8362;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6310;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6311;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7882;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6891;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5586;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1591;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6204;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1666;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8151;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5437;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1493;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8056;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=2868;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7846;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6705;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8300;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8301;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8672;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1769;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1950;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1346;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1824;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6955;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6887;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1260;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6393;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5417;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6884;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1341;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6950;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6951;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6479;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8008;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=2948;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=3039;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7845;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1664;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4868;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=3991;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1304;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6153;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8472;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8816;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6262;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1928;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6298;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6297;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6296;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6299;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6300;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6301;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6826;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6827;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6848;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6866;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6489;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7879;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7827;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7854;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4028;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1266;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1920;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1265;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1504;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8499;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=145;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6495;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6494;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7759;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6516;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6731;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5821;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=4365;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1222;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7769;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6670;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6669;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6776;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6624;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=2947;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6976;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6316;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1725;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=2257;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6483;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1899;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1898;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1897;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6979;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6980;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1821;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6895;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6896;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6283;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6284;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6944;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6340;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8444;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8445;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8329;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1395;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1378;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1800;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6941;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6942;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6940;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1375;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6274;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6275;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6272;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6273;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6509;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=5411;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1383;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1340;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1384;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1126;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8117;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1256;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7946;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7945;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1127;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=460;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=3721;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8630;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8353;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8354;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8355;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1026;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6491;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6492;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6545;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=6463;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7308;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=7863;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=431;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=8019;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1784;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=897;
UPDATE municipio SET ind_excluido=TRUE WHERE ide_municipio=1543;

