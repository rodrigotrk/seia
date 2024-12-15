package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipologiaTipoAtoDAOImpl;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaTipoAto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaTipoAtoService {

	@Inject
	TipologiaTipoAtoDAOImpl tipologiaTipoAtoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaTipoAto filtrarTipologiaTipoAtoGrupoById(TipologiaTipoAto pTipologiaTipoAto) throws Exception {
		return tipologiaTipoAtoDAOImpl.filtrarTipologiaTipoAtoGrupoById(pTipologiaTipoAto);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipologiaTipoAto(TipologiaTipoAto pTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.salvarTipologiaTipoAto(pTipologiaTipoAto);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipologiaTipoAtos(Collection<TipologiaTipoAto> pColTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.salvarTipologiaTipoAtos(pColTipologiaTipoAto);
	}
	
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTipologiaTipoAto(TipologiaTipoAto pTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.atualizarTipologiaTipoAto(pTipologiaTipoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  TipologiaTipoAto carregarTipologiaTipoAtos(Integer id) throws Exception{
		return tipologiaTipoAtoDAOImpl.carregarTipologiaTipoAtos(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<TipologiaTipoAto> recuperarTipologiaTipoAto(Integer id ) throws Exception{
		return tipologiaTipoAtoDAOImpl.recuperarTipologiaTipoAto(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarTipologiaTipoAto(Collection<TipologiaTipoAto> pTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.deletarTipologiaTipoAto(pTipologiaTipoAto);
	}

	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipologiaTipoAto listarTipologiaTipoAto(TipologiaGrupo tipologiaGrupo, Integer ideTipoAto ) throws Exception{
		return tipologiaTipoAtoDAOImpl.listarTipologiaTipoAto(tipologiaGrupo, ideTipoAto);
	} 
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaTipoAto> filtrarTipologiaTipoAto(TipologiaGrupo tipologiaGrupo,Collection<TipologiaTipoAto> listTipologiaTipoAto) throws Exception {
		 return tipologiaTipoAtoDAOImpl.filtrarTipologiaTipoAto(tipologiaGrupo, listTipologiaTipoAto);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaTipoAto(Collection<TipologiaTipoAto> pColTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.excluirTipologiaTipoAto(pColTipologiaTipoAto);
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaTipoAto(TipologiaGrupo tipologiaGrupo) throws Exception{
		tipologiaTipoAtoDAOImpl.excluirTipologiaTipoAto(tipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipologiaTipoAto listarTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo, Integer ideAtoAmbiental ) throws Exception{
		return tipologiaTipoAtoDAOImpl.listarTipologiaAtoAmbiental(tipologiaGrupo, ideAtoAmbiental);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaTipoAto> filtrarTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo,Collection<TipologiaTipoAto> listTipologiaTipoAto) throws Exception {
		 return tipologiaTipoAtoDAOImpl.filtrarTipologiaAtoAmbiental(tipologiaGrupo, listTipologiaTipoAto);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaAtoAmbiental(Collection<TipologiaTipoAto> pColTipologiaTipoAto) throws Exception {
		tipologiaTipoAtoDAOImpl.excluirTipologiaAtoAmbiental(pColTipologiaTipoAto);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo) throws Exception{
		tipologiaTipoAtoDAOImpl.excluirTipologiaAtoAmbiental(tipologiaGrupo);
	}
	
	
}