package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name="tipo_poco")
@NamedQueries({
    @NamedQuery(name = "TipoPoco.findAll", query = "SELECT tp.ideTipoPoco, tp.dscTipoPoco, tp.indAtivo FROM TipoPoco tp")})
public class TipoPoco implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name="ide_tipo_poco")
	private Integer ideTipoPoco;
	
	@Column(name="dsc_tipo_poco")
	private String dscTipoPoco;
	
	@Column(name="ind_ativo")
	private boolean indAtivo;
	
	public Integer getIdeTipoPoco(){
		return ideTipoPoco;
	}
	
	public void setIdeTipoPoco(Integer ideTipoPoco){
		this.ideTipoPoco = ideTipoPoco;
	}
	
	public String getDscTipoPoco(){
		return dscTipoPoco;
	}
	
	public void setDscTipoPoco(String dscTipoPoco){
		this.dscTipoPoco = dscTipoPoco;
	}
	
	public boolean getIndAtivo(){
		return indAtivo;
	}
	
	public void setIndAtivo(boolean indAtivo){
		this.indAtivo = indAtivo;
	}
	
	public TipoPoco(){
		
	}
	public TipoPoco(Integer ideTipoPoco){
		this.ideTipoPoco = ideTipoPoco;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoPoco == null) ? 0 : ideTipoPoco.hashCode());
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
		TipoPoco other = (TipoPoco) obj;
		if (ideTipoPoco == null) {
			if (other.ideTipoPoco != null)
				return false;
		} else if (!ideTipoPoco.equals(other.ideTipoPoco))
			return false;
		return true;
	}
	
	public Long getId() {
		return new Long(this.ideTipoPoco);
	}
}
