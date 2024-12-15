package br.gov.ba.seia.service;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.TipoSeguimentoPctDAOImpl;
import br.gov.ba.seia.entity.TipoSeguimentoPct;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSeguimentoPctService {

	@EJB
	private TipoSeguimentoPctDAOImpl tipoSeguimentoPctDAOImpl;
	
	public TipoSeguimentoPct obterTipoSeguimentoPct(Integer ideTipoSeguimentoPct) throws Exception {
		return tipoSeguimentoPctDAOImpl.obterTipoSeguimentoPct(ideTipoSeguimentoPct);
	}
	
	public List<TipoSeguimentoPct> listarTipoSeguimentoPct() throws Exception{
		List<TipoSeguimentoPct> lista = tipoSeguimentoPctDAOImpl.listarTipoSeguimentoPct();
		
		TipoSeguimentoPct tipoSeguimentoPctOutro = null;
		
		for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			TipoSeguimentoPct tipoSeguimentoPct = (TipoSeguimentoPct) iterator.next();
			if ("Outros".equals(tipoSeguimentoPct.getDscTipoSeguimentoPct())) {
				tipoSeguimentoPctOutro = tipoSeguimentoPct;
				iterator.remove();
			}
		}
		
		lista.add(tipoSeguimentoPctOutro);
		
		return lista;
	}
}
