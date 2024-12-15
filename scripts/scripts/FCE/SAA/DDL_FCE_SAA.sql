
CREATE SEQUENCE public.tipo_reservatorio_seq;

CREATE TABLE public.tipo_reservatorio (
                ide_tipo_reservatorio INTEGER NOT NULL DEFAULT nextval('public.tipo_reservatorio_seq'),
                dsc_tipo_reservatorio VARCHAR(50) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT tipo_reservatorio_pk PRIMARY KEY (ide_tipo_reservatorio)
);


INSERT INTO public.tipo_reservatorio (dsc_tipo_reservatorio) VALUES ('Elevado');
INSERT INTO public.tipo_reservatorio (dsc_tipo_reservatorio) VALUES ('Apoiado');


CREATE SEQUENCE public.fce_saa_composicoes_seq;

CREATE TABLE public.fce_saa_tipo_composicao (
                ide_fce_saa_tipo_composicao INTEGER NOT NULL DEFAULT nextval('public.fce_saa_composicoes_seq'),
                dsc_fce_saa_tipo_composicao VARCHAR(150) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT fce_saa_tipo_composicao_pk PRIMARY KEY (ide_fce_saa_tipo_composicao)
);


INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Calha Parshall');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUEs ('Floculador');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Decantador');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Filtro');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Unidade de desinfecção');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Unidade de reaproveitamento de água de lavagem');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Sistema simplificado com simples desinfecção');
INSERT INTO public.fce_saa_tipo_composicao (dsc_fce_saa_tipo_composicao) VALUES ('Outros');



CREATE SEQUENCE public.tipo_material_utilizado_seq;

CREATE TABLE public.tipo_material_utilizado (
                ide_tipo_material_utilizado INTEGER NOT NULL DEFAULT nextval('public.tipo_material_utilizado_seq'),
                dsc_material_utilizado VARCHAR(50) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT tipo_material_utilizado_pk PRIMARY KEY (ide_tipo_material_utilizado)
);

INSERT INTO public.tipo_material_utilizado (dsc_material_utilizado) VALUES ('PVC');
INSERT INTO public.tipo_material_utilizado (dsc_material_utilizado) VALUES ('Ferro Fundido');
INSERT INTO public.tipo_material_utilizado (dsc_material_utilizado) VALUES ('Aço');
INSERT INTO public.tipo_material_utilizado (dsc_material_utilizado) VALUES ('Outros');


CREATE SEQUENCE public.faixa_diametro_adutora_seq;

CREATE TABLE public.faixa_diametro_adutora (
                ide_faixa_diametro_adutora INTEGER NOT NULL DEFAULT nextval('public.faixa_diametro_adutora_seq'),
                dsc_faixa_diametro_adutora VARCHAR(50) NOT NULL,
                tipo_fase_agua INTEGER NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT faixa_diametro_adutora_pk PRIMARY KEY (ide_faixa_diametro_adutora)
);
COMMENT ON TABLE public.faixa_diametro_adutora IS 'Tabela que armazena as faixas de diâmetro utilizado nas adutoras';
COMMENT ON COLUMN public.faixa_diametro_adutora.ide_faixa_diametro_adutora IS 'Chave primária da tabela';
COMMENT ON COLUMN public.faixa_diametro_adutora.dsc_faixa_diametro_adutora IS 'Descrição da faixa do diâmetro utilizado na adutora.';
COMMENT ON COLUMN public.faixa_diametro_adutora.tipo_fase_agua IS 'Em qual fase da água será utilizada as faixas da adutora. Se na água bruta, água tratada ou reservação
';

INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('Abaixo de 100 mm',0);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('100 mm a 200 mm',1);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('201 mm - 300 mm',1);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('301mm - 400 mm',1);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('Acima de 400 mm',1);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('Abaixo de 50 mm',2);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('51 mm - 100 mm',2);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('101mm - 150 mm',2);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('151 mm - 200 mm',2);
INSERT INTO  public.faixa_diametro_adutora (dsc_faixa_diametro_adutora, tipo_fase_agua) VALUES ('Acima de 200 mm',2);


