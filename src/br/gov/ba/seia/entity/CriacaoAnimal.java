package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the criacao_animal database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="criacao_animal")
public class CriacaoAnimal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "criacao_animal_ide_criacao_animal_generator")
	@SequenceGenerator(name = "criacao_animal_ide_criacao_animal_generator", sequenceName = "criacao_animal_ide_criacao_animal_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_criacao_animal")
	private Integer ideCriacaoAnimal;

	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	@Column(name="num_animais",nullable = true)
	private Integer numAnimais;

	@Column(name="num_cabecas",nullable = true)
	private Integer numCabecas;

	@Column(name="num_capacidade", precision = 10, scale = 2, nullable = true)
	private Double numCapacidade;

	@Column(name="num_matrizes",nullable = true)
	private Integer numMatrizes;

	@Column(name="num_producao", precision = 10, scale = 3, nullable = true)
	private BigDecimal numProducao;
	
	@Column(name="num_reprodutores",nullable = true)
	private Integer numReprodutores;
	
	@JoinColumn(name = "ide_unidade_tipologia_atividade", referencedColumnName = "ide_unidade_tipologia_atividade", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private UnidadeTipologiaAtividade ideUnidadeTipologiaAtividade;
	
	@Transient
	private Integer numProducaoInt;
	@Transient
	private List<Finalidade> listaFinalidade;
	@Transient
	private List<Finalidade> listaFinalidadeSelecionadas;
	@Transient
	private boolean desabilitado;
	

    public CriacaoAnimal() {
    }

    
	public CriacaoAnimal(UnidadeTipologiaAtividade ideUnidadeTipologiaAtividade,boolean desabilitado) {
		this.ideUnidadeTipologiaAtividade = ideUnidadeTipologiaAtividade;
		this.desabilitado = desabilitado;
	}


	public CriacaoAnimal(UnidadeTipologiaAtividade ideUnidadeTipologiaAtividade) {
		this.ideUnidadeTipologiaAtividade = ideUnidadeTipologiaAtividade;
	}

	public Integer getIdeCriacaoAnimal() {
		return this.ideCriacaoAnimal;
	}

	public void setIdeCriacaoAnimal(Integer ideCriacaoAnimal) {
		this.ideCriacaoAnimal = ideCriacaoAnimal;
	}

	public Integer getNumAnimais() {
		return this.numAnimais;
	}

	public void setNumAnimais(Integer numAnimais) {
		this.numAnimais = numAnimais;
	}

	public Integer getNumCabecas() {
		return this.numCabecas;
	}

	public void setNumCabecas(Integer numCabecas) {
		this.numCabecas = numCabecas;
	}

	public Integer getNumMatrizes() {
		return this.numMatrizes;
	}

	public void setNumMatrizes(Integer numMatrizes) {
		this.numMatrizes = numMatrizes;
	}

	public Integer getNumReprodutores() {
		return this.numReprodutores;
	}

	public void setNumReprodutores(Integer numReprodutores) {
		this.numReprodutores = numReprodutores;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public Double getNumCapacidade() {
		return numCapacidade;
	}

	public void setNumCapacidade(Double numCapacidade) {
		this.numCapacidade = numCapacidade;
	}

	public UnidadeTipologiaAtividade getIdeUnidadeTipologiaAtividade() {
		return ideUnidadeTipologiaAtividade;
	}

	public void setIdeUnidadeTipologiaAtividade(UnidadeTipologiaAtividade ideUnidadeTipologiaAtividade) {
		this.ideUnidadeTipologiaAtividade = ideUnidadeTipologiaAtividade;
	}

	public List<Finalidade> getListaFinalidadeSelecionadas() {
		return listaFinalidadeSelecionadas;
	}

	public void setListaFinalidadeSelecionadas(List<Finalidade> listaFinalidadeSelecionadas) {
		this.listaFinalidadeSelecionadas = listaFinalidadeSelecionadas;
	}

	public List<Finalidade> getListaFinalidade() {
		return listaFinalidade;
	}

	public void setListaFinalidade(List<Finalidade> listaFinalidade) {
		this.listaFinalidade = listaFinalidade;
	}

	public Integer getNumProducaoInt() {
		return numProducaoInt;
	}

	public void setNumProducaoInt(Integer numProducaoInt) {
		this.numProducaoInt = numProducaoInt;
		if(Util.isNull(this.numProducao) || this.numProducao.intValue() != numProducaoInt){
			this.numProducao = new BigDecimal(numProducaoInt);
		}
	}
	
	public BigDecimal getNumProducao() {
		return numProducao;
	}

	public void setNumProducao(BigDecimal numProducao) {
		this.numProducao = numProducao.setScale(3,RoundingMode.UP);
		this.numProducaoInt = numProducao.intValue();
	}
	

	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideUnidadeTipologiaAtividade == null) ? 0
						: ideUnidadeTipologiaAtividade.hashCode());
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
		CriacaoAnimal other = (CriacaoAnimal) obj;
		if (ideUnidadeTipologiaAtividade == null) {
			if (other.ideUnidadeTipologiaAtividade != null)
				return false;
		} else if (!ideUnidadeTipologiaAtividade
				.equals(other.ideUnidadeTipologiaAtividade))
			return false;
		return true;
	}

}