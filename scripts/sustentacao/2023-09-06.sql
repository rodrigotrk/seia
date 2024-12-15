--Scripts em caráter prioritário
--Data de geração 23/08/2023
--Versão 4.28.8

BEGIN;

-- [35503] - Erro ao realizar a análise técnica da ASV

	UPDATE
	        arquivo_processo
	SET
	        ide_imovel = 85478
	WHERE
	        ide_arquivo_processo = 245639;
	        
	INSERT
	        INTO
	        motivo_notificacao_imovel (ide_motivo_notificacao_imovel, ide_notificacao_motivo_notificacao,
	        ide_imovel)
	VALUES(nextval('motivo_notificacao_imovel_seq'), 81516,
	85478);
	
-- [35488] - Correção usuário não conseguindo cadastrar
	
	UPDATE
		usuario_autorizacao_geobahia
	SET
		ide_pessoa_fisica = 0
	WHERE
		ide_pessoa_fisica = 1101361;
		
-- [35119] - Correção logradouros com municípios errados

	UPDATE
		logradouro
	SET
		ide_bairro = 320838
	WHERE
		ide_bairro = 912
		AND num_cep = '44655000';
		
-- [35548] Transferência de Pauta

	UPDATE
	        controle_tramitacao
	SET
	        ind_fim_da_fila = FALSE
	WHERE
	        ide_controle_tramitacao IN (667151, 614679, 665458, 665004, 654057, 622381, 627706, 613274, 653902, 662217, 636231, 652301, 657660,
	                                    627460, 638517, 633578, 663917, 658712, 650581, 650594, 657540, 659212, 664814, 665018, 665019, 665021, 665022);
	                                                                
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
	VALUES (49270, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (49772, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (63074, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (65583, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (78965, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (79685, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (80133, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (83124, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (83445, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84010, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84079, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84221, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84224, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84273, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84304, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84305, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84349, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84418, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84427, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84610, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (84611, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (87249, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (88357, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (88358, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (88822, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (88823, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL),
	       (89283, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35548', 42, FALSE, 8527, NULL);

-- [35543] - Imóvel rural sumiu do sistema 

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1953490;
	        
-- [35527] Erro ao tentar efetuar enquadramento

	UPDATE
		enquadramento
	SET
		ide_requerimento = NULL
	WHERE
		ide_enquadramento = 168408;
		
--[35505] - Erro ao baixar resumo do requerimento etapa 7

	DELETE
	FROM
		licenca
	WHERE
		ide_licenca = 57417;
		
-- [35125] - Erro na finalização CEFIR

	UPDATE 
		requerimento_imovel
	SET 
		ind_excluido = true
	WHERE 
		ide_requerimento_imovel = 1015814;
		
-- [35556] Transferência de Titularidade

	UPDATE 
		requerimento
	SET
		nom_contato = 'Agrifirma Bahia Agropecuária Ltda'
	WHERE 
		ide_requerimento = 1110783;
	        
	UPDATE 
		requerimento_pessoa
	SET
		ide_pessoa = 80145
	WHERE 
		ide_requerimento = 1110783 AND ide_tipo_pessoa_requerimento = 1;		

-- [35559] Transferência de Pauta

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = FALSE
	WHERE
		ide_controle_tramitacao IN (676780, 676783, 676782);
                                                                
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
	VALUES (83446, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35559', 42, FALSE, 8527, NULL),
		   (82038, 5, 38, now(), TRUE, NULL, 'Solicitação de remanejamento de processo através do chamado Redmine 35559', 42, FALSE, 8527, NULL);
		   

-- [35555] Erro ao salvar shapefile

	UPDATE
	        logradouro
	SET
	        ide_municipio = 1109
	WHERE
	        ide_logradouro = 4493621;
	UPDATE
	        bairro
	SET
	        ide_municipio = 1109
	WHERE
	        ide_bairro = 3329911;

-- [35553] Imóvel Rural sumiu do sistema

	UPDATE
	        pessoa_imovel
	SET
	        ind_excluido = FALSE
	WHERE
	        ide_pessoa_imovel = 1911954;
		
-- [35461] - Erro ao finalizar requerimento

	UPDATE
		licenca
	SET
		ide_requerimento = NULL
	WHERE
		ide_licenca = 44283;

-- [34909] - Erro na aba de Reserva Legal

        DELETE
        FROM
                reserva_legal
        WHERE
                ide_reserva_legal = 888301;

COMMIT;