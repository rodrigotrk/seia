package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.AsvDadosGeraisController;
import br.gov.ba.seia.controller.DadoConcedidoAsvController;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.DadoConcedidoPoligonalDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 06/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DadoConcedidoAsvServiceFacade {

	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;

	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ImovelDAOImpl imovelDAO;
	
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeofacade;
	
	@EJB
	private LocalizacaoGeograficaService locGeoService;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDao;
	
	@EJB
	private FlorestalService florestalService;
	
	@EJB
	private AsvSupressaoService asvSupressaoService;
	
	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 06/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param dadoConcedidoPoligonalDTO
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessoAtoConcedido(List<DadoConcedidoDTO> listaDTO, DadoConcedidoImpl dadoConcedido) throws Exception {
		for(DadoConcedidoDTO dadoConcedidoDTO : listaDTO) {
			if(!Util.isNull(dadoConcedidoDTO.getPoligonalConcedida())) {
				ProcessoAtoConcedido processoAtoConcedido = new ProcessoAtoConcedido(dadoConcedidoDTO);
				processoAtoConcedido.setIdeProcessoAto(dadoConcedido.getProcessoAto());
				processoAtoConcedido.setIdeTipoAreaConcedida(new TipoAreaConcedida(TipoAreaConcedidaEnum.ATIVIDADE));
				processoAtoConcedidoDAOImpl.salvar(processoAtoConcedido);
				
				/*for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao()) {
					especieSupressaoAutorizacao.setIdeEspecieSupressaoAutorizacao(null);
					especieSupressaoAutorizacao.setIdeProcessoAtoConcedido(processoAtoConcedido);
				}

				asvSupressaoService.salvarListaEspecieSupressaoAutorizacao(dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao());*/
			} 
		}
		dadoConcedido.setConcluido(true);
	}
	
	public void salvarEspecieSupressaoAutorizacaoProcessoAto(EspecieSupressaoAutorizacao especieSupressaoAutorizacao) throws Exception{
		asvSupressaoService.salvarEspecieSupressaoAutorizacao(especieSupressaoAutorizacao);
	}
	
	public void salvarASVeFceASV(DadoConcedidoAsvController dadoConcedidoAsvController,AsvDadosGeraisController asvDadosGeraisController) throws Exception{
		
		asvDadosGeraisController.finalizar();
		dadoConcedidoAsvController.salvarASVeFceEASV();
		
	}
	
	public String montarDto(DadoConcedidoAsvController controller, PerguntaEnum perguntaEnum, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		controller.setListaDTO(new ArrayList<DadoConcedidoDTO>());
		Integer ideProcesso = controller.getDadoConcedido().getProcessoAto().getProcesso().getIdeProcesso();
		Integer ideRequerimento = obterIdeRequerimento(ideProcesso);
		ProcessoAto processoAto = controller.getDadoConcedido().getProcessoAto();
		Collection<Imovel> imoveis = listarAllImoveisBy(ideProcesso);
		
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
//				List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao = null;
				
				if(Util.isNullOuVazio(locGeo)) return "ALE-0011";
				
				if(Util.isNullOuVazio(locGeo.getIdeSistemaCoordenada())){
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					
					calcularArea(poligonalDTO);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					
					listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);
					
//					listaEspecieSupressaoAutorizacao = preencherEspecieSupressaoAutorizacao(processoAto, dadoConcedidoDTO, ideRequerimento, i);
//					dadoConcedidoDTO.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
					
					controller.getListaDTO().add(dadoConcedidoDTO);
				}else{
					
					Florestal florestal = florestalService.obterFlorestalByRequerimentoImovel(ideRequerimento, i);
					
					DadoConcedidoPoligonalDTO poligonalDTO = new DadoConcedidoPoligonalDTO(locGeo, null);
					poligonalDTO.setValArea(new Double(florestal.getNumAreaSuprimida().toString()));
					locGeofacade.tratarPonto(locGeo);
					dadoConcedidoDTO.getListaPoligonalDTO().add(poligonalDTO);
					dadoConcedidoDTO.setLatitudeInicial(locGeo.getLatitudeInicial());
					dadoConcedidoDTO.setLongitudeInicial(locGeo.getLongitudeInicial());
					listarPoligonalNotificacao(dadoConcedidoDTO, ideProcesso, i, motivoNotificacaoEnum);
					
//					listaEspecieSupressaoAutorizacao = preencherEspecieSupressaoAutorizacao(processoAto, dadoConcedidoDTO, ideRequerimento, i);
//					dadoConcedidoDTO.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
					
					controller.getListaDTO().add(dadoConcedidoDTO);
				}	
				
				if (posicao == 0){
					controller.setAutorizacaoManejoCabrucaDTOSelecionado(dadoConcedidoDTO);
//					controller.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
				}
				posicao++;
				
			}
		}
		
		return "";
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<EspecieSupressaoAutorizacao> preencherEspecieSupressaoAutorizacao(ProcessoAto processoAto, DadoConcedidoDTO dadoConcedidoDTO, Integer ideRequerimento, Imovel imovel) throws Exception {
		List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao = null;
		
		ProcessoAtoConcedido processoAtoConcedido = listarProcessoAtoConcedidoPorImovel(processoAto.getIdeProcessoAto(), imovel);
		if (!Util.isNullOuVazio(processoAtoConcedido)){
			listaEspecieSupressaoAutorizacao = asvSupressaoService.listaEspecieSupressaoAutorizacaoPorProcessoAtoConcedido(processoAtoConcedido, imovel);
		}

		if (Util.isNullOuVazio(listaEspecieSupressaoAutorizacao)){
			listaEspecieSupressaoAutorizacao = asvSupressaoService.listaEspecieSupressaoAutorizacaoPorRequerimento(ideRequerimento);
		}
		//dadoConcedidoDTO.setListaEspecieSupressaoAutorizacao(listaEspecieSupressaoAutorizacao);
		
		return listaEspecieSupressaoAutorizacao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Integer obterIdeRequerimento(Integer ideProcesso) throws Exception {
		return processoService.carregarProcesso(ideProcesso).getIdeRequerimento().getIdeRequerimento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Imovel> listarAllImoveisBy(Integer ideProcesso) throws Exception {
		return imovelDAO.listarAllImoveisPor(ideProcesso);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Imovel> listarImoveisBy(Integer ideProcesso) throws Exception {
		return imovelDAO.listarImoveisPor(ideProcesso);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterNomeDoEmpreendimento(Integer ideProcesso) throws Exception {
		return empreendimentoService.buscarEmpreendimentoPorProcesso(ideProcesso).getNomEmpreendimento();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private LocalizacaoGeografica carregarLocalizacaoGeografica(PerguntaEnum perguntaEnum, Integer ideRequerimento, Imovel i) throws Exception {
		return locGeoService.buscarLocalizacaoGeograficaAbaFlorestal(ideRequerimento, i.getIdeImovel(), perguntaEnum);
	}
	
	public void verificarDadoConcedido(DadoConcedidoAsvController controller) throws Exception {
		try {
			if (controller == null || controller.getDadoConcedido() == null || 
				controller.getDadoConcedido().getProcessoAto() == null) {
				return;
			}

			Integer ideProcessoAto = null;
			try {
				ideProcessoAto = controller.getDadoConcedido().getProcessoAto().getIdeProcessoAto();
			} catch (NullPointerException e) {
				return;
			}

			if (ideProcessoAto == null) {
				return;
			}

			List<ProcessoAtoConcedido> processosAtoConcedidos = null;
			try {
				processosAtoConcedidos = listarProcessoAtoConcedido(ideProcessoAto);
			} catch (Exception e) {
				return;
			}
			
			if (processosAtoConcedidos == null || controller.getListaDTO() == null) {
				return;
			}

			for (ProcessoAtoConcedido processoAtoConcedido : processosAtoConcedidos) {
				try {
					if (processoAtoConcedido == null || processoAtoConcedido.getIdeImovel() == null || 
						processoAtoConcedido.getIdeLocalizacaoGeografica() == null) {
						continue;
					}
					
					for (DadoConcedidoDTO dto : controller.getListaDTO()) {
						try {
							if (dto == null || dto.getIdeImovel() == null) {
								continue;
							}

							if(dto.getIdeImovel().equals(processoAtoConcedido.getIdeImovel().getIdeImovel())) {
								DadoConcedidoPoligonalDTO poligonalConcedida = null;
								
								try {
									if(!Util.isNull(dto.getListaPoligonalNotificacaoDTO())) {
										poligonalConcedida = localizacaoGeograficaConcedida(
											dto.getListaPoligonalNotificacaoDTO(), 
											processoAtoConcedido.getIdeLocalizacaoGeografica());
									}
									
									if(Util.isNull(poligonalConcedida) && !Util.isNull(dto.getListaPoligonalDTO())) {
										poligonalConcedida = localizacaoGeograficaConcedida(
											dto.getListaPoligonalDTO(), 
											processoAtoConcedido.getIdeLocalizacaoGeografica());
									}
									
									if (poligonalConcedida != null) {
										poligonalConcedida.setConcedido(true);
										dto.setPoligonalConcedida(poligonalConcedida);
									}
								} catch (Exception e) {
									continue;
								}
								break;
							}
						} catch (Exception e) {
							continue;
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao verificar dados concedidos: " + e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProcessoAtoConcedido> listarProcessoAtoConcedido(Integer ideProcessoAto) throws Exception {
		return processoAtoConcedidoDAOImpl.listarProcessoAtoConcedido(ideProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ProcessoAtoConcedido listarProcessoAtoConcedidoPorImovel(Integer ideProcessoAto, Imovel imovel) throws Exception {
		return processoAtoConcedidoDAOImpl.buscarProcessoAtoConcedidoPorImovel(ideProcessoAto, imovel);
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
	private ArquivoProcesso carregarArquivoProcessoBy(Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum, Notificacao notificacao) throws Exception {
		return arquivoProcessoService.carregarArquivoShape(notificacao, motivoNotificacaoEnum, imovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Notificacao> listarNotificacoes(Integer ideProcesso, Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		return notificacaoDao.listarNotificacaoRespondidasBy(ideProcesso, imovel.getIdeImovel(), motivoNotificacaoEnum);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 13/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param dadoConcedido
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoAtoConcedido(DadoConcedidoImpl dadoConcedido) throws Exception {
		processoAtoConcedidoDAOImpl.excluirBy(dadoConcedido.getProcessoAto());
	}
	

}
