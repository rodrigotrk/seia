package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tipo_corpo_hidrico")
public class TipoCorpoHidrico extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_CORPO_HIDRICO_SEQ", sequenceName="TIPO_CORPO_HIDRICO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_CORPO_HIDRICO_SEQ")
	@Column(name="ide_tipo_corpo_hidrico")
	private Integer ideTipoCorpoHidrico;

	@Historico(name="Tipo do corpo hídrico")
	@Column(name="nom_tipo_corpo_hidrico")
	private String nomTipoCorpoHidrico;

	public TipoCorpoHidrico() {
	}

	public Integer getIdeTipoCorpoHidrico() {
		return this.ideTipoCorpoHidrico;
	}

	public void setIdeTipoCorpoHidrico(Integer ideTipoCorpoHidrico) {
		this.ideTipoCorpoHidrico = ideTipoCorpoHidrico;
	}

	public String getNomTipoCorpoHidrico() {
		return this.nomTipoCorpoHidrico;
	}

	public void setNomTipoCorpoHidrico(String nomTipoCorpoHidrico) {
		this.nomTipoCorpoHidrico = nomTipoCorpoHidrico;
	}
}