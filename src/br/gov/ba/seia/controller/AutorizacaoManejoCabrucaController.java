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
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.facade.AutorizacaoManejoCabrucaServiceFacade;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("autorizacaoManejoCabrucaController")
@ViewScoped
public class AutorizacaoManejoCabrucaController extends EspecieSupressaoController {
	
	@EJB
	private AutorizacaoManejoCabrucaServiceFacade autorizacaomanejoCabrucaServiceFacade;
	
	@EJB
	private AsvSupressaoService asvSupressaoService;
	
	private List<DadoConcedidoDTO> listaAutorizacaoManejoCabrucaDTO;
	private boolean somenteVisualizacao;
	private DadoConcedidoImpl dadoConcedido;
	
	private MetodoUtil metodoRetornoEspecie;
	private DadoConcedidoDTO autorizacaoManejoCabrucaDTOSelecionado;
	
	@PostConstruct
	public void init() {
		metodoRetornoEspecie = new MetodoUtil(this, "retornoEspecie");
		super.init();
	}
	
	public void load(DadoConcedidoInterface dadoConcedidoDTO, boolean somenteVisualizacao) {
		try{
			init();
			this.somenteVisualizacao = somenteVisualizacao;
			dadoConcedido = (DadoConcedidoImpl) dadoConcedidoDTO;
			autorizacaomanejoCabrucaServiceFacade.montarDto(this, PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p11, MotivoNotificacaoEnum.SHAPE_CABRUCA);
			verificarPoligonalConcedida();
			asvSupressaoService.removerListaEspecieSupressaoEdicao(getListaEspecieSupressaoAutorizacao(), getListaEspecieSupressao(), getListaEspecieSupressaoAll());
			atualizar("pnlAutorizacaoManejoCabruca");
			exibir("dlgAutorizacaoManejoCabruca");
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void retornoEspecie() {
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
			
			String id = "accAutorizacaoManejoCabruca:"
					.concat(obterPosicaoNaLista(listaAutorizacaoManejoCabrucaDTO, dadoConcedidoDTO).toString())
					.concat(":gridTableEspecieSupressao");
			
			Html.atualizar(id);
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
	}
	
	public void validarEspecie(EspecieSupressaoAutorizacao especieSupressao) {
		if (Util.isNullOuVazio(especieSupressao.getVolumeTotalForaApp())) {
			MensagemUtil.msg0003("Volume total de APP");
			return;
		}
		
		if (Util.isNullOuVazio(especieSupressao.getVolumeTotalEmApp())) {
			MensagemUtil.msg0003("Volume total em APP");
			return;
		}
		
		if (Util.isNullOuVazio(especieSupressao.getListaDestinoSocioeconomicoSelecionado())){
			MensagemUtil.msg0003("Destino socioeconêmico do Produto");
			return;
		}
		
		especieSupressao.setEdicao(false);
	}
	public boolean isRenderedBtnSalvar() {
		return !somenteVisualizacao;
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

	public void salvar() {
		try {
			if(validarPreenchimento()){
				autorizacaomanejoCabrucaServiceFacade.salvarProcessoAtoConcedido(listaAutorizacaoManejoCabrucaDTO, dadoConcedido);
				atualizar("pnlAnaliseTecnica");
				JsfUtil.addSuccessMessage(Util.getString("MSG-010"));
				esconder("dlgAutorizacaoManejoCabruca");
			}

		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("dado_concedido_cabruca_msg_erro"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	//Verificar se ao menos um item (requerimento ou notificação) foi marcado
	private Boolean validarPreenchimento() {
		Boolean retornoRequerimento = Boolean.FALSE;
		Boolean retornoNotificacao = Boolean.FALSE;
		Boolean retornoEspecie = Boolean.TRUE;
		
		for (DadoConcedidoDTO dadoConcedidoDTO : listaAutorizacaoManejoCabrucaDTO) {
			//quando recomeçar o for as flags são reinicializadas
			retornoRequerimento = Boolean.FALSE;
			retornoNotificacao = Boolean.FALSE;
			retornoEspecie = Boolean.TRUE;
			
			if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalDTO())){
				for(DadoConcedidoPoligonalDTO requerimento : dadoConcedidoDTO.getListaPoligonalDTO()){
					if(!requerimento.isConcedido()){
						retornoRequerimento = Boolean.FALSE;
						if(!Util.isNull(dadoConcedidoDTO.getListaPoligonalNotificacaoDTO())){
							
							for(DadoConcedidoPoligonalDTO notificacao : dadoConcedidoDTO.getListaPoligonalNotificacaoDTO()){
								if(!notificacao.isConcedido()){
									JsfUtil.addErrorMessage(Util.getString("MSG-erro-selecionar-areas"));
									return Boolean.FALSE;
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
			
			if (Util.isNullOuVazio(dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao())){
				JsfUtil.addErrorMessage("A seleção da especie " + Util.getString("lac_dadosGerais_msg003"));
				return Boolean.FALSE;
			} else {
				for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao :  dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao()) {
					if (especieSupressaoAutorizacao.getEdicao()){
						JsfUtil.addErrorMessage("A confirmação da especie " + Util.getString("lac_dadosGerais_msg003"));
						return Boolean.FALSE;
					}
				}
			}
			
		}
		return (retornoRequerimento || retornoNotificacao) && retornoEspecie;
	}

	private void verificarPoligonalConcedida() {
		autorizacaomanejoCabrucaServiceFacade.verificarDadoConcedido(this);
	}
	
	public boolean isRenderedPoligonalNotificacao(DadoConcedidoDTO dadoConceidodDTO) {
		return !dadoConceidodDTO.getListaPoligonalNotificacaoDTO().isEmpty();
	}
	
	
	public void add(DadoConcedidoDTO dadoConcedidoDTO, DadoConcedidoPoligonalDTO pc) {
		if(!Util.isNull(pc) && pc.isConcedido()) {
			dadoConcedidoDTO.setPoligonalConcedida(pc);
		}
		else {
			dadoConcedidoDTO.setPoligonalConcedida(null);
		}
	}
	
	public String criarUrlShape(LocalizacaoGeografica lg) {
		return Util.criarUrlShape(lg);
	}
	
	public boolean isRenderedPoligonalRequerimento(DadoConcedidoDTO dadoConcedidoDTO) {
		return !dadoConcedidoDTO.getListaPoligonalDTO().isEmpty();
	}

	public List<DadoConcedidoDTO> getListaAutorizacaoManejoCabrucaDTO() {
		return listaAutorizacaoManejoCabrucaDTO;
	}

	public void setListaAutorizacaoManejoCabrucaDTO(
			 List<DadoConcedidoDTO> listaAutorizacaoManejoCabrucaDTO) {
		this.listaAutorizacaoManejoCabrucaDTO = listaAutorizacaoManejoCabrucaDTO;
	}

	public DadoConcedidoImpl getDadoConcedido() {
		return dadoConcedido;
	}

	public void setDadoConcedido(DadoConcedidoImpl dadoConcedido) {
		this.dadoConcedido = dadoConcedido;
	}

	public MetodoUtil getMetodoRetornoEspecie() {
		return metodoRetornoEspecie;
	}

	public void setMetodoRetornoEspecie(MetodoUtil metodoRetornoEspecie) {
		this.metodoRetornoEspecie = metodoRetornoEspecie;
	}

	public DadoConcedidoDTO getAutorizacaoManejoCabrucaDTOSelecionado() {
		return autorizacaoManejoCabrucaDTOSelecionado;
	}

	public void setAutorizacaoManejoCabrucaDTOSelecionado(
			DadoConcedidoDTO autorizacaoManejoCabrucaDTOSelecionado) {
		this.autorizacaoManejoCabrucaDTOSelecionado = autorizacaoManejoCabrucaDTOSelecionado;
	}
}
