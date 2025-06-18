package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoPoco;
import br.gov.ba.seia.enumerator.TipoPocoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoPocoService {

	@Inject
	private IDAO<TipoPoco> TipoPocoIDAO;
	
	/**
	 * listar tipo poco
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPoco> listarTipoPoco() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoPoco.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("dscTipoPoco"));
		List<TipoPoco> lista = TipoPocoIDAO.listarPorCriteria(criteria);
		adicionarOutrosNaUltimaPosicao(lista);
		return lista;
	}

	private void adicionarOutrosNaUltimaPosicao(List<TipoPoco> lista) {
		TipoPoco outros = new TipoPoco(TipoPocoEnum.OUTROS.getId());
		if(lista.contains(outros)){
			outros = lista.get(lista.indexOf(outros));
			lista.remove(outros);
			lista.add(outros);
		}
		
	}
	
	
}
