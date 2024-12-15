BEGIN;

--[34379]-[PROCESSO] Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (557760,557761);
--[34374]-[PROCESSO] Processo sumiu do sistema
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = true
        WHERE
                ide_controle_tramitacao = 563643;

--[34346] - [PROCESSO] Transferência de Titularidade    
    UPDATE
        requerimento_pessoa
    SET
        ide_pessoa = 10937
    WHERE
        ide_requerimento = 904237
        AND ide_pessoa = 1420;
        
    UPDATE
        requerimento
    SET
        nom_contato = 'PETRORECONCAVO S.A'
    WHERE
        ide_requerimento = 904237;

--[34355]-[REQUERIMENTO] Requerimento sem número 

UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2022.001.2' AS VARCHAR) || 
                CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
                CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1150209 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
        ide_requerimento = 1150209;

--[34379]-[PROCESSO] Processo sumiu do sistema

	UPDATE 
	        controle_tramitacao
	SET 
	        ind_fim_da_fila = TRUE 
	WHERE 
	        ide_controle_tramitacao IN (557760,557761);

--[34374]-[PROCESSO] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = true
	WHERE
		ide_controle_tramitacao = 563643;
	
--[34379]-[PROCESSO] Processo sumiu do sistema

	UPDATE 
	        controle_tramitacao
	SET 
	        ind_fim_da_fila = TRUE  
	WHERE 
	        ide_controle_tramitacao IN (557760,557761);	
	
--[34382]-Processo sumiu do sistema

	UPDATE 
	        controle_tramitacao
	SET 
	        ind_fim_da_fila = TRUE  
	WHERE 
	        ide_controle_tramitacao =563643;

COMMIT;

--[33620] - Ocorreu erro ao salvar.

UPDATE
	public.reserva_legal
SET
	ide_imovel_rural = 319048,
	ide_localizacao_geografica = 873145,
	ide_tipo_arl = 2,
	ide_tipo_estado_conservacao = 1,
	ide_tipo_origem_certificado = NULL,
	val_area = 4.3110,
	ind_processo_tramite = FALSE,
	num_processo = NULL,
	num_certificado = NULL,
	ide_documento_aprovacao = NULL,
	ind_averbada = FALSE,
	ind_sobreposicao_app = FALSE,
	ind_sobreposicao_ap = FALSE,
	ide_status = 3,
	ide_usuario_aprovacao = NULL,
	dtc_aprovacao = NULL,
	observacao = NULL,
	ind_menor_vinte_porcento = TRUE,
	num_sicar_compensacao = NULL,
	dtc_aprovacao_declarada = NULL,
	ide_dado_origem = NULL,
	ide_notificacao = NULL,
	ide_processo_ato = NULL,
	ide_reserva_legal_pai = NULL,
	ide_forma_realocacao_rl = NULL,
	dsc_observacao_alteracao_shape = NULL,
	ind_deseja_cad_prad = NULL,
	dtc_resp_deseja_cad_prad = NULL,
	ind_excluido = NULL
WHERE
	ide_reserva_legal = 201584;
	

UPDATE
	public.cronograma_recuperacao
SET
	ide_app = NULL,
	ide_reserva_legal = NULL,
	sts_aceite = TRUE,
	ide_documento_obrigatorio = NULL,
	dtc_criacao = '2017-07-14 17:42:15.815',
	ind_prad_importada = NULL,
	ide_outros_passivos_ambientais = NULL,
	ide_localizacao_geografica = NULL,
	val_area = NULL,
	dtc_criacao_localizacao = NULL
WHERE
	ide_cronograma_recuperacao = 50756;

--[34096] - Erro no questionário da Reserva Legal/CEFIR.
        UPDATE
                reserva_legal
        SET
                ide_status = 3
        WHERE
                ide_reserva_legal = 846840;
        
--[34382] - Processo sumiu do sistema.
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao in (557754,557755);

--[34396] - Processo sumiu do sistema.
        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = TRUE
        WHERE
            ide_controle_tramitacao = 547936;
        
--[34411] - Processo sumiu do sistema.

	UPDATE 
	   public.controle_tramitacao
	SET
	   ind_fim_da_fila=TRUE 
	WHERE
	   ide_controle_tramitacao=566228;

--[34414] - O CEP informado não pertence ao município do seu cadastro.
--FAZENDA QUIXABEIRA

        UPDATE 
                logradouro 
        SET 
                num_cep = '48400000', ide_municipio = 570
        WHERE 
                ide_logradouro = 3651120;
        
--FAZENDA QUIXABEIRA II

        UPDATE 
                logradouro 
        SET 
                num_cep = '48400000', ide_municipio = 570
        WHERE 
                ide_logradouro = 3633715;
        
--FAZENDA QUIXABEIRA III

    UPDATE 
        logradouro 
    SET 
        num_cep = '48400000', ide_municipio = 570
    WHERE 
        ide_logradouro = 3651144;

--[34420]-Processo sumiu do sistema

	UPDATE 
	     controle_tramitacao
	SET 
	     ind_fim_da_fila = TRUE 
	WHERE 
	     ide_controle_tramitacao=561054;

--[34406] - [CADASTRO] Imóvel Rural sumiu do sistema (TROCA DE TITULARIDADE)

INSERT INTO public.pessoa_imovel
(ide_pessoa, ide_imovel, dtc_criacao, ind_excluido, ide_tipo_vinculo_imovel, dtc_exclusao, ide_tipo_vinculo_pct, dsc_tipo_vinculo_pct_outros)
VALUES(1072528,628546, now(), false, 1, NULL, NULL, NULL);

	UPDATE
         imovel_rural
    SET
         ide_requerente_cadastro = 1072528
    WHERE
         ide_imovel_rural = 628546;

        UPDATE 
                logradouro 
        SET 
                num_cep = '48400000', ide_municipio = 570
        WHERE 
                ide_logradouro = 3651144;
    
--[34410] - [REQUERIMENTO] Erro ao inserir dados
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_licenca = 49063;
	
--[34421]-Processo sumiu do sistema

	UPDATE 
	     controle_tramitacao
	SET 
	     ind_fim_da_fila = TRUE 
	WHERE     
        ide_controle_tramitacao IN (566777,566778);

COMMIT;

--[34355]-[REQUERIMENTO] Requerimento sem número 
        UPDATE
                requerimento
        SET
                num_requerimento = (
                SELECT
                        CAST('2022.001.2' AS VARCHAR) || 
                        CAST((CAST(split_part(split_part((
                                SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
                        CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
                dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1150209 ORDER BY dtc_movimentacao DESC LIMIT 1)
        WHERE
                ide_requerimento = 1150209;

--[34421]-Processo sumiu do sistema
--[34406] - [CADASTRO] Imóvel Rural sumiu do sistema (TROCA DE TITULARIDADE)

INSERT INTO public.pessoa_imovel
(ide_pessoa, ide_imovel, dtc_criacao, ind_excluido, ide_tipo_vinculo_imovel, dtc_exclusao, ide_tipo_vinculo_pct, dsc_tipo_vinculo_pct_outros)
VALUES(1072528,628546, now(), false, 1, NULL, NULL, NULL);

	UPDATE
         imovel_rural
    SET
         ide_requerente_cadastro = 1072528
    WHERE
         ide_imovel_rural = 628546;


UPDATE 
        controle_tramitacao
SET 
        ind_fim_da_fila = TRUE 
WHERE 
        ide_controle_tramitacao IN (566777,566778);
COMMIT;

