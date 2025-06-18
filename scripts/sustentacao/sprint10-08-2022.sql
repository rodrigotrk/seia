
BEGIN;

--[34430]-[PROCESSO] Processo sumiu do sistema
    UPDATE 
        controle_tramitacao
    SET 
        ind_fim_da_fila = TRUE 
    WHERE 
        ide_controle_tramitacao = 564925;

--[34429]-[PROCESSO] Processo sumiu do sistema

UPDATE 
     controle_tramitacao
SET 
     ind_fim_da_fila = TRUE 
WHERE 
     ide_controle_tramitacao = 563018;
        
--[34430]-[PROCESSO] Processo sumiu do sistema

UPDATE 
     controle_tramitacao
SET 
     ind_fim_da_fila = TRUE 
WHERE 
     ide_controle_tramitacao = 564925;
        
--[34429]-[PROCESSO] Processo sumiu do sistema
    UPDATE 
        controle_tramitacao
    SET 
        ind_fim_da_fila = TRUE 
    WHERE 
        ide_controle_tramitacao = 563018;
--[34428]-[PROCESSO] Processo sumiu do sistema
    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = true
    WHERE
        ide_controle_tramitacao IN (567192,567193);

--[34415] - [PROCESSO] Erro ao concluir processo
    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = FALSE
    WHERE
        ide_controle_tramitacao = 560256;
        
--[34438] -Processo sumiu do sistema

        ide_controle_tramitacao = 560256;       
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao IN (568017,568018);
        
--[34432]-[PROCESSO] Processo sumiu do sistema

	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
	     ide_controle_tramitacao IN (550222,550223);

--[34433]-[PROCESSO] Processo sumiu do sistema
	UPDATE 
	    controle_tramitacao
	SET 
	    ind_fim_da_fila = TRUE 
	WHERE 
	     ide_controle_tramitacao = 565691;
        
--[34437] -Processo sumiu do sistema

	UPDATE 
	    controle_tramitacao
	SET 
	    ind_fim_da_fila = TRUE 
	WHERE 
	    ide_controle_tramitacao = 568893;
	    
--[34436] -Processo sumiu do sistema
	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 568936;

--[34434]-[PROCESSO] Processo sumiu do sistema
	UPDATE 
	     controle_tramitacao
	SET 
	     ind_fim_da_fila = TRUE 
	WHERE 
         ide_controle_tramitacao = 567157;

--34291 - [PROCESSO] Erro ao realizar a análise técnica da ASV

	--Ativar o status "Análise Técnica" para o processo atul que está com status "Concluído".
	
	--obs: rodar o script apenas para ver a solução e depois desativar a flag, mudando o "ind_fim_da_fila" para false na mesmo "UPDATE" abaixo:

UPDATE
	controle_tramitacao 
SET
	ind_fim_da_fila = TRUE 
WHERE
	ide_controle_tramitacao = 557734;
	
--Script de solução do ticket 34291

UPDATE
	arquivo_processo
SET
	ide_imovel = 23844
WHERE
	ide_arquivo_processo = 210842;
               
--[34433]-[PROCESSO] Processo sumiu do sistema
	UPDATE 
	     controle_tramitacao
	SET 
	     ind_fim_da_fila = TRUE  
	WHERE 
	     ide_controle_tramitacao = 565691;

--[34432]-[PROCESSO] Processo sumiu do sistema
	UPDATE 
	     controle_tramitacao
	SET 
	     ind_fim_da_fila = TRUE 
	WHERE 
	     ide_controle_tramitacao IN (550222,550223);

--[34291]-[PROCESSO] Erro ao realizar a análise técnica da ASV
--Ativar o status "Análise Técnica" para o processo atual que está com status "Concluído".
--obs: rodar o script apenas para ver a solução e depois desativar a flag, mudando o "ind_fim_da_fila" para false na mesmo "UPDATE" abaixo:
    -- UPDATE
    --     controle_tramitacao 
    -- SET
    --     ind_fim_da_fila = TRUE 
    -- WHERE
    --     ide_controle_tramitacao = 557734;
	
--[34291]-Script de solução do ticket 34291
    UPDATE
        arquivo_processo
    SET
        ide_imovel = 23844
    WHERE
        ide_arquivo_processo = 210842;
           
--[34127] - [CADASTRO] Erro de finalização	

UPDATE
	app
SET
	ide_tipo_estado_conservacao = 1
WHERE
	ide_app = 124847;

UPDATE
	cronograma_recuperacao
SET
	ide_localizacao_geografica = 1428544
WHERE
	ide_app = 124847;   
	
--[34445] -Processo sumiu do sistema

UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE 
WHERE 
    ide_controle_tramitacao = 569801;

--[34371] - [REQUERIMENTO] Falha ao tentar gerar a LAC
	UPDATE 
		licenca 
	SET 
		ind_excluido =TRUE 
	WHERE 
		ide_licenca =48157;
		
--[34446] -Processo sumiu do sistema
	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
	    ide_controle_tramitacao = 569903;
        
--[34465] -Processo sumiu do sistema
	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 568902;

        ide_controle_tramitacao = 564958;
        
--[34464] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 565691;
        
--[34445] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 569801;

--[34458] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao IN (551064,551065);
        
--[34447] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 568902;
--[34446] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 569903;
        
