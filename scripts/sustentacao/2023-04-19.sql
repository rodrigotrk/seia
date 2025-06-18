--Scripts em caráter prioritário
--Data de geração 05/04/2023
--Versão 4.27.3

BEGIN;

-- [35183] Retorno de status da RL

	UPDATE
		reserva_legal
	SET
		ide_status = 3
	WHERE
		ide_reserva_legal = 729640;
        
-- Inserindo a linha de tramitação no "Histórico Alteração" do CEFIR:
        
	INSERT
		INTO
		hist_historico (data_historico,
		acao_historico,
		ip_historico,
		ide_usuario)
	VALUES (now(),
		'UPD',
		'168.90.79.182',
		8527);
	
	INSERT
		INTO
		hist_valor (val_valor,
		ide_campo,
		ide_registro,
		ide_historico)
	VALUES (3,
		195,
		729640,
		currval('historico_ide_historico_seq'));
        
-- [35181] Retorno de status da RL
-- Imóvel 1: Fazenda Arrojado
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 14660;
                
INSERT
        INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
        'UPD',
        '168.90.79.118',
        8527);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
        195,
        14660,
        currval('historico_ide_historico_seq'));
        
-- Imóvel 2: Fazenda Dona Moça I
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 53132;
                
INSERT
        INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
        'UPD',
        '168.90.79.118',
        8527);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
        195,
        53132,
        currval('historico_ide_historico_seq'));
        
-- Imóvel 3: Fazenda Dona Moça II
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 53137;
                
INSERT
        INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
        'UPD',
        '168.90.79.118',
        8527);
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
        195,
        53137,
        currval('historico_ide_historico_seq'));
-- [35176] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 565894;
-- [34618] Erro ao salvar reserva legal. 

--Reserva
    UPDATE 
        cronograma_recuperacao
    SET 
        ide_reserva_legal= NULL, ide_documento_obrigatorio= NULL  
    WHERE 
        ide_cronograma_recuperacao=128628;

    UPDATE 
        reserva_legal
    SET
        ide_tipo_estado_conservacao=1
    WHERE
        ide_reserva_legal=12025;

--App
    UPDATE
        cronograma_recuperacao
    SET 
        ide_app= NULL, ide_documento_obrigatorio= NULL
    WHERE 
        ide_cronograma_recuperacao=128629;

    UPDATE 
        app
    SET 
        ide_tipo_estado_conservacao=1
    WHERE 
        ide_app=191878;
-- [35193] Processo sumiu do sistema 
    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = TRUE
    WHERE
        ide_controle_tramitacao = 614678;
        
-- [35199] Pagamento REPFLOR

UPDATE
	tramitacao_requerimento
SET
	ide_status_requerimento = 8
WHERE
	ide_tramitacao_requerimento = 1430462;        
--[35152] - erro ao finalizar cefir

UPDATE
	reserva_legal
SET
	ide_tipo_estado_conservacao = 1
WHERE
	ide_reserva_legal = 412640;

UPDATE
	cronograma_recuperacao
SET
	ide_reserva_legal = NULL,
	ide_localizacao_geografica = NULL
WHERE
	ide_cronograma_recuperacao = 127983;
-- [35083] - Erro Análise Técnica
UPDATE
    documento_ato 
SET 
    ind_ativo = TRUE
WHERE 
    ide_documento_ato = 7399;

-- [34827] Erro ao baixar termo de compromisso
 
-- Tirando o vínculo com o documento errado:
	
UPDATE
	documento_imovel_rural_posse
SET
	ide_documento_imovel_rural = NULL
WHERE
	ide_imovel_rural = 1217886 AND ide_documento_imovel_rural_posse = 1129306;
        
-- Zerando a RL:        
        
UPDATE
	reserva_legal
SET
	ide_imovel_rural = NULL
WHERE
	ide_reserva_legal = 888670;         
-- [34826] Erro ao Consultar Empreendimento

-- Tirando o vínculo com o documento errado:

	UPDATE
		documento_imovel_rural_posse
	SET
		ide_documento_imovel_rural = NULL
	WHERE
		ide_imovel_rural = 1217846 AND ide_documento_imovel_rural_posse = 1129268;

-- Atualizando os tipos de inserções dos shapes:
        
	UPDATE
		localizacao_geografica
	SET
		ide_classificacao_secao = 2
	WHERE
		ide_localizacao_geografica = 3478764;        
        
	UPDATE
		localizacao_geografica
	SET
		ide_classificacao_secao = 2
	WHERE
		ide_localizacao_geografica = 3478813;        
-- [34689] Notificação Não Respondida

-- Processo 1: 2018.001.004997/INEMA/LIC-04997

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 435874;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 436097;
        
-- Processo 2: 2021.001.004637/INEMA/LIC-04637

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 609467;
	
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
	VALUES (69966,
		8,
		76,
		now(),
		TRUE,
		NULL,
		NULL,
		1210,
		FALSE,
		NULL,
		NULL);        
        
