--[33870] - Processo sumiu do sistema.
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 524837;
COMMIT;

--[33791] - [PROCESSO] Shapes da "Realocação de Reserva Legal" não aparece na Análise Técnica.

--Processo = 2016.001.003326/INEMA/LIC-03326
BEGIN;
        UPDATE 
                arquivo_processo 
        SET 
                ide_imovel = 25640 
        WHERE 
                ide_arquivo_processo = 202881;
COMMIT;
        INSERT INTO 
                motivo_notificacao_imovel(ide_notificacao_motivo_notificacao, ide_imovel)
        VALUES
                (55861,25640);
--Processo = 2016.001.003328/INEMA/LIC-03328
        UPDATE 
                arquivo_processo 
        SET 
                ide_imovel = 25478
        WHERE 
                ide_arquivo_processo = 202879;
COMMIT;
        INSERT INTO 
                motivo_notificacao_imovel(ide_notificacao_motivo_notificacao, ide_imovel)
        VALUES
                (55857,25478);
--Processo = 2016.001.003329/INEMA/LIC-03329
        UPDATE 
                arquivo_processo 
        SET 
                ide_imovel = 29197
        WHERE 
                ide_arquivo_processo = 202885;        
COMMIT;
        INSERT INTO 
                motivo_notificacao_imovel(ide_notificacao_motivo_notificacao, ide_imovel)
        VALUES
                (55873,29197);                
                
                
--Processo = 2016.001.003332/INEMA/LIC-03332        
                
        UPDATE 
                arquivo_processo 
        SET 
                ide_imovel = 155919
        WHERE 
                ide_arquivo_processo = 202887;
COMMIT;                
        
        INSERT INTO 
                motivo_notificacao_imovel(ide_notificacao_motivo_notificacao, ide_imovel)
        VALUES
                (55877,155919);        
                
--Processo = 2017.001.006787/INEMA/LIC-06787
                        
        UPDATE 
                arquivo_processo 
        SET 
                ide_imovel = 74454
        WHERE 
                ide_arquivo_processo = 202883;
COMMIT;                
        
        INSERT INTO 
                motivo_notificacao_imovel(ide_notificacao_motivo_notificacao, ide_imovel)
        VALUES
                (55865, 74454);     
                   
--[33896] - [PROCESSO] Processo sumiu do sistema
--Processo 2022.001.001728/INEMA/LIC-01728
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 539248;
COMMIT;

--Processo 2021.001.007149/INEMA/LIC-07149
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 523184;
COMMIT;
--[33898] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao IN (531518,531519);
COMMIT;	                


--[33894] - [PROCESSO] Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 540203;
COMMIT;

--[33893] - [PROCESSO] Processo sumiu do sistema
BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao IN (340462,340463);
COMMIT;	

