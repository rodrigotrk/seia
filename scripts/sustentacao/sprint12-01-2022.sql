-- #33237 - Processo sumiu

-- 33237 - Processo sumiu
BEGIN;
	UPDATE 
		controle_tramitacao 
	SET 	
		ind_fim_da_fila = true
    WHERE 
    		ide_controle_tramitacao = 501444;
COMMIT;
-- #33147 - Troca de imóveis vinculados impropriamente
BEGIN;

UPDATE 
	pessoa_imovel 
SET 
	ind_excluido = TRUE 
WHERE 
	ide_pessoa = 496994
	AND ide_imovel = 21564;
	
INSERT
	INTO
	pessoa_imovel(ide_pessoa,
	ide_imovel,
	dtc_criacao,
	ind_excluido,
	ide_tipo_vinculo_imovel)
VALUES (22456,
21564,
now(),
FALSE,
1);

-- #33201 - Solução processo sumido

BEGIN;

 UPDATE 
 	controle_tramitacao 
 SET 
 	ind_fim_da_fila = true
 WHERE
 	ide_controle_tramitacao = 489062;
COMMIT;

-- #33231 - Solução para processo duplicado

BEGIN;
	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = false  
	where 
		ide_controle_tramitacao = 501489;
 COMMIT;
	COMMIT;
END ;
-- #33236 - Inclusão de Espécies
BEGIN;

-- Nome Científico "Henriettea succosa". 
-- Nome Popular: "mundururú branco".

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('mundururú branco');



INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
	('12033')
	);

        
COMMIT;

-- #33234 - CEP e localidade corrigida do imóvel que estava incorretos.

--Mudando o id do município na tabela bairro do município de Pé da Serra para Riachão do Jacuípe

BEGIN;	
	
	UPDATE 
		logradouro 
	SET 
		ide_municipio = 854 
	WHERE 
		ide_municipio = 1176 AND ide_bairro = 912;
	
--Desabilitando a flag ind_origem_correio da tabela bairro para parar de aparecer onde não deveria
	
	UPDATE 
		bairro 
	SET 
	 	ind_origem_correio = false
	WHERE 
		ide_bairro = 1218 AND ide_municipio = 939;

--Desabilitando a flag ind_origem_correio da tabela bairro para parar de aparecer onde não deveria

	UPDATE 
		bairro 
	SET 
	 	ind_origem_correio = false
	WHERE 
		ide_bairro = 1608 AND ide_municipio = 691;

--Mudando o CEP da tabela 

	UPDATE 
		logradouro 
	SET 
	 	num_cep = '44640000', ide_municipio = 854
	WHERE 
		ide_logradouro = 3812227;
	COMMIT;
	
END;	
--[33238] - Inclusão de espécies.

-- nome científico: Copaifera sabulicola
-- nome popular: pau d'óleo
BEGIN;
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Copaifera sabulicola J.Costa & L.P.Queirozde',
TRUE);

-- Esse insert está comentando pois o eclipse não lê a regex para ignorar o apóstrofo, 
-- quando for rodar no banco de dados, retirar o comentario
-- e estará funcionando normalmente


/*INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('pau d\'óleo');
*/ 

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Stryphnodendron coriaceum Benth
-- nome popular: barbatimão-graúdo

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Stryphnodendron coriaceum Benth',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('barbatimão-graúdo');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Mimosa sericantha Benth
-- nome popular: sena-cana

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Mimosa sericantha Benth',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('sena-cana');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Matayba heterophylla (Mart.) Radlk.
-- nome popular: laranjinha-lisa

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Matayba heterophylla (Mart.) Radlk.',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('laranjinha-lisa');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Oxandra sessiliflora R.E.Fr
-- nome popular: fruta-de-jacu

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Oxandra sessiliflora R.E.Fr.',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('fruta-de-jacu');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Cupania paniculata Cambess
-- nome popular: camboatã

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Cupania paniculata Cambess',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('camboatã');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Chamaecrista orbiculata (Benth.) H.S.Irwin & Barnebyde
-- nome popular: moeda

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Chamaecrista orbiculata (Benth.) H.S.Irwin & Barnebyde',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('moeda');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));


-- nome científico: Rourea induta Planch
-- nome popular: botica-inteira

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Rourea induta Planch',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('botica-inteira');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));

-- nome científico: Ouratea glaucescens (A.St.-Hil.) Engl.
-- nome popular: flor-de-ouro

INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
('Ouratea glaucescens (A.St.-Hil.) Engl.',
TRUE);

INSERT INTO
nome_popular_especie
(nom_popular_especie)
VALUES
('flor-de-ouro');

INSERT INTO
especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(SELECT
currval ('especie_supressao_seq' )
));
COMMIT;


-- #32936 - Excluída duplicata de requerimento
BEGIN;

DELETE
FROM
	requerimento_imovel
WHERE
	ide_requerimento_imovel = 140146;

DELETE
FROM
	requerimento
WHERE
	ide_requerimento = 178463;
	
COMMIT;

-- #33253 - processo sumido

BEGIN;
	UPDATE
		controle_tramitacao
	SET 
		ind_fim_da_fila = true
    	WHERE
   		ide_controle_tramitacao = 507011;
	COMMIT;
END;
-- #33252 - Processo sumido

BEGIN;
	UPDATE
		controle_tramitacao 
	SET 
		ind_fim_da_fila = true
	WHERE
		ide_controle_tramitacao = 512403;
	COMMIT;
END;
