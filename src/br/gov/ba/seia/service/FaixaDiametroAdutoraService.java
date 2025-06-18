package br.gov.ba.seia.service;

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
import br.gov.ba.seia.entity.FaixaDiametroAdutora;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FaixaDiametroAdutoraService {

	@Inject
	private IDAO<FaixaDiametroAdutora> faixaDiametroIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FaixaDiametroAdutora> carregarFaixasDiametro(Integer tipoFase01, Integer tipoFase02, Integer tipoFase03 ) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FaixaDiametroAdutora.class);
		
		criteria.add(Restrictions.or(
				Restrictions.or(
						Restrictions.eq("tipoFaseAgua", tipoFase01),
						Restrictions.eq("tipoFaseAgua", tipoFase02)
						),
				Restrictions.eq("tipoFaseAgua", tipoFase03)
				));
		
		return faixaDiametroIDAO.listarPorCriteria(criteria, null);
	}
	
}
