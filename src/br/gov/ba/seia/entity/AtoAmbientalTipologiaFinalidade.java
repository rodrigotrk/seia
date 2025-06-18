package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author luis
 */
@Entity
@Table(name = "ato_ambiental_tipologia_finalidade")
@NamedQueries({ @NamedQuery(name = "AtoAmbientalTipologiaFinalidade.findAll", query = "SELECT a FROM AtoAmbientalTipologiaFinalidade a") })
public class AtoAmbientalTipologiaFinalidade implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "ide_ato_ambiental_tipologia_finalidade")
	private Integer ideAtoAmbientalTipologiaFinalidade;

	@JoinColumn(name = "ide_ato_ambiental_tipologia", referencedColumnName = "ide_ato_ambiental_tipologia", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private AtoAmbientalTipologia ideAtoAmbientalTipologia;

	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;

	public AtoAmbientalTipologiaFinalidade() {
	}

	public AtoAmbientalTipologiaFinalidade(Integer ideAtoAmbientalTipologiaFinalidade) {
		this.ideAtoAmbientalTipologiaFinalidade = ideAtoAmbientalTipologiaFinalidade;
	}

	public Integer getIdeAtoAmbientalTipologiaFinalidade() {
		return ideAtoAmbientalTipologiaFinalidade;
	}

	public void setIdeAtoAmbientalTipologiaFinalidade(Integer ideAtoAmbientalTipologiaFinalidade) {
		this.ideAtoAmbientalTipologiaFinalidade = ideAtoAmbientalTipologiaFinalidade;
	}

	public AtoAmbientalTipologia getIdeAtoAmbientalTipologia() {
		return ideAtoAmbientalTipologia;
	}

	public void setIdeAtoAmbientalTipologia(AtoAmbientalTipologia ideAtoAmbientalTipologia) {
		this.ideAtoAmbientalTipologia = ideAtoAmbientalTipologia;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideAtoAmbientalTipologiaFinalidade != null ? ideAtoAmbientalTipologiaFinalidade.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof AtoAmbientalTipologiaFinalidade)) {
			return false;
		}
		AtoAmbientalTipologiaFinalidade other = (AtoAmbientalTipologiaFinalidade) object;
		if ((this.ideAtoAmbientalTipologiaFinalidade == null && other.ideAtoAmbientalTipologiaFinalidade != null)
				|| (this.ideAtoAmbientalTipologiaFinalidade != null && !this.ideAtoAmbientalTipologiaFinalidade
						.equals(other.ideAtoAmbientalTipologiaFinalidade))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "javaapplication1.AtoAmbientalTipologiaFinalidade[ ideAtoAmbientalTipologiaFinalidade="
				+ ideAtoAmbientalTipologiaFinalidade + " ]";
	}

}
