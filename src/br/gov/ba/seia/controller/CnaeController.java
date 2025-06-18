
package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.FceCaptacaoSubterraneaCnae;
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceCaptacaoSuperficialCnae;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CnaeService;
import br.gov.ba.seia.service.FceCaptacaoCnaeService;
import br.gov.ba.seia.service.FceCaptacaoSubterraneaCnaeService;
import br.gov.ba.seia.service.PessoaJuridicaCnaeService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("cnaeController")
@ViewScoped
public class CnaeController {

	@EJB
	private CnaeService cnaeService;
	@EJB
	private PessoaJuridicaCnaeService pessoaJuridicaCnaeService;
	
	@EJB
	private FceCaptacaoCnaeService fceCaptacaoSuperficialCnaeService;
	
	@EJB
	private FceCaptacaoSubterraneaCnaeService fceCaptacaoSubterraneaCnaeService;
	
	private PessoaJuridicaCnae pessoaJuridicaCnae;
	private FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnae;
	private FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnae;
	private Boolean flagTableCnae;
	private Cnae cnaeSecao;
	private List<SelectItem> listaSecao;
	private Cnae cnaeDivisao = new Cnae();
	private List<SelectItem> listaDivisao;
	private Cnae cnaeGrupo = new Cnae();
	private List<SelectItem> listaGrupo;
	private Cnae cnaeClasse = new Cnae();
	private Collection<Cnae> listaClasse;
	private Cnae cnaeSubclasse = new Cnae();
	private Collection<Cnae> listaSubclasse;
	private DataModel<PessoaJuridicaCnae> cnaeData;
	private DataModel<FceCaptacaoSuperficialCnae> fceCaptacaoSuperficialCnaeData;
	private DataModel<FceCaptacaoSubterraneaCnae> fceCaptacaoSubterraneaCnaeData;
	private Boolean editMode;
	private Boolean enableFormCnae;
	private FceCaptacaoSuperficial ideFceCaptacaoSuperficial;
	private FceCaptacaoSuperficialCnae fceSuperficialSemClone;
	private FceCaptacaoSubterranea ideFceCaptacaoSubterranea;
	private FceCaptacaoSubterraneaCnae fceSubterraneaSemClone;

	
	private PessoaJuridica pessoaJuridica;
	
	private FceCaptacaoSuperficial fceCaptacaoSuperficial;
	private FceCaptacaoSubterranea fceCaptacaoSubterranea;
	
	private Boolean cnaeSuperficial;
	
	private Boolean cnaeSubterranea;
	
	private Boolean emptyList;
		
	List<FceCaptacaoSuperficialCnae> fceSuperficialList;
	List<FceCaptacaoSubterraneaCnae> fceSubterraneaList;
	
	private FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnaeSelecionado;
	private FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnaeSelecionado;

	@PostConstruct
	public void init()  {
		cnaeSecao = new Cnae();
		cnaeDivisao = new Cnae();
		cnaeGrupo = new Cnae();
		cnaeClasse = new Cnae();
		cnaeSubclasse = new Cnae();
		pessoaJuridicaCnae = new PessoaJuridicaCnae();
		fceCaptacaoSuperficialCnae = new FceCaptacaoSuperficialCnae();
		fceCaptacaoSuperficialCnae.setIdeCnae(new Cnae());
		fceCaptacaoSuperficialCnae.getIdeCnae().setIdeCnaePai(new Cnae());
		fceSuperficialList = new ArrayList<FceCaptacaoSuperficialCnae>();
		fceSubterraneaList = new ArrayList<FceCaptacaoSubterraneaCnae>();
		fceCaptacaoSubterraneaCnae = new FceCaptacaoSubterraneaCnae();
		fceCaptacaoSubterraneaCnae.setIdeCnae(new Cnae());
		fceCaptacaoSubterraneaCnae.getIdeCnae().setIdeCnaePai(new Cnae());
		fceCaptacaoSubterraneaCnaeData = new ListDataModel<FceCaptacaoSubterraneaCnae>();
		pessoaJuridicaCnae.setIdeCnae(new Cnae());
		pessoaJuridicaCnae.getIdeCnae().setIdeCnaePai(new Cnae());
		
		//fceSuperficial = new ArrayList<FceCaptacaoSuperficialCnae>();
		fceSubterraneaList = new ArrayList<FceCaptacaoSubterraneaCnae>();
		carregarListaSecao();
		this.flagTableCnae = false;
		setPessoaJuridica(new PessoaJuridica());
		setFceCaptacaoSuperficial(new FceCaptacaoSuperficial());
		setIdeFceCaptacaoSubterranea(new FceCaptacaoSubterranea());
		editMode = Boolean.FALSE;
		enableFormCnae = Boolean.TRUE;
		tratarPessoaJuridicaSessao();
		carregarListaPessoaJuridicaCnae();
		this.cnaeSuperficial = Boolean.FALSE;
		
		this.cnaeSubterranea = Boolean.FALSE;
		
	}
	
