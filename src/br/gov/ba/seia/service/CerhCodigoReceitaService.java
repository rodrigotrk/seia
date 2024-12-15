/**
 * 
 */
package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CerhCodigoReceitaDaoImpl;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCodigoReceitaService {
	
	@EJB
	private CerhCodigoReceitaDaoImpl dao;

	
	public SefazCodigoReceita getCodigoReceita(TipoUsoRecursoHidrico recursoHidrico){
		return dao.getSefazCodigoReceita(recursoHidrico);
	}
}
