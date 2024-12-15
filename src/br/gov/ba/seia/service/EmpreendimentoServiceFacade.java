package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpreendimentoServiceFacade {

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private MunicipioService municipioService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isEmpreendimentoCompleto(Empreendimento empreendimento) {
		
	
		return false;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Empreendimento> listarEmpreendimento(Pessoa requerente, String nomEmpreendimento)  {
		Collection<Empreendimento> listaEmpreendimento = empreendimentoService.listarEmpreendimentoBy(requerente, nomEmpreendimento);
		if(!Util.isNullOuVazio(listaEmpreendimento)){
			for(Empreendimento empreendimento : listaEmpreendimento){
				empreendimento.setMunicipio(municipioService.getMunicipioByEmpreendimento(empreendimento));
			}
		}
		return listaEmpreendimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarComMunicipio(Empreendimento empreendimentoSelecionado)  {
		return empreendimentoService.carregarPorIdComMunicipio(empreendimentoSelecionado.getIdeEmpreendimento());
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarEmpreendimento(Empreendimento empreendimento) {
		return empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
	}
	
	
}