create SEQUENCE public.torre_anemometrica_ide_torre_anemometrica_seq;
CREATE TABLE public.tipo_unidade_conservacao_torre (
                ide_tipo_unidade_conservacao_torre INTEGER NOT NULL,
                nom_tipo_unidade_conservacao_torre VARCHAR(30) NOT NULL,
                CONSTRAINT ide_tipo_unidade_conservacao_torre PRIMARY KEY (ide_tipo_unidade_conservacao_torre)
);

CREATE SEQUENCE public.tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq;

CREATE TABLE public.localizacao_atividade_torre (
                ide_localizacao_atividade_torre INTEGER NOT NULL,
                nom_localizacao_atividade_torre VARCHAR(30) NOT NULL,
                CONSTRAINT ide_localizacao_atividade_torre PRIMARY KEY (ide_localizacao_atividade_torre)
);

CREATE SEQUENCE public.localizacao_atividade_torre_ide_localizacao_atividade_torre_seq;

CREATE TABLE public.torre_anemometrica (
                ide_torre_anemometrica INTEGER NOT NULL DEFAULT nextval('public.torre_anemometrica_ide_torre_anemometrica_seq'),
                ide_tipo_unidade_conservacao_torre INTEGER,
                ide_localizacao_atividade_torre INTEGER NOT NULL,
                ide_tipo_natureza_torre INTEGER NOT NULL,
                nom_idenf_torre_anemometrica VARCHAR(100) NOT NULL,
                val_projecao_monitoramento INTEGER,
                ind_conservacao_amortecimento BOOLEAN NOT NULL,
                ind_processo_inema BOOLEAN NOT NULL,
                num_processo_inema character varying(50),
                val_altura_torre NUMERIC(14,2) NOT NULL,
                ind_possui_cefir BOOLEAN NOT NULL,
                ind_excluido BOOLEAN NOT NULL,
                nom_unidade_conservadora VARCHAR(100),
                ind_aceite_responsabilidade BOOLEAN NOT NULL,
                ind_aceite_instrucoes_cadastro BOOLEAN NOT NULL,
                ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL,
                ide_localizacao_geografica INTEGER NOT NULL,
                area_supressao_vegetal numeric(14,4) NOT NULL,
                ind_atividade BOOLEAN,
                CONSTRAINT ide_torre_anemometrica PRIMARY KEY (ide_torre_anemometrica)
);




COMMENT ON COLUMN public.torre_anemometrica.nom_idenf_torre_anemometrica IS 'Esse campo refere-se ao campos de Identificação da torre anemométrica.';
COMMENT ON COLUMN public.torre_anemometrica.val_projecao_monitoramento IS 'Projeção de monitoramento';
COMMENT ON COLUMN public.torre_anemometrica.ind_conservacao_amortecimento IS 'Esse campo refere-se a pergunta "Atividade situada em Unidade de Conservação ou em sua zona de amortecimento"
Sim = True
Não = Falso';
COMMENT ON COLUMN public.torre_anemometrica.ind_processo_inema IS 'A resposta refere-se a pergunta:
Se o empreendimento tem processo em tramite ou concluido no Inema.
Sim = true
Não = False';
COMMENT ON COLUMN public.torre_anemometrica.ind_possui_cefir IS 'Essa campo refere-se a pergunta "O imóvel possui CEFIR?"
Sim = True
Não = False';
COMMENT ON COLUMN public.torre_anemometrica.ind_aceite_responsabilidade IS 'No final do cadastro tem um tela de aceite de responsabilidade pelas informações declaradas:
Sim=true
Não= False';
COMMENT ON COLUMN public.torre_anemometrica.ind_aceite_instrucoes_cadastro IS 'Para iniciar o cadastro o usuário precisa confirmar a declaração de leitura das intruções do cadastro:
Sim = True
Não = False';

ALTER SEQUENCE public.torre_anemometrica_ide_torre_anemometrica_seq OWNED BY public.torre_anemometrica.ide_torre_anemometrica;

CREATE TABLE public.tipo_natureza_torre (
                ide_tipo_natureza_torre INTEGER NOT NULL,
                nom_tipo_natureza_torre VARCHAR(15) NOT NULL,
                CONSTRAINT ide_tipo_natureza_torre PRIMARY KEY (ide_tipo_natureza_torre)
);

CREATE SEQUENCE public.tipo_natureza_torre_ide_tipo_natureza_torre_seq;

ALTER TABLE public.torre_anemometrica ADD CONSTRAINT tipo_unidade_conservacao_torre_torre_anemometrica_fk
FOREIGN KEY (ide_tipo_unidade_conservacao_torre)
REFERENCES public.tipo_unidade_conservacao_torre (ide_tipo_unidade_conservacao_torre);

ALTER TABLE public.torre_anemometrica ADD CONSTRAINT localizacao_atividade_torre_torre_anemometrica_fk
FOREIGN KEY (ide_localizacao_atividade_torre)
REFERENCES public.localizacao_atividade_torre (ide_localizacao_atividade_torre);

