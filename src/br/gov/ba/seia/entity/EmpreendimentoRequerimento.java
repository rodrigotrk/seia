package br.gov.ba.seia.entity;

/**
 * @author eduardo.fernandes
 */

import java.math.BigDecimal;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "empreendimento_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmpreendimentoRequerimento.findByIdeRequerimento", query = "SELECT r FROM EmpreendimentoRequerimento r WHERE r.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "EmpreendimentoRequerimento.findByIdeEmpreendimento", query = "SELECT r FROM EmpreendimentoRequerimento r WHERE r.ideEmpreendimento = :ideEmpreendimento") })

public class EmpreendimentoRequerimento extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMPREENDIMENTO_REQUERIMENTO_IDE_EMPREENDIMENTO_REQUERIMENTO_seq")    
    @SequenceGenerator(name="EMPREENDIMENTO_REQUERIMENTO_IDE_EMPREENDIMENTO_REQUERIMENTO_seq", sequenceName="EMPREENDIMENTO_REQUERIMENTO_IDE_EMPREENDIMENTO_REQUERIMENTO_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_empreendimento_requerimento", nullable = false)
	private Integer ideEmpreendimentoRequerimento; 

	@Column(name = "dtc_fase_empreendimento", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFaseEmpreendimento;

	@Column(name = "ind_dla")
	private Boolean indDla;
	
	@Column(name = "num_processo_ana")
	private String numProcessoAna;
	
	@Column(name = "num_portaria_ana")
	private String numPortariaAna;
	
	@Column(name = "num_vazao_total")
	private BigDecimal numVazaoTotal;
	
	@NotNull
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@NotNull
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Empreendimento ideEmpreendimento;
	
	@JoinColumn(name = "ide_fase_empreendimento", referencedColumnName = "ide_fase_empreendimento", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private FaseEmpreendimento ideFaseEmpreendimento;
	
	@JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Porte idePorte;
	
	@JoinColumn(name = "ide_classe", referencedColumnName = "ide_classe")
	@ManyToOne
	private Classe ideClasse;
	
	@JoinColumn(name = "ide_programa_governo", referencedColumnName = "ide_programa_governo")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProgramaGoverno programaGoverno;

	@Transient
	private Boolean isDeveSalvarClasseNulo;
	
	public EmpreendimentoRequerimento() {

	}
	
	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public FaseEmpreendimento getIdeFaseEmpreendimento() {
		return ideFaseEmpreendimento;
	}

	public void setIdeFaseEmpreendimento(FaseEmpreendimento ideFaseEmpreendimento) {
		this.ideFaseEmpreendimento = ideFaseEmpreendimento;
	}

	public Date getDtcFaseEmpreendimento() {
		return dtcFaseEmpreendimento;
	}

	public void setDtcFaseEmpreendimento(Date dtcFaseEmpreendimento) {
		this.dtcFaseEmpreendimento = dtcFaseEmpreendimento;
	}

	public Porte getIdePorte() {
		return idePorte;
	}

	public void setIdePorte(Porte idePorte) {
		this.idePorte = idePorte;
	}

	public Boolean getIndDla() {
		return indDla;
	}

	public void setIndDla(Boolean indDla) {
		this.indDla = indDla;
	}

	public Classe getIdeClasse() {
		return ideClasse;
	}

	public void setIdeClasse(Classe ideClasse) {
		this.ideClasse = ideClasse;
	}

	public String getNumProcessoAna() {
		return numProcessoAna;
	}

	public void setNumProcessoAna(String numProcessoAna) {
		this.numProcessoAna = numProcessoAna;
	}

	public String getNumPortariaAna() {
		return numPortariaAna;
	}

	public void setNumPortariaAna(String numPortariaAna) {
		this.numPortariaAna = numPortariaAna;
	}

	/**
	 * @return the numVazaoTotal
	 */
	public BigDecimal getNumVazaoTotal() {
		return numVazaoTotal;
	}

	/**
	 * @param numVazaoTotal the numVazaoTotal to set
	 */
	public void setNumVazaoTotal(BigDecimal numVazaoTotal) {
		this.numVazaoTotal = numVazaoTotal;
	}

	public Integer getIdeEmpreendimentoRequerimento() {
		return ideEmpreendimentoRequerimento;
	}


	public void setIdeEmpreendimentoRequerimento(Integer ideEmpreendimentoRequerimento) {
		this.ideEmpreendimentoRequerimento = ideEmpreendimentoRequerimento;
	}
	
	public ProgramaGoverno getProgramaGoverno() {
		return programaGoverno;
	}
	
	public void setProgramaGoverno(ProgramaGoverno programaGoverno) {
		this.programaGoverno = programaGoverno;
	}
	
	@Transient
	public Boolean isDeveSalvarClasseNulo() {
		if(isDeveSalvarClasseNulo == null){
			return false;
		}
		return isDeveSalvarClasseNulo;
	}

	@Transient
	public void setIsDeveSalvarClasseNulo(Boolean isDeveSalvarClasseNulo) {
		this.isDeveSalvarClasseNulo = isDeveSalvarClasseNulo;
	}

	@Override
	public String toString() {
		return 
				 "["+ideEmpreendimentoRequerimento.toString() + "]" + "\n" +  
				 "["+ideRequerimento.toString() + "]" + "\n "+
				 "["+ideEmpreendimento.toString() + "]" ;
	}
}