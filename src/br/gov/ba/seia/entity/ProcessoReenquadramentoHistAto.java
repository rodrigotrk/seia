package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "processo_reenquadramento_hist_ato")
public class ProcessoReenquadramentoHistAto implements Serializable {

	private static final long serialVersionUID = -4768794461285342146L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_REENQUADRAMENTO_HIST_ATO_GENERATOR")
	@SequenceGenerator(name = "PROCESSO_REENQUADRAMENTO_HIST_ATO_GENERATOR", sequenceName = "PROCESSO_REENQUADRAMENTO_HIST_ATO_SEQ", allocationSize = 1)
	@Column(name="ide_processo_reenquadramento_hist_ato")
	private Integer ideProcessoReenquadramentoHistAto;

	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private ProcessoReenquadramento ideProcessoReenquadramento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtc_reenquadramento")
	private Date dtcReenquadramento;

	@JoinColumn(name = "ide_ato_ambiental_original", referencedColumnName = "ide_ato_ambiental")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbientalOriginal;

	@JoinColumn(name = "ide_tipologia_original", referencedColumnName = "ide_tipologia")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Tipologia ideTipologiaOriginal;
	
	@JoinColumn(name = "ide_enquadramento_ato_ambiental_reenquadrado", referencedColumnName = "ide_enquadramento_ato_ambiental", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbientalReenquadrado;

	public Integer getIdeProcessoReenquadramentoHistAto() {
		return ideProcessoReenquadramentoHistAto;
	}

	public void setIdeProcessoReenquadramentoHistAto(
			Integer ideProcessoReenquadramentoHistAto) {
		this.ideProcessoReenquadramentoHistAto = ideProcessoReenquadramentoHistAto;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(
			ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public Date getDtcReenquadramento() {
		return dtcReenquadramento;
	}

	public void setDtcReenquadramento(Date dtcReenquadramento) {
		this.dtcReenquadramento = dtcReenquadramento;
	}

	public AtoAmbiental getIdeAtoAmbientalOriginal() {
		return ideAtoAmbientalOriginal;
	}

	public void setIdeAtoAmbientalOriginal(AtoAmbiental ideAtoAmbientalOriginal) {
		this.ideAtoAmbientalOriginal = ideAtoAmbientalOriginal;
	}

	public Tipologia getIdeTipologiaOriginal() {
		return ideTipologiaOriginal;
	}

	public void setIdeTipologiaOriginal(Tipologia ideTipologiaOriginal) {
		this.ideTipologiaOriginal = ideTipologiaOriginal;
	}

	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbientalReenquadrado() {
		return ideEnquadramentoAtoAmbientalReenquadrado;
	}

	public void setIdeEnquadramentoAtoAmbientalReenquadrado(
			EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbientalReenquadrado) {
		this.ideEnquadramentoAtoAmbientalReenquadrado = ideEnquadramentoAtoAmbientalReenquadrado;
	}

}
