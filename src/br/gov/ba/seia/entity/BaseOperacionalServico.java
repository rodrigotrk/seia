package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the base_operacional_servico database table.
 * 
 */
@Entity
@Table(name="base_operacional_servico")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "BaseOperacionalServico.excluirByIdeBaseOperacional", query = "DELETE FROM BaseOperacionalServico bs WHERE bs.ideBaseOperacional.ideBaseOperacional = :ideBaseOperacional")
})
public class BaseOperacionalServico implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected BaseOperacionalServicoPK id;
	
	@NotNull
	@JoinColumn(name = "ide_base_operacional", referencedColumnName = "ide_base_operacional", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private BaseOperacional ideBaseOperacional;

	@NotNull
	@JoinColumn(name = "ide_tipo_servico_base", referencedColumnName = "ide_tipo_servico_base", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoServicoBase ideTipoServicoBase;
	
    public BaseOperacionalServico() {
    	
    }

	public BaseOperacionalServicoPK getId() {
		return this.id;
	}

	public void setId(BaseOperacionalServicoPK id) {
		this.id = id;
	}

	public BaseOperacional getIdeBaseOperacional() {
		return ideBaseOperacional;
	}

	public void setIdeBaseOperacional(BaseOperacional ideBaseOperacional) {
		this.ideBaseOperacional = ideBaseOperacional;
	}

	public TipoServicoBase getIdeTipoServicoBase() {
		return ideTipoServicoBase;
	}

	public void setIdeTipoServicoBase(TipoServicoBase ideTipoServicoBase) {
		this.ideTipoServicoBase = ideTipoServicoBase;
	}

}