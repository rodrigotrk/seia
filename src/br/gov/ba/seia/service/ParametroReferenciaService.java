package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ParamentroReferenciaDAOImpl;
import br.gov.ba.seia.entity.ParametroReferencia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaTipoAto;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroReferenciaService {

	@Inject
	ParamentroReferenciaDAOImpl paramentroReferenciaDAOImpl;
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ParametroReferencia filtrarTipologiaTipoAtoGrupoById(ParametroReferencia pParametroReferencia)  {
		return paramentroReferenciaDAOImpl.filtrarTipologiaTipoAtoGrupoById(pParametroReferencia);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarParametroReferencia(ParametroReferencia pParametroReferencia)  {
		paramentroReferenciaDAOImpl.salvarParametroReferencia(pParametroReferencia);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarParametroReferencia(ParametroReferencia pParametroReferencia)  {
		paramentroReferenciaDAOImpl.atualizarParametroReferencia(pParametroReferencia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  ParametroReferencia carregarParametroReferencia(Integer id) {
		return paramentroReferenciaDAOImpl.carregarParametroReferencia(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<ParametroReferencia> recuperarParametroReferencia(Collection<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupo ) {
		return paramentroReferenciaDAOImpl.recuperarParametroReferencia(unidadeMedidaTipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarParametroReferencias(Collection<ParametroReferencia> pParametroReferencia)  {
		paramentroReferenciaDAOImpl.deletarParametroReferencias(pParametroReferencia);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<ParametroReferencia> listarParamentroReferenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		return paramentroReferenciaDAOImpl.listarParamentroReferenciaTipologiaGrupo(tipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ParametroReferencia buscarParametroReferencia(TipologiaTipoAto ideTipologiaTipoAto,UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo) {
		return paramentroReferenciaDAOImpl.buscarParametroReferencia(ideTipologiaTipoAto, ideUnidadeMedidaTipologiaGrupo);
	}
	
	
}