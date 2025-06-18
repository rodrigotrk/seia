package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PctImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PctImovelRuralDAOImpl {

	@Inject
	private IDAO<PctImovelRural> idao;
	
	public PctImovelRural buscarPctImovelRural(ImovelRural imovelRural) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctImovelRural.class);
		
		criteria.setFetchMode("tipoSeguimentoPctCollection", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		
		return idao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPctImovelRural(PctImovelRural pctImovelRural) throws Exception{
		
		idao.salvarOuAtualizar(pctImovelRural);
	}
	
	public boolean isPCT(ImovelRural imovelRural) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PctImovelRural.class);
		
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		
		return idao.isExiste(criteria);
	}
}
