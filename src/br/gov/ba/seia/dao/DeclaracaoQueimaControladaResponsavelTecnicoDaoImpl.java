package br.gov.ba.seia.dao;

import java.util.List;

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

import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaResponsavelTecnico;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoQueimaControladaResponsavelTecnicoDaoImpl {
	
	@Inject
	private IDAO<DeclaracaoQueimaControladaResponsavelTecnico> dqcRtDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoQueimaControladaResponsavelTecnico dqcRt) {
		try {
			dqcRtDAO.salvarOuAtualizar(dqcRt);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(DeclaracaoQueimaControladaResponsavelTecnico dqcRt) {
		try {
			dqcRtDAO.remover(dqcRt);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaResponsavelTecnico> listarPorDeclaracaoQueimaControlada(DeclaracaoQueimaControlada dqc) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControladaResponsavelTecnico.class)
					.createAlias("ideDeclaracaoQueimaControlada", "dqc", JoinType.INNER_JOIN)
					.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControladaResponsavelTecnico"), "ideDeclaracaoQueimaControladaResponsavelTecnico")
							.add(Projections.property("dqc.ideDeclaracaoQueimaControlada"), "ideDeclaracaoQueimaControlada.ideDeclaracaoQueimaControlada")
							.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
							.add(Projections.property("pf.numCpf"), "idePessoaFisica.numCpf")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControladaResponsavelTecnico.class))
					
					.add(Restrictions.eq("dqc.ideDeclaracaoQueimaControlada", dqc.getIdeDeclaracaoQueimaControlada()));
					 
				 return dqcRtDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}