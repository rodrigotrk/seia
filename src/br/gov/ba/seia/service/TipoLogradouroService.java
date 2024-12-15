package br.gov.ba.seia.service;

import java.util.Collection;

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
import br.gov.ba.seia.entity.TipoLogradouro;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoLogradouroService {

	@Inject
	private IDAO<TipoLogradouro> daoTipoLogradouro;
	
	public void salvarTipoLogradouro(TipoLogradouro tipoLogradouro) throws Exception {
		daoTipoLogradouro.salvar(tipoLogradouro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoLogradouro> listarTipoLogradouro() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoLogradouro.class);
		return  daoTipoLogradouro.listarPorCriteria(criteria, Order.asc("nomTipoLogradouro"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoLogradouro> filtrarListaTipoLogradouro(TipoLogradouro tipoLogradouro) {
		return daoTipoLogradouro.listarPorExemplo(tipoLogradouro);
	}
	
	public void excluirTipoLogradouro(TipoLogradouro tipoLogradouro) throws Exception {
		daoTipoLogradouro.remover(tipoLogradouro);
	}
	
	public TipoLogradouro carregaTipoLogradouroById(Integer ideTipoLogradouro) throws Exception {
		return daoTipoLogradouro.carregarLoad(ideTipoLogradouro);
	}
	
	public TipoLogradouro findTipoLogradouroByIde(Integer ideTipoLogradouro) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoLogradouro.class)
		.add(Restrictions.eq("ideTipoLogradouro", ideTipoLogradouro));
		
		return  daoTipoLogradouro.buscarPorCriteria(criteria);
	}

	
}
