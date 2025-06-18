package br.gov.ba.seia.middleware.seia.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe modelo para baixa de boleto
 * @author 
 *
 */
public class BaixaBoletoDTO {

	private Requerimento requerimento;
	private Processo processo;
	private ProcessoReenquadramento processoReenquadramento;
	private List<BoletoPagamentoRequerimento> boletos;
	private boolean gerarProcesso;
	private boolean tramitarProcessoReenquadramento;
	
	public BaixaBoletoDTO() {
		boletos = new ArrayList<BoletoPagamentoRequerimento>();
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}
	
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public ProcessoReenquadramento getProcessoReenquadramento() {
		return processoReenquadramento;
	}

	public void setProcessoReenquadramento(
			ProcessoReenquadramento processoReenquadramento) {
		this.processoReenquadramento = processoReenquadramento;
	}

	public List<BoletoPagamentoRequerimento> getBoletos() {
		return boletos;
	}

	public void setBoletos(List<BoletoPagamentoRequerimento> boletos) {
		this.boletos = boletos;
	}

	public boolean isGerarProcesso() {
		return gerarProcesso;
	}

	public void setGerarProcesso(boolean gerarProcesso) {
		this.gerarProcesso = gerarProcesso;
	}

	public boolean isTramitarProcessoReenquadramento() {
		return tramitarProcessoReenquadramento;
	}

	public void setTramitarProcessoReenquadramento(
			boolean tramitarProcessoReenquadramento) {
		this.tramitarProcessoReenquadramento = tramitarProcessoReenquadramento;
	}

}
