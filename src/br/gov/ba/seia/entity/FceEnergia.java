package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.entity.generic.AbstractEntityLocalizacaoGeografica;

@Entity
@Table(name="FCE_ENERGIA")
public class FceEnergia extends AbstractEntityLocalizacaoGeografica {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_seq")
	@SequenceGenerator(name = "fce_energia_seq", sequenceName = "fce_energia_seq", allocationSize = 1)
	@Column(name="IDE_FCE_ENERGIA")
	private Integer ideFceEnergia;

	@Column(name="DES_IDENTIFICACAO_EMPREENDIMENTO")
	private String desIdentificacaoEmpreendimento;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "VAL_AREA", nullable = false)
	private BigDecimal valorArea;
	
	@Basic(optional = false)
    @Column(name = "NUM_PROCESSO_OUTORGA")
	private String numProcessoOutorga;
	
	@Basic(optional = false)
    @Column(name = "NUM_PROCESSO_asv")
	private String numProcessoASV;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "IND_ASV", nullable = false)
    private Boolean indASV;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "IND_CAPTACAO", nullable = false)
    private Boolean indCaptacao;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "ide_fce", nullable = false)
	private Fce ideFce;
	
	@ManyToOne(cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@JoinTable(name = "FCE_ENERGIA_FINALIDADE", joinColumns = { @JoinColumn(name = "IDE_FCE_ENERGIA", referencedColumnName = "IDE_FCE_ENERGIA", updatable = true) }, inverseJoinColumns = { @JoinColumn(name = "IDE_FINALIDADE_GERACAO_ENERGIA", referencedColumnName = "IDE_FINALIDADE_GERACAO_ENERGIA", updatable = true) })
	@ManyToMany(fetch = FetchType.LAZY)
	private List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergia;
	
	@Transient
	private List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaSelecionado;
	
	public FceEnergia() {
		ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public FceEnergia(Fce ideFce){
		this.ideFce = ideFce;
	}

	public Integer getIdeFceEnergia() {
		return ideFceEnergia;
	}

	public void setIdeFceEnergia(Integer ideFceEnergia) {
		this.ideFceEnergia = ideFceEnergia;
	}

	public String getDesIdentificacaoEmpreendimento() {
		return desIdentificacaoEmpreendimento;
	}

	public void setDesIdentificacaoEmpreendimento(String desIdentificacaoEmpreendimento) {
		this.desIdentificacaoEmpreendimento = desIdentificacaoEmpreendimento;
	}

	public BigDecimal getValorArea() {
		return valorArea;
	}

	public void setValorArea(BigDecimal valorArea) {
		this.valorArea = valorArea;
	}

	public String getNumProcessoOutorga() {
		return numProcessoOutorga;
	}

	public void setNumProcessoOutorga(String numProcessoOutorga) {
		this.numProcessoOutorga = numProcessoOutorga;
	}

	public String getNumProcessoASV() {
		return numProcessoASV;
	}

	public void setNumProcessoASV(String numProcessoASV) {
		this.numProcessoASV = numProcessoASV;
	}

	public Boolean getIndASV() {
		return indASV;
	}

	public void setIndASV(Boolean indASV) {
		this.indASV = indASV;
	}

	public Boolean getIndCaptacao() {
		return indCaptacao;
	}

	public void setIndCaptacao(Boolean indCaptacao) {
		this.indCaptacao = indCaptacao;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public List<FinalidadeGeracaoEnergia> getListaFinalidadeGeracaoEnergia() {
		return listaFinalidadeGeracaoEnergia;
	}

	public void setListaFinalidadeGeracaoEnergia(List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergia) {
		this.listaFinalidadeGeracaoEnergia = listaFinalidadeGeracaoEnergia;
	}

	
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public List<FinalidadeGeracaoEnergia> getListaFinalidadeGeracaoEnergiaSelecionado() {
		return listaFinalidadeGeracaoEnergiaSelecionado;
	}

	public void setListaFinalidadeGeracaoEnergiaSelecionado(List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaSelecionado) {
		this.listaFinalidadeGeracaoEnergiaSelecionado = listaFinalidadeGeracaoEnergiaSelecionado;
	}

	@Override
	public List<LocalizacaoGeografica> getLocalizacoesGeograficas() {
		return Arrays.asList(ideLocalizacaoGeografica);
	}
	
}