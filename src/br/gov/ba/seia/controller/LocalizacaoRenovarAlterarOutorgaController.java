package br.gov.ba.seia.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Named("localizacaoOutorgaController")
@ViewScoped
public class LocalizacaoRenovarAlterarOutorgaController extends BaseDialogController {

	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;

	@Inject
	private RenovarAlterarOutorgaController renovarAlterarOutorgaController;
	
	
	@Override
	public void load() {
		this.limpar();
		this.editMode = false;
		this.novoRequerimentoController.listarImovel();
		if(!testaTipoImovel()){
			outorgaLocalizacaoGeografica.setListaImovel(this.novoRequerimentoController.getListaImovel());
		}
	}

	private boolean testaTipoImovel() {
		return !novoRequerimentoController.isImovelUrbano() && !novoRequerimentoController.isCessionario() && !novoRequerimentoController.isConversaoTcraLac();
	}

	@Override
	public <T> void editar(T objeto) {
		this.outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objeto;
		this.editMode = true;
	}

	@Override
	public void limpar() {
		this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica();
	}

	@Override
	public void salvar() {
		if (validar()) {
			if (!this.editMode) {
				JsfUtil.addSuccessMessage("Localização geográfica inserida com sucesso.");
			} else {
				JsfUtil.addSuccessMessage("Localização geográfica atualizada com sucesso.");
			}
			this.renovarAlterarOutorgaController.adicionarOutorgaLocalizacaoGeografica(this.outorgaLocalizacaoGeografica);
			RequestContext.getCurrentInstance().execute("localizacaoAlteracaoOutorga.hide()");
		}
	}

	@Override
	public boolean validar() {
		boolean valido = true;

		if (novoRequerimentoController.isImovelRural() && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())) {
			JsfUtil.addWarnMessage("Favor selecionar o(s) imovel(is).");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
			JsfUtil.addWarnMessage("Favor incluir os pontos da localização geografica.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVazao()) && (renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(1)
				|| renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(2))
				&& renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() != 6
				&& renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() != 7
				&& renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() != 8) {
			
			JsfUtil.addWarnMessage("Favor incluir a vazão.");
			valido = false;
		}
		
		if ( (renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(1) 
				|| renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(2))
			 && (renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() == 1 
				|| renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() == 2) 
			 && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVazaoFinal())) {
			
			JsfUtil.addWarnMessage("Favor incluir o valor total.");
			valido = false;
		}

		if (((renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga() != 4) 
				&& (renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() == 1
				|| renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() == 2
				|| renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() == 5))
				&& Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIndFinalidade())) {
			JsfUtil.addWarnMessage("Favor informar o objetivo da finalidade.");
			valido = false;
		}

		if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua()) 
				&& renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(1)
				&& renovarAlterarOutorgaController.getOutorga().getIdeTipoAlteracao().getIdeTipoAlteracao() != 8) {
			
			JsfUtil.addWarnMessage("Favor incluir a(s) finalidade(s).");
			valido = false;
		} else if (renovarAlterarOutorgaController.getOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(1)){
			if (this.outorgaLocalizacaoGeografica.isIrrigacao() && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao())) {
				JsfUtil.addWarnMessage("Favor informar a área irrigada.");
				valido = false;
			}
			
			if (this.outorgaLocalizacaoGeografica.isPulverizacao() && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaPulverizada())) {
				JsfUtil.addWarnMessage("Favor informar a área pulverizada.");
				valido = false;
			}
		}
		
		return valido;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeografica() {
		return outorgaLocalizacaoGeografica;
	}

	public void setOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
	}

	public Outorga getOutorga(){
		return this.renovarAlterarOutorgaController.getOutorga();
	}
	
}
