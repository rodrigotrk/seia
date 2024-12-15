package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NotificacaoGeracaoDaeImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.GeoBahia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoGeracaoDaeServiceFacade {

	@Inject
	private IDAO<GeoBahia> daoGeobahia;
		
	@EJB
	private NotificacaoGeracaoDaeImpl notificacaoGeracaoDaeImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cerh> listarCerh(Integer first, Integer pageSize, Map<String, Object> params) throws Exception {
		return notificacaoGeracaoDaeImpl.listarCerh(first, pageSize, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarCerhCount(Map<String, Object> params) throws Exception {
		return notificacaoGeracaoDaeImpl.listarCerhCount(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<GeoBahia> listarRPGA() {
		List<GeoBahia> listGeobahia = new ArrayList<GeoBahia>();
		String sql = new String("SELECT gid, nom_rpga FROM geo_rpga ORDER BY nom_rpga");
		List<GeoBahia> buscarPorNativeQuery = daoGeobahia.buscarPorNativeQuery(sql, null);
		for (Object geoBahia : buscarPorNativeQuery) {
			Object[] obj = ((Object[])geoBahia);
			GeoBahia objGeoBahia = new GeoBahia();
			objGeoBahia.setGid(Integer.parseInt(obj[0].toString()));
			objGeoBahia.setNome(obj[1].toString());
			
			listGeobahia.add(objGeoBahia);
		}
		return listGeobahia;
	}
	
	
}
