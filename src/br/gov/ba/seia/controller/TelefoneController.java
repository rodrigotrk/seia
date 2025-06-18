package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

@Named("telefoneController")
@ViewScoped
public class TelefoneController {
	
	@EJB
	private TipoTelefoneService tipoTelefoneService;	
	@EJB
	private TelefoneService telefoneService;
	
	
	
	private Telefone telefone;
	private Telefone telefoneBck;
	private TipoTelefone tipoTelefone;	
	private List<Telefone> listaTelefone;
	private DataModel<Telefone> modelTelefone;	
	private Collection<TipoTelefone> listaTiposTelefone;
	private boolean flagValidate;	
	private Pessoa pessoa;
	
	private Pessoa pessoaJuridica;
	private Pessoa pessoaProcurador;
	private Pessoa pessoaRepresentanteLegal;
	
	private List<Telefone> listaTelefonePessoaJuridica;
	private DataModel<Telefone> modelTelefonePessoaJuridica;
	private List<Telefone> listaTelefonePessoaProcurador;
	private DataModel<Telefone> modelTelefonePessoaProcurador;
	private List<Telefone> listaTelefonePessoaRepresentanteLegal;
	private DataModel<Telefone> modelTelefonePessoaRepresentanteLegal;
	
	
	private boolean visualiza; 
	private boolean mostraLista;
	private boolean mostraListaPessoaJuridica;
	private boolean mostraListaProcuradorPessoaJuridica;
	private boolean mostraListaRepresentanteLegal;
	
