package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.enumerator.CerhSituacaoRegularizacaoEnum;


@Entity
@Table(name="cerh_situacao_regularizacao")
public class CerhSituacaoRegularizacao extends AbstractEntityHist implements Comparable<CerhSituacaoRegularizacao> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_situacao_regularizacao")
	private Integer ideCerhSituacaoRegularizacao;

	@Historico(name="Situação da regularização")
	@Column(name="dsc_situacao_regularizacao")
	private String dscSituacaoRegularizacao;

	@OneToMany(mappedBy="ideCerhSituacaoRegularizacao")
	private Collection<CerhProcesso> cerhProcessoCollection;

	public CerhSituacaoRegularizacao() {
	}
	
	public CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum cerhSituacaoRegularizacaoEnum) {
		if (cerhSituacaoRegularizacaoEnum != null) {
			this.ideCerhSituacaoRegularizacao = cerhSituacaoRegularizacaoEnum.getId();
			this.dscSituacaoRegularizacao = cerhSituacaoRegularizacaoEnum.getDsc();
		}
	}

	public CerhSituacaoRegularizacao(Integer ideCerhSituacaoRegularizacao) {
		this.ideCerhSituacaoRegularizacao = ideCerhSituacaoRegularizacao;
	}

	public Integer getIdeCerhSituacaoRegularizacao() {
		return ideCerhSituacaoRegularizacao;
	}

	public void setIdeCerhSituacaoRegularizacao(Integer ideCerhSituacaoRegularizacao) {
		this.ideCerhSituacaoRegularizacao = ideCerhSituacaoRegularizacao;
	}

	public String getDscSituacaoRegularizacao() {
		return dscSituacaoRegularizacao;
	}

	public void setDscSituacaoRegularizacao(String dscSituacaoRegularizacao) {
		this.dscSituacaoRegularizacao = dscSituacaoRegularizacao;
	}

	public Collection<CerhProcesso> getCerhProcessoCollection() {
		return cerhProcessoCollection;
	}

	public void setCerhProcessoCollection(Collection<CerhProcesso> cerhProcessoCollection) {
		this.cerhProcessoCollection = cerhProcessoCollection;
	}

	@Override
	public int compareTo(CerhSituacaoRegularizacao o) {
		return dscSituacaoRegularizacao.compareTo(o.getDscSituacaoRegularizacao());
	}

}