package br.gov.ba.seia.service.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.AutorizacaoManejoCabrucaController;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.LocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.DadoConcedidoPoligonalDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.facade.AnaliseTecnicaServiceFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AutorizacaoManejoCabrucaServiceFacade {

	@EJB
	private LocalizacaoGeograficaDAOImpl localizacaoGeograficaDAOImpl;
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	@EJB
	private ImovelDAOImpl imovelListagemDAOImpl;
	
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	
	@EJB
	private FlorestalService florestalService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDao;
	
	@EJB
	private LocalizacaoGeograficaService locGeoService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeofacade;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoIDAO;
	
	@EJB
	private AnaliseTecnicaServiceFacade analiseTecnicaServiceFacade;
	
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoService;
	
	@EJB
	private AsvSupressaoService asvSupressaoService;
	
			
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessoAtoConcedido(List<DadoConcedidoDTO> listaDTO, DadoConcedidoImpl dadoConcedido) {
		
		asvSupressaoService.removerListaEspecieSupressaoAutorizacao(listaDTO);
		excluirProcessoAtoConcedido(dadoConcedido);
		
		for(DadoConcedidoDTO dadoConcedidoDTO : listaDTO) {
			if(!Util.isNull(dadoConcedidoDTO.getPoligonalConcedida())) {
				ProcessoAtoConcedido processoAtoConcedido = new ProcessoAtoConcedido(dadoConcedidoDTO);
				processoAtoConcedido.setIdeProcessoAto(dadoConcedido.getProcessoAto());
				processoAtoConcedido.setIdeTipoAreaConcedida(new TipoAreaConcedida(TipoAreaConcedidaEnum.ATIVIDADE));
				processoAtoConcedidoDAOImpl.salvar(processoAtoConcedido);
				
				for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao()) {
					especieSupressaoAutorizacao.setIdeEspecieSupressaoAutorizacao(null);
					especieSupressaoAutorizacao.setIdeProcessoAtoConcedido(processoAtoConcedido);
					especieSupressaoAutorizacao.setFceAsv(null);
				}

				asvSupressaoService.salvarListaEspecieSupressaoAutorizacao(dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao());
			} 
		}
		dadoConcedido.setConcluido(true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoAtoConcedido(DadoConcedidoImpl dadoConcedido) {
		for (ProcessoAtoConcedido processoAtoConcedido : listarProcessoAtoConcedido(dadoConcedido.getProcessoAto().getIdeProcessoAto())) {
			processoAtoConcedidoDAOImpl.remover(processoAtoConcedido);
		}
	}
	
	public void montarDto(AutorizacaoManejoCabrucaController controller, PerguntaEnum perguntaEnum, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		controller.setListaAutorizacaoManejoCabrucaDTO(new ArrayList<DadoConcedidoDTO>());
		Integer ideProcesso = controller.getDadoConcedido().getProcessoAto().getProcesso().getIdeProcesso();
		Integer ideRequerimento = obterIdeRequerimento(ideProcesso);
		ProcessoAto processoAto = controller.getDadoConcedido().getProcessoAto();
		Processo processo = processoAto.getProcesso();
		
		Collection<Imovel> imoveis = imovelListagemDAOImpl.listarAllImoveisPor(processo.getIdeProcesso());
		
		if(!Util.isNullOuVazio(imoveis)) {
			Integer posicao = 0; 
			for(Imovel i : imoveis) {
				String nome = "";
				
				if(Util.isNull(i.getImovelRural())) {
					nome = obterNomeDoEmpreendimento(ideProcesso);
				} 
				else {
					nome = i.getNomeImovelRural();
				}
				DadoConcedidoDTO dadoConcedidoDTO = new DadoConcedidoDTO(i.getIdeImovel(), nome);
				LocalizacaoGeografica locGeo = carregarLocalizacaoGeografica(perguntaEnum, ideRequerimento, i); 
				List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao = null;
				if(Util.isNullOuVazio(locGeo.getIdeSistemaCoordenada())){
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					
					calcularArea(poligonalDTO);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					
					listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);

					listaEspecieSupressaoAutorizacao = preencherEspecieSupressaoAutorizacao(processoAto, i);
					dadoConcedidoDTO.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
					
					controller.getListaAutorizacaoManejoCabrucaDTO().add(dadoConcedidoDTO);
				}else{
					
					Florestal florestal = florestalService.obterFlorestalByRequerimento(ideRequerimento);
					
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					poligonalDTO.setValArea(new Double(florestal.getNumAreaManejoCabruca().toString()));
					locGeofacade.tratarPonto(locGeo);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					dadoConcedidoDTO.setLatitudeInicial(locGeo.getLatitudeInicial());
					dadoConcedidoDTO.setLongitudeInicial(locGeo.getLongitudeInicial());
					listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);
					
					listaEspecieSupressaoAutorizacao = preencherEspecieSupressaoAutorizacao(processoAto,  i);
					dadoConcedidoDTO.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
					
					controller.getListaAutorizacaoManejoCabrucaDTO().add(dadoConcedidoDTO);
				}	
				if (posicao == 0){
					controller.setAutorizacaoManejoCabrucaDTOSelecionado(dadoConcedidoDTO);
					controller.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
				}
				posicao++;
			}
		}
		
	}
	
	private List<EspecieSupressaoAutorizacao> preencherEspecieSupressaoAutorizacao(ProcessoAto processoAto,  Imovel imovel) {
		List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao = null;

		
		ProcessoAtoConcedido processoAtoConcedido = listarProcessoAtoConcedidoPorImovel(processoAto.getIdeProcessoAto(), imovel);
		if (!Util.isNullOuVazio(processoAtoConcedido)){
			listaEspecieSupressaoAutorizacao = asvSupressaoService.listaEspecieSupressaoAutorizacaoPorProcessoAtoConcedido(processoAtoConcedido, imovel);
		}

		return listaEspecieSupressaoAutorizacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ProcessoAtoConcedido listarProcessoAtoConcedidoPorImovel(Integer ideProcessoAto, Imovel imovel) {
		return processoAtoConcedidoDAOImpl.buscarProcessoAtoConcedidoPorImovel(ideProcessoAto, imovel);
	}
	
	public void verificarDadoConcedido(AutorizacaoManejoCabrucaController controller)  {
		Integer ideProcessoAto = controller.getDadoConcedido().getProcessoAto().getIdeProcessoAto();
		// Lista de Dados já Concedidos
		for (ProcessoAtoConcedido processoAtoConcedido : listarProcessoAtoConcedido(ideProcessoAto)) {
			// Lista de DTOs montados
			for (DadoConcedidoDTO dto : controller.getListaAutorizacaoManejoCabrucaDTO()) {
				// Comparação para saber se o imóvel concedido é o mesmo do DTO
				if(dto.getIdeImovel().equals(processoAtoConcedido.getIdeImovel().getIdeImovel())) {
					// Comparação para descobrir qual Localização foi concedida
					
					DadoConcedidoPoligonalDTO poligonalConcedida = null;
					
					if(!Util.isNull(dto.getListaPoligonalNotificacaoDTO())) {
						poligonalConcedida = localizacaoGeograficaConcedida(dto.getListaPoligonalNotificacaoDTO(), processoAtoConcedido.getIdeLocalizacaoGeografica());
					}
					
					if(Util.isNull(poligonalConcedida)) {
						poligonalConcedida = localizacaoGeograficaConcedida(dto.getListaPoligonalDTO(), processoAtoConcedido.getIdeLocalizacaoGeografica());
					}
					// Seta a Poligonal Concedida no DTO
					poligonalConcedida.setConcedido(true);
					dto.setPoligonalConcedida(poligonalConcedida);
					break;
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProcessoAtoConcedido> listarProcessoAtoConcedido(Integer ideProcessoAto) {
		return processoAtoConcedidoDAOImpl.listarProcessoAtoConcedido(ideProcessoAto);
	}
	
	public DadoConcedidoPoligonalDTO localizacaoGeograficaConcedida(List<DadoConcedidoPoligonalDTO> list, LocalizacaoGeografica localizacaoGeografica) {
		for (DadoConcedidoPoligonalDTO poligonalDTO : list) {
			if(localizacaoGeografica.equals(poligonalDTO.getLocalizacaoGeografica())) {
				return poligonalDTO;
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void calcularArea(DadoConcedidoPoligonalDTO dto) throws Exception {
		dto.setValArea(locGeofacade.retornarAreaShape(dto.getLocalizacaoGeografica()));
	}
	
	private void listarPoligonalNotificacao(DadoConcedidoDTO dto, Integer ideProcesso, Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		List<Notificacao> notificacoes = listarNotificacoes(ideProcesso, imovel, motivoNotificacaoEnum);
		if(!Util.isNullOuVazio(notificacoes)) {
			dto.setListaPoligonalNotificacaoDTO(new ArrayList<DadoConcedidoPoligonalDTO>());
			for(Notificacao notificacao : notificacoes) {
				ArquivoProcesso arquivoProcesso = carregarArquivoProcessoBy(imovel, motivoNotificacaoEnum, notificacao);
				if(!Util.isNull(arquivoProcesso)) {
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(arquivoProcesso.getIdeLocalizacaoGeografica(), notificacao.getNumNotificacao());
					calcularArea(poligonalDTO);
					dto.getListaPoligonalNotificacaoDTO().add(poligonalDTO);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listaProcessoConcedidoAprovadoNaAnaliseTecnicaBy(Integer ideProcesso) throws Exception {
		AnaliseTecnica analiseTecnica = analiseTecnicaServiceFacade.buscarAnaliseTecnica(ideProcesso);
		
		if(!Util.isNull(analiseTecnica)){
			List<ProcessoAtoConcedido> listarProcessoConcedidoPor = processoAtoConcedidoIDAO.listarProcessoConcedidoPor(analiseTecnica, true);
			
			for (ProcessoAtoConcedido processoAtoConcedido : listarProcessoConcedidoPor) {
				processoAtoConcedido.getIdeProcessoAto().getProcesso().getIdeRequerimento().setDocumentoObrigatorioRequerimentoCollection(documentoService.buscarDocumentosObrigatoriosRequerimentoPorIdeRequerimento(processoAtoConcedido.getIdeProcessoAto().getProcesso().getIdeRequerimento().getIdeRequerimento()));
			}
			
			return listarProcessoConcedidoPor;
		}
		return null;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ArquivoProcesso carregarArquivoProcessoBy(Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum, Notificacao notificacao) {
		return arquivoProcessoService.carregarArquivoShape(notificacao, motivoNotificacaoEnum, imovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Notificacao> listarNotificacoes(Integer ideProcesso, Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum) {
		return notificacaoDao.listarNotificacaoRespondidasBy(ideProcesso, imovel.getIdeImovel(), motivoNotificacaoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private LocalizacaoGeografica carregarLocalizacaoGeografica(PerguntaEnum perguntaEnum, Integer ideRequerimento, Imovel i) throws Exception {
		return locGeoService.buscarLocalizacaoGeograficaAbaFlorestal(ideRequerimento, i.getIdeImovel(), perguntaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterNomeDoEmpreendimento(Integer ideProcesso) throws Exception {
		return empreendimentoService.buscarEmpreendimentoPorProcesso(ideProcesso).getNomEmpreendimento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Integer obterIdeRequerimento(Integer ideProcesso) {
		return processoService.carregarProcesso(ideProcesso).getIdeRequerimento().getIdeRequerimento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent getImprimirRelatorio(Integer ideProcesso) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("IDE_PROCESSO", ideProcesso);
		lParametros.put("NOM_DOC", "AUTORIZAÇÃO DE MANEJO DA CABRUCA");
		lParametros.put("TIPO_DOC", "DADOS CONCEDIDOS");

		return new RelatorioUtil("amc.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
}