
--[34117] - Erro ao consultar Requerimento
BEGIN;
        UPDATE
                licenca
        SET
                ind_excluido = TRUE 
        WHERE
                ide_requerimento = 1140418;
COMMIT;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE 
        WHERE
                ide_licenca = 48193;
COMMIT;
END;

-- [34055] - Retirando duplicata de processo
BEGIN;

UPDATE
	processo
SET
	ind_excluido = TRUE,
	ide_requerimento = NULL
WHERE
	ide_processo = 78668;

COMMIT;
--Ticket #34121
--Retorno do requerimento para o status Validado (5)
UPDATE 
	tramitacao_requerimento 
SET  
	ide_status_requerimento = 5 
WHERE  
	ide_tramitacao_requerimento = 1279344;
		
--Mudança do boleto para o status Pago(3)
UPDATE 
	hist_situacao_dae
SET
	ide_situacao_dae = 3
WHERE
	ide_dae = 119;

--[34119] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao IN (550992,550993);
        COMMIT;
END;
--[34118] - Erro ao consultar Requerimento
BEGIN;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_requerimento = 1147033;
        
        COMMIT;
END;
--[34112] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao IN (517232,517233);
        COMMIT;
END;
--[34111] - Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 551804;
        COMMIT;
END;
--[34109] - Erro ao consultar Requerimento
BEGIN;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_requerimento = 1113344;
        
        COMMIT;
END;
--[34102] - Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 551701;
        COMMIT;
END;
-- [34098] - Alteração unique DTRP
BEGIN;

DELETE
FROM
	declaracao_transporte_destinatario_residuo
WHERE
	ide_declaracao_transporte_destinatario_residuo IN 
(1919,
2773,
3646,
3934,
4254,
4256
);

CREATE UNIQUE INDEX declaracao_transporte_destinatario_residuo_ide_declaracao_transporte_idx ON
public.declaracao_transporte_destinatario_residuo (ide_declaracao_transporte);

ALTER TABLE declaracao_transporte_destinatario_residuo
DROP COLUMN ind_excluido;

COMMIT;
	
	
-- [34090] -  Conserto DTRP e exclusão de excedentes

BEGIN;

UPDATE
	declaracao_transporte_destinatario_residuo
SET
	ind_excluido = TRUE
WHERE
	ide_declaracao_transporte_destinatario_residuo IN (1919,
2773,
3646,
3934);BEGIN;

UPDATE
	declaracao_transporte_destinatario_residuo
SET
	ind_excluido = TRUE
WHERE
	ide_declaracao_transporte_destinatario_residuo IN (1919,
2773,
3646,
3934);

ALTER TABLE declaracao_transporte 
ADD COLUMN 
ind_excluido BOOLEAN
DEFAULT FALSE
NOT NULL;

UPDATE declaracao_transporte SET ind_excluido = true 
WHERE ide_declaracao_transporte IN (5896,
5892,
5810,
5898,
5856,
5911,
6026,
5982);

COMMIT;
-- [33921] - Adição de índice excluído em finalidade de outorga
BEGIN;

ALTER TABLE 
	outorga_localizacao_geografica_finalidade
ADD COLUMN 
	ind_excluido BOOLEAN 
	DEFAULT FALSE
	NOT NULL ;

UPDATE
	outorga_localizacao_geografica_finalidade
SET
	ind_excluido = TRUE
WHERE
	ide_outorga_localizacao_geografica_finalidade = 60935;

COMMIT;

--[34156] - [PROCESSO] Correção nos Dados Gerais do Processo - Troca de Titularidade

BEGIN;
	UPDATE
		requerimento
	SET
		nom_contato = 'GRATT AGROPECUARIA EIRELI'
	WHERE
		ide_requerimento = 618793;
	       
	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 746389
	WHERE
		ide_requerimento = 618793
	AND 
		ide_pessoa = 11928;
	COMMIT;
END;	
	

	
-- [33763] - Erro ao preencher FCE de reenquadramento

BEGIN
	
UPDATE
	public.documento_ato
SET
	ide_documento_obrigatorio = 3000,
	ide_ato_ambiental = 2,
	ide_tipologia = 43,
	ide_tipo_finalidade_uso_agua = NULL,
	ind_ativo = FALSE
WHERE
	ide_documento_ato = 7399;
	
COMMIT;


--[34099] - [REQUERIMENTO] Erro ao inserir endereço
BEGIN;
        UPDATE
                declaracao_transporte
        SET
                ind_excluido = TRUE
        WHERE
                ide_declaracao_transporte IN (5957, 5958);        
        COMMIT;
END;
