package br.gov.ba.seia.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="natureza_especie_floresta")
public class NaturezaEspecieFloresta extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="NATUREZA_ESPECIE_FLORESTA_IDENATUREZAESPECIEFLORESTA_GENERATOR", sequenceName="SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NATUREZA_ESPECIE_FLORESTA_IDENATUREZAESPECIEFLORESTA_GENERATOR")
	@Column(name="ide_natureza_especie_floresta", unique=true, nullable=false)
	private Integer ideNaturezaEspecieFloresta;

	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;

	@Column(name="dtc_exclusao", nullable=false)
	private Date dtcExclusao;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="nom_natureza_especie_floresta", nullable=false, length=20)
	private String nomNaturezaEspecieFloresta;
	
	@OneToMany(mappedBy="ideNaturezaEspecieFloresta")
	private List<EspecieFloresta > especieFlorestaList;

	public NaturezaEspecieFloresta() {
	}

	public Integer getIdeNaturezaEspecieFloresta() {
		return this.ideNaturezaEspecieFloresta;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomNaturezaEspecieFloresta() {
		return nomNaturezaEspecieFloresta;
	}

	public void setNomNaturezaEspecieFloresta(String nomNaturezaEspecieFloresta) {
		this.nomNaturezaEspecieFloresta = nomNaturezaEspecieFloresta;
	}

	public void setIdeNaturezaEspecieFloresta(Integer ideNaturezaEspecieFloresta) {
		this.ideNaturezaEspecieFloresta = ideNaturezaEspecieFloresta;
	}

	public List<EspecieFloresta> getEspecieFlorestaList() {
		return especieFlorestaList;
	}

	public void setEspecieFlorestaList(List<EspecieFloresta> especieFlorestaList) {
		this.especieFlorestaList = especieFlorestaList;
	}

}