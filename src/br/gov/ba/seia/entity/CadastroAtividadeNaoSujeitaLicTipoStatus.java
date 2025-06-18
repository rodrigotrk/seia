package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="cadastro_atividade_nao_sujeita_lic_tipo_status")
@NamedQuery(name="CadastroAtividadeNaoSujeitaLicTipoStatus.findAll", query="SELECT c FROM CadastroAtividadeNaoSujeitaLicTipoStatus c")
public class CadastroAtividadeNaoSujeitaLicTipoStatus implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_STATUS_GENERATOR", sequenceName = "cadastro_atividade_nao_sujeita_lic_tipo_status_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_STATUS_GENERATOR")
	@Column(name="ide_cadastro_atividade_nao_sujeita_lic_tipo_status")
	private Integer ideCadastroAtividadeNaoSujeitaLicTipoStatus;

	@Column(name="nom_tipo_status")
	private String nomTipoStatus;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLicStatus
	@OneToMany(mappedBy="cadastroAtividadeNaoSujeitaLicTipoStatus")
	private List<CadastroAtividadeNaoSujeitaLicStatus> cadastroAtividadeNaoSujeitaLicStatuses;

	public CadastroAtividadeNaoSujeitaLicTipoStatus() {
	}

	public CadastroAtividadeNaoSujeitaLicTipoStatus(CadastroAtividadeNaoSujeitaLicTipoStatusEnum cadastroEnum) {
		this.ideCadastroAtividadeNaoSujeitaLicTipoStatus = cadastroEnum.getIde();
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLicTipoStatus() {
		return this.ideCadastroAtividadeNaoSujeitaLicTipoStatus;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLicTipoStatus(Integer ideCadastroAtividadeNaoSujeitaLicTipoStatus) {
		this.ideCadastroAtividadeNaoSujeitaLicTipoStatus = ideCadastroAtividadeNaoSujeitaLicTipoStatus;
	}

	public String getNomTipoStatus() {
		return this.nomTipoStatus;
	}

	public void setNomTipoStatus(String nomTipoStatus) {
		this.nomTipoStatus = nomTipoStatus;
	}

	public List<CadastroAtividadeNaoSujeitaLicStatus> getCadastroAtividadeNaoSujeitaLicStatuses() {
		return this.cadastroAtividadeNaoSujeitaLicStatuses;
	}

	public void setCadastroAtividadeNaoSujeitaLicStatuses(List<CadastroAtividadeNaoSujeitaLicStatus> cadastroAtividadeNaoSujeitaLicStatuses) {
		this.cadastroAtividadeNaoSujeitaLicStatuses = cadastroAtividadeNaoSujeitaLicStatuses;
	}

	public CadastroAtividadeNaoSujeitaLicStatus addCadastroAtividadeNaoSujeitaLicStatus(CadastroAtividadeNaoSujeitaLicStatus cadastroAtividadeNaoSujeitaLicStatus) {
		getCadastroAtividadeNaoSujeitaLicStatuses().add(cadastroAtividadeNaoSujeitaLicStatus);
		cadastroAtividadeNaoSujeitaLicStatus.setCadastroAtividadeNaoSujeitaLicTipoStatus(this);

		return cadastroAtividadeNaoSujeitaLicStatus;
	}

	public CadastroAtividadeNaoSujeitaLicStatus removeCadastroAtividadeNaoSujeitaLicStatus(CadastroAtividadeNaoSujeitaLicStatus cadastroAtividadeNaoSujeitaLicStatus) {
		getCadastroAtividadeNaoSujeitaLicStatuses().remove(cadastroAtividadeNaoSujeitaLicStatus);
		cadastroAtividadeNaoSujeitaLicStatus.setCadastroAtividadeNaoSujeitaLicTipoStatus(null);

		return cadastroAtividadeNaoSujeitaLicStatus;
	}

	@Override
	public Long getId() {
		return new Long(this.ideCadastroAtividadeNaoSujeitaLicTipoStatus);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCadastroAtividadeNaoSujeitaLicTipoStatus == null) ? 0 : ideCadastroAtividadeNaoSujeitaLicTipoStatus.hashCode());
		result = prime * result + ((nomTipoStatus == null) ? 0 : nomTipoStatus.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CadastroAtividadeNaoSujeitaLicTipoStatus other = (CadastroAtividadeNaoSujeitaLicTipoStatus) obj;
		if (ideCadastroAtividadeNaoSujeitaLicTipoStatus == null) {
			if (other.ideCadastroAtividadeNaoSujeitaLicTipoStatus != null)
				return false;
		}
		else if (!ideCadastroAtividadeNaoSujeitaLicTipoStatus.equals(other.ideCadastroAtividadeNaoSujeitaLicTipoStatus))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CadastroAtividadeNaoSujeitaLicTipoStatus [" + ideCadastroAtividadeNaoSujeitaLicTipoStatus + "]";
	}

}