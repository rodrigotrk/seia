package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "requerimento_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequerimentoImovel.findAll", query = "SELECT r FROM RequerimentoImovel r"),
    @NamedQuery(name = "RequerimentoImovel.findByIdeRequerimento", query = "SELECT r FROM RequerimentoImovel r WHERE r.requerimento = :requerimento"),
    @NamedQuery(name = "RequerimentoImovel.findByIdeImovel", query = "SELECT r FROM RequerimentoImovel r WHERE r.imovel = :imovel"),
    @NamedQuery(name = "RequerimentoImovel.findByVlrArea", query = "SELECT r FROM RequerimentoImovel r WHERE r.vlrArea = :vlrArea"),
    @NamedQuery(name = "RequerimentoImovel.findByDscPontoReferencia", query = "SELECT r FROM RequerimentoImovel r WHERE r.dscPontoReferencia = :dscPontoReferencia"),
    @NamedQuery(name = "RequerimentoImovel.findByIdeLocalizacaoGeografica", query = "SELECT r FROM RequerimentoImovel r WHERE r.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
    @NamedQuery(name = "RequerimentoImovel.findByDtcCriacao", query = "SELECT r FROM RequerimentoImovel r WHERE r.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "RequerimentoImovel.deleteByIde", query = "DELETE FROM RequerimentoImovel r WHERE r.ideRequerimentoImovel = :ideRequerimentoImovel"),
    @NamedQuery(name = "RequerimentoImovel.findByIndExcluido", query = "SELECT r FROM RequerimentoImovel r WHERE r.indExcluido = :indExcluido"),
    @NamedQuery(name = "RequerimentoImovel.findByDtcExclusao", query = "SELECT r FROM RequerimentoImovel r WHERE r.dtcExclusao = :dtcExclusao")})
public class RequerimentoImovel implements Serializable,Cloneable {
   
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @SequenceGenerator(name = "REQUERIMENTO_IMOVEL_IDE_REQUERIMENTO_IMOVEL_GENERATOR", sequenceName = "requerimento_imovel_ide_requerimento_imovel_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUERIMENTO_IMOVEL_IDE_REQUERIMENTO_IMOVEL_GENERATOR")
    @NotNull
    @Column(name = "ide_requerimento_imovel")
    private Integer ideRequerimentoImovel;
    
	@JoinColumn(name = "ide_tipo_imovel_requerimento", referencedColumnName = "ide_tipo_imovel_requerimento")
    @Column(name = "vlr_area", precision = 10, scale = 2)
    private BigDecimal vlrArea;
    
	@Column(name = "dsc_ponto_referencia", length = 200)
    private String dscPontoReferencia;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido")
    private boolean indExcluido;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @JoinColumn(name = "ide_tipo_imovel_requerimento", referencedColumnName = "ide_tipo_imovel_requerimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoImovelRequerimento ideTipoImovelRequerimento;
    
    @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Requerimento requerimento;
    
    @JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false, updatable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Imovel imovel;
    
    @OneToMany( mappedBy = "ideRequerimentoImovel")
    private Collection<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaCollection;
    
    @Transient
    private boolean requerimentoUnico;
    
    @Transient
    private List<LocalizacaoGeografica> localizacaoGeograficaNaLista;
    
    @Transient
    private PerguntaRequerimento perguntaRequerimento;
    
    @Transient
	private String fracaoGrauLatitude;

    @Transient
	private String fracaoGrauLongitude;

    public RequerimentoImovel() {    	
    }

    public BigDecimal getVlrArea() {
        return vlrArea;
    }
    
    public Boolean getVlrAreaIsNotNull() {
        return Util.isNull(vlrArea) ? false : true;
    }

    public void setVlrArea(BigDecimal vlrArea) {
        this.vlrArea = vlrArea;
    }

    public String getDscPontoReferencia() {
        return dscPontoReferencia;
    }

    public void setDscPontoReferencia(String dscPontoReferencia) {
        this.dscPontoReferencia = dscPontoReferencia;
    }

    public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean isIndExcluido() {
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

	public TipoImovelRequerimento getIdeTipoImovelRequerimento() {
        return ideTipoImovelRequerimento;
    }

    public void setIdeTipoImovelRequerimento(TipoImovelRequerimento ideTipoImovelRequerimento) {
        this.ideTipoImovelRequerimento = ideTipoImovelRequerimento;
    }

    public Requerimento getRequerimento() {
        return requerimento;
    }

    public void setRequerimento(Requerimento requerimento) {
        this.requerimento = requerimento;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public boolean isRequerimentoUnico() {
		return requerimentoUnico;
	}

	public void setRequerimentoUnico(boolean requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
	}

	public String getFracaoGrauLatitude() {
		return fracaoGrauLatitude;
	}

	public void setFracaoGrauLatitude(String fracaoGrauLatitude) {
		this.fracaoGrauLatitude = fracaoGrauLatitude;
	}

	public String getFracaoGrauLongitude() {
		return fracaoGrauLongitude;
	}

	public void setFracaoGrauLongitude(String fracaoGrauLongitude) {
		this.fracaoGrauLongitude = fracaoGrauLongitude;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRequerimentoImovel != null ? ideRequerimentoImovel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RequerimentoImovel)) {
            return false;
        }
        RequerimentoImovel other = (RequerimentoImovel) object;
        if ((this.ideRequerimentoImovel == null && other.ideRequerimentoImovel != null) || (this.ideRequerimentoImovel != null && !this.ideRequerimentoImovel.equals(other.ideRequerimentoImovel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoImovel[ ideRequerimentoImovel=" + ideRequerimentoImovel + " ]";
    }

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		this.localizacaoGeograficaNaLista = new ArrayList<LocalizacaoGeografica>();
		this.localizacaoGeograficaNaLista.add(0, ideLocalizacaoGeografica);
	}

	public Integer getIdeRequerimentoImovel() {
		return ideRequerimentoImovel;
	}

	public void setIdeRequerimentoImovel(Integer ideRequerimentoImovel) {
		this.ideRequerimentoImovel = ideRequerimentoImovel;
	}

	public Collection<ObjetivoRequerimentoLimpezaArea> getObjetivoRequerimentoLimpezaAreaCollection() {
		return objetivoRequerimentoLimpezaAreaCollection;
	}

	public void setObjetivoRequerimentoLimpezaAreaCollection(
			Collection<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaCollection) {
		this.objetivoRequerimentoLimpezaAreaCollection = objetivoRequerimentoLimpezaAreaCollection;
	}

	public PerguntaRequerimento getPerguntaRequerimento() {
		return perguntaRequerimento;
	}

	public void setPerguntaRequerimento(PerguntaRequerimento perguntaRequerimento) {
		this.perguntaRequerimento = perguntaRequerimento;
	}
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }

	public List<LocalizacaoGeografica> getLocalizacaoGeograficaNaLista() {
		return localizacaoGeograficaNaLista;
	}

	public void setLocalizacaoGeograficaNaLista(
			List<LocalizacaoGeografica> localizacaoGeograficaNaLista) {
		this.localizacaoGeograficaNaLista = localizacaoGeograficaNaLista;
	}
}
