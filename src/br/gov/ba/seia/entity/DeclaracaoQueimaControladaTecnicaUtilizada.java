package br.gov.ba.seia.entity;

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
@Table(name="declaracao_queima_controlada_tecnica_utilizada")
public class DeclaracaoQueimaControladaTecnicaUtilizada extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqc_tecnica_utilizada_ide_dqc_tecnica_utilizada_seq")
	@SequenceGenerator(name = "dqc_tecnica_utilizada_ide_dqc_tecnica_utilizada_seq", sequenceName = "dqc_tecnica_utilizada_ide_dqc_tecnica_utilizada_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_declaracao_queima_controlada_tecnica_utilizada")
	private Integer ideDeclaracaoQueimaControladaTecnicaUtilizada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_declaracao_queima_controlada", referencedColumnName = "ide_declaracao_queima_controlada")
	private DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tecnica_queima_controlada", referencedColumnName = "ide_tecnica_queima_controlada")
	private TecnicaQueimaControlada ideTecnicaQueimaControlada;

	@Transient
	private boolean checked;

	public DeclaracaoQueimaControladaTecnicaUtilizada() {
	}

	public DeclaracaoQueimaControladaTecnicaUtilizada(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada, TecnicaQueimaControlada ideTecnicaQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
		this.ideTecnicaQueimaControlada = ideTecnicaQueimaControlada;
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeDeclaracaoQueimaControladaTecnicaUtilizada() {
		return ideDeclaracaoQueimaControladaTecnicaUtilizada;
	}

	public void setIdeDeclaracaoQueimaControladaTecnicaUtilizada(Integer ideDeclaracaoQueimaControladaTecnicaUtilizada) {
		this.ideDeclaracaoQueimaControladaTecnicaUtilizada = ideDeclaracaoQueimaControladaTecnicaUtilizada;
	}

	public DeclaracaoQueimaControlada getIdeDeclaracaoQueimaControlada() {
		return ideDeclaracaoQueimaControlada;
	}

	public void setIdeDeclaracaoQueimaControlada(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}

	public TecnicaQueimaControlada getIdeTecnicaQueimaControlada() {
		return ideTecnicaQueimaControlada;
	}

	public void setIdeTecnicaQueimaControlada(TecnicaQueimaControlada ideTecnicaQueimaControlada) {
		this.ideTecnicaQueimaControlada = ideTecnicaQueimaControlada;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}