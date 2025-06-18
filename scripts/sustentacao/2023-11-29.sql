--Scripts em caráter prioritário
--Data de geração 16/11/2023
--Versão 4.29.5

BEGIN;

-- [35764] Transferência de Pauta (NOUT) e Alteração de Usuário
-- PATRICIA VIRARÁ TÉCNICA
	UPDATE
	        usuario
	SET
	        ide_perfil = 3
	WHERE
	        ide_pessoa_fisica = 2789;
	        
-- ATUALIZANDO A PAUTA DE PATRÍCIA PARA O TIPO PAUTA TÉCNICA
	UPDATE
	        pauta
	SET
	        ide_tipo_pauta = 3
	WHERE
	        ide_pauta = 292;
        
-- ALTAMIRANO VIRARÁ COORDENADOR
	UPDATE
	        usuario
	SET
	        ide_perfil = 4
	WHERE
	        ide_pessoa_fisica = 144783;
        
-- POPULA A TABELA FUNCIONÁRIO QUE REGULA AS OUTRAS TABELAS DE PAUTA E ÁREA
	INSERT INTO funcionario (ide_pessoa_fisica, ide_area, matricula, ind_excluido)
	VALUES(144783, 76, '', FALSE);
	INSERT INTO pauta (ide_pauta, ide_tipo_pauta, ide_area, ide_pessoa_fisica)
	VALUES(nextval('PAUTA_IDE_PAUTA_seq'), 2, 76, 144783);

-- VIRARÁ COORDENADOR DA ÁREA DE NOUT
	UPDATE
	        area
	SET
	        ide_pessoa_fisica = 144783
	WHERE
	        ide_area = 76;
       
-- DESATIVANDO TATYANE COMO SUBSTITUTA
	UPDATE
	        funcionalidade_acao_pessoa_fisica_pauta
	SET
	        ind_substituto = FALSE
	WHERE
	        ide_funcionalidade_acao_pessoa_fisica = 1583;
        
