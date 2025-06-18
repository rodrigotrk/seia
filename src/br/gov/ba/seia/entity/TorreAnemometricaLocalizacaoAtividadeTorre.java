package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "torre_anemometrica_localizacao_atividade_torre")
public class TorreAnemometricaLocalizacaoAtividadeTorre implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TorreAnemometricaLocalizacaoAtividadeTorrePK anemometricaLocalizacaoAtividadeTorrePK;
	
	@JoinColumn(name = "ide_torre_anemometrica", referencedColumnName = "ide_torre_anemometrica", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private TorreAnemometrica ideTorreAnemometrica;

	@JoinColumn(name = "ide_localizacao_atividade_torre", referencedColumnName = "ide_localizacao_atividade_torre", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private LocalizacaoAtividadeTorre ideLocalizacaoAtividadeTorre;
	
	@Column(name = "ind_excluido", nullable = false)
	private Boolean indExcluido;

	public TorreAnemometricaLocalizacaoAtividadeTorre() {
	}

	public TorreAnemometricaLocalizacaoAtividadeTorrePK getAnemometricaLocalizacaoAtividadeTorrePK() {
		return anemometricaLocalizacaoAtividadeTorrePK;
	}
	
	public void setAnemometricaLocalizacaoAtividadeTorrePK(
			TorreAnemometricaLocalizacaoAtividadeTorrePK anemometricaLocalizacaoAtividadeTorrePK) {
		this.anemometricaLocalizacaoAtividadeTorrePK = anemometricaLocalizacaoAtividadeTorrePK;
	}

	public TorreAnemometrica getIdeTorreAnemometrica() {
		return ideTorreAnemometrica;
	}

	public void setIdeTorreAnemometrica(TorreAnemometrica ideTorreAnemometrica) {
		this.ideTorreAnemometrica = ideTorreAnemometrica;
	}

	public LocalizacaoAtividadeTorre getIdeLocalizacaoAtividadeTorre() {
		return ideLocalizacaoAtividadeTorre;
	}

	public void setIdeLocalizacaoAtividadeTorre(
			LocalizacaoAtividadeTorre ideLocalizacaoAtividadeTorre) {
		this.ideLocalizacaoAtividadeTorre = ideLocalizacaoAtividadeTorre;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}
	
	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((anemometricaLocalizacaoAtividadeTorrePK == null) ? 0
						: anemometricaLocalizacaoAtividadeTorrePK.hashCode());
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
		TorreAnemometricaLocalizacaoAtividadeTorre other = (TorreAnemometricaLocalizacaoAtividadeTorre) obj;
		if (anemometricaLocalizacaoAtividadeTorrePK == null) {
			if (other.anemometricaLocalizacaoAtividadeTorrePK != null)
				return false;
		} else if (!anemometricaLocalizacaoAtividadeTorrePK
				.equals(other.anemometricaLocalizacaoAtividadeTorrePK))
			return false;
		return true;
	}

}
