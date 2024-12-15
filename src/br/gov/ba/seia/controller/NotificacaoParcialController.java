package br.gov.ba.seia.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dao.NotificacaoParcialDAOImpl;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.MotivoNotificacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("notificacaoParcialController")
@ViewScoped
public class NotificacaoParcialController {
	
	@EJB
	private ProcessoService processoService;
	@EJB
	private NotificacaoServiceFacade notificacaoServiceFacade;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	@EJB
	private NotificacaoParcialDAOImpl notificacaoParcialDAOImpl;
	@EJB
	private PautaService pautaService;

	private Processo processo;
	private VwConsultaProcesso vwProcesso;
	private Pauta pauta;
	private Boolean existeNotificacao;
	private Boolean isTelaProcesso;
	private PessoaFisica pessoaFisica;
	private MotivoNotificacao motivoNotificacao;
	private NotificacaoParcial notificacaoParcial;
	private TipoNotificacaoEnum tipoNotificacaoEnum;
	private Notificacao notificacao;

	@PostConstruct
	public void init() {
		limpaTudo();		
	}
	
	public void limpaTudo() {
		processo = null;
		vwProcesso = null;
		pauta = null;
		existeNotificacao = null;
		isTelaProcesso = null;
		pessoaFisica = null;
		motivoNotificacao = null;
		notificacaoParcial = null;
		tipoNotificacaoEnum = null;
		notificacao  = null;
	}
	
	public void load(VwConsultaProcesso vwConsultaProcesso, TipoNotificacaoEnum tipoNotificacaoEnum) throws Exception {
		vwProcesso = vwConsultaProcesso;
		this.tipoNotificacaoEnum=tipoNotificacaoEnum;
		if(vwProcesso != null){
			obterPautaDoFuncionario();
			carregarProcesso();
		}
		isTelaProcesso = Boolean.FALSE;
	}

	public void carregarProcesso() {
		pessoaFisica = new PessoaFisica();
		try {
			pessoaFisica = pessoaFisicaService.carregarPessoaFisicaGet(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());// Pessoa Logada. NULO no contexto.
			processo = processoService.carregarProcesso(vwProcesso.getIdeProcesso());
			obterNotificacaoParcial();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void obterNotificacaoParcial(){
		try {
			notificacaoParcial = notificacaoParcialDAOImpl.obterNotificacaoParcialByTipo(ContextoUtil.getContexto().getUsuarioLogado(),this.processo, tipoNotificacaoEnum);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	public void verificarNotificaoAberto() throws Exception{
		List<NotificacaoParcial> listaNotificacaoParcial = notificacaoParcialDAOImpl.listarNotificacaoParcialPorPessoaFisica(pauta, processo);
		if (listaNotificacaoParcial.isEmpty()) {
			existeNotificacao = Boolean.TRUE;
		} else {
			notificacaoParcial = listaNotificacaoParcial.get(0);
			for (NotificacaoParcial notificacaoParcial : listaNotificacaoParcial) {
				existeNotificacao = notificacaoParcial.getIndEmissao();
			}
		}
	} 

	public void salvarNotificacao() throws Exception {
		 notificacaoParcial.setIdePauta(pauta);
		 notificacaoParcial.setIdeProcesso(processo);
		 notificacaoParcial.setDtcCriacao(new Date());
		 notificacaoParcial.setTipo(tipoNotificacaoEnum.getTipo());
		 notificacaoServiceFacade.salvarNotificacaoParcial(notificacaoParcial);
		 JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		 fecharDialog();
	}
	
	private void fecharDialog(){
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("flagValidate", Boolean.TRUE);
	}
	
	private void obterPautaDoFuncionario() throws Exception {
		pauta = pautaService.obtemPautaPorIdeFuncionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}
	
	public boolean isNotificacaoPrazo() {
		return TipoNotificacaoEnum.NOTIFICACAO_PRAZO.equals(tipoNotificacaoEnum);
	}
	
	public boolean isNotificacaoComunicacao() {
		return TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.equals(tipoNotificacaoEnum);
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public MotivoNotificacao getMotivoNotificacao() {
		return motivoNotificacao;
	}

	public void setMotivoNotificacao(MotivoNotificacao motivoNotificacao) {
		this.motivoNotificacao = motivoNotificacao;
	}

	public NotificacaoParcial getNotificacaoParcial() {
		return notificacaoParcial;
	}

	public void setNotificacaoParcial(
			NotificacaoParcial notificacaoParcial) {
		this.notificacaoParcial = notificacaoParcial;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Boolean getExisteNotificacao() {
		return existeNotificacao;
	}

	public void setExisteNotificacao(Boolean existeNotificacao) {
		this.existeNotificacao = existeNotificacao;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Boolean getIsTelaProcesso() {
		return isTelaProcesso;
	}

	public void setIsTelaProcesso(Boolean isTelaProcesso) {
		this.isTelaProcesso = isTelaProcesso;
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}
}