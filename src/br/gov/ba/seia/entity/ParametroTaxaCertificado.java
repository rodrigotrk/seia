package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "parametro_taxa_certificado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroTaxaCertificado.findAll", query = "SELECT p FROM ParametroTaxaCertificado p"),
    @NamedQuery(name = "ParametroTaxaCertificado.findByIdeParametroTaxaCertificado", query = "SELECT p FROM ParametroTaxaCertificado p WHERE p.ideParametroTaxaCertificado = :ideParametroTaxaCertificado"),
    @NamedQuery(name = "ParametroTaxaCertificado.findByValTaxaCertificado", query = "SELECT p FROM ParametroTaxaCertificado p WHERE p.valTaxaCertificado = :valTaxaCertificado")})
public class ParametroTaxaCertificado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_parametro_taxa_certificado", nullable = false)
    private Integer ideParametroTaxaCertificado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_taxa_certificado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valTaxaCertificado;

    @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false)
    @OneToOne(fetch=FetchType.LAZY)
    private AtoAmbiental atoAmbiental;
    
    public ParametroTaxaCertificado() {
    }

    public ParametroTaxaCertificado(Integer ideParametroTaxaCertificado) {
        this.ideParametroTaxaCertificado = ideParametroTaxaCertificado;
    }

    public ParametroTaxaCertificado(Integer ideParametroTaxaCertificado, BigDecimal valTaxaCertificado) {
        this.ideParametroTaxaCertificado = ideParametroTaxaCertificado;
        this.valTaxaCertificado = valTaxaCertificado;
    }

    public Integer getIdeParametroTaxaCertificado() {
        return ideParametroTaxaCertificado;
    }

    public void setIdeParametroTaxaCertificado(Integer ideParametroTaxaCertificado) {
        this.ideParametroTaxaCertificado = ideParametroTaxaCertificado;
    }

    public BigDecimal getValTaxaCertificado() {
        return valTaxaCertificado;
    }

    public void setValTaxaCertificado(BigDecimal valTaxaCertificado) {
        this.valTaxaCertificado = valTaxaCertificado;
    }

    public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParametroTaxaCertificado != null ? ideParametroTaxaCertificado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ParametroTaxaCertificado)) {
            return false;
        }
        ParametroTaxaCertificado other = (ParametroTaxaCertificado) object;
        if ((this.ideParametroTaxaCertificado == null && other.ideParametroTaxaCertificado != null) || (this.ideParametroTaxaCertificado != null && !this.ideParametroTaxaCertificado.equals(other.ideParametroTaxaCertificado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ParametroTaxaCertificado[ ideParametroTaxaCertificado=" + ideParametroTaxaCertificado + " ]";
    }
    
}
