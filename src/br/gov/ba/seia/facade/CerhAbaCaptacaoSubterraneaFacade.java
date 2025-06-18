package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CerhNaturezaPocoDAOImpl;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhNaturezaPoco;

/**
 * @author eduardo.fernandes 
 * @since 20/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbaCaptacaoSubterraneaFacade extends CerhAbasCaptacoesFacade {

	@EJB
	private CerhNaturezaPocoDAOImpl naturezaPocoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNaturezaPoco> listarCerhNaturezaPoco() {
		return naturezaPocoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhNaturezaPoco carregarCerhNaturezaPoco(Integer ide) {
		return naturezaPocoDAO.carregar(ide);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void prepararLocalizacaoGeografica(CerhLocalizacaoGeografica cerhLocalizacaoGeografica)  {
		super.prepararLocalizacaoGeografica(cerhLocalizacaoGeografica);
		cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().setSistemaAquifero(super.localizacaoGeograficaServiceFacade.getSistemaAquifero(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
	}
}
