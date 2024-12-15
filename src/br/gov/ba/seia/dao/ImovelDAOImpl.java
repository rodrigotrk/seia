package br.gov.ba.seia.dao;

import java.util.Collection;
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

import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


/**
 * @author Alexandre Queiroz
 * @ 
 * @since 11/12/2016
 * 
 **/

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelDAOImpl {

	@Inject
	private IDAO<Imovel> imovelDAO;
	

	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarRequerimentoImovelPor(Requerimento requerimento) {
		
		try{
			 DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Imovel.class)
				.createAlias("imovelUrbano", "ideImovelUrbano", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovelRural", "ideImovelRural", JoinType.LEFT_OUTER_JOIN)
				.createAlias("florestalCollection","ideFlorestal")
				.createAlias("ideFlorestal.ideRequerimento","ideRequerimento")
				
					.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideImovel"),"ideImovel")
					.add(Projections.property("ideImovelUrbano.ideImovelUrbano"),"imovelUrbano.ideImovelUrbano")
					.add(Projections.property("ideImovelRural.ideImovelRural"),"imovelRural.ideImovelRural")
					.add(Projections.property("ideImovelRural.desDenominacao"),"imovelRural.desDenominacao")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class));
			 
			return imovelDAO.listarPorCriteria(detachedCriteria);
			 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarImoveisPor(Notificacao notificacao)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class)
				.createAlias("imovelRural","ir", JoinType.LEFT_OUTER_JOIN)
				.createAlias("motivoNotificacaoImovelCollection","mni", JoinType.INNER_JOIN);

		if(notificacao != null && !Util.isNullOuVazio(notificacao.getNotificacaoMotivoNotificacaoCollection())) {
			criteria.add(Restrictions.in("mni.ideNotificacaoMotivoNotificacao", notificacao.getNotificacaoMotivoNotificacaoCollection()));
		}

		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideImovel"),"ideImovel")
				.add(Projections.groupProperty("ir.ideImovelRural"),"imovelRural.ideImovelRural")
				.add(Projections.groupProperty("ir.desDenominacao"),"imovelRural.desDenominacao")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
		;

		return imovelDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarAllImoveisPor(Integer ideProcesso)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Florestal.class);
		criteria
				.createAlias("ideRequerimento","r", JoinType.INNER_JOIN)
				.createAlias("r.processoCollection","p", JoinType.INNER_JOIN)
				.createAlias("ideImovel","i", JoinType.INNER_JOIN)
				.createAlias("i.imovelRural","ir", JoinType.LEFT_OUTER_JOIN)

				.add(Restrictions.eq("p.ideProcesso", ideProcesso))

				.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("i.ideImovel"),"ideImovel")
						.add(Projections.groupProperty("ir.ideImovelRural"),"imovelRural.ideImovelRural")
						.add(Projections.groupProperty("ir.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.groupProperty("ir.numMatricula"),"imovelRural.numMatricula")
				)
				.addOrder(Order.asc("ir.desDenominacao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
		;

		return imovelDAO.listarPorCriteria(criteria);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarImoveisPor(Integer ideProcesso)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Florestal.class);
		criteria
				.createAlias("ideRequerimento","r", JoinType.INNER_JOIN)
				.createAlias("r.processoCollection","p", JoinType.INNER_JOIN)
				.createAlias("ideImovel","i", JoinType.INNER_JOIN)
				.createAlias("i.imovelRural","ir", JoinType.INNER_JOIN)

				.add(Restrictions.eq("p.ideProcesso", ideProcesso))

				.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("i.ideImovel"),"ideImovel")
						.add(Projections.groupProperty("ir.ideImovelRural"),"imovelRural.ideImovelRural")
						.add(Projections.groupProperty("ir.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.groupProperty("ir.numMatricula"),"imovelRural.numMatricula")
				)
				.addOrder(Order.asc("ir.desDenominacao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
		;

		return imovelDAO.listarPorCriteria(criteria);
	}
	
	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(ImovelRural imovelRural){
		
		try{
			return imovelDAO.buscarPorCriteria(
				DetachedCriteria.forClass(Imovel.class)
					.createAlias("imovelRural", "ideImovelRural", JoinType.INNER_JOIN)
					.createAlias("imovelRural.imovelRuralSicar", "ideImovelRuralSicar", JoinType.INNER_JOIN)
					.createAlias("ideEndereco", "ideEndereco", JoinType.INNER_JOIN)
					.createAlias("ideEndereco.ideLogradouro", "ideLogradouro", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ideLogradouro.ideMunicipio", "ideMunicipio", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideImovelRuralSicar.numSicar", imovelRural.getImovelRuralSicar().getNumSicar()))
					
					.setProjection(Projections.projectionList()
						.add(Projections.property("ideImovel"),"ideImovel")
						.add(Projections.property("ideImovelRural.ideImovelRural"),"imovelRural.ideImovelRural")
						.add(Projections.property("ideImovelRural.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.property("ideImovelRuralSicar.ideImovelRuralSicar"),"ideImovelRuralSicar.ideImovelRuralSicar")
						.add(Projections.property("ideImovelRuralSicar.numSicar"),"imovelRuralSicar.numSicar")
						.add(Projections.property("ideEndereco.ideEndereco"),"ideEndereco.ideEndereco")
						.add(Projections.property("ideLogradouro.ideLogradouro"),"ideEndereco.ideLogradouro.ideLogradouro")
						.add(Projections.property("ideMunicipio.ideMunicipio"),"ideEndereco.ideLogradouro.ideMunicipio.ideMunicipio")
						.add(Projections.property("ideMunicipio.indBloqueioDQC"),"ideEndereco.ideLogradouro.ideMunicipio.indBloqueioDQC")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
				);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(String numCar) {
		
		try{
			return imovelDAO.buscarPorCriteria(
				DetachedCriteria.forClass(Imovel.class)
					.createAlias("imovelRural", "ideImovelRural", JoinType.INNER_JOIN)
					.createAlias("imovelRural.imovelRuralSicar", "ideImovelRuralSicar", JoinType.INNER_JOIN)
					.createAlias("ideEndereco", "ideEndereco", JoinType.INNER_JOIN)
					.createAlias("ideEndereco.ideLogradouro", "ideLogradouro", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ideLogradouro.ideMunicipio", "ideMunicipio", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideImovelRuralSicar.numSicar", numCar))
					
					.setProjection(Projections.projectionList()
						.add(Projections.property("ideImovel"),"ideImovel")
						.add(Projections.property("ideImovelRural.ideImovelRural"),"imovelRural.ideImovelRural")
						.add(Projections.property("ideImovelRural.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.property("ideImovelRuralSicar.ideImovelRuralSicar"),"ideImovelRuralSicar.ideImovelRuralSicar")
						.add(Projections.property("ideImovelRuralSicar.numSicar"),"imovelRuralSicar.numSicar")
						.add(Projections.property("ideEndereco.ideEndereco"),"ideEndereco.ideEndereco")
						.add(Projections.property("ideLogradouro.ideLogradouro"),"ideEndereco.ideLogradouro.ideLogradouro")
						.add(Projections.property("ideMunicipio.ideMunicipio"),"ideEndereco.ideLogradouro.ideMunicipio.ideMunicipio")
						.add(Projections.property("ideMunicipio.indBloqueioDQC"),"ideEndereco.ideLogradouro.ideMunicipio.indBloqueioDQC")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
				);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel carregarImovel(RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel) {
		
		try{
			return imovelDAO.buscarPorCriteria(
				DetachedCriteria.forClass(Imovel.class)
					.createAlias("imovelRural", "rural", JoinType.LEFT_OUTER_JOIN)
					.createAlias("imovelUrbano", "urbano", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideImovel", registroFlorestaProducaoImovel.getIdeImovel().getIdeImovel()))
					
					.setProjection(Projections.projectionList()
						
						.add(Projections.property("ideImovel"),"ideImovel")
						.add(Projections.property("rural.ideImovelRural"),"imovelRural.ideImovelRural")
						
						.add(Projections.property("rural.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.property("urbano.ideImovelUrbano"),"imovelUrbano.ideImovelUrbano")
						
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
				);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
