CREATE OR REPLACE FUNCTION atualizar_num_certificado_inconsistentes()
  RETURNS void AS
$BODY$
DECLARE

    REGISTRO RECORD;
    CONT integer;
    ANO_DATA_EMISSAO_ANTERIOR double precision;
    ANO_DATA_EMISSAO_REGISTRO_ATUAL double precision;
    
BEGIN
    CONT = 0;
    ANO_DATA_EMISSAO_ANTERIOR = 0;
    ANO_DATA_EMISSAO_REGISTRO_ATUAL = 0;
    
    FOR REGISTRO IN 
	select ide_certificado, dtc_emissao_certificado, num_certificado from certificado where ide_tipo_certificado = 7 
		order by dtc_emissao_certificado asc
    LOOP
	CONT = CONT + 1;

	--Verifica se ano mudou para zerar o contador do num_certificado
	if ANO_DATA_EMISSAO_ANTERIOR = 0 then
	   ANO_DATA_EMISSAO_ANTERIOR = date_part('year', REGISTRO.dtc_emissao_certificado);
	   ANO_DATA_EMISSAO_REGISTRO_ATUAL = ANO_DATA_EMISSAO_ANTERIOR;
        else
           ANO_DATA_EMISSAO_REGISTRO_ATUAL = date_part('year', REGISTRO.dtc_emissao_certificado);
        end if;
          
	if ANO_DATA_EMISSAO_ANTERIOR <> ANO_DATA_EMISSAO_REGISTRO_ATUAL then
	   ANO_DATA_EMISSAO_ANTERIOR = ANO_DATA_EMISSAO_REGISTRO_ATUAL;
	   CONT = 1;
	end if;
		
	UPDATE certificado SET num_certificado = substring(REGISTRO.num_certificado from 1 for 9) || LPAD(CAST(CONT AS VARCHAR), 6, '0') || '/CERH'
		WHERE ide_certificado = REGISTRO.ide_certificado;
	
    END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION atualizar_num_certificado_inconsistentes()
  OWNER TO postgres;



---



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



---
CREATE SEQUENCE tipo_certificado_cerh_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999999
  START 1
  CACHE 1;
ALTER TABLE tipo_certificado_cefir_seq
  OWNER TO seia_sema;
---

  
 ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD COLUMN val_trecho_vazao_reduzida NUMERIC(10,2) NULL;
  
    
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD COLUMN ide_cerh_localizacao_geografica_intervencao integer;
ALTER TABLE cerh_barragem_aproveitamento_hidreletrico
  ADD CONSTRAINT cerh_barragem_apr_hidreletico_cerh_loc_geo FOREIGN KEY (ide_cerh_localizacao_geografica_intervencao) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;

  ALTER TABLE cerh_intervencao_caracterizacao
  ADD COLUMN ide_cerh_localizacao_geografica_barragem integer;
ALTER TABLE cerh_intervencao_caracterizacao
  ADD CONSTRAINT cerh_intervencao_caracterizacao_ide_cerh_loc_geo_barragem FOREIGN KEY (ide_cerh_localizacao_geografica_barragem) REFERENCES cerh_localizacao_geografica (ide_cerh_localizacao_geografica) ON UPDATE NO ACTION ON DELETE NO ACTION;