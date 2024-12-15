INSERT INTO ato_ambiental(
ide_tipo_ato, sgl_ato_ambiental, nom_ato_ambiental, 
ind_declaratorio, num_dias_validade, ind_ativo, ind_automatico, 
ind_visivel_solicitacao_tla)
VALUES (7, 'DIAP', 'Declaração de Intervenção em Área de Preservação Permanente (DIAP)', 
true, 0, true, false, true);

INSERT INTO parametro_calculo(ide_ato_ambiental, valor_taxa, dtc_criacao, ind_boleto, ind_ativo)
    VALUES ((SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 250.00, now(), true, true);

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho, 
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
ind_ativo)
VALUES ('Projeto técnico especificando a categoria da área de intervenção, descrição da atual ocupação e estado de conservação da área, proposta de ocupação e suas alternativas locacionais, discriminação das operações a serem realizadas, cronograma de execução, objetivos e justificativa técnica para as intervenções propostas', 0.000, false, 
true, 1, 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho, 
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
ind_ativo)
VALUES ('Comprovação/Declaração de utilidade pública, de interesse social ou de baixo impacto ambiental da intervenção', 0.000, false, 
true, 1, 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho, 
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
ind_ativo) VALUES ('Anotação de Responsabilidade Técnica (ART), ou equivalente, registrada no competente conselho de classe, dos responsáveis pela elaboração dos documentos técnicos', 0.000, false, 
true, 1, 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho, 
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
ind_ativo) VALUES ('Cópia do ato administrativo de regularidade ambiental (licença ou autorização ambiental) do empreendimento ou atividade expedido pelo ente federativo competente, quando couber', 0.000, false, 
true, 1, 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_obrigatorio(
nom_documento_obrigatorio, num_tamanho, 
ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
ind_ativo) VALUES ('Formulário de Declaração de Intervenção em Área de Preservação Permanente', 0.000, true, 
true, 1, 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

UPDATE documento_obrigatorio  SET ind_ativo  = true WHERE ide_documento_obrigatorio IN (1279,1280);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES (1279, (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES (1280, (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO documento_ato(
ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo)
VALUES (1308, (SELECT currval ('ATO_AMBIENTAL_IDE_ATO_AMBIENTAL_seq')), 
true);

INSERT INTO caracteristica_intervencao_app(nom_caracteristica_intervencao, dtc_criacao,ind_excluido)
    VALUES ('Utilidade pública', now(), false);
    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('as atividades de segurança nacional e proteção sanitária',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('as obras de infraestrutura destinadas às concessões e aos serviços públicos de transporte, sistema viário, inclusive aquele necessário aos parcelamentos de solo urbano aprovados pelos Municípios, saneamento, gestão de resíduos, energia, telecomunicações, radiodifusão, instalações necessárias à realização de competições esportivas estaduais, nacionais ou internacionais, bem como mineração, exceto, neste último caso, a extração de areia, argila, saibro e cascalho',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 


INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('atividades e obras de defesa civil',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 


INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('atividades que comprovadamente proporcionem melhorias na proteção das funções ambientais referidas no inciso II deste artigo',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('outras atividades similares devidamente caracterizadas e motivadas em procedimento administrativo próprio, quando inexistir alternativa técnica e locacional ao empreendimento proposto, definidas em ato do Chefe do Poder Executivo federal',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 
    
INSERT INTO caracteristica_intervencao_app(nom_caracteristica_intervencao, dtc_criacao,ind_excluido)
    VALUES('Interesse social', now(), false);

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('as atividades imprescindíveis à proteção da integridade da vegetação nativa, tais como prevenção, combate e controle do fogo, controle da erosão, erradicação de invasoras e proteção de plantios com espécies nativas',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('a exploração agroflorestal sustentável praticada na pequena propriedade ou posse rural familiar ou por povos e comunidades tradicionais, desde que não descaracterize a cobertura vegetal existente e não prejudique a função ambiental da área',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('a implantação de infraestrutura pública destinada a esportes, lazer e atividades educacionais e culturais ao ar livre em áreas urbanas e rurais consolidadas, observadas as condições estabelecidas nesta Lei',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('a regularização fundiária de assentamentos humanos ocupados predominantemente por população de baixa renda em áreas urbanas consolidadas, observadas as condições estabelecidas na Lei no 11.977, de 7 de julho de 2009',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 


INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('implantação de instalações necessárias à captação e condução de água e de efluentes tratados para projetos cujos recursos hídricos são partes integrantes e essenciais da atividade',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('as atividades de pesquisa e extração de areia, argila, saibro e cascalho, outorgadas pela autoridade competente',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT ide_atividade_intervencao_app FROM atividade_intervencao_app WHERE des_atividade_intervencao_app ilike '%Chefe do Poder Executivo federal') , now(), false); 
    
INSERT INTO caracteristica_intervencao_app(nom_caracteristica_intervencao, dtc_criacao,ind_excluido)
    VALUES('Baixo impacto ambiental', now(), false);

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('abertura de pequenas vias de acesso interno e suas pontes e pontilhões, quando necessárias à travessia de um curso d’água, ao acesso de pessoas e animais para a obtenção de água ou à retirada de produtos oriundos das atividades de manejo agroflorestal sustentável',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false);

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('implantação de instalações necessárias à captação e condução de água e efluentes tratados, desde que comprovada a outorga do direito de uso da água, quando couber',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 


INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('implantação de trilhas para o desenvolvimento do ecoturismo',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('construção de rampa de lançamento de barcos e pequeno ancoradouro',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('construção de moradia de agricultores familiares, remanescentes de comunidades quilombolas e outras populações extrativistas e tradicionais em áreas rurais, onde o abastecimento de água se dê pelo esforço próprio dos moradores',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

       
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('construção e manutenção de cercas na propriedade',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

           
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('pesquisa científica relativa a recursos ambientais, respeitados outros requisitos previstos na legislação aplicável',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('coleta de produtos não madeireiros para fins de subsistência e produção de mudas, como sementes, castanhas e frutos, respeitada a legislação específica de acesso a recursos genéticos',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('plantio de espécies nativas produtoras de frutos, sementes, castanhas e outros produtos vegetais, desde que não implique supressão da vegetação existente nem prejudique a função ambiental da área',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('exploração agroflorestal e manejo florestal sustentável, comunitário e familiar, incluindo a extração de produtos florestais não madeireiros, desde que não descaracterizem a cobertura vegetal nativa existente nem prejudiquem a função ambiental da área',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 

    
INSERT INTO atividade_intervencao_app(
            des_atividade_intervencao_app, 
            dtc_criacao, ind_excluido)
    VALUES ('outras ações ou atividades similares, reconhecidas como eventuais e de baixo impacto ambiental em ato do Conselho Nacional do Meio Ambiente - CONAMA ou dos Conselhos Estaduais de Meio Ambiente',
            now(), false);

INSERT INTO caracteristica_atividade_intervencao_app(
            ide_caracteristica_intervencao_app, 
            ide_atividade_intervencao_app, dtc_criacao, ind_excluido)
    VALUES ((SELECT currval ('caracteristica_intervencao_app_seq')), (SELECT currval('atividade_intervencao_app_seq')), now(), false); 