INSERT INTO funcionalidade(ide_secao, dsc_funcionalidade, ind_excluido, dtc_criacao)
    VALUES (2, 'Atividades não sujeitas a Licenciamento Ambiental', false, now());

INSERT INTO funcionalidade_url (ide_funcionalidade, dsc_url, ind_principal) 
VALUES ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),'/paginas/manter-atividade-nao-sujeita-licenciamento/consulta.xhtml', false),
((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),'/paginas/manter-atividade-nao-sujeita-licenciamento/pesquisa-mineral/pesquisaMineralSemGuia.xhtml', false),
((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), '/paginas/manter-atividade-nao-sujeita-licenciamento/identificarAtividade.xhtml', false);
    
INSERT INTO funcionalidade_acao(ide_funcionalidade, ide_acao)
    VALUES ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 1),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 2),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 3),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 4),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 6),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 13),
    ((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')), 14);
    
INSERT INTO rel_grupo_perfil_funcionalidade(ide_funcionalidade, ide_acao, ide_perfil) VALUES 
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),1,2),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),2,2),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),3,2),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),4,2),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),6,2),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),14,2),
	
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),4,5),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),6,5),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),13,5),
	((SELECT currval('Funcionalidade_IDE_FUNCIONALIDADE_seq')),14,5)
	;
	
INSERT INTO tipo_certificado  (dsc_tipo_certificado ) VALUES ('CPM');

INSERT INTO cadastro_atividade_nao_sujeita_lic_tipo_status(nom_tipo_status)
	VALUES ('Cadastro incompleto'), 
	('Cadastro completo'), 
	('Aguardando validação'), 
	('Sendo validado'), 
	('Pendência de validação'), 
	('Concluído');

INSERT INTO formacao_profissional (nom_formacao_profissional) 
	VALUES ('Engenheiro de Minas'), 
	('Geólogo'), 
	('Técnico em mineração ou geologia'), 
	('Engenheiro geólogo'), 
	('Outros da área de mineração');

INSERT INTO tipo_atividade_nao_sujeita_licenciamento(nom_atividade, ind_ativo)
    VALUES ('Silos e armazéns destinados ao armazenamento, secagem e beneficiamento de produtos agrícolas não industrializados', false), 
    ('Pesquisa mineral, sem guia de utilização, envolvendo sondagens e trincheiras', true),
    ('Perfuração de poços terrestres de petróleo e gás, sondagens e poços estratigráficos em campos já licenciados', false),
    ('Microgeradores Eólicos', false),
    ('Instalação de torres anemométricas', false);
    
INSERT INTO pesquisa_mineral_documento_captacao(nom_documento) VALUES ('Portaria de outorga'), ('Declaração de dispensa de outorga de direito de uso da água');

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            dsc_caminho_arquivo, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)
    VALUES ('Relatório de Caracterização da Atividade e do Meio Ambiente acompanhada da respectiva ART',
	    true, 
	    '/opt/ARQUIVOS/DOCUMENTOOBRIGATORIO/RCE_Pesquisa_Mineral.doc', 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
    VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);

 INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)         
            VALUES ('Documentos que atestem a manifestação do(s) município(s) quanto a conformidade da atividade com a legislação aplicável ao uso e ocupação do solo conforme Anexo I da Portaria INEMA nº 11.292/2016',
	    false, 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
    ide_documento_obrigatorio, 
    ide_tipo_atividade_nao_sujeita_licenciamento)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)
	    VALUES ('Autorização do proprietário superficiário e documento comprobatório de propriedade ou posse do imóvel',
	    false, 
	    false, 
	    1,
	    true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
    VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)
            VALUES ('Mapa de localização georreferenciado padrão ABNT escala adequada à perfeita visualização e entendimento da atividade contendo os seguintes elementos: a) Localização da área da pesquisa em relação a propriedade indicando as vias de acesso principais todas devidamente denominadas. Caso o terreno em questão se situe em estrada ou rodovia ou a ela referenciada indicar o nome/sigla a direção e o quilômetro. Colocar sempre que possível os confrontantes: à direita à esquerda fundos e frente com as respectivas numerações: b) Poligonal do processo no DNPM c) Cotas topográficas d) Malhas de sondagem e/ou escavações e) APP delimitada',
	    false, 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
    VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)          
            VALUES ('Fotos georreferenciadas nítidas e representativas dos alvos e locais de intervenções com a data das tomadas',
	    false, 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
    VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);
     
INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)       
            VALUES ('Imagem de satélite delimitando a área objeto da pesquisa propriedades rurais e poligonal do DNPM',
	    false, 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
    VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, 
            ind_formulario, 
            ind_publico, 
            ide_tipo_documento_obrigatorio, 
            ind_ativo, num_tamanho)
            VALUES ('Cópia do Plano de Pesquisa encaminhado ao DNPM acompanhada da respectiva ART',
	    false, 
            false, 
            1,
            true, 0.000);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
    ide_documento_obrigatorio, 
    ide_tipo_atividade_nao_sujeita_licenciamento)
VALUES ((SELECT currval ('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq')), 2);
