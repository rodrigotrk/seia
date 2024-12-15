INSERT INTO acao(dsc_acao, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES ('Cadastrar', false, now(), null);

INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao, dtc_exclusao)
    VALUES (2, 'CERH', false, now(), null);

    
    
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass));

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-cerh/consulta.xhtml', false);

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-cerh/cadastro.xhtml', false);

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/identificar-papel/identificar-papel.xhtml', false);
    
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass), 1);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass), 2);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass), 3);

INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), currval(('Acao_IDE_ACAO_seq'::text)::regclass), 5);

    
        
INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Em análise');

INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Indeferido');

INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Dispensado');

INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Inexigível');
    
INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Outorgado');
    
INSERT INTO cerh_situacao_regularizacao(dsc_situacao_regularizacao)
    VALUES ('Cancelado');

    
    
INSERT INTO tipo_uso_recurso_hidrico(dsc_tipo_uso_recurso_hidrico)
    VALUES ('Barragem');
    
INSERT INTO tipo_uso_recurso_hidrico(dsc_tipo_uso_recurso_hidrico)
    VALUES ('Captação Subterrânea');
    
INSERT INTO tipo_uso_recurso_hidrico(dsc_tipo_uso_recurso_hidrico)
    VALUES ('Captação Superficial');
    
INSERT INTO tipo_uso_recurso_hidrico(dsc_tipo_uso_recurso_hidrico)
    VALUES ('Outros tipo de intervenção');
    
INSERT INTO tipo_uso_recurso_hidrico(dsc_tipo_uso_recurso_hidrico)
    VALUES ('Lançamento de efluentes em corpo hídrico');

    
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('Este cadastro está associado a um Contrato/Convênio?', true, now(), null, 'CERH_DG_P01');
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('O empreendimento possui processo(s) de outorga em trâmite ou concluído no INEMA?', true, now(), null, 'CERH_DG_P02');
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('O empreendimento faz outros usos além dos contemplados no(s) processos informado(s)?', true, now(), null, 'CERH_DG_P03');
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('O empreendimento faz intervenção em corpo hídrico?', true, now(), null, 'CERH_DG_P04');
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('O empreendimento faz captação de água?', true, now(), null, 'CERH_DG_P05');
    
INSERT INTO cerh_pergunta_dados_gerais(dsc_pergunta, ind_ativo, dtc_criacao, dtc_exclusao, cod_pergunta)
    VALUES ('O empreendimento faz lançamento de efluentes em corpo hídrico?', true, now(), null, 'CERH_DG_P06');
    
    
    
INSERT INTO cerh_tipo_autorizacao_outorgado(dsc_cerh_tipo_autorizacao_outorgado)
	VALUES ('Direito de uso');
	
INSERT INTO cerh_tipo_autorizacao_outorgado(dsc_cerh_tipo_autorizacao_outorgado)
	VALUES ('Preventiva');

INSERT INTO cerh_tipo_ato_dispensa(dsc_tipo_ato_dispensa)
    VALUES ('Carta');
    
INSERT INTO cerh_tipo_ato_dispensa(dsc_tipo_ato_dispensa)
    VALUES ('Declaração');
    
INSERT INTO cerh_tipo_ato_dispensa(dsc_tipo_ato_dispensa)
    VALUES ('Notificação');
    
INSERT INTO cerh_tipo_ato_dispensa(dsc_tipo_ato_dispensa)
    VALUES ('Ofício');

INSERT INTO tipo_projeto(nom_tipo_projeto, dtc_criacao, ind_excluido, dtc_exclusao)
    VALUES ('Cadastro de Usuários de Recursos Hídricos', now(), false, null);
    
INSERT INTO cerh_tratamento_efluente (dsc_tratamento_efluente) 
	VALUES ('Preliminar');
	
INSERT INTO cerh_tratamento_efluente (dsc_tratamento_efluente) 
	VALUES ('Primário');
	
INSERT INTO cerh_tratamento_efluente (dsc_tratamento_efluente) 
	VALUES ('Secundário');
	
INSERT INTO cerh_tratamento_efluente (dsc_tratamento_efluente)
	VALUES ('Terciário');

	
	
INSERT INTO cerh_tipo_prestador_servico(dsc_tipo_prestador_servico) 
	VALUES ('Administração direta (prefeitura)');
	
INSERT INTO cerh_tipo_prestador_servico(dsc_tipo_prestador_servico)
	VALUES ('Administração indireta (SAAE ou similares)');

INSERT INTO cerh_tipo_prestador_servico(dsc_tipo_prestador_servico) 
	VALUES ('Concessionária (comp. Estaduais, empresa privada)');

INSERT INTO cerh_tipo_prestador_servico(dsc_tipo_prestador_servico) 
	VALUES ('Autorizada (Associações, cooperativas)');

	
