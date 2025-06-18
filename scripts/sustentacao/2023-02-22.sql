--Scripts em caráter prioritário
--Data de geração 08/02/2023
--Versão 4.26.7

BEGIN;

-- [35024] Erro ao gerar certificado RLAC

	UPDATE
		licenca
	SET
		ind_excluido = TRUE
	WHERE
		ide_licenca IN (47571, 47650);
-- [35005] Erro ao consultar filtro 'Técnico' na aba de requerimento
	
	UPDATE
		pessoa
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa = 830874;
--[34668] - Licença de requerimento duplicada

UPDATE public.licenca
SET ind_excluido=true
WHERE ide_licenca=51016;

--[34963] - Licença de requerimento duplicada

UPDATE public.licenca
SET ind_excluido=true
WHERE ide_licenca=51000;

--[34996] - Pergunta de requerimento duplicada

UPDATE pergunta_requerimento 
SET ind_excluido = TRUE 
WHERE ide_pergunta_requerimento IN (2245015,
2245018,
2245028,
2245450,
2245451,
2245452,
2245453,
2248174,
2248175,
2248176,
2248184,
2248185,
2248186,
2248187,
2248188,
2248190,
2248195,
2248199,
2248200,
2253465,
2253466,
2253467,
2256700,
2256704,
2256705,
2256706,
2256707,
2269382,
2269383,
2269384);

UPDATE pergunta_requerimento 
SET ind_excluido = TRUE 
WHERE ide_pergunta_requerimento IN (2244976,
2244979,
2244989,
2248177,
2248178,
2248179);

--[34979] - Pergunta de requerimento duplicada

UPDATE pergunta_requerimento 
SET ind_excluido = TRUE 
WHERE ide_pergunta_requerimento IN (2244183,
2244184,
2244185,
2244186,
2244187,
2244182,
2244177,
2244179,
2244180,
2244181,
2244075,
2244141,
2246557,
2246558,
2246559,
2246560,
2246561);


--[34821] - Licença de requerimento duplicada

UPDATE
	public.licenca
SET
	ind_excluido = TRUE
WHERE
	ide_licenca = 51042;

-- [34973] Erro ao realizar a análise técnica da ASV

-- Vinculando o imóvel com o shape da notificação:
	UPDATE
		arquivo_processo
	SET
		ide_imovel = 918894
	WHERE
		ide_arquivo_processo = 227501;

-- 'Alimentando' a tabela que vincula o imóvel, o motivo e a própria notificação: 
	INSERT INTO motivo_notificacao_imovel (ide_notificacao_motivo_notificacao,
                                           ide_imovel)                
	SELECT 70109, 918894
	WHERE NOT EXISTS (SELECT 1 FROM motivo_notificacao_imovel WHERE ide_notificacao_motivo_notificacao = 70109);

COMMIT;