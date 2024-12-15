package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.TipoVolumeFlorestalRemanescente;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVolumeFlorestalRemanescenteService {

	@Inject
	private IDAO<TipoVolumeFlorestalRemanescente> tipoVolumeFlorestalRemanescenteDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoVolumeFlorestalRemanescente> listarTipoVolumeFlorestalRemanescente() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoVolumeFlorestalRemanescente.class);
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideTipoVolumeFlorestalRemanescente"), "ideTipoVolumeFlorestalRemanescente")
					.add(Projections.property("nomTipoVolumeFlorestalRemanescente"), "nomTipoVolumeFlorestalRemanescente")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoVolumeFlorestalRemanescente.class))
				;
		return tipoVolumeFlorestalRemanescenteDAO.listarPorCriteria(criteria);
	}
}
