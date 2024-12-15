package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 06/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoAtoConcedidoDAOImpl {

	@Inject
	private IDAO<ProcessoAtoConcedido> dao;
	
	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 06/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param processoAtoConcedido
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProcessoAtoConcedido processoAtoConcedido)  {
		try {
			dao.salvarOuAtualizar(processoAtoConcedido);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 06/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param ideProcessoAto
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoAtoConcedido(Integer ideProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
				.createAlias("ideProcessoAto", "pa")
				.createAlias("ideImovel", "i", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("ideTipoAreaConcedida", "tac")
				.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideProcessoAtoConcedido"), "ideProcessoAtoConcedido")
						.add(Projections.property("valAtividade"), "valAtividade")
						.add(Projections.property("i.ideImovel"), "ideImovel.ideImovel")
						.add(Projections.property("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
						.add(Projections.property("tac.ideTipoAreaConcedida"), "ideTipoAreaConcedida.ideTipoAreaConcedida")
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoConcedido.class))
				;
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoAtoConcedidoPor(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
			
			.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "a", JoinType.INNER_JOIN)
			.createAlias("a.ideAtoSinaflor", "as", JoinType.INNER_JOIN)
			.createAlias("ideImovel", "i", JoinType.INNER_JOIN)
			.createAlias("ideTipoAreaConcedida", "tac", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.setProjection(
					Projections.projectionList()
					.add(Projections.groupProperty("ideProcessoAtoConcedido"), "ideProcessoAtoConcedido")
					.add(Projections.groupProperty("valAtividade"), "valAtividade")
					.add(Projections.groupProperty("i.ideImovel"), "ideImovel.ideImovel")
					.add(Projections.groupProperty("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
					.add(Projections.groupProperty("tac.ideTipoAreaConcedida"), "ideTipoAreaConcedida.ideTipoAreaConcedida")
					.add(Projections.groupProperty("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
					.add(Projections.groupProperty("a.ideAtoAmbiental"), "ideProcessoAto.atoAmbiental.ideAtoAmbiental")
					.add(Projections.groupProperty("as.ideAtoSinaflor"), "ideProcessoAto.atoAmbiental.ideAtoSinaflor.ideAtoSinaflor")
					.add(Projections.groupProperty("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.sqlGroupProjection("(select ST_Y(ST_centroid(the_geom)) from dado_geografico where ide_localizacao_geografica=lg1_.ide_localizacao_geografica) as pontoLatitude_", "pontoLatitude_", new String[] {"pontoLatitude_"},	new Type[] {StandardBasicTypes.STRING}),"ideLocalizacaoGeografica.pontoLatitude")
					.add(Projections.sqlGroupProjection("(select ST_X(ST_centroid(the_geom)) from dado_geografico where ide_localizacao_geografica=lg1_.ide_localizacao_geografica) as pontoLongitude_", "pontoLongitude_", new String[] {"pontoLongitude_"}, new Type[] {StandardBasicTypes.STRING}),"ideLocalizacaoGeografica.pontoLongitude")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoConcedido.class))
		;
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoAtoConcedido buscarProcessoAtoConcedidoPorImovel(Integer ideProcessoAto, Imovel imovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
				.createAlias("ideProcessoAto", "pa")
				.createAlias("ideImovel", "i")
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("ideTipoAreaConcedida", "tac")
				.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
				.add(Restrictions.eq("i.ideImovel", imovel.getIdeImovel()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideProcessoAtoConcedido"), "ideProcessoAtoConcedido")
						.add(Projections.property("valAtividade"), "valAtividade")
						.add(Projections.property("i.ideImovel"), "ideImovel.ideImovel")
						.add(Projections.property("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
						.add(Projections.property("tac.ideTipoAreaConcedida"), "ideTipoAreaConcedida.ideTipoAreaConcedida")
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoConcedido.class))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoConcedidoPor(AnaliseTecnica analiseTecnica, Boolean indAprovado)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.processo", "pro", JoinType.INNER_JOIN)
			.createAlias("pro.ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("pro.analiseTecnicaCollection", "analiseTecnica", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("analiseTecnica.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoAtoConcedido"), "ideProcessoAtoConcedido")
				.add(Projections.property("valAtividade"), "valAtividade")
				.add(Projections.property("pro.ideProcesso"),"ideProcessoAto.processo.ideProcesso")
				.add(Projections.property("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
				.add(Projections.property("req.ideRequerimento"),"ideProcessoAto.processo.ideRequerimento.ideRequerimento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoConcedido.class));
		
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoConcedidoPorAmcAsv(AnaliseTecnica analiseTecnica, Boolean indAprovado)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
				.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.processo", "pro", JoinType.INNER_JOIN)
				.createAlias("pro.ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("pro.analiseTecnicaCollection", "analiseTecnica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("analiseTecnica.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
				.add(Restrictions.or(Restrictions.eq("pa.atoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.AMC.getId()), 
						Restrictions.eq("pa.atoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.ASV.getId())))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideProcessoAtoConcedido"), "ideProcessoAtoConcedido")
					.add(Projections.property("valAtividade"), "valAtividade")
					.add(Projections.property("pro.ideProcesso"),"ideProcessoAto.processo.ideProcesso")
					.add(Projections.property("pa.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
					.add(Projections.property("req.ideRequerimento"),"ideProcessoAto.processo.ideRequerimento.ideRequerimento")
					.add(Projections.property("pa.atoAmbiental"), "ideProcessoAto.atoAmbiental")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAtoConcedido.class));
			
			return dao.listarPorCriteria(criteria);
	}
	
	public List<ProcessoAtoConcedido> listarProcessoAMC() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
				.createAlias("ideProcessoAto", "pa");
		
		return dao.listarPorCriteria(criteria);
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean existeProcessoAtoConcedido(Integer ideProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
			.createAlias("ideProcessoAto", "pa").add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto));
		
		return !Util.isNullOuVazio(dao.count(criteria));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorProcessoAto(ProcessoAto processoAto)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideProcessoAto", processoAto.getIdeProcessoAto());
		dao.executarNamedQuery("ProcessoAtoConcedido.excluirByIdeProcessoAto", map);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> obterByLocalizacaoGeografica(LocalizacaoGeografica locGeo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAtoConcedido.class)
			.add(Restrictions.eq("ideLocalizacaoGeografica", locGeo));
		return dao.listarPorCriteria(criteria, Order.asc("ideLocalizacaoGeografica"));
	}
	
	/**
	 * 
	 * 
	 * @author eduardo.fernandes 
	 * @since 13/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param processoAto 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirBy(ProcessoAto processoAto)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideProcessoAto", processoAto.getIdeProcessoAto());
		dao.executarNamedQuery("ProcessoAtoConcedido.excluirByIdeProcessoAto", map);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ProcessoAtoConcedido processoAtoConcedido)  {
		dao.remover(processoAtoConcedido);
	}
}