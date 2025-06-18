/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "tipo_revestimento")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoRevestimento.findByFceCanalTrecho", query = "SELECT fceCanalTrecho.tiposRevestimentos FROM FceCanalTrecho fceCanalTrecho WHERE fceCanalTrecho = :fceCanalTrecho")
})
public class TipoRevestimento implements Serializable, BaseEntity, Comparable<TipoRevestimento>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tipo_revestimento_seq")    
    @SequenceGenerator(name="tipo_revestimento_seq", sequenceName="tipo_revestimento_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_revestimento", nullable = false)
    private Integer ideTipoRevestimento;
    
    @Column(name = "dsc_tipo_revestimento")
    private String dsTiporevestimento;
	
    @Transient
    private boolean selecionado;
    
    public TipoRevestimento() {
	}
    
	public Integer getIdeTipoRevestimento() {
		return ideTipoRevestimento;
	}

	public void setIdeTipoRevestimento(Integer ideTipoRevestimento) {
		this.ideTipoRevestimento = ideTipoRevestimento;
	}

	public String getDsTiporevestimento() {
		return dsTiporevestimento;
	}

	public void setDsTiporevestimento(String dsTiporevestimento) {
		this.dsTiporevestimento = dsTiporevestimento;
	}

	@Override
	public int compareTo(TipoRevestimento o) {
		if(this.getIdeTipoRevestimento() > o.getIdeTipoRevestimento()){
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public Long getId() {
		return new Long(getIdeTipoRevestimento());
	}

	@Override
	public boolean equals(Object object) {
        if (!(object instanceof TipoRevestimento)) {
            return false;
        }
        TipoRevestimento other = (TipoRevestimento) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

}
