package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.EnderecoGenericoController;
import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoGenericoFacade {

	@EJB
	private EnderecoService enderecoService;

	@EJB
	private LogradouroService logradouroService;

	@EJB
	private BairroService bairroService;
	
	@EJB
	private ServicoDeCepService cepService;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;

	@EJB
	private EstadoService estadoService;
	
	private boolean isInexigibilidade = false;
	
	private boolean isOutroEstado = true;
	

	public void inicializarVariaveis(EnderecoGenericoController enderecoGenericoController) {
		Endereco endereco = null;
		if(enderecoGenericoController.isEdicao()){
			endereco = enderecoGenericoController.getEndereco();
		} 
		else {
			endereco = new Endereco();
		}
		
		endereco.setDtcCriacao(new Date());
		endereco.setNumEndereco(null);
		endereco.setDesPontoReferencia(null);
		endereco.setDesComplemento(null);
		endereco.setIdeLogradouro(new Logradouro());
		endereco.getIdeLogradouro().setIdeBairro(new Bairro());
		endereco.getIdeLogradouro().setIdeTipoLogradouro(new TipoLogradouro());
		endereco.getIdeLogradouro().setIdeMunicipio(enderecoGenericoController.getMunicipioSelecione());
		endereco.getIdeLogradouro().getIdeMunicipio().setIdeEstado(enderecoGenericoController.getEstadoSelecione());
		
		enderecoGenericoController.setEndereco(endereco);
		enderecoGenericoController.setBairroOutroInexibilidade(false);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void buscarEnderecoPorCep(EnderecoGenericoController enderecoGenericoController) throws Exception {
		
		try {
			inicializarVariaveis(enderecoGenericoController);

			List<Logradouro> logradouros = new ArrayList<Logradouro>();
			
			listarTipoLogradouro(enderecoGenericoController);
			enderecoGenericoController.setListaEstado(listarEstado());
			List<Municipio> municipios = null;
			List<Bairro> bairros = null;

	
			Integer cep = retornarCEP(enderecoGenericoController.getCepConsultado());
			cepService.carregar(cep.toString());
			cepService.montarEstrutura();
			
			
			if(!cepService.getLogradouroEnt().getNomLogradouro().isEmpty()) {
				logradouros.add(cepService.getLogradouroEnt());
				enderecoGenericoController.setLogradouro(cepService.getLogradouroEnt());
				enderecoGenericoController.getEndereco().setIdeLogradouro(cepService.getLogradouroEnt());
			}
			
			logradouros.add(getOutroLogradouro());
			enderecoGenericoController.setListaLogradouro(logradouros);
			
			if(cepService.getTipoLogradouroEnt()!=null) {
				if(!enderecoGenericoController.getListaTipoLogradouro().contains(cepService.getTipoLogradouroEnt())){
					enderecoGenericoController.getListaTipoLogradouro().add(cepService.getTipoLogradouroEnt());
				}
				if(cepService.isLogradouroInexistente()) {
					enderecoGenericoController.setTipoLogradouro(new TipoLogradouro());	
				}else{
					enderecoGenericoController.setTipoLogradouro(cepService.getTipoLogradouroEnt());					
				}
			}
			
			
			municipios = new ArrayList<Municipio>();
			if(!municipios.contains(cepService.getMunicipioEnt())){
				municipios.add(cepService.getMunicipioEnt());
			}
			if(!Util.isNullOuVazio(municipios)) {
				if(municipios.size() == 1){
					enderecoGenericoController.setMunicipio(cepService.getMunicipioEnt());
				}
				enderecoGenericoController.setListaMunicipio(municipios);
			}
				
	
			bairros = new ArrayList<Bairro>();
			if(cepService.getBairroEnt()!=null) {
				if(!bairros.contains(cepService.getBairroEnt())){
					bairros.add(cepService.getBairroEnt());
					enderecoGenericoController.setListaBairro(bairros);
				}
				if(bairros.size() == 1){
					enderecoGenericoController.setBairro(cepService.getBairroEnt());
				}
				enderecoGenericoController.setListaBairro(bairros);
				bairros.add(getOutroBairro());
			}else {
				enderecoGenericoController.setListaBairro(new ArrayList<Bairro>());
				enderecoGenericoController.getListaBairro().add(getOutroBairro());					
			}
			
			enderecoGenericoController.getEndereco().setNumEndereco(null);
			enderecoGenericoController.getEndereco().setDesComplemento(null);
			enderecoGenericoController.getEndereco().setDesPontoReferencia(null);
			
			enderecoGenericoController.setEstado(cepService.getEstadoEnt());
			enderecoGenericoController.setListaEstado(listarEstado());
			
		
		} catch (Exception ex) {
			if (ex.getMessage().contains("-1") || ex.getMessage().contains("CEP inválido.")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
				enderecoGenericoController.setEndereco(new Endereco());
			} else if (ex.getMessage().contains("-2")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do endereço, tente mais tarde ou entre em contato com o administrador do sistema.");
			} else {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
		        JsfUtil.addErrorMessage("Erro ao carregar dados do endereço, contate o administrador do sistema.");
			}
	    } 
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco buscarEndereco(Endereco endereco) throws Exception{
		return enderecoService.carregar(endereco.getIdeEndereco());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Bairro carregarBairro(Endereco endereco) throws Exception {
		return bairroService.carregarBairroById(endereco.getIdeEndereco());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void listarBairro(EnderecoGenericoController enderecoGenericoController) throws Exception {
		enderecoGenericoController.setListaBairro(bairroService.listarBairroByLogradouro(enderecoGenericoController.getLogradouro()));
	}   

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void listarLogradouro(EnderecoGenericoController enderecoGenericoController, Bairro bairro) throws Exception {
		Integer cep = retornarCEP(enderecoGenericoController.getCepConsultado());
		enderecoGenericoController.setListaLogradouro(listarLogradouroByBairroAndCEP(bairro, cep));
		List<Logradouro> listaLogradouro = (List<Logradouro>) enderecoGenericoController.getListaLogradouro();
		if(!Util.isNullOuVazio(listaLogradouro) && listaLogradouro.size() == 1) {
			enderecoGenericoController.setLogradouro(listaLogradouro.get(0));
		}
		listaLogradouro.add(getOutroLogradouro());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Logradouro> listarLogradouroByBairroAndCEP(Bairro bairro, Integer cep) throws Exception {
		return logradouroService.filtrarLogradouroByBairro(bairro, cep);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void listarTipoLogradouro(EnderecoGenericoController enderecoGenericoController) throws Exception {
		enderecoGenericoController.setListaTipoLogradouro(tipoLogradouroService.listarTipoLogradouro());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void listarMunicipio(EnderecoGenericoController enderecoGenericoController, Estado estado) throws Exception {
		Collection<Municipio> municipios = new ArrayList<Municipio>();
		municipios.add(new Municipio(0, "Selecione...", estado));
		municipios.addAll(municipioService.listarMunicipiosPorEstado(estado));
		enderecoGenericoController.setListaMunicipio(municipios);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Estado> listarEstado() throws Exception{
		Collection<Estado> estados = new ArrayList<Estado>();
		estados.add(new Estado(0, "Selecione..."));
		estados.addAll(estadoService.listar());
		return estados;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarEndereco(EnderecoGenericoController enderecoGenericoController) throws Exception {

		String msg = obterMsg(enderecoGenericoController);
		
		salvarBairro(enderecoGenericoController);
		salvarLogradouro(enderecoGenericoController);
		salvarEndereco(enderecoGenericoController);
		
		enderecoGenericoController.getMetodo().executeMethod();
		
		montarListaBairroEmEdicao(enderecoGenericoController);
		montarListaLogradouroEmEdicao(enderecoGenericoController);
		
		JsfUtil.addSuccessMessage("Endereço " + msg + " com sucesso!");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarBairro(EnderecoGenericoController enderecoGenericoController) throws Exception {
		if(!enderecoGenericoController.getBairro().getIndOrigemCorreio() && !enderecoGenericoController.getBairro().isIndOrigemApi() ){
			
			if(enderecoGenericoController.isOutroBairro()){
				
				if(enderecoGenericoController.isEdicao()){
					Bairro bairroAntigo = carregarBairro(enderecoGenericoController.getEndereco());
					if(!bairroAntigo.getIndOrigemCorreio() && !bairroAntigo.isIndOrigemApi()){
						enderecoGenericoController.getBairro().setIdeBairro(bairroAntigo.getIdeBairro());
					} 
					else {
						enderecoGenericoController.getBairro().setIdeBairro(null);
					}
				} 
				else {
					enderecoGenericoController.getBairro().setIdeBairro(null);
				}
			}
			
			bairroService.salvarBairro(enderecoGenericoController.getBairro());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Municipio retornarMunicipioByCep(Integer cep){
		return enderecoService.retornarMunicipioByCep(cep);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarEndereco(EnderecoGenericoController enderecoGenericoController) throws Exception {
		enderecoService.salvarOrAtualizarEndereco(enderecoGenericoController.getEndereco());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarLogradouro(EnderecoGenericoController enderecoGenericoController) throws Exception {
		if(!Util.isNotNullAndTrue(enderecoGenericoController.getLogradouro().getIndOrigemCorreio()) && !Util.isNotNullAndTrue(enderecoGenericoController.getLogradouro().getIndOrigemApi())){
			
			if(enderecoGenericoController.isOutroLogradouro()){
			
				if(enderecoGenericoController.isEdicao()){
					Logradouro logradouroAntigo = carregarIdeLogradouroAntigo(enderecoGenericoController.getEndereco());
					if(!Util.isNotNullAndTrue(logradouroAntigo.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouroAntigo.getIndOrigemApi())){
						enderecoGenericoController.getLogradouro().setIdeLogradouro(logradouroAntigo.getIdeLogradouro());
					} 
					else {
						enderecoGenericoController.getLogradouro().setIdeLogradouro(null);
					}
				} 
				else {
					enderecoGenericoController.getLogradouro().setIdeLogradouro(null);
				}
				
			}
			
			enderecoGenericoController.getLogradouro().setNumCep(retornarCEP(enderecoGenericoController.getCepConsultado()));
			logradouroService.salvarLogradouro(enderecoGenericoController.getLogradouro());
			enderecoGenericoController.getLogradouro().setEnderecoCollection(null);
			
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Logradouro carregarIdeLogradouroAntigo(Endereco endereco) throws Exception {
		return logradouroService.carregarIdeByEndereco(endereco);
	}

	public void montarEndereco(	EnderecoGenericoController enderecoGenericoController) throws Exception {
		Endereco end = buscarEndereco(enderecoGenericoController.getEndereco());
		
		enderecoGenericoController.setEndereco(end);
		enderecoGenericoController.setCepConsultado(enderecoGenericoController.getLogradouro().getNumCepString());
		
		montarListaBairroEmEdicao(enderecoGenericoController);
		
		listarTipoLogradouro(enderecoGenericoController);
		
		montarListaLogradouroEmEdicao(enderecoGenericoController);
		
		listarMunicipio(enderecoGenericoController, enderecoGenericoController.getEstado());
		enderecoGenericoController.setListaEstado(listarEstado());
		
	}

	private void montarListaBairroEmEdicao(EnderecoGenericoController enderecoGenericoController) throws Exception {
		listarBairro(enderecoGenericoController);
		if(!enderecoGenericoController.getListaBairro().contains(enderecoGenericoController.getBairro())){
			enderecoGenericoController.getListaBairro().add(enderecoGenericoController.getBairro());
		}
		
		if(!enderecoGenericoController.getListaBairro().contains(getOutroBairro())){
			enderecoGenericoController.getListaBairro().add(getOutroBairro());
		}
	}

	private void montarListaLogradouroEmEdicao(EnderecoGenericoController enderecoGenericoController) throws Exception {
		enderecoGenericoController.setListaLogradouro(listarLogradouroByBairroAndCEP(enderecoGenericoController.getBairro(), retornarCEP(enderecoGenericoController.getCepConsultado())));
		if(!enderecoGenericoController.getListaLogradouro().contains(enderecoGenericoController.getLogradouro())){
			enderecoGenericoController.getListaLogradouro().add(enderecoGenericoController.getLogradouro());
		}
		if(!enderecoGenericoController.getListaLogradouro().contains(getOutroLogradouro())){
			enderecoGenericoController.getListaLogradouro().add(getOutroLogradouro());
		}
	}

	public Logradouro getOutroLogradouro() {
		return new Logradouro(-1, "Outro", false);
	}
	
	public Bairro getOutroBairro() {
		return new Bairro(-1, "Outro", false);
	}
	
	public Integer retornarCEP(String strCep) {
		String aux = strCep.replace(".", "").replace("-", "");
		return Integer.parseInt(aux);
	}
	
	private String obterMsg(EnderecoGenericoController enderecoGenericoController) {
		if(!enderecoGenericoController.isEdicao()){
			return "salvo";
		} 
		else {
			return "atualizado";	
		}
	}

	public boolean isInexigibilidade() {
		return isInexigibilidade;
	}

	public void setInexigibilidade(boolean isInexigibilidade) {
		this.isInexigibilidade = isInexigibilidade;
	}
	
}
