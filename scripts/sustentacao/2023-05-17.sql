--Scripts em caráter prioritário
--Data de geração 10/05/2023
--Versão 4.27.6

BEGIN;

 -- [35256] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 11491;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			11491,
			currval('historico_ide_historico_seq'));
			
 -- [35257] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 355205;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			355205,
			currval('historico_ide_historico_seq'));
			
 -- [35258] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 355110;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			355110,
			currval('historico_ide_historico_seq'));

 -- [35259] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 354836;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			354836,
			currval('historico_ide_historico_seq'));

 -- [35260] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 11371;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			11371,
			currval('historico_ide_historico_seq'));

 -- [35261] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 355242;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			355242,
			currval('historico_ide_historico_seq'));

 -- [35262] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 129301;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			129301,
			currval('historico_ide_historico_seq'));

 -- [35263] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 354846;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			354846,
			currval('historico_ide_historico_seq'));

 -- [35264] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 11452;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			11452,
			currval('historico_ide_historico_seq'));

 -- [35265] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal = 11317;

-- HIST_HISTORICO
	INSERT
		INTO
			hist_historico (data_historico,
			acao_historico,
			ip_historico,
			ide_usuario)
		VALUES
			(now(),
			'UPD',
			'138.97.188.56',
			11446);

-- HIST_VALOR
	INSERT
		INTO
			hist_valor (val_valor,
			ide_campo,
			ide_registro,
			ide_historico)
		VALUES 
			(4,
			195,
			11317,
			currval('historico_ide_historico_seq'));
			
--[35247] - Pagamento REPFLOR

UPDATE 
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento = 1483340;
-- [35250] - Processo sumiu do sistema

UPDATE 
    controle_tramitacao
SET 
    ind_fim_da_fila = TRUE 
WHERE 
    ide_controle_tramitacao = 582684;


COMMIT;