--Scripts em caráter prioritário
--Data de geração 02/10/2024
--Versão 4.33.7

BEGIN;

-- [36772] - Processos duplicados
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 699812;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 699686;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 699623;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 699627;  

COMMIT;