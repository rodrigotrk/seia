--Scripts em caráter prioritário
--Data de geração 07/08/2024
--Versão 4.33.3

BEGIN;

--[36639] - Mudança de Status
INSERT
	INTO
	tramitacao_requerimento
(ide_tramitacao_requerimento,
	ide_status_requerimento,
	ide_requerimento,
	dtc_movimentacao,
	ide_pessoa)
VALUES(nextval('TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq'),
1,
1143536,
now(),
1);

INSERT
	INTO
	comunicacao_requerimento
(ide_comunicacao_requerimento,
	ide_requerimento,
	dtc_comunicacao,
	des_mensagem,
	assunto,
	ind_enviado)
VALUES(nextval('COMUNICACAO_REQUERIMENTO_IDE_COMUNICACAO_REQUERIMENTO_seq'),
1143536,
now(),
'Prezado(a) requerente, 
o status do requerimento foi alterado mediante atendimento à solicitação do ticket #36639.

 Atte.,
SEMA/INEMA.
',
NULL,
NULL);

-- [36635] Tratamento do processo 2013.001.000521

	UPDATE
		notificacao
	SET
		ind_cancelado = TRUE
	WHERE
		ide_notificacao = 3481;
	        
	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 63625;
	        
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (1826, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36635', 224, FALSE, 1, NULL);

-- [36634] Transferência de Pauta
	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 791638;
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (82961, 12, 3, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36634', 224, FALSE, 1, NULL);

-- [36630] - Processo sumiu
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 580795;
        
--[36625] - Imóvel Rural sumiu do sistema
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
12888,
7718,
now(),
FALSE,
1,
NULL,
NULL,
NULL);

UPDATE
	imovel_rural
SET
	ide_requerente_cadastro = 12888
WHERE
	ide_imovel_rural = 7718;
	
-- [36616] Erro ao Inserir Shapes
UPDATE
        localizacao_geografica
SET
        ide_classificacao_secao = 2
WHERE
        ide_localizacao_geografica = 3289336;
        
-- [36615] - Realocação - Zerar RL - Fazenda Regina
UPDATE
	reserva_legal
SET
	ide_imovel_rural = NULL
WHERE
	ide_reserva_legal = 109366;
	
-- [36588] - Erro em Efetuar Validação Prévia
	UPDATE documento_obrigatorio
	   SET  
		   ide_tipo_documento_obrigatorio=1, 
		   ind_ativo=true 
	   WHERE ide_documento_obrigatorio=9981; 
	   
--[36578] Erro ao realizar análise técnica
UPDATE
        public.fce
SET
        dtc_criacao = '2023-07-13 10:31:29.449',
        ide_requerimento = 114812,
        ide_documento_obrigatorio = 1444,
        ide_origem_fce = 2,
        ind_concluido = TRUE,
        ide_analise_tecnica = NULL,
        ide_pessoa_fisica = NULL,
        ide_processo_reenquadramento = NULL
WHERE
        ide_fce = 68536;
        
-- [36647] - Usuários sem e-mail cadastrados
UPDATE
        usuario u
SET
        ind_excluido = TRUE
WHERE
        u.ide_pessoa_fisica IN (SELECT p.ide_pessoa FROM usuario u 
INNER JOIN pessoa_fisica pf ON u.ide_pessoa_fisica = pf.ide_pessoa_fisica 
INNER JOIN pessoa p ON pf.ide_pessoa_fisica = p.ide_pessoa 
WHERE des_email IS NULL OR des_email='');
UPDATE
        pessoa p
SET
        ind_excluido = TRUE
WHERE
        p.ide_pessoa IN (SELECT p.ide_pessoa FROM usuario u 
INNER JOIN pessoa_fisica pf ON u.ide_pessoa_fisica = pf.ide_pessoa_fisica 
INNER JOIN pessoa p ON pf.ide_pessoa_fisica = p.ide_pessoa 
WHERE des_email IS NULL OR des_email='');
UPDATE
        pessoa p
SET
        dtc_exclusao = now()
WHERE
        p.ide_pessoa IN (SELECT p.ide_pessoa FROM usuario u 
INNER JOIN pessoa_fisica pf ON u.ide_pessoa_fisica = pf.ide_pessoa_fisica 
INNER JOIN pessoa p ON pf.ide_pessoa_fisica = p.ide_pessoa 
WHERE des_email IS NULL OR des_email='');

-- [36663] - Transferência de Titularidade
-- 2015.001.001181/INEMA/LIC-01181
UPDATE 
        requerimento
SET
        nom_contato = 'PetroReconcavo S.A'
WHERE 
        ide_requerimento = 32742;
        
UPDATE 
        requerimento_pessoa
SET
        ide_pessoa = 10937
WHERE 
        ide_requerimento = 32742 AND ide_tipo_pessoa_requerimento = 1;
       
DELETE
FROM
        requerimento_pessoa
WHERE
        ide_requerimento = 32742
        AND ide_pessoa = 1411
        AND ide_tipo_pessoa_requerimento = 3;
       
--2015.001.001225/INEMA/LIC-01225
UPDATE 
        requerimento
SET
        nom_contato = 'PetroReconcavo S.A'
WHERE 
        ide_requerimento = 32764;
        
UPDATE 
        requerimento_pessoa
SET
        ide_pessoa = 10937
WHERE 
        ide_requerimento = 32764 AND ide_tipo_pessoa_requerimento
        = 1;
       
DELETE
FROM
        requerimento_pessoa
WHERE
        ide_requerimento = 32764
        AND ide_pessoa = 1411
        AND ide_tipo_pessoa_requerimento = 3;
        
-- [36668] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 725978;
        
-- [36683] - Pagamento REPFLOR
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1211447, now(), 1);    
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1342658, now(), 1);  
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1355892, now(), 1);        
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1356147, now(), 1);    
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1354113, now(), 1);  
INSERT INTO tramitacao_requerimento
(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
VALUES(8, 1357861, now(), 1);     

-- [36657] - Erro ao editar imóvel rural
UPDATE
	documento_imovel_rural_posse
SET
	ide_tipo_documento_imovel_rural = 2
WHERE
	ide_imovel_rural = (
	SELECT
		ide_imovel_rural
	FROM
		imovel_rural
	WHERE
		ide_imovel_rural = 1377366);		


--[36627] - Erro ao responder FCE - ASV
 DELETE FROM fce_asv fa WHERE fa.ide_fce_asv IN (10479,10478,10619,10618,10617,10616);
 
--[36626] - Erro ao responder FCE - ASV

INSERT INTO fce_asv (ide_fce) VALUES(87592);
INSERT INTO fce_asv (ide_fce) VALUES(78200);
INSERT INTO fce_asv (ide_fce) VALUES(68877);
INSERT INTO fce_asv (ide_fce) VALUES(58190);

COMMIT;