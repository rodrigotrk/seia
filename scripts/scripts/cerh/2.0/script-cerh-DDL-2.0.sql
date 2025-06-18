-- #### Abaixo Scripts para a versão 1.3 ####
CREATE SEQUENCE cerh_hist_envio_notificacao_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE public.cerh_hist_envio_notificacao (
   ide_cerh_hist_envio_notificacao INTEGER NOT NULL DEFAULT nextval('public.cerh_hist_envio_notificacao_seq'),
   ide_geo_rpga INTEGER NOT NULL,
   ide_usuario_agua INTEGER,
   ide_empreendimento INTEGER,
   dt_envio TIMESTAMP NOT NULL,
   ide_usuario_envio INTEGER NOT NULL,
   vlr_quantidade INTEGER NOT NULL,
   CONSTRAINT cerh_hist_envio_notificacao_pk PRIMARY KEY (ide_cerh_hist_envio_notificacao));

COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_cerh_hist_envio_notificacao IS 'Chave primária da tabela';
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
--## COBRANCA
CREATE SEQUENCE public.cerh_selic_mensal_seq;

CREATE TABLE public.cerh_selic_mensal (
                ide_cerh_selic_mensal INTEGER NOT NULL DEFAULT nextval('public.cerh_selic_mensal_seq'),
                num_mes INTEGER NOT NULL,
                num_ano INTEGER NOT NULL,
                val_taxa NUMERIC(10,4) NOT NULL,
                ide_usuario_cadastro INTEGER,
                dtc_alteracao TIMESTAMP NOT NULL,
                CONSTRAINT cerh_selic_mensal_pk PRIMARY KEY (ide_cerh_selic_mensal)
);

CREATE SEQUENCE public.cerh_param_dae_seq;

CREATE TABLE public.cerh_param_dae (
                ide_cerh_param_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_param_dae_seq'),
                dsc_usuario VARCHAR(100) NOT NULL,
                dsc_senha VARCHAR(100) NOT NULL,
                dsc_versao_aplicacao VARCHAR(100) NOT NULL,
                CONSTRAINT cerh_param_dae_pk PRIMARY KEY (ide_cerh_param_dae)
);
COMMENT ON COLUMN public.cerh_param_dae.dsc_usuario IS 'Usuário utilizado para autenticação no serviço de emissão de DAE da SEFAZ';
COMMENT ON COLUMN public.cerh_param_dae.dsc_senha IS 'Senha utilizado para autenticação no serviço de emissão de DAE da SEFAZ';
COMMENT ON COLUMN public.cerh_param_dae.dsc_versao_aplicacao IS 'Versão a aplicação a ser utilizada.
Solicitada pelo serviço de emissão de DAE';

CREATE SEQUENCE public.cerh_classe_corpo_hidrico_seq;

CREATE TABLE public.cerh_classe_corpo_hidrico (
                ide_cerh_classe_corpo_hidrico INTEGER NOT NULL DEFAULT nextval('public.cerh_classe_corpo_hidrico_seq'),
                dsc_classe_corpo_hidrico VARCHAR(50) NOT NULL,
                ind_excluido BOOLEAN DEFAULT FALSE NOT NULL,
                CONSTRAINT cerh_classe_corpo_hidrico_pk PRIMARY KEY (ide_cerh_classe_corpo_hidrico)
);
COMMENT ON COLUMN public.cerh_classe_corpo_hidrico.ide_cerh_classe_corpo_hidrico IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_classe_corpo_hidrico.dsc_classe_corpo_hidrico IS 'Nome da classe do corpo hídrico';
COMMENT ON COLUMN public.cerh_classe_corpo_hidrico.ind_excluido IS 'Indica se foi excluído logicamente.';

CREATE SEQUENCE public.cerh_pond_vol_consumido_seq;

