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

import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;


@Named("enderecoEmpreendimentoController")
@ViewScoped
public class EnderecoEmpreendimentoController extends EstadoMunicipioController {

	private Endereco endereco;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Bairro bairro;
	private TipoLogradouro tipoLogradouro;
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	private Pessoa pessoa;
	private Pessoa pessoaJuridica;
	private Pessoa pessoaRepresentanteLegal;
	private Pessoa pessoaProcuradorPessoaJuridica;
	private Empreendimento empreendimento;
	private Boolean showInputBairro;
	private boolean disableUFMunicipio = true;
	
	@EJB
	private EnderecoFacade enderecoService;
	
	@EJB
	private ServicoDeCepService cepService;
	
	@EJB
	private MunicipioService municipioService;

	@EJB
	private ImovelService imovelService;

	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;

	@EJB
	private BairroService bairroService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;

	public void resetaVariaveis() {
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
	}
	
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
			carregarEnderecoEmpreendimento(empreendimento);
			enableFormEndereco = true;
		}
		else {

			empreendimento = new Empreendimento();
		}
	}

	public String salvarEndereco() {
		salvarEndereco(getEmpreendimento());
		return "";
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
		Exception erro = null;
		try {
			
			if (bairro.getIdeBairro().equals(-1)) {
				montarDependencias();
			}
			
			montarLogradouro();
			if (Util.isNullOuVazio(logradouro.getIdeLogradouro()) || logradouro.getIdeLogradouro().equals(-1)) {
			}
			
			if(!Util.isNullOuVazio(logradouro) && (
					Util.isNullOuVazio(logradouro.getNomLogradouro()	)||
					Util.isNullOuVazio(logradouro.getIndOrigemCorreio() )||
					Util.isNullOuVazio(logradouro.getIndOrigemApi() )||
					Util.isNullOuVazio(logradouro.getNumCep()	 		)||
					Util.isNullOuVazio(logradouro.getMunicipio()		)||
					Util.isNullOuVazio(logradouro.getIdeBairro())
				)){
					
				Logradouro l = logradouroService.getLogradouroById(logradouro.getIdeLogradouro());
				
				if(!Util.isNullOuVazio(l) && !Util.isNullOuVazio(l.getNomLogradouro())){
					logradouro.setNomLogradouro(l.getNomLogradouro());
					
				}
				
				if(!Util.isNullOuVazio(l) && !Util.isNullOuVazio(l.getIndOrigemCorreio())){
					logradouro.setIndOrigemCorreio(l.getIndOrigemCorreio());
					
				}

				if(!Util.isNullOuVazio(l) && !Util.isNullOuVazio(l.getIndOrigemApi())){
					logradouro.setIndOrigemApi(l.getIndOrigemApi());
					
				}
				
				if(!Util.isNullOuVazio(l) && !Util.isNullOuVazio(l.getNumCep())){
					logradouro.setNumCep(l.getNumCep());
					
				}	
			}
			
			endereco.setIdeLogradouro(logradouro);

			if (!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeEndereco())) {
				enderecoService.salvarEnderecoEmpreendimento(endereco, logradouro, bairro, empreendimento);
				salvarEnderecoImovel(empreendimento, endereco);
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
			} else {
				montarEnderecoEmpreendimento(empreendimento);
				enderecoService.salvarEnderecoEmpreendimento(endereco,logradouro,bairro,empreendimento);
				salvarEnderecoImovel(empreendimento, endereco);
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			}
			
			if (bairro != null && !bairro.getIndOrigemCorreio() && !bairro.isIndOrigemApi()) {
				bairro.setIdeBairro(-1);
			}
			
			/*Verifica se o endereço é valido para Salvar*/
			if (isSemCep() == true) {
				enableFormEndereco = Boolean.FALSE;
			}else {
				enableFormEndereco = Boolean.TRUE;

			}
			
			SessaoUtil.adicionarObjetoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
		} catch (Exception e) {
		 erro = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Não foi possível salvar o endereço!!");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	// ticket 4147
	private void salvarEnderecoImovel(Empreendimento empreendimento, Endereco endereco) {
		
		if (!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getImovelCollection())) {

			for (Imovel lImovel : empreendimento.getImovelCollection()) {
				Exception erro = null; 
				
				try {
					lImovel = imovelService.buscarImovelPorIde(lImovel);
				} catch (Exception e) {
					erro =e;
					
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
				ImovelUrbano lImovelUrbano = lImovel.getImovelUrbano();

				if (!Util.isNullOuVazio(lImovelUrbano) && !Util.isNullOuVazio(lImovelUrbano.getNumInscricaoIptu())) {
					erro = null;
					try {
						lImovel.setIdeEndereco(endereco);
						imovelService.salvarAtualizarImovel(lImovel);
					}
					catch (Exception e) {
						erro =e;
						JsfUtil.addErrorMessage("Não foi possível setar endereço do imovel."+e.getMessage());
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
					
				}
				
				if (!Util.isNullOuVazio(lImovel.getIdeTipoImovel()) && lImovel.getIdeTipoImovel().getIdeTipoImovel() == 3) {
					erro = null;
					try {
						lImovel.setIdeEndereco(endereco);
						imovelService.salvarAtualizarImovel(lImovel);
					}
					catch (Exception e) {
						erro =e;
						JsfUtil.addErrorMessage("Não foi possível setar endereço do imovel."+e.getMessage());
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
					
				}
			}
		}
	}

	private void salvarEndereco(Pessoa pessoa) {
		Exception erro = null;
		try {
			if (bairro.getIdeBairro().equals(-1)) {
				montarDependencias();
			}
			
			if (logradouro.getIdeLogradouro().equals(-1)) {
				montarLogradouro();
			}

			endereco.setIdeLogradouro(logradouro);
			enderecoService.salvarEnderecoPF(endereco, logradouro, bairro, montarEnderecoPessoa(pessoa));
			
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			enableFormEndereco = Boolean.TRUE;
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);// log
			if (e.getMessage().equals("-1"))
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}
	
	}

	private EnderecoEmpreendimento montarEnderecoEmpreendimento(Empreendimento empreendimento) {
			EnderecoEmpreendimento endEmpreend = new EnderecoEmpreendimento(new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId()), endereco, empreendimento);
			empreendimento.setEnderecoEmpreendimentoCollection(new ArrayList<EnderecoEmpreendimento>());
			empreendimento.getEnderecoEmpreendimentoCollection().add(endEmpreend);
			return endEmpreend;

	}

	private EnderecoPessoa montarEnderecoPessoa(Pessoa pessoa) {
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setIdePessoa(pessoa);
		enderecoPessoa.setIdeEndereco(endereco);
		enderecoPessoa.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId()));
		List<EnderecoPessoa> lista = new ArrayList<EnderecoPessoa>();
		lista.add(enderecoPessoa);
		endereco.setEnderecoPessoaCollection(lista);
		return enderecoPessoa;
	}
	
	public void filtrarPorCep() {
		Exception erro = null;
		try {
			listaLogradouro = new ArrayList<Logradouro>();
			EnderecoDTO	enderecoApi = cepService.carregar(logradouroPesquisa.getNumCep().toString());
			if(Util.isNullOuVazio(enderecoApi) || Util.isNullOuVazio(logradouroPesquisa.getNumCep())) {
				throw new Exception("CEP inválido.");
			}

			logradouroPesquisa = cepService.montarEstrutura();
			boolean achou =false;
			
			if(listaLogradouro.contains(logradouroPesquisa)==false && !logradouroPesquisa.getNomLogradouro().isEmpty()) {
				listaLogradouro.add(logradouroPesquisa);
			}
			
			
			
			logradouro = new Logradouro(0);
			if(cepService.getLogradouroEnt()!=null) {
				logradouro = cepService.getLogradouroEnt();
				municipio = logradouro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
			}
			
			montaListaBairros();
			endereco.setIdeLogradouro(logradouroPesquisa);
			enableFormEndereco = true;
			bairro = new Bairro(0);
			if(cepService.getBairroEnt()!=null) {
				bairro = cepService.getBairroEnt();
			}
			showInputs = false;
			disableUFMunicipio=true;
			showInputLogradouro = false;
			endereco.setNumEndereco("");
			endereco.setDesComplemento("");
			endereco.setDesPontoReferencia("");
			
			
		} catch (Exception ex) {
			if (ex.getMessage().equals("-1") || ex.getMessage().equals("CEP inválido.")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
				resetaVariaveis();
			} if (ex.getMessage().equals("-2")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
			}else {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
			}
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		Exception erro = null;
		try {
			Bairro bairroSelected = (Bairro)event.getNewValue();
			if (bairroSelected.getIdeBairro() == -1) {
				showInputs = Boolean.TRUE;
				bairro = new Bairro();
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
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		Exception erro = null;
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
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private void montarDependencias() {
		Exception erro = null;
		try {
			bairro.setIdeBairro(null);
			bairro.setIdeMunicipio(municipio);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);

			montarLogradouro();
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	
	private void montarLogradouro() {
		Exception erro =null;
		try {
			logradouro.setIdeLogradouro(null);
			logradouro.setIdeMunicipio(municipio);
			logradouro.setIndOrigemCorreio(false);
			logradouro.setIndOrigemApi(false);
			logradouro.setIdeBairro(bairro);
			logradouro.setIdeTipoLogradouro(tipoLogradouro);
			logradouro.setNumCep(logradouroPesquisa.getNumCep());

		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private void montaListaBairros() {
		Exception erro = null;
		try {
			listaBairro = new ArrayList<Bairro>();
			listaBairro = enderecoService.filtrarBairroByCep(logradouroPesquisa);
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}	
	
	public Collection<Logradouro> getValuesComboLogradouro() {
	
		Logradouro selecione = new Logradouro(0, "Selecione...");
		Logradouro outro = new Logradouro(-1, "Outro");
		
		
		if(!listaLogradouro.contains(selecione)){
			listaLogradouro.add(0, selecione);			
		}
		if(!listaLogradouro.contains(outro)){
			listaLogradouro.add(listaLogradouro.size(), outro);
		}
		
		    
	     
	     return listaLogradouro;
	}
	
	public Collection<SelectItem> getValuesComboBairro() {
		 	Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<Bairro> i = listaBairro.iterator();
	        toReturn.add(new SelectItem(new Bairro(0),"Selecione..."));
	        while (i.hasNext()) {
	        	Bairro bairroObj = i.next();
	        	toReturn.add(new SelectItem(bairroObj, bairroObj.getNomBairro()));
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
		Exception erro = null;
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
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void carregarEnderecoEmpreendimento(Empreendimento empreendimento) {
		Exception erro = null;
		try {

			endereco = enderecoService.filtrarEnderecoByEnderecoEmpreendimento(new EnderecoEmpreendimento(empreendimento, new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId())));
			
			if (!Util.isNull(endereco)) {
				endereco = enderecoService.carregar(endereco.getIdeEndereco());
				logradouro = enderecoService.filtrarLogradouroById(endereco.getIdeLogradouro());
			
				if(!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi())){
					showInputs = false;
					showInputLogradouro = true;
				}
				
				bairro = enderecoService.filtrarBairroById(logradouro.getIdeBairro());
				municipio = logradouro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				
				montaListaBairros();
				
				listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				
				if (!Util.isNotNullAndTrue(bairro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(bairro.isIndOrigemApi())) {
					showInputs = true;
					showInputBairro = true;
					bairro.setIdeBairro(-1);
				}
			} else {
				endereco = new Endereco();
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
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
	
	private boolean isSemCep(){
		return Util.isNullOuVazio(logradouroPesquisa.getNumCep());
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

	public void setPessoaProcuradorPessoaJuridica(Pessoa pessoaProcuradorPessoaJuridica) {
		this.pessoaProcuradorPessoaJuridica = pessoaProcuradorPessoaJuridica;
	}

	public Empreendimento getEmpreendimento() {

		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {

		this.empreendimento = empreendimento;
	}

	public boolean isDesabilitaEnderecoCorrespondencia() {

		if (!Util.isNullOuVazio(getEndereco()) && !Util.isNullOuVazio(getEndereco().getIdeEndereco()))
			return false;
		return true;
	}

	public Boolean getShowInputBairro() {
		return showInputBairro;
	}

	public void setShowInputBairro(Boolean showInputBairro) {
		this.showInputBairro = showInputBairro;
	}

	public boolean isDisableUFMunicipio() {
		return disableUFMunicipio;
	}

	public void setDisableUFMunicipio(boolean disableUFMunicipio) {
		this.disableUFMunicipio = disableUFMunicipio;
	}

}