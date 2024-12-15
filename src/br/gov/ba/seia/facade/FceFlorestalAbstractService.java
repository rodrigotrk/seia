package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceFlorestalAbstractController;
import br.gov.ba.seia.dao.DestinoSocioeconomicoDAOImpl;
import br.gov.ba.seia.dao.EspecieFlorestalAutDestinoSocioEconomicoDAOImpl;
import br.gov.ba.seia.dao.EspecieFlorestalAutorizacaoDAOImpl;
import br.gov.ba.seia.dao.EspecieFlorestalDAOImpl;
import br.gov.ba.seia.dao.FceFlorestalDAOImpl;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.NomePopularEspecieDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.DadoConcedidoPoligonalDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.DestinoSocioeconomico;
import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.EspecieFlorestalAutDestinoSocioEconomico;
import br.gov.ba.seia.entity.EspecieFlorestalAutorizacao;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceFlorestal;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.enumerator.TipoEspecieFlorestalEnum;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.ProdutoService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public abstract class FceFlorestalAbstractService {
	
	/*public abstract List<Produto> carregarListaProdutosByTipoProduto(int ideTipoProduto) throws Exception;
	
	public abstract List<EspecieFlorestal> listarEspecieFlorestal(Integer ideEspecieFlorestal) throws Exception;

	public abstract List<EspecieFlorestal> pesquisarEspecieFlorestal(List<EspecieFlorestal> listaEspecieSupressao, List<EspecieFlorestal> listaEspecieSupressaoAll, String nomEspecieSupressao);*/
	
	public abstract BigDecimal obterValorTotalArea(Integer ideRequerimento) throws Exception;
	
	@EJB
	private ProdutoService produtoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeofacade;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private FlorestalService florestalService;
	
	@EJB
	private EspecieFlorestalDAOImpl florestalDAO;
	
	@EJB
	private NomePopularEspecieDAOImpl nomePopularEspecieDAO;
	
	@EJB
	private DestinoSocioeconomicoDAOImpl destinoSocioeconomicoDAO;
	
	@EJB
	private FceFlorestalDAOImpl fceFlorestalDAO;
	
	@EJB
	private EspecieFlorestalAutDestinoSocioEconomicoDAOImpl especieFlorestalAutDestinoSocioEconomicoDAO;
	
	@EJB
	private EspecieFlorestalAutorizacaoDAOImpl especieFlorestalAutorizacaoDAO;
	
	@EJB
	private ImovelDAOImpl imovelListagemDAOImpl;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDao;
	
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	
	public Processo buscarProcessoPorRequerimento(Integer ideRequerimento) throws Exception {
		return processoService.buscarPorRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarImoveisBy(Integer ideProcesso) throws Exception {
		return imovelListagemDAOImpl.listarImoveisPor(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String obterNomeDoEmpreendimento(Integer ideProcesso) throws Exception {
		return empreendimentoService.buscarEmpreendimentoPorProcesso(ideProcesso).getNomEmpreendimento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregarLocalizacaoGeografica(PerguntaEnum perguntaEnum, Integer ideRequerimento, Imovel i) throws Exception {
		return localizacaoGeograficaService.buscarLocalizacaoGeograficaAbaFlorestal(ideRequerimento, i.getIdeImovel(), perguntaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void calcularArea(DadoConcedidoPoligonalDTO dto) throws Exception {
		dto.setValArea(locGeofacade.retornarAreaShape(dto.getLocalizacaoGeografica()));
	}
	
	public List<Produto> carregarListaProdutosByTipoProduto(int ideTipoProduto) throws Exception{
		return produtoService.carregarListaProdutosByTipoProduto(ideTipoProduto);
	}
	
	public List<EspecieFlorestal> listarEspecieFlorestal(Integer ideEspecieFlorestal) throws Exception{
		return florestalDAO.listarEspecieFlorestal(ideEspecieFlorestal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacoes(Integer ideProcesso, Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
		return notificacaoDao.listarNotificacaoRespondidasBy(ideProcesso, imovel.getIdeImovel(), motivoNotificacaoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ArquivoProcesso carregarArquivoProcessoBy(Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum, Notificacao notificacao) throws Exception {
		return arquivoProcessoService.carregarArquivoShape(notificacao, motivoNotificacaoEnum, imovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Florestal obterFlorestalByRequerimento(Integer ideRequerimento) throws Exception {
		return florestalService.obterFlorestalByRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarPonto(LocalizacaoGeografica locGeo) {
		locGeofacade.tratarPonto(locGeo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerProcessoAtoConcedido(ProcessoAto processoAto) throws Exception {
		processoAtoConcedidoDAOImpl.excluirPorProcessoAto(processoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessoAtoConcedido(List<DadoConcedidoDTO> listaDTO, DadoConcedidoImpl dadoConcedido) throws Exception {
		removerProcessoAtoConcedido(dadoConcedido.getProcessoAto());
		
		for(DadoConcedidoDTO dadoConcedidoDTO : listaDTO) {
			if(!Util.isNull(dadoConcedidoDTO.getPoligonalConcedida())) {
				ProcessoAtoConcedido processoAtoConcedido = new ProcessoAtoConcedido(dadoConcedidoDTO);
				processoAtoConcedido.setIdeProcessoAto(dadoConcedido.getProcessoAto());
				processoAtoConcedido.setIdeTipoAreaConcedida(new TipoAreaConcedida(TipoAreaConcedidaEnum.ATIVIDADE));
				processoAtoConcedidoDAOImpl.salvar(processoAtoConcedido);
			} 
		}
		dadoConcedido.setConcluido(true);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoAtoConcedido(Integer ideProcessoAto) throws Exception {
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
	
	public void listarPoligonalNotificacao(DadoConcedidoDTO dto, Integer ideProcesso, Imovel imovel, MotivoNotificacaoEnum motivoNotificacaoEnum) throws Exception {
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
	public List<EspecieFlorestal> pesquisarEspecieFlorestal(List<EspecieFlorestal> listaEspecieSupressao, List<EspecieFlorestal> listaEspecieSupressaoAll, String nomEspecieSupressao){
		listaEspecieSupressao = new ArrayList<EspecieFlorestal>();
		for (EspecieFlorestal tipoClassificacao : listaEspecieSupressaoAll) {
			if(tipoClassificacao.getNomEspecieFlorestal().toLowerCase().indexOf(nomEspecieSupressao.toLowerCase())!= -1){
				listaEspecieSupressao.add(tipoClassificacao);
			}
		}
		
		return listaEspecieSupressao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removerListaEspecieSupressaoAll(List<EspecieFlorestal> lista,
			EspecieFlorestal especieSupressao,
			NomePopularEspecie nomePopularEspecie) throws Exception {

		List<NomePopularEspecie> listarNomePopularEspecie = listarNomePopularEspecie(especieSupressao);

		if (!Util.isNullOuVazio(listarNomePopularEspecie)) {
			Integer posicao = lista.indexOf(especieSupressao);

			if (posicao != -1) {
				EspecieFlorestal es = lista.get(posicao);
				if (Util.isNullOuVazio(es.getRemovidosNomePopularEspecie())) {
					es.setRemovidosNomePopularEspecie(new ArrayList<NomePopularEspecie>());
				}

				if (!es.getRemovidosNomePopularEspecie().contains(
						nomePopularEspecie)) {
					if ((es.getRemovidosNomePopularEspecie().size() + 1) == listarNomePopularEspecie
							.size()) {
						lista.remove(es);
					}
					es.getRemovidosNomePopularEspecie().add(nomePopularEspecie);
				}
			}
		} else {
			if (lista.contains(especieSupressao)) {
				lista.remove(especieSupressao);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean ajustarRetornoEspecie(FceFlorestalAbstractController ctrl) throws Exception {
		
		if (!Util.isNullOuVazio(ctrl.getEspecieFlorestalSelecionada())){
			
			boolean verificarSeEspecieJaAdd = verificarSeEspecieJaAdd(ctrl.getEspecieFlorestalAutorizacaoList(), ctrl.getEspecieFlorestalSelecionada(), ctrl.getNomePopularEspecieSelecionada());
			if (!verificarSeEspecieJaAdd){
				
				if (Util.isNullOuVazio(ctrl.getEspecieFlorestalAutorizacaoList())){
					ctrl.setEspecieFlorestalAutorizacaoList(new ArrayList<EspecieFlorestalAutorizacao>());
				}
				
				EspecieFlorestalAutorizacao especieFlorestalAutorizacao = new EspecieFlorestalAutorizacao();
				
				if (!Util.isNullOuVazio(ctrl.getNomePopularEspecieSelecionada().getIdeNomePopularEspecie())){
					especieFlorestalAutorizacao.setIdeNomePopularEspecie(ctrl.getNomePopularEspecieSelecionada());
				}
				
				especieFlorestalAutorizacao.setIdeEspecieFlorestal(ctrl.getEspecieFlorestalSelecionada());
				especieFlorestalAutorizacao.setFceFlorestal(ctrl.getFceFlorestal());
				especieFlorestalAutorizacao.setIdeProduto(ctrl.getProdutoNativoSelecionado());
				
				removerListaEspecieSupressaoAll(ctrl.getEspecieFlorestalList(), ctrl.getEspecieFlorestalSelecionada(), ctrl.getNomePopularEspecieSelecionada());
				removerListaEspecieSupressaoAll(ctrl.getEspecieFlorestalListAll(), ctrl.getEspecieFlorestalSelecionada(), ctrl.getNomePopularEspecieSelecionada());
				
				ctrl.getEspecieFlorestalAutorizacaoList().add(especieFlorestalAutorizacao);
				ctrl.setEspecieFlorestalSelecionada(null);
				ctrl.setProdutoNativoSelecionado(null);
				return true;
			}
		}else if(!Util.isNullOuVazio(ctrl.getProdutoNativoSelecionado())){
			if(ctrl.getProdutoNativoSelecionado().getDscProduto().equalsIgnoreCase("Lenha")){
				
				EspecieFlorestalAutorizacao especieFlorestalAutorizacao = new EspecieFlorestalAutorizacao();
				
				especieFlorestalAutorizacao.setFceFlorestal(ctrl.getFceFlorestal());
				especieFlorestalAutorizacao.setIdeProduto(ctrl.getProdutoNativoSelecionado());
				
				if(Util.isNull(ctrl.getEspecieFlorestalAutorizacaoList())){
					ctrl.setEspecieFlorestalAutorizacaoList(new ArrayList<EspecieFlorestalAutorizacao>());
				}
				ctrl.getEspecieFlorestalAutorizacaoList().add(especieFlorestalAutorizacao);
				
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarSeEspecieJaAdd(List<EspecieFlorestalAutorizacao> lista, EspecieFlorestal especieSupressao, NomePopularEspecie nomePopularEspecie) {
		if (!Util.isNullOuVazio(lista)){
			for (EspecieFlorestalAutorizacao especieFlorestalAutorizacao : lista) {
				if (!Util.isNullOuVazio(especieFlorestalAutorizacao.getIdeNomePopularEspecie())) {
					if (especieFlorestalAutorizacao.getIdeNomePopularEspecie().getIdeNomePopularEspecie().equals(nomePopularEspecie.getIdeNomePopularEspecie()) &&
							especieFlorestalAutorizacao.getIdeEspecieFlorestal().getIdeEspecieFlorestal().equals(especieSupressao.getIdeEspecieFlorestal())){
						return true;
					}
				} else {
					
					if(Util.isNullOuVazio(especieFlorestalAutorizacao.getIdeProduto()) || !especieFlorestalAutorizacao.getIdeProduto().getDscProduto().equalsIgnoreCase("Lenha")){
						if (especieFlorestalAutorizacao.getIdeEspecieFlorestal().getIdeEspecieFlorestal().equals(especieSupressao.getIdeEspecieFlorestal())){
							return true;
						}
					}
					
				}
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieFlorestal especieFlorestal) throws Exception {
		return nomePopularEspecieDAO.listarNomePopularEspecie(especieFlorestal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoSocioeconomico> listarDestinoSocioeconomico() throws Exception {
		return destinoSocioeconomicoDAO.listarDestinoSocioeconomico();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFlorestalAutDestinoSocioEconomico> listarEspSuprAutDestSocioEconPorEspSuprAut(EspecieFlorestalAutorizacao especieFlorestalAutorizacao) throws Exception {
		return especieFlorestalAutDestinoSocioEconomicoDAO.listarEspSuprAutDestSocioEconPorEspSuprAut(especieFlorestalAutorizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceFlorestal(FceFlorestal fceFlorestal) throws Exception {
		prepararParaSalvarFceFlorestal(fceFlorestal);
		fceFlorestalDAO.salvar(fceFlorestal);
		salvarEspecieFlorestalAutorizacao(fceFlorestal.getListaEspecieFlorestalAutorizacaoNativa());
		salvarEspecieFlorestalAutorizacao(fceFlorestal.getListaEspecieFlorestalAutorizacaoExotica());
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void prepararParaSalvarFceFlorestal(FceFlorestal fceFlorestal) throws Exception {
		if(!Util.isNullOuVazio(fceFlorestal)) {
			removerEspecieFlorestalAutorizacao(fceFlorestal.getListaEspecieFlorestalAutorizacaoNativaRemocao());
			removerEspecieFlorestalAutorizacao(fceFlorestal.getListaEspecieFlorestalAutorizacaoExoticaRemocao());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void removerEspecieFlorestalAutorizacao(Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacao) throws Exception {
		if(!Util.isNullOuVazio(listaEspecieFlorestalAutorizacao)) {
			for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : listaEspecieFlorestalAutorizacao) {
				if(!Util.isNullOuVazio(especieFlorestalAutorizacao)) {
					especieFlorestalAutDestinoSocioEconomicoDAO.removerEspecieFlorestalAutDestinoSocioEconomico(especieFlorestalAutorizacao);
					especieFlorestalAutorizacaoDAO.remover(especieFlorestalAutorizacao);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarEspecieFlorestalAutorizacao(Collection<EspecieFlorestalAutorizacao> listaEspecieFlorestalAutorizacao) throws Exception {
		if(!Util.isNullOuVazio(listaEspecieFlorestalAutorizacao)) {
			for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : listaEspecieFlorestalAutorizacao) {
				especieFlorestalAutDestinoSocioEconomicoDAO.removerEspecieFlorestalAutDestinoSocioEconomico(especieFlorestalAutorizacao);
				especieFlorestalAutorizacaoDAO.salvar(especieFlorestalAutorizacao);
				for(EspecieFlorestalAutDestinoSocioEconomico especieFlorestalAutDestinoSocioEconomico : especieFlorestalAutorizacao.getEspecieFlorestalAutDestinoSocioEconomicoCollection()) {
					especieFlorestalAutDestinoSocioEconomicoDAO.salvar(especieFlorestalAutDestinoSocioEconomico);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceFlorestalAbstractController ctrl) throws Exception {
		ctrl.finalizar();
	}
	
	public FceFlorestal buscarFceFlorestal(Fce fce) throws Exception{
		
		return fceFlorestalDAO.buscarFceFlorestal(fce);
	}
	
	public List<EspecieFlorestalAutorizacao> buscarEspecieFlorestalAutorizacaoNativa(FceFlorestal fceFlorestal, TipoEspecieFlorestalEnum ideTipoEspecieFlorestal) throws Exception{
		return prepararListaDestinoSocioEconomico(especieFlorestalAutorizacaoDAO.buscarEspecieFlorestalAutorizacaoNativa(fceFlorestal, ideTipoEspecieFlorestal));
	}
	
	public List<EspecieFlorestalAutorizacao> prepararListaDestinoSocioEconomico(List<EspecieFlorestalAutorizacao> especieFlorestalAutorizacaoes){
		
		for(EspecieFlorestalAutorizacao especieFlorestalAutorizacao : especieFlorestalAutorizacaoes){
			
			especieFlorestalAutorizacao.setListaDestinoSocioeconomicoSelecionado(new ArrayList<DestinoSocioeconomico>());
			
			for(EspecieFlorestalAutDestinoSocioEconomico autDestinoSocioEconomico : especieFlorestalAutorizacao.getEspecieFlorestalAutDestinoSocioEconomicoCollection()){
				especieFlorestalAutorizacao.getListaDestinoSocioeconomicoSelecionado().add(autDestinoSocioEconomico.getIdeDestinoSocioeconomico());
			}
			
		}
		
		return especieFlorestalAutorizacaoes;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removerListaEspecieSupressaoEdicao(FceFlorestalAbstractController ctrl) throws Exception {
		for (EspecieFlorestalAutorizacao especieFlorestalAutorizacao : ctrl.getEspecieFlorestalAutorizacaoList()) {
			if(!Util.isNullOuVazio(especieFlorestalAutorizacao.getIdeEspecieFlorestal())){
				
				removerListaEspecieSupressaoAll(ctrl.getEspecieFlorestalList(), especieFlorestalAutorizacao.getIdeEspecieFlorestal(), especieFlorestalAutorizacao.getIdeNomePopularEspecie());
				removerListaEspecieSupressaoAll(ctrl.getEspecieFlorestalListAll(), especieFlorestalAutorizacao.getIdeEspecieFlorestal(), especieFlorestalAutorizacao.getIdeNomePopularEspecie());
			}
		}
	}
}
