package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the fce_licenciamento_mineral_servidao_mineraria
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_servidao_mineraria")
@NamedQuery(name = "FceLicenciamentoMineralServidaoMineraria.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralServidaoMineraria f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralServidaoMineraria implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralServidaoMinerariaPK ideFceLicenciamentoMineralServidaoMinerariaPK;

	@Column(name = "area_servidao_mineraria")
	private BigDecimal areaServidaoMineraria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_servidao_mineraria", nullable = false, insertable = false, updatable = false)
	private ServidaoMineraria servidaoMineraria;

	@Transient
	private boolean confirmado;

	@Transient
	private boolean edicao;

	public FceLicenciamentoMineralServidaoMineraria() {

	}

	public FceLicenciamentoMineralServidaoMineraria(FceLicenciamentoMineral fceLicenciamentoMineral, ServidaoMineraria servidaoMineraria) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.servidaoMineraria = servidaoMineraria;
	}

	public BigDecimal getAreaServidaoMineraria() {
		return areaServidaoMineraria;
	}

	public void setAreaServidaoMineraria(BigDecimal areaServidaoMineraria) {
		this.areaServidaoMineraria = areaServidaoMineraria;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public ServidaoMineraria getServidaoMineraria() {
		return servidaoMineraria;
	}

	public void setServidaoMineraria(ServidaoMineraria servidaoMineraria) {
		this.servidaoMineraria = servidaoMineraria;
	}

	public boolean isConfirmado() {
		return this.confirmado;
	}

	public void setConfirmado(boolean b) {
		this.confirmado = b;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao() {
		this.edicao = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fceLicenciamentoMineral == null) ? 0 : fceLicenciamentoMineral.hashCode());
		result = prime * result + ((servidaoMineraria == null) ? 0 : servidaoMineraria.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FceLicenciamentoMineralServidaoMineraria other = (FceLicenciamentoMineralServidaoMineraria) obj;
		if(fceLicenciamentoMineral == null){
			if(other.fceLicenciamentoMineral != null)
				return false;
		}
		else if(!fceLicenciamentoMineral.equals(other.fceLicenciamentoMineral))
			return false;
		if(servidaoMineraria == null){
			if(other.servidaoMineraria != null)
				return false;
		}
		else if(!servidaoMineraria.equals(other.servidaoMineraria))
			return false;
		return true;
	}

	public FceLicenciamentoMineralServidaoMinerariaPK getIdeFceLicenciamentoMineralServidaoMinerariaPK() {
		return ideFceLicenciamentoMineralServidaoMinerariaPK;
	}

	public void setIdeFceLicenciamentoMineralServidaoMinerariaPK(FceLicenciamentoMineralServidaoMinerariaPK ideFceLicenciamentoMineralServidaoMinerariaPK) {
		this.ideFceLicenciamentoMineralServidaoMinerariaPK = ideFceLicenciamentoMineralServidaoMinerariaPK;
	}

}