--Scripts em caráter prioritário
--Data de geração 30/01/2023
--Versão 4.26.6

BEGIN;

-- [35020] Falha ao inserir CEP do Empreendimento

	UPDATE
		bairro
	SET
		ide_municipio = 871
	WHERE
		ide_bairro = 133665;
-- [34995] - Pagamento REPFLOR
    UPDATE
            tramitacao_requerimento
    SET
            ide_status_requerimento = 8
    WHERE
            ide_tramitacao_requerimento = 1419731;
-- [34830] Erro ao solicitar Regulação Ambiental do Empreendimento

	UPDATE
		documento_imovel_rural_posse
	SET
		ide_documento_imovel_rural = NULL
	WHERE
		ide_imovel_rural = 1217870
		AND ide_documento_imovel_rural_posse = 1129289;
-- [34468] Solicitação de exclusão de técnicos 
ALTER TABLE public.funcionario ADD ind_excluido boolean NOT NULL DEFAULT FALSE;

UPDATE
	funcionario
SET
	ind_excluido = TRUE
WHERE
	ide_pessoa_fisica IN (65105, 539939, 659048, 63019, 528262, 10395, 772866);


COMMIT;