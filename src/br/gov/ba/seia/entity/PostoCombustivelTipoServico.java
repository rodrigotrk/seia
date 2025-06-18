package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "posto_combustivel_tipo_servico")
@XmlRootElement
@NamedQuery(name="PostoCombustivelTipoServico.removerByIdeLac",query="delete from PostoCombustivelTipoServico l where l.postoCombustivelTipoServicoPK.idePostoCombustivel = :ideLac")
public class PostoCombustivelTipoServico implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PostoCombustivelTipoServicoPK postoCombustivelTipoServicoPK;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation

	@Column(name = "dsc_outros")
	private String dscOutros;

	@JoinColumn(name = "ide_tipo_servico_posto", referencedColumnName = "ide_tipo_servico_posto", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private TipoServicoPosto tipoServicoPosto;

	@JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private LacPostoCombustivel postoCombustivel;

	public PostoCombustivelTipoServico() {
	}

	public PostoCombustivelTipoServicoPK getPostoCombustivelTipoServicoPK() {
		return postoCombustivelTipoServicoPK;
	}

	public void setPostoCombustivelTipoServicoPK(PostoCombustivelTipoServicoPK postoCombustivelTipoServicoPK) {
		this.postoCombustivelTipoServicoPK = postoCombustivelTipoServicoPK;
	}

	public String getDscOutros() {
		return dscOutros;
	}

	public void setDscOutros(String dscOutros) {
		this.dscOutros = dscOutros;
	}

	public TipoServicoPosto getTipoServicoPosto() {
		return tipoServicoPosto;
	}

	public void setTipoServicoPosto(TipoServicoPosto tipoServicoPosto) {
		this.tipoServicoPosto = tipoServicoPosto;
	}

	public LacPostoCombustivel getPostoCombustivel() {
		return postoCombustivel;
	}

	public void setPostoCombustivel(LacPostoCombustivel postoCombustivel) {
		this.postoCombustivel = postoCombustivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postoCombustivelTipoServicoPK == null) ? 0 : postoCombustivelTipoServicoPK.hashCode());
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
		PostoCombustivelTipoServico other = (PostoCombustivelTipoServico) obj;
		if (postoCombustivelTipoServicoPK == null) {
			if (other.postoCombustivelTipoServicoPK != null)
				return false;
		} else if (!postoCombustivelTipoServicoPK.equals(other.postoCombustivelTipoServicoPK))
			return false;
		return true;
	}

}