CREATE SEQUENCE public.fce_saa_seq;

CREATE TABLE public.fce_saa (
                ide_fce_saa INTEGER NOT NULL DEFAULT nextval('public.fce_saa_seq'),
                val_horizonte_projeto INTEGER NOT NULL,
                val_populacao_atendida INTEGER NOT NULL,
                val_consumo_per_capta NUMERIC(10,2) NOT NULL,
                val_vazao_media_projeto NUMERIC(10,2) NOT NULL,
                val_ext_total_adutora_bruta NUMERIC(10,2),
				ide_fce INTEGER NOT NULL,
                ide_faixa_diametro_adutora_bruta INTEGER,
                ide_tipo_material_utilizado_bruta INTEGER,
                val_ext_total_adutora_tratada NUMERIC(10,2),
                ide_faixa_diametro_adutora_tratada INTEGER,
                ide_tipo_material_utilizado_tratada INTEGER,
                val_reserva_ext_total_rede NUMERIC(10,2),
                ide_faixa_diametro_adutora_reserva INTEGER,
                CONSTRAINT fce_saa_pk PRIMARY KEY (ide_fce_saa)
);
COMMENT ON TABLE  public.fce_saa IS 'Tabela principal do FCE SAA';
COMMENT ON COLUMN public.fce_saa.ide_fce_saa IS 'Chave primária artificial da tabela';
COMMENT ON COLUMN public.fce_saa.val_horizonte_projeto IS 'Horizonte do projeto em anos';
COMMENT ON COLUMN public.fce_saa.val_populacao_atendida IS 'Tamanho da população atendida';
COMMENT ON COLUMN public.fce_saa.val_consumo_per_capta IS 'Consumo per capta';
COMMENT ON COLUMN public.fce_saa.val_vazao_media_projeto IS 'Vazão média do projeto em m3/dia';
COMMENT ON COLUMN public.fce_saa.val_ext_total_adutora_bruta IS 'Extensão total da adutora da água bruta.';
COMMENT ON COLUMN public.fce_saa.ide_faixa_diametro_adutora_bruta IS 'Faixa do diâmetro da adutora bruta.';
COMMENT ON COLUMN public.fce_saa.ide_tipo_material_utilizado_bruta IS 'Tipo de material predominante na adutora de água bruta.';
COMMENT ON COLUMN public.fce_saa.val_ext_total_adutora_tratada IS 'Extensão total da adutora da água tratada.';
COMMENT ON COLUMN public.fce_saa.ide_faixa_diametro_adutora_tratada IS 'Faixa do diâmetro da adutora tratada.';
COMMENT ON COLUMN public.fce_saa.ide_tipo_material_utilizado_tratada IS 'Tipo de material predominante na adutora de água tratada.';
COMMENT ON COLUMN public.fce_saa.val_reserva_ext_total_rede IS 'Extensão total da rede da reservação e distribuição.';
COMMENT ON COLUMN public.fce_saa.ide_faixa_diametro_adutora_reserva IS 'Faixa do diâmetro  adutora da reservação e distribuição.';



CREATE SEQUENCE public.fce_saa_localizacao_geografica_elevatoria_tratada_seq;

