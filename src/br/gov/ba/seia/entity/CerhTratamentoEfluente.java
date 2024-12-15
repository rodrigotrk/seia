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
@Table(name="cerh_tratamento_efluente")
public class CerhTratamentoEfluente extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_tratamento_efluente")
	private Integer ideCerhTratamentoEfluente;

	@Historico(name="Tratamento do efluente")
	@Column(name="dsc_tratamento_efluente")
	private String dscTratamentoEfluente;

	@OneToMany(mappedBy="ideCerhTratamentoEfluente")
	private Collection<CerhLancamentoEfluenteCaracterizacao> cerhLancamentoEfluenteCaracterizacaoCollection;

	public CerhTratamentoEfluente() {
	}

	public Integer getIdeCerhTratamentoEfluente() {
		return ideCerhTratamentoEfluente;
	}

	public void setIdeCerhTratamentoEfluente(Integer ideCerhTratamentoEfluente) {
		this.ideCerhTratamentoEfluente = ideCerhTratamentoEfluente;
	}

	public String getDscTratamentoEfluente() {
		return dscTratamentoEfluente;
	}

	public void setDscTratamentoEfluente(String dscTratamentoEfluente) {
		this.dscTratamentoEfluente = dscTratamentoEfluente;
	}

	public Collection<CerhLancamentoEfluenteCaracterizacao> getCerhLancamentoEfluenteCaracterizacaoCollection() {
		return cerhLancamentoEfluenteCaracterizacaoCollection;
	}

	public void setCerhLancamentoEfluenteCaracterizacaoCollection(Collection<CerhLancamentoEfluenteCaracterizacao> cerhLancamentoEfluenteCaracterizacaoCollection) {
		this.cerhLancamentoEfluenteCaracterizacaoCollection = cerhLancamentoEfluenteCaracterizacaoCollection;
	}
}