package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AcaoControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.StatusFluxoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PerfilUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("pautaGestorController")
@ViewScoped
public class PautaGestorController extends PautaCompartilhadaController {

	@EJB
	private StatusFluxoService statusFluxoService;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;	
	@EJB
	private PautaService pautaService;	

	private Boolean usuarioDiretor;
	private VwConsultaProcesso processoSelecionado;	
	private Collection<StatusFluxo> listaStatusFluxo;
	private StatusFluxo statusFluxo;
	
	@PostConstruct
	public void init() {
		super.init();
		try{
			super.listarCategoria();
			operacaoProcessoEnum=OperacaoProcessoEnum.PAUTA_GESTOR;
			consultarProcesso();
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void carregarNotificacaoPrazo(VwConsultaProcesso vwp) throws Exception {
		carregarNotificacao(vwp, TipoNotificacaoEnum.NOTIFICACAO_PRAZO);
	}
	
	public void carregarNotificacaoComunicacao(VwConsultaProcesso vwp) throws Exception {
		carregarNotificacao(vwp, TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO);
	}
	
	private void carregarNotificacao(VwConsultaProcesso vwp, TipoNotificacaoEnum tipoNotificacaoEnum) throws Exception {
		NotificacaoFinalController notificacaoFinalController = null;
		boolean acessoFeitoPorPermissaoConcedida = !funcionarioResponsavel;
		notificacaoFinalController = (NotificacaoFinalController) SessaoUtil.recuperarManagedBean("#{notificacaoFinalController}", NotificacaoFinalController.class);
		notificacaoFinalController.limparTudo();
		notificacaoFinalController.load(vwp,tipoNotificacaoEnum, acessoFeitoPorPermissaoConcedida);
	}
	
	
	public void limparTela(){
		super.limpar();
		statusFluxo = null;
	}
	
	public Collection<StatusFluxo> getStatus() {
		if (listaStatusFluxo == null) {
			try {
				listaStatusFluxo = statusFluxoService.listarStatusProcessoPorIde(retornarStatusValidos());
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		return listaStatusFluxo;
	}

	/**
	 * Metodo inicial da busca paginada.
	 */
	@Override
	public void consultarProcesso() {
		try {
			if (validate()) {
				consultar();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	private List<Integer> retornarStatusValidos() {
		
		Integer[] statusValidos = new Integer[] {
				StatusFluxoEnum.NOTIFICACAO_ALTERADA.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_CANCELADA.getStatus(),
				StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus(),
				StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus(),
				StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus(),
				StatusFluxoEnum.ANALISE_TECNICA_APROVADA.getStatus(),
				StatusFluxoEnum.NOTIFICACAO_ALTERADA.getStatus()
		};
		
		return Arrays.asList(statusValidos);
	}
	
	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> params = super.getParametros();
		
		List<Integer> statusValidos = retornarStatusValidos();
		
		if(!Util.isNullOuVazio(statusFluxo)){
			statusValidos = new ArrayList<Integer>();
			statusValidos.add(statusFluxo.getIdeStatusFluxo());
			params.put("statusFluxo", statusValidos);
		}
		else{
			statusValidos = retornarStatusValidos();
			params.put("statusFluxo", statusValidos);
		}
		return params;
	}

	public boolean existeNotificacao(Integer ideProcesso){
		 try{
			 Integer ideFuncionarioLogado;
			 Pauta pautaFuncionarioLogado;
			 Pauta pautaGestor;
			 if(funcionarioResponsavel!=null && funcionarioResponsavel){
				ideFuncionarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
				pautaFuncionarioLogado = pautaService.obtemPautaPorIdeFuncionario(ideFuncionarioLogado);
				return notificacaoDAOImpl.verificarExistenciaNotificacao(ideProcesso,pautaFuncionarioLogado);
			 }
			 else{
				pautaGestor = pautaService.obtemPautaCoordenadorArea(area.getIdeArea(), TipoPautaEnum.PAUTA_COORDENADOR_AREA);
				return notificacaoDAOImpl.verificarExistenciaNotificacao(ideProcesso,pautaGestor);				 
			 }
			
		}
		catch(Exception e){
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	 
	public boolean isDisabledCronograma() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId()); 
	}
	
	public boolean isDisabledVisualizarEquipe() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.VISUALIZAR_EQUIPE.getId()); 
	}
	
	public boolean isDisabledFormarEquipe() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId()); 
	}
	
	public boolean isDisabledNotificacao() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.NOTIFICAR.getId()); 
	}
	
	public boolean isDisabledApensarDocumento() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.APENSAR_DOCUMENTO.getId()); 
	}
	
	public boolean isDisabledAcoesNotificacao() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.ACOES_DA_NOTIFICACAO.getId()); 
	}
	
	public boolean isDisabledEncaminhar() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId()); 
	}
	
	public boolean isRenderedNotificacaoPrazo(VwConsultaProcesso vwConsultaProcesso) {
		return vwConsultaProcesso.isRenderedNotificacaoPrazo() && !isDiretor();
	}
	
	public boolean isRenderedNotificacaoComunicacao(VwConsultaProcesso vwConsultaProcesso) {
		return vwConsultaProcesso.isRenderedNotificacaoComunicacao() && !isDiretor();
	}
	
	public boolean isRenderedReservaAgua(VwConsultaProcesso vwConsultaProcesso) {
		return vwConsultaProcesso.isRenderedReservaAgua();
	}
	
	private boolean isDiretor() {
		
		boolean perfilDiretor = ContextoUtil.getContexto().getUsuarioLogado().isPerfilDiretor();
		
		if(perfilDiretor) {
			return true;
		}
		
		if(temAcessoConcedidoPeloGestor()) {
			return new TipoPauta(TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo()).equals(pauta.getIdeTipoPauta());
		}
		
		return false;
	}

	public Collection<StatusFluxo> getListaStatusFluxo() {
		return listaStatusFluxo;
	}

	public void setListaStatusFluxo(Collection<StatusFluxo> listaStatusFluxo) {
		this.listaStatusFluxo = listaStatusFluxo;
	}

	public StatusFluxo getStatusFluxo() {
		return statusFluxo;
	}

	public void setStatusFluxo(StatusFluxo statusFluxo) {
		this.statusFluxo = statusFluxo;
	}

	public VwConsultaProcesso getProcessoSelecionado() {
		return processoSelecionado;
	}

	public void setProcessoSelecionado(VwConsultaProcesso processoSelecionado) {
		this.processoSelecionado = processoSelecionado;
	}

	public Boolean getUsuarioDiretor() {
		usuarioDiretor = PerfilUtil.isDiretor();
		return usuarioDiretor;
	}

	public void setUsuarioDiretor(Boolean usuarioDiretor) {
		this.usuarioDiretor = usuarioDiretor;
	}
}