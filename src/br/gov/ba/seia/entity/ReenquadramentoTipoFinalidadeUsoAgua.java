package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "reenquadramento_tipo_finalidade_uso_agua")
@NamedQuery(name = "ReenquadramentoTipoFinalidadeUsoAgua.findAll", query = "SELECT r FROM ReenquadramentoTipoFinalidadeUsoAgua r")
public class ReenquadramentoTipoFinalidadeUsoAgua implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@EmbeddedId
	private ReenquadramentoTipoFinalidadeUsoAguaPK reenquadramentoTipoFinalidadeUsoAguaPK;


	@JoinColumn(name = "ide_reenquadramento_processo_ato", referencedColumnName = "ide_reenquadramento_processo_ato", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ReenquadramentoProcessoAto ideReenquadramentoProcessoAto;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", 
	referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	public ReenquadramentoTipoFinalidadeUsoAgua() {
		
	}
	
	public void gerarPK() {
		reenquadramentoTipoFinalidadeUsoAguaPK = new ReenquadramentoTipoFinalidadeUsoAguaPK(
				ideReenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto(),
				ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()
		);
	}
	
	public ReenquadramentoTipoFinalidadeUsoAgua(ReenquadramentoTipoFinalidadeUsoAguaPK reenquadramentoTipoFinalidadeUsoAguaPK) {
		this.reenquadramentoTipoFinalidadeUsoAguaPK = reenquadramentoTipoFinalidadeUsoAguaPK;
	}

	public ReenquadramentoTipoFinalidadeUsoAguaPK getReenquadramentoTipoFinalidadeUsoAguaPK() {
		return reenquadramentoTipoFinalidadeUsoAguaPK;
	}

	public void setReenquadramentoTipoFinalidadeUsoAguaPK(ReenquadramentoTipoFinalidadeUsoAguaPK reenquadramentoTipoFinalidadeUsoAguaPK) {
		this.reenquadramentoTipoFinalidadeUsoAguaPK = reenquadramentoTipoFinalidadeUsoAguaPK;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public ReenquadramentoProcessoAto getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(ReenquadramentoProcessoAto ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reenquadramentoTipoFinalidadeUsoAguaPK == null) ? 0
				: reenquadramentoTipoFinalidadeUsoAguaPK.hashCode());
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
		ReenquadramentoTipoFinalidadeUsoAgua other = (ReenquadramentoTipoFinalidadeUsoAgua) obj;
		if (reenquadramentoTipoFinalidadeUsoAguaPK == null) {
			if (other.reenquadramentoTipoFinalidadeUsoAguaPK != null)
				return false;
		} else if (!reenquadramentoTipoFinalidadeUsoAguaPK.equals(other.reenquadramentoTipoFinalidadeUsoAguaPK))
			return false;
		return true;
	}

}