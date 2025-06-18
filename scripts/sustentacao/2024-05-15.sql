--Scripts em caráter prioritário
--Data de geração 08/05/2024
--Versão 4.31.8

BEGIN;

-- [36289] Redirecionamento de processos
-- ATEND:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (26825, 24847);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (550, 12, 34, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 118, FALSE, 1, NULL);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (2648, 12, 34, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 118, FALSE, 1, NULL);
-- DIRRE:
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 704849;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93687, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 704847;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (92746, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2884, FALSE, 1, NULL);
-- NOUT:
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (730766,727666,727667,701501,726760,704095);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (74430, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90834, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91520, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93143, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93777, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
-- Residuais da pauta de "altamirano.neto":
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763189;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93515, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763520;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99185, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763531;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99204, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763271;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96570, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763128;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (72425, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763518;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99188, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763193;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99200, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763196;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99197, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763122;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87315, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763199;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (85771, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763529;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99205, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763527;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99206, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763191;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99202, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763198;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99192, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763519;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99187, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763200;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99190, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763201;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99189, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763186;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93183, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 764586;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90944, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763190;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93456, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763535;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99198, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763533;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99203, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763539;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99193, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763536;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99194, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763192;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (99201, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763138;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (81748, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 763165;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (79731, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36289', 2942, FALSE, 1, NULL);


--[36233] - Requerimento sumiu do sistema

insert
	into
	tramitacao_requerimento (ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
values(14,
1324152,
'2024-04-09 15:04:38.945',
90158 );

update 
	requerimento
set
	dtc_finalizacao = '2024-04-09 15:04:38.945'
where
	ide_requerimento = 1324152;

insert
	into
	requerimento_pessoa (ide_requerimento,
	ide_pessoa,
	ide_tipo_pessoa_requerimento,
	ind_solicitante,
	ind_usuario_logado)
values(1324152,
90158,
1,
'true',
'true');

insert
	into
	empreendimento_requerimento
(ide_requerimento,
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
values(1324152,
203066,
3,
null,
2,
1,
false,
null,
null,
null,
null);

-- [36239] - Requerimento sumiu do sistema
INSERT
        INTO
        tramitacao_requerimento (ide_status_requerimento,
        ide_requerimento,
        dtc_movimentacao,
        ide_pessoa)
VALUES(14,
1324284,
'2024-04-10 09:55:58.977',
434628);
UPDATE
        requerimento
SET
        dtc_finalizacao = '2024-04-10 09:55:58.977'
WHERE
        ide_requerimento = 1324284;
INSERT
        INTO
        requerimento_pessoa (ide_requerimento,
        ide_pessoa,
        ide_tipo_pessoa_requerimento,
        ind_solicitante,
        ind_usuario_logado)
VALUES(1324284,
434628,
1,
'true',
'true');
INSERT
        INTO
        empreendimento_requerimento
(ide_requerimento,
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
VALUES(1324284,
132112,
3,
NULL,
2,
1,
FALSE,
NULL,
NULL,
NULL,
NULL);

-- [36308] Transferência de Pauta

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 759429;
	        
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88408, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36308', 2942, FALSE, 1, NULL);

COMMIT;