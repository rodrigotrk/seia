package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "outorga_tipo_receptor")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "OutorgaTipoReceptor.deleteByIdeOutorga", query = "DELETE FROM OutorgaTipoReceptor otc WHERE otc.ideOutorga = :ideOutorga"),
		@NamedQuery(name = "OutorgaTipoReceptor.deleteByIdeOutorgaTipoReceptor", query = "DELETE FROM OutorgaTipoReceptor otc WHERE otc.ideOutorgaTipoReceptor = :ideOutorgaTipoReceptor") })
public class OutorgaTipoReceptor implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outorga_tipo_receptor_ide_outorga_tipo_receptor_seq")
	@SequenceGenerator(name = "outorga_tipo_receptor_ide_outorga_tipo_receptor_seq", sequenceName = "outorga_tipo_receptor_ide_outorga_tipo_receptor_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_outorga_tipo_receptor", nullable = false)
	private Integer ideOutorgaTipoReceptor;

	@JoinColumn(name = "ide_outorga", referencedColumnName = "ide_outorga", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Outorga ideOutorga;

	@JoinColumn(name = "ide_tipo_receptor", referencedColumnName = "ide_tipo_receptor", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoReceptor ideTipoReceptor;

	public OutorgaTipoReceptor(Outorga ideOutorga, TipoReceptor ideTipoReceptor) {

		this.ideOutorga = ideOutorga;
		this.ideTipoReceptor = ideTipoReceptor;
	}

	public OutorgaTipoReceptor() {

	}

	public Integer getIdeOutorgaTipoReceptor() {
		return ideOutorgaTipoReceptor;
	}

	public void setIdeOutorgaTipoReceptor(Integer ideOutorgaTipoReceptor) {
		this.ideOutorgaTipoReceptor = ideOutorgaTipoReceptor;
	}

	public Outorga getIdeOutorga() {
		return ideOutorga;
	}

	public void setIdeOutorga(Outorga ideOutorga) {
		this.ideOutorga = ideOutorga;
	}

	public TipoReceptor getIdeTipoReceptor() {
		return ideTipoReceptor;
	}

	public void setIdeTipoReceptor(TipoReceptor ideTipoReceptor) {
		this.ideTipoReceptor = ideTipoReceptor;
	}

	@Override
	public Long getId() {
		return new Long(this.ideOutorgaTipoReceptor);
	}

}
