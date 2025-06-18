package br.gov.ba.seia.entity;

import java.util.Date;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;
/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */

@Entity
@Table(name="especie_floresta")
public class EspecieFloresta extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESPECIE_FLORESTA_IDEESPECIEFLORESTA_GENERATOR", sequenceName="SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESPECIE_FLORESTA_IDEESPECIEFLORESTA_GENERATOR")
	@Column(name="ide_especie_floresta", unique=true, nullable=false)
	private Integer ideEspecieFloresta;

	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="nom_especie_floresta", nullable=false, length=20)
	private String nomEspecieFloresta;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_natureza_especie_floresta")
	private NaturezaEspecieFloresta ideNaturezaEspecieFloresta;
	
	public EspecieFloresta() {
	}

	public Integer getIdeEspecieFloresta() {
		return this.ideEspecieFloresta;
	}

	public void setIdeEspecieFloresta(Integer ideEspecieFloresta) {
		this.ideEspecieFloresta = ideEspecieFloresta;
	}

	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return this.dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomEspecieFloresta() {
		return this.nomEspecieFloresta;
	}

	public void setNomEspecieFloresta(String nomEspecieFloresta) {
		this.nomEspecieFloresta = nomEspecieFloresta;
	}
	public NaturezaEspecieFloresta getIdeNaturezaEspecieFloresta() {
		return ideNaturezaEspecieFloresta;
	}

	public void setIdeNaturezaEspecieFloresta(NaturezaEspecieFloresta ideNaturezaEspecieFloresta) {
		this.ideNaturezaEspecieFloresta = ideNaturezaEspecieFloresta;
	}
}