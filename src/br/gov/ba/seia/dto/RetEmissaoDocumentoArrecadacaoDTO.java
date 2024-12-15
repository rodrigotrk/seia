/**
 * 
 */
package br.gov.ba.seia.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lesantos
 *
 */
@XmlRootElement(name = "ret_emissao_documento_arrecadacao", namespace = "http://www.sefaz.ba.gov.br/warr")
public class RetEmissaoDocumentoArrecadacaoDTO extends EmissaoDocumentoArrecadacaoBaseDTO{

	private static final long serialVersionUID = 1L;
	
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

	@XmlElement(name = "emissao_documento_arrecadacao")
	public EmissaoDocumentoArrecadacaoRespDTO getEmissao_documento_arrecadacao() {
		return emissao_documento_arrecadacao;
	}

	public void setEmissao_documento_arrecadacao(EmissaoDocumentoArrecadacaoRespDTO emissao_documento_arrecadacao) {
		this.emissao_documento_arrecadacao = emissao_documento_arrecadacao;
	}
	
	

}
