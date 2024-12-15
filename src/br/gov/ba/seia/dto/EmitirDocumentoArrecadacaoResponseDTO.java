package br.gov.ba.seia.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lesantos
 *
 */
@XmlRootElement(name = "emitir_documento_arrecadacaoResponse")
public class EmitirDocumentoArrecadacaoResponseDTO implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	private EmitirDocumentoArrecadacaoResultDTO emitir_documento_arrecadacaoResult;
	
	public EmitirDocumentoArrecadacaoResultDTO getEmitir_documento_arrecadacaoResult() {
		return emitir_documento_arrecadacaoResult;
	}

	public void setEmitir_documento_arrecadacaoResult(EmitirDocumentoArrecadacaoResultDTO emitir_documento_arrecadacaoResult) {
		this.emitir_documento_arrecadacaoResult = emitir_documento_arrecadacaoResult;
	}

	@XmlRootElement(name = "emitir_documento_arrecadacaoResult")
	public static class EmitirDocumentoArrecadacaoResultDTO {
		
		private RetEmissaoDocumentoArrecadacaoDTO ret_emissao_documento_arrecadacao;

		public RetEmissaoDocumentoArrecadacaoDTO getRet_emissao_documento_arrecadacao() {
			return ret_emissao_documento_arrecadacao;
		}

		public void setRet_emissao_documento_arrecadacao(
				RetEmissaoDocumentoArrecadacaoDTO ret_emissao_documento_arrecadacao) {
			this.ret_emissao_documento_arrecadacao = ret_emissao_documento_arrecadacao;
		}
	}
	
	@XmlRootElement(name = "RetEmissaoDocumentoArrecadacao")
	public static class RetEmissaoDocumentoArrecadacaoDTO {
		
		private String tpAmb;

		private String verAplic;
		
		private String cStat;
		
		private String xMotivo;
		
		private EmissaoDocumentoArrecadacaoRespDTO emissao_documento_arrecadacao;

		public String getTpAmb() {
			return tpAmb;
		}

		public void setTpAmb(String tpAmb) {
			this.tpAmb = tpAmb;
		}

		public String getVerAplic() {
			return verAplic;
		}

		public void setVerAplic(String verAplic) {
			this.verAplic = verAplic;
		}

		public String getcStat() {
			return cStat;
		}

		public void setcStat(String cStat) {
			this.cStat = cStat;
		}

		public String getxMotivo() {
			return xMotivo;
		}

		public void setxMotivo(String xMotivo) {
			this.xMotivo = xMotivo;
		}

		public EmissaoDocumentoArrecadacaoRespDTO getEmissao_documento_arrecadacao() {
			return emissao_documento_arrecadacao;
		}

		public void setEmissao_documento_arrecadacao(EmissaoDocumentoArrecadacaoRespDTO emissao_documento_arrecadacao) {
			this.emissao_documento_arrecadacao = emissao_documento_arrecadacao;
		}
	}
}
