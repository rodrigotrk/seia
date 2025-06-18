--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 21/05/2025 09:40:17
--Versão 4.36.0

BEGIN;

UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 8
WHERE
        ide_tramitacao_requerimento IN (1922386,
1930986,
1940894,
1930607,
1924093,
1933213,
1932041,
1937665,
1921373,
1760519,
1930352,
1932021,
1926871,
1929620,
1943552,
1827329,
1946803,
1905171,
1904835,
1936283);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 854644;
        
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 594687;
INSERT
        INTO
        controle_tramitacao
(
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
VALUES
(
    6546,
    12,
    52,
    now(),
    TRUE,
    NULL,
    'Conforme sinalizado anteriormente em comentário do dia 09/04, solicitações à COTIC deverão ser formalizadas por meio de abertura de chamado ou tramitação de processo no SEI.',
    753,
    TRUE,
    1,
    NULL
);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 594686;
INSERT
        INTO
        controle_tramitacao
(
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
VALUES
(
    7041,
    12,
    2,
    now(),
    TRUE,
    NULL,
    'A solicitação de parecer foi atendida por meio do processo SEI (inserir o número do processo). Reforçamos que as próximas solicitações à COTIC devem ser encaminhadas por meio de chamado ou por meio de processo SEI-BA.',
    2884,
    TRUE,
    1,
    NULL
);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 848898;
INSERT
        INTO
        controle_tramitacao
(
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
VALUES
(
    31464,
    12,
    2,
    now(),
    TRUE,
    NULL,
    'A solicitação de parecer foi atendida por meio do processo SEI 046.0573.2025.0010276-60. Reforçamos que as próximas solicitações à COTIC devem ser encaminhadas por meio de chamado ou por meio de processo SEI-BA.',
    2884,
    TRUE,
    1,
    NULL
);
update
        enquadramento
set
        ide_requerimento = null
where
        ide_enquadramento = 221258;
        
UPDATE
        requerimento_pessoa
SET
        ide_pessoa = 64537
WHERE
        ide_requerimento = 794881;
            
UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1412408;
UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1412409;
UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1412410;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1204857;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1204858;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1204859;
UPDATE
        requerimento_imovel
SET
        ide_requerimento = 1412411
WHERE
        ide_imovel = 1448694
        AND ide_tipo_imovel_requerimento = 2
        AND ide_requerimento IN (1412408, 1412409, 1412410);
        
UPDATE
        controle_tramitacao ct
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 852690;
        

COMMIT;
