package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="fce_localizacao_geografica")
public class FceLocalizacaoGeografica implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLocalizacaoGeograficaPK ideFceLocalizacaoGeograficaPK;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce",  nullable = false, insertable = false, updatable = false)
	private Fce ideFce;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica",referencedColumnName = "ide_localizacao_geografica",  nullable = false, insertable = false, updatable = false)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@Transient
	private boolean novoRequerimento;

	public FceLocalizacaoGeografica(){

	}

	public FceLocalizacaoGeografica(Fce fce){
		this.ideFce = fce;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public FceLocalizacaoGeografica(Fce ideFce, LocalizacaoGeografica ideLocalizacaoGeografica, boolean novoRequerimento) {
		this.ideFce = ideFce;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		this.novoRequerimento = novoRequerimento;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public FceLocalizacaoGeograficaPK getIdeFceLocalizacaoGeograficaPK() {
		return ideFceLocalizacaoGeograficaPK;
	}

	public void setIdeFceLocalizacaoGeograficaPK(FceLocalizacaoGeograficaPK ideFceLocalizacaoGeograficaPK) {
		this.ideFceLocalizacaoGeograficaPK = ideFceLocalizacaoGeograficaPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFce == null) ? 0 : ideFce.hashCode());
		result = prime
				* result
				+ ((ideLocalizacaoGeografica == null) ? 0
						: ideLocalizacaoGeografica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceLocalizacaoGeografica other = (FceLocalizacaoGeografica) obj;
		if (ideFce == null) {
			if (other.ideFce != null) {
				return false;
			}
		} else if (!ideFce.equals(other.ideFce)) {
			return false;
		}
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideLocalizacaoGeografica
				.equals(other.ideLocalizacaoGeografica)) {
			return false;
		}
		return true;
	}

	public boolean isNovoRequerimento() {
		return novoRequerimento;
	}

	public void setNovoRequerimento(boolean novoRequerimento) {
		this.novoRequerimento = novoRequerimento;
	}

}