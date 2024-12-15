-- [34928] Inclusão de espécies florestais no SEIA
-- Nome científico: Sapindus sp
-- Nome popular: Sapindus
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Sapindus sp', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Sapindus');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	
-- Nome científico: Pleroma Fissinervium
-- Nome popular: Quaresmeira
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Pleroma Fissinervium', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Quaresmeira');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	
-- Nome científico: Erythroxylum passerinum
-- Nome popular: Pau-de-cobra
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Erythroxylum passerinum', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Pau-de-cobra');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	
-- Nome científico: Monteverdia distichophylla
-- Nome popular: Bom-nome
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Monteverdia distichophylla', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Bom-nome');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	
-- Nome científico: Poecilanthe itapuana
-- Nome popular: Poecilante
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Poecilanthe itapuana', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Poecilante');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
	
-- Nome científico: Eugenia adstringens
-- Nome popular: Eugenia
	INSERT INTO especie_supressao (nom_especie_supressao, ind_ativo)
	VALUES ('Eugenia adstringens', TRUE);
       
	INSERT INTO nome_popular_especie (nom_popular_especie)
	VALUES ('Eugenia');
       
	INSERT INTO especie_supressao_nome_popular_especie (ide_nome_popular_especie, ide_especie_supressao)
	VALUES (currval ('nome_popular_especie_seq'), currval ('especie_supressao_seq'));
-- [34921] Erro ao finalizar DTRP
	UPDATE
		enquadramento
	SET
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 141019;
-- [34914] Transferência de Titularidade 
    
	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 23426
	WHERE
		ide_requerimento = 756601 AND ide_pessoa = 23156;
	UPDATE
		requerimento
	SET
		nom_contato = 'YALEN QUIMICA LTDA-ME'
	WHERE
		ide_requerimento = 756601;
-- [34900] Erro ao gerar Certificado RLAC

	UPDATE
		licenca
	SET
		ind_excluido = TRUE
	WHERE
		ide_licenca IN (48010, 48330);

-- [34884] - Transferência de Titularidade

	UPDATE
		requerimento_pessoa
	SET
		ide_pessoa = 1095432
	WHERE
		ide_requerimento = 1070246
		AND ide_tipo_pessoa_requerimento = 1;
	
	UPDATE
		requerimento
	SET
		nom_contato = 'FERROLENE SA INDUSTRIA E COMERCIO DE METAIS'
	WHERE
		ide_requerimento = 1070246;
		
-- [34874] Requerimento Duplicado
-- Corrigindo o erro de duplicação do requerimento:
	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1367506;

-- Inserindo os dados referente ao status do ato:
	INSERT INTO status_processo_ato (ide_processo_ato,
    	                             ide_tipo_status_processo_ato,
        	                         dtc_status_processo_ato,
            	                     num_prazo_validade,
                	                 dsc_justificativa_status,
                    	             ind_aprovado,
                        	         ide_pessoa_fisica,
                            	     ind_prazo_indeterminado)                
	SELECT 89688, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89688);

-- [34841] Erro ao finalizar DTRP
		UPDATE
		enquadramento
	SET
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 149440;

-- [34840] Requerimento duplicado
-- Corrigindo o erro de duplicação do requerimento:
	UPDATE
		tramitacao_requerimento
	SET
		ide_status_requerimento = 8
	WHERE
		ide_tramitacao_requerimento = 1367538;
        
-- Inserindo os dados referente ao status do ato:
	INSERT INTO status_processo_ato (ide_processo_ato,
    	                             ide_tipo_status_processo_ato,
                    	             dtc_status_processo_ato,
                        	         num_prazo_validade,
                            	     dsc_justificativa_status,
                                	 ind_aprovado,
                          	         ide_pessoa_fisica,
                            	     ind_prazo_indeterminado)                
	SELECT 89666, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89666);

--[34823] - Requerimento Duplicado

	-- Corrigindo o erro de duplicação do status do requerimento:
 
	UPDATE
	    tramitacao_requerimento
	SET
	    ide_status_requerimento = 8
	WHERE
	    ide_tramitacao_requerimento = 1367510;

	-- Inserindo os dados referente ao status do ato:

	INSERT INTO status_processo_ato (ide_processo_ato,
		                        ide_tipo_status_processo_ato,
		                        dtc_status_processo_ato,
		                        num_prazo_validade,
		                        dsc_justificativa_status,
		                        ind_aprovado,
		                        ide_pessoa_fisica,
		                        ind_prazo_indeterminado)        
	SELECT 89656, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89656);

	INSERT INTO status_processo_ato (ide_processo_ato,
		                        ide_tipo_status_processo_ato,
		                        dtc_status_processo_ato,
		                        num_prazo_validade,
		                        dsc_justificativa_status,
		                        ind_aprovado,
		                        ide_pessoa_fisica,
		                        ind_prazo_indeterminado)   
	SELECT 89657, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89657);

	INSERT INTO status_processo_ato (ide_processo_ato,
		                        ide_tipo_status_processo_ato,
		                        dtc_status_processo_ato,
		                        num_prazo_validade,
		                        dsc_justificativa_status,
		                        ind_aprovado,
		                        ide_pessoa_fisica,
		                        ind_prazo_indeterminado)   
	SELECT 89658, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89658);

	INSERT INTO status_processo_ato (ide_processo_ato,
		                        ide_tipo_status_processo_ato,
		                        dtc_status_processo_ato,
		                        num_prazo_validade,
		                        dsc_justificativa_status,
		                        ind_aprovado,
		                        ide_pessoa_fisica,
		                        ind_prazo_indeterminado)   
	SELECT 89659, 8, now(), NULL, NULL, TRUE, NULL, NULL
	WHERE NOT EXISTS (SELECT 1 FROM status_processo_ato WHERE ide_processo_ato = 89659);
	
-- [34657] - Exclusão de perguntas repetidas, conserto função 

CREATE OR REPLACE FUNCTION public.f_get_resposta(p_ide_pergunta integer, p_ide_requerimento integer)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
DECLARE
    resposta boolean;
BEGIN
	resposta := (select ind_resposta from pergunta_requerimento where ide_requerimento = p_ide_requerimento 
		and ide_pergunta = p_ide_pergunta
		and ind_excluido IS NOT TRUE);
	
	return resposta;
  
END;
$function$
;

UPDATE
	pergunta_requerimento
SET
	ind_excluido = TRUE
WHERE
	ide_pergunta_requerimento IN 
(2093930,2093931,2093932,2093933,2093934,2093935,2093936,2093937,2093940,2101432,2101621,2101626,2093938);



