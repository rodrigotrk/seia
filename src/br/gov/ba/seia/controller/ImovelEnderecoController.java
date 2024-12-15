package br.gov.ba.seia.controller;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Named("imovelEnderecoController")
@ViewScoped
public class ImovelEnderecoController {
		
	private Endereco endereco;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa = new Logradouro();
	private Bairro bairro;
	private Municipio municipio;
	private Estado estado;
	private TipoLogradouro tipoLogradouro;
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private List<Municipio> listaMunicipio;
	private List<Estado> listaEstado;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	private Pessoa pessoa;
	private Pessoa pessoaJuridica;
	private Pessoa pessoaRepresentanteLegal;
	private Pessoa pessoaProcuradorPessoaJuridica;
	private EnderecoPessoa enderecoPessoa;
	private boolean escolheuBairro = false;
	private Imovel imovel;
	
	
	@EJB
	private EnderecoFacade enderecoService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private MunicipioService municipioService;
	private Boolean isMunicipioSalvador;
	
	@EJB
	private BairroService bairroService;

	@EJB
	private AuditoriaFacade auditoriaFacade;

	@EJB
	private LogradouroService logradouroService;
	

	@PostConstruct
	public void init() {
		endereco = new Endereco();
	    logradouro = new Logradouro();
	    logradouroPesquisa = new Logradouro();
	    listaLogradouro = new ArrayList<Logradouro>();
	    tipoLogradouro = new TipoLogradouro();
	    bairro = new Bairro();
	    listaBairro = new ArrayList<Bairro>();
	    municipio = new Municipio();
	    estado = new Estado();
	    enableFormEndereco = false;
	    pessoa = new Pessoa();
	    pessoaJuridica = new Pessoa();
	    pessoaRepresentanteLegal = new Pessoa();
	    pessoaProcuradorPessoaJuridica = new Pessoa();
	    //Para carregar o endereço quando estiver em imovel rural
	    if(!Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural())){
			this.imovel = ContextoUtil.getContexto().getImovelRural().getImovel();
			carregarEndereco();
		}
	}
	
	public String salvarEndereco() {
		salvarEnderecoImovel();
		return "";
	}
	
	/*public String salvarEnderecoPessoaJuridica() {
		salvarEndereco(pessoaJuridica);
		return "";
	}
	
	public String salvarEnderecoPessoaRepresentanteLegal() {
		salvarEndereco(pessoaRepresentanteLegal);
		return "";
	}
	
	public String salvarEnderecoPessoaProcuradorPessoaJuridica() {
		salvarEndereco(pessoaProcuradorPessoaJuridica);
		return "";
	}*/
	
	public void limparEnderecoImovel(){
		this.endereco = null;
		this.logradouro = null;
		this.bairro = null;
		this.tipoLogradouro = null;
		this.estado = null;
	}
	
	private void salvarEnderecoImovel() {
		try {
			if (bairro.getIdeBairro().equals(-1)) {
				montarDependencias();
			}else if(Util.isNullOuVazio(logradouro.getIdeLogradouro())){
				montarLogradouro();
			}
						
//			endereco.setIdeEndereco(null);
			endereco.setIdeLogradouro(logradouro);
			//Auditoria
			if(!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeEndereco())){
				Endereco objEnderecoAntigo = enderecoService.carregarTudo(endereco);
				Bairro objBairroNovo = bairroService.filtrarBairroById(bairro);
				Logradouro objLogradouroNovo = logradouroService.filtrarLogradouroById(logradouro);
				enderecoService.salvarEnderecoImovel(endereco, logradouro, bairro, this.imovel);
				auditoriaFacade.atualizar(objEnderecoAntigo, endereco);
				auditoriaFacade.atualizar(objEnderecoAntigo.getIdeLogradouro().getIdeBairro(), objBairroNovo);
				auditoriaFacade.atualizar(objEnderecoAntigo.getIdeLogradouro(), objLogradouroNovo);
				JsfUtil.addSuccessMessage("Alteração de Endereço efetuada com sucesso!");
			} else { 
				enderecoService.salvarEnderecoImovel(endereco,logradouro,bairro,this.imovel);
				auditoriaFacade.salvar(endereco);
				auditoriaFacade.salvar(bairro);
				auditoriaFacade.salvar(logradouro);
				JsfUtil.addSuccessMessage("Inclusão de Endereço efetuada com sucesso!");
			}
			//enderecoService.salvarEnderecoImovel(endereco,logradouro,bairro,this.imovel);
			endereco = enderecoService.carregar(endereco.getIdeEndereco());
			//salvarEnderecoPessoa(pessoa);
			ContextoUtil.getContexto().getImovelRural().getImovel().setIdeEndereco(endereco);
			ContextoUtil.getContexto().setEnderecoSalvo(true);
			enableFormEndereco = Boolean.TRUE;
		} catch (Exception e) {
			if (e.getMessage().contains("bairro_uk")) {
				JsfUtil.addErrorMessage("O bairro informado já existe em nossa base de dados!");
			}else if (e.getMessage().contains("endereco_logradouro_fk")) {
				JsfUtil.addErrorMessage("O logradouro informado já existe em nossa base de dados!");
			} else {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
	}
	
//	private void salvarEnderecoPessoa(Pessoa pessoa) {
//		if (Util.isNull(enderecoPessoa)) {
//			enderecoPessoa = new EnderecoPessoa();
//		}		
//		enderecoPessoa.setIdePessoa(pessoa);
//		enderecoPessoa.setIdeEndereco(endereco);
//		enderecoPessoa.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId()));
//		List<EnderecoPessoa> lista = new ArrayList<EnderecoPessoa>();
//		lista.add(enderecoPessoa);
//		endereco.setEnderecoPessoaCollection(lista);
//		try {
//			enderecoService.salvarEnderecoPessoa(enderecoPessoa);
//		} catch (Exception e) {
//			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
//		}
//	}
	
	public void filtrarPorCep() {
		try {
			logradouro = new Logradouro(0);
			montaListaBairros();
			enableFormEndereco = true;
			bairro = new Bairro(0);
			showInputs = false;
			escolheuBairro = false;
			showInputLogradouro = false;
			endereco.setNumEndereco("");
			endereco.setDesComplemento("");
			endereco.setDesPontoReferencia("");
			
			if(!Util.isNullOuVazio(logradouroPesquisa.getIdeMunicipio()) && !Util.isNullOuVazio(logradouroPesquisa.getIdeMunicipio().getIdeMunicipio()) && logradouroPesquisa.getIdeMunicipio().getIdeMunicipio().equals(837)){
				isMunicipioSalvador = true;
			}else{
				isMunicipioSalvador = false;
			}
			municipio = logradouroPesquisa.getIdeMunicipio();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		try {
			Bairro bairroSelected = (Bairro)event.getNewValue();
			if (bairroSelected.getIdeBairro() == -1) {
				showInputs = true;
				bairro = new Bairro();
				escolheuBairro  = false;
				estado = new Estado(0);
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro()!=0 && bairroSelected.getIdeBairro()!=-1) {
				bairro = enderecoService.filtrarBairroById(bairroSelected);
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				municipio = bairro.getIdeMunicipio();
				isMunicipioSalvador = municipio.getIdeMunicipio().equals(837);
				estado = municipio.getIdeEstado();
				showInputs = false;
				escolheuBairro = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		try {
			Logradouro logradouroSelected = (Logradouro)event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == -1) {
				showInputs = false;
				showInputLogradouro = true;
				logradouro = new Logradouro();
			}
			
			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro()!=0 && logradouroSelected.getIdeLogradouro()!=-1) {
				logradouro = enderecoService.filtrarLogradouroById(logradouroSelected);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	public void changeEstado() {
		if(estado !=null && !(estado.getIdeEstado()==null)) {
			try {
				listaMunicipio = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(estado);
			    
				if(Util.isNullOuVazio(imovel))
					this.imovel = ContextoUtil.getContexto().getImovelRural().getImovel();
			    
				imovel = imovelService.buscarImovelPorIdeSemImRural(imovel);
				if(!Util.isNullOuVazio(imovel) && !Util.isNullOuVazio(imovel.getIdeEndereco()) 
					&& !Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro()) 
					&& !Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio())){
					setMunicipio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio());
				}else{
					if (estado.getIdeEstado().intValue() == 5 && (isMunicipioSalvador == null || isMunicipioSalvador)) {
						setMunicipio(new Municipio(837, "Salvador"));
						isMunicipioSalvador = null;
					}
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		} else {
			listaMunicipio = new ArrayList<Municipio>();
		}
	}
	
	
	private void montarDependencias() {
		try {
			bairro.setIdeBairro(null);
			bairro.setIdeMunicipio(municipio);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);
			//enderecoService.salvarBairro(bairro);
			montarLogradouro();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	
	private void montarLogradouro() {
		try {
			logradouro.setIdeLogradouro(null);
			logradouro.setIdeMunicipio(municipio);
			logradouro.setIndOrigemCorreio(false);
			logradouro.setIndOrigemApi(false);
			logradouro.setIdeBairro(bairro);
			logradouro.setIdeTipoLogradouro(tipoLogradouro);
			logradouro.setNumCep(logradouroPesquisa.getNumCep());
			enderecoService.salvarLogradouro(logradouro);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	private void montaListaBairros() {
		try {
			listaBairro = new ArrayList<Bairro>();
			listaBairro = enderecoService.filtrarBairroByCep(logradouroPesquisa);
			if(!Util.isNullOuVazio(listaBairro) && !listaBairro.isEmpty())
				logradouroPesquisa.setIdeMunicipio(listaBairro.get(0).getIdeMunicipio());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}	
	
	public Collection<SelectItem> getValuesComboLogradouro() {
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	     Iterator<Logradouro> i = listaLogradouro.iterator();
	     toReturn.add(new SelectItem(new Logradouro(0),"Selecione..."));
	     while (i.hasNext()) {
	      	Logradouro logradouro = i.next();
	       	toReturn.add(new SelectItem(logradouro, logradouro.getNomLogradouro()));  
	     }
	     toReturn.add(new SelectItem(new Logradouro(-1),"Outro"));
	     return toReturn;
	}
	
	public Collection<SelectItem> getValuesComboBairro() {
		 	Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<Bairro> i = listaBairro.iterator();
	        toReturn.add(new SelectItem(new Bairro(0),"Selecione..."));
	        while (i.hasNext()) {
	        	Bairro bairro = i.next();
	        	toReturn.add(new SelectItem(bairro, bairro.getNomBairro()));
	        }
	        toReturn.add(new SelectItem(new Bairro(-1),"Outro"));
	        return toReturn;
	}
	
	public void carregarEnderecoPessoa() {
		carregarEnderecoPessoa(pessoa);
	}
	
	public void carregarEnderecoPessoaJuridica() {
		tratarPessoaJuridicaSessao();
		carregarEnderecoPessoa(pessoaJuridica);
		
	}
	
	public void carregarEnderecoPessoaRepresentanteLegal() {
		carregarEnderecoPessoa(pessoaRepresentanteLegal);
	}
	
	public void carregarEnderecoPessoaProcuradorPessoaJuridica() {
		carregarEnderecoPessoa(pessoaProcuradorPessoaJuridica);
		
	}
	
	private void carregarEnderecoPessoa(Pessoa pessoa) {
		try {
			enderecoPessoa = enderecoService.filtrarEnderecoByPessoa(pessoa);
			if (!Util.isNull(enderecoPessoa)) {
				endereco = enderecoPessoa.getIdeEndereco();
				logradouro = enderecoService.filtrarLogradouroById(endereco.getIdeLogradouro());
				if (!logradouro.getIndOrigemCorreio()) {
					showInputLogradouro = true;
					showInputs = true;
				}
				bairro = enderecoService.filtrarBairroById(logradouro.getIdeBairro());
				municipio = bairro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				if(logradouroPesquisa==null)
					logradouroPesquisa = new Logradouro();
				
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				montaListaBairros();
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
			} else {
				endereco = new Endereco();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}
	
	private void carregarEndereco(){
		endereco = imovel.getIdeEndereco();
		if(Util.isNullOuVazio(endereco)){
			endereco = new Endereco();
		}
		if (!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeEndereco())) {
			try {
				this.endereco = enderecoService.carregar(this.endereco.getIdeEndereco());
				logradouro = enderecoService.filtrarLogradouroById(endereco
						.getIdeLogradouro());

				if (!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi()) ) {
					showInputLogradouro = true;
					showInputs = true;
				}
				bairro = enderecoService.filtrarBairroById(logradouro
						.getIdeBairro());
				municipio = bairro.getIdeMunicipio();
				
				estado = municipio.getIdeEstado();
				if(logradouroPesquisa==null)
					logradouroPesquisa = new Logradouro();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				montaListaBairros();
				if(!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi()) ){
					listaBairro.add(bairro);
				}
				listaLogradouro = enderecoService
						.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				enableFormEndereco = true;
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
	}
	
	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica().getPessoa();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new Pessoa();
			} 
		}
	}
	
	public void SetImovelCarregarEndereco(ImovelRural imovelRural){
		if(!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getImovel())){
			this.imovel = 	imovelRural.getImovel();	
			carregarEndereco();
			this.enableFormEndereco = false;
		}
		else {
			init();
		}
		
	}
	
	public Boolean getlimparEnderecoImovelDepoisDeImovelSalvo(){
		if(ContextoUtil.getContexto().getBloquearEnderecoImovelDepoisDeImovelSalvo()){
			limparEnderecoImovel();
			this.logradouroPesquisa = null;
			this.enableFormEndereco = false;
			ContextoUtil.getContexto().setBloquearEnderecoImovelDepoisDeImovelSalvo(false);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
		carregarEndereco();
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public Logradouro getLogradouroPesquisa() {
		return logradouroPesquisa;
	}

	public void setLogradouroPesquisa(Logradouro logradouroPesquisa) {
		this.logradouroPesquisa = logradouroPesquisa;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public List<Logradouro> getListaLogradouro() {
		return listaLogradouro;
	}

	public void setListaLogradouro(List<Logradouro> listaLogradouro) {
		this.listaLogradouro = listaLogradouro;
	}

	public List<Bairro> getListaBairro() {
		return listaBairro;
	}

	public void setListaBairro(List<Bairro> listaBairro) {
		this.listaBairro = listaBairro;
	}

	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(List<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public boolean isShowInputs() {
		return showInputs;
	}

	public void setShowInputs(boolean showInputs) {
		this.showInputs = showInputs;
	}

	public boolean isShowInputLogradouro() {
		return showInputLogradouro;
	}

	public void setShowInputLogradouro(boolean showInputLogradouro) {
		this.showInputLogradouro = showInputLogradouro;
	}

	public boolean isEnableFormEndereco() {
		return enableFormEndereco;
	}

	public void setEnableFormEndereco(boolean enableFormEndereco) {
		this.enableFormEndereco = enableFormEndereco;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getIdePessoa()) ) {
			carregarEnderecoPessoa();
		}
	}

	public Pessoa getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(Pessoa pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarEnderecoPessoaJuridica();
	}

	public Pessoa getPessoaRepresentanteLegal() {
		return pessoaRepresentanteLegal;
	}

	public void setPessoaRepresentanteLegal(Pessoa pessoaRepresentanteLegal) {
		this.pessoaRepresentanteLegal = pessoaRepresentanteLegal;
	}

	public Pessoa getPessoaProcuradorPessoaJuridica() {
		return pessoaProcuradorPessoaJuridica;
	}

	public void setPessoaProcuradorPessoaJuridica(
			Pessoa pessoaProcuradorPessoaJuridica) {
		this.pessoaProcuradorPessoaJuridica = pessoaProcuradorPessoaJuridica;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}
	
	public boolean getDesabilitarLogradouro() {
		return !(enableFormEndereco && escolheuBairro);
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
		if(!Util.isNull(imovel)){
			carregarEndereco();
		}
	}

	
	public Boolean getIsMunicipioSalvador() {
		return isMunicipioSalvador;
	}

	
	public void setIsMunicipioSalvador(Boolean isMunicipioSalvador) {
		this.isMunicipioSalvador = isMunicipioSalvador;
	}

}
