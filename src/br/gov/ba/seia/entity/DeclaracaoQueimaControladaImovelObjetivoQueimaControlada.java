package br.gov.ba.seia.entity;

import java.math.BigDecimal;

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
@Table(name="declaracao_queima_controlada_imovel_objetivo_queima_controlada")
public class DeclaracaoQueimaControladaImovelObjetivoQueimaControlada extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqci_objetivo_qc_ide_dqci_objetivo_qc_seq")
	@SequenceGenerator(name = "dqci_objetivo_qc_ide_dqci_objetivo_qc_seq", sequenceName = "dqci_objetivo_qc_ide_dqci_objetivo_qc_seq", allocationSize = 1)
	@Column(name="ide_dqc_imovel_objetivo_queima_controlada")
	private Integer ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
	
	@Column(name="val_area_prevista_queima", nullable=false, precision=13, scale=4)
	private BigDecimal valAreaPrevistaQueima;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_declaracao_queima_controlada_imovel", referencedColumnName = "ide_declaracao_queima_controlada_imovel")
	private DeclaracaoQueimaControladaImovel ideDeclaracaoQueimaControladaImovel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_objetivo_queima_controlada", referencedColumnName = "ide_objetivo_queima_controlada")
	private ObjetivoQueimaControlada ideObjetivoQueimaControlada;

	@Transient
	private boolean checked;
	
	public DeclaracaoQueimaControladaImovelObjetivoQueimaControlada() {
	}
	
	public DeclaracaoQueimaControladaImovelObjetivoQueimaControlada(DeclaracaoQueimaControladaImovel ideDeclaracaoQueimaControladaImovel,ObjetivoQueimaControlada ideObjetivoQueimaControlada) {
		this.ideDeclaracaoQueimaControladaImovel = ideDeclaracaoQueimaControladaImovel;
		this.ideObjetivoQueimaControlada = ideObjetivoQueimaControlada;
	}
		
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeDeclaracaoQueimaControladaImovelObjetivoQueimaControlada() {
		return ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
	}
	
	public void setIdeDeclaracaoQueimaControladaImovelObjetivoQueimaControlada(Integer ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada) {
		this.ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada = ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
	}

	public BigDecimal getValAreaPrevistaQueima() {
		return valAreaPrevistaQueima;
	}

	public void setValAreaPrevistaQueima(BigDecimal valAreaPrevistaQueima) {
		this.valAreaPrevistaQueima = valAreaPrevistaQueima;
	}

	public DeclaracaoQueimaControladaImovel getIdeDeclaracaoQueimaControladaImovel() {
		return ideDeclaracaoQueimaControladaImovel;
	}

	public void setIdeDeclaracaoQueimaControladaImovel(DeclaracaoQueimaControladaImovel ideDeclaracaoQueimaControladaImovel) {
		this.ideDeclaracaoQueimaControladaImovel = ideDeclaracaoQueimaControladaImovel;
	}

	public ObjetivoQueimaControlada getIdeObjetivoQueimaControlada() {
		return ideObjetivoQueimaControlada;
	}

	public void setIdeObjetivoQueimaControlada(ObjetivoQueimaControlada ideObjetivoQueimaControlada) {
		this.ideObjetivoQueimaControlada = ideObjetivoQueimaControlada;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}