package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.TipoAtoDAOImpl;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipologiaGrupo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAtoService {
	
	@EJB
	TipoAtoDAOImpl TipoAtoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoAto carregar(Integer ideTipoAto) throws Exception {
		return TipoAtoDAOImpl.carregar(ideTipoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoOrderByAsc() throws Exception {
		return TipoAtoDAOImpl.listarTiposAtoOrderByAsc();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAto() throws Exception {
		return TipoAtoDAOImpl.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoConsultaveis() throws Exception {
		return TipoAtoDAOImpl.listarTiposAtoConsultaveis();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> filtrarListaTipoAto(TipoAto tipoAto) throws Exception {
		return TipoAtoDAOImpl.filtrarListaTipoAto(tipoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTipoAtoTipologiaGrupo(TipologiaGrupo tipologiaGrupo) throws Exception {
		 return TipoAtoDAOImpl.listarTipoAtoTipologiaGrupo(tipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> filtrarListaTipoAtoSouce(Collection<TipoAto> listTipoAto) throws Exception {
		 return TipoAtoDAOImpl.filtrarListaTipoAtoSouce(listTipoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoEnquadraveis() throws Exception {
		return TipoAtoDAOImpl.listarTiposAtoEnquadraveis();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoJuridicos() throws Exception {
		return TipoAtoDAOImpl.listarTiposAtosJuridicos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoNaoJuridicos(Boolean isDla) throws Exception {
		return TipoAtoDAOImpl.listarTiposAtosNaoJuridicos(isDla);
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtosParaReenquadramento(Boolean isDla) throws Exception {
		return TipoAtoDAOImpl.listarTiposAtosParaReenquadramento(isDla);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAto> listarTiposAtoComAtoAmbiental() throws Exception {
		return TipoAtoDAOImpl.listarTiposAtoComAtoAmbiental();
	}

}
