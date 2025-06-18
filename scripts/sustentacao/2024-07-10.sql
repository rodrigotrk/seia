--Scripts em caráter prioritário
--Data de geração 26/06/2024
--Versão 4.32.7

BEGIN;

-- [36479] - Requerimento sumiu do sistema
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
                                  ide_requerimento = 1335403
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1335403;
        
-- [36460] - Requerimento sem número
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
                                  ide_requerimento = 1343997
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1343997;
       
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
                                  ide_requerimento = 1345367
        ORDER BY
                                  dtc_movimentacao DESC
        LIMIT 1
                          )
WHERE
        ide_requerimento = 1345367;
        
--[36435] - Pagamento REPFLOR

update
  tramitacao_requerimento
set
  ide_status_requerimento = 8
where
  ide_tramitacao_requerimento = 1554607;

--[36451] - Alteração de perfil de usuário interno para externo
UPDATE
	usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE
WHERE
	ide_pessoa_fisica = 18873;
	
--[36412] - Inclusão de espécies

-- Espécie: Holoregmia viscida
-- Nome popular: São-cipriano
INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
VALUES ('Holoregmia viscida', TRUE);

INSERT INTO nome_popular_especie (nom_popular_especie)
VALUES ('São-cipriano');

INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'), currval('especie_supressao_seq'));

COMMIT;

-- Espécie: Pseudobombax parvifolium
-- Nome popular: Imbiruçu
INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
VALUES ('Pseudobombax parvifolium', TRUE);

INSERT INTO nome_popular_especie (nom_popular_especie)
VALUES ('Imbiruçu');

INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'), currval('especie_supressao_seq'));

COMMIT;

-- Espécie: Eugenia caatingicola
-- Nome popular: Araça
INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
VALUES ('Eugenia caatingicola', TRUE);

INSERT INTO nome_popular_especie (nom_popular_especie)
VALUES ('Araça');

INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'), currval('especie_supressao_seq'));

COMMIT;

-- Espécie: Combretum monetaria
-- Nome popular: Vaqueta
INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
VALUES ('Combretum monetaria', TRUE);

INSERT INTO nome_popular_especie (nom_popular_especie)
VALUES ('Vaqueta');

INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'), currval('especie_supressao_seq'));

COMMIT;

-- Espécie: Simira gardneriana
-- Nome popular: Bacupari
INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
VALUES ('Simira gardneriana', TRUE);

INSERT INTO nome_popular_especie (nom_popular_especie)
VALUES ('Bacupari');

INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
VALUES (currval('nome_popular_especie_seq'), currval('especie_supressao_seq'));

-- Cadastro de espécies na lista de espécies nativas do RFP

-- Holoregmia viscida / São-cipriano
INSERT INTO especie_floresta (ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Holoregmia viscida / São-cipriano', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Holoregmia viscida / São-cipriano');

-- Pseudobombax parvifolium / Imbiruçu
INSERT INTO especie_floresta (ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Pseudobombax parvifolium / Imbiruçu', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Pseudobombax parvifolium / Imbiruçu');

-- Eugenia caatingicola / Araça
INSERT INTO especie_floresta (ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Eugenia caatingicola / Araça', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Eugenia caatingicola / Araça');

-- Combretum monetaria / Vaqueta
INSERT INTO especie_floresta (ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Combretum monetaria / Vaqueta', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Combretum monetaria / Vaqueta');

-- Simira gardneriana / Bacupari
INSERT INTO especie_floresta (ide_especie_floresta, nom_especie_floresta, ide_natureza_especie_floresta, dtc_criacao, dtc_exclusao, ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Simira gardneriana / Bacupari', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Simira gardneriana / Bacupari');


---[36466] - [ADMINISTRAÇÃO DE SISTEMAS] Alteração de perfil de usuário interno para externo
update
  usuario
set
  ide_perfil = 2,
  ind_tipo_usuario = false
where
  ide_pessoa_fisica = 2709;
