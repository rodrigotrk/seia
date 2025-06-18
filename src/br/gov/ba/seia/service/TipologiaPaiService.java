package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipologiaPaiDAOImpl;
import br.gov.ba.seia.entity.Tipologia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaPaiService {
	
	@Inject
	TipologiaPaiDAOImpl tipologiaPaiDAOImpl;
	
	public void salvarTipologia(Tipologia tipologia) throws Exception {
		tipologiaPaiDAOImpl.salvarTipologia(tipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listaTipologiaPai() throws Exception {
		return  tipologiaPaiDAOImpl.listaTipologiaPai();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listaTipologiaPaiPossuiFilho() throws Exception {
		return tipologiaPaiDAOImpl.listaTipologiaPaiPossuiFilho();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarTipologia(Integer id) throws Exception{
		return tipologiaPaiDAOImpl.carregarTipologia(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarTipologiaPorId(Integer id) throws Exception{
		return tipologiaPaiDAOImpl.carregarTipologiaPorId(id);
	}
	

}
