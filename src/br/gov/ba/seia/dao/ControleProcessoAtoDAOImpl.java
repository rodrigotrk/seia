package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleProcessoAtoDAOImpl {
	
	@Inject
	private IDAO<ControleProcessoAto> controleProcessoAtoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ControleProcessoAto controleProcessoAto) {
		try{
			controleProcessoAtoDAO.salvarOuAtualizar(controleProcessoAto);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ControleProcessoAto controleProcessoAto) {
		try{
			controleProcessoAtoDAO.atualizar(controleProcessoAto);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleProcessoAto buscarUltimoDeferidoOuIndeferidoPorProcessoAto(Integer ideProcessoAto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideControleProcessoAto").eq(
				DetachedCriteria.forClass(ControleProcessoAto.class)
					.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
					.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
					.add(Restrictions.in("ts.ideStatusProcessoAto", new Integer[] {
							StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId(),
							StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()
					})).setProjection(Projections.max("ideControleProcessoAto"))
					
			)).setProjection(Projections.projectionList()
				.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
				.add(Projections.property("dscJustificativaStatus"),"dscJustificativaStatus")
				.add(Projections.property("dtcControleProcessoAto"),"dtcControleProcessoAto")
				.add(Projections.property("indAprovado"),"indAprovado")
				.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
				.add(Projections.property("indPrazoIndeterminado"),"indPrazoIndeterminado")
				
				.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.property("ts.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class))
		;
		
		return controleProcessoAtoDAO.buscarPorCriteria(criteria);
	}
	
	/**
	 * <p>
	 * 		Método que busca último status do processo_ato
	 * </p>
	 * @param ideProcessoAto - ide referente ao processo_ato
	 * @return {@link ControleProcessoAto} - último status do processo_ato
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleProcessoAto buscarUltimoPorProcessoAto(Integer ideProcessoAto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class)
				.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
				.createAlias("idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
					
				.add(Property.forName("ideControleProcessoAto").eq(
					DetachedCriteria.forClass(ControleProcessoAto.class)
						.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
						.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
						
						.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
						.setProjection(Projections.max("ideControleProcessoAto"))))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
					.add(Projections.property("dscJustificativaStatus"),"dscJustificativaStatus")
					.add(Projections.property("dtcControleProcessoAto"),"dtcControleProcessoAto")
					.add(Projections.property("indAprovado"),"indAprovado")
					.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
					.add(Projections.property("indPrazoIndeterminado"),"indPrazoIndeterminado")
					
					.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
					.add(Projections.property("ts.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto"))
					
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class));
			
			return controleProcessoAtoDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleProcessoAto> listarControleProcessoAtoAprovacao(Integer ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class);
		criteria
			.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "fun",  JoinType.INNER_JOIN)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.in("ts.ideStatusProcessoAto", new Integer[] {
					StatusProcessoAtoEnum.DEFERIDO.getId(),
					StatusProcessoAtoEnum.INDEFERIDO.getId()
				}
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
				.add(Projections.property("dscJustificativaStatus"),"dscJustificativaStatus")
				.add(Projections.property("dtcControleProcessoAto"),"dtcControleProcessoAto")
				.add(Projections.property("indAprovado"),"indAprovado")
				.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
				.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.property("ts.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class))
		;
		
		return controleProcessoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleProcessoAto> listarControleProcessoAtoAtualPor(Integer ideProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class);
		criteria
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "at", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fun.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
				.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
				.add(Restrictions.in("ts.ideStatusProcessoAto", new Integer[] {
						StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId(),
						StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()
					}
				)
			)
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
					.add(Projections.property("dscJustificativaStatus"),"dscJustificativaStatus")
					.add(Projections.property("dtcControleProcessoAto"),"dtcControleProcessoAto")
					.add(Projections.property("indAprovado"),"indAprovado")
					.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
					.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					.add(Projections.property("indPrazoIndeterminado"),"indPrazoIndeterminado")
					.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.pessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.pessoaFisica.nomPessoa")
					.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
					
					.add(Projections.property("at.ideAtoAmbiental"),"ideProcessoAto.atoAmbiental.ideAtoAmbiental")
					
					.add(Projections.property("ts.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto")
					)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class))
		;
		Collection<ControleProcessoAto> lista =controleProcessoAtoDAO.listarPorCriteria(criteria);
		Collection<ControleProcessoAto> listaAux = new ArrayList();
		if(lista.size()>1) {
			ControleProcessoAto paAnt=null;
			int lastId = 0;
			for(ControleProcessoAto pa : lista) {
				if(pa.getId()>lastId) {
					lastId=pa.getId();
					if(paAnt!=null) {
						listaAux.add(paAnt);
					}
				}
				paAnt=pa;
			}
		}
		if(listaAux.size()>0) {
			lista.removeAll(listaAux);
		}
		return lista;
	}
	

	
	/**
	 * <p>
	 * 		Método que busca último status do processo_ato modificado por um determidado técnico 
	 * </p>
	 * @param ideProcessoAto - ide referente ao processo_ato
	 * @param idePessoaFisica - ide referene ao funcionario que modificou o status do ato
	 * @return {@link ControleProcessoAto} - último status do processo_ato
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleProcessoAto buscarStatusProcessoAtoAtualPor(Integer ideProcessoAto, Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class);
		criteria
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
			.add(Property.forName("ideControleProcessoAto").eq(
					DetachedCriteria.forClass(ControleProcessoAto.class)
					.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
					.createAlias("ideStatusProcessoAto", "ts", JoinType.INNER_JOIN)
					.createAlias("pa.processoAtoIntegranteEquipeCollection", "paie", JoinType.INNER_JOIN)
					.createAlias("paie.ideIntegranteEquipe", "ie", JoinType.INNER_JOIN)
					.createAlias("ie.idePessoaFisica", "fun", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
					.add(Restrictions.eq("fun.idePessoaFisica", idePessoaFisica))
					.add(Restrictions.in("ts.ideStatusProcessoAto", new Integer[] {
							StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId(),
							StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()
					}))
					
					.setProjection(Projections.max("ideControleProcessoAto"))
			))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
				.add(Projections.property("dscJustificativaStatus"),"dscJustificativaStatus")
				.add(Projections.property("dtcControleProcessoAto"),"dtcControleProcessoAto")
				.add(Projections.property("indAprovado"),"indAprovado")
				.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
				.add(Projections.property("fun.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.property("ts.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class))
		;
		
		return controleProcessoAtoDAO.buscarPorCriteria(criteria);
	}
}