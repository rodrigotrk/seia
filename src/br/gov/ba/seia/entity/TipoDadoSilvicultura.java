package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * The persistent class for the tipo_dado_silvicultura database table.
 * 
 */
@Entity
@Table(name="tipo_dado_silvicultura")
public class TipoDadoSilvicultura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_dado_silvicultura_ide_tipo_dado_silvicultura_generator")
	@SequenceGenerator(name = "tipo_dado_silvicultura_ide_tipo_dado_silvicultura_generator", sequenceName = "tipo_dado_silvicultura_ide_tipo_dado_silvicultura_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_dado_silvicultura")
	private Integer ideTipoDadoSilvicultura;
	
	@Size(min = 1, max = 50)
	@Column(name="dsc_tipo_dado_silvicultura" ,nullable = false, length = 50)
	private String dscTipoDadoSilvicultura;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public TipoDadoSilvicultura() {
    }

    
	public TipoDadoSilvicultura(Integer ideTipoDadoSilvicultura) {
		this.ideTipoDadoSilvicultura = ideTipoDadoSilvicultura;
	}

	public Integer getIdeTipoDadoSilvicultura() {
		return this.ideTipoDadoSilvicultura;
	}

	public void setIdeTipoDadoSilvicultura(Integer ideTipoDadoSilvicultura) {
		this.ideTipoDadoSilvicultura = ideTipoDadoSilvicultura;
	}

	public String getDscTipoDadoSilvicultura() {
		return this.dscTipoDadoSilvicultura;
	}

	public void setDscTipoDadoSilvicultura(String dscTipoDadoSilvicultura) {
		this.dscTipoDadoSilvicultura = dscTipoDadoSilvicultura;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

}