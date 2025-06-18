package br.gov.ba.seia.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Classe sada para uso do XML de requisição.
 *
 */
@XmlRootElement(name = "emissao_documento_arrecadacao")
public class EmissaoDocumentoArrecadacaoReqDTO {

	private DocumentoArrecadacao documento_arrecadacao;

	@XmlRootElement(name = "documento_arrecadacao")
	@XmlType(name = "documento_arrecadacao", propOrder = {
	        "seq_documento_arrecadacao",
	        "cod_receita",
	        "cod_identificacao_emitente",
	        "num_identificacao_emitente",
	        "cod_tipo_documento_origem",
	        "num_documento_origem",
	        "dtc_vencimento",
	        "dtc_max_pagamento",
	        "referencia",
	        "val_principal",
	        "val_acrescimo",
	        "nom_razao_social",
	        "des_tipo_logradouro",
	        "des_endereco",
	        "des_bairro",
	        "num_cep",
	        "cod_municipio_ibge",
	        "tipo_dae",
	        "cod_unidade_orcamentaria_origem",
	        "cod_unidade_gestora_origem",
	        "cod_unidade_orcamentaria_destino",
	        "cod_unidade_gestora_destino",
	        "cod_tipo_retorno_documento",
	        "sts_retornar_barra"
	    })
	public static class DocumentoArrecadacao {

		private Integer seq_documento_arrecadacao;

		private Integer cod_receita;

		private Integer cod_identificacao_emitente;

		private NumIdentificacaoEmitente num_identificacao_emitente;

		private Integer cod_tipo_documento_origem;

		private String num_documento_origem;

		//AAAA-MM-dd
		private String dtc_vencimento;

		//AAAA-MM-dd
		private String dtc_max_pagamento;

		private Referencia referencia;

		private double val_principal;

		private double val_acrescimo;

		private String nom_razao_social;

		private String des_tipo_logradouro;

		private String des_endereco;

		private String des_bairro;

		private String num_cep;

		private String cod_municipio_ibge;

		private Integer tipo_dae;

		private Integer cod_unidade_orcamentaria_origem;

		private Integer cod_unidade_gestora_origem;

		private Integer cod_unidade_orcamentaria_destino;

		private Integer cod_unidade_gestora_destino;

		private Integer cod_tipo_retorno_documento;

		private Integer sts_retornar_barra;

		public Integer getSeq_documento_arrecadacao() {
			return seq_documento_arrecadacao;
		}

		public void setSeq_documento_arrecadacao(Integer seq_documento_arrecadacao) {
			this.seq_documento_arrecadacao = seq_documento_arrecadacao;
		}

		public Integer getCod_receita() {
			return cod_receita;
		}

		public void setCod_receita(Integer cod_receita) {
			this.cod_receita = cod_receita;
		}

		public Integer getCod_identificacao_emitente() {
			return cod_identificacao_emitente;
		}

		public void setCod_identificacao_emitente(Integer cod_identificacao_emitente) {
			this.cod_identificacao_emitente = cod_identificacao_emitente;
		}

		public NumIdentificacaoEmitente getNum_identificacao_emitente() {
			return num_identificacao_emitente;
		}

		public void setNum_identificacao_emitente(NumIdentificacaoEmitente num_identificacao_emitente) {
			this.num_identificacao_emitente = num_identificacao_emitente;
		}

		public Integer getCod_tipo_documento_origem() {
			return cod_tipo_documento_origem;
		}

		public void setCod_tipo_documento_origem(Integer cod_tipo_documento_origem) {
			this.cod_tipo_documento_origem = cod_tipo_documento_origem;
		}

		public String getNum_documento_origem() {
			return num_documento_origem;
		}

		public void setNum_documento_origem(String num_documento_origem) {
			this.num_documento_origem = num_documento_origem;
		}

		public String getDtc_vencimento() {
			return dtc_vencimento;
		}

		public void setDtc_vencimento(String dtc_vencimento) {
			this.dtc_vencimento = dtc_vencimento;
		}

		public String getDtc_max_pagamento() {
			return dtc_max_pagamento;
		}

		public void setDtc_max_pagamento(String dtc_max_pagamento) {
			this.dtc_max_pagamento = dtc_max_pagamento;
		}

		public Referencia getReferencia() {
			return referencia;
		}

		public void setReferencia(Referencia referencia) {
			this.referencia = referencia;
		}

		public double getVal_principal() {
			return val_principal;
		}

		public void setVal_principal(double val_principal) {
			this.val_principal = val_principal;
		}

		public double getVal_acrescimo() {
			return val_acrescimo;
		}

		public void setVal_acrescimo(double val_acrescimo) {
			this.val_acrescimo = val_acrescimo;
		}

		public String getNom_razao_social() {
			return nom_razao_social;
		}

		public void setNom_razao_social(String nom_razao_social) {
			this.nom_razao_social = nom_razao_social;
		}

		public String getDes_tipo_logradouro() {
			return des_tipo_logradouro;
		}

		public void setDes_tipo_logradouro(String des_tipo_logradouro) {
			this.des_tipo_logradouro = des_tipo_logradouro;
		}

		public String getDes_endereco() {
			return des_endereco;
		}

		public void setDes_endereco(String des_endereco) {
			this.des_endereco = des_endereco;
		}

		public String getDes_bairro() {
			return des_bairro;
		}

