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

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="formacao_profissional")
@NamedQuery(name="FormacaoProfissional.findAll", query="SELECT f FROM FormacaoProfissional f")
public class FormacaoProfissional implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FORMACAO_PROFISSIONAL_GENERATOR", sequenceName = "FORMACAO_PROFISSIONAL_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORMACAO_PROFISSIONAL_GENERATOR")
	@Column(name="ide_formacao_profissional")
	private Integer ideFormacaoProfissional;

	@Column(name="nom_formacao_profissional")
	private String nomFormacaoProfissional;

	//bi-directional many-to-one association to PesquisaMineralResponsavelTecnico
	@OneToMany(mappedBy="formacaoProfissional")
	private List<PesquisaMineralResponsavelTecnico> pesquisaMineralResponsavelTecnicos;

	public FormacaoProfissional() {
	}

	public Integer getIdeFormacaoProfissional() {
		return this.ideFormacaoProfissional;
	}

	public void setIdeFormacaoProfissional(Integer ideFormacaoProfissional) {
		this.ideFormacaoProfissional = ideFormacaoProfissional;
	}

	public String getNomFormacaoProfissional() {
		return this.nomFormacaoProfissional;
	}

	public void setNomFormacaoProfissional(String nomFormacaoProfissional) {
		this.nomFormacaoProfissional = nomFormacaoProfissional;
	}

	public List<PesquisaMineralResponsavelTecnico> getPesquisaMineralResponsavelTecnicos() {
		return this.pesquisaMineralResponsavelTecnicos;
	}

	public void setPesquisaMineralResponsavelTecnicos(List<PesquisaMineralResponsavelTecnico> pesquisaMineralResponsavelTecnicos) {
		this.pesquisaMineralResponsavelTecnicos = pesquisaMineralResponsavelTecnicos;
	}

	public PesquisaMineralResponsavelTecnico addPesquisaMineralResponsavelTecnico(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico) {
		getPesquisaMineralResponsavelTecnicos().add(pesquisaMineralResponsavelTecnico);
		pesquisaMineralResponsavelTecnico.setFormacaoProfissional(this);

		return pesquisaMineralResponsavelTecnico;
	}

	public PesquisaMineralResponsavelTecnico removePesquisaMineralResponsavelTecnico(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico) {
		getPesquisaMineralResponsavelTecnicos().remove(pesquisaMineralResponsavelTecnico);
		pesquisaMineralResponsavelTecnico.setFormacaoProfissional(null);

		return pesquisaMineralResponsavelTecnico;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideFormacaoProfissional);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFormacaoProfissional == null) ? 0 : ideFormacaoProfissional.hashCode());
		result = prime * result + ((nomFormacaoProfissional == null) ? 0 : nomFormacaoProfissional.hashCode());
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
		FormacaoProfissional other = (FormacaoProfissional) obj;
		if (ideFormacaoProfissional == null) {
			if (other.ideFormacaoProfissional != null)
				return false;
		}
		else if (!ideFormacaoProfissional.equals(other.ideFormacaoProfissional))
			return false;
		if (nomFormacaoProfissional == null) {
			if (other.nomFormacaoProfissional != null)
				return false;
		}
		else if (!nomFormacaoProfissional.equals(other.nomFormacaoProfissional))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FormacaoProfissional [" + nomFormacaoProfissional + "] / " + ideFormacaoProfissional;
	}

	public boolean isOutros(){
		return this.ideFormacaoProfissional.equals(5);
	}
	
}