INSERT INTO cerh_situacao_tipo_uso(dsc_situacao_tipo_uso) 
	VALUES 	('Projeto'), 
			('Construção'), 
			('Operação'), 
			('Desativada');

	
	
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.071341 -10.569916)', 4674)), false, 'Adustina');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.867389 -12.484833)', 4674)), false, 'Afligidos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-45.724167 -11.941944)', 4674)), false, 'Agronol 01');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.592778 -14.924722)', 4674)), false, 'Água Fria I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.571594 -14.912097)', 4674)), false, 'Água Fria II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.343056 -10.596944)', 4674)), false, 'Aipim');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.044167 -10.880556)', 4674)), false, 'Alazão   ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.513106 -12.985181)', 4674)), false, 'Alpercata I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.562333 -12.419556)', 4674)), false, 'Alto Alegre');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.872217 -11.345328)', 4674)), false, 'Alto Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.562944 -14.768972)', 4674)), false, 'Amargoso');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.190090 -14.630331)', 4674)), false, 'Anagé ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.785278 -10.301111)', 4674)), false, 'Andorinha II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.947639 -11.638250)', 4674)), false, 'Angelim');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.949000 -14.947778)', 4674)), false, 'Angico');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.326833 -11.703153)', 4674)), false, 'Angico');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.465556 -14.377500)', 4674)), false, 'Aniceto');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.330133 -10.383417)', 4674)), false, 'Antas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.442056 -13.077639)', 4674)), false, 'Apertado ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.530318 -14.449261)', 4674)), false, 'Aracatu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.101667 -11.252556)', 4674)), false, 'Araci');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.494444 -12.092222)', 4674)), false, 'Aramari');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-44.423333 -12.095278)', 4674)), false, 'Aricobé');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.395106 -13.332388)', 4674)), false, 'Arizona I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.387884 -13.330444)', 4674)), false, 'Arizona II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.377328 -13.330166)', 4674)), false, 'Arizona III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.608336 -11.675197)', 4674)), false, 'Arroz');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.274114 -11.555131)', 4674)), false, 'Baixa do Governo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.940556 -10.612500)', 4674)), false, 'Baixão do Cecílio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.948611 -12.359722)', 4674)), false, 'Bambu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.813472 -13.029194)', 4674)), false, 'Bandeira de Melo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.157244 -11.605933)', 4674)), false, 'Bandiaçu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.999325 -9.316711)', 4674)), false, 'Barauna');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.056889 -11.810944)', 4674)), false, 'Barra do Mendes ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.037778 -13.168333)', 4674)), false, 'Barra do São João');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.863333 -9.580000)', 4674)), false, 'Barragem 01 - Caraibas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.851944 -9.586111)', 4674)), false, 'Barragem 02 - Caraibas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.851944 -9.588889)', 4674)), false, 'Barragem 03 - Caraibas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.840000 -9.590000)', 4674)), false, 'Barragem 04 - Caraibas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.278611 -14.513528)', 4674)), false, 'Barreiro ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.738889 -14.947778)', 4674)), false, 'Barreiro 2');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.959864 -13.611228)', 4674)), false, 'Barreiro Vermelho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.579722 -12.966111)', 4674)), false, 'Barrocão (P)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.726250 -12.540389)', 4674)), false, 'Beco Bebedouro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.342611 -14.763333)', 4674)), false, 'Bela Vista');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.267328 -11.476658)', 4674)), false, 'Boa Vista');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.693611 -11.759833)', 4674)), false, 'Boa Vista 2');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.112500 -11.395472)', 4674)), false, 'Boca d''Água');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.431389 -13.400028)', 4674)), false, 'Boca do Mato');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.532500 -13.398611)', 4674)), false, 'Botuporã');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.073056 -11.279167)', 4674)), false, 'Brejão');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.458718 -13.450306)', 4674)), false, 'Brejinho Carlinhos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.436218 -13.481278)', 4674)), false, 'Brejinho Maracujá ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.421774 -13.466001)', 4674)), false, 'Brejinho Saladino I ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.420663 -13.458778)', 4674)), false, 'Brejinho Saladino II ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.435941 -13.459334)', 4674)), false, 'Brejinho Sede / BA03');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.444552 -13.422667)', 4674)), false, 'Brejinho Tonhão / Riacho Lagoinha - BA02');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.734111 -11.330417)', 4674)), false, 'Brejo Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.670233 -14.227540)', 4674)), false, 'Brumado');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.428056 -14.984917)', 4674)), false, 'Caatiba');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.701944 -10.984444)', 4674)), false, 'Caatinga do Moura');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.990039 -11.553742)', 4674)), false, 'Cabeça da Vaca');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.053917 -12.007853)', 4674)), false, 'Cabeceira do Rio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.893889 -11.801667)', 4674)), false, 'Caçadinho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.582806 -14.604528)', 4674)), false, 'Cachoeira');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.431947 -11.351808)', 4674)), false, 'Cachoeira Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.432289 -12.956467)', 4674)), false, 'Cachoeirinha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.465278 -11.694444)', 4674)), false, 'Cafarnaum');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.482222 -12.962500)', 4674)), false, 'Caibaté');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.719917 -12.233778)', 4674)), false, 'Caiçara');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.432267 -10.834683)', 4674)), false, 'Cajueiro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.568072 -10.617278)', 4674)), false, 'Caldeirão Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-44.004217 -12.620264)', 4674)), false, 'Campestre');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.592500 -12.919156)', 4674)), false, 'Campinas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.171167 -14.089583)', 4674)), false, 'Campo do Araça');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.905222 -11.822111)', 4674)), false, 'Candeal');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.026583 -10.598292)', 4674)), false, 'Cândido Caldas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-45.084747 -11.625842)', 4674)), false, 'Canudos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.637522 -13.021150)', 4674)), false, 'Capão Comprido');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.554842 -13.001503)', 4674)), false, 'Capão do Mel');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.215439 -10.669689)', 4674)), false, 'Capim Duro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-44.151806 -12.638694)', 4674)), false, 'Caraíbas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.355833 -10.486667)', 4674)), false, 'Cariacá');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.979250 -13.603972)', 4674)), false, 'Carro Quebrado I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.032222 -10.843889)', 4674)), false, 'Casa Nova');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.280972 -14.317222)', 4674)), false, 'Casa Velha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.703775 -14.247025)', 4674)), false, 'Catiboaba');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.382944 -12.349500)', 4674)), false, 'Catú ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.379586 -11.778864)', 4674)), false, 'Cedro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.681389 -14.284444)', 4674)), false, 'Ceraíma');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.962809 -14.908885)', 4674)), false, 'Champrão');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.799722 -11.141556)', 4674)), false, 'Chororó');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.780556 -14.246944)', 4674)), false, 'Cipó');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.461111 -12.892222)', 4674)), false, 'Cobre');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.826111 -10.721667)', 4674)), false, 'Coité');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.961833 -13.840139)', 4674)), false, 'Coloco');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.275556 -14.546111)', 4674)), false, 'Comocoxico');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.923056 -11.138333)', 4674)), false, 'Conceição');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.531111 -15.422222)', 4674)), false, 'Córrego Braço do Sul');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.810083 -12.818750)', 4674)), false, 'Cotia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.795556 -14.767500)', 4674)), false, 'Cova da Mandioca');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.975698 -13.943543)', 4674)), false, 'Crisciúma');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.473131 -13.961124)', 4674)), false, 'Cristalândia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.529714 -11.260033)', 4674)), false, 'Cuia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.279833 -13.582222)', 4674)), false, 'Curral da Vargem');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.573467 -10.693083)', 4674)), false, 'Curral Falso');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.504622 -11.092181)', 4674)), false, 'Curralinho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.223333 -10.459444)', 4674)), false, 'Delfino');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.364167 -14.536667)', 4674)), false, 'Divino ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.920833 -10.917500)', 4674)), false, 'Espanta Gado');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.724167 -14.763944)', 4674)), false, 'Espinheiro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.670972 -12.398361)', 4674)), false, 'Estreito');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.798333 -11.100000)', 4674)), false, 'Fazenda Alagadiço Raso');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.946111 -11.279167)', 4674)), false, 'Fazenda Barriguda I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.914167 -11.193611)', 4674)), false, 'Fazenda Barriguda II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.767778 -13.766944)', 4674)), false, 'Fazenda Bonfim');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.493056 -13.086111)', 4674)), false, 'Fazenda Brogodó e Olhos D''Água');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.802722 -12.356583)', 4674)), false, 'Fazenda Campestre');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.948889 -15.252222)', 4674)), false, 'Fazenda Casca');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.534722 -15.417500)', 4674)), false, 'Fazenda Itaci');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.840556 -11.245833)', 4674)), false, 'Fazenda Jiló de Intã');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.978333 -10.690000)', 4674)), false, 'Fazenda Junco');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.726417 -11.830056)', 4674)), false, 'Fazenda Marquesa');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.278417 -13.343278)', 4674)), false, 'Fazenda Nova');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.462311 -13.391542)', 4674)), false, 'Fazenda Paraíso I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.458775 -13.390250)', 4674)), false, 'Fazenda Paraíso II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.006111 -10.730278)', 4674)), false, 'Fazenda Pedra Vermelha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.570833 -13.759444)', 4674)), false, 'Fazenda Pedrinhas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.730278 -11.243611)', 4674)), false, 'Fazenda Praça');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.368083 -13.313583)', 4674)), false, 'Fazenda Progresso I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.502653 -13.137375)', 4674)), false, 'Fazenda Progresso II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.439492 -13.402653)', 4674)), false, 'Fazenda Progresso III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.442639 -13.364992)', 4674)), false, 'Fazenda Riachão');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.127500 -11.086944)', 4674)), false, 'Fazenda Roçado da Tabúa');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.790000 -11.189444)', 4674)), false, 'Fazenda Saco');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.822222 -11.227500)', 4674)), false, 'Fazenda Santo Antônio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.033056 -10.811111)', 4674)), false, 'Fazenda Socó Boi');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.096472 -11.236556)', 4674)), false, 'Fazenda Tamboril');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.514683 -12.861697)', 4674)), false, 'Fazenda Três Irmãos I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.511117 -12.870306)', 4674)), false, 'Fazenda Três Irmãos II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.920000 -11.166389)', 4674)), false, 'Fazenda Várzea Grande ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.282028 -15.939917)', 4674)), false, 'Fazendas Reunidas Boa Sorte');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.198542 -10.267561)', 4674)), false, 'Fonte Velha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.602417 -11.560361)', 4674)), false, 'França');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.474581 -12.766775)', 4674)), false, 'FWR ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.031944 -10.193333)', 4674)), false, 'Gameleira do Dida');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.773944 -11.467481)', 4674)), false, 'Gavião');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.872731 -10.674078)', 4674)), false, 'Genipapo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.122033 -13.668569)', 4674)), false, 'Girau I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.545111 -12.989417)', 4674)), false, 'Graciosa I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.537894 -12.999369)', 4674)), false, 'Graciosa II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.540111 -12.993686)', 4674)), false, 'Graciosa III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.400139 -11.800556)', 4674)), false, 'Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.506774 -13.177860)', 4674)), false, 'Guaíra I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.493163 -13.192943)', 4674)), false, 'Guaíra II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.963889 -14.609444)', 4674)), false, 'Guajeru');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.518611 -12.854444)', 4674)), false, 'Guará (P)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.275003 -10.670747)', 4674)), false, 'Heliópolis');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.994722 -9.178889)', 4674)), false, 'Honorato Viana');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.082660 -14.746108)', 4674)), false, 'Iguape ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.383333 -12.897500)', 4674)), false, 'Ipitanga I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.397222 -12.859722)', 4674)), false, 'Ipitanga II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.133611 -12.328694)', 4674)), false, 'Itapeba');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.538611 -11.230000)', 4674)), false, 'Itapicuruzinho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.283461 -11.536853)', 4674)), false, 'Itarandi');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.327472 -14.302444)', 4674)), false, 'Jacaré');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.489453 -10.828711)', 4674)), false, 'Jacu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.112500 -12.130833)', 4674)), false, 'Jaguara');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.938389 -14.311944)', 4674)), false, 'Jatobá');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.324281 -12.836200)', 4674)), false, 'Joanes I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.378611 -12.674444)', 4674)), false, 'Joanes II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.267917 -13.338611)', 4674)), false, 'Joaninha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.855139 -13.254944)', 4674)), false, 'Juraci ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.316667 -12.536389)', 4674)), false, 'Juraci Magalhães');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.597778 -13.508833)', 4674)), false, 'Jussiape');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.654006 -10.571006)', 4674)), false, 'Lage Nova');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.779444 -11.054722)', 4674)), false, 'Lages do Batata');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.210333 -10.479333)', 4674)), false, 'Laginha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.150139 -13.931250)', 4674)), false, 'Lagoa Barauna');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.688611 -13.678333)', 4674)), false, 'Lagoa da Torta');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.286944 -10.528056)', 4674)), false, 'Lagoa do Angico');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.397653 -10.621058)', 4674)), false, 'Lagoa do Meio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.449769 -10.040039)', 4674)), false, 'Lagoa do Pires');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.717556 -14.993333)', 4674)), false, 'Lagoa do Umbuzeiro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.565333 -12.687000)', 4674)), false, 'Lagoa dos Patos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.380825 -13.294089)', 4674)), false, 'Lagoa dos Patos I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.380875 -13.293453)', 4674)), false, 'Lagoa dos Patos II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.046389 -11.353056)', 4674)), false, 'Lagoa dos Remédios');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.122211 -9.975197)', 4674)), false, 'Lagoa São Miguel');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.622750 -10.458339)', 4674)), false, 'Laje do Antônio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.471944 -14.327500)', 4674)), false, 'Lajedão');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.472264 -12.776708)', 4674)), false, 'LANDFIL III (Resíduo)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.669444 -10.985556)', 4674)), false, 'Leste');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.015194 -11.657028)', 4674)), false, 'Leste');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.649444 -9.553611)', 4674)), false, 'Localidade Campo dos Cavalos (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.643333 -9.629444)', 4674)), false, 'Localidade de Angico (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.648889 -9.614444)', 4674)), false, 'Localidade do Alfavaca (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.650833 -9.590556)', 4674)), false, 'Localidade do Arame (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.650167 -9.528056)', 4674)), false, 'Localidade do Curral Novo (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.653333 -9.544444)', 4674)), false, 'Localidade do Horto (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.651667 -9.576111)', 4674)), false, 'Localidade do Recanto (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.653056 -9.515556)', 4674)), false, 'Localidade do Sabiá (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.653056 -9.496944)', 4674)), false, 'Localidade do Sabiá (galgável)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.822722 -13.569306)', 4674)), false, 'Luiz Vieira ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.930561 -11.822503)', 4674)), false, 'Macacos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.360833 -12.133333)', 4674)), false, 'Macajuba');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.351111 -12.133611)', 4674)), false, 'Macajuba 2');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.550000 -13.013611)', 4674)), false, 'Macaúbas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.480833 -14.674167)', 4674)), false, 'Maetinga');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.878361 -14.399000)', 4674)), false, 'Malhada de Pedras');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.420833 -11.309167)', 4674)), false, 'Malhada Velha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.897469 -12.157928)', 4674)), false, 'Malhador ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.065944 -14.518333)', 4674)), false, 'Mamonas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.298333 -11.609583)', 4674)), false, 'Manguinhas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.833636 -11.921403)', 4674)), false, 'Manoel Maria');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.917369 -11.349089)', 4674)), false, 'Maracujá');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.891667 -14.375639)', 4674)), false, 'Marota');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.183333 -14.255944)', 4674)), false, 'Mata do Meio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.432361 -11.180750)', 4674)), false, 'Mata do Milho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.489722 -14.658056)', 4674)), false, 'Mateiro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.487500 -14.651167)', 4674)), false, 'Mateiro 2');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.797278 -13.710278)', 4674)), false, 'Matheus');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.543667 -14.390889)', 4674)), false, 'Matias');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.355000 -10.535000)', 4674)), false, 'Mato Limpo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.103333 -10.591389)', 4674)), false, 'Melancia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.989467 -11.693772)', 4674)), false, 'Miguel Calmon');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.985000 -12.040806)', 4674)), false, 'Milagres');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.344444 -11.459444)', 4674)), false, 'Mirorós ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.198056 -14.414167)', 4674)), false, 'Mocambo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.578889 -10.818333)', 4674)), false, 'Monteiro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.301394 -14.571385)', 4674)), false, 'Morrinhos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.268611 -13.036389)', 4674)), false, 'Mulungu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.759964 -13.012561)', 4674)), false, 'MWR 101');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.764953 -13.026986)', 4674)), false, 'MWR 102');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.366667 -13.257778)', 4674)), false, 'Nova Esperança');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.502336 -12.980550)', 4674)), false, 'Nova Guiné');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.821917 -11.932028)', 4674)), false, 'OAS');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.086667 -10.975556)', 4674)), false, 'Ourolândia ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.531389 -15.420361)', 4674)), false, 'Panelão');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.244956 -12.017356)', 4674)), false, 'Paty');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.172272 -9.361486)', 4674)), false, 'Pau Preto');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.545497 -10.642914)', 4674)), false, 'Pedra');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.000325 -12.586500)', 4674)), false, 'Pedra do Cavalo (UHE)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.521572 -10.694656)', 4674)), false, 'Pedra Riscada');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.920833 -14.454167)', 4674)), false, 'Pedras');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.053472 -11.188833)', 4674)), false, 'Pedras Altas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.007036 -10.481586)', 4674)), false, 'Pedregulho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.545900 -10.528072)', 4674)), false, 'Periperi ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.144722 -11.183056)', 4674)), false, 'Piabas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.855278 -13.097778)', 4674)), false, 'Piatã (P)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.797833 -12.888833)', 4674)), false, 'Piau');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.404778 -10.797472)', 4674)), false, 'Pindobaçu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.887361 -9.580750)', 4674)), false, 'Pinhões');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.786667 -15.032222)', 4674)), false, 'Piripá');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.425833 -12.958333)', 4674)), false, 'Pituaçu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.825083 -13.487778)', 4674)), false, 'Placa');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.026667 -9.042222)', 4674)), false, 'Poço da Pedra');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.816667 -14.257222)', 4674)), false, 'Poço do Magro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.049444 -11.903056)', 4674)), false, 'Poço Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.170833 -9.748056)', 4674)), false, 'Poções');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.171972 -10.853528)', 4674)), false, 'Ponto Novo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.864722 -11.260556)', 4674)), false, 'Povoado de Lagoa de Dentro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.933056 -11.213611)', 4674)), false, 'Povoado de Salinas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.205833 -10.508056)', 4674)), false, 'Prata');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.525556 -14.538333)', 4674)), false, 'Queimadas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.495278 -12.868447)', 4674)), false, 'Quem Quem');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.010222 -10.488833)', 4674)), false, 'Quicé');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.937928 -11.759342)', 4674)), false, 'Quinji');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.830556 -12.292500)', 4674)), false, 'Quixabeira');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.028701 -9.638199)', 4674)), false, 'Rancharia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.481919 -13.570036)', 4674)), false, 'Rapadura');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.566572 -12.907367)', 4674)), false, 'Ratinho I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.567492 -12.908811)', 4674)), false, 'Ratinho II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.568286 -12.920947)', 4674)), false, 'Ratinho III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.850708 -13.097042)', 4674)), false, 'Ressaca');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.987750 -11.545000)', 4674)), false, 'Riacho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.836389 -11.294444)', 4674)), false, 'Riacho Conceição');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.148689 -10.389217)', 4674)), false, 'Riacho da Água');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-44.155000 -12.948889)', 4674)), false, 'Riacho da Ema');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.796222 -11.087306)', 4674)), false, 'Riacho da Lage');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.735556 -11.243611)', 4674)), false, 'Riacho da Onça');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.871111 -13.597500)', 4674)), false, 'Riacho de Santana');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.804500 -13.758806)', 4674)), false, 'Riacho do Paulo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.946667 -10.215833)', 4674)), false, 'Riacho do Sítio');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.075719 -9.238978)', 4674)), false, 'Riacho dos Bois');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.582333 -12.719278)', 4674)), false, 'Riacho dos Poços');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.069444 -11.420833)', 4674)), false, 'Riacho Jacarezinho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-37.814725 -10.056717)', 4674)), false, 'Riacho Lagoa Grande');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.460664 -13.436556)', 4674)), false, 'Riacho Lagoinha (P)');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.626861 -10.890472)', 4674)), false, 'Riacho Tamandua');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.647778 -10.702778)', 4674)), false, 'Ribeira do Pombal');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.209444 -13.064722)', 4674)), false, 'Rio da Caixa');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.282222 -13.059722)', 4674)), false, 'Rio da Dona');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.308889 -14.707500)', 4674)), false, 'Rio de Contas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.095833 -14.414167)', 4674)), false, 'Rio do Antônio ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.965833 -11.230472)', 4674)), false, 'Rio do Peixe');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.170325 -9.455428)', 4674)), false, 'Rio do Sal');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.454444 -12.832222)', 4674)), false, 'Rio dos Macacos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.756389 -11.010806)', 4674)), false, 'Rio Lages do Batata');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-45.910556 -11.846944)', 4674)), false, 'Rio Limpo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.514550 -12.826192)', 4674)), false, 'Rio Ratinho');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.371544 -12.099225)', 4674)), false, 'Rio Tijuco');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.508700 -9.840256)', 4674)), false, 'Rodeador');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.726389 -10.653889)', 4674)), false, 'Rômulo Campos ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.485052 -13.399001)', 4674)), false, 'Roncador');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.511225 -13.139825)', 4674)), false, 'Roncador I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.498500 -13.135692)', 4674)), false, 'Roncador II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.644015 -14.837222)', 4674)), false, 'Salomé');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.388611 -13.046667)', 4674)), false, 'Santa Apolonia');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.627367 -12.845100)', 4674)), false, 'Santa Cruz I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.622983 -12.834253)', 4674)), false, 'Santa Cruz II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.625353 -12.814669)', 4674)), false, 'Santa Cruz III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.159978 -12.562314)', 4674)), false, 'Santa Helena');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.128333 -14.038056)', 4674)), false, 'Santa Maria');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.413056 -13.011944)', 4674)), false, 'Santa Terezinha');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.090389 -11.812222)', 4674)), false, 'São Bento I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.100667 -11.814917)', 4674)), false, 'São Bento II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-44.972778 -12.361111)', 4674)), false, 'São Desidério');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.522139 -11.466639)', 4674)), false, 'São Domingos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.045417 -11.525778)', 4674)), false, 'São José do Jacuípe');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.568847 -12.913886)', 4674)), false, 'São Lucas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.567470 -12.680666)', 4674)), false, 'São Paulo');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.536679 -13.042642)', 4674)), false, 'São Pedro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.834167 -14.467500)', 4674)), false, 'Sapé');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.425278 -10.898056)', 4674)), false, 'Saúde');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.141111 -14.041944)', 4674)), false, 'Sede');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.475644 -14.844168)', 4674)), false, 'Serra Preta');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.307222 -11.408333)', 4674)), false, 'Serrote ');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.032839 -13.757633)', 4674)), false, 'Sincorá');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.943033 -11.840969)', 4674)), false, 'Sítio das Flores');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.954844 -10.794128)', 4674)), false, 'Sítio dos Moços');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.096528 -10.368806)', 4674)), false, 'Sohen');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.380116 -14.335143)', 4674)), false, 'Tábua II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-43.166111 -14.539444)', 4674)), false, 'Tabuas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.998333 -14.690556)', 4674)), false, 'Tabuleiro de Dentro');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.124444 -13.938056)', 4674)), false, 'Tamboril I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.102361 -11.213389)', 4674)), false, 'Tamboril II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.276389 -14.872778)', 4674)), false, 'Tancão do Caititu');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.305278 -14.093333)', 4674)), false, 'Tanque');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.193333 -14.110000)', 4674)), false, 'Tapera - CAR');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.367778 -11.275278)', 4674)), false, 'Tapera - DNOCS');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-38.822191 -12.986894)', 4674)), false, 'Tapera - EMBASA');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.663611 -10.934167)', 4674)), false, 'Taquarendi');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.398056 -14.102500)', 4674)), false, 'Tocadas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.383717 -13.173221)', 4674)), false, 'Tremedal');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.562722 -12.896267)', 4674)), false, 'Três Morros I');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.564267 -12.900678)', 4674)), false, 'Três Morros II');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.565317 -12.904228)', 4674)), false, 'Três Morros III');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.320917 -14.546833)', 4674)), false, 'Truvisco');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.633611 -10.850278)', 4674)), false, 'Umburana');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.471111 -11.419167)', 4674)), false, 'Valente');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-41.970556 -13.888056)', 4674)), false, 'Varzea D''água');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.241917 -11.052528)', 4674)), false, 'Varzea do Mato');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.298889 -13.031667)', 4674)), false, 'Varzea dos Porcos');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.193056 -13.570833)', 4674)), false, 'Varzea Redonda');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.651594 -11.745544)', 4674)), false, 'Veredas');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.514722 -12.300833)', 4674)), false, 'Vilobaldo Alencar');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-39.471389 -11.492778)', 4674)), false, 'Vista Bela');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-40.818278 -11.815889)', 4674)), false, 'Vitor Carroso');
INSERT INTO barragem(the_geom, ind_origem_usuario, nom_barragem) VALUES ((SELECT ST_GeomFromText('POINT(-42.214722 -13.438889)', 4674)), false, 'Zabumbão');
INSERT INTO barragem(nom_barragem, ind_origem_usuario) VALUES ('Outros', FALSE);


