  -- #### Abaixo Scripts para a versão 1.3 ####   
    
INSERT INTO parametro (ide_parametro,nom_parametro,dsc_valor)
	values (41,'Número máximo de emails por consulta',250);

insert into parametro (ide_parametro,nom_parametro,dsc_valor)
	values (42,'Horário da execução da rotina para envio de email','0 0/30 * * * ?');

insert into parametro (ide_parametro,nom_parametro,dsc_valor)
	values (43,'Horário da execução da rotina para verificar emails pendentes','0 0/59 8-18 ? * MON-FRI');

	-- ### COBRANÇA
	
INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (2, 'CERH - DAE', false, now(), null);
    
INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (78, '/paginas/manter-cerh/gerar-dae/solicitar-dae.xhtml', false);
    
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (78, 14, 2);
    
--Classe do corpo Hidrico
--CAPTAÇÃO/DERIVAÇÃO/EXTRAÇÃO
---SUPERFICIAL
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (1, 'ESPECIAL', false);
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (2, '1', false);
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (3, '2', false);
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (4, '3', false);
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (5, '4', false);
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (6, '4', false);
--SUBTERRANEA
INSERT INTO cerh_classe_corpo_hidrico(ide_cerh_classe_corpo_hidrico, dsc_classe_corpo_hidrico, ind_excluido)
	VALUES (7, '-', false);
