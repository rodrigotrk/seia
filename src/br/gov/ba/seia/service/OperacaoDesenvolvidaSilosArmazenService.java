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

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OperacaoDesenvolvidaSilosArmazenService {

	@Inject
	IDAO<OperacaoDesenvolvidaSilosArmazen> idao;
	
	
	public List<OperacaoDesenvolvidaSilosArmazen> listarOperacaoDesenvolvidaSilosArmazen(){
		DetachedCriteria criteria = DetachedCriteria.forClass(OperacaoDesenvolvidaSilosArmazen.class);
		
		criteria.add(Restrictions.isNull("ideOperacaoDesenvolvidaPai"));
		
		List<OperacaoDesenvolvidaSilosArmazen> listaPrincipal = idao.listarPorCriteria(criteria, Order.asc("ideOperacaoDesenvolvidaSilosArmazens"));
		
		List<OperacaoDesenvolvidaSilosArmazen> listaSecundaria = listarOperacaDesenvolvidaSilosArmazenFilhos();
		
		for(OperacaoDesenvolvidaSilosArmazen operacaoPai : listaPrincipal){
			
			operacaoPai.setOperacaoDesenvolvidaSilosArmazenAuxiliar(new ArrayList<OperacaoDesenvolvidaSilosArmazen>());
			for(OperacaoDesenvolvidaSilosArmazen operacaoFilho : listaSecundaria){
				
				if(operacaoPai.getIdeOperacaoDesenvolvidaSilosArmazens().equals(operacaoFilho.getIdeOperacaoDesenvolvidaPai())){
					operacaoPai.getOperacaoDesenvolvidaSilosArmazenAuxiliar().add(operacaoFilho);
				}
			}
		}
		
				
		return listaPrincipal;
	}
	
	
	public List<OperacaoDesenvolvidaSilosArmazen> listarOperacaDesenvolvidaSilosArmazenFilhos() {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(OperacaoDesenvolvidaSilosArmazen.class);

		criteria.add(Restrictions.isNotNull("ideOperacaoDesenvolvidaPai"));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideOperacaoDesenvolvidaPai"));
	}
}
