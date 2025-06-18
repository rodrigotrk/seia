--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 02/04/2025
--Versão 4.36.1

BEGIN;

--[37282] - Transferência de Pauta
--2022.001.007107/INEMA/LIC-07107
update
        controle_tramitacao
set 
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 827215;        
insert
        into
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 
82573, 12, 38, now(), true, null, 'Solicitação de remanejamento de processo através do chamado Redmine 37282', 3019, false, 1, null);
--2021.001.000385/INEMA/LIC-00385
update
        controle_tramitacao
set 
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 827224;
insert
        into
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 
65549, 12, 38, now(), true, null, 'Solicitação de remanejamento de processo através do chamado Redmine 37282', 3019, false, 1, null);
--2015.001.003034/INEMA/LIC-03034
update
        controle_tramitacao
set 
        ind_fim_da_fila = false 
where
        ide_controle_tramitacao = 827530;
insert
        into
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
9858, 12, 38, now(), true, null, 'Solicitação de remanejamento de processo através do chamado Redmine 37282', 3019, false, 1, null);


--[37276] - Erro na Geração do BOLETO
UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 6
WHERE
        ide_tramitacao_requerimento = 1896986;
        
-- [37270] - Requerimento sumiu do sistema
UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2025.001.0' AS VARCHAR) || 
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
           CAST('/INEMA/REQ' AS VARCHAR)
),
        dtc_finalizacao = (
        SELECT
                dtc_movimentacao
        FROM
                tramitacao_requerimento
        WHERE
                ide_requerimento = 1420032
        ORDER BY
                dtc_movimentacao DESC
        LIMIT 1
)
WHERE
        ide_requerimento = 1420032;
        
--[37253] - Erro ao aprovar a análise técnica
update
        area
set 
        ide_pessoa_fisica = 8229
where
        ide_area = 32;
        
-- [37251] - Erro no fluxo da Notificação
UPDATE
        notificacao
SET
        ind_aprovado = FALSE,
        dtc_envio = NULL
WHERE
        ide_notificacao = 50398;
UPDATE
        notificacao
SET
        ind_aprovado = FALSE,
        dtc_envio = NULL
WHERE
        ide_notificacao = 49577;
        
--[37242] - Erro no fluxo de processos
UPDATE
        public.pauta
SET
        ide_tipo_pauta = 1,
        ide_area = 2,
        ide_pessoa_fisica = 6389
WHERE
        ide_pauta = 1213;
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 843521;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2024.001.010278%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 842976;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2024.001.008586%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 833709;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2024.001.010537%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);
        
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 815620;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2022.001.004094%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 823556;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2023.001.009335%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);        
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 823560;
INSERT
        INTO
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
VALUES(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
(
SELECT
        ide_processo
FROM
        processo
WHERE
        num_processo ILIKE '%2022.001.006399%'),
12, 
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37242',
2884,
TRUE,
1,
NULL);        

--[37233] - Erro no download do Termo de Compromisso CEFIR
DELETE
FROM
        requerimento_imovel
WHERE
        ide_requerimento = 1408781;
DELETE
FROM
        requerimento
WHERE
        ide_requerimento = 1408781;
        

--[37190] - Erro no download do Termo de Compromisso CEFIR
DELETE
FROM
        requerimento_imovel
WHERE
        ide_requerimento = 1411399;
DELETE
FROM
        requerimento
WHERE
        ide_requerimento = 1411399;
        
-- [37187] - Complemento do erro de empreendimento
        ALTER TABLE 
                imovel_urbano 
        ADD COLUMN 
                ind_excluido 
                        boolean DEFAULT FALSE NOT NULL;
                        

--[37154] - Erro no download do Termo de Compromisso CEFIR
DELETE
FROM
        requerimento_imovel
WHERE
        ide_requerimento = 1404327;
DELETE
FROM
        requerimento
WHERE
        ide_requerimento = 1404327;
        
--[37309] -  Transferência de pauta
update
        controle_tramitacao
set
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 822592;
insert
        into
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
83966, 5, 39, now(), true, null, 'Solicitação de remanejamento de processo através do chamado Redmine 37309', 43, false, 1, null);

-- [37307] - Transferência de pauta
update
        controle_tramitacao
set 
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 814257;
update
        controle_tramitacao
set 
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 814258;
insert
        into
        controle_tramitacao
(ide_controle_tramitacao,
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
        ind_area_secundaria)
values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'),
31464, 6, 32, now(), true, null, 'Solicitações à COTIC deverão ser formalizadas por meio de abertura de chamado ou tramitação de processo no SEI.', 1077, false, 1, null);

--[37286] - Transferencia de pauta. 
UPDATE public.controle_tramitacao
SET ide_status_fluxo=12
WHERE ide_controle_tramitacao in (842469, 842470);

-- [37260] - Erro ao gerar Declaração de Inexigibilidade
update
        atividade_inexigivel
set
        permite_endereco = true
where
        ide_atividade_inexigivel = 333;  
        
--[37306] - Erro ao aprovar notificação
update
        public.pauta
set
        ide_tipo_pauta = 2,
        ide_area = null,
        ide_pessoa_fisica = 6389
where
        ide_pauta = 1213;
        
-- [37295] - Erro no download do Termo de Compromisso CEFIR
  UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1416389;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1207584;

-- [37264] Erro ao finalizar o CEFIR
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1205180;
UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1412772;

COMMIT;