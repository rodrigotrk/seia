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
@Table(name="cerh_finalidade_transposicao")
public class CerhFinalidadeTransposicao extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_finalidade_transposicao")
	private Integer ideCerhFinalidadeTransposicao;

	@Historico(name="Finalidade da transposição")
	@Column(name="dsc_finalidade_transposicao")
	private String dscFinalidadeTransposicao;

	@OneToMany(mappedBy="ideCerhFinalidadeTransposicao")
	private Collection<CerhCaptacaoTransposicao> cerhCaptacaoTransposicaoCollection;

	public CerhFinalidadeTransposicao() {
		
	}

	public CerhFinalidadeTransposicao(Integer ide, String dsc) {
		this.ideCerhFinalidadeTransposicao = ide;
		this.dscFinalidadeTransposicao = dsc;
	}

	public Integer getIdeCerhFinalidadeTransposicao() {
		return ideCerhFinalidadeTransposicao;
	}

	public void setIdeCerhFinalidadeTransposicao(Integer ideCerhFinalidadeTransposicao) {
		this.ideCerhFinalidadeTransposicao = ideCerhFinalidadeTransposicao;
	}

	public String getDscFinalidadeTransposicao() {
		return dscFinalidadeTransposicao;
	}

	public void setDscFinalidadeTransposicao(String dscFinalidadeTransposicao) {
		this.dscFinalidadeTransposicao = dscFinalidadeTransposicao;
	}

	public Collection<CerhCaptacaoTransposicao> getCerhCaptacaoTransposicaoCollection() {
		return cerhCaptacaoTransposicaoCollection;
	}

	public void setCerhCaptacaoTransposicaoCollection(Collection<CerhCaptacaoTransposicao> cerhCaptacaoTransposicaoCollection) {
		this.cerhCaptacaoTransposicaoCollection = cerhCaptacaoTransposicaoCollection;
	}

	public boolean isOutros() {
		return this.ideCerhFinalidadeTransposicao == 6;
	}
}