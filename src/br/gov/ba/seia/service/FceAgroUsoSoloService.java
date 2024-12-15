package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAgroUsoSolo;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoUsoSolo;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAgroUsoSoloService {
	
	@Inject
	private IDAO<FceAgroUsoSolo> fceAgroUsoSoloIDAO;
	@Inject
	private IDAO<TipoUsoSolo> tipoUsoSoloIDAO;
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAgroUsoSolo(FceAgroUsoSolo fceAgroUsoSolo) {
		fceAgroUsoSoloIDAO.salvar(fceAgroUsoSolo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAgroUsoSoloByIdeFceAgrossilvo(FceAgrossilvopastoril fceAgrossilvopastoril) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideFceAgrossilvopastoril", fceAgrossilvopastoril.getIdeFceAgrossilvopastoril());
		fceAgroUsoSoloIDAO.executarNamedQuery("FceAgroUsoSolo.excluirByIdeFceAgrossilvopastoril", maps);
	}
	/**
	 * @param fceAgrossilvopastoril
	 * @return
	 * @throws Exception
	 * @INFO Carrega todas as FceAgroUsoSolo pelo ide da fceAgrossilvopastoril
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAgroUsoSolo> listarFceAgroUsoSoloByIdeFceAgrossilvo(FceAgrossilvopastoril fceAgrossilvopastoril){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAgroUsoSolo.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril.ideFceAgrossilvopastoril", fceAgrossilvopastoril.getIdeFceAgrossilvopastoril()));
		criteria.setFetchMode("ideTipoUsoSolo", FetchMode.JOIN);
		return fceAgroUsoSoloIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoUsoSolo> listarTipoUsoSolo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoUsoSolo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoUsoSoloIDAO.listarPorCriteria(criteria,Order.asc("dscTipoUsoSolo"));
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double buscarValAreaImovelRuralByIdeRequerimento(Requerimento requerimento){

		return null;
	}

}
