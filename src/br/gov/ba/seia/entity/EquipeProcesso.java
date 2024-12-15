package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import flexjson.JSON;

/**
 * @author MJunior
 */
@Entity
@Table(name = "equipe_processo")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "EquipeProcesso.findAll", query = "SELECT e FROM EquipeProcesso e"), 
	@NamedQuery(name = "EquipeProcesso.excluirEquipeProcesso", query = "DELETE FROM EquipeProcesso e WHERE e.ideEquipeProcesso = :ideEquipeProcesso"),
	@NamedQuery(name = "EquipeProcesso.findByIndLiderEquipe", query = "SELECT e FROM EquipeProcesso e WHERE e.indLiderEquipe = :indLiderEquipe"),
	@NamedQuery(name = "EquipeProcesso.findByIdeEquipeProcesso", query = "SELECT e FROM EquipeProcesso e WHERE e.ideEquipeProcesso = :ideEquipeProcesso"),
	@NamedQuery(name = "EquipeProcesso.findByIdeProcessoPessoaFica", query = "SELECT e FROM EquipeProcesso e WHERE e.ideProcesso.ideProcesso = :ideProcesso AND e.idePessoaFisica.idePessoaFisica =:idePessoaFisica"),
	@NamedQuery(name = "EquipeProcesso.findByIdeProcesso", query = "SELECT e FROM EquipeProcesso e WHERE e.ideProcesso.ideProcesso = :ideProcesso") })
public class EquipeProcesso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EQUIPE_PROCESSO_IDE_EQUIPE_PROCESSO_seq")
	@SequenceGenerator(name = "EQUIPE_PROCESSO_IDE_EQUIPE_PROCESSO_seq", sequenceName = "EQUIPE_PROCESSO_IDE_EQUIPE_PROCESSO_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_equipe_processo", nullable = false)
	private Integer ideEquipeProcesso;
	
	@Basic(optional = false)
	@Column(name = "ind_lider_equipe", nullable = false)
	private boolean indLiderEquipe;
    
	@JoinTable(name = "ide_integrante_equipe", joinColumns = {
            @JoinColumn(name = "ide_equipe_processo", referencedColumnName = "ide_equipe_processo", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false)})
        @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AtoAmbiental> atoAmbientalCollection;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Funcionario idePessoaFisica;
	
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = true)
	@OneToOne(fetch=FetchType.LAZY, optional=true)
	private Area ideArea;
	
	@JoinColumn(name = "ide_area_responsavel", referencedColumnName = "ide_area", nullable = true)
	@OneToOne(fetch=FetchType.LAZY, optional=true)
	private Area ideAreaResponsavel;

	@Transient
	private boolean isTecnicoAreaSecundaria;

	public EquipeProcesso() {
		atoAmbientalCollection = new ArrayList<AtoAmbiental>();
	}
	
	public EquipeProcesso(Integer ideEquipeProcesso, boolean indLiderEquipe, Collection<AtoAmbiental> atoAmbientalCollection, Processo ideProcesso, Funcionario idePessoaFisica,
			Area ideArea, Area ideAreaResponsavel, boolean isTecnicoAreaSecundaria) {
	
		super();
		this.ideEquipeProcesso = ideEquipeProcesso;
		this.indLiderEquipe = indLiderEquipe;
		this.atoAmbientalCollection = atoAmbientalCollection;
		this.ideProcesso = ideProcesso;
		this.idePessoaFisica = idePessoaFisica;
		this.ideArea = ideArea;
		this.ideAreaResponsavel = ideAreaResponsavel;
		this.isTecnicoAreaSecundaria = isTecnicoAreaSecundaria;
		
		this.atoAmbientalCollection = new ArrayList<AtoAmbiental>();
	}

	public EquipeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
	
	public EquipeProcesso(Integer ideEquipeProcesso) {
		this.ideEquipeProcesso = ideEquipeProcesso;
	}
	
	public EquipeProcesso(Integer ideEquipeProcesso, boolean indLiderEquipe) {
		this.ideEquipeProcesso = ideEquipeProcesso;
		this.indLiderEquipe = indLiderEquipe;
	}
	
	@JSON(include = false)
	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}


	@JSON(include = false)
	public boolean getIndLiderEquipe() {
		return indLiderEquipe;
	}

	public void setIndLiderEquipe(boolean indLiderEquipe) {
		this.indLiderEquipe = indLiderEquipe;
	}

	@JSON(include = false)
	public Integer getIdeEquipeProcesso() {
		return ideEquipeProcesso;
	}

	public void setIdeEquipeProcesso(Integer ideEquipeProcesso) {
		this.ideEquipeProcesso = ideEquipeProcesso;
	}

	@XmlTransient
	public Collection<AtoAmbiental> getAtoAmbientalCollection() {
		return atoAmbientalCollection;
	}

	public void setAtoAmbientalCollection(Collection<AtoAmbiental> atoAmbientalCollection) {
		this.atoAmbientalCollection = atoAmbientalCollection;
	}

	@JSON(include = false)
	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	public boolean isTecnicoAreaSecundaria() {
		return isTecnicoAreaSecundaria;
	}

	public void setTecnicoAreaSecundaria(boolean isTecnicoAreaSecundaria) {
		this.isTecnicoAreaSecundaria = isTecnicoAreaSecundaria;
	}
	
	
	public Area getIdeAreaResponsavel() {
	
		return ideAreaResponsavel;
	}
	
	public void setIdeAreaResponsavel(Area ideAreaResponsavel) {
	
		this.ideAreaResponsavel = ideAreaResponsavel;
	}

    @Transient
    public String getIndicadorLider() {

    	if (getIndLiderEquipe())
    		return "Sim";

    	return "Não";
    }

    @Transient
    public String getDescricaoAtos() {

    	String lDescricaoAtos = "";

    	for (AtoAmbiental lAtoAmbiental : atoAmbientalCollection) {

    		lDescricaoAtos = lDescricaoAtos  + ", " + lAtoAmbiental.getNomAtoAmbiental();
    	}

    	lDescricaoAtos = lDescricaoAtos.replaceFirst(", ", "");

    	return lDescricaoAtos;
    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideEquipeProcesso != null ? ideEquipeProcesso.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EquipeProcesso)) {
			return false;
		}
		EquipeProcesso other = (EquipeProcesso) object;
		if ((this.ideEquipeProcesso == null && other.ideEquipeProcesso != null) || (this.ideEquipeProcesso != null && !this.ideEquipeProcesso.equals(other.ideEquipeProcesso))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.EquipeProcesso[ ideEquipeProcesso=" + ideEquipeProcesso + " ]";
	}
}
