package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="pesquisa_mineral_responsavel_tecnico")
@NamedQueries({
	@NamedQuery(name = "PesquisaMineralResponsavelTecnico.removeByIdePesquisaMineral", query = "DELETE FROM PesquisaMineralResponsavelTecnico pmrt WHERE pmrt.pesquisaMineral.idePesquisaMineral = :idePesquisaMineral"),
	@NamedQuery(name = "PesquisaMineralResponsavelTecnico.removeByIde", query = "DELETE FROM PesquisaMineralResponsavelTecnico pmrt  WHERE pmrt.idePesquisaMineralResponsavelTecnico = :idePesquisaMineralResponsavelTecnico")
})
public class PesquisaMineralResponsavelTecnico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PESQUISA_MINERAL_RESPONSAVEL_TECNICO_GENERATOR", sequenceName = "PESQUISA_MINERAL_RESPONSAVEL_TECNICO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESQUISA_MINERAL_RESPONSAVEL_TECNICO_GENERATOR")
	@Column(name="ide_pesquisa_mineral_responsavel_tecnico")
	private Integer idePesquisaMineralResponsavelTecnico;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_responsavel_tecnico", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisicaResponsavelTecnico;

	//bi-directional many-to-one association to PesquisaMineral
	@ManyToOne
	@JoinColumn(name="ide_pesquisa_mineral")
	private PesquisaMineral pesquisaMineral;
	
	//bi-directional many-to-one association to PesquisaMineral
	@ManyToOne
	@JoinColumn(name="ide_formacao_profissional")
	private FormacaoProfissional formacaoProfissional;

	@Transient
	private List<FormacaoProfissional> listaFormacaoProfissional;

	@Transient
	private String numeroCarteiraConselho;

	public PesquisaMineralResponsavelTecnico() {
	}

	/**
	 * @param pesquisaMineral
	 * @param responsavelTecncio
	 */
	public PesquisaMineralResponsavelTecnico(PesquisaMineral pesquisaMineral, ResponsavelEmpreendimento responsavelTecncio) {
		this.pesquisaMineral = pesquisaMineral;
		this.idePessoaFisicaResponsavelTecnico = responsavelTecncio.getIdePessoaFisica();
	}

	public Integer getIdePesquisaMineralResponsavelTecnico() {
		return this.idePesquisaMineralResponsavelTecnico;
	}

	public void setIdePesquisaMineralResponsavelTecnico(Integer idePesquisaMineralResponsavelTecnico) {
		this.idePesquisaMineralResponsavelTecnico = idePesquisaMineralResponsavelTecnico;
	}

	public PessoaFisica getIdePessoaFisicaResponsavelTecnico() {
		return this.idePessoaFisicaResponsavelTecnico;
	}

	public void setIdePessoaFisicaResponsavelTecnico(PessoaFisica idePessoaFisicaResponsavelTecnico) {
		this.idePessoaFisicaResponsavelTecnico = idePessoaFisicaResponsavelTecnico;
	}

	public PesquisaMineral getPesquisaMineral() {
		return this.pesquisaMineral;
	}

	public void setPesquisaMineral(PesquisaMineral pesquisaMineral) {
		this.pesquisaMineral = pesquisaMineral;
	}

	public FormacaoProfissional getFormacaoProfissional() {
		return formacaoProfissional;
	}

	public void setFormacaoProfissional(FormacaoProfissional formacaoProfissional) {
		this.formacaoProfissional = formacaoProfissional;
	}

	public String getNumeroCarteiraConselho() {
		return numeroCarteiraConselho;
	}

	public void setNumeroCarteiraConselho(String numeroCarteiraConselho) {
		this.numeroCarteiraConselho = numeroCarteiraConselho;
	}

	public boolean isExisteCarteiraConselho() {
		return !Util.isNullOuVazio(this.numeroCarteiraConselho) && !this.numeroCarteiraConselho.equals("Não informado");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PesquisaMineralResponsavelTecnico [ide =" + idePesquisaMineralResponsavelTecnico + "]";
	}

	public List<FormacaoProfissional> getListaFormacaoProfissional() {
		return listaFormacaoProfissional;
	}

	public void setListaFormacaoProfissional(List<FormacaoProfissional> listaFormacaoProfissional) {
		this.listaFormacaoProfissional = listaFormacaoProfissional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idePessoaFisicaResponsavelTecnico == null) ? 0
						: idePessoaFisicaResponsavelTecnico.hashCode());
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
		PesquisaMineralResponsavelTecnico other = (PesquisaMineralResponsavelTecnico) obj;
		if (idePessoaFisicaResponsavelTecnico == null) {
			if (other.idePessoaFisicaResponsavelTecnico != null)
				return false;
		} else if (!idePessoaFisicaResponsavelTecnico
				.equals(other.idePessoaFisicaResponsavelTecnico))
			return false;
		return true;
	}
	
}