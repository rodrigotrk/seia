package br.gov.ba.seia.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.util.ApiCepUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicoDeCepService {

	
	@Inject
	private LogradouroService logradouroService;

	@Inject
	private BairroService bairroService;

	@Inject
	private MunicipioService municipioService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;

	@Inject
	private EstadoService estadoService;
	

	static int codigoSucesso = 200;
	
	private boolean isLogradouroInexistente = false;
	
	private boolean isOutroEstado = true;
	
	private EnderecoDTO enderecoApi;
	
	private Municipio municipioEnt = null;
	private Bairro bairroEnt = null;
	private Logradouro logradouroEnt = null;
	private TipoLogradouro tipoLogradouroEnt = null;
	private Estado estadoEnt = null;

	private boolean newBairro = false;
	
	public ServicoDeCepService() {
		municipioService = new MunicipioService();
		logradouroService = new LogradouroService();
		bairroService = new BairroService();
		tipoLogradouroService = new TipoLogradouroService();
	}
	
	public EnderecoDTO carregar(String cep) throws Exception {
		cep = String.format("%08d", Integer.valueOf(cep));
		String urlChamada = ConfigEnum.CEP_INEMA_SERVER + cep;;
		if(cep.equals("0")) {
			return null;	
		}
		URL url = new URL(urlChamada);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		if (urlConnection.getResponseCode() != codigoSucesso) {
	
			URL url2 = new URL(ConfigEnum.CEP_VIACEP_SERVER.toString().replace("{num_cep}", cep));
            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader resposta = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonEmString = ApiCepUtil.converteJsonEmString(resposta);
                
                if(!jsonEmString.contains("\"erro\": \"true\"")) {

	                jsonEmString = jsonEmString.replace("\"localidade\"", "\"municipio\"");
	                resposta.close();
	                
	                Gson gson = new Gson();
	    			enderecoApi = gson.fromJson(jsonEmString, EnderecoDTO.class);
	    			enderecoApi.setCep(enderecoApi.getCep().replace("-", ""));
	    			enderecoApi.setNomOrigem("VI");
	    			return enderecoApi;
	    			
                }else {
                    System.out.println("Erro na resposta da API ViaCEP: " + jsonEmString);
                    throw new RuntimeException("Erro ao buscar o CEP na API ViaCEP");
                }
			
            } else {
	            System.out.println("Erro ao chamar a API ViaCEP. Código: " + responseCode);
	            throw new RuntimeException("Erro ao buscar o CEP na API ViaCEP. Código: " + responseCode);
	        }


		} else {
			InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream(),"UTF-8");
			BufferedReader resposta = new BufferedReader(isr);
			String jsonEmString = ApiCepUtil.converteJsonEmString(resposta);

			Gson gson = new Gson();
			enderecoApi = gson.fromJson(jsonEmString, EnderecoDTO.class);
			enderecoApi.setNomOrigem("CO");
			return enderecoApi;
		}

	}
	
	public EnderecoDTO getEnderecoApi() {
		return enderecoApi;
	}


	public Logradouro montarEstrutura() throws Exception {
		
		if(Util.isNullOuVazio(enderecoApi)) {
			throw new Exception("CEP inválido.");
		}
		isLogradouroInexistente = false;
		estadoEnt = estadoService.obterEstadoByUf(enderecoApi.getUf().toUpperCase());
		enderecoApi.setIdeEstado(estadoEnt.getIdeEstado());
		
        if (!Util.isNullOuVazio(enderecoApi.getMunicipio())) {
        	Integer ideMunicipio = municipioService.buscarIdeMunicipioByNomeANDIdeEstado(enderecoApi.getMunicipio(),enderecoApi.getIdeEstado());
        	enderecoApi.setIdeMunicipio(ideMunicipio);
        	municipioEnt = municipioService.obterMunicipio(ideMunicipio);	        	
        	if(municipioEnt==null) {
        		throw new Exception("municipio não cadastrado");
        	}
        }
		
        if(enderecoApi.getBairro().isEmpty()) {
			setNewBairro(true);
			bairroEnt = null;
		}
        if(!enderecoApi.getBairro().isEmpty()) {
	        bairroEnt = bairroService.buscarBairroByNome(enderecoApi.getBairro(), enderecoApi.getIdeMunicipio());
			
			/*Condição para incluir bairro, de acordo com os correios, caso ele não exista no banco*/
			 if(Util.isNullOuVazio(bairroEnt) && (!Util.isNullOuVazio(enderecoApi.getIdeMunicipio()))) {
				 bairroService.adicionarBairroApi(enderecoApi.getBairro(), enderecoApi.getIdeMunicipio());
				 bairroEnt = bairroService.buscarBairroByNome(enderecoApi.getBairro(), enderecoApi.getIdeMunicipio());
			 }
			 if(bairroEnt!=null) {
				 enderecoApi.setIdeBairro(bairroEnt.getIdeBairro());
			 }
        }

		/*A FAZER (Criação de Bairro Centro quando o municipio não tiver*/
//			else {
//				bairroApi.setIdeMunicipio(enderecoApi.getIdeMunicipio());
//				bairroService.salvarBairro(bairroApi);
//			}
		Collection<TipoLogradouro> tiposLogradouro = tipoLogradouroService.listarTipoLogradouro();
	    for(TipoLogradouro tipo:tiposLogradouro) {
	    	if(enderecoApi.getLogradouro().toUpperCase().indexOf(tipo.getNomTipoLogradouro().toUpperCase())==0 || 
	    			enderecoApi.getLogradouro().toUpperCase().indexOf(tipo.getSglTipoLogradouro().toUpperCase())==0) {
	    		enderecoApi.setIdeTipoLogradouro(tipo.getIdeTipoLogradouro());	
	    		Integer pos = tipo.getNomTipoLogradouro().length()+1;
	    		enderecoApi.setLogradouro(enderecoApi.getLogradouro().substring(pos));
	    	}
	    }
	    if(enderecoApi.getIdeTipoLogradouro() == null) {
	    	enderecoApi.setIdeTipoLogradouro(3);
	    }
	    tipoLogradouroEnt  = tipoLogradouroService.findTipoLogradouroByIde(enderecoApi.getIdeTipoLogradouro());
		Logradouro logradouroPesquisa = logradouroService.carregarLogradouro(enderecoApi.getLogradouro(),
					enderecoApi.getIdeBairro(), enderecoApi.getIdeMunicipio(),
					Integer.parseInt(enderecoApi.getCep()),enderecoApi.getIdeTipoLogradouro());
		if(Util.isNullOuVazio(logradouroPesquisa)) {
			isLogradouroInexistente = true;
		}else if(Util.isNotNullAndTrue(logradouroPesquisa.getIndOrigemApi()) && !Util.isNotNullAndTrue(logradouroPesquisa.getIndOrigemCorreio()) 
				&& Util.isNullOuVazio(logradouroPesquisa.getNomLogradouro())) {
			isLogradouroInexistente = true;
		}
		if(logradouroPesquisa==null) {
			logradouroPesquisa = new Logradouro();
			logradouroPesquisa.setNumCep(Integer.parseInt(enderecoApi.getCep()));
			logradouroPesquisa.setNomLogradouro(enderecoApi.getLogradouro());					
			logradouroPesquisa.setIdeBairro(bairroEnt);
			logradouroPesquisa.setIdeMunicipio(municipioEnt);
			logradouroPesquisa.setIdeTipoLogradouro(tipoLogradouroEnt);
			logradouroPesquisa.setIndOrigemApi(true);
			logradouroPesquisa.setNomOrigemApi(enderecoApi.getNomOrigem());
			logradouroPesquisa.setIndOrigemCorreio(false);
			logradouroService.salvarLogradouro(logradouroPesquisa);
		}else {
			logradouroPesquisa.setIndOrigemApi(true);
            logradouroService.salvarLogradouro(logradouroPesquisa);
		}
		logradouroEnt =logradouroPesquisa;
		logradouroPesquisa.setIdeBairro(bairroEnt);
		logradouroPesquisa.setIdeMunicipio(municipioEnt);
		logradouroPesquisa.setIdeTipoLogradouro(tipoLogradouroEnt);	
	

		return logradouroPesquisa;
		
	}

	
	public Municipio getMunicipioEnt() {
		return municipioEnt;
	}

	public void setMunicipioEnt(Municipio municipioEnt) {
		this.municipioEnt = municipioEnt;
	}

	public Bairro getBairroEnt() {
		return bairroEnt;
	}

	public void setBairroEnt(Bairro bairroEnt) {
		this.bairroEnt = bairroEnt;
	}

	public Logradouro getLogradouroEnt() {
		return logradouroEnt;
	}

	public void setLogradouroEnt(Logradouro logradouroEnt) {
		this.logradouroEnt = logradouroEnt;
	}

	public TipoLogradouro getTipoLogradouroEnt() {
		return tipoLogradouroEnt;
	}

	public void setTipoLogradouroEnt(TipoLogradouro tipoLogradouroEnt) {
		this.tipoLogradouroEnt = tipoLogradouroEnt;
	}


	public Estado getEstadoEnt() {
		return estadoEnt;
	}


	public void setEstadoEnt(Estado estadoEnt) {
		this.estadoEnt = estadoEnt;
	}


	public boolean isNewBairro() {
		return newBairro;
	}

	public boolean isLogradouroInexistente() {
		return isLogradouroInexistente;
	}

	public void setNewBairro(boolean newBairro) {
		this.newBairro = newBairro;
	}
	
}
