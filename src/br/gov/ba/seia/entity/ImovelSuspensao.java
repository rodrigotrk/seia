package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;
/**
 * 
 * @author danilo.santos
 *
 */

@Entity
@Table(name = "imovel_suspensao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ImovelSuspensao.findAll", query = "SELECT i FROM ImovelSuspensao i"),
		@NamedQuery(name = "ImovelSuspensao.findByIdeImovelSuspensao", query = "SELECT i FROM ImovelSuspensao i WHERE i.ideImovelSuspensao = :ideImovelSuspensao"),
		@NamedQuery(name = "ImovelSuspensao.findByIndExcluido", query = "SELECT i FROM ImovelSuspensao i WHERE i.indExcluido = :indExcluido"),
		@NamedQuery(name = "ImovelSuspensao.findByDtcExclusao", query = "SELECT i FROM ImovelSuspensao i WHERE i.dtcExclusao = :dtcExclusao")})
public class ImovelSuspensao implements Serializable, BaseEntity, Cloneable {
	
	
	private static final long serialVersionUID = -4672218018137018872L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMOVEL_IDE_IMOVEL_seq")
	@SequenceGenerator(name = "IMOVEL_IDE_IMOVEL_seq", sequenceName = "IMOVEL_IDE_IMOVEL_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_suspensao", nullable = false)
	private Integer ideImovelSuspensao;
	
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_inclusao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcInclusao;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "dsc_documento_validador")
	private String dscDocumentoValidador;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	
	
	public ImovelSuspensao() {}
	
	public ImovelSuspensao(Integer ideImovelSuspensao) {
		this.ideImovelSuspensao = ideImovelSuspensao;
	}
	
	public ImovelSuspensao(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
	
	public ImovelSuspensao(Integer ideImovelSuspensao, ImovelRural ideImovelRural, Date dtcInclusao, boolean indExcluido) {
		this.ideImovelSuspensao = ideImovelSuspensao;
		this.ideImovelRural = ideImovelRural;
		this.dtcInclusao = dtcInclusao;
		this.indExcluido = indExcluido;
	}
	
	public ImovelSuspensao(Integer ideImovelSuspensao, Date dtcInclusao, boolean indExcluido) {
		this.ideImovelSuspensao = ideImovelSuspensao;
		this.dtcInclusao = dtcInclusao;
		this.indExcluido = indExcluido;
	}
	
	
	public Integer getIdeImovelSuspensao() {
		return ideImovelSuspensao;
	}
	
	public void setIdeImovelSuspensao(Integer ideImovelSuspensao) {
		this.ideImovelSuspensao = ideImovelSuspensao;
	}
	
	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}
	
	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
	
	public Date getDtcInclusao() {
		return dtcInclusao;
	}
	
	public void setDtcInclusao(Date dtcInclusao) {
		this.dtcInclusao = dtcInclusao;
	}
	
	public boolean getIndExcluido() {
		return indExcluido;
	}
	
	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	public Date getDtcExclusao() {
		return dtcExclusao;
	}
	
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	@Override
	public Long getId() {
		return new Long(this.ideImovelSuspensao);
	}
	
	@Override
	public ImovelSuspensao clone() throws CloneNotSupportedException {
		return (ImovelSuspensao) super.clone();
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideImovelSuspensao != null ? ideImovelSuspensao.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ImovelSuspensao)) {
			return false;
		}
		ImovelSuspensao other = (ImovelSuspensao) object;
		if ((this.ideImovelSuspensao == null && other.ideImovelSuspensao != null) || (this.ideImovelSuspensao != null && !this.ideImovelSuspensao.equals(other.ideImovelSuspensao))) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideImovelSuspensao);
	}
}