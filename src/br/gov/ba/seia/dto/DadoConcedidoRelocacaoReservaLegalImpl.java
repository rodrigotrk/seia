package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.util.Util;


public class DadoConcedidoRelocacaoReservaLegalImpl implements DadoConcedidoInterface{

	private ProcessoAto processoAto;
	private boolean concluido;
	
	public DadoConcedidoRelocacaoReservaLegalImpl(ProcessoAto processoAto, boolean concluido) {
		this.processoAto = processoAto;
		this.concluido = concluido;
	}
	
	@Override
	public String getStatus() {
		
		if(concluido) {
			return Util.getString("analise_tecnica_lbl_status_fce_concluido");
		}
		return Util.getString("analise_tecnica_lbl_status_fce_nao_concluido");
	}

	@Override
	public String getDescricao() {
		return "Formulário de dados concedidos de realocação de reserva legal";
	}

	@Override
	public boolean isRenderedFce() {
		return false;
	}
	
	@Override
	public boolean isRenderedDadoConcedido() {
		return false;
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

	@Override
	public boolean isVisivel() {
		return true;
	}

}
