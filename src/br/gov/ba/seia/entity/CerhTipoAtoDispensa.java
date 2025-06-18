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
@Table(name="cerh_tipo_ato_dispensa")
public class CerhTipoAtoDispensa extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_tipo_ato_dispensa")
	private Integer ideCerhTipoAtoDispensa;

	@Historico(name="Tipo de ato dispensa")
	@Column(name="dsc_tipo_ato_dispensa")
	private String dscTipoAtoDispensa;

	@OneToMany(mappedBy="ideCerhTipoAtoDispensa")
	private Collection<CerhProcesso> cerhProcessoCollection;

	public CerhTipoAtoDispensa() {
	}

	public Integer getIdeCerhTipoAtoDispensa() {
		return ideCerhTipoAtoDispensa;
	}

	public void setIdeCerhTipoAtoDispensa(Integer ideCerhTipoAtoDispensa) {
		this.ideCerhTipoAtoDispensa = ideCerhTipoAtoDispensa;
	}

	public String getDscTipoAtoDispensa() {
		return dscTipoAtoDispensa;
	}

	public void setDscTipoAtoDispensa(String dscTipoAtoDispensa) {
		this.dscTipoAtoDispensa = dscTipoAtoDispensa;
	}

	public Collection<CerhProcesso> getCerhProcessoCollection() {
		return cerhProcessoCollection;
	}

	public void setCerhProcessoCollection(Collection<CerhProcesso> cerhProcessoCollection) {
		this.cerhProcessoCollection = cerhProcessoCollection;
	}

}