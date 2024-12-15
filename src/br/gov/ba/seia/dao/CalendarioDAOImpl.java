package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Calendario;
import br.gov.ba.seia.entity.generic.AbstractEntity;

public class CalendarioDAOImpl {
	
	
	@Inject
	IDAO<Calendario>calendarioDAO;
	
	
	public List<Calendario> listarFeriados() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Calendario.class);
		criteria.add(Restrictions.gt("dtcFeriado", new Date()));
		List<Calendario>  lista = calendarioDAO.listarPorCriteria(criteria, Order.asc(null));
        if(lista.isEmpty()){
        	return new ArrayList<Calendario>();	
        }
		return lista;
		
	}
	
	public Calendario obterFeriadoPorData(Date data) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Calendario.class);
		criteria.add(Restrictions.eq("dtcFeriado", data));
		return calendarioDAO.buscarPorCriteria(criteria);
	}

}
