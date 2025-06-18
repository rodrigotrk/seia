
--INSERIR DOCUMENTO ATO
INSERT INTO documento_obrigatorio(ide_documento_obrigatorio, nom_documento_obrigatorio, num_tamanho, ind_formulario, dsc_caminho_arquivo, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo) VALUES 
(3002, 'Formulário de Caracterização do Empreendimento - Intervenção em Recursos Hídricos para Fins de Mineração', 0, TRUE, NULL, FALSE, 1,TRUE);

INSERT INTO documento_ato(ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia, ind_ativo)
select 3002 as ide_documento_obrigatorio, a.ide_ato_ambiental, 31, TRUE from ato_ambiental a  
where  a.sgl_ato_ambiental  in ('AA', 'DOUT', 'LA', 'LI', 'LO', 'LP', 'OUT') OR a.ide_ato_ambiental = 35;