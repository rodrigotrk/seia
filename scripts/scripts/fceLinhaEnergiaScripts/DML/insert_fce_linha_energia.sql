BEGIN; 
INSERT INTO public.tipo_subestacao (dsc_tipo_subestacao, ind_ativo) VALUES ('Coletora', true);
INSERT INTO public.tipo_subestacao (dsc_tipo_subestacao, ind_ativo) VALUES ('Supridora', true);


INSERT INTO public.tipo_energia (dsc_tipo_energia, ind_ativo) VALUES ('Hídrica', true);
INSERT INTO public.tipo_energia (dsc_tipo_energia, ind_ativo) VALUES ('Eólica', true);
INSERT INTO public.tipo_energia (dsc_tipo_energia, ind_ativo) VALUES ('Termoelétrica', true);
INSERT INTO public.tipo_energia (dsc_tipo_energia, ind_ativo) VALUES ('Solar', true);

insert into documento_obrigatorio (nom_documento_obrigatorio,num_tamanho,ind_formulario,ind_publico,
ide_tipo_documento_obrigatorio, ind_ativo) 
values('Formulário De Caracterização De Empreendimento - Linha de Transmissão e Distribuição de Energia',0.000,true, true, 1, true);


insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,3, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,1, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,2, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,6, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,124, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,48, 222,true);

insert into documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ide_tipologia,ind_ativo) values
(10073,7, 222,true);


insert into tipo_area_concedida (ide_tipo_area_concedida, des_tipo_area_concedida) values (4,'Objeto de Licença - Linha de Transmissão e Distribuição');


insert into motivo_notificacao (ide_motivo_notificacao, nom_motivo_notificacao, ind_notificacao_prazo, ind_notificacao_comunicacao, ind_notificacao_homologacao, ind_envio_shape) 
values (21, 'Envio de shape para Linha de transmissão e distribuição', true, false, false, true);
COMMIT;

--Ordenar com o tipo Outros sempr no final #10029
ALTER TABLE destino_residuo ADD COLUMN dt_cadastro timestamp(6) without time zone NOT NULL DEFAULT now();
UPDATE destino_residuo SET dt_cadastro  = now() WHERE ide_destino_residuo = 5
