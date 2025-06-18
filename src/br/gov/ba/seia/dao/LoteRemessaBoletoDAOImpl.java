package br.gov.ba.seia.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.entity.LoteRemessaBoleto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoteRemessaBoletoDAOImpl {
	
	
	@Inject
	private IDAO<LoteRemessaBoleto> loteRemessaBoletoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LoteRemessaBoleto> listarRemessasaPorLote(LoteBoleto lote) {
		
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideLoteBoleto", lote.getIdeLoteBoleto());
		
		return loteRemessaBoletoDAO.buscarPorNamedQuery("LoteRemessaBoleto.findByIdeLoteBoleto", parametros);
	}
	
	public void salvar(LoteRemessaBoleto loteRemessaBoleto)  {		
		loteRemessaBoletoDAO.salvar(loteRemessaBoleto);
	}
	
	public void atualizar(LoteRemessaBoleto loteRemessaBoleto)  {		
		loteRemessaBoletoDAO.atualizar(loteRemessaBoleto);
	}
}
