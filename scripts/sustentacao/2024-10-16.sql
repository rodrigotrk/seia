--Scripts em caráter prioritário
--Data de geração 02/10/2024
--Versão 4.33.7

BEGIN;

-- [36766] - Pauta do Gestor em branco
ALTER TABLE public.reenquadramento_processo ADD ind_excluido boolean NULL DEFAULT FALSE;

UPDATE
	reenquadramento_processo r
SET
	ind_excluido = TRUE
WHERE
	r.ide_reenquadramento_processo IN (1561);

-- [36752] Pagamento REPFLOR

	INSERT INTO tramitacao_requerimento
	(ide_status_requerimento, ide_requerimento, dtc_movimentacao, ide_pessoa)
	VALUES(8, 1223547, now(), 1);
	
-- [36730] - Imóvel Rural sumiu do sistema
update
        imovel
set
        ind_excluido = false 
where
        ide_imovel = 1099814;
        
-- [36747] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 781611;
        
-- [36741] Erro ao finalizar requerimento

	UPDATE
		enquadramento
	SET
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 207342;
		
-- [36691] Notificação fora do prazo não retornou

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao = 725978;
       
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (84669, 8, 2, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36634', 6, FALSE, 1, NULL);

-- [36703] - Erro ao efetuar validação prévia
-- Endereço aparentemente excluido erroneamente, e remoção de duplicata.
UPDATE
	endereco_pessoa
SET
	ide_endereco = 308
WHERE
	ide_endereco_pessoa = 67042;

UPDATE
	endereco
SET
	ind_excluido = FALSE,
	dtc_exclusao = NULL
WHERE
	ide_endereco = 210148;
	
-- [36735] Processo Duplicado
DELETE
FROM
        controle_tramitacao
WHERE
        ide_controle_tramitacao = 774290;
        
-- [36719] - erro ao realizar análise tecnica

update
        integrante_equipe
set
        ide_pessoa_fisica = 19476,
        ind_lider_equipe = true,
        ide_area = 76,
        ide_area_responsavel = 76,
        ide_equipe = 41126
where
        ide_integrante_equipe = 78190;
        

-- [36737] Erro ao realizar análise técnica (ASV)
INSERT INTO
        fce_asv (ide_fce_asv , num_area_supressao, ind_app, num_area_app, ide_fce, num_area_suprimida, ind_material_lenhoso, ind_aceite)
VALUES (nextval('fce_asv_ide_fce_asv_seq'), 303.0000, false, NULL, 89524, NULL, NULL, true);
INSERT INTO
        fce_asv_classi_vegetacao (ide_fce_asv, ide_classificacao_vegetacao)
VALUES (currval('fce_asv_ide_fce_asv_seq'), 4),
           (currval('fce_asv_ide_fce_asv_seq'), 9),
           (currval('fce_asv_ide_fce_asv_seq'), 28),
           (currval('fce_asv_ide_fce_asv_seq'), 30);
INSERT INTO
        fce_asv_obj_supressao (ide_fce_asv, ide_objetivo_supressao)
VALUES (currval('fce_asv_ide_fce_asv_seq'), 1),
           (currval('fce_asv_ide_fce_asv_seq'), 9);
      
INSERT INTO
        especie_supressao_autorizacao (ide_especie_supressao, ide_nome_popular_especie, ide_fce_asv, ide_processo_ato_concedido, ide_produto, ide_processo_ato, volume_total_fora_app, volume_total_em_app)
VALUES (NULL, NULL, currval('fce_asv_ide_fce_asv_seq'), NULL, 2223, NULL, 10643.0433, NULL);

--[36732] - Pagamento REPFLOR
INSERT
        INTO
        tramitacao_requerimento (
    ide_status_requerimento,
        ide_requerimento,
        dtc_movimentacao,
        ide_pessoa
)
VALUES
(8,
1349379,
now(),
1),
(8,
1348227,
now(),
1),
(8,
1352957,
now(),
1),
(8,
1356385,
now(),
1);

-- [36728] Erro ao aprovar notificação
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 800475;
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE
WHERE
        ide_controle_tramitacao = 800476;
        
-- [36738] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 800437;
        
--[36729] - Trasferência de pauta
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_processo = 92288;

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
VALUES(92288,
12,
2,
now(),
TRUE,
NULL,
'Solicitação de remanejamento de processo através do chamado Redmine 36729',
2884,
FALSE,
1,
NULL);

-- [36740] Requerimento sumiu do sistema
   insert
           into
           requerimento_pessoa(ide_requerimento,
           ide_pessoa,
           ide_tipo_pessoa_requerimento,
           ind_solicitante,
           ind_usuario_logado)
   values(1371236,
   1257796,
   1,
   false,
   false);
   insert
           into
           tramitacao_requerimento(ide_tramitacao_requerimento,
           ide_status_requerimento,
           ide_requerimento,
           dtc_movimentacao,
           ide_pessoa)
   values(nextval('TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq'),
   14,
   1371236,
   '2024-09-16 10:55:54.774',
   1257796);
   
-- [36746] - Processo sumiu do sistema 
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 652205;

--[36551] - Histórico de Alterações Pessoa Física e Jurídica
DROP TABLE pessoa_fisica_historico;

CREATE TABLE pessoa_fisica_historico (
	ide_pessoa_fisica_historico INT4 NOT NULL PRIMARY KEY,
	ide_pessoa_fisica INT4 NOT NULL,
	ide_usuario_alteracao INT4 NOT NULL,
	ide_procurador_pessoa_fisica INT4,
	nom_pessoa VARCHAR(200),
	des_email VARCHAR(70),
	dtc_historico TIMESTAMP NOT NULL,
	status_procurador BOOLEAN,
	CONSTRAINT ide_pessoa_fisica_historico_ide_pessoa_fisica_fkey FOREIGN KEY(ide_pessoa_fisica) REFERENCES pessoa_fisica(ide_pessoa_fisica),
	CONSTRAINT ide_pessoa_fisica_historico_ide_usuario_fkey FOREIGN KEY (ide_usuario_alteracao) REFERENCES usuario(ide_pessoa_fisica),
	CONSTRAINT ide_pessoa_fisica_historico_ide_procurador_pessoa_fisica_fkey FOREIGN KEY (ide_procurador_pessoa_fisica) REFERENCES procurador_pessoa_fisica(ide_procurador_pessoa_fisica)
);

ALTER TABLE pessoa_juridica_historico
ADD ide_usuario_alteracao INT4,
ADD ide_procurador_representante INT4,
ADD status_procurador BOOLEAN,
ADD ide_representante_legal INT4,
ADD status_representante BOOLEAN,
ADD CONSTRAINT ide_pessoa_juridica_historico_ide_usuario_fkey FOREIGN KEY (ide_usuario_alteracao) REFERENCES usuario(ide_pessoa_fisica),
ADD CONSTRAINT ide_pessoa_juridica_historico_ide_procurador_pessoa_fkey FOREIGN KEY (ide_procurador_representante) REFERENCES procurador_representante(ide_procurador_representante),
ADD CONSTRAINT ide_pessoa_juridica_historico_ide_representante_legal_fkey FOREIGN KEY (ide_representante_legal) REFERENCES representante_legal(ide_representante_legal); 


UPDATE pessoa_juridica_historico SET ide_usuario_alteracao = 1;

ALTER TABLE pessoa_juridica_historico RENAME COLUMN nom_razaosocial TO nom_razao_social;
ALTER TABLE pessoa_juridica_historico RENAME COLUMN nom_nomefantasia TO nom_nome_fantasia;

ALTER TABLE pessoa_juridica_historico ALTER COLUMN ide_usuario_alteracao SET NOT NULL;
ALTER TABLE pessoa_juridica_historico ALTER COLUMN nom_razao_social DROP NOT NULL;
ALTER TABLE pessoa_juridica_historico ALTER COLUMN nom_natureza_juridica DROP NOT NULL;
ALTER TABLE pessoa_juridica_historico ALTER COLUMN ide_processo DROP NOT NULL;
ALTER TABLE pessoa_juridica_historico ALTER COLUMN nom_nome_fantasia DROP NOT NULL;
ALTER TABLE pessoa_juridica_historico ALTER COLUMN dtc_abertura DROP NOT NULL;

--[36585] Erro ao finalizar o CEFIR
 
UPDATE
	imovel_rural
SET
	status_cadastro = 0
WHERE
	ide_imovel_rural = 266638;     
 
UPDATE
	public.endereco
SET
	ide_logradouro = 3062460,
	num_endereco = 's/n',
	dtc_criacao = '2024-09-02',
	ind_excluido = FALSE,
	dtc_exclusao = NULL,
	des_complemento = 'LOTEAMENTO CAMPINHOS',
	des_ponto_referencia = 'ESTRADA VICINAL L C 07'
WHERE
	ide_endereco = 541148;		

--[36767] Imóvel Rural sumiu do sistema
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
VALUES(
nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'::text::regclass),
2974,
16136,
now(),
FALSE,
1,
NULL,
NULL,
NULL);

UPDATE
	imovel_rural
SET
	ide_requerente_cadastro = 2974
WHERE
	ide_imovel_rural = 16136;    
	
	
-- [36753] - Alteração de Usuário
UPDATE
        usuario
SET
        ide_perfil = 72
WHERE
        ide_pessoa_fisica = 1311411;
        
-- [36768] - Requerimento sumiu do sistema
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
		ide_requerimento = 1371146
	ORDER BY
		dtc_movimentacao DESC
	LIMIT 1
                          )
