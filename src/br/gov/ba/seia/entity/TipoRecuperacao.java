package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "tipo_recuperacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRecuperacao.findAll", query = "SELECT t FROM TipoRecuperacao t"),
    @NamedQuery(name = "TipoRecuperacao.findByIdeTipoRecuperacao", query = "SELECT t FROM TipoRecuperacao t WHERE t.ideTipoRecuperacao = :ideTipoRecuperacao"),
    @NamedQuery(name = "TipoRecuperacao.findByDscTipoRecuperacao", query = "SELECT t FROM TipoRecuperacao t WHERE t.dscTipoRecuperacao = :dscTipoRecuperacao"),
    @NamedQuery(name = "TipoRecuperacao.findByIndExcluido", query = "SELECT t FROM TipoRecuperacao t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "TipoRecuperacao.findByDtcCriacao", query = "SELECT t FROM TipoRecuperacao t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoRecuperacao.findByDtcExclusao", query = "SELECT t FROM TipoRecuperacao t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoRecuperacao implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_RECUPERACAO_IDE_TIPO_RECUPERACAO_SEQ") 
    @SequenceGenerator(name="TIPO_RECUPERACAO_IDE_TIPO_RECUPERACAO_SEQ", sequenceName="TIPO_RECUPERACAO_IDE_TIPO_RECUPERACAO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_recuperacao", nullable = false)
    private Integer ideTipoRecuperacao;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_recuperacao")
    private String dscTipoRecuperacao;
    
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
    	

    public TipoRecuperacao() {
    }

    public TipoRecuperacao(Integer ideTipoRecuperacao) {
        this.ideTipoRecuperacao = ideTipoRecuperacao;
    }

    public TipoRecuperacao(Integer ideTipoRecuperacao, String dscTipoRecuperacao, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoRecuperacao = ideTipoRecuperacao;
        this.dscTipoRecuperacao = dscTipoRecuperacao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoRecuperacao() {
        return ideTipoRecuperacao;
    }

    public void setIdeTipoRecuperacao(Integer ideTipoRecuperacao) {
        this.ideTipoRecuperacao = ideTipoRecuperacao;
    }

    public String getDscTipoRecuperacao() {
        return dscTipoRecuperacao;
    }

    public void setDscTipoRecuperacao(String dscTipoRecuperacao) {
        this.dscTipoRecuperacao = dscTipoRecuperacao;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoRecuperacao != null ? ideTipoRecuperacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoRecuperacao)) {
            return false;
        }
        TipoRecuperacao other = (TipoRecuperacao) object;
        if ((this.ideTipoRecuperacao == null && other.ideTipoRecuperacao != null) || (this.ideTipoRecuperacao != null && !this.ideTipoRecuperacao.equals(other.ideTipoRecuperacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideTipoRecuperacao;
    }
    
}
