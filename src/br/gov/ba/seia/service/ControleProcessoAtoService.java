package br.gov.ba.seia.service;

import java.util.List;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleProcessoAtoService {
	
	@Inject
	IDAO<ControleProcessoAto> controleProcessoAtoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ControleProcessoAto controleProcessoAto)  {
		controleProcessoAtoDAO.salvarOuAtualizar(controleProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<ControleProcessoAto> controleProcessoAto)  {
		controleProcessoAtoDAO.salvarEmLote(controleProcessoAto);
	}
	
	public List<ControleProcessoAto> listarPorProcessoAto(Integer ideProcessoAto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class)
				.createAlias("idePessoaFisica", "funcionario", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideProcessoAto", "processoAto", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideStatusProcessoAto", "status", JoinType.LEFT_OUTER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideControleProcessoAto"), "ideControleProcessoAto")
					.add(Projections.property("dscJustificativaStatus"), "dscJustificativaStatus")
					.add(Projections.property("dtcControleProcessoAto"), "dtcControleProcessoAto")
					.add(Projections.property("indAprovado"), "indAprovado")
					.add(Projections.property("numPrazoValidade"), "numPrazoValidade")
					
					.add(Projections.property("funcionario.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
					.add(Projections.property("processoAto.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
					.add(Projections.property("status.ideStatusProcessoAto"), "ideStatusProcessoAto.ideStatusProcessoAto")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class))
				
				.add(Restrictions.eq("processoAto.ideProcessoAto", ideProcessoAto));
			
			return controleProcessoAtoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ControleProcessoAto buscarControleProcessoAtoPorIdeProcessoAto(Integer ideProcessoAto, Integer ideTipoStatus)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class);
		
		
		criteria.createAlias("idePessoaFisica", "idePessoaFisica", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideProcessoAto.ideProcessoAto", ideProcessoAto));
		criteria.add(Restrictions.eq("ideStatusProcessoAto.ideStatusProcessoAto", ideTipoStatus));
		criteria.addOrder(Order.desc("ideControleProcessoAto"));
		List<ControleProcessoAto> lista = controleProcessoAtoDAO.listarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		return null;
	}
}
