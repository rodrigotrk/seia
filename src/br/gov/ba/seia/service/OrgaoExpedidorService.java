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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrgaoExpedidorService {
	
	@Inject
	private IDAO<OrgaoExpedidor> orgaoExpedidorDAO;
	
	/** Mover para o DAO*/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrgaoExpedidor> listar(){
		return 
			orgaoExpedidorDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(OrgaoExpedidor.class)
						.addOrder(Order.asc("dscOrgaoExpedidor"))
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideOrgaoExpedidor"), "ideOrgaoExpedidor")
							.add(Projections.property("dscOrgaoExpedidor"), "dscOrgaoExpedidor")
							.add(Projections.property("dscSiglaOrgaoExpedidor"), "dscSiglaOrgaoExpedidor")
							.add(Projections.property("dtcCriacao"), "dtcCriacao")
							.add(Projections.property("indConselhoProfissional"), "indConselhoProfissional")
							.add(Projections.property("indExcluido"), "indExcluido")
							.add(Projections.property("dtcExclusao"), "dtcExclusao")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(OrgaoExpedidor.class)));
	}
	

	
	/** Mover para o DAO*/
	public OrgaoExpedidor carregarOrgaoExpedidor(Integer id){
		return orgaoExpedidorDAO.carregarGet(id);
	}
			
	/** Mover para o DAO*/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrgaoExpedidor> listarOrgaoExpedidor(TipoIdentificacao tipoIdentificacao) {
		
		if(Util.isNullOuVazio(tipoIdentificacao))
			return orgaoExpedidorDAO.listarTodos();
		else{
			DetachedCriteria criteria = DetachedCriteria.forClass(OrgaoExpedidor.class);
			criteria.createAlias("tipoIdentificacaoOrgaoExpedidorCollection", "tpIdentOrgaoExpedCollection");
			criteria.add(Restrictions.eq("tpIdentOrgaoExpedCollection.ideTipoIdentificacao.ideTipoIdentificacao", tipoIdentificacao.getIdeTipoIdentificacao()));
			return orgaoExpedidorDAO.listarPorCriteria(criteria, Order.asc("dscOrgaoExpedidor"));
		}
	}

}
