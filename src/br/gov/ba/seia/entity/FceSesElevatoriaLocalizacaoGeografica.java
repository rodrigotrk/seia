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

@Entity
@Table(name="fce_ses_elevatoria_localizacao_geografica")
public class FceSesElevatoriaLocalizacaoGeografica {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_elevatoria_localizacao_geografica_seq")
	@SequenceGenerator(name = "fce_ses_elevatoria_localizacao_geografica_seq", sequenceName = "fce_ses_elevatoria_localizacao_geografica_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_elevatoria_localizacao_geografica", nullable = false)
	private Integer ideFceSesElevatoriaLocalizacaoGeografica;
	
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses_dados_elevatoria", referencedColumnName="ide_fce_ses_dados_elevatoria")
	private FceSesDadosElevatoria ideFceSesDadosElevatoria;
	
	@Column(name="ind_extravasamento")
	private Boolean indExtravasamento;

	@Transient
	private boolean localizacaoFinal;
	
	public Integer getIdeFceSesElevatoriaLocalizacaoGeografica() {
		return ideFceSesElevatoriaLocalizacaoGeografica;
	}

	public void setIdeFceSesElevatoriaLocalizacaoGeografica(
			Integer ideFceSesElevatoriaLocalizacaoGeografica) {
		this.ideFceSesElevatoriaLocalizacaoGeografica = ideFceSesElevatoriaLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public FceSesDadosElevatoria getIdeFceSesDadosElevatoria() {
		return ideFceSesDadosElevatoria;
	}

	public void setIdeFceSesDadosElevatoria(
			FceSesDadosElevatoria ideFceSesDadosElevatoria) {
		this.ideFceSesDadosElevatoria = ideFceSesDadosElevatoria;
	}

	public boolean isLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}

	public Boolean getIndExtravasamento() {
		return indExtravasamento;
	}

	public void setIndExtravasamento(Boolean indExtravasamento) {
		this.indExtravasamento = indExtravasamento;
	}

}
