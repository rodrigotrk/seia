package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CerhCodReceitaTpUso;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCodigoReceitaDaoImpl extends BaseDAO<SefazCodigoReceita>{

	@Inject
	IDAO<SefazCodigoReceita> iDao;
	
	@Inject
	IDAO<CerhCodReceitaTpUso> idaoCerhCodReceitaTpUso;
	
	@Override
	protected IDAO<SefazCodigoReceita> getDao() {
		return iDao;
	}
	
	public SefazCodigoReceita getSefazCodigoReceita(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCodReceitaTpUso.class, "a");
		criteria.add(Restrictions.eq("a.tipoUsoRecursoHidrico", tipoUsoRecursoHidrico));
		return idaoCerhCodReceitaTpUso.buscarPorCriteria(criteria).getSefazCodigoReceita();
	}

}
