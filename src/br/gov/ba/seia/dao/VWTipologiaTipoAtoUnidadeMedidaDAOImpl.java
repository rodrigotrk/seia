package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.VwTipologiaTipoAtoUnidadeMedida;



public class VWTipologiaTipoAtoUnidadeMedidaDAOImpl {
	
	@Inject
	IDAO<VwTipologiaTipoAtoUnidadeMedida> vwTipologiaTipoAtoUnidadeMedidaDAO;

	
	public List<VwTipologiaTipoAtoUnidadeMedida> listarVwTipologiaTipoAtoUnidadeMedidaPorTipologia(Integer ideTipologia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwTipologiaTipoAtoUnidadeMedida.class);
		criteria.add(Restrictions.eq("ideTipologia", ideTipologia));
		return vwTipologiaTipoAtoUnidadeMedidaDAO.listarPorCriteria(criteria);
	}
	
	
	
	public List<VwTipologiaTipoAtoUnidadeMedida> listarVwTipologiaTipoAtoUnidadeMedidaPorTipologiaGrupo(Integer ideTipologiaGrupo)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(VwTipologiaTipoAtoUnidadeMedida.class);
		criteria.add(Restrictions.eq("idetipologiagrupo", ideTipologiaGrupo));
		return vwTipologiaTipoAtoUnidadeMedidaDAO.listarPorCriteria(criteria);
	}
	
	public List<VwTipologiaTipoAtoUnidadeMedida> listarVwTipologiaTipoAtoUnidadeMedida(Integer ideTipologia,Integer ideTipologiaGrupo)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(VwTipologiaTipoAtoUnidadeMedida.class);
		criteria.add(Restrictions.eq("ideTipologia", ideTipologiaGrupo));
		criteria.add(Restrictions.eq("idetipologiagrupo", ideTipologia));
		return vwTipologiaTipoAtoUnidadeMedidaDAO.listarPorCriteria(criteria);
	}
	
	
	
}