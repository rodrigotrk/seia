package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PorteCompetenciaDAOImpl;
import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteCompetencia;
import br.gov.ba.seia.entity.TipologiaGrupo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PorteCompetenciaService {

	@Inject
	PorteCompetenciaDAOImpl porteCompetenciaDAOImpl;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PorteCompetencia filtrarPorteCompetenciaById(PorteCompetencia pPorteCompetencia)  {
		return porteCompetenciaDAOImpl.filtrarPorteCompetenciaById(pPorteCompetencia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorteCompetencia(PorteCompetencia pPorteCompetencia)  {
		porteCompetenciaDAOImpl.salvarPorteCompetencia(pPorteCompetencia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorteCompetencias(Collection<PorteCompetencia> pColPorteCompetencia)  {
		porteCompetenciaDAOImpl.salvarPorteCompetencias(pColPorteCompetencia);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTPorteCompetencia(PorteCompetencia pPorteTipologia)  {
		porteCompetenciaDAOImpl.atualizarTPorteCompetencia(pPorteTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<PorteCompetencia> recuperarPorteCompetencia(Integer id ) {
		return porteCompetenciaDAOImpl.recuperarPorteCompetencia(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarPorteCompetencia(Collection<PorteCompetencia> pPorteCompetencia)  {
		porteCompetenciaDAOImpl.deletarPorteCompetencia(pPorteCompetencia);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<PorteCompetencia> listarPorteCompetenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		return porteCompetenciaDAOImpl.listarPorteCompetenciaTipologiaGrupo(tipologiaGrupo);
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorteCompetenciaNivel(TipologiaGrupo tipologiaGrupo, NivelCompetencia nivelCompetencia, Porte porte) {
		porteCompetenciaDAOImpl.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
	}
}