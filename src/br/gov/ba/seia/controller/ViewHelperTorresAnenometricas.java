package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoNaturezaTorre;
import br.gov.ba.seia.entity.TipoUnidadeConservacaoTorre;
import br.gov.ba.seia.entity.TorreAnemometrica;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: ViewHelperTorresAnenometricas.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.controller
 * @autor: diegoraian em 19 de set de 2017
 * Objetivo: 	
	
 */
/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: ViewHelperTorresAnenometricas.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.controller
 * @autor: diegoraian em 4 de out de 2017
 * Objetivo: 	
	
 */
public class ViewHelperTorresAnenometricas {
	
	
	protected int activeTab;
	/**
	 * Propriedade: torreAnenometrica
	 * @type: TorreAnenometrica
	 */
	protected TorreAnemometrica torreAnemometrica;
	

	/**
	 * Propriedade: cadastro
	 * @type: CadastroAtividadeNaoSujeitaLic
	 */
	protected CadastroAtividadeNaoSujeitaLic cadastro;
	
	/**
	 * Propriedade: empreendimentos
	 * @type: List<Empreendimento>
	 */
	private Collection<Empreendimento> empreendimentos;
	
	
	/**
	 * Propriedade: responsavelEmpreendimento
	 * @type: Collection<ResponsavelEmpreendimento>
	 */
	private Collection<ResponsavelEmpreendimento> responsavelEmpreendimento;
	
	
	/**
	 * Propriedade: tiposUnidadeConservacao
	 * @type: Collection<TipoUnidadeConservacaoTorre>
	 */
	protected Collection<TipoUnidadeConservacaoTorre> tiposUnidadeConservacao;
	/**
	 * Propriedade: itemsNaturezaTorre
	 * @type: Collection<TipoNaturezaTorre>
	 */
	protected Collection<TipoNaturezaTorre> itemsNaturezaTorre;
	
	/**
	 * Propriedade: localizacaoAtividadesTorres
	 * @type: Collection<LocalizacaoAtividadeTorre>
	 */
	protected Collection<LocalizacaoAtividadeTorre> localizacaoAtividadesTorres;
	
	/**
	 * Propriedade: empreendimento
	 * @type: Empreendimento
	 */
	private Empreendimento empreendimento;
	/**
	 * Propriedade: imoveisRurais
	 * @type: List<ImovelRural>
	 */
	private List<ImovelRural> imoveisRurais;

	/**
	 * Propriedade: torres
	 * @type: List<TorreAnemometrica>
	 */
	private List<TorreAnemometrica> torres;

	/**
	 * Propriedade: imovel
	 * @type: Imovel
	 */
	private Imovel imovel;
	
	
	
	/**
	 * Propriedade: numSicar
	 * @type: String
	 */
	private String numSicar;
	
	/**
	 * Construtor padrão
	 */
	public ViewHelperTorresAnenometricas() {
//		this.torreAnenometrica.getIdeCadastroAtividadeNaoSugeitaLic().setIdeEmpreendimento(new Empreendimento());
//		this.torreAnenometrica.getIdeCadastroAtividadeNaoSugeitaLic().setIdePessoaRequerente(new Pessoa());
		this.empreendimentos = new ArrayList<Empreendimento>();
		this.responsavelEmpreendimento = new ArrayList<ResponsavelEmpreendimento>(); 
		this.torres = new ArrayList<TorreAnemometrica>();
	}
	
	/**
	 * Getter do campo cadastro
	 *	
	 * @return the cadastro
	 */
	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}


	/**
	 * Setter do campo  cadastro
	 * @param cadastro the cadastro to set
	 */
	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	
	/**
	 * Getter do campo empreendimento
	 *	
	 * @return the empreendimento
	 */
	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}


	/**
	 * Setter do campo  empreendimento
	 * @param empreendimento the empreendimento to set
	 */
	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	/**
	 * Getter do campo torreAnenometrica
	 *	
	 * @return the torreAnenometrica
	 */
	public TorreAnemometrica getTorreAnemometrica() {
		return torreAnemometrica;
	}

	/**
	 * Setter do campo  torreAnemometrica
	 * @param torreAnenometrica the torreAnemometrica to set
	 */
	public void setTorreAnemmetrica(TorreAnemometrica torreAnemometrica) {
		this.torreAnemometrica = torreAnemometrica;
	}

	/**
	 * Getter do campo empreendimentos
	 *	
	 * @return the empreendimentos
	 */
	public Collection<Empreendimento> getEmpreendimentos() {
		return empreendimentos;
	}


	/**
	 * Setter do campo  empreendimentos
	 * @param empreendimentos the empreendimentos to set
	 */
	public void setEmpreendimentos(Collection<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}


	/**
	 * Getter do campo responsavelEmpreendimento
	 *	
	 * @return the responsavelEmpreendimento
	 */
	public Collection<ResponsavelEmpreendimento> getResponsavelEmpreendimento() {
		return responsavelEmpreendimento;
	}


	/**
	 * Setter do campo  responsavelEmpreendimento
	 * @param responsavelEmpreendimento the responsavelEmpreendimento to set
	 */
	public void setResponsavelEmpreendimento(Collection<ResponsavelEmpreendimento> responsavelEmpreendimento) {
		this.responsavelEmpreendimento = responsavelEmpreendimento;
	}


	/**
	 * Setter do campo  empreendimentos
	 * @param empreendimentos the empreendimentos to set
	 */
	public void setEmpreendimentos(List<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}

	/**
	 * Getter do campo imoveisRurais
	 *	
	 * @return the imoveisRurais
	 */
	public List<ImovelRural> getImoveisRurais() {
		return imoveisRurais;
	}

	/**
	 * Setter do campo  imoveisRurais
	 * @param imoveisRurais the imoveisRurais to set
	 */
	public void setImoveisRurais(List<ImovelRural> imoveisRurais) {
		this.imoveisRurais = imoveisRurais;
	}

	/**
	 * Getter do campo torres
	 *	
	 * @return the torres
	 */
	public List<TorreAnemometrica> getTorres() {
		return torres;
	}

	/**
	 * Setter do campo  torres
	 * @param torres the torres to set
	 */
	public void setTorres(List<TorreAnemometrica> torres) {
		this.torres = torres;
	}

	
	/**
	 * Getter do campo activeTab
	 *	
	 * @return the activeTab
	 */
	public int getActiveTab() {
		return activeTab;
	}

	/**
	 * Setter do campo  activeTab
	 * @param activeTab the activeTab to set
	 */
	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}


	/**
	 * Getter do campo imovel
	 *	
	 * @return the imovel
	 */
	public Imovel getImovel() {
		return imovel;
	}


	/**
	 * Setter do campo  imovel
	 * @param imovel the imovel to set
	 */
	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}


	/**
	 * Getter do campo numSicar
	 *	
	 * @return the numSicar
	 */
	public String getNumSicar() {
		return numSicar;
	}


	/**
	 * Setter do campo  numSicar
	 * @param numSicar the numSicar to set
	 */
	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}


	/**
	 * Getter do campo itemsNaturezaTorre
	 *	
	 * @return the itemsNaturezaTorre
	 */
	public Collection<TipoNaturezaTorre> getItemsNaturezaTorre() {
		return itemsNaturezaTorre;
	}


	/**
	 * Setter do campo  itemsNaturezaTorre
	 * @param itemsNaturezaTorre the itemsNaturezaTorre to set
	 */
	public void setItemsNaturezaTorre(Collection<TipoNaturezaTorre> itemsNaturezaTorre) {
		this.itemsNaturezaTorre = itemsNaturezaTorre;
	}


	/**
	 * Getter do campo tiposUnidadeConservacao
	 *	
	 * @return the tiposUnidadeConservacao
	 */
	public Collection<TipoUnidadeConservacaoTorre> getTiposUnidadeConservacao() {
		return tiposUnidadeConservacao;
	}


	/**
	 * Setter do campo  tiposUnidadeConservacao
	 * @param tiposUnidadeConservacao the tiposUnidadeConservacao to set
	 */
	public void setTiposUnidadeConservacao(Collection<TipoUnidadeConservacaoTorre> tiposUnidadeConservacao) {
		this.tiposUnidadeConservacao = tiposUnidadeConservacao;
	}

	/**
	 * Getter do campo localizacaoAtividadesTorres
	 *	
	 * @return the localizacaoAtividadesTorres
	 */
	public Collection<LocalizacaoAtividadeTorre> getLocalizacaoAtividadesTorres() {
		return localizacaoAtividadesTorres;
	}

	/**
	 * Setter do campo  localizacaoAtividadesTorres
	 * @param localizacaoAtividadesTorres the localizacaoAtividadesTorres to set
	 */
	public void setLocalizacaoAtividadesTorres(Collection<LocalizacaoAtividadeTorre> localizacaoAtividadesTorres) {
		this.localizacaoAtividadesTorres = localizacaoAtividadesTorres;
	}

	
	
}
