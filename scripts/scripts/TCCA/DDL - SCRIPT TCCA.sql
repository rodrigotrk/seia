----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.movimentacao_financeira_ide_movimentacao_financeira_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE movimentacao_financeira
(
	ide_movimentacao_financeira 		INTEGER NOT NULL,
	val_operacao         				NUMERIC(20,2) NOT NULL,
	dtc_operacao         				TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	num_resolucao						VARCHAR(100) NULL,
	ide_tcca							INTEGER NOT NULL,
	ide_pessoa_fisica_operacao 			INTEGER NOT NULL,
	ide_produto_execucao 				INTEGER NULL,
	ide_tcca_historico_reajuste_valor 	INTEGER NULL,
	ide_tcca_projeto_operacao 			INTEGER NOT NULL
);

ALTER TABLE movimentacao_financeira
ADD PRIMARY KEY (ide_movimentacao_financeira);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.movimentacao_financeira_tcca_produto_ide_movimentacao_financeira_tcca_produto_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE movimentacao_financeira_tcca_produto
(
	ide_movimentacao_financeira_tcca_produto 	INTEGER NOT NULL,
	ide_movimentacao_financeira 				INTEGER NOT NULL,
	ide_tipo_origem      						INTEGER NULL,
	ide_tipo_destino     						INTEGER NULL,
	ide_projeto_acao_produto_origem 			INTEGER NULL,
	ide_projeto_acao_produto_destino 			INTEGER NULL,
	ide_tcca_origem      						INTEGER NULL,
	ide_tcca_destino     						INTEGER NULL
);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD PRIMARY KEY (ide_movimentacao_financeira_tcca_produto);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.produto_execucao_ide_produto_execucao_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE produto_execucao
(
	ide_produto_execucao 		INTEGER NOT NULL,
	val_previsto         		NUMERIC(20,2) NOT NULL,
	val_contratado       		NUMERIC(20,2) NULL,
	val_executado        		NUMERIC(20,2) NULL,
	ide_projeto_acao_produto 	INTEGER NOT NULL
);

ALTER TABLE produto_execucao
ADD PRIMARY KEY (ide_produto_execucao);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.produto_saldo_ide_produto_saldo_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE produto_saldo
(
	ide_produto_saldo    			INTEGER NOT NULL,
	val_saldo_produto				NUMERIC(20,2) NOT NULL,
	val_saldo_remanejado			NUMERIC(20,2) NOT NULL,
	val_saldo_suplementado			NUMERIC(20,2) NOT NULL,
	ide_projeto_acao_produto 		INTEGER NOT NULL,
	ide_movimentacao_financeira 	INTEGER NOT NULL
);

ALTER TABLE produto_saldo
ADD PRIMARY KEY (ide_produto_saldo);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.projeto_acao_ide_projeto_acao_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE projeto_acao
(
	ide_projeto_acao     INTEGER NOT NULL,
	nom_acao             VARCHAR(200) NOT NULL,
	ind_excluido         BOOLEAN NOT NULL,
	ide_tcca_projeto     INTEGER NOT NULL
);

ALTER TABLE projeto_acao
ADD PRIMARY KEY (ide_projeto_acao);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.projeto_acao_produto_ide_projeto_acao_produto_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE projeto_acao_produto
(
	ide_projeto_acao_produto 	INTEGER NOT NULL,
	nom_produto          		VARCHAR(200) NOT NULL,
	ind_excluido         		BOOLEAN NOT NULL,
	ide_projeto_acao     		INTEGER NOT NULL
);

ALTER TABLE projeto_acao_produto
ADD PRIMARY KEY (ide_projeto_acao_produto);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.projeto_empresa_executora_ide_projeto_empresa_executora_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE projeto_empresa_executora
(
	ide_projeto_empresa_executora 	INTEGER NOT NULL,
	ind_inativa          			BOOLEAN NOT NULL,
	dtc_inativa          			TIMESTAMP(6) WITHOUT TIME ZONE NULL DEFAULT now(),
	dt_vigencia_contrato_inicio 	TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	dt_vigencia_contrato_fim 		TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	ide_pessoa_fisica_inativadora  	INTEGER NULL,
	ide_pessoa_executora 			INTEGER NOT NULL,
	ide_tcca_projeto     			INTEGER NOT NULL
);

ALTER TABLE projeto_empresa_executora
ADD PRIMARY KEY (ide_projeto_empresa_executora);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.projeto_status_ide_projeto_status_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE projeto_status
(
	ide_projeto_status   				INTEGER NOT NULL,
	dtc_projeto_status   				TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	ide_tcca_projeto_tipo_status 		INTEGER NOT NULL,
	ide_pessoa_fisica_projeto_status 	INTEGER NOT NULL,
	ide_tcca_projeto     				INTEGER NOT NULL
);

