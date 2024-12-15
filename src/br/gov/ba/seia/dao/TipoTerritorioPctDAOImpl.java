package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoTerritorioPct;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTerritorioPctDAOImpl {

	@Inject
	private IDAO<TipoTerritorioPct> idao;
	
	@Inject
	private IDAO<TipoDocumentoImovelRural> idaoTipoVinculoTerritorio;
	
	public TipoTerritorioPct obterTipoTerritorioPct(Integer ideTipoTerritorioPct) throws Exception {
		return idao.carregarGet(ideTipoTerritorioPct);
	}
	
	public List<TipoTerritorioPct> listarTipoTerritorioPct() throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoTerritorioPct.class);
		
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideTipoTerritorioPct"));
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoDocumentoImovelRural> findByIdeTipoVinculoImovelPorTerritorio(Integer ideTipoVinculoImovel) throws Exception {
		
		if(!Util.isNullOuVazio(ideTipoVinculoImovel)){
			if(ideTipoVinculoImovel == 4){
				ideTipoVinculoImovel = 2;
			}
		}
		
		HashMap< String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoTerritorioPct", ideTipoVinculoImovel);
		Collection<TipoDocumentoImovelRural> listaMunicipio = idaoTipoVinculoTerritorio.buscarPorNamedQuery("TipoDocumentoImovelRural.findByideTipoTerritorioPct", params);
		return listaMunicipio;
	}
}
