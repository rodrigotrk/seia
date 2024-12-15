package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.duarte
 */
@Entity
@Table(name = "gestor_financeiro_cefir")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestorFinanceiroCefir.findAll", query = "SELECT g FROM GestorFinanceiroCefir g"),
    @NamedQuery(name = "GestorFinanceiroCefir.findByIdeGestorFinanceiroCefir", query = "SELECT g FROM GestorFinanceiroCefir g WHERE g.ideGestorFinanceiroCefir = :ideGestorFinanceiroCefir"),
    @NamedQuery(name = "GestorFinanceiroCefir.findByNomGestorFinanceiroCefir", query = "SELECT g FROM GestorFinanceiroCefir g WHERE g.nomGestorFinanceiroCefir = :nomGestorFinanceiroCefir")})
public class GestorFinanceiroCefir implements Serializable {
    
	private static final long serialVersionUID = 8910443630180757528L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GESTOR_FINANCEIRO_CEFIR_SEQ") 
    @SequenceGenerator(name="GESTOR_FINANCEIRO_CEFIR_SEQ", sequenceName="GESTOR_FINANCEIRO_CEFIR_SEQ", allocationSize=1)
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_gestor_financeiro_cefir", nullable = false)
    private Integer ideGestorFinanceiroCefir;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_gestor_financeiro_cefir")
    private String nomGestorFinanceiroCefir;
    
    @OneToMany(mappedBy = "ideGestorFinanceiroCefir")
    private Collection<ContratoConvenioCefir> contratoConvenioCefirCollection;
    
    public GestorFinanceiroCefir() {
    }

    public GestorFinanceiroCefir(Integer ideGestorFinanceiroCefir) {
        this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
    }

    public GestorFinanceiroCefir(Integer ideGestorFinanceiroCefir, String nomGestorFinanceiroCefir) {
        this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
        this.nomGestorFinanceiroCefir = nomGestorFinanceiroCefir;
    }    

    public Integer getIdeGestorFinanceiroCefir() {
		return ideGestorFinanceiroCefir;
	}

	public void setIdeGestorFinanceiroCefir(Integer ideGestorFinanceiroCefir) {
		this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
	}

	public String getNomGestorFinanceiroCefir() {
		return nomGestorFinanceiroCefir;
	}

	public void setNomGestorFinanceiroCefir(String nomGestorFinanceiroCefir) {
		this.nomGestorFinanceiroCefir = nomGestorFinanceiroCefir;
	}
	

	public Collection<ContratoConvenioCefir> getContratoConvenioCefirCollection() {
		return contratoConvenioCefirCollection;
	}

	public void setContratoConvenioCefirCollection(Collection<ContratoConvenioCefir> contratoConvenioCefirCollection) {
		this.contratoConvenioCefirCollection = contratoConvenioCefirCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGestorFinanceiroCefir != null ? ideGestorFinanceiroCefir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GestorFinanceiroCefir)) {
            return false;
        }
        GestorFinanceiroCefir other = (GestorFinanceiroCefir) object;
        if ((this.ideGestorFinanceiroCefir == null && other.ideGestorFinanceiroCefir != null) || (this.ideGestorFinanceiroCefir != null && !this.ideGestorFinanceiroCefir.equals(other.ideGestorFinanceiroCefir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideGestorFinanceiroCefir;
    }
    
}
