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
@Table(name="tipo_material_utilizado")
public class TipoMaterialUtilizado extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_material_utilizado_seq")
	@SequenceGenerator(name = "tipo_material_utilizado_seq", sequenceName = "tipo_material_utilizado_seq", allocationSize = 1)
	@Column(name="ide_tipo_material_utilizado")
	private Integer ideTipoMaterialUtilizado;
	
	@Column(name="dsc_material_utilizado")
	private String descricaoTipoMaterialUtilizado;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	public TipoMaterialUtilizado() {
	}

	public Integer getIdeTipoMaterialUtilizado() {
		return ideTipoMaterialUtilizado;
	}

	public void setIdeTipoMaterialUtilizado(Integer ideTipoMaterialUtilizado) {
		this.ideTipoMaterialUtilizado = ideTipoMaterialUtilizado;
	}

	public String getDescricaoTipoMaterialUtilizado() {
		return descricaoTipoMaterialUtilizado;
	}

	public void setDescricaoTipoMaterialUtilizado(String descricaoTipoMaterialUtilizado) {
		this.descricaoTipoMaterialUtilizado = descricaoTipoMaterialUtilizado;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
}
