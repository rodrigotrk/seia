-- [33580] - Processo sumiu

BEGIN; 

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 522046;
COMMIT; 
--[33564] - [CONSULTA] CEP divergente da localidade cadastrada

        UPDATE 
                bairro 
        SET 
               ide_municipio = 924 
        WHERE 
               ide_bairro = 2770679;
               
        UPDATE 
                logradouro 
        SET 
                ide_municipio = 924
        WHERE 
                ide_logradouro IN (1083483,2204391,3892255,3894504) AND ide_municipio = 837 AND num_cep = '43850000';

--[33558] - [REQUERIMENTO] DTRP - Erro ao salvar endereço
 
 BEGIN;
 
        DELETE FROM 
                declaracao_transporte_gerador_residuo 
        WHERE
                ide_declaracao_transporte = 4934;
                          
        
        DELETE FROM         
                declaracao_transporte dt
        WHERE 
                ide_declaracao_transporte = 4934;
                
COMMIT;
--[33545] - [REQUERIMENTO] Emissão do Certificado de Pagamento REPFLOR

-- 2021.001.068234/INEMA/REPFLOR
BEGIN;
        UPDATE 
                tramitacao_requerimento 
        SET  
                ide_status_requerimento = 8 
        WHERE  
                ide_tramitacao_requerimento = 1148281;
COMMIT;
-- 2021.001.017964/INEMA/REPFLOR
        UPDATE 
                tramitacao_requerimento 
        SET  
                ide_status_requerimento = 8 
        WHERE  
                ide_tramitacao_requerimento = 1065559;
COMMIT;
-- #33540 - Reinserção de documento de posse
BEGIN;
INSERT
	INTO
	documento_imovel_rural_posse(ide_imovel_rural,
	ide_tipo_documento_imovel_rural,
	ide_documento_imovel_rural,
	dtc_documento,
	ind_excluido,
	dtc_criacao)

VALUES (532010,
19,
827025,
'2018-06-12',
FALSE,
now());

COMMIT;

--[33619] - Alteração de Usuário Interno para Usuário Externo

BEGIN;
        UPDATE
                usuario 
        SET 
                ide_perfil = 2, ind_tipo_usuario = false
        WHERE 
                ide_pessoa_fisica IN (1030311,1032551);
COMMIT;     
--[33617] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        
        UPDATE 

--[33594] - [ PROCESSO ] Processo sumiu


BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (524582,524581);
        
COMMIT;    
--[33608] - [PROCESSO] Processo sumiu do sistema

BEGIN;
        
        UPDATE 
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 525972;
        
COMMIT;

--[33598] - [PROCESSO] Processo não consta na Pauta do Líder (Rodrigo).
BEGIN;
        
        UPDATE 
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 522889;
        
COMMIT;

--[33596] - Processo sumiu do sistema.

BEGIN;
        UPDATE   
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE 
        WHERE       
                ide_controle_tramitacao = 515552;
COMMIT;    

--[33595] - [ PROCESSO ] Processo sumiu

BEGIN;
        UPDATE   
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE 
        WHERE  
		ide_controle_tramitacao = 528233;
COMMIT;

--[33594] - [ PROCESSO ] Processo sumiu

BEGIN;
        UPDATE   
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE 
        WHERE  
		ide_controle_tramitacao = 523189;
COMMIT;


--[33593] - [REQUERIMENTO] "CEP inválido ou não encontrado na base dos correios."

BEGIN;
        UPDATE 
                municipio 
        SET
                nom_municipio = 'São Sebastião do Passé'
        WHERE 
                ide_municipio = 924;
                               
COMMIT;    

--[33581] - [REQUERIMENTO] DTRP - Erro ao salvar endereço

BEGIN;

        UPDATE 
                municipio 
        SET
                nom_municipio = 'Santo Estêvão'
        WHERE 
                ide_municipio = 944;
                
                
                
        UPDATE 
                bairro 
        SET
                ind_origem_correio = FALSE 
        WHERE 
                ide_bairro = 1212;
COMMIT;                

--[33579] - Processo duplicado
        
BEGIN;        
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = FALSE 
        WHERE 
                ide_controle_tramitacao = 48636;
COMMIT; 

--[33572] - Processo sumiu.
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 526702;
COMMIT;    
-- #33524 - Transferência de Titularidade
BEGIN;

UPDATE
	requerimento
SET
	nom_contato = '3R CANDEIAS S.A.'
WHERE
	ide_requerimento = 266732;

UPDATE
	requerimento_pessoa
SET
	ide_pessoa = 965656
WHERE
	ide_requerimento = 266732
	AND 
    ide_pessoa = 251165;

COMMIT;
-- [33506] - documento não estava salavando. 

BEGIN; 
INSERT
	INTO
	documento_imovel_rural_posse(ide_imovel_rural,
	ide_tipo_documento_imovel_rural,
	ide_documento_imovel_rural,
	dtc_documento,
	ind_excluido,
	dtc_criacao)
VALUES (987197,
19,
1361994,
'2018-06-12',
FALSE,
now());
COMMIT;
