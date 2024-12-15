package br.gov.ba.seia.dao;

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
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPauta;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PautaDAOImpl {

	@Inject
	private IDAO<Pauta> pautaDAO;
	
	@Inject
	private IDAO<ControleTramitacao> controleTramitacaoDAO;
	
	@Inject
	private IDAO<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaDAO;
	
	@EJB
	private AreaService areaService;
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta getPauta(Pauta pPauta) {

		if (Util.isNull(pPauta)) {
			
			return null;
			
		}
		else {
			DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
			criteria.createAlias("ideTipoPauta", "t", JoinType.INNER_JOIN)
					.createAlias("idePessoaFisica", "fun",
							JoinType.LEFT_OUTER_JOIN)
					.createAlias("ideArea", "a", JoinType.LEFT_OUTER_JOIN);

			if (!Util.isNull(pPauta.getIdeArea())
				&& !Util.isNull(pPauta.getIdeArea().getIdeArea())) {
				criteria.add(Restrictions.eq("a.ideArea", pPauta.getIdeArea().getIdeArea()));
			}

			if (!Util.isNull(pPauta.getIdePessoaFisica())
				&& !Util.isNull(pPauta.getIdePessoaFisica().getIdePessoaFisica())) {
				criteria.add(Restrictions.eq("fun.idePessoaFisica", pPauta.getIdePessoaFisica().getIdePessoaFisica()));
			}

			criteria
				.setProjection(Projections.projectionList()
					.add(Projections.property("idePauta"), "idePauta")
					.add(Projections.property("t.ideTipoPauta"),"ideTipoPauta.ideTipoPauta")
					.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class));

			try {
				return pautaDAO.buscarPorCriteria(criteria);
			}
			catch (Exception e) {
				return null;
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Pauta> listarPautaIntegraEquipe(Integer ideProcesso)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.integranteEquipeCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.ideEquipe", "eq", JoinType.INNER_JOIN)
			
			.add(Property.forName("eq.ideEquipe").eq(
				DetachedCriteria.forClass(Equipe.class)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.setProjection(Projections.max("ideEquipe"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta").as("idePauta"))
			)
					
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.listarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta buscarPautaGestorAtualProcesso(Integer ideProcesso)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		criteria
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.pautaCollection","pta", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideEquipe").eq(
					DetachedCriteria.forClass(Equipe.class)
						.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
						.add(Restrictions.eq("p.ideProcesso", ideProcesso))
						.setProjection(Projections.max("ideEquipe"))
			))
			
			.setProjection(Projections.property("pta.idePauta").as("idePauta"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta buscarPautaGestorPor(Integer ideArea) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		
		criteria
			.createAlias("idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.pautaCollection","pta", JoinType.INNER_JOIN)
			.createAlias("pta.ideTipoPauta","tp", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ideArea", ideArea))
			.add(Restrictions.ne("tp.ideTipoPauta", TipoPautaEnum.PAUTA_AREA.getTipo()))
			
			.setProjection(Projections.property("pta.idePauta").as("idePauta"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Pauta pPauta)  {
		pautaDAO.salvarOuAtualizar(pPauta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta retornarPautaDoTecnicoCriadorDaNotificacao(Integer ideProcesso, Integer ideArea) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("integranteEquipeCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.pautaCollection", "pta", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("a.ideArea", ideArea))
			.add(Restrictions.eq("ie.indLiderEquipe", true))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("pta.idePauta"), "idePauta")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pauta> listarPautasGestorComAcessoPermitido(PessoaFisica pessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("func.ideArea", "afunc", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("funcionalidadeAcaoPessoaFisicaPautaCollection", "fapfp", JoinType.INNER_JOIN)
			.createAlias("fapfp.ideFuncionalidadeAcaoPessoaFisica", "fapf", JoinType.INNER_JOIN)
			.createAlias("fapf.idePessoaFisica", "pf", JoinType.INNER_JOIN)
			
			.add(Restrictions.in("t.ideTipoPauta", new Integer[] {
					TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo(),
					TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo()
				}
			))
			.add(Restrictions.eq("pf.idePessoaFisica", pessoaFisica.getIdePessoaFisica()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta"),"idePauta")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				
				.add(Projections.property("func.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("afunc.ideArea"),"idePessoaFisica.ideArea.ideArea")
				.add(Projections.property("afunc.nomArea"),"idePessoaFisica.ideArea.nomArea")
				
				.add(Projections.property("t.ideTipoPauta"),"ideTipoPauta.ideTipoPauta")
				
				.add(Projections.groupProperty("idePauta"))
				.add(Projections.groupProperty("a.ideArea"))
				.add(Projections.groupProperty("a.nomArea"))
				.add(Projections.groupProperty("t.ideTipoPauta"))
				.add(Projections.groupProperty("func.idePessoaFisica"))
				.add(Projections.groupProperty("afunc.ideArea"))
				.add(Projections.groupProperty("afunc.nomArea"))
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		
		;
		
		List<Pauta> listaPauta = pautaDAO.listarPorCriteria(criteria);
		
		for (Pauta pauta : listaPauta) {
			pauta.setFuncionalidadeAcaoPessoaFisicaPautaCollection(carregar(pauta.getIdePauta(), pessoaFisica.getIdePessoaFisica()));
		}
		
		return listaPauta;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pauta> listarPautasAreaComAcessoPermitido(PessoaFisica pessoaFisica)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("funcionalidadeAcaoPessoaFisicaPautaCollection", "fapfp", JoinType.INNER_JOIN)
			.createAlias("fapfp.ideFuncionalidadeAcaoPessoaFisica", "fapf", JoinType.INNER_JOIN)
			.createAlias("fapf.idePessoaFisica", "pf", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("t.ideTipoPauta", TipoPautaEnum.PAUTA_AREA.getTipo()))
			.add(Restrictions.eq("pf.idePessoaFisica", pessoaFisica.getIdePessoaFisica()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta"),"idePauta")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("t.ideTipoPauta"),"ideTipoPauta.ideTipoPauta")
				.add(Projections.groupProperty("idePauta"))
				.add(Projections.groupProperty("a.ideArea"))
				.add(Projections.groupProperty("a.nomArea"))
				.add(Projections.groupProperty("t.ideTipoPauta"))
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		
		;
		
		List<Pauta> listaPauta = pautaDAO.listarPorCriteria(criteria);
		
		for (Pauta pauta : listaPauta) {
			pauta.setFuncionalidadeAcaoPessoaFisicaPautaCollection(carregar(pauta.getIdePauta(), pessoaFisica.getIdePessoaFisica()));
		}
		
		return listaPauta;
	}

	private Collection<FuncionalidadeAcaoPessoaFisicaPauta> carregar(Integer idePauta, Integer idePessoaFisica)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisicaPauta.class);
		
		criteria
			.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
			.createAlias("ideFuncionalidadeAcaoPessoaFisica", "fapf", JoinType.INNER_JOIN)
			.createAlias("fapf.ideFuncionalidade", "f", JoinType.INNER_JOIN)
			.createAlias("fapf.ideAcao", "a", JoinType.INNER_JOIN)
			.createAlias("fapf.idePessoaFisica", "pf", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pta.idePauta", idePauta))
			.add(Restrictions.eq("pf.idePessoaFisica", idePessoaFisica))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("fapf.ideFuncionalidadeAcaoPessoaFisica"),"ideFuncionalidadeAcaoPessoaFisica.ideFuncionalidadeAcaoPessoaFisica")
				.add(Projections.property("f.ideFuncionalidade"),"ideFuncionalidadeAcaoPessoaFisica.ideFuncionalidade.ideFuncionalidade")
				.add(Projections.property("a.ideAcao"),"ideFuncionalidadeAcaoPessoaFisica.ideAcao.ideAcao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FuncionalidadeAcaoPessoaFisicaPauta.class))
		;
		
		return funcionalidadeAcaoPessoaFisicaPautaDAO.listarPorCriteria(criteria); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaArea(Area pArea)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideArea", pArea.getIdeArea());
		parametros.put("ideTipoPauta", TipoPautaEnum.PAUTA_AREA.getTipo());
		return buscarEntidadePorNamedQuery("Pauta.findByIdeAreaTipoPauta", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaAreaCoordenador(Integer pArea)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideArea", pArea);
		parametros.put("ideTipoPauta", TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo());
		return buscarEntidadePorNamedQuery("Pauta.findByIdeAreaTipoPauta", parametros);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaArea(Integer ideArea) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("a.ideArea", ideArea ))
			.add(Restrictions.eq("t.ideTipoPauta", TipoPautaEnum.PAUTA_AREA.getTipo()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta"),"idePauta")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("t.ideTipoPauta"),"ideTipoPauta.ideTipoPauta")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta buscarEntidadePorNamedQuery(String namedQuery, Map<String, Object> parametros)  {
		return pautaDAO.buscarEntidadePorNamedQuery(namedQuery, parametros);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaCoordenadorArea(Integer ideArea, TipoPautaEnum tipoPautaEnum) throws Exception  {
		
		Funcionario func = areaService.obterPessoaFisicaCoordenadorPorIdeArea(ideArea);
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("idePessoaFisica", func.getIdePessoaFisica());
		parametros.put("ideTipoPauta", tipoPautaEnum.getTipo());
		
		return pautaDAO.buscarEntidadePorNamedQuery("Pauta.findByIdePessoaFisicaIdeTipoPauta", parametros);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionario(Integer ideFuncionario) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("ideArea", "a", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("fun.idePessoaFisica", ideFuncionario ))
			.add(Restrictions.ne("t.ideTipoPauta", TipoPautaEnum.PAUTA_AREA.getTipo()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta"),"idePauta")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("t.ideTipoPauta"),"ideTipoPauta.ideTipoPauta")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionario(Integer ideFuncionario, Integer ideTipoPauta)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideFuncionario", ideFuncionario);
		parametros.put("ideTipoPauta", ideTipoPauta);
		
		return pautaDAO.buscarEntidadePorNamedQuery("Pauta.findByIdeFuncionarioPauta", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionarioCriteria(Integer ideFuncionario, Integer ideTipoPauta)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idePessoaFisica", ideFuncionario);
		parametros.put("ideTipoPauta", ideTipoPauta);
		
		return pautaDAO.buscarEntidadePorNamedQuery("Pauta.findByIdePessoaFisicaIdeTipoPauta", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Pauta buscarPorQuery(String sql, Map<String, Object> parametros)  {
		return pautaDAO.buscarPorQuery(sql, parametros);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Pauta pauta)  {
		pautaDAO.atualizar(pauta);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta carregarPautaPorIdeProcessoFimFila(Integer ideProcesso) {

		Map<String, Object> mapControleTramitacao = new HashMap<String, Object>();
		mapControleTramitacao.put("ideProcesso", ideProcesso);
		mapControleTramitacao.put("indFimDaFila", true);
		String nq1 = "ControleTramitacao.findByIdeProcessoIndFimDaFila";

		List<ControleTramitacao> listControleTramitacao = controleTramitacaoDAO.buscarPorNamedQuery(nq1, mapControleTramitacao);

		return listControleTramitacao.get(0).getIdePauta();

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Pauta> listarPautaIntegraEquipeArea(Integer ideProcesso, Integer ideArea)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Pauta.class);
		
		criteria
			.createAlias("idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.integranteEquipeCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.ideEquipe", "eq", JoinType.INNER_JOIN)
			
			.add(Property.forName("eq.ideEquipe").eq(
				DetachedCriteria.forClass(Equipe.class)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.createAlias("ideArea", "a", JoinType.INNER_JOIN)
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.add(Restrictions.eq("a.ideArea", ideArea))
					.setProjection(Projections.max("ideEquipe"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePauta").as("idePauta"))
			)
					
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pauta.class))
		;
		
		return pautaDAO.listarPorCriteria(criteria);

	}
}