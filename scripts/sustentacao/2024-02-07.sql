--Scripts em caráter prioritário
--Data de geração 24/01/2024
--Versão 4.30.4

BEGIN;

-- [36005] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (688725, 688724, 688723);
		
-- [36000] Imóvel Rural sumiu do sistema

	UPDATE
		pessoa_imovel
	SET
		ind_excluido = FALSE,
		dtc_exclusao = NULL
	WHERE
		ide_pessoa_imovel = 1613495;
		
-- [35999] Imóvel Rural sumiu do sistema

	UPDATE
		imovel_rural
	SET
		ide_requerente_cadastro = 1199519
	WHERE
		ide_imovel_rural = 1271583;
		
	INSERT INTO pessoa_imovel
	(ide_pessoa, ide_imovel, dtc_criacao, ind_excluido, ide_tipo_vinculo_imovel, dtc_exclusao, ide_tipo_vinculo_pct, dsc_tipo_vinculo_pct_outros)
	VALUES(1199519, 1271583, now(), FALSE, 1, NULL, NULL, NULL);
	
--[35991] - Imóvel Rural sumiu do sistema
UPDATE imovel_rural 
SET ide_requerente_cadastro = 939541
WHERE ide_imovel_rural = 555530;

INSERT
        INTO
        pessoa_imovel
(ide_pessoa_imovel,
        ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'),
939541,
555530,
now(),
FALSE,
2,
NULL,
NULL,
NULL);

--[35986] - Imóvel Rural sumiu do sistema
UPDATE
	imovel_rural
SET
	ide_requerente_cadastro = 1237267
WHERE
	ide_imovel_rural = 91134;

INSERT
        INTO
        pessoa_imovel
(ide_pessoa_imovel,
        ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'),
1237267,
91134,
now(),
FALSE,
1,
NULL,
NULL,
NULL);

-- [35980] Transferência de Pauta
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 693589;

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
VALUES (94089,
12,
39,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 35980',
205,
FALSE,
1,
NULL);

-- [35967] - Retorno de status da RL
UPDATE
	public.reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal IN (828512);

INSERT
	INTO
	hist_historico (data_historico,
	acao_historico,
	ip_historico,
	ide_usuario)
VALUES (now(),
'UPD',
'201.50.135.226',
1);

INSERT
	INTO
	hist_valor (val_valor,
	ide_campo,
	ide_registro,
	ide_historico)
VALUES (3,
195,
828512,
currval('historico_ide_historico_seq'));

--[35762] - Erro de atualização do resumo

DELETE
FROM
        requerimento_tipologia
WHERE
        ide_requerimento_tipologia = 84896;
        
--[36011] - Processo duplicado
UPDATE
	public.controle_tramitacao
SET
	ide_processo = 77787,
	ide_status_fluxo = 7,
	ide_area = 3,
	dtc_tramitacao = '2024-01-23 09:35:51.658',
	ind_fim_da_fila = FALSE,
	dsc_comentario_externo = NULL,
	dsc_comentario_interno = NULL,
	ide_pauta = 7,
	ind_responsavel = FALSE,
	ide_pessoa_fisica = NULL,
	ind_area_secundaria = NULL
WHERE
	ide_controle_tramitacao = 734523;
	
-- [35985] Erro ao baixar relatório da notificação
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao IN (725340, 725339);
        
-- [35894] Erro ao realizar Transferência de Pauta
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 698687;
        
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(46280, 4, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35894', 2884, FALSE, 1, NULL);

-- [36006] - Transferência de Titularidade
UPDATE 
        requerimento
SET
        nom_contato = 'AGROMYNERA EMPREENDIMENTOS LTDA'
WHERE 
        ide_requerimento = 1210467;
        
UPDATE 
        requerimento_pessoa
SET
        ide_pessoa = 1183604
WHERE 
        ide_requerimento = 1210467 AND ide_tipo_pessoa_requerimento = 1;

--[36015] - Imóvel Rural sumiu do sistema
INSERT
	INTO
	public.pessoa_imovel
(ide_pessoa_imovel,
	ide_pessoa,
	ide_imovel,
	dtc_criacao,
	ind_excluido,
	ide_tipo_vinculo_imovel,
	dtc_exclusao,
	ide_tipo_vinculo_pct,
	dsc_tipo_vinculo_pct_outros)
VALUES(nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'::text::regclass),
338215,
970191,
now(),
FALSE,
1,
NULL,
NULL,
NULL);

UPDATE
	imovel_rural
SET
	ide_requerente_cadastro = 338215
WHERE
	ide_imovel_rural = 970191 ;
	
-- [36001] - Realocação, Zerar RL

UPDATE
        reserva_legal
SET
        ide_imovel_rural = NULL
WHERE
        ide_reserva_legal =50721
        
COMMIT;