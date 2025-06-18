package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceReservaAgua;
import br.gov.ba.seia.entity.ReservaAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceReservaAguaService {

	@Inject
	private IDAO<FceReservaAgua> fceReservaAguaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceReservaAgua(FceReservaAgua fceReservaAgua)  {
		fceReservaAguaIDAO.salvarOuAtualizar(fceReservaAgua);
	}

	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceReservaAgua(ReservaAgua reservaAgua)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceReservaAgua", reservaAgua.getIdeFceReservaAgua().getIdeFceReservaAgua());
		fceReservaAguaIDAO.executarNamedQuery("FceReservaAgua.excluirByIdeFceReservaAgua", params);
	}
}