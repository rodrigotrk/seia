package br.gov.ba.seia.service.rules.impl.business;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.service.rules.impl.BusinessManager;
/**
 * Classe de negocio do ato ambiental business
 * @author 
 *
 */
public class AtoAmbientalBusiness extends BusinessManager<AtoAmbiental> {

	@Override
	public boolean prePersist(AtoAmbiental aBean, CrudOperationEnum crudOperation) {
		if(CrudOperationEnum.INSERT == crudOperation){
			
			Criteria criteria = this.getSession().createCriteria(AtoAmbiental.class);
			criteria.add(Restrictions.like("nomAtoAmbiental", aBean.getNomAtoAmbiental(), MatchMode.EXACT));
			
			AtoAmbiental doc = (AtoAmbiental) criteria.uniqueResult();
			
			if(doc != null){
				throw new AppExceptionError("Já existe um ato ambiental com este nome.");
			}
		}
		return true;
	}

	@Override
	public boolean postPersist(AtoAmbiental aBean, CrudOperationEnum crudOperation){
		
		return false;
	}

	@Override
	public boolean preFind() {
		
		return false;
	}
}
