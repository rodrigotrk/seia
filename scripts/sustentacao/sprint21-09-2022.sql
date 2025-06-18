BEGIN;

-- [34577] PROCESSO SUMIU DO SISTEMA
    UPDATE
        controle_tramitacao
    SET
        ind_fim_da_fila = TRUE
    WHERE
        ide_controle_tramitacao = 569941;

-- [34427] - Correção boleto com valor errado
	UPDATE
		outorga_localizacao_geografica_finalidade
	SET
		ind_excluido = TRUE
	WHERE
		ide_outorga_localizacao_geografica_finalidade = 68943;

--34535 - [REQUERIMENTO] Erro ao realizar enquadramento

	UPDATE
		pessoa
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa = 5039;

--[34547] - Correção de cadastro no CEFIR.
    UPDATE
        reserva_legal
    SET
        ide_status = 3
    WHERE
        ide_reserva_legal = 27447;

--34535 - [REQUERIMENTO] Erro ao realizar enquadramento
    UPDATE
        pessoa
    SET
        ind_excluido = FALSE
    WHERE
        ide_pessoa = 5039;
        
-- A branch feature/34588 possui a resolução dos tickets do final 40 ao 88.
-- [34540] - PROCESSO SUMIU
	UPDATE 
	        controle_tramitacao 
	SET 
	        ind_fim_da_fila = TRUE 
	WHERE 
	        ide_controle_tramitacao = 573174;
        
-- [34546] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 571650;
        
-- [34562] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao IN (579428, 579427, 579426);        
--[34389] - Erro na finalização do CEFIR
	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural IN (519450, 518541);

-- [34567] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao IN (578499, 578500);

-- [34568] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao = 580105;

-- [34551] PROCESSO SUMIU DO SISTEMA+
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao IN (572984, 572985);

-- [34583] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao IN (531658, 531657);

-- [34585] PROCESSO SUMIU DO SISTEMA
	UPDATE        
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
        	ide_controle_tramitacao = 580325;
-- [34587] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE 
	WHERE
	        ide_controle_tramitacao IN (576475, 576476);
        
--[34224] - Erro ao salvar RL
    UPDATE
	        cronograma_recuperacao
	SET
	        ide_reserva_legal = NULL
	WHERE
	        ide_cronograma_recuperacao = 68794;

	UPDATE
	        reserva_legal
	SET
	        ide_tipo_estado_conservacao = 1
	WHERE
	        ide_reserva_legal = 264848;

COMMIT;

-- [34586] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE 
	WHERE
	        ide_controle_tramitacao = 565884;

-- [34588] PROCESSO SUMIU DO SISTEMA
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = TRUE
	WHERE
	        ide_controle_tramitacao IN (534956, 534957);        
-- [34550] CERTIFICADO DE PAGAMENTO REPFLOR
	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento = 1175308;
-- [34553] CEP DIVERGENTE DA LOCALIDADE
	UPDATE
	        logradouro
	SET
	        ide_municipio = 569
	WHERE
	        ide_municipio = 950 AND num_cep = 48440000;
	        
	UPDATE
	        bairro
	SET
	        ide_municipio = 569
	WHERE
	        ide_bairro = 938;
	        
--[33536] - Erro na finalização de CEFIR. 

	UPDATE
		cronograma_recuperacao
	SET
		ide_app = NULL
	WHERE
		ide_cronograma_recuperacao = 52778;

--[34431] - [REQUERIMENTO] Sistema não salva a isenção
    DELETE FROM
            processo
    WHERE
            ide_processo = 80547;
            
--[34584] - Mudança de perfil em usuário
	UPDATE 
		usuario
	SET 
		ide_perfil = 2
	WHERE 
		ide_pessoa_fisica = 935931;
	UPDATE 
		usuario
	SET 
		ind_tipo_usuario = FALSE 
	WHERE 
		ide_pessoa_fisica = 935931;

-- [34582] Processo sumiu do sistema            
	-- 2022.001.003892/INEMA/LIC-03892
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 578547;

	-- 2022.001.003900/INEMA/LIC-03900
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 578550;

--[34576] - Correção de enquadramento de DTRP
	UPDATE 
		enquadramento 
	SET 
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 139612;
-- [34592] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (579427, 579426);
-- [34599] - Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (583155, 583154);

-- [34595] Processo sumiu do sistema

	UPDATE
        controle_tramitacao
	SET
        ind_fim_da_fila = TRUE
	WHERE
        ide_controle_tramitacao = 576872;
        
COMMIT;
