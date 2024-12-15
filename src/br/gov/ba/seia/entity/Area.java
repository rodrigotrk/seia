package br.gov.ba.seia.entity;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "area", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_area"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a"),
    @NamedQuery(name = "Area.findByIdeArea", query = "SELECT a FROM Area a WHERE a.ideArea = :ideArea"),
    @NamedQuery(name = "Area.findPessoaFisicaByIdeArea", query = "SELECT a FROM Area a WHERE a.ideArea = :ideArea AND a.idePessoaFisica IS NOT NULL"),
    @NamedQuery(name = "Area.findByNomArea", query = "SELECT a FROM Area a WHERE a.nomArea = :nomArea"),
    @NamedQuery(name = "Area.findByDtcCriacao", query = "SELECT a FROM Area a WHERE a.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Area.findByIndExcluido", query = "SELECT a FROM Area a WHERE a.indExcluido = :indExcluido"),
    
    @NamedQuery(name = "Area.findByIdeRequerimento", query = ""
    		+ "SELECT DISTINCT a FROM Area a INNER JOIN a.tipologiaGrupoAreaCollection tga "
    		+ "WHERE a.ideArea <> :ideAreaConstante AND tga.ideTipologiaGrupo IN "
    		+ "( SELECT umtg.ideTipologiaGrupo FROM RequerimentoTipologia rt INNER JOIN rt.ideUnidadeMedidaTipologiaGrupo umtg "
    		+ "WHERE rt.indTipologiaPrincipal = true AND rt.ideRequerimento.ideRequerimento = :ideRequerimento AND a.indExcluido = false "
    		+ "GROUP BY umtg.ideTipologiaGrupo)"),
    		
	@NamedQuery(name = "Area.findByIdeRequerimentoSemTipologiaPrincipal", query = ""
    		+ "SELECT DISTINCT a FROM Area a INNER JOIN a.tipologiaGrupoAreaCollection tga "
    		+ "WHERE a.ideArea <> :ideAreaConstante AND tga.ideTipologiaGrupo IN "
    		+ "( SELECT umtg.ideTipologiaGrupo FROM RequerimentoTipologia rt INNER JOIN rt.ideUnidadeMedidaTipologiaGrupo umtg "
    		+ "WHERE rt.ideRequerimento.ideRequerimento = :ideRequerimento AND a.indExcluido = false "
    		+ "GROUP BY umtg.ideTipologiaGrupo)"),
    		
    @NamedQuery(name = "Area.findByDtcExclusao", query = "SELECT a FROM Area a WHERE a.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Area.findByIdeTipoArea", query = "SELECT a FROM Area a WHERE a.ideTipoArea = :ideTipoArea"),
    @NamedQuery(name = "Area.findByUR", query = "SELECT DISTINCT a.area FROM AreaMunicipio a INNER JOIN a.area"),
    @NamedQuery(name = "Area.findByMunicipio", query = "SELECT DISTINCT a.area FROM AreaMunicipio a INNER JOIN a.area WHERE a.areaMunicipioPK.ideMunicipio= :ideMunicipio"),
    @NamedQuery(name = "Area.findByOrgao", query = "SELECT a FROM Area a WHERE a.ideOrgao = :ideOrgao and a.indExcluido = :indExcluido order by a.nomArea asc"),
	@NamedQuery(name = "Area.findByOrgaoWithoutCotic", query = "SELECT a FROM Area a WHERE a.ideOrgao = :ideOrgao and a.indExcluido = :indExcluido and a.ideArea!=32 order by a.nomArea asc")})
public class Area extends AbstractEntity implements Cloneable, Comparable<Area> {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_IDE_AREA_seq")    
    @SequenceGenerator(name="AREA_IDE_AREA_seq", sequenceName="AREA_IDE_AREA_seq", allocationSize=1)
    @Column(name = "ide_area", nullable = false)
    private Integer ideArea;
    
	@Basic(optional = false)
    @Column(name = "nom_area", nullable = false, length = 100)
    private String nomArea;
    
	@Basic(optional = false)
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	@NotAudited
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @Column(name = "ind_excluido", nullable = false)
	@NotAudited
    private boolean indExcluido;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
	@NotAudited
    private Date dtcExclusao;
    
	@Column(name = "ide_tipo_area")
	@NotAudited
    private Integer ideTipoArea;
	
