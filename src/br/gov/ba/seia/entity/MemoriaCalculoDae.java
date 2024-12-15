package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name="memoria_calculo_dae")
@XmlRootElement
public class MemoriaCalculoDae implements Serializable {

	private static final long serialVersionUID = 7749144263285954865L;

	@Id
	@SequenceGenerator(name="MEMORIA_CALCULO_DAE_SEQ", sequenceName="MEMORIA_CALCULO_DAE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEMORIA_CALCULO_DAE_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_memoria_calculo_dae")
	private Integer ideMemoriaCalculoDae;
	
	@JoinColumn(name = "ide_cumprimento_reposicao_florestal", referencedColumnName = "ide_cumprimento_reposicao_florestal")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal;
	
	@JoinColumn(name = "ide_parametro_calculo", referencedColumnName = "ide_parametro_calculo")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private ParametroCalculo ideParametroCalculo;
	
	@Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
	private Double valorTotal;
	
	@Column(name = "qtd_parcelas", nullable = false)
	private Integer qtdParcelas;
	
	@Column(name = "ind_parcelado", nullable = false)
	private Boolean indParcelado;
	
	@OneToMany(mappedBy="ideMemoriaCalculoDae", fetch=FetchType.LAZY)
	private Collection<MemoriaCalculoDaeParcela> memoriaCalculoDaeCollection;

	public MemoriaCalculoDae() {}
	
	public MemoriaCalculoDae(Integer ideMemoriaCalculoDae) {
		this.ideMemoriaCalculoDae = ideMemoriaCalculoDae;
	}
	
	//valor sem os juros
	@Transient
	public Double getValorDaeParcelado() {
		if (!Util.isNullOuVazio(valorTotal)) {
			return (valorTotal / qtdParcelas);
		}
		return 0.0;
	}
	
	public Integer getIdeMemoriaCalculoDae() {
		return ideMemoriaCalculoDae;
	}

	public void setIdeMemoriaCalculoDae(Integer ideMemoriaCalculoDae) {
		this.ideMemoriaCalculoDae = ideMemoriaCalculoDae;
	}

	public CumprimentoReposicaoFlorestal getIdeCumprimentoReposicaoFlorestal() {
		return ideCumprimentoReposicaoFlorestal;
	}

	public void setIdeCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}

	public ParametroCalculo getIdeParametroCalculo() {
		return ideParametroCalculo;
	}

	public void setIdeParametroCalculo(ParametroCalculo ideParametroCalculo) {
		this.ideParametroCalculo = ideParametroCalculo;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public Boolean getIndParcelado() {
		return indParcelado;
	}

	public void setIndParcelado(Boolean indParcelado) {
		this.indParcelado = indParcelado;
	}

	public Collection<MemoriaCalculoDaeParcela> getMemoriaCalculoDaeCollection() {
		return memoriaCalculoDaeCollection;
	}

	public void setMemoriaCalculoDaeCollection(
			Collection<MemoriaCalculoDaeParcela> memoriaCalculoDaeCollection) {
		this.memoriaCalculoDaeCollection = memoriaCalculoDaeCollection;
	}

}
