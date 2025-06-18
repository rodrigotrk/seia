--[33970] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 525449;

--[33965] - [PROCESSO] Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 475479;
COMMIT;

--33956 - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE
                ide_controle_tramitacao = 524836;
COMMIT; 

--[33953] - [CONSULTA] CEP divergente da localidade cadastrada
BEGIN;

UPDATE bairro SET ind_origem_correio = FALSE WHERE ide_bairro = 1734;

UPDATE bairro SET ind_origem_correio = FALSE WHERE ide_bairro = 916;

UPDATE bairro SET ind_origem_correio = FALSE WHERE ide_bairro = 1594;

UPDATE bairro SET ide_municipio = 758 WHERE ide_bairro = 1594;

UPDATE bairro SET ide_municipio = 698 WHERE ide_bairro = 1165;

UPDATE logradouro SET ind_origem_api = FALSE WHERE ide_logradouro = 3980256;

COMMIT; 

--[33946] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE      
                ide_controle_tramitacao IN (543860,543861,543862,543863);
COMMIT;   

 
--[33944] - Inclusão de espécie
BEGIN;
-- nome científico: Eugenia punicifolia
-- nome popular: Cereja-do-cerrado
INSERT INTO
        especie_supressao
        (nom_especie_supressao,
        ind_ativo)
VALUES
        ('Eugenia punicifolia',
        TRUE);
INSERT INTO
        nome_popular_especie
        (nom_popular_especie)
VALUES
        ('Cereja-do-cerrado');
INSERT INTO
        especie_supressao_nome_popular_especie
        (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
        (currval ('nome_popular_especie_seq'),
        (SELECT currval ('especie_supressao_seq' ))
        );
--[33845] - Corrigir exibição errada na tela de Detalhes do processo.
begin
	UPDATE public.notificacao_motivo_notificacao
SET ide_notificacao=22749, ide_motivo_notificacao=1
WHERE ide_notificacao_motivo_notificacao=28320;

UPDATE public.notificacao_motivo_notificacao
SET ide_notificacao=23881, ide_motivo_notificacao=1
WHERE ide_notificacao_motivo_notificacao=31302;

end 

--[33938] Processo sumiu do sistema
BEGIN;
        UPDATE
                controle_tramitacao
        SET
                ind_fim_da_fila = TRUE
        WHERE
                ide_controle_tramitacao IN (534253, 534254);
COMMIT;

--[34030] - [CADASTRO] Erro na finalização do CEFIR

BEGIN;

	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE
	WHERE
		ide_requerimento = 1137833;

COMMIT;

