CREATE SEQUENCE tipo_corpo_hidrico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE TABLE tipo_corpo_hidrico
(
	ide_tipo_corpo_hidrico         INTEGER NOT NULL DEFAULT nextval(('tipo_corpo_hidrico_seq'::text)::regclass),
	nom_tipo_corpo_hidrico         VARCHAR(200) NOT NULL
);
ALTER TABLE tipo_corpo_hidrico ADD PRIMARY KEY (ide_tipo_corpo_hidrico);
-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE barragem_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE TABLE barragem
(
	ide_barragem         INTEGER NOT NULL DEFAULT nextval(('barragem_seq'::text)::regclass),
	nom_barragem         VARCHAR(200) NOT NULL,
	the_geom geometry,
	ind_origem_usuario boolean not null
);
ALTER TABLE barragem
ADD PRIMARY KEY (ide_barragem);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh
(
	ide_cerh             INTEGER NOT NULL DEFAULT nextval(('cerh_seq'::text)::regclass),
	ide_pessoa_requerente INTEGER NOT NULL,
	ide_pessoa_fisica_cadastro INTEGER NOT NULL,
	dtc_cadastro         TIMESTAMP NOT NULL,
	num_cadastro         VARCHAR(50) NULL,
	ide_empreendimento   INTEGER NOT NULL,
	ind_excluido         BOOLEAN NULL,
	dtc_exclusao         TIMESTAMP NULL,
	ide_contrato_convenio INTEGER NULL,
	ide_certificado INTEGER NULL
);
ALTER TABLE cerh
ADD PRIMARY KEY (ide_cerh);

ALTER TABLE cerh
  ADD CONSTRAINT cerh_ide_certificado FOREIGN KEY (ide_certificado) REFERENCES certificado (ide_certificado) ON UPDATE NO ACTION ON DELETE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_barragem_aproveitamento_hidreletrico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_barragem_aproveitamento_hidreletrico
