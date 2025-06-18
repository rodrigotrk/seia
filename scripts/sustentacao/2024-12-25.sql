--Scripts em caráter prioritário
--Data de geração 11/12/2024
--Versão 4.34.5

BEGIN;

--[36889] - Transferência de Pauta

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_processo = 108916;
	
INSERT
	INTO
	controle_tramitacao
(ide_processo,
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
VALUES(108916,
12,
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36889',
2884,
FALSE,
1,
NULL);

-- [36888] - Conversão para Usuário Externo
update
	usuario
set
	ide_perfil = 2,
	ind_tipo_usuario = false
where
	ide_pessoa_fisica = 1234110;
	
-- [36877] - Transferência de Titularidade
update
	requerimento
set
	nom_contato = 'IFQ PARTICIPAÇÕES S.A'
where
	num_requerimento = '2023.001.086619/INEMA/REQ';

update
	requerimento_pessoa
set
	ide_pessoa = 14632
where
	ide_requerimento = 1290608
	and ide_pessoa = 25048;

update
	pessoa_juridica
set 
	nom_razao_social = 'IFQ PARTICIPAÇÕES S.A'
where
	ide_pessoa_juridica = 14632;

-- [36852] - Erro no fluxo da Notificação
UPDATE
	notificacao
SET
	ind_aprovado = FALSE,
	dtc_envio = NULL
WHERE
	ide_notificacao = 48434;
	
COMMIT;