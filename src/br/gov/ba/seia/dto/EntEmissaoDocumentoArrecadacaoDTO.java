/**
 * 
 */
package br.gov.ba.seia.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author lesantos
 *
 */
@XmlRootElement(name = "ent_emissao_documento_arrecadacao", namespace = "http://www.sefaz.ba.gov.br/warr")
@XmlType(name = "ent_emissao_documento_arrecadacao", propOrder = {  
        "tpAmb",  
        "verAplic",  
        //"cod_usuario",  
        //"str_senha",
        "emissao_documento_arrecadacao",
        "cod_tipo_processamento"
    })
public class EntEmissaoDocumentoArrecadacaoDTO implements Serializable{

	private static final long serialVersionUID = -8310246213734949402L;
	
	private String tpAmb;

	private String verAplic;

	//private String cod_usuario;

	//private String str_senha;

	private EmissaoDocumentoArrecadacaoReqDTO emissao_documento_arrecadacao;
	
	private Integer cod_tipo_processamento;
	
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

	public Integer getCod_tipo_processamento() {
		return cod_tipo_processamento;
	}

	public void setCod_tipo_processamento(Integer cod_tipo_processamento) {
		this.cod_tipo_processamento = cod_tipo_processamento;
	}

	public EmissaoDocumentoArrecadacaoReqDTO getEmissao_documento_arrecadacao() {
		return emissao_documento_arrecadacao;
	}

	public void setEmissao_documento_arrecadacao(
			EmissaoDocumentoArrecadacaoReqDTO emissao_documento_arrecadacao) {
		this.emissao_documento_arrecadacao = emissao_documento_arrecadacao;
	}

}