CREATE TABLE public.cerh_pond_vol_consumido (
                ide_cerh_pond_vol_consumido INTEGER NOT NULL DEFAULT nextval('public.cerh_pond_vol_consumido_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                dt_cadastro TIMESTAMP NOT NULL,
                vlr_referencia NUMERIC(17,2) NOT NULL,
                ind_excluido BOOLEAN DEFAULT FALSE NOT NULL,
                dt_excluido TIMESTAMP NOT NULL,
                CONSTRAINT cerh_pond_vol_consumido_pk PRIMARY KEY (ide_cerh_pond_vol_consumido)
);
COMMENT ON COLUMN public.cerh_pond_vol_consumido.ide_cerh_pond_vol_consumido IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_pond_vol_consumido.ide_geo_rpga IS 'Chave estrangeira da tabela geo_rpga';
COMMENT ON COLUMN public.cerh_pond_vol_consumido.dt_cadastro IS 'Data de cadastro do valor de referência';
COMMENT ON COLUMN public.cerh_pond_vol_consumido.vlr_referencia IS 'Valor de referência a ser usado para o cálculo';
COMMENT ON COLUMN public.cerh_pond_vol_consumido.ind_excluido IS 'Indica se o registro foi inativado
False - não excluído
True - excluído';
COMMENT ON COLUMN public.cerh_pond_vol_consumido.dt_excluido IS 'Data exclusão lógico do registro';

CREATE SEQUENCE public.cerh_pond_gestao_seq;

CREATE TABLE public.cerh_pond_gestao (
                ide_cerh_pond_gestao INTEGER NOT NULL DEFAULT nextval('public.cerh_pond_gestao_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                dt_cadastro TIMESTAMP NOT NULL,
                vl_referencia NUMERIC(17,2) NOT NULL,
                ind_excluido BOOLEAN DEFAULT FALSE NOT NULL,
                dt_excluido TIMESTAMP,
                CONSTRAINT cerh_pond_gestao_pk PRIMARY KEY (ide_cerh_pond_gestao)
);
COMMENT ON COLUMN public.cerh_pond_gestao.ide_cerh_pond_gestao IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_pond_gestao.ide_geo_rpga IS 'Chave estrangeira para a tabela geo_rpga';
COMMENT ON COLUMN public.cerh_pond_gestao.dt_cadastro IS 'Data de cadastro da referência';
COMMENT ON COLUMN public.cerh_pond_gestao.ind_excluido IS 'Indica se o registro foi inativado
False - não excluído
True - excluído';
COMMENT ON COLUMN public.cerh_pond_gestao.dt_excluido IS 'Data da exclusão do registro';

ALTER TABLE public.cerh_localizacao_geografica ADD COLUMN ide_geo_rpga INTEGER;

COMMENT ON COLUMN public.cerh_localizacao_geografica.ide_geo_rpga IS 'Chave estrangeira para a tabela geo_rpga. Esta fk deve ser criada';

ALTER TABLE public.cerh_localizacao_geografica ADD CONSTRAINT geo_rpga_cerh_localizacao_geografica_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.sefaz_codigo_receita_seq;

CREATE TABLE public.sefaz_codigo_receita (
                ide_sefaz_codigo_receita INTEGER NOT NULL DEFAULT nextval('public.sefaz_codigo_receita_seq'),
                num_codigo_receita INTEGER NOT NULL,
                dsc_codigo_receita VARCHAR(500) NOT NULL,
                ind_ativo BOOLEAN DEFAULT TRUE NOT NULL,
                CONSTRAINT sefaz_codigo_receita_pk PRIMARY KEY (ide_sefaz_codigo_receita)
);
COMMENT ON COLUMN public.sefaz_codigo_receita.ide_sefaz_codigo_receita IS 'Chave primária da tabela';
COMMENT ON COLUMN public.sefaz_codigo_receita.num_codigo_receita IS 'numero do código de receita a ser utilizado para gerar o DAE';
COMMENT ON COLUMN public.sefaz_codigo_receita.dsc_codigo_receita IS 'descrição sobre uso do código de receita';

ALTER TABLE public.tipo_uso_recurso_hidrico ADD COLUMN ide_sefaz_codigo_receita INTEGER NULL;

COMMENT ON COLUMN public.tipo_uso_recurso_hidrico.ide_sefaz_codigo_receita IS 'Chave primária da tabela sefaz_codigo_receita';

ALTER TABLE public.tipo_uso_recurso_hidrico ADD CONSTRAINT tipo_uso_recurso_hidrico_sefaz_codigo_receita_fk
FOREIGN KEY (ide_sefaz_codigo_receita)
REFERENCES public.sefaz_codigo_receita (ide_sefaz_codigo_receita)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.cerh_pond_classe_corpo_hidrico_seq;

CREATE TABLE public.cerh_pond_classe_corpo_hidrico (
                ide_cerh_pond_classe_corpo_hidrico INTEGER NOT NULL DEFAULT nextval('public.cerh_pond_classe_corpo_hidrico_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
                ide_cerh_classe_corpo_hidrico INTEGER NOT NULL,
                vlr_referencia NUMERIC(17,2) NOT NULL,
                ind_consumo BOOLEAN,
                dt_cadastro TIMESTAMP NOT NULL,
                ind_excluido BOOLEAN DEFAULT FALSE NOT NULL,
                dt_excluido TIMESTAMP,
                CONSTRAINT cerh_pond_classe_corpo_hidrico_pk PRIMARY KEY (ide_cerh_pond_classe_corpo_hidrico)
);
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ide_cerh_pond_classe_corpo_hidrico IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ide_geo_rpga IS 'Chave estrangeira para a tabela geo_rpga';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ide_tipo_uso_recurso_hidrico IS 'Chave estrangeira para a tabela tipo de uso do recurso hídrico';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ide_cerh_classe_corpo_hidrico IS 'Chave estrangeira para a tabela classe do corpo hídrico';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.vlr_referencia IS 'Valor de referência a ser usado para o cálculo';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ind_consumo IS 'Indica se os valores são utilizados para cálculo do consumo da captação.
1 - é consumo(utilizado no termo 2 do cálculo)
0 - não é consumo(utilizado no termo 1 do cálculo)';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.dt_cadastro IS 'Guarda a data em que o registro foi incluido';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.ind_excluido IS 'Indica se o registro foi inativado
False - não excluído
True - excluído';
COMMENT ON COLUMN public.cerh_pond_classe_corpo_hidrico.dt_excluido IS 'Data da exclusão do registro';

CREATE SEQUENCE public.cerh_preco_pub_unitario_seq;

CREATE TABLE public.cerh_preco_pub_unitario (
                ide_cerh_preco_pub_unitario INTEGER NOT NULL DEFAULT nextval('public.cerh_preco_pub_unitario_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
                ind_consumo BOOLEAN,
                vlr_referencia NUMERIC(17,2) NOT NULL,
                dt_cadstro TIMESTAMP NOT NULL,
                ind_excluido BOOLEAN DEFAULT FALSE NOT NULL,
                dt_excluido TIMESTAMP,
                CONSTRAINT cerh_preco_pub_unitario_pk PRIMARY KEY (ide_cerh_preco_pub_unitario)
);
COMMENT ON COLUMN public.cerh_preco_pub_unitario.ide_cerh_preco_pub_unitario IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.ide_geo_rpga IS 'Chave estrangeira para a tabela geo_rpga';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.ide_tipo_uso_recurso_hidrico IS 'Chave estrangeira para a tabela tipo_uso_recurso_hidrico';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.ind_consumo IS 'Indica se os valores são utilizados para cálculo do consumo
True - é consumo
False - não é consumo';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.vlr_referencia IS 'Valor de referência';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.dt_cadstro IS 'Data de cadastro do registro';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.ind_excluido IS 'Indica se o registro foi inativado
False - não excluído
True - excluído';
COMMENT ON COLUMN public.cerh_preco_pub_unitario.dt_excluido IS 'Data em que o registro foi inativado';

CREATE SEQUENCE public.arquivo_baixa_dae_seq;

CREATE TABLE public.arquivo_baixa_dae (
                ide_arquivo_baixa_dae INTEGER NOT NULL DEFAULT nextval('public.arquivo_baixa_dae_seq'),
                ide_usuario INTEGER NOT NULL,
                ide_sefaz_codigo_receita INTEGER,
                nom_arquivo VARCHAR(150) NOT NULL,
                dt_inicio_periodo_pagamento DATE,
                dt_fim_periodo_pagamento DATE,
                dsc_processamento VARCHAR(500),
                dt_processamento DATE NOT NULL,
                ind_processado boolean,
                CONSTRAINT arquivo_baixa_dae_pk PRIMARY KEY (ide_arquivo_baixa_dae)
);
COMMENT ON COLUMN public.arquivo_baixa_dae.ide_arquivo_baixa_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.arquivo_baixa_dae.ide_usuario IS 'Chave estrangeira para a tabela usuario indica o autor da ação';
COMMENT ON COLUMN public.arquivo_baixa_dae.nom_arquivo IS 'Nome do arquivo utilizado';
COMMENT ON COLUMN public.arquivo_baixa_dae.dt_inicio_periodo_pagamento IS 'Data do início do período de pagamento considerado no arquivo';
COMMENT ON COLUMN public.arquivo_baixa_dae.dt_fim_periodo_pagamento IS 'Data de fim do período de pagamento considerado
no arquivo';
COMMENT ON COLUMN public.arquivo_baixa_dae.dsc_processamento IS 'Texto descrevendo o resultado do
processamento do arquivo (sucesso e quantidade de dae''s processados ou erro e descrição do erro)';
COMMENT ON COLUMN public.arquivo_baixa_dae.dt_processamento IS 'Data em que o arquivo foi processado';





/**
 * é conflitando com o script_entrega1.3
 *  */


-- ALTER TABLE public.cerh ADD COLUMN ide_cerh_pai INTEGER;
ALTER TABLE public.cerh ADD COLUMN ide_cerh_pai INTEGER;
ALTER TABLE public.cerh ADD COLUMN dtc_alteracao_hist TIMESTAMP;
ALTER TABLE public.cerh ADD COLUMN ide_usuario_alteracao_hist INTEGER;

ALTER TABLE public.cerh ADD CONSTRAINT ide_cerh_pai_fk
FOREIGN KEY (ide_cerh_pai)
REFERENCES public.cerh (ide_cerh)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

--MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE public.cerh ADD FOREIGN KEY (ide_usuario_alteracao_hist) REFERENCES public.pessoa_fisica (ide_pessoa_fisica) 
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE SEQUENCE public.cerh_hist_comunicacao_seq;

CREATE TABLE public.cerh_hist_comunicacao (
                ide_cerh_hist_comunicacao INTEGER NOT NULL DEFAULT nextval('public.cerh_hist_comunicacao_seq'),
                ide_cerh INTEGER NOT NULL,
                ide_usuario INTEGER NOT NULL,
                dt_comunicacao TIMESTAMP NOT NULL,
                dsc_mensagem VARCHAR(500) NOT NULL,
                CONSTRAINT cerh_hist_comunicacao_pk PRIMARY KEY (ide_cerh_hist_comunicacao)
);
COMMENT ON COLUMN public.cerh_hist_comunicacao.ide_cerh_hist_comunicacao IS 'chave primária da tabela';
COMMENT ON COLUMN public.cerh_hist_comunicacao.ide_cerh IS 'chave estrangeira para a tabela cerh indica a qual cadastro a mensagem foi enviada.';
COMMENT ON COLUMN public.cerh_hist_comunicacao.ide_usuario IS 'chave estrangeira para a tabela usuário indica o autor da ação';
COMMENT ON COLUMN public.cerh_hist_comunicacao.dt_comunicacao IS 'Data em que a mensagem foi disparada';
COMMENT ON COLUMN public.cerh_hist_comunicacao.dsc_mensagem IS 'Texto da mensagem enviada';

CREATE SEQUENCE public.cerh_cobranca_seq;

CREATE TABLE public.cerh_cobranca (
                ide_cerh_cobranca INTEGER NOT NULL DEFAULT nextval('public.cerh_cobranca_seq'),
                ide_cerh INTEGER NOT NULL,
                num_ano_cobranca INTEGER NOT NULL,
                ind_parcelado BOOLEAN DEFAULT FALSE NOT NULL,
                num_max_parcelas INTEGER NOT NULL,
                ind_envio_correios BOOLEAN DEFAULT FALSE NOT NULL,
                CONSTRAINT cerh_cobranca_pk PRIMARY KEY (ide_cerh_cobranca)
);
COMMENT ON COLUMN public.cerh_cobranca.ide_cerh_cobranca IS 'chave primaria da tabela';
COMMENT ON COLUMN public.cerh_cobranca.ide_cerh IS 'Chave estrangeira para a tabela cerh';
COMMENT ON COLUMN public.cerh_cobranca.num_ano_cobranca IS 'número do ano base de cobrança';
COMMENT ON COLUMN public.cerh_cobranca.ind_parcelado IS 'indica a opção de pagamento
0- parcela unica -
gera um único DAE
1 parcelado -
permite dividir o
pagamento em
mais de um DAE';
COMMENT ON COLUMN public.cerh_cobranca.num_max_parcelas IS 'identifica o número total de parcelas em
que o pagamento será feito. Varia de acordo com o valor devido e não pode ser maior que 10 Mesmo pagamento em parcela única, pode apresentar mais de uma parcela.';

CREATE SEQUENCE public.dae_seq;

CREATE TABLE public.dae (
                ide_dae INTEGER NOT NULL DEFAULT nextval('public.dae_seq'),
                ide_dae_pai INTEGER,
                ide_cerh_parcelas_cobranca INTEGER,
                ide_sefaz_codigo_receita INTEGER NOT NULL,
                dsc_nosso_numero VARCHAR(10) NOT NULL,
                dt_emissao DATE NOT NULL,
                dt_vencimento DATE NOT NULL,
                cod_documento_origem INTEGER NOT NULL,
                num_documento_origem VARCHAR(18) NOT NULL,
                cod_referencia INTEGER NOT NULL,
                num_mes_referencia INTEGER NOT NULL,
                num_ano_referencia INTEGER NOT NULL,
                num_parcela_referencia INTEGER NOT NULL,
                num_total_parcela_referencia INTEGER NOT NULL,
                url_dae VARCHAR(500) NOT NULL,
                cod_barras VARCHAR(50) NOT NULL,
                des_aviso VARCHAR(20),
                dsc_informacoes_complementares VARCHAR(100),
                CONSTRAINT dae_pk PRIMARY KEY (ide_dae)
);
COMMENT ON COLUMN public.dae.ide_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.dae.ide_dae_pai IS 'FK para a própria tabela, indicando que o DAE é uma segunda via gerada a partir de outro DAE
já vencido';
COMMENT ON COLUMN public.dae.dsc_nosso_numero IS 'Nosso Número - identificador único dos DAEs gerado pela SEFAZ';
COMMENT ON COLUMN public.dae.dt_emissao IS 'Data de emissão do DAE';
COMMENT ON COLUMN public.dae.dt_vencimento IS 'Data de vencimento do DAE';
COMMENT ON COLUMN public.dae.cod_documento_origem IS 'tipo de documento de origem utilizado
para gerar o DAE.
1 - Auto Infração/Denúncia/Notific
2 - Número do Parcelamento
3 - Intimação Fiscal
4 - Notificação Agente Arrecadador
5 - Número da DI/DSI
6 - Termo de Depósito
8 - Contribuição Previdenciária
9 - Número da DIRE
10 - Número da Nota Fiscal 

necessário para
criação do DAE';
COMMENT ON COLUMN public.dae.num_documento_origem IS 'Número do documento de origem necessário para
criação do DAE';
COMMENT ON COLUMN public.dae.cod_referencia IS 'Tipo de referência utilizado no DAE
1 – Mês / Ano de Referência
2 – Parcela / Total de Parcelas
3 – Ano Exercício necessário para criação do DAE';
COMMENT ON COLUMN public.dae.num_mes_referencia IS 'Nês da referência utilizada para gerar
o DAE

necessário para criar o DAE';
COMMENT ON COLUMN public.dae.num_ano_referencia IS 'Ano da referência utilizada para gerar o DAE

necessário para criação do DAE';
COMMENT ON COLUMN public.dae.num_parcela_referencia IS 'Numero da atual parcela, quando este é utilizado como referência para gerar o DAE

necessário para criação do DAE';
COMMENT ON COLUMN public.dae.num_total_parcela_referencia IS 'Numero to total de parcelas do parcelamento, quando este é utilizado como referência para gerar o DAE.';
COMMENT ON COLUMN public.dae.url_dae IS 'url para acessar o DAE gerado';
COMMENT ON COLUMN public.dae.cod_barras IS 'Código de barras do DAE gerado';
COMMENT ON COLUMN public.dae.des_aviso IS 'Informações enviadas pela SEFAZ referente á futuras mudanças no serviço de Emissão de DAE';
COMMENT ON COLUMN public.dae.dsc_informacoes_complementares IS 'Texto que pode ser incluído no DAE no
campo "informações Complementares." Será composto pelo Nº do cadastro, pelas parcelas que compõem o DAE, podendo ser mais de uma, e o tipo de uso considerado no DAE';

CREATE SEQUENCE public.cerh_dae_tipo_uso_seq;

CREATE TABLE public.cerh_dae_tipo_uso (
                ide_cerh_dae_tipo_uso INTEGER NOT NULL DEFAULT nextval('public.cerh_dae_tipo_uso_seq'),
                ide_dae INTEGER NOT NULL,
                ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
                val_valor_original NUMERIC(10,2) NOT NULL,
                val_acrescimo NUMERIC(10,2),
		ide_cerh_localizacao_geografica INTEGER NOT NULL,
                CONSTRAINT cerh_dae_tipo_uso_pk PRIMARY KEY (ide_cerh_dae_tipo_uso)
);
COMMENT ON COLUMN public.cerh_dae_tipo_uso.ide_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_dae_tipo_uso.ide_tipo_uso_recurso_hidrico IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_dae_tipo_uso.ide_tipo_uso_recurso_hidrico IS 'Chave primária da tabela';

CREATE SEQUENCE public.cerh_parcelas_cobranca_seq;

CREATE TABLE public.cerh_parcelas_cobranca (
                ide_cerh_parcelas_cobranca INTEGER NOT NULL DEFAULT nextval('public.cerh_parcelas_cobranca_seq'),
                num_parcela INTEGER NOT NULL,
                vlr_original_parcela NUMERIC(10,2) NOT NULL,
				ide_cerh_cobranca INTEGER NOT NULL,
                CONSTRAINT ide_cerh_parcelas_cobranca_pk PRIMARY KEY (ide_cerh_parcelas_cobranca)
);
COMMENT ON COLUMN public.cerh_parcelas_cobranca.ide_cerh_parcelas_cobranca IS 'chave primária da tabela';
COMMENT ON COLUMN public.cerh_parcelas_cobranca.ide_cerh_cobranca IS 'chave estrangeira para da tabela cerh_cobrança';

CREATE SEQUENCE public.pagamento_dae_seq;

CREATE TABLE public.pagamento_dae (
                ide_pagamento_dae INTEGER NOT NULL DEFAULT nextval('public.pagamento_dae_seq'),
                ide_dae INTEGER NOT NULL,
                ide_arquivo_baixa_dae INTEGER NOT NULL,
                dt_pagamento DATE NOT NULL,
                vlr_pagamento NUMERIC(17,2) NOT NULL,
                CONSTRAINT pagamento_dae_pk PRIMARY KEY (ide_pagamento_dae)
);
COMMENT ON COLUMN public.pagamento_dae.ide_pagamento_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.pagamento_dae.ide_dae IS 'Chave estrangeira da tabela dae';
COMMENT ON COLUMN public.pagamento_dae.ide_arquivo_baixa_dae IS 'Chave da tabela arquivo_baixa_dae';
COMMENT ON COLUMN public.pagamento_dae.dt_pagamento IS 'Data do pagamento, obtido no arquivo de baixa do DAE';
COMMENT ON COLUMN public.pagamento_dae.vlr_pagamento IS 'Valor que foi pago';

CREATE SEQUENCE public.hist_situacao_dae_seq;

CREATE TABLE public.hist_situacao_dae (
                ide_hist_situacao_dae INTEGER NOT NULL DEFAULT nextval('public.hist_situacao_dae_seq'),
                ide_dae INTEGER NOT NULL,
                ide_usuario INTEGER,
                ide_situacao_dae INTEGER NOT NULL,
                dt_alteracao TIMESTAMP NOT NULL,
                CONSTRAINT hist_situacao_dae_pk PRIMARY KEY (ide_hist_situacao_dae)
);
COMMENT ON COLUMN public.hist_situacao_dae.ide_hist_situacao_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.hist_situacao_dae.ide_dae IS 'Chave estrangeira para a tabela DAE';
COMMENT ON COLUMN public.hist_situacao_dae.ide_usuario IS 'Chave estrangeira para a tabela usuario indica o autor da ação. 
Quando nulo, indica que a ação foi feita
pelo sistema (exemplo: dae vencido)';
COMMENT ON COLUMN public.hist_situacao_dae.ide_situacao_dae IS 'Chave estrangeira para a tabela situacao DAE';
COMMENT ON COLUMN public.hist_situacao_dae.dt_alteracao IS 'Indica a data em que a situação do DAE foi alterada';

CREATE TABLE public.situacao_dae (
                ide_situacao_dae INTEGER NOT NULL,
                dsc_situacao_dae VARCHAR(100) NOT NULL,
                CONSTRAINT situacao_dae_pk PRIMARY KEY (ide_situacao_dae)
);
COMMENT ON COLUMN public.situacao_dae.ide_situacao_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.situacao_dae.dsc_situacao_dae IS 'Descritivo das situações do DAE';

ALTER TABLE public.cerh_hist_envio_notificacao ADD CONSTRAINT empreendimento_cerh_hist_envio_notificacao_fk
FOREIGN KEY (ide_empreendimento)
REFERENCES public.empreendimento (ide_empreendimento)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_classe_corpo_hidrico ADD CONSTRAINT cerh_pond_classe_corpo_hidrico_cerh_classe_corpo_hidrico_fk
FOREIGN KEY (ide_cerh_classe_corpo_hidrico)
REFERENCES public.cerh_classe_corpo_hidrico (ide_cerh_classe_corpo_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_gestao ADD CONSTRAINT geo_rpga_cerh_pond_gestao_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_vol_consumido ADD CONSTRAINT geo_rpga_cerh_pond_vol_consumido_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_preco_pub_unitario ADD CONSTRAINT geo_rpga_cerh_preco_pub_unitario_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_classe_corpo_hidrico ADD CONSTRAINT geo_rpga_cerh_pond_classe_corpo_hidrico_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_envio_notificacao ADD CONSTRAINT geo_rpga_cerh_hist_envio_notificacao_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.tipo_uso_recurso_hidrico ADD CONSTRAINT sefaz_codigo_receita_tipo_uso_recurso_hidrico_fk
FOREIGN KEY (ide_sefaz_codigo_receita)
REFERENCES public.sefaz_codigo_receita (ide_sefaz_codigo_receita)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.dae ADD CONSTRAINT sefaz_codigo_receita_dae_fk
FOREIGN KEY (ide_sefaz_codigo_receita)
REFERENCES public.sefaz_codigo_receita (ide_sefaz_codigo_receita)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.dae ADD CONSTRAINT cerh_parcelas_cobranca_dae_fk
FOREIGN KEY (ide_cerh_parcelas_cobranca)
REFERENCES public.cerh_parcelas_cobranca (ide_cerh_parcelas_cobranca)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_preco_pub_unitario ADD CONSTRAINT tipo_uso_recurso_hidrico_cerh_preco_pub_unitario_fk
FOREIGN KEY (ide_tipo_uso_recurso_hidrico)
REFERENCES public.tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_classe_corpo_hidrico ADD CONSTRAINT tipo_uso_recurso_hidrico_cerh_pond_classe_corpo_hidrico_fk
FOREIGN KEY (ide_tipo_uso_recurso_hidrico)
REFERENCES public.tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae_tipo_uso ADD CONSTRAINT cerh_dae_tipo_uso_tipo_uso_recurso_hidrico_fk
FOREIGN KEY (ide_tipo_uso_recurso_hidrico)
REFERENCES public.tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE public.hist_situacao_dae ADD CONSTRAINT usuario_hist_situacao_dae_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.arquivo_baixa_dae ADD CONSTRAINT usuario_arquivo_baixa_dae_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.arquivo_baixa_dae ADD CONSTRAINT sefaz_codigo_receita_arquivo_baixa_dae_fk
FOREIGN KEY (ide_sefaz_codigo_receita)
REFERENCES public.sefaz_codigo_receita (ide_sefaz_codigo_receita)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_envio_notificacao ADD CONSTRAINT usuario_cerh_hist_envio_notificacao_fk
FOREIGN KEY (ide_usuario_envio)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.pagamento_dae ADD CONSTRAINT arquivo_baixa_dae_pagamento_dae_fk
FOREIGN KEY (ide_arquivo_baixa_dae)
REFERENCES public.arquivo_baixa_dae (ide_arquivo_baixa_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_cobranca ADD CONSTRAINT cerh_cerh_cobranca_fk
FOREIGN KEY (ide_cerh)
REFERENCES public.cerh (ide_cerh)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_comunicacao ADD CONSTRAINT cerh_cerh_hist_comunicacao_fk
FOREIGN KEY (ide_cerh)
REFERENCES public.cerh (ide_cerh)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_parcelas_cobranca ADD CONSTRAINT cerh_cobranca_cerh_parcelas_cobranca_fk
FOREIGN KEY (ide_cerh_cobranca)
REFERENCES public.cerh_cobranca (ide_cerh_cobranca)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.dae ADD CONSTRAINT dae_dae_fk
FOREIGN KEY (ide_dae_pai)
REFERENCES public.dae (ide_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.hist_situacao_dae ADD CONSTRAINT dae_hist_situacao_dae_fk
FOREIGN KEY (ide_dae)
REFERENCES public.dae (ide_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.pagamento_dae ADD CONSTRAINT dae_pagamento_dae_fk
FOREIGN KEY (ide_dae)
REFERENCES public.dae (ide_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae_tipo_uso ADD CONSTRAINT dae_cerh_dae_tipo_uso_fk
FOREIGN KEY (ide_dae)
REFERENCES public.dae (ide_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_comunicacao ADD CONSTRAINT usuario_cerh_hist_comunicacao_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.hist_situacao_dae ADD CONSTRAINT hist_situacao_dae_situacao_dae_fk
FOREIGN KEY (ide_situacao_dae)
REFERENCES public.situacao_dae (ide_situacao_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


--Pendente do script CERH
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD COLUMN ide_cerh_localizacao_geografica_intervencao integer;
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD CONSTRAINT cerh_barragem_apr_hidreletico_cerh_loc_geo FOREIGN KEY (ide_cerh_localizacao_geografica_intervencao) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE cerh_intervencao_caracterizacao
  ADD COLUMN ide_cerh_localizacao_geografica_barragem integer;
ALTER TABLE cerh_intervencao_caracterizacao
  ADD CONSTRAINT cerh_intervencao_caracterizacao_ide_cerh_loc_geo_barragem FOREIGN KEY (ide_cerh_localizacao_geografica_barragem) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;
  