CREATE TABLE public.fce_saa_localizacao_geografica_elevatoria_tratada (
                ide_fce_saa_localizacao_geografica_elevatoria_tratada INTEGER NOT NULL DEFAULT nextval('public.fce_saa_localizacao_geografica_elevatoria_tratada_seq'),
                ide_fce_saa INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER DEFAULT nextval(('LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq'::text)::regclass) NOT NULL,
                nom_identificacao VARCHAR(100) NOT NULL,
                val_vazao_media NUMERIC(10,2) NOT NULL,
                CONSTRAINT fce_saa_localizacao_geografica_elevatoria_tratada_pk PRIMARY KEY (ide_fce_saa_localizacao_geografica_elevatoria_tratada)
);
COMMENT ON TABLE public.fce_saa_localizacao_geografica_elevatoria_tratada IS 'Relacionamento entre as tabelas fce_saa e localizacao_geografica ';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_tratada.ide_fce_saa_localizacao_geografica_elevatoria_tratada IS 'Chave primária da tabela';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_tratada.ide_fce_saa IS 'Chave estrangeira da tabela fce_saa';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_tratada.ide_localizacao_geografica IS 'Chave estrangeira da tabela localizacao_geografica';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_tratada.nom_identificacao IS 'Nome identificador da elevatória';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_tratada.val_vazao_media IS 'Vazão média da elevatória';


CREATE SEQUENCE public.fce_saa_localizacao_geografica_reservatorio_seq;

CREATE TABLE public.fce_saa_localizacao_geografica_reservatorio (
                ide_fce_saa_localizacao_geografica_reservatorio INTEGER NOT NULL DEFAULT nextval('public.fce_saa_localizacao_geografica_reservatorio_seq'),
                ide_fce_saa INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER DEFAULT nextval(('LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq'::text)::regclass) NOT NULL,
                nom_identificacao VARCHAR(100) NOT NULL,
                val_capacidade NUMERIC(10,2) NOT NULL,
                ide_tipo_reservatorio INTEGER NOT NULL,
                CONSTRAINT fce_saa_localizacao_geografica_reservatorio_pk PRIMARY KEY (ide_fce_saa_localizacao_geografica_reservatorio)
);
COMMENT ON TABLE public.fce_saa_localizacao_geografica_reservatorio IS 'Relacionamento entre as tabelas localizacao_geografica e fce_saa para o reservatório a reservação e distribuição de água.';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_reservatorio.ide_fce_saa IS 'Chave primária artificial da tabela';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_reservatorio.ide_localizacao_geografica IS 'Chave estrangeira da tabela localização_geografica';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_reservatorio.nom_identificacao IS 'Identificador da estação de tratamento de água.';


CREATE SEQUENCE public.fce_saa_localizacao_geografica_eta_seq;

CREATE TABLE public.fce_saa_localizacao_geografica_eta (
                ide_fce_saa_localizacao_geografica_eta INTEGER NOT NULL DEFAULT nextval('public.fce_saa_localizacao_geografica_eta_seq'),
                ide_fce_saa INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER DEFAULT nextval(('LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq'::text)::regclass) NOT NULL,
                nom_identificacao VARCHAR(100) NOT NULL,
                val_vazao_media NUMERIC(10,2) NOT NULL,
                CONSTRAINT fce_saa_localizacao_geografica_eta_pk PRIMARY KEY (ide_fce_saa_localizacao_geografica_eta)
);
COMMENT ON TABLE public.fce_saa_localizacao_geografica_eta IS 'Relacionamento entre a tabela fce_saa sobre a água tratada e a tabela localização geográfica relacionada elevatória de água bruta.';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_eta.ide_fce_saa_localizacao_geografica_eta IS 'Chave primária da tabela';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_eta.ide_fce_saa IS 'Chave estrangeira da tabela fce_saa';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_eta.ide_localizacao_geografica IS 'Chave estrangeira da tabela localização_geografica';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_eta.nom_identificacao IS 'Identificador da estação de tratamento de água.';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_eta.val_vazao_media IS 'Vazão média da estação em m3/dia.';


CREATE SEQUENCE public.eta_tipo_composicao_seq;

