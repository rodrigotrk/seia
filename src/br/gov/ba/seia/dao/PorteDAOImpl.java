package br.gov.ba.seia.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import br.gov.ba.seia.entity.Porte;

public class PorteDAOImpl {
	
	@Inject
	IDAO<Porte> porteDAO;
	
	public Porte filtrarPorteById(Porte pPorte) {
		return porteDAO.buscarEntidadePorExemplo(pPorte);
	}
	
	public Porte buscarPortePorId(Integer idePorte){
		return porteDAO.carregarGet(idePorte);
	}
	
	public void salvarPorte(Porte pPorte)  {
		porteDAO.salvarOuAtualizar(pPorte);
	}
	
	public void salvarPortes(Collection<Porte> pColPorte)  {
		for (Porte listPorte : pColPorte) {
			listPorte.setIdTipologiaGrupo(listPorte.getIdTipologiaGrupo()) ;
			porteDAO.salvar(listPorte);
		}
	}
	
	public Porte buscarPorte(Map<String, Object> parametros) {
		return porteDAO.buscarEntidadePorNamedQuery("Porte.findByIdePorte", parametros);
	}

	public void atualizarTipologiaGrupoArea(Porte pPorte) {
		porteDAO.atualizar(pPorte);
	}
	
	public Porte calcularValorPorte(Integer ideTipologiaGrupo,BigDecimal valor)  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideTipologiaGrupo", ideTipologiaGrupo);
		parametros.put("valor", valor);
		return porteDAO.buscarEntidadePorNamedQueryWithMaxResult("Porte.findByAtividades", parametros, 1);
	}
	
	public List<Porte> listarPorte(Integer idePorte)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idePorte", idePorte);
		return porteDAO.buscarPorNamedQuery("Porte.findDifIndentificado",params);
	}
	
	public List<Porte> listarPorte() {
		return porteDAO.buscarPorNamedQuery("Porte.findByAtivos");
	}
}