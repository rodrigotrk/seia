package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh_captacao_abastecimento_publico")
public class CerhCaptacaoAbastecimentoPublico extends AbstractEntityHist implements CerhDadosFinalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_abastecimento_publico_seq")
	@SequenceGenerator(name = "cerh_captacao_abastecimento_publico_seq", sequenceName = "cerh_captacao_abastecimento_publico_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_abastecimento_publico")
	private Integer ideCerhCaptacaoAbastecimentoPublico;

	@Historico(name="Perda na distribuição")
	@Column(name="ind_perda_distribuicao")
	private Boolean indPerdaDistribuicao;
	
	@Historico(name="Incerto/Desconhecido")
	@Column(name="ind_incerto_desconhecido")
	private Boolean indIncertoDesconhecido;

	@Historico(name="Valor do índice de perda na distribuição(%)")
	@Column(name="val_indice_perda_distribuicao")
	private Integer valIndicePerdaDistribuicao;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_tipo_prestador_servico")
	private CerhTipoPrestadorServico ideCerhTipoPrestadorServico;

	@Transient
	private int tipoPerda;
	
	public CerhCaptacaoAbastecimentoPublico() {
	}

	public CerhCaptacaoAbastecimentoPublico(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeAbastecimentoPublico) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeAbastecimentoPublico;
	}

	public Integer getIdeCerhCaptacaoAbastecimentoPublico() {
		return ideCerhCaptacaoAbastecimentoPublico;
	}

	public void setIdeCerhCaptacaoAbastecimentoPublico(Integer ideCerhCaptacaoAbastecimentoPublico) {
		this.ideCerhCaptacaoAbastecimentoPublico = ideCerhCaptacaoAbastecimentoPublico;
	}

	public Boolean getIndPerdaDistribuicao() {
		return indPerdaDistribuicao;
	}

	public Integer getValIndicePerdaDistribuicao() {
		return valIndicePerdaDistribuicao;
	}

	public void setValIndicePerdaDistribuicao(Integer valIndicePerdaDistribuicao) {
		this.valIndicePerdaDistribuicao = valIndicePerdaDistribuicao;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(
			CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public CerhTipoPrestadorServico getIdeCerhTipoPrestadorServico() {
		return ideCerhTipoPrestadorServico;
	}

	public void setIdeCerhTipoPrestadorServico(CerhTipoPrestadorServico ideCerhTipoPrestadorServico) {
		this.ideCerhTipoPrestadorServico = ideCerhTipoPrestadorServico;
	}

	public int getTipoPerda() {
		return tipoPerda;
	}

	public void setTipoPerda(int tipoPerda) {
		this.tipoPerda = tipoPerda;
	}

	public Boolean getIndIncertoDesconhecido() {
		return indIncertoDesconhecido;
	}

	public void setIndIncertoDesconhecido(Boolean indIncertoDesconhecido) {
		this.indIncertoDesconhecido = indIncertoDesconhecido;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoAbastecimentoPublico;
	}
	
	public void setIndPerdaDistribuicao(Boolean indPerdaDistribuicao) {
		this.indPerdaDistribuicao = indPerdaDistribuicao;
		if(!Util.isNull(this.indPerdaDistribuicao)) {
			this.indIncertoDesconhecido = null;
		}
	}

}