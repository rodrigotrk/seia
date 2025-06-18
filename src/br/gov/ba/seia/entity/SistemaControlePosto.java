package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "sistema_controle_posto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SistemaControlePosto.findAll", query = "SELECT s FROM SistemaControlePosto s"),
		@NamedQuery(name = "SistemaControlePosto.findByIdeSistemaControlePosto", query = "SELECT s FROM SistemaControlePosto s WHERE s.ideSistemaControlePosto = :ideSistemaControlePosto"),
		@NamedQuery(name = "SistemaControlePosto.findByIndSituacao", query = "SELECT s FROM SistemaControlePosto s WHERE s.indImplantado = :indImplantado"),
		@NamedQuery(name = "SistemaControlePosto.removerByIde", query = "delete from SistemaControlePosto s where s.ideSistemaControlePosto = :ideSistemaControlePosto"),
		@NamedQuery(name = "SistemaControlePosto.findByDtcImplantacao", query = "SELECT s FROM SistemaControlePosto s WHERE s.dtcImplantacao = :dtcImplantacao") })
public class SistemaControlePosto implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistema_seq")
	@SequenceGenerator(name = "sistema_seq", sequenceName = "sistema_controle_posto_ide_sistema_controle_posto_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_sistema_controle_posto")
	private Integer ideSistemaControlePosto;
	@Basic(optional = false)
	@Column(name = "ind_implantado")
	private Boolean indImplantado;
	@Basic(optional = false)
	@Column(name = "dtc_implantacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcImplantacao;
	@JoinColumn(name = "ide_tipo_sistema_controle_posto", referencedColumnName = "ide_tipo_sistema_controle_posto")
	@ManyToOne(optional = false)
	private TipoSistemaControlePosto ideTipoSistemaControlePosto;
	@JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel")
	@ManyToOne(optional = false)
	private LacPostoCombustivel idePostoCombustivel;

	public SistemaControlePosto() {
	}

	public SistemaControlePosto(Integer ideSistemaControlePosto) {
		this.ideSistemaControlePosto = ideSistemaControlePosto;
	}

	public SistemaControlePosto(Integer ideSistemaControlePosto, Boolean indImplantado, Date dtcImplantacao) {
		this.ideSistemaControlePosto = ideSistemaControlePosto;
		this.indImplantado = indImplantado;
		this.dtcImplantacao = dtcImplantacao;
	}

	public Integer getIdeSistemaControlePosto() {
		return ideSistemaControlePosto;
	}

	public void setIdeSistemaControlePosto(Integer ideSistemaControlePosto) {
		this.ideSistemaControlePosto = ideSistemaControlePosto;
	}

	public Boolean getIndImplantado() {
		return indImplantado;
	}

	public void setIndImplantado(Boolean indImplantado) {
		this.indImplantado = indImplantado;
	}

	public Date getDtcImplantacao() {
		return dtcImplantacao;
	}

	public void setDtcImplantacao(Date dtcImplantacao) {
		this.dtcImplantacao = dtcImplantacao;
	}

	public TipoSistemaControlePosto getIdeTipoSistemaControlePosto() {
		return ideTipoSistemaControlePosto;
	}

	public void setIdeTipoSistemaControlePosto(TipoSistemaControlePosto ideTipoSistemaControlePosto) {
		this.ideTipoSistemaControlePosto = ideTipoSistemaControlePosto;
	}

	public LacPostoCombustivel getIdePostoCombustivel() {
		return idePostoCombustivel;
	}

	public void setIdePostoCombustivel(LacPostoCombustivel idePostoCombustivel) {
		this.idePostoCombustivel = idePostoCombustivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtcImplantacao == null) ? 0 : dtcImplantacao.hashCode());
		result = prime * result + ((ideSistemaControlePosto == null) ? 0 : ideSistemaControlePosto.hashCode());
		result = prime * result + ((ideTipoSistemaControlePosto == null) ? 0 : ideTipoSistemaControlePosto.hashCode());
		result = prime * result + ((indImplantado == null) ? 0 : indImplantado.hashCode());
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
		SistemaControlePosto other = (SistemaControlePosto) obj;
		if (dtcImplantacao == null) {
			if (other.dtcImplantacao != null)
				return false;
		} else if (!dtcImplantacao.equals(other.dtcImplantacao))
			return false;
		if (ideSistemaControlePosto == null) {
			if (other.ideSistemaControlePosto != null)
				return false;
		} else if (!ideSistemaControlePosto.equals(other.ideSistemaControlePosto))
			return false;
		if (ideTipoSistemaControlePosto == null) {
			if (other.ideTipoSistemaControlePosto != null)
				return false;
		} else if (!ideTipoSistemaControlePosto.equals(other.ideTipoSistemaControlePosto))
			return false;
		if (indImplantado == null) {
			if (other.indImplantado != null)
				return false;
		} else if (!indImplantado.equals(other.indImplantado))
			return false;
		return true;
	}

	@Override
	public SistemaControlePosto clone() throws CloneNotSupportedException {
		return (SistemaControlePosto) super.clone();
	}
	
	@Override
	public String toString() {
		return "entity.SistemaControlePosto[ ideSistemaControlePosto=" + ideSistemaControlePosto + " ]";
	}

}
