--Scripts em caráter prioritário
--Data de geração 18/09/2024
--Versão 4.33.6

BEGIN;

-- [36725] - Conversão para Usuário Externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 62709;
        
-- [36724] - Pagamento REPFLOR
INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1350711,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1352961,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1358056,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1363490,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1364729,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1332651,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1365286,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1364724,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1365982,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1366667,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1366488,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1367924,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1369390,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1367010,
now(),
1);

INSERT
	INTO
	tramitacao_requerimento
(ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(8,
1368712,
now(),
1);

-- [36718] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 796954;
        
--[36771] - Requerimento sumiu do sistema
UPDATE
        requerimento
SET
        dtc_finalizacao = '2024-09-09 10:34:07.217'
WHERE
        ide_requerimento = 1369296;
INSERT
        INTO
        tramitacao_requerimento
(
        ide_status_requerimento,
        ide_requerimento,
        dtc_movimentacao,
        ide_pessoa)
VALUES(
14,
1369296,
'2024-09-09 10:34:07.217',
356205);
INSERT
        INTO
        requerimento_pessoa
(
        ide_requerimento,
        ide_pessoa,
        ide_tipo_pessoa_requerimento,
        ind_solicitante,
        ind_usuario_logado)
VALUES(
1369296,
356205,
1,
FALSE,
FALSE);
INSERT
        INTO
        empreendimento_requerimento (
        ide_requerimento,
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
VALUES(
1369296,
121095,
NULL,
NULL,
6,
1,
FALSE,
NULL,
NULL,
NULL,
NULL);

--[36704] - Trasferência de pauta
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_processo = 19250;
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(19250, 12, 36, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36704', 205, false, 1227, NULL);

-- [36694] - Erro ao Aprovar Reserva Legal 
UPDATE
	reserva_legal_bloqueio
SET
	ide_usuario = (
	SELECT
		u.ide_usuario
	FROM
		usuario u
	WHERE
		u.dsc_login = 'vanessa.santos'
)
WHERE
	ide_reserva_legal IN (
	SELECT
		rl.ide_reserva_legal
	FROM
		reserva_legal rl
	JOIN imovel_rural ir ON
		ir.ide_imovel_rural = rl.ide_imovel_rural
	WHERE
		ir.ide_imovel_rural IN (1201305, 977351)
);
-- Registrando no histórico do Imóvel Rural a alteração realizada:
INSERT
	INTO
	hist_historico (ide_historico,
	data_historico,
	acao_historico,
	ip_historico,
	ide_usuario)
VALUES (nextval('historico_ide_historico_seq'),
now(),
'UPD',
'0',
(
SELECT
	ide_usuario
FROM
	usuario
WHERE
	dsc_login = 'vanessa.santos'
LIMIT 1));
-- Inserindo valores no histórico para cada imóvel:
INSERT
	INTO
	hist_valor (val_valor,
	ide_campo,
	ide_registro,
	ide_historico)
SELECT
	'Desbloqueio da RL para análise (atendimento ticket REDMINE #36694).',
	279,
	ir.ide_imovel_rural,
	currval('historico_ide_historico_seq')
FROM
	imovel_rural ir
WHERE
	ir.ide_imovel_rural IN (1201305, 977351);
	
-- [36680] - Transferência de Pauta
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE 
WHERE ide_processo IN (83041,
63865,
75085,
75084,
90439,
95427,
95952,
98164);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(83041, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(63865, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, 2795, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(75085, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(75084, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(90439, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(95427, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, 2789, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(95952, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, 123, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(98164, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36680', 2942, true, NULL, NULL);

-- [36719] - Erro ao realizar análise técnica
update
    integrante_equipe
set
    ide_pessoa_fisica = 19476,
    ind_lider_equipe = true,
    ide_area = 76,
    ide_area_responsavel = 76,
    ide_equipe = 41126
where
    ide_integrante_equipe = 78190;

COMMIT;