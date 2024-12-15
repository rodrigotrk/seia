-- #33483 - Atualização status reserva legal

BEGIN;

UPDATE reserva_legal SET ind_averbada=FALSE, ide_status=3 WHERE ide_reserva_legal =10853;

DELETE FROM reserva_legal_averbada WHERE ide_reserva_legal_averbada = 10853;

INSERT INTO  hist_historico (data_historico,acao_historico,ip_historico,ide_usuario) VALUES

(now(),'UPD','200.149.170.231',6858);

INSERT INTO hist_valor (val_valor,ide_campo,ide_registro,ide_historico) VALUES

(4,195,1066916,currval('historico_ide_historico_seq'));

--[33481] - Processo sumiu

BEGIN; 
        UPDATE
                controle_tramitacao  
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 521896;
--[33478] - Processo sumiu
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET  
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 517055;
COMMIT;
--[33474] - Inclusão de espécies.
BEGIN;
INSERT INTO
	especie_supressao
	(nom_especie_supressao,
	ind_ativo)
VALUES
	('Senna acuruensis', TRUE);

INSERT INTO
	nome_popular_especie
	(nom_popular_especie)
VALUES
	('Canjoão');

INSERT INTO
	especie_supressao_nome_popular_especie
	(ide_nome_popular_especie,
	ide_especie_supressao)
VALUES
	(currval ('nome_popular_especie_seq'),
	(
SELECT
	currval ('especie_supressao_seq')
	));
COMMIT;
END;
--[33472] - Processo sumiu do sistema

BEGIN;

	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE 
    WHERE 
   		ide_controle_tramitacao = 521230;
COMMIT;
--[33471] - Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 520015;
        
COMMIT;
--[33470] - Pauta Técnica - Retirar da Pauta do Gestor
	
BEGIN;
	
	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = FALSE  
   	WHERE 
   		ide_controle_tramitacao = 520749;
   		
COMMIT;    		
--[33447] - Processo sumiu

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE
	WHERE 
		ide_controle_tramitacao = 520970;
COMMIT;	
--[33441] - Processo sumiu da Pauta Técnica

BEGIN; 
        UPDATE
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE
        WHERE 
                ide_controle_tramitacao = 506644;
COMMIT; 
-- #33437 - Inclusão de Unidade de Conservação
BEGIN;

INSERT
	INTO
	unidade_conservacao (ide_unidade_conservacao,
	ide_tipo_gestao,
	nom_unidade_conservacao,
	ind_ativo,
	ide_bioma)
VALUES (172,
1,
'APA do Boqueirão da Onça'
,
TRUE,
2);

--[33422] - Erro no redirecionamento de página!

BEGIN;

	UPDATE
	        pessoa
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa = 76161;
COMMIT;
--[33409] - Erro ao salvar procurador da Pessoa Jurídica.
BEGIN;
UPDATE
	public.pessoa_fisica
SET
	ide_estado_civil = NULL,
	ide_escolaridade = 6,
	ide_ocupacao = NULL,
	ide_pais = 1,
	nom_pessoa = 'CLODOALDO DA SILVA DOURADO',
	tip_sexo = NULL,
	dtc_nascimento = '1969-11-05',
	des_naturalidade = 'IBITITÁ-BA',
	nom_pai = NULL,
	nom_mae = 'LIRIAN MARQUES DOURADO',
	num_cpf = '41756169500',
	ide_estado = NULL,
	ide_usuario = 971485,
	endereco_ip = '172.16.3.18',
	caminho_requisicao = '/paginas/manter-pessoafisica/cadastro.xhtml'
WHERE
	ide_pessoa_fisica = 1059;

UPDATE
	public.pessoa
SET
	dtc_criacao = '2015-02-27 11:51:11.836',
	ind_excluido = TRUE,
	des_email = 'clodoaldosd05@yahoo.com.br',
	dtc_exclusao = NULL,
	ide_usuario = 0,
	endereco_ip = '',
	caminho_requisicao = ''
WHERE
	ide_pessoa = 41205;
COMMIT;
-- #33287 - Correção do tipo de localização
BEGIN;

UPDATE
	localizacao_geografica
SET
	ide_classificacao_secao = 2
WHERE
	ide_localizacao_geografica IN (2737002, 2737013);
	
COMMIT;

--[33463] - [CADASTRO] CEFIR - Erro ao finalizar o imóvel rural.
BEGIN;
	UPDATE 
		requerimento_imovel 
	SET 	
		ind_excluido = true
	WHERE 
		ide_requerimento = 603414;
COMMIT;	

--[33446] - Processo sumiu
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET 
                ind_fim_da_fila = TRUE
        WHERE 
                ide_controle_tramitacao =  519583;
COMMIT;  
--[33445] - CEP divergente da localidade 
BEGIN;
        UPDATE  
                bairro 
        SET 
                ide_municipio = 825
        WHERE 
                ide_bairro = 1212 AND ide_municipio = 870;
COMMIT; 
