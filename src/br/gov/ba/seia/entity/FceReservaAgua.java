package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name="fce_reserva_agua")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name  = "FceReservaAgua.excluirByIdeFceReservaAgua",
			query = "DELETE FROM FceReservaAgua fra WHERE fra.ideFceReservaAgua = :ideFceReservaAgua"),
})
public class FceReservaAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fce_reserva_agua_seq")    
	@SequenceGenerator(name="fce_reserva_agua_seq", sequenceName="fce_reserva_agua_seq", allocationSize=1)
	@Column(name="ide_fce_reserva_agua", unique=true, nullable=false)
	private Integer ideFceReservaAgua;

	@JoinColumn(name="ide_fce", referencedColumnName="ide_fce",nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Fce ideFce;

	@OneToMany(mappedBy="ideFceReservaAgua", fetch=FetchType.LAZY)
	private Collection<ReservaAgua> reservaAguaCollection;

	public FceReservaAgua() {
	}

	public FceReservaAgua(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Integer getIdeFceReservaAgua() {
		return this.ideFceReservaAgua;
	}

	public void setIdeFceReservaAgua(Integer ideFceReservaAgua) {
		this.ideFceReservaAgua = ideFceReservaAgua;
	}

	public Collection<ReservaAgua> getReservaAguaCollection() {
		return reservaAguaCollection;
	}

	public void setReservaAguaCollection(Collection<ReservaAgua> reservaAguaCollection) {
		this.reservaAguaCollection = reservaAguaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceReservaAgua == null) ? 0 : ideFceReservaAgua.hashCode());
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
		FceReservaAgua other = (FceReservaAgua) obj;
		if (ideFceReservaAgua == null) {
			if (other.ideFceReservaAgua != null)
				return false;
		}
		else if (!ideFceReservaAgua.equals(other.ideFceReservaAgua))
			return false;
		return true;
	}

}