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
@Table(name="cerh_obras_hidraulicas")
public class CerhObrasHidraulicas extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_obras_hidraulicas")
	private Integer ideCerhObrasHidraulicas;

	@Historico(name="Descrição obras hidraulicas")
	@Column(name="dsc_obras_hidraulicas")
	private String dscObrasHidraulicas;

	@OneToMany(mappedBy="ideCerhObrasHidraulicas")
	private Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection;

	public CerhObrasHidraulicas() {
	}

	public Integer getIdeCerhObrasHidraulicas() {
		return ideCerhObrasHidraulicas;
	}

	public void setIdeCerhObrasHidraulicas(Integer ideCerhObrasHidraulicas) {
		this.ideCerhObrasHidraulicas = ideCerhObrasHidraulicas;
	}

	public String getDscObrasHidraulicas() {
		return dscObrasHidraulicas;
	}

	public void setDscObrasHidraulicas(String dscObrasHidraulicas) {
		this.dscObrasHidraulicas = dscObrasHidraulicas;
	}

	public Collection<CerhIntervencaoCaracterizacao> getCerhIntervencaoCaracterizacaoCollection() {
		return cerhIntervencaoCaracterizacaoCollection;
	}

	public void setCerhIntervencaoCaracterizacaoCollection(Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection) {
		this.cerhIntervencaoCaracterizacaoCollection = cerhIntervencaoCaracterizacaoCollection;
	}
	
	public boolean isOutros() {
		return this.dscObrasHidraulicas.compareTo("Outros") == 0;
	}

}