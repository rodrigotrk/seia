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
@Table(name="cerh_tipo_prestador_servico")
public class CerhTipoPrestadorServico extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_tipo_prestador_servico")
	private Integer ideCerhTipoPrestadorServico;
	
	@Historico(name= "Esgotamento Sanitário(Abastecimento Público)")
	@Column(name="dsc_tipo_prestador_servico")
	private String dscTipoPrestadorServico;

	@OneToMany(mappedBy="ideCerhTipoPrestadorServico")
	private Collection<CerhCaptacaoAbastecimentoPublico> cerhCaptacaoAbastecimentoPublicoCollection;

	@OneToMany(mappedBy="ideCerhTipoPrestadorServico")
	private Collection<CerhLancamentoAbastecimentoPublico> cerhLancamentoAbastecimentoPublicoCollection;

	
	public CerhTipoPrestadorServico() {
	}

	public Integer getIdeCerhTipoPrestadorServico() {
		return ideCerhTipoPrestadorServico;
	}

	public void setIdeCerhTipoPrestadorServico(Integer ideCerhTipoPrestadorServico) {
		this.ideCerhTipoPrestadorServico = ideCerhTipoPrestadorServico;
	}

	public String getDscTipoPrestadorServico() {
		return dscTipoPrestadorServico;
	}

	public void setDscTipoPrestadorServico(String dscTipoPrestadorServico) {
		this.dscTipoPrestadorServico = dscTipoPrestadorServico;
	}

	public Collection<CerhCaptacaoAbastecimentoPublico> getCerhCaptacaoAbastecimentoPublicoCollection() {
		return cerhCaptacaoAbastecimentoPublicoCollection;
	}

	public void setCerhCaptacaoAbastecimentoPublicoCollection(Collection<CerhCaptacaoAbastecimentoPublico> cerhCaptacaoAbastecimentoPublicoCollection) {
		this.cerhCaptacaoAbastecimentoPublicoCollection = cerhCaptacaoAbastecimentoPublicoCollection;
	}

	public Collection<CerhLancamentoAbastecimentoPublico> getCerhLancamentoAbastecimentoPublicoCollection() {
		return cerhLancamentoAbastecimentoPublicoCollection;
	}

	public void setCerhLancamentoAbastecimentoPublicoCollection(Collection<CerhLancamentoAbastecimentoPublico> cerhLancamentoAbastecimentoPublicoCollection) {
		this.cerhLancamentoAbastecimentoPublicoCollection = cerhLancamentoAbastecimentoPublicoCollection;
	}

}