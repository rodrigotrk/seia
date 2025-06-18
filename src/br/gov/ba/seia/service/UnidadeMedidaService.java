package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.UnidadeMedidaDAOImpl;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.TipologiaEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UnidadeMedidaService {

	@Inject
	UnidadeMedidaDAOImpl unidadeMedidaDAOImpl;
	@Inject
	private IDAO<UnidadeMedida> UnidadeMedidalDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUnidadeMedida(UnidadeMedida unidadeMedida) throws Exception {
		unidadeMedidaDAOImpl.salvarUnidadeMedida(unidadeMedida);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> listarUnidadeMedida() throws Exception {
		return unidadeMedidaDAOImpl.listarUnidadeMedida();
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> filtrarListaUnidadeMedida(UnidadeMedida unidadeMedida) throws Exception {
		return 	unidadeMedidaDAOImpl.filtrarListaUnidadeMedida(unidadeMedida);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUnidadeMedida(UnidadeMedida unidadeMedida) throws Exception {
		unidadeMedidaDAOImpl.excluirUnidadeMedida(unidadeMedida);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> filtrarListaUnidadeMedidaSouce(Collection<UnidadeMedida> listunidadeMedidas) throws Exception {
		 return unidadeMedidaDAOImpl.filtrarListaUnidadeMedidaSouce(listunidadeMedidas);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeMedida> listarUnidadeMedida(List<UnidadeMedida> listunidadeMedidas) throws Exception {
		 return unidadeMedidaDAOImpl.listarUnidadeMedida(listunidadeMedidas);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> ListarUnidadeMedidaTipologia(TipologiaGrupo tipologiaGrupo) throws Exception {
		//Collection<UnidadeMedida> uM =UnidadeMedidaDAOImpl.listarUnidadeMedidaTipologia(tipologiaGrupo); 
		 return unidadeMedidaDAOImpl.listarUnidadeMedidaTipologia(tipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> listarUnidadeMedidaTipologia(TipologiaEnum tipologiaEnum) throws Exception {
		return unidadeMedidaDAOImpl.listarUnidadeMedidaTipologia(tipologiaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeMedida> filtrarListaUnidadeMedida(List<UnidadeMedida> listaUnidadeMedidas) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedida.class);
		List<UnidadeMedida> listaUMedida = new ArrayList<UnidadeMedida>();
		for (UnidadeMedida unidadeMedida : listaUnidadeMedidas) {
			criteria.add(Restrictions.eq("ideUnidadeMedida", unidadeMedida.getIdeUnidadeMedida()));
			listaUMedida.add(UnidadeMedidalDAO.buscarPorCriteria(criteria));
			criteria = DetachedCriteria.forClass(UnidadeMedida.class);
		}
		return listaUMedida;
	}
	
	
}
