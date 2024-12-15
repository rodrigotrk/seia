--Scripts em caráter prioritário
--Data de geração 27/12/2023
--Versão 4.29.9

BEGIN;

-- [35903] - Transferência de Processos - DIRRE
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao IN (
729214,
729215,
729216,
729217,
729218,
729219,
729220,
729221,
729222,
729183,
729184,
729186,
729187,
729188,
729189,
729190,
729191,
729192,
729193,
729194,
729195,
729196,
729197,
729198,
729199,
729200,
729201,
729202,
729204,
729205,
729206,
729207,
729208,
729209,
729210,
729211,
729212,
729213,
729174,
729175,
729176,
729177,
729178,
729179,
729180,
729181,
729182,
729090,
729091,
729092,
729093,
729094,
729095,
729096,
729097,
729098,
729099,
729100,
729101,
729102,
729061,
729062,
729063,
729064,
729066,
729067,
729068,
729069,
729070,
729071,
729072,
729073,
729074,
729075,
729076,
729077,
729078,
729079,
729080,
729081,
729082,
729084,
729085,
729086,
729087,
729088,
729089,
729054,
729055,
729056,
729057,
729058,
729059,
729060);
-- Alteração de Usuário
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728446;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (875, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728447;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (5891, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728435;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (18541, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728306;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (40167, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728397;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (48505, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 712192;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (65018, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 710028;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (65462, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 704994;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (65463, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 723551;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (65465, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727123;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (65720, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728338;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (71279, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728060;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (73689, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728486;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77444, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 708548;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77747, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728331;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (80039, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728037;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (80329, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728483;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (81065, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 717974;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (81644, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727760;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (82225, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727767;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (82535, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 709063;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86034, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728116;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86632, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728005;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86995, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728300;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87226, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727931;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87295, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728041;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88314, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728148;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88490, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728464;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88608, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728437;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90503, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727996;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91012, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 723226;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91044, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727923;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (92836, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727925;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93524, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728055;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93713, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728051;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93769, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728146;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94538, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728139;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95190, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728149;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95203, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727782;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95285, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728475;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95406, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728141;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96434, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728143;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96564, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728474;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96923, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728449;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97646, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728440;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97669, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728453;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97678, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728438;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (97865, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35903', 2884, FALSE, 1, NULL);        
-- Processos residuais
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 729047;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91171, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728805;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93639, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728686;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (70281, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728884;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95148, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728785;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96437, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728645;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91939, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728952;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90082, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728815;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94339, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728649;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90472, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728655;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93078, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728939;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77728, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728786;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (75873, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728802;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (82936, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728613;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77563, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728764;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (79378, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728798;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93752, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 729034;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94640, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728616;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (72637, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728800;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (80957, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728953;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87436, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728885;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86955, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728804;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (96589, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728575;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77292, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728661;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90471, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 729037;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88994, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728598;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (83155, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728647;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91408, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728651;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (90470, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728774;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (73938, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728635;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (91409, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728607;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (72867, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728714;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (77657, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728588;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (76988, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728641;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (89705, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728772;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (69724, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728604;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95776, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728523;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86272, 4, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728606;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (50931, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728943;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (73459, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728788;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (92270, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728747;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (69076, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728976;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95509, 12, 2, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através DO chamado Redmine 35903 ', 2884, FALSE, 1, NULL);

-- [35913] - Correção de Duplicidade
DELETE FROM especie_supressao_nome_popular_especie
WHERE ide_nome_popular_especie=9945 AND ide_especie_supressao=12477;
DELETE FROM especie_supressao_nome_popular_especie
WHERE ide_nome_popular_especie=9944 AND ide_especie_supressao=12476;
DELETE FROM especie_supressao_nome_popular_especie
WHERE ide_nome_popular_especie=9943 AND ide_especie_supressao=12475;
DELETE FROM especie_supressao_nome_popular_especie
WHERE ide_nome_popular_especie=9942 AND ide_especie_supressao=12474;
DELETE FROM especie_supressao_nome_popular_especie
WHERE ide_nome_popular_especie=9941 AND ide_especie_supressao=12473;
DELETE FROM especie_supressao
WHERE ide_especie_supressao=12477;
DELETE FROM especie_supressao
WHERE ide_especie_supressao=12476;
DELETE FROM especie_supressao
WHERE ide_especie_supressao=12475;
DELETE FROM especie_supressao
WHERE ide_especie_supressao=12474;
DELETE FROM especie_supressao
WHERE ide_especie_supressao=12473;
DELETE FROM nome_popular_especie
WHERE ide_nome_popular_especie=9945;
DELETE FROM nome_popular_especie
WHERE ide_nome_popular_especie=9944;
DELETE FROM nome_popular_especie
WHERE ide_nome_popular_especie=9943;
DELETE FROM nome_popular_especie
WHERE ide_nome_popular_especie=9942;
DELETE FROM nome_popular_especie
WHERE ide_nome_popular_especie=9941;

-- [35898] Alteração de Usuário
								
	update
		usuario
	set
		ide_perfil = 3
	where
		ide_pessoa_fisica = 445;
									
	update
		usuario
	set
		ide_perfil = 4
	where
		ide_pessoa_fisica = 2631;
	
	update
		controle_tramitacao
	set
		ind_fim_da_fila = false
	where
		ide_controle_tramitacao = 725504;
	
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
	values (58726,
	19,
	45,
	now(),
	true,
	null,
	'Solicitação de remanejamento de processo através do chamado Redmine 35898 ',
	247,
	false,
	445,
	null);
	
	update
		controle_tramitacao
	set
		ind_fim_da_fila = false
	where
		ide_controle_tramitacao = 728717;
	
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
	values (77937,
	19,
	45,
	now(),
	true,
	null,
	'Solicitação de remanejamento de processo através do chamado Redmine 35898 ',
	247,
	false,
	445,
	null);
	
	update
		controle_tramitacao
	set
		ind_fim_da_fila = false
	where
		ide_controle_tramitacao = 728768;
	
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
	values (80102,
	12,
	45,
	now(),
	true,
	null,
	'Solicitação de remanejamento de processo através do chamado Redmine 35898 ',
	247,
	false,
	445,
	null);
	
	update
		controle_tramitacao
	set
		ind_fim_da_fila = false
	where
		ide_controle_tramitacao = 727310;
	
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
	values (93545,
	19,
	45,
	now(),
	true,
	null,
	'Solicitação de remanejamento de processo através do chamado Redmine 35898 ',
	247,
	false,
	445,
	null);
	
	update
		controle_tramitacao
	set
		ind_fim_da_fila = false
	where
		ide_controle_tramitacao = 726499;
	
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
	values (96808,
	19,
	45,
	now(),
	true,
	null,
	'Solicitação de remanejamento de processo através do chamado Redmine 35898 ',
	247,
	false,
	445,
	null);
	
-- [35896] - Processo sumiu do sistema
	
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 689705;
		
--[35895] - Cadastro de espécies no SEIA
-- Nome científico: Parapiptadenia zehntner
-- Nome popular:  Angelim-amarelo

	UPDATE
		especie_supressao_nome_popular_especie
	SET
		ide_nome_popular_especie = 5147
	WHERE
		ide_especie_supressao = 12087;
		
--[35890] - Imóvel Rural sumiu do sistema

	INSERT
	        INTO
	        pessoa_imovel
	(ide_pessoa_imovel,
	        ide_pessoa,
	        ide_imovel,
	        dtc_criacao,
	        ind_excluido,
	        ide_tipo_vinculo_imovel,
	        dtc_exclusao,
	        ide_tipo_vinculo_pct,
	        dsc_tipo_vinculo_pct_outros)
	VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'),
	1183220,
	1097617,
	now(),
	FALSE,
	2,
	NULL,
	NULL,
	NULL);

-- [35879] - Pagamento REPFLOR

       UPDATE
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE
                ide_tramitacao_requerimento IN (1527273, 1531126, 1533323, 1498150);

-- [35917] Transferência de Pauta

	UPDATE
	        usuario
	SET
	        ide_perfil = 4
	WHERE
	        ide_pessoa_fisica = 1206568;
	        
	UPDATE
	        pauta
	SET
	        ide_tipo_pauta = 2,
	        ide_area = 44
	WHERE
	        ide_pessoa_fisica = 1206568;
	        
	UPDATE
	        area
	SET
	        ide_pessoa_fisica = 1206568
	WHERE
	        ide_area = 44;        
        
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728516;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84498, 8, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728086;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79893, 4, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727945;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86694, 4, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 702296;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84963, 12, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728076;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (66776, 4, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 726799;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (78211, 20, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727950;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91487, 4, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 724986;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (23450, 12, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 727325;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (66532, 12, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 728260;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (63405, 4, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 724949;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93921, 12, 44, now(), TRUE, NULL, ' Solicitação de remanejamento de processo através do chamado Redmine 35917', 2826, FALSE, 1, NULL);
      
-- [35930] - Mudança de perfil em usuário
	UPDATE
	        usuario
	SET
	        ide_perfil = 2,
	        ind_tipo_usuario = FALSE 
	WHERE
	        ide_pessoa_fisica = 9569;
        
-- [35927] - Pagamento REPFLOR

	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento IN (1602988, 1615768, 1614615, 1615281, 1578032, 1635541);
                
-- [35889] - Transferência de pauta

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 724949;
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
	VALUES (93921,
	12,
	38,
	now(),
	TRUE,
	NULL,
	'Solicitação de remanejamento de processo através do chamado Redmine 35889',
	211,
	FALSE,
	1,
	NULL);
	
-- [35595] Erro ao cadastrar Pessoa Física

-- Desativando o cadastro errado:

	UPDATE
		pessoa
	SET
		ind_excluido = TRUE
	WHERE
		ide_pessoa = 1194938;
	
	UPDATE
		usuario
	SET
		ind_excluido = TRUE
	WHERE
		ide_pessoa_fisica = 1194938;

-- Desvinculando o imóvel do cadastro errado:

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 1200663
	WHERE
		ide_imovel_rural = 1305296;
	
	UPDATE
		pessoa_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_pessoa_imovel = 1971328;
	
	INSERT INTO pessoa_imovel
	(ide_pessoa, ide_imovel, dtc_criacao, ind_excluido, ide_tipo_vinculo_imovel, dtc_exclusao, ide_tipo_vinculo_pct, dsc_tipo_vinculo_pct_outros)
	VALUES(1200663, 1305296, now(), FALSE, 2, NULL, NULL, NULL);

-- Inserindo a linha de tramitação da correção no "Histórico Alteração" do CEFIR:

	INSERT INTO hist_historico (ide_historico, data_historico, acao_historico, ip_historico, ide_usuario)
	VALUES (nextval('historico_ide_historico_seq'), now(), 'UPD', '0', 1);
	
	INSERT INTO hist_valor (val_valor, ide_campo, ide_registro, ide_historico)
	VALUES ('Correção do CPF para atendimento do ticket #35595', 279, 1305296, currval('historico_ide_historico_seq'));                
                
COMMIT;