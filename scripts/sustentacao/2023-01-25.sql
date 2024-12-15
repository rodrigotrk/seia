--Scripts em caráter prioritário
--Data de geração 11/01/2023
--Versão 4.26.5

BEGIN;

-- [34966] Imóvel sumiu do sistema

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE
	WHERE
		ide_pessoa_imovel = 1884206;
-- [34940] Emissão de Certificado de Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1404549;
	
-- [34941] Emissão de Certificado de Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1404550;
	
-- [34943] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1404557;
        
-- [34957] Pagamento REPFLOR

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1393263;
        
-- [34950] Pagamento REPFLOR 

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1393294;
-- [34856] Erro ao gerar Certificado DIAP

	UPDATE
		dado_geografico
	SET
		coord_geo_numerica = 'POINT (8436908.59 421089.17)',
		the_geom = 'POINT (-39.731138467794 -14.137818426020027)'
	WHERE
		ide_dado_geografico = 5304783;
--[34563] -  Erro na aba "Dados Específicos"
UPDATE
        public.reserva_legal
SET
        ide_imovel_rural = NULL
        WHERE
        ide_reserva_legal = 871939;

COMMIT;