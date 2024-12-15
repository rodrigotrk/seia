package br.gov.ba.seia.entity;

import java.util.Date;


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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;


@Entity
@Table(name="status_processo_ato")
public class ControleProcessoAto extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="status_processo_ato_seq")    
	@SequenceGenerator(name="status_processo_ato_seq", sequenceName="status_processo_ato_seq", allocationSize=1)
	@Column(name="ide_status_processo_ato", unique=true, nullable=false)
	private Integer ideControleProcessoAto;

	@Column(name="dsc_justificativa_status", length=18)
	private String dscJustificativaStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_status_processo_ato")
	private Date dtcControleProcessoAto;

	@Column(name="ind_aprovado")
	private Boolean indAprovado;
	
	@Column(name="num_prazo_validade", length=2)
	private Integer numPrazoValidade;
	
	@JoinColumn(name="ide_pessoa_fisica", referencedColumnName="ide_pessoa_fisica", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Funcionario idePessoaFisica;
	
	@JoinColumn(name="ide_processo_ato", referencedColumnName="ide_processo_ato", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name="ide_tipo_status_processo_ato", referencedColumnName="ide_tipo_status_processo_ato", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private StatusProcessoAto ideStatusProcessoAto;
	
	@Column(name="ind_prazo_indeterminado")
	private Boolean indPrazoIndeterminado;

	public ControleProcessoAto() {
		
	}

	public ControleProcessoAto(Integer ideControleProcessoAto) {
		this.ideControleProcessoAto = ideControleProcessoAto;
	}
	
	public Integer getIdeControleProcessoAto() {
		return ideControleProcessoAto;
	}

	public void setIdeControleProcessoAto(Integer ideControleProcessoAto) {
		this.ideControleProcessoAto = ideControleProcessoAto;
	}

	public String getDscJustificativaStatus() {
		return dscJustificativaStatus;
	}

	public void setDscJustificativaStatus(String dscJustificativaStatus) {
		this.dscJustificativaStatus = dscJustificativaStatus;
	}

	public Date getDtcControleProcessoAto() {
		return dtcControleProcessoAto;
	}

	public void setDtcControleProcessoAto(Date dtcControleProcessoAto) {
		this.dtcControleProcessoAto = dtcControleProcessoAto;
	}

	public Boolean getIndAprovado() {
		return indAprovado;
	}

	public void setIndAprovado(Boolean indAprovado) {
		this.indAprovado = indAprovado;
	}

	public Integer getNumPrazoValidade() {
		return numPrazoValidade;
	}

	public void setNumPrazoValidade(Integer numPrazoValidade) {
		this.numPrazoValidade = numPrazoValidade;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public StatusProcessoAto getIdeStatusProcessoAto() {
		return ideStatusProcessoAto;
	}

	public void setIdeStatusProcessoAto(
			StatusProcessoAto ideStatusProcessoAto) {
		this.ideStatusProcessoAto = ideStatusProcessoAto;
	}

	public Boolean getIndPrazoIndeterminado() {
		return indPrazoIndeterminado;
	}

	public void setIndPrazoIndeterminado(Boolean indPrazoIndeterminado) {
		this.indPrazoIndeterminado = indPrazoIndeterminado;
	}
	
	public ControleProcessoAto clone() throws CloneNotSupportedException {
		return (ControleProcessoAto) super.clone();
	}
}