--Scripts em caráter prioritário
--Data de geração 17/04/2024
--Versão 4.31.6

BEGIN;

-- [36247] - Inclusão de espécies florestais no SEIA (RFP)
-- 1
-- Anacardium occidentale / Caju
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Anacardium occidentale / Caju', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Anacardium occidentale / Caju');
-- 2
-- Eugenia uniflora - Pitanga
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Eugenia uniflora / Pitanga', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Eugenia uniflora / Pitanga');
-- 3
-- Psidium guajava - Goiaba
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Psidium guajava / Goiaba', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Psidium guajava / Goiaba');
-- 4
-- Pterocarpus rohrii - Pau Sangue
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Pterocarpus rohrii / Pau Sangue', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Pterocarpus rohrii / Pau Sangue');
-- 5
-- Acnistus arborescens - Fruto de sabiá
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Acnistus arborescens / Fruto de sabiá', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Acnistus arborescens / Fruto de sabiá');
-- 6
-- Apeiba tibourbou - Escova de Macaco
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Apeiba tibourbou / Escova de Macaco', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Apeiba tibourbou / Escova de Macaco');
-- 7
-- Bixa orellana - Urucum
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Bixa orellana / Urucum', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Bixa orellana / Urucum');
-- 8
-- Croton floribundus - Capixingui
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Croton floribundus / Capixingui', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Croton floribundus / Capixingui');
-- 9
-- Guazuma ulmifolia - Mutambo
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Guazuma ulmifolia / Mutambo', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Guazuma ulmifolia / Mutambo');
-- 10
-- Senna macranthera - Manduí
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Senna macranthera / Manduí', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Senna macranthera / Manduí');
-- 11
-- Senna multijuga - Pau - cigarra
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Senna multijuga / Pau - cigarra', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Senna multijuga / Pau - cigarra');
-- 12
-- Trema micrantha - Curindiba
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Trema micrantha / Curindiba', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Trema micrantha / Curindiba');
-- 13
-- Apuleia leiocarpa - Garapa
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Apuleia leiocarpa / Garapa', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Apuleia leiocarpa / Garapa');
-- 14
-- Cariniana estrellensis - Jequitiba Branco
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Cariniana estrellensis / Jequitiba Branco', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Cariniana estrellensis / Jequitiba Branco');
-- 15
-- Cecropia hololeuca - Embauba Prateada
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Cecropia hololeuca / Embauba Prateada', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Cecropia hololeuca / Embauba Prateada');
-- 16
-- Cedrella fissilis - Cedro Rosa
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Cedrella fissilis / Cedro Rosa', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Cedrella fissilis / Cedro Rosa');
-- 17
-- Colubrina glandulosa - Saguaraji
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Colubrina glandulosa / Saguaraji', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Colubrina glandulosa / Saguaraji');
-- 18
-- Enterolobium contortisiliquum - Tamburil
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Enterolobium contortisiliquum / Tamburil', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Enterolobium contortisiliquum / Tamburil');
-- 19
-- Gallesia integrifolia - Pau Dalho
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Gallesia integrifolia / Pau Dalho', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Gallesia integrifolia / Pau Dalho');
-- 20
-- Handroanthus impetiginosus - Ipê Roxo de Bola
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Handroanthus impetiginosus / Ipê Roxo de Bola', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Handroanthus impetiginosus / Ipê Roxo de Bola');
-- 21
-- Hymenaea courbaril - Jatoba
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Hymenaea courbaril / Jatoba', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Hymenaea courbaril / Jatoba');
-- 22
-- Inga edulis - Inga de Metro
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Inga edulis / Inga de Metro', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Inga edulis / Inga de Metro');
-- 23
-- Inga laurina - Inga Laurina
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Inga laurina / Inga Laurina', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Inga laurina / Inga Laurina');
-- 24
-- Inga Vera - Inga Banana
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Inga Vera / Inga Banana', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Inga Vera / Inga Banana');
-- 25
-- Libidibia ferrea - Pau Ferro
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Libidibia ferrea / Pau Ferro', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Libidibia ferrea / Pau Ferro');
-- 26
-- Schinus terebinthifolius - Aroeira
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Schinus terebinthifolius / Aroeira', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Schinus terebinthifolius / Aroeira');
-- 27
-- Sparattosperma leucanthum - Cinco Folhas
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Sparattosperma leucanthum / Cinco Folhas', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Sparattosperma leucanthum / Cinco Folhas');
-- 28
-- Tabebuia heptaphylla - Ipe Rosa
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Tabebuia heptaphylla / Ipe Rosa', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Tabebuia heptaphylla / Ipe Rosa');
-- 29
-- Tapirira guianensis - Pau Pombo
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Tapirira guianensis / Pau Pombo', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Tapirira guianensis / Pau Pombo');
-- 30
-- Trema micrantha - Gurindiba
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Trema micrantha / Gurindiba', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Trema micrantha / Gurindiba');
-- 31
-- Balfourodendron riedelianum - Pau-Marfim
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Balfourodendron riedelianum / Pau-Marfim', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Balfourodendron riedelianum / Pau-Marfim');
-- 32
-- Handroanthus heptaphyllus - Ipe Rosa
INSERT INTO
        especie_floresta (ide_especie_floresta,
        nom_especie_floresta,
        ide_natureza_especie_floresta,
        dtc_criacao,
        dtc_exclusao,
        ind_excluido)
