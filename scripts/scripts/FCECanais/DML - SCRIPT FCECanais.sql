INSERT INTO caracteristica_canal(ide_caracteristica_canal, dsc_caracteristica_canal) VALUES (1, 'Canal de desvio');
INSERT INTO caracteristica_canal(ide_caracteristica_canal, dsc_caracteristica_canal) VALUES (2, 'Canal de derivação');

INSERT INTO tipo_canal(ide_tipo_canal, dsc_tipo_canal) VALUES (1, 'Canal aberto');
INSERT INTO tipo_canal(ide_tipo_canal, dsc_tipo_canal) VALUES (2, 'Canal fechado');
INSERT INTO tipo_canal(ide_tipo_canal, dsc_tipo_canal) VALUES (3, 'Canal parcialmente fechado');

INSERT INTO secao_geometrica(ide_secao_geometrica, dsc_secao_geometrica) VALUES (1, 'Trapezoidal');
INSERT INTO secao_geometrica(ide_secao_geometrica, dsc_secao_geometrica) VALUES (2, 'Retangular');
INSERT INTO secao_geometrica(ide_secao_geometrica, dsc_secao_geometrica) VALUES (3, 'Circular');
INSERT INTO secao_geometrica(ide_secao_geometrica, dsc_secao_geometrica) VALUES (4, 'Outros');

INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (1, 'Terra');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (2, 'Terra armada');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (3, 'Pedra argamassada');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (4, 'Pedra argamassada com fundo natural');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (5, 'Concreto');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (6, 'Concreto com fundo natural');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (7, 'Gabião');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (8, 'Enrocamento');
INSERT INTO tipo_revestimento(ide_tipo_revestimento, dsc_tipo_revestimento) VALUES (9, 'Outros');

INSERT INTO tipo_rio(ide_tipo_rio, dsc_tipo_rio) VALUES (1, 'Perene');
INSERT INTO tipo_rio(ide_tipo_rio, dsc_tipo_rio) VALUES (2, 'Intermitente');
INSERT INTO tipo_rio(ide_tipo_rio, dsc_tipo_rio) VALUES (3, 'Efêmero');

INSERT INTO documento_obrigatorio(ide_documento_obrigatorio, nom_documento_obrigatorio, num_tamanho, ind_formulario, dsc_caminho_arquivo, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo) VALUES (10070, 'Formulário de Caracterização do Empreendimento - Canais', 0, TRUE, NULL, FALSE, 1,TRUE);


--delete from enquadramento_documento_ato where ide_documento_ato  = 8174

--delete from documento_ato where ide_documento_obrigatorio  = 10070 AND ide_tipologia = 305;


----### Insere Documento ATO
--select * from documento_ato where ide_documento_obrigatorio  = 10070;

INSERT INTO documento_ato(ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia, ind_ativo)
select 10070 as ide_documento_obrigatorio, a.ide_ato_ambiental, 381, TRUE from ato_ambiental a 
where a.sgl_ato_ambiental  in ('LI', 'LO', 'LP', 'LU', 'RLI', 'RLO', 'RLP', 'RLU');

INSERT INTO documento_ato(ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia, ind_ativo)
select 10070 as ide_documento_obrigatorio, a.ide_ato_ambiental, 305, TRUE from ato_ambiental a 
where a.sgl_ato_ambiental  in ('OUT');

--#9907
UPDATE tipo_intervencao
   SET nom_tipo_intervencao = 'Canais'
 WHERE ide_tipo_intervencao = 12;
 
 INSERT INTO tipo_finalidade_uso_agua(nom_tipo_finalidade_uso_agua, ind_ativo, ind_requerimento)
    VALUES ('Geração de Energia', true, true);