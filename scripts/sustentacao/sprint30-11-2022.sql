--Scripts em caráter prioritário
--Data de geração 23/11/2022
--Versão 4.25.8

BEGIN;
-- [34741] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (577111, 577112);

-- [34715] Erro ao efetuar análise técnica

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 585104;

--[34705] Erro no histórico de processos

--Adicionando um novo fluxo de tramitação com o status alterado para o processo 2019.001.001419/INEMA/LIC-01419

--Inserindo um novo item para a tabela controle_tramitacao

	INSERT
		INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
	VALUES (50294,
	8,
	8,
	'2022-10-14 00:00:00.100',
	true,
	null,
	null,
	215,
	false,
	null,
	null);

--Retirando indice de fim de fila para o ultimo item gerado antes da criação acima

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = false
	WHERE
		ide_controle_tramitacao = 538424;
	
--Adicionando um novo fluxo de tramitação com o status alterado para o processo 2020.001.000920/INEMA/LIC-00920

--Inserindo um novo item para a tabela controle_tramitacao

	INSERT
		INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
	VALUES (58498,
	8,
	8,
	'2022-10-08 00:00:00.100',
	true,
	null,
	null,
	12,
	false,
	null,
	null);

--Retirando indice de fim de fila para o ultimo item gerado antes da criação acima

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = false
	WHERE
		ide_controle_tramitacao = 537031;
	
--Adicionando um novo fluxo de tramitação com o status alterado para o processo 2020.001.001319/INEMA/LIC-01319

--Inserindo um novo item para a tabela controle_tramitacao

	INSERT
		INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
	VALUES (58905,
	8,
	8,
	'2022-10-07 00:00:00.100',
	true,
	null,
	null,
	12,
	false,
	null,
	null);

--Retirando indice de fim de fila para o ultimo item gerado antes da criação acima

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = false
	WHERE
		ide_controle_tramitacao = 536441;
	
--Adicionando um novo fluxo de tramitação com o status alterado para o processo 2019.001.002234/INEMA/LIC-02234

--Inserindo um novo item para a tabela controle_tramitacao

	INSERT
		INTO
		controle_tramitacao (ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria)
	VALUES (51147,
	8,
	8,
	'2022-10-19 00:00:00.100',
	true,
	null,
	null,
	215,
	false,
	null,
	null);

--Retirando indice de fim de fila para o ultimo item gerado antes da criação acima

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = false
	WHERE
		ide_controle_tramitacao = 539545;

-- [34691] Processo duplicado

	-- 2022.001.000164/INEMA/JUR-00164
	UPDATE
			controle_tramitacao
	SET
			ind_fim_da_fila = FALSE
	WHERE
			ide_controle_tramitacao IN (591258, 585294);
	UPDATE
			controle_tramitacao
	SET
			ind_fim_da_fila = TRUE
	WHERE
			ide_controle_tramitacao = 591030;
        
-- 2019.001.005015/INEMA/LIC-05015
	UPDATE
			controle_tramitacao
	SET
			ind_fim_da_fila = FALSE
	WHERE
			ide_controle_tramitacao IN (585291, 591037);
			
	UPDATE
			controle_tramitacao
	SET
			ind_fim_da_fila = TRUE
	WHERE
			ide_controle_tramitacao = 591034;

--[34680] Erro ao realizar transferência de titularidade
--Processo Nº 2021.001.000184/INEMA/LIC-00184

	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 1064863
	WHERE
		ide_requerimento = 901170 AND ide_pessoa = 20749;

	UPDATE
		requerimento
	SET
		nom_contato = 'DAVOS COMERCIAL E EXPLORAÇÃO MINERAL LTDA'
	WHERE
		ide_requerimento = 901170;	


--Processo Nº 2020.001.007134/INEMA/LIC-07134	

	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 1064863
	WHERE
		ide_requerimento = 901172 AND ide_pessoa = 20749;

	UPDATE
		requerimento
	SET
		nom_contato = 'DAVOS COMERCIAL E EXPLORAÇÃO MINERAL LTDA'
	WHERE
		ide_requerimento = 901172;
	
-- #34674 - Erro na consulta dos filtros
	DELETE FROM analise_tecnica WHERE ide_analise_tecnica = 23326;
	ALTER TABLE analise_tecnica ADD CONSTRAINT analise_tecnica_un UNIQUE (ide_processo);
	
--[34719] - Transferência de Titularidade
    UPDATE
        requerimento_pessoa
    SET
        ide_pessoa = 398
    WHERE
        ide_requerimento = 41744
        AND ide_pessoa = 38792;
        
    UPDATE
        requerimento
    SET
        nom_contato = 'COPA ENGENHARIA AMBIENTAL E LOCACAO DE EQUIPAMENTOS LTDA'
    WHERE
        ide_requerimento = 41744;

-- [34745] Mudança de perfil em usuário
	UPDATE
		usuario
	SET
		ind_tipo_usuario = FALSE,
		ide_perfil = 2,
		ind_excluido = TRUE
	WHERE
		ide_pessoa_fisica IN (18396,
		18397,
		18427,
		18434,
		18568,
		18451,
		18505,
		18448,
		18503,
		18562,
		18356,
		18558,
		18918,
		18386,
		18836,
		1208,
		241936,
		242091,
		241471,
		242007,
		28952,
		17454,
		18353,
		542042,
		542047,
		4057,
		911252,
		911244,
		18444,
		8699,
		7265,
		18384,
		11715);

--[34765] - Processo sumiu do sistema
	
	UPDATE 
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 579874;
-- [34734] Inclusão de espécies florestais no SEIA

-- Nome científico: Desmodium procumbens
-- Nome popular: Açoita-cavalo

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Desmodium procumbens', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Açoita-cavalo');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));
        
-- Nome científico: Huberia carvalhoi
-- Nome popular: Mundururu

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Huberia carvalhoi', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Mundururu');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));
        
-- Nome científico: Annona dolabripetala
-- Nome popular: Araticum

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Annona dolabripetala', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Araticum');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));
        
-- Nome científico: Erythroxylum grandifolium
-- Nome popular: Cafezinho

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Erythroxylum grandifolium', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Cafezinho');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));

-- Nome científico: Senefeldera verticillata
-- Nome popular: Pau-osso

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Senefeldera verticillata', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Pau-osso');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));

-- Nome científico: Monteverdia brasiliensis
-- Nome popular: Espinheira-santa

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Monteverdia brasiliensis', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Espinheira-santa');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));

-- Nome científico: Simira alba
-- Nome popular: Araribá

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Simira alba', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Araribá');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));

-- Nome científico: Neoraputia magnifica
-- Nome popular: Cocão

	INSERT INTO
		especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES
		('Neoraputia magnifica', TRUE);
       
	INSERT INTO
		nome_popular_especie (nom_popular_especie)
	VALUES
		('Cocão');
       
	INSERT INTO
		especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES
		(currval ('nome_popular_especie_seq'), (SELECT currval ('especie_supressao_seq')));	
	
-- [34640] - Certificado de pagamento REPFLOR
	UPDATE
	        tramitacao_requerimento
	SET
	        ide_status_requerimento = 8
	WHERE
	        ide_tramitacao_requerimento = 1353354;

COMMIT;