--[33890] - [PROCESSO

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 531412;
COMMIT;	
                
-- [33886] - Retorno de status de Licenciamento Ambiental
BEGIN;

INSERT
	INTO
	cadastro_atividade_nao_sujeita_lic_status 
SELECT
	(SELECT ide_cadastro_atividade_nao_sujeita_lic_status + 1FROM cadastro_atividade_nao_sujeita_lic_status ORDER BY ide_cadastro_atividade_nao_sujeita_lic_status DESC LIMIT 1),
	now(),
	595355,
	271,
	3
WHERE
	NOT EXISTS (
	SELECT 1
	FROM cadastro_atividade_nao_sujeita_lic_status
	WHERE ide_cadastro_atividade_nao_sujeita_lic_status = (SELECT ide_cadastro_atividade_nao_sujeita_lic_status + 1 FROM cadastro_atividade_nao_sujeita_lic_status ORDER BY ide_cadastro_atividade_nao_sujeita_lic_status DESC LIMIT 1)
	AND dtc_status = now()
	AND ide_pessoa_fisica = 595355
	AND ide_cadastro_atividade_nao_sujeita_lic = 271
	AND ide_cadastro_atividade_nao_sujeita_lic_status = 3);

COMMIT;

--[33885] - [REQUERIMENTO] Requerimento sem número
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2022.001.1' AS VARCHAR) || 
                CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
                CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1138204 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
        ide_requerimento = 1138204;
COMMIT;

--[33881] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (533276,533277,533278);
COMMIT; 
                
--[33878] - Cadastro de espécies no SEIA
--(Espécie: Kielmeyera corymbosa,Nome popular: Olho-de-boi)

BEGIN;
          
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Kielmeyera corymbosa',TRUE);
                
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (3741, (SELECT currval ('especie_supressao_seq')));
COMMIT;        

--[33876] - [REQUERIMENTO] Requerimento sem número

UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2022.001.2' AS VARCHAR) || 
                CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
                CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (SELECT dtc_movimentacao FROM tramitacao_requerimento WHERE ide_requerimento = 1133750 ORDER BY dtc_movimentacao DESC LIMIT 1)
WHERE
        ide_requerimento = 1133750;
COMMIT;

--[33857] - [REQUERIMENTO] Erro ao efetuar enquadramento

--Criando a coluna "ind_excluido" na tabela licenca.

ALTER TABLE licenca ADD ind_excluido boolean DEFAULT false;

--excluindo logicamente o registro da licenca repetida do requerimento.
BEGIN;

	UPDATE 
		licenca 
	SET
		ind_excluido = TRUE 
	WHERE 
		ide_licenca = 47216;
COMMIT;	
--[33831] - [PROCESSO] Erro na análise técnica

--Processo = 2021.001.006749/INEMA/LIC-06749

--inserção na tabela "equipe"

INSERT INTO equipe
(dtc_formacao_equipe, dtc_exclusao_equipe, ind_ativo, ide_area, ide_area_responsavel, ide_processo)
SELECT '2022-01-13 09:09:05.000', NULL, NULL, 76, NULL, 72175 WHERE NOT EXISTS 
(SELECT 1 FROM equipe WHERE dtc_formacao_equipe = '2022-01-13 09:09:05.000' and dtc_exclusao_equipe = NULL and ind_ativo = NULL and ide_area = 76 and ide_area_responsavel = NULL and ide_processo = 72175);

--inserção na tabela "integrante_equipe"

INSERT INTO 
	integrante_equipe 
	(ide_pessoa_fisica, ind_lider_equipe, ide_area, ide_area_responsavel, ide_equipe)
VALUES 
	(978883, TRUE, 76, NULL, (SELECT currval('equipe_seq')));
 
--inserção na tabela "ato" 

INSERT INTO
        processo_ato_integrante_equipe
VALUES 
	((SELECT currval('integrante_equipe_seq')),77832);
-----------------------------------------------------------------------------------------------------------------------------------------------------

--Processo = 2022.001.000054/INEMA/LIC-00054

--inserção na tabela "equipe"

INSERT INTO equipe
(dtc_formacao_equipe, dtc_exclusao_equipe, ind_ativo, ide_area, ide_area_responsavel, ide_processo)
SELECT '2022-01-25 16:14:14.000', NULL, NULL, 76, NULL, 75041 WHERE NOT EXISTS 
(SELECT 1 FROM equipe WHERE dtc_formacao_equipe = '2022-01-25 16:14:14.000' and dtc_exclusao_equipe = NULL and ind_ativo = NULL and ide_area = 76 and ide_area_responsavel = NULL and ide_processo = 75041);

--inserção na tabela "integrante_equipe"

INSERT INTO 
	integrante_equipe 
	(ide_pessoa_fisica, ind_lider_equipe, ide_area, ide_area_responsavel, ide_equipe)
VALUES 
	(978883, TRUE, 76, NULL, (SELECT currval('equipe_seq')));
		
--inserção na tabela "ato" - 2 atos nesse processo

INSERT INTO
        processo_ato_integrante_equipe
VALUES 
	((SELECT currval('integrante_equipe_seq')),81154);

INSERT INTO
        processo_ato_integrante_equipe
VALUES 
	((SELECT currval('integrante_equipe_seq')),81155);





--[33814] - [CADASTRO] Erro ao tentar finalizar cadastro de Imóvel Rural

BEGIN;
        UPDATE 
                cronograma_recuperacao 
        SET
                ide_localizacao_geografica = 2629964
        WHERE 
                ide_cronograma_recuperacao = 124552;
COMMIT;                
                
 --[29764] - Erro ao finalizar cadastro do imóvel.

BEGIN;

INSERT
        INTO
        documento_imovel_rural_posse(ide_imovel_rural,
        ide_tipo_documento_imovel_rural,
        ide_documento_imovel_rural,
        dtc_documento,
        ind_excluido,
        dtc_criacao)
VALUES (5168,
19,
1586785,
'2021-06-01',
FALSE,
now());
COMMIT;
       
 --[33855] - [REQUERIMENTO] Erro para Incluir entidade transportadora

BEGIN;

	UPDATE 
		declaracao_transporte_destinatario_residuo dtdr
	SET
		ind_excluido = TRUE 
	WHERE 
		ide_declaracao_transporte_destinatario_residuo = 3934;
COMMIT;	
--[33384] - [CADASTRO] Imóvel Rural sumiu no sistema

BEGIN;
        UPDATE 
                pessoa_imovel 
        SET 
                ind_excluido = FALSE 
        WHERE 
                ide_pessoa_imovel = 1571769;
 COMMIT; 
