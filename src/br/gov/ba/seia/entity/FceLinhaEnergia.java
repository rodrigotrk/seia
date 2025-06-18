package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "fce_linha_energia")
public class FceLinhaEnergia extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_fce_linha_energia", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_linha_energia_seq")
	@SequenceGenerator(name = "fce_linha_energia_seq", sequenceName = "fce_linha_energia_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergia;

	@Column(name = "val_extensao_total_linha", precision = 10, scale = 2, nullable = false)
	private BigDecimal valExtensaoTotalLinha;

	@Column(name = "val_largura_faixa_servidao", precision = 10, scale = 2, nullable = false)
	private BigDecimal valLarguraFaixaServidao;

	@Column(name = "val_tensao_operacao", precision = 10, scale = 2, nullable = false)
	private BigDecimal valTensaoOperacao;

	@Column(name = "val_area_total_faixa_servidao", precision = 20, scale = 4, nullable = false)
	private BigDecimal valAreaTotalFaixaServidao;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@NotNull
	@Column(name = "ind_alternativa_locacional")
	private Boolean indAlternativaLocacional;
	
	@NotNull
	@Column(name = "ind_excluido")
	private Boolean indExcluido;

	@Size(max = 500)
	@Column(name = "obs_alternativa_locacional")
	private String obsAlternativaLocacional;

	@OneToMany(mappedBy = "ideFceLinhaEnergia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<FceLinhaEnergiaTipoEnergia> fceLinhaEnergiaTipoEnergiaCollection;

	@OneToMany(mappedBy = "ideFceLinhaEnergia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<FceLinhaEnergiaTipoSubestacao> fceLinhaEnergiaTipoSubestacaoCollection;

	@OneToMany(mappedBy = "ideFceLinhaEnergia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<FceLinhaEnergiaResiduoGerado> fceLinhaEnergiaResiduoGeradoCollection;

	@OneToMany(mappedBy = "ideFceLinhaEnergia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<FceLinhaEnergiaDestinoResiduo> fceLinhaEnergiaDestinoResiduoCollection;

	@OneToMany(mappedBy = "ideFceLinhaEnergia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<FceLinhaEnergiaLocalizacaoGeografica> fceLinhaEnergiaLocalizacaoGeograficaCollection;

	@Transient
	private FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaPreferencial;

	@Transient
	private String valExtensaoTotalLinhaFormatada;
	
	@Transient
	private TipoEnergia tipoEnergia;

	public FceLinhaEnergia() {
		this.fceLinhaEnergiaLocalizacaoGeograficaPreferencial = new FceLinhaEnergiaLocalizacaoGeografica(this);
		this.fceLinhaEnergiaLocalizacaoGeograficaPreferencial.setIndPreferencial(true);
		this.fceLinhaEnergiaLocalizacaoGeograficaCollection = new ArrayList<FceLinhaEnergiaLocalizacaoGeografica>();
		this.fceLinhaEnergiaTipoEnergiaCollection = new ArrayList<FceLinhaEnergiaTipoEnergia>();
		this.fceLinhaEnergiaResiduoGeradoCollection = new ArrayList<FceLinhaEnergiaResiduoGerado>();
		this.fceLinhaEnergiaDestinoResiduoCollection = new ArrayList<FceLinhaEnergiaDestinoResiduo>();
		this.fceLinhaEnergiaTipoSubestacaoCollection = new ArrayList<FceLinhaEnergiaTipoSubestacao>();
	}

	public FceLinhaEnergia(FceLinhaEnergia fceLinha){
		if(!Util.isNullOuVazio(fceLinha.getFceLinhaEnergiaDestinoResiduoCollection()) &&
				!Util.isNullOuVazio(fceLinha.getFceLinhaEnergiaResiduoGeradoCollection()) && !Util.isNull(fceLinha.getFceLinhaEnergiaTipoSubestacaoCollection())
				&& !Util.isNull(fceLinha.getFceLinhaEnergiaLocalizacaoGeograficaCollection()) && !Util.isNull(fceLinha.getFceLinhaEnergiaTipoEnergiaCollection())){
			
			this.fceLinhaEnergiaLocalizacaoGeograficaCollection = new ArrayList<FceLinhaEnergiaLocalizacaoGeografica>(fceLinha.getFceLinhaEnergiaLocalizacaoGeograficaCollection());
			this.fceLinhaEnergiaResiduoGeradoCollection = new ArrayList<FceLinhaEnergiaResiduoGerado>(fceLinha.getFceLinhaEnergiaResiduoGeradoCollection());
			this.fceLinhaEnergiaDestinoResiduoCollection = new ArrayList<FceLinhaEnergiaDestinoResiduo>(fceLinha.getFceLinhaEnergiaDestinoResiduoCollection());
			this.fceLinhaEnergiaTipoSubestacaoCollection = new ArrayList<FceLinhaEnergiaTipoSubestacao>(fceLinha.getFceLinhaEnergiaTipoSubestacaoCollection());
			this.fceLinhaEnergiaTipoEnergiaCollection = new ArrayList<FceLinhaEnergiaTipoEnergia>(fceLinha.getFceLinhaEnergiaTipoEnergiaCollection());
		}
	}
	
	public Integer getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(Integer ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public BigDecimal getValExtensaoTotalLinha() {
		return valExtensaoTotalLinha;
	}

	public void setValExtensaoTotalLinha(BigDecimal valExtensaoTotalLinha) {
		this.valExtensaoTotalLinha = valExtensaoTotalLinha;
	}

	public BigDecimal getValLarguraFaixaServidao() {
		return valLarguraFaixaServidao;
	}

	public void setValLarguraFaixaServidao(BigDecimal valLarguraFaixaServidao) {
		this.valLarguraFaixaServidao = valLarguraFaixaServidao;
	}

	public BigDecimal getValTensaoOperacao() {
		return valTensaoOperacao;
	}

	public void setValTensaoOperacao(BigDecimal valTensaoOperacao) {
		this.valTensaoOperacao = valTensaoOperacao;
	}

	public BigDecimal getValAreaTotalFaixaServidao() {
		return valAreaTotalFaixaServidao;
	}

	public void setValAreaTotalFaixaServidao(
			BigDecimal valAreaTotalFaixaServidao) {
		this.valAreaTotalFaixaServidao = valAreaTotalFaixaServidao;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndAlternativaLocacional() {
		return indAlternativaLocacional;
	}

	public void setIndAlternativaLocacional(Boolean indAlternativaLocacional) {
		this.indAlternativaLocacional = indAlternativaLocacional;
	}

	public String getObsAlternativaLocacional() {
		return obsAlternativaLocacional;
	}

	public void setObsAlternativaLocacional(String obsAlternativaLocacional) {
		this.obsAlternativaLocacional = obsAlternativaLocacional;
	}

	public Collection<FceLinhaEnergiaTipoEnergia> getFceLinhaEnergiaTipoEnergiaCollection() {
		return fceLinhaEnergiaTipoEnergiaCollection;
	}

	public void setFceLinhaEnergiaTipoEnergiaCollection(
			Collection<FceLinhaEnergiaTipoEnergia> fceLinhaEnergiaTipoEnergiaCollection) {
		this.fceLinhaEnergiaTipoEnergiaCollection = fceLinhaEnergiaTipoEnergiaCollection;
	}

	public Collection<FceLinhaEnergiaTipoSubestacao> getFceLinhaEnergiaTipoSubestacaoCollection() {
		return fceLinhaEnergiaTipoSubestacaoCollection;
	}

	public void setFceLinhaEnergiaTipoSubestacaoCollection(
			Collection<FceLinhaEnergiaTipoSubestacao> fceLinhaEnergiaTipoSubestacaoCollection) {
		this.fceLinhaEnergiaTipoSubestacaoCollection = fceLinhaEnergiaTipoSubestacaoCollection;
	}

	public Collection<FceLinhaEnergiaResiduoGerado> getFceLinhaEnergiaResiduoGeradoCollection() {
		return fceLinhaEnergiaResiduoGeradoCollection;
	}

	public void setFceLinhaEnergiaResiduoGeradoCollection(
			Collection<FceLinhaEnergiaResiduoGerado> fceLinhaEnergiaResiduoGeradoCollection) {
		this.fceLinhaEnergiaResiduoGeradoCollection = fceLinhaEnergiaResiduoGeradoCollection;
	}

	public Collection<FceLinhaEnergiaDestinoResiduo> getFceLinhaEnergiaDestinoResiduoCollection() {
		return fceLinhaEnergiaDestinoResiduoCollection;
	}

	public void setFceLinhaEnergiaDestinoResiduoCollection(
			Collection<FceLinhaEnergiaDestinoResiduo> fceLinhaEnergiaDestinoResiduoCollection) {
		this.fceLinhaEnergiaDestinoResiduoCollection = fceLinhaEnergiaDestinoResiduoCollection;
	}

	public Collection<FceLinhaEnergiaLocalizacaoGeografica> getFceLinhaEnergiaLocalizacaoGeograficaCollection() {
		return fceLinhaEnergiaLocalizacaoGeograficaCollection;
	}

	public void setFceLinhaEnergiaLocalizacaoGeograficaCollection(
			Collection<FceLinhaEnergiaLocalizacaoGeografica> fceLinhaEnergiaLocalizacaoGeograficaCollection) {
		this.fceLinhaEnergiaLocalizacaoGeograficaCollection = fceLinhaEnergiaLocalizacaoGeograficaCollection;
	}

	public FceLinhaEnergiaLocalizacaoGeografica getFceLinhaEnergiaLocalizacaoGeograficaPreferencial() {
		return fceLinhaEnergiaLocalizacaoGeograficaPreferencial;
	}

	public void setFceLinhaEnergiaLocalizacaoGeograficaPreferencial(
			FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaPreferencial) {
		this.fceLinhaEnergiaLocalizacaoGeograficaPreferencial = fceLinhaEnergiaLocalizacaoGeograficaPreferencial;
	}

	public TipoEnergia getTipoEnergia() {
		return tipoEnergia;
	}

	public void setTipoEnergia(TipoEnergia tipoEnergia) {
		this.tipoEnergia = tipoEnergia;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
}
