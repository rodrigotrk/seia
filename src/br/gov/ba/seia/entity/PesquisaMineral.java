package br.gov.ba.seia.entity;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * The persistent class for the pesquisa_mineral database table.
 * 
 */
@Entity
@Table(name="pesquisa_mineral")
@NamedQuery(name="PesquisaMineral.findAll", query="SELECT p FROM PesquisaMineral p")
public class PesquisaMineral extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PESQUISA_MINERAL_GENERATOR", sequenceName = "PESQUISA_MINERAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESQUISA_MINERAL_GENERATOR")
	@Column(name="ide_pesquisa_mineral")
	private Integer idePesquisaMineral;

	@Column(name="ind_utiliza_agua")
	private Boolean indUtilizaAgua;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLic
	@ManyToOne
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic")
	private CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic;

	//bi-directional many-to-one association to PesquisaMineralProcessoDnpm
	@OneToMany(mappedBy = "idePesquisaMineral", fetch = FetchType.LAZY)
	private List<ProcessoDnpm> processoDnpms;

	//bi-directional many-to-one association to PesquisaMineralResponsavelTecnico
	@OneToMany(mappedBy = "pesquisaMineral", fetch = FetchType.LAZY)
	private List<PesquisaMineralResponsavelTecnico> pesquisaMineralResponsavelTecnicos;

	//bi-directional many-to-one association to PesquisaMineralSubstanciaMineral
	@OneToMany(mappedBy = "pesquisaMineral", fetch = FetchType.LAZY)
	private List<PesquisaMineralSubstanciaMineral> pesquisaMineralSubstanciaMinerals;

	//bi-directional many-to-one association to PesquisaMineralUsoDaAgua
	@OneToMany(mappedBy = "pesquisaMineral", fetch = FetchType.LAZY)
	private List<PesquisaMineralUsoDaAgua> pesquisaMineralUsoDaAguas;

	@Transient
	private List<PesquisaMineralUsoDaAgua> pesquisaMineralUsoDaAguaToSelected;
	
	public PesquisaMineral() {
	}

	public PesquisaMineral(CadastroAtividadeNaoSujeitaLic cadastroAtividade) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividade;
	}

	public Integer getIdePesquisaMineral() {
		return this.idePesquisaMineral;
	}

	public void setIdePesquisaMineral(Integer idePesquisaMineral) {
		this.idePesquisaMineral = idePesquisaMineral;
	}

	public Boolean getIndUtilizaAgua() {
		return this.indUtilizaAgua;
	}

	public void setIndUtilizaAgua(Boolean indUtilizaAgua) {
		this.indUtilizaAgua = indUtilizaAgua;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastroAtividadeNaoSujeitaLic() {
		return this.cadastroAtividadeNaoSujeitaLic;
	}

	public void setCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividadeNaoSujeitaLic;
	}

	public List<ProcessoDnpm> getProcessoDnpms() {
		return this.processoDnpms;
	}

	public void setProcessoDnpms(List<ProcessoDnpm> processoDnpms) {
		this.processoDnpms = processoDnpms;
	}

	public ProcessoDnpm addProcessoDnpm(ProcessoDnpm processoDnpm) {
		getProcessoDnpms().add(processoDnpm);
		processoDnpm.setIdePesquisaMineral(this);

		return processoDnpm;
	}

	public ProcessoDnpm removeProcessoDnpm(ProcessoDnpm processoDnpm) {
		getProcessoDnpms().remove(processoDnpm);
		processoDnpm.setIdePesquisaMineral(null);

		return processoDnpm;
	}

	public List<PesquisaMineralResponsavelTecnico> getPesquisaMineralResponsavelTecnicos() {
		return this.pesquisaMineralResponsavelTecnicos;
	}

	public void setPesquisaMineralResponsavelTecnicos(List<PesquisaMineralResponsavelTecnico> pesquisaMineralResponsavelTecnicos) {
		this.pesquisaMineralResponsavelTecnicos = pesquisaMineralResponsavelTecnicos;
	}

	public PesquisaMineralResponsavelTecnico addPesquisaMineralResponsavelTecnico(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico) {
		getPesquisaMineralResponsavelTecnicos().add(pesquisaMineralResponsavelTecnico);
		pesquisaMineralResponsavelTecnico.setPesquisaMineral(this);

		return pesquisaMineralResponsavelTecnico;
	}

	public PesquisaMineralResponsavelTecnico removePesquisaMineralResponsavelTecnico(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico) {
		getPesquisaMineralResponsavelTecnicos().remove(pesquisaMineralResponsavelTecnico);
		pesquisaMineralResponsavelTecnico.setPesquisaMineral(null);

		return pesquisaMineralResponsavelTecnico;
	}

	public List<PesquisaMineralSubstanciaMineral> getPesquisaMineralSubstanciaMinerals() {
		return this.pesquisaMineralSubstanciaMinerals;
	}

	public void setPesquisaMineralSubstanciaMinerals(List<PesquisaMineralSubstanciaMineral> pesquisaMineralSubstanciaMinerals) {
		this.pesquisaMineralSubstanciaMinerals = pesquisaMineralSubstanciaMinerals;
	}

	public PesquisaMineralSubstanciaMineral addPesquisaMineralSubstanciaMineral(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral) {
		getPesquisaMineralSubstanciaMinerals().add(pesquisaMineralSubstanciaMineral);
		pesquisaMineralSubstanciaMineral.setPesquisaMineral(this);

		return pesquisaMineralSubstanciaMineral;
	}

	public PesquisaMineralSubstanciaMineral removePesquisaMineralSubstanciaMineral(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral) {
		getPesquisaMineralSubstanciaMinerals().remove(pesquisaMineralSubstanciaMineral);
		pesquisaMineralSubstanciaMineral.setPesquisaMineral(null);

		return pesquisaMineralSubstanciaMineral;
	}

	public List<PesquisaMineralUsoDaAgua> getPesquisaMineralUsoDaAguas() {
		return this.pesquisaMineralUsoDaAguas;
	}

	public void setPesquisaMineralUsoDaAguas(List<PesquisaMineralUsoDaAgua> pesquisaMineralUsoDaAguas) {
		this.pesquisaMineralUsoDaAguas = pesquisaMineralUsoDaAguas;
	}

	public PesquisaMineralUsoDaAgua addPesquisaMineralUsoDaAgua(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua) {
		getPesquisaMineralUsoDaAguas().add(pesquisaMineralUsoDaAgua);
		pesquisaMineralUsoDaAgua.setPesquisaMineral(this);
		pesquisaMineralUsoDaAgua.setTipoCaptacao(pesquisaMineralUsoDaAgua.getTipoCaptacao());
		return pesquisaMineralUsoDaAgua;
	}

	public PesquisaMineralUsoDaAgua removePesquisaMineralUsoDaAgua(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua) {
		getPesquisaMineralUsoDaAguas().remove(pesquisaMineralUsoDaAgua);
		pesquisaMineralUsoDaAgua.setPesquisaMineral(null);

		return pesquisaMineralUsoDaAgua;
	}
	
	public void montarListaPesquisaMineralUsoAguaToSelected(List<TipoCaptacao> lista){
		pesquisaMineralUsoDaAguaToSelected = new ArrayList<PesquisaMineralUsoDaAgua>();
		for(TipoCaptacao captacao : lista){
			pesquisaMineralUsoDaAguaToSelected.add(new PesquisaMineralUsoDaAgua(this, captacao));
		}
	}

	public List<PesquisaMineralUsoDaAgua> getPesquisaMineralUsoDaAguaToSelected() {
		return pesquisaMineralUsoDaAguaToSelected;
	}
	
	public void setPesquisaMineralUsoDaAguaToSelected(List<PesquisaMineralUsoDaAgua> listaPesquisaMineralUsoDaAgua) {
		this.pesquisaMineralUsoDaAguaToSelected = listaPesquisaMineralUsoDaAgua;
	}
}