package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="tipo_periodo_derivacao")
public class TipoPeriodoDerivacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_periodo_derivacao_ide_tipo_periodo_derivacao_generator")
	@SequenceGenerator(name = "tipo_periodo_derivacao_ide_tipo_periodo_derivacao_generator", sequenceName = "tipo_periodo_derivacao_ide_tipo_periodo_derivacao_seq", allocationSize = 1)
	@Column(name="ide_tipo_periodo_derivacao")
	private Integer ideTipoPeriodoDerivacao;
	
	@Column(name="dsc_tipo_periodo_derivacao")
	private String dscTipoPeriodoDerivacao;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<FceAbastecimentoHumano> fceIdeAbastecimentoHumano;
	
	public TipoPeriodoDerivacao() {}
	
	// Controlador de Tela
	public boolean isIntermitente(){
		return !Util.isNullOuVazio(this.ideTipoPeriodoDerivacao) && this.ideTipoPeriodoDerivacao.equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId());
	}
	
	public boolean isSazonal(){
		return !Util.isNullOuVazio(this.ideTipoPeriodoDerivacao) && this.ideTipoPeriodoDerivacao.equals(TipoPeriodoDerivacaoEnum.SAZONAL.getId());
	}
	
	public TipoPeriodoDerivacao(Integer ideTipoPeriodoDerivacao){
		this.ideTipoPeriodoDerivacao=ideTipoPeriodoDerivacao;
	}
	
	public Integer getIdeTipoPeriodoDerivacao() {
		return this.ideTipoPeriodoDerivacao;
	}
	
	public void setIdeTipoPeriodoDerivacao(Integer ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}
	
	public String getDscTipoPeriodoDerivacao() {
		return this.dscTipoPeriodoDerivacao;
	}
	
	public void setDscTipoPeriodoDerivacao(String dscTipoPeriodoDerivacao) {
		this.dscTipoPeriodoDerivacao = dscTipoPeriodoDerivacao;
	}
	
	public Boolean getIndAtivo() {
		return this.indAtivo;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public List<FceAbastecimentoHumano> getFceIdeAbastecimentoHumano() {
		return fceIdeAbastecimentoHumano;
	}
	
	public void setFceIdeAbastecimentoHumano(List<FceAbastecimentoHumano> fceIdeAbastecimentoHumano) {
		this.fceIdeAbastecimentoHumano = fceIdeAbastecimentoHumano;
	}

}