package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "cronograma")
@XmlRootElement
@NamedQueries({
	
    @NamedQuery(name = "Cronograma.findAll", query = "SELECT c FROM Cronograma c"),
    @NamedQuery(name = "Cronograma.findByIdeProcesso", query = "SELECT c FROM Cronograma c WHERE c.ideProcesso.ideProcesso = :ideProcesso"),
    @NamedQuery(name = "Cronograma.findByIdeCronograma", query = "SELECT c FROM Cronograma c WHERE c.ideCronograma = :ideCronograma")})
public class Cronograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRONOGRAMA_IDE_CRONOGRAMA_seq")    
    @SequenceGenerator(name="CRONOGRAMA_IDE_CRONOGRAMA_seq", sequenceName="CRONOGRAMA_IDE_CRONOGRAMA_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cronograma")
    private Integer ideCronograma;
    @JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo")
    @ManyToOne(optional = false)
    private Processo ideProcesso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideCronograma")
    private Collection<ControleCronograma> controleCronogramaCollection;
    @JoinColumn(name="ide_area", referencedColumnName = "ide_area")
    @OneToOne(optional=true)
    private Area ideArea;

    public Cronograma() {
    }

    public Cronograma(Integer ideCronograma) {
        this.ideCronograma = ideCronograma;
    }
    
    public Cronograma(Processo ideProcesso) {
        this.ideProcesso = ideProcesso;
    }

    public Integer getIdeCronograma() {
        return ideCronograma;
    }

    public void setIdeCronograma(Integer ideCronograma) {
        this.ideCronograma = ideCronograma;
    }

    public Processo getIdeProcesso() {
        return ideProcesso;
    }

    public void setIdeProcesso(Processo ideProcesso) {
        this.ideProcesso = ideProcesso;
    }

    @XmlTransient
    public Collection<ControleCronograma> getControleCronogramaCollection() {
        return controleCronogramaCollection;
    }

    public void setControleCronogramaCollection(Collection<ControleCronograma> controleCronogramaCollection) {
        this.controleCronogramaCollection = controleCronogramaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCronograma != null ? ideCronograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Cronograma)) {
            return false;
        }
        Cronograma other = (Cronograma) object;
        if ((this.ideCronograma == null && other.ideCronograma != null) || (this.ideCronograma != null && !this.ideCronograma.equals(other.ideCronograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cronograma[ ideCronograma=" + ideCronograma + " ]";
    }

	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}
    
}
