--Scripts em caráter prioritário
--Data de geração 15/12/2022
--Versão 4.26.2

BEGIN;
--[34707] INSERT somente dos nomes popular

-- Espécie: Thyrsodium spruceanum
-- nome popular: Camboatã-de-leite

	INSERT INTO
	        nome_popular_especie
	        (nom_popular_especie)
	VALUES
	        ('Camboatã-de-leite');
	INSERT INTO
	        especie_supressao_nome_popular_especie
	
	        (ide_nome_popular_especie,
	
	        ide_especie_supressao)
	VALUES
	        (currval ('nome_popular_especie_seq'),
	11398);
	
	-- Espécie: Casearia sylvestris
	-- nome popular: Guaçatonga
	
	INSERT INTO
	        nome_popular_especie
	
	        (nom_popular_especie)
	VALUES
	        ('Guaçatonga');
	INSERT INTO
	        especie_supressao_nome_popular_especie
	
	        (ide_nome_popular_especie,
	
	        ide_especie_supressao)
	
	VALUES
	        (currval ('nome_popular_especie_seq'),
	1753);

-- [34797] - Retorno de Status da rl
        UPDATE
                reserva_legal
        SET
                ide_status = 3
        WHERE
                ide_reserva_legal = 16251;
                
-- HIST_HISTORICO                
        INSERT
                INTO
                hist_historico (data_historico,
                acao_historico,
                ip_historico,
                ide_usuario)
        VALUES
        (now(),
        'UPD',
        '172.16.254.1',
        18636);
        
-- HIST_VALOR
        INSERT
                INTO
                hist_valor (val_valor,
                ide_campo,
                ide_registro,
                ide_historico)
        VALUES 
        (3,
        195,
        16251,
        currval('historico_ide_historico_seq'));

-- [34815] Alteração de Usuário
	UPDATE
	        usuario
	SET
	        ide_perfil = 2,
	        ind_tipo_usuario = FALSE
	WHERE
	        ide_pessoa_fisica = 54719;
	        
-- [34868] Falha ao inserir CEP do Empreendimento
	UPDATE
		logradouro
	SET
		ide_municipio = 4931,
		num_cep = 66070000
	WHERE
		ide_logradouro = 4089411;

-- [34858] Retorno de Status
	UPDATE
		reserva_legal
	SET
		ide_imovel_rural = NULL
	WHERE
		ide_reserva_legal = 4117;

-- [34854] Retorno de status da RL
	UPDATE
		reserva_legal
	SET
		ide_status = 4
	WHERE
		ide_reserva_legal IN (559427, 561816);

-- [34852] Status Incorreto
	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1376077; 

-- [34847] O CEP informado não pertence ao município do seu cadastro
	UPDATE
		imovel
	SET
		ide_endereco = 2401427
	WHERE
		ide_imovel = 33851;

-- [34843] Erro ao gerar Certificado LAC 
	UPDATE
		licenca
	SET
		ind_excluido = TRUE
	WHERE
		ide_licenca IN (47909, 47813, 47786);

	UPDATE
		licenca
	SET
		ind_excluido = FALSE
	WHERE
		ide_licenca = 48075;

-- [34746] Erro ao gerar Certificado LAC
	UPDATE
		licenca
	SET
		ind_excluido = TRUE
	WHERE
		ide_licenca IN (47803, 47591);
	        

-- [34653] Botão de emitir notificação não aparece
	
	DELETE FROM reenquadramento_tipo_finalidade_uso_agua
	WHERE ide_reenquadramento_processo_ato=1825 AND ide_tipo_finalidade_uso_agua IN (5,11);
	
	DELETE FROM reenquadramento_processo_ato
	WHERE ide_reenquadramento_processo_ato=1825;
	
	DELETE FROM finalidade_reenquadramento_processo
	WHERE ide_finalidade_reenquadramento=1 AND ide_reenquadramento_processo=669;
	
	DELETE FROM reenquadramento_processo
	WHERE ide_reenquadramento_processo=669;
	
	DELETE FROM notificacao_motivo_notificacao
	WHERE ide_notificacao_motivo_notificacao IN (28246,28248,66359);
	
	DELETE FROM notificacao
	WHERE ide_notificacao IN (22723,22722,15421);
	
-- [33984] - Ajuste de status de reserva legal
UPDATE
	reserva_legal
SET
	ide_status = 7,
	ide_dado_origem = NULL
WHERE
	ide_reserva_legal = 10947;


-- [34733] CONVERSÃO DE USUÁRIO INTERNO - EXTERNO	    
	UPDATE
        usuario
	SET
		ide_perfil = 2,
		ind_tipo_usuario = false
	WHERE
		ide_pessoa_fisica = 3020;
COMMIT;