INSERT INTO cerh_outros_usos(dsc_outros_usos) 
	VALUES 
	('Eclusa'),
	('Poço de injeção para recuperação ambiental'),
	('Contenção de sedimentos'),
	('Recreação '),
	('Umectação de vias'), 
	('Depuração de efluentes (exceto proveniente de consumo humano)'),
	('Salvamento'), 
	('Sistema de resfriamento'), 
	('Disposição de rejeitos'),
	('Poço de bombeamento de recuperação ambiental'),
	('Lavagem de veículos'), 
	('Pesquisa mineral'),
	('Poço de monitoramento/Piezômetro'), 
	('Clarificação da água'), 
	('Pesquisa hidrogeológica/teste de bombeamento'),
	('Recirculação de água'), 
	('Urbanização'), 
	('Rebaixamento de aquífero'),
	('Balneário – lazer e clube'), 
	('Lavanderia'), 
	('Combate a incêndio'),
	('Hortas, pomares, jardins (área < 1 ha)'), 
	('Teste hidrostático'), 
	('Abastecimento de pulverizadores'),
	('Construção civil'), 
	('Outros');

	
INSERT INTO cerh_finalidade_transposicao( dsc_finalidade_transposicao) 
	VALUES ('Abastecimento Público'), ('Agropecuário'), ('Industrial'), ('Irrigação'), ('Aquicultura'), ('Outros');
    
