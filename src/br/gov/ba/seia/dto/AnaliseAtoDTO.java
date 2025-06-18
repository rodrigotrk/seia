package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ControleProcessoAto;

/**
 * Classe de integração com atributos de outras classes
 * @author 
 *
 */
public class AnaliseAtoDTO {
	
	private ProcessoAto processoAto;
	private ControleProcessoAto contoleProcessoAto;
	private Funcionario tecnico;
	private boolean disabled;
	
	public ProcessoAto getProcessoAto() {
		return processoAto;
	}
	
	public void setProcessoAto(ProcessoAto processoAto) {
		this.processoAto = processoAto;
	}
	
	public ControleProcessoAto getControleProcessoAto() {
		return contoleProcessoAto;
	}
	
	public void setControleProcessoAto(ControleProcessoAto controleProcessoAto) {
		this.contoleProcessoAto = controleProcessoAto;
	}
	
	public Funcionario getTecnico() {
		return tecnico;
	}
	
	public void setTecnico(Funcionario tecnico) {
		this.tecnico = tecnico;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
