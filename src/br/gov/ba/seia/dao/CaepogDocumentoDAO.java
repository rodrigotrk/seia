package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.enumerator.CaepogCategoriaDocumentoEnum;

public class CaepogDocumentoDAO {

	@Inject
	IDAO<CaepogDocumento> caepogDocumentoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogDocumento> listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum categoriaDocumento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogDocumento.class)
				.createAlias("ideCaepogCategoriaDocumento", "ideCaepogCategoriaDocumento", JoinType.INNER_JOIN)
				.add(Restrictions.eq("ideCaepogCategoriaDocumento.ideCaepogCategoriaDocumento", categoriaDocumento.getId()))
				.addOrder(Order.asc("ideCaepogDocumento"));
				
		return caepogDocumentoIDAO.listarPorCriteria(criteria);
	}
}
