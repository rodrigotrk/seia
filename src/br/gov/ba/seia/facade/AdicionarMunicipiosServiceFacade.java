package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.EnderecoEmpreendimentoMunicipioDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AdicionarMunicipiosServiceFacade {
	
	@EJB
	private MunicipioService municipioService;
	@EJB
	private EnderecoEmpreendimentoMunicipioDAOImpl enderecoEmpreendimentoMunicipioDAOImpl;
	@EJB
	private LocalizacaoGeograficaService locGeoService;

	@Inject
	private IDAO<String> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> obterMunicipiosBahiaByNomeDemanda(String nomeMunicipio, Integer first, Integer pageSize) {
		return municipioService.obterMunicipiosBahiaByNomeDemanda(nomeMunicipio, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterMunicipiosBahiaByNomeCount(String nomeMunicipio) {
		return municipioService.obterMunicipiosBahiaByNomeCount(nomeMunicipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipio(Integer ideEmpreendimento) {
		return municipioService.listarMunicipioAdicional(ideEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarTodosMunicipiosEnvolvidos(Integer ideEmpreendimento)  {
		return municipioService.listarTodosMunicipiosEnvolvidos(ideEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Empreendimento empreendimento, Collection<Municipio> listaMunicipio) throws Exception {
		enderecoEmpreendimentoMunicipioDAOImpl.salvar(empreendimento, listaMunicipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Empreendimento empreendimento, Municipio municipio) {
		enderecoEmpreendimentoMunicipioDAOImpl.remover(municipio,empreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Map<String, Object> validarMunicipios(Integer ideLocalizacaoGeografica, Collection<Municipio> listaMunicipio) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Collection<Municipio> listaMunicipioIBGE = null;
		Collection<Double> listarIBGE = listarIBGE(ideLocalizacaoGeografica);
		
		if(!Util.isNullOuVazio(listarIBGE)){
			listaMunicipioIBGE = municipioService.listarMunicipioPorListaIBGE(listarIBGE);
		}
		
		if (!Util.isNullOuVazio(listaMunicipioIBGE)){
			
			if(listaMunicipio.containsAll(listaMunicipioIBGE) && listaMunicipioIBGE.containsAll(listaMunicipio)) {
				retorno.put("isShapeValido", true);
			} else {
				retorno.put("isShapeValido", false);
				Collection<Municipio> listaMunicipioFaltando = new ArrayList<Municipio>();
				Collection<Municipio> listaMunicipioSobrando = new ArrayList<Municipio>();
				
				for(Municipio m : listaMunicipio) {
					if(!listaMunicipioIBGE.contains(m)) {
						listaMunicipioFaltando.add(m);
					}
				}
				for(Municipio m : listaMunicipioIBGE) {
					if(!listaMunicipio.contains(m)) {
						listaMunicipioSobrando.add(m);
					}
				}
				
				if(!Util.isNullOuVazio(listaMunicipioFaltando)) {
					retorno.put("listaMunicipioFaltando", listaMunicipioFaltando);
				}
				
				if(!Util.isNullOuVazio(listaMunicipioSobrando)) {
					retorno.put("listaMunicipioSobrando", listaMunicipioSobrando);
				}			
			}	
		}
		
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Map<String, Object> validarMunicipiosParaRequerimento(Integer ideLocalizacaoGeografica, Collection<Municipio> listaMunicipio) throws Exception {
		
		Collection<Double> listarIBGE = listarIBGE(ideLocalizacaoGeografica);
		Collection<Municipio> listaMunicipioIBGE = municipioService.listarMunicipioPorListaIBGE(listarIBGE);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		if(listaMunicipio.containsAll(listaMunicipioIBGE) && listaMunicipioIBGE.containsAll(listaMunicipio)) {
			retorno.put("isShapeValido", true);
		}
		else{
			
			boolean isShapeValido = true;
			
			
			Collection<Municipio> listaMunicipioFaltando = new ArrayList<Municipio>();
			Collection<Municipio> listaMunicipioSobrando = new ArrayList<Municipio>();
			
			for(Municipio m : listaMunicipio) {
				if(!listaMunicipioIBGE.contains(m)) {
					listaMunicipioFaltando.add(m);
				}
			}
			
			for(Municipio m : listaMunicipioIBGE) {
				if(!listaMunicipio.contains(m)) {
					listaMunicipioSobrando.add(m);
					isShapeValido = false;
				}
			}
			
			if(!Util.isNullOuVazio(listaMunicipioFaltando)) {
				retorno.put("listaMunicipioFaltando", listaMunicipioFaltando);
			}
			
			if(!Util.isNullOuVazio(listaMunicipioSobrando)) {
				retorno.put("listaMunicipioSobrando", listaMunicipioSobrando);
			}
			
			retorno.put("isShapeValido", isShapeValido);
		}	
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDadoGeografico(Integer ideLocalizacaoGeografica) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", ideLocalizacaoGeografica);
		dao.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", parametros);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Double> listarIBGE(Integer ideLocalizacaoGeografica) throws Exception {
		return locGeoService.listarCodIBGE(ideLocalizacaoGeografica);
	}
	

}