WHERE
	ide_requerimento = 1371146;    
	
-- [36754] - Retorno de status da RL
-- Atualização para Nova Fronteira (Mat. 4142)
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
946090,
currval('historico_ide_historico_seq'));
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 946090;
-- Atualização para FAZENDA PASSAGEM FUNDA DA SERRA GERAL - GLEBA D3
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
523496,
currval('historico_ide_historico_seq'));
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 523496;
-- Atualização para Fazenda Pequizeiro - Parcela II
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
425456,
currval('historico_ide_historico_seq'));
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 425456;
-- Atualização para Fazenda Santa Marta - Parcela I
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
427891,
currval('historico_ide_historico_seq'));
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 427891;
        
-- [36771] Erro ao realizar análise técnica
DELETE
FROM
        processo_ato_integrante_equipe
WHERE
        ide_equipe_integrante = 76459;
        
DELETE
FROM
        integrante_equipe
WHERE
        ide_integrante_equipe = 76459;
        
DELETE
FROM
        equipe
WHERE
        ide_equipe = 40433;
        
-- [36733] - Erro ao inserir CEP  
     
DELETE
FROM
	declaracao_inexigibilidade_info_geral
WHERE
	ide_endereco = 2953033;

DELETE
FROM
	endereco
WHERE
	ide_logradouro = 4858144;

