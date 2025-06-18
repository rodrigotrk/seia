package br.gov.ba.seia.entity;
import java.io.Serializable;

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
 * The persistent class for the produto_supressao_destino database table.
 * 
 */
@Entity
@Table(name="produto_supressao_destino")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProdutoSupressaoDestino.deleteByIdeProdutoSupressao", query = "DELETE FROM ProdutoSupressaoDestino WHERE ideProdutoSupressao = :ideProdutoSupressao")})
public class ProdutoSupressaoDestino implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProdutoSupressaoDestinoPK ideProdutoSupressaoDestinoPK;

	@NotNull
	@JoinColumn(name = "ide_destino_socio_produto", referencedColumnName = "ide_destino_socio_produto", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private DestinoSocioeconomicoProduto ideDestinoSocioeconomicoProduto;

	@NotNull
	@JoinColumn(name = "ide_produto_supressao", referencedColumnName = "ide_produto_supressao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProdutoSupressao ideProdutoSupressao;

	public ProdutoSupressaoDestino() {
	}

	public ProdutoSupressaoDestino(ProdutoSupressaoDestinoPK ideProdutoSupressaoDestinoPK) {
		this.ideProdutoSupressaoDestinoPK = ideProdutoSupressaoDestinoPK;
	}

	public ProdutoSupressaoDestino(Integer ideDestino, Integer ideProduto) {
		this.ideProdutoSupressaoDestinoPK = new ProdutoSupressaoDestinoPK(ideDestino, ideProduto);
	}

	public ProdutoSupressaoDestinoPK getIdeProdutoSupressaoDestinoPK() {
		return ideProdutoSupressaoDestinoPK;
	}

	public void setIdeProdutoSupressaoDestinoPK(ProdutoSupressaoDestinoPK ideProdutoSupressaoDestinoPK) {
		this.ideProdutoSupressaoDestinoPK = ideProdutoSupressaoDestinoPK;
	}

	public ProdutoSupressao getIdeProdutoSupressao() {
		return ideProdutoSupressao;
	}

	public void setIdeProdutoSupressao(ProdutoSupressao ideProdutoSupressao) {
		this.ideProdutoSupressao = ideProdutoSupressao;
	}

	public DestinoSocioeconomicoProduto getIdeDestinoSocioeconomicoProduto() {
		return ideDestinoSocioeconomicoProduto;
	}

	public void setIdeDestinoSocioeconomicoProduto(DestinoSocioeconomicoProduto ideDestinoSocioeconomicoProduto) {
		this.ideDestinoSocioeconomicoProduto = ideDestinoSocioeconomicoProduto;
	}
}