ALTER TABLE public.torre_anemometrica ADD CONSTRAINT localizacao_geografica_torre_anemometrica_fk
FOREIGN KEY (ide_localizacao_geografica)
REFERENCES public.localizacao_geografica (ide_localizacao_geografica);

ALTER TABLE public.torre_anemometrica ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_torre_anenometrica_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES public.cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

/* 
ALTER TABLE public.tipo_natureza_torre ADD CONSTRAINT torre_anenometrica_natureza_torre_fk
FOREIGN KEY (ide_tipo_natureza_torre)
REFERENCES public.torre_anemometrica (ide_torre_anemometrica);*/

ALTER TABLE public.torre_anemometrica ADD CONSTRAINT tipo_natureza_torre_torre_anemometrica_fk
FOREIGN KEY (ide_tipo_natureza_torre)
REFERENCES public.tipo_natureza_torre (ide_tipo_natureza_torre);

ALTER TABLE cadastro_atividade_nao_sujeita_lic ADD COLUMN ind_possui_cefir boolean ;

CREATE SEQUENCE public.cadastro_atividade_nao_sujeita_lic_resp_empreendimento_seq;

CREATE TABLE public.cadastro_atividade_nao_sujeita_lic_resp_empreendimento(
	ide_cadastro_atividade_nao_sujeita_lic_resp_empreendimento INTEGER NOT NULL DEFAULT nextval(('cadastro_atividade_nao_sujeita_lic_resp_empreendimento_seq'::text)::regclass),
	ide_cadastro_atividade_nao_sujeita_lic INTEGER NOT NULL,
	ide_responsavel_empreendimento INTEGER NOT NULL,
	CONSTRAINT ide_cadastro_atividade_nao_sujeita_lic_resp_tecnico PRIMARY KEY(ide_cadastro_atividade_nao_sujeita_lic_resp_empreendimento)
);

ALTER TABLE public.cadastro_atividade_nao_sujeita_lic_resp_empreendimento
ADD CONSTRAINT ide_cadastro_atividade_nao_sujeita_lic_fk
FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
REFERENCES public.cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

ALTER TABLE public.cadastro_atividade_nao_sujeita_lic_resp_empreendimento
ADD CONSTRAINT ide_responsavel_empreendimento_fk
FOREIGN KEY (ide_responsavel_empreendimento)
REFERENCES public.responsavel_empreendimento (ide_responsavel_empreendimento);

CREATE SEQUENCE public.cadastro_atividade_nao_sujeita_lic_imovel_ide_cadastro_ativi572;

CREATE TABLE public.cadastro_atividade_nao_sujeita_lic_imovel(
  ide_cadastro_atividade_nao_sujeita_lic_imovel integer NOT NULL DEFAULT nextval('public.cadastro_atividade_nao_sujeita_lic_imovel_ide_cadastro_ativi572'),
  ide_imovel_rural integer NOT NULL,
  ide_cadastro_atividade_nao_sujeita_lic integer NOT NULL,
  CONSTRAINT ide_cadastro_atividade_nao_sujeita_lic_imovel PRIMARY KEY (ide_cadastro_atividade_nao_sujeita_lic_imovel));

  ALTER TABLE cadastro_atividade_nao_sujeita_lic_imovel
  ADD CONSTRAINT cadastro_atividade_nao_sujeita_lic_fk 
  FOREIGN KEY (ide_cadastro_atividade_nao_sujeita_lic)
  REFERENCES public.cadastro_atividade_nao_sujeita_lic (ide_cadastro_atividade_nao_sujeita_lic);

  ALTER TABLE cadastro_atividade_nao_sujeita_lic_imovel
  ADD CONSTRAINT imovel_rural_cadastro_atividade_nao_sujeita_lic_imovel_fk 
  FOREIGN KEY (ide_imovel_rural)
  REFERENCES public.imovel_rural (ide_imovel_rural);

  ALTER SEQUENCE public.cadastro_atividade_nao_sujeita_lic_imovel_ide_cadastro_ativi572 OWNED BY public.cadastro_atividade_nao_sujeita_lic_imovel.ide_cadastro_atividade_nao_sujeita_lic_imovel;

CREATE TABLE torre_anemometrica_localizacao_atividade_torre(
	ide_torre_anemometrica integer NOT NULL,
	ide_localizacao_atividade_torre integer NOT NULL,

	CONSTRAINT torre_anemometrica_localizacao_atividade_torre_ide_torre_fk FOREIGN KEY (ide_torre_anemometrica)
	      REFERENCES torre_anemometrica (ide_torre_anemometrica) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION,

	CONSTRAINT torre_anemometrica_localizacao_atividade_ide_localizacao_fk FOREIGN KEY (ide_torre_anemometrica)
	      REFERENCES torre_anemometrica (ide_torre_anemometrica) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION      

);
  