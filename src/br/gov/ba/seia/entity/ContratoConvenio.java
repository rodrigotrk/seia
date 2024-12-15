package br.gov.ba.seia.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name = "contrato_convenio")
public class ContratoConvenio extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="contrato_convenio_seq")
    @SequenceGenerator(name="contrato_convenio_seq", sequenceName="contrato_convenio_seq", allocationSize=1)
	@Column(name = "ide_contrato_convenio")
	private Integer ideContratoConvenio;

	@Historico(name="Contrato/Convênio?")
	@Column(name = "nom_contrato_convenio")
	private String nomContratoConvenio;

	@Column(name = "num_contrato")
	private String numContrato;

	@ManyToOne
	@JoinColumn(name = "ide_gestor_financeiro")
	private GestorFinanceiro ideGestorFinanceiro;

	@ManyToOne
	@JoinColumn(name = "ide_pessoa_juridica")
	private PessoaJuridica idePessoaJuridica;

	@ManyToOne
	@JoinColumn(name = "ide_tipo_projeto")
	private TipoProjeto ideTipoProjeto;

	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private boolean indExcluido;

	@OneToMany(mappedBy="ideContratoConvenio")
	private List<PessoaFisicaContratoConvenio> pessoaFisicaContratoConvenio;
	
	public ContratoConvenio() {
	}

	public Integer getIdeContratoConvenio() {
		return ideContratoConvenio;
	}

	public void setIdeContratoConvenio(Integer ideContratoConvenio) {
		this.ideContratoConvenio = ideContratoConvenio;
	}

	public GestorFinanceiro getIdeGestorFinanceiro() {
		return ideGestorFinanceiro;
	}

	public void setIdeGestorFinanceiro(GestorFinanceiro ideGestorFinanceiro) {
		this.ideGestorFinanceiro = ideGestorFinanceiro;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public String getNomContratoConvenio() {
		return nomContratoConvenio;
	}

	public void setNomContratoConvenio(String nomContratoConvenio) {
		this.nomContratoConvenio = nomContratoConvenio;
	}

	public String getNumContrato() {
		return numContrato;
	}

	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}

	public TipoProjeto getIdeTipoProjeto() {
		return ideTipoProjeto;
	}

	public void setIdeTipoProjeto(TipoProjeto ideTipoProjeto) {
		this.ideTipoProjeto = ideTipoProjeto;
	}

	@Override
	public ContratoConvenio clone() throws CloneNotSupportedException {
		return (ContratoConvenio) super.clone();
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public List<PessoaFisicaContratoConvenio> getPessoaFisicaContratoConvenio() {
		return pessoaFisicaContratoConvenio;
	}

	public void setPessoaFisicaContratoConvenio(List<PessoaFisicaContratoConvenio> pessoaFisicaContratoConvenio) {
		this.pessoaFisicaContratoConvenio = pessoaFisicaContratoConvenio;
	}

}