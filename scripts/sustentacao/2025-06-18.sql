--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 04/06/2025 11:02:25
--Versão 4.36.0

BEGIN;

UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 8
WHERE
        ide_tramitacao_requerimento IN (1695750);
UPDATE
        controle_tramitacao ct
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 858680;
        
UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 8
WHERE
        ide_tramitacao_requerimento IN (1541175);
        
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 847231;
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
91980,
5,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 37463',
43,
FALSE,
1,
NULL);
update
        controle_tramitacao
set
        ind_fim_da_fila = false
where
        ide_controle_tramitacao = 844216;
insert
        into
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
values (17561,
12,
8,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 37461 ',
215,
false,
1,
null);
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE
WHERE
        ide_requerimento = 1427670;
        
update
        imovel_rural
set
        status_cadastro = 3
where
        ide_imovel_rural = 233314;
        
        update
                controle_tramitacao
        set
                ind_fim_da_fila = false
        where
                ide_controle_tramitacao = 846327;
         
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
        values(nextval('CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq'), 98992, 12, 45, now(), true, null, null, 247, false, 388, null);
        
update
        municipio
set 
        num_cep = 47804332
where
        ide_municipio = 921;

COMMIT;
