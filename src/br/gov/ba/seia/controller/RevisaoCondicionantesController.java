package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TipoRevisaoCondicionante;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("revisaoCondicionantesController")
@ViewScoped
public class RevisaoCondicionantesController extends BaseDialogController {

	private SolicitacaoAdministrativo revisaoCondicionante;

	private Collection<TipoRevisaoCondicionante> tiposRevisaoCondicionante;
	@Inject
	private AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoAlteracaoProrrogacaoController;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;

	@Override
	public void load() {
		try {
			this.limpar();
			this.carregarListas();
			this.editMode = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			this.revisaoCondicionante = (SolicitacaoAdministrativo) objeto;
			this.carregarListas();
			this.editMode = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void limpar() {
		this.revisaoCondicionante = new SolicitacaoAdministrativo(super.getRequerimento());
	}

	private void carregarListas() throws Exception {
		this.tiposRevisaoCondicionante = this.solicitacaoAdministrativoService.obterListaTipoRevisaoCondicionante();
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){
				this.revisaoCondicionante.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.REVISAO_CONDICIONANTE.getId()));
				this.solicitacaoAdministrativoService.salvarOuAtualizarSolicitacaoAdministrativa(this.revisaoCondicionante);
				
				if(this.editMode)
					JsfUtil.addSuccessMessage("Condicionante Atualizada com sucesso");
				else
					JsfUtil.addSuccessMessage("Condicionante Salva com sucesso");
				
				this.abaRenovacaoAlteracaoProrrogacaoController.adicionarOuAtualizarRevisaoDeCondicionante(this.revisaoCondicionante);
				
				RequestContext.getCurrentInstance().execute("dialogRevisaoCondicionantes.hide()");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public boolean validar() {
		boolean valido = true;
		if (Util.isNullOuVazio(this.revisaoCondicionante.getIdeTipoRevisaoCondicionante())) {
			JsfUtil.addWarnMessage("Por favor, selecione uma opção no campo 1.");
			valido = false;
		}
	
		if (Util.isNullOuVazio(this.revisaoCondicionante.getNumProcesso())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
			valido = false;
	
		} else {
			
			if (valido) {
				if (!Util.validaTamanhoString(this.revisaoCondicionante.getNumProcesso(), 50)) {
					JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
					valido = false;
				}
			}
		}
	
		if (Util.isNullOuVazio(this.revisaoCondicionante.getNumPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
			valido = false;
		} else {
			if (!Util.validaTamanhoString(this.revisaoCondicionante.getNumPortaria(), 50)) {
				JsfUtil.addWarnMessage("O campo 3. só aceita 50 caracteres");
				valido = false;
			}
		}
	
		if (Util.isNullOuVazio(this.revisaoCondicionante.getDtcPublicacaoPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 4.");
			valido = false;
		}
	
		if (Util.isNullOuVazio(this.revisaoCondicionante.getDtcValidade())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
			valido = false;
		}
	
		return valido;
	}

	public SolicitacaoAdministrativo getRevisaoCondicionante() {
		return revisaoCondicionante;
	}

	public void setRevisaoCondicionante(SolicitacaoAdministrativo revisaoCondicionante) {
		this.revisaoCondicionante = revisaoCondicionante;
	}

	public Collection<TipoRevisaoCondicionante> getTiposRevisaoCondicionante() {
		return tiposRevisaoCondicionante;
	}

	public void setTiposRevisaoCondicionante(Collection<TipoRevisaoCondicionante> tiposRevisaoCondicionante) {
		this.tiposRevisaoCondicionante = tiposRevisaoCondicionante;
	}
	
	public Date getDataAtual() {
		return new Date();
	}
}
