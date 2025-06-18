--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 17/04/2024
--Versão 4.30.6

BEGIN;

-- [36224] - Requerimento sem número
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
                                  ide_requerimento = 1324005
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1324005;
        
-- [36219] - Pagamento REPFLOR
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (1688982, 1698334, 1691609, 1698232, 1696735);
    
-- [36200] Erro ao aprovar notificação
UPDATE
        controle_tramitacao
SET
        ide_area = 44,
        ide_pauta = 2826
WHERE
        ide_controle_tramitacao = 750504;
        
-- [36192] Erro ao realizar análise técnica
INSERT INTO equipe
(ide_equipe, dtc_formacao_equipe, dtc_exclusao_equipe, ind_ativo, ide_area, ide_area_responsavel, ide_processo)
VALUES(nextval('equipe_seq'), now(), NULL, TRUE, 76, NULL, 98092);
INSERT INTO integrante_equipe
(ide_pessoa_fisica, ind_lider_equipe, ide_integrante_equipe, ide_area, ide_area_responsavel, ide_equipe)
VALUES(23939, TRUE, nextval('integrante_equipe_seq'), 76, 76, currval('equipe_seq'));
INSERT INTO processo_ato_integrante_equipe 
(ide_equipe_integrante, ide_processo_ato)
VALUES(currval('integrante_equipe_seq'), 107499);

-- [36046] - Erro ao aprovar notificação
INSERT INTO controle_tramitacao
(ide_controle_tramitacao, ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 8835, 12, 8, now(), true, NULL, 'Correção do status do processo via atendimento por meio do ticket 36046', 215, false, 1, NULL);
UPDATE controle_tramitacao
SET ind_fim_da_fila=false
WHERE ide_controle_tramitacao=725403;

-- [36044] Erro na emissão do certificado
UPDATE
        certificado
SET
        ide_requerimento = NULL
WHERE
        ide_certificado = 1432676;

COMMIT;