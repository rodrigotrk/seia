package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the produto_supressao_destino database table.
 * 
 */
@Embeddable
public class ProdutoSupressaoDestinoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_produto_supressao")
	private Integer ideProdutoSupressao;

	@Column(name="ide_destino_socio_produto")
	private Integer ideDestinoSocioProduto;

	public ProdutoSupressaoDestinoPK() {
	}

	public ProdutoSupressaoDestinoPK(Integer ideDestinoSocioProduto, Integer ideProdutoSupressao) {
		this.ideDestinoSocioProduto = ideDestinoSocioProduto;
		this.ideProdutoSupressao = ideProdutoSupressao;
	}

	public Integer getIdeProdutoSupressao() {
		return this.ideProdutoSupressao;
	}
	public void setIdeProdutoSupressao(Integer ideProdutoSupressao) {
		this.ideProdutoSupressao = ideProdutoSupressao;
	}
	public Integer getIdeDestinoSocioProduto() {
		return this.ideDestinoSocioProduto;
	}
	public void setIdeDestinoSocioProduto(Integer ideDestinoSocioProduto) {
		this.ideDestinoSocioProduto = ideDestinoSocioProduto;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProdutoSupressaoDestinoPK)) {
			return false;
		}
		ProdutoSupressaoDestinoPK castOther = (ProdutoSupressaoDestinoPK)other;
		return
				this.ideProdutoSupressao.equals(castOther.ideProdutoSupressao)
				&& this.ideDestinoSocioProduto.equals(castOther.ideDestinoSocioProduto);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideProdutoSupressao.hashCode();
		hash = hash * prime + this.ideDestinoSocioProduto.hashCode();
		return hash;
	}
}