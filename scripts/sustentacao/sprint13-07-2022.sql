BEGIN;

--[34335]-[ADMINISTRAÇÃO] Alteração de Usuário

UPDATE
	usuario 
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE 
WHERE
	ide_pessoa_fisica = 971391;

--[34317]-[ADMINISTRAÇÃO] Alteração de Usuário Interno para Usuário Externo

UPDATE
	public.usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE 
WHERE
	ide_pessoa_fisica = 122960;

--[34328]-[PROCESSO] Processo sumiu do sistema

UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE 
WHERE 
	ide_controle_tramitacao IN (551433,551434);

--[34329]-[PROCESSO] Processo sumiu do sistema

UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE 
WHERE	
	ide_controle_tramitacao IN (542493,542494,542495);

--[34228]- Erro ao realizar analise de asv.
UPDATE
	arquivo_processo
SET
	ide_imovel = 140821
WHERE
	ide_arquivo_processo = 207575;
	
--[34338]-Processo sumiu do sistema
UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE 
WHERE 
	ide_controle_tramitacao = 538684;

--[34339]-Processo sumiu do sistema

UPDATE 
        controle_tramitacao
SET 
        ind_fim_da_fila = TRUE 
WHERE 
        ide_controle_tramitacao = 556245;

--[34340]-Processo sumiu do sistema

UPDATE 
        controle_tramitacao
SET 
        ind_fim_da_fila = TRUE 
WHERE 
        ide_controle_tramitacao = 538684;
        
--[34337]-Processo sumiu do sistema

UPDATE 
        controle_tramitacao
SET 
        ind_fim_da_fila = TRUE  
WHERE 
        ide_controle_tramitacao IN (558911,558912);
        
COMMIT;

--[34340]-Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 538684;
                
--[34225] - Erro no redirecionamento de pagina 
        UPDATE declaracao_transporte 
        SET ind_excluido = TRUE 
        WHERE ide_declaracao_transporte IN (
        5957,
        5958);
        DELETE
        FROM
                declaracao_transporte_destinatario_residuo
        WHERE
                ide_declaracao_transporte_destinatario_residuo IN 
        (1919,
        2773,
        3646,
        3732,
        3934,
        4254,
        4256,
        4262,
        3908,
        4275,
        4173,
        4216
        );
        DELETE FROM declaracao_transporte_destinatario_residuo WHERE ide_declaracao_transporte IS NULL;
        CREATE UNIQUE INDEX declaracao_transporte_destinatario_residuo_ide_declaracao_transporte_ide ON
        public.declaracao_transporte_destinatario_residuo (ide_declaracao_transporte);
        ALTER TABLE declaracao_transporte_destinatario_residuo
        DROP COLUMN ind_excluido;

--[34337]-Processo sumiu do sistema
UPDATE 
        controle_tramitacao
SET 
        ind_fim_da_fila = TRUE  
WHERE 
        ide_controle_tramitacao IN (558911,558912);

-- [33557] - Erro ao salvar
        DELETE
        FROM
                        cronograma_etapa
        WHERE
                        ide_cronograma_etapa IN (403122,
                         403123,
                         403124,
                         403125,
                         403126,
                         403127,
                         403128,
                         403129,
                         403130,
                         403131,
                         403132,
                         403133,
                         403134,
                         403135,
                         403136);
        DELETE
        FROM
                        cronograma_recuperacao
        WHERE
                        ide_cronograma_recuperacao = 88722;
        UPDATE
                        reserva_legal
        SET
                        ide_tipo_estado_conservacao = 1
        WHERE
                        ide_reserva_legal = 39795;

--[34339]-Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 556245;
	
--[34338]-Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 538684;

--34240 [PROCESSO] Processo sumiu do sistema
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 557830;

--[34335]-[ADMINISTRAÇÃO] Alteração de Usuário
        UPDATE
                usuario 
        SET
                ide_perfil = 2,
                ind_tipo_usuario = FALSE 
        WHERE
                ide_pessoa_fisica = 971391;
	
--[34329]-[PROCESSO] Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (542493,542494,542495);

--[34328]-[PROCESSO] Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (551433,551434);

--[34317]-[ADMINISTRAÇÃO] Alteração de Usuário Interno para Usuário Externo
        UPDATE
                usuario
        SET
                ide_perfil = 2,
                ind_tipo_usuario = FALSE 
        WHERE
                ide_pessoa_fisica = 122960;

--[34312] - [CADASTRO] Certificado não é gerado
        UPDATE
		requerimento
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 125547;
	
	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE 
	WHERE
		ide_requerimento = 125547;

--[34263] - Erro ao consultar Requerimento
	UPDATE
		licenca
	SET
		ind_excluido = TRUE
	WHERE
		ide_licenca = 49086;
	
	UPDATE
		licenca
	SET
		ind_excluido = FALSE
	WHERE
		ide_licenca = 49078;

--[34259]--[REQUERIMENTO] Emissão de Certificado de Pagamento REPFLOR
        UPDATE
	        tramitacao_requerimento
        SET
	        ide_status_requerimento = 8
        WHERE
	        ide_tramitacao_requerimento = 1287851;
--[34258]--[REQUERIMENTO] Emissão de Certificado de Pagamento REPFLOR
        UPDATE
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE
                ide_tramitacao_requerimento = 1277629;

--[37257] - [CADASTRO] Solicitação de reativação de CEFIR
        UPDATE
                imovel
        SET
                ind_excluido = FALSE
        WHERE
                ide_imovel = 400949;

--[34255] - [REQUERIMENTO] Erro no resumo do requerimento
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_requerimento = 1141321;

--[34242]-Processo sumiu do sistema
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = true
        WHERE
                ide_controle_tramitacao = 558359;

--[34241]-[PROCESSO] Processo sumiu do sistema
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 559372;

--[34228]- Erro ao realizar analise de asv.
        UPDATE
                arquivo_processo
        SET
                ide_imovel = 140821
        WHERE
                ide_arquivo_processo = 207575;
                
--[34347]-Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE  
        WHERE 
                ide_controle_tramitacao = 561032;
COMMIT;

--[34101] - [REQUERIMENTO] Erro ao inserir endereço
BEGIN;
        UPDATE
                declaracao_transporte
        SET
                ind_excluido = TRUE
        WHERE
                ide_declaracao_transporte = 5845;        
        COMMIT;
END;

