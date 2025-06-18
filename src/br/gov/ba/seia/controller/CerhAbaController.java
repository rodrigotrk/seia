package br.gov.ba.seia.controller;

import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.util.ContextoUtil;

public abstract class CerhAbaController extends ConverterPontoGeograficoController{

	protected CerhDTO cerhDTO;
	
	public abstract void armazenarHistorico() throws CloneNotSupportedException, Exception;
	public abstract void validarAba();
	public abstract void salvarAba() throws Exception ;
	
	public CerhAbaController() {
		
	}

	public boolean isUsuarioInterno() {
		return !ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
	}
	
	public boolean isUsuarioConsorcio(){
		return cerhDTO.getAbaDadoGerais().isConvenio();
	}
	
	public boolean isUsuarioExterno(){
		return !isUsuarioInterno() && !isUsuarioConsorcio();
	}
	
	public CerhAbaController(CerhDTO cerhDTO) {
		this.cerhDTO=cerhDTO;
	}

	public CerhDTO getCerhDTO() {
		return cerhDTO;
	}
		
}