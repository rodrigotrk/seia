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
import br.gov.ba.seia.dao.ImovelRuralSicarDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralSicar;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralSicarService {

	@Inject
	ImovelRuralSicarDAOImpl daoImpl;

	@Inject
	IDAO<ImovelRuralSicar> dao;

	public ImovelRuralSicar listarImovelRuralSicarByImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralSicar.class, "imovelRuralSicar");
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideImovelRuralSicar"));
		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralSicar pImovelRuralSicar)  {
		this.daoImpl.salvar(pImovelRuralSicar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralSicar pImovelRuralSicar)  {
		this.daoImpl.atualizar(pImovelRuralSicar);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComMerge(ImovelRuralSicar pImovelRuralSicar)  {
		this.daoImpl.atualizarComMerge(pImovelRuralSicar);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralSicar carregarTudo(ImovelRuralSicar pImovelRuralSicar)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralSicar.class, "VegetacaoNativa");
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRuralSicar", pImovelRuralSicar.getIdeImovelRuralSicar()));
		criteria.addOrder(Order.asc("ideImovelRuralSicar"));
		return dao.buscarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ImovelRuralSicar pImovelRuralSicar)  {
		this.daoImpl.remover(pImovelRuralSicar);
	}
	
	public List<ImovelRural> listarPorCriteriaDemanda(ImovelRural imovelRuralPesquisa, Boolean temNumCar, int first, int pageSize)  {
		return daoImpl.listarImoveisComFiltroeNativeQuery(imovelRuralPesquisa, temNumCar, first, pageSize);
	}
	
	public Integer count(ImovelRural imovelRuralPesquisa, Boolean temNumCar)  {
	 	Integer countListaImoveis;	 	
		countListaImoveis = daoImpl.qtdImoveisComFiltroeNativeQuery(imovelRuralPesquisa, temNumCar, 0, Integer.MAX_VALUE);
		return countListaImoveis;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> listarImovelRuralComErroSincronia(boolean isCount) {
		return daoImpl.listarImoveisSemSicar(isCount);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRuralSicar> listarImovelRuralSicarPor(Integer ideEmpreendimento) {
		return daoImpl.listarImovelRuralSicarPor(ideEmpreendimento);
	}
	
}