-- [34666] Não exibição dos boletos complementares
CREATE  VIEW vw_daes_boletos AS 
SELECT
		NULL AS ide_boleto_pagamento_requerimento,
		bdr.ide_boleto_dae_requerimento, 
		NULL AS num_boleto,
		NULL AS dtc_vencimento,
		bdr.ide_requerimento, 
		bdr.ide_processo, 
		bdr.vlr_area_vistoria, 
		bdr.vlr_total_certificado, 		
		bdr.vlr_total_vistoria,
		FALSE AS ind_boleto_registrado,
		bdr.ide_tipo_boleto_pagamento,
		NULL AS dtc_pagamento,
		bdr.ind_isento, 
		(SELECT h.dtc_tramitacao FROM boleto_dae_historico h 
	 	WHERE h.ide_boleto_dae_requerimento=bdr.ide_boleto_dae_requerimento AND h.ide_status_boleto_pagamento =1 
	 	ORDER BY h.dtc_tramitacao DESC LIMIT 1) AS dtc_emissao,
	  compr.dtc_validacao,
	 n_comprovantes,
	0 as val_boleto, 
	
    0 as val_boleto_outorga, 	
	 (SELECT h.dtc_tramitacao FROM boleto_dae_historico h 
	 	WHERE h.ide_boleto_dae_requerimento=bdr.ide_boleto_dae_requerimento  AND h.ide_status_boleto_pagamento =5 
	 	ORDER BY h.dtc_tramitacao DESC LIMIT 1) AS dt_cancelamento,
	 
 	 (SELECT max(tr.ide_tramitacao_requerimento) FROM tramitacao_requerimento tr 
	 		WHERE tr.ide_requerimento=bdr.ide_requerimento) ide_max_tramitacao_requerimento,
	 		NULL AS ide_max_boleto_pagamento_historico,
	 ide_max_comprovante_pagamento_dae
	 		
 FROM  boleto_dae_requerimento bdr 
 	LEFT JOIN (SELECT count(DISTINCT comp.dsc_caminho_arquivo) n_comprovantes, max(comp.ide_comprovante_pagamento_dae) ide_max_comprovante_pagamento_dae, 
 				max(comp.dtc_validacao) dtc_validacao, comp.ide_boleto_dae_requerimento 
		 		FROM comprovante_pagamento_dae comp WHERE comp.dtc_validacao IS NOT NULL
					GROUP BY comp.ide_boleto_dae_requerimento) compr ON compr.ide_boleto_dae_requerimento=bdr.ide_boleto_dae_requerimento
					
UNION 
					
SELECT 
	bpr.ide_boleto_pagamento_requerimento,
	NULL AS ide_boleto_dae_requerimento,
    
	bpr.num_boleto,
   
    bpr.dtc_vencimento,
    
    COALESCE (bpr.ide_requerimento,p.ide_requerimento) ide_requerimento, 
    
    bpr.ide_processo,
    
    0.0 AS vlr_area_vistoria,
    
    0.0 AS valorTotalCertificado, 
     
    0.0 AS valorTotalVistoria, 
    
    bpr.ind_boleto_registrado, 
	
    bpr.ide_tipo_boleto_pagamento,
    
    bpr.dtc_pagamento,
	
    bpr.ind_isento, 
    
    bpr.dtc_emissao, 
    
    (select max(bph5.dtc_tramitacao) from boleto_pagamento_historico bph5 
    		where bpr.ide_boleto_pagamento_requerimento = bph5.ide_boleto_pagamento 
             and bph5.ide_status_boleto_pagamento = 4) AS dtc_validacao,
    
    0 AS n_comprovantes,
    
    coalesce(bpr.val_boleto, 0) as val_boleto, 
	
    coalesce(bpr.val_boleto_outorga, 0) as val_boleto_outorga, 			

	
   (select max(bph4.dtc_tramitacao) from boleto_pagamento_historico bph4 
    		where bpr.ide_boleto_pagamento_requerimento = bph4.ide_boleto_pagamento 
             and  ( bph4.ide_status_boleto_pagamento = 5)) as dt_cancelamento,

 	(SELECT max(tr.ide_tramitacao_requerimento) FROM tramitacao_requerimento tr 
		WHERE tr.ide_requerimento=COALESCE (bpr.ide_requerimento,p.ide_requerimento)) ide_max_tramitacao_requerimento,
				 
             
    (select MAX(ide_boleto_pagamento_historico) FROM boleto_pagamento_historico bph3 
			WHERE bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento) as	 ide_max_boleto_pagamento_historico,
  	NULL AS ide_max_comprovante_pagamento_dae
  	

		FROM boleto_pagamento_requerimento bpr 
			LEFT JOIN processo p ON p.ide_processo =bpr.ide_processo;
COMMIT;