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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "tipologia_grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipologiaGrupo.findAll", query = "SELECT t FROM TipologiaGrupo t"),
    @NamedQuery(name = "TipologiaGrupo.findByIdeTipologiaGrupo", query = "SELECT t FROM TipologiaGrupo t WHERE t.ideTipologiaGrupo = :ideTipologiaGrupo"),
    @NamedQuery(name = "TipologiaGrupo.findByDtcCriacao", query = "SELECT t FROM TipologiaGrupo t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipologiaGrupo.findByDtcExcluido", query = "SELECT t FROM TipologiaGrupo t WHERE t.dtcExcluido = :dtcExcluido"),
    @NamedQuery(name = "TipologiaGrupo.findByIndExcluido", query = "SELECT t FROM TipologiaGrupo t WHERE t.indExcluido = :indExcluido")})
public class TipologiaGrupo extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq")    
    @SequenceGenerator(name="TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq", sequenceName="TIPOLOGIA_GRUPO_IDE_TIPOLOGIA_GRUPO_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipologia_grupo", nullable = false)
    private Integer ideTipologiaGrupo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
    @Column(name = "dtc_excluido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExcluido;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tipologia ideTipologia;
    
    @JoinColumn(name = "ide_potencial_poluicao", referencedColumnName = "ide_potencial_poluicao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PotencialPoluicao idePotencialPoluicao;
    
    @OneToOne(mappedBy="ideTipologiaGrupo")
    private UnidadeMedidaTipologiaGrupo unidadeMedidaTipologiaGrupo;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipologiaGrupo", fetch = FetchType.LAZY)
    private Collection<TipologiaTipoAto> tipologiaTipoAtoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipologiaGrupo", fetch = FetchType.LAZY)
    private Collection<PorteTipologia> porteTipologiaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipologiaGrupo", fetch = FetchType.LAZY)
    private Collection<EmpreendimentoTipologia> empreendimentoTipologiaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipologiaGrupo", fetch = FetchType.LAZY)
    private Collection<PorteCompetencia> porteCompetenciaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipologiaGrupo", fetch = FetchType.LAZY)
    private Collection<TipologiaGrupoArea> tipologiaGrupoAreaCollection;

    public TipologiaGrupo() {}
    
    public TipologiaGrupo(Tipologia ideTipologia) {
    	this.ideTipologia = ideTipologia;
    }
    public TipologiaGrupo(Integer ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public TipologiaGrupo(Integer ideTipologiaGrupo, Date dtcCriacao, boolean indExcluido) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

	public TipologiaGrupo(TipologiaEnum tipologiaEnum) {
		this.ideTipologia = new Tipologia(tipologiaEnum);
	}

	public Integer getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(Integer ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Date getDtcExcluido() {
        return dtcExcluido;
    }

    public void setDtcExcluido(Date dtcExcluido) {
        this.dtcExcluido = dtcExcluido;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    @XmlTransient
    public Collection<TipologiaTipoAto> getTipologiaTipoAtoCollection() {
        return tipologiaTipoAtoCollection;
    }

    public void setTipologiaTipoAtoCollection(Collection<TipologiaTipoAto> tipologiaTipoAtoCollection) {
        this.tipologiaTipoAtoCollection = tipologiaTipoAtoCollection;
    }

    @XmlTransient
    public Collection<PorteTipologia> getPorteTipologiaCollection() {
        return porteTipologiaCollection;
    }

    public void setPorteTipologiaCollection(Collection<PorteTipologia> porteTipologiaCollection) {
        this.porteTipologiaCollection = porteTipologiaCollection;
    }

    @XmlTransient
    public Collection<EmpreendimentoTipologia> getEmpreendimentoTipologiaCollection() {
        return empreendimentoTipologiaCollection;
    }

    public void setEmpreendimentoTipologiaCollection(Collection<EmpreendimentoTipologia> empreendimentoTipologiaCollection) {
        this.empreendimentoTipologiaCollection = empreendimentoTipologiaCollection;
    }

    @XmlTransient
    public Collection<PorteCompetencia> getPorteCompetenciaCollection() {
        return porteCompetenciaCollection;
    }

    public void setPorteCompetenciaCollection(Collection<PorteCompetencia> porteCompetenciaCollection) {
        this.porteCompetenciaCollection = porteCompetenciaCollection;
    }

    public Tipologia getIdeTipologia() {
        return ideTipologia;
    }

    public void setIdeTipologia(Tipologia ideTipologia) {
        this.ideTipologia = ideTipologia;
    }

    public PotencialPoluicao getIdePotencialPoluicao() {
        return idePotencialPoluicao;
    }

    public void setIdePotencialPoluicao(PotencialPoluicao idePotencialPoluicao) {
        this.idePotencialPoluicao = idePotencialPoluicao;
    }

    @XmlTransient
    public Collection<TipologiaGrupoArea> getTipologiaGrupoAreaCollection() {
        return tipologiaGrupoAreaCollection;
    }

    public void setTipologiaGrupoAreaCollection(Collection<TipologiaGrupoArea> tipologiaGrupoAreaCollection) {
        this.tipologiaGrupoAreaCollection = tipologiaGrupoAreaCollection;
    }

    public UnidadeMedidaTipologiaGrupo getUnidadeMedidaTipologiaGrupo() {
		return unidadeMedidaTipologiaGrupo;
	}
	public void setUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo unidadeMedidaTipologiaGrupo) {
		this.unidadeMedidaTipologiaGrupo = unidadeMedidaTipologiaGrupo;
	}
	@Transient
    public String getDescricaoNo() {

    	if (!Util.isNullOuVazio(getIdeTipologia())) {

    		String lDescricaoNo = getIdeTipologia().getDesTipologia();

    		return lDescricaoNo;
    	}
    	else {

    		return "";
    	}
    }
}