CREATE TABLE public.eta_tipo_composicao (
                ide_eta_tipo_composicao INTEGER NOT NULL DEFAULT nextval('public.eta_tipo_composicao_seq'),
                ide_fce_saa_localizacao_geografica_eta INTEGER NOT NULL,
                ide_fce_saa_tipo_composicao INTEGER NOT NULL,
                val_quantidade INTEGER NOT NULL,
                CONSTRAINT eta_tipo_composicao_pk PRIMARY KEY (ide_eta_tipo_composicao)
);
COMMENT ON TABLE public.eta_tipo_composicao IS 'Tabela de relacionamento entre as tabelas fce_saa_tipo_composicao e fce_saa_localizacao_geografica_eta';
COMMENT ON COLUMN public.eta_tipo_composicao.ide_eta_tipo_composicao IS 'Chave primária da tabela';
COMMENT ON COLUMN public.eta_tipo_composicao.ide_fce_saa_localizacao_geografica_eta IS 'Chave estrangeira da tabela fce_saa_localizacao_geografica_eta';
COMMENT ON COLUMN public.eta_tipo_composicao.ide_fce_saa_tipo_composicao IS 'Chave estrangeira da tabela fce_saa_tipo_composicao';
COMMENT ON COLUMN public.eta_tipo_composicao.val_quantidade IS 'Armazena a quantidade da composição da estação de tratamento da água tratada.';


CREATE SEQUENCE public.fce_saa_localizacao_geografica_dados_concedidos_seq;

CREATE TABLE public.fce_saa_localizacao_geografica_dados_concedidos (
                ide_fce_saa_localizacao_geografica_dados_concedidos INTEGER NOT NULL DEFAULT nextval('public.fce_saa_localizacao_geografica_dados_concedidos_seq'),
                ide_localizacao_geografica INTEGER NOT NULL,
                ide_fce_saa INTEGER NOT NULL,
                ide_tipo_captacao INTEGER NOT NULL,
                nom_corpo_hidrico VARCHAR(100) NULL,
                val_vazao NUMERIC(10,2) NOT NULL,
                CONSTRAINT fce_saa_localizacao_geografica_dados_concedidos_pk PRIMARY KEY (ide_fce_saa_localizacao_geografica_dados_concedidos)
);
COMMENT ON TABLE public.fce_saa_localizacao_geografica_dados_concedidos IS 'Relacionamento entre as tabelas localizacao_geografica e fce_saa para as coordenadas dos dados concedidos';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.ide_fce_saa_localizacao_geografica_dados_concedidos IS 'Chave primária da tabela';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.ide_localizacao_geografica IS 'Chave estrangeira da tabela localização_geografica';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.ide_fce_saa IS 'Chave estrangeira da tabela fce_saa';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.ide_tipo_captacao IS 'Chave estrangeira da tabela tipo_captacao para determinar se é captação superficial ou subterrânea';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.nom_corpo_hidrico IS 'Nome do corpo hídrico utilizado na captação superficial';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_dados_concedidos.val_vazao IS 'Vazão ou vazão média dependendo do tipo de captação.';



CREATE SEQUENCE public.fce_saa_localizacao_geografica_elevatoria_bruta_seq;

CREATE TABLE public.fce_saa_localizacao_geografica_elevatoria_bruta (
                ide_fce_saa_localizacao_geografica_elevatoria_bruta INTEGER NOT NULL DEFAULT nextval('public.fce_saa_localizacao_geografica_elevatoria_bruta_seq'),
                ide_fce_saa INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER DEFAULT nextval(('LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq'::text)::regclass) NOT NULL,
                val_vazao NUMERIC(10,2) NOT NULL,
                CONSTRAINT fce_saa_localizacao_geografica_elevatoria_bruta_pk PRIMARY KEY (ide_fce_saa_localizacao_geografica_elevatoria_bruta)
);
COMMENT ON TABLE public.fce_saa_localizacao_geografica_elevatoria_bruta IS 'Relacionamento entre as tabelas localizacao_geografica e fce_saa';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_bruta.ide_fce_saa_localizacao_geografica_elevatoria_bruta IS 'Chave primária da tabela';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_bruta.ide_fce_saa IS 'Chave estrangeira da tabela fce_saa';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_bruta.ide_localizacao_geografica IS 'Chave estrangeira da tabela localização_geografica';
COMMENT ON COLUMN public.fce_saa_localizacao_geografica_elevatoria_bruta.val_vazao IS 'Vazão em metro cúbico por dia.';