update
  usuario
set
  ide_perfil = 2,
  ind_tipo_usuario = false
where
  ide_pessoa_fisica = 1190166;
  
-- [36468] Erro ao finalizar requerimento - aba 6 em branco

	UPDATE
		fauna
	SET
		ind_excluido = TRUE
	WHERE
		ide_fauna = 85365;
		
--[36469] - Retorno de Status
INSERT INTO tramitacao_requerimento
(ide_tramitacao_requerimento, ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(nextval('TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq'),1, 1209093, now(), 1);
INSERT INTO comunicacao_requerimento
(ide_comunicacao_requerimento, ide_requerimento, dtc_comunicacao, des_mensagem, assunto, ind_enviado)
VALUES(nextval('COMUNICACAO_REQUERIMENTO_IDE_COMUNICACAO_REQUERIMENTO_seq'), 1209093, now(), 'Prezado(a) requerente, 
o status do requerimento foi alterado mediante atendimento à solicitação do ticket #36469.

 Atte.,
SEMA/INEMA.
', NULL, NULL);

-- [36486] Processo sumiu do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 777231;
		
-- [36485] Pagamento REPFLOR

	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1339055, now(), 1); 
	
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1331901, now(), 1);   
	
	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1333605, now(), 1);
	
-- [36480] Status incorreto

-- Alterando o status dos 3 atos (LI, ASV, Manejo):
	UPDATE
		status_processo_ato
	SET
		dtc_status_processo_ato = now(),
		ind_aprovado = TRUE
	WHERE
		ide_status_processo_ato IN (315874, 315875, 315328);
        
-- Corrigindo a aprovação do ato de LI:
	UPDATE
		status_processo_ato
	SET
		ind_aprovado = NULL
	WHERE
		ide_status_processo_ato IN (315817, 315816, 315815);
		
-- [36205] Novos campos vinculado a funcionalidade de desbloqueio
	CREATE TABLE public.imovel_rural_desbloqueio (
		ide_imovel_rural_desbloqueio int4 DEFAULT nextval('DESBLOQUEIO_IMOVEL_RURAL_seq'::text::regclass) NOT NULL,
		ide_imovel_rural int4 NOT NULL,
		ide_documento_solicitacao int4 NOT NULL,
		ide_usuario int4 NOT NULL,
		observacao varchar(2000) NOT NULL,
		dtc_justificativa timestamp(6) DEFAULT now() NOT NULL,
		CONSTRAINT ide_imovel_rural_desbloqueio_pk PRIMARY KEY (ide_imovel_rural_desbloqueio)
	);

	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_documento_solicitacao_fk FOREIGN KEY (ide_documento_solicitacao) REFERENCES public.documento_imovel_rural(ide_documento_imovel_rural);
	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_imovel_rural_fk FOREIGN KEY (ide_imovel_rural) REFERENCES public.imovel_rural(ide_imovel_rural);
	ALTER TABLE public.imovel_rural_desbloqueio ADD CONSTRAINT ir_imovel_rural_desbloqueio_ide_usuario_fk FOREIGN KEY (ide_usuario) REFERENCES public.usuario(ide_pessoa_fisica);
	
	CREATE SEQUENCE public.DESBLOQUEIO_IMOVEL_RURAL_seq
		INCREMENT BY 1
		MINVALUE 1
		MAXVALUE 9223372036854775807
		START 1
		NO CYCLE;
	
	INSERT INTO public.hist_tabela
	(ide_tabela, desc_tabela)
	VALUES(nextval('tabela_ide_tabela_seq'), 'imovel_rural_desbloqueio');
	
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideImovelRuralDesbloqueio');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideImovelRural');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideDocumentoSolicitacao');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'ideUsuario');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'observacao');
	INSERT INTO public.hist_campo
	(ide_campo, ide_tabela, desc_campo)
	VALUES(nextval('campo_ide_campo_seq'), 35, 'dtcJustificativa');

COMMIT;