package br.gov.ba.seia.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "emissao_documento_arrecadacao")
public class EmissaoDocumentoArrecadacaoRespDTO {
	
	private DocumentoArrecadacao documento_arrecadacao;
	
	public DocumentoArrecadacao getDocumento_arrecadacao() {
		return documento_arrecadacao;
	}

	public void setDocumento_arrecadacao(DocumentoArrecadacao documento_arrecadacao) {
		this.documento_arrecadacao = documento_arrecadacao;
	}

	@XmlRootElement(name = "documento_arrecadacao")
	public static class DocumentoArrecadacao {
		
		//AAAA-MM-dd
		private String dtc_operacao;
		
		private Integer seq_documento_arrecadacao;
		
		private short cod_retorno;
		
		private String des_mensagem;
		
		private Integer seq_dae_emitido;
		
		private String des_endereco_doc_arrec;
		
		private String des_codigo_barra;

		public String getDtc_operacao() {
			return dtc_operacao;
		}

		public void setDtc_operacao(String dtc_operacao) {
			this.dtc_operacao = dtc_operacao;
		}

		public Integer getSeq_documento_arrecadacao() {
			return seq_documento_arrecadacao;
		}

		public void setSeq_documento_arrecadacao(Integer seq_documento_arrecadacao) {
			this.seq_documento_arrecadacao = seq_documento_arrecadacao;
		}

		public short getCod_retorno() {
			return cod_retorno;
		}

		public void setCod_retorno(short cod_retorno) {
			this.cod_retorno = cod_retorno;
		}

		public String getDes_mensagem() {
			return des_mensagem;
		}

		public void setDes_mensagem(String des_mensagem) {
			this.des_mensagem = des_mensagem;
		}

		public Integer getSeq_dae_emitido() {
			return seq_dae_emitido;
		}

		public void setSeq_dae_emitido(Integer seq_dae_emitido) {
			this.seq_dae_emitido = seq_dae_emitido;
		}

		public String getDes_endereco_doc_arrec() {
			return des_endereco_doc_arrec;
		}

		public void setDes_endereco_doc_arrec(String des_endereco_doc_arrec) {
			this.des_endereco_doc_arrec = des_endereco_doc_arrec;
		}

		public String getDes_codigo_barra() {
			return des_codigo_barra;
		}

		public void setDes_codigo_barra(String des_codigo_barra) {
			this.des_codigo_barra = des_codigo_barra;
		}
		
	}
}