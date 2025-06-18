--Scripts em caráter prioritário
--Data de geração 10/01/2024
--Versão 4.30.2

BEGIN;
-- [35926] - Transferência de Titularidade

update
	requerimento
set
	nom_contato = 'ADELMO ROCHA DE OLIVEIRA'
where
	ide_requerimento = 1091062;



update
	requerimento_pessoa
set
	ide_pessoa = 1011047
where
	ide_requerimento = 1091062
	and ide_tipo_pessoa_requerimento = 1;

-- [35941] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 686189;
		
-- [35935] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 687276;		
		
-- [35931] - Alteração de Usuário 
        UPDATE
                area
        SET 
                ide_pessoa_fisica = 18604
        WHERE
                ide_area = 11;
        UPDATE
                usuario
        SET
                ide_perfil = 4
        WHERE
                ide_pessoa_fisica = 18604;
        UPDATE
                pauta
        SET
                ide_tipo_pauta = 2,
                ide_area = 11
        WHERE
                ide_pauta = 806;
                
-- [35907] - Erro ao arquivar notificação

	UPDATE
	        notificacao
	SET
	        ind_enviado_aprovacao = FALSE
	WHERE
	        num_notificacao IS NULL
	        AND ind_enviado_aprovacao = TRUE
	        AND ide_processo IN (11497, 23981, 75953, 84327);
	        
-- [35729] - Remoção de responsáveis técnicos

	UPDATE
		responsavel_imovel_rural
	SET
		ind_excluido = TRUE,
		dtc_exclusao = now()
	WHERE
		ide_imovel_rural IN
	(
		SELECT
			rir.ide_imovel_rural
		FROM
			responsavel_imovel_rural rir
		INNER JOIN imovel_rural ir ON
			ir.ide_imovel_rural = rir.ide_imovel_rural
		INNER JOIN reserva_legal rl ON
			rl.ide_imovel_rural = ir.ide_imovel_rural
		WHERE
			ir.status_cadastro = 3
			AND rl.ide_status = 3
			AND rir.ide_pessoa_fisica IN (1161486, 1161484)
				AND rir.ind_excluido IS FALSE)
	AND ide_pessoa_fisica IN (1161486, 1161484) 
	OR 
		ide_imovel_rural IN 
	(
		SELECT
			rir.ide_imovel_rural
		FROM
			responsavel_imovel_rural rir
		INNER JOIN assentado_incra_imovel_rural aiir ON 
			aiir.ide_imovel_rural = rir.ide_imovel_rural
		WHERE
			 rir.ide_pessoa_fisica IN (1161486, 1161484)
				AND rir.ind_excluido IS FALSE)
	AND ide_pessoa_fisica IN (1161486, 1161484);
	
-- [35973] - Alteração de usuário

	UPDATE
	        usuario
	SET
	        ide_perfil = 2, ind_tipo_usuario = FALSE
	WHERE
	        ide_pessoa_fisica = 5067;

-- [35969] - Processo formado sem pauta

	UPDATE
	        controle_tramitacao
	SET
	        ide_pauta = 756
	WHERE
	        ide_controle_tramitacao = 61475;
	        
-- [35929] - Sistema SEIA e CERBERUS
-- Tirando ind_substituto para o usuário de Marcelo
UPDATE public.funcionalidade_acao_pessoa_fisica_pauta
SET ind_substituto=false
WHERE ide_funcionalidade_acao_pessoa_fisica=1512 AND ide_pauta=344;
-- Trocando pauta de Marcelo para Técnico
UPDATE public.pauta
SET ide_tipo_pauta=3
WHERE ide_pauta=2180;
-- Trocando pauta de Ana Paula para Coordenadora
UPDATE public.pauta
SET ide_tipo_pauta=2
WHERE ide_pauta=247;
--Tirando vínculo registrado da pauta da área sobre o usuário de Marcelo
DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
WHERE ide_funcionalidade_acao_pessoa_fisica=1517 AND ide_pauta=115;
DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
WHERE ide_funcionalidade_acao_pessoa_fisica=1516 AND ide_pauta=115;
DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
WHERE ide_funcionalidade_acao_pessoa_fisica=1515 AND ide_pauta=115;
DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
WHERE ide_funcionalidade_acao_pessoa_fisica=1514 AND ide_pauta=115;
DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
WHERE ide_funcionalidade_acao_pessoa_fisica=1513 AND ide_pauta=115;

--Remanejamento processos Marcelo para Ana Paula
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734309;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (84899, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732621;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (82469, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732666;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93577, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732511;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (89709, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 729919;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88927, 19, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 730913;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (75241, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 733232;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (87295, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 733198;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (92972, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 733671;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (86410, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 733570;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (93986, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 731433;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95005, 19, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 730772;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95937, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732820;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (63573, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732372;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (89235, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734157;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (94611, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734234;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (92689, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 731556;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (50931, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734379;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (98677, 19, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 730985;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (95846, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 732863;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (79015, 4, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734709;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (81486, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);
UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao = 734637;                                                          
INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES (88531, 19, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35929', 247, FALSE, 1, NULL);

-- [35944] Erro ao realizar análise técnica

	INSERT INTO fce
	(ide_fce, dtc_criacao, ide_requerimento, ide_documento_obrigatorio, ide_origem_fce, ind_concluido, ide_analise_tecnica, ide_pessoa_fisica, ide_processo_reenquadramento)
	VALUES(nextval('fce_ide_fce_seq'), now(), 1201504, 1444, 2, NULL, 28236, NULL, NULL);
	
	UPDATE
		fce_aquicultura
	SET
		ide_fce = currval('fce_ide_fce_seq')
	WHERE
		ide_fce_aquicultura = 331;	
		
		
-- [35998] - Processo Sumido

	UPDATE
        controle_tramitacao
	SET
        ind_fim_da_fila = TRUE
	WHERE
        ide_controle_tramitacao = 733272;
       
	UPDATE
        controle_tramitacao
	SET
        ind_fim_da_fila = TRUE
	WHERE
        ide_controle_tramitacao = 733271;
	
COMMIT;