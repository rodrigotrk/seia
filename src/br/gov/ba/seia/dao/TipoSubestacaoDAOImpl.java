package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoSubestacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSubestacaoDAOImpl {

	@Inject
	IDAO<TipoSubestacao> tipoSubestacaoDAOI;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSubestacao> buscarTipoSubestacao() {

		DetachedCriteria criteria = DetachedCriteria.forClass(
				TipoSubestacao.class, "ts");

		criteria.add(Restrictions.eq("ts.indAtivo", true));

		return tipoSubestacaoDAOI.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoSubestacao buscarPorId(TipoSubestacao tipoSubestacao){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(
				TipoSubestacao.class, "ts");

		criteria.add(Restrictions.eq("ts.ideTipoSubestacao", tipoSubestacao.getIdeTipoSubestacao()));

		return tipoSubestacaoDAOI.buscarPorCriteria(criteria);
		
	}

}
