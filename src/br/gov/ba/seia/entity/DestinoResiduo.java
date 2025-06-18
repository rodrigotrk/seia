package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the destino_residuo database table.
 * 
 */
@Entity
@Table(name = "destino_residuo")
public class DestinoResiduo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ide_destino_residuo")
	private Integer ideDestinoResiduo;

	@Column(name = "dsc_destino_residuo")
	private String dscDestinoResiduo;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro; 
	
	@Transient
	private boolean checked;
	
	public DestinoResiduo() {
	}

	public DestinoResiduo(String string) {
		this.dscDestinoResiduo = string;
	}

	public DestinoResiduo(Integer ide, String string) {
		this.ideDestinoResiduo = ide;
		this.dscDestinoResiduo = string;
	}

	public Integer getIdeDestinoResiduo() {
		return this.ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(Integer ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

	public String getDscDestinoResiduo() {
		return this.dscDestinoResiduo;
	}

	public void setDscDestinoResiduo(String dscDestinoResiduo) {
		this.dscDestinoResiduo = dscDestinoResiduo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isOutros() {
		if(dscDestinoResiduo.compareTo("Outros") == 0){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscDestinoResiduo == null) ? 0 : dscDestinoResiduo.hashCode());
		result = prime * result + ((ideDestinoResiduo == null) ? 0 : ideDestinoResiduo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		DestinoResiduo other = (DestinoResiduo) obj;
		if(dscDestinoResiduo == null){
			if(other.dscDestinoResiduo != null)
				return false;
		}
		else if(!dscDestinoResiduo.equals(other.dscDestinoResiduo))
			return false;
		if(ideDestinoResiduo == null){
			if(other.ideDestinoResiduo != null)
				return false;
		}
		else if(!ideDestinoResiduo.equals(other.ideDestinoResiduo))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideDestinoResiduo);
	}
}