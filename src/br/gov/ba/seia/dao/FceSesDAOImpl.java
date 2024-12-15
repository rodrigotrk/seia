package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceSes;
import br.gov.ba.seia.entity.FceSesCaracteristicaLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesDadosElevatoria;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTipoComposicao;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTratamentoEsgoto;
import br.gov.ba.seia.entity.FceSesElevatoriaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class FceSesDAOImpl {

	@Inject
	IDAO<FceSes> fceSesIDao;
	
	@Inject
	IDAO<FceSesDadosElevatoria> fceSesDadosElevatoriaIDao;
	
	@Inject
	IDAO<FceSesDadosEstacaoTipoComposicao> fceSesDadosEstacaoTipoComposicaoIDao;
	
	@Inject
	IDAO<FceSesElevatoriaLocalizacaoGeografica> fceSesLocalizacaoGeograficaElevatoriaIDAO;
		
	@Inject
	IDAO<FceSesDadosEstacaoTratamentoEsgoto> fceSesDadosEstacaoTratamentoEsgotoIDAO;
	
	@Inject
	IDAO<FceSesElevatoriaLocalizacaoGeografica> fceSesElevatoriaLocalizacaoGeograficaIDAO;
	
	@Inject
	IDAO<FceSesCoordenadasLancamento> fceSesCoordenadasLancamentoIDAO;
	
	@Inject
	IDAO<FceSesCaracteristicaLancamento> fceSesCaracteristicaLancamentoIDAO;
	
	@Inject
	IDAO<FceSesCoordenadasLancamentoLocalizacaoGeografica> fceSesCoordenadasLancamentoLocalizacaoGeograficaIDAO;
	
	@Inject
	LocalizacaoGeograficaService localizacaoGeograficaService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSes(FceSes fceSes) {
		fceSesIDao.salvarOuAtualizar(fceSes);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamento(FceSesCoordenadasLancamento fceSesCoordenadasLancamento) {
		fceSesCoordenadasLancamentoIDAO.salvarOuAtualizar(fceSesCoordenadasLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCaracteristicaLancamento(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamento) {
		fceSesCaracteristicaLancamentoIDAO.salvarOuAtualizar(fceSesCaracteristicaLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesCaracteristicaLancamento(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesLancamento)  {
		String sql = "delete from FceSesCaracteristicaLancamento fscl where fscl.ideCoordenadaFceLancamentoLocalizacaoGeografica.ideFceSesLancamentoLocalizacaoGeografica = :ideFceSesLancamentoLocalizacaoGeografica";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesLancamentoLocalizacaoGeografica", fceSesLancamento.getIdeFceSesLancamentoLocalizacaoGeografica());
		
		fceSesLocalizacaoGeograficaElevatoriaIDAO.executarQuery(sql, params);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesElevatoriaLocalizacaoGeografica(FceSesElevatoriaLocalizacaoGeografica localizacaoElevatoria)  {
		fceSesLocalizacaoGeograficaElevatoriaIDAO.salvarOuAtualizar(localizacaoElevatoria);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoBruta(FceSesElevatoriaLocalizacaoGeografica localizacaoElevatoria)  {
		String sql = "delete from FceSesElevatoriaLocalizacaoGeografica localizacao where localizacao.ideFceSesElevatoriaLocalizacaoGeografica= :ideFceSesElevatoriaLocalizacaoGeografica";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesElevatoriaLocalizacaoGeografica", localizacaoElevatoria.getIdeFceSesElevatoriaLocalizacaoGeografica());
		
		fceSesLocalizacaoGeograficaElevatoriaIDAO.executarQuery(sql, params);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSes.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceSesIDao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesElevatoriaLocalizacaoGeografica> listarLocalizacaoElevatoriaByIdeFceSes(
			FceSes fceSes)   {
		
		List<FceSesElevatoriaLocalizacaoGeografica> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesElevatoriaLocalizacaoGeografica.class,"localizacaoElevatoria");
		
		criteria
			.createAlias("localizacaoElevatoria.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSes.ideFceSaa", fceSes.getIdeFceSes()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("localizacaoElevatoria.ideFceSesElevatoriaLocalizacaoGeografica"), "ideFceSesElevatoriaLocalizacaoGeografica")
				.add(Projections.property("localizacaoElevatoria.ideFceSes.ideFceSes"), "ideFceSes.ideFceSes")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesElevatoriaLocalizacaoGeografica.class));
		
		list = fceSesLocalizacaoGeograficaElevatoriaIDAO.listarPorCriteria(criteria, Order.asc("ideFceSes.ideFceSes"));
		
		for(FceSesElevatoriaLocalizacaoGeografica locElevatoria : list){
			locElevatoria.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locElevatoria.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesCoordenadasLancamento buscarFceSesCoordenadasLancamentoByIdeFceSes(FceSes fceSes){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesCoordenadasLancamento.class);
		criteria.add(Restrictions.eq("ideFceSes.ideFceSes", fceSes.getIdeFceSes()));
		return fceSesCoordenadasLancamentoIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosElevatoria(
			FceSesDadosElevatoria dadosElevatoria)  {
		fceSesDadosElevatoriaIDao.salvarOuAtualizar(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTratamentoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento)  {
		fceSesDadosEstacaoTratamentoEsgotoIDAO.salvarOuAtualizar(dadosTratamento);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTratamentoEsgoto(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento)  {
		String sql = "delete from FceSesDadosEstacaoTratamentoEsgoto localizacao where localizacao.ideFceSesDadosTramentoEsgoto= :ideFceSesDadosTramentoEsgoto";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesDadosTramentoEsgoto", localizacaoTratamento.getIdeFceSesDadosTramentoEsgoto());
		
		fceSesDadosEstacaoTratamentoEsgotoIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesElevatoriaLocalizacaoGeografica(FceSesDadosElevatoria localizacaoElevatoria)  {
		String sql = "delete from FceSesElevatoriaLocalizacaoGeografica localizacao where localizacao.ideFceSesDadosElevatoria.ideFceSesDadosElevatoria= :ideFceSesDadosElevatoria";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesDadosElevatoria", localizacaoElevatoria.getIdeFceSesDadosElevatoria());
		
		fceSesLocalizacaoGeograficaElevatoriaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCoordenadasLancamentoLocalizacaoGeografica(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamento) {
		
		String sql = "delete from FceSesCoordenadasLancamentoLocalizacaoGeografica localizacao where localizacao.ideFceSesLancamentoLocalizacaoGeografica= :ideFceSesLancamentoLocalizacaoGeografica";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesLancamentoLocalizacaoGeografica", fceSesCoordenadasLancamento.getIdeFceSesLancamentoLocalizacaoGeografica());
		
		fceSesCoordenadasLancamentoLocalizacaoGeograficaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoria(FceSesDadosElevatoria dadosElevatoria)  {
		fceSesDadosElevatoriaIDao.remover(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoriaImpl(FceSesDadosElevatoria dadosElevatoria)  {
		String sql = "delete from FceSesDadosElevatoria elevatoria where elevatoria.ideFceSesDadosElevatoria = :ideFceSesDadosElevatoria";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesDadosElevatoria", dadosElevatoria.getIdeFceSesDadosElevatoria());
		
		fceSesLocalizacaoGeograficaElevatoriaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTratamentoEsgoto> listarlocalizacaoTratamentoByIdeFceSes(
			FceSes fceSes)   {
		
		List<FceSesDadosEstacaoTratamentoEsgoto> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosEstacaoTratamentoEsgoto.class,"localizacaoTratamento");
		
		criteria
			.createAlias("localizacaoTratamento.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSes.ideFceSaa", fceSes.getIdeFceSes()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("localizacaoTratamento.ideFceSesDadosTramentoEsgoto"), "ideFceSesDadosTramentoEsgoto")
				.add(Projections.property("localizacaoTratamento.ideFceSes.ideFceSes"), "ideFceSes.ideFceSes")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesDadosEstacaoTratamentoEsgoto.class));
		
		list = fceSesDadosEstacaoTratamentoEsgotoIDAO.listarPorCriteria(criteria, Order.asc("ideFceSes.ideFceSes"));
		
		for(FceSesDadosEstacaoTratamentoEsgoto locElevatoria : list){
			locElevatoria.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locElevatoria.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTipoComposicao(
			FceSesDadosEstacaoTipoComposicao composicao)  {
		
		fceSesDadosEstacaoTipoComposicaoIDao.salvarOuAtualizar(composicao);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> buscarComposicoesSelecionadas(FceSesDadosEstacaoTratamentoEsgoto dadosTratamento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosEstacaoTipoComposicao.class);
		
		criteria.add(Restrictions.eq("ideFceSesDadosEstacaoTratamentoEsgoto", dadosTratamento.getIdeFceSesDadosTramentoEsgoto()));
		
		return fceSesDadosEstacaoTipoComposicaoIDao.listarPorCriteria(criteria, Order.asc("ideFceSesDadosEstacaoTratamentoEsgoto"));
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesElevatoriaLocalizacaoGeografica> buscarLocalizacaoElevatoria(FceSesDadosElevatoria dados)   {
		
		List<FceSesElevatoriaLocalizacaoGeografica> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesElevatoriaLocalizacaoGeografica.class,"localizacaoElevatoria");
		
		criteria
			.createAlias("localizacaoElevatoria.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideClassificacaoSecao", "classificacaoSecao",JoinType.INNER_JOIN)
			.add(Restrictions.eq("localizacaoElevatoria.ideFceSesDadosElevatoria.ideFceSesDadosElevatoria", dados.getIdeFceSesDadosElevatoria()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("localizacaoElevatoria.ideFceSesElevatoriaLocalizacaoGeografica"), "ideFceSesElevatoriaLocalizacaoGeografica")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("classificacaoSecao.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
				.add(Projections.property("localizacaoElevatoria.indExtravasamento"), "indExtravasamento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesElevatoriaLocalizacaoGeografica.class));
		
		list = fceSesElevatoriaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria,Order.desc("ideFceSesElevatoriaLocalizacaoGeografica"));
		
		for(FceSesElevatoriaLocalizacaoGeografica locElevatoria : list){
			locElevatoria.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locElevatoria.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica coordenadaLocalizacao)  {
		
		fceSesCoordenadasLancamentoLocalizacaoGeograficaIDAO.salvarOuAtualizar(coordenadaLocalizacao);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCaracteristicaLancamento> buscarFceCaracteristicasLancamento(FceSesCoordenadasLancamento fceSesCoordenadaLancamento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesCaracteristicaLancamento.class,"caracteristica");
		
		criteria
			.createAlias("caracteristica.ideCaracteristicaEfluente", "efluente",JoinType.INNER_JOIN)
			.createAlias("caracteristica.ideCoordenadaFceLancamento", "lancamento",JoinType.INNER_JOIN)
			.add(Restrictions.eq("lancamento.ideFceSesCoordenadasLancamento", fceSesCoordenadaLancamento.getIdeFceSesCoordenadasLancamento()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("caracteristica.ideFceSesCaracteristicaLancamento"),"ideFceSesCaracteristicaLancamento")
				.add(Projections.property("caracteristica.valorEfluenteTratado"),"valorEfluenteTratado")
				.add(Projections.property("caracteristica.ValorEficienciRemocao"),"ValorEficienciRemocao")
				.add(Projections.property("caracteristica.valorBrutoEfluente"),"valorBrutoEfluente")
				.add(Projections.property("lancamento.ideFceSesCoordenadasLancamento"),"ideCoordenadaFceLancamento.ideFceSesCoordenadasLancamento")
				.add(Projections.property("efluente.ideCaracteristicaEfluente"),"ideCaracteristicaEfluente.ideCaracteristicaEfluente")
				.add(Projections.property("efluente.nomCaracteristicaEfluente"),"ideCaracteristicaEfluente.nomCaracteristicaEfluente")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesCaracteristicaLancamento.class));
		
		return fceSesCaracteristicaLancamentoIDAO.listarPorCriteria(criteria, Order.asc("ideFceSesCaracteristicaLancamento"));
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCoordenadasLancamentoLocalizacaoGeografica> buscarfceSesCoordenadasLancamentoLocalizacaoGeografica(FceSes fceSes)   {
		
		List<FceSesCoordenadasLancamentoLocalizacaoGeografica> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesCoordenadasLancamentoLocalizacaoGeografica.class,"localizacaoCoordenada");
		
		criteria
			.createAlias("localizacaoCoordenada.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacaoCoordenada.ideFceSes", "ideFceSes",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.createAlias("localizacaoCoordenada.ideTipoDerivacao", "tipoPeriodo",JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("localizacaoCoordenada.ideFceSes.ideFceSes", fceSes.getIdeFceSes()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("localizacaoCoordenada.ideFceSesLancamentoLocalizacaoGeografica"), "ideFceSesLancamentoLocalizacaoGeografica")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("localizacaoCoordenada.nomeCoporHidrico"), "nomeCoporHidrico")
				.add(Projections.property("localizacaoCoordenada.numPortaria"), "numPortaria")
				.add(Projections.property("localizacaoCoordenada.valorVazaoMedia"), "valorVazaoMedia")
				.add(Projections.property("localizacaoCoordenada.ideFceSes.ideFceSes"), "ideFceSes.ideFceSes")
				.add(Projections.property("tipoPeriodo.ideTipoPeriodoDerivacao"), "ideTipoDerivacao.ideTipoPeriodoDerivacao")
				.add(Projections.property("tipoPeriodo.dscTipoPeriodoDerivacao"), "ideTipoDerivacao.dscTipoPeriodoDerivacao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesCoordenadasLancamentoLocalizacaoGeografica.class));
		
		list = fceSesCoordenadasLancamentoLocalizacaoGeograficaIDAO.listarPorCriteria(criteria,Order.desc("ideFceSesLancamentoLocalizacaoGeografica"));
		
		for(FceSesCoordenadasLancamentoLocalizacaoGeografica locCoordenada : list){
			locCoordenada.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locCoordenada.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosElevatoria> listarDadosElevatoria(FceSes fceSes)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosElevatoria.class,"dadosElevatoria");
		
		criteria.add(Restrictions.eq("dadosElevatoria.ideFceSes.ideFceSes",fceSes.getIdeFceSes()));
		
		return fceSesDadosElevatoriaIDao.listarPorCriteria(criteria, Order.desc("ideFceSesDadosElevatoria"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTratamentoEsgoto> listarDadosEstacaoEsgoto(
			FceSes fceSes)   {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosEstacaoTratamentoEsgoto.class,"dadostratamento");
		
		criteria.add(Restrictions.eq("dadostratamento.ideFceSes.ideFceSes",fceSes.getIdeFceSes()));
		
		List<FceSesDadosEstacaoTratamentoEsgoto> list = fceSesDadosEstacaoTratamentoEsgotoIDAO.listarPorCriteria(criteria, Order.desc("ideFceSesDadosTramentoEsgoto"));
		
		for(FceSesDadosEstacaoTratamentoEsgoto locElevatoria : list){
			locElevatoria.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locElevatoria.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}
		
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> listarComposicoesByFceIdeEstacaoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosEstacaoTipoComposicao.class,"tipoComposicao");
		
		criteria.add(Restrictions.eq("tipoComposicao.ideFceSesDadosEstacaoTratamentoEsgoto.ideFceSesDadosTramentoEsgoto",dadosTratamento.getIdeFceSesDadosTramentoEsgoto()));
		
		return fceSesDadosEstacaoTipoComposicaoIDao.listarPorCriteria(criteria, Order.desc("ideFceSesDadosEstacaoTratamentoEsgoto"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTipoComposicaoImpl(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento)  {
		String sql = "delete from FceSesDadosEstacaoTipoComposicao composicao where composicao.ideFceSesDadosEstacaoTratamentoEsgoto.ideFceSesDadosTramentoEsgoto = :ideFceSesDadosTramentoEsgoto";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSesDadosTramentoEsgoto", localizacaoTratamento.getIdeFceSesDadosTramentoEsgoto());
		
		fceSesDadosEstacaoTratamentoEsgotoIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFceSes(FceSes ses) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSes.class);
		criteria.add(Restrictions.eq("ideFceSes", ses.getIdeFceSes()));
		return fceSesIDao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosElevatoria buscarFceSesDadosElevatoriaByIdeDadosElevatoria(FceSesDadosElevatoria dadosElevatoria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosElevatoria.class);
		criteria.add(Restrictions.eq("ideFceSesDadosElevatoria", dadosElevatoria.getIdeFceSesDadosElevatoria()));
		return fceSesDadosElevatoriaIDao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosEstacaoTratamentoEsgoto buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(FceSesDadosEstacaoTratamentoEsgoto estacaoTratamento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesDadosEstacaoTratamentoEsgoto.class);
		criteria.add(Restrictions.eq("ideFceSesDadosTramentoEsgoto", estacaoTratamento.getIdeFceSesDadosTramentoEsgoto()));
		return fceSesDadosEstacaoTratamentoEsgotoIDAO.buscarPorCriteria(criteria);
	}
}
