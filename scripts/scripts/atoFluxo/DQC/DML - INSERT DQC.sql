----------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- INSERT DOCUMENTO OBRIGATORIO -------------------------------------------------------------
INSERT INTO documento_obrigatorio(nom_documento_obrigatorio, num_tamanho, ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, ind_ativo) 
VALUES ('Formulário de Declaração de Queima Controlada', 0.000, TRUE, TRUE, 1, TRUE);

----------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- INSERT DOCUMENTO ATO ---------------------------------------------------------------------
INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental, ind_ativo) VALUES (
	(SELECT ide_documento_obrigatorio FROM documento_obrigatorio WHERE nom_documento_obrigatorio LIKE 'Formulário de Declaração de Queima Controlada'), 
	(SELECT ide_ato_ambiental FROM ato_ambiental WHERE sgl_ato_ambiental LIKE 'DQC'), 
	TRUE
);

----------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- INSERT ELEMENTO INTERVENÇÃO --------------------------------------------------------------
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Rede de transmissão ou distribuição de energia elétrica');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Subestação de energia elétrica');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Área de Preservação Permanente');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Reserva Legal');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Outras áreas com vegetação nativa');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Estradas');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Residências');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Equipamentos Urbanos');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Aeródromo');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Aeroporto');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Rodovias');
INSERT INTO elemento_intervencao_queima_controlada VALUES (nextval('elemento_intervencao_qc_ide_elemento_intervencao_qc_seq'::regclass), now(), null, false, 'Outro');

----------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- INSERT OBJETIVO QUEIMA -------------------------------------------------------------------
INSERT INTO objetivo_queima_controlada VALUES (nextval('objetivo_qc_ide_objetivo_queima_controlada_seq'::regclass), 'Destinação de resíduos de ASV', now(), null, false, FALSE);
INSERT INTO objetivo_queima_controlada VALUES (nextval('objetivo_qc_ide_objetivo_queima_controlada_seq'::regclass), 'Práticas Agrossilvopastoris', now(), null, false, TRUE);
INSERT INTO objetivo_queima_controlada VALUES (nextval('objetivo_qc_ide_objetivo_queima_controlada_seq'::regclass), 'Outro', now(), null, false, FALSE);

----------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------- INSERT TÉCNICA QUEIMA --------------------------------------------------------------------
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Em faixa a favor do vento', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Contra o vento', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima por pontos', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Circular simples', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Circular em concentração de calor', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Centrífuga ou pelos francos', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima em manchas', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima central (ou em anel)', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima em V (ou Chevron)', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima em L', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima por empilhamento', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima frontal', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Queima em retrocesso', now(), null, false);
INSERT INTO tecnica_queima_controlada VALUES (nextval('tecnica_qc_ide_tecnica_queima_controlada_seq'::regclass), 'Outro', now(), null, false);

