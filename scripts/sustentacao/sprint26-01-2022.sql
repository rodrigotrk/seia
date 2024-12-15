-- #33293 - Transferência de processos
BEGIN;

UPDATE 
	controle_tramitacao 
SET
	ind_fim_da_fila = FALSE 
WHERE
	ide_processo IN (16885,
36702,
49866,
51563,
62137,
62182);

INSERT INTO controle_tramitacao
(ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
VALUES
(16885, 12, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 1241, NULL),
(36702, 12, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 17753, NULL),
(49866, 12, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 8487, NULL),
(51563, 12, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 1241, NULL),
(62137, 20, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 1241, NULL),
(62182, 12, 50, now(), true, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 33293', 2347, false, 1241, NULL);

UPDATE
	usuario 
SET
	ide_perfil = 3
WHERE
	ide_pessoa_fisica = 178247;

UPDATE 
	pauta 
SET
	ide_tipo_pauta = 3
WHERE 
	ide_pauta = 1686;

-- #33291 - Conserto troca de titularidade
BEGIN;

UPDATE 
	pessoa_imovel 
SET
	ind_excluido = FALSE 
WHERE 
	ide_pessoa_imovel = 1734808;
	
COMMIT;

--[33284] - Processo duplicado 

BEGIN;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508128;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508127;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508126;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508124;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508123;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508118;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508117;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao = 508115;
COMMIT;
END;

--#33283 - processo sumiu

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = true
          WHERE 
                ide_controle_tramitacao = 481489;
 COMMIT;
-- #33278 - Caixas dados concedidos pauta ténica
BEGIN;

INSERT
	INTO
	equipe(dtc_formacao_equipe,
	ind_ativo,
	ide_area,
	ide_processo)
VALUES (now(),
TRUE,
76,
70241);

INSERT
	INTO
	integrante_equipe(ide_pessoa_fisica,
	ind_lider_equipe,
	ide_area,
	ide_area_responsavel,
	ide_equipe)
VALUES (19476,
TRUE,
76,
76,
(
SELECT
	currval('equipe_seq')));

INSERT
	INTO
	processo_ato_integrante_equipe
VALUES ((
SELECT
	currval('integrante_equipe_seq')),
75621);

COMMIT;
-- #33276 - Processo Sumido.

BEGIN;
        UPDATE 
                controle_tramitacao set ind_fim_da_fila = true
        WHERE
                ide_controle_tramitacao = 490215;
   COMMIT;
   
-- #33276 - Erro na aba de declaração de inexigibilidade. 
  
    BEGIN;
    
        UPDATE 
                atividade_inexigivel 
        SET 
                permite_local_realizacao = TRUE 
        WHERE         
                ide_atividade_inexigivel = 323;
        
     COMMIT;
     
-- #33302 - Conserto troca de titularidade
BEGIN;
	
UPDATE
	pessoa_imovel
SET
	ind_excluido = FALSE
WHERE
	ide_pessoa_imovel = 1771074;
  
-- #33280 - Atualização equipe processo
BEGIN;

INSERT
        INTO
        equipe(dtc_formacao_equipe,
        ind_ativo,
        ide_area,
        ide_processo)
VALUES (now(),
TRUE,
76,
70064);

INSERT
        INTO
        integrante_equipe(ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe)
VALUES (19476,
TRUE,
76,
76,
(
SELECT
        currval('equipe_seq')));

INSERT
        INTO
        processo_ato_integrante_equipe
VALUES ((
SELECT
        currval('integrante_equipe_seq')),
75427);

COMMIT;

-- #33267 - Recuperação área DIRAF
BEGIN;

UPDATE
	area
SET
	ind_excluido = FALSE
WHERE
	ide_area = 27;

INSERT
	INTO
	funcionario
VALUES (734885,
27);

UPDATE
	area
SET
	ide_pessoa_fisica = 734885
WHERE
	ide_area = 27;

COMMIT;
--#33292 - o sistema pula a aba de "Dados Específicos" ao editar o cadastro do Imóvel Rural
BEGIN;

-- #33262 - Dados concedidos

BEGIN;

INSERT
        INTO
        equipe(dtc_formacao_equipe,
        ind_ativo,
        ide_area,
        ide_processo)
VALUES (now(),
TRUE,
39,
69570);
INSERT
        INTO
        integrante_equipe(ide_pessoa_fisica,
        ind_lider_equipe,
        ide_area,
        ide_area_responsavel,
        ide_equipe)
VALUES (8527,
TRUE,
39,
39,
(
SELECT
        currval('equipe_seq')));
INSERT
        INTO
        processo_ato_integrante_equipe
VALUES ((
SELECT
        currval('integrante_equipe_seq')),
74865);
COMMIT;

-- #33258 remanejamento de processos para pauta do lider

BEGIN;

UPDATE 
	controle_tramitacao
SET
	ide_pauta = 2236
WHERE 
	ide_controle_tramitacao = 513213;


UPDATE 
	controle_tramitacao
SET
	ide_pauta = 2264
WHERE 
	ide_controle_tramitacao = 507011;

UPDATE
	
	controle_tramitacao
SET
	ind_responsavel = false
WHERE
	ide_controle_tramitacao = 507009;	
	
UPDATE
	
	controle_tramitacao
SET
	ind_responsavel = true
WHERE
	ide_controle_tramitacao = 507011;

COMMIT;
	
-- #33294 - Erro ao tentar efetuar Análise Técnica

BEGIN;
UPDATE pergunta_requerimento 
SET ide_localizacao_geografica = 3227595 
        WHERE ide_pergunta_requerimento =1650964;
commit;    
-- #33309 - CEP divergente

BEGIN;
--#33314 - [EMPREENDIMENTO] CEP divergente da localidade.

BEGIN;

	UPDATE 
		bairro 
	SET 
		ide_municipio = 854
	WHERE 
		ide_bairro = 912 and ide_municipio = 1176;

	UPDATE 
		bairro 
	SET 
		ide_municipio = 870
	WHERE 
		ide_bairro = 1212;
 COMMIT; 	
--#33312 - Processo sumiu do sistema - novamente
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 513213;
 COMMIT;         
--#33315 - O CEP informado não pertence ao município do seu cadastro do imóvel

BEGIN;	
	UPDATE 
    	logradouro 
    SET 
        num_cep = '44885000', ide_municipio = 1207
    WHERE 
        ide_logradouro = 1213262 AND ide_municipio = 994 AND num_cep = '44890000';
                
 COMMIT;
		ind_origem_correio = false
	WHERE 
		ide_bairro = 1608 and ide_municipio = 691;
		
	UPDATE 
		bairro 
	SET 
		ind_origem_correio = false
	WHERE 
		ide_bairro = 1218 and ide_municipio = 939;

	UPDATE 
		logradouro 
	SET 
		num_cep = '44655000'
	WHERE 
		ide_logradouro = 1954531;	
	
	UPDATE 
		logradouro 
	SET 
		num_cep = '44660000'
	WHERE 
		ide_logradouro = 1549834;	
	
	UPDATE 
		logradouro 
	SET 
		num_cep = '44002344'
	WHERE 
		ide_logradouro = 995025;
		
	UPDATE 
		municipio 
	SET 
		num_cep = '44002344'
	WHERE 
		ide_municipio = 691;	
 COMMIT;		
	UPDATE 
		cronograma_recuperacao 
	SET
		ide_localizacao_geografica = 2020325
	WHERE
		ide_cronograma_recuperacao = 114543 and ide_reserva_legal = 462849;
 COMMIT;	

--#31670 - CEFIR - Sistema não exibe imóvel rural
	BEGIN;
		UPDATE
			imovel 
		SET 
			ind_excluido = TRUE 
		WHERE 
			ide_imovel = 1098781;
	COMMIT;	

		DELETE FROM 	
			area_rural_consolidada 
		WHERE 
			ide_area_rural_consolidada = 223;
	COMMIT;	
 	
		UPDATE
			imovel_rural 
		SET
			des_denominacao = trim(' PA BOA SORTE') 
		WHERE 
			ide_imovel_rural = 1098781;	
	COMMIT;
-- [33490] - Inclusão de espécies
	BEGIN;

	INSERT INTO
		especie_supressao
		(nom_especie_supressao,
		ind_ativo)
	VALUES
		('Jacaranda irwinii',
		TRUE);

	INSERT INTO
		especie_supressao_nome_popular_especie
		(ide_nome_popular_especie,
		ide_especie_supressao)
	VALUES
		(5336,	( SELECT currval('especie_supressao_seq')));

	COMMIT;
--#33493 - Processo sumido
BEGIN;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 517233;
COMMIT;
