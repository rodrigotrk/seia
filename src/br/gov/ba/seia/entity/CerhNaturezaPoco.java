package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_natureza_poco")
public class CerhNaturezaPoco extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_natureza_poco")
	private Integer ideCerhNaturezaPoco;

	@Column(name="dsc_natureza_poco")
	private String dscNaturezaPoco;

	@OneToMany(mappedBy="ideCerhNaturezaPoco")
	private Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection;

	public CerhNaturezaPoco() {
	}

	public Integer getIdeCerhNaturezaPoco() {
		return ideCerhNaturezaPoco;
	}

	public void setIdeCerhNaturezaPoco(Integer ideCerhNaturezaPoco) {
		this.ideCerhNaturezaPoco = ideCerhNaturezaPoco;
	}

	public String getDscNaturezaPoco() {
		return dscNaturezaPoco;
	}

	public void setDscNaturezaPoco(String dscNaturezaPoco) {
		this.dscNaturezaPoco = dscNaturezaPoco;
	}

	public Collection<CerhCaptacaoCaracterizacao> getCerhCaptacaoCaracterizacaoCollection() {
		return cerhCaptacaoCaracterizacaoCollection;
	}

	public void setCerhCaptacaoCaracterizacaoCollection(Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection) {
		this.cerhCaptacaoCaracterizacaoCollection = cerhCaptacaoCaracterizacaoCollection;
	}
}