	@PostConstruct
	public void init() {
		modelTelefone = new ListDataModel<Telefone>();
		pessoa = new Pessoa();	
		telefone = new Telefone();
		visualiza= false;		
		pessoaJuridica = new Pessoa();
		pessoaProcurador = new Pessoa();
		pessoaRepresentanteLegal = new Pessoa();
		tratarPessoaJuridicaSessao();
		carregarListaTelefone();
		carregarListaTelefonePessoaJuridica();
		carregarListaTelefoneProcurador();
		carregarListaTelefoneRepresentanteLegal();
		carregarListaTiposTelefone();
		mostraLista = false;
	}
	
	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica().getPessoa();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new Pessoa();
			} 
		}
	}
	
	public void carregarTelefones(){
		modelTelefone = carregarTelefones(pessoa);
	}
	
	public void carregarTelefonesPessoaJuridica(){
		modelTelefonePessoaJuridica = carregarTelefones(pessoaJuridica);
	}
	
	public void carregarTelefonesRepresentanteLegal(){
		 modelTelefonePessoaRepresentanteLegal = carregarTelefones(pessoaRepresentanteLegal);
	}
	
	public void carregarTelefonesProcurador(){
		modelTelefonePessoaProcurador =carregarTelefones(pessoaProcurador);
	}
	
	private DataModel<Telefone> carregarTelefones(Pessoa pessoa) {
		return new ListDataModel<Telefone>((List<Telefone>) this.pessoa.getTelefoneCollection());
	}
	
	
	public void carregarListaTiposTelefone() {
		Exception erro = null;
		try {
			listaTiposTelefone =  tipoTelefoneService.listarTipoTelefone();
		} catch (Exception e) {
			erro =e;
			//Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public Collection<SelectItem> getValuesComboBoxTipoTelefone(){
		carregarListaTiposTelefone();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	     Iterator<TipoTelefone> i = listaTiposTelefone.iterator();
	     while (i.hasNext()) {
	       	TipoTelefone tipoTelefone = i.next();
	       	toReturn.add(new SelectItem(tipoTelefone, tipoTelefone.getNomTipoTelefone()));  
	     }
	      return toReturn;
     }
	
	public void carregarListaTelefone() {
		modelTelefone = Util.castListToDataModel(carregarListaTelefone(pessoa));
	}
	
	public void carregarListaTelefonePessoaJuridica() {
		modelTelefonePessoaJuridica= Util.castListToDataModel(carregarListaTelefone(pessoaJuridica));
	}
	
	public void carregarListaTelefoneRepresentanteLegal() {
		modelTelefonePessoaRepresentanteLegal = Util.castListToDataModel(carregarListaTelefone(pessoaRepresentanteLegal));
	}
	
	public void carregarListaTelefoneProcurador() {
		modelTelefonePessoaProcurador = Util.castListToDataModel(carregarListaTelefone(pessoaProcurador));
	}
	
	public List<Telefone> carregarListaTelefone(Pessoa pessoa) {
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		listaPessoa.add(pessoa);
		Telefone telefoneFiltro = new Telefone();
		telefoneFiltro.setPessoaCollection(listaPessoa);
		
		Exception erro =null;
		try {
			return telefoneService.buscarTelefonesPorPessoa(pessoa);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	private List<Telefone> carregarListaTelefoneNamedQuery(Pessoa pessoa) {
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		listaPessoa.add(pessoa);
		Telefone telefoneFiltro = new Telefone();
		telefoneFiltro.setPessoaCollection(listaPessoa);
		
		Exception erro = null;
		
		try {
			return telefoneService.buscarTelefonesPorPessoa(pessoa);
		} catch (Exception e) {
			erro= e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void adicionaTelefone() {
		 adicionaTelefone(pessoa);
		 limparTelefone();
		 carregarListaTelefone();
	}
	
	public void adicionaTelefonePessoaJuridica() {
		 adicionaTelefone(pessoaJuridica);
 		 carregarListaTelefonePessoaJuridica();
	}
	
	public void adicionaTelefoneRepresentanteLegal() {
		adicionaTelefone(pessoaRepresentanteLegal);	
		Html.esconder("telefoneDialogRepLegal");
		RepresentanteLegalController rlc = (RepresentanteLegalController) SessaoUtil.recuperarManagedBean("#{representanteLegalController}", RepresentanteLegalController.class);
		
		if(Util.getAttributeSession("representanteLegalSelecionado") != null) {
			rlc.setRepresentanteLegalSelecionado((RepresentanteLegal) Util.getAttributeSession("representanteLegalSelecionado"));
			rlc.setIniciarAbaTelefone(true);
			rlc.prepararParaEdicao();
		}
		
		carregarListaTelefoneRepresentanteLegal();
	}
	
	public void adicionaTelefoneProcurador() {
		adicionaTelefone(pessoaProcurador);
		carregarListaTelefoneProcurador();
	}
	
	private void adicionaTelefone(Pessoa pessoa) {
		String msg = "Inclusão efetuada com sucesso!";
		if (!Util.isNull(telefone.getIdeTelefone())) {
			msg = "Alteração efetuada com sucesso!";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		final Integer ideTelefoneEditado = telefone.getIdeTelefone();
		
		Exception erro =null;
		try {
			Telefone telefonePersist = null;
			telefonePersist = telefone;			
			adicionaTelefonePessoa(pessoa);
			if (validate(pessoa)) {
				telefonePersist.setIdeTelefone(ideTelefoneEditado);
				telefoneService.salvarTelefone(telefonePersist);
				flagValidate = true;
				 JsfUtil.addSuccessMessage(msg);
			} else {
				flagValidate = false;
			}
			context.addCallbackParam("flagValidate", flagValidate);
			carregarListaTelefone(pessoa);
			this.limparTelefone();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	
	private void limparTelefone() {
		telefone = new Telefone();		
		visualiza = false;
	}
	
	public String limparTelefoneAction() {
		limparTelefone();
		return null;
	}
		
	private Boolean validate(Pessoa pessoa) {
		List<Telefone> listaAtualizadaBanco  = new ArrayList<Telefone>();
		Exception erro =null;
		try {
			listaAtualizadaBanco = telefoneService.filtrarTelefonesNaoExcluidosPorPessoa(pessoa);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		Boolean retorno = Boolean.TRUE;
		Telefone telefoneValidate = null;		
		telefoneValidate = telefone;
		
		if(Util.isNull(telefoneValidate.getNumTelefone())  || Util.isEmptyString(telefoneValidate.getNumTelefone())) {  
			retorno = Boolean.FALSE;
            JsfUtil.addErrorMessage("O campo Número é de preenchimento obrigatório.");  
        }
		
		if(Util.isNull(telefoneValidate.getIdeTipoTelefone())  || Util.isEmptyString(telefoneValidate.getIdeTipoTelefone().getNomTipoTelefone())) {  
			retorno = Boolean.FALSE;
            JsfUtil.addErrorMessage("O campo Tipo é de preenchimento obrigatório.");  
        }
		telefoneValidate.setIdeTelefone(null);
		if (Util.isObjectInList(telefoneValidate, listaAtualizadaBanco)) {
			retorno = Boolean.FALSE;
			JsfUtil.addErrorMessage("O Tipo de telefone e o Número já foram inseridos."); 
		}
		return retorno;
	}
		
	
	private void adicionaTelefonePessoa(Pessoa pessoa) {
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(pessoa);
		telefone.setPessoaCollection(lista);
	}
	
	public void removerTelefone() {
		removerTelefonePessoa();
		carregarListaTelefone();
	}
	
	public void removerTelefonePessoaJuridica() {
		removerTelefonePessoa(); 
		carregarListaTelefonePessoaJuridica();
	}
	
	public void removerTelefonePessoaRepresentanteLegal() {
		removerTelefonePessoa();
		carregarListaTelefoneRepresentanteLegal();
	}
	
	public void removerTelefonePessoaProcurador() {
		removerTelefonePessoa(); 
		carregarListaTelefoneProcurador();
	}
	
	
	private void removerTelefonePessoa() {
		Exception erro = null;
		
		try {
			telefoneService.excluirTelefone(telefone);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void abrirDialogTelefoneRepLegal(Telefone telefoneParam) {
		this.telefone = telefoneParam;
		Html.exibir("telefoneDialogRepLegal");
		Html.atualizar("telefoneDialogRepLegal");
	}
	
    public void limpar() {
    	telefone = new Telefone();
    	visualiza= false;
    	
    }
    
    public void limparTelefoneResponsavelLegal() { 
    	telefone = new Telefone(); 
    	visualiza= false; 
    	Html.atualizar("telefoneDialogRepLegalForm"); 
    	 
    } 
    
    public void prepararParaEdicao() {
    	
    	try {
    		telefone = telefoneService.filtrarTelefonePorID(telefone.getIdeTelefone());
    		visualiza = false;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao buscar o telefone.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
    }
   
	public DataModel<Telefone> getModelTelefone() {
		return modelTelefone;
	}
	public void setModelTelefone(DataModel<Telefone> modelTelefone) {
		this.modelTelefone = modelTelefone;
	}
	public Telefone getTelefone() {
		return telefone;
	}
	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;		
	}
	
	public List<Telefone> getListaTelefone() {
		return listaTelefone;
	}
	public void setListaTelefone(List<Telefone> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}
	public Collection<TipoTelefone> getListaTiposTelefone() {
		return listaTiposTelefone;
	}
	public void setListaTiposTelefone(Collection<TipoTelefone> listaTiposTelefone) {
		this.listaTiposTelefone = listaTiposTelefone;
	}
	public boolean isFlagValidate() {
		return flagValidate;
	}
	public void setFlagValidate(boolean flagValidate) {
		this.flagValidate = flagValidate;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		carregarListaTelefone();
	}



	public boolean isVisualiza() {
		return visualiza;
	}



	public void setVisualiza(boolean visualiza) {
		this.visualiza = visualiza;
	}



	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}



	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}



	public Pessoa getPessoaJuridica() {
		return pessoaJuridica;
	}



	public void setPessoaJuridica(Pessoa pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarListaTelefonePessoaJuridica();
	}



	public Pessoa getPessoaProcurador() {
		return pessoaProcurador;
	}



	public void setPessoaProcurador(Pessoa pessoaProcurador) {
		this.pessoaProcurador = pessoaProcurador;
		carregarListaTelefoneProcurador();
	}



	public Pessoa getPessoaRepresentanteLegal() {
		return pessoaRepresentanteLegal;
	}



	public void setPessoaRepresentanteLegal(Pessoa pessoaRepresentanteLegal) {
		this.pessoaRepresentanteLegal = pessoaRepresentanteLegal;
		carregarListaTelefoneRepresentanteLegal();
	}

	public List<Telefone> getListaTelefonePessoaJuridica() {
		return listaTelefonePessoaJuridica;
	}

	public void setListaTelefonePessoaJuridica(
			List<Telefone> listaTelefonePessoaJuridica) {
		this.listaTelefonePessoaJuridica = listaTelefonePessoaJuridica;
	}

	public DataModel<Telefone> getModelTelefonePessoaJuridica() {
		return modelTelefonePessoaJuridica;
	}

	public void setModelTelefonePessoaJuridica(
			DataModel<Telefone> modelTelefonePessoaJuridica) {
		this.modelTelefonePessoaJuridica = modelTelefonePessoaJuridica;
	}

	public List<Telefone> getListaTelefonePessoaProcurador() {
		return listaTelefonePessoaProcurador;
	}

	public void setListaTelefonePessoaProcurador(
			List<Telefone> listaTelefonePessoaProcurador) {
		this.listaTelefonePessoaProcurador = listaTelefonePessoaProcurador;
	}

	public DataModel<Telefone> getModelTelefonePessoaProcurador() {
		return modelTelefonePessoaProcurador;
	}

	public void setModelTelefonePessoaProcurador(
			DataModel<Telefone> modelTelefonePessoaProcurador) {
		this.modelTelefonePessoaProcurador = modelTelefonePessoaProcurador;
	}

	public List<Telefone> getListaTelefonePessoaRepresentanteLegal() {
		return listaTelefonePessoaRepresentanteLegal;
	}

	public void setListaTelefonePessoaRepresentanteLegal(
			List<Telefone> listaTelefonePessoaRepresentanteLegal) {
		this.listaTelefonePessoaRepresentanteLegal = listaTelefonePessoaRepresentanteLegal;
	}

	public DataModel<Telefone> getModelTelefonePessoaRepresentanteLegal() {
		return modelTelefonePessoaRepresentanteLegal;
	}

	public void setModelTelefonePessoaRepresentanteLegal(
			DataModel<Telefone> modelTelefonePessoaRepresentanteLegal) {
		this.modelTelefonePessoaRepresentanteLegal = modelTelefonePessoaRepresentanteLegal;
	}

	public boolean isMostraLista() {
		if(!Util.isNullOuVazio(modelTelefone) && modelTelefone.getRowCount()>0) {
			mostraLista = true;
		}
		else {
			mostraLista = false;
		}
		return mostraLista;
	}

	public void setMostraLista(boolean mostraLista) {
		this.mostraLista = mostraLista;
	}

	public boolean isMostraListaPessoaJuridica() {
		if(!Util.isNullOuVazio(modelTelefonePessoaJuridica) && modelTelefonePessoaJuridica.getRowCount()>0) {
			mostraListaPessoaJuridica = true;
		}
		else {
			mostraListaPessoaJuridica = false;
		}
		return mostraListaPessoaJuridica;
	}

	public void setMostraListaPessoaJuridica(boolean mostraListaPessoaJuridica) {
		this.mostraListaPessoaJuridica = mostraListaPessoaJuridica;
	}

	public boolean isMostraListaProcuradorPessoaJuridica() {
		if(!Util.isNullOuVazio(modelTelefonePessoaProcurador) && modelTelefonePessoaProcurador.getRowCount()>0) {
			mostraListaProcuradorPessoaJuridica = true;
		}
		else {
			mostraListaProcuradorPessoaJuridica = false;
		}
		return mostraListaProcuradorPessoaJuridica;
	}

	public void setMostraListaProcuradorPessoaJuridica(
			boolean mostraListaProcuradorPessoaJuridica) {
		this.mostraListaProcuradorPessoaJuridica = mostraListaProcuradorPessoaJuridica;
	}

	public boolean isMostraListaRepresentanteLegal() {
		if(!Util.isNullOuVazio(modelTelefonePessoaRepresentanteLegal) && modelTelefonePessoaRepresentanteLegal.getRowCount()>0) {
			mostraListaRepresentanteLegal = true;
		}
		else {
			mostraListaRepresentanteLegal = false;
		}
		return mostraListaRepresentanteLegal;
	}

	public void setMostraListaRepresentanteLegal(
			boolean mostraListaRepresentanteLegal) {
		this.mostraListaRepresentanteLegal = mostraListaRepresentanteLegal;
	}

	public Telefone getTelefoneBck() {
		return telefoneBck;
	}

	public void setTelefoneBck(Telefone telefoneBck) {
		this.telefoneBck = telefoneBck;
	}

}
