package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Sistema;
import br.gov.ba.seia.enumerator.SistemaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.ProcessoTramiteService;
import br.gov.ba.seia.service.SistemaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("processoTramiteController")
@ViewScoped
public class ProcessoTramiteController extends BaseDialogController {

	private ProcessoTramite processoTramite;
	
	@Inject
	private AbaProcessoController abaProcessoController;
	
	@EJB
	private ProcessoExternoService processoExternoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private SistemaService sistemaService;
	
	@EJB
	private ProcessoTramiteService processoTramiteService;
	
	
	public void load() {
		this.limpar();
	}

	void limpar() {
		Requerimento requerimento = this.abaProcessoController.getRequerimento();
		this.processoTramite = new ProcessoTramite(requerimento);
		this.editMode = false;
	}
	
	public <T> void editar(T processoTramite){
		this.processoTramite = (ProcessoTramite) processoTramite;
		this.editMode = true;
	}
	
	public void salvar(){
		
		try {
			
			if(!this.validar()){
				return;
			}
			
			Sistema sistemaProcesso = this.verificarSistema();			
			this.salvarSistemaProcessoExterno(sistemaProcesso);
			
			this.processoTramite.setIdeSistema(sistemaProcesso);
			this.processoTramiteService.salvarOuAtualizar(this.processoTramite);
			
			this.abaProcessoController.adicionarOuAtualizarListaProcessoTramite(this.processoTramite);
			
			if(this.editMode){
				RequestContext.getCurrentInstance().execute("dialogProcessoInema.hide()");
				JsfUtil.addSuccessMessage("Processo atualizado com sucesso.");
			}else{
				this.load();
				JsfUtil.addSuccessMessage("Processo salvo com sucesso.");
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean validar() {
		
		boolean valido = true;
		
		if (Util.isNullOuVazio(processoTramite.getNumProcessoTramite())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 1.");
			valido = false;
		} 
		
		this.formatarNumeroProcesso();
		
		Collection<ProcessoTramite> listaProcessoTramite = this.abaProcessoController.getListProcessoTramite();
		if (listaProcessoTramite.contains(processoTramite) && Util.isNullOuVazio(this.processoTramite.getIdeProcessoTramite())) {
			JsfUtil.addWarnMessage("Processo já cadastrado.");
			valido = false;
		}
		
		return valido;
	}


	private void formatarNumeroProcesso() {
		String processo = processoTramite.getNumProcessoTramite();
		processoTramite.setNumProcessoTramite(this.trim(processo));
	}
	
	public String trim(String str) {
		return  str.replace(String.valueOf((char) 160), " ").trim();
	}
	
	
	public Sistema verificarSistema() throws Exception{
		
		try {
			
			String numProcesso = this.processoTramite.getNumProcessoTramite();
			
			Processo processo = this.processoService.buscarProcessoPorCriteria(numProcesso);
			
			if (Util.isNullOuVazio(processo)) {
				ProcessoExterno processoExterno = this.processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso);
				
				if (Util.isNullOuVazio(processoExterno)) {
					JsfUtil.addWarnMessage("Processo não encontrado.");
					return this.getSistema(SistemaEnum.DESCONHECIDO);
				} else{
					this.processoTramite.setDscTipoProcessoTramite(processoExterno.getTipo());
					this.processoTramite.setNumProcessoTramite(processoExterno.getProcesso());
					return this.getSistema(processoExterno.getSistema());
				}
			} else {
				this.processoTramite.setNumProcessoTramite(processo.getNumProcesso());
				return this.getSistema(SistemaEnum.SEIA);
			}

		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro em algum processo:" + e.getMessage());
			throw e;
		}
	}
	
	private Sistema getSistema(SistemaEnum sistemaEnum) {
		return new Sistema(sistemaEnum.getId(), sistemaEnum.getNomSistema());
	}
	
	private Sistema getSistema(String sistema) {
		for (SistemaEnum sistemaEnum : SistemaEnum.values()) {
			if(sistemaEnum.getNomSistema().equalsIgnoreCase(sistema)){
				return  new Sistema(sistemaEnum.getId(), sistemaEnum.getNomSistema());
			}
		}
		
		return new Sistema(SistemaEnum.DESCONHECIDO.getId(), SistemaEnum.DESCONHECIDO.getNomSistema());
	}
	
	private void salvarSistemaProcessoExterno(Sistema sistemaProcesso) throws Exception {
		Sistema sistema = this.sistemaService.buscarSistemaByNom(sistemaProcesso.getNomSistema());
		if (Util.isNullOuVazio(sistema)) { 
			this.sistemaService.adicionarSistema(sistemaProcesso);
		}
	}
	
	public ProcessoTramite getProcessoTramite() {
		return processoTramite;
	}

	public void setProcessoTramite(ProcessoTramite processoTramite) {
		this.processoTramite = processoTramite;
	}

}
