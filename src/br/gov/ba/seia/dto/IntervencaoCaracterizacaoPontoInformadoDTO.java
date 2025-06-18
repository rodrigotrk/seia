package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;

@SuppressWarnings("serial")
public class IntervencaoCaracterizacaoPontoInformadoDTO extends CaracterizacaoPontoInformadoDTO {

	public IntervencaoCaracterizacaoPontoInformadoDTO(){}
	
	public IntervencaoCaracterizacaoPontoInformadoDTO(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao) {
		super.cerhCaracterizacao = cerhIntervencaoCaracterizacao;
	}	
	
	@Override
	public CerhIntervencaoCaracterizacao getCerhCaracterizacao() {
		return (CerhIntervencaoCaracterizacao) super.cerhCaracterizacao;
	}
	
	@Override
	public BigDecimal getValPotencia() {
		return ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao()).getValPotenciaInstaladaTotal();
	}

	@Override
	public BigDecimal getValProducaoAnual() {
		return ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao()).getValProducaoAnualEfetivamenteVerificada();
	}

	@Override
	public Boolean getIndOperacao() {
		return ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao()).getIndOperacao();
	}

	@Override
	public Boolean getIndPCH() {
		return ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao()).getIndPch();
	}

	@Override
	public Date getDataOperacao() {
		return ((CerhIntervencaoCaracterizacao) getCerhCaracterizacao()).getDtInicioOperacao();
	}
	
}
