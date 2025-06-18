--Scripts em caráter prioritário
--Data de geração 06/03/2025
--Versão 4.35.7

BEGIN;

-- [37186] - Alteração de perfil de usuário interno para externo

update

        usuario

set

        ide_perfil = 2,

        ind_tipo_usuario = false

where

        ide_pessoa_fisica = 29067;

 

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (701296,701297,701298,701299);

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

71620,

6,

43,

now(),

true,

null,

'Solicitação de remanejamento de processo através do chamado Redmine 37186.',

1056,

true,

1,

null);

-- [37088] - Erro no fluxo da Notificação

UPDATE

        notificacao

SET

        ind_aprovado = FALSE,

        dtc_envio = NULL

WHERE

        ide_notificacao = 49577;

-- [37170] - Levantamento de Processos
-- NOUT

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (646100,

586013,

814765,

804124);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699842;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (69314, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699873;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (77988, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =700468;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (88898, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699848;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (49687, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699858;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (57312, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =700460;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (95857, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699957;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (72837, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =700503;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (94733, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699912;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (81284, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699910;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (79591, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =699857;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (49382, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =785052;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (65810, 8, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =700471;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (90875, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 144783, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =764569;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (76350, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 2791, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =578539;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80755, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2942, FALSE, 980982, NULL);

--COINS

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =723900;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (74893, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =725532;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (78636, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =739210;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80412, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =724670;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (86527, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =731985;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (84204, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =731639;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (65042, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =704282;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (74892, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =748625;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (77248, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =736920;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (68018, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =723900;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (74893, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =725532;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (78636, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =739210;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (80412, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =724670;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (86527, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =731985;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (84204, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =731639;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (65042, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =704282;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (74892, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =748625;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (77248, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =736920;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (68018, 12, 8, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 215, FALSE, 973429, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (660187,

688105);

--COFAQ

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =745484;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (55031, 12, 9, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 354, FALSE, 3650, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (812061, 812062, 745482);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (799942, 776183);

--URPST

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =390570;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (9807, 12, 42, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 2316, FALSE, 34484, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =803592;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (84610, 12, 43, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 1254, FALSE, 8527, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (779636,

803592);

--COIND

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (726991,

728672,

788379);

--COMIN

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =730262;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (13456, 6, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 224, FALSE, 1528, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (465233,

730262,

816234,

730264,

826137,

789200,

793227);

--COINE

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =827518;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (75196, 12, 6, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 810, FALSE, 998344, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (48645);

--UROTS

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =567702;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (10617, 12, 47, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 346, FALSE, 28665, NULL);

UPDATE   controle_tramitacao SET  ind_fim_da_fila = FALSE WHERE ide_controle_tramitacao =542029;

INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria) VALUES (2643, 12, 47, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 37170 ', 346, FALSE, 28665, NULL);

update

        controle_tramitacao

set

        ind_fim_da_fila = false

where

        ide_controle_tramitacao in (836996,

796001);

-- [37062] - Erro ao gerar Declaração de Inexigibilidade

UPDATE

        atividade_inexigivel_modelo_certificado_inexigibilidade

SET

        ide_modelo_certificado_inexigibilidade = 8

WHERE

        ide_atividade_inexigivel = 384;



-- [37199] - Requerimento sumiu do sistema

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

                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO

                           ),

        dtc_finalizacao = (

        SELECT

                dtc_movimentacao

        FROM

                tramitacao_requerimento

        WHERE

                ide_requerimento = 1412788

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

                          )

WHERE

        ide_requerimento = 1412788;
        
-- [37202] - Requerimento sumiu do sistema

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

                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO

                           ),

        dtc_finalizacao = (

        SELECT

                dtc_movimentacao

        FROM

                tramitacao_requerimento

        WHERE

                ide_requerimento = 282509

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

                          )

WHERE

        ide_requerimento = 282509;
        
-- [37206] - Requerimento sumiu do sistema

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

                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO

                           ),

        dtc_finalizacao = (

        SELECT

                dtc_movimentacao

        FROM

                tramitacao_requerimento

        WHERE

                ide_requerimento = 1413035

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

                          )

WHERE

        ide_requerimento = 1413035;
        
-- [37197] - Requerimento sumiu do sistema

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

                ide_requerimento = 1243803

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

)

WHERE

        ide_requerimento = 1243803;

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

                ide_requerimento = 1414101

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

)

WHERE

        ide_requerimento = 1414101;

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

                ide_requerimento = 1411008

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

)

WHERE

        ide_requerimento = 1411008;
        


-- [37200] - Requerimento sumiu do sistema

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1414193

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1414193;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1413035

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1413035;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1414184

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1414184;

        
-- [37204] - Requerimento sumiu do sistema

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 281910

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 281910;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1414350

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1414350;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1414210

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1414210;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1411346

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1411346;

UPDATE

        requerimento

SET

        num_requerimento = (

        SELECT

                CAST('2025.001.0' AS VARCHAR) || 

               CAST((CAST(SPLIT_PART(SPLIT_PART((

                SELECT

                        r.num_requerimento

                FROM

                        requerimento r

                WHERE

                        r.num_requerimento IS NOT NULL

                ORDER BY

                        r.num_requerimento DESC

                LIMIT 1

                ),

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

                ide_requerimento = 1414208

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

    )

WHERE

        ide_requerimento = 1414208;

-- [37193] - Requerimento sumiu do sistema

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

                                  CAST('/INEMA/REQ' AS VARCHAR) AS NUM_REQUERIMENTO

                           ),

        dtc_finalizacao = (

        SELECT

                dtc_movimentacao

        FROM

                tramitacao_requerimento

        WHERE

                ide_requerimento = 1413694

        ORDER BY

                dtc_movimentacao DESC

        LIMIT 1

                          )

WHERE

        ide_requerimento = 1413694;
        


--[37213] - Requerimento sumiu do sistema

INSERT

        INTO

        tramitacao_requerimento(ide_status_requerimento,

        ide_requerimento,

        dtc_movimentacao,

        ide_pessoa)

VALUES(14, 

1397702, 

'2025-01-06 11:11:10.176',

16272);

UPDATE

        requerimento

SET

        dtc_finalizacao = '2025-01-06 11:11:10.176'

WHERE

        ide_requerimento = 1397702;

INSERT

        INTO

        requerimento_pessoa (

        ide_requerimento,

        ide_pessoa,

        ide_tipo_pessoa_requerimento,

        ind_solicitante,

        ind_usuario_logado)

VALUES(1397702,

16272,

1,

'true',

'true');

INSERT

        INTO

        empreendimento_requerimento (

        ide_requerimento,

        ide_empreendimento,

        ide_fase_empreendimento,

        dtc_fase_empreendimento,

        ide_porte,

        ide_classe,

        ind_dla,

        num_processo_ana,

        num_portaria_ana,

        num_vazao_total,

        ide_programa_governo)

VALUES(1397702,

17358,

3,

NULL,

2,

1,

FALSE,

NULL,

NULL,

NULL,

NULL);

--[37217] - Requerimento sumiu do sistema

INSERT

        INTO

        tramitacao_requerimento(ide_status_requerimento,

        ide_requerimento,

        dtc_movimentacao,

        ide_pessoa)

VALUES(14, 

1398091, 

'2025-01-07 12:18:54.764',

16272);

UPDATE

        requerimento

SET

        dtc_finalizacao = '2025-01-07 12:18:54.764'

WHERE

        ide_requerimento = 1398091;

INSERT

        INTO

        requerimento_pessoa (

        ide_requerimento,

        ide_pessoa,

        ide_tipo_pessoa_requerimento,

        ind_solicitante,

        ind_usuario_logado)

VALUES(1398091,

16272,

1,

'true',

'true');

INSERT

        INTO

        empreendimento_requerimento (

        ide_requerimento,

        ide_empreendimento,

        ide_fase_empreendimento,

        dtc_fase_empreendimento,

        ide_porte,

        ide_classe,

        ind_dla,

        num_processo_ana,

        num_portaria_ana,

        num_vazao_total,

        ide_programa_governo)

VALUES(1398091,

54792,

3,

NULL,

2,

1,

FALSE,

NULL,

NULL,

NULL,

NULL);

COMMIT;