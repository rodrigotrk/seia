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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovel;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoQueimaControladaImovelDaoImpl {
	
	@Inject
	private IDAO<DeclaracaoQueimaControladaImovel> dqcImovelDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoQueimaControladaImovel dqcImovel) {
		try {
			dqcImovelDAO.salvarOuAtualizar(dqcImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(DeclaracaoQueimaControladaImovel dqcImovel) {
		try {
			dqcImovelDAO.remover(dqcImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaImovel> listarPorDeclaracaoQueimaControlada(DeclaracaoQueimaControlada dqc) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControladaImovel.class)
					.createAlias("ideDeclaracaoQueimaControlada", "dqc", JoinType.INNER_JOIN)
					.createAlias("ideImovel", "imv", JoinType.INNER_JOIN)
					.createAlias("imv.imovelRural", "imvRural", JoinType.INNER_JOIN)
					.createAlias("ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN)
					.createAlias("locGeo.ideSistemaCoordenada", "sisCoord", JoinType.LEFT_OUTER_JOIN)
					.createAlias("locGeo.ideClassificacaoSecao", "classSecao", JoinType.LEFT_OUTER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControladaImovel"), "ideDeclaracaoQueimaControladaImovel")
							.add(Projections.property("indArrendado"), "indArrendado")
							
							.add(Projections.property("imv.ideImovel"), "ideImovel.ideImovel")
							.add(Projections.property("imvRural.ideImovelRural"), "ideImovel.imovelRural.ideImovelRural")
							.add(Projections.property("imvRural.desDenominacao"), "ideImovel.imovelRural.desDenominacao")
							
							.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
							.add(Projections.property("sisCoord.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
							.add(Projections.property("sisCoord.nomSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
							.add(Projections.property("classSecao.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
							.add(Projections.property("classSecao.nomClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.nomClassificacaoSecao")
							
							.add(Projections.property("dqc.ideDeclaracaoQueimaControlada"), "ideDeclaracaoQueimaControlada.ideDeclaracaoQueimaControlada")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControladaImovel.class))
					
					.add(Restrictions.eq("dqc.ideDeclaracaoQueimaControlada", dqc.getIdeDeclaracaoQueimaControlada()));
					 
				 return dqcImovelDAO.listarPorCriteria(criteria, Order.asc("ideDeclaracaoQueimaControladaImovel"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}