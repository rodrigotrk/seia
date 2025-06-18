--Scripts em caráter prioritário

BEGIN;
--[34402],[34383],[34381] - Solução dos casos Coelba. 

UPDATE
	public.empreendimento
SET
	ide_localizacao_geografica = 3401661,
	ide_pessoa = 1882,
	nom_empreendimento = 'AC-REG-FV. RR-CCOL-POV. CALDEIRÃORURAL (X-0987241)',
	val_investimento = NULL,
	dtc_criacao = '2022-06-30 09:54:42.019',
	ind_excluido = FALSE,
	qtd_funcionarios = NULL,
	des_email = 'testesema@gmail.com',
	num_cadastro_tecnico_federal = NULL,
	dtc_validade_ctf = NULL,
	ind_correspondencia = TRUE,
	dtc_exclusao = NULL,
	ind_endereco_correspondencia = FALSE,
	ind_cessionario = TRUE,
	ind_conversao_tcra_lac = NULL,
	ind_base_operacional = NULL,
	ind_unidade_conservacao = NULL
WHERE
	ide_empreendimento = 164495;

--[34047]- Erro na finalização do CEFIR-FAZENDA LOTE 17(Ipê)
        UPDATE
        	reserva_legal 
        SET 
            ide_tipo_arl = 3 
        WHERE 
            ide_reserva_legal = 10953;

--[34494] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 524171;

--[34491] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE  
	WHERE 
                ide_controle_tramitacao = 571579;

--[34490] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE  
        WHERE 
                ide_controle_tramitacao = 572611;
--[34484] -Processo Duplicado
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = FALSE  
        WHERE 
                ide_controle_tramitacao = 563643;
--[34476] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN  (570099,570100);

--[34475] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 570830;
                
 --[34474] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE 
	WHERE 
                ide_controle_tramitacao = 567232;  
        
--[34403]-Imóvel Rural/CEFIR - erro na reserva legal
--FAZENDA PEDRA V - LOTE 31
        UPDATE 
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 4526; 
        
--Fazenda São João - Lote 33
        UPDATE 
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 56610;
        
--FAZENDA BAUZINHO I - LOTE 32
        UPDATE 
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 4515;

COMMIT;

                
 --Scripts tickets comuns   
  
 --[34474] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 567232;  
        
--[34475] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 570830;
        
--[34476] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao IN  (570099,570100);
COMMIT;

--[34402],[34383],[34381] - Solução dos casos Coelba. 

        UPDATE
                public.empreendimento
        SET
                ide_localizacao_geografica = 3401661,
                ide_pessoa = 1882,
                nom_empreendimento = 'AC-REG-FV. RR-CCOL-POV. CALDEIRÃORURAL (X-0987241)',
                val_investimento = NULL,
                dtc_criacao = '2022-06-30 09:54:42.019',
                ind_excluido = FALSE,
                qtd_funcionarios = NULL,
                des_email = 'testesema@gmail.com',
                num_cadastro_tecnico_federal = NULL,
                dtc_validade_ctf = NULL,
                ind_correspondencia = TRUE,
                dtc_exclusao = NULL,
                ind_endereco_correspondencia = FALSE,
                ind_cessionario = TRUE,
                ind_conversao_tcra_lac = NULL,
                ind_base_operacional = NULL,
                ind_unidade_conservacao = NULL
        WHERE
                ide_empreendimento = 164495;

--[34484] -Processo Duplicado
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = FALSE  
	WHERE 
        ide_controle_tramitacao = 563643;

--[34490] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao = 572611;

--[34491] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao = 571579;

--[34494] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao = 524171;

--[33903] -Processo Duplicado
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = FALSE 
	WHERE 
        ide_controle_tramitacao = 541124;

--[34251]
        UPDATE
                arquivo_processo
        SET
                ide_imovel = 1010190
        WHERE
                ide_arquivo_processo = 210094;

        UPDATE
                arquivo_processo
        SET
                ide_imovel = 1010190
        WHERE
                ide_arquivo_processo = 203291;
                
