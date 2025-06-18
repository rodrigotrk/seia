package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoTipologiaService {

	@Inject
	private IDAO<Tipologia> tipologiaDAO;

	@Inject
	private IDAO<RequerimentoTipologia> requerimentoTipologiaDAO;
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTipologiasNaoContidasEm(Requerimento requerimento, Collection<Tipologia> tipologias) {
		try{
			List<RequerimentoTipologia> listaRequerimentoTipologia = (List<RequerimentoTipologia>) buscarRequerimentoTipologiaByRequerimento(requerimento);
			for(RequerimentoTipologia requerimentoTipologia : listaRequerimentoTipologia) {
				Tipologia tipologia = requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia();
				if(!tipologias.contains(tipologia)) {
					Map<String,Object> params = new HashMap<String, Object>();
					params.put("ideRequerimentoTipologia", requerimentoTipologia.getIdeRequerimentoTipologia());
					requerimentoTipologiaDAO.executarQuery("delete from RequerimentoTipologia r where r.ideRequerimentoTipologia=:ideRequerimentoTipologia", params);					
				}
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RequerimentoTipologia> buscarRequerimentoTipologiaByRequerimento(Requerimento requerimento) {
		return requerimentoTipologiaDAO.listarPorCriteria(getCriteriaRequerimentoTipologia(requerimento), Order.asc("ideRequerimento"));
	}


	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 */
	private DetachedCriteria getCriteriaRequerimentoTipologia(Requerimento requerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoTipologia.class, "rt")
				.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "umtg",JoinType.LEFT_OUTER_JOIN)
				.createAlias("umtg.ideUnidadeMedida", "um",JoinType.INNER_JOIN)
				.createAlias("umtg.ideTipologiaGrupo", "tg", JoinType.INNER_JOIN)
				.createAlias("tg.ideTipologia", "t", JoinType.INNER_JOIN)
				.createAlias("tg.idePotencialPoluicao", "pp", JoinType.INNER_JOIN)
				
				.setProjection(
					Projections.projectionList()
					
					.add(Property.forName("ideRequerimentoTipologia"), "ideRequerimentoTipologia")
					.add(Property.forName("valAtividade"), "valAtividade")
					.add(Property.forName("indTipologiaPrincipal"), "indTipologiaPrincipal")
					.add(Property.forName("indAcaoTipologia"), "indAcaoTipologia")
					
					.add(Property.forName("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
					
					.add(Property.forName("umtg.ideUnidadeMedidaTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
					
					.add(Property.forName("um.ideUnidadeMedida"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
					.add(Property.forName("um.codUnidadeMedida"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida")
					.add(Property.forName("um.nomUnidadadeMedida"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedida.nomUnidadadeMedida")
					
					.add(Property.forName("tg.ideTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologiaGrupo")
					
					.add(Property.forName("t.ideTipologia"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.ideTipologia")
					.add(Property.forName("t.desTipologia"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.desTipologia")
					.add(Property.forName("t.codTipologia"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologia.codTipologia")
					
					.add(Property.forName("pp.idePotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
					.add(Property.forName("pp.sglPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.sglPotencialPoluicao")
					.add(Property.forName("pp.nomPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.nomPotencialPoluicao")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoTipologia.class))
				
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		return detachedCriteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoTipologia buscarRequerimentoTipologiaPrincipal(Requerimento requerimento)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoTipologia.class, "rt")
				.createAlias("ideRequerimento", "req")
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "umtg")
				.createAlias("umtg.ideTipologiaGrupo", "tg")
				.createAlias("tg.idePotencialPoluicao", "pp")
				
				.setProjection(
					Projections.projectionList()
					
					.add(Property.forName("ideRequerimentoTipologia"), "ideRequerimentoTipologia")
					.add(Property.forName("valAtividade"), "valAtividade")
					.add(Property.forName("indTipologiaPrincipal"), "indTipologiaPrincipal")
					.add(Property.forName("indAcaoTipologia"), "indAcaoTipologia")
					
					.add(Property.forName("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
					
					.add(Property.forName("umtg.ideUnidadeMedidaTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
					.add(Property.forName("umtg.ideUnidadeMedida.ideUnidadeMedida"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
					
					.add(Property.forName("tg.ideTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologiaGrupo")
					
					.add(Property.forName("pp.idePotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
					.add(Property.forName("pp.sglPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.sglPotencialPoluicao")
					.add(Property.forName("pp.nomPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.nomPotencialPoluicao")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoTipologia.class))
				
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("indTipologiaPrincipal", true));
		
		return requerimentoTipologiaDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(RequerimentoTipologia requerimentoTipologia) {
		this.requerimentoTipologiaDAO.salvarOuAtualizar(requerimentoTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoTipologia carregarRequerimentoTipologia(RequerimentoTipologia requerimentoTipologia)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoTipologia.class, "rt")
				.createAlias("ideRequerimento", "req")
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "umtg")
				.createAlias("umtg.ideTipologiaGrupo", "tg")
				.createAlias("tg.idePotencialPoluicao", "pp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tg.ideTipologia", "t", JoinType.INNER_JOIN)
				
				.setProjection(
					Projections.projectionList()
					
					.add(Property.forName("ideRequerimentoTipologia"), "ideRequerimentoTipologia")
					.add(Property.forName("valAtividade"), "valAtividade")
					.add(Property.forName("indTipologiaPrincipal"), "indTipologiaPrincipal")
					.add(Property.forName("indAcaoTipologia"), "indAcaoTipologia")
					
					.add(Property.forName("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
					
					.add(Property.forName("umtg.ideUnidadeMedidaTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
					.add(Property.forName("umtg.ideUnidadeMedida.ideUnidadeMedida"), "ideUnidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
					
					.add(Property.forName("tg.ideTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.ideTipologiaGrupo")
					
					.add(Property.forName("pp.idePotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
					.add(Property.forName("pp.sglPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.sglPotencialPoluicao")
					.add(Property.forName("pp.nomPotencialPoluicao"), "ideUnidadeMedidaTipologiaGrupo.ideTipologiaGrupo.idePotencialPoluicao.nomPotencialPoluicao")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoTipologia.class))
				
				.add(Restrictions.eq("req.ideRequerimento", requerimentoTipologia.getIdeRequerimento().getIdeRequerimento()))
				.add(Restrictions.eq("umtg.ideUnidadeMedidaTipologiaGrupo", requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo()));

		return this.requerimentoTipologiaDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> buscarTipologiasLicenca(Requerimento requerimento)  {
		return this.buscarTipologias(requerimento, false);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> buscarTipologiasLicenca(Requerimento requerimento, boolean filtrarTipologiaExcluida, boolean filtrarTipologiaGrupoExcluida)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoTipologia.class)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("ideUnidadeMedidaTipologiaGrupo", "iumtg", JoinType.INNER_JOIN)
			.createAlias("iumtg.ideUnidadeMedida", "um", JoinType.INNER_JOIN)
			.createAlias("iumtg.ideTipologiaGrupo", "tg", JoinType.INNER_JOIN)
			.createAlias("tg.ideTipologia", "t", JoinType.INNER_JOIN)
			.createAlias("t.ideTipologiaPai", "tp", JoinType.LEFT_OUTER_JOIN) 
			
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("t.ideTipologia"),"ideTipologia")
				.add(Projections.property("t.desTipologia"),"desTipologia")
				.add(Projections.property("t.codTipologia"),"codTipologia")

				.add(Projections.property("tp.ideTipologia"),"ideTipologiaPai.ideTipologia") 
				.add(Projections.property("tp.codTipologia"),"ideTipologiaPai.codTipologia")

				.add(Projections.property("indTipologiaPrincipal"),"indPrincipal")
				.add(Projections.property("valAtividade"),"valorAtividade")
				.add(Projections.property("indAcaoTipologia"),"option")
				
				.add(Projections.property("tg.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo")
				.add(Projections.property("iumtg.ideUnidadeMedidaTipologiaGrupo"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
				

				.add(Projections.property("um.ideUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
				.add(Projections.property("um.codUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida")
			)).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		
			.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.eq("t.indAutorizacao", false));
			
			if(filtrarTipologiaExcluida) criteria.add(Restrictions.eq("t.indExcluido", false));
			if(filtrarTipologiaGrupoExcluida) criteria.add(Restrictions.eq("tg.indExcluido", false));

		return tipologiaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> buscarIdesTipologiasLicenca(Requerimento requerimento)  {
		Collection<Integer> collInt = null;

		Collection<Tipologia> collTipol = this.buscarTipologias(requerimento);

		if(collTipol != null && !collTipol.isEmpty()){
			collInt = new ArrayList<Integer>();
			for (Tipologia tipologia : collTipol) {
				collInt.add(tipologia.getIdeTipologia());
			}
		}

		return collInt;
	}

	public Collection<Tipologia> buscarTipologiasAutorizacao(Requerimento requerimento)  {
		return this.buscarTipologias(requerimento, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Tipologia> buscarTipologias(Requerimento requerimento, boolean indAutorizacao)  {

		DetachedCriteria criteria = getCriteriaBuscarTipologias(requerimento);
		criteria.add(Restrictions.eq("ti.indAutorizacao", indAutorizacao));
		
		return tipologiaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> buscarTipologias(Requerimento requerimento)  {

		DetachedCriteria criteria = getCriteriaBuscarTipologias(requerimento);
		return tipologiaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaBuscarTipologias(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoTipologia.class)
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "iumtg", JoinType.INNER_JOIN)
				.createAlias("iumtg.ideTipologiaGrupo", "itg", JoinType.INNER_JOIN)
				.createAlias("iumtg.ideUnidadeMedida", "um", JoinType.INNER_JOIN)
				.createAlias("itg.ideTipologia", "ti", JoinType.INNER_JOIN)
				.createAlias("itg.idePotencialPoluicao", "ipp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ti.ideTipologiaPai", "pai", JoinType.INNER_JOIN); // Melhoria #6590

		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("ti.ideTipologia"),"ideTipologia")
				.add(Property.forName("ti.desTipologia"),"desTipologia")
				.add(Property.forName("ti.codTipologia"),"codTipologia")
				.add(Property.forName("ti.indAutorizacao"),"indAutorizacao")

				.add(Property.forName("pai.ideTipologia"),"ideTipologiaPai.ideTipologia") // Melhoria #6590
				.add(Property.forName("pai.codTipologia"),"ideTipologiaPai.codTipologia") // Melhoria #6590

				.add(Property.forName("indTipologiaPrincipal"),"indPrincipal")
				.add(Property.forName("valAtividade"),"valorAtividade")
				.add(Property.forName("indAcaoTipologia"),"option")

				.add(Property.forName("itg.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo")
				.add(Property.forName("ipp.idePotencialPoluicao"),"tipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
				//.add(Property.forName("iumtg.ideUnidadeMedidaTipologiaGrupo"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")

				.add(Property.forName("um.ideUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
				.add(Property.forName("um.codUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida");

		criteria.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(RequerimentoTipologia requerimentoTipologia)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideReqTipologia", requerimentoTipologia.getIdeRequerimentoTipologia());
		this.requerimentoTipologiaDAO.executarNativeQuery("delete from requerimento_tipologia where ide_requerimento_tipologia = :ideReqTipologia ", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerByRequerimento(Integer ideRequerimento)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideRequerimento", ideRequerimento);
		this.requerimentoTipologiaDAO.executarNativeQuery("delete from requerimento_tipologia where ide_requerimento = :ideRequerimento ", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerByEmpreendimentoByTipologiaGrupo(Integer ideEmpreendimento, Integer ideTipologiaGrupo)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideEmpreendimento", ideEmpreendimento);
		params.put("ideTipologiaGrupo", ideTipologiaGrupo);
		this.requerimentoTipologiaDAO.executarNativeQuery("delete from requerimento_tipologia rt where rt.ide_requerimento_tipologia = ("
				+ "select ide_requerimento_tipologia from requerimento_tipologia rt "
				+ "inner join requerimento r on r.ide_requerimento = rt.ide_requerimento "
				+ "inner join empreendimento_requerimento er on er.ide_requerimento = r.ide_requerimento "
				+ "inner join empreendimento e on e.ide_empreendimento = er.ide_empreendimento "
				+ "inner join unidade_medida_tipologia_grupo umtg on umtg.ide_unidade_medida_tipologia_grupo = rt.ide_unidade_medida_tipologia_grupo "
				+ "inner join tipologia_grupo tg on tg.ide_tipologia_grupo = umtg.ide_tipologia_grupo "
				+ "where e.ide_empreendimento = :ideEmpreendimento and tg.ide_tipologia_grupo = :ideTipologiaGrupo"
				+ " and r.ide_requerimento in ("
				+ "select r1.ide_requerimento from requerimento r1 "
				+ "inner join tramitacao_requerimento tr on tr.ide_requerimento = r1.ide_requerimento "
				+ "where r1.ide_requerimento = r.ide_requerimento and ("
				+ "(select ide_status_requerimento from tramitacao_requerimento where ide_requerimento = r.ide_requerimento "
				+ "order by ide_tramitacao_requerimento desc limit 1) = 1 "
				+ "or "
				+ "(select ide_status_requerimento from tramitacao_requerimento where ide_requerimento = r.ide_requerimento "
				+ "order by ide_tramitacao_requerimento desc limit 1) = 14) "
				+ "order by ide_tramitacao_requerimento desc));", params);
	}

	public BigDecimal retornarValorAtividadeByRequerimentoAndTipologia(Requerimento requerimento, TipologiaEnum tipologia)  {
		return retornarValorAtividadeByRequerimentoAndTipologia(requerimento.getIdeRequerimento(), tipologia.getId());
	}
	
	public BigDecimal retornarValorAtividadeByRequerimentoAndTipologia(Integer ideRequerimento, Integer ideTipologia)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoTipologia.class)
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "um")
				.createAlias("um.ideTipologiaGrupo", "tg")
				.createAlias("tg.ideTipologia", "t")
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("t.ideTipologia", ideTipologia));
		RequerimentoTipologia requerimentoTipologia = requerimentoTipologiaDAO.buscarPorCriteria(detachedCriteria);
		return requerimentoTipologia.getValAtividade();
	}

	public BigDecimal buscarValorAtividadeByRequerimentoAndTipologia(Integer ideRequerimento, Integer ideTipologia)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoTipologia.class)
				.createAlias("ideUnidadeMedidaTipologiaGrupo", "um")
				.createAlias("um.ideTipologiaGrupo", "tg")
				.createAlias("tg.ideTipologia", "t")
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("tg.indExcluido", false))
				.add(Restrictions.eq("t.ideTipologia", ideTipologia))
				.setProjection(Projections.projectionList()
						.add(Property.forName("valAtividade"),"valAtividade")
						);
		Object valoAtividade = requerimentoTipologiaDAO.buscarPorCriteria(detachedCriteria);
		return (BigDecimal) valoAtividade ;
	}
	
	public Double retornarSomatorioValorAtividadeByGrupo(Requerimento requerimento, String codTipologia)  {
		DetachedCriteria criteria = getCriteriaBuscarTipologias(requerimento).add(Restrictions.ilike("ti.codTipologia", codTipologia, MatchMode.START));
		return (Double) requerimentoTipologiaDAO.sum(criteria, Projections.sum("valAtividade"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RequerimentoTipologia> buscarRequerimentoTipologiasPorRequerimento(Requerimento requerimento) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideRequerimento", requerimento.getIdeRequerimento());
		return requerimentoTipologiaDAO.buscarPorNamedQuery("RequerimentoTipologia.findByIdeRequerimento", params);
	}
}
