--Scripts em caráter prioritário
--Data de geração 06/03/2025
--Versão 4.36.0

BEGIN;

-- [37187] - Complemento do erro de empreendimento
	ALTER TABLE 
		imovel_urbano 
	ADD COLUMN 
		ind_excluido 
			boolean DEFAULT FALSE NOT NULL;

COMMIT;