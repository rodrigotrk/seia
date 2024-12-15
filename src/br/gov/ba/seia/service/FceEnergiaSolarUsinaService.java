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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceEnergiaSolar;
import br.gov.ba.seia.entity.FceEnergiaSolarUsina;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaSolarUsinaService {
	
	@Inject
	private IDAO<FceEnergiaSolarUsina> fceEnergiaSolaUsinaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaSolarUsina fceEnergiaSolarUsina) {
		this.fceEnergiaSolaUsinaIDAO.salvarOuAtualizar(fceEnergiaSolarUsina);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FceEnergiaSolarUsina fceEnergiaSolarUsina)  {
		this.fceEnergiaSolaUsinaIDAO.remover(fceEnergiaSolarUsina);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaSolarUsina> listarUsinaBySolar(FceEnergiaSolar fceEnergiaSolar) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaSolarUsina.class)
		.createAlias("ideLocalizacaoGeografica", "ideLocalizacaoGeografica",JoinType.LEFT_OUTER_JOIN)		
		.add(Restrictions.eq("ideFceEnergiaSolar.ideFceEnergiaSolar", fceEnergiaSolar.getIdeFceEnergiaSolar())); 
		
		return this.fceEnergiaSolaUsinaIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaSolarUsina"));
	}
}
