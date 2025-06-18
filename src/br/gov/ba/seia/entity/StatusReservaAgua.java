package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.StatusReservaAguaEnum;


/**
 * Tabela que armazena o status da reserva de água
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Entity
@Table(name="status_reserva_agua")
public class StatusReservaAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="status_reserva_agua_seq")    
	@SequenceGenerator(name="status_reserva_agua_seq", sequenceName="status_reserva_agua_seq", allocationSize=1)
	@Column(name="ide_status_reserva_agua", unique=true, nullable=false)
	private Integer ideStatusReservaAgua;

	@Column(name="nom_status_reserva_agua", length=20)
	private String nomStatusReservaAgua;

	@OneToMany(mappedBy="ideStatusReservaAgua", fetch=FetchType.LAZY)
	private Collection<ReservaAgua> reservaAguaCollection;

	public StatusReservaAgua() {
		
	}
	
	public StatusReservaAgua(StatusReservaAguaEnum statusReservaAguaEnum) {
		this.ideStatusReservaAgua = statusReservaAguaEnum.getIde();
	}

	public Integer getIdeStatusReservaAgua() {
		return ideStatusReservaAgua;
	}

	public void setIdeStatusReservaAgua(Integer ideStatusReservaAgua) {
		this.ideStatusReservaAgua = ideStatusReservaAgua;
	}

	public String getNomStatusReservaAgua() {
		return nomStatusReservaAgua;
	}

	public void setNomStatusReservaAgua(String nomStatusReservaAgua) {
		this.nomStatusReservaAgua = nomStatusReservaAgua;
	}

	public Collection<ReservaAgua> getReservaAguaCollection() {
		return reservaAguaCollection;
	}

	public void setReservaAguaCollection(
			Collection<ReservaAgua> reservaAguaCollection) {
		this.reservaAguaCollection = reservaAguaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideStatusReservaAgua == null) ? 0 : ideStatusReservaAgua
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
		StatusReservaAgua other = (StatusReservaAgua) obj;
		if (ideStatusReservaAgua == null) {
			if (other.ideStatusReservaAgua != null)
				return false;
		} else if (!ideStatusReservaAgua.equals(other.ideStatusReservaAgua))
			return false;
		return true;
	}

}