package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.util.Util;

@SuppressWarnings("serial")
public class BarragemCaracterizacaoPontoInformadoDTO extends CaracterizacaoPontoInformadoDTO {
	/**
	 * Construtor
	 */
	public BarragemCaracterizacaoPontoInformadoDTO(){}
	/**
	 * Construtor
	 * @param cerhBarragemCaracterizacao
	 */
	public BarragemCaracterizacaoPontoInformadoDTO(CerhBarragemCaracterizacao cerhBarragemCaracterizacao) {
		super.cerhCaracterizacao = cerhBarragemCaracterizacao;
	}

	@Override
	public CerhBarragemCaracterizacao getCerhCaracterizacao() {
		return (CerhBarragemCaracterizacao) super.cerhCaracterizacao;
	}
	/**
	 * 
	 * @return caracterização de barragem no cerh
	 */
	public CerhBarragemAproveitamentoHidreletrico getCerhBarragemAproveitamentoHidreletrico(){
		for (CerhBarragemCaracterizacaoFinalidade barragemCaracterizacaoFinalidade : getCerhCaracterizacao().getCerhBarragemCaracterizacaoFinalidadeCollection()) {
			if(!Util.isNull(barragemCaracterizacaoFinalidade.getIdeCerhBarragemAproveitamentoHidreletrico())){
				return barragemCaracterizacaoFinalidade.getIdeCerhBarragemAproveitamentoHidreletrico();
			}
		}
		return null;
	}
	/**
	 * 
	 */
	@Override
	public BigDecimal getValPotencia() {
		if(!Util.isNull(getCerhBarragemAproveitamentoHidreletrico())){
			return getCerhBarragemAproveitamentoHidreletrico().getValPotenciaInstaladaTotal();
		}
		return null;
	}

	@Override
	public BigDecimal getValProducaoAnual() {
		if(!Util.isNull(getCerhBarragemAproveitamentoHidreletrico())){
			return getCerhBarragemAproveitamentoHidreletrico().getValProducaoAnualEfetivamenteVerificada();
		}
		return null;
	}
	
	@Override
	public Boolean getIndOperacao() {
		if(!Util.isNull(getCerhBarragemAproveitamentoHidreletrico())){
			return getCerhBarragemAproveitamentoHidreletrico().getIndEmOperacao();
		}
		return null;
	}

	@Override
	public Boolean getIndPCH() {
		if(!Util.isNull(getCerhBarragemAproveitamentoHidreletrico())){
			return getCerhBarragemAproveitamentoHidreletrico().getIndPch();
		}
		return null;
	}

	@Override
	public Date getDataOperacao() {
		if(!Util.isNull(getCerhBarragemAproveitamentoHidreletrico())){
			return getCerhBarragemAproveitamentoHidreletrico().getDtInicioOperacao();
		}
		return null;
	}
	
}
