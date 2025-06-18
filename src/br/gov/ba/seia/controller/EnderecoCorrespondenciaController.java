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
import org.hibernate.HibernateException;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;


@Named("enderecoCorrespondenciaController")
@ViewScoped
public class EnderecoCorrespondenciaController extends EstadoMunicipioController {

	@EJB
	private EnderecoFacade enderecoService;
	
	@EJB
	private ServicoDeCepService cepService;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@EJB
	private BairroService bairroService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;

	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private MunicipioService municipioService;
	
	private Endereco endereco;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Bairro bairro;
	private TipoLogradouro tipoLogradouro;
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean showInputBairro;
	private boolean enableFormEndereco;
	private Pessoa pessoa;
	private Pessoa pessoaJuridica;
	private Pessoa pessoaRepresentanteLegal;
	private Pessoa pessoaProcuradorPessoaJuridica;
	private Empreendimento empreendimento;
	
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

		empreendimento = (Empreendimento) SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO");

		if (!Util.isNullOuVazio(empreendimento)) {
			carregarEnderecoCorrespondencia(empreendimento);
			enableFormEndereco = true;
		}
		else {

			empreendimento = new Empreendimento();
		}
	}
	public void salvarEndereco() {
		salvarEndereco(empreendimento);
		
	}
	
	public String salvarEnderecoPessoaJuridica() {
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
	}

	private void salvarEndereco(Empreendimento empreendimento) {
		try {
			if (bairro.getIdeBairro().equals(-1)) {
				persistirDependencias();
			}
			
			if (Util.isNull(logradouro.getIdeLogradouro()) || (!Util.isNull(logradouro.getIdeLogradouro()) && logradouro.getIdeLogradouro().equals(-1)) ) {
				persistirLogradouro();
			}
			
//			endereco.setIdeEndereco(null);
			endereco.setIdeLogradouro(logradouro);

			if (!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeEndereco())) {

				enderecoService.salvarEndereco(endereco);
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
			} else {

				enderecoService.salvarEndereco(endereco);
				salvarEnderecoCorrespondencia(empreendimento);
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			}
			
			if (bairro != null && !bairro.getIndOrigemCorreio()  && !bairro.isIndOrigemApi()) {
				bairro.setIdeBairro(-1);
			}

			enableFormEndereco = Boolean.TRUE;
			SessaoUtil.adicionarObjetoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
		} catch (Exception e) {
			if (!Util.isNullOuVazio(e) && !Util.isNullOuVazio(e.getMessage()) && e.getMessage().contains("bairro_uk")) {
				JsfUtil.addErrorMessage("O bairro informado já existe em nossa base de dados!");
			}else if (!Util.isNullOuVazio(e) && !Util.isNullOuVazio(e.getMessage()) && e.getMessage().contains("endereco_logradouro_fk")) {
				JsfUtil.addErrorMessage("O logradouro informado já existe em nossa base de dados!");
			} else if (!Util.isNullOuVazio(e) && !Util.isNullOuVazio(e.getMessage())) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void salvarEndereco(Pessoa pessoa) {
		try {
			if (bairro.getIdeBairro().equals(-1)) {
				persistirDependencias();
			}
			
			if (logradouro.getIdeLogradouro().equals(-1)) {
				persistirLogradouro();
			}

			endereco.setIdeLogradouro(logradouro);
			enderecoService.salvarEndereco(endereco);
			salvarEnderecoPessoa(pessoa);
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			enableFormEndereco = Boolean.TRUE;
		} catch (Exception e) {
			if (e.getMessage().contains("bairro_uk")) {
				JsfUtil.addErrorMessage("O bairro informado já existe em nossa base de dados!");
			}else if (e.getMessage().contains("endereco_logradouro_fk")) {
				JsfUtil.addErrorMessage("O logradouro informado já existe em nossa base de dados!");
			} else {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void salvarEnderecoCorrespondencia(Empreendimento empreendimento) {
		try {
			enderecoService.salvarEnderecoEmpreendimento(new EnderecoEmpreendimento(new TipoEndereco(Integer.valueOf(2)), endereco, empreendimento));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarEnderecoPessoa(Pessoa pessoa) {
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setIdePessoa(pessoa);
		enderecoPessoa.setIdeEndereco(endereco);
		enderecoPessoa.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId()));
		List<EnderecoPessoa> lista = new ArrayList<EnderecoPessoa>();
		lista.add(enderecoPessoa);
		endereco.setEnderecoPessoaCollection(lista);
		try {
			enderecoService.salvarEnderecoPessoa(enderecoPessoa);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void filtrarPorCep() {
		try {

			
			EnderecoDTO	enderecoApi = cepService.carregar(logradouroPesquisa.getNumCep().toString());		
			if(Util.isNullOuVazio(enderecoApi) || Util.isNullOuVazio(logradouroPesquisa.getNumCep())) {
				throw new Exception("CEP inválido.");
			}
			Logradouro logradouroPesquisa = cepService.montarEstrutura();
			if(listaLogradouro.contains(logradouroPesquisa)==false && !logradouroPesquisa.getNomLogradouro().isEmpty()) {
				listaLogradouro.add(logradouroPesquisa);
			}
			logradouro = new Logradouro(0);
			if(cepService.getLogradouroEnt()!=null) {
				logradouro = cepService.getLogradouroEnt();
				municipio = logradouro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
			}
			montaListaBairros();
			enableFormEndereco = true;
			bairro = new Bairro(0);
			if(cepService.getBairroEnt()!=null) {
				bairro = cepService.getBairroEnt();
			}
			showInputs = false;
			showInputLogradouro = false;
			endereco.setNumEndereco("");
			endereco.setDesComplemento("");
			endereco.setDesPontoReferencia("");
			
			
		} catch (Exception ex) {
			if (ex.getMessage().equals("-1") || ex.getMessage().equals("CEP inválido.")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
				enableFormEndereco = false;
			}
			if (ex.getMessage().equals("-2")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
				enableFormEndereco = false;
			} else {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
				enableFormEndereco = false;
			}
		}

	}

	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		try {
			Bairro bairroSelected = (Bairro)event.getNewValue();
			if (bairroSelected.getIdeBairro() == -1) {
				bairro = new Bairro();
				showInputs = Boolean.TRUE;
				showInputBairro = Boolean.TRUE;
				estado = new Estado(0);
				logradouroPesquisa.setIndOrigemApi(true);
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro()!=0 && bairroSelected.getIdeBairro()!=-1) {
				
				bairro = enderecoService.filtrarBairroById(bairroSelected);
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				
				if(Util.isNullOuVazio(listaLogradouro)) {
					listaLogradouro = enderecoService.filtrarLogradouroByBairroAndApi(bairro, logradouroPesquisa.getNumCep());
				}
				
				municipio = bairro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				showInputs = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		Exception erro = null;
		try {
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == -1) {
				showInputs = false;
				showInputLogradouro = true;
				logradouro = new Logradouro();
			}

			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro() != 0
					&& logradouroSelected.getIdeLogradouro() != -1) {
				logradouro = enderecoService.filtrarLogradouroById(logradouroSelected);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);// log
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}

	}
	
	private void persistirDependencias() {
		try {
			bairro.setIdeBairro(null);
			bairro.setIdeMunicipio(municipio);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);
			enderecoService.salvarBairro(bairro);
			persistirLogradouro();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	private void persistirLogradouro() {
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
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void montaListaBairros() {
		try {
			listaBairro = new ArrayList<Bairro>();
			listaBairro = enderecoService.filtrarBairroByCep(logradouroPesquisa);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
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

			if (!Util.isNull(endereco)) {
				logradouro = enderecoService.filtrarLogradouroById(endereco.getIdeLogradouro());
				bairro = enderecoService.filtrarBairroById(logradouro.getIdeBairro());
				municipio = bairro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				montaListaBairros();
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
			} else {
				endereco = new Endereco();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarEndereco(){
		carregarEnderecoCorrespondencia(this.empreendimento);
	}

	private void carregarEnderecoCorrespondencia(Empreendimento empreendimento) {
	
		try {
			
			endereco = enderecoService.filtrarEnderecoByEnderecoEmpreendimento(new EnderecoEmpreendimento(empreendimento, new TipoEndereco(2)));
			
			if (!Util.isNull(endereco)) {
				logradouro = enderecoService.filtrarLogradouroById(endereco.getIdeLogradouro());
				bairro = enderecoService.filtrarBairroById(logradouro.getIdeBairro());
				municipio = bairro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				
				montaListaBairros();
				
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				
				if(!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi())){
					showInputs = false;
					showInputLogradouro = true;
				}
				
				if (!Util.isNotNullAndTrue(bairro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(bairro.isIndOrigemApi())) {
					showInputs = true;
					showInputBairro = true;
					bairro.setIdeBairro(-1);
				}
			} else {
				endereco = new Endereco();
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
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
	
	public void limparEndereco(Boolean apagar){
		
		if(apagar){
			
			try{
				if(!Util.isNullOuVazio(endereco.getIdeEndereco())){
					EnderecoEmpreendimento enderecoCorrespondencia = enderecoEmpreendimentoService.obterEnderecoCorrespondenciaEmpreendimento(empreendimento.getIdeEmpreendimento());
					endereco.setIdeLogradouro(logradouro);
					enderecoEmpreendimentoService.removerEndereco(enderecoCorrespondencia);
					enderecoService.removerEndereco(endereco);					
				}
			}catch(HibernateException h){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, h);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi possível desvincular o endereço de correspondência: favor entrar em contato com o suporte");
			}
			
			endereco = new Endereco();
			logradouro = new Logradouro();
			logradouroPesquisa = new Logradouro();
			bairro = new Bairro();
			
			municipio = new Municipio();
			estado = new Estado();

			
			listaLogradouro = new ArrayList<Logradouro>();
			tipoLogradouro = new TipoLogradouro();
			listaBairro = new ArrayList<Bairro>();
			
			
			RequestContext.getCurrentInstance().addPartialUpdateTarget("tabAbas:panelEnderecoCorrespondencia");
	    }
	}
	
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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

	public void setPessoaProcuradorPessoaJuridica(Pessoa pessoaProcuradorPessoaJuridica) {
		this.pessoaProcuradorPessoaJuridica = pessoaProcuradorPessoaJuridica;
	}

	public Empreendimento getEmpreendimento() {

		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {

		this.empreendimento = empreendimento;
	}

	public boolean isDesabilitaTabImoveisResponsavelTecnico() {

		if (!Util.isNullOuVazio(getEndereco()) && !Util.isNullOuVazio(getEndereco().getIdeEndereco()))
			return false;
		return true;
	}

	public boolean isShowInputBairro() {
		return showInputBairro;
	}

	public void setShowInputBairro(boolean showInputBairro) {
		this.showInputBairro = showInputBairro;
	}

}