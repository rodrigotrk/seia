package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PorteTipologiaDAOImpl;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PorteTipologiaService {

	@Inject
	PorteTipologiaDAOImpl porteTipologiaDAOImpl;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PorteTipologia filtrarPorteTipologiaById(PorteTipologia pPorteTipologia)  {
		return porteTipologiaDAOImpl.filtrarPorteTipologiaById(pPorteTipologia);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorteTipologia(PorteTipologia pPorteTipologia)  {
		porteTipologiaDAOImpl.salvarPorteTipologia(pPorteTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorteTipologia2(PorteTipologia pPorteTipologia)  {
		porteTipologiaDAOImpl.salvarPorteTipologia2(pPorteTipologia);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorteTipologias(Collection<PorteTipologia> pColPorteTipologia)  {

		for (PorteTipologia listPorteTipologia : pColPorteTipologia) {
			PorteTipologia porteTipologia = new PorteTipologia();
			PorteTipologia pt =  buscarPorteTipologia(listPorteTipologia.getIdeTipologiaGrupo(),listPorteTipologia.getIdePorte());
			
			if(pt==null){
				porteTipologia.setIdeTipologiaGrupo(listPorteTipologia.getIdeTipologiaGrupo());
				porteTipologia.setIdePorte(listPorteTipologia.getIdePorte());
				porteTipologia.setValMinimo(listPorteTipologia.getValMinimo());
				porteTipologia.setValMaximo(listPorteTipologia.getValMaximo());
			}else{
				porteTipologia = 	pt;
				porteTipologia.setValMinimo(listPorteTipologia.getValMinimo());
				porteTipologia.setValMaximo(listPorteTipologia.getValMaximo());
			}
			porteTipologiaDAOImpl.salvarPorteTipologias(porteTipologia);
		}
		
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPorteTipologia(PorteTipologia pPorteTipologia)  {
		porteTipologiaDAOImpl.atualizarPorteTipologia(pPorteTipologia);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PorteTipologia carregarPorteTipologia(Integer id) {
		return porteTipologiaDAOImpl.carregarPorteTipologia(id);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void deletarPorteTipologia(PorteTipologia porteTipologia)  {
		porteTipologiaDAOImpl.deletarPorteTipologia(porteTipologia);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarPorteTipologias(Collection<PorteTipologia> pPorteTipologia)  {
		porteTipologiaDAOImpl.deletarPorteTipologias(pPorteTipologia);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<PorteTipologia> recuperarPorteTipologia(Integer id ) {
		return porteTipologiaDAOImpl.recuperarPorteTipologia(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<PorteTipologia> listarPorteCompetenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		return porteTipologiaDAOImpl.listarPorteCompetenciaTipologiaGrupo(tipologiaGrupo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorteTipologia(TipologiaGrupo tipologiaGrupo,  Porte porte) {
		porteTipologiaDAOImpl.excluirPorteTipologia(tipologiaGrupo, porte);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PorteTipologia buscarPorteTipologia(TipologiaGrupo tipologiaGrupo,  Porte porte) {
		return porteTipologiaDAOImpl.buscarPorteTipologia(tipologiaGrupo, porte);
	}


}