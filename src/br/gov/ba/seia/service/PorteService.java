package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PorteDAOImpl;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.enumerator.PorteEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PorteService {

	@Inject
	PorteDAOImpl porteDAOImpl;
	
	@EJB 
	TipologiaGrupoService tipologiaGrupoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte filtrarPorteById(Porte pPorte)  {
		return porteDAOImpl.filtrarPorteById(pPorte);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte buscarPortePorId(Integer idePorte)  {
		return porteDAOImpl.buscarPortePorId(idePorte);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPorte(Porte pPorte)  {
		porteDAOImpl.salvarPorte(pPorte);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPortes(Collection<Porte> pColPorte)  {
		porteDAOImpl.salvarPortes(pColPorte);
	}
	
	public Porte calcularValorPorte(Integer ideTipologiaGrupo,BigDecimal valor) {
		return porteDAOImpl.calcularValorPorte(ideTipologiaGrupo, valor);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTipologiaGrupoArea(Porte pPorte)  {
		porteDAOImpl.atualizarTipologiaGrupoArea(pPorte);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Porte> listarPorte(Integer idePorte)  {
		return porteDAOImpl.listarPorte(idePorte);
	}

	public Porte buscarPorte(Map<String, Object> parametros)  {
		return porteDAOImpl.buscarPorte(parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Porte> listarPorte()  {
		return porteDAOImpl.listarPorte();
	}

	public Boolean calcularValorPorte(Collection<Tipologia> licencas)  {
		
		for (Tipologia tipologia : licencas) {
			
			TipologiaGrupo tipologiaGrupo = null;
			
			if(Util.isNullOuVazio(tipologia.getTipologiaGrupo())) {
				tipologiaGrupo = tipologiaGrupoService.carregarTipologiaGrupoPorProjection(tipologia);				
			} else {
				tipologiaGrupo = tipologia.getTipologiaGrupo();
			}
			
			BigDecimal valor = null;
			
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && !tipologia.getValAtividade().equalsIgnoreCase("0,00")) {
				valor = formatarValor(tipologia);
			} else {
				return false;
			}
				
			Porte porte = this.calcularValorPorte(tipologiaGrupo.getIdeTipologiaGrupo(), valor);
			
			if(Util.isNull(porte)) {
				porte = this.carregarPorteDLA();
			}
			
			tipologia.setPorte(porte);
			
		}
		
		return true;
		
	}

	private BigDecimal formatarValor(Tipologia tipologia) {
		String valorAtividade = null;
		
		if(tipologia.getValAtividade().contains(",")) {
			valorAtividade = tipologia.getValAtividade().replace(".", "").replace(",", ".");
		} else {
			valorAtividade = tipologia.getValAtividade();
		}
		
		return BigDecimal.valueOf(Double.parseDouble(valorAtividade));
	}

	public Porte carregarPorteDLA()  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePorte", PorteEnum.NAO_IDENTIFICADO.getId());
		
		return this.buscarPorte(parametros);
	}
}