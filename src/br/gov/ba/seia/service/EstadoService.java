package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.EstadoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoService extends AbstractService<Estado>{
	private static final long serialVersionUID = 1L;

	@Inject
	private EstadoDAOImpl daoEstado;
	
	@Inject
	IDAO<Estado> estadoIdao;
	
	
	@Override
	public AbstractDAO<Estado> dao() {
		return daoEstado;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Estado> listar(){
		return super.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Estado buscar(Estado estado) {
		return super.buscar(estado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer buscarIdeEstadoByUF(String uf) {
		String sql = "SELECT ide_estado "
				+ "FROM estado "
				+ "WHERE upper(des_sigla) = '"+uf.toUpperCase()+"' limit 1";
				
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql);
								
		Integer obj = (Integer) lQuery.getSingleResult();
        if(!Util.isNull(obj)){
        	return obj; 
        }
			return null;
	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Estado obterEstadoByUf(String uf) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Estado.class)
				.add(Restrictions.eq("desSigla", uf));
		return estadoIdao.buscarPorCriteria(criteria);
	}
	
}
