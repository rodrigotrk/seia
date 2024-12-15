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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoEnergia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEnergiaDAOImpl {

	@Inject
	private IDAO<TipoEnergia> tipoEnergiaDAOImplementacao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<TipoEnergia> buscarTiposEnergias()  {

		DetachedCriteria  criteria = DetachedCriteria.forClass(TipoEnergia.class, "te");
		
		criteria.add(Restrictions.eq("te.indAtivo", true));
		
		return tipoEnergiaDAOImplementacao.listarPorCriteria(criteria);
	}

}

