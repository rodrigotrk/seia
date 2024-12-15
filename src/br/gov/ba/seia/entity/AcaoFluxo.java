package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "acao_fluxo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_acao_fluxo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AcaoFluxo.findAll", query = "SELECT a FROM AcaoFluxo a"),
    @NamedQuery(name = "AcaoFluxo.findByIdeAcaoFluxo", query = "SELECT a FROM AcaoFluxo a WHERE a.ideAcaoFluxo = :ideAcaoFluxo"),
    @NamedQuery(name = "AcaoFluxo.findByDscAcaoFluxo", query = "SELECT a FROM AcaoFluxo a WHERE a.dscAcaoFluxo = :dscAcaoFluxo"),
    @NamedQuery(name = "AcaoFluxo.findByDscLinkAcao", query = "SELECT a FROM AcaoFluxo a WHERE a.dscLinkAcao = :dscLinkAcao"),
    @NamedQuery(name = "AcaoFluxo.findByIndExcluido", query = "SELECT a FROM AcaoFluxo a WHERE a.indExcluido = :indExcluido")})
public class AcaoFluxo implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_acao_fluxo", nullable = false)
    private Integer ideAcaoFluxo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_acao_fluxo", nullable = false, length = 100)
    private String dscAcaoFluxo;
    @Size(max = 200)
    @Column(name = "dsc_link_acao", length = 200)
    private String dscLinkAcao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;

    public AcaoFluxo() {
    }

    public AcaoFluxo(Integer ideAcaoFluxo) {
        this.ideAcaoFluxo = ideAcaoFluxo;
    }

    public AcaoFluxo(Integer ideAcaoFluxo, String dscAcaoFluxo, boolean indExcluido) {
        this.ideAcaoFluxo = ideAcaoFluxo;
        this.dscAcaoFluxo = dscAcaoFluxo;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeAcaoFluxo() {
        return ideAcaoFluxo;
    }

    public void setIdeAcaoFluxo(Integer ideAcaoFluxo) {
        this.ideAcaoFluxo = ideAcaoFluxo;
    }

    public String getDscAcaoFluxo() {
        return dscAcaoFluxo;
    }

    public void setDscAcaoFluxo(String dscAcaoFluxo) {
        this.dscAcaoFluxo = dscAcaoFluxo;
    }

    public String getDscLinkAcao() {
        return dscLinkAcao;
    }

    public void setDscLinkAcao(String dscLinkAcao) {
        this.dscLinkAcao = dscLinkAcao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAcaoFluxo != null ? ideAcaoFluxo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof AcaoFluxo)) {
            return false;
        }
        AcaoFluxo other = (AcaoFluxo) object;
        if ((this.ideAcaoFluxo == null && other.ideAcaoFluxo != null) || (this.ideAcaoFluxo != null && !this.ideAcaoFluxo.equals(other.ideAcaoFluxo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.AcaoFluxo[ ideAcaoFluxo=" + ideAcaoFluxo + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.ideAcaoFluxo);
	}
    
}
