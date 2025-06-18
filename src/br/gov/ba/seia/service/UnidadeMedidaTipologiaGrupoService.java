package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.UnidadeMedidaTipologiaGrupoDAOImpl;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UnidadeMedidaTipologiaGrupoService {

	@Inject
	UnidadeMedidaTipologiaGrupoDAOImpl unidadeMedidaTipologiaGrupoDAOImpl;
	
	@Inject
	IDAO<UnidadeMedidaTipologiaGrupo> daoUnidadeMedidaTipologiaGrupo;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeMedidaTipologiaGrupo buscarPorTipologia(Tipologia tipologia) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class,"t")
			.createAlias("t.tipologiaGrupo", "tg")
			.createAlias("tg.unidadeMedidaTipologiaGrupo", "umtg")
			.createAlias("umtg.ideUnidadeMedida", "um")
			.createAlias("umtg.ideTipologiaGrupo", "tg_umtg")
			
			.setProjection(Projections.projectionList()
					.add(Projections.property("umtg.ideUnidadeMedidaTipologiaGrupo"), "ideUnidadeMedidaTipologiaGrupo")
					.add(Projections.property("um.ideUnidadeMedida"), "ideUnidadeMedida.ideUnidadeMedida")
					.add(Projections.property("tg_umtg.ideTipologiaGrupo"), "ideTipologiaGrupo.ideTipologiaGrupo"))
			
			.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
			.add(Restrictions.eq("tg.indExcluido", false))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(UnidadeMedidaTipologiaGrupo.class));
		
		return daoUnidadeMedidaTipologiaGrupo.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeMedidaTipologiaGrupo filtrarUnidadeMedidaTipologiaGrupoById(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo) throws Exception {
		return unidadeMedidaTipologiaGrupoDAOImpl.filtrarUnidadeMedidaTipologiaGrupoById(pUnidadeMedidaTipologiaGrupo);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo) throws Exception {
		unidadeMedidaTipologiaGrupoDAOImpl.salvarUnidadeMedidaTipologiaGrupo(pUnidadeMedidaTipologiaGrupo);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUnidadeMedidaTipologiaGrupos(Collection<UnidadeMedidaTipologiaGrupo> pColUnidadeMedidaTipologiaGrupo) throws Exception {
		unidadeMedidaTipologiaGrupoDAOImpl.salvarUnidadeMedidaTipologiaGrupos(pColUnidadeMedidaTipologiaGrupo);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo) throws Exception {
		unidadeMedidaTipologiaGrupoDAOImpl.atualizarUnidadeMedidaTipologiaGrupo(pUnidadeMedidaTipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  UnidadeMedidaTipologiaGrupo carregarUnidadeMedidaTipologiaGrupo(Integer id) throws Exception{
		return 	unidadeMedidaTipologiaGrupoDAOImpl.carregarUnidadeMedidaTipologiaGrupo(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<UnidadeMedidaTipologiaGrupo> recuperarUnidadeMedidaTipologiaGrupo(Integer id) throws Exception{
		return 	unidadeMedidaTipologiaGrupoDAOImpl.recuperarUnidadeMedidaTipologiaGrupo(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarUnidadeMedidaTipologiaGrupos(Collection<UnidadeMedidaTipologiaGrupo> pUnidadeMedidaTipologiaGrupo) throws Exception {
		unidadeMedidaTipologiaGrupoDAOImpl.deletarUnidadeMedidaTipologiaGrupos(pUnidadeMedidaTipologiaGrupo);
		Logger.getLogger("Unidade Medida Tipologia grupo  deletado com sucesso.");
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UnidadeMedidaTipologiaGrupo listarUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo, Integer ideUnidadeMedida ) throws Exception{
		return unidadeMedidaTipologiaGrupoDAOImpl.listarUnidadeMedidaTipologiaGrupo(tipologiaGrupo, ideUnidadeMedida);
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedidaTipologiaGrupo> filtrarUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo,Collection<UnidadeMedidaTipologiaGrupo> listUnidadeMedidaTipologiaGrupo) throws Exception {
		 return unidadeMedidaTipologiaGrupoDAOImpl.filtrarUnidadeMedidaTipologiaGrupo(tipologiaGrupo, listUnidadeMedidaTipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUnidadeMedidaTipologiaGrupo(Collection<UnidadeMedidaTipologiaGrupo> pColUnidadeMedidaTipologiaGrupo) throws Exception {
		unidadeMedidaTipologiaGrupoDAOImpl.excluirUnidadeMedidaTipologiaGrupo(pColUnidadeMedidaTipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) throws Exception{
		unidadeMedidaTipologiaGrupoDAOImpl.excluirUnidadeMedidaTipologiaGrupo(tipologiaGrupo);
	}
	
}