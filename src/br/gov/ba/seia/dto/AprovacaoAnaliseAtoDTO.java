package br.gov.ba.seia.dto;

import java.util.Collection;

import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ControleProcessoAto;

/**
 * Classe de integração
 * @author 
 *
 */
public class AprovacaoAnaliseAtoDTO {
	
	private ProcessoAto processoAto;
	private Collection<ControleProcessoAto> listaControleProcessoAto;
	
	public ProcessoAto getProcessoAto() {
		return processoAto;
	}
	
	public void setProcessoAto(ProcessoAto processoAto) {
		this.processoAto = processoAto;
	}
	/**
	 * 
	 * @return Lista de controle de processos
	 */
	public Collection<ControleProcessoAto> getListaControleProcessoAto() {
		return listaControleProcessoAto;
	}

	public void setListaControleProcessoAto(Collection<ControleProcessoAto> listaControleProcessoAto) {
		this.listaControleProcessoAto = listaControleProcessoAto;
	}
}
