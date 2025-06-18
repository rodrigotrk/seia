--Scripts em caráter prioritário
--Data de geração 14/06/2023
--Versão 4.27.8

BEGIN;

-- [35274] - CEP divergente da localidade

UPDATE 
	logradouro l
SET
	ind_origem_api = FALSE  
WHERE 
	ide_logradouro = 3896015;
	
	
-- [35341] Inclusão de espécies florestais no SEIA

	-- Nome científico: Schnella angulosa
	-- Nome popular: Escada-de-macaco
	
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Schnella angulosa', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Escada-de-macaco');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));

	
	-- [35350] - Processo sumiu do sistema

	UPDATE
		public.controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 583270;
		
-- [35343] - Processo sumiu da pauta do técnico / líder

	UPDATE
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 532000;

COMMIT;