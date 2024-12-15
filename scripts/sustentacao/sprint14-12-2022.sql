--Scripts em caráter prioritário
--Data de geração 30/11/2022
--Versão 4.26.0

BEGIN;

--[34794] Transferência de Titularidade 
    UPDATE
        requerimento_pessoa
    SET
        ide_pessoa = 10937
    WHERE
        ide_requerimento = 904275
        AND ide_pessoa = 1420;
       
    UPDATE
        requerimento
    SET
        nom_contato = 'PETRORECONCAVO S.A'
    WHERE
        ide_requerimento = 904275;

-- [34791] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 598611;
-- [34776] Emissão de Certificado de Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
        ide_tramitacao_requerimento = 1329571;

-- [34775] Emissão de Certificado de Pagamento REPFLOR
    UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1329578;
        
-- [34766] Emissão de Certificado de Pagamento REPFLOR
	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1349309;

-- [34757] Emissão de Certificado de Pagamento REPFLOR		
    UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1370998;
		
-- [34641] Processo duplicado
    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = FALSE
    WHERE
        ide_controle_tramitacao IN (585465, 586460);

    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = TRUE
    WHERE
        ide_controle_tramitacao = 586013;
-- [34805] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 585124;	
        
-- [34803] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 571544;       
        
 -- [34782] Retorno de Status
	UPDATE
	    reserva_legal
	SET
	    ide_imovel_rural = NULL
	WHERE
	    ide_reserva_legal IN (22846,4545);
-- [34613] - erro ao concultar
    UPDATE 
        equerimento_pessoa
	SET 
        ide_tipo_pessoa_requerimento=1
	WHERE 
        ide_requerimento=1158515 AND ide_pessoa=831238 AND ide_tipo_pessoa_requerimento=2;

COMMIT;