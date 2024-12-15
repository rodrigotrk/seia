package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "posto_combustivel_produtos_comercializados")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PostoCombustivelProdutoComercializado.findAll", query = "SELECT p FROM PostoCombustivelProdutoComercializado p"),		
		@NamedQuery(name = "PostoCombustivelProdutoComercializado.findByIdePostoCombustivel", query = "SELECT p FROM PostoCombustivelProdutoComercializado p WHERE p.postoCombustivelProdutosComercializadosPK.idePostoCombustivel = :idePostoCombustivel"),
		@NamedQuery(name = "PostoCombustivelProdutoComercializado.findByideProduto", query = "SELECT p FROM PostoCombustivelProdutoComercializado p WHERE p.postoCombustivelProdutosComercializadosPK.ideProduto = :ideProduto"),
		@NamedQuery(name = "PostoCombustivelProdutoComercializado.removerByIde", query = "delete from PostoCombustivelProdutoComercializado p WHERE p.postoCombustivelProdutosComercializadosPK.ideProduto = :ideProduto and p.postoCombustivelProdutosComercializadosPK.idePostoCombustivel = :idePostoCombustivel"),
		@NamedQuery(name = "PostoCombustivelProdutoComercializado.findByQtdVendida", query = "SELECT p FROM PostoCombustivelProdutoComercializado p WHERE p.qtdVendida = :qtdVendida") })
public class PostoCombustivelProdutoComercializado implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected PostoCombustivelProdutosComercializadosPK postoCombustivelProdutosComercializadosPK;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "qtd_vendida")
	private BigDecimal qtdVendida;
	@JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Produto produto;
	@JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private LacPostoCombustivel postoCombustivel;

	public PostoCombustivelProdutoComercializado() {
	}

	public PostoCombustivelProdutoComercializado(PostoCombustivelProdutosComercializadosPK postoCombustivelProdutosComercializadosPK) {
		this.postoCombustivelProdutosComercializadosPK = postoCombustivelProdutosComercializadosPK;
	}

	public PostoCombustivelProdutoComercializado(PostoCombustivelProdutosComercializadosPK postoCombustivelProdutosComercializadosPK, BigDecimal qtdVendida) {
		this.postoCombustivelProdutosComercializadosPK = postoCombustivelProdutosComercializadosPK;
		this.qtdVendida = qtdVendida;
	}

	public PostoCombustivelProdutoComercializado(int idePostoCombustivel, int ideProduto) {
		this.postoCombustivelProdutosComercializadosPK = new PostoCombustivelProdutosComercializadosPK( idePostoCombustivel,
				ideProduto);
	}

	public PostoCombustivelProdutosComercializadosPK getPostoCombustivelProdutosComercializadosPK() {
		return postoCombustivelProdutosComercializadosPK;
	}

	public void setPostoCombustivelProdutosComercializadosPK(PostoCombustivelProdutosComercializadosPK postoCombustivelProdutosComercializadosPK) {
		this.postoCombustivelProdutosComercializadosPK = postoCombustivelProdutosComercializadosPK;
	}

	public BigDecimal getQtdVendida() {
		return qtdVendida;
	}

	public void setQtdVendida(BigDecimal qtdVendida) {
		this.qtdVendida = qtdVendida;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public LacPostoCombustivel getPostoCombustivel() {
		return postoCombustivel;
	}

	public void setPostoCombustivel(LacPostoCombustivel postoCombustivel) {
		this.postoCombustivel = postoCombustivel;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postoCombustivel == null) ? 0 : postoCombustivel.hashCode());
		result = prime * result + ((postoCombustivelProdutosComercializadosPK == null) ? 0 : postoCombustivelProdutosComercializadosPK.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((qtdVendida == null) ? 0 : qtdVendida.hashCode());
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
		PostoCombustivelProdutoComercializado other = (PostoCombustivelProdutoComercializado) obj;
		if (postoCombustivel == null) {
			if (other.postoCombustivel != null)
				return false;
		} else if (!postoCombustivel.equals(other.postoCombustivel))
			return false;
		if (postoCombustivelProdutosComercializadosPK == null) {
			if (other.postoCombustivelProdutosComercializadosPK != null)
				return false;
		} else if (!postoCombustivelProdutosComercializadosPK.equals(other.postoCombustivelProdutosComercializadosPK))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (qtdVendida == null) {
			if (other.qtdVendida != null)
				return false;
		} else if (!qtdVendida.equals(other.qtdVendida))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.PostoCombustivelProdutoComercializado[ postoCombustivelProdutosComercializadosPK=" + postoCombustivelProdutosComercializadosPK + " ]";
	}

}