ALTER TABLE public.fce_saa_localizacao_geografica_reservatorio ADD CONSTRAINT tipo_reservatorio_local_geo_coord_reservatorio_fk
FOREIGN KEY (ide_tipo_reservatorio)
REFERENCES public.tipo_reservatorio (ide_tipo_reservatorio)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.eta_tipo_composicao ADD CONSTRAINT fce_saa_tipo_composicao_fce_saa_tipo_composicoes_fk
FOREIGN KEY (ide_fce_saa_tipo_composicao)
REFERENCES public.fce_saa_tipo_composicao (ide_fce_saa_tipo_composicao)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_elevatoria_bruta ADD CONSTRAINT localizacao_geografica_local_geo_bruta_elevatoria_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_eta ADD CONSTRAINT localizacao_geografica_local_geo_tratada_eta_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_reservatorio ADD CONSTRAINT localizacao_geografica_local_geo_coord_reservatorio_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_elevatoria_tratada ADD CONSTRAINT localizacao_geografica_local_geo_estacao_tratada_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT tipo_material_utilizado_fce_ssa_bruta_fk
FOREIGN KEY (ide_tipo_material_utilizado_bruta)
REFERENCES public.tipo_material_utilizado (ide_tipo_material_utilizado)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT fce_fk
FOREIGN KEY (ide_fce)
REFERENCES public.fce (ide_fce)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT tipo_material_utilizado_fce_saa_tratada_fk
FOREIGN KEY (ide_tipo_material_utilizado_tratada)
REFERENCES public.tipo_material_utilizado (ide_tipo_material_utilizado)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT faixa_diametro_adutora_fce_ssa_bruta_fk
FOREIGN KEY (ide_faixa_diametro_adutora_bruta)
REFERENCES public.faixa_diametro_adutora (ide_faixa_diametro_adutora)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT faixa_diametro_adutora_fce_saa_bruta_fk
FOREIGN KEY (ide_faixa_diametro_adutora_tratada)
REFERENCES public.faixa_diametro_adutora (ide_faixa_diametro_adutora)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa ADD CONSTRAINT faixa_diametro_adutora_fce_saa_reserva_fk
FOREIGN KEY (ide_faixa_diametro_adutora_reserva)
REFERENCES public.faixa_diametro_adutora (ide_faixa_diametro_adutora)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_elevatoria_bruta ADD CONSTRAINT fce_ssa_local_geo_bruta_elevatoria_fk
FOREIGN KEY (ide_fce_saa)
REFERENCES public.fce_saa (ide_fce_saa)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_eta ADD CONSTRAINT fce_saa_local_geo_tratada_eta_fk
FOREIGN KEY (ide_fce_saa)
REFERENCES public.fce_saa (ide_fce_saa)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_reservatorio ADD CONSTRAINT fce_saa_local_geo_coord_reservatorio_fk
FOREIGN KEY (ide_fce_saa)
REFERENCES public.fce_saa (ide_fce_saa)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.fce_saa_localizacao_geografica_elevatoria_tratada ADD CONSTRAINT fce_saa_local_geo_estacao_tratada_fk
FOREIGN KEY (ide_fce_saa)
REFERENCES public.fce_saa (ide_fce_saa)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.eta_tipo_composicao ADD CONSTRAINT local_geo_tratada_eta_fce_saa_tipo_composicoes_fk
FOREIGN KEY (ide_fce_saa_localizacao_geografica_eta)
REFERENCES public.fce_saa_localizacao_geografica_eta (ide_fce_saa_localizacao_geografica_eta)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
  
COMMIT;