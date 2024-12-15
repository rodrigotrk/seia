INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Comprovação da concessão de autorização de supressão de vegetação nativa, quando couber, ou declaração de ocupação antrópica preexistente a 22 de julho de 2008, com edificações, benfeitorias ou atividades agrossilvipastoris'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);


INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Comprovação da concessão de outorga de direito de uso de recursos hídricos ou declaração de dispensa, quando couber'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);

INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Certidão Municipal de regular uso e ocupação do solo'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);

INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Anuência do órgão gestor da UC'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);

INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Termo de compromisso, certificado do CEFIR ou recibo do CAR'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);

INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Formulário de Caracterização do Empreendimento - Agrossilvipastoril'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);

INSERT INTO documento_ato(
            ide_documento_obrigatorio, 
            ide_ato_ambiental, 
            ide_tipologia, 
            ide_tipo_finalidade_uso_agua, 
            ind_ativo)
    VALUES ((SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio = 'Declaração da atividade antrópica'),
	    (SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental = 'APE'),
	     NULL,
	     NULL,
	     TRUE);
