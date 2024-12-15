package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.entity.Cerh;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhHistoricoServiceFacade {
	
	@EJB
	private CerhDAOImpl cerhDAOImpl;
	
	public List<Cerh> listarHistorico(Cerh cerh, Integer first, Integer pageSize) throws Exception {
		return cerhDAOImpl.listarHistorico(cerh.getIdeCerh(), first, pageSize);
	}
	
	public Integer listarHistoricoCount(Cerh cerh) throws Exception {
		return cerhDAOImpl.listarHistoricoCount(cerh.getIdeCerh());
	}

	public Cerh getCerhAnterior(Cerh cerh) throws Exception {
		return cerhDAOImpl.getCerhAnterior(cerhDAOImpl.buscar(cerh));
	}

}
