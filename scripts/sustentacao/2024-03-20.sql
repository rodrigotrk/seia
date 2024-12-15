--Scripts em caráter prioritário
--Data de geração 08/03/2024
--Versão 4.30.7

BEGIN;

-- [36097] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1530073);
    
-- [36092] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1657832, 1674965, 1665860, 1670313, 1672041, 1670334, 1673532, 1292210);
    
-- [30047] Alteração no volume máximo acumulado

	ALTER TABLE fce_barragem
		ALTER COLUMN val_volume_maximo_acumulado TYPE NUMERIC(11,2);
	
	UPDATE
		fce_barragem
	SET
		val_volume_maximo_acumulado = 999999999.99
	WHERE
		val_volume_maximo_acumulado = 99999999.99;

--[36082] - Transferência de pauta
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 746605;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
24358,
19,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine #36082',
205,
FALSE,
1,
NULL);

-- [36067] - Transferência de Titularidade
UPDATE
        imovel_rural
SET
        status_cadastro = 3
WHERE
        ide_imovel_rural = 418999;
        
-- [36076] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 744268;
		
-- [35943] Vinculação de Substituto no Grupo de Acesso a Pauta do Gestor

	UPDATE
	        pauta
	SET
	        ide_tipo_pauta = 2
	WHERE
	        ide_pauta = 224;
	        
-- [35740] - Erro no FCE Irrigação

UPDATE
	outorga_localizacao_geografica_finalidade
SET
	ind_excluido = TRUE,
	ind_captacao = FALSE
WHERE
	ide_outorga_localizacao_geografica_finalidade = 78560;

UPDATE
	fce_atividade_area
SET
	num_area = 630.00
WHERE
	ide_fce_atividade_area = 21550;

COMMIT;