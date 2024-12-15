--Scripts em caráter prioritário
--Data de geração 24/07/2024
--Versão 4.33.1

BEGIN;

--[36527] - Transferência de Pauta

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747589;

	-- [36459] Erro na finalização do CEFIR
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1130266;
        
UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1314138;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
93880,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747564;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
91941,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747596;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
93895,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 620254;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
70130,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 531321;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
69509,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 726874;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
60864,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747559;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
89520,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 663701;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
63787,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747576;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
92928,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747562;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
90660,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 761557;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
57795,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 579691;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
73241,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747577;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
93244,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 664406;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
56982,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 782586;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
50872,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 780333;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
26600,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 732125;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
71066,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 534049;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
72170,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747555;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
89461,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 752972;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
94582,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 739857;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
91313,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 656658;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
54877,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 642813;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
54558,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 511186;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
69569,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747578;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
93285,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 634105;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
78853,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 765384;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
87903,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 748890;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
89704,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 748892;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
90576,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 732126;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
70877,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 643706;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
70097,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 687877;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
72630,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 777670;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
71771,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 583354;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
31207,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 506132;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
72924,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 747563;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
91517,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 702766;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
72920,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 696299;

INSERT
	INTO
	controle_tramitacao (
	ide_processo,
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
VALUES (
70131,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36309 ',
205,
FALSE,
972839,
NULL);

-- [36489] Pagamento REPFLOR

-- Requerimento que estão com status de "Validado":

	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1333108, now(), 1);        
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1337087, now(), 1);        
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1336508, now(), 1);        
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1335318, now(), 1);
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1326558, now(), 1);        
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1341655, now(), 1);        
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1344081, now(), 1);
	
-- [36402] - Gravar taxa boleto / dae

INSERT
        INTO
        parametro_calculo (
        ide_ato_ambiental,
        ide_tipologia,
        ide_tipo_finalidade_uso_agua,
        valor_taxa,
        area_minima,
        area_maxima,
        ide_classe,
        num_ufir,
        dtc_criacao,
        ind_boleto,
        ind_ativo,
        fator_multiplicador,
        vazao_minima,
        vazao_maxima,
        ide_tipo_criadouro_fauna,
        ide_bioma)
VALUES(
108,
302,
29,
2500.00,
NULL,
NULL,
NULL,
NULL,
now(),
TRUE,
TRUE,
NULL,
NULL,
NULL,
NULL,
NULL);

-- [36515] - Retorno de Status
    UPDATE
        reserva_legal
    SET
        ide_imovel_rural = NULL
    WHERE
        ide_reserva_legal = 35210;
 
--[36520] - Erro ao realizar análise técnica
UPDATE
        fce
SET
        ide_origem_fce = 2,
        ide_analise_tecnica = 30997
WHERE
        ide_fce = 46127;
        
--[36559] - Processo sumiu do sistema

UPDATE         
    controle_tramitacao 
SET 
    ind_fim_da_fila = TRUE 
WHERE 
    ide_controle_tramitacao = 784072;
    
-- [36548] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1079283;
        
--[36533] - Erro ao realizar análise técnica

INSERT
	INTO
	integrante_equipe (
    ide_pessoa_fisica,
	ind_lider_equipe,
	ide_area,
	ide_area_responsavel,
	ide_equipe
)
VALUES (
    2795,
    FALSE,
    NULL,
    NULL,
    38571
);

INSERT
	INTO
	processo_ato_integrante_equipe (
    ide_equipe_integrante,
	ide_processo_ato
)
VALUES (
    (
SELECT
	currval('integrante_equipe_seq')),
    99022
);

UPDATE
	public.processo_ato
SET
	ide_processo = 90515,
	ide_ato_ambiental = 109,
	ind_excluido = FALSE,
	dtc_exclusao = NULL,
	ide_tipologia = NULL,
	ide_processo_reenquadramento = NULL
WHERE
	ide_processo_ato = 99022;
	