(
	ide_cerh_barragem_aproveitamento_hidreletrico INTEGER NOT NULL DEFAULT nextval(('cerh_barragem_aproveitamento_hidreletrico_seq'::text)::regclass),
	val_potencia_instalada_total NUMERIC(10,2) NULL,
	ind_desvio_trecho    BOOLEAN NULL,
	val_extensao         NUMERIC(10,2) NULL,
	ind_pch              BOOLEAN NULL,
	ind_em_operacao      BOOLEAN NULL,
	ind_producao_anual_intervencao BOOLEAN NULL,
	val_producao_anual_efetivamente_verificada NUMERIC(10,2) NULL,
	val_trecho_vazao_reduzida NUMERIC(10,2) NULL,
	ide_cerh_barragem_caracterizacao_finalidade INTEGER NULL,
	ide_tipo_aproveitamento_hidreletrico INTEGER NULL,
	ind_potencia_instalada_intervencao BOOLEAN NULL,
	dt_inicio_operacao DATE NULL
);
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
ADD PRIMARY KEY (ide_cerh_barragem_aproveitamento_hidreletrico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_barragem_caracterizacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_barragem_caracterizacao
(
	ide_cerh_barragem_caracterizacao INTEGER NOT NULL DEFAULT nextval(('cerh_barragem_caracterizacao_seq'::text)::regclass),
	nom_corpo_hidrico    VARCHAR(40) NULL,
	dsc_observacao    VARCHAR(200) NULL,
	val_vazao_liberada_jusante NUMERIC(10,2) NULL,
	val_altura_maxima_barragem NUMERIC(10,2) NULL,
	val_volume_maximo_reservatorio NUMERIC(10,2) NULL,
	val_vazao_regularizada NUMERIC(10,2) NULL,
	ide_cerh_localizacao_geografica INTEGER NULL,
	ide_tipo_barragem    INTEGER NULL,
	ide_cerh_situacao_tipo_uso INTEGER NULL,
	ide_barragem         INTEGER NULL,
	ide_tipo_corpo_hidrico INTEGER NULL
);
ALTER TABLE cerh_barragem_caracterizacao
ADD PRIMARY KEY (ide_cerh_barragem_caracterizacao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_barragem_caracterizacao_finalidade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_barragem_caracterizacao_finalidade
(
	ide_cerh_barragem_caracterizacao_finalidade INTEGER NOT NULL DEFAULT nextval(('cerh_barragem_caracterizacao_finalidade_seq'::text)::regclass),
	ide_cerh_barragem_caracterizacao INTEGER NOT NULL,
	ide_tipo_finalidade_uso_agua INTEGER NOT NULL
);
ALTER TABLE cerh_barragem_caracterizacao_finalidade
ADD PRIMARY KEY (ide_cerh_barragem_caracterizacao_finalidade);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_cadastro_cancelado_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_cadastro_cancelado
(
	ide_cerh_cadastro_cancelado INTEGER NOT NULL DEFAULT nextval(('cerh_cadastro_cancelado_seq'::text)::regclass),
	dt_cancelamento      DATE NOT NULL,
	dsc_observacao       VARCHAR(1000) NOT NULL,
	url_documento        VARCHAR(500) NULL,
	ide_pessoa_fisica_cancelamento INTEGER NOT NULL,
	dtc_pessoa_fisica_cancelamento DATE NOT NULL,
	ide_cerh             INTEGER NOT NULL
);
ALTER TABLE cerh_cadastro_cancelado
ADD PRIMARY KEY (ide_cerh_cadastro_cancelado);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_abastecimento_industrial_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_abastecimento_industrial
(
	ide_cerh_captacao_abastecimento_industrial INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_abastecimento_industrial_seq'::text)::regclass),
	nom_produto          VARCHAR(40) NOT NULL,
	val_consumo_agua     NUMERIC(10,2) NOT NULL,
	val_producao_diaria  NUMERIC(10,2) NOT NULL,
	val_producao_anual   NUMERIC(10,2) NOT NULL,
	ide_unidade_medida   INTEGER,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NOT NULL
);
ALTER TABLE cerh_captacao_abastecimento_industrial
ADD PRIMARY KEY (ide_cerh_captacao_abastecimento_industrial);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_abastecimento_publico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_abastecimento_publico
(
	ide_cerh_captacao_abastecimento_publico INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_abastecimento_publico_seq'::text)::regclass),
	ind_perda_distribuicao BOOLEAN NULL,
	val_indice_perda_distribuicao INTEGER NULL,
	ide_cerh_tipo_prestador_servico INTEGER NULL,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NULL,
	ind_incerto_desconhecido boolean
);
ALTER TABLE cerh_captacao_abastecimento_publico
ADD PRIMARY KEY (ide_cerh_captacao_abastecimento_publico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_caracterizacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_caracterizacao
(
	ide_cerh_captacao_caracterizacao INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_caracterizacao_seq'::text)::regclass),
	nom_corpo_hidrico    VARCHAR(40) NULL,
	dsc_observacao    VARCHAR(1000) NULL,
	dt_inicio_captacao   DATE NULL,
	ind_captacao_reservatorio BOOLEAN NULL,
	val_profundidade     NUMERIC(10,2) NULL,
	val_vazao_teste      NUMERIC(10,2) NULL,
	val_nivel_estatico   NUMERIC(10,2) NULL,
	val_nivel_dinamico   NUMERIC(10,2) NULL,
	val_vazao_maxima_instantanea NUMERIC(10,2) NULL,
	ide_barragem         INTEGER NULL,
	ide_cerh_situacao_tipo_uso INTEGER NULL,
	ide_cerh_localizacao_geografica INTEGER NULL,
	ide_cerh_natureza_poco INTEGER NULL,
	ide_tipo_corpo_hidrico INTEGER NULL
);
ALTER TABLE cerh_captacao_caracterizacao
ADD PRIMARY KEY (ide_cerh_captacao_caracterizacao);

ALTER TABLE cerh_captacao_caracterizacao ADD CONSTRAINT cerh_captacao_caracterizacao_tipo_corpo_hidrico FOREIGN KEY (ide_tipo_corpo_hidrico) 
REFERENCES tipo_corpo_hidrico (ide_tipo_corpo_hidrico) ON UPDATE NO ACTION ON DELETE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_caracterizacao_finalidade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_caracterizacao_finalidade
(
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_caracterizacao_finalidade_seq'::text)::regclass),
	ide_cerh_captacao_caracterizacao INTEGER NOT NULL,
	ide_tipo_finalidade_uso_agua  INTEGER NOT NULL
);
ALTER TABLE cerh_captacao_caracterizacao_finalidade
ADD PRIMARY KEY (ide_cerh_captacao_caracterizacao_finalidade);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_dados_irrigacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_dados_irrigacao
(
	ide_cerh_captacao_dados_irrigacao INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_dados_irrigacao_seq'::text)::regclass),
	ide_metodo_irrigacao INTEGER NOT NULL,
	val_area_irrigada    NUMERIC(10,2) NOT NULL,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NOT NULL,
	ide_tipologia_atividade integer NOT NULL
);
ALTER TABLE cerh_captacao_dados_irrigacao
ADD PRIMARY KEY (ide_cerh_captacao_dados_irrigacao);

ALTER TABLE cerh_captacao_dados_irrigacao 
ADD CONSTRAINT fk_ide_tipologia_atividade FOREIGN KEY (ide_tipologia_atividade) 

REFERENCES tipologia_atividade (ide_tipologia_atividade) 
ON UPDATE NO ACTION 
ON DELETE NO ACTION;
-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_dados_mineracao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_dados_mineracao
(
	ide_cerh_captacao_dados_mineracao INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_dados_mineracao_seq'::text)::regclass),
	nom_produto          VARCHAR(40) NOT NULL,
	val_consumo_agua     NUMERIC(10,2) NOT NULL,
	val_producao_maxima_mensal   NUMERIC(10,2) NOT NULL,
	val_producao_maxima_anual  NUMERIC(10,2) NOT NULL,
	ide_unidade_medida   INTEGER,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NOT NULL
);
ALTER TABLE cerh_captacao_dados_mineracao
ADD PRIMARY KEY (ide_cerh_captacao_dados_mineracao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_mineracao_extracao_areia_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_mineracao_extracao_areia
(
	ide_cerh_captacao_mineracao_extracao_areia INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_mineracao_extracao_areia_seq'::text)::regclass),
	val_producao_maxima_mensal NUMERIC(10,2) NULL,
	val_proporcao_agua_polpa NUMERIC(2,1) NULL,
	val_volume_medio_agua NUMERIC(10,2) NULL,
	val_teor_umidade    INTEGER NULL,
	val_producao_maxima_anual NUMERIC(10,2) NULL,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NULL
);
ALTER TABLE cerh_captacao_mineracao_extracao_areia
ADD PRIMARY KEY (ide_cerh_captacao_mineracao_extracao_areia);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_outros_usos_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_outros_usos
(
	ide_cerh_captacao_outros_usos INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_outros_usos_seq'::text)::regclass),
	ide_cerh_outros_usos INTEGER NULL,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NULL
);
ALTER TABLE cerh_captacao_outros_usos
ADD PRIMARY KEY (ide_cerh_captacao_outros_usos);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_termoeletrica_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_termoeletrica
(
	ide_cerh_captacao_termoeletrica INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_termoeletrica_seq'::text)::regclass),
	nom_combustivel_principal VARCHAR(40) NULL,
	val_potencia_instalada NUMERIC(10,2) NULL,
	val_producao_mensal_energia NUMERIC(10,2) NULL,
	val_estimativa_consumo_agua NUMERIC(10,2) NULL,
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NULL
);
ALTER TABLE cerh_captacao_termoeletrica
ADD PRIMARY KEY (ide_cerh_captacao_termoeletrica);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_transposicao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_transposicao
(
	ide_cerh_captacao_transposicao INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_transposicao_seq'::text)::regclass),
	ide_cerh_captacao_caracterizacao_finalidade INTEGER NULL,
	ide_cerh_finalidade_transposicao INTEGER NULL
);
ALTER TABLE cerh_captacao_transposicao
ADD PRIMARY KEY (ide_cerh_captacao_transposicao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_captacao_vazao_sazonalidade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_captacao_vazao_sazonalidade
(
	ide_cerh_captacao_vazao_sazonalidade INTEGER NOT NULL DEFAULT nextval(('cerh_captacao_vazao_sazonalidade_seq'::text)::regclass),
	val_vazao_captacao            NUMERIC(10,2) NULL,
	val_tempo_captacao   INTEGER NULL,
	val_dia_mes          INTEGER NULL,
	ide_mes         INTEGER NULL,
	ide_cerh_captacao_caracterizacao INTEGER NULL
);
ALTER TABLE cerh_captacao_vazao_sazonalidade
ADD PRIMARY KEY (ide_cerh_captacao_vazao_sazonalidade);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_finalidade_transposicao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_finalidade_transposicao
(
	ide_cerh_finalidade_transposicao INTEGER NOT NULL DEFAULT nextval(('cerh_finalidade_transposicao_seq'::text)::regclass),
	dsc_finalidade_transposicao VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_finalidade_transposicao
ADD PRIMARY KEY (ide_cerh_finalidade_transposicao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_intervencao_caracterizacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_intervencao_caracterizacao
(
	ide_cerh_intervencao_caracterizacao INTEGER NOT NULL DEFAULT nextval(('cerh_intervencao_caracterizacao_seq'::text)::regclass),
	nom_corpo_hidrico    VARCHAR(40) NULL,
	dsc_observacao    VARCHAR(200) NULL,
	ind_pch              BOOLEAN NULL,
	ind_potencia_instalada_barragem BOOLEAN NULL,
	val_potencia_instalada_total NUMERIC(10,2) NULL,
	ind_operacao         BOOLEAN NULL,
	ind_producao_anual_barragem BOOLEAN NULL,
	val_producao_anual_efetivamente_verificada NUMERIC(10,2) NULL,
	ide_cerh_localizacao_geografica INTEGER NULL,
	ide_cerh_obras_hidraulicas INTEGER,
	ide_cerh_intervencao_servico INTEGER NULL,
	ide_cerh_situacao_tipo_uso INTEGER NULL,
	ide_tipo_intervencao INTEGER NULL,
	ide_tipo_corpo_hidrico INTEGER NULL,
	dt_inicio_operacao DATE NULL
);
ALTER TABLE cerh_intervencao_caracterizacao
ADD PRIMARY KEY (ide_cerh_intervencao_caracterizacao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_intervencao_servico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_intervencao_servico
(
	ide_cerh_intervencao_servico INTEGER NOT NULL DEFAULT nextval(('cerh_intervencao_servico_seq'::text)::regclass),
	dsc_intervencao_servico VARCHAR(150) NOT NULL
);
ALTER TABLE cerh_intervencao_servico
ADD PRIMARY KEY (ide_cerh_intervencao_servico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_lancamento_caracterizacao_origem_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_lancamento_caracterizacao_origem
(
	ide_cerh_lancamento_caracterizacao_origem INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_caracterizacao_origem_seq'::text)::regclass),
	ide_cerh_lancamento_efluente_caracterizacao INTEGER NOT NULL,
	ide_tipo_finalidade_uso_agua  INTEGER NOT NULL
);
ALTER TABLE cerh_lancamento_caracterizacao_origem
ADD PRIMARY KEY (ide_cerh_lancamento_caracterizacao_origem);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_lancamento_efluente_caracterizacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_lancamento_efluente_caracterizacao
(
	ide_cerh_lancamento_efluente_caracterizacao INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_efluente_caracterizacao_seq'::text)::regclass),
	nom_corpo_hidrico    VARCHAR(40) NULL,
	dsc_observacao    VARCHAR(300) NULL,
	dt_inicio_lancamento DATE NULL,
	ind_efluente_recebe_tratamento BOOLEAN NULL,
	val_dbo_efluente_bruto NUMERIC(10,2) NULL,
	val_dbo_efluente_tratado NUMERIC(10,2) NULL,
	val_dbo_eficiencia_tratamento NUMERIC(4,1) NULL,
	val_coliformes_efluente_bruto NUMERIC(10,2) NULL,
	val_coliformes_efluente_tratado NUMERIC(10,2) NULL,
	val_coliformes_eficiencia_tratamento NUMERIC(4,1) NULL,
	val_vazao_efluente_maxima_instantanea NUMERIC(10,2) NULL,
	val_vazao_diluicao_outorgada NUMERIC(10,2) NULL,
	ide_cerh_localizacao_geografica INTEGER NULL,
	ide_cerh_situacao_tipo_uso INTEGER NULL,
	ide_cerh_tratamento_efluente INTEGER NULL,
	ide_tipo_corpo_hidrico INTEGER NULL	
);


ALTER TABLE cerh_lancamento_efluente_caracterizacao
ADD PRIMARY KEY (ide_cerh_lancamento_efluente_caracterizacao);

ALTER TABLE cerh_lancamento_efluente_caracterizacao 
    ADD CONSTRAINT cerh_lancamento_efluente_caracterizacao_tipo_corpo_hidrico 
FOREIGN KEY (ide_tipo_corpo_hidrico) 
REFERENCES tipo_corpo_hidrico (ide_tipo_corpo_hidrico) ON UPDATE NO ACTION ON DELETE NO ACTION;

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_lancamento_efluente_sazonalidade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_lancamento_efluente_sazonalidade
(
	ide_cerh_lancamento_efluente_sazonalidade INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_efluente_sazonalidade_seq'::text)::regclass),
	val_vazao_efluente   NUMERIC(10,2) NULL,
	val_tempo_lancamento NUMERIC(10,2) NULL,
	val_dia_mes          NUMERIC(10,2) NULL,
	ide_mes         INTEGER NULL,
	ide_cerh_lancamento_efluente_caracterizacao INTEGER NULL
);
ALTER TABLE cerh_lancamento_efluente_sazonalidade
ADD PRIMARY KEY (ide_cerh_lancamento_efluente_sazonalidade);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_localizacao_geografica_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_localizacao_geografica
(
	ide_cerh_localizacao_geografica INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_efluente_sazonalidade_seq'::text)::regclass),
	ide_localizacao_geografica INTEGER NOT NULL,
	ide_cerh_processo    INTEGER NULL,
	ide_cerh_tipo_uso    INTEGER NOT NULL
);
ALTER TABLE cerh_localizacao_geografica
ADD PRIMARY KEY (ide_cerh_localizacao_geografica);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_natureza_poco_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_natureza_poco
(
	ide_cerh_natureza_poco INTEGER NOT NULL DEFAULT nextval(('cerh_natureza_poco_seq'::text)::regclass),
	dsc_natureza_poco    VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_natureza_poco
ADD PRIMARY KEY (ide_cerh_natureza_poco);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_obras_hidraulicas_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_obras_hidraulicas
(
	ide_cerh_obras_hidraulicas INTEGER NOT NULL DEFAULT nextval(('cerh_obras_hidraulicas_seq'::text)::regclass),
	dsc_obras_hidraulicas VARCHAR(150) NOT NULL
);
ALTER TABLE cerh_obras_hidraulicas
ADD PRIMARY KEY (ide_cerh_obras_hidraulicas);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_outros_usos_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_outros_usos
(
	ide_cerh_outros_usos INTEGER NOT NULL DEFAULT nextval(('cerh_outros_usos_seq'::text)::regclass),
	dsc_outros_usos      VARCHAR(100) NOT NULL
);
ALTER TABLE cerh_outros_usos
ADD PRIMARY KEY (ide_cerh_outros_usos);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_pergunta_dados_gerais_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_pergunta_dados_gerais
(
	ide_cerh_pergunta_dados_gerais INTEGER NOT NULL DEFAULT nextval(('cerh_pergunta_dados_gerais_seq'::text)::regclass),
	dsc_pergunta         VARCHAR(500) NOT NULL,
	ind_ativo            BOOLEAN NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	dtc_exclusao         TIMESTAMP NULL,
	cod_pergunta         VARCHAR(20) NULL
);
ALTER TABLE cerh_pergunta_dados_gerais
ADD PRIMARY KEY (ide_cerh_pergunta_dados_gerais);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_processo_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_processo
(
	ide_cerh_processo    INTEGER NOT NULL DEFAULT nextval(('cerh_processo_seq'::text)::regclass),
	num_processo         VARCHAR(40) NOT NULL,
	ind_dados_concedidos BOOLEAN NULL,
	num_portaria_documento VARCHAR(40) NULL,
	dt_inicio_autorizacao DATE NULL,
	num_prazo_validade   INTEGER NULL,
	ind_possui_carta_inexigibilidade BOOLEAN NULL,
	ide_cerh             INTEGER NULL,
	ide_cerh_situacao_regularizacao INTEGER NULL,
	ide_cerh_tipo_autorizacao_outorgado INTEGER NULL,
	ide_cerh_tipo_ato_dispensa INTEGER NULL,
	ide_sistema          INTEGER NULL,
	ind_outorga_referente_ponto_cadastrado_cerh BOOLEAN NULL
);
ALTER TABLE cerh_processo
ADD PRIMARY KEY (ide_cerh_processo);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_resposta_dados_gerais_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_resposta_dados_gerais
(
	ide_cerh_resposta_dados_gerais INTEGER NOT NULL DEFAULT nextval(('cerh_resposta_dados_gerais_seq'::text)::regclass),
	ind_resposta         BOOLEAN NULL,
	ide_cerh_pergunta_dados_gerais INTEGER NOT NULL,
	ide_cerh             INTEGER NOT NULL
);
ALTER TABLE cerh_resposta_dados_gerais
ADD PRIMARY KEY (ide_cerh_resposta_dados_gerais);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_situacao_tipo_uso_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_situacao_tipo_uso
(
	ide_cerh_situacao_tipo_uso INTEGER NOT NULL DEFAULT nextval(('cerh_situacao_tipo_uso_seq'::text)::regclass),
	dsc_situacao_tipo_uso VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_situacao_tipo_uso
ADD PRIMARY KEY (ide_cerh_situacao_tipo_uso);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_situacao_regularizacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_situacao_regularizacao
(
	ide_cerh_situacao_regularizacao INTEGER NOT NULL DEFAULT nextval(('cerh_situacao_regularizacao_seq'::text)::regclass),
	dsc_situacao_regularizacao VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_situacao_regularizacao
ADD PRIMARY KEY (ide_cerh_situacao_regularizacao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_status_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_status
(
	ide_cerh_status      INTEGER NOT NULL DEFAULT nextval(('cerh_status_seq'::text)::regclass),
	dtc_status           TIMESTAMP NOT NULL,
	ide_pessoa_fisica    INTEGER NOT NULL,
	ide_cerh_tipo_status INTEGER NULL,
	ide_cerh             INTEGER NULL
);
ALTER TABLE cerh_status
ADD PRIMARY KEY (ide_cerh_status);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tipo_ato_dispensa_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tipo_ato_dispensa
(
	ide_cerh_tipo_ato_dispensa INTEGER NOT NULL DEFAULT nextval(('cerh_tipo_ato_dispensa_seq'::text)::regclass),
	dsc_tipo_ato_dispensa VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_tipo_ato_dispensa
ADD PRIMARY KEY (ide_cerh_tipo_ato_dispensa);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tipo_autorizacao_outorgado_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tipo_autorizacao_outorgado
(
	ide_cerh_tipo_autorizacao_outorgado INTEGER NOT NULL DEFAULT nextval(('cerh_tipo_autorizacao_outorgado_seq'::text)::regclass),
	dsc_cerh_tipo_autorizacao_outorgado VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_tipo_autorizacao_outorgado
ADD PRIMARY KEY (ide_cerh_tipo_autorizacao_outorgado);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tipo_prestador_servico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tipo_prestador_servico
(
	ide_cerh_tipo_prestador_servico INTEGER NOT NULL DEFAULT nextval(('cerh_tipo_autorizacao_outorgado_seq'::text)::regclass),
	dsc_tipo_prestador_servico VARCHAR(100) NOT NULL
);
ALTER TABLE cerh_tipo_prestador_servico
ADD PRIMARY KEY (ide_cerh_tipo_prestador_servico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tipo_status_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tipo_status
(
	ide_cerh_tipo_status INTEGER NOT NULL DEFAULT nextval(('cerh_tipo_status_seq'::text)::regclass),
	dsc_tipo_status      VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_tipo_status
ADD PRIMARY KEY (ide_cerh_tipo_status);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tipo_uso_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tipo_uso
(
	ide_cerh_tipo_uso    INTEGER NOT NULL DEFAULT nextval(('cerh_tipo_uso_seq'::text)::regclass),
	ide_cerh             INTEGER NOT NULL,
	ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
	ide_cerh_resposta_dados_gerais INTEGER NULL,
	ide_cerh_processo    INTEGER NULL
);
ALTER TABLE cerh_tipo_uso
ADD PRIMARY KEY (ide_cerh_tipo_uso);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_tratamento_efluente_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_tratamento_efluente
(
	ide_cerh_tratamento_efluente INTEGER NOT NULL DEFAULT nextval(('cerh_tratamento_efluente_seq'::text)::regclass),
	dsc_tratamento_efluente VARCHAR(40) NOT NULL
);
ALTER TABLE cerh_tratamento_efluente
ADD PRIMARY KEY (ide_cerh_tratamento_efluente);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE contrato_convenio_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE contrato_convenio
(
	ide_contrato_convenio INTEGER NOT NULL DEFAULT nextval(('contrato_convenio_seq'::text)::regclass),
	nom_contrato_convenio VARCHAR(200) NOT NULL,
	num_contrato         VARCHAR(20) NOT NULL,
	dtc_criacao			TIMESTAMP NOT NULL,
	dtc_exclusao		TIMESTAMP NULL,
	ind_excluido         BOOLEAN NOT NULL,
	ide_pessoa_juridica  INTEGER NOT NULL,
	ide_tipo_projeto     INTEGER NOT NULL,
	ide_gestor_financeiro INTEGER NOT NULL
);
ALTER TABLE contrato_convenio
ADD PRIMARY KEY (ide_contrato_convenio);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE gestor_financeiro_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE gestor_financeiro
(
	ide_gestor_financeiro INTEGER NOT NULL DEFAULT nextval(('gestor_financeiro_seq'::text)::regclass),
	nom_gestor_financeiro VARCHAR(40) NOT NULL
);
ALTER TABLE gestor_financeiro
ADD PRIMARY KEY (ide_gestor_financeiro);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE tipo_aproveitamento_hidreletrico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE tipo_aproveitamento_hidreletrico
(
	ide_tipo_aproveitamento_hidreletrico INTEGER NOT NULL DEFAULT nextval(('tipo_aproveitamento_hidreletrico_seq'::text)::regclass),
	dsc_tipo_aproveitamento_hidreletrico VARCHAR(40) NOT NULL,
	ind_ativo            BOOLEAN NOT NULL
);
ALTER TABLE tipo_aproveitamento_hidreletrico
ADD PRIMARY KEY (ide_tipo_aproveitamento_hidreletrico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE tipo_projeto_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE tipo_projeto
(
	ide_tipo_projeto     INTEGER NOT NULL DEFAULT nextval(('tipo_projeto_seq'::text)::regclass),
	nom_tipo_projeto     VARCHAR(200) NOT NULL,
	dtc_criacao          TIMESTAMP NOT NULL,
	ind_excluido         BOOLEAN NULL,
	dtc_exclusao         TIMESTAMP NULL
);
ALTER TABLE tipo_projeto
ADD PRIMARY KEY (ide_tipo_projeto);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE tipo_uso_recurso_hidrico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE tipo_uso_recurso_hidrico
(
	ide_tipo_uso_recurso_hidrico INTEGER NOT NULL DEFAULT nextval(('tipo_uso_recurso_hidrico_seq'::text)::regclass),
	dsc_tipo_uso_recurso_hidrico VARCHAR(40) NOT NULL
);
ALTER TABLE tipo_uso_recurso_hidrico
ADD PRIMARY KEY (ide_tipo_uso_recurso_hidrico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_processo_suspensao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_processo_suspensao
(
    ide_cerh_processo_suspensao INTEGER NOT NULL DEFAULT nextval(('cerh_processo_suspensao_seq'::text)::regclass),
    dt_inicio_suspensao            DATE NOT NULL,
    dt_fim_suspensao               DATE NULL,
    ide_cerh_processo    INTEGER NOT NULL
);
ALTER TABLE cerh_processo_suspensao
ADD PRIMARY KEY (ide_cerh_processo_suspensao);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_lancamento_abastecimento_publico_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_lancamento_abastecimento_publico
(
    ide_cerh_lancamento_abastecimento_publico INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_abastecimento_publico_seq'::text)::regclass),
    ide_cerh_tipo_prestador_servico INTEGER NOT NULL,
    ide_cerh_lancamento_caracterizacao_origem INTEGER NOT NULL
);
ALTER TABLE cerh_lancamento_abastecimento_publico
ADD PRIMARY KEY (ide_cerh_lancamento_abastecimento_publico);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE cerh_lancamento_outros_usos_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_lancamento_outros_usos
(
    ide_cerh_lancamento_outros_usos INTEGER NOT NULL DEFAULT nextval(('cerh_lancamento_outros_usos_seq'::text)::regclass),
    ide_cerh_outros_usos INTEGER NOT NULL,
    ide_cerh_lancamento_caracterizacao_origem INTEGER NOT NULL
);
ALTER TABLE cerh_lancamento_outros_usos
ADD PRIMARY KEY (ide_cerh_lancamento_outros_usos);


-------------------------------------------------------------------------------------------------------------------------------------------------------------


﻿ALTER TABLE  [ IF EXISTS ]  cerh_intervencao_caracterizacao 
  DROP CONSTRAINT r_88;
ALTER TABLE cerh_intervencao_caracterizacao
  ADD CONSTRAINT fk_cerh_intervencao_caracterizacao_tipo_intervencao FOREIGN KEY (ide_tipo_intervencao) REFERENCES tipo_intervencao (ide_tipo_intervencao);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_caracterizacao_origem 
  DROP CONSTRAINT r_87;
ALTER TABLE cerh_lancamento_caracterizacao_origem
ADD CONSTRAINT fk_cer_lan_car_ori_tipo_finalidade_uso_agua FOREIGN KEY (ide_tipo_finalidade_uso_agua) REFERENCES tipo_finalidade_uso_agua (ide_tipo_finalidade_uso_agua);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_barragem_caracterizacao_finalidade 
  DROP CONSTRAINT r_86;
ALTER TABLE cerh_barragem_caracterizacao_finalidade
ADD CONSTRAINT fk_cer_bar_car_fin_tipo_finalidade_uso_agua FOREIGN KEY (ide_tipo_finalidade_uso_agua) REFERENCES tipo_finalidade_uso_agua (ide_tipo_finalidade_uso_agua);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_caracterizacao_finalidade 
  DROP CONSTRAINT r_85;
ALTER TABLE cerh_captacao_caracterizacao_finalidade
ADD CONSTRAINT fk_cer_cap_car_fin_tipo_finalidade_uso_agua FOREIGN KEY (ide_tipo_finalidade_uso_agua) REFERENCES tipo_finalidade_uso_agua (ide_tipo_finalidade_uso_agua);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_abastecimento_publico 
  DROP CONSTRAINT r_84;
ALTER TABLE cerh_lancamento_abastecimento_publico
ADD CONSTRAINT fk_cer_lan_aba_pub_cerh_tipo_prestador_servico FOREIGN KEY (ide_cerh_tipo_prestador_servico) REFERENCES cerh_tipo_prestador_servico (ide_cerh_tipo_prestador_servico);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_abastecimento_publico 
  DROP CONSTRAINT r_83;
ALTER TABLE cerh_lancamento_abastecimento_publico
ADD CONSTRAINT fk_cer_lan_aba_pub_cerh_lancamento_caracterizacao_origem FOREIGN KEY (ide_cerh_lancamento_caracterizacao_origem) REFERENCES cerh_lancamento_caracterizacao_origem (ide_cerh_lancamento_caracterizacao_origem);

ALTER TABLE cerh_lancamento_outros_usos 
  DROP CONSTRAINT r_82;
ALTER TABLE cerh_lancamento_outros_usos
ADD CONSTRAINT fk_cerh_lancamento_abastecimento_publico_cerh_outros_usos FOREIGN KEY (ide_cerh_outros_usos) REFERENCES cerh_outros_usos (ide_cerh_outros_usos);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_outros_usos 
  DROP CONSTRAINT r_81;
ALTER TABLE cerh_lancamento_outros_usos
ADD CONSTRAINT fk_cer_lan_out_uso_cerh_lancamento_caracterizacao_origem FOREIGN KEY (ide_cerh_lancamento_caracterizacao_origem) REFERENCES cerh_lancamento_caracterizacao_origem (ide_cerh_lancamento_caracterizacao_origem);

ALTER TABLE cerh_processo_suspensao 
  DROP CONSTRAINT r_80;
ALTER TABLE cerh_processo_suspensao
ADD CONSTRAINT fk_cerh_processo_suspensao_cerh_processo FOREIGN KEY (ide_cerh_processo) REFERENCES cerh_processo (ide_cerh_processo);

ALTER TABLE cerh 
  DROP CONSTRAINT r_73;
ALTER TABLE cerh
ADD CONSTRAINT fk_cerh_contrato_convenio FOREIGN KEY (ide_contrato_convenio) REFERENCES contrato_convenio (ide_contrato_convenio);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico 
  DROP CONSTRAINT r_18;
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
ADD CONSTRAINT fk_cer_bar_apr_hid_cerh_barragem_caracterizacao_finalidade FOREIGN KEY (ide_cerh_barragem_caracterizacao_finalidade) REFERENCES cerh_barragem_caracterizacao_finalidade (ide_cerh_barragem_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico 
  DROP CONSTRAINT r_19;
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
ADD CONSTRAINT fk_cer_bar_apr_hid_tipo_aproveitamento_hidreletrico FOREIGN KEY (ide_tipo_aproveitamento_hidreletrico) REFERENCES tipo_aproveitamento_hidreletrico (ide_tipo_aproveitamento_hidreletrico);

ALTER TABLE cerh_barragem_caracterizacao 
  DROP CONSTRAINT r_11;
ALTER TABLE cerh_barragem_caracterizacao
ADD CONSTRAINT fk_cerh_barragem_caracterizacao_cerh_localizacao_geografica FOREIGN KEY (ide_cerh_localizacao_geografica) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica);

ALTER TABLE cerh_barragem_caracterizacao 
  DROP CONSTRAINT r_13;
ALTER TABLE cerh_barragem_caracterizacao
ADD CONSTRAINT fk_cerh_barragem_caracterizacao_cerh_situacao_tipo_uso FOREIGN KEY (ide_cerh_situacao_tipo_uso) REFERENCES cerh_situacao_tipo_uso (ide_cerh_situacao_tipo_uso);

ALTER TABLE cerh_barragem_caracterizacao 
  DROP CONSTRAINT r_14;
ALTER TABLE cerh_barragem_caracterizacao
ADD CONSTRAINT fk_cerh_barragem_caracterizacao_barragem FOREIGN KEY (ide_barragem) REFERENCES barragem (ide_barragem);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_barragem_caracterizacao_finalidade 
  DROP CONSTRAINT r_15;
ALTER TABLE cerh_barragem_caracterizacao_finalidade
ADD CONSTRAINT fk_cer_bar_car_fin_cerh_barragem_caracterizacao FOREIGN KEY (ide_cerh_barragem_caracterizacao) REFERENCES cerh_barragem_caracterizacao (ide_cerh_barragem_caracterizacao);

ALTER TABLE cerh_cadastro_cancelado 
  DROP CONSTRAINT r_3;
ALTER TABLE cerh_cadastro_cancelado
ADD CONSTRAINT fk_cerh_cadastro_cancelado_cerh FOREIGN KEY (ide_cerh) REFERENCES cerh (ide_cerh);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_abastecimento_industrial 
  DROP CONSTRAINT r_39;
ALTER TABLE cerh_captacao_abastecimento_industrial
ADD CONSTRAINT fk_cer_cap_aba_ind_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_abastecimento_publico 
  DROP CONSTRAINT r_36;
ALTER TABLE cerh_captacao_abastecimento_publico
ADD CONSTRAINT fk_cer_cap_aba_pub_cerh_tipo_prestador_servico FOREIGN KEY (ide_cerh_tipo_prestador_servico) REFERENCES cerh_tipo_prestador_servico (ide_cerh_tipo_prestador_servico);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_abastecimento_publico 
  DROP CONSTRAINT r_54;
ALTER TABLE cerh_captacao_abastecimento_publico
ADD CONSTRAINT fk_cer_cap_aba_pub_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

ALTER TABLE cerh_captacao_caracterizacao 
  DROP CONSTRAINT r_29;
ALTER TABLE cerh_captacao_caracterizacao
ADD CONSTRAINT fk_cerh_captacao_caracterizacao_barragem FOREIGN KEY (ide_barragem) REFERENCES barragem (ide_barragem);

ALTER TABLE cerh_captacao_caracterizacao 
  DROP CONSTRAINT r_30;
ALTER TABLE cerh_captacao_caracterizacao
ADD CONSTRAINT fk_cerh_captacao_caracterizacao_cerh_situacao_tipo_uso FOREIGN KEY (ide_cerh_situacao_tipo_uso) REFERENCES cerh_situacao_tipo_uso (ide_cerh_situacao_tipo_uso);

ALTER TABLE cerh_captacao_caracterizacao 
  DROP CONSTRAINT r_31;
ALTER TABLE cerh_captacao_caracterizacao
ADD CONSTRAINT fk_cerh_captacao_caracterizacao_cerh_localizacao_geografica FOREIGN KEY (ide_cerh_localizacao_geografica) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica);

ALTER TABLE cerh_captacao_caracterizacao 
  DROP CONSTRAINT r_75;
ALTER TABLE cerh_captacao_caracterizacao
ADD CONSTRAINT fk_cerh_captacao_caracterizacao_cerh_natureza_poco FOREIGN KEY (ide_cerh_natureza_poco) REFERENCES cerh_natureza_poco (ide_cerh_natureza_poco);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_caracterizacao_finalidade 
  DROP CONSTRAINT r_33;
ALTER TABLE cerh_captacao_caracterizacao_finalidade
ADD CONSTRAINT fk_cer_cap_car_fin_cerh_captacao_caracterizacao FOREIGN KEY (ide_cerh_captacao_caracterizacao) REFERENCES cerh_captacao_caracterizacao (ide_cerh_captacao_caracterizacao);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_dados_irrigacao 
  DROP CONSTRAINT r_37;
ALTER TABLE cerh_captacao_dados_irrigacao
ADD CONSTRAINT fk_cer_cap_dad_irr_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_dados_mineracao 
  DROP CONSTRAINT r_52;
ALTER TABLE cerh_captacao_dados_mineracao
ADD CONSTRAINT fk_cer_cap_dad_min_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_mineracao_extracao_areia 
  DROP CONSTRAINT r_51;
ALTER TABLE cerh_captacao_mineracao_extracao_areia
ADD CONSTRAINT fk_cer_cap_min_ext_are_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

ALTER TABLE cerh_captacao_outros_usos 
  DROP CONSTRAINT r_45;
ALTER TABLE cerh_captacao_outros_usos
ADD CONSTRAINT fk_cerh_captacao_outros_usos_cerh_outros_usos FOREIGN KEY (ide_cerh_outros_usos) REFERENCES cerh_outros_usos (ide_cerh_outros_usos);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_outros_usos 
  DROP CONSTRAINT r_55;
ALTER TABLE cerh_captacao_outros_usos
ADD CONSTRAINT fk_cer_cap_out_uso_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_termoeletrica 
  DROP CONSTRAINT r_40;
ALTER TABLE cerh_captacao_termoeletrica
ADD CONSTRAINT fk_cer_cap_ter_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_transposicao 
  DROP CONSTRAINT r_42;
ALTER TABLE cerh_captacao_transposicao
ADD CONSTRAINT fk_cer_cap_tra_cerh_captacao_caracterizacao_finalidade FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade) REFERENCES cerh_captacao_caracterizacao_finalidade (ide_cerh_captacao_caracterizacao_finalidade);

ALTER TABLE cerh_captacao_transposicao 
  DROP CONSTRAINT r_43;
ALTER TABLE cerh_captacao_transposicao
ADD CONSTRAINT fk_cerh_captacao_transposicao_cerh_finalidade_transposicao FOREIGN KEY (ide_cerh_finalidade_transposicao) REFERENCES cerh_finalidade_transposicao (ide_cerh_finalidade_transposicao);

ALTER TABLE cerh_captacao_vazao_sazonalidade 
  DROP CONSTRAINT r_48;
ALTER TABLE cerh_captacao_vazao_sazonalidade
ADD CONSTRAINT fk_cerh_captacao_vazao_sazonalidade_mes FOREIGN KEY (ide_mes) REFERENCES mes (ide_mes);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_captacao_vazao_sazonalidade 
  DROP CONSTRAINT r_49;
ALTER TABLE cerh_captacao_vazao_sazonalidade
ADD CONSTRAINT fk_cer_cap_vaz_saz_cerh_captacao_caracterizacao FOREIGN KEY (ide_cerh_captacao_caracterizacao) REFERENCES cerh_captacao_caracterizacao (ide_cerh_captacao_caracterizacao);

ALTER TABLE cerh_intervencao_caracterizacao 
  DROP CONSTRAINT r_24;
ALTER TABLE cerh_intervencao_caracterizacao
ADD CONSTRAINT fk_cerh_intervencao_caracterizacao_cerh_localizacao_geografica FOREIGN KEY (ide_cerh_localizacao_geografica) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica);

ALTER TABLE cerh_intervencao_caracterizacao 
  DROP CONSTRAINT r_26;
ALTER TABLE cerh_intervencao_caracterizacao
ADD CONSTRAINT fk_cerh_intervencao_caracterizacao_cerh_obras_hidraulicas FOREIGN KEY (ide_cerh_obras_hidraulicas) REFERENCES cerh_obras_hidraulicas (ide_cerh_obras_hidraulicas);

ALTER TABLE cerh_intervencao_caracterizacao 
  DROP CONSTRAINT r_27;
ALTER TABLE cerh_intervencao_caracterizacao
ADD CONSTRAINT fk_cerh_intervencao_caracterizacao_cerh_intervencao_servico FOREIGN KEY (ide_cerh_intervencao_servico) REFERENCES cerh_intervencao_servico (ide_cerh_intervencao_servico);

ALTER TABLE cerh_intervencao_caracterizacao 
  DROP CONSTRAINT r_32;
ALTER TABLE cerh_intervencao_caracterizacao
ADD CONSTRAINT fk_cerh_intervencao_caracterizacao_cerh_situacao_tipo_uso FOREIGN KEY (ide_cerh_situacao_tipo_uso) REFERENCES cerh_situacao_tipo_uso (ide_cerh_situacao_tipo_uso);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_caracterizacao_origem 
  DROP CONSTRAINT r_60;
ALTER TABLE cerh_lancamento_caracterizacao_origem
ADD CONSTRAINT fk_cer_lan_car_ori_cerh_lancamento_efluente_caracterizacao FOREIGN KEY (ide_cerh_lancamento_efluente_caracterizacao) REFERENCES cerh_lancamento_efluente_caracterizacao (ide_cerh_lancamento_efluente_caracterizacao);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_efluente_caracterizacao 
  DROP CONSTRAINT r_50;
ALTER TABLE cerh_lancamento_efluente_caracterizacao
ADD CONSTRAINT fk_cer_lan_efl_car_cerh_localizacao_geografica FOREIGN KEY (ide_cerh_localizacao_geografica) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_efluente_caracterizacao 
  DROP CONSTRAINT r_58;
ALTER TABLE cerh_lancamento_efluente_caracterizacao
ADD CONSTRAINT fk_cer_lan_efl_car_cerh_situacao_tipo_uso FOREIGN KEY (ide_cerh_situacao_tipo_uso) REFERENCES cerh_situacao_tipo_uso (ide_cerh_situacao_tipo_uso);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_efluente_caracterizacao 
  DROP CONSTRAINT r_59;
ALTER TABLE cerh_lancamento_efluente_caracterizacao
ADD CONSTRAINT fk_cer_lan_efl_car_cerh_tratamento_efluente FOREIGN KEY (ide_cerh_tratamento_efluente) REFERENCES cerh_tratamento_efluente (ide_cerh_tratamento_efluente);

ALTER TABLE cerh_lancamento_efluente_sazonalidade 
  DROP CONSTRAINT r_62;
ALTER TABLE cerh_lancamento_efluente_sazonalidade
ADD CONSTRAINT fk_cerh_lancamento_efluente_sazonalidade_mes FOREIGN KEY (ide_mes) REFERENCES mes (ide_mes);

--Nome da FK grande - Foi necessário alterar nome
ALTER TABLE cerh_lancamento_efluente_sazonalidade 
  DROP CONSTRAINT r_63;
ALTER TABLE cerh_lancamento_efluente_sazonalidade
ADD CONSTRAINT fk_cer_lan_efl_saz_cerh_lancamento_efluente_caracterizacao FOREIGN KEY (ide_cerh_lancamento_efluente_caracterizacao) REFERENCES cerh_lancamento_efluente_caracterizacao (ide_cerh_lancamento_efluente_caracterizacao);

ALTER TABLE cerh_localizacao_geografica 
  DROP CONSTRAINT r_5;
ALTER TABLE cerh_localizacao_geografica
ADD CONSTRAINT fk_cerh_localizacao_geografica_cerh_processo FOREIGN KEY (ide_cerh_processo) REFERENCES cerh_processo (ide_cerh_processo);

ALTER TABLE cerh_localizacao_geografica 
  DROP CONSTRAINT r_8;
ALTER TABLE cerh_localizacao_geografica
ADD CONSTRAINT fk_cerh_localizacao_geografica_cerh_tipo_uso FOREIGN KEY (ide_cerh_tipo_uso) REFERENCES cerh_tipo_uso (ide_cerh_tipo_uso);

ALTER TABLE cerh_processo 
  DROP CONSTRAINT r_4;
ALTER TABLE cerh_processo
ADD CONSTRAINT fk_cerh_processo_cerh FOREIGN KEY (ide_cerh) REFERENCES cerh (ide_cerh);

ALTER TABLE cerh_processo 
  DROP CONSTRAINT r_20;
ALTER TABLE cerh_processo
ADD CONSTRAINT fk_cerh_processo_cerh_situacao_regularizacao FOREIGN KEY (ide_cerh_situacao_regularizacao) REFERENCES cerh_situacao_regularizacao (ide_cerh_situacao_regularizacao);

ALTER TABLE cerh_processo 
  DROP CONSTRAINT r_21;
ALTER TABLE cerh_processo
ADD CONSTRAINT fk_cerh_processo_cerh_tipo_autorizacao_outorgado FOREIGN KEY (ide_cerh_tipo_autorizacao_outorgado) REFERENCES cerh_tipo_autorizacao_outorgado (ide_cerh_tipo_autorizacao_outorgado);

ALTER TABLE cerh_processo 
  DROP CONSTRAINT r_22;
ALTER TABLE cerh_processo
ADD CONSTRAINT fk_cerh_processo_cerh_tipo_ato_dispensa FOREIGN KEY (ide_cerh_tipo_ato_dispensa) REFERENCES cerh_tipo_ato_dispensa (ide_cerh_tipo_ato_dispensa);

ALTER TABLE cerh_resposta_dados_gerais 
  DROP CONSTRAINT r_77;
ALTER TABLE cerh_resposta_dados_gerais
ADD CONSTRAINT fk_cerh_resposta_dados_gerais_cerh_pergunta_dados_gerais FOREIGN KEY (ide_cerh_pergunta_dados_gerais) REFERENCES cerh_pergunta_dados_gerais (ide_cerh_pergunta_dados_gerais);

ALTER TABLE cerh_resposta_dados_gerais 
  DROP CONSTRAINT r_78;
ALTER TABLE cerh_resposta_dados_gerais
ADD CONSTRAINT fk_cerh_resposta_dados_gerais_cerh FOREIGN KEY (ide_cerh) REFERENCES cerh (ide_cerh);

ALTER TABLE cerh_status 
  DROP CONSTRAINT r_1;
ALTER TABLE cerh_status
ADD CONSTRAINT fk_cerh_status_cerh_tipo_status FOREIGN KEY (ide_cerh_tipo_status) REFERENCES cerh_tipo_status (ide_cerh_tipo_status);

ALTER TABLE cerh_status 
  DROP CONSTRAINT r_2;
ALTER TABLE cerh_status
ADD CONSTRAINT fk_cerh_status_cerh FOREIGN KEY (ide_cerh) REFERENCES cerh (ide_cerh);

ALTER TABLE cerh_tipo_uso 
  DROP CONSTRAINT r_7;
ALTER TABLE cerh_tipo_uso
ADD CONSTRAINT fk_cerh_tipo_uso_cerh FOREIGN KEY (ide_cerh) REFERENCES cerh (ide_cerh);

ALTER TABLE cerh_tipo_uso 
  DROP CONSTRAINT r_10;
ALTER TABLE cerh_tipo_uso
ADD CONSTRAINT fk_cerh_tipo_uso_tipo_uso_recurso_hidrico FOREIGN KEY (ide_tipo_uso_recurso_hidrico) REFERENCES tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico);

ALTER TABLE cerh_tipo_uso 
  DROP CONSTRAINT r_74;
ALTER TABLE cerh_tipo_uso
ADD CONSTRAINT fk_cerh_tipo_uso_cerh_resposta_dados_gerais FOREIGN KEY (ide_cerh_resposta_dados_gerais) REFERENCES cerh_resposta_dados_gerais (ide_cerh_resposta_dados_gerais);

ALTER TABLE cerh_tipo_uso 
  DROP CONSTRAINT r_79;
ALTER TABLE cerh_tipo_uso
ADD CONSTRAINT fk_cerh_tipo_uso_cerh_processo FOREIGN KEY (ide_cerh_processo) REFERENCES cerh_processo (ide_cerh_processo);

ALTER TABLE contrato_convenio 
  DROP CONSTRAINT r_69;
ALTER TABLE contrato_convenio
ADD CONSTRAINT fk_contrato_convenio_tipo_projeto FOREIGN KEY (ide_tipo_projeto) REFERENCES tipo_projeto (ide_tipo_projeto);

ALTER TABLE contrato_convenio 
  DROP CONSTRAINT r_70;
ALTER TABLE contrato_convenio
ADD CONSTRAINT fk_contrato_convenio_gestor_financeiro FOREIGN KEY (ide_gestor_financeiro) REFERENCES gestor_financeiro (ide_gestor_financeiro);

ALTER TABLE tipo_finalidade_uso_agua ADD COLUMN ind_requerimento boolean DEFAULT true;
ALTER TABLE tipo_finalidade_uso_agua ADD COLUMN ind_cerh boolean;


ALTER TABLE imovel_urbano
ALTER COLUMN num_inscricao_iptu DROP NOT NULL;

---

CREATE OR REPLACE FUNCTION formatar_cpf_cnpj(cpf_cnpj varchar) RETURNS text AS
$BODY$ 
	BEGIN
		IF (char_length(cpf_cnpj) != 11 and char_length(cpf_cnpj) != 14) THEN  
			RETURN 'Inválido';
		END IF;

		IF (char_length(cpf_cnpj) = 11 ) THEN  
			RETURN substring(cpf_cnpj FROM 1 FOR 3) || '.' || substring(cpf_cnpj FROM 4 FOR 3) || '.' || substring(cpf_cnpj FROM 7 FOR 3) || '-' || substring(cpf_cnpj FROM 10 FOR 2);
		END IF;

		IF (char_length(cpf_cnpj) = 14 ) THEN  
			RETURN substring(cpf_cnpj FROM 1 FOR 2) || '.' || substring(cpf_cnpj FROM 3 FOR 3) || '.' || substring(cpf_cnpj FROM 6 FOR 3) || '/' || substring(cpf_cnpj FROM 9 FOR 4) || '-' || substring(cpf_cnpj FROM 13 FOR 2);
		END IF;
	END; 
$BODY$

LANGUAGE plpgsql VOLATILE
COST 100;

ALTER FUNCTION formatar_cpf_cnpj(varchar)
  OWNER TO seia_sema;
  
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD COLUMN ide_cerh_localizacao_geografica_intervencao integer;
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD CONSTRAINT cerh_barragem_apr_hidreletico_cerh_loc_geo FOREIGN KEY (ide_cerh_localizacao_geografica_intervencao) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE cerh_intervencao_caracterizacao
  ADD COLUMN ide_cerh_localizacao_geografica_barragem integer;
ALTER TABLE cerh_intervencao_caracterizacao
  ADD CONSTRAINT cerh_intervencao_caracterizacao_ide_cerh_loc_geo_barragem FOREIGN KEY (ide_cerh_localizacao_geografica_barragem) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;

  -- #### Abaixo Scripts para a versão 1.3 ####
  
CREATE SEQUENCE cerh_hist_notificacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE public.cerh_hist_envio_notificacao (
   ide_cerh_hist_notificacao INTEGER NOT NULL DEFAULT nextval('public.cerh_hist_notificacao_seq'),
   ide_geo_rpga INTEGER NOT NULL,
   ide_usuario_agua INTEGER,
   ide_empreendimento INTEGER,
   dt_envio TIMESTAMP NOT NULL,
   ide_usuario_envio INTEGER NOT NULL,
   vlr_quantidade INTEGER NOT NULL,
   CONSTRAINT cerh_hist_envio_notificacao_pk PRIMARY KEY (ide_cerh_hist_notificacao));

COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_cerh_hist_notificacao IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.dt_envio IS 'Data e hora do envio da notificação';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_usuario_agua IS 'Chave estrangeira para a tabela Pessoa indicando o usuário água ';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_empreendimento IS 'Chave estrangeira para a tabela Empreendimento indicando o empreendimento';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_usuario_envio IS 'Chave estrangeira para a tabela Pessoa Fisica indicando o autor da ação';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.vlr_quantidade IS 'Quantidade de notificações enviadas no envio';


CREATE SEQUENCE cerh_notificacao_email_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE cerh_notificacao_emails
(
  ide_cerh_notificacao_email integer NOT NULL DEFAULT nextval('cerh_notificacao_email_seq'::regclass),
  dsc_email character varying(100) NOT NULL, 
  dsc_assunto character varying(100) NOT NULL, 
  dsc_conteudo character varying(1000) NOT NULL,
  dtc_envio timestamp without time zone NOT NULL, 
  ide_usuario_envio integer NOT NULL,
  num_ano_base integer NOT NULL,
  ind_enviado boolean NOT NULL DEFAULT false,
  CONSTRAINT cerh_notificacao_emails_pk PRIMARY KEY (ide_cerh_notificacao_email));

COMMENT ON COLUMN cerh_notificacao_emails.dsc_email IS 'Email para o qual a notificação será enviada';
COMMENT ON COLUMN cerh_notificacao_emails.dsc_assunto IS 'Texto constante no assunto do email';
COMMENT ON COLUMN cerh_notificacao_emails.dtc_envio IS 'Data em que o usuário do sistema comandou o envio no sistema';  
