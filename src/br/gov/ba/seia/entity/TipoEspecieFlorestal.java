package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "tipo_especie_florestal")
public class TipoEspecieFlorestal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_especie_florestal_ide_tipo_especie_florestal_seq")
	@SequenceGenerator(name = "tipo_especie_florestal_ide_tipo_especie_florestal_seq", sequenceName = "tipo_especie_florestal_ide_tipo_especie_florestal_seq")
	@Column(name = "ide_tipo_especie_florestal")
	private Integer ideTipoEspecieFlorestal;
	
	@Column(name = "dsc_tipo_especie_florestal")
	private String dscTipoEspecieFlorestal;
	
	@Column(name = "ind_ativo")
	private Boolean indAtivo;
	
	public TipoEspecieFlorestal() {}
	
	public TipoEspecieFlorestal(Integer ideTipoEspecieFlorestal) {
		this.ideTipoEspecieFlorestal = ideTipoEspecieFlorestal;
	}
	
	public TipoEspecieFlorestal(String dscTipoEspecieFlorestal) {
		this.dscTipoEspecieFlorestal = dscTipoEspecieFlorestal;
	}
	

	public Integer getIdeTipoEspecieFlorestal() {
		return ideTipoEspecieFlorestal;
	}
	
	public void setIdeTipoEspecieFlorestal(Integer ideTipoEspecieFlorestal) {
		this.ideTipoEspecieFlorestal = ideTipoEspecieFlorestal;
	}
	
	public String getDscTipoEspecieFlorestal() {
		return dscTipoEspecieFlorestal;
	}
	
	public void setDscTipoEspecieFlorestal(String dscTipoEspecieFlorestal) {
		this.dscTipoEspecieFlorestal = dscTipoEspecieFlorestal;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
}