INSERT INTO gestor_financeiro (ide_gestor_financeiro, nom_gestor_financeiro) VALUES (1, 'INEMA');

INSERT INTO mes(ide_mes, cod_mes, nom_mes)
	VALUES  (1, 'JAN', 'Janeiro'), (2, 'FEV', 'Fevereiro'), (3, 'MAR', 'Março'), (4, 'ABR', 'Abril'), 
	(5, 'MAI', 'Maio'), (6, 'JUN', 'Junho'), (7, 'JUL', 'Julho'), (8, 'AGO', 'Agosto'), 
	(9, 'SET', 'Setembro'), (10, 'OUT', 'Outubro'), (11, 'NOV', 'Novembro'), (12, 'DEZ', 'Dezembro');
	

INSERT INTO tipologia_grupo(
            ide_tipologia, dtc_criacao, ind_excluido) VALUES (302, now(), false);
            
INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('m³', 'Metros Cúbicos');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('kg', 'Quilogramas');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('t', 'Tonelada');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('l', 'Litros');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))); 

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('m³/m', 'Metros cúbicos por mês');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('MWt', 'Megawatts');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('un', 'Unidade');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('un', 'Milhares de unidades');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('m', 'Metro');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('CX', 'Caixa');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('KM/m', 'Kilometros por mês');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('m²', 'Metros quadrados');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('ml', 'Mililitros');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('un', 'Dezenas de milhares de unidade');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('sc', 'Sacas');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO unidade_medida(cod_unidade_medida, nom_unidadade_medida)
    VALUES ('l', 'Milhares de litros');

