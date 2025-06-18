--[33331] - Inclusão de Espécies
BEGIN;
-- 1- Fraunhofera multiflora e Pau-vidro-branco
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Fraunhofera multiflora',
	TRUE);

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('Pau-vidro-branco');

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
	(SELECT
	currval ('especie_supressao_seq' )
	));
-- 2 - Coursetia rostrata e Brinco-de-princesa
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Coursetia rostrata',
	TRUE);

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('Brinco-de-princesa');

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
	(SELECT
	currval ('especie_supressao_seq' )
	));
-- 3 - Pau-d'arco

-- Trecho comentado pois o eclipse não reconhece a REGEX para skippar o apóstrofo (')
-- Quando necessário, descomentar e estará funcionando normalmente
	
--UPDATE nome_popular_especie
--SET nom_popular_especie='Pau-d\'arco'
--WHERE ide_nome_popular_especie=9764;

-- 4 - Jacaratia corumbensis
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Jacaratia corumbensis',
	TRUE);

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,ide_especie_supressao)
VALUES
	(2140, ( SELECT currval('especie_supressao_seq')));
-- 5 - Phytolacca thyrsiflora e Papo-de-peru
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Phytolacca thyrsiflora',
	TRUE);

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('Papo-de-peru');

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
	(SELECT
	currval ('especie_supressao_seq' )
	));
-- 6 - Ptilochaeta bahiensis e Falso-vidro
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Ptilochaeta bahiensis',
	TRUE);

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('Falso-vidro');

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
(SELECT
	currval ('especie_supressao_seq' )
	));
--[33345] - Inclusão de ESpécies
BEGIN;
--Cnidoscolus vitifolius de nome popular Favela-de-galinha
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Cnidoscolus vitifolius',
TRUE);

INSERT
	INTO
	nome_popular_especie
(nom_popular_especie)
VALUES
('Favela-de-galinha');

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
	currval ('especie_supressao_seq')
));
--Vitex schaueriana de nome popular Tarumã
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Vitex schaueriana',
TRUE);

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(2809,
(
SELECT
	currval('especie_supressao_seq')));
--Combretum hilarianum de nome popular Vaqueta
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Combretum hilarianum',
TRUE);

INSERT
	INTO
	nome_popular_especie
(nom_popular_especie)
VALUES
('Vaqueta');

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(currval ('nome_popular_especie_seq'),
(
SELECT
	currval ('especie_supressao_seq')
));
--Krameria bahiana de nome popular Carrapicho
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Krameria bahiana',
TRUE);

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(8445,
(
SELECT
	currval('especie_supressao_seq')));
--Terminalia eichleriana de nome popular Musssambê-X
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Terminalia eichleriana',
TRUE);

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(2164,
(
SELECT
	currval('especie_supressao_seq')));
--Rosmarinus officinalis de nome popular Alecrim
INSERT
	INTO
	especie_supressao
(nom_especie_supressao,
	ind_ativo)
VALUES
('Rosmarinus officinalis',
TRUE);

INSERT
	INTO
	especie_supressao_nome_popular_especie
(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
(5008,
(
SELECT
	currval('especie_supressao_seq')));
--Correção do nome da Senegalia langsdorffii
UPDATE
	especie_supressao
SET
	nom_especie_supressao = 'Senegalia langsdorffii',
	ind_ativo = TRUE
WHERE
	ide_especie_supressao = 12063;
--[33350] - Processo sumiu
BEGIN;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 516626;

COMMIT;
END;
 -- #33258 Script de ajuste rejeição dos processos das pautas dos lideres
BEGIN

UPDATE
	public.controle_tramitacao
SET
	ide_pauta = 2236,
	ind_responsavel = true,
	ide_pessoa_fisica = 4982,
	ide_area = 47
WHERE
	ide_controle_tramitacao = 513213;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = 'false'
WHERE
	ide_processo = 65175
	and ide_pauta = 2235
	and ind_fim_da_fila = true;

INSERT
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_interno,
	ide_pauta,
	ind_responsavel,
	ide_pessoa_fisica)
VALUES (
65175,
6,
47,
now(),
true,
'Solicitação de remanejamento de processo através do chamado Redmine 33258',
2235,
false,
4982);

UPDATE
	public.controle_tramitacao
SET
	ide_pauta = 2264,
	ind_responsavel = true,
	ide_pessoa_fisica = 1500,
	ide_area = 8,
	ind_fim_da_fila = true
WHERE
	ide_controle_tramitacao = 507009;

UPDATE
	public.controle_tramitacao
SET
	ind_fim_da_fila = true
WHERE
	ide_controle_tramitacao = 507011
	and ide_pauta = 1202;

COMMIT;
-- [33330] - Processo duplicado
BEGIN;
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 508120;

COMMIT;

--[33373] - Processo sumido

BEGIN;
	UPDATE  
		controle_tramitacao 
	SET 
		ind_fim_da_fila = true
    WHERE  
   		ide_controle_tramitacao = 513796;
  COMMIT;
  
  
-- [33213] Erro ao enquadrar requerimento

  BEGIN;

UPDATE
	public.documento_obrigatorio
SET
	nom_documento_obrigatorio = 'Proposta de compensação, devido ao corte ou supressão em Mata Atlântica de vegetação nativa primária ou secundária nos estágios médio ou avançado de regeneração, contendo mapa de uso e ocupação do solo da região e caracterização da área destinada para a compensação, com ênfase na vegetação, quando couber ',
	num_tamanho = 0.000,
	ind_formulario = false,
	dsc_caminho_arquivo = null,
	ind_publico = true,
	ide_tipo_documento_obrigatorio = 2,
	ind_ativo = true,
	ind_formulario_digital = false
