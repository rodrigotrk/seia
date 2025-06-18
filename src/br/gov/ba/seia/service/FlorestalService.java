package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.FlorestalAtoAmbiental;
import br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoReservaLegal;

/**
 * 
 * @author eduardo.fernandes
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FlorestalService {

	@Inject
	private IDAO<Florestal> florestalDAO;

	@Inject
	private IDAO<FlorestalAtoAmbiental> florestalRvfrDAO;

	@Inject
	private IDAO<TipoReservaLegal> tipoReservaLegalDAO;

	@Inject
	private IDAO<CaracteristicaFlorestaProducao> caracteristicaFlorestaProducaoDAO;

	@Inject
	private IDAO<FlorestalCaracteristicaFlorestaProducao> florestalCaracteristicaFlorestaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Florestal> carregarListaFlorestal(Requerimento requerimentoSelecionado)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Florestal.class);
		criteria.createAlias("ideImovel", "imo");
		criteria.createAlias("ideRequerimento", "ireq");
		criteria.createAlias("ideTipoReservaLegal", "itrl", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ireq.ideRequerimento", requerimentoSelecionado.getIdeRequerimento()));
		return florestalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoReservaLegal> carregarListaFlorestalTipoReservaLegal() {
		return tipoReservaLegalDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Florestal obterFlorestal(Florestal florestal)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFlorestal", florestal.getIdeFlorestal());
		return florestalDAO.buscarEntidadePorNamedQuery("Florestal.findByIdeFlorestal", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Florestal obterFlorestalByRequerimento(Integer ideRequerimento)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", ideRequerimento);
		return florestalDAO.buscarEntidadePorNamedQuery("Florestal.findByIdeRequerimento", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Florestal obterFlorestalByRequerimentoImovel(Integer ideRequerimento, Imovel imovel)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", ideRequerimento);
		parameters.put("ideImovel", imovel.getIdeImovel());
		return florestalDAO.buscarEntidadePorNamedQuery("Florestal.findByIdeRequerimentoImovel", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Florestal obterFlorestal(Integer ideFlorestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Florestal.class)
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideTipoSolicitacao", "ideTipoSolicitacao")
				.createAlias("ideImovel", "ideImovel")
				.createAlias("ideTipoReservaLegal", "ideTipoReservaLegal",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideFlorestal", ideFlorestal));
		
		return this.florestalDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarFlorestal(Florestal florestal)  {
		florestalDAO.salvarOuAtualizar(florestal);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFlorestal(Florestal florestal)  {
		florestalDAO.remover(florestal);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarFlorestalRVFR(FlorestalAtoAmbiental florestalRVFR)  {
		florestalRvfrDAO.salvarOuAtualizar(florestalRVFR);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFlorestalRVFRbyIdeFlorestal(Integer ideFlorestal)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFlorestal", ideFlorestal);
		this.florestalRvfrDAO.executarNamedQuery("FlorestalAtoAmbiental.remover",parameters);
	}

	public List<FlorestalAtoAmbiental> obterListaFlorestalAtoSelecionado(Florestal florestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FlorestalAtoAmbiental.class);
		criteria.add(Restrictions.eq("ideFlorestal", florestal));
		criteria.setFetchMode("ideFlorestal", FetchMode.JOIN);
		criteria.setFetchMode("ideAtoAmbiental", FetchMode.JOIN);
		List<FlorestalAtoAmbiental> listTemp = florestalRvfrDAO.listarPorCriteria(criteria);
		return listTemp;
	}

	public List<CaracteristicaFlorestaProducao> obterCaracteristicasFlorestaProducaoByIndAtivo()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaFlorestaProducao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return caracteristicaFlorestaProducaoDAO.listarPorCriteria(criteria,
				Order.asc("ideCaracteristicaFlorestaProducao"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFlorestaProducao(FlorestalCaracteristicaFlorestaProducao florestaProducao)  {
		florestalCaracteristicaFlorestaDAO.salvarOuAtualizar(florestaProducao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFlorestaProducao(Integer ideFlorestal)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFlorestal", ideFlorestal);
		this.florestalRvfrDAO.executarNamedQuery("FlorestalCaracteristicaFlorestaProducao.remover",parameters);
	}

	public List<FlorestalCaracteristicaFlorestaProducao> obterListaFlorestaProducaoSelecionado(Florestal florestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FlorestalCaracteristicaFlorestaProducao.class)
				.setFetchMode("ideFlorestal", FetchMode.JOIN)
				.setFetchMode("ideCaracteristicaFlorestaProducao", FetchMode.JOIN)
				.add(Restrictions.eq("ideFlorestal", florestal));
		
		return florestalCaracteristicaFlorestaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerByRequerimento(Integer ideRequerimento)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", ideRequerimento);
		
		this.florestalRvfrDAO.executarNamedQuery("FlorestalAtoAmbiental.removerByIdeRequerimento",parameters);
		this.florestalRvfrDAO.executarNamedQuery("FlorestalCaracteristicaFlorestaProducao.removerByIdeRequerimento",parameters);
		this.florestalRvfrDAO.executarNamedQuery("Florestal.removeByIdeRequerimento",parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerByIdeFlorestal(Integer ideFlorestal)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFlorestal", ideFlorestal);
		
		this.florestalRvfrDAO.executarNamedQuery("FlorestalAtoAmbiental.remover",parameters);
		this.florestalRvfrDAO.executarNamedQuery("FlorestalCaracteristicaFlorestaProducao.remover",parameters);
		this.florestalRvfrDAO.executarNamedQuery("Florestal.removeByIdeFlorestal",parameters);
	}
}
