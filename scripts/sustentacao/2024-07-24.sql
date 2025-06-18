--Scripts em caráter prioritário
--Data de geração 12/07/2024
--Versão 4.32.9

BEGIN;

--[36534] - Processo sumiu do sistema

update 
	controle_tramitacao
set
	ind_fim_da_fila = true 
where
	ide_controle_tramitacao = 738332;
	
--[36531] Erro ao realizar análise técnica

INSERT
	INTO
	integrante_equipe (
    ide_pessoa_fisica,
	ind_lider_equipe,
	ide_area,
	ide_area_responsavel,
	ide_equipe
)
VALUES (
    51972,
    FALSE,
    76,
    76,
    18761
);

INSERT
	INTO
	processo_ato_integrante_equipe (
    ide_equipe_integrante,
	ide_processo_ato
)
VALUES (
    (
SELECT
	currval('integrante_equipe_seq')),
    35037
);

UPDATE
	public.processo_ato
SET
	ide_processo = 33281,
	ide_ato_ambiental = 24,
	ind_excluido = FALSE,
	dtc_exclusao = NULL,
	ide_tipologia = NULL,
	ide_processo_reenquadramento = NULL
WHERE
	ide_processo_ato = 35037;
	
--[36523] - Alteração de perfil de usuário interno para externo

UPDATE 
	usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE
WHERE 
	ide_pessoa_fisica = 1220672;

-- [36522] Rotograma- DTRP
UPDATE
        documento_obrigatorio
SET
        ind_ativo = TRUE
WHERE
        ide_documento_obrigatorio = 1327
        AND nom_documento_obrigatorio = 'Roteiro programado para o transporte';
        
-- [36505] - Notificação fora do prazo não retornou
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 728061;
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
VALUES(91100,
8,
45,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36505',
115,
FALSE,
1,
NULL);

-- [36504] - Processo sumiu do sistema 
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 779278;
	
-- [36503] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1694859);
    
-- [36502] - Alteração de perfil de usuário interno para externo
UPDATE
	usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE
WHERE
	ide_pessoa_fisica = 3555;
	
--[36501] - Autorização GEOBAHIA 
UPDATE
        perfil_autorizacao_geobahia
SET
        ind_autorizacao = TRUE
WHERE
        ide_perfil = 33;
        
-- [36482] - Erro no valor do boleto - Vistoria
ALTER TABLE parametro_calculo
ALTER COLUMN area_minima TYPE NUMERIC(10,4);

ALTER TABLE parametro_taxa_vistoria
ALTER COLUMN val_area_minima TYPE NUMERIC(10,4);

UPDATE
	parametro_taxa_vistoria
SET
	val_area_minima = 0.0001
WHERE
	ide_parametro_taxa_vistoria = 1;

UPDATE
	parametro_calculo
SET
	area_minima = 0.0001
WHERE
	ide_parametro_calculo = 32704;

-- [36498] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 735747;
        
--[36478] - Erro ao realizar análise técnica
INSERT
        INTO
        integrante_equipe (
    ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe
)
VALUES (
    19476,
    FALSE,
    76,
    76,
    31226
);
INSERT
        INTO
        processo_ato_integrante_equipe (
    ide_equipe_integrante,
        ide_processo_ato
)
VALUES (
    (
SELECT
        currval('integrante_equipe_seq')),
    68975
);
UPDATE
        public.processo_ato
SET
        ide_processo = 64348,
        ide_ato_ambiental = 24,
        ind_excluido = FALSE,
        dtc_exclusao = NULL,
        ide_tipologia = NULL,
        ide_processo_reenquadramento = NULL
WHERE
        ide_processo_ato = 68975;
INSERT
        INTO
        integrante_equipe (
    ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe
)
VALUES (
    19476,
    FALSE,
    76,
    76,
    31228
);
INSERT
        INTO
        processo_ato_integrante_equipe (
    ide_equipe_integrante,
        ide_processo_ato
)
VALUES (
    (
SELECT
        currval('integrante_equipe_seq')),
    68976
);
UPDATE
        public.processo_ato
SET
        ide_processo = 64349,
        ide_ato_ambiental = 24,
        ind_excluido = FALSE,
        dtc_exclusao = NULL,
        ide_tipologia = NULL,
        ide_processo_reenquadramento = NULL
WHERE
        ide_processo_ato = 68976;
        
-- [36467] - Erro ao realizar análise técnica

-- Nº Processo: 2023.001.003599/INEMA/LIC-03599

INSERT
	INTO
	integrante_equipe (
    ide_pessoa_fisica,
	ind_lider_equipe,
	ide_area,
	ide_area_responsavel,
	ide_equipe
)
VALUES (
    19476,
    FALSE,
    76,
    76,
    37912
);