--[34419] - [CADASTRO] Erro na aba de Reserva Legal
    UPDATE
        localizacao_geografica
    SET
        ide_classificacao_secao = 2
    WHERE
        ide_localizacao_geografica = 276968;    
	
--[34371] - [REQUERIMENTO] Falha ao tentar gerar a LAC
	UPDATE 
		licenca 
	SET 
		ind_excluido =TRUE 
	WHERE 
		ide_licenca =48157;
--[34356] - [PROCESSO] Erro ao tentar enviar Análise Técnica
    INSERT
        INTO
        equipe(dtc_formacao_equipe,
        ind_ativo,
        ide_area,
        ide_processo)
    VALUES (now(),
    TRUE,
    76,
    72213);

    INSERT
        INTO
        integrante_equipe(ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe)
    VALUES (19476,
    TRUE,
    76,
    76,
    (
    SELECT
            currval('equipe_seq')));
    INSERT
            INTO
            processo_ato_integrante_equipe
    VALUES ((
    SELECT
            currval('integrante_equipe_seq')),
    77876);
--[34218] - [REQUERIMENTO] Falha ao salvar informações da pessoa jurídica

--desabilitar a trigger primeiro para poder inserir os dados da PJ
    ALTER TABLE pessoa_juridica DISABLE TRIGGER trigger_pessoa_juridica_au;

--rodar o script de inserção dos dados que faltaram
    UPDATE 
        pessoa_juridica
    SET 
        ide_natureza_juridica = 31,
        nom_nome_fantasia = 'UMC AMBIENTAL E TRANSPORTES',
        dtc_abertura = '2009-10-28 00:00:00.000',
        num_inscricao_municipal = '88350819553',
        num_inscricao_estadual = '000000000',
        dsc_caminho_arquivo = '/opt/ARQUIVOS/EMPREENDIMENTO/CNPJ.pdf'
    WHERE 
        ide_pessoa_juridica = 925465;

--Por fim, reativar a trigger padrão
ALTER TABLE pessoa_juridica ENABLE TRIGGER trigger_pessoa_juridica_au;	

--[34419] - [CADASTRO] Erro na aba de Reserva Legal

UPDATE
	localizacao_geografica
SET
	ide_classificacao_secao = 2
WHERE
	ide_localizacao_geografica = 276968;    

--[34356] - [PROCESSO] Erro ao tentar enviar Análise Técnica
    ALTER TABLE pessoa_juridica ENABLE TRIGGER trigger_pessoa_juridica_au;	

--[34127] - [CADASTRO] Erro de finalização	
    UPDATE
        app
    SET
        ide_tipo_estado_conservacao = 1
    WHERE
        ide_app = 124847;

INSERT
	INTO
	integrante_equipe(ide_pessoa_fisica,
	ind_lider_equipe,
	ide_area,
	ide_area_responsavel,
	ide_equipe)
VALUES (19476,
TRUE,
76,
76,
(
SELECT
        currval('equipe_seq')));
INSERT
        INTO
        processo_ato_integrante_equipe
VALUES ((
SELECT
        currval('integrante_equipe_seq')),
77876);
        
--[34458] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao IN (551064,551065);
        
--[34464] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 565691;
        
--[34465] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 564958;
        

--[34221] - [CADASTRO] Erro ao finalizar cadastro

    UPDATE 
        logradouro 
    SET 
        num_cep = '45836000', ide_municipio = 716
    WHERE 
        ide_logradouro = 873788;
        
    UPDATE 
   		cronograma_recuperacao 
    SET 	
   		ide_localizacao_geografica = 112984
    WHERE 
   		ide_cronograma_recuperacao = 4402;    

--[34222] - [PROCESSO] Erro ao realizar a análise técnica da ASV

UPDATE
	arquivo_processo
SET
	ide_imovel = 1154200
WHERE
	ide_arquivo_processo = 204267;
	
--[34222.]OBS: a query debaixo é somente para verificar a correção, voltando o status do processo, pois atualmente ele já está como "processo concluído" e não daria pra acessar a análise técnica sem essa modificação.	

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao IN (559705, 559706);
	

    UPDATE
        cronograma_recuperacao
    SET
        ide_localizacao_geografica = 1428544
    WHERE
        ide_app = 124847;        
        
--[29724] - [Prodeb #141772] [Processo] Documentos não apensados
	UPDATE
		arquivo_processo
	SET
		ide_notificacao = 20373
	WHERE
		ide_arquivo_processo = 133209;        

--[34222] - [PROCESSO] Erro ao realizar a análise técnica da ASV

UPDATE
	arquivo_processo
SET
	ide_imovel = 1154200
WHERE
	ide_arquivo_processo = 204267;
	
--[34222.]OBS: a query debaixo é somente para verificar a correção, voltando o status do processo, pois atualmente ele já está como "processo concluído" e não daria pra acessar a análise técnica sem essa modificação.	

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao IN (559705, 559706);
	
--[34221] - [CADASTRO] Erro ao finalizar cadastro

    UPDATE 
        logradouro 
    SET 
        num_cep = '45836000', ide_municipio = 716
    WHERE 
        ide_logradouro = 873788;
        
    UPDATE 
   		cronograma_recuperacao 
    SET 	
   		ide_localizacao_geografica = 112984
    WHERE 
   		ide_cronograma_recuperacao = 4402;    

COMMIT;
