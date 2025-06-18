package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.OperacaoDesenvolvidaSilosArmazen;
import br.gov.ba.seia.entity.TipoCombustivelSiloArmazen;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCombustivelSiloArmazenService {

	@Inject
	private IDAO<TipoCombustivelSiloArmazen> idao;
	
	
	public List<TipoCombustivelSiloArmazen> listarTipoCombustivelSiloArmazen() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoCombustivelSiloArmazen.class);
		
		criteria.add(Restrictions.isNull("ideTipoCombustivelSiloArmazensPai"));
		
		List<TipoCombustivelSiloArmazen> listaPrincipal = idao.listarPorCriteria(criteria, Order.asc("ideTipoCombustivelSiloArmazens"));
		
		List<TipoCombustivelSiloArmazen> listaSecundaria = listarTipoCombustivelSiloArmazenFilhos();
		
		for(TipoCombustivelSiloArmazen operacaoPai : listaPrincipal){
			
			operacaoPai.setTipoCombustivelSiloArmazensAuxiliar(new ArrayList<TipoCombustivelSiloArmazen>());
			for(TipoCombustivelSiloArmazen operacaoFilho : listaSecundaria){
				
				if(operacaoPai.getIdeTipoCombustivelSiloArmazens() == operacaoFilho.getIdeTipoCombustivelSiloArmazensPai()){
					operacaoPai.getTipoCombustivelSiloArmazensAuxiliar().add(operacaoFilho);
				}
			}
		}
		
		return listaPrincipal;
	}	
	
	public List<TipoCombustivelSiloArmazen> listarTipoCombustivelSiloArmazenFilhos() throws Exception {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(TipoCombustivelSiloArmazen.class);

		criteria.add(Restrictions.isNotNull("ideTipoCombustivelSiloArmazensPai"));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideTipoCombustivelSiloArmazensPai"));
	}
	
}
