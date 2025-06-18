package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelUrbanoService {

	@Inject
	IDAO<ImovelUrbano> imovelUrbanoDAO;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarImovelUrbano(ImovelUrbano pImovelUrbano){
		imovelUrbanoDAO.salvarOuAtualizar(pImovelUrbano);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ImovelUrbano buscarByIdeImovel(Integer ideImovel) {
    	
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelUrbano.class);
		criteria.add(Restrictions.eq("ideImovelUrbano", ideImovel));
		return imovelUrbanoDAO.buscarPorCriteria(criteria);
    }
}