DELETE
FROM
	logradouro
WHERE
	num_cep = 42710580
	AND ide_municipio IN (
	SELECT
		ide_municipio
	FROM
		municipio
	WHERE
		nom_municipio = 'São Paulo'
);

-- [36745] - Transferência de Titularidade
-- 2023.001.005490/INEMA/LIC-05490
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1230353;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1230353
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004405/INEMA/LIC-04405
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1230235;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1230235
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004684/INEMA/LIC-04684
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1230094;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1230094
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004406/INEMA/LIC-04406
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1230085;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1230085
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004296/INEMA/LIC-04296
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1229407;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1229407
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004408/INEMA/LIC-04408
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1229337;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1229337
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.004575/INEMA/LIC-04575
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1228470;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1228470
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.001814/INEMA/LIC-01814
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1058820;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1058820
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.002028/INEMA/LIC-02028
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1058815;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1058815
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.002090/INEMA/LIC-02090
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1058752;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1058752
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.002027/INEMA/LIC-02027
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1056705;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1056705
        and ide_tipo_pessoa_requerimento = 1;
-- 2023.001.009362/INEMA/LIC-09362
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1056464;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1056464
        and ide_tipo_pessoa_requerimento = 1;
--2023.001.009355/INEMA/LIC-09355
update
        requerimento
set
        nom_contato = '3R BAHIA S.A'
where
        ide_requerimento = 1056700;
update
        requerimento_pessoa
set
        ide_pessoa = 965656
where
        ide_requerimento = 1056700
        and ide_tipo_pessoa_requerimento = 1;
	
-- [36758] - Retorno de status da RL
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
215383,
currval('historico_ide_historico_seq'));
update
	reserva_legal
set
	ide_status = 3
where
	ide_reserva_legal = 215383;

COMMIT;