ALTER TABLE projeto_status
ADD PRIMARY KEY (ide_projeto_status);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.projeto_unidade_conservacao_ide_projeto_unidade_conservacao_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE projeto_unidade_conservacao
(
	ide_projeto_unidade_conservacao 	INTEGER NOT NULL,
	ide_unidade_conservacao 			INTEGER NOT NULL,
	ide_tcca_projeto     				INTEGER NOT NULL
);

ALTER TABLE projeto_unidade_conservacao
ADD PRIMARY KEY (ide_projeto_unidade_conservacao);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_ide_tcca_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca
(
	ide_tcca             				INTEGER NOT NULL,
	val_tcca             				NUMERIC(20,2) NOT NULL,
	val_gradacao_impacto 				NUMERIC(20,2) NOT NULL,
	dt_assinatura        				TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	dt_publicacao        				TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	num_tcca             				VARCHAR(40) NOT NULL,
	num_tcca_origem      				VARCHAR(40) NULL,
	num_processo_licenca 				VARCHAR(40) NULL,
	num_processo_sema    				VARCHAR(40) NULL,
	num_prazo_validade   				INTEGER NOT NULL,
	ind_renovado         				BOOLEAN NULL,
	ind_possui_tcca_origem 				BOOLEAN NOT NULL,
	ind_excluido         				BOOLEAN NOT NULL,
	ind_cancelado        				BOOLEAN NOT NULL,
	ind_reajustado       				BOOLEAN NOT NULL,
	ind_origem_licenciamento_estadual 	BOOLEAN NOT NULL,
	ind_modalidade_execucao_direta 		BOOLEAN NOT NULL,
	ide_pessoa_requerente 				INTEGER NOT NULL,
	ide_empreendimento   				INTEGER NOT NULL
);

ALTER TABLE tcca
ADD PRIMARY KEY (ide_tcca);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_documento_apensado_ide_tcca_documento_apensado_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_documento_apensado
(
	ide_tcca_documento_apensado 	INTEGER NOT NULL,
	url_tcca_documento   			VARCHAR(100) NOT NULL,
	ind_excluido         			BOOLEAN NOT NULL,
	ide_tcca             			INTEGER NOT NULL
);

ALTER TABLE tcca_documento_apensado
ADD PRIMARY KEY (ide_tcca_documento_apensado);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_historico_reajuste_valor_ide_tcca_historico_reajuste_valor_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_historico_reajuste_valor
(
	ide_tcca_historico_reajuste_valor 	INTEGER NOT NULL,
	dtc_reajuste          				TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	indice_reajuste  					NUMERIC(20,2) NOT NULL,
	val_tcca_anterior	  				NUMERIC(20,2) NOT NULL,
	val_reajuste         				NUMERIC(20,2) NOT NULL,
	val_tcca			  				NUMERIC(20,2) NOT NULL,
	ide_tcca             				INTEGER NOT NULL
);

ALTER TABLE tcca_historico_reajuste_valor
ADD PRIMARY KEY (ide_tcca_historico_reajuste_valor);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_historico_renovacao_prazo_validade_ide_tcca_historico_renovacao_prazo_validade_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_historico_renovacao_prazo_validade
(
	ide_tcca_historico_renovacao_prazo_validade 	INTEGER NOT NULL,
	dtc_renovacao_prazo_validade 					TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	dtc_renovacao        							TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	num_novo_prazo_validade 						INTEGER NOT NULL,
	ide_tcca             							INTEGER NOT NULL,
	ide_pessoa_fisica_renovacao 					INTEGER NOT NULL
);

ALTER TABLE tcca_historico_renovacao_prazo_validade
ADD PRIMARY KEY (ide_tcca_historico_renovacao_prazo_validade);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_projeto_ide_tcca_projeto_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_projeto
(
	ide_tcca_projeto     	INTEGER NOT NULL,
	nom_projeto          	VARCHAR(100) NOT NULL,
	num_projeto_resolucao 	VARCHAR(100) NULL,
	ind_excluido         	BOOLEAN NOT NULL,
	ind_cancelado        	BOOLEAN NOT NULL,
	ide_tcca             	INTEGER NOT NULL
);

ALTER TABLE tcca_projeto
ADD PRIMARY KEY (ide_tcca_projeto);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE tcca_projeto_operacao
(
	ide_tcca_projeto_operacao 	INTEGER NOT NULL,
	nom_operacao         		VARCHAR(100) NOT NULL,
	ind_oculto					BOOLEAN NOT NULL
);

