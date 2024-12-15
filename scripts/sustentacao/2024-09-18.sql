--Scripts em caráter prioritário
--Data de geração 11/09/2024
--Versão 4.33.5

BEGIN;

-- [36710] - Transferência de Titularidade
INSERT INTO pessoa_imovel (ide_pessoa_imovel,
                            ide_pessoa,
                            ide_imovel,
                            dtc_criacao,
                            ind_excluido,
                            ide_tipo_vinculo_imovel,
                            dtc_exclusao,
                            ide_tipo_vinculo_pct,
                            dsc_tipo_vinculo_pct_outros)
VALUES (nextval('pessoa_imovel_ide_pessoa_imovel_seq'),
       30795,
       1043518,
       now(),
       FALSE,
       1,
       NULL,
       NULL,
       NULL);
      
-- Atualizando o cadastro do imóvel ao novo vínculo:
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 30795
WHERE
        ide_imovel_rural = 1043518;
        
-- [36707] - Conversão para Usuário Externo
UPDATE 
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica = 1124604;
        
-- [36687] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 648510;
        
-- [36685] - Solicitação de exclusão do imóvel Fazenda Reunidas Fortaleza
UPDATE
        imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now(),
        ide_usuario_exclusao = 1
WHERE
        ide_imovel = 671088;
-- [36684] - Solicitação de exclusão do imóvel Fazenda Martins
UPDATE
	imovel
SET
	ind_excluido = TRUE,
	dtc_exclusao = now(),
	ide_usuario_exclusao = 1
WHERE
	ide_imovel = 1217941;
	
-- [36677] - Transferência de Pauta

UPDATE controle_tramitacao SET ind_fim_da_fila = FALSE 
WHERE ide_controle_tramitacao IN (
790880,
792747,
795289,
795696,
795676,
794518,
795668,
794809,
794199);

INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(88728, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(95409, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(98608, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(81561, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, 2499, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(83793, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, 2499, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(96642, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(89281, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, 2499, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(19589, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, NULL, NULL);
INSERT INTO public.controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(91134, 6, 3, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36677', 224, true, 2499, NULL);

-- [36642] - Erro ao solicitar DTRP
DELETE
FROM
        declaracao_transporte_residuo_endereco
WHERE
        ide_declaracao_transporte_residuo_endereco IN (17219,
17220,
17223,
17227,
17229,
17349);

COMMIT;