SELECT nextval('especie_floresta_seq'), 'Handroanthus heptaphyllus / Ipe Rosa', 1, now(), NULL, FALSE
WHERE NOT EXISTS (SELECT nom_especie_floresta FROM especie_floresta WHERE nom_especie_floresta = 'Handroanthus heptaphyllus / Ipe Rosa');

-- [36250] Erro ao anexar shapes a notificação

	UPDATE
		arquivo_processo
	SET
		ide_imovel = 86768
	WHERE
		ide_arquivo_processo = 260541;
	        
	INSERT INTO motivo_notificacao_imovel
	(ide_notificacao_motivo_notificacao, ide_imovel)
	VALUES(86958, 86768);        
	
-- [36225] - Processo sumiu do sistema
		UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao = 755027;
                
--[36056] - Inclusão de Nova substância Fosfato
	INSERT
		INTO
		producao_produto
	VALUES (
		10,
		'Fosfatos',
		8
	);
	
-- [35995] - Erro ao inserir CEP
	UPDATE
		imovel_rural 
	SET
		status_cadastro  = 0
	WHERE
		ide_imovel_rural  = 796701;

	UPDATE
		imovel
	SET
		ide_endereco = NULL
	WHERE
		ide_imovel = 796701;

	INSERT
		INTO
		logradouro (
			ide_logradouro ,
			nom_logradouro ,
			ide_tipo_logradouro ,
			ide_municipio ,
			num_cep 
		)
	VALUES (
		(
			SELECT
				max(ide_logradouro) + 1
			FROM
				logradouro 
		),
		'',
		86,
		767,
		0
	);

	INSERT
		INTO
		endereco 
	VALUES (
		(
			SELECT
				max(ide_endereco) + 1
			FROM
				endereco
		),
		(
			SELECT
				max(ide_logradouro)
			FROM
				logradouro
		),
		'',
		now(),
		FALSE,
		NULL,
		'',
		''
	);

	UPDATE
		imovel 
	SET
		ide_endereco = (
			SELECT
				max(ide_endereco)
			FROM
				endereco 
		)
	WHERE
		ide_imovel  = 796701;

	UPDATE
		logradouro 
	SET
		ide_bairro  = 1085
	WHERE
		ide_logradouro  = 4289092;

	UPDATE
		logradouro
	SET
		ide_bairro = 83103
	WHERE
		ide_logradouro = 2273184;

	UPDATE
		BAIRRO
	SET
		ind_origem_api  = TRUE
	WHERE
		ide_bairro = 83103;
		
--[36290] - Cadastro de espécies

	INSERT
		INTO
		especie_supressao
			(nom_especie_supressao,
		ind_ativo)
		VALUES ('Piptadenia retusa',
		true);
		
-- [36254] - Retorno de status da RL
-- Fazenda Colmeia(Mat. 4621)
UPDATE
        reserva_legal
SET
        ide_status = 3
WHERE
        ide_reserva_legal = 544854;
        
-- Inserindo a tramitação no "Histórico Alteração"
       
--hist_historico
INSERT
        INTO
        hist_historico (data_historico,
        acao_historico,
        ip_historico,
        ide_usuario)
VALUES (now(),
'UPD',
'177.193.213.89',
1);
--hist_valor
INSERT
        INTO
        hist_valor (val_valor,
        ide_campo,
        ide_registro,
        ide_historico)