ALTER TABLE tcca_projeto_operacao
ADD PRIMARY KEY (ide_tcca_projeto_operacao);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE tcca_projeto_tipo_status
(
	ide_tcca_projeto_tipo_status 	INTEGER NOT NULL,
	nom_tcca_tipo_status 			VARCHAR(100) NOT NULL
);

ALTER TABLE tcca_projeto_tipo_status
ADD PRIMARY KEY (ide_tcca_projeto_tipo_status);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_saldo_ide_tcca_saldo_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_saldo
(
	ide_tcca_saldo       				INTEGER NOT NULL,
	val_saldo_suplementado 				NUMERIC(20,2) NOT NULL,
	val_saldo_nao_destinado_projeto 	NUMERIC(20,2) NOT NULL,
	ide_tcca             				INTEGER NOT NULL,
	ide_movimentacao_financeira 		INTEGER NOT NULL
);

ALTER TABLE tcca_saldo
ADD PRIMARY KEY (ide_tcca_saldo);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE public.tcca_status_ide_tcca_status_seq
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;

CREATE TABLE tcca_status
(
	ide_tcca_status      			INTEGER NOT NULL,
	dtc_tcca_status      			TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	ide_pessoa_fisica    			INTEGER NOT NULL,
	ide_tcca_projeto_tipo_status 	INTEGER NOT NULL,
	ide_tcca             			INTEGER NOT NULL
);

ALTER TABLE tcca_status
ADD PRIMARY KEY (ide_tcca_status);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE tipo_origem_destino
(
	ide_tipo_origem_destino 	INTEGER NOT NULL,
	nom_tipo_origem_destino 	VARCHAR(100) NOT NULL
);

ALTER TABLE tipo_origem_destino
ADD PRIMARY KEY (ide_tipo_origem_destino);
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE movimentacao_financeira
ADD CONSTRAINT movimentacao_financeira_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE movimentacao_financeira
ADD CONSTRAINT movimentacao_financeira_ide_produto_execucao_fkey FOREIGN KEY (ide_produto_execucao) REFERENCES produto_execucao (ide_produto_execucao);

ALTER TABLE movimentacao_financeira
ADD CONSTRAINT movimentacao_financeira_ide_tcca_historico_reajuste_valor_fkey FOREIGN KEY (ide_tcca_historico_reajuste_valor) REFERENCES tcca_historico_reajuste_valor (ide_tcca_historico_reajuste_valor);

ALTER TABLE movimentacao_financeira
ADD CONSTRAINT movimentacao_financeira_ide_tcca_projeto_operacao_fkey FOREIGN KEY (ide_tcca_projeto_operacao) REFERENCES tcca_projeto_operacao (ide_tcca_projeto_operacao);

ALTER TABLE movimentacao_financeira
ADD CONSTRAINT movimentacao_financeira_ide_pessoa_fisica_operacao_fkey FOREIGN KEY (ide_pessoa_fisica_operacao) REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_movimentacao_financeira_fkey FOREIGN KEY (ide_movimentacao_financeira) REFERENCES movimentacao_financeira (ide_movimentacao_financeira);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_tipo_origem_fkey FOREIGN KEY (ide_tipo_origem) REFERENCES tipo_origem_destino (ide_tipo_origem_destino);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_tipo_destino_fkey FOREIGN KEY (ide_tipo_destino) REFERENCES tipo_origem_destino (ide_tipo_origem_destino);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_projeto_acao_produto_origem_fkey FOREIGN KEY (ide_projeto_acao_produto_origem) REFERENCES projeto_acao_produto (ide_projeto_acao_produto);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_projeto_acao_produto_destino_fkey FOREIGN KEY (ide_projeto_acao_produto_destino) REFERENCES projeto_acao_produto (ide_projeto_acao_produto);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_tcca_origem_fkey FOREIGN KEY (ide_tcca_origem) REFERENCES tcca (ide_tcca);

ALTER TABLE movimentacao_financeira_tcca_produto
ADD CONSTRAINT movimentacao_financeira_tcca_produto_ide_tcca_destino_fkey FOREIGN KEY (ide_tcca_destino) REFERENCES tcca (ide_tcca);

ALTER TABLE produto_execucao
ADD CONSTRAINT produto_execucao_ide_projeto_acao_produto_fkey FOREIGN KEY (ide_projeto_acao_produto) REFERENCES projeto_acao_produto (ide_projeto_acao_produto);

ALTER TABLE produto_saldo
ADD CONSTRAINT produto_saldo_ide_projeto_acao_produto_fkey FOREIGN KEY (ide_projeto_acao_produto) REFERENCES projeto_acao_produto (ide_projeto_acao_produto);

ALTER TABLE produto_saldo
ADD CONSTRAINT produto_saldo_ide_movimentacao_financeira_fkey FOREIGN KEY (ide_movimentacao_financeira) REFERENCES movimentacao_financeira (ide_movimentacao_financeira);

