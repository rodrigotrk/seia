package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author eduardo.fernandes
 *
 */
@Entity
@Table(name = "lac_transporte_produto")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "LacTransporteProduto.findByIdeLacTransporte", query = "SELECT l FROM LacTransporteProduto l WHERE l.ideLacTransporte = :ideLacTransporte"),
@NamedQuery(name = "LacTransporteProduto.findByIdeProduto", query = "SELECT l FROM LacTransporteProduto l WHERE l.ideProduto = :ideProduto")})

public class LacTransporteProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected LacTransporteProdutoPK LacTransporteProdutoPK;

	@NotNull
	@JoinColumn(name = "ide_lac_transporte", referencedColumnName = "ide_lac_transporte", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LacTransporte ideLacTransporte;

	@NotNull
	@JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Produto ideProduto;

	@Column(name = "qtd_media_transporte_anual",precision = 10, scale = 2)
	private Double qtdMediaTransporteAnual;
	
	public LacTransporteProduto() {

	}

	public LacTransporteProduto(LacTransporteProdutoPK lacTransporteProdutoPK) {
		LacTransporteProdutoPK = lacTransporteProdutoPK;
	}

	public LacTransporteProduto(int ideLacTransporte, int ideProduto) {
		this.LacTransporteProdutoPK = new LacTransporteProdutoPK(ideLacTransporte, ideProduto);
	}

	public LacTransporteProdutoPK getLacTransporteProdutoPK() {
		return LacTransporteProdutoPK;
	}

	public void setLacTransporteProdutoPK(LacTransporteProdutoPK lacTransporteProdutoPK) {
		LacTransporteProdutoPK = lacTransporteProdutoPK;
	}

	public LacTransporte getIdeLacTransporte() {
		return ideLacTransporte;
	}

	public void setIdeLacTransporte(LacTransporte ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public Produto getIdeProduto() {
		return ideProduto;
	}

	public void setIdeProduto(Produto ideProduto) {
		this.ideProduto = ideProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LacTransporteProdutoPK == null) ? 0 : LacTransporteProdutoPK.hashCode());
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
		LacTransporteProduto other = (LacTransporteProduto) obj;
		if (LacTransporteProdutoPK == null) {
			if (other.LacTransporteProdutoPK != null)
				return false;
		} else if (!LacTransporteProdutoPK.equals(other.LacTransporteProdutoPK))
			return false;
		return true;
	}

	public Double getQtdMediaTransporteAnual() {
		return qtdMediaTransporteAnual;
	}

	public void setQtdMediaTransporteAnual(Double qtdMediaTransporteAnual) {
		this.qtdMediaTransporteAnual = qtdMediaTransporteAnual;
	}
}