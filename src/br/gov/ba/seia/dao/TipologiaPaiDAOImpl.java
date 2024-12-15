package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Tipologia;



public class TipologiaPaiDAOImpl {
	
	@Inject
	private IDAO<Tipologia> tipologiaDAO;
	
	public void salvarTipologia(Tipologia tipologia)  {
		tipologiaDAO.salvarOuAtualizar(tipologia);
	}
	
	
	public Collection<Tipologia> listaTipologiaPai() {
		return  tipologiaDAO.listarTodos();
	}
	
	
	
	public Collection<Tipologia> listaTipologiaPaiPossuiFilho()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);

		criteria.add(Restrictions.eq("indPossuiFilhos", Boolean.TRUE));
		return tipologiaDAO.listarPorCriteria(criteria);
	}
	
	
	public Tipologia carregarTipologia(Integer id){
		return tipologiaDAO.carregarGet(id);
	}
	
	
	public Tipologia carregarTipologiaPorId(Integer id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);
		criteria.add(Restrictions.eq("ideTipologia", id ));
		criteria.setFetchMode("cnaeCollection", FetchMode.JOIN);
		criteria.createAlias("ideTipologiaPai", "ideTipologiaPai", JoinType.LEFT_OUTER_JOIN);
		return tipologiaDAO.buscarPorCriteria(criteria);
	}


}