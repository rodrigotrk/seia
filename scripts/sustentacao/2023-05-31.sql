--Scripts em caráter prioritário
--Data de geração 24/05/2023
--Versão 4.27.7

BEGIN;

-- [35327] Erro ao cadastrar Pessoa Física

	UPDATE
		pessoa
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa = 998603;

COMMIT;