--[34047]- Erro na finalização do CEFIR-FAZENDA LOTE 17(Ipê)
        UPDATE
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 10953;
--[33903] -Processo Duplicado
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = FALSE 
        WHERE 
                ide_controle_tramitacao = 541124;
                
 --[34271] - Casos de Lac

		UPDATE
			public.licenca
		SET
			ind_excluido = TRUE
		WHERE
			ide_licenca IN (47597, 47598, 47820, 49154, 48360, 48318, 48319, 48348, 48349, 48026, 48464, 48205, 48531);

        UPDATE
                public.licenca
        SET
                ind_excluido = TRUE
        WHERE
                ide_licenca IN (47597, 47598, 47820, 49154, 48360, 48318, 48319, 48348, 48349, 48026, 48464, 48205, 48531);

--[34506] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE  
	WHERE 
                ide_controle_tramitacao IN (301954,301955,301956);
			
--[34504] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE 
	WHERE 
                ide_controle_tramitacao IN (544068,544069);
        UPDATE
                public.licenca
        SET
                ind_excluido = TRUE
        WHERE
                ide_licenca IN (47597, 47598, 47820, 49154, 48360, 48318, 48319, 48348, 48349, 48026, 48464, 48205, 48531);

--#34501 - Inclusão de espécie
-- nome científico: Cordiera coriacea
-- nome popular: PAU-DE-MACACO
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Cordiera coriacea',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('PAU-DE-MACACO');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'),
                (SELECT currval ('especie_supressao_seq' ))
                );
        -- nome científico: Leptolobium bijugum 
        -- nome popular: VASSOURINHA
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Leptolobium bijugum',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('VASSOURINHA');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'),
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Manilkara dardanoi
        -- nome popular: MASSARANDUBA
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Manilkara dardanoi',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('MASSARANDUBA');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Manilkara salzmannii
        -- nome popular: MASSARANDUBA PRETA
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('MASSARANDUBA PRETA');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval('nome_popular_especie_seq'),12366);
                        
        -- nome científico: Monteverdia obtusifolia
        -- nome popular: BOM NOME 
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Monteverdia obtusifolia',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('BOM NOME');
        
-- nome científico: Salzmannia nitida
-- nome popular: CATUCÁ
INSERT INTO
        especie_supressao
        (nom_especie_supressao,
        ind_ativo)
VALUES
        ('Salzmannia nitida',
        TRUE);
       
INSERT INTO
        nome_popular_especie
        (nom_popular_especie)
VALUES
        ('CATUCÁ');
       
INSERT INTO
        especie_supressao_nome_popular_especie
        (ide_nome_popular_especie,
        ide_especie_supressao)
VALUES
        (currval ('nome_popular_especie_seq'), 
        (SELECT currval ('especie_supressao_seq' ))
        );
			
--[34504] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE 
	WHERE 
        ide_controle_tramitacao IN (544068,544069);
		
--[34289] - [REQUERIMENTO] Correção das informações no resumo do requerimento

		UPDATE
			solicitacao_administrativo
		SET
			num_portaria = '26.183',
			dtc_publicacao_portaria = '2022-06-01 00:00:00.000',
			dtc_validade = '2026-06-01 00:00:00.000'
		WHERE		
			ide_solicitacao_administrativo = 5491;
			
--[34506] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao IN (301954,301955,301956);

        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico:Myrcia bergiana 
        -- nome popular: ARAÇÁ
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Myrcia bergiana',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('ARAÇÁ');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Myrcia decorticans
        -- nome popular: ARAÇÁ
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Myrcia decorticans',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('ARAÇÁ');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Myrcia rosangelae 
        -- nome popular: MURTA 
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Myrcia rosangelae',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('MURTA');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Psidium amplexicaule
        -- nome popular: ARAÇÁ
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Psidium amplexicaule',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('ARAÇÁ');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
                
        -- nome científico: Salzmannia nitida
        -- nome popular: CATUCÁ
        INSERT INTO
                especie_supressao
                (nom_especie_supressao,
                ind_ativo)
        VALUES
                ('Salzmannia nitida',
                TRUE);
        
        INSERT INTO
                nome_popular_especie
                (nom_popular_especie)
        VALUES
                ('CATUCÁ');
        
        INSERT INTO
                especie_supressao_nome_popular_especie
                (ide_nome_popular_especie,
                ide_especie_supressao)
        VALUES
                (currval ('nome_popular_especie_seq'), 
                (SELECT currval ('especie_supressao_seq' ))
                );
       		
