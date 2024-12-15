--Scripts em caráter prioritário
--Data de geração 14/06/2023
--Versão 4.27.9

BEGIN;
-- [35392] Retorno de status da RL
        UPDATE
                reserva_legal
        SET
                ide_status = 4
        WHERE
                ide_reserva_legal = 18141;
                 
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

        INSERT
                INTO
                hist_valor (val_valor,
                ide_campo,
                ide_registro,
                ide_historico)
        VALUES 
        (4,
        195,
        18141,
        currval('historico_ide_historico_seq'));
        
-- [35363] Imóvel rural sumiu do sistema 
	
	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1930730;

-- [35360] Imóvel rural sumiu do sistema 

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1801148;

-- [35351] Imóvel rural sumiu do sistema 

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1806770;

COMMIT;