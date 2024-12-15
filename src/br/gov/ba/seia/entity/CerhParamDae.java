/**
 * 
 */
package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_param_dae")
public class CerhParamDae {
	
	@Id
	@Column( name = "ide_cerh_param_dae")
	private Integer ideCerhParamDae;
	
	@Column(name = "dsc_usuario")
	private String dscUsuario;
	
	@Column(name = "dsc_senha")
	private String dscSenha;
	
	@Column(name = "dsc_versao_aplicacao")
	private String dscVersaoAplicacao;

	/**
	 * @return the ideCerhParamDae
	 */
	public Integer getIdeCerhParamDae() {
		return ideCerhParamDae;
	}

	/**
	 * @param ideCerhParamDae the ideCerhParamDae to set
	 */
	public void setIdeCerhParamDae(Integer ideCerhParamDae) {
		this.ideCerhParamDae = ideCerhParamDae;
	}

	/**
	 * @return the dscUsuario
	 */
	public String getDscUsuario() {
		return dscUsuario;
	}

	/**
	 * @param dscUsuario the dscUsuario to set
	 */
	public void setDscUsuario(String dscUsuario) {
		this.dscUsuario = dscUsuario;
	}

	/**
	 * @return the dscSenha
	 */
	public String getDscSenha() {
		return dscSenha;
	}

	/**
	 * @param dscSenha the dscSenha to set
	 */
	public void setDscSenha(String dscSenha) {
		this.dscSenha = dscSenha;
	}

	/**
	 * @return the dscVersaoAplicacao
	 */
	public String getDscVersaoAplicacao() {
		return dscVersaoAplicacao;
	}

	/**
	 * @param dscVersaoAplicacao the dscVersaoAplicacao to set
	 */
	public void setDscVersaoAplicacao(String dscVersaoAplicacao) {
		this.dscVersaoAplicacao = dscVersaoAplicacao;
	}
	
	
}