INSERT INTO unidade_medida_tipologia_grupo(
            ide_unidade_medida, ide_tipologia_grupo)
    VALUES ((select currval ('UNIDADE_MEDIDA_IDE_UNIDADE_MEDIDA_seq')), (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));

INSERT INTO tipo_corpo_hidrico(nom_tipo_corpo_hidrico)
    VALUES ('Lagoa'), ('Nascente'), ('Rio'), ('Lago'), ('Riacho');
    
INSERT INTO tipo_finalidade_uso_agua(
            nom_tipo_finalidade_uso_agua, ind_ativo, 
            ind_requerimento, ind_cerh)
    VALUES ('Abastecimento público', true, false, true)
	,('Infraestrutura', true, false, true)
	,('Mineração - Extração de Areia/Cascalho em leito de rio', true, false, true)
	,('Termoelétrica', true, false, true)
	,('Transposição', true, false, true)
	,('Outros', true, false, true)
	,('Aquicultura em tanque escavado', true, false, true)
	,('Industrial', True, false, true)
	,('Esgotamento sanitário (uso doméstico)', true, false, true)
	,('Esgotamento sanitário (Abastecimento Público)', true, false, true)
	,('Criação Animal', true, false, true)
	,('Outra', true, false, true)
    ;

UPDATE tipo_finalidade_uso_agua  SET ind_cerh = true WHERE ide_tipo_finalidade_uso_agua IN (2,3,5,8,9,11,13,25);

