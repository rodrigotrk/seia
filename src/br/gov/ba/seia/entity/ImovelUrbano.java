package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "imovel_urbano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImovelUrbano.findAll", query = "SELECT i FROM ImovelUrbano i"),
    @NamedQuery(name = "ImovelUrbano.findByIdeImovelUrbano", query = "SELECT i FROM ImovelUrbano i WHERE i.ideImovelUrbano = :ideImovelUrbano"),
    @NamedQuery(name = "ImovelUrbano.findByNumInscricaoIptu", query = "SELECT i FROM ImovelUrbano i WHERE i.numInscricaoIptu = :numInscricaoIptu")})
public class ImovelUrbano implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_urbano", nullable = false)
	private Integer ideImovelUrbano;
	
	@Basic(optional = false)
	@Column(name = "num_inscricao_iptu", length = 20)
	private String numInscricaoIptu;
	
	@JoinColumn(name = "ide_imovel_urbano", referencedColumnName = "ide_imovel", nullable = false, insertable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Imovel imovel;

    public ImovelUrbano() {}
    
    public ImovelUrbano(Integer ideImovelUrbano) {
        this.ideImovelUrbano = ideImovelUrbano;
    }
    public ImovelUrbano(String numInscricaoIptu) {
        this.numInscricaoIptu = numInscricaoIptu;
    }
    public ImovelUrbano(Integer ideImovelUrbano, String numInscricaoIptu) {
        this.ideImovelUrbano = ideImovelUrbano;
        this.numInscricaoIptu = numInscricaoIptu;
    }

    public Integer getIdeImovelUrbano() {
        return ideImovelUrbano;
    }

    public void setIdeImovelUrbano(Integer ideImovelUrbano) {
        this.ideImovelUrbano = ideImovelUrbano;
    }

    public String getNumInscricaoIptu() {
        return numInscricaoIptu;
    }

    public void setNumInscricaoIptu(String numInscricaoIptu) {
        this.numInscricaoIptu = numInscricaoIptu;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    @Transient
    public ImovelUrbano getClone() {

    	try {

    		return (ImovelUrbano) this.clone();
		}
    	catch (CloneNotSupportedException e) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}

    	return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideImovelUrbano != null ? ideImovelUrbano.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelUrbano)) {
            return false;
        }
        ImovelUrbano other = (ImovelUrbano) object;
        if ((this.ideImovelUrbano == null && other.ideImovelUrbano != null) || (this.ideImovelUrbano != null && !this.ideImovelUrbano.equals(other.ideImovelUrbano))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ImovelUrbano[ ideImovelUrbano=" + ideImovelUrbano + " ]";
    }
    
}
