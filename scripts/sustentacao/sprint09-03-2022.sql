--[33513] - Processo sumiu do sistema
BEGIN;
    UPDATE 
            controle_tramitacao  
    SET         
            ind_fim_da_fila = TRUE
    WHERE 
            ide_controle_tramitacao = 521183;
COMMIT;    
    
--[33511] - Processo sumiu do sistema
BEGIN;
    UPDATE 
            controle_tramitacao  
    SET         
            ind_fim_da_fila = TRUE
    WHERE 
            ide_controle_tramitacao = 519142;
COMMIT;   

--[33503] - Processo sumiu do sistema
BEGIN;
	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 475070;
COMMIT;

--[33497] - Processo sumiu do sistema
BEGIN;
    UPDATE 
            controle_tramitacao  
    SET         
            ind_fim_da_fila = TRUE
    WHERE 
            ide_controle_tramitacao = 523685;
COMMIT;    
--[33494] - Alteração de Usuário Interno para Usuário Externo

BEGIN;
        UPDATE
                usuario 
        SET 
                ide_perfil = 2, ind_tipo_usuario = false
        WHERE 
                ide_pessoa_fisica = 924958;
COMMIT;     
--[33488] - Processo duplicado no sistema

BEGIN;
        UPDATE
                controle_tramitacao
        SET 
                ind_fim_da_fila = false
        WHERE 
                ide_controle_tramitacao = 513262;
COMMIT;     
			
--[33130] - correção de valor do boleto.

UPDATE
	empreendimento_requerimento
SET
	ide_classe = 2
WHERE
	ide_requerimento = 20613;
-- #32911 - Shape na análise técnica
BEGIN;

UPDATE
	arquivo_processo
SET
	ide_imovel = 1091549
WHERE
	ide_arquivo_processo = 179224;

UPDATE
	arquivo_processo
SET
	ide_imovel = 1091549
WHERE
	ide_arquivo_processo = 176097;

COMMIT;
-- [33219] - Processo sumiu. 
BEGIN;

UPDATE 
		controle_tramitacao
SET 
		ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 488859;

--[33515] - Alteração de Perfil e Transferência de Processos da Pauta
BEGIN; 
        UPDATE 
                usuario 
        SET 
                ide_perfil = 4
        WHERE 
                ide_pessoa_fisica = 1045155;
COMMIT;

BEGIN; 
        UPDATE 
                pauta 
        SET 
                ide_tipo_pauta = 2
        WHERE 
                ide_pauta = 2373;
COMMIT;

--Ticket #33361
BEGIN;
UPDATE
        outorga_localizacao_geografica
SET
        ide_tipo_barragem = 1
WHERE
        ide_localizacao_geografica IN (3201586,
3201588,
3201590,
3201592,
3201596,
3201598,
3201600,
3201603,
3201605,
3201609,
3158830);
COMMIT;



-- [33257] - Erro no redirecionamento da página
BEGIN;
ALTER TABLE
declaracao_transporte_destinatario_residuo
ADDind_excluido BOOLEAN
DEFAULT
FALSE;

UPDATE
	declaracao_transporte_destinatario_residuo
SET
	ind_excluido = TRUE
WHERE
	ide_declaracao_transporte_destinatario_residuo = 3646;
--[33515] - Alteração de Perfil e Transferência de Processos da Pauta
BEGIN; 
        UPDATE 
                usuario 
        SET 
                ide_perfil = 4
        WHERE 
                ide_pessoa_fisica = 1045155;
COMMIT;

BEGIN; 
        UPDATE 
                pauta 
        SET 
                ide_tipo_pauta = 2
        WHERE 
                ide_pauta = 2373;
COMMIT;

--[33539] - [REQUERIMENTO] Emissão do Certificado de Pagamento REPFLOR

--requerimento = 2021.001.067088/INEMA/REPFLOR
BEGIN;
        UPDATE 
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE 
                ide_tramitacao_requerimento = 1143473;
COMMIT;                
--requerimento = 2021.001.026247/INEMA/REPFLOR
        UPDATE 
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE 
                ide_tramitacao_requerimento = 1068346;
COMMIT;
--requerimento = 2019.001.171929/INEMA/REPFLOR
        UPDATE 
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE 
                ide_tramitacao_requerimento = 861831;
COMMIT;                

BEGIN;

--[33530] - [PROCESSO] Processo sumiu do sistema

UPDATE 
	controle_tramitacao 
SET
	ind_fim_da_fila = TRUE 
WHERE 
	ide_controle_tramitacao = 525975;
COMMIT;

--[33529] - Processo sumiu

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 494181;
COMMIT;


--[33528] - Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao  
        SET         
                ind_fim_da_fila = TRUE
        WHERE 
                ide_controle_tramitacao = 523685;
COMMIT;        
--[33477] - Erro na etapa 6 do requerimento
BEGIN;
ALTER TABLE fauna ADD ind_excluido bool NOT NULL DEFAULT false;
UPDATE  fauna 
        SET ind_excluido = true 
    WHERE ide_fauna =82200;
COMMIT;

