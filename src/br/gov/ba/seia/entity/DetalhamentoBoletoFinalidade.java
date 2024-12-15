package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "detalhamento_boleto_finalidade")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DetalhamentoBoletoFinalidade.findByIdeDetalhamentoBoleto", query = "SELECT d FROM DetalhamentoBoletoFinalidade d WHERE d.ideDetalhamentoBoleto = :ideDetalhamentoBoleto"), })
public class DetalhamentoBoletoFinalidade implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalhamento_boleto_finalidade_generator")
	@SequenceGenerator(name = "detalhamento_boleto_finalidade_generator", sequenceName = "detalhamento_boleto_finalidade_seq", allocationSize = 1)
	@Column(name = "ide_detalhamento_boleto_finalidade", nullable = false)
	private Integer ideDetalhamentoBoletoFinalidade;
	
	@JoinColumn(name = "ide_detalhamento_boleto", referencedColumnName = "ide_detalhamento_boleto")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DetalhamentoBoleto ideDetalhamentoBoleto;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	
	@Column(name = "num_area_dessedentacao_animal", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaDessedentacaoAnimal;

	@Column(name = "num_area_empreendimento", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaEmpreendimento;
	
	@Column(name = "num_area_irrigada", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaIrrigada;
	
	@Column(name = "num_area_pulverizada", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaPulverizada;
	
	@Column(name = "ind_abastecimento_em_distrito_industrial")
	private Boolean indAbastecimentoEmDistritoIndustrial;
	
	@Transient
	public boolean indCalculado;
	
	public DetalhamentoBoletoFinalidade() {}
	
	public DetalhamentoBoletoFinalidade(DetalhamentoBoleto ideDetalhamentoBoleto) {
		super();
		this.ideDetalhamentoBoleto = ideDetalhamentoBoleto;
	}
	
	/********************
	 * 					*
	 * XXX: AUXILIARES	*
	 * 					*
	 ********************/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((ideDetalhamentoBoletoFinalidade == null) ? 0 : ideDetalhamentoBoletoFinalidade.hashCode());
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
		
		DetalhamentoBoletoFinalidade other = (DetalhamentoBoletoFinalidade) obj;
		
		if (ideDetalhamentoBoletoFinalidade == null) {
			return false;
		} else if (!ideDetalhamentoBoletoFinalidade.equals(other.ideDetalhamentoBoletoFinalidade)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public DetalhamentoBoletoFinalidade clone() throws CloneNotSupportedException {
		return (DetalhamentoBoletoFinalidade) super.clone();
	}
	
	/********************
	 * 					*
	 * XXX: GETS E SETS	*
	 * 					*
	 ********************/
	
	public Integer getIdeDetalhamentoBoletoFinalidade() {
		return ideDetalhamentoBoletoFinalidade;
	}
	
	public void setIdeDetalhamentoBoletoFinalidade(Integer ideDetalhamentoBoletoFinalidade) {
		this.ideDetalhamentoBoletoFinalidade = ideDetalhamentoBoletoFinalidade;
	}
	
	public DetalhamentoBoleto getIdeDetalhamentoBoleto() {
		return ideDetalhamentoBoleto;
	}
	
	public void setIdeDetalhamentoBoleto(DetalhamentoBoleto ideDetalhamentoBoleto) {
		this.ideDetalhamentoBoleto = ideDetalhamentoBoleto;
	}
	
	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}
	
	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public Boolean getIndAbastecimentoEmDistritoIndustrial() {
		return indAbastecimentoEmDistritoIndustrial;
	}

	public void setIndAbastecimentoEmDistritoIndustrial(Boolean indAbastecimentoEmDistritoIndustrial) {
		this.indAbastecimentoEmDistritoIndustrial = indAbastecimentoEmDistritoIndustrial;
	}

	public BigDecimal getNumAreaDessedentacaoAnimal() {
		return numAreaDessedentacaoAnimal;
	}

	public void setNumAreaDessedentacaoAnimal(BigDecimal numAreaDessedentacaoAnimal) {
		this.numAreaDessedentacaoAnimal = numAreaDessedentacaoAnimal;
	}

	public BigDecimal getNumAreaEmpreendimento() {
		return numAreaEmpreendimento;
	}

	public void setNumAreaEmpreendimento(BigDecimal numAreaEmpreendimento) {
		this.numAreaEmpreendimento = numAreaEmpreendimento;
	}
	
	public boolean isIndCalculado() {
		return indCalculado;
	}

	public void setIndCalculado(boolean indCalculado) {
		this.indCalculado = indCalculado;
	}

	public BigDecimal getNumAreaIrrigada() {
		return numAreaIrrigada;
	}

	public void setNumAreaIrrigada(BigDecimal numAreaIrrigada) {
		this.numAreaIrrigada = numAreaIrrigada;
	}

	public BigDecimal getNumAreaPulverizada() {
		return numAreaPulverizada;
	}

	public void setNumAreaPulverizada(BigDecimal numAreaPulverizada) {
		this.numAreaPulverizada = numAreaPulverizada;
	}
}