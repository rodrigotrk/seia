package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * CLASSE POVOADA MANUALMENTE ATRAVÉS DE INSERTs NO BD.
 */
@Entity
@Table(name="destino_socioeconomico_produto")
public class DestinoSocioeconomicoProduto implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_destino_socio_produto", nullable = false)
	private Integer ideDestinoSocioProduto;

	//bi-directional many-to-one association to DestinoSocioeconomico
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_destino_socioeconomico")
	private DestinoSocioeconomico ideDestinoSocioeconomico;

	//bi-directional many-to-one association to Produto
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_produto")
	private Produto ideProduto;

	public DestinoSocioeconomicoProduto() {
	}

	public Integer getIdeDestinoSocioProduto() {
		return ideDestinoSocioProduto;
	}

	public void setIdeDestinoSocioProduto(Integer ideDestinoSocioProduto) {
		this.ideDestinoSocioProduto = ideDestinoSocioProduto;
	}


	public DestinoSocioeconomico getIdeDestinoSocioeconomico() {
		return ideDestinoSocioeconomico;
	}

	public void setIdeDestinoSocioeconomico(DestinoSocioeconomico ideDestinoSocioeconomico) {
		this.ideDestinoSocioeconomico = ideDestinoSocioeconomico;
	}

	public Produto getIdeProduto() {
		return ideProduto;
	}

	public void setIdeProduto(Produto ideProduto) {
		this.ideProduto = ideProduto;
	}

	@Override
	public Long getId() {
		return new Long(this.ideDestinoSocioProduto);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDestinoSocioProduto == null) ? 0
						: ideDestinoSocioProduto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DestinoSocioeconomicoProduto other = (DestinoSocioeconomicoProduto) obj;
		if (ideDestinoSocioProduto == null) {
			if (other.ideDestinoSocioProduto != null) {
				return false;
			}
		} else if (!ideDestinoSocioProduto.equals(other.ideDestinoSocioProduto)) {
			return false;
		}
		return true;
	}


}