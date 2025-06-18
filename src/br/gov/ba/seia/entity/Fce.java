package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.util.Util;

@Entity
@XmlRootElement
@Table(name="fce")
public class Fce extends AbstractEntity implements Comparable<Fce>, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ide_fce_generator")
	@SequenceGenerator(name = "fce_ide_fce_generator", sequenceName = "fce_ide_fce_seq", allocationSize = 1)
	@Column(name="ide_fce")
	private Integer ideFce;

	@Column(name="dtc_criacao")
	private Date dtcCriacao;
	
	@Column(name="ind_concluido")
	private Boolean indConcluido;
	
	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_analise_tecnica ", referencedColumnName = "ide_analise_tecnica", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private AnaliseTecnica ideAnaliseTecnica;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = true)
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	private Funcionario idePessoaFisica;
	
	@OneToMany(mappedBy="ideFce", fetch=FetchType.LAZY)
	private Collection<FceReservaAgua> fceReservaAguaCollection;
	
	@JoinColumn(name = "ide_origem_fce", referencedColumnName = "ide_dado_origem", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private DadoOrigem ideDadoOrigem;
	
	@OneToMany(mappedBy="ideFce", fetch=FetchType.LAZY)
	private Collection<FceOutorgaLocalizacaoGeografica> fceOutorgaLocalizacaoGeografica;
	
	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento", nullable = true)
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	private ProcessoReenquadramento ideProcessoReenquadramento;
	
	@OneToMany(mappedBy="ideFce", fetch=FetchType.LAZY)
	private Collection<FceIntervencaoBarragem> fceIntervencaoBarragemCollection;
	
	@Transient
	private DocumentoAto documentoAto;
	
	@Transient
	private ProcessoAto processoAto;
	
	public Fce() {
		
	}

	public Fce(Integer ideFce) {
		this.ideFce = ideFce;
	}

	public Fce(DocumentoObrigatorio ideDocumentoObrigatorio, Requerimento ideRequerimento, AnaliseTecnica analiseTecnica) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
		this.ideRequerimento = ideRequerimento;
		if(!Util.isNull(analiseTecnica)){
			this.ideDadoOrigem = new DadoOrigem(DadoOrigemEnum.TECNICO.getId());
		} 
		else {
			this.ideDadoOrigem = new DadoOrigem(DadoOrigemEnum.REQUERIMENTO.getId());
		}
		this.ideAnaliseTecnica = analiseTecnica;
		this.dtcCriacao = new Date();
	}
	
	public Fce(DocumentoObrigatorio ideDocumentoObrigatorio, Requerimento ideRequerimento, AnaliseTecnica analiseTecnica, ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
		this.ideRequerimento = ideRequerimento;
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
		if(!Util.isNull(analiseTecnica)){
			this.ideDadoOrigem = new DadoOrigem(DadoOrigemEnum.TECNICO.getId());
		} 
		else {
			this.ideDadoOrigem = new DadoOrigem(DadoOrigemEnum.REQUERIMENTO.getId());
		}
		this.ideAnaliseTecnica = analiseTecnica;
		this.dtcCriacao = new Date();
	}

	public Integer getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Integer ideFce) {
		this.ideFce = ideFce;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Boolean isIndConcluido() {
		if(this.indConcluido==null) {
			return false;
		}
		return indConcluido;
	}

	public void setIndConcluido(Boolean indConcluido) {
		this.indConcluido = indConcluido;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public AnaliseTecnica getIdeAnaliseTecnica() {
		return ideAnaliseTecnica;
	}

	public void setIdeAnaliseTecnica(AnaliseTecnica ideAnaliseTecnica) {
		this.ideAnaliseTecnica = ideAnaliseTecnica;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Collection<FceReservaAgua> getFceReservaAguaCollection() {
		return fceReservaAguaCollection;
	}

	public void setFceReservaAguaCollection(Collection<FceReservaAgua> fceReservaAguaCollection) {
		this.fceReservaAguaCollection = fceReservaAguaCollection;
	}

	public DadoOrigem getIdeDadoOrigem() {
		return ideDadoOrigem;
	}

	public void setIdeDadoOrigem(DadoOrigem ideDadoOrigem) {
		this.ideDadoOrigem = ideDadoOrigem;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(
			ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	@Override
	public int compareTo(Fce fce) {
		if(Util.isNull(fce) || Util.isNull(ideDocumentoObrigatorio) || Util.isNull(ideDocumentoObrigatorio.getNomDocumentoObrigatorio())) {
			return 0;
		}
		return fce.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio().compareTo(ideDocumentoObrigatorio.getNomDocumentoObrigatorio()) * -1;
	}

	public Fce clone() throws CloneNotSupportedException{
		return (Fce) super.clone();
	}	

	public boolean isFceTecnico() {
		return !Util.isNull(getIdeDadoOrigem()) && getIdeDadoOrigem().getIdeDadoOrigem().equals(DadoOrigemEnum.TECNICO.getId());
	}

	public Collection<FceOutorgaLocalizacaoGeografica> getFceOutorgaLocalizacaoGeografica() {
		return fceOutorgaLocalizacaoGeografica;
	}

	public void setFceOutorgaLocalizacaoGeografica(
			Collection<FceOutorgaLocalizacaoGeografica> fceOutorgaLocalizacaoGeografica) {
		this.fceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeografica;
	}

	public Collection<FceIntervencaoBarragem> getFceIntervencaoBarragemCollection() {
		return fceIntervencaoBarragemCollection;
	}

	public void setFceIntervencaoBarragemCollection(
			Collection<FceIntervencaoBarragem> fceIntervencaoBarragemCollection) {
		this.fceIntervencaoBarragemCollection = fceIntervencaoBarragemCollection;
	}

	public DocumentoAto getDocumentoAto() {
		return documentoAto;
	}

	public void setDocumentoAto(DocumentoAto documentoAto) {
		this.documentoAto = documentoAto;
	}

	public ProcessoAto getProcessoAto() {
		return processoAto;
	}

	public void setProcessoAto(ProcessoAto processoAto) {
		this.processoAto = processoAto;
	}
}