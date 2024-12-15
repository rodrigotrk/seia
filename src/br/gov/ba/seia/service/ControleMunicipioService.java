package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ControleMunicipioDAOImpl;
import br.gov.ba.seia.dto.ControleMunicipioAtribuicaoDTO;
import br.gov.ba.seia.dto.ControleMunicipioDTO;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleMunicipioService {
	
	@Inject
	private ControleMunicipioDAOImpl controleMunicipioDAOImpl;
	
	@EJB
	private MunicipioService municipioService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleMunicipioDTO> listarMunicipiosDaBahia() {
		List<Municipio> lista = controleMunicipioDAOImpl.listarMunicipiosDaBahia();
		
		List<ControleMunicipioDTO> listaControleMunicipioDTO = new ArrayList<ControleMunicipioDTO>();
		
		for (Municipio municipio : lista) {
			ControleMunicipioDTO controleMunicipioDTO = new ControleMunicipioDTO(municipio);
			controleMunicipioDTO.setListaDetalhe(retornarSelecionados(controleMunicipioDTO.getListaAtribuicao()));
			listaControleMunicipioDTO.add(controleMunicipioDTO);
		}
		
		return listaControleMunicipioDTO;
	}
	
	public List<ControleMunicipioAtribuicaoDTO> retornarSelecionados(List<ControleMunicipioAtribuicaoDTO> listaAtribuicao) {
		List<ControleMunicipioAtribuicaoDTO> listaDetalhe = new ArrayList<ControleMunicipioAtribuicaoDTO>(); 
		for (ControleMunicipioAtribuicaoDTO controleMunicipioAtribuicaoDTO : listaAtribuicao) {
			if (controleMunicipioAtribuicaoDTO.isSelecionado()){
				listaDetalhe.add(controleMunicipioAtribuicaoDTO);
			}
		}
		return listaDetalhe;
	}
	
	public List<ControleMunicipioDTO> pesquisarMunicipio(List<ControleMunicipioDTO> listaControleMunicipioDTO, List<ControleMunicipioDTO> listaControleMunicipioDTOAll, String nomeMunicipio){
		
		for (ControleMunicipioDTO controleMunicipioDTO : listaControleMunicipioDTOAll) {
			if(controleMunicipioDTO.getMunicipio().getNomMunicipio().toLowerCase().indexOf(nomeMunicipio.toLowerCase())!= -1){
				listaControleMunicipioDTO.add(controleMunicipioDTO);
			}
		}
		
		return listaControleMunicipioDTO;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarControleMunicipio(ControleMunicipioDTO controleMunicipioDTOSelecionado) {
		
		Municipio municipio = controleMunicipioDTOSelecionado.getMunicipio();
		
		for (ControleMunicipioAtribuicaoDTO controleMunicipioAtribuicaoDTO : controleMunicipioDTOSelecionado.getListaAtribuicao()) {
			if (Util.getString("controle_municipio_ind_bloqueio_DQC").equals(controleMunicipioAtribuicaoDTO.getNomeDetalhe())){
				municipio.setIndBloqueioDQC(controleMunicipioAtribuicaoDTO.isSelecionado());
			} 
			else if (Util.getString("controle_municipio_ind_estado_emergencia").equals(controleMunicipioAtribuicaoDTO.getNomeDetalhe())) {
				municipio.setIndEstadoEmergencia(controleMunicipioAtribuicaoDTO.isSelecionado());
			}
			
			 adicionarOuRemoverDetalhe(controleMunicipioAtribuicaoDTO, controleMunicipioDTOSelecionado.getListaDetalhe());
		}
		
		municipioService.salvarOuAtualizar(municipio);
	}
	
	private void adicionarOuRemoverDetalhe(ControleMunicipioAtribuicaoDTO controleMunicipioAtribuicaoDTO, List<ControleMunicipioAtribuicaoDTO> listaDetalhe) {
		if (listaDetalhe.contains(controleMunicipioAtribuicaoDTO)){
			if (!controleMunicipioAtribuicaoDTO.isSelecionado()){
				listaDetalhe.remove(controleMunicipioAtribuicaoDTO);
			}
		} else {
			if (controleMunicipioAtribuicaoDTO.isSelecionado()){
				listaDetalhe.add(controleMunicipioAtribuicaoDTO);
			}
		}
	}
}