	public void salvarCnaePessoaJuridica(){
		try {
			
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public void salvarCnae(){
		try {
			if (validate()) {
				// Verificar qual o tipo de cnae (Superficial, Subterranea ou PessoaJuridica)
				if(cnaeSuperficial){
					if(!Util.isNull(this.fceCaptacaoSuperficial)){
						this.fceCaptacaoSuperficialCnae.setIdeFceCaptacaoSupercial(this.fceCaptacaoSuperficial);
					}

					//Passar os dados da DataModel da grid para o ArrayList 
					preencherFceCaptacao();

					//Realizar algumas validações do cane
					if(validarCnaeCaptacao(fceSuperficialList)){
						//pegar as informações do Cnae (descrição, código...)
						this.fceCaptacaoSuperficialCnae.setIdeCnae(cnaeService.carregarCnae(getCnae().getIdeCnae()));
						if(!editMode){
							fceSuperficialList.add(fceCaptacaoSuperficialCnae);
							this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(fceSuperficialList);
							sucessoInclusaoCnae();
						}
						else {
							Integer index = fceSuperficialList.indexOf(fceSuperficialSemClone);
							fceSuperficialList.set(index,fceCaptacaoSuperficialCnae);
							this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(fceSuperficialList);
							sucessoEdicaoCnae();
						}
						Html.esconder("dialogCnae");
					}	 
				}else if(cnaeSubterranea){
					
					if(!Util.isNull(this.fceCaptacaoSubterranea)){
						this.fceCaptacaoSubterraneaCnae.setIdeFceCaptacaoSubterranea(this.fceCaptacaoSubterranea);
					}
					
					//Passar os dados da DataModel da grid para o ArrayList 
					preencherFceCaptacaoSubterranea();

					//Realizar algumas validações do cane
					if(validarCnaeCaptacaoSubterranea(fceSubterraneaList)){
						//pegar as informações do Cnae (descrição, código...)
						this.fceCaptacaoSubterraneaCnae.setIdeCnae(cnaeService.carregarCnae(getCnae().getIdeCnae()));
						if(!editMode){
							fceSubterraneaList.add(fceCaptacaoSubterraneaCnae);
							this.fceCaptacaoSubterraneaCnaeData = new ListDataModel<FceCaptacaoSubterraneaCnae>(fceSubterraneaList);
							sucessoInclusaoCnae();
						}
						else {
							Integer index = fceSubterraneaList.indexOf(fceSubterraneaSemClone);
							fceSubterraneaList.set(index,fceCaptacaoSubterraneaCnae);
							this.fceCaptacaoSubterraneaCnaeData = new ListDataModel<FceCaptacaoSubterraneaCnae>(fceSubterraneaList);
							sucessoEdicaoCnae();
						}
						Html.esconder("dialogSubterraneaCnae");
					}	
				}else{
					pessoaJuridicaCnae.setIdePessoaJuridica(this.pessoaJuridica);
					if (editMode) {
						editarCnae();
					} else {
						salvarNovoCnae();
	
					}
					carregarListaPessoaJuridicaCnae();
				}
					
				this.flagTableCnae = Boolean.TRUE;
			}	
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private Boolean validarCnaeCaptacao(List<FceCaptacaoSuperficialCnae> fceSuperficial){
		
		if(!Util.isNullOuVazio(fceSuperficial)){
		
			if(fceSuperficial.size() > 0){
				//Verificar se já existe um Cnae salvo setado como principal
				if(fceCaptacaoSuperficialCnae.isIndCnaePrincipal()){
					for (FceCaptacaoSuperficialCnae fceSuperficialCnae : fceSuperficial) {
						if (!fceSuperficialCnae.equals(fceSuperficialSemClone) && fceSuperficialCnae.isIndCnaePrincipal()){
							JsfUtil.addErrorMessage("Já existe um Cnae como atividade Principal.");
							return false;
						} 
					}
				}
				//Verificação se já exite o mesmo Cnae inserido na lista
				for (FceCaptacaoSuperficialCnae fceSuperficialCnae : fceSuperficial) {
					if(!fceSuperficialCnae.equals(fceSuperficialSemClone) && fceSuperficialCnae.getIdeCnae().equals(getCnae())){
						JsfUtil.addErrorMessage("Esse Cnae já foi inserido.");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private Boolean validarCnaeCaptacaoSubterranea(List<FceCaptacaoSubterraneaCnae> fceSubterranea){
		
		if(!fceSubterranea.isEmpty()){
			//Verificar se já existe um Cnae salvo setado como principal
			if(fceCaptacaoSubterraneaCnae.isIndCnaePrincipal()){
				for (FceCaptacaoSubterraneaCnae fceSubterraneaCnae : fceSubterranea) {
					if ((!fceSubterraneaCnae.equals(fceCaptacaoSubterraneaCnae)) && fceSubterraneaCnae.isIndCnaePrincipal()){
						if((fceSubterraneaSemClone != null && fceSubterraneaSemClone.getIdeFceCaptacaoSubterraneaCnae() != null) || (!fceSubterraneaCnae.equals(fceSubterraneaSemClone))){
							JsfUtil.addErrorMessage("Já existe um Cnae como atividade Principal.");
							return false;
						}
					} 
				}
			}
			//Verificação se já exite o mesmo Cnae inserido na lista
			for (FceCaptacaoSubterraneaCnae fceSubterraneaCnae : fceSubterranea) {
				if(!fceSubterraneaCnae.equals(fceSubterraneaCnae) && fceSubterraneaCnae.getIdeCnae().equals(getCnae())){
					JsfUtil.addErrorMessage("Esse Cnae já foi inserido.");
					return false;
				}
			}
		}
		return true;
	}
	
	private void editarCnae() {
		try {
			if (!pessoaJuridicaCnaeService.salvarOuAtualizarPessoaJuridicaCnae(pessoaJuridicaCnae)) {
				JsfUtil.addErrorMessage("É necessário informar apenas uma atividade CNAE como principal.");
			} else {
				sucessoEdicaoCnae();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarNovoCnae() {
		try {
			if (!pessoaJuridicaCnaeService.salvarPessoaJuridicaCnae(pessoaJuridicaCnae)) {
				JsfUtil.addErrorMessage("É necessário informar apenas uma atividade CNAE como principal.");
			} else {
				sucessoInclusaoCnae();
			}
		} catch (Exception exception) {
			if(exception.getCause().getMessage().contains("unicidade"))
				JsfUtil.addErrorMessage("Já existe um registro para esse CNAE cadastrado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
		
	private void sucessoInclusaoCnae() {
		JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
		limparObjetos();
	}
	
	private void sucessoEdicaoCnae() {
		JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
	}
	
	public void editar(PessoaJuridicaCnae pessoaJuridicaCnae){
		try {
			this.pessoaJuridicaCnae = pessoaJuridicaCnae.clone();
			//recupera Subclasse
			
			this.cnaeSubclasse = pessoaJuridicaCnae.getIdeCnae();
			//recupera Classe
			this.cnaeClasse = this.cnaeSubclasse.getIdeCnaePai();
			//recupera Grupo
			this.cnaeGrupo = this.cnaeClasse.getIdeCnaePai();
			//recupera Divisao
			this.cnaeDivisao = this.cnaeGrupo.getIdeCnaePai();
			//recupera Sessão
			this.cnaeSecao = this.cnaeDivisao.getIdeCnaePai();
			if(Util.isNullOuVazio(cnaeSecao)){//se não foi escolhida subclasse.
				cnaeSecao = cnaeDivisao;
				cnaeDivisao = cnaeGrupo;
				cnaeGrupo = cnaeClasse;
				cnaeClasse = cnaeSubclasse;
			}
			listarDivisoes();
			listarGrupos();
			listarClasses();
			listarSubclasse();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		RequestContext.getCurrentInstance().addPartialUpdateTarget(":tabviewpj:form-pagina-cnae:dialogCnae");
		RequestContext.getCurrentInstance().execute("dialogCnae.show()");
	}
	
	public void excluir(){
		try {
			pessoaJuridicaCnaeService.excluirPessoaJuridicaCnae(pessoaJuridicaCnae);
			carregarListaPessoaJuridicaCnae();
			RequestContext.getCurrentInstance().addPartialUpdateTarget(":tabviewpj:form-pagina-cnae:tableCnae");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirItemLista(){
		preencherFceCaptacao();
		fceSuperficialList.remove(fceCaptacaoSuperficialCnaeSelecionado);
		this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(fceSuperficialList);
		if(fceSuperficialList.isEmpty()){
			this.flagTableCnae = Boolean.FALSE;
		}
		try {
			fceCaptacaoSuperficialCnaeService.excluirFceCaptacaoSuperficialCnae(fceCaptacaoSuperficialCnaeSelecionado);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void excluirItemListaSubterranea(){
		preencherFceCaptacaoSubterranea();
		fceSubterraneaList.remove(fceCaptacaoSubterraneaCnaeSelecionado);
		this.fceCaptacaoSubterraneaCnaeData = new ListDataModel<FceCaptacaoSubterraneaCnae>(fceSubterraneaList);
		if(fceSubterraneaList.isEmpty()){
			this.flagTableCnae = Boolean.FALSE;
		}
		try {
			fceCaptacaoSubterraneaCnaeService.excluirFceCaptacaoSubterraneaCnae(fceCaptacaoSubterraneaCnaeSelecionado);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void editarItemLista(Object object){
		String dialog = "";
		try {
			if(object instanceof FceCaptacaoSuperficialCnae){
				this.fceCaptacaoSuperficialCnae = ((FceCaptacaoSuperficialCnae) object).clone();
				//Objeto criado sem o clone para fazer a validação se é o mesmo item para editar
				//validarCnaeCaptacao()
				this.fceSuperficialSemClone = ((FceCaptacaoSuperficialCnae) object) ;
				dialog = "dialogCnae";
				//recupera Subclasse
				this.cnaeSubclasse = fceCaptacaoSuperficialCnae.getIdeCnae();
				//recupera Classe
			}
			else if (object instanceof FceCaptacaoSubterraneaCnae){
				this.fceCaptacaoSubterraneaCnae = ((FceCaptacaoSubterraneaCnae) object).clone();
				//Objeto criado sem o clone para fazer a validação se é o mesmo item para editar
				//validarCnaeCaptacaoSubterranea()
				this.fceSubterraneaSemClone = ((FceCaptacaoSubterraneaCnae) object);
				dialog = "dialogSubterraneaCnae";
				//recupera Subclasse
				this.cnaeSubclasse = fceCaptacaoSubterraneaCnae.getIdeCnae();
				//recupera Classe
			}
			this.cnaeClasse = this.cnaeSubclasse.getIdeCnaePai();
			//recupera Grupo
			this.cnaeGrupo = this.cnaeClasse.getIdeCnaePai();
			//recupera Divisao
			this.cnaeDivisao = this.cnaeGrupo.getIdeCnaePai();
			//recupera Sessão
			this.cnaeSecao = this.cnaeDivisao.getIdeCnaePai();
			
			if(Util.isNullOuVazio(cnaeSecao)){//se não foi escolhida subclasse.
				cnaeSecao = cnaeDivisao;
				cnaeDivisao = cnaeGrupo;
				cnaeGrupo = cnaeClasse;
				cnaeClasse = cnaeSubclasse;
			}
			listarDivisoes();
			listarGrupos();
			listarClasses();
			listarSubclasse();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		Html.atualizar(dialog);
		Html.exibir(dialog);
	}
	
	public void carregarListaPessoaJuridicaCnae(){
		try {		
			if(!Util.isNullOuVazio(this.pessoaJuridica) && !Util.isNullOuVazio(this.pessoaJuridica.getIdePessoaJuridica())){
				// this.cnaeData = new ListDataModel<Cnae>((List<Cnae>)
				// cnaeService.listarCnaePorPessoaJuridica((this.pessoaJuridica)));
				this.cnaeData = new ListDataModel<PessoaJuridicaCnae>(
						(List<PessoaJuridicaCnae>) pessoaJuridicaCnaeService.buscaPessoaJuridicaCnaePorPessoaJuridica(this.pessoaJuridica));
			}			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarListaFceCaptacaoSuperficialCnae(){
		try {
			if(!Util.isNullOuVazio(this.fceCaptacaoSuperficial) && !Util.isNullOuVazio(this.fceCaptacaoSuperficial.getIdeFceCaptacaoSuperficial())){
				this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(
						(List<FceCaptacaoSuperficialCnae>) fceCaptacaoSuperficialCnaeService.buscaFceCaptacaoSuperficialCnaePorFceCaptacaoSupericial(this.fceCaptacaoSuperficial));
				if(fceCaptacaoSuperficialCnaeData.getRowCount() > 0){
					this.flagTableCnae = Boolean.TRUE;
				}
			}			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarListaSecao() {
		try {
			this.listaSecao = new ArrayList<SelectItem>();
			List<Cnae> listaCnae = (List<Cnae>) cnaeService.listarCnaeSecao();
			for (Cnae cnae : listaCnae) {
				
				this.listaSecao.add(new SelectItem(cnae.getIdeCnae() ,cnae.getCodCnae()+" - " +cnae.getDesCnae()));
			}
			
			this.limparDivisao();
			this.limparGrupo();
			this.limparClasse();
			this.limparSubclasse();
			
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void limparSubclasse() {
		this.cnaeSubclasse = new Cnae();
		this.listaSubclasse = new ArrayList<Cnae>();
	}

	private void limparClasse() {
		this.cnaeClasse = new Cnae();
		this.listaClasse = new ArrayList<Cnae>();
	}

	private void limparGrupo() {
		this.cnaeGrupo = new Cnae();
		this.listaGrupo = new ArrayList<SelectItem>();
	}

	private void limparDivisao() {
		this.cnaeDivisao = new Cnae();
		this.listaDivisao = new ArrayList<SelectItem>();
	}

	public void buscarDivisaoPorSecao() {
		try {
			
			this.limparDivisao();
			
			listarDivisoes();
			
			this.limparGrupo();
			this.limparClasse();
			this.limparSubclasse();

		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void listarDivisoes() {
			listaDivisao = new ArrayList<SelectItem>();
			
			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeSecao)) {
				this.listaDivisao.add(new SelectItem(cnae.getIdeCnae(), cnae.getIdeCnaePai().getCodCnae()+cnae.getCodCnae()+" - " +cnae.getDesCnae()));
			}
	}
			
	public void buscarGrupoPorDivisao() {
		try {
			
			this.limparGrupo();
			
			listarGrupos();
			
			this.limparClasse();
			this.limparSubclasse();
			
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void listarGrupos() {
			listaGrupo = new ArrayList<SelectItem>();
			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeDivisao)) {
				this.listaGrupo.add(new SelectItem(cnae.getIdeCnae(), cnae.getIdeCnaePai().getIdeCnaePai().getCodCnae() + cnae.getCodCnae() + " - "
						+ cnae.getDesCnae()));
			}
	}

	public void buscarClassePorGrupo() {		
		try {
			this.limparClasse();
			listarClasses();
			this.limparSubclasse();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void listarClasses()  {
		listaClasse = cnaeService.listarCnaePorPai(cnaeGrupo);
	}

	public void buscarSubclassePorClasse() {
		try {
			limparSubclasse();
			listarSubclasse();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void listarSubclasse()  {
		if(!Util.isNull(cnaeClasse)){
			this.listaSubclasse = cnaeService.listarCnaePorPai(cnaeClasse);
		}
	}
	
	public void limparObjetos() {
		pessoaJuridicaCnae = new PessoaJuridicaCnae();
		fceCaptacaoSuperficialCnae = new FceCaptacaoSuperficialCnae();
		fceCaptacaoSubterraneaCnae = new FceCaptacaoSubterraneaCnae();
		limparEdicao();
	}
	
	private void preencherFceCaptacao() {
		
		if(fceCaptacaoSuperficialCnaeData.getRowCount() > 0){
			fceSuperficialList = new ArrayList<FceCaptacaoSuperficialCnae>();
			for (int i = 0; i < this.fceCaptacaoSuperficialCnaeData.getRowCount(); i++) { 
				this.fceCaptacaoSuperficialCnaeData.setRowIndex(i); 
				fceSuperficialList.add( (FceCaptacaoSuperficialCnae) this.fceCaptacaoSuperficialCnaeData.getRowData()); 
			}
		}
	}
	
	private void preencherFceCaptacaoSubterranea() {
		if(fceCaptacaoSubterraneaCnaeData.getRowCount() > 0){
			fceSubterraneaList = new ArrayList<FceCaptacaoSubterraneaCnae>();
			for (int i = 0; i < fceCaptacaoSubterraneaCnaeData.getRowCount(); i++) { 
				this.fceCaptacaoSubterraneaCnaeData.setRowIndex(i); 
				fceSubterraneaList.add( (FceCaptacaoSubterraneaCnae) fceCaptacaoSubterraneaCnaeData.getRowData()); 
			}
		}
	}

	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new PessoaJuridica();
			} 
		}
	}
	
	private Boolean validate() throws CloneNotSupportedException {
		Boolean retorno = Boolean.TRUE;
		
		if (Util.isNull(cnaeSecao.getIdeCnae()) || cnaeSecao.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Seção é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		
		if (Util.isNull(cnaeDivisao.getIdeCnae()) || cnaeDivisao.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Divisão é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		
		if (Util.isNull(cnaeGrupo.getIdeCnae()) || cnaeGrupo.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Grupo é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		
		if (Util.isNull(cnaeClasse) || Util.isNull(cnaeClasse.getIdeCnae()) || cnaeClasse.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Classe é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		
		if (Util.isNull(pessoaJuridicaCnae.getIdeCnae()) || Util.isNull(pessoaJuridicaCnae.getIdeCnae().getIdeCnae())
				|| pessoaJuridicaCnae.getIdeCnae().getIdeCnae() == 0 && cnaeClasse.getIdeCnae() != 0) {
			pessoaJuridicaCnae.setIdeCnae(cnaeClasse.clone());
		}
		
		if (Util.isNull(fceCaptacaoSuperficialCnae.getIdeCnae()) || Util.isNull(fceCaptacaoSuperficialCnae.getIdeCnae().getIdeCnae())
				|| fceCaptacaoSuperficialCnae.getIdeCnae().getIdeCnae() == 0 && cnaeClasse.getIdeCnae() != 0) {
			fceCaptacaoSuperficialCnae.setIdeCnae(cnaeClasse.clone());	
		}
		
		if (Util.isNull(fceCaptacaoSubterraneaCnae.getIdeCnae()) || Util.isNull(fceCaptacaoSubterraneaCnae.getIdeCnae().getIdeCnae())
				|| fceCaptacaoSubterraneaCnae.getIdeCnae().getIdeCnae() == 0 && cnaeClasse.getIdeCnae() != 0) {
			fceCaptacaoSubterraneaCnae.setIdeCnae(cnaeClasse.clone());	
		}
		
		return retorno;
	}
	
	public void limparEdicao() {
		this.pessoaJuridicaCnae.setIdeCnae(new Cnae());
		this.fceCaptacaoSuperficialCnae.setIdeCnae(new Cnae());
		this.fceCaptacaoSubterraneaCnae.setIdeCnae(new Cnae());
		this.cnaeSecao = new Cnae();
		carregarListaSecao();
	}
	
	public PessoaJuridicaCnae getPessoaJuridicaCnae() {
		return pessoaJuridicaCnae;
	}

	public void setPessoaJuridicaCnae(PessoaJuridicaCnae pessoaJuridicaCnae) {
		this.pessoaJuridicaCnae = pessoaJuridicaCnae;
	}

	public Boolean getFlagTableCnae() {
		return flagTableCnae;
	}

	public void setFlagTableCnae(Boolean flagTableCnae) {
		this.flagTableCnae = flagTableCnae;
	}

	public List<SelectItem> getListaSecao() {
		return listaSecao;
	}

	public void setListaSecao(List<SelectItem> listaSecao) {
		this.listaSecao = listaSecao;
	}

	public List<SelectItem> getListaDivisao() {
		return listaDivisao;
	}

	public void setListaDivisao(List<SelectItem> listaDivisao) {
		this.listaDivisao = listaDivisao;
	}

	public List<SelectItem> getListaGrupo() {
		return listaGrupo;
	}

	public void setListaGrupo(List<SelectItem> listaGrupo) {
		this.listaGrupo = listaGrupo;
	}

	public Collection<Cnae> getListaClasse() {
		return listaClasse;
	}

	public void setListaClasse(Collection<Cnae> listaClasse) {
		this.listaClasse = listaClasse;
	}

	public Collection<Cnae> getListaSubclasse() {
		return listaSubclasse;
	}

	public void setListaSubclasse(Collection<Cnae> listaSubclasse) {
		this.listaSubclasse = listaSubclasse;
	}

	public DataModel<PessoaJuridicaCnae> getCnaeData() {
		return cnaeData;
	}

	public void setCnaeData(DataModel<PessoaJuridicaCnae> cnaeData) {		
		this.cnaeData = cnaeData;
	}

	public Cnae getCnaeSecao() {
		return cnaeSecao;
	}

	public void setCnaeSecao(Cnae cnaeSecao) {
		this.cnaeSecao = cnaeSecao;
	}

	public Cnae getCnaeDivisao() {
		return cnaeDivisao;
	}

	public void setCnaeDivisao(Cnae cnaeDivisao) {
		this.cnaeDivisao = cnaeDivisao;
	}

	public Cnae getCnaeGrupo() {
		return cnaeGrupo;
	}

	public void setCnaeGrupo(Cnae cnaeGrupo) {
		this.cnaeGrupo = cnaeGrupo;
	}

	public Cnae getCnaeClasse() {
		return cnaeClasse;
	}

	public void setCnaeClasse(Cnae cnaeClasse) {
		this.cnaeClasse = cnaeClasse;
	}

	public Cnae getCnaeSubclasse() {
		return cnaeSubclasse;
	}

	public void setCnaeSubclasse(Cnae cnaeSubclasse) {
		this.cnaeSubclasse = cnaeSubclasse;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		
		carregarListaPessoaJuridicaCnae();		
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getEnableFormCnae() {
		return enableFormCnae;
	}

	public void setEnableFormCnae(Boolean enableFormCnae) {
		this.enableFormCnae = enableFormCnae;
	}

	public FceCaptacaoSuperficialCnae getFceCaptacaoSuperficialCnae() {
		return fceCaptacaoSuperficialCnae;
	}

	public void setFceCaptacaoSuperficialCnae(
			FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnae) {
		this.fceCaptacaoSuperficialCnae = fceCaptacaoSuperficialCnae;
	}

	public DataModel<FceCaptacaoSuperficialCnae> getFceCaptacaoSuperficialCnaeData() {
		return fceCaptacaoSuperficialCnaeData;
	}

	public void setFceCaptacaoSuperficialCnaeData(
			DataModel<FceCaptacaoSuperficialCnae> fceCaptacaoSuperficialCnaeData) {
		this.fceCaptacaoSuperficialCnaeData = fceCaptacaoSuperficialCnaeData;
	}

	public FceCaptacaoSuperficial getFceCaptacaoSuperficial() {
		return fceCaptacaoSuperficial;
	}

	public void setFceCaptacaoSuperficial(
			FceCaptacaoSuperficial fceCaptacaoSuperficial) {
		this.fceCaptacaoSuperficial = fceCaptacaoSuperficial;
	}

	public Boolean getCnaeSuperficial() {
		return cnaeSuperficial;
	}

	public void setCnaeSuperficial(Boolean cnaeSuperficial) {
		this.cnaeSuperficial = cnaeSuperficial;
	}

	public Boolean getCnaeSubterranea() {
		return cnaeSubterranea;
	}

	public void setCnaeSubterranea(Boolean cnaeSubterranea) {
		this.cnaeSubterranea = cnaeSubterranea;
	}

	public Boolean getEmptyList() {
		if (Util.isNull(cnaeData) || cnaeData.getRowCount() == 0) {
			emptyList =  Boolean.FALSE;
		} else {
			emptyList =  Boolean.TRUE;
		}
		return emptyList;
	}

	public void setEmptyList(Boolean emptyList) {
		this.emptyList = emptyList;
	}
	
	public List<FceCaptacaoSuperficialCnae> getFceSuperficialList() {
		return fceSuperficialList;
	}

	public void setFceSuperficialList(List<FceCaptacaoSuperficialCnae> fceSuperficial) {
		this.fceSuperficialList = fceSuperficial;
	}

	public FceCaptacaoSuperficial getIdeFceCaptacaoSuperficial() {
		return ideFceCaptacaoSuperficial;
	}

	public void setIdeFceCaptacaoSuperficial(
			FceCaptacaoSuperficial ideFceCaptacaoSuperficial) {
		this.ideFceCaptacaoSuperficial = ideFceCaptacaoSuperficial;
	}

	public FceCaptacaoSubterraneaCnae getFceCaptacaoSubterraneaCnae() {
		return fceCaptacaoSubterraneaCnae;
	}

	public void setFceCaptacaoSubterraneaCnae(
			FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnae) {
		this.fceCaptacaoSubterraneaCnae = fceCaptacaoSubterraneaCnae;
	}

	public DataModel<FceCaptacaoSubterraneaCnae> getFceCaptacaoSubterraneaCnaeData() {
		return fceCaptacaoSubterraneaCnaeData;
	}

	public void setFceCaptacaoSubterraneaCnaeData(
			DataModel<FceCaptacaoSubterraneaCnae> fceCaptacaoSubterraneaCnaeData) {
		this.fceCaptacaoSubterraneaCnaeData = fceCaptacaoSubterraneaCnaeData;
	}

	public FceCaptacaoSubterranea getIdeFceCaptacaoSubterranea() {
		return ideFceCaptacaoSubterranea;
	}

	public void setIdeFceCaptacaoSubterranea(
			FceCaptacaoSubterranea ideFceCaptacaoSubterranea) {
		this.ideFceCaptacaoSubterranea = ideFceCaptacaoSubterranea;
	}

	public List<FceCaptacaoSubterraneaCnae> getFceSubterraneaList() {
		return fceSubterraneaList;
	}

	public void setFceSubterraneaList(List<FceCaptacaoSubterraneaCnae> fceSubterranea) {
		this.fceSubterraneaList = fceSubterranea;
	}

	public FceCaptacaoSubterranea getFceCaptacaoSubterranea() {
		return fceCaptacaoSubterranea;
	}

	public void setFceCaptacaoSubterranea(
			FceCaptacaoSubterranea fceCaptacaoSubterranea) {
		this.fceCaptacaoSubterranea = fceCaptacaoSubterranea;
	}

	public FceCaptacaoSuperficialCnae getFceCaptacaoSuperficialCnaeSelecionado() {
		return fceCaptacaoSuperficialCnaeSelecionado;
	}

	public void setFceCaptacaoSuperficialCnaeSelecionado(
			FceCaptacaoSuperficialCnae fceCaptacaoSuperficialCnaeSelecionado) {
		this.fceCaptacaoSuperficialCnaeSelecionado = fceCaptacaoSuperficialCnaeSelecionado;
	}

	public FceCaptacaoSubterraneaCnae getFceCaptacaoSubterraneaCnaeSelecionado() {
		return fceCaptacaoSubterraneaCnaeSelecionado;
	}

	public void setFceCaptacaoSubterraneaCnaeSelecionado(
			FceCaptacaoSubterraneaCnae fceCaptacaoSubterraneaCnaeSelecionado) {
		this.fceCaptacaoSubterraneaCnaeSelecionado = fceCaptacaoSubterraneaCnaeSelecionado;
	}

	private Cnae getCnae(){
		if(Util.isNull(cnaeSubclasse)){
			return cnaeClasse;
		} 
		else {
			if(cnaeSuperficial){
				return cnaeSubclasse = fceCaptacaoSuperficialCnae.getIdeCnae();
			}else{
				return cnaeSubclasse = fceCaptacaoSubterraneaCnae.getIdeCnae();
			}
		}
	}
}
