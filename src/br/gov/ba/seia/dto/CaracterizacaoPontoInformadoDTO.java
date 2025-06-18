package br.gov.ba.seia.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
/**
 * Classe abstrata de caracterização do ponto informado
 * @author 
 *
 */
public abstract class CaracterizacaoPontoInformadoDTO implements Serializable {

	public static final long serialVersionUID = 1L;

	protected CerhCaracterizacaoInterface cerhCaracterizacao;
	private boolean indSelecionado;
	
	public abstract Boolean getIndPCH();
	public abstract BigDecimal getValPotencia();
	
	public abstract Boolean getIndOperacao();
	public abstract Date getDataOperacao();
	public abstract BigDecimal getValProducaoAnual();

	public abstract CerhCaracterizacaoInterface getCerhCaracterizacao();

	public boolean getIndSelecionado() {
		return indSelecionado;
	}

	public void setIndSelecionado(boolean indSelecionado) {
		this.indSelecionado = indSelecionado;
	}
	
}
