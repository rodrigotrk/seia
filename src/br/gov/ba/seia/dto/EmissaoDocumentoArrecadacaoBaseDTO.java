package br.gov.ba.seia.dto;
import java.io.Serializable;

public abstract class EmissaoDocumentoArrecadacaoBaseDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4794406414750880282L;

	private String tpAmb;

	private String verAplic;

	private String cod_usuario;

	private String str_senha;

	private Integer cod_tipo_processamento;

	/**
	 * @return the tpAmb
	 */
	public String getTpAmb() {
		return tpAmb;
	}

	/**
	 * @param tpAmb the tpAmb to set
	 */
	public void setTpAmb(String tpAmb) {
		this.tpAmb = tpAmb;
	}

	/**
	 * @return the verAplic
	 */
	public String getVerAplic() {
		return verAplic;
	}

	/**
	 * @param verAplic the verAplic to set
	 */
	public void setVerAplic(String verAplic) {
		this.verAplic = verAplic;
	}

	/**
	 * @return the cod_usuario
	 */
	public String getCod_usuario() {
		return cod_usuario;
	}

	/**
	 * @param cod_usuario the cod_usuario to set
	 */
	public void setCod_usuario(String cod_usuario) {
		this.cod_usuario = cod_usuario;
	}

	/**
	 * @return the str_senha
	 */
	public String getStr_senha() {
		return str_senha;
	}

	/**
	 * @param str_senha the str_senha to set
	 */
	public void setStr_senha(String str_senha) {
		this.str_senha = str_senha;
	}

	/**
	 * @return the cod_tipo_processamento
	 */
	public Integer getCod_tipo_processamento() {
		return cod_tipo_processamento;
	}

	/**
	 * @param cod_tipo_processamento the cod_tipo_processamento to set
	 */
	public void setCod_tipo_processamento(Integer cod_tipo_processamento) {
		this.cod_tipo_processamento = cod_tipo_processamento;
	}

}