--Consumo
--CAPTAÇÃO/DERIVAÇÃO/EXTRAÇÃO
---SUPERFICIAL
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (1, 1, 3, 1, 1.15, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (2, 1, 3, 2, 1.10, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (3, 1, 3, 3, 1.00, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (4, 1, 3, 4, 0.9, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (5, 1, 3, 5, 0.8, false, now(), false, null);
--SUBTERRANEA
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (6, 1, 2, 6, 1.0, false, now(), false, null);
--CONSUMO    
---SUPERFICIAL
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (7, 1, 3, 2, 1.0, true, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (8, 1, 3, 3, 1.0, true, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (9, 1, 3, 4, 1.0, true, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (10, 1, 3, 5, 1.0, true, now(), false, null);
--SUBTERRRANEA
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (11, 1, 2, 6, 1.0, true, now(), false, null);
--LANÇAMENTO DE EFLUENTES
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (12, 1, 5, 3, 1.0, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (13, 1, 5, 4, 0.9, false, now(), false, null);
 INSERT INTO cerh_pond_classe_corpo_hidrico(
            ide_cerh_pond_classe_corpo_hidrico, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ide_cerh_classe_corpo_hidrico, 
            vlr_referencia, 
            ind_consumo, 
            dt_cadastro, 
            ind_excluido, 
            dt_excluido)
    VALUES (14, 1, 5, 5, 0.8, false, now(), false, null);
--PPU – Preço Público Unitário
--Captação
---Superficial
INSERT INTO cerh_preco_pub_unitario(
            ide_cerh_preco_pub_unitario, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ind_consumo, 
            vlr_referencia, 
            dt_cadstro, 
            ind_excluido, 
            dt_excluido)
    VALUES (1, 1, 3, false, 0.03, now(), false, null);
--Subterranea
INSERT INTO cerh_preco_pub_unitario(
            ide_cerh_preco_pub_unitario, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ind_consumo, 
            vlr_referencia, 
            dt_cadstro, 
            ind_excluido, 
            dt_excluido)
    VALUES (2, 1, 2, false, 0.04, now(), false, null);
--Consumo
INSERT INTO cerh_preco_pub_unitario(
            ide_cerh_preco_pub_unitario, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ind_consumo, 
            vlr_referencia, 
            dt_cadstro, 
            ind_excluido, 
            dt_excluido)
    VALUES (3, 1, 3, true, 0.04, now(), false, null);
 INSERT INTO cerh_preco_pub_unitario(
            ide_cerh_preco_pub_unitario, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ind_consumo, 
            vlr_referencia, 
            dt_cadstro, 
            ind_excluido, 
            dt_excluido)
    VALUES (4, 1, 2, true, 0.04, now(), false, null);
--Lançamento de Efluentes
INSERT INTO cerh_preco_pub_unitario(
            ide_cerh_preco_pub_unitario, 
            ide_geo_rpga, 
            ide_tipo_uso_recurso_hidrico, 
            ind_consumo, 
            vlr_referencia, 
            dt_cadstro, 
            ind_excluido, 
            dt_excluido)
    VALUES (5, 1, 5, false, 0.05, now(), false, null);
    
--Ponderador de Volume Consumido
INSERT INTO cerh_pond_vol_consumido(
            ide_cerh_pond_vol_consumido, 
            ide_geo_rpga, 
            dt_cadastro, 
            vlr_referencia, 
            ind_excluido, 
            dt_excluido)
    VALUES (1, 1, now(), 0.2, false, now());
--Ponderador de Gestão
INSERT INTO cerh_pond_gestao(
            ide_cerh_pond_gestao, 
            ide_geo_rpga, 
            dt_cadastro, 
            vl_referencia, 
            ind_excluido, 
            dt_excluido)
    VALUES (1, 1, now(), 1.0, false, null);
    
--Código de Receita -> Dados ficticios inseridos para testar sistema 
INSERT INTO sefaz_codigo_receita(
            ide_sefaz_codigo_receita, num_codigo_receita, dsc_codigo_receita)
    VALUES (1, 2214, 'Codigo de Receita CERH');--TODO 2214 obtido no exemplo enviado pela SEFAZ
INSERT INTO sefaz_codigo_receita(
            ide_sefaz_codigo_receita, num_codigo_receita, dsc_codigo_receita)
    VALUES (2, 2248, 'Codigo de Receita Reposição Florestal');

INSERT INTO cerh_cod_receita_tipo_uso(
            ide_cerh_cod_receita_tipo_uso, ide_sefaz_codigo_receita, ide_tipo_uso_recurso_hidrico)
    VALUES (1, 1, 1);
INSERT INTO cerh_cod_receita_tipo_uso(
            ide_cerh_cod_receita_tipo_uso, ide_sefaz_codigo_receita, ide_tipo_uso_recurso_hidrico)
    VALUES (2, 1, 2);
INSERT INTO cerh_cod_receita_tipo_uso(
            ide_cerh_cod_receita_tipo_uso, ide_sefaz_codigo_receita, ide_tipo_uso_recurso_hidrico)
    VALUES (3, 1, 3);
INSERT INTO cerh_cod_receita_tipo_uso(
            ide_cerh_cod_receita_tipo_uso, ide_sefaz_codigo_receita, ide_tipo_uso_recurso_hidrico)
    VALUES (4, 1, 4);
INSERT INTO cerh_cod_receita_tipo_uso(
            ide_cerh_cod_receita_tipo_uso, ide_sefaz_codigo_receita, ide_tipo_uso_recurso_hidrico)
    VALUES (5, 1, 5);    
    
-- Situação DAE
INSERT INTO situacao_dae(ide_situacao_dae, dsc_situacao_dae)
values(1, 'Aguarando impressão');
INSERT INTO situacao_dae(ide_situacao_dae, dsc_situacao_dae)
values(2, 'Em Aberto');
INSERT INTO situacao_dae(ide_situacao_dae, dsc_situacao_dae)
values(3, 'Pago');
INSERT INTO situacao_dae(ide_situacao_dae, dsc_situacao_dae)
values(4, 'Vencido');
INSERT INTO situacao_dae(ide_situacao_dae, dsc_situacao_dae)
values(5, 'Cancelado');


--TODO Alterar Tipos de Uso com Código da Receita
UPDATE tipo_uso_recurso_hidrico SET ide_sefaz_codigo_receita = 1 WHERE ide_sefaz_codigo_receita is null

--Juros SELIC
INSERT INTO cerh_selic_mensal(ide_cerh_selic_mensal, num_mes, num_ano, val_taxa, ide_usuario_cadastro, dtc_alteracao)
    VALUES
    --2006
    (1, 1, 2016, 1.0629, null, now()),
    (2, 2, 2016, 1.0095, null, now()),
    (3, 3, 2016, 1.1698, null, now()),
    (4, 4, 2016, 1.0629, null, now()),
    (5, 5, 2016, 1.1163, null, now()),
    (6, 6, 2016, 1.1698, null, now()),
    (7, 7, 2016, 1.1163, null, now()),
    (8, 8, 2016, 1.2233, null, now()),
    (9, 9, 2016, 1.1163, null, now()),
    (10, 10, 2016, 1.1275, null, now()),
    (11, 11, 2016, 1.0798, null, now()),
    (12, 12, 2016, 1.1002, null, now()),
    --2017
    (13, 1, 2017, 1.0636, null, now()),
    (14, 2, 2017, 0.9309, null, now()),
    (15, 3, 2017, 0.9863, null, now()),
    (16, 4, 2017, 0.9098, null, now()),
    (17, 4, 2017, 0.9096, null, now()),
    (18, 6, 2017, 0.8053, null, now()),
    (19, 7, 2017, 0.8196, null, now()),
    (20, 8, 2017, 0.7542, null, now())
/*
 * 
deletar cobrança
SELECT * FROM cerh_cobranca;
SELECT * FROM cerh_parcelas_cobranca;
SELECT * FROM cerh_dae;
SELECT * FROM cerh_dae_tipo_uso;
SELECT * from hist_situacao_dae
SELECT * from situacao_dae


delete from hist_situacao_dae;
delete from cerh_dae_tipo_uso;
delete from cerh_dae;
delete from cerh_parcelas_cobranca;
delete from cerh_cobranca;
 * 
 */
