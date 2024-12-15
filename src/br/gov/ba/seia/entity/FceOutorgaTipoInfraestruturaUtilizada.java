package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="fce_outorga_tipo_infraestrutura_utilizada")
public class FceOutorgaTipoInfraestruturaUtilizada extends AbstractEntity {
	
	private static final long serialVersionUID = 7447308331320963807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_infraestrutura_utilizada_seq")
	@SequenceGenerator(name = "fce_infraestrutura_utilizada_seq", sequenceName = "fce_infraestrutura_utilizada_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_outorga_tipo_infraestrutura_utilizada")
	private Integer ideFceOutorgaTipoInfraestruturaUtilizada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_outorga_infraestrutura", referencedColumnName = "ide_fce_outorga_infraestrutura")
	private FceOutorgaInfraestrutura ideFceOutorgaInfraestrutura;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_outorga_tipo_infraestrutura", referencedColumnName = "ide_fce_outorga_tipo_infraestrutura")
	private FceOutorgaTipoInfraestrutura ideFceOutorgaTipoInfraestrutura;
	
	public FceOutorgaTipoInfraestruturaUtilizada() {
		super();
	}
	
	public FceOutorgaTipoInfraestruturaUtilizada(FceOutorgaInfraestrutura ideFceOutorgaInfraestrutura, FceOutorgaTipoInfraestrutura ideFceOutorgaTipoInfraestrutura) {
		this.ideFceOutorgaInfraestrutura = ideFceOutorgaInfraestrutura;
		this.ideFceOutorgaTipoInfraestrutura = ideFceOutorgaTipoInfraestrutura;
	}

	public Integer getIdeFceOutorgaTipoInfraestruturaUtilizada() {
		return ideFceOutorgaTipoInfraestruturaUtilizada;
	}

	public void setIdeFceOutorgaTipoInfraestruturaUtilizada(Integer ideFceOutorgaTipoInfraestruturaUtilizada) {
		this.ideFceOutorgaTipoInfraestruturaUtilizada = ideFceOutorgaTipoInfraestruturaUtilizada;
	}

	public FceOutorgaInfraestrutura getIdeFceOutorgaInfraestrutura() {
		return ideFceOutorgaInfraestrutura;
	}

	public void setIdeFceOutorgaInfraestrutura(FceOutorgaInfraestrutura ideFceOutorgaInfraestrutura) {
		this.ideFceOutorgaInfraestrutura = ideFceOutorgaInfraestrutura;
	}

	public FceOutorgaTipoInfraestrutura getIdeFceOutorgaTipoInfraestrutura() {
		return ideFceOutorgaTipoInfraestrutura;
	}

	public void setIdeFceOutorgaTipoInfraestrutura(FceOutorgaTipoInfraestrutura ideFceOutorgaTipoInfraestrutura) {
		this.ideFceOutorgaTipoInfraestrutura = ideFceOutorgaTipoInfraestrutura;
	}

}