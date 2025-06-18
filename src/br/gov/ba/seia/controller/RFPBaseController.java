package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoResponsavelTecnico;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.RFPFacade;
import br.gov.ba.seia.service.PessoaFisicaService;

public class RFPBaseController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	protected RFPFlorestaProducaoController florestaProducao;
	
	@Inject
	protected RFPPlantioController dadosPlantio;
	
	@Inject
	protected RFPResponsavelTecnicoController responsavelTecnico;
	
	@Inject
	protected RFPEspecieProducaoController especieProducao;
	
	@Inject
	protected RFPImovelController imovel;
	
	@EJB
	protected RFPFacade facade;
	
	@EJB
	protected PessoaFisicaService pessoaFisicaService;
	
	
	protected RegistroFlorestaProducao ideRegistroFlorestaProducao;

	protected RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao;
	
	public RFPBaseController() {
	}
	
	public RegistroFlorestaProducao getIdeRegistroFlorestaProducao() {
		
		if(ideRegistroFlorestaProducao==null){
			ideRegistroFlorestaProducao = new RegistroFlorestaProducao();
		}
		
		return ideRegistroFlorestaProducao;
	}

	public void setIdeRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		this.ideRegistroFlorestaProducao = ideRegistroFlorestaProducao;
	}
	
	public RegistroFlorestaProducaoResponsavelTecnico getIdeRegistroFlorestaProducaoResponsavelTecnico(){
		return getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoResponsavelTecnico();
	}
	
	public void setIdeRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducaoResponsavelTecnico ideRegistroFlorestaProducaoResponsavelTecnico){
		getIdeRegistroFlorestaProducao().setIdeRegistroFlorestaProducaoResponsavelTecnico(ideRegistroFlorestaProducaoResponsavelTecnico);
	}

	public RegistroFlorestaProducaoImovel getIdeRegistroFlorestaProducaoImovel(){
		
		if(getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoImovel()==null){
			getIdeRegistroFlorestaProducao().setIdeRegistroFlorestaProducaoImovel(new RegistroFlorestaProducaoImovel());
		}
		
		return getIdeRegistroFlorestaProducao().getIdeRegistroFlorestaProducaoImovel();
	}
	
	public void setIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel){
		getIdeRegistroFlorestaProducao().setIdeRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducaoImovel);
	}
	
	public RegistroFlorestaProducaoImovelPlantio getIdeRegistroFlorestaProducaoImovelPlantio(){
		if(getIdeRegistroFlorestaProducaoImovel().getIdeRegistroFlorestaProducaoImovelPlantio()==null){
			getIdeRegistroFlorestaProducaoImovel().setIdeRegistroFlorestaProducaoImovelPlantio(new RegistroFlorestaProducaoImovelPlantio());
		}
		
		return getIdeRegistroFlorestaProducaoImovel().getIdeRegistroFlorestaProducaoImovelPlantio();
	}
	
	public void setIdeRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio){
		getIdeRegistroFlorestaProducaoImovel().setIdeRegistroFlorestaProducaoImovelPlantio(registroFlorestaProducaoImovelPlantio);
	}
	
	public void setRegistroFlorestaProducaoResponsavelTecnicoList(List<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoList){
		getIdeRegistroFlorestaProducao().setRegistroFlorestaProducaoResponsavelTecnicoList(registroFlorestaProducaoResponsavelTecnicoList);
	}
	
	public List<RegistroFlorestaProducaoResponsavelTecnico> getRegistroFlorestaProducaoResponsavelTecnicoList(){
		if(getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoResponsavelTecnicoList()==null){
			getIdeRegistroFlorestaProducao().setRegistroFlorestaProducaoResponsavelTecnicoList(new ArrayList<RegistroFlorestaProducaoResponsavelTecnico>());
		}
		
		return getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoResponsavelTecnicoList();
	}
	
	public List<RegistroFlorestaProducaoImovel> getRegistroFlorestaProducaoImovelList(){
		
		if(getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoImovelList()==null){
			getIdeRegistroFlorestaProducao().setRegistroFlorestaProducaoImovelList(new ArrayList<RegistroFlorestaProducaoImovel>());
		}
		
		return getIdeRegistroFlorestaProducao().getRegistroFlorestaProducaoImovelList();
	}
	
	public void setRegistroFlorestaProducaoImovelList(List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovelList){
		getIdeRegistroFlorestaProducao().setRegistroFlorestaProducaoImovelList(registroFlorestaProducaoImovelList);
	}
	
	public List<RegistroFlorestaProducaoImovelPlantio> getRegistroFlorestaProducaoImovelPlantioList(){
		
		if(getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList()==null){
		   getIdeRegistroFlorestaProducaoImovel().setRegistroFlorestaProducaoImovelPlantioList(new ArrayList<RegistroFlorestaProducaoImovelPlantio>());
		}
		
		return getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelPlantioList();
	}
	
	public List<RegistroFlorestaProducaoImovelEspecieProducao> getRegistroFlorestaProducaoImovelEspecieProducaoList(){
		
		if(getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelEspecieProducaoList()==null){
			getIdeRegistroFlorestaProducaoImovel().setRegistroFlorestaProducaoImovelEspecieProducaoList(new ArrayList<RegistroFlorestaProducaoImovelEspecieProducao>());
		}
		
		return getIdeRegistroFlorestaProducaoImovel().getRegistroFlorestaProducaoImovelEspecieProducaoList();
	}

	public Requerimento getIdeRequerimento(){
		return getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio().getIdeRequerimento();
	}
	
	public void setIdeRequerimento(Requerimento ideRequerimento){
		if(getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio()==null){
			getIdeRegistroFlorestaProducao().setIdeAtoDeclaratorio(new AtoDeclaratorio());
		}
		
		getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio().setIdeRequerimento(ideRequerimento);
		getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio().setIdeDocumentoObrigatorio(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FORMULARIO_RFP.getId()));
	}
	
	public RFPFacade getFacade() {
		return facade;
	}

	public RegistroFlorestaProducaoImovelEspecieProducao getIdeRegistroFlorestaProducaoImovelEspecieProducao() {
		return ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

	public void setIdeRegistroFlorestaProducaoImovelEspecieProducao(
			RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao) {
		this.ideRegistroFlorestaProducaoImovelEspecieProducao = ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

}
