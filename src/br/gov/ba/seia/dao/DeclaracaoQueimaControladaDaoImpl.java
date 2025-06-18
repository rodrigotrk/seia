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

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoQueimaControladaDaoImpl {
	
	@Inject
	private IDAO<DeclaracaoQueimaControlada> dqcDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoQueimaControlada dqc) {
		try {
			dqcDAO.salvarOuAtualizar(dqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoQueimaControlada buscarPorAtoDeclaratorio(AtoDeclaratorio ad) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControlada.class)
					.createAlias("ideAtoDeclaratorio", "ad", JoinType.INNER_JOIN)
					.createAlias("ad.ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
					.createAlias("ad.ideRequerimento", "req", JoinType.INNER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControlada"), "ideDeclaracaoQueimaControlada")
							.add(Projections.property("indAceiteResponsabilidade"), "indAceiteResponsabilidade")
							.add(Projections.property("indCienteTermoCompromisso"), "indCienteTermoCompromisso")
							
							.add(Projections.property("ad.ideAtoDeclaratorio"), "ideAtoDeclaratorio.ideAtoDeclaratorio")
							.add(Projections.property("ad.dtcCriacao"), "ideAtoDeclaratorio.dtcCriacao")
							.add(Projections.property("ad.indConcluido"), "ideAtoDeclaratorio.indConcluido")
							
							.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideAtoDeclaratorio.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
							.add(Projections.property("req.ideRequerimento"), "ideAtoDeclaratorio.ideRequerimento.ideRequerimento")
							.add(Projections.property("req.numRequerimento"), "ideAtoDeclaratorio.ideRequerimento.numRequerimento")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControlada.class))
					
					.add(Restrictions.eq("ad.ideAtoDeclaratorio", ad.getIdeAtoDeclaratorio()));
					 
				 return dqcDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}