INSERT
	INTO
	processo_ato_integrante_equipe (
    ide_equipe_integrante,
	ide_processo_ato
)
VALUES (
    (
SELECT
	currval('integrante_equipe_seq')),
    96835
);

UPDATE
	processo_ato
SET
	ide_processo = 88630,
	ide_ato_ambiental = 109,
	ind_excluido = FALSE,
	dtc_exclusao = NULL,
	ide_tipologia = NULL,
	ide_processo_reenquadramento = NULL
WHERE
	ide_processo_ato = 96835;
	
-- [36458] - Erro ao realizar análise técnica
INSERT
        INTO
        integrante_equipe
VALUES (
        2789,
        FALSE,
        (
                SELECT
                        max(ide_integrante_equipe)+ 1
                FROM
                        integrante_equipe
        ),
        NULL,
        NULL,
        7037
);
INSERT
        INTO
        processo_ato_integrante_equipe
VALUES (
        (
                SELECT
                        max(ie2.ide_integrante_equipe)
                FROM
                        integrante_equipe ie2
        ),
        68975
);


--[36438] - Erro ao realizar análise técnica
INSERT
        INTO
        integrante_equipe (
    ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe
)
VALUES (
    19476,
    FALSE,
    76,
    76,
    31228
);
INSERT
        INTO
        processo_ato_integrante_equipe (
    ide_equipe_integrante,
        ide_processo_ato
)
VALUES (
    (
SELECT
        currval('integrante_equipe_seq')),
    68976
);
UPDATE
        public.processo_ato
SET
        ide_processo = 64349,
        ide_ato_ambiental = 24,
        ind_excluido = FALSE,
        dtc_exclusao = NULL,
        ide_tipologia = NULL,
        ide_processo_reenquadramento = NULL
WHERE
        ide_processo_ato = 68976;
        
-- [36530] - Transferência de pauta
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE,
	ind_responsavel = FALSE
WHERE
	ide_controle_tramitacao = 723509;

INSERT
	INTO
	controle_tramitacao
(
		ide_controle_tramitacao,
		ide_processo,
		ide_status_fluxo,
		ide_area,
		dtc_tramitacao,
		ind_fim_da_fila,
		dsc_comentario_externo,
		dsc_comentario_interno,
		ide_pauta,
		ind_responsavel,
		ide_pessoa_fisica,
		ind_area_secundaria
	)
VALUES(
	(
		SELECT
			max(ide_controle_tramitacao) + 1
		FROM
			controle_tramitacao
	),
	88267,
	12,
	76,
	now(),
	TRUE,
	NULL,
	'Solicitação de remanejamento de processo através do chamado Redmine 36530',
	2942,
	TRUE,
	2795,
	NULL
);

        

-- [36524] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 27309;
        
--[36519] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 20369;
UPDATE 
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 46891;
        
        
        
-- [36517] - Requerimento sem número
-- Fazenda Córrego do Vinagre (APE) 
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                                  CAST('2024.001.0' AS VARCHAR) || 
                                  CAST((CAST(split_part(split_part((
                SELECT
                                        r.num_requerimento
                FROM
                                        requerimento r
                WHERE
                                        r.num_requerimento IS NOT NULL
                ORDER BY
                                        r.num_requerimento DESC
                LIMIT 1),
                                  '.',
                                   3),
                                   '/',
                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                           ),
        dtc_finalizacao = (
        SELECT
                                  dtc_movimentacao
        FROM
                                  tramitacao_requerimento
        WHERE
                                  ide_requerimento = 1350135
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1350135;
        
-- Faz. Córrego do Vinagre (APE).
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                                  CAST('2024.001.0' AS VARCHAR) || 
                                  CAST((CAST(split_part(split_part((
                SELECT
                                        r.num_requerimento
                FROM
                                        requerimento r
                WHERE
                                        r.num_requerimento IS NOT NULL
                ORDER BY
                                        r.num_requerimento DESC
                LIMIT 1),
                                  '.',
                                   3),
                                   '/',
                                   1) AS INTEGER) + 1 ) AS VARCHAR) || 
                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO
                           ),
        dtc_finalizacao = (
        SELECT
                                  dtc_movimentacao
        FROM
                                  tramitacao_requerimento
        WHERE
                                  ide_requerimento = 1347037
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1347037;
        
-- [36506] - Notificação fora do prazo não retornou
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 729834;
INSERT
        INTO
        controle_tramitacao ( ide_processo,
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
VALUES(73689,
8,
2,
now(),
TRUE,
NULL, 
'Solicitação de remanejamento de processo através do chamado Redmine 36506',
247,
FALSE,
1,
NULL);

-- [36493] Erro no fluxo da Notificação
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 778118;
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 778117;

COMMIT