--[34448] - [PROCESSO] Transferência de pauta
        UPDATE
                controle_tramitacao
        SET
                ide_pauta = 378,ide_status_fluxo = 5
        WHERE
                ide_processo in (18072,52577,56200,56201,63219,65127)
                and ide_pauta = 1918
                and ind_fim_da_fila = true;
        
--Mudar tipo de usuário de interno para externo 
UPDATE
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE 
WHERE
        ide_pessoa_fisica = 7056;

--Mudar tipo de usuário de interno para externo
        UPDATE
                usuario 
        SET
                ide_perfil = 2,
                ind_tipo_usuario = FALSE 
        WHERE
                ide_pessoa_fisica = 7056;
		UPDATE
			public.licenca
		SET
			ind_excluido = TRUE
		WHERE
			ide_licenca IN (47597, 47598, 47820, 49154, 48360, 48318, 48319, 48348, 48349, 48026, 48464, 48205, 48531);
		
--[34423] - PROCESSO [ Transferência de processo ]
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = FALSE 
WHERE
        ide_processo in (74359,75421,20700,74543,75064,76667,76969,79388,79455,80040,80063)
        and ide_pauta IN (1612,715)
        and ind_fim_da_fila = TRUE;
        
INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (74359,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (75421,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226);       
        
 INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (20700,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (74543,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (75064,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (76667,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (76969,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (79388,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (79455,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (80040,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226); 
      INSERT INTO 
        controle_tramitacao (ide_processo,
        ide_status_fluxo,
        ide_area,
        dtc_tramitacao,
        ind_fim_da_fila,
        dsc_comentario_interno,ide_pauta ,
        ind_responsavel,
        ide_pessoa_fisica)
       VALUES  (80063,5,49,now(),TRUE,'Solicitação de remanejamento de processo através do chamado Redmine 34423',375,TRUE,13226);
       
--Mudando de usuário interno para externo      
UPDATE
        usuario 
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE 
WHERE
        ide_pessoa_fisica = 118385;   
        
--1° Delete 
DELETE FROM processo_ato_integrante_equipe WHERE ide_equipe_integrante IN (16063,17482,18000,18365,19036,23994,23996,24954,30893,
40107,40110,38755,40113,40750,40753,41353,41626,41628,41630,41632,42513,42813,42816,43121,46730,46732,47162,57006,42332,47080,53585,53586,
53466,53467,53468,53767,53776,53916); 

--2° Delete 
DELETE FROM integrante_equipe WHERE ide_integrante_equipe  IN (16063,17482,18000,18365,19036,23994,23996,24954,30893,
40107,40110,38755,40113,40750,40753,41353,41626,41628,41630,41632,42513,42813,42816,43121,46730,46732,47162,57006,42332,47080,53585,53586,
53466,53467,53468,53767,53776,53916);

UPDATE pauta SET ide_pessoa_fisica = NULL WHERE ide_pauta =1612;

--3° Delete 
DELETE FROM funcionario WHERE ide_pessoa_fisica = 118385;        	     			
       			
--[34289] - [REQUERIMENTO] Correção das informações no resumo do requerimento

		UPDATE
			solicitacao_administrativo
		SET
			num_portaria = '26.183',
			dtc_publicacao_portaria = '2022-06-01 00:00:00.000',
			dtc_validade = '2026-06-01 00:00:00.000'
		WHERE
			ide_solicitacao_administrativo = 5491;		
			
DELETE FROM funcionario WHERE ide_pessoa_fisica = 118385;  

COMMIT;

