package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="processo_dnpm_prospecao")
@NamedQueries({
		@NamedQuery(name = "ProcessoDnpmProspecao.removeByIdeProcessoDnpm", query = "DELETE FROM ProcessoDnpmProspecao pdp WHERE pdp.processoDnpm.ideProcessoDnpm = :ideProcessoDnpm"),
		@NamedQuery(name = "ProcessoDnpmProspecao.removeByIde", query = "DELETE FROM ProcessoDnpmProspecao pdp WHERE pdp.ideProcessoDnpmProspecao = :ideProcessoDnpmProspecao")
})
public class ProcessoDnpmProspecao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROCESSO_DNPM_PROSPECAO_GENERATOR", sequenceName = "PROCESSO_DNPM_PROSPECAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_DNPM_PROSPECAO_GENERATOR")
	@Column(name="ide_processo_dnpm_prospecao")
	private Integer ideProcessoDnpmProspecao;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_prospeccao", referencedColumnName = "ide_metodo_prospeccao")
	private MetodoProspeccao ideMetodoProspeccao;

	//bi-directional many-to-one association to PesquisaMineralProcessoDnpm
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_processo_dnpm")
	private ProcessoDnpm processoDnpm;

	//bi-directional many-to-one association to ProspecaoDetalhamento
	@OneToMany(mappedBy = "processoDnpmProspecao")
	private List<ProspecaoDetalhamento> prospecaoDetalhamentos;

	public ProcessoDnpmProspecao() {
	}

	public ProcessoDnpmProspecao(MetodoProspeccaoEnum metodoProspeccaoEnum) {
		this.ideMetodoProspeccao = new MetodoProspeccao(metodoProspeccaoEnum);
	}
	
	public ProcessoDnpmProspecao(ProcessoDnpm processoDnpm, MetodoProspeccao metodoProspeccao) {
		this.processoDnpm = processoDnpm;
		this.ideMetodoProspeccao = metodoProspeccao;
		this.prospecaoDetalhamentos = new ArrayList<ProspecaoDetalhamento>();
	}

	public Integer getIdeProcessoDnpmProspecao() {
		return this.ideProcessoDnpmProspecao;
	}

	public void setIdeProcessoDnpmProspecao(Integer ideProcessoDnpmProspecao) {
		this.ideProcessoDnpmProspecao = ideProcessoDnpmProspecao;
	}

	public MetodoProspeccao getIdeMetodoProspeccao() {
		return this.ideMetodoProspeccao;
	}

	public void setIdeMetodoProspeccao(MetodoProspeccao ideMetodoProspeccao) {
		this.ideMetodoProspeccao = ideMetodoProspeccao;
	}

	public ProcessoDnpm getProcessoDnpm() {
		return this.processoDnpm;
	}

	public void setProcessoDnpm(ProcessoDnpm pesquisaMineralProcessoDnpm) {
		this.processoDnpm = pesquisaMineralProcessoDnpm;
	}

	public List<ProspecaoDetalhamento> getProspecaoDetalhamentos() {
		return this.prospecaoDetalhamentos;
	}

	public void setProspecaoDetalhamentos(List<ProspecaoDetalhamento> prospecaoDetalhamentos) {
		this.prospecaoDetalhamentos = prospecaoDetalhamentos;
	}

	public ProspecaoDetalhamento addProspecaoDetalhamento(ProspecaoDetalhamento prospecaoDetalhamento) {
		getProspecaoDetalhamentos().add(prospecaoDetalhamento);
		prospecaoDetalhamento.setProcessoDnpmProspecao(this);

		return prospecaoDetalhamento;
	}

	public ProspecaoDetalhamento removeProspecaoDetalhamento(ProspecaoDetalhamento prospecaoDetalhamento) {
		getProspecaoDetalhamentos().remove(prospecaoDetalhamento);
		prospecaoDetalhamento.setProcessoDnpmProspecao(null);

		return prospecaoDetalhamento;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideMetodoProspeccao == null) ? 0 : ideMetodoProspeccao.hashCode());
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
		ProcessoDnpmProspecao other = (ProcessoDnpmProspecao) obj;
		if (ideMetodoProspeccao == null) {
			if (other.ideMetodoProspeccao != null)
				return false;
		}
		else if (!ideMetodoProspeccao.equals(other.ideMetodoProspeccao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return ideMetodoProspeccao.getId();
	}

}