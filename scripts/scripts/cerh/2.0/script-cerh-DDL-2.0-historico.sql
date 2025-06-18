

ALTER TABLE cerh ADD COLUMN ide_cerh_pai INTEGER;

ALTER TABLE cerh ADD COLUMN ind_historico BOOLEAN;
ALTER TABLE cerh ADD COLUMN ide_cerh_status INTEGER;

ALTER TABLE cerh ADD FOREIGN KEY (ide_cerh_pai) REFERENCES public.cerh (ide_cerh) 
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE cerh ADD CONSTRAINT fk_cerh_cerh_status FOREIGN KEY (ide_cerh_status) REFERENCES cerh_status (ide_cerh_status)
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE cerh_status RENAME TO cerh_status_historico;
ALTER SEQUENCE cerh_status_seq RENAME TO cerh_status_historico_seq;

ALTER TABLE cerh_tipo_status RENAME ide_cerh_tipo_status  TO ide_cerh_status;
ALTER TABLE cerh_tipo_status RENAME dsc_tipo_status TO dsc_status;
ALTER TABLE cerh_tipo_status RENAME TO cerh_status;
ALTER SEQUENCE cerh_tipo_status_seq RENAME TO cerh_status_seq;

update cerh set ind_historico = false 
where ind_historico is null;

ALTER TABLE public.cerh ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.barragem ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_barragem_aproveitamento_hidreletrico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_barragem_caracterizacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_barragem_caracterizacao_finalidade ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_abastecimento_industrial ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_abastecimento_publico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_caracterizacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_caracterizacao_finalidade ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_dados_irrigacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_dados_mineracao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_mineracao_extracao_areia ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_outros_usos ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_termoeletrica ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_transposicao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_captacao_vazao_sazonalidade ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_finalidade_transposicao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_intervencao_caracterizacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_intervencao_servico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_lancamento_abastecimento_publico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_lancamento_caracterizacao_origem ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_lancamento_efluente_caracterizacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_lancamento_efluente_sazonalidade ADD COLUMN ide_objeto_pai INTEGER;

ALTER TABLE public.cerh_lancamento_outros_usos ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_localizacao_geografica ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_natureza_poco ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_obras_hidraulicas ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_outros_usos ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_pergunta_dados_gerais ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_processo ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_processo_suspensao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_resposta_dados_gerais ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_situacao_regularizacao ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_situacao_tipo_uso ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_tipo_ato_dispensa ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_tipo_autorizacao_outorgado ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_tipo_prestador_servico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_tipo_uso ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.cerh_tratamento_efluente ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.contrato_convenio ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.gestor_financeiro ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.tipo_aproveitamento_hidreletrico ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.tipo_projeto ADD COLUMN ide_objeto_pai INTEGER;
ALTER TABLE public.tipo_uso_recurso_hidrico ADD COLUMN ide_objeto_pai INTEGER;


