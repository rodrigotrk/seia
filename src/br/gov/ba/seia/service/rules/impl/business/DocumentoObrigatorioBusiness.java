package br.gov.ba.seia.service.rules.impl.business;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.service.rules.impl.BusinessManager;
/**
 * Classe de negocio de documento obrigatorio 
 * @author 
 *
 */
public class DocumentoObrigatorioBusiness extends BusinessManager<DocumentoObrigatorio> {

	@Override
	public boolean prePersist(DocumentoObrigatorio aBean, CrudOperationEnum crudOperation){
		if(CrudOperationEnum.INSERT == crudOperation){
			
			Criteria criteria = this.getSession().createCriteria(DocumentoObrigatorio.class);
			criteria.add(Restrictions.like("nomDocumentoObrigatorio", aBean.getNomDocumentoObrigatorio(), MatchMode.EXACT));
			
			DocumentoObrigatorio doc = (DocumentoObrigatorio) criteria.uniqueResult();
			
			if(doc != null){
				throw new AppExceptionError("Já existe um documento Obrigatório com este nome.");
			}
		}
		return true;
	}

	@Override
	public boolean postPersist(DocumentoObrigatorio aBean, CrudOperationEnum crudOperation) {
		
		return false;
	}

	@Override
	public boolean preFind() {
		
		return false;
	}
}
