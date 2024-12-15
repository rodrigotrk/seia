package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_captacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_captacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCaptacao.findAll", query = "SELECT t FROM TipoCaptacao t order by t.nomTipoCaptacao"),
    @NamedQuery(name = "TipoCaptacao.findByIdeTipoCaptacao", query = "SELECT t FROM TipoCaptacao t WHERE t.ideTipoCaptacao = :ideTipoCaptacao"),
    @NamedQuery(name = "TipoCaptacao.findByNomTipoCaptacao", query = "SELECT t FROM TipoCaptacao t WHERE t.nomTipoCaptacao = :nomTipoCaptacao"),
    @NamedQuery(name = "TipoCaptacao.findTipoCaptacaoByRequerimentoUnico", query = "SELECT t FROM TipoCaptacao t left join t.requerimentoUnicoCollection ru WHERE ru.ideRequerimentoUnico = :ideRequerimentoUnico")})
public class TipoCaptacao implements Serializable, BaseEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_captacao", nullable = false)
    private Integer ideTipoCaptacao;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_tipo_captacao", nullable = false, length = 50)
    private String nomTipoCaptacao;
    
	@ManyToMany(mappedBy = "tipoCaptacaoCollection", fetch = FetchType.LAZY)
    private Collection<RequerimentoUnico> requerimentoUnicoCollection;
    
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tipologia ideTipologia;
    
    public TipoCaptacao() {
    }

    public TipoCaptacao(Integer ideTipoCaptacao) {
        this.ideTipoCaptacao = ideTipoCaptacao;
    }

    public TipoCaptacao(Integer ideTipoCaptacao, String nomTipoCaptacao) {
        this.ideTipoCaptacao = ideTipoCaptacao;
        this.nomTipoCaptacao = nomTipoCaptacao;
    }

	public Integer getIdeTipoCaptacao() {
        return ideTipoCaptacao;
    }

    public void setIdeTipoCaptacao(Integer ideTipoCaptacao) {
        this.ideTipoCaptacao = ideTipoCaptacao;
    }

    public String getNomTipoCaptacao() {
        return nomTipoCaptacao;
    }

    public void setNomTipoCaptacao(String nomTipoCaptacao) {
        this.nomTipoCaptacao = nomTipoCaptacao;
    }

    public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
		return requerimentoUnicoCollection;
	}

	public void setRequerimentoUnicoCollection(
			Collection<RequerimentoUnico> requerimentoUnicoCollection) {
		this.requerimentoUnicoCollection = requerimentoUnicoCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoCaptacao != null ? ideTipoCaptacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCaptacao)) {
            return false;
        }
        TipoCaptacao other = (TipoCaptacao) object;
        if ((this.ideTipoCaptacao == null && other.ideTipoCaptacao != null) || (this.ideTipoCaptacao != null && !this.ideTipoCaptacao.equals(other.ideTipoCaptacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCaptacao[ ideTipoCaptacao=" + ideTipoCaptacao + " ]";
    }

	@Override
	public Long getId() {
		return  new Long(this.ideTipoCaptacao);
	}

	/**
	 * @return the ideTipologia
	 */
	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	/**
	 * @param ideTipologia the ideTipologia to set
	 */
	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}
	
}