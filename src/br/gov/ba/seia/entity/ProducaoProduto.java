package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the producao_produto database table.
 *
 */
@Entity
@Table(name = "producao_produto")
public class ProducaoProduto implements Serializable, Comparable<ProducaoProduto> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PRODUCAO_PRODUTO_IDEPRODUCAOPRODUTO_GENERATOR", sequenceName = "PRODUCAO_PRODUTO_IDE_PRODUCAO_PRODUTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCAO_PRODUTO_IDEPRODUCAOPRODUTO_GENERATOR")
	@Column(name = "ide_producao_produto")
	private Integer ideProducaoProduto;

	@Column(name = "nom_producao_produto")
	private String nomProducaoProduto;

	@JoinColumn(name = "ide_unidade_medida", referencedColumnName = "ide_unidade_medida", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadeMedida ideUnidadeMedida;
	
	public ProducaoProduto() {

	}

	public ProducaoProduto(Integer ide, String nomProduto) {
		this.ideProducaoProduto = ide;
		this.nomProducaoProduto = nomProduto;
	}

	public Integer getIdeProducaoProduto() {
		return this.ideProducaoProduto;
	}

	public void setIdeProducaoProduto(Integer ideProducaoProduto) {
		this.ideProducaoProduto = ideProducaoProduto;
	}

	public String getNomProducaoProduto() {
		return this.nomProducaoProduto;
	}

	public void setNomProducaoProduto(String nomProducaoProduto) {
		this.nomProducaoProduto = nomProducaoProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideProducaoProduto == null) ? 0 : ideProducaoProduto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		ProducaoProduto other = (ProducaoProduto) obj;
		if(ideProducaoProduto == null){
			if(other.ideProducaoProduto != null) {
				return false;
			}
		}
		else if(!ideProducaoProduto.equals(other.ideProducaoProduto)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ProducaoProduto o) {
		if (Util.isNull(getIdeProducaoProduto())){
			return -1;
		}
		return getIdeProducaoProduto().compareTo(o.getIdeProducaoProduto());
	}

	public boolean isOutros() {
		if(this.nomProducaoProduto.compareTo("Outros") == 0){
			return true;
		}
		return false;
	}

	public UnidadeMedida getIdeUnidadeMedida() {
		return ideUnidadeMedida;
	}

	public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
		this.ideUnidadeMedida = ideUnidadeMedida;
	}

}