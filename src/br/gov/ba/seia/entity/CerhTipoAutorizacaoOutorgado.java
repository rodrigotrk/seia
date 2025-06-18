package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_tipo_autorizacao_outorgado")
public class CerhTipoAutorizacaoOutorgado extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_tipo_autorizacao_outorgado")
	private Integer ideCerhTipoAutorizacaoOutorgado;

	@Historico(name="Tipo de Autorização outorgado")
	@Column(name="dsc_cerh_tipo_autorizacao_outorgado")
	private String dscTipoAutorizacaoOutorgado;

	@OneToMany(mappedBy="ideCerhTipoAutorizacaoOutorgado")
	private Collection<CerhProcesso> cerhProcessoCollection;

	public CerhTipoAutorizacaoOutorgado() {
	}

	public Integer getIdeCerhTipoAutorizacaoOutorgado() {
		return ideCerhTipoAutorizacaoOutorgado;
	}

	public void setIdeCerhTipoAutorizacaoOutorgado(Integer ideCerhTipoAutorizacaoOutorgado) {
		this.ideCerhTipoAutorizacaoOutorgado = ideCerhTipoAutorizacaoOutorgado;
	}

	public String getDscTipoAutorizacaoOutorgado() {
		return dscTipoAutorizacaoOutorgado;
	}

	public void setDscTipoAutorizacaoOutorgado(String dscTipoAutorizacaoOutorgado) {
		this.dscTipoAutorizacaoOutorgado = dscTipoAutorizacaoOutorgado;
	}

	public Collection<CerhProcesso> getCerhProcessoCollection() {
		return cerhProcessoCollection;
	}

	public void setCerhProcessoCollection(Collection<CerhProcesso> cerhProcessoCollection) {
		this.cerhProcessoCollection = cerhProcessoCollection;
	}

}