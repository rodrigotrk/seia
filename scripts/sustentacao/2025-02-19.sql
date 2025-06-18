--Scripts em caráter prioritário
--Data de geração 05/02/2025
--Versão 4.35.4

BEGIN;

-- [37082] - US 01_Ajustes no perfil TEC_COGEC
	INSERT
		INTO
		rel_grupo_perfil_funcionalidade
	VALUES (
		33,
		1,
		36
	);
	
COMMIT;