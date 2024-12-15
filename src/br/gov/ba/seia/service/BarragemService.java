package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.BarragemDAOImpl;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BarragemService {

	private static final Integer IDE_BARRAGEM_OUTROS = 361;
	
	@EJB
	private BarragemDAOImpl barragemDAO;

	public BarragemDTO carregarBarragemDTO(Integer ideBarragem) {
		return montarBarragemDTO(barragemDAO.carregarBarragemByNativeQuery(ideBarragem));
	}
	
	public BarragemDTO carregarBarragemOutrosDTO() {
		return carregarBarragemDTO(IDE_BARRAGEM_OUTROS);
	}
	
	public List<BarragemDTO> montarListaBarragem() {
		List<BarragemDTO> listaBarragem = null;
		List<Object[]> listaObj = barragemDAO.listarBarragemByNativeQuery();
		if(!Util.isNullOuVazio(listaObj)){
			listaBarragem = new ArrayList<BarragemDTO>();
			for(Object[] obj : listaObj){
				listaBarragem.add(montarBarragemDTO(obj));
			}
		}
		return listaOrdenada(listaBarragem);
	}

	private List<BarragemDTO> listaOrdenada(List<BarragemDTO> listaBarragem)  {
		BarragemDTO dtoOutros = carregarBarragemOutrosDTO();
		listaBarragem.remove(dtoOutros);
		Collections.sort(listaBarragem);
		listaBarragem.add(dtoOutros);
		return listaBarragem;
	}

	private BarragemDTO montarBarragemDTO(Object[] obj) {
		BarragemDTO dto = new BarragemDTO();
		dto.setIdeBarragem(new Barragem((Integer)obj[0], (String)obj[1]));
		
		if(!dto.getIdeBarragem().isOutros()){
			dto.setLongitude(((Double)obj[3]).toString());
			dto.setLatitude(((Double)obj[4]).toString());
			dto.setNomMunicipio((String)obj[5]);
		} 
		
		else {
			dto.setNomMunicipio("--");
			dto.setLongitude("--");
			dto.setLatitude("--");
		}
		return dto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Barragem carregar(Integer ideBarragem)  {
		return barragemDAO.carregar(ideBarragem);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Barragem barragem) {
		barragemDAO.salvar(barragem);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gravarBarragemTheGeom(Map<String, Object> parametros) {
		barragemDAO.updateTheGeomInBarragem(parametros);		
	}
	
}