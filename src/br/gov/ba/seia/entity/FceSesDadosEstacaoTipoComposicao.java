package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="fce_ses_dados_estacao_tipo_composicao")
public class FceSesDadosEstacaoTipoComposicao {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_dados_estacao_tipo_composicao_seq")
	@SequenceGenerator(name = "fce_ses_dados_estacao_tipo_composicao_seq", sequenceName = "fce_ses_dados_estacao_tipo_composicao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_dados_estacao_tipo_composicao")
	private Integer ideFceSesDadosEstacaoTipoComposicao;
	
	@Column(name="val_qtde_ativo")
	private Integer valorQuantidadeAtivo;
	
	@Column(name="val_extensao_ativo")
	private Double valorExtensaoAtivo;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses_dados_estacao_tratam_esgoto", referencedColumnName="ide_fce_ses_dados_estacao_tratam_esgoto")
	private FceSesDadosEstacaoTratamentoEsgoto ideFceSesDadosEstacaoTratamentoEsgoto;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses_tipo_composicao", referencedColumnName="ide_fce_ses_tipo_composicao")
	private FceSesTipoComposicao ideFceSesTipoComposicao;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora", referencedColumnName="ide_faixa_diametro_adutora")
	private FaixaDiametroAdutora ideFaixaDiametroAdutora;
	
	@Transient
	private Boolean indDigestorAnaerobio;
	
	@Transient
	private boolean desabilitarItem;
	

	public Integer getIdeFceSesDadosEstacaoTipoComposicao() {
		return ideFceSesDadosEstacaoTipoComposicao;
	}

	public void setIdeFceSesDadosEstacaoTipoComposicao(
			Integer ideFceSesDadosEstacaoTipoComposicao) {
		this.ideFceSesDadosEstacaoTipoComposicao = ideFceSesDadosEstacaoTipoComposicao;
	}

	public Integer getValorQuantidadeAtivo() {
		return valorQuantidadeAtivo;
	}

	public void setValorQuantidadeAtivo(Integer valorQuantidadeAtivo) {
		this.valorQuantidadeAtivo = valorQuantidadeAtivo;
	}

	public Double getValorExtensaoAtivo() {
		return valorExtensaoAtivo;
	}

	public void setValorExtensaoAtivo(Double valorExtensaoAtivo) {
		this.valorExtensaoAtivo = valorExtensaoAtivo;
	}

	public FceSesDadosEstacaoTratamentoEsgoto getIdeFceSesDadosEstacaoTratamentoEsgoto() {
		return ideFceSesDadosEstacaoTratamentoEsgoto;
	}

	public void setIdeFceSesDadosEstacaoTratamentoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto ideFceSesDadosEstacaoTratamentoEsgoto) {
		this.ideFceSesDadosEstacaoTratamentoEsgoto = ideFceSesDadosEstacaoTratamentoEsgoto;
	}

	public FceSesTipoComposicao getIdeFceSesTipoComposicao() {
		return ideFceSesTipoComposicao;
	}

	public void setIdeFceSesTipoComposicao(
			FceSesTipoComposicao ideFceSesTipoComposicao) {
		this.ideFceSesTipoComposicao = ideFceSesTipoComposicao;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutora() {
		return ideFaixaDiametroAdutora;
	}

	public void setIdeFaixaDiametroAdutora(
			FaixaDiametroAdutora ideFaixaDiametroAdutora) {
		this.ideFaixaDiametroAdutora = ideFaixaDiametroAdutora;
	}

	public boolean getDesabilitarItem() {
		return desabilitarItem;
	}

	public void setDesabilitarItem(boolean desabilitarItem) {
		this.desabilitarItem = desabilitarItem;
	}

	public Boolean getIndDigestorAnaerobio() {
		return indDigestorAnaerobio;
	}

	public void setIndDigestorAnaerobio(Boolean indDigestorAnaerobio) {
		this.indDigestorAnaerobio = indDigestorAnaerobio;
	}

}
