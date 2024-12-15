package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_bioma")
public class TipoBioma implements Serializable {
    
	private static final long serialVersionUID = 7140016241818857863L;

	@Id
    @Column(name = "ide_tipo_bioma", nullable = false)
    private Integer ideTipoBioma;
    
    @Column(name = "nom_tipo_bioma", nullable = false, length = 50)
    private String nomTipoBioma;
    
    @Column(name = "ind_ativo", nullable = false)
    private Boolean indAtivo;
    
    public TipoBioma() {
    	
    }

	public Integer getIdeTipoBioma() {
		return ideTipoBioma;
	}

	public void setIdeTipoBioma(Integer ideTipoBioma) {
		this.ideTipoBioma = ideTipoBioma;
	}

	public String getNomTipoBioma() {
		return nomTipoBioma;
	}

	public void setNomTipoBioma(String nomTipoBioma) {
		this.nomTipoBioma = nomTipoBioma;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoBioma == null) ? 0 : ideTipoBioma.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoBioma other = (TipoBioma) obj;
		if (ideTipoBioma == null) {
			if (other.ideTipoBioma != null)
				return false;
		} else if (!ideTipoBioma.equals(other.ideTipoBioma))
			return false;
		return true;
	}
	
}