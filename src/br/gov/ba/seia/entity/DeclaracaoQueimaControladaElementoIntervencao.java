package br.gov.ba.seia.entity;

import java.math.BigDecimal;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="declaracao_queima_controlada_elemento_intervencao")
public class DeclaracaoQueimaControladaElementoIntervencao extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqc_elemento_intervencao_ide_dqc_elemento_intervencao_seq")
	@SequenceGenerator(name = "dqc_elemento_intervencao_ide_dqc_elemento_intervencao_seq", sequenceName = "dqc_elemento_intervencao_ide_dqc_elemento_intervencao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_declaracao_queima_controlada_elemento_intervencao")
	private Integer ideDeclaracaoQueimaControladaElementoIntervencao;
	
	@Column(name="val_distancia", precision=13, scale=4)
	private BigDecimal valDistancia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_declaracao_queima_controlada", referencedColumnName = "ide_declaracao_queima_controlada")
	private DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_elemento_intervencao_queima_controlada", referencedColumnName = "ide_elemento_intervencao_queima_controlada")
	private ElementoIntervencaoQueimaControlada ideElementoIntervencaoQueimaControlada;

	@Transient
	private boolean checked;
	
	public DeclaracaoQueimaControladaElementoIntervencao() {
	}

	public DeclaracaoQueimaControladaElementoIntervencao(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada,
			ElementoIntervencaoQueimaControlada ideElementoIntervencaoQueimaControlada) {

		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
		this.ideElementoIntervencaoQueimaControlada = ideElementoIntervencaoQueimaControlada;
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public Integer getIdeDeclaracaoQueimaControladaElementoIntervencao() {
		return ideDeclaracaoQueimaControladaElementoIntervencao;
	}

	public void setIdeDeclaracaoQueimaControladaElementoIntervencao(Integer ideDeclaracaoQueimaControladaElementoIntervencao) {
		this.ideDeclaracaoQueimaControladaElementoIntervencao = ideDeclaracaoQueimaControladaElementoIntervencao;
	}

	public BigDecimal getValDistancia() {
		return valDistancia;
	}

	public void setValDistancia(BigDecimal valDistancia) {
		this.valDistancia = valDistancia;
	}

	public DeclaracaoQueimaControlada getIdeDeclaracaoQueimaControlada() {
		return ideDeclaracaoQueimaControlada;
	}

	public void setIdeDeclaracaoQueimaControlada(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}

	public ElementoIntervencaoQueimaControlada getIdeElementoIntervencaoQueimaControlada() {
		return ideElementoIntervencaoQueimaControlada;
	}

	public void setIdeElementoIntervencaoQueimaControlada(ElementoIntervencaoQueimaControlada ideElementoIntervencaoQueimaControlada) {
		this.ideElementoIntervencaoQueimaControlada = ideElementoIntervencaoQueimaControlada;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}