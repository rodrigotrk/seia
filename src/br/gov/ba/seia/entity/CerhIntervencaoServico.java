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
@Table(name="cerh_intervencao_servico")
public class CerhIntervencaoServico  extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_intervencao_servico")
	private Integer ideCerhIntervencaoServico;

	@Historico(name="Descrição da intervenção")
	@Column(name="dsc_intervencao_servico")
	private String dscIntervencaoServico;

	@OneToMany(mappedBy="ideCerhIntervencaoServico")
	private Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection;

	public CerhIntervencaoServico() {
	}

	public Integer getIdeCerhIntervencaoServico() {
		return ideCerhIntervencaoServico;
	}

	public void setIdeCerhIntervencaoServico(Integer ideCerhIntervencaoServico) {
		this.ideCerhIntervencaoServico = ideCerhIntervencaoServico;
	}

	public String getDscIntervencaoServico() {
		return dscIntervencaoServico;
	}

	public void setDscIntervencaoServico(String dscIntervencaoServico) {
		this.dscIntervencaoServico = dscIntervencaoServico;
	}

	public Collection<CerhIntervencaoCaracterizacao> getCerhIntervencaoCaracterizacaoCollection() {
		return cerhIntervencaoCaracterizacaoCollection;
	}

	public void setCerhIntervencaoCaracterizacaoCollection(Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection) {
		this.cerhIntervencaoCaracterizacaoCollection = cerhIntervencaoCaracterizacaoCollection;
	}
	
	public boolean isOutros() {
		return this.dscIntervencaoServico.contains("Outros");
	}

}