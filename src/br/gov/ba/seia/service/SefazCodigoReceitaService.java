package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SefazCodigoReceita;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SefazCodigoReceitaService {

	@Inject
	IDAO<SefazCodigoReceita> sefazCodigoReceitaImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SefazCodigoReceita buscarSefazCodigo(Integer codigoReceita) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SefazCodigoReceita.class)
				.add(Restrictions.eq("numCodigoCeceita", codigoReceita));
		
		return sefazCodigoReceitaImpl.buscarPorCriteriaMaxResult(criteria);
	}


}