package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tipo_reservatorio")
public class TipoReservatorio extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_reservatorio_seq")
	@SequenceGenerator(name = "tipo_reservatorio_seq", sequenceName = "tipo_reservatorio_seq", allocationSize = 1)
	@Column(name="ide_tipo_reservatorio")
	private Integer ideTipoReservatorio;
	
	@Column(name="dsc_tipo_reservatorio")
	private String descricaoTipoReservatorio;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	public TipoReservatorio() {

	}
	
	public Integer getIdeTipoReservatorio() {
		return ideTipoReservatorio;
	}

	public void setIdeTipoReservatorio(Integer ideTipoReservatorio) {
		this.ideTipoReservatorio = ideTipoReservatorio;
	}

	public String getDescricaoTipoReservatorio() {
		return descricaoTipoReservatorio;
	}

	public void setDescricaoTipoReservatorio(String descricaoTipoReservatorio) {
		this.descricaoTipoReservatorio = descricaoTipoReservatorio;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
}
