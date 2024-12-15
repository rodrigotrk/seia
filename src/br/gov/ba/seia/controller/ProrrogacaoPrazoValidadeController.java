package br.gov.ba.seia.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK;
import br.gov.ba.seia.entity.TipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("prorrogacaoPrazoValidadeController")
@ViewScoped
public class ProrrogacaoPrazoValidadeController extends BaseDialogController {

	private SolicitacaoAdministrativo prorrogacaoPrazoValidade;

	private Collection<TipoProrrogacaoPrazoValidade> tiposProrrogacaoValidade;
	
	private Collection<TipoProrrogacaoPrazoValidade> tiposProrrogacaoValidadeLocalizacao;

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
			this.prorrogacaoPrazoValidade = (SolicitacaoAdministrativo) objeto;
			this.carregarListas();
			this.editMode = true;
			
			this.carregarTiposProrrogacaoSelecionados();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarTiposProrrogacaoSelecionados() throws Exception {
		List<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade> tiposProrrogacaoLocalizacaoSelecionados = 
				solicitacaoAdministrativoService.buscarSolicitacaoAdmTipoValByIdSolicitacao(this.prorrogacaoPrazoValidade);
		
		for (TipoProrrogacaoPrazoValidade tipoProrrogacao : this.tiposProrrogacaoValidadeLocalizacao) {
			for (SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade tipoSelecionado : tiposProrrogacaoLocalizacaoSelecionados) {
				if(tipoSelecionado.getIdeTipoProrrogacaoPrazoValidade().equals(tipoProrrogacao)){
					tipoProrrogacao.setLocalizacaoGeograficaTransient(tipoSelecionado.getIdeLocalizacaoGeografica());
					tipoProrrogacao.setChecked(true);
				}
			}
		}
	}

	private void carregarListas() throws Exception {
		this.tiposProrrogacaoValidade = this.solicitacaoAdministrativoService.listarTipoProrrogacaoValidade();
		this.tiposProrrogacaoValidadeLocalizacao = this.solicitacaoAdministrativoService.listaTipoProrrogacaoValidadeFilho();
	}
	
