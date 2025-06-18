/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 4 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CadastroAtividadeNaoSujeitaLicImovel.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 4 de out de 2017
 * Objetivo: 	
	
 */

@Entity
@Table(name="cadastro_atividade_nao_sujeita_lic_imovel")
public class CadastroAtividadeNaoSujeitaLicImovel implements Serializable{

	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_IMOVEL_GENERATOR", sequenceName = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_IMOVEL_IDE_CADASTRO_ATIVI572", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_IMOVEL_GENERATOR")
	@Column(name="ide_cadastro_atividade_nao_sujeita_lic_imovel", nullable = false)
	private Integer ideCadastroAtividadeNaoSujeitaLicImovel;
	
	/**
	 * Propriedade: ideCadastroAtividadeNaoSujeitaLic
	 * @type: CadastroAtividadeNaoSujeitaLic
	 */
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic", referencedColumnName = "ide_cadastro_atividade_nao_sujeita_lic", nullable = false)
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic;
	
	/**
	 * Propriedade: ideImovelRural
	 * @type: ImovelRural
	 */
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	private ImovelRural ideImovelRural;


	public CadastroAtividadeNaoSujeitaLicImovel() {
		// Auto-generated constructor stub
	}
	
	public CadastroAtividadeNaoSujeitaLicImovel(
			CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic,
			ImovelRural ideImovelRural) {
		super();
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
		this.ideImovelRural = ideImovelRural;
	}

	/**
	 * Getter do campo ideCadastroAtividadeNaoSujeitaLic
	 *	
	 * @return the ideCadastroAtividadeNaoSujeitaLic
	 */
	public CadastroAtividadeNaoSujeitaLic getIdeCadastroAtividadeNaoSujeitaLic() {
		return ideCadastroAtividadeNaoSujeitaLic;
	}

	/**
	 * Setter do campo  ideCadastroAtividadeNaoSujeitaLic
	 * @param ideCadastroAtividadeNaoSujeitaLic the ideCadastroAtividadeNaoSujeitaLic to set
	 */
	public void setIdeCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic) {
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
	}

	/**
	 * Getter do campo ideImovelRural
	 *	
	 * @return the ideImovelRural
	 */
	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	/**
	 * Setter do campo  ideImovelRural
	 * @param ideImovelRural the ideImovelRural to set
	 */
	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLicImovel() {
		return ideCadastroAtividadeNaoSujeitaLicImovel;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLicImovel(
			Integer ideCadastroAtividadeNaoSujeitaLicImovel) {
		this.ideCadastroAtividadeNaoSujeitaLicImovel = ideCadastroAtividadeNaoSujeitaLicImovel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCadastroAtividadeNaoSujeitaLic == null) ? 0
						: ideCadastroAtividadeNaoSujeitaLic.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CadastroAtividadeNaoSujeitaLicImovel other = (CadastroAtividadeNaoSujeitaLicImovel) obj;
		if (ideCadastroAtividadeNaoSujeitaLic == null) {
			if (other.ideCadastroAtividadeNaoSujeitaLic != null)
				return false;
		} else if (!ideCadastroAtividadeNaoSujeitaLic
				.equals(other.ideCadastroAtividadeNaoSujeitaLic))
			return false;
		return true;
	}
	
}
