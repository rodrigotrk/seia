package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
/**
 * 
 * @author eduardo.fernandes
 *
 */
@Embeddable
public class LacTransporteProdutoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Basic(optional = false)
	@NotNull
    @Column(name = "ide_lac_transporte", nullable = false)
	private int ideLacTransporte;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "ide_produto", nullable = false)
	private int ideProduto;

	public LacTransporteProdutoPK() {
	}

	public LacTransporteProdutoPK(int ideLacTransporte, int ideProduto) {
		this.ideLacTransporte = ideLacTransporte;
		this.ideProduto = ideProduto;
	}    
    
	public int getIdeLacTransporte() {
		return ideLacTransporte;
	}

	public void setIdeLacTransporte(int ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public int getIdeProduto() {
		return ideProduto;
	}

	public void setIdeProduto(int ideProduto) {
		this.ideProduto = ideProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideLacTransporte;
		result = prime * result + ideProduto;
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
		LacTransporteProdutoPK other = (LacTransporteProdutoPK) obj;
		if (ideLacTransporte != other.ideLacTransporte)
			return false;
		if (ideProduto != other.ideProduto)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LacTransporteProdutoPK [ideLacTransporte=" + ideLacTransporte + ", ideProduto=" + ideProduto + "]";
	}
}