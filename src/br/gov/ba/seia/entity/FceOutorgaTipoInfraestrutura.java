package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="fce_outorga_tipo_infraestrutura")
public class FceOutorgaTipoInfraestrutura extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_outorga_tipo_infraestrutura_seq")
	@SequenceGenerator(name = "fce_outorga_tipo_infraestrutura_seq", sequenceName = "fce_outorga_tipo_infraestrutura_seq", allocationSize = 1)
	@Column(name="ide_fce_outorga_tipo_infraestrutura")
	private Integer ideFceOutorgaTipoInfraestrutura;
	
	@Basic(optional = false)
	@Column(name="dsc_fce_tipo_infraestrutura")
	private String dscFceTipoInfraestrutura;
	
	@Basic(optional = false)
	@Column(name="ind_excluido")
	private boolean indExcluido;
	
	@Transient
	private boolean indChecked;
	
	@OneToMany(mappedBy="ideFceOutorgaTipoInfraestrutura", fetch = FetchType.LAZY)
	private List<FceOutorgaTipoInfraestruturaUtilizada> fceOutorgaTipoInfraestruturaUtilizadaCollection;
	
	public FceOutorgaTipoInfraestrutura() {}
	
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeFceOutorgaTipoInfraestrutura() {
		return ideFceOutorgaTipoInfraestrutura;
	}

	public void setIdeFceOutorgaTipoInfraestrutura(Integer ideFceOutorgaTipoInfraestrutura) {
		this.ideFceOutorgaTipoInfraestrutura = ideFceOutorgaTipoInfraestrutura;
	}

	public String getDscFceTipoInfraestrutura() {
		return dscFceTipoInfraestrutura;
	}

	public void setDscFceTipoInfraestrutura(String dscFceTipoInfraestrutura) {
		this.dscFceTipoInfraestrutura = dscFceTipoInfraestrutura;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public boolean isIndChecked() {
		return indChecked;
	}

	public void setIndChecked(boolean indChecked) {
		this.indChecked = indChecked;
	}

	public List<FceOutorgaTipoInfraestruturaUtilizada> getFceOutorgaTipoInfraestruturaUtilizadaCollection() {
		return fceOutorgaTipoInfraestruturaUtilizadaCollection;
	}

	public void setFceOutorgaTipoInfraestruturaUtilizadaCollection(List<FceOutorgaTipoInfraestruturaUtilizada> fceOutorgaTipoInfraestruturaUtilizadaCollection) {
		this.fceOutorgaTipoInfraestruturaUtilizadaCollection = fceOutorgaTipoInfraestruturaUtilizadaCollection;
	}
}