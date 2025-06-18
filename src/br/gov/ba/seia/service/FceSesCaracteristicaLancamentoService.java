/**
 * 	
 * @author rodrigo.santos
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceSesCaracteristicaLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceSesCaracteristicaLancamentoService {

	@Inject
	private IDAO<FceSesCaracteristicaLancamento> fceSesCaracteristicaLancamentoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCaracteristicaLancamento> listarFceSesCaracteristicaLancamento(FceSesCoordenadasLancamentoLocalizacaoGeografica fceLancamentoLocalizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSesCaracteristicaLancamento.class);
			criteria.createAlias("ideCaracteristicaEfluente", "caracteristica", JoinType.INNER_JOIN);
			criteria.createAlias("ideCoordenadaFceLancamentoLocalizacaoGeografica", "fceLancamentoLoc", JoinType.INNER_JOIN);
			
		
		criteria.add(Restrictions.eq("ideCoordenadaFceLancamentoLocalizacaoGeografica", fceLancamentoLocalizacao));
		
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideFceSesCaracteristicaLancamento"), "ideFceSesCaracteristicaLancamento")
				.add(Projections.property("valorEfluenteTratado"), "valorEfluenteTratado")
				.add(Projections.property("ValorEficienciRemocao"), "ValorEficienciRemocao")
				.add(Projections.property("valorBrutoEfluente"), "valorBrutoEfluente")
				.add(Projections.property("fceLancamentoLoc.ideFceSesLancamentoLocalizacaoGeografica"), "ideCoordenadaFceLancamentoLocalizacaoGeografica.ideFceSesLancamentoLocalizacaoGeografica")
				.add(Projections.property("caracteristica.ideCaracteristicaEfluente"), "ideCaracteristicaEfluente.ideCaracteristicaEfluente")
				.add(Projections.property("caracteristica.nomCaracteristicaEfluente"), "ideCaracteristicaEfluente.nomCaracteristicaEfluente"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSesCaracteristicaLancamento.class))	;
		
		return fceSesCaracteristicaLancamentoIDAO.listarPorCriteria(criteria, Order.asc("ideCoordenadaFceLancamentoLocalizacaoGeografica"));
	}
}