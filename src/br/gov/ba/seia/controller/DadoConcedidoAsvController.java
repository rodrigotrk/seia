package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.Html.atualizar;
import static br.gov.ba.seia.util.Html.esconder;
import static br.gov.ba.seia.util.Html.exibir;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.DadoConcedidoPoligonalDTO;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.facade.DadoConcedidoAsvServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.facade.AutorizacaoManejoCabrucaServiceFacade;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * 
 * @author eduardo.fernandes 
 * @since 08/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Named("dadoConcedidoAsvController")
@ViewScoped
public class DadoConcedidoAsvController extends EspecieSupressaoController {
	
	@EJB
	private DadoConcedidoAsvServiceFacade facade;
	
	@EJB
	private AsvSupressaoService asvSupressaoService;
	
	@EJB
	private AutorizacaoManejoCabrucaServiceFacade autorizacaomanejoCabrucaServiceFacade;
	
	private DadoConcedidoImpl dadoConcedido;
	private List<DadoConcedidoDTO> listaDTO;
	private boolean somenteVisualizacao;
	
	private DadoConcedidoDTO autorizacaoManejoCabrucaDTOSelecionado;
	private MetodoUtil metodoRetornoEspecie;
	
	private Integer activeTabIndex = 0;
	
	@PostConstruct
	public void init() {
		metodoRetornoEspecie = new MetodoUtil(this, "retornoEspecie");

	}
	
