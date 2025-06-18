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

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena o motivo do status da reserva de água
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Entity
@Table(name="motivo_reserva_agua")
public class MotivoReservaAgua implements Serializable, BaseEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="motivo_reserva_agua_seq")    
	@SequenceGenerator(name="motivo_reserva_agua_seq", sequenceName="motivo_reserva_agua_seq", allocationSize=1)
	@Column(name="ide_motivo_reserva_agua", unique=true, nullable=false)
	private Integer ideMotivoReservaAgua;

	@Column(name="nom_motivo_reserva_agua", length=100)
	private String nomMotivoReservaAgua;

	@OneToMany(mappedBy="ideMotivoReservaAgua", fetch=FetchType.LAZY)
	private Collection<ReservaAgua> reservaAguaCollection;


	public MotivoReservaAgua() {
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideMotivoReservaAgua == null) ? 0 : ideMotivoReservaAgua.hashCode());
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
		MotivoReservaAgua other = (MotivoReservaAgua) obj;
		if (ideMotivoReservaAgua == null) {
			if (other.ideMotivoReservaAgua != null)
				return false;
		}
		else if (!ideMotivoReservaAgua.equals(other.ideMotivoReservaAgua))
			return false;
		return true;
	}

	public Integer getIdeMotivoReservaAgua() {
		return ideMotivoReservaAgua;
	}

	public void setIdeMotivoReservaAgua(Integer ideMotivoReservaAgua) {
		this.ideMotivoReservaAgua = ideMotivoReservaAgua;
	}

	public String getNomMotivoReservaAgua() {
		return nomMotivoReservaAgua;
	}

	public void setNomMotivoReservaAgua(String nomMotivoReservaAgua) {
		this.nomMotivoReservaAgua = nomMotivoReservaAgua;
	}

	public Collection<ReservaAgua> getReservaAguaCollection() {
		return reservaAguaCollection;
	}

	public void setReservaAguaCollection(Collection<ReservaAgua> reservaAguaCollection) {
		this.reservaAguaCollection = reservaAguaCollection;
	}

	@Override
	public Long getId() {
		return new Long(this.ideMotivoReservaAgua);
	}
	
}