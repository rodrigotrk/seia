package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the fce_licenciamento_mineral_producao_produto
 * database table.
 *
 */
@Entity
@Table(name = "fce_licenciamento_mineral_producao_produto")
@NamedQuery(name = "FceLicenciamentoMineralProducaoProduto.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralProducaoProduto f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralProducaoProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralProducaoProdutoPK ideFceLicenciamentoMineralProducaoProdutoPK;

	@Column(name = "val_producao")
	private BigDecimal valProducao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_producao_produto", nullable = false, insertable = false, updatable = false)
	private ProducaoProduto producaoProduto;

	@Transient
	private boolean confirmado;

	@Transient
	private boolean edicao;

	public FceLicenciamentoMineralProducaoProduto() {

	}

	public FceLicenciamentoMineralProducaoProduto(FceLicenciamentoMineral fceLicenciamentoMineral, ProducaoProduto producaoProduto) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.producaoProduto = producaoProduto;
	}

	public BigDecimal getValProducao() {
		return valProducao;
	}

	public void setValProducao(BigDecimal valProducao) {
		this.valProducao = valProducao;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public ProducaoProduto getProducaoProduto() {
		return producaoProduto;
	}

	public void setProducaoProduto(ProducaoProduto producaoProduto) {
		this.producaoProduto = producaoProduto;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isConfirmado() {
		return this.confirmado;
	}

	public void setEdicao() {
		this.edicao = true;
	}

	public boolean isEdicao() {
		return this.edicao;
	}

	public FceLicenciamentoMineralProducaoProdutoPK getIdeFceLicenciamentoMineralProducaoProdutoPK() {
		return ideFceLicenciamentoMineralProducaoProdutoPK;
	}

	public void setIdeFceLicenciamentoMineralProducaoProdutoPK(FceLicenciamentoMineralProducaoProdutoPK ideFceLicenciamentoMineralProducaoProdutoPK) {
		this.ideFceLicenciamentoMineralProducaoProdutoPK = ideFceLicenciamentoMineralProducaoProdutoPK;
	}

	public boolean isOutros(){
		return this.producaoProduto.isOutros();
	}
}