	@Override
	public void limpar() {
		this.prorrogacaoPrazoValidade = new SolicitacaoAdministrativo(super.getRequerimento());
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){
				this.prorrogacaoPrazoValidade.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.PRORROGACAO_PRAZO_VALIDADE.getId()));
				this.solicitacaoAdministrativoService.salvarOuAtualizarSolicitacaoAdministrativa(this.prorrogacaoPrazoValidade);
				this.solicitacaoAdministrativoService.excluirLocalizacaoDadoGeograficoByIdSolicitacao(this.prorrogacaoPrazoValidade);
				this.salvarSolicitacaoTipoProrrogacaoValidade();
				
				if(editMode){
					JsfUtil.addSuccessMessage("Prorrogação de Prazo de Validade atualizada com sucesso!");
				}else{
					JsfUtil.addSuccessMessage("Prorrogação de Prazo de Validade salva com sucesso!");
				}
				
				RequestContext.getCurrentInstance().execute("prorrogPrazoValidade.hide()");
				this.abaRenovacaoAlteracaoProrrogacaoController.adicionarOuAtualizarProrrogacaoDeValidade(this.prorrogacaoPrazoValidade);
			
			}	
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarSolicitacaoTipoProrrogacaoValidade() throws Exception {
		for (TipoProrrogacaoPrazoValidade tipoProrrogacaoPrazoValidade : this.tiposProrrogacaoValidadeLocalizacao) {
			if (!tipoProrrogacaoPrazoValidade.isChecked()) {
				continue;
			}
			tipoProrrogacaoPrazoValidade.getLocalizacaoGeograficaTransient().setDtcCriacao(new Date());
			tipoProrrogacaoPrazoValidade.getLocalizacaoGeograficaTransient().setIndExcluido(false);
			SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade solicitacaoAdministrativoTipoProrrogacaoPrazoValidade = new SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade();
			SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK = 
					new SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK(this.prorrogacaoPrazoValidade.getIdeSolicitacaoAdministrativo(), tipoProrrogacaoPrazoValidade.getIdeTipoProrrogacaoPrazoValidade(), tipoProrrogacaoPrazoValidade
									.getLocalizacaoGeograficaTransient().getIdeLocalizacaoGeografica());
			solicitacaoAdministrativoTipoProrrogacaoPrazoValidade.setSolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK(solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK);
			solicitacaoAdministrativoTipoProrrogacaoPrazoValidade.setIdeLocalizacaoGeografica(tipoProrrogacaoPrazoValidade.getLocalizacaoGeograficaTransient());
			solicitacaoAdministrativoTipoProrrogacaoPrazoValidade.setIdeSolicitacaoAdministrativo(this.prorrogacaoPrazoValidade);
			solicitacaoAdministrativoTipoProrrogacaoPrazoValidade.setIdeTipoProrrogacaoPrazoValidade(tipoProrrogacaoPrazoValidade);
			solicitacaoAdministrativoService.salvarOuAtualizarSolicitacaoAdmTipoValidadeLocalizacaoGeo(solicitacaoAdministrativoTipoProrrogacaoPrazoValidade);
		}
	}

	@Override
	public boolean validar() {
		boolean valido = true;
		if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getIdeTipoProrrogacaoPrazoValidade())) {
			JsfUtil.addWarnMessage("Por favor, selecione uma opção no campo 1.");
			valido = false;
		} else if (this.prorrogacaoPrazoValidade.getIdeTipoProrrogacaoPrazoValidade().getIdeTipoProrrogacaoPrazoValidade().equals(6)) {
			if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getNumCertificado())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
				valido = false;
			} else {
				if (!Util.validaTamanhoString(this.prorrogacaoPrazoValidade.getNumCertificado(), 50)) {
					JsfUtil.addWarnMessage("O campo 3. só aceita 50 caracteres");
					valido = false;
				}
			}
		} else if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getNumPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
			valido = false;
		} else {
			if (!Util.validaTamanhoString(this.prorrogacaoPrazoValidade.getNumPortaria(), 50)) {
				JsfUtil.addWarnMessage("O campo 3. só aceita 50 caracteres");
				valido = false;
			}
		}

		if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getNumProcesso())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
			valido = false;
		} else {
			
		}
		if (!Util.validaTamanhoString(this.prorrogacaoPrazoValidade.getNumProcesso(), 50)) {
			JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
			valido = false;
		}
		if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getDtcPublicacaoPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 4.");
			valido = false;
		} else if (this.prorrogacaoPrazoValidade.getDtcPublicacaoPortaria().after(new Date())) {
			JsfUtil.addWarnMessage("A data de publicação na portaria não pode ser superior a data de hoje.");
			valido = false;
		}
		if (Util.isNullOuVazio(this.prorrogacaoPrazoValidade.getDtcValidade())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
			valido = false;
		} else {
			if (this.prorrogacaoPrazoValidade.getDtcValidade().before(this.prorrogacaoPrazoValidade.getDtcPublicacaoPortaria())) {
				JsfUtil.addWarnMessage("A data do prazo de validade não pode ser inferior a data de publicação.");
				valido = false;
			}
		}

		if (this.prorrogacaoPrazoValidade.getIdeTipoProrrogacaoPrazoValidade().getIdeTipoProrrogacaoPrazoValidade().equals(1)) {
			
			boolean existeTipoProrrogacaoLocalizacao = false;
			
			for (TipoProrrogacaoPrazoValidade prazoValidade : tiposProrrogacaoValidadeLocalizacao) {
				
					if(prazoValidade.isChecked()){
						existeTipoProrrogacaoLocalizacao = true;
				
						if (Util.isNullOuVazio(prazoValidade.getLocalizacaoGeograficaTransient()) || Util.isNull(prazoValidade.getLocalizacaoGeograficaTransient().getIdeLocalizacaoGeografica())){
							JsfUtil.addWarnMessage("Favor incluir a localização dos tipos selecionados");
							valido = false;
							break;
						}
					}
			}
			
			if(!existeTipoProrrogacaoLocalizacao){
				JsfUtil.addWarnMessage("Por favor, selecione pelo menos um item do campo 1.1.");
				valido = false;
				
			}
			
		}
		return valido;
	}

	public SolicitacaoAdministrativo getProrrogacaoPrazoValidade() {
		return prorrogacaoPrazoValidade;
	}

	public void setProrrogacaoPrazoValidade(SolicitacaoAdministrativo prorrogacaoPrazoValidade) {
		this.prorrogacaoPrazoValidade = prorrogacaoPrazoValidade;
	}

	public Collection<TipoProrrogacaoPrazoValidade> getTiposProrrogacaoValidade() {
		return tiposProrrogacaoValidade;
	}

	public void setTiposProrrogacaoValidade(Collection<TipoProrrogacaoPrazoValidade> tiposProrrogacaoValidade) {
		this.tiposProrrogacaoValidade = tiposProrrogacaoValidade;
	}

	public Collection<TipoProrrogacaoPrazoValidade> getTiposProrrogacaoValidadeLocalizacao() {
		return tiposProrrogacaoValidadeLocalizacao;
	}

	public void setTiposProrrogacaoValidadeLocalizacao(Collection<TipoProrrogacaoPrazoValidade> tiposProrrogacaoValidadeLocalizacao) {
		this.tiposProrrogacaoValidadeLocalizacao = tiposProrrogacaoValidadeLocalizacao;
	}
	
	public Date getDataAtual() {
		return new Date();
	}
	
	public Date getDataMinima() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +60);
		Date date = calendar.getTime();
		return date;
	}
}
