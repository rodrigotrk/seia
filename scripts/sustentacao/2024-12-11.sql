--Scripts em caráter prioritário
--Data de geração 02/10/2024
--Versão 4.33.7

BEGIN;
-- [36806] - Ajustes Pauta COTIC
        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 793171;

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
        VALUES (89197,
        12,
        38,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        211,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 598542;

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
        VALUES (937,
        12,
        39,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        205,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 605027;

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
        VALUES (24764,
        12,
        39,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        205,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 806374;

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
        VALUES (92764,
        12,
        38,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        211,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 695925;

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
        VALUES (31463,
        19,
        52,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        753,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 732664;

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
        VALUES (31464,
        19,
        52,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        753,
        FALSE,
        1,
        NULL);

        UPDATE
            controle_tramitacao
        SET
            ind_fim_da_fila = FALSE
        WHERE
            ide_controle_tramitacao = 598547;

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
        VALUES (990,
        12,
        39,
        now(),
        TRUE,
        NULL,
        'Solicitação de remanejamento de processo através do chamado Redmine 36806 ',
        205,
        FALSE,
        1,
        NULL);
        
-- [36777] Transferência de Pauta
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 794664;
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(88697, 12, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36777', 247, FALSE, 1, NULL);

-- [36782] - Transferência de Pauta
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_processo = 87631;

INSERT
	INTO
	public.controle_tramitacao
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
VALUES(87631,
12,
76,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36782',
2942,
TRUE,
NULL,
NULL);

-- [36791] - Erro em Efetuar Validação Prévia
update
	documento_obrigatorio
set
	ind_ativo = true
where
	ide_documento_obrigatorio = 9978;
	
-- [36802] - Correção de usuário para Administrador
UPDATE
        hist_historico
SET
        ide_usuario = 1
WHERE
        ide_historico = 54735763;
UPDATE
        hist_historico
SET
        ide_usuario = 1
WHERE
        ide_historico = 54569340;
        
-- [36812] Nova execução para o Ticket #36808
UPDATE
        controle_tramitacao
SET
        dsc_comentario_interno = 'Solicitação de remanejamento de processo através do chamado Redmine 36808.'
WHERE
        ide_controle_tramitacao = 811336
        AND ide_processo = 92288;
       
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 813523;
        
INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES(92288, 12, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36808. Conforme e-mail enviado em 27/09/2024, as solicitações de análise em questão tramitaram por meio dos processos SEI 027.2179.2024.0003692-55 Fazenda Invernada I (Mat. 2236) e 027.2179.2024.0003691-74 Fazenda Alegre (Mat. 3463).', 2884, FALSE, 1, NULL);

-- [36807] Cadastro de espécies
-- Nome científico: Cenostigma laxiflorum
-- Nome popular: Catingueira
INSERT
	INTO
	especie_supressao (nom_especie_supressao,
	ind_ativo)
VALUES ('Cenostigma laxiflorum',
TRUE);

INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('Catingueira');

INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));
-- Nome popular: Catinga-de-porco
INSERT
	INTO
	nome_popular_especie (nom_popular_especie)
VALUES ('Catinga-de-porco');

INSERT
	INTO
	especie_supressao_nome_popular_especie (ide_nome_popular_especie,
	ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'),
currval('especie_supressao_seq'));

-- [36776] - Retorno de status da RL
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
4725,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 4725;

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
4727,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 4727;
	
-- [36845] - Erro no fluxo de processos
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 810335;

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
VALUES(
	71204,
	19,
	43,
	now(),
	TRUE,
	NULL,
	'Fluxo alterado através do pedido Redmine #36845',
	1254,
	FALSE,
	2499,
	NULL
);

-- [36780] - Processo consulta indisponível
--Gerado cópias indevidas de reenquadramento por stutter no sistema
DELETE
FROM
	reenquadramento_tipologia_empreendimento
WHERE
	ide_reenquadramento_processo_ato = 4309;

DELETE
FROM
	reenquadramento_processo_ato
WHERE
	ide_reenquadramento_processo = 1561;

DELETE
FROM
	finalidade_reenquadramento_processo
WHERE
	ide_reenquadramento_processo = 1561;

DELETE
FROM
	reenquadramento_processo
WHERE
	ide_reenquadramento_processo = 1561;

-- [36847] Zerar RL - FAZENDA POUCO TEMPO (matricula 3916)
UPDATE
        reserva_legal
SET
        ide_imovel_rural = NULL
WHERE
        ide_reserva_legal = 6877;

-- [36819] - [PROCESSO] Transferência de Titularidade

UPDATE
	requerimento_pessoa
SET
	ide_pessoa = 691117
WHERE
	ide_requerimento = 8487
	AND ide_pessoa = 1258;

UPDATE
	requerimento
SET
	nom_contato = 'Nova Petróleo S.A - Exploração e Produção'
WHERE
	ide_requerimento = 8487;    
	
-- [36814] - Pagamento REPFLOR
UPDATE
        tramitacao_requerimento
SET
        ide_status_requerimento = 8
WHERE
        ide_tramitacao_requerimento IN (
        1820455,
        1763467,
        1810969,
        1809919,
        1763472,
        1814430,
        1822905,
        1826391,
        1762659,
        1773962,
        1829874,
        1830177
    );
    
-- [36850] Processo não retornou para a pauta da técnica

	UPDATE
		controle_tramitacao
	SET
		ide_pauta = 756
	WHERE
		ide_controle_tramitacao = 807619;    

-- [36854] - Erro ao incluir procurador de pessoa jurídica
UPDATE
	pessoa_fisica
SET
	ide_pais = 1
WHERE
	num_cpf = '40846016400';

UPDATE
	procurador_representante
SET
	ind_excluido = TRUE
WHERE
	ide_procurador = 1326064
	AND ide_pessoa_juridica = 1327566;


-- [36858] PROCESSO - Processos sumidos

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao IN (752659, 756137, 769843, 743447, 764903, 773474, 794123, 622211, 813523);
	       
	--NOUT
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(88230, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 2942, FALSE, 1, FALSE);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(85248, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 2942, FALSE, 1, FALSE);
	
	--COASP
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(89979, 12, 39, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 205, FALSE, 1, FALSE);
	
	--DIREG
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(92149, 12, 1, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 156, FALSE, 1, FALSE);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(85584, 12, 1, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 156, FALSE, 1, FALSE);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(87294, 12, 1, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 156, FALSE, 1, FALSE);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(87299, 12, 1, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 156, FALSE, 1, FALSE);
	
	--COIND
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(85330, 8, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 115, FALSE, 1, FALSE);
	INSERT INTO controle_tramitacao
	(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES(85712, 8, 45, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36858', 115, FALSE, 1, FALSE);    
    
-- [36867] - Transferência de Pauta
insert
	into
	public.controle_tramitacao
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
107767,
12,
19,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36867',
2884,
false,
1,
null);

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
79596,
12,
19,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36867',
2884,
false,
1,
null);

update
	controle_tramitacao
set 
	ind_fim_da_fila = false 
where
	ide_controle_tramitacao = 815678;

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 796050;

-- [36843] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
	    ide_pauta=514,
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 801155;    

-- [36827] - Processo em pauta incorreta
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
98889,
12,
43,
now(),
true,
null,
'Solicitação de remanejamento de processo através do chamado Redmine 36827',
1254,
false,
1,
null);

update
	controle_tramitacao
set
	ind_fim_da_fila = false
where
	ide_controle_tramitacao = 813198;

-- [36820] - INCLUSÃO DE CATEGORIA
INSERT
	INTO
	objetivo_atividade_manejo
VALUES (
	8,
	'Alienar animais da fauna silvestre e da fauna exótica vivos, provenientes de criadouros legalmente autorizados',
	TRUE
);

INSERT
	INTO
	objetivo_atividade_manejo
VALUES (
	9,
	'Alienar partes, produtos e subprodutos da fauna silvestre ou exótica',
	TRUE
);

INSERT
	INTO
	tipo_criadouro_fauna
VALUES (
	6,
	'Empreendimento comercial de animais vivos da fauna silvestre ou fauna exótica',
	TRUE
);

INSERT
	INTO
	tipo_criadouro_fauna
VALUES (
	7,
	'Empreendimento comercial de partes, produtos e subprodutos da fauna silvestre ou exótica',
	TRUE
);
	
	
COMMIT;