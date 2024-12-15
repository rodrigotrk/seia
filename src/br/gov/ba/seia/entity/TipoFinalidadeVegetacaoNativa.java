package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_finalidade_vegetacao_nativa", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsc_tipo_finalidade_vegetacao_nativa"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoFinalidadeVegetacaoNativa.findAll", query = "SELECT t FROM TipoFinalidadeUsoAgua t order by t.nomTipoFinalidadeUsoAgua"),
    @NamedQuery(name = "TipoFinalidadeVegetacaoNativa.findByIdeTipoFinalidadeVegetacaoNativa", query = "SELECT t FROM TipoFinalidadeVegetacaoNativa t WHERE t.ideTipoFinalidadeVegetacaoNativa = :ideTipoFinalidadeVegetacaoNativa"),
    @NamedQuery(name = "TipoFinalidadeVegetacaoNativa.findByDscTipoFinalidadeVegetacaoNativa", query = "SELECT t FROM TipoFinalidadeVegetacaoNativa t WHERE t.dscTipoFinalidadeVegetacaoNativa = :dscTipoFinalidadeVegetacaoNativa")})
public class TipoFinalidadeVegetacaoNativa implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_finalidade_vegetacao_nativa", nullable = false)
    private Integer ideTipoFinalidadeVegetacaoNativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "dsc_tipo_finalidade_vegetacao_nativa", nullable = false)
    private String dscTipoFinalidadeVegetacaoNativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "dsc_tipo_finalidade_vegetacao_nativa_sicar", nullable = false)
    private String dscTipoFinalidadeVegetacaoNativaSicar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

    public TipoFinalidadeVegetacaoNativa() {
    }

    public TipoFinalidadeVegetacaoNativa(Integer ideTipoFinalidadeVegetacaoNativa) {
        this.ideTipoFinalidadeVegetacaoNativa = ideTipoFinalidadeVegetacaoNativa;
    }

    public TipoFinalidadeVegetacaoNativa(Integer ideTipoFinalidadeVegetacaoNativa, String dscTipoFinalidadeVegetacaoNativa) {
        this.ideTipoFinalidadeVegetacaoNativa = ideTipoFinalidadeVegetacaoNativa;
        this.dscTipoFinalidadeVegetacaoNativa = dscTipoFinalidadeVegetacaoNativa;
    }

    

	public Integer getIdeTipoFinalidadeVegetacaoNativa() {
		return ideTipoFinalidadeVegetacaoNativa;
	}

	public void setIdeTipoFinalidadeVegetacaoNativa(
			Integer ideTipoFinalidadeVegetacaoNativa) {
		this.ideTipoFinalidadeVegetacaoNativa = ideTipoFinalidadeVegetacaoNativa;
	}

	public String getDscTipoFinalidadeVegetacaoNativa() {
		return dscTipoFinalidadeVegetacaoNativa;
	}

	public void setDscTipoFinalidadeVegetacaoNativa(
			String dscTipoFinalidadeVegetacaoNativa) {
		this.dscTipoFinalidadeVegetacaoNativa = dscTipoFinalidadeVegetacaoNativa;
	}

	public String getDscTipoFinalidadeVegetacaoNativaSicar() {
		return dscTipoFinalidadeVegetacaoNativaSicar;
	}

	public void setDscTipoFinalidadeVegetacaoNativaSicar(
			String dscTipoFinalidadeVegetacaoNativaSicar) {
		this.dscTipoFinalidadeVegetacaoNativaSicar = dscTipoFinalidadeVegetacaoNativaSicar;
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

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoFinalidadeVegetacaoNativa != null ? ideTipoFinalidadeVegetacaoNativa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoFinalidadeVegetacaoNativa)) {
            return false;
        }
        TipoFinalidadeVegetacaoNativa other = (TipoFinalidadeVegetacaoNativa) object;
        if ((this.ideTipoFinalidadeVegetacaoNativa == null && other.ideTipoFinalidadeVegetacaoNativa != null) || (this.ideTipoFinalidadeVegetacaoNativa != null && !this.ideTipoFinalidadeVegetacaoNativa.equals(other.ideTipoFinalidadeVegetacaoNativa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa[ ideTipoFinalidadeVegetacaoNativa=" + ideTipoFinalidadeVegetacaoNativa + " ]";
    }
    
    @Override
	public Long getId() {
		return new Long(this.ideTipoFinalidadeVegetacaoNativa);
	}
    
}
