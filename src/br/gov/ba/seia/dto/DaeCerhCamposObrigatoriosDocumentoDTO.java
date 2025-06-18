/**
 * 
 */
package br.gov.ba.seia.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author lesantos
 *
 */
@XmlRootElement(name = "ent_campos_obrigatorios_documento")
@XmlType(propOrder = {})
public class DaeCerhCamposObrigatoriosDocumentoDTO {

	
	private Integer ambiente;
	
	private String versao;

	private Integer receita;

	/**
	 * @return the ambiente
	 */
	@XmlElement(name = "tpAmp", type = TipoAmbiente.class)
	public Integer getAmbiente() {
		return ambiente;
	}

	/**
	 * @param ambiente the ambiente to set
	 */
	public void setAmbiente(Integer ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * @return the versao
	 */
	@XmlElement(name = "verAplic")
	public String getVersao() {
		return versao;
	}

	/**
	 * @param versao the versao to set
	 */
	public void setVersao(String versao) {
		this.versao = versao;
	}

	/**
	 * @return the receita
	 */
	@XmlElement(name = "cod_receita")
	public Integer getReceita() {
		return receita;
	}

	/**
	 * @param receita the receita to set
	 */
	public void setReceita(Integer receita) {
		this.receita = receita;
	}

	@XmlType(name = "TipoAmbiente")
	public static class TipoAmbiente{
		
		private String value;

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}
}
