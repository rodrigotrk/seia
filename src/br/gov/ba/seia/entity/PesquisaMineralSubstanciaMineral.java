package br.gov.ba.seia.entity;

import java.io.Serializable;

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


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="pesquisa_mineral_substancia_mineral")
@NamedQueries({
		@NamedQuery(name = "PesquisaMineralSubstanciaMineral.removeByIdePesquisaMineral", query = "DELETE FROM PesquisaMineralSubstanciaMineral pmsm WHERE pmsm.pesquisaMineral.idePesquisaMineral = :idePesquisaMineral"),
		@NamedQuery(name = "PesquisaMineralSubstanciaMineral.removeByIde", query = "DELETE FROM PesquisaMineralSubstanciaMineral pmsm WHERE pmsm.idePesquisaMineralSubstanciaMineral = :idePesquisaMineralSubstanciaMineral")
})
public class PesquisaMineralSubstanciaMineral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PESQUISA_MINERAL_SUBSTANCIA_MINERAL_GENERATOR", sequenceName = "PESQUISA_MINERAL_SUBSTANCIA_MINERAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESQUISA_MINERAL_SUBSTANCIA_MINERAL_GENERATOR")
	@Column(name="ide_pesquisa_mineral_substancia_mineral")
	private Integer idePesquisaMineralSubstanciaMineral;

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_substancia_mineral", referencedColumnName = "ide_substancia_mineral")
	private SubstanciaMineral ideSubstanciaMineral;

	//bi-directional many-to-one association to PesquisaMineral
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pesquisa_mineral")
	private PesquisaMineral pesquisaMineral;

	public PesquisaMineralSubstanciaMineral() {
	}

	public PesquisaMineralSubstanciaMineral(PesquisaMineral pesquisaMineral, SubstanciaMineral ideSubstanciaMineral) {
		this.pesquisaMineral = pesquisaMineral;
		this.ideSubstanciaMineral = ideSubstanciaMineral;
	}

	public Integer getIdePesquisaMineralSubstanciaMineral() {
		return this.idePesquisaMineralSubstanciaMineral;
	}

	public void setIdePesquisaMineralSubstanciaMineral(Integer idePesquisaMineralSubstanciaMineral) {
		this.idePesquisaMineralSubstanciaMineral = idePesquisaMineralSubstanciaMineral;
	}

	public SubstanciaMineral getIdeSubstanciaMineral() {
		return this.ideSubstanciaMineral;
	}

	public void setIdeSubstanciaMineral(SubstanciaMineral ideSubstanciaMineral) {
		this.ideSubstanciaMineral = ideSubstanciaMineral;
	}

	public PesquisaMineral getPesquisaMineral() {
		return this.pesquisaMineral;
	}

	public void setPesquisaMineral(PesquisaMineral pesquisaMineral) {
		this.pesquisaMineral = pesquisaMineral;
	}

}