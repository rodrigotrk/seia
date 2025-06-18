--[33040] - Erro ao emitir notificação de prazo
BEGIN
UPDATE
	public.florestal
SET
	ide_imovel = 31628
WHERE
	ide_florestal = 9901;
COMMIT;

-- [34033] - Correção DTRPs
BEGIN;

DELETE
FROM
	declaracao_transporte_gerador_residuo
WHERE
	ide_declaracao_transporte = 5729;

DELETE
FROM
	declaracao_transporte
WHERE
	ide_declaracao_transporte = 5729;

DELETE
FROM
	declaracao_transporte_destinatario_residuo
WHERE
	ide_declaracao_transporte_destinatario_residuo = 4215;

DELETE
FROM
	declaracao_transporte_destinatario_residuo
WHERE
	ide_declaracao_transporte_destinatario_residuo = 3934;

COMMIT;

--[33949] - Erro ao selecionar empreendimento.
BEGIN
UPDATE
	public.imovel_empreendimento
SET
	dtc_associacao = '2022-05-25 12:48:10.219'
WHERE
	ide_imovel = 860615
	AND ide_empreendimento = 136258;
COMMIT;


--[33751] - Erro ao tentar finalizar cadastro de Imóvel Rural
BEGIN;

	UPDATE
		cronograma_recuperacao
	SET
		ide_localizacao_geografica = 1016482
	WHERE
		ide_cronograma_recuperacao = 60120;

COMMIT;
--[34089] - Erro ao inserir CEP no Empreendimento
 
BEGIN;

UPDATE
	bairro
SET
	ide_municipio = 744
WHERE
	ide_bairro = 1210;

UPDATE
	bairro
SET
	ind_origem_correio = FALSE
WHERE
	ide_bairro = 1210;

UPDATE
	bairro
SET
	ide_municipio = 744
WHERE
	ide_bairro = 1274;

UPDATE
	bairro
SET
	ind_origem_correio = FALSE
WHERE
	ide_bairro = 1274;

UPDATE
	bairro
SET
	ide_municipio = 744
WHERE
	ide_bairro = 1483;

UPDATE
	bairro
SET
	ind_origem_correio = FALSE
WHERE
	ide_bairro = 1483;
	
END;	
	
--[34091] - [REQUERIMENTO] Emissão do Certificado de Pagamento REPFLOR
BEGIN;
        UPDATE 
                tramitacao_requerimento 
        SET  
                ide_status_requerimento = 8 
        WHERE  
                ide_tramitacao_requerimento = 1279344;
COMMIT;
END;

--[34088] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 549727;
        COMMIT;
END;

--[34060] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 549727;

	COMMIT;
END;

--[34087] - Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 360168;

--[34086] - Erro ao consultar Requerimento
BEGIN;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_requerimento = 1142569;
 COMMIT;
END;

--[34084] - Erro ao consultar Requerimento

BEGIN;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_requerimento = 1146340;
        
        COMMIT;
END;

--34083 - Processo sumiu do sistema
BEGIN;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 549968;

COMMIT;  

--34082 - [PROCESSO] Processo sumiu do sistema
BEGIN;
	UPDATE
		        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 539398;
COMMIT;  

--[34053] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 548022;
COMMIT;  

--34068 - [CADASTRO] Imóvel Rural sumiu do sistema (Troca de Titularidade)

BEGIN;

	UPDATE
		imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_imovel = 29516;

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_pessoa_imovel = 27438;

	UPDATE
		pessoa_imovel
	SET
		ide_pessoa = 880507
	WHERE
		ide_pessoa_imovel = 27437;

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 880507
	WHERE
		ide_imovel_rural = 29516;

COMMIT;

END;

--[34060] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 546143;

--[34057] - Solicitação Retorno de status do CEFIR

UPDATE
    reserva_legal
SET
    ide_status = 3
WHERE
    ide_reserva_legal = 26542;
INSERT
    INTO
    hist_historico (data_historico,
    acao_historico,
    ip_historico,
    ide_usuario)
VALUES
(now(),
'UPD',
'172.16.4.160',
9154);
INSERT
    INTO
    hist_valor (val_valor,
    ide_campo,
    ide_registro,
    ide_historico)
VALUES 
(3,
195,
26542,
currval('historico_ide_historico_seq'));

--[34056] - Processo sumiu do sistema
BEGIN;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 533258;
COMMIT;

--[34053] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 548022;
--[34052] - Solicitação para alteração de usuário interno para externo

BEGIN
	UPDATE
		usuario 
	SET
		ide_perfil = 2
	WHERE
		ide_pessoa_fisica = 63072;
COMMIT;

	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE
	WHERE
		ide_pessoa_fisica = 63072;
--[34051] - Processo sumiu do sistema
BEGIN;

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
                ide_controle_tramitacao = 538609;
--[34048] - Processo sumiu
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 548235;

--[34045] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 504332;
COMMIT;
--33978 - Erro ao consultar Requerimento.

BEGIN;

	UPDATE
		licenca
	SET
		ind_excluido = FALSE
	WHERE
		ide_licenca = 47583;
-- [33900] - Correção documentação de barragem de FCE
BEGIN;

INSERT INTO documento_obrigatorio_requerimento
(ide_requerimento, ide_documento_obrigatorio, dsc_caminho_arquivo, ind_documento_validado, ind_sigiloso) 
select
1129118, 1437, '/opt/ARQUIVOS/DOCUMENTOOBRIGATORIO/F-OUT-046-00_Formulario_de_caracterizacao_do_uso_da_agua_intervencao_barragem.doc',false, false
WHERE NOT EXISTS (SELECT 1 FROM documento_obrigatorio_requerimento WHERE ide_requerimento = 1129118 AND ide_documento_obrigatorio = 1437);

UPDATE fce SET ind_concluido = TRUE WHERE ide_fce = 47153;

INSERT INTO fce_barragem 
(ide_localizacao_geografica, ide_fce, val_vazao_maxima, val_vazao_regularizada, val_descarga_fundo, val_altura_maxima,
val_volume_maximo_acumulado, val_cota_maxima, val_cota_minima, val_cota_media_diarias) 
SELECT  
3325929, 47153, 44.00, 8.81, 9.66, 3.00, 1460000.00, 5.00, 2.74, 3.01
WHERE NOT EXISTS (SELECT 1 FROM fce_barragem WHERE ide_localizacao_geografica = 3325929 AND ide_fce = 47153);

INSERT INTO fce_intervencao_barragem 
(ide_fce_intervencao_barragem, ide_fce, ide_outorga_localizacao_geografica, ide_fce_barragem, ide_documento_obrigatorio_requerimento) 
SELECT 
(select max(ide_fce_intervencao_barragem) from fce_intervencao_barragem) + 1,
47153, 
49258, 
(SELECT max(ide_fce_barragem) FROM fce_barragem), 
(select max(ide_documento_obrigatorio_requerimento) from documento_obrigatorio_requerimento)
WHERE NOT EXISTS (SELECT 1 FROM fce_intervencao_barragem WHERE ide_fce = 47153 AND ide_outorga_localizacao_geografica = 49258);

COMMIT;
-- [33871] Adequação do CEP
BEGIN;
ALTER TABLE bairro ADD ind_origem_api bool NOT NULL DEFAULT false;
ALTER TABLE logradouro ALTER COLUMN ind_origem_correio SET DEFAULT false;
ALTER TABLE logradouro ALTER COLUMN ind_origem_api SET DEFAULT false;
COMMIT;
--[33632] - [CADASTRO] Imóvel Rural sumiu do sistema (Troca de Titularidade)
BEGIN;
        UPDATE
                pessoa_imovel
        SET
                ide_pessoa = 587054
        WHERE
                ide_pessoa_imovel = 1534947;
               
        UPDATE
                pessoa_imovel
        SET
                ind_excluido = FALSE
        WHERE
                ide_pessoa_imovel = 1534947;
                   
        UPDATE
                imovel_rural
        SET
                ide_requerente_cadastro = 587054
        WHERE
                ide_imovel_rural = 975176;
COMMIT;
END;
