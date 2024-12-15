package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_producao_produto
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralProducaoProdutoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_producao_produto", insertable = false, updatable = false)
	private Integer ideProducaoProduto;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralProducaoProdutoPK() {
	}

	public FceLicenciamentoMineralProducaoProdutoPK(FceLicenciamentoMineral fceLicenciamentoMineral, ProducaoProduto producaoProduto) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideProducaoProduto = producaoProduto.getIdeProducaoProduto();
	}

	public Integer getIdeProducaoProduto() {
		return this.ideProducaoProduto;
	}

	public void setIdeProducaoProduto(Integer ideProducaoProduto) {
		this.ideProducaoProduto = ideProducaoProduto;
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return this.ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FceLicenciamentoMineralProducaoProdutoPK)){
			return false;
		}
		FceLicenciamentoMineralProducaoProdutoPK castOther = (FceLicenciamentoMineralProducaoProdutoPK) other;
		return this.ideProducaoProduto.equals(castOther.ideProducaoProduto) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideProducaoProduto.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}