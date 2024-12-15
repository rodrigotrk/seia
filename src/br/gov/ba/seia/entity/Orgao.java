package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.enumerator.OrgaoEnum;

@Entity
@Table(name = "orgao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_orgao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orgao.findAll", query = "SELECT o FROM Orgao o"),
    @NamedQuery(name = "Orgao.findByIdeOrgao", query = "SELECT o FROM Orgao o WHERE o.ideOrgao = :ideOrgao"),
    @NamedQuery(name = "Orgao.findByDscOrgao", query = "SELECT o FROM Orgao o WHERE o.dscOrgao = :dscOrgao"),
    @NamedQuery(name = "Orgao.findByDscSiglaOrgao", query = "SELECT o FROM Orgao o WHERE o.dscSiglaOrgao = :dscSiglaOrgao"),
    @NamedQuery(name = "Orgao.findByCodOrgao", query = "SELECT o FROM Orgao o WHERE o.codOrgao = :codOrgao"),
    @NamedQuery(name = "Orgao.findByIndExcluido", query = "SELECT o FROM Orgao o WHERE o.indExcluido = :indExcluido"),
    @NamedQuery(name = "Orgao.findByDtcCriacao", query = "SELECT o FROM Orgao o WHERE o.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Orgao.findByDtcExclusao", query = "SELECT o FROM Orgao o WHERE o.dtcExclusao = :dtcExclusao")})
public class Orgao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORGAO_IDE_ORGAO_seq")    
    @SequenceGenerator(name="ORGAO_IDE_ORGAO_seq", sequenceName="ORGAO_IDE_ORGAO_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_orgao", nullable = false)
    private Integer ideOrgao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_orgao", nullable = false, length = 50)
    private String dscOrgao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_orgao", nullable = false)
    private Integer codOrgao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "dsc_sigla_orgao", nullable = false, length = 10)
    private String dscSiglaOrgao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideOrgao", fetch = FetchType.LAZY)
    private Collection<Area> areaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideOrgao", fetch = FetchType.LAZY)
    private Collection<Requerimento> requerimentoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orgao", fetch = FetchType.LAZY)
    private Collection<RelOrgaoMunicipio> relOrgaoMunicipioCollection;
    @OneToMany(mappedBy = "ideOrgaoPai", fetch = FetchType.LAZY)
    private Collection<Orgao> orgaoCollection;
    @JoinColumn(name = "ide_orgao_pai", referencedColumnName = "ide_orgao")
    @ManyToOne(fetch = FetchType.LAZY)
    private Orgao ideOrgaoPai;
    @JoinColumn(name = "ide_nivel_competencia", referencedColumnName = "ide_nivel_competencia")
    @ManyToOne(fetch = FetchType.EAGER)
    private NivelCompetencia ideNivelCompetencia;
    @OneToMany(mappedBy = "ideOrgao", fetch = FetchType.LAZY)
    private Collection<Processo> processoCollection;

    public Orgao() {
    }
    public Orgao(Integer ideOrgao) {
        this.ideOrgao = ideOrgao;
    }
    public Orgao(String dscOrgao) {
    	this.dscOrgao = dscOrgao;
    }
    
    public Orgao(Integer ideOrgao,Integer codOrgao) {
		super();
		this.ideOrgao = ideOrgao;
		this.codOrgao = codOrgao;
	}
	public Orgao(Integer ideOrgao, String dscOrgao, String dscSiglaOrgao, boolean indExcluido, Date dtcCriacao) {
        this.ideOrgao = ideOrgao;
        this.dscOrgao = dscOrgao;
        this.dscSiglaOrgao = dscSiglaOrgao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

	public Orgao(OrgaoEnum orgaoEnum){
		this.ideOrgao = orgaoEnum.getId();
		this.codOrgao = orgaoEnum.getCodOrgao();
	}
	
    public Integer getIdeOrgao() {
        return ideOrgao;
    }

    public void setIdeOrgao(Integer ideOrgao) {
        this.ideOrgao = ideOrgao;
    }

    public String getDscOrgao() {
        return dscOrgao;
    }

    public void setDscOrgao(String dscOrgao) {
        this.dscOrgao = dscOrgao;
    }

    public Integer getCodOrgao() {
		return codOrgao;
	}

    public void setCodOrgao(Integer codOrgao) {
		this.codOrgao = codOrgao;
	}

	public String getDscSiglaOrgao() {
        return dscSiglaOrgao;
    }

    public void setDscSiglaOrgao(String dscSiglaOrgao) {
        this.dscSiglaOrgao = dscSiglaOrgao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
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

    @XmlTransient
    public Collection<Area> getAreaCollection() {
        return areaCollection;
    }

    public void setAreaCollection(Collection<Area> areaCollection) {
        this.areaCollection = areaCollection;
    }

    @XmlTransient
    public Collection<Requerimento> getRequerimentoCollection() {
        return requerimentoCollection;
    }

    public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
        this.requerimentoCollection = requerimentoCollection;
    }

    @XmlTransient
    public Collection<RelOrgaoMunicipio> getRelOrgaoMunicipioCollection() {
        return relOrgaoMunicipioCollection;
    }

    public void setRelOrgaoMunicipioCollection(Collection<RelOrgaoMunicipio> relOrgaoMunicipioCollection) {
        this.relOrgaoMunicipioCollection = relOrgaoMunicipioCollection;
    }

    @XmlTransient
    public Collection<Orgao> getOrgaoCollection() {
        return orgaoCollection;
    }

    public void setOrgaoCollection(Collection<Orgao> orgaoCollection) {
        this.orgaoCollection = orgaoCollection;
    }

    public Orgao getIdeOrgaoPai() {
        return ideOrgaoPai;
    }

    public void setIdeOrgaoPai(Orgao ideOrgaoPai) {
        this.ideOrgaoPai = ideOrgaoPai;
    }

    public NivelCompetencia getIdeNivelCompetencia() {
    	return ideNivelCompetencia;
    }

    public void setIdeNivelCompetencia(NivelCompetencia ideNivelCompetencia) {
        this.ideNivelCompetencia = ideNivelCompetencia;
    }
    
	public Collection<Processo> getProcessoCollection() {
		return processoCollection;
	}
	public void setProcessoCollection(Collection<Processo> processoCollection) {
		this.processoCollection = processoCollection;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOrgao != null ? ideOrgao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Orgao)) {
            return false;
        }
        Orgao other = (Orgao) object;
        if ((this.ideOrgao == null && other.ideOrgao != null) || (this.ideOrgao != null && !this.ideOrgao.equals(other.ideOrgao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideOrgao);
    }
}