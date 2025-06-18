--Scripts em caráter prioritário
--Data de geração 07/06/2023
--Versão 4.27.8

BEGIN;

 -- [35347] e [35331] Imóvel Rural sumiu do sistema
 -- Tickets duplicados

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1927081;

-- [35337] Alteração de Usuário

-- Atualizando o perfil e a pauta de 'Ícaro Meirelles Dias':

	UPDATE
		usuario
	SET
		ide_perfil = 4,
		ind_tipo_usuario = TRUE
	WHERE
		ide_pessoa_fisica = 417763;

	UPDATE
		pauta
	SET
		ide_tipo_pauta = 2,
		ide_area = 18
	WHERE
		ide_pessoa_fisica = 417763 AND ide_pauta = 1784;

-- Atualizando o perfil e a pauta de 'MARIA QUITERIA CASTRO DE OLIVEIRA':
        
	UPDATE
		usuario
	SET
		ide_perfil = 3
	WHERE
		ide_pessoa_fisica = 19185;

	UPDATE
		pauta
	SET
		ide_tipo_pauta = 3
	WHERE
		ide_pessoa_fisica = 19185 AND ide_pauta = 827;        
        
-- Vinculando o novo coordenador a área 'COSEB - Coordenação de Segurança de Barragem':

	UPDATE
		area
	SET
		ide_pessoa_fisica = 417763
	WHERE
		ide_area = 18;

-- [35332] Processo sumiu do sistema
	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE
	WHERE 
	   ide_controle_tramitacao = 635294;
	   
-- [35324] Imóvel Rural sumiu do sistema
	UPDATE
        pessoa_imovel
	SET
        ind_excluido = FALSE
	WHERE
        ide_pessoa_imovel = 1743810;
        
COMMIT;
