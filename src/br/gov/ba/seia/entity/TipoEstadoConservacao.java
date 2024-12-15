package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;

/**
 * @author micael.coutinho
 */
@Entity
@Table(name = "tipo_estado_conservacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEstadoConservacao.findAll", query = "SELECT t FROM TipoEstadoConservacao t"),
    @NamedQuery(name = "TipoEstadoConservacao.findByIdeTipoEstadoConservacao", query = "SELECT t FROM TipoEstadoConservacao t WHERE t.ideTipoEstadoConservacao = :ideTipoEstadoConservacao"),
    @NamedQuery(name = "TipoEstadoConservacao.findByDscTipoEstadoConservacao", query = "SELECT t FROM TipoEstadoConservacao t WHERE t.dscTipoEstadoConservacao = :dscTipoEstadoConservacao"),
    @NamedQuery(name = "TipoEstadoConservacao.findByIndExcluido", query = "SELECT t FROM TipoEstadoConservacao t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "TipoEstadoConservacao.findByDtcCriacao", query = "SELECT t FROM TipoEstadoConservacao t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoEstadoConservacao.findByDtcExclusao", query = "SELECT t FROM TipoEstadoConservacao t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoEstadoConservacao extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ESTADO_CONSERVACAO_IDE_TIPO_ESTADO_CONSERVACAO_SEQ") 
    @SequenceGenerator(name="TIPO_ESTADO_CONSERVACAO_IDE_TIPO_ESTADO_CONSERVACAO_SEQ", sequenceName="TIPO_ESTADO_CONSERVACAO_IDE_TIPO_ESTADO_CONSERVACAO_SEQ", allocationSize=1)
	@Basic(optional = false)
    @NotNull
	@Column(name = "ide_tipo_estado_conservacao", nullable = false)
	private Integer ideTipoEstadoConservacao;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_estado_conservacao")
    private String dscTipoEstadoConservacao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido")
    private boolean indExcluido;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIME)
    private Date dtcCriacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIME)
    private Date dtcExclusao;
    
	@OneToMany(mappedBy = "ideTipoEstadoConservacao")
    private Collection<ReservaLegal> reservaLegalCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoEstadoConservacao")
    private Collection<App> appCollection;

    public TipoEstadoConservacao() {}
    
    public TipoEstadoConservacao(TipoEstadoConservacaoEnum tipoEstadoConservacaoEnum) {
    	this.ideTipoEstadoConservacao = tipoEstadoConservacaoEnum.getId();
    	this.dscTipoEstadoConservacao = tipoEstadoConservacaoEnum.getDscTipoEstadoConservacao();
    }

    public TipoEstadoConservacao(Integer ideTipoEstadoConservacao) {
        this.ideTipoEstadoConservacao = ideTipoEstadoConservacao;
    }

    public TipoEstadoConservacao(Integer ideTipoEstadoConservacao, String dscTipoEstadoConservacao, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoEstadoConservacao = ideTipoEstadoConservacao;
        this.dscTipoEstadoConservacao = dscTipoEstadoConservacao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoEstadoConservacao() {
        return ideTipoEstadoConservacao;
    }

    public void setIdeTipoEstadoConservacao(Integer ideTipoEstadoConservacao) {
        this.ideTipoEstadoConservacao = ideTipoEstadoConservacao;
    }

    public String getDscTipoEstadoConservacao() {
        return dscTipoEstadoConservacao;
    }

    public void setDscTipoEstadoConservacao(String dscTipoEstadoConservacao) {
        this.dscTipoEstadoConservacao = dscTipoEstadoConservacao;
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
    public Collection<ReservaLegal> getReservaLegalCollection() {
        return reservaLegalCollection;
    }

    public void setReservaLegalCollection(Collection<ReservaLegal> reservaLegalCollection) {
        this.reservaLegalCollection = reservaLegalCollection;
    }

    @XmlTransient
    public Collection<App> getAppCollection() {
        return appCollection;
    }

    public void setAppCollection(Collection<App> appCollection) {
        this.appCollection = appCollection;
    }
}
