package br.gov.ba.seia.entity;

import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "processo_dnpm")
@NamedQueries({
	@NamedQuery(name = "ProcessoDnpm.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM ProcessoDnpm dnpm WHERE dnpm.ideFceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral"),
	@NamedQuery(name = "ProcessoDnpm.removeByIdeFcePesquisaMineral", query = "DELETE FROM ProcessoDnpm dnpm WHERE dnpm.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral"),
	@NamedQuery(name = "ProcessoDnpm.removeByideProcessoDnpm", query = "DELETE FROM ProcessoDnpm dnpm WHERE dnpm.ideProcessoDnpm = :ideProcessoDnpm")

})
public class ProcessoDnpm extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "PROCESSO_DNPM_IDEPROCESSODNPM_GENERATOR", sequenceName = "PROCESSO_DNPM_IDE_PROCESSO_DNPM_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_DNPM_IDEPROCESSODNPM_GENERATOR")
	@Column(name = "ide_processo_dnpm")
	private Integer ideProcessoDnpm;

	@Column(name = "area_processo_dnpm")
	private BigDecimal areaProcessoDnpm;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Pattern(regexp="[0-9]{6}/[0-9]{4}", message = "Digite um número de processo no formato: xxxxxx/xxxx")
	@Column(name = "num_processo_dnpm")
	private String numProcessoDnpm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", referencedColumnName = "ide_fce_licenciamento_mineral", nullable = true)
	private FceLicenciamentoMineral ideFceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", referencedColumnName = "ide_fce_pesquisa_mineral", nullable = true)
	private FcePesquisaMineral ideFcePesquisaMineral;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pesquisa_mineral", referencedColumnName = "ide_pesquisa_mineral", nullable = true)
	private PesquisaMineral idePesquisaMineral;

	//bi-directional many-to-one association to ProcessoDnpmProspecao
	@OneToMany(mappedBy = "processoDnpm", fetch = FetchType.LAZY)
	private List<ProcessoDnpmProspecao> listaProcessoDnpmProspecao;
	
	@Transient
	private List<ProcessoDnpmProspecao> listaProcessoDnpmProspecaoToSelect;

	public ProcessoDnpm() {

	}

	public ProcessoDnpm(FceLicenciamentoMineral ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public ProcessoDnpm(FceLicenciamentoMineral ideFceLicenciamentoMineral, LocalizacaoGeografica localizacaoGeografica) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
		this.ideLocalizacaoGeografica = localizacaoGeografica;
	}

	public ProcessoDnpm(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public ProcessoDnpm(PesquisaMineral pesquisaMineral) {
		this.idePesquisaMineral = pesquisaMineral;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeProcessoDnpm() {
		return this.ideProcessoDnpm;
	}

	public void setIdeProcessoDnpm(Integer ideProcessoDnpm) {
		this.ideProcessoDnpm = ideProcessoDnpm;
	}

	public BigDecimal getAreaProcessoDnpm() {
		return this.areaProcessoDnpm;
	}

	public void setAreaProcessoDnpm(BigDecimal areaProcessoDnpm) {
		this.areaProcessoDnpm = areaProcessoDnpm;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return this.ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getNumProcessoDnpm() {
		return this.numProcessoDnpm;
	}

	public void setNumProcessoDnpm(String numProcessoDnpm) {
		this.numProcessoDnpm = numProcessoDnpm;
	}

	public FceLicenciamentoMineral getIdeFceLicenciamentoMineral() {
		return ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(	FceLicenciamentoMineral ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}
	
	 /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideProcessoDnpm == null) ? 0 : ideProcessoDnpm.hashCode());
		result = prime * result + ((numProcessoDnpm == null) ? 0 : numProcessoDnpm.hashCode());
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
		ProcessoDnpm other = (ProcessoDnpm) obj;
		if (ideProcessoDnpm == null) {
			if (other.ideProcessoDnpm != null)
				return false;
		}
		else if (!ideProcessoDnpm.equals(other.ideProcessoDnpm))
			return false;
		if (numProcessoDnpm == null) {
			if (other.numProcessoDnpm != null)
				return false;
		}
		else if (!numProcessoDnpm.equals(other.numProcessoDnpm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		if(ideProcessoDnpm==null){
			return numProcessoDnpm;
		}
		
		return ideProcessoDnpm.toString();
	}

	public PesquisaMineral getIdePesquisaMineral() {
		return idePesquisaMineral;
	}

	public void setIdePesquisaMineral(PesquisaMineral idePesquisaMineral) {
		this.idePesquisaMineral = idePesquisaMineral;
	}

	public List<ProcessoDnpmProspecao> getListaProcessoDnpmProspecao() {
		System.identityHashCode(listaProcessoDnpmProspecao);
		return listaProcessoDnpmProspecao;
	}

	public void setListaProcessoDnpmProspecao(List<ProcessoDnpmProspecao> listaProcessoDnpmProspecao) {
		this.listaProcessoDnpmProspecao = listaProcessoDnpmProspecao;
	}

	public List<ProcessoDnpmProspecao> getListaProcessoDnpmProspecaoToSelect() {
		return listaProcessoDnpmProspecaoToSelect;
	}

	public void setListaProcessoDnpmProspecaoToSelect(List<ProcessoDnpmProspecao> listaProcessoDnpmProspecaoToSelect) {
		this.listaProcessoDnpmProspecaoToSelect = listaProcessoDnpmProspecaoToSelect;
	}

	public boolean isExisteMetodoProspeccaoFuroSondagem(){
		return !Util.isNullOuVazio(listaProcessoDnpmProspecao)
				&& listaProcessoDnpmProspecao.contains(new ProcessoDnpmProspecao(MetodoProspeccaoEnum.SONDAGENS));
	}

	public boolean isExisteMetodoProspeccaoTrincheiras(){
		return !Util.isNullOuVazio(listaProcessoDnpmProspecao) 
				&& listaProcessoDnpmProspecao.contains(new ProcessoDnpmProspecao(MetodoProspeccaoEnum.TRINCHEIRAS));
	}

	public boolean isExisteMetodoProspeccaoPocos(){
		return !Util.isNullOuVazio(listaProcessoDnpmProspecao) 
				&& listaProcessoDnpmProspecao.contains(new ProcessoDnpmProspecao(MetodoProspeccaoEnum.POCOS));
	}

	public ProcessoDnpmProspecao getProcessoDnpmProspecao(MetodoProspeccaoEnum metodoProspeccaoEnum) {
		for (ProcessoDnpmProspecao dnpmProspecao : listaProcessoDnpmProspecao) {
			if (dnpmProspecao.getIdeMetodoProspeccao().equals(new MetodoProspeccao(metodoProspeccaoEnum))) {
				return dnpmProspecao;
			}
		}
		return null;
	}

	public boolean isExisteListaProspeccaoDetalhamentoSondagem(ProcessoDnpm processoDnpm) {
		return !Util.isNullOuVazio(getListaProspeccaoDetalhamentoSondagem(processoDnpm));
	}

	public boolean isExisteListaProspeccaoDetalhamentoTrincheira(ProcessoDnpm processoDnpm) {
		return !Util.isNullOuVazio(getListaProspeccaoDetalhamentoTrincheira(processoDnpm));
	}

	public boolean isExisteListaProspeccaoDetalhamentoPoco(ProcessoDnpm processoDnpm) {
		return !Util.isNullOuVazio(getListaProspeccaoDetalhamentoPoco(processoDnpm));
	}

	public List<ProspecaoDetalhamento> getListaProspeccaoDetalhamentoSondagem(ProcessoDnpm processoDnpm) {
		return processoDnpm.getProcessoDnpmProspecao(MetodoProspeccaoEnum.SONDAGENS).getProspecaoDetalhamentos();
	}

	public List<ProspecaoDetalhamento> getListaProspeccaoDetalhamentoTrincheira(ProcessoDnpm processoDnpm) {
		return processoDnpm.getProcessoDnpmProspecao(MetodoProspeccaoEnum.TRINCHEIRAS).getProspecaoDetalhamentos();
	}

	public List<ProspecaoDetalhamento> getListaProspeccaoDetalhamentoPoco(ProcessoDnpm processoDnpm) {
		return processoDnpm.getProcessoDnpmProspecao(MetodoProspeccaoEnum.POCOS).getProspecaoDetalhamentos();
	}
	
}