WHERE
	ide_documento_obrigatorio = 10146;

UPDATE
	public.documento_obrigatorio
SET
	nom_documento_obrigatorio = 'Inventário Florestal para Supressão de Vegetação Nativa, por Amostragem ou Censo Florestal, conforme modelo disponível no site do INEMA',
	num_tamanho = 0.000,
	ind_formulario = false,
	dsc_caminho_arquivo = null,
	ind_publico = true,
	ide_tipo_documento_obrigatorio = 2,
	ind_ativo = true,
	ind_formulario_digital = false
WHERE
	ide_documento_obrigatorio = 10144;

UPDATE
	public.documento_obrigatorio
SET
	nom_documento_obrigatorio = 'Estudo fitossociológico, incluindo a classificação do estágio de regeneração de acordo com a Resolução CONAMA nº 5 de 04/05/1994, para áreas inseridas no bioma Mata Atlântica ou nas áreas do mapa de aplicação da Lei da Mata Atlântica (Lei 11.428/2006), independente do tamanho/porcentagem em relação à área total requerida para ASV',
	num_tamanho = 0.000,
	ind_formulario = false,
	dsc_caminho_arquivo = null,
	ind_publico = true,
	ide_tipo_documento_obrigatorio = 2,
	ind_ativo = true,
	ind_formulario_digital = false
WHERE
	ide_documento_obrigatorio = 10145;

COMMIT;

-- [33413] - Requerimento "CEP não localizado"

BEGIN;

	UPDATE 	
		municipio 
	SET 
		num_cep = '40157151'
	WHERE 
		ide_municipio = 837;
		
COMMIT;	

-- [33338] - Mudança de Status do Requerimento (Erro ao Gerar Certificado)

BEGIN;	
--(Caso 1)
	
	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1193938;

--(Caso 2)
		
	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1152323;
		
--(Caso 3)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1141464;

--(Caso 4)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1152289;

--(Caso 5)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1152308;

--(Caso 6)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1152334;

--(Caso 7)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1152303;

--(Caso 10)
	
	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1193232;
		
--(Caso 11 - GLPI 119988)	

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1204046;

	
--(Caso 12 - GLPI 119988)	

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1204049;	
			
--(Caso 13 - GLPI 119988)	

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1204043;
		
--(Caso 14 - GLPI 119988)	

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1204041;
				
--(Caso 15 - GLPI 119988)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1204038;

--(Caso 16 - GLPI 119983)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1203994;

--(Caso 17 - GLPI 119983)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1203998;

--(Caso 18 - GLPI 119983)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1212795;

--(Caso 19 - GLPI 119983)

	UPDATE 
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE 
		ide_tramitacao_requerimento = 1214032;	
 COMMIT;		
 
-- [33410] - Processo sumido
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 503351;
COMMIT;      
-- #33342 - Desativação de atos para enquadramento
BEGIN;

UPDATE
	ato_ambiental
SET
	ind_ativo = FALSE
WHERE
	ide_ato_ambiental = 51;

COMMIT;

-- #33034 - Transferência de requerimento
BEGIN;

UPDATE
	requerimento_pessoa
SET
	ide_pessoa = 645567
WHERE
	ide_requerimento = 743385;

UPDATE
	requerimento
SET
	nom_contato = 'FLAVIO ARAUJO DE SOUZA',
	num_telefone = '(77) 9861-0192',
	des_email = (
	SELECT
		p.des_email
	FROM
		pessoa p
	WHERE
		p.ide_pessoa = 645567)
WHERE
	ide_requerimento = '743385';

UPDATE
	empreendimento
SET
	ide_pessoa = 645567
WHERE
	ide_empreendimento = 103897;

UPDATE
	imovel_empreendimento
SET
	ide_imovel = 806722
WHERE
	ide_empreendimento = 103897;

UPDATE
	usuario
SET
	ind_excluido = TRUE
WHERE
	ide_pessoa_fisica = 91275;

UPDATE
	pessoa
SET
	ind_excluido = TRUE
WHERE
	ide_pessoa = 91275;
	
COMMIT;

-- [33418] - Processo Sumido (2 processos com mesmo número e status diferentes)

BEGIN;

-- Habilitando o processo com "ide_status_fluxo = 6"
 
		UPDATE 
			controle_tramitacao 
		SET  
			ind_fim_da_fila = true
		WHERE  
		   	ide_controle_tramitacao = 517683;
		   	
COMMIT;		   	


  --[33417] - Processo Sumido

BEGIN;
	UPDATE 
		controle_tramitacao 
	SET  
		ind_fim_da_fila = true
	WHERE  
	   	ide_controle_tramitacao = 506645;
COMMIT;

END;

 -- [32958] - Shape sem aparecer na analise técnica
 BEGIN;
 	UPDATE arquivo_processo SET ide_imovel=8482 WHERE ide_arquivo_processo =171060;
	INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES (34835, 8482);
	UPDATE arquivo_processo SET ide_imovel=8482 WHERE ide_arquivo_processo =171057;
	INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES (34834, 8482);
	UPDATE arquivo_processo SET ide_imovel=8482 WHERE ide_arquivo_processo =183071;
	INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES (42601, 8482);
	UPDATE arquivo_processo SET ide_imovel=8482 WHERE ide_arquivo_processo =183087;
	INSERT INTO public.motivo_notificacao_imovel (ide_notificacao_motivo_notificacao, ide_imovel) VALUES (42602, 8482);
 COMMIT;
--  [33172] - Requerimento não visualizado
BEGIN; 
DELETE FROM public.lac wHERE ide_lac=4693;
COMMIT; 
 
