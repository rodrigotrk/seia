package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the fce_licenciamento_mineral_destino_residuo
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_destino_residuo")
@NamedQuery(name = "FceLicenciamentoMineralDestinoResiduo.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralDestinoResiduo f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralDestinoResiduo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralDestinoResiduoPK ideFceLicenciamentoMineralDestinoResiduoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_destino_residuo", nullable = false, insertable = false, updatable = false)
	private DestinoResiduo ideDestinoResiduo;

	public FceLicenciamentoMineralDestinoResiduo() {

	}

	public FceLicenciamentoMineralDestinoResiduo(FceLicenciamentoMineral ideFceLicenciamentoMineral, DestinoResiduo ideDestinoResiduo) {
		this.fceLicenciamentoMineral = ideFceLicenciamentoMineral;
		this.ideDestinoResiduo = ideDestinoResiduo;
		this.ideFceLicenciamentoMineralDestinoResiduoPK = new FceLicenciamentoMineralDestinoResiduoPK(ideFceLicenciamentoMineral, ideDestinoResiduo);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral ideFceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public DestinoResiduo getIdeDestinoResiduo() {
		return ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(DestinoResiduo ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

}