		public void setDes_bairro(String des_bairro) {
			this.des_bairro = des_bairro;
		}

		public String getNum_cep() {
			return num_cep;
		}

		public void setNum_cep(String num_cep) {
			this.num_cep = num_cep;
		}

		public String getCod_municipio_ibge() {
			return cod_municipio_ibge;
		}

		public void setCod_municipio_ibge(String cod_municipio_ibge) {
			this.cod_municipio_ibge = cod_municipio_ibge;
		}

		public Integer getTipo_dae() {
			return tipo_dae;
		}

		public void setTipo_dae(Integer tipo_dae) {
			this.tipo_dae = tipo_dae;
		}

		public Integer getCod_unidade_orcamentaria_origem() {
			return cod_unidade_orcamentaria_origem;
		}

		public void setCod_unidade_orcamentaria_origem(
				Integer cod_unidade_orcamentaria_origem) {
			this.cod_unidade_orcamentaria_origem = cod_unidade_orcamentaria_origem;
		}

		public Integer getCod_unidade_gestora_origem() {
			return cod_unidade_gestora_origem;
		}

		public void setCod_unidade_gestora_origem(Integer cod_unidade_gestora_origem) {
			this.cod_unidade_gestora_origem = cod_unidade_gestora_origem;
		}

		public Integer getCod_unidade_orcamentaria_destino() {
			return cod_unidade_orcamentaria_destino;
		}

		public void setCod_unidade_orcamentaria_destino(Integer cod_unidade_orcamentaria_destino) {
			this.cod_unidade_orcamentaria_destino = cod_unidade_orcamentaria_destino;
		}

		public Integer getCod_unidade_gestora_destino() {
			return cod_unidade_gestora_destino;
		}

		public void setCod_unidade_gestora_destino(Integer cod_unidade_gestora_destino) {
			this.cod_unidade_gestora_destino = cod_unidade_gestora_destino;
		}

		public Integer getCod_tipo_retorno_documento() {
			return cod_tipo_retorno_documento;
		}

		public void setCod_tipo_retorno_documento(Integer cod_tipo_retorno_documento) {
			this.cod_tipo_retorno_documento = cod_tipo_retorno_documento;
		}

		public Integer getSts_retornar_barra() {
			return sts_retornar_barra;
		}

		public void setSts_retornar_barra(Integer sts_retornar_barra) {
			this.sts_retornar_barra = sts_retornar_barra;
		}

		@XmlRootElement(name = "num_identificacao_emitente")
		@XmlType(name = "num_identificacao_emitente", propOrder = {
		        "num_cnpj_cpf"
		    })
		public static class NumIdentificacaoEmitente {

			private String num_cnpj_cpf;

			public NumIdentificacaoEmitente() {

			}

			public NumIdentificacaoEmitente(String num_cnpj_cpf) {
				this.num_cnpj_cpf = num_cnpj_cpf;
			}

			/**
			 * @return the num_cnpj_cpf
			 */
			public String getNum_cnpj_cpf() {
				return num_cnpj_cpf;
			}

			/**
			 * @param num_cnpj_cpf
			 *            the num_cnpj_cpf to set
			 */
			public void setNum_cnpj_cpf(String num_cnpj_cpf) {
				this.num_cnpj_cpf = num_cnpj_cpf;
			}

		}

		@XmlRootElement(name = "referencia")
		@XmlType(name = "referencia", propOrder = {
		        "cod_tipo_referencia",
		        "num_mes",
		        "num_ano"
		    })
		public static class Referencia {
			private int cod_tipo_referencia;

			private String num_mes;

			private int num_ano;

			public Referencia() {

			}

			public Referencia(int cod_tipo_referencia, int num_mes, int num_ano) {
				this.cod_tipo_referencia = cod_tipo_referencia;
				if(num_mes < 10){
					this.num_mes = "0" + num_mes;
				}else{
					this.num_mes = "" + num_mes;
				}
				this.num_ano = num_ano;
			}

			/**
			 * @return the cod_tipo_referencia
			 */
			public int getCod_tipo_referencia() {
				return cod_tipo_referencia;
			}

			/**
			 * @param cod_tipo_referencia
			 *            the cod_tipo_referencia to set
			 */
			public void setCod_tipo_referencia(int cod_tipo_referencia) {
				this.cod_tipo_referencia = cod_tipo_referencia;
			}

			/**
			 * @return the num_mes
			 */
			public String getNum_mes() {
				return num_mes;
			}

			/**
			 * @param num_mes
			 *            the num_mes to set
			 */
			public void setNum_mes(String num_mes) {
				this.num_mes = num_mes;
			}

			/**
			 * @return the num_ano
			 */
			public int getNum_ano() {
				return num_ano;
			}

			/**
			 * @param num_ano
			 *            the num_ano to set
			 */
			public void setNum_ano(int num_ano) {
				this.num_ano = num_ano;
			}

		}
	}

	/**
	 * @return the documento_arrecadacao
	 */
	public DocumentoArrecadacao getDocumento_arrecadacao() {
		return documento_arrecadacao;
	}

	/**
	 * @param documento_arrecadacao the documento_arrecadacao to set
	 */
	public void setDocumento_arrecadacao(DocumentoArrecadacao documento_arrecadacao) {
		this.documento_arrecadacao = documento_arrecadacao;
	}

}