-- [36543] - Transferência de pauta

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =93616 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (93616, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =79075 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (79075, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =73552 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (73552, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82091 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82091, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =88267 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (88267, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =80430 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80430, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =64488 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (64488, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =76881 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (76881, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =102333 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (102333, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =77753 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (77753, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =80948 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80948, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =79663 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (79663, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =80053 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80053, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =75830 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (75830, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =72886 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (72886, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =83234 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (83234, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =73579 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (73579, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =87803 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (87803, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =64063 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (64063, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82850 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82850, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =78080 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (78080, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =75096 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (75096, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =64349 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (64349, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82342 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82342, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =96484 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (96484, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =84702 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (84702, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =78747 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (78747, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =15891 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (15891, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =71819 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (71819, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =69936 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (69936, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =94576 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (94576, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =64348 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (64348, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =90667 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (90667, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82075 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82075, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =80227 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80227, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =91871 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (91871, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =81629 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (81629, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =33281 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (33281, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =81785 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (81785, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =92700 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (92700, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =71585 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (71585, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =73537 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (73537, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =91642 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (91642, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82472 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82472, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82938 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82938, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =74061 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (74061, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =75424 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (75424, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =82961 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (82961, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =72425 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (72425, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =64355 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (64355, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =68821 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (68821, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 46891, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =71206 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (71206, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =75209 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (75209, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =89310 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (89310, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =50872 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (50872, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);
UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_processo =86341 AND ind_fim_da_fila=TRUE;
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (86341, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36543 ', 2942, FALSE, 20369, NULL);                     

-- [36541] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 18604;
        
-- [36538] Transferência de Pauta

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 781602;
	        
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(79586, 19, 43, NOW(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36538', 1254, FALSE, 1, NULL);

-- [36277] Remoção de "Outros" da lista de "Tipo de vínculo/domínio" (PCT)

	UPDATE
		tipo_territorio_pct
	SET
		ind_excluido = TRUE,
		dtc_exclusao = now()
	WHERE
		ide_tipo_territorio_pct = 4;
		
-- [36411] - Erro ao visualizar cadastro de Imóvel Rural
ALTER TABLE outros_passivos_ambientais ADD COLUMN ind_excluido BOOLEAN DEFAULT FALSE;

UPDATE
	outros_passivos_ambientais
SET
	ind_excluido = TRUE
WHERE
	ide_outros_passivos_ambientais = 2010;
	
-- [36497] Processo sendo encaminhado para o coordenador errado (retorno de notificação não respondida)

	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772388;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72371, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771545;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73249, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 776861;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94700, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772832;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (50531, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775074;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73949, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 768938;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73508, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772091;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90175, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 773543;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65467, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 783518;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80024, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772831;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (89902, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771223;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83218, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771227;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83099, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772836;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93913, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771216;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81153, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 768937;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73507, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775077;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74806, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 769495;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92837, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 781353;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75397, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771225;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76160, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 776889;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (50532, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 773547;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73791, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771224;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90122, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775090;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75228, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 779125;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83217, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 773208;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74361, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 778241;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93009, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 778240;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74154, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 770874;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83749, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771215;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71841, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775076;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71132, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772389;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (70991, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 778903;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (21771, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 772837;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (51870, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 775157;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74822, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 771218;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75511, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36497', 2942, FALSE, 1, NULL);
	
-- [36561] Erro ao anexar shapes a notificação
UPDATE
        motivo_notificacao_imovel
SET
        ide_imovel = 1370599
WHERE
        ide_motivo_notificacao_imovel = 2596;

<<<<<<< HEAD
	
-- [36555] - Resolução de chamado notificação
DELETE
FROM
	controle_tramitacao
WHERE
	ide_controle_tramitacao = 778118;
	
=======
--[36569] - Todos os requerimentos sem numero 
CREATE OR REPLACE FUNCTION resolver() 
RETURNS void
LANGUAGE plpgsql 
AS $function$
    DECLARE
    t record;
BEGIN
    FOR t IN 
        (
           SELECT r.ide_requerimento
                    FROM tramitacao_requerimento tr 
                    INNER  JOIN requerimento r  
                    ON r.ide_requerimento = tr.ide_requerimento
                    WHERE tr.ide_status_requerimento != 14 
                    AND tr.ide_status_requerimento != 9
                    AND r.num_requerimento IS NULL
                    AND r.dtc_finalizacao IS NULL
                    ORDER BY r.num_requerimento DESC
        )
           
        LOOP 
                           UPDATE
                                        requerimento r1
                                SET
                                        num_requerimento = (
                                                            SELECT
                                                                  CAST('2024.001.0' AS VARCHAR) || 
                                                                  CAST((CAST(split_part(split_part((
                                                                   SELECT
                                                                        r.num_requerimento
                                                                   FROM
                                                                        requerimento r
                                                                   WHERE
                                                                        r.num_requerimento IS NOT NULL
                                                                   ORDER BY
                                                                        r.num_requerimento DESC
                                                                   LIMIT 1),
                                                                  '.',
                                                                   3),
                                                                   '/',
                                                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                                                           ),
                                        dtc_finalizacao = (SELECT dtc_movimentacao
                                                       FROM tramitacao_requerimento tr2 
                                                       WHERE tr2.ide_requerimento = t.ide_requerimento
                                                       ORDER BY dtc_movimentacao 
                                                       DESC LIMIT 1)
                                WHERE
                                        r1.ide_requerimento = (SELECT r.ide_requerimento
                                                                    FROM tramitacao_requerimento tr 
                                                                    INNER  JOIN requerimento r  
                                                                    ON r.ide_requerimento = tr.ide_requerimento
                                                                    WHERE tr.ide_status_requerimento != 14 
                                                                    AND tr.ide_status_requerimento != 9
                                                                    AND r.num_requerimento IS NULL
                                                                    AND r.dtc_finalizacao IS NULL
                                                                    ORDER BY r.num_requerimento DESC
                                                                           LIMIT 1);
                                        END LOOP;
                                    END;
$function$
;
SELECT * FROM resolver();
DROP FUNCTION resolver();
        
COMMIT;