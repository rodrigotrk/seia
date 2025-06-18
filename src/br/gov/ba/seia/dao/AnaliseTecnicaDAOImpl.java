package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnaliseTecnicaDAOImpl {
	
	@Inject
	private IDAO<AnaliseTecnica> analiseTecnicaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		try {
			analiseTecnicaDAO.salvar(analiseTecnica);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		try {
			analiseTecnicaDAO.atualizar(analiseTecnica);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AnaliseTecnica buscarAnaliseTecnica(Integer ideProcesso){
		DetachedCriteria criteria = DetachedCriteria.forClass(AnaliseTecnica.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideAnaliseTecnica"), "ideAnaliseTecnica")
				.add(Projections.property("dtcFimAnaliseProcesso"), "dtcFimAnaliseProcesso")
				.add(Projections.property("dtcInicioAnaliseTecnica"), "dtcInicioAnaliseTecnica")
				.add(Projections.property("indAprovado"), "indAprovado")
				.add(Projections.property("observacao"), "observacao")
				.add(Projections.property("p.ideProcesso"), "ideProcesso.ideProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AnaliseTecnica.class))
		;
		
		return analiseTecnicaDAO.buscarPorCriteria(criteria);
	}
}