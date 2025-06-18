--Scripts em caráter prioritário
--Data de geração 06/06/2024
--Versão 4.32.3

BEGIN;

--[36373] - Processo sumiu do sistema

update 
	controle_tramitacao 
set 
	ind_fim_da_fila = true 
where 
	ide_controle_tramitacao = 769915;


 -- [36052] - Erro ao gerar Declaração de Inexigibilidade
update
	atividade_inexigivel
set
	permite_endereco = true,
	permite_projeto = true,
	permite_unidade = true,
	permite_ponte = true
where
	ide_atividade_inexigivel = 377;
	

-- [36355] Erro ao anexar shapes a notificação
UPDATE
        motivo_notificacao_imovel
SET
        ide_imovel = 1129176
WHERE
        ide_motivo_notificacao_imovel = 1843;
        
--[35977] - Atualização de endereços INEXIG

	-- 2020.001.000809/INEMA/INEXIG
	UPDATE endereco 
	SET num_endereco = 's/n', des_complemento = ''
	WHERE ide_endereco = 2063708;
	UPDATE logradouro SET num_cep = '47970000', ide_bairro = 935, nom_logradouro = 'Avenida Rodoviária BR 020/135, Km 37', ide_municipio = 853 WHERE ide_logradouro = 2521195;
	
	-- 2020.001.000570/INEMA/INEXIG
	UPDATE endereco 
	SET num_endereco = 's/n', des_complemento = ''
	WHERE ide_endereco = 2033200;
	UPDATE logradouro SET num_cep = '46430000', ide_bairro = 44683, nom_logradouro = 'Distrito de Ceraíma', ide_municipio = 653 WHERE ide_logradouro = 2844108;
	
	-- 2020.001.000816/INEMA/INEXIG
	UPDATE endereco 
	SET num_endereco = 's/n', des_complemento = ''
	WHERE ide_endereco = 2064897;
	UPDATE logradouro SET num_cep = '47600000', ide_bairro = 660, nom_logradouro = 'Rodovia 359', ide_municipio = 902 WHERE ide_logradouro = 2520889;
	
	

-- [36384] Modificação de usuário

	UPDATE
		usuario
	SET
		ide_perfil = 2,
		ind_tipo_usuario = FALSE
	WHERE
		ide_pessoa_fisica = 1073099;        
		
-- [36379] - Requerimento sem número
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                                  CAST('2024.001.0' AS VARCHAR) || 
                                  CAST((CAST(split_part(split_part((
                SELECT
                                        r.num_requerimento
                FROM
                                        requerimento r
                WHERE
                                        r.num_requerimento IS NOT NULL
                ORDER BY
                                        r.num_requerimento DESC
                LIMIT 1),
                                  '.',
                                   3),
                                   '/',
                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                           ),
        dtc_finalizacao = (
        SELECT
                                  dtc_movimentacao
        FROM
                                  tramitacao_requerimento
        WHERE
                                  ide_requerimento = 1310876
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1310876;
        

--[36373] - Processo sumiu do sistema
update 
        controle_tramitacao 
set 
        ind_fim_da_fila = true 
where 
        ide_controle_tramitacao = 769915;
        
-- [36382] Erro ao aprovar notificação

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 770417;
	        
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 770416;        
		
-- [36387] - Pagamento REPFLOR

UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 8
WHERE
        ide_tramitacao_requerimento IN (1710147, 1641030, 1689778, 1700271, 1692379, 1698232, 1708668, 1645444, 1641041);

COMMIT;