package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TorreAnemometricaLocalizacaoAtividadeTorrePK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "ide_torre_anemometrica")
	private Long ideTorreAnemometrica;
	
	@Column(name = "ide_localizacao_atividade_torre")
	private Long ideLocalizacaoAtividadeTorre;

	public TorreAnemometricaLocalizacaoAtividadeTorrePK() {
	}

	public TorreAnemometricaLocalizacaoAtividadeTorrePK(
			Long ideTorreAnemometrica, Long ideLocalizacaoAtividadeTorre) {
		super();
		this.ideTorreAnemometrica = ideTorreAnemometrica;
		this.ideLocalizacaoAtividadeTorre = ideLocalizacaoAtividadeTorre;
	}

	public Long getIdeTorreAnemometrica() {
		return ideTorreAnemometrica;
	}

	public void setIdeTorreAnemometrica(Long ideTorreAnemometrica) {
		this.ideTorreAnemometrica = ideTorreAnemometrica;
	}

	public Long getIdeLocalizacaoAtividadeTorre() {
		return ideLocalizacaoAtividadeTorre;
	}

	public void setIdeLocalizacaoAtividadeTorre(Long ideLocalizacaoAtividadeTorre) {
		this.ideLocalizacaoAtividadeTorre = ideLocalizacaoAtividadeTorre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideLocalizacaoAtividadeTorre == null) ? 0
						: ideLocalizacaoAtividadeTorre.hashCode());
		result = prime
				* result
				+ ((ideTorreAnemometrica == null) ? 0 : ideTorreAnemometrica
						.hashCode());
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
		TorreAnemometricaLocalizacaoAtividadeTorrePK other = (TorreAnemometricaLocalizacaoAtividadeTorrePK) obj;
		if (ideLocalizacaoAtividadeTorre == null) {
			if (other.ideLocalizacaoAtividadeTorre != null)
				return false;
		} else if (!ideLocalizacaoAtividadeTorre
				.equals(other.ideLocalizacaoAtividadeTorre))
			return false;
		if (ideTorreAnemometrica == null) {
			if (other.ideTorreAnemometrica != null)
				return false;
		} else if (!ideTorreAnemometrica.equals(other.ideTorreAnemometrica))
			return false;
		return true;
	}
	
	
	
}