ALTER TABLE public.barragem ADD CONSTRAINT barragem_fk  FOREIGN KEY (ide_barragem ) REFERENCES  public.barragem(ide_barragem) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_barragem_aproveitamento_hidreletrico ADD CONSTRAINT cerh_barragem_aproveitamento_hidreletrico_fk  FOREIGN KEY (ide_cerh_barragem_aproveitamento_hidreletrico ) REFERENCES  public.cerh_barragem_aproveitamento_hidreletrico(ide_cerh_barragem_aproveitamento_hidreletrico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_barragem_caracterizacao ADD CONSTRAINT cerh_barragem_caracterizacao_fk  FOREIGN KEY (ide_cerh_barragem_caracterizacao ) REFERENCES  public.cerh_barragem_caracterizacao(ide_cerh_barragem_caracterizacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_barragem_caracterizacao_finalidade ADD CONSTRAINT cerh_barragem_caracterizacao_finalidade_fk  FOREIGN KEY (ide_cerh_barragem_caracterizacao_finalidade ) REFERENCES  public.cerh_barragem_caracterizacao_finalidade(ide_cerh_barragem_caracterizacao_finalidade) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_abastecimento_industrial ADD CONSTRAINT cerh_captacao_abastecimento_industrial_fk  FOREIGN KEY (ide_cerh_captacao_abastecimento_industrial ) REFERENCES  public.cerh_captacao_abastecimento_industrial(ide_cerh_captacao_abastecimento_industrial) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_abastecimento_publico ADD CONSTRAINT cerh_captacao_abastecimento_publico_fk  FOREIGN KEY (ide_cerh_captacao_abastecimento_publico ) REFERENCES  public.cerh_captacao_abastecimento_publico(ide_cerh_captacao_abastecimento_publico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_caracterizacao ADD CONSTRAINT cerh_captacao_caracterizacao_fk  FOREIGN KEY (ide_cerh_captacao_caracterizacao ) REFERENCES  public.cerh_captacao_caracterizacao(ide_cerh_captacao_caracterizacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_caracterizacao_finalidade ADD CONSTRAINT cerh_captacao_caracterizacao_finalidade_fk  FOREIGN KEY (ide_cerh_captacao_caracterizacao_finalidade ) REFERENCES  public.cerh_captacao_caracterizacao_finalidade(ide_cerh_captacao_caracterizacao_finalidade) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_dados_irrigacao ADD CONSTRAINT cerh_captacao_dados_irrigacao_fk  FOREIGN KEY (ide_cerh_captacao_dados_irrigacao ) REFERENCES  public.cerh_captacao_dados_irrigacao(ide_cerh_captacao_dados_irrigacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_dados_mineracao ADD CONSTRAINT cerh_captacao_dados_mineracao_fk  FOREIGN KEY (ide_cerh_captacao_dados_mineracao ) REFERENCES  public.cerh_captacao_dados_mineracao(ide_cerh_captacao_dados_mineracao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_mineracao_extracao_areia ADD CONSTRAINT cerh_captacao_mineracao_extracao_areia_fk  FOREIGN KEY (ide_cerh_captacao_mineracao_extracao_areia ) REFERENCES  public.cerh_captacao_mineracao_extracao_areia(ide_cerh_captacao_mineracao_extracao_areia) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_outros_usos ADD CONSTRAINT cerh_captacao_outros_usos_fk  FOREIGN KEY (ide_cerh_captacao_outros_usos ) REFERENCES  public.cerh_captacao_outros_usos(ide_cerh_captacao_outros_usos) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_termoeletrica ADD CONSTRAINT cerh_captacao_termoeletrica_fk  FOREIGN KEY (ide_cerh_captacao_termoeletrica ) REFERENCES  public.cerh_captacao_termoeletrica(ide_cerh_captacao_termoeletrica) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_transposicao ADD CONSTRAINT cerh_captacao_transposicao_fk  FOREIGN KEY (ide_cerh_captacao_transposicao ) REFERENCES  public.cerh_captacao_transposicao(ide_cerh_captacao_transposicao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_captacao_vazao_sazonalidade ADD CONSTRAINT cerh_captacao_vazao_sazonalidade_fk  FOREIGN KEY (ide_cerh_captacao_vazao_sazonalidade ) REFERENCES  public.cerh_captacao_vazao_sazonalidade(ide_cerh_captacao_vazao_sazonalidade) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_finalidade_transposicao ADD CONSTRAINT cerh_finalidade_transposicao_fk  FOREIGN KEY (ide_cerh_finalidade_transposicao ) REFERENCES  public.cerh_finalidade_transposicao(ide_cerh_finalidade_transposicao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_intervencao_caracterizacao ADD CONSTRAINT cerh_intervencao_caracterizacao_fk  FOREIGN KEY (ide_cerh_intervencao_caracterizacao ) REFERENCES  public.cerh_intervencao_caracterizacao(ide_cerh_intervencao_caracterizacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_intervencao_servico ADD CONSTRAINT cerh_intervencao_servico_fk  FOREIGN KEY (ide_cerh_intervencao_servico ) REFERENCES  public.cerh_intervencao_servico(ide_cerh_intervencao_servico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_lancamento_abastecimento_publico ADD CONSTRAINT cerh_lancamento_abastecimento_publico_fk  FOREIGN KEY (ide_cerh_lancamento_abastecimento_publico ) REFERENCES  public.cerh_lancamento_abastecimento_publico(ide_cerh_lancamento_abastecimento_publico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_lancamento_caracterizacao_origem ADD CONSTRAINT cerh_lancamento_caracterizacao_origem_fk  FOREIGN KEY (ide_cerh_lancamento_caracterizacao_origem ) REFERENCES  public.cerh_lancamento_caracterizacao_origem(ide_cerh_lancamento_caracterizacao_origem) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_lancamento_efluente_caracterizacao ADD CONSTRAINT cerh_lancamento_efluente_caracterizacao_fk  FOREIGN KEY (ide_cerh_lancamento_efluente_caracterizacao ) REFERENCES  public.cerh_lancamento_efluente_caracterizacao(ide_cerh_lancamento_efluente_caracterizacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_lancamento_efluente_sazonalidade ADD CONSTRAINT cerh_lancamento_efluente_sazonalidade_fk  FOREIGN KEY (ide_cerh_lancamento_efluente_sazonalidade ) REFERENCES  public.cerh_lancamento_efluente_sazonalidade(ide_cerh_lancamento_efluente_sazonalidade) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;

ALTER TABLE public.cerh_lancamento_outros_usos ADD CONSTRAINT cerh_lancamento_outros_usos_fk  FOREIGN KEY (ide_cerh_lancamento_outros_usos ) REFERENCES  public.cerh_lancamento_outros_usos(ide_cerh_lancamento_outros_usos) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_localizacao_geografica ADD CONSTRAINT cerh_localizacao_geografica_fk  FOREIGN KEY (ide_cerh_localizacao_geografica ) REFERENCES  public.cerh_localizacao_geografica(ide_cerh_localizacao_geografica) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_natureza_poco ADD CONSTRAINT cerh_natureza_poco_fk  FOREIGN KEY (ide_cerh_natureza_poco ) REFERENCES  public.cerh_natureza_poco(ide_cerh_natureza_poco) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_obras_hidraulicas ADD CONSTRAINT cerh_obras_hidraulicas_fk  FOREIGN KEY (ide_cerh_obras_hidraulicas ) REFERENCES  public.cerh_obras_hidraulicas(ide_cerh_obras_hidraulicas) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_outros_usos ADD CONSTRAINT cerh_outros_usos_fk  FOREIGN KEY (ide_cerh_outros_usos ) REFERENCES  public.cerh_outros_usos(ide_cerh_outros_usos) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_pergunta_dados_gerais ADD CONSTRAINT cerh_pergunta_dados_gerais_fk  FOREIGN KEY (ide_cerh_pergunta_dados_gerais ) REFERENCES  public.cerh_pergunta_dados_gerais(ide_cerh_pergunta_dados_gerais) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_processo ADD CONSTRAINT cerh_processo_fk  FOREIGN KEY (ide_cerh_processo ) REFERENCES  public.cerh_processo(ide_cerh_processo) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_processo_suspensao ADD CONSTRAINT cerh_processo_suspensao_fk  FOREIGN KEY (ide_cerh_processo_suspensao ) REFERENCES  public.cerh_processo_suspensao(ide_cerh_processo_suspensao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_resposta_dados_gerais ADD CONSTRAINT cerh_resposta_dados_gerais_fk  FOREIGN KEY (ide_cerh_resposta_dados_gerais ) REFERENCES  public.cerh_resposta_dados_gerais(ide_cerh_resposta_dados_gerais) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_situacao_regularizacao ADD CONSTRAINT cerh_situacao_regularizacao_fk  FOREIGN KEY (ide_cerh_situacao_regularizacao ) REFERENCES  public.cerh_situacao_regularizacao(ide_cerh_situacao_regularizacao) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_situacao_tipo_uso ADD CONSTRAINT cerh_situacao_tipo_uso_fk  FOREIGN KEY (ide_cerh_situacao_tipo_uso ) REFERENCES  public.cerh_situacao_tipo_uso(ide_cerh_situacao_tipo_uso) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_tipo_ato_dispensa ADD CONSTRAINT cerh_tipo_ato_dispensa_fk  FOREIGN KEY (ide_cerh_tipo_ato_dispensa ) REFERENCES  public.cerh_tipo_ato_dispensa(ide_cerh_tipo_ato_dispensa) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_tipo_autorizacao_outorgado ADD CONSTRAINT cerh_tipo_autorizacao_outorgado_fk  FOREIGN KEY (ide_cerh_tipo_autorizacao_outorgado ) REFERENCES  public.cerh_tipo_autorizacao_outorgado(ide_cerh_tipo_autorizacao_outorgado) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_tipo_prestador_servico ADD CONSTRAINT cerh_tipo_prestador_servico_fk  FOREIGN KEY (ide_cerh_tipo_prestador_servico ) REFERENCES  public.cerh_tipo_prestador_servico(ide_cerh_tipo_prestador_servico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_tipo_uso ADD CONSTRAINT cerh_tipo_uso_fk  FOREIGN KEY (ide_cerh_tipo_uso ) REFERENCES  public.cerh_tipo_uso(ide_cerh_tipo_uso) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.cerh_tratamento_efluente ADD CONSTRAINT cerh_tratamento_efluente_fk  FOREIGN KEY (ide_cerh_tratamento_efluente ) REFERENCES  public.cerh_tratamento_efluente(ide_cerh_tratamento_efluente) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.contrato_convenio ADD CONSTRAINT contrato_convenio_fk  FOREIGN KEY (ide_contrato_convenio ) REFERENCES  public.contrato_convenio(ide_contrato_convenio) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.gestor_financeiro ADD CONSTRAINT gestor_financeiro_fk  FOREIGN KEY (ide_gestor_financeiro ) REFERENCES  public.gestor_financeiro(ide_gestor_financeiro) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.tipo_aproveitamento_hidreletrico ADD CONSTRAINT tipo_aproveitamento_hidreletrico_fk  FOREIGN KEY (ide_tipo_aproveitamento_hidreletrico ) REFERENCES  public.tipo_aproveitamento_hidreletrico(ide_tipo_aproveitamento_hidreletrico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.tipo_projeto ADD CONSTRAINT tipo_projeto_fk  FOREIGN KEY (ide_tipo_projeto ) REFERENCES  public.tipo_projeto(ide_tipo_projeto) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
ALTER TABLE public.tipo_uso_recurso_hidrico ADD CONSTRAINT tipo_uso_recurso_hidrico_fk  FOREIGN KEY (ide_tipo_uso_recurso_hidrico ) REFERENCES  public.tipo_uso_recurso_hidrico(ide_tipo_uso_recurso_hidrico) ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;





CREATE OR REPLACE FUNCTION preencher_cerh_status()
RETURNS void AS
$BODY$
DECLARE
        item RECORD;
BEGIN
	FOR item IN select * from cerh_status_historico 
		    where ide_cerh_status in (select max(ide_cerh_status) from cerh_status_historico group by ide_cerh)
		    order by ide_cerh
	LOOP
	     update cerh set ide_cerh_status=item.ide_cerh_tipo_status where ide_cerh = item.ide_cerh;
        END LOOP;        
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

SELECT preencher_cerh_status();
DROP FUNCTION preencher_cerh_status();

INSERT INTO funcionalidade_url(ide_funcionalidade, dsc_url, ind_principal)
    VALUES (73, '/paginas/manter-cerh/historico.xhtml', false);

    
    
    CREATE SEQUENCE public.cerh_notificacao_email_seq;
 
CREATE TABLE public.cerh_notificacao_emails (
                ide_cerh_notificacao_email INTEGER NOT NULL DEFAULT nextval('public.cerh_notificacao_email_seq'),
                dsc_email VARCHAR(100) NOT NULL,
                dsc_assunto VARCHAR(100) NOT NULL,
                dsc_conteudo VARCHAR(1000) NOT NULL,
                dtc_envio TIMESTAMP NOT NULL,
                ide_usuario_envio INTEGER NOT NULL,
                num_ano_base INTEGER NOT NULL,
                ind_enviado BOOLEAN DEFAULT False NOT NULL,
                CONSTRAINT cerh_notificacao_emails_pk PRIMARY KEY (ide_cerh_notificacao_email)
);
COMMENT ON COLUMN public.cerh_notificacao_emails.dsc_email IS 'Email para o qual a notificação será enviada';
COMMENT ON COLUMN public.cerh_notificacao_emails.dsc_assunto IS 'Texto constante no assunto do email';
COMMENT ON COLUMN public.cerh_notificacao_emails.dtc_envio IS 'Data em que o usuário do sistema comandou o envio no sistema';


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



CREATE SEQUENCE public.cerh_codigo_receita_seq;

CREATE TABLE public.cerh_codigo_receita (
                ide_cerh_codigo_receita INTEGER NOT NULL DEFAULT nextval('public.cerh_codigo_receita_seq'),
                num_codigo_receita INTEGER NOT NULL,
                dsc_codigo_receita VARCHAR(500) NOT NULL,
                CONSTRAINT cerh_codigo_receita_pk PRIMARY KEY (ide_cerh_codigo_receita)
);
COMMENT ON COLUMN public.cerh_codigo_receita.ide_cerh_codigo_receita IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_codigo_receita.num_codigo_receita IS 'numero do código de receita a ser utilizado para gerar o DAE';
COMMENT ON COLUMN public.cerh_codigo_receita.dsc_codigo_receita IS 'descrição sobre uso do código de receita';


ALTER SEQUENCE public.cerh_codigo_receita_seq OWNED BY public.cerh_codigo_receita.ide_cerh_codigo_receita;



CREATE SEQUENCE public.cerh_cod_receita_tipo_uso_seq;

CREATE TABLE public.cerh_cod_receita_tipo_uso (
                ide_cerh_cod_receita_tipo_uso INTEGER NOT NULL DEFAULT nextval('public.cerh_cod_receita_tipo_uso_seq'),
                ide_cerh_codigo_receita INTEGER NOT NULL,
                ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
                CONSTRAINT cerh_cod_receita_tipo_uso_pk PRIMARY KEY (ide_cerh_cod_receita_tipo_uso)
);
COMMENT ON COLUMN public.cerh_cod_receita_tipo_uso.ide_cerh_cod_receita_tipo_uso IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_cod_receita_tipo_uso.ide_cerh_codigo_receita IS 'chave estrangeira para a tabela codigo_receita';
COMMENT ON COLUMN public.cerh_cod_receita_tipo_uso.ide_tipo_uso_recurso_hidrico IS 'chave estrangeira para a tabela tipo_uso_recurso_hídrico';


CREATE SEQUENCE public.cerh_hist_notificacao_seq;

CREATE TABLE public.cerh_hist_envio_notificacao (
                ide_cerh_hist_notificacao INTEGER NOT NULL DEFAULT nextval('public.cerh_hist_notificacao_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                ide_usuario_agua INTEGER,
                ide_empreendimento INTEGER,
                dt_envio TIMESTAMP NOT NULL,
                ide_usuario_envio INTEGER NOT NULL,
                vlr_quantidade INTEGER NOT NULL,
                CONSTRAINT cerh_hist_envio_notificacao_pk PRIMARY KEY (ide_cerh_hist_notificacao)
);
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_cerh_hist_notificacao IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_usuario_agua IS 'Chave estrangeira para a tabela CERH com o campo Requerente que representa uma pessoa física ou jurídica';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_empreendimento IS 'Chave estrangeira para a tabela Empreendimento indicando o empreendimento selecionado no filtro, se houver, e que recebeu a notificação';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.dt_envio IS 'Data e hora do envio da notificação';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.ide_usuario_envio IS 'Chave estrangeira para a tabela pessoa_fisica indicando quem enviou a notificação.';
COMMENT ON COLUMN public.cerh_hist_envio_notificacao.vlr_quantidade IS 'Quantidade de notificações enviadas no envio';


CREATE SEQUENCE public.cerh_arquivo_baixa_dae_seq;

CREATE TABLE public.cerh_arquivo_baixa_dae (
                ide_cerh_arquivo_baixa_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_arquivo_baixa_dae_seq'),
                ide_usuario INTEGER NOT NULL,
                nom_arquivo VARCHAR(150) NOT NULL,
                dt_inicio_periodo_pagamento DATE NOT NULL,
                dt_fim_periodo_pagamento DATE NOT NULL,
                dsc_processamento VARCHAR(500) NOT NULL,
                dt_processamento DATE NOT NULL,
                ind_processado boolean,
                CONSTRAINT cerh_arquivo_baixa_dae_pk PRIMARY KEY (ide_cerh_arquivo_baixa_dae)
);
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.ide_cerh_arquivo_baixa_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.ide_usuario IS 'Chave estrangeira para a tabela usuario indica o autor da ação';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.nom_arquivo IS 'Nome do arquivo utilizado';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.dt_inicio_periodo_pagamento IS 'Data do início do período de pagamento considerado no arquivo';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.dt_fim_periodo_pagamento IS 'Data de fim do período de pagamento considerado
no arquivo';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.dsc_processamento IS 'Texto descrevendo o resultado do
processamento do arquivo (sucesso e quantidade de dae''s processados ou erro e descrição do erro)';
COMMENT ON COLUMN public.cerh_arquivo_baixa_dae.dt_processamento IS 'Data em que o arquivo foi processado';




/**
 * é conflitando com o script_entrega1.3
 *  */


-- ALTER TABLE public.cerh ADD COLUMN ide_cerh_pai INTEGER;
ALTER TABLE public.cerh ADD COLUMN dtc_alteracao_hist TIMESTAMP;
ALTER TABLE public.cerh ADD COLUMN ide_usuario_alteracao_hist INTEGER;

ALTER TABLE public.cerh ADD CONSTRAINT ide_cerh_pai_fk
FOREIGN KEY (ide_cerh_pai)
REFERENCES public.cerh (ide_cerh)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
/*
ALTER TABLE public.cerh ADD CONSTRAINT ide_cerh_pai_fk
FOREIGN KEY (ide_cerh_pai)
REFERENCES public.cerh (ide_cerh)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
*/
ALTER TABLE public.cerh ADD FOREIGN KEY (ide_cerh_pai) REFERENCES public.cerh (ide_cerh) 
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
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



CREATE SEQUENCE public.cerh_cobranca_ide_cerh_cobranca_seq;

CREATE TABLE public.cerh_cobranca (
                ide_cerh_cobranca INTEGER NOT NULL DEFAULT nextval('public.cerh_cobranca_ide_cerh_cobranca_seq'),
                ide_cerh INTEGER NOT NULL,
                num_ano_cobranca INTEGER NOT NULL,
                vlr_original_cobranca NUMERIC(17,2) NOT NULL,
                ind_parcelado BOOLEAN DEFAULT FALSE NOT NULL,
                num_max_parcelas INTEGER NOT NULL,
                CONSTRAINT cerh_cobranca_pk PRIMARY KEY (ide_cerh_cobranca)
);
COMMENT ON COLUMN public.cerh_cobranca.ide_cerh_cobranca IS 'chave primaria da tabela';
COMMENT ON COLUMN public.cerh_cobranca.ide_cerh IS 'Chave estrangeira para a tabela cerh';
COMMENT ON COLUMN public.cerh_cobranca.num_ano_cobranca IS 'número do ano base de cobrança';
COMMENT ON COLUMN public.cerh_cobranca.vlr_original_cobranca IS 'valor original da cobrança, sem
considerar multas por atraso ou envio dos correios';
COMMENT ON COLUMN public.cerh_cobranca.ind_parcelado IS 'indica a opção de
pagamento
0- parcela unica -
gera um único DAE
1 parcelado -
permite dividir o
pagamento em
mais de um DAE';
COMMENT ON COLUMN public.cerh_cobranca.num_max_parcelas IS 'identifica o número total de parcelas em
que o pagamento será feito. Varia de acordo com o valor devido e não pode ser maior que 10 Mesmo pagamento em parcela única, pode apresentar mais de uma parcela.';


CREATE SEQUENCE public.cerh_parcelas_cobranca_seq;

CREATE TABLE public.cerh_parcelas_cobranca (
                ide_cerh_parcelas_cobranca INTEGER NOT NULL DEFAULT nextval('public.cerh_parcelas_cobranca_seq'),
                ide_cerh_cobranca INTEGER,
                num_parcela INTEGER NOT NULL,
                vlr_original_parcela NUMERIC(17,2) NOT NULL,
                CONSTRAINT cerh_parcelas_cobranca_pk PRIMARY KEY (ide_cerh_parcelas_cobranca)
);
COMMENT ON COLUMN public.cerh_parcelas_cobranca.ide_cerh_parcelas_cobranca IS 'chave primária da
tabela';
COMMENT ON COLUMN public.cerh_parcelas_cobranca.ide_cerh_cobranca IS 'chave estrangeira para da tabela cerh_cobrança';
COMMENT ON COLUMN public.cerh_parcelas_cobranca.num_parcela IS 'número da parcela';
COMMENT ON COLUMN public.cerh_parcelas_cobranca.vlr_original_parcela IS 'valor original da parcela, sem considerar multa e juros por atraso ou envio pelos correios';



CREATE SEQUENCE public.cerh_dae_seq;

CREATE TABLE public.cerh_dae (
                ide_cerh_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_dae_seq'),
                ide_cerh_dae_pai INTEGER,
                ide_cerh_dae_loc_geografica INTEGER NOT NULL,
                ide_usuario INTEGER NOT NULL,
                ide_cerh_cod_receita_tipo_uso INTEGER NOT NULL,
                dsc_nosso_numero VARCHAR(10) NOT NULL,
                dt_emissao DATE NOT NULL,
                dt_vencimento DATE NOT NULL,
                vlr_principal NUMERIC(17,2) NOT NULL,
                vlr_acrescimo NUMERIC(17,2) NOT NULL,
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
                ind_envio_correios BOOLEAN DEFAULT FALSE NOT NULL,
                CONSTRAINT cerh_dae_pk PRIMARY KEY (ide_cerh_dae)
);
COMMENT ON COLUMN public.cerh_dae.ide_cerh_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_dae.ide_cerh_dae_pai IS 'FK para a própria tabela, indicando que o DAE é uma segunda via gerada a partir de outro DAE
já vencido';
COMMENT ON COLUMN public.cerh_dae.ide_cerh_dae_loc_geografica IS 'Chave estrangeira da tabela de localizacao geografica';
COMMENT ON COLUMN public.cerh_dae.ide_usuario IS 'Chave estrangeira para a tabela usuario indica o autor da ação';
COMMENT ON COLUMN public.cerh_dae.ide_cerh_cod_receita_tipo_uso IS 'Chave estrangeira da tabela cerh_cod_receita_tipo_uso';
COMMENT ON COLUMN public.cerh_dae.dsc_nosso_numero IS 'Nosso Número - identificador único dos DAEs gerado pela SEFAZ';
COMMENT ON COLUMN public.cerh_dae.dt_emissao IS 'Data de emissão do DAE';
COMMENT ON COLUMN public.cerh_dae.dt_vencimento IS 'Data de vencimento do DAE';
COMMENT ON COLUMN public.cerh_dae.vlr_principal IS 'Valor principal a ser cobrado no DAE sem
considerar multas e juros por atraso/envio pelos correios';
COMMENT ON COLUMN public.cerh_dae.vlr_acrescimo IS 'Valor de multas e juros a ser somado ao valor Principal';
COMMENT ON COLUMN public.cerh_dae.cod_documento_origem IS 'tipo de documento de origem utilizado
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
COMMENT ON COLUMN public.cerh_dae.num_documento_origem IS 'Número do documento de origem necessário para
criação do DAE';
COMMENT ON COLUMN public.cerh_dae.cod_referencia IS 'Tipo de referência utilizado no DAE
1 – Mês / Ano de Referência
2 – Parcela / Total de Parcelas
3 – Ano Exercício necessário para criação do DAE';
COMMENT ON COLUMN public.cerh_dae.num_mes_referencia IS 'Nês da referência utilizada para gerar o DAE necessário para criar o DAE';
COMMENT ON COLUMN public.cerh_dae.num_ano_referencia IS 'Ano da referência utilizada para gerar o DAE necessário para criação do DAE';
COMMENT ON COLUMN public.cerh_dae.num_parcela_referencia IS 'Numero da atual parcela, quando este é utilizado como referência para gerar o DAE necessário para criação do DAE';
COMMENT ON COLUMN public.cerh_dae.num_total_parcela_referencia IS 'Numero to total de parcelas do parcelamento, quando este é utilizado como referência para gerar o DAE';
COMMENT ON COLUMN public.cerh_dae.url_dae IS 'url para acessar o DAE gerado';
COMMENT ON COLUMN public.cerh_dae.cod_barras IS 'Código de barras do DAE gerado';
COMMENT ON COLUMN public.cerh_dae.des_aviso IS 'Informações enviadas pela SEFAZ referente á futuras mudanças no serviço de Emissão de DAE';
COMMENT ON COLUMN public.cerh_dae.dsc_informacoes_complementares IS 'Texto que pode ser incluído no DAE no
campo "informações Complementares." Será composto pelo Nº do cadastro, pelas parcelas que compõem o DAE, podendo ser mais de uma, e o tipo de uso considerado no DAE';
COMMENT ON COLUMN public.cerh_dae.ind_envio_correios IS 'Indicador se o DAE deve ser enviado pelos Correios';



CREATE SEQUENCE public.cerh_pagamento_dae_seq;

CREATE TABLE public.cerh_pagamento_dae (
                ide_cerh_pagamento_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_pagamento_dae_seq'),
                ide_cerh_dae INTEGER NOT NULL,
                ide_cerh_arquivo_baixa_dae INTEGER NOT NULL,
                dt_pagamento DATE NOT NULL,
                vlr_pagamento NUMERIC(17,2) NOT NULL,
                CONSTRAINT cerh_pagamento_dae_pk PRIMARY KEY (ide_cerh_pagamento_dae)
);
COMMENT ON COLUMN public.cerh_pagamento_dae.ide_cerh_pagamento_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_pagamento_dae.ide_cerh_dae IS 'Chave estrangeira da tabela cerh_dae';
COMMENT ON COLUMN public.cerh_pagamento_dae.ide_cerh_arquivo_baixa_dae IS 'Chave da tabela cerh_arquivo_baixa_dae';
COMMENT ON COLUMN public.cerh_pagamento_dae.dt_pagamento IS 'Data do pagamento, obtido no arquivo de baixa do DAE';
COMMENT ON COLUMN public.cerh_pagamento_dae.vlr_pagamento IS 'Valor que foi pago';



CREATE SEQUENCE public.cerh_hist_situacao_dae_seq;

CREATE TABLE public.cerh_hist_situacao_dae (
                ide_cerh_hist_situacao_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_hist_situacao_dae_seq'),
                ide_cerh_dae INTEGER NOT NULL,
                ide_usuario INTEGER,
                ide_cerh_situacao_dae INTEGER NOT NULL,
                dt_alteracao TIMESTAMP NOT NULL,
                CONSTRAINT cerh_hist_situacao_dae_pk PRIMARY KEY (ide_cerh_hist_situacao_dae)
);
COMMENT ON COLUMN public.cerh_hist_situacao_dae.ide_cerh_hist_situacao_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_hist_situacao_dae.ide_cerh_dae IS 'Chave estrangeira para a tabela DAE';
COMMENT ON COLUMN public.cerh_hist_situacao_dae.ide_usuario IS 'Chave estrangeira para a tabela usuario indica o autor da ação. 
Quando nulo, indica que a ação foi feita
pelo sistema (exemplo: dae vencido)';
COMMENT ON COLUMN public.cerh_hist_situacao_dae.ide_cerh_situacao_dae IS 'Chave estrangeira para a tabela situacao DAE';
COMMENT ON COLUMN public.cerh_hist_situacao_dae.dt_alteracao IS 'Indica a data em que a situação do DAE foi alterada';



CREATE TABLE public.cerh_situacao_dae (
                ide_cerh_situacao_dae INTEGER NOT NULL,
                dsc_situacao_dae VARCHAR(100) NOT NULL,
                CONSTRAINT cerh_situacao_dae_pk PRIMARY KEY (ide_cerh_situacao_dae)
);
COMMENT ON COLUMN public.cerh_situacao_dae.ide_cerh_situacao_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_situacao_dae.dsc_situacao_dae IS 'Descritivo das situações do DAE';



CREATE SEQUENCE public.cerh_parcelas_dae_seq;

CREATE TABLE public.cerh_parcelas_dae (
                ide_cerh_parcelas_dae INTEGER NOT NULL DEFAULT nextval('public.cerh_parcelas_dae_seq'),
                ide_cerh_parcelas_cobranca INTEGER NOT NULL,
                ide_cerh_dae INTEGER NOT NULL,
                CONSTRAINT cerh_parcelas_dae_pk PRIMARY KEY (ide_cerh_parcelas_dae)
);
COMMENT ON COLUMN public.cerh_parcelas_dae.ide_cerh_parcelas_dae IS 'Chave primária da tabela';
COMMENT ON COLUMN public.cerh_parcelas_dae.ide_cerh_parcelas_cobranca IS 'Chave estrangeira da tabela cerh_parcelas_dae';
COMMENT ON COLUMN public.cerh_parcelas_dae.ide_cerh_dae IS 'Chave estrangeira da tabela cerh_dae';

CREATE SEQUENCE public.cerh_dae_loc_geografica_seq;

CREATE TABLE public.cerh_dae_loc_geografica (
                ide_cerh_dae_loc_geografica INTEGER NOT NULL DEFAULT nextval('public.cerh_dae_loc_geografica_seq'),
                ide_cerh_localizacao_geografica INTEGER NOT NULL,
                CONSTRAINT cerh_dae_loc_geografica_pk PRIMARY KEY (ide_cerh_dae_loc_geografica)
);

ALTER TABLE public.cerh_hist_envio_notificacao ADD CONSTRAINT empreendimento_cerh_hist_envio_notificacao_fk
FOREIGN KEY (ide_empreendimento)
REFERENCES public.empreendimento (ide_empreendimento)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_classe_corpo_hidrico ADD CONSTRAINT new_tablecerh_classe_corpo_hidrico_cerh_pond_classe_corpo_hi954
FOREIGN KEY (ide_cerh_classe_corpo_hidrico)
REFERENCES public.cerh_classe_corpo_hidrico (ide_cerh_classe_corpo_hidrico)
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

ALTER TABLE public.cerh_cod_receita_tipo_uso ADD CONSTRAINT tipo_uso_recurso_hidrico_cerh_cod_receita_tipo_uso_fk
FOREIGN KEY (ide_tipo_uso_recurso_hidrico)
REFERENCES public.tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_localizacao_geografica ADD CONSTRAINT geo_rpga_cerh_localizacao_geografica_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
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

ALTER TABLE public.cerh_dae_loc_geografica ADD CONSTRAINT cerh_localizacao_geografica_cerh_dae_loc_geografica_fk
FOREIGN KEY (ide_cerh_localizacao_geografica)
REFERENCES public.cerh_localizacao_geografica (ide_cerh_localizacao_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae ADD CONSTRAINT cerh_dae_loc_geografica_cerh_dae_fk
FOREIGN KEY (ide_cerh_dae_loc_geografica)
REFERENCES public.cerh_dae_loc_geografica (ide_cerh_dae_loc_geografica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_cod_receita_tipo_uso ADD CONSTRAINT cerh_codigo_receita_cerh_cod_receita_tipo_uso_fk
FOREIGN KEY (ide_cerh_codigo_receita)
REFERENCES public.cerh_codigo_receita (ide_cerh_codigo_receita)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae ADD CONSTRAINT cerh_cod_receita_tipo_uso_cerh_dae_fk
FOREIGN KEY (ide_cerh_cod_receita_tipo_uso)
REFERENCES public.cerh_cod_receita_tipo_uso (ide_cerh_cod_receita_tipo_uso)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_comunicacao ADD CONSTRAINT usuario_cerh_hist_comunicacao_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae ADD CONSTRAINT usuario_cerh_dae_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_situacao_dae ADD CONSTRAINT usuario_cerh_hist_situacao_dae_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_arquivo_baixa_dae ADD CONSTRAINT usuario_cerh_arquivo_baixa_dae_fk
FOREIGN KEY (ide_usuario)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_envio_notificacao ADD CONSTRAINT usuario_cerh_hist_envio_notificacao_fk
FOREIGN KEY (ide_usuario_envio)
REFERENCES public.usuario (ide_pessoa_fisica)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pagamento_dae ADD CONSTRAINT cerh_arquivo_baixa_dae_cerh_pagamento_dae_fk
FOREIGN KEY (ide_cerh_arquivo_baixa_dae)
REFERENCES public.cerh_arquivo_baixa_dae (ide_cerh_arquivo_baixa_dae)
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

ALTER TABLE public.cerh_parcelas_dae ADD CONSTRAINT cerh_parcelas_cobranca_cerh_parcelas_dae_fk
FOREIGN KEY (ide_cerh_parcelas_cobranca)
REFERENCES public.cerh_parcelas_cobranca (ide_cerh_parcelas_cobranca)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_dae ADD CONSTRAINT cerh_dae_cerh_dae_fk
FOREIGN KEY (ide_cerh_dae_pai)
REFERENCES public.cerh_dae (ide_cerh_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_parcelas_dae ADD CONSTRAINT cerh_dae_cerh_parcelas_dae_fk
FOREIGN KEY (ide_cerh_dae)
REFERENCES public.cerh_dae (ide_cerh_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_situacao_dae ADD CONSTRAINT cerh_dae_cerh_hist_situacao_dae_fk
FOREIGN KEY (ide_cerh_dae)
REFERENCES public.cerh_dae (ide_cerh_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pagamento_dae ADD CONSTRAINT cerh_dae_cerh_pagamento_dae_fk
FOREIGN KEY (ide_cerh_dae)
REFERENCES public.cerh_dae (ide_cerh_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_hist_situacao_dae ADD CONSTRAINT cerh_hist_situacao_dae_cerh_situacao_dae_fk
FOREIGN KEY (ide_cerh_situacao_dae)
REFERENCES public.cerh_situacao_dae (ide_cerh_situacao_dae)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.cerh_pond_clas_corpo_hidrico_seq;

CREATE TABLE public.cerh_pond_clas_corpo_hidrico (
                ide_cerh_pon_cla_corpo_hidrico INTEGER NOT NULL DEFAULT nextval('public.cerh_pond_clas_corpo_hidrico_seq'),
                ide_geo_rpga INTEGER NOT NULL,
                ide_tipo_uso_recurso_hidrico INTEGER NOT NULL,
                ide_cerh_classe_corpo_hidrico INTEGER NOT NULL,
                vlr_referencia NUMERIC(17,2) NOT NULL,
                ind_consumo BOOLEAN DEFAULT False NOT NULL,
                dt_cadastro TIMESTAMP NOT NULL,
                ind_excluido BOOLEAN DEFAULT False NOT NULL,
                dt_excluido TIMESTAMP,
                
                CONSTRAINT ide_cerh_pon_cla_corpo_hidrico_pk PRIMARY KEY (ide_cerh_pon_cla_corpo_hidrico)
);

ALTER TABLE public.cerh_pond_clas_corpo_hidrico ADD CONSTRAINT cerh_pond_clas_corpo_hidrico_geo_rpga_fk
FOREIGN KEY (ide_geo_rpga)
REFERENCES public.geo_rpga (gid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_clas_corpo_hidrico ADD CONSTRAINT cerh_pond_clas_corpo_hidrico__fk
FOREIGN KEY (ide_tipo_uso_recurso_hidrico)
REFERENCES public.tipo_uso_recurso_hidrico (ide_tipo_uso_recurso_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cerh_pond_clas_corpo_hidrico ADD CONSTRAINT cerh_pond_clas_corpo_hidrico_classe_corpo_hidrico_fk
FOREIGN KEY (ide_cerh_classe_corpo_hidrico)
REFERENCES public.cerh_classe_corpo_hidrico (ide_cerh_classe_corpo_hidrico)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
    



/*
 * */
alter table tipo_uso_recurso_hidrico add column ide_cerh_codigo_receita integer ; 