INSERT INTO ato_ambiental_tipologia_finalidade(
            ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia, 
            ide_tipo_finalidade_uso_agua, ind_ativo)
    VALUES 
    (35, 1, 28, true)
    ,(36, 13, 28, true)
    ,(37, 16, 28, true)
    ,(38, 24, 28, true)
    ,(39, 1, 29, true)
    ,(40, 13, 29, true)
    ,(41, 16, 29, true)
    ,(42, 24, 29, true)
    ,(43, 1, 30, true)
    ,(44, 13, 30, true)
    ,(45, 16, 30, true)
    ,(46, 24, 30, true)
    ,(47, 1, 31, true)
    ,(48, 13, 31, true)
    ,(49, 16, 31, true)
    ,(50, 24, 31, true)
    ,(51, 1, 32, true)
    ,(52, 13, 32, true)
    ,(53, 16, 32, true)
    ,(54, 24, 32, true)
    ,(55, 1, 33, true)
    ,(56, 13, 33, true)
    ,(57, 16, 33, true)
    ,(58, 24, 33, true)
    ,(59, 3,34 , true),
	(60, 3, 35, true),
	(61, 3, 36, true),
	(62, 3, 37, true), 
	(63, 3, 38, true), 
	(64, 3, 39, true), 
	(65, 3, 31, true), 
	(67, 3, 30, true);
   
	