-- TRANSFERÊNCIAS DE PROCESSO
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 682569;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71478, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697992;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95482, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697994;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95221, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698944;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (67796, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698401;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80796, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 93775;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (10377, 9, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691169;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75114, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698410;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83555, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 666290;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (69314, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699488;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95950, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699489;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95966, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699490;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95967, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697981;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81543, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 687701;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (60464, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 453412;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (49687, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 578441;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65395, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 577971;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65392, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 679705;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65546, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 585532;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80732, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 585533;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81062, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698910;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94491, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693726;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94576, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 695825;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95751, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 492303;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (49382, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 505956;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (57312, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697104;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90838, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699491;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95985, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699492;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95673, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699493;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95949, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699494;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95764, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699495;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95773, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 584027;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76010, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 584029;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75953, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 579075;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (63831, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 685275;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (50531, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 685277;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (51870, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698915;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93942, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686706;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88408, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686707;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80301, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 608734;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (77988, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697512;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91395, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 583618;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (67885, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693708;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92803, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 573591;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65878, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693739;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94529, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 576169;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74987, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698887;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94017, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 578442;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (65291, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692220;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94888, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692222;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94889, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698890;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93981, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 572682;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75705, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 540464;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (70824, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 519468;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72624, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 505258;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (57382, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 381487;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (40130, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 348000;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (23981, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 329092;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (11497, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 540485;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71593, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698895;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93522, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 584240;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (64975, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 683478;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88347, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 582359;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73789, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699390;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (68267, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699392;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94348, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699395;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95857, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692709;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75187, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 644352;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (26858, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699402;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96586, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699403;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96589, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699404;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96289, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697935;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92185, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699409;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95669, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699413;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96449, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699347;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93263, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 678580;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79237, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 606491;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79591, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699349;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94058, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 616264;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81284, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692692;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88030, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699369;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91471, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697361;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90248, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 696988;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91897, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 668296;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84327, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692710;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72867, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692711;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83485, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698298;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71918, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 690872;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72425, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 689132;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72308, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 689147;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85423, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691852;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88597, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 689155;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85598, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698840;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90995, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694179;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76537, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699332;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (96437, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698813;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95184, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698816;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92504, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698793;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90834, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698238;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92467, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 689101;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81177, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694180;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92899, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694181;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (60182, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694182;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92842, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694183;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85111, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694184;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75870, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694185;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91286, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694186;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74808, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694187;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72981, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694190;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92357, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698803;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88898, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694191;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85160, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694192;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88490, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698203;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91380, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698205;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91035, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694194;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86341, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 663361;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (63933, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 696932;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72766, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 696934;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (70575, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694195;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86656, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699318;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94046, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697858;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76978, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694198;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80884, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697866;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93609, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 679959;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72837, 2, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698780;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76164, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686601;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84140, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694160;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93523, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694161;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94084, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699280;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92432, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694164;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94083, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694165;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94070, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699485;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (66130, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 679492;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90510, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694168;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82454, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694171;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (87634, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691435;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88994, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694173;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93055, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694174;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92667, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 664507;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73985, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694176;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88951, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692128;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88829, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691460;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93993, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 673989;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79596, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692544;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84847, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699261;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94494, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 672538;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79075, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 672550;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (68856, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 672530;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79663, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 696019;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91246, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691748;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85704, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699219;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92527, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691749;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (66476, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691753;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82388, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691752;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83034, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691757;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81460, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691758;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (79379, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 685090;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (50532, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 694528;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (71248, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698716;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95799, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 683305;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (63279, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 681243;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80099, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691762;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85346, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691766;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85054, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697302;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91886, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697303;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90976, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697304;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91038, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697305;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91036, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697306;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91209, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697307;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91405, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697308;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90974, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691768;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75378, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691769;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83864, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691770;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86714, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691771;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83165, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686483;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (70173, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 683734;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83790, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699192;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94763, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686490;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (69949, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 684634;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (68675, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698692;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95672, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692514;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80302, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 686322;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88649, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699199;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90813, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699202;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94733, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692516;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83626, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691738;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76941, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691739;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76557, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691740;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83167, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691742;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82387, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691743;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83697, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691744;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (76487, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 687785;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86932, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 688286;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (66559, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692518;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80328, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692519;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75340, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692520;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (80049, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692522;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (81244, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692524;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (77444, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692525;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83169, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692527;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (83304, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699161;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92493, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691722;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (77345, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692045;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91171, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 695596;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93639, 20, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697687;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92188, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693483;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (86956, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699092;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73123, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699169;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92492, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 690228;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93196, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699113;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (51616, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699164;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92350, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692944;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (70198, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699159;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95885, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698661;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72226, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692084;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93264, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699060;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90747, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698579;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91490, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692943;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93862, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691710;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (74201, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691347;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90875, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699158;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95884, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691717;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (63865, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699156;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92402, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697301;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91207, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699027;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94799, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699093;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90613, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699160;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92489, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691703;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75084, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698676;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91301, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698663;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92164, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699125;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73122, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691711;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (68771, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693488;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93839, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693510;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95007, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699162;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92490, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699168;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95968, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693436;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93530, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698662;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92166, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691713;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (67676, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699038;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (95028, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691716;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (78148, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699170;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92491, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698644;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (72143, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692528;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (73171, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698621;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (94771, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 692053;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92811, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691721;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (88417, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691712;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82301, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691720;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (69315, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 698664;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92162, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699068;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85638, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693474;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (85057, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 695658;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93695, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 699163;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (92401, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691699;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (75085, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 693443;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93543, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 691312;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (91230, 19, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 41434, NULL);
	UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 697670;                                                          
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (93934, 6, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35764', currval('PAUTA_IDE_PAUTA_seq'), FALSE, 2789, NULL);

-- [35735] - Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 696042;
	
--[35731] - Retorno de status da RL
	
	UPDATE
	public.reserva_legal
	SET
	ide_status = 3
	WHERE
	ide_reserva_legal IN (505026, 804973);

-- [35713] - Erro ao realizar análise Técnica

	INSERT
		INTO
		motivo_notificacao_imovel (ide_motivo_notificacao_imovel,
		ide_notificacao_motivo_notificacao,
		ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'),
	87084,
	913235);
	
	UPDATE
		arquivo_processo
	SET
		ide_imovel = 913235
	WHERE
		ide_arquivo_processo = 256060;
		
--[35729] - Transferência de pauta
-- Retirando os velhos do fim da fila
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_processo IN (80957,
	71283,
	86341,
	94555,
	85160,
	92357,
	72981,
	91286,
	85111,
	92842,
	60182,
	88951);
--Adicionando os novos
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 73005, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, NULL, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 73014, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, NULL, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 93839, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 83751, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 131505, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 80113, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, NULL, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 92899, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 76537, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 85448, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 1166004, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 85377, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 1166004, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 85107, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 1166004, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 72637, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 978761, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 78119, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 131505, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 88999, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 19476, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94008, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 1074462, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 93934, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 20369, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 80957, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 19476, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 71283, 12, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 51972, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 86341, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94555, 19, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 980982, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 85160, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 92357, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 72981, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 91286, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 85111, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 92842, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, false, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 60182, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 88951, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 92667, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 93055, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 87634, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 80884, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 86656, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94070, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94083, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94084, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 93523, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 77169, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2789, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 88490, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2795, NULL);
	INSERT INTO public.controle_tramitacao
	(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 94626, 6, 76, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35785', 2881, true, 2789, NULL);
	
			
COMMIT;