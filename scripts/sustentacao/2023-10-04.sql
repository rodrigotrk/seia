--Scripts em caráter prioritário
--Data de geração 20/09/2023
--Versão 4.29.1

BEGIN;

-- [35504] - Erro ao finalizar requerimento - SHAPE
	
	UPDATE
		municipio
	SET
		cod_ibge_municipio = NULL
	WHERE
		ide_municipio = 610;
		
-- [35549] - Erro ao realizar análise técnica

	UPDATE
		fce_licenciamento_mineral_substancia_mineral_tipologia
	SET
		ide_substancia_mineral_tipologia = 92
	WHERE
		ide_fce_licenciamento_mineral = 691;

	UPDATE
		documento_ato
	SET
		ide_ato_ambiental = 2,
		ide_tipologia = 43
	WHERE
		ide_documento_ato = 7378;


-- [35600] - Imóvel rural sumiu do sistema 

	UPDATE
	        imovel_rural
	SET
	        ide_requerente_cadastro = 1181948
	WHERE
	        ide_imovel_rural = 553825;

-- [35611] - Processo sumiu

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 684246;
		
COMMIT;