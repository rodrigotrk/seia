--[34214]-Processo sumiu do sistema

begin;

update
	controle_tramitacao
set
	ind_fim_da_fila = true
where
	ide_controle_tramitacao in (542163,542164,542165,542166);

commit;
end;
--34207 - [CADASTRO] Erro ao tentar finalizar cadastro de Imóvel Rural
BEGIN;
        DELETE
        FROM
                cronograma_recuperacao
        WHERE
                ide_cronograma_recuperacao = 82019;
        COMMIT;
        UPDATE
                app
        SET
                ide_tipo_estado_conservacao = 1
        WHERE
                ide_app = 119716;
        COMMIT;
        UPDATE
                imovel_rural
        SET
                num_folha = 'Sem informação'
        WHERE
                ide_imovel_rural = 501214;
        COMMIT;
--[34183] - Erro na finalização do CEFIR

BEGIN;

	UPDATE
		requerimento
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 1143935;
	
	COMMIT;
	
	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 1143935;
	
	COMMIT;
END;
-- [29732] - Adição ind_excluido fce_linha_energia
BEGIN;

ALTER TABLE fce_linha_energia
ADD COLUMN ind_excluido boolean NOT NULL DEFAULT FALSE;

UPDATE
	fce_linha_energia
SET
	ind_excluido = TRUE
WHERE
	ide_fce_linha_energia IN (80,
81,
82,
86,
88);

COMMIT;
--34210 - [REQUERIMENTO] Emissão de Certificado de Pagamento REPFLOR
BEGIN;

	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1287078;
	COMMIT;
END;
-- [33839] - Correção ponto duplicado DIAP
BEGIN;

UPDATE 
	dado_geografico 
SET 
	ide_localizacao_geografica = 10527
WHERE 
	ide_dado_geografico = 2115602;
	
COMMIT;