INSERT INTO cerh_natureza_poco(ide_cerh_natureza_poco, dsc_natureza_poco) VALUES (1, 'Poço escavado'), (2, 'Poço tubular');

INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 2, 28, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 2, 29, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 2, 33, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 2, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 3, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 5, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 8, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 9, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 11, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 13, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 25, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 28, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 29, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, 33, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 2, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 3, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 5, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 8, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 9, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 11, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 13, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 25, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 28, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 29, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, 33, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 2, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 3, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 5, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 8, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 11, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 13, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 25, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 28, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 29, TRUE);
INSERT INTO ato_ambiental_tipologia_finalidade(ide_ato_ambiental_tipologia_finalidade, ide_ato_ambiental_tipologia,ide_tipo_finalidade_uso_agua, ind_ativo) VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, 33, TRUE);

INSERT INTO tipologia_grupo(
    ide_tipologia, dtc_criacao, ind_excluido) VALUES (303, now(), false);

INSERT INTO unidade_medida_tipologia_grupo(ide_unidade_medida, ide_tipologia_grupo) VALUES 
(70, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(71, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(72, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(73, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(74, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(75, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(76, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(77, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(78, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(79, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(80, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(81, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(82, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(83, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(84, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq'))),
(85, (SELECT currval ('TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq')));    


INSERT INTO cerh_tipo_status(ide_cerh_tipo_status, dsc_tipo_status)
    VALUES (1, 'Cadastro Incompleto'), (2, 'Cadastro Completo'), (3, 'Cancelado');
    

INSERT INTO tipo_finalidade_uso_agua(
        nom_tipo_finalidade_uso_agua, ind_ativo, 
        ind_requerimento, ind_cerh)
    VALUES ('Comércio e Serviços', true, false, true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 1, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 2, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 13, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 14, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 16, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 17, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 24, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
		ide_ato_ambiental_tipologia_finalidade, 
		ide_ato_ambiental_tipologia,
		ide_tipo_finalidade_uso_agua, 
		ind_ativo) 
	VALUES ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 25, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO tipo_certificado(ide_tipo_certificado, dsc_tipo_certificado)
	VALUES (7, 'CERH');
	
INSERT INTO cerh_intervencao_servico(dsc_intervencao_servico)
    VALUES ('Desassoreamento'),
    ('Limpeza e Conservação de margens'),
    ('Extração Mineral (sem uso de água para composição de polpa)'),
    ('Outros fins que não alteram o regime de vazões');

INSERT INTO cerh_obras_hidraulicas(dsc_obras_hidraulicas)
    VALUES ('Travessia (ponte, passagem molhada, bueiro, dutos)'),
    ('Soleira de Nível'),
    ('Diques'),
    ('Retificação/Canalização'),
    ('Desvio'),
    ('Conteção de Taludes'),
    ('Derrocamento'),
    ('Outros');
    
INSERT INTO tipo_finalidade_uso_agua(
        nom_tipo_finalidade_uso_agua, ind_ativo, 
        ind_requerimento, ind_cerh)
    VALUES ('Aproveitamento Hidrelétrico', true, false, true), 
    ('Reservatório/Barramento/Regularização de Vazões para Usos Múltiplos', true, false, true);

INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 4, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 18, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);
	
INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 27, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO tipo_finalidade_uso_agua(
        nom_tipo_finalidade_uso_agua, ind_ativo, 
        ind_requerimento, ind_cerh)
    VALUES ('Reservatório/Barramento/Regularização de Vazões para Usos Múltiplos', true, false, true);

INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 4, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);

INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 18, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);
	
INSERT INTO ato_ambiental_tipologia_finalidade(
	ide_ato_ambiental_tipologia_finalidade, 
	ide_ato_ambiental_tipologia,
	ide_tipo_finalidade_uso_agua, 
	ind_ativo) 
	VALUES  ((select MAX (ide_ato_ambiental_tipologia_finalidade) + 1 from ato_ambiental_tipologia_finalidade), 27, (SELECT currval ('TIPO_FINALIDADE_USO_AGUA_IDE_TIPO_FINALIDADE_USO_AGUA_seq')), true);    
    
INSERT INTO tipo_aproveitamento_hidreletrico(
            dsc_tipo_aproveitamento_hidreletrico, 
            ind_ativo)
    VALUES ('Fio D''água', true), ('Regularização diária', true), ('Regularização mensal/anual', true);