	public void load(DadoConcedidoImpl dadoConcedido, boolean somenteVisualizacao) {
		try{
			init();
			this.somenteVisualizacao = somenteVisualizacao;
			this.dadoConcedido = dadoConcedido;
			
			String erro = montarDto();
			if(erro.isEmpty()){
				verificarPoligonalConcedida();
				if(!Util.isNullOuVazio(listaDTO)){
					for(DadoConcedidoDTO dto: listaDTO){
						if(Util.isNullOuVazio(dto.getLatitudeInicial())){
							verificarPoligonalConcedida();
							break;
						}
					}
				}
				asvSupressaoService.removerListaEspecieSupressaoEdicao(getListaEspecieSupressaoAutorizacao(), getListaEspecieSupressao(), getListaEspecieSupressaoAll());		
				atualizar("pnlDadoConcedido");
				exibir("dlgDadoConcedido");
			
			}else {
				JsfUtil.addWarnMessage((Util.getString(erro)));
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void retornoEspecie()  {
		if (!Util.isNullOuVazio(getEspecieSupressaoSelecionada())){
					
			boolean verificarSeEspecieJaAdd = asvSupressaoService.verificarSeEspecieJaAdd(getListaEspecieSupressaoAutorizacao(), getEspecieSupressaoSelecionada(), getNomePopularEspecieSelecionada());
			if (!verificarSeEspecieJaAdd){
				if (Util.isNullOuVazio(autorizacaoManejoCabrucaDTOSelecionado.getListaEspecieSupressaoAutorizacao())) {
					autorizacaoManejoCabrucaDTOSelecionado.setListaEspecieSupressaoAutorizacao(new ArrayList<EspecieSupressaoAutorizacao>());
				}
				
				EspecieSupressaoAutorizacao especieSupressaoAutorizacao = new EspecieSupressaoAutorizacao();
				
				if (!Util.isNullOuVazio(getNomePopularEspecieSelecionada().getIdeNomePopularEspecie())){
					especieSupressaoAutorizacao.setIdeNomePopularEspecie(getNomePopularEspecieSelecionada());
				}
				
				especieSupressaoAutorizacao.setIdeEspecieSupressao(getEspecieSupressaoSelecionada());
				
				asvSupressaoService.removerListaEspecieSupressaoAll(getListaEspecieSupressao(), getEspecieSupressaoSelecionada(), getNomePopularEspecieSelecionada());
				asvSupressaoService.removerListaEspecieSupressaoAll(getListaEspecieSupressaoAll(), getEspecieSupressaoSelecionada(), getNomePopularEspecieSelecionada());
				
				autorizacaoManejoCabrucaDTOSelecionado.getListaEspecieSupressaoAutorizacao().add(especieSupressaoAutorizacao);
				setListaEspecieSupressaoAutorizacao(autorizacaoManejoCabrucaDTOSelecionado.getListaEspecieSupressaoAutorizacao());
			}
		}
	}
	
	public void onChange(TabChangeEvent e){
		try {
			DadoConcedidoDTO dadoConcedidoDTO = (DadoConcedidoDTO)e.getData();
			carregarEspecie();
			asvSupressaoService.removerListaEspecieSupressaoEdicao(dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao(), getListaEspecieSupressao(), getListaEspecieSupressaoAll());
			
			String id = "accDadoConcedidoImovel:"
					.concat(obterPosicaoNaLista(listaDTO, dadoConcedidoDTO).toString())
					.concat(":frmDadoConcedidoPoligonal");
			
			Html.atualizar(id);
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
	}
	
	/**
	 * 
	 * 
	 * @author eduardo.fernandes 
	 * @throws Exception 
	 * @since 10/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 */
	private String montarDto() throws Exception {
		return facade.montarDto(this, PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_15, MotivoNotificacaoEnum.SHAPE_ASV);	
	}
	
	/**
	 * 
	 * 
	 * @author eduardo.fernandes 
	 * @throws Exception 
	 * @since 13/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 */
	private void verificarPoligonalConcedida() throws Exception {
		facade.verificarDadoConcedido(this);
	}

	public boolean isDisabled(DadoConcedidoDTO dto, DadoConcedidoPoligonalDTO poligonalDTO) {
		if(somenteVisualizacao) {
			return true;
		}
		else if(Util.isNull(dto.getPoligonalConcedida())) {
			return false;
		}
		return !dto.getPoligonalConcedida().getLocalizacaoGeografica().equals(poligonalDTO.getLocalizacaoGeografica());
	}
	
	public void add(DadoConcedidoDTO dto, DadoConcedidoPoligonalDTO poligonalDTO) {
		if(poligonalDTO.isConcedido()) {
			dto.setPoligonalConcedida(poligonalDTO);
		}
		else {
			dto.setPoligonalConcedida(null);
		}
	}
	
	public void salvar() {
		
		AsvSupressaoController asvSupressaoController = (AsvSupressaoController) SessaoUtil.recuperarManagedBean("#{asvSupressaoController}", AsvSupressaoController.class);
		
		AsvDadosGeraisController asvDadosGeraisController = (AsvDadosGeraisController) SessaoUtil.recuperarManagedBean("#{asvDadosGeraisController}", AsvDadosGeraisController.class);

		if(validarPreenchimento() && asvSupressaoController.validarAbaSupressao()){
			try {
				facade.salvarASVeFceASV(this,asvDadosGeraisController);
			} catch (Exception e) {

				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	
	public void salvarASVeFceEASV(){
		
		try {	
				salvarEspecieAutorizacao();
				facade.salvarProcessoAtoConcedido(listaDTO, dadoConcedido);
				
				atualizar("pnlAnaliseTecnica");
				JsfUtil.addSuccessMessage(Util.getString("MSG-010"));
				esconder("dlgDadoConcedido");

		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("relocacao_reverva_legal_msg_erro"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarEspecieAutorizacao() throws Exception{
		

		AsvSupressaoController asvSupressaoController = (AsvSupressaoController) SessaoUtil.recuperarManagedBean("#{asvSupressaoController}", AsvSupressaoController.class);
		
		List<EspecieSupressaoAutorizacao> especies = asvSupressaoController.getListaEspecieSupressaoAutorizacao();
		
		if(!Util.isNullOuVazio(especies)){
			
			for(EspecieSupressaoAutorizacao eespecieAutrotiracao :especies){
				eespecieAutrotiracao.setIdeProcessoAto(dadoConcedido.getProcessoAto());
				facade.salvarEspecieSupressaoAutorizacaoProcessoAto(eespecieAutrotiracao);
			}
		}
	}
	
	//Verificar se ao menos um item (requerimento ou notificação) foi marcado
	private Boolean validarPreenchimento() {
		Boolean retornoRequerimento = Boolean.FALSE;
		Boolean retornoNotificacao = Boolean.FALSE;

		
		for (DadoConcedidoDTO dadoConcedidoDTO : listaDTO) {
			//quando recomeçar o for as flags são reinicializadas
			retornoRequerimento = Boolean.FALSE;
			retornoNotificacao = Boolean.FALSE;

			
			if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalDTO())){
				for(DadoConcedidoPoligonalDTO requerimento : dadoConcedidoDTO.getListaPoligonalDTO()){
					if(!requerimento.isConcedido()){
						retornoRequerimento = Boolean.FALSE;
						if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalNotificacaoDTO())){
							
							for(DadoConcedidoPoligonalDTO notificacao : dadoConcedidoDTO.getListaPoligonalNotificacaoDTO()){
								if(!notificacao.isConcedido()){
									if(dadoConcedidoDTO.getPoligonalConcedida().getNumeroNotificacao().equals(notificacao.getNumeroNotificacao())) {
										JsfUtil.addErrorMessage(Util.getString("MSG-erro-selecionar-areas"));
										return Boolean.FALSE;
									}
								}else{
									retornoNotificacao = Boolean.TRUE;
								}
							}
						} else {
							JsfUtil.addErrorMessage(Util.getString("MSG-erro-selecionar-areas"));
							return Boolean.FALSE;
						}
					}else{
						retornoRequerimento = Boolean.TRUE;
					}
				}
			}
			
			
		}
		
		return (retornoRequerimento || retornoNotificacao);
	}
	
	public boolean isRenderedPoligonalRequerimento(DadoConcedidoDTO dto) {
		return !Util.isNullOuVazio(dto.getListaPoligonalDTO());
	}
	
	public boolean isRenderedPoligonalNotificacao(DadoConcedidoDTO dto) {
		return !Util.isNullOuVazio(dto.getListaPoligonalNotificacaoDTO());
	}
	
	public List<DadoConcedidoDTO> getListaDTO() {
		return listaDTO;
	}

	public void setListaDTO(List<DadoConcedidoDTO> listaDTO) {
		this.listaDTO = listaDTO;
	}

	public DadoConcedidoImpl getDadoConcedido() {
		return dadoConcedido;
	}

	public boolean isSomenteVisualizacao() {
		return somenteVisualizacao;
	}

	public DadoConcedidoDTO getAutorizacaoManejoCabrucaDTOSelecionado() {
		return autorizacaoManejoCabrucaDTOSelecionado;
	}

	public void setAutorizacaoManejoCabrucaDTOSelecionado(
			DadoConcedidoDTO autorizacaoManejoCabrucaDTOSelecionado) {
		this.autorizacaoManejoCabrucaDTOSelecionado = autorizacaoManejoCabrucaDTOSelecionado;
	}

	public MetodoUtil getMetodoRetornoEspecie() {
		return metodoRetornoEspecie;
	}

	public void setMetodoRetornoEspecie(MetodoUtil metodoRetornoEspecie) {
		this.metodoRetornoEspecie = metodoRetornoEspecie;
	}

	public Integer getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(Integer activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}
}
