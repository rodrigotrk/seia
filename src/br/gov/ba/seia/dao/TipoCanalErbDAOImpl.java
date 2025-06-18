package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoCanalErb;

/***
 * 
 * @author luis
 *
 */
public class TipoCanalErbDAOImpl {

	@Inject
	private IDAO<TipoCanalErb> daoTipoCanalErb;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoCanalErb> listar() {
		return daoTipoCanalErb.listarTodos();
	}
	
}
