-- TIPOLOGIAS

INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (2, 2, 'A1','Produtos da Agricultura',NOW(), NULL, FALSE, TRUE, FALSE);

INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (3, (SELECT ide_tipologia FROM tipologia WHERE des_tipologia ILIKE 'Produtos da Agricultura' and ind_excluido = FALSE), 'A1.1','Agricultura',NOW(), NULL, FALSE, TRUE, FALSE);

INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (3, (SELECT ide_tipologia FROM tipologia WHERE des_tipologia ILIKE 'Agricultura' and ind_excluido = FALSE), 'A1.1.1','Agricultura de sequeiro',NOW(), NULL, FALSE, FALSE, FALSE);

INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (3, (SELECT ide_tipologia FROM tipologia WHERE des_tipologia ILIKE 'Agricultura' and ind_excluido = FALSE), 'A1.1.2','Agricultura irrigada',NOW(), NULL, FALSE, FALSE, FALSE);
           
INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (3, 7, 'A2.1','Pecuária',NOW(), NULL, FALSE, TRUE, FALSE);

INSERT INTO tipologia(ide_nivel_tipologia, ide_tipologia_pai, cod_tipologia, 
            des_tipologia, dtc_criacao, dtc_exclusao, ind_excluido, ind_possui_filhos, 
            ind_autorizacao)
    VALUES (3, (SELECT ide_tipologia FROM tipologia WHERE des_tipologia ILIKE 'Pecuária' and ind_excluido = FALSE), 'A2.1.1','Pecuária Extensiva',NOW(), NULL, FALSE, FALSE, FALSE);

INSERT INTO tipologia_grupo(ide_tipologia, ide_tipo_manejo, ide_potencial_poluicao, 
            dtc_criacao, dtc_excluido, ind_excluido)
    VALUES ((SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A1.1.1' and ind_excluido = false) , NULL, NULL, NOW(), NULL, FALSE);

INSERT INTO tipologia_grupo(ide_tipologia, ide_tipo_manejo, ide_potencial_poluicao, 
            dtc_criacao, dtc_excluido, ind_excluido)
    VALUES ((SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A1.1.2' and ind_excluido = false) , NULL, NULL, NOW(), NULL, FALSE);

INSERT INTO tipologia_grupo(ide_tipologia, ide_tipo_manejo, ide_potencial_poluicao, 
            dtc_criacao, dtc_excluido, ind_excluido)
    VALUES ((SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A2.1.1' and ind_excluido = false) , NULL, NULL, NOW(), NULL, FALSE);
 
INSERT INTO unidade_medida_tipologia_grupo (ide_unidade_medida, ide_tipologia_grupo) 
	VALUES (2,(SELECT ide_tipologia_grupo FROM tipologia_grupo WHERE ide_tipologia IN (SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A1.1.1' and ind_excluido = false)));

INSERT INTO unidade_medida_tipologia_grupo (ide_unidade_medida, ide_tipologia_grupo) 
	VALUES (2,(SELECT ide_tipologia_grupo FROM tipologia_grupo WHERE ide_tipologia IN (SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A1.1.2' and ind_excluido = false)));

INSERT INTO unidade_medida_tipologia_grupo (ide_unidade_medida, ide_tipologia_grupo) 
	VALUES (2,(SELECT ide_tipologia_grupo FROM tipologia_grupo WHERE ide_tipologia IN (SELECT ide_tipologia FROM tipologia WHERE cod_tipologia ='A2.1.1' and ind_excluido = false)));