	@JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@NotAudited
	private Orgao ideOrgao;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.EAGER)
	@NotAudited
	private Funcionario idePessoaFisica;
	
	@JoinColumn(name = "ide_area_pai", referencedColumnName = "ide_area")
	@ManyToOne(fetch = FetchType.EAGER)
	@NotAudited
	private Area ideAreaPai;
    
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<TipologiaGrupoArea> tipologiaGrupoAreaCollection;
    
	@NotAudited
	@OneToMany(mappedBy = "ideAreaPai", fetch = FetchType.LAZY)
    private Collection<Area> areaCollection;
    
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<Funcionario> funcionarioCollection;
    
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<ControleTramitacao> controleTramitacaoCollection;
    
	@NotAudited
	@OneToMany(mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<Pauta> pautaCollection;
    
	@NotAudited
	@OneToMany(mappedBy = "ideAreaDestino", fetch = FetchType.LAZY)
    private Collection<ControleFluxo> controleFluxoCollection;
    
	@NotAudited
    @OneToMany(mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection;
    
	@NotAudited
    @OneToMany(mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<Equipe> equipeCollection;
    
	@NotAudited
    @OneToMany(mappedBy = "ideArea", fetch = FetchType.LAZY)
    private Collection<IntegranteEquipe> integranteEquipeCollection;
        
    public Area() { }

    public Area(Integer ideArea) {
        this.ideArea = ideArea;
    }
    public Area(String nomArea) {
    	this.nomArea = nomArea;
    }
    public Area(Integer ideArea, String nomArea, Date dtcCriacao, boolean indExcluido) {
        this.ideArea = ideArea;
        this.nomArea = nomArea;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeArea() {
        return ideArea;
    }

    public void setIdeArea(Integer ideArea) {
        this.ideArea = ideArea;
    }

    public String getNomArea() {
        return nomArea;
    }

    public void setNomArea(String nomArea) {
        this.nomArea = nomArea;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public Integer getIdeTipoArea() {
        return ideTipoArea;
    }

    public void setIdeTipoArea(Integer ideTipoArea) {
        this.ideTipoArea = ideTipoArea;
    }

    @XmlTransient
    public Collection<TipologiaGrupoArea> getTipologiaGrupoAreaCollection() {
        return tipologiaGrupoAreaCollection;
    }

    public void setTipologiaGrupoAreaCollection(Collection<TipologiaGrupoArea> tipologiaGrupoAreaCollection) {
        this.tipologiaGrupoAreaCollection = tipologiaGrupoAreaCollection;
    }

    public Orgao getIdeOrgao() {
        return ideOrgao;
    }

    public void setIdeOrgao(Orgao ideOrgao) {
        this.ideOrgao = ideOrgao;
    }

    public Funcionario getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(Funcionario idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    @XmlTransient
    public Collection<Area> getAreaCollection() {
        return areaCollection;
    }

    public void setAreaCollection(Collection<Area> areaCollection) {
        this.areaCollection = areaCollection;
    }

    public Area getIdeAreaPai() {
        return ideAreaPai;
    }

    public void setIdeAreaPai(Area ideAreaPai) {
        this.ideAreaPai = ideAreaPai;
    }

    @XmlTransient
    public Collection<Funcionario> getFuncionarioCollection() {
        return funcionarioCollection;
    }

    public void setFuncionarioCollection(Collection<Funcionario> funcionarioCollection) {
        this.funcionarioCollection = funcionarioCollection;
    }

    @XmlTransient
    public Collection<ControleTramitacao> getControleTramitacaoCollection() {
        return controleTramitacaoCollection;
    }

    public void setControleTramitacaoCollection(Collection<ControleTramitacao> controleTramitacaoCollection) {
        this.controleTramitacaoCollection = controleTramitacaoCollection;
    }

    @XmlTransient
    public Collection<Pauta> getPautaCollection() {
        return pautaCollection;
    }

    public void setPautaCollection(Collection<Pauta> pautaCollection) {
        this.pautaCollection = pautaCollection;
    }

    @XmlTransient
    public Collection<ControleFluxo> getControleFluxoCollection() {
        return controleFluxoCollection;
    }

    public void setControleFluxoCollection(Collection<ControleFluxo> controleFluxoCollection) {
        this.controleFluxoCollection = controleFluxoCollection;
    }

	public Collection<CategoriaDocumentoPerfilArea> getCategoriaDocumentoPerfilAreaCollection() {
		return categoriaDocumentoPerfilAreaCollection;
	}

	public void setCategoriaDocumentoPerfilAreaCollection(
			Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection) {
		this.categoriaDocumentoPerfilAreaCollection = categoriaDocumentoPerfilAreaCollection;
	}

	@Override
	public int compareTo(Area area) {
		if(Util.isNullOuVazio(area.getNomArea())){
			return 0;
		} else {
			return this.getNomArea().compareTo(area.getNomArea());
		}
	}
	
	public Area clone() throws CloneNotSupportedException {
		return (Area) super.clone();
	}

	public Collection<Equipe> getEquipeCollection() {
		return equipeCollection;
	}

	public void setEquipeCollection(Collection<Equipe> equipeCollection) {
		this.equipeCollection = equipeCollection;
	}

	public Collection<IntegranteEquipe> getIntegranteEquipeCollection() {
		return integranteEquipeCollection;
	}

	public void setIntegranteEquipeCollection(
			Collection<IntegranteEquipe> integranteEquipeCollection) {
		this.integranteEquipeCollection = integranteEquipeCollection;
	}    
}
