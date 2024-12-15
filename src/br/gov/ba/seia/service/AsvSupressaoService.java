package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.AsvDadosGeraisController;
import br.gov.ba.seia.controller.AsvSupressaoController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.DadoConcedidoDTO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.ClassificacaoVegetacao;
import br.gov.ba.seia.entity.DestinoSocioeconomico;
import br.gov.ba.seia.entity.DestinoSocioeconomicoProduto;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.EspecieSupressaoAutDestinoSocioEconomico;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAsv;
import br.gov.ba.seia.entity.FceAsvClassiVegetacao;
import br.gov.ba.seia.entity.FceAsvJustificativaSupressao;
import br.gov.ba.seia.entity.FceAsvJustificativaSupressaoPK;
import br.gov.ba.seia.entity.FceAsvObjetivoSupressao;
import br.gov.ba.seia.entity.FceAsvObjetivoSupressaoPK;
import br.gov.ba.seia.entity.FceAsvOcorrenciaArea;
import br.gov.ba.seia.entity.FceAsvOcorrenciaAreaPK;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.JustificativaSupressao;
import br.gov.ba.seia.entity.NomePopularEspecie;
import br.gov.ba.seia.entity.ObjetivoSupressao;
import br.gov.ba.seia.entity.OcorrenciaArea;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.ProdutoSupressao;
import br.gov.ba.seia.entity.ProdutoSupressaoDestino;
import br.gov.ba.seia.entity.ProdutoSupressaoDestinoPK;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.OcorrenciaAreaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AsvSupressaoService {

	@Inject
	private IDAO<FceAsv> fceAsvDAO;
	@Inject
	private IDAO<ClassificacaoVegetacao> classificacaoVegetacaoDAO;
	@Inject
	private IDAO<FceAsvClassiVegetacao> fceAsvClassiVegetacaoDAO;
	@Inject
	private IDAO<OcorrenciaArea> ocorrenciaAreaDAO;
	@Inject
	private IDAO<FceAsvOcorrenciaArea> fceAsvOcorrenciaAreaDAO;
	@Inject
	private IDAO<JustificativaSupressao> justificativaSupressaoDAO;
	@Inject
	private IDAO<FceAsvJustificativaSupressao> fceAsvJustificativaSupressaoDAO;
	@Inject
	private IDAO<ObjetivoSupressao> objSupressaoDAO;
	@Inject
	private IDAO<FceAsvObjetivoSupressao> fceAsvObjetivoSupressaoDAO;
	@Inject
	private IDAO<DestinoSocioeconomico> destinoSocioEconomicoDAO;
	@Inject
	private IDAO<DestinoSocioeconomicoProduto> destinoSocioEconomicoProdutoDAO;
	@Inject
	private IDAO<ProdutoSupressao> produtoSupressaoDAO;
	@Inject
	private IDAO<ProdutoSupressaoDestino> produtoSupressaoDestinoDAO;
	@Inject
	private IDAO<App> appDAO;
	@Inject
	private IDAO<EspecieSupressao> especieSupressaoDAO;
	@Inject
	private IDAO<NomePopularEspecie> nomePopularEspecieDAO;
	@Inject
	private IDAO<DestinoSocioeconomico> destinoSocioeconomicoIDAO;
	@Inject
	private IDAO<EspecieSupressaoAutDestinoSocioEconomico> especieSupressaoAutDestinoSocioEconomicoDAO;
	@Inject
	private IDAO<EspecieSupressaoAutorizacao> especieSupressaoAutorizacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OcorrenciaArea> listarOccorrenciaAreas() {
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaArea.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return ocorrenciaAreaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<JustificativaSupressao> listarJustificativaSupressao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(JustificativaSupressao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return justificativaSupressaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoSupressao> listarTodosObjetivoSupressao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoSupressao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return objSupressaoDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método usado para listar os ObjetivoSupressao "pai", aqueles com ide_objetivo_supressao_pai = null
	 * @return List<ObjetivoSupressao>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoSupressao> listarObjetivoSupressao(){
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoSupressao.class);
		criteria.add(Restrictions.isNull("objetivoSupressao"));
		return objSupressaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarObjetivoSupressao(ObjetivoSupressao objetivoSupressao) {
		objSupressaoDAO.salvarOuAtualizar(objetivoSupressao);
	}

	/**
	 * Método uasdo para retornar os ObjetivoSupressao "filhas" de acordo com o ide_objetivo_supressao_pai passado.
	 * @param ideObjetoSupressao
	 * @return List<ObjetivoSupressao>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoSupressao> listarObjetivoSupressaoFilhaByIdeObjPai(int ideObjetoSupressao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoSupressao.class);
		criteria.add(Restrictions.eq("objetivoSupressao", ideObjetoSupressao));
		return objSupressaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ObjetivoSupressao buscarObjetivoSupressao(Integer ideObjetoSupressao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoSupressao.class);
		criteria.add(Restrictions.eq("objetivoSupressao", ideObjetoSupressao));
		return objSupressaoDAO.buscarPorCriteria(criteria);
	}

	/**
	 * Os Produtos podem ter diferentes DestinoSocioEconomicos. Por exemplo, CARVOEJAMENTO é exclusivo de LENHA.
	 * Por isso listamos os DestinoSocioeconomico por ideProduto.
	 * @param ideProduto
	 * @return
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoSocioeconomicoProduto> listarDestinoSocioEconomicoProdutoByIdeProduto(Produto ideProduto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DestinoSocioeconomicoProduto.class);
		criteria.add(Restrictions.eq("ideProduto", ideProduto));
		criteria.createAlias("ideDestinoSocioeconomico", "dest");
		return destinoSocioEconomicoProdutoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DestinoSocioeconomico buscarDestinoSocioEconomicoByIde(Integer ideDestino) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DestinoSocioeconomico.class);
		criteria.add(Restrictions.eq("ideDestinoSocioeconomico", ideDestino));
		return destinoSocioEconomicoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoVegetacao> listarClassificacaoVegetal () {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoVegetacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return classificacaoVegetacaoDAO.listarPorCriteria(criteria, Order.asc("dscClassificacaoVegetacao"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAsv (FceAsv fceAsv) {
		
		if(Util.isNullOuVazio(fceAsv.getIdeFceAsv())){
			
			fceAsvDAO.salvar(fceAsv);
			
		}else{
			fceAsvDAO.merge(fceAsv);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAsvClassiVegetacao (FceAsvClassiVegetacao fceAsvClassiVegetacao)  {
		fceAsvClassiVegetacaoDAO.salvarOuAtualizar(fceAsvClassiVegetacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoVegetacao>  listarClassificacaoVegetacaoByIdeFce(FceAsv fceAsv)  {
		List<ClassificacaoVegetacao> listaClassificacaoVegetal = new ArrayList<ClassificacaoVegetacao>();
		List<FceAsvClassiVegetacao> tmpFceAscClassiVegetacao = buscarFceAsvClassificacaoVegetacaoByIdeFceAsv(fceAsv);
		for (FceAsvClassiVegetacao fceAsvClassiVegetacao : tmpFceAscClassiVegetacao){
			listaClassificacaoVegetal.add(fceAsvClassiVegetacao.getIdeClassificacaoVegetacao());
		}
		return listaClassificacaoVegetal;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClassificacaoVegetacao buscarClassificacaoVegetacaoByIde(Integer ideClassificacaoVegetacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoVegetacao.class);
		criteria.add(Restrictions.eq("ideClassificacaoVegetacao", ideClassificacaoVegetacao));
		return classificacaoVegetacaoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAsvClassiVegetacao> buscarFceAsvClassificacaoVegetacaoByIdeFceAsv (FceAsv fceAsv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAsvClassiVegetacao.class);
		criteria.add(Restrictions.eq("ideFceAsv", fceAsv));
		criteria.setFetchMode("ideFceAsv", FetchMode.JOIN);
		criteria.setFetchMode("ideClassificacaoVegetacao", FetchMode.JOIN);
		return fceAsvClassiVegetacaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAsv buscarFceAsvByIdeFce (Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAsv.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.createAlias("ideFce", "fce");
		return fceAsvDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAsvClassiVegetacao(FceAsv fceAsv)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAsv", fceAsv);
		fceAsvClassiVegetacaoDAO.executarNamedQuery("FceAsvClassiVegetacao.removerByIdeFceAsv", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListasComFceAsv(FceAsv fceAsv, List<?> list) {
		if (!Util.isNullOuVazio(list)){
			for (Object object : list) {
				if(object instanceof JustificativaSupressao){
					FceAsvJustificativaSupressao fceAsvJustificativaSupressao = new FceAsvJustificativaSupressao(new FceAsvJustificativaSupressaoPK(fceAsv.getIdeFceAsv(), ((JustificativaSupressao) object).getIdeJustificativaSupressao()));
					fceAsvJustificativaSupressao.setIdeFceAsv(fceAsv);
					fceAsvJustificativaSupressao.setIdeJustificativaSupressao((JustificativaSupressao) object);
					salvarFceAsvAssociativas(fceAsvJustificativaSupressao);
				} else if(object instanceof ObjetivoSupressao){
					FceAsvObjetivoSupressao fceAsvObjetivoSupressao = new FceAsvObjetivoSupressao(new FceAsvObjetivoSupressaoPK(fceAsv.getIdeFceAsv(), ((ObjetivoSupressao) object).getIdeObjetoSupressao()));
					fceAsvObjetivoSupressao.setIdeFceAsv(fceAsv);
					fceAsvObjetivoSupressao.setIdeObjetivoSupressao((ObjetivoSupressao) object);
					salvarFceAsvAssociativas(fceAsvObjetivoSupressao);
				} else if(object instanceof OcorrenciaArea){
					FceAsvOcorrenciaArea fceAsvOcorrenciaArea = new FceAsvOcorrenciaArea(new FceAsvOcorrenciaAreaPK(fceAsv.getIdeFceAsv(), ((OcorrenciaArea) object).getIdeOcorrenciaArea()));
					fceAsvOcorrenciaArea.setIdeFceAsv(fceAsv);
					fceAsvOcorrenciaArea.setIdeOcorrenciaArea((OcorrenciaArea) object);
					if(fceAsvOcorrenciaArea.getIdeOcorrenciaArea().getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.ABANDONADA.getId())){
						fceAsvOcorrenciaArea.setNumArea(fceAsvOcorrenciaArea.getIdeOcorrenciaArea().getNumArea());
					} else if(fceAsvOcorrenciaArea.getIdeOcorrenciaArea().getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.INVIAVEL.getId())){
						fceAsvOcorrenciaArea.setNumArea(fceAsvOcorrenciaArea.getIdeOcorrenciaArea().getNumArea());
					}
					salvarFceAsvAssociativas(fceAsvOcorrenciaArea);
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceAsvAssociativas(Object object) {
		if (object instanceof FceAsvJustificativaSupressao){
			fceAsvJustificativaSupressaoDAO.salvarOuAtualizar((FceAsvJustificativaSupressao) object);
		} else if (object instanceof FceAsvObjetivoSupressao){
			fceAsvObjetivoSupressaoDAO.salvar((FceAsvObjetivoSupressao) object);
		} else 	if (object instanceof FceAsvOcorrenciaArea){
			fceAsvOcorrenciaAreaDAO.salvarOuAtualizar((FceAsvOcorrenciaArea) object);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaProdutoSupressao(List<ProdutoSupressao> list, FceAsv fceAsv){
		for(ProdutoSupressao produtoSupressao : list){
			if(!Util.isNullOuVazio(produtoSupressao.getIdeProdutoSupressao())){
				produtoSupressao.setIdeProdutoSupressao(null);
			}
			produtoSupressao.setDestinoSocioeconomicoProdutosBD(null);
			List<DestinoSocioeconomicoProduto> listDest = produtoSupressao.getDestinoSocioeconomicoProdutosSelecionados();
			produtoSupressao.setDestinoSocioeconomicoProdutosSelecionados(null);
			produtoSupressaoDAO.salvarOuAtualizar(produtoSupressao);
			salvarProdutoSupressaoDestino(produtoSupressao, listDest);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarProdutoSupressaoDestino(ProdutoSupressao produtoSupressao, List<DestinoSocioeconomicoProduto> list) {
		for(DestinoSocioeconomicoProduto destinoSocioeconomicoProduto : list){
			ProdutoSupressaoDestino produtoSupressaoDestino = new ProdutoSupressaoDestino(new ProdutoSupressaoDestinoPK(destinoSocioeconomicoProduto.getIdeDestinoSocioProduto(), produtoSupressao.getIdeProdutoSupressao()));
			produtoSupressaoDestino.setIdeProdutoSupressao(produtoSupressao);
			produtoSupressaoDestino.setIdeDestinoSocioeconomicoProduto(destinoSocioeconomicoProduto);
			produtoSupressaoDestinoDAO.salvarOuAtualizar(produtoSupressaoDestino);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAsvAssociativas(FceAsv fceAsv) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAsv", fceAsv);
		fceAsvJustificativaSupressaoDAO.executarNamedQuery("FceAsvJustificativaSupressao.deleteByIdeFceAsv", parameters);
		ObjetivoSupressao objetivoSupressao = armazenaObjetivoSupressaoOUTROS(fceAsv);
		fceAsvObjetivoSupressaoDAO.executarNamedQuery("FceAsvObjetivoSupressao.deleteByIdeFceAsv", parameters);
		if(!Util.isNullOuVazio(objetivoSupressao)){
			excluirObjetivoSupressaoOutro(objetivoSupressao);
		}
		fceAsvOcorrenciaAreaDAO.executarNamedQuery("FceAsvOcorrenciaArea.deleteByIdeFceAsv", parameters);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProdutosSupressaoDestinoByIdeProdutoSupressao(ProdutoSupressao produtoSupressao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProdutoSupressao", produtoSupressao);
		produtoSupressaoDestinoDAO.executarNamedQuery("ProdutoSupressaoDestino.deleteByIdeProdutoSupressao", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProdutosSupressaoByIdeFceAsv(FceAsv fceAsv) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fceAsv", fceAsv);
		produtoSupressaoDAO.executarNamedQuery("ProdutoSupressao.deleteByIdeFceAsv", parameters);
	}

	private ObjetivoSupressao armazenaObjetivoSupressaoOUTROS(FceAsv fceAsv) {
		List<FceAsvObjetivoSupressao> list = buscarObjetivoSupressaoByIdeFceAsv(fceAsv);
		for(FceAsvObjetivoSupressao asvObjetivoSupressao : list){
			if(!Util.isNullOuVazio(asvObjetivoSupressao.getIdeObjetivoSupressao().getObjetivoSupressao()) && asvObjetivoSupressao.getIdeObjetivoSupressao().getObjetivoSupressao().equals(8)){
				return asvObjetivoSupressao.getIdeObjetivoSupressao();
			}
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void excluirObjetivoSupressaoOutro(ObjetivoSupressao objetivoSupressao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideObjetoSupressao", objetivoSupressao.getIdeObjetoSupressao());
		objSupressaoDAO.executarNamedQuery("ObjetivoSupressao.deleteByIdeObjetivoSupressao", parameters);
	}

	/**
	 * Esse método é usado para calcular as áreas de APP daquele requerimento.
	 * [RN 0032 - Área total de APP a ser Suprimida, em FCE ASV, não pode ser maior que o somatório de TODAS as áreas de APPs dos imóveis do empreendimento daquele requerimento]
	 * @param requerimento
	 * @return Double areaTotalAppEmpreendimento
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double buscarAppByIdeRequerimento(Requerimento requerimento) {

		Double areaTotalAppEmpreendimento = 0.0;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", requerimento.getIdeRequerimento());
		List<App> listApp = appDAO.buscarPorNamedQuery("App.findValAreaByIdeRequerimento", parameters);
		for(App app : listApp){
			areaTotalAppEmpreendimento += app.getValArea();
		}
		return areaTotalAppEmpreendimento;
	}

	/**
	 * Método que busca as JustificativaSupressao salvas para aquela FceAsv
	 * List<JustificativaSupressao>
	 * @param fceAsv
	 * @return
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<JustificativaSupressao> buscarListaJustificativaSupressaoByIdeFceAsv(FceAsv fceAsv) {
		List<JustificativaSupressao> listaJustificativaSupressao = new ArrayList<JustificativaSupressao>();
		List<FceAsvJustificativaSupressao> lista = buscarFceJustificativaSupressaoByIdeFceAsv(fceAsv);
		for(FceAsvJustificativaSupressao asvJustificativaSupressao : lista){
			listaJustificativaSupressao.add(asvJustificativaSupressao.getIdeJustificativaSupressao());
		}
		return listaJustificativaSupressao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceAsvJustificativaSupressao> buscarFceJustificativaSupressaoByIdeFceAsv(FceAsv fceAsv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAsvJustificativaSupressao.class);
		criteria.add(Restrictions.eq("ideFceAsv", fceAsv));
		criteria.createAlias("ideFceAsv", "fceAsv");
		criteria.createAlias("ideJustificativaSupressao", "just");
		return fceAsvJustificativaSupressaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoSupressao> buscarListaObjetivoSupressaoByIdeFceAsv(FceAsv fceAsv) {
		List<ObjetivoSupressao> listaObjetivoSupressao = new ArrayList<ObjetivoSupressao>();
		List<FceAsvObjetivoSupressao> lista = buscarObjetivoSupressaoByIdeFceAsv(fceAsv);
		for(FceAsvObjetivoSupressao asvObjetivoSupressao : lista){
			listaObjetivoSupressao.add(asvObjetivoSupressao.getIdeObjetivoSupressao());
		}
		return listaObjetivoSupressao;
	}

	/**
	 * Método que retorna um ObjetivoSupressao de acordo com o seu Id.
	 * @param ide
	 * @return ObjetivoSupressao
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ObjetivoSupressao buscarObjetivoSupressaoByIde(Integer ide) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoSupressao.class);
		criteria.add(Restrictions.eq("ideObjetoSupressao", ide));
		return objSupressaoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceAsvObjetivoSupressao> buscarObjetivoSupressaoByIdeFceAsv(FceAsv fceAsv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAsvObjetivoSupressao.class);
		criteria.add(Restrictions.eq("ideFceAsv", fceAsv));
		criteria.setFetchMode("ideFceAsv", FetchMode.JOIN);
		criteria.setFetchMode("ideObjetivoSupressao", FetchMode.JOIN);
		return fceAsvObjetivoSupressaoDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método que busca as OcorrenciaArea salvas para aquela FceAsv
	 * @param fceAsv
	 * @return List<OcorrenciaArea>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OcorrenciaArea> buscarListaOcorrenciaAreaByIdeFceAsv(FceAsv fceAsv) {
		List<OcorrenciaArea> listaOcorrenciaArea = new ArrayList<OcorrenciaArea>();
		List<FceAsvOcorrenciaArea> lista = buscarOcorrenciaAreaByIdeFceAsv(fceAsv);
		for(FceAsvOcorrenciaArea asvOcorrenciaArea : lista){
			asvOcorrenciaArea.getIdeOcorrenciaArea().setNumArea(asvOcorrenciaArea.getNumArea());
			listaOcorrenciaArea.add(asvOcorrenciaArea.getIdeOcorrenciaArea());
		}
		return listaOcorrenciaArea;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceAsvOcorrenciaArea> buscarOcorrenciaAreaByIdeFceAsv(FceAsv fceAsv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAsvOcorrenciaArea.class);
		criteria.add(Restrictions.eq("ideFceAsv", fceAsv));
		criteria.setFetchMode("ideFceAsv", FetchMode.JOIN);
		criteria.setFetchMode("ideOcorrenciaArea", FetchMode.JOIN);
		return fceAsvOcorrenciaAreaDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método que retona a grid de ProdutoSupressao preenchida quando 'fceAsv.getIndMaterialLenhoso()' = true.
	 * Seta-se os Produtos e os DestinoSocioeconomicos para serem exibidos na dataTable.
	 * @param fceAsv
	 * @return List<ProdutoSupressao>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoSupressao> buscarProdutoSupressao(FceAsv fceAsv) {
		List<ProdutoSupressao> listaProdutoSupressao = buscarProdutoSupressaoByIdeFceAsv(fceAsv);
		/* Dentro da grid de ProdutoSupressao em 'asvAbaSupressao' nós temos informações presentes em várias tabelas.
		 * Para conseguirmos listar os DestinoSocioeconomico precisamos verificar que ProdutoSupressao é aquele.
		 */
		if(!Util.isNullOuVazio(listaProdutoSupressao)) {

			for(ProdutoSupressao produtoSupressao : listaProdutoSupressao){
				List<ProdutoSupressaoDestino> lista = buscarProdutoSupressaoDestinoByIdeProdutoSupressao(produtoSupressao);
				if(!Util.isNullOuVazio(lista)) {

					List<DestinoSocioeconomicoProduto> destinoSocioeconomicos = new ArrayList<DestinoSocioeconomicoProduto>();
					// Adicionamos na lista 'destinoSocioeconomicos' os DestinoSocioeconomicoProduto daquele ProdutoSupressaoDestino
					for(ProdutoSupressaoDestino supressaoDestino : lista) {
						destinoSocioeconomicos.add(supressaoDestino.getIdeDestinoSocioeconomicoProduto());
					}
					// Preenchemos os DestinoSocioecnomicoProduto daquele PordutoSupressao com a lista 'destinoSocioeconomicos'
					produtoSupressao.setDestinoSocioeconomicoProdutosSelecionados(destinoSocioeconomicos);
					// Para preenhcer o Produto daquele ProdutoSupressao basta pegar qualquer um dos produtos da lista de DestinoSocionEconomicoProduto,
					// como essa lista terá no mínimo 01 produto usamos o get(0)
					produtoSupressao.setProduto(produtoSupressao.getDestinoSocioeconomicoProdutosSelecionados().get(0).getIdeProduto());
					// Para que todos os Destinos daquele Produto sejam exibidos na tela, precisamos listar os Destinos existentes para aquele Produto.
					produtoSupressao.setDestinoSocioeconomicoProdutosBD(listarDestinoSocioEconomicoProdutoByIdeProduto(produtoSupressao.getProduto()));
					// Seta o campo transient como true para que a grid venha bloqueada.
					produtoSupressao.setDesabilitaQtd(true);
				}
			}
		}
		return listaProdutoSupressao;
	}

	/**
	 * Método que busca os ProdutoSupressao salvos para aquela FceAsv
	 * @param fceAsv
	 * @return List<ProdutoSupressao>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProdutoSupressao> buscarProdutoSupressaoByIdeFceAsv(FceAsv fceAsv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoSupressao.class);
		criteria.add(Restrictions.eq("fceAsv", fceAsv));
		criteria.setFetchMode("fceAsv", FetchMode.JOIN);
		criteria.setFetchMode("ideProdutoSupressao", FetchMode.JOIN);
		return produtoSupressaoDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método que vai na tabela associativa destino_socioeconomico_produto e busca as informções presentes nas tabelas destino_socioeconomico e produto
	 * para retornar uma lista de produto_supressao_destino carregada.
	 * @param produtoSupressao
	 * @return List<ProdutoSupressaoDestino>
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProdutoSupressaoDestino> buscarProdutoSupressaoDestinoByIdeProdutoSupressao(ProdutoSupressao produtoSupressao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoSupressaoDestino.class);
		criteria.add(Restrictions.eq("ideProdutoSupressao", produtoSupressao));
		criteria.setFetchMode("ideProdutoSupressao", FetchMode.JOIN);
		criteria.setFetchMode("ideDestinoSocioeconomicoProduto", FetchMode.JOIN);
		criteria.createAlias("ideDestinoSocioeconomicoProduto", "destProd");
		criteria.createAlias("destProd.ideProduto", "prod");
		criteria.createAlias("destProd.ideDestinoSocioeconomico", "destSocio");
		return produtoSupressaoDestinoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(AsvDadosGeraisController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieSupressao> listarEspecieSupressao()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return especieSupressaoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieSupressao especieSupressao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NomePopularEspecie.class);
		
		criteria.createAlias("especieSupressaoNomePopularEspecieCollection", "esnp", JoinType.INNER_JOIN)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideNomePopularEspecie"), "ideNomePopularEspecie")
				.add(Projections.property("nomPopularEspecie"), "nomPopularEspecie"))
			.add(Restrictions.eq("esnp.especieSupressaoNomePopularEspeciePK.ideEspecieSupressao.ideEspecieSupressao", especieSupressao.getIdeEspecieSupressao()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NomePopularEspecie.class));
		
		return nomePopularEspecieDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoSocioeconomico> listarDestinoSocioeconomico() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DestinoSocioeconomico.class);
		return destinoSocioeconomicoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean ajustarRetornoEspecie(AsvSupressaoController ctrl) {
		
		if (!Util.isNullOuVazio(ctrl.getEspecieSupressaoSelecionada())){
							
				if (Util.isNullOuVazio(ctrl.getListaEspecieSupressaoAutorizacao())){
					ctrl.setListaEspecieSupressaoAutorizacao(new ArrayList<EspecieSupressaoAutorizacao>());
				}
				
				EspecieSupressaoAutorizacao especieSupressaoAutorizacao = new EspecieSupressaoAutorizacao();
				
				if (!Util.isNullOuVazio(ctrl.getNomePopularEspecieSelecionada().getIdeNomePopularEspecie())){
					especieSupressaoAutorizacao.setIdeNomePopularEspecie(ctrl.getNomePopularEspecieSelecionada());
				}
				
				especieSupressaoAutorizacao.setIdeEspecieSupressao(ctrl.getEspecieSupressaoSelecionada());
				especieSupressaoAutorizacao.setFceAsv(ctrl.getFceAsv());
				especieSupressaoAutorizacao.setIdeProduto(ctrl.getProdutoSelecionadoSupressao());
				
				removerListaEspecieSupressaoAll(ctrl.getListaEspecieSupressaoAll(), ctrl.getEspecieSupressaoSelecionada(), ctrl.getNomePopularEspecieSelecionada());
				
				ctrl.getListaEspecieSupressaoAutorizacao().add(especieSupressaoAutorizacao);
				ctrl.setEspecieSupressaoSelecionada(null);
				ctrl.setProdutoSelecionadoSupressao(null);
				return true;

		}else if(!Util.isNullOuVazio(ctrl.getProdutoSelecionadoSupressao())){
			if(ctrl.getProdutoSelecionadoSupressao().getDscProduto().equalsIgnoreCase("Lenha")){
				
				EspecieSupressaoAutorizacao especieSupressaoAutorizacao = new EspecieSupressaoAutorizacao();
				
				especieSupressaoAutorizacao.setFceAsv(ctrl.getFceAsv());
				especieSupressaoAutorizacao.setIdeProduto(ctrl.getProdutoSelecionadoSupressao());
				
				if(Util.isNull(ctrl.getListaEspecieSupressaoAutorizacao())){
					ctrl.setListaEspecieSupressaoAutorizacao(new ArrayList<EspecieSupressaoAutorizacao>());
				}
				ctrl.getListaEspecieSupressaoAutorizacao().add(especieSupressaoAutorizacao);
				
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean ajustarRetornoEspecie(List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao, EspecieSupressao especieSupressaoSelecionada, 
			NomePopularEspecie nomePopularEspecieSelecionada, FceAsv fceAsv,List<EspecieSupressao> listaEspecieSupressao, List<EspecieSupressao> listaEspecieSupressaoAll)  {
		
		if (!Util.isNullOuVazio(especieSupressaoSelecionada)){
			
			boolean verificarSeEspecieJaAdd = verificarSeEspecieJaAdd(listaEspecieSupressaoAutorizacao, especieSupressaoSelecionada, nomePopularEspecieSelecionada);
			if (!verificarSeEspecieJaAdd){
				
				if (Util.isNullOuVazio(listaEspecieSupressaoAutorizacao)){
					listaEspecieSupressaoAutorizacao = new ArrayList<EspecieSupressaoAutorizacao>();
				}
				
				EspecieSupressaoAutorizacao especieSupressaoAutorizacao = new EspecieSupressaoAutorizacao();
				
				if (!Util.isNullOuVazio(nomePopularEspecieSelecionada.getIdeNomePopularEspecie())){
					especieSupressaoAutorizacao.setIdeNomePopularEspecie(nomePopularEspecieSelecionada);
				}
				
				especieSupressaoAutorizacao.setIdeEspecieSupressao(especieSupressaoSelecionada);
				especieSupressaoAutorizacao.setFceAsv(fceAsv);
				
				removerListaEspecieSupressaoAll(listaEspecieSupressao, especieSupressaoSelecionada, nomePopularEspecieSelecionada);
				removerListaEspecieSupressaoAll(listaEspecieSupressaoAll, especieSupressaoSelecionada, nomePopularEspecieSelecionada);
				
				listaEspecieSupressaoAutorizacao.add(especieSupressaoAutorizacao);
				
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarSeEspecieJaAdd(List<EspecieSupressaoAutorizacao> lista, EspecieSupressao especieSupressao, NomePopularEspecie nomePopularEspecie) {
		if (!Util.isNullOuVazio(lista)){
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : lista) {
				if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeNomePopularEspecie())) {
					if (especieSupressaoAutorizacao.getIdeNomePopularEspecie().getIdeNomePopularEspecie().equals(nomePopularEspecie.getIdeNomePopularEspecie()) &&
							especieSupressaoAutorizacao.getIdeEspecieSupressao().getIdeEspecieSupressao().equals(especieSupressao.getIdeEspecieSupressao())){
						return true;
					}
				} else {
					
					if(Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeProduto()) || !especieSupressaoAutorizacao.getIdeProduto().getDscProduto().equalsIgnoreCase("Lenha")){
						if (especieSupressaoAutorizacao.getIdeEspecieSupressao().getIdeEspecieSupressao().equals(especieSupressao.getIdeEspecieSupressao())){
							return true;
						}
					}
					
				}
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removerListaEspecieSupressaoAll(List<EspecieSupressao> lista, EspecieSupressao especieSupressao, NomePopularEspecie nomePopularEspecie)  {
		
		List<NomePopularEspecie> listarNomePopularEspecie = listarNomePopularEspecie(especieSupressao);
		
		if (!Util.isNullOuVazio(listarNomePopularEspecie)){
			Integer posicao = lista.indexOf(especieSupressao);
			
			if (posicao != -1) {
				EspecieSupressao es = lista.get(posicao);
				if (Util.isNullOuVazio(es.getRemovidosNomePopularEspecie())){
					es.setRemovidosNomePopularEspecie(new ArrayList<NomePopularEspecie>());
				}
				
				if (!es.getRemovidosNomePopularEspecie().contains(nomePopularEspecie)){
					if ((es.getRemovidosNomePopularEspecie().size() + 1) == listarNomePopularEspecie.size()){
						lista.remove(es);
					} 
					es.getRemovidosNomePopularEspecie().add(nomePopularEspecie) ;
				}
			}
		} else {
			if (lista.contains(especieSupressao)){
				lista.remove(especieSupressao);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieSupressao> pesquisarEspecieSupressao(List<EspecieSupressao> listaEspecieSupressao, List<EspecieSupressao> listaEspecieSupressaoAll, String nomEspecieSupressao){
		listaEspecieSupressao = new ArrayList<EspecieSupressao>();
		for (EspecieSupressao tipoClassificacao : listaEspecieSupressaoAll) {
			if(tipoClassificacao.getNomEspecieSupressao().toLowerCase().indexOf(nomEspecieSupressao.toLowerCase())!= -1){
				listaEspecieSupressao.add(tipoClassificacao);
			}
		}
		
		return listaEspecieSupressao;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEspecie(EspecieSupressaoAutorizacao especieSupressaoAutorizacao, List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao) {
		if (!Util.isNullOuVazio(especieSupressaoAutorizacao)) { 
			if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeEspecieSupressaoAutorizacao())){
				removerEspecieSupressaoAutDestinoSocioEconomico(especieSupressaoAutorizacao);
				especieSupressaoAutorizacaoDAO.remover(especieSupressaoAutorizacao);
			}
			
			List<NomePopularEspecie> removidosNomePopularEspecie = new ArrayList<NomePopularEspecie>();
			
			if(!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeEspecieSupressao())){
				removidosNomePopularEspecie = especieSupressaoAutorizacao.getIdeEspecieSupressao().getRemovidosNomePopularEspecie();
			}
			
			if (!Util.isNullOuVazio(removidosNomePopularEspecie) && removidosNomePopularEspecie.contains(especieSupressaoAutorizacao.getIdeNomePopularEspecie())){
					removidosNomePopularEspecie.remove(especieSupressaoAutorizacao.getIdeNomePopularEspecie());
				
			}
			
			if (listaEspecieSupressaoAutorizacao.contains(especieSupressaoAutorizacao)){
				int index = listaEspecieSupressaoAutorizacao.indexOf(especieSupressaoAutorizacao);
				listaEspecieSupressaoAutorizacao.remove(index);

			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void adicionarEspecie(EspecieSupressaoAutorizacao especieSupressaoAutorizacao, List<EspecieSupressao> listaEspecieSupressao,	List<EspecieSupressao> listaEspecieSupressaoAll) {
		if (!listaEspecieSupressao.contains(especieSupressaoAutorizacao.getIdeEspecieSupressao())) {
			listaEspecieSupressao.add(especieSupressaoAutorizacao.getIdeEspecieSupressao());
			listaEspecieSupressaoAll.add(especieSupressaoAutorizacao.getIdeEspecieSupressao());
			Collections.sort(listaEspecieSupressao);
			Collections.sort(listaEspecieSupressaoAll);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaEspecieSupressaoAutorizacao(List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao)  {
		
		especieSupressaoAutorizacaoDAO.salvarEmLote(listaEspecieSupressaoAutorizacao);
		
		for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : listaEspecieSupressaoAutorizacao) {
			removerEspecieSupressaoAutDestinoSocioEconomico(especieSupressaoAutorizacao);
			for (DestinoSocioeconomico destinoSocioeconomico : especieSupressaoAutorizacao.getListaDestinoSocioeconomicoSelecionado()) {
				EspecieSupressaoAutDestinoSocioEconomico especieSupressaoAutDestinoSocioEconomico = new EspecieSupressaoAutDestinoSocioEconomico();
				especieSupressaoAutDestinoSocioEconomico.setIdeDestinoSocioeconomico(destinoSocioeconomico);
				especieSupressaoAutDestinoSocioEconomico.setIdeEspecieSupressaoAutorizacao(especieSupressaoAutorizacao);
				especieSupressaoAutDestinoSocioEconomicoDAO.salvarOuAtualizar(especieSupressaoAutDestinoSocioEconomico);
			}
		}
	}
	
	public void salvarEspecieSupressaoAutorizacao(EspecieSupressaoAutorizacao especieSupressaoAutorizacao) {
		
		especieSupressaoAutorizacaoDAO.salvarOuAtualizar(especieSupressaoAutorizacao);
	}
	
	public void removerEspecieSupressaoAutDestinoSocioEconomico(EspecieSupressaoAutorizacao especieSupressaoAutorizacao) {
		List<EspecieSupressaoAutDestinoSocioEconomico> lista = listarEspSuprAutDestSocioEconPorEspSuprAut(especieSupressaoAutorizacao);
		for (EspecieSupressaoAutDestinoSocioEconomico especieSupressaoAutDestinoSocioEconomico : lista) {
			especieSupressaoAutDestinoSocioEconomicoDAO.remover(especieSupressaoAutDestinoSocioEconomico);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao(FceAsv fceAsv)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressaoAutorizacao.class);
		
		criteria
			.createAlias("ideNomePopularEspecie", "nple", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEspecieSupressao", "essu", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProduto", "produto", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fceAsv", "fceAsv", JoinType.INNER_JOIN)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEspecieSupressaoAutorizacao"), "ideEspecieSupressaoAutorizacao")
				.add(Projections.property("fceAsv.ideFceAsv"), "fceAsv.ideFceAsv")
				.add(Projections.property("volumeTotalForaApp"), "volumeTotalForaApp")
				.add(Projections.property("volumeTotalEmApp"), "volumeTotalEmApp")
				.add(Projections.property("nple.ideNomePopularEspecie"), "ideNomePopularEspecie.ideNomePopularEspecie")
				.add(Projections.property("nple.nomPopularEspecie"), "ideNomePopularEspecie.nomPopularEspecie")
				.add(Projections.property("essu.ideEspecieSupressao"), "ideEspecieSupressao.ideEspecieSupressao")
				.add(Projections.property("essu.nomEspecieSupressao"), "ideEspecieSupressao.nomEspecieSupressao")
				.add(Projections.property("produto.ideProduto"), "ideProduto.ideProduto")
				.add(Projections.property("produto.dscProduto"), "ideProduto.dscProduto"))
				
			.add(Restrictions.eq("fceAsv.ideFceAsv", fceAsv.getIdeFceAsv()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutorizacao.class));
		List<EspecieSupressaoAutorizacao> lista = especieSupressaoAutorizacaoDAO.listarPorCriteria(criteria);
		
		for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : lista) {
			List<EspecieSupressaoAutDestinoSocioEconomico> listaEspecieSupressaoAutDestinoSocioEconomico = listarEspSuprAutDestSocioEconPorEspSuprAut(especieSupressaoAutorizacao);
			especieSupressaoAutorizacao.setListaDestinoSocioeconomicoSelecionado(new ArrayList<DestinoSocioeconomico>());
			for (EspecieSupressaoAutDestinoSocioEconomico especieSupressaoAutDestinoSocioEconomico : listaEspecieSupressaoAutDestinoSocioEconomico) {
				especieSupressaoAutorizacao.getListaDestinoSocioeconomicoSelecionado().add(especieSupressaoAutDestinoSocioEconomico.getIdeDestinoSocioeconomico());
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieSupressaoAutDestinoSocioEconomico> listarEspSuprAutDestSocioEconPorEspSuprAut(EspecieSupressaoAutorizacao especieSupressaoAutorizacao)  {
		DetachedCriteria criteriaEsadse = DetachedCriteria.forClass(EspecieSupressaoAutDestinoSocioEconomico.class)
				.createAlias("ideDestinoSocioeconomico", "ideDestinoSocioeconomico", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideEspecieSupressaoAutDestinoSocioEconomico"), "ideEspecieSupressaoAutDestinoSocioEconomico")
					.add(Projections.property("ideDestinoSocioeconomico.ideDestinoSocioeconomico"), "ideDestinoSocioeconomico.ideDestinoSocioeconomico")
					.add(Projections.property("ideDestinoSocioeconomico.dscDestinoSocioeconomico"), "ideDestinoSocioeconomico.dscDestinoSocioeconomico"))
				.add(Restrictions.eq("ideEspecieSupressaoAutorizacao.ideEspecieSupressaoAutorizacao", especieSupressaoAutorizacao.getIdeEspecieSupressaoAutorizacao()))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutDestinoSocioEconomico.class));
		
		return especieSupressaoAutDestinoSocioEconomicoDAO.listarPorCriteria(criteriaEsadse);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removerListaEspecieSupressaoEdicao(AsvSupressaoController ctrl) {
		for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : ctrl.getListaEspecieSupressaoAutorizacao()) {
			if(!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeEspecieSupressao())){
				
				removerListaEspecieSupressaoAll(ctrl.getListaEspecieSupressao(), especieSupressaoAutorizacao.getIdeEspecieSupressao(), especieSupressaoAutorizacao.getIdeNomePopularEspecie());
				removerListaEspecieSupressaoAll(ctrl.getListaEspecieSupressaoAll(), especieSupressaoAutorizacao.getIdeEspecieSupressao(), especieSupressaoAutorizacao.getIdeNomePopularEspecie());
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removerListaEspecieSupressaoEdicao(List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao, List<EspecieSupressao> listaEspecieSupressao, List<EspecieSupressao> listaEspecieSupressaoAll)  {
		
		if (!Util.isNullOuVazio(listaEspecieSupressaoAutorizacao)){
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : listaEspecieSupressaoAutorizacao) {
				if(!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeEspecieSupressao())){
					
					removerListaEspecieSupressaoAll(listaEspecieSupressao, especieSupressaoAutorizacao.getIdeEspecieSupressao(), especieSupressaoAutorizacao.getIdeNomePopularEspecie());
					removerListaEspecieSupressaoAll(listaEspecieSupressaoAll, especieSupressaoAutorizacao.getIdeEspecieSupressao(), especieSupressaoAutorizacao.getIdeNomePopularEspecie());
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirObjetivoSupressao(FceAsv fceAsv)  {
		List<FceAsvObjetivoSupressao> lista = buscarObjetivoSupressaoByIdeFceAsv(fceAsv);
		
		for (FceAsvObjetivoSupressao fceAsvObjetivoSupressao : lista) {
			fceAsvObjetivoSupressaoDAO.remover(fceAsvObjetivoSupressao);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerListaEspecieSupressaoAutorizacao (List<DadoConcedidoDTO> listaAutorizacaoManejoCabrucaDTO)  {
		for (DadoConcedidoDTO dadoConcedidoDTO : listaAutorizacaoManejoCabrucaDTO) {
			for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : dadoConcedidoDTO.getListaEspecieSupressaoAutorizacao()) {
				if (!Util.isNullOuVazio(especieSupressaoAutorizacao.getIdeEspecieSupressaoAutorizacao())){
					removerEspecieSupressaoAutDestinoSocioEconomico(especieSupressaoAutorizacao);
					especieSupressaoAutorizacaoDAO.remover(especieSupressaoAutorizacao);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacaoPorProcessoAtoConcedido(ProcessoAtoConcedido ideProcessoAtoConcedido, Imovel imovel)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressaoAutorizacao.class);
		
		criteria
			.createAlias("ideNomePopularEspecie", "nple", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEspecieSupressao", "essu", JoinType.INNER_JOIN)
			.createAlias("ideProcessoAtoConcedido", "pac", JoinType.INNER_JOIN)
			.createAlias("fceAsv", "fceAsv", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEspecieSupressaoAutorizacao"), "ideEspecieSupressaoAutorizacao")
				.add(Projections.property("fceAsv.ideFceAsv"), "fceAsv.ideFceAsv")
				.add(Projections.property("volumeTotalForaApp"), "volumeTotalForaApp")
				.add(Projections.property("volumeTotalEmApp"), "volumeTotalEmApp")
				.add(Projections.property("nple.ideNomePopularEspecie"), "ideNomePopularEspecie.ideNomePopularEspecie")
				.add(Projections.property("nple.nomPopularEspecie"), "ideNomePopularEspecie.nomPopularEspecie")
				.add(Projections.property("essu.ideEspecieSupressao"), "ideEspecieSupressao.ideEspecieSupressao")
				.add(Projections.property("essu.nomEspecieSupressao"), "ideEspecieSupressao.nomEspecieSupressao"))
				
			.add(Restrictions.eq("ideProcessoAtoConcedido.ideProcessoAtoConcedido", ideProcessoAtoConcedido.getIdeProcessoAtoConcedido()))
			.add(Restrictions.eq("pac.ideImovel.ideImovel", imovel.getIdeImovel()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutorizacao.class));
		List<EspecieSupressaoAutorizacao> lista = especieSupressaoAutorizacaoDAO.listarPorCriteria(criteria);
		
		for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : lista) {
			List<EspecieSupressaoAutDestinoSocioEconomico> listaEspecieSupressaoAutDestinoSocioEconomico = listarEspSuprAutDestSocioEconPorEspSuprAut(especieSupressaoAutorizacao);
			especieSupressaoAutorizacao.setListaDestinoSocioeconomicoSelecionado(new ArrayList<DestinoSocioeconomico>());
			for (EspecieSupressaoAutDestinoSocioEconomico especieSupressaoAutDestinoSocioEconomico : listaEspecieSupressaoAutDestinoSocioEconomico) {
				especieSupressaoAutorizacao.getListaDestinoSocioeconomicoSelecionado().add(especieSupressaoAutDestinoSocioEconomico.getIdeDestinoSocioeconomico());
			}
		}
		
		return lista;
	}

	public List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacaoPorRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieSupressaoAutorizacao.class);
		
		criteria
			.createAlias("fceAsv", "asv", JoinType.INNER_JOIN)
			.createAlias("asv.ideFce", "f", JoinType.INNER_JOIN)
			.createAlias("f.ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("ideNomePopularEspecie", "nple", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEspecieSupressao", "essu", JoinType.INNER_JOIN)
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideEspecieSupressaoAutorizacao"), "ideEspecieSupressaoAutorizacao")
					.add(Projections.property("fceAsv.ideFceAsv"), "fceAsv.ideFceAsv")
					.add(Projections.property("volumeTotalForaApp"), "volumeTotalForaApp")
					.add(Projections.property("volumeTotalEmApp"), "volumeTotalEmApp")
					.add(Projections.property("nple.ideNomePopularEspecie"), "ideNomePopularEspecie.ideNomePopularEspecie")
					.add(Projections.property("nple.nomPopularEspecie"), "ideNomePopularEspecie.nomPopularEspecie")
					.add(Projections.property("essu.ideEspecieSupressao"), "ideEspecieSupressao.ideEspecieSupressao")
					.add(Projections.property("essu.nomEspecieSupressao"), "ideEspecieSupressao.nomEspecieSupressao"))
					
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieSupressaoAutorizacao.class));
		

		List<EspecieSupressaoAutorizacao> lista = especieSupressaoAutorizacaoDAO.listarPorCriteria(criteria);
		
		for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : lista) {
			List<EspecieSupressaoAutDestinoSocioEconomico> listaEspecieSupressaoAutDestinoSocioEconomico = listarEspSuprAutDestSocioEconPorEspSuprAut(especieSupressaoAutorizacao);
			especieSupressaoAutorizacao.setListaDestinoSocioeconomicoSelecionado(new ArrayList<DestinoSocioeconomico>());
			for (EspecieSupressaoAutDestinoSocioEconomico especieSupressaoAutDestinoSocioEconomico : listaEspecieSupressaoAutDestinoSocioEconomico) {
				especieSupressaoAutorizacao.getListaDestinoSocioeconomicoSelecionado().add(especieSupressaoAutDestinoSocioEconomico.getIdeDestinoSocioeconomico());
			}
		}
		return lista;
	}
}