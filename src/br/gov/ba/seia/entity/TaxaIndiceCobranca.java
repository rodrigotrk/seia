package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="taxa_indice_cobranca")
@XmlRootElement
public class TaxaIndiceCobranca implements Serializable {

	private static final long serialVersionUID = 4138679460336048787L;

	@Id
	@SequenceGenerator(name="TAXA_INDICE_COBRANCA_SEQ", sequenceName="INDICE_COBRANCA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TAXA_INDICE_COBRANCA_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_taxa_indice_cobranca")
	private Integer ideTaxaIndiceCobranca;
	
	@JoinColumn(name = "ide_indice_cobranca", referencedColumnName = "ide_indice_cobranca")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private IndiceCobranca ideIndiceCobranca;
	
	@Column(name = "valor", nullable = false, precision = 12, scale = 8)
	private Double valor;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	@Column(name = "dtc_criacao", nullable = false)
	private Date dtcCriacao;
	
	@Column(name = "dtc_exclusao", nullable = true)
	private Date dtcExclusao;
	
	@Column(name = "ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisica;
	
	@Column(name = "mes_referencia", nullable = false)
	private Integer mesReferencia;
	
	@Column(name = "ano_referencia", nullable = false)
	private Integer anoReferencia;

	public Integer getIdeTaxaIndiceCobranca() {
		return ideTaxaIndiceCobranca;
	}

	public void setIdeTaxaIndiceCobranca(Integer ideTaxaIndiceCobranca) {
		this.ideTaxaIndiceCobranca = ideTaxaIndiceCobranca;
	}

	public IndiceCobranca getIdeIndiceCobranca() {
		return ideIndiceCobranca;
	}

	public void setIdeIndiceCobranca(IndiceCobranca ideIndiceCobranca) {
		this.ideIndiceCobranca = ideIndiceCobranca;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
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

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Integer getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(Integer mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public Integer getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(Integer anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

}
