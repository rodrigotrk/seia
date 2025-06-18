package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoPessoaJuridicaPctEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="pct_imovel_rural")
public class PctImovelRural extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name = "pct_imovel_rural_generator", sequenceName = "pct_imovel_rural_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pct_imovel_rural_generator")
	@Column(name="ide_pct")
	private Integer idePctImovelRural;
	
	@Column(name="num_familias", nullable=false)
	private Integer numFamilias;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_imovel_rural", referencedColumnName="ide_imovel_rural",nullable=true)
	private ImovelRural ideImovelRural;
	
	@JoinTable(name = "pct_imovel_rural_tipo_seguimento_pct", joinColumns = { @JoinColumn(name = "ide_pct", referencedColumnName = "ide_pct") }, 
			inverseJoinColumns = { @JoinColumn(name = "ide_tipo_seguimento_pct", referencedColumnName = "ide_tipo_seguimento_pct") })
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<TipoSeguimentoPct> tipoSeguimentoPctCollection;
	
	@ManyToOne
	@JoinColumn(name="ide_tipo_territorio_pct", referencedColumnName="ide_tipo_territorio_pct",nullable=true)
	private TipoTerritorioPct ideTipoTerritorioPct;
	
	@Column(name="dsc_tipo_seguimento_pct_outros", nullable=true)
	private String dscTipoSeguimentoPctOutros;
	
	@Column(name="dsc_tipo_territorio_pct_outros",nullable=true)
	private String dscTipoTerritorioPctOutros;
	
	@OneToMany(mappedBy = "idePctImovelRural")
	private Collection<PctFamilia> pctFamiliaCollection;
	
	@Column(name="ind_aceite")
	private boolean indAceite;
	
	@Column(name="ind_associacao_comunidade")
	private Boolean indAssociacaoComunidade;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_contrato_convenio", referencedColumnName="ide_contrato_convenio",nullable=true)
	private ContratoConvenio ContratoConvenio;
	
	@Transient
	private TipoTerritorioPct ideTipoTerritorioPctOld;
	
	@OneToMany(mappedBy="idePctImovelRural", fetch=FetchType.LAZY)
	private List<PessoaJuridicaPct> pessoaJuridicaPctList;
	
	@Transient
	private Collection<PctFamilia> listarPctRepresentanteFamiliaCollection;
	
	@Transient
	private PctFamilia pessoaRepresentanteFamiliaSelecionada;
	
	@Transient
	private Collection<PctFamilia> pctFamiliaExclusaoCollection;
	
	@Transient
	private List<PessoaJuridicaPct> pessoaJuridicaPctListaAssociacao;
	
	@Transient
	private List<PessoaJuridicaPct> pessoaJuridicaPctListaConcedente;
	
	@Transient
	private List<PessoaJuridicaPct> pessoaJuridicaPctListaConcessionario;
	
	@Transient
	private List<PessoaJuridicaPct> pessoaJuridicaPctListaExclusao;
	
	@Transient
	private TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEnum;
	
	
	public PctImovelRural() {
	}
	
	public boolean isRenderedPessoaJuridicaPctListaAssociacao() {
		return !Util.isNullOuVazio(pessoaJuridicaPctListaAssociacao);
	}
	
	public boolean isRenderedPessoaJuridicaPctListaConcedente() {
		return !Util.isNullOuVazio(pessoaJuridicaPctListaConcedente);
	}
	
	public boolean isRenderedPessoaJuridicaPctListaConcessionario() {
		return !Util.isNullOuVazio(pessoaJuridicaPctListaConcessionario);
	}
	
	public Integer getIdePctImovelRural() {
		return idePctImovelRural;
	}

	public void setIdePctImovelRural(Integer idePct) {
		this.idePctImovelRural = idePct;
	}

	public Integer getNumFamilias() {
		return numFamilias;
	}

	public void setNumFamilias(Integer numFamilias) {
		this.numFamilias = numFamilias;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public List<TipoSeguimentoPct> getTipoSeguimentoPctCollection() {
		return tipoSeguimentoPctCollection;
	}

	public void setTipoSeguimentoPctCollection(
			List<TipoSeguimentoPct> tipoSeguimentoPctCollection) {
		this.tipoSeguimentoPctCollection = tipoSeguimentoPctCollection;
	}

	public TipoTerritorioPct getIdeTipoTerritorioPct() {
		return ideTipoTerritorioPct;
	}

	public void setIdeTipoTerritorioPct(TipoTerritorioPct ideTipoTerritorioPct) {
		this.ideTipoTerritorioPct = ideTipoTerritorioPct;
	}

	public String getDscTipoSeguimentoPctOutros() {
		return dscTipoSeguimentoPctOutros;
	}

	public void setDscTipoSeguimentoPctOutros(String dscTipoSeguimentoPctOutros) {
		this.dscTipoSeguimentoPctOutros = dscTipoSeguimentoPctOutros;
	}

	public String getDscTipoTerritorioPctOutros() {
		return dscTipoTerritorioPctOutros;
	}

	public void setDscTipoTerritorioPctOutros(String dscTipoTerritorioPctOutros) {
		this.dscTipoTerritorioPctOutros = dscTipoTerritorioPctOutros;
	}

	public Collection<PctFamilia> getPctFamiliaCollection() {
		return pctFamiliaCollection;
	}

	public void setPctFamiliaCollection(Collection<PctFamilia> pctFamiliaCollection) {
		this.pctFamiliaCollection = pctFamiliaCollection;
	}

	public TipoTerritorioPct getIdeTipoTerritorioPctOld() {
		return ideTipoTerritorioPctOld;
	}

	public void setIdeTipoTerritorioPctOld(TipoTerritorioPct ideTipoTerritorioPctOld) {
		this.ideTipoTerritorioPctOld = ideTipoTerritorioPctOld;
	}

	public boolean isIndAceite() {
		return indAceite;
	}

	public void setIndAceite(boolean indAceite) {
		this.indAceite = indAceite;
	}

	public List<PessoaJuridicaPct> getPessoaJuridicaPctList() {
		return pessoaJuridicaPctList;
	}

	public void setPessoaJuridicaPctList(
			List<PessoaJuridicaPct> pessoaJuridicaPctList) {
		this.pessoaJuridicaPctList = pessoaJuridicaPctList;
	}

	public Boolean getIndAssociacaoComunidade() {
		return indAssociacaoComunidade;
	}

	public void setIndAssociacaoComunidade(Boolean indAssociacaoComunidade) {
		this.indAssociacaoComunidade = indAssociacaoComunidade;
	}

	public Collection<PctFamilia> getListarPctRepresentanteFamiliaCollection() {
		return listarPctRepresentanteFamiliaCollection;
	}

	public void setListarPctRepresentanteFamiliaCollection(
			Collection<PctFamilia> listarPctRepresentanteFamiliaCollection) {
		this.listarPctRepresentanteFamiliaCollection = listarPctRepresentanteFamiliaCollection;
	}

	public PctFamilia getPessoaRepresentanteFamiliaSelecionada() {
		return pessoaRepresentanteFamiliaSelecionada;
	}

	public void setPessoaRepresentanteFamiliaSelecionada(
			PctFamilia pessoaRepresentanteFamiliaSelecionada) {
		this.pessoaRepresentanteFamiliaSelecionada = pessoaRepresentanteFamiliaSelecionada;
	}

	public Collection<PctFamilia> getPctFamiliaExclusaoCollection() {
		return pctFamiliaExclusaoCollection;
	}

	public void setPctFamiliaExclusaoCollection(
			Collection<PctFamilia> pctFamiliaExclusaoCollection) {
		this.pctFamiliaExclusaoCollection = pctFamiliaExclusaoCollection;
	}

	public List<PessoaJuridicaPct> getPessoaJuridicaPctListaExclusao() {
		return pessoaJuridicaPctListaExclusao;
	}

	public void setPessoaJuridicaPctListaExclusao(
			List<PessoaJuridicaPct> pessoaJuridicaPctListaExclusao) {
		this.pessoaJuridicaPctListaExclusao = pessoaJuridicaPctListaExclusao;
	}

	public ContratoConvenio getContratoConvenio() {
		return ContratoConvenio;
	}

	public void setContratoConvenio(ContratoConvenio contratoConvenio) {
		ContratoConvenio = contratoConvenio;
	}

	public TipoPessoaJuridicaPctEnum getTipoPessoaJuridicaPctEnum() {
		return tipoPessoaJuridicaPctEnum;
	}

	public void setTipoPessoaJuridicaPctEnum(TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEnum) {
		this.tipoPessoaJuridicaPctEnum = tipoPessoaJuridicaPctEnum;
	}
	
	public List<PessoaJuridicaPct> getPessoaJuridicaPctListaAssociacao() {
		return pessoaJuridicaPctListaAssociacao;
	}

	public void setPessoaJuridicaPctListaAssociacao(
			List<PessoaJuridicaPct> pessoaJuridicaPctListaAssociacao) {
		this.pessoaJuridicaPctListaAssociacao = pessoaJuridicaPctListaAssociacao;
	}

	public List<PessoaJuridicaPct> getPessoaJuridicaPctListaConcedente() {
		return pessoaJuridicaPctListaConcedente;
	}

	public void setPessoaJuridicaPctListaConcedente(
			List<PessoaJuridicaPct> pessoaJuridicaPctListaConcedente) {
		this.pessoaJuridicaPctListaConcedente = pessoaJuridicaPctListaConcedente;
	}

	public List<PessoaJuridicaPct> getPessoaJuridicaPctListaConcessionario() {
		return pessoaJuridicaPctListaConcessionario;
	}

	public void setPessoaJuridicaPctListaConcessionario(List<PessoaJuridicaPct> pessoaJuridicaPctListaConcessionario) {
		this.pessoaJuridicaPctListaConcessionario = pessoaJuridicaPctListaConcessionario;
	}

}
