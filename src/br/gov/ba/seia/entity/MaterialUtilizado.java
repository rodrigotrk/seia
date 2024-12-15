/**
 * 
 */
package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.SimpleEntityImpl;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="material_utilizado")
@XmlRootElement
public class MaterialUtilizado extends SimpleEntityImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ide_material_utilizado_seq", sequenceName="ide_material_utilizado_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_material_utilizado_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_material_utilizado")
	private Integer ideMaterialUtilizado;
	
	@Column(name = "nom_material_utilizado")
	private String materialUtilizado;
	
	@Column(name = "ind_ativo")
	private boolean ativo;
	
	@Override
	public Long getId() {
		return new Long(ideMaterialUtilizado);
	}


	/**
	 * @return the ideMaterialUtilizado
	 */
	public Integer getIdeMaterialUtilizado() {
		return ideMaterialUtilizado;
	}


	/**
	 * @param ideMaterialUtilizado the ideMaterialUtilizado to set
	 */
	public void setIdeMaterialUtilizado(Integer ideMaterialUtilizado) {
		this.ideMaterialUtilizado = ideMaterialUtilizado;
	}


	/**
	 * @return the materialUtilizado
	 */
	public String getMaterialUtilizado() {
		return materialUtilizado;
	}

	/**
	 * @param materialUtilizado the materialUtilizado to set
	 */
	public void setMaterialUtilizado(String materialUtilizado) {
		this.materialUtilizado = materialUtilizado;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
		MaterialUtilizado other = (MaterialUtilizado) obj;
		if (ideMaterialUtilizado == null) {
			if (other.ideMaterialUtilizado != null) {
				return false;
			}
		} else if (!ideMaterialUtilizado.equals(other.ideMaterialUtilizado)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideMaterialUtilizado == null) ? 0 : ideMaterialUtilizado.hashCode());
		return result;
	}
	
}