VALUES (3,
195,
544854,
currval('historico_ide_historico_seq'));

--[36189] - Erro ao realizar análise técnica

	INSERT
		INTO
		fce_intervencao_barragem(ide_fce_intervencao_barragem,
		ide_outorga_localizacao_geografica,
		ide_fce)
	VALUES (nextval('fce_intervencao_barragem_ide_fce_intervencao_barragem_seq'),
		56461,
		78766);
		
	
	INSERT
		INTO
		especie_supressao_nome_popular_especie
			(ide_nome_popular_especie,
		ide_especie_supressao)
		VALUES (1268,
		12478);
		
--[36280] - Transferência de Pauta
	
	UPDATE
		notificacao
	SET
		dtc_envio = null,
		ind_aprovado = false
	WHERE
		ide_processo = 50532
		and ide_notificacao = 45312;
	
-- [36266] Saneamento de Usuários - Alteração do Campo "Ind_tipo_usuário"

	UPDATE
	        usuario
	SET
	        ind_tipo_usuario = FALSE
	WHERE
	        ide_pessoa_fisica IN (
	        SELECT
	                pf.ide_pessoa_fisica
	        FROM
	                pessoa_fisica pf
	        INNER JOIN usuario u ON
	                pf.ide_pessoa_fisica = u.ide_pessoa_fisica
	        WHERE
	                pf.num_cpf IN ('94122440530',
	        '53578024082',
	        '15772502069',
	        '33588066500',
	        '04980544541',
	        '35106360587',
	        '06504127530',
	        '25419692520',
	        '00858065525',
	        '32690885824',
	        '68769725500',
	        '05075972578',
	        '01255062525',
	        '87911213534',
	        '01761699547',
	        '86565133549',
	        '00694816523',
	        '96788976520',
	        '13032902860',
	        '01950646580',
	        '05337800558',
	        '05608241576',
	        '32107986487',
	        '28368630568',
	        '03558165551',
	        '36335754568',
	        '01294676504',
	        '03829893507',
	        '02501118537',
	        '31280218568',
	        '11233842587',
	        '92886370525',
	        '15601286078',
	        '05105497058',
	        '06461357599',
	        '01841349518',
	        '11774940515',
	        '02282412516'));	
	        
-- [36259] - Inclusão de espécies
INSERT
        INTO
        especie_supressao
                (nom_especie_supressao,
        ind_ativo)
VALUES ('Casearia arborea',
true);
INSERT
        INTO
        especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES (2301,
12478);

-- [36255] Transferência de Pauta

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao = 759396;
	        
	INSERT INTO controle_tramitacao (ide_processo, ide_status_fluxo, ide_area, dtc_tramitacao, ind_fim_da_fila, dsc_comentario_externo, dsc_comentario_interno, ide_pauta, ind_responsavel, ide_pessoa_fisica, ind_area_secundaria)
	VALUES (90944, 12, 76, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 36255', 2881, FALSE, 1, NULL);
		
-- [36093] - Pagamento REPFLOR

	UPDATE
	    tramitacao_requerimento
	SET
	    ide_status_requerimento = 8
	WHERE
	    ide_tramitacao_requerimento IN (1430460);
	    
-- [36091] - Transferência de Titularidade

UPDATE
        requerimento
SET
        nom_contato = 'PETRORECONCAVO S.A'
WHERE
        ide_requerimento = 32763;
       
UPDATE
        requerimento_pessoa
SET
        ide_pessoa = 10937
WHERE
        ide_requerimento = 32763
        and ide_tipo_pessoa_requerimento = 1;
        
-- [35939] Erro ao aprovar RL
-- Atualizando o vínculo do bloqueio da RL à técnica 'rute.santana':
UPDATE
        reserva_legal_bloqueio
SET
        ide_usuario = 2903
WHERE
        ide_reserva_legal_bloqueio = 1079;
-- Registrando no histórico do Imóvel Rural a alteração realizada:
INSERT INTO hist_historico (ide_historico, data_historico, acao_historico, ip_historico, ide_usuario)
VALUES (nextval('historico_ide_historico_seq'), now(), 'UPD', '0', 1);
INSERT INTO hist_valor (val_valor, ide_campo, ide_registro, ide_historico)
VALUES ('Desbloqueio da RL para análise por outro técnico (atendimento ticket REDMINE #35939).', 279, 985306, currval('historico_ide_historico_seq'));
    
COMMIT;