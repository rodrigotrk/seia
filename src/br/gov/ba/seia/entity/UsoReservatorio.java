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
@Table(name="uso_reservatorio")
@XmlRootElement
public class UsoReservatorio extends SimpleEntityImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ide_uso_reservatorio_seq", sequenceName="ide_uso_reservatorio_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_uso_reservatorio_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_uso_reservatorio")
	private Integer ideUsoReservatorio;
	
	@Column(name = "nom_uso_reservatorio")
	private String usoReservatorio;
	
	@Column(name = "ind_ativo")
	private boolean ativo;
	
	/**
	 * @return the ideUsoReservatorio
	 */
	public Integer getIdeUsoReservatorio() {
		return ideUsoReservatorio;
	}

	/**
	 * @param ideUsoReservatorio the ideUsoReservatorio to set
	 */
	public void setIdeUsoReservatorio(Integer ideUsoReservatorio) {
		this.ideUsoReservatorio = ideUsoReservatorio;
	}

	/**
	 * @return the usoReservatorio
	 */
	public String getUsoReservatorio() {
		return usoReservatorio;
	}

	/**
	 * @param usoReservatorio the usoReservatorio to set
	 */
	public void setUsoReservatorio(String usoReservatorio) {
		this.usoReservatorio = usoReservatorio;
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
	public Long getId() {
		return new Long(this.ideUsoReservatorio);
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
		UsoReservatorio other = (UsoReservatorio) obj;
		if (ideUsoReservatorio == null) {
			if (other.ideUsoReservatorio != null) {
				return false;
			}
		} else if (!ideUsoReservatorio.equals(other.ideUsoReservatorio)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideUsoReservatorio == null) ? 0 : ideUsoReservatorio.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.getUsoReservatorio();
	}

}
