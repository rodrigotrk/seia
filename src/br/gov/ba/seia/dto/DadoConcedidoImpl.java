package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.util.Util;


public class DadoConcedidoImpl implements DadoConcedidoInterface {

	private ProcessoAto processoAto;
	private boolean concluido;
	
	public DadoConcedidoImpl(ProcessoAto processoAto) {
		this.processoAto = processoAto;
	}
	
	public DadoConcedidoImpl(ProcessoAto processoAto, boolean concluido) {
		this(processoAto);
		this.concluido = concluido;
	}

	public String getStatus() {
		if(concluido) {
			return Util.getString("analise_tecnica_lbl_status_fce_concluido");
		}
		return Util.getString("analise_tecnica_lbl_status_fce_nao_concluido");
	}
	
	public String getDescricao() {
		if(isAtoRealocacaoReservaLegal()) {
			return "Formulário de dados concedidos de relocação de reserva legal";
		}  
		else if (processoAto.getAtoAmbiental().isFlorestal()) {
			return processoAto.getNomAtoAmbiental();
		}
		return "";
	}

	public boolean isAtoRealocacaoReservaLegal() {
		return getAtoAmbientalEnum().equals(AtoAmbientalEnum.ARRL);
	}
	
	public boolean isAtoAutorizacaoSupressaoVegetacao() {
		return getAtoAmbientalEnum().equals(AtoAmbientalEnum.ASV);
	}
	
	public boolean isAMC() {
		return getAtoAmbientalEnum().equals(AtoAmbientalEnum.AMC);
	}
	
	private AtoAmbientalEnum getAtoAmbientalEnum() {
		return AtoAmbientalEnum.getEnum(processoAto.getAtoAmbiental());
	}
	
	public boolean isRenderedFce() {
		return false;
	}

	public boolean isRenderedDadoConcedido() {
		return true;
	}

	public ProcessoAto getProcessoAto() {
		return processoAto;
	}

	public void setProcessoAto(ProcessoAto processoAto) {
		this.processoAto = processoAto;
	}
	
	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	public boolean isVisivel() {
		return true;
	}


}
