--Scripts em caráter prioritário
--Data de geração 26/04/2023
--Versão 4.27.4

BEGIN;

--[35246]
	UPDATE 
		logradouro l
	SET 
		ide_bairro = 1395180
	WHERE 
		ide_logradouro IN (4295755,4295660,4012560);

--[35245]
	UPDATE 
		controle_tramitacao ct
	SET  
		ind_fim_da_fila=true
	WHERE 
		ide_controle_tramitacao=630738;

-- [35242] Processo sumiu do sistema
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (635925, 635924);
		
-- [35200] Erro ao aprovar notificação
	UPDATE
		equipe
	SET
		ind_ativo = NULL
	WHERE
		ide_equipe = 6232;
		
COMMIT;