ALTER TABLE projeto_acao
ADD CONSTRAINT projeto_acao_ide_tcca_projeto_fkey FOREIGN KEY (ide_tcca_projeto) REFERENCES tcca_projeto (ide_tcca_projeto);

ALTER TABLE projeto_acao_produto
ADD CONSTRAINT projeto_acao_produto_ide_projeto_acao_fkey FOREIGN KEY (ide_projeto_acao) REFERENCES projeto_acao (ide_projeto_acao);

ALTER TABLE projeto_empresa_executora
ADD CONSTRAINT projeto_empresa_executora_ide_tcca_projeto_fkey FOREIGN KEY (ide_tcca_projeto) REFERENCES tcca_projeto (ide_tcca_projeto);

ALTER TABLE projeto_empresa_executora
ADD CONSTRAINT projeto_empresa_executora_ide_pessoa_executora_fkey FOREIGN KEY (ide_pessoa_executora) REFERENCES pessoa (ide_pessoa);

ALTER TABLE projeto_empresa_executora
ADD CONSTRAINT projeto_empresa_executora_ide_pessoa_fisica_inativadora_fkey FOREIGN KEY (ide_pessoa_fisica_inativadora) REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE projeto_status
ADD CONSTRAINT projeto_status_ide_tcca_projeto_tipo_status_fkey FOREIGN KEY (ide_tcca_projeto_tipo_status) REFERENCES tcca_projeto_tipo_status (ide_tcca_projeto_tipo_status);

ALTER TABLE projeto_status
ADD CONSTRAINT projeto_status_ide_tcca_projeto_fkey FOREIGN KEY (ide_tcca_projeto) REFERENCES tcca_projeto (ide_tcca_projeto);

ALTER TABLE projeto_status
ADD CONSTRAINT projeto_status_ide_pessoa_fisica_projeto_status_fkey FOREIGN KEY (ide_pessoa_fisica_projeto_status) REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE projeto_unidade_conservacao
ADD CONSTRAINT projeto_unidade_conservacao_ide_tcca_projeto_fkey FOREIGN KEY (ide_tcca_projeto) REFERENCES tcca_projeto (ide_tcca_projeto);

ALTER TABLE projeto_unidade_conservacao
ADD CONSTRAINT projeto_unidade_conservacao_ide_unidade_conservacao_fkey FOREIGN KEY (ide_unidade_conservacao) REFERENCES unidade_conservacao (ide_unidade_conservacao);

ALTER TABLE tcca
ADD CONSTRAINT tcca_ide_pessoa_requerente_fkey FOREIGN KEY (ide_pessoa_requerente) REFERENCES pessoa (ide_pessoa);

ALTER TABLE tcca
ADD CONSTRAINT tcca_ide_empreendimento_fkey FOREIGN KEY (ide_empreendimento) REFERENCES empreendimento (ide_empreendimento);

ALTER TABLE tcca_documento_apensado
ADD CONSTRAINT tcca_documento_apensado_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_historico_reajuste_valor
ADD CONSTRAINT tcca_historico_reajuste_valor_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_historico_renovacao_prazo_validade
ADD CONSTRAINT tcca_historico_renovacao_prazo_validade_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_historico_renovacao_prazo_validade
ADD CONSTRAINT tcca_historico_renovacao_prazo_validade_ide_pessoa_fisica_renovacao_fkey FOREIGN KEY (ide_pessoa_fisica_renovacao) REFERENCES pessoa_fisica (ide_pessoa_fisica);

ALTER TABLE tcca_projeto
ADD CONSTRAINT tcca_projeto_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_saldo
ADD CONSTRAINT tcca_saldo_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_saldo
ADD CONSTRAINT tcca_saldo_ide_movimentacao_financeira_fkey FOREIGN KEY (ide_movimentacao_financeira) REFERENCES movimentacao_financeira (ide_movimentacao_financeira);

ALTER TABLE tcca_status
ADD CONSTRAINT tcca_status_ide_tcca_projeto_tipo_status_fkey FOREIGN KEY (ide_tcca_projeto_tipo_status) REFERENCES tcca_projeto_tipo_status (ide_tcca_projeto_tipo_status);

ALTER TABLE tcca_status
ADD CONSTRAINT tcca_status_ide_tcca_fkey FOREIGN KEY (ide_tcca) REFERENCES tcca (ide_tcca);

ALTER TABLE tcca_status
ADD CONSTRAINT tcca_status_ide_pessoa_fisica_fkey FOREIGN KEY (ide_pessoa_fisica) REFERENCES pessoa_fisica (ide_pessoa_fisica);