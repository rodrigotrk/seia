package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.GestorFinanceiro;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaContratoConvenio;
import br.gov.ba.seia.entity.PessoaFisicaContratoConvenioPK;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.TipoProjeto;
import br.gov.ba.seia.facade.ConsultaCerhServiceFacade;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ContratoConvenioServiceImpl;
import br.gov.ba.seia.service.GestorFinanceiroService;
import br.gov.ba.seia.service.PessoaFisicaContratoConvenioService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.TipoProjetoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@ViewScoped
@Named("contratoConvenioController")
public class ContratoConvenioController {

	@EJB
	private ConsultaCerhServiceFacade consultaCerhServiceFacade;

	@EJB
	private GestorFinanceiroService gestorFinanceiroService;

	@EJB
	private TipoProjetoService tipoProjetoService;

	@EJB
	private PessoaFacade pessoaFacade;

	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@EJB
	private ContratoConvenioServiceImpl contratoConvenioService;

	@EJB
	private PessoaFisicaContratoConvenioService pessoaFisicaContratoConvenioService;

	private LazyDataModel<ContratoConvenio> listConvenio;

	private String cnpj;
	private String numCpf;
	private String numCnpjFormatado;
	private String numCpfFormatado;

	private PessoaJuridica pessoaJuridica;
	private PessoaFisica pessoaFisica;
	private ContratoConvenio contratoConvenio;
	private PessoaFisicaContratoConvenio pessoaFisicaContratoConvenio;
	private List<PessoaFisicaContratoConvenio> listPessoaFisicaContratoConvenio = new ArrayList<PessoaFisicaContratoConvenio>();
	private List<TipoProjeto> listTipoProjeto;
	private List<GestorFinanceiro> listGestorFinanceiro;
	private ContratoConvenio contratoConvenioFiltro;

	private boolean convenioSalvo;
	private boolean desabilitar;
	private boolean adicionarUsuario;

	@PostConstruct
	private void inicializar(){

		try {
			listGestorFinanceiro = gestorFinanceiroService.listarGestorFinanceiro();
			listTipoProjeto = tipoProjetoService.listarTipoProjeto();
			pessoaJuridica = new PessoaJuridica();
			contratoConvenio = new ContratoConvenio();
			contratoConvenio.setIdeTipoProjeto(new TipoProjeto());
			contratoConvenio.setIdeGestorFinanceiro(new GestorFinanceiro());
			pessoaFisicaContratoConvenio = new PessoaFisicaContratoConvenio();
			convenioSalvo = false;
			desabilitar = true;
			adicionarUsuario = false;
			contratoConvenioFiltro = new ContratoConvenio();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void consultarConvenio(){
		try {

			if(!Util.isNullOuVazio(cnpj)){
				pessoaJuridica.setNumCnpj(cnpj);
				pessoaJuridica = pessoaFacade.buscarPessoaJuridicaByCNPJ(pessoaJuridica);
				contratoConvenioFiltro.setIdePessoaJuridica(pessoaJuridica);
				
			}else{
				contratoConvenioFiltro.setIdePessoaJuridica(new PessoaJuridica());
			}

			listConvenio = new LazyDataModel<ContratoConvenio>() {

				private static final long serialVersionUID = -6475445635129726741L;

				@Override
				public List<ContratoConvenio> load(int first, int pageSize, String arg2, SortOrder arg3, Map<String, String> arg4) {
					try{
						Collection<ContratoConvenio> contratos = consultaCerhServiceFacade.listarContratoConvenioPorFiltro(contratoConvenioFiltro, first, pageSize);
						List<ContratoConvenio> retorno = new ArrayList<ContratoConvenio>();
						
						retorno.addAll(contratos);

						return retorno;
					} catch(Exception e) {
						e.printStackTrace();
						Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
					return Collections.emptyList();
				}
			};
				
			listConvenio.setRowCount(consultaCerhServiceFacade.listarContratoConvenioPorFiltroCount(contratoConvenioFiltro));			

			if(Util.isNullOuVazio(listConvenio)){
				MensagemUtil.erro("Nenhum Convênio encontrado para esta empresa.");
			}else{
				if(!Util.isNullOuVazio(cnpj)){
					numCnpjFormatado = Util.formatarCNPJ(cnpj);
				}else {
					
					pessoaJuridica = new PessoaJuridica();
				}
				
			}
			
			Html.atualizar("formConvenio");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void consultarEmpresa(){
		try {

			if(	validarConsulta()){
				pessoaJuridica.setNumCnpj(cnpj);
				pessoaJuridica = pessoaFacade.buscarPessoaJuridicaByCNPJ(pessoaJuridica);
				if(!Util.isNullOuVazio(pessoaJuridica)){
					contratoConvenio.setIdePessoaJuridica(pessoaJuridica);
					desabilitar = false;
					Html.atualizar("formIncluirConvenio");
				}else{
					contratoConvenio.setIdePessoaJuridica(new PessoaJuridica());
					pessoaJuridica = new PessoaJuridica();
					desabilitar = true;
					Html.atualizar("formIncluirConvenio");
					MensagemUtil.erro("CNPJ não encontrado");
				}
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private boolean validarConsulta(){
		if(cnpj == null || cnpj.isEmpty()){
			MensagemUtil.msg0003("CNPJ");
			return false;
		}

		return true;
	}

	public void salvarConvenio(){
		try {
			contratoConvenio.setDtcCriacao(new Date());
			contratoConvenioService.salvarOuAtualizar(contratoConvenio);
			convenioSalvo = true;
			adicionarUsuario = true;
			Html.atualizar("formIncluirConvenio:panelUsarioxConvenio");
			MensagemUtil.sucesso("Convênio salvo com sucesso, vincule pessoas a este convênio.");

		} catch (Exception e) {
			MensagemUtil.sucesso(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void excluirConvenio(){
		try {
			contratoConvenio.setDtcExclusao(new Date());
			contratoConvenio.setIndExcluido(true);
			contratoConvenioService.atualizar(contratoConvenio);

			/*Deixe assim, não mecha no Equals do Contrato Convenio*/
			Iterator<ContratoConvenio> iterator = listConvenio.iterator();
			while(iterator.hasNext()){
				if(iterator.next().getNomContratoConvenio().equals(contratoConvenio.getNomContratoConvenio())){
					iterator.remove();
				}
			}

			Html.getCurrency()
				.update("formConvenio")
				.update("formConvenio");

			MensagemUtil.sucesso("Convênio excluído com sucesso");

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void editarContratoConvenio(){
		try {
			numCpf = "";
			pessoaJuridica = contratoConvenio.getIdePessoaJuridica();
			cnpj = contratoConvenio.getIdePessoaJuridica().getNumCnpj();
			listPessoaFisicaContratoConvenio = pessoaFisicaContratoConvenioService.listarPessoaFisicaContratoConvenioByConvenio(contratoConvenio);
			if(!Util.isNullOuVazio(listPessoaFisicaContratoConvenio)){
				for(PessoaFisicaContratoConvenio pfcc : listPessoaFisicaContratoConvenio){
					pfcc.getIdePessoaFisica().setNumCpf(Util.formatarCPF(pfcc.getIdePessoaFisica().getNumCpf()));
				}
			}
			convenioSalvo = true;
			desabilitar = false;
			adicionarUsuario = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void adicionarUsuario(){
		try {
			boolean usuarioExistente = false;

			if(!Util.isNullOuVazio(numCpf)){
				PessoaFisica pf = pessoaFisicaService.consultarPessoaFisicaByNumCpf(numCpf);

				if(!Util.isNullOuVazio(pf)){
					if(!Util.isNullOuVazio(listPessoaFisicaContratoConvenio)){
						for(PessoaFisicaContratoConvenio pfcc : listPessoaFisicaContratoConvenio){
							if(pfcc.getIdePessoaFisica().equals(pf)){
								usuarioExistente = true;
								break;
							}
						}
					}
					if(!usuarioExistente){
						pessoaFisicaContratoConvenio = new PessoaFisicaContratoConvenio();
						pessoaFisicaContratoConvenio.setIdePessoaFisica(pf);
						pessoaFisicaContratoConvenio.setIdeContratoConvenio(contratoConvenio);
						pessoaFisicaContratoConvenio.setDtcCriacao(new Date());
						pessoaFisicaContratoConvenio.setIndAtivo(true);
						pessoaFisicaContratoConvenio.setPessoaFisicaContratoConvenioPK(new PessoaFisicaContratoConvenioPK(pf.getIdePessoaFisica(), contratoConvenio.getIdeContratoConvenio()));
						pessoaFisicaContratoConvenioService.salvarPessoaFisicaContratoConvenio(pessoaFisicaContratoConvenio);

						pessoaFisicaContratoConvenio.getIdePessoaFisica().setNumCpf(Util.formatarCPF(numCpf));
						listPessoaFisicaContratoConvenio.add(pessoaFisicaContratoConvenio);
						numCpf = "";
						Html.atualizar("formIncluirConvenio");
						MensagemUtil.sucesso("Usuário vinculado ao convênio.");
					}else{
						MensagemUtil.alerta("Usuário já vinculado a este convênio");
					}
				}else{
					MensagemUtil.alerta("CPF não encontrado. É necessário cadastrar a pessoa física antes de associá-la a um convênio.");
				}
			}else{
				MensagemUtil.alerta("CPF é de preenchimento obrigatório");
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void desvincularUsuarioConvenio(){
		try {
			pessoaFisicaContratoConvenio.setIndAtivo(false);
			pessoaFisicaContratoConvenio.setDtcExclusao(new Date());
			pessoaFisicaContratoConvenio.setPessoaFisicaContratoConvenioPK(new PessoaFisicaContratoConvenioPK(pessoaFisicaContratoConvenio.getIdePessoaFisica().getIdePessoaFisica(), pessoaFisicaContratoConvenio.getIdeContratoConvenio().getIdeContratoConvenio()));
			pessoaFisicaContratoConvenioService.excluirVinculo(pessoaFisicaContratoConvenio);
			listPessoaFisicaContratoConvenio.remove(pessoaFisicaContratoConvenio);
			Html.atualizar("formIncluirConvenio");
			MensagemUtil.sucesso("Usuário desvinculado");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void incluirConvenio(){
		cnpj = "";
		pessoaJuridica = new PessoaJuridica();
		contratoConvenio = new ContratoConvenio();
		contratoConvenio.setIdeTipoProjeto(new TipoProjeto());
		contratoConvenio.setIdeGestorFinanceiro(new GestorFinanceiro());
		pessoaFisicaContratoConvenio = new PessoaFisicaContratoConvenio();
		listPessoaFisicaContratoConvenio = new ArrayList<PessoaFisicaContratoConvenio>();
		convenioSalvo = false;
		adicionarUsuario = false;
		desabilitar = true;
		if(!Util.isNullOuVazio(cnpj)){
			consultarEmpresa();
		}
		Html.atualizar("formIncluirConvenio");
	}

	public void visualizarConvenio(){
		try{
			pessoaJuridica = contratoConvenio.getIdePessoaJuridica();
			cnpj = contratoConvenio.getIdePessoaJuridica().getNumCnpj();
			listPessoaFisicaContratoConvenio = pessoaFisicaContratoConvenioService.listarPessoaFisicaContratoConvenioByConvenio(contratoConvenio);
			convenioSalvo = true;
			desabilitar = true;
			adicionarUsuario = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void limpar(){
		cnpj = "";
		pessoaJuridica = new PessoaJuridica();
		listConvenio = null;
		contratoConvenio = new ContratoConvenio();
		contratoConvenioFiltro = new ContratoConvenio();
		Html.atualizar("formConvenio");
	}

	public LazyDataModel<ContratoConvenio> getListConvenio() {
		return listConvenio;
	}

	public void setListConvenio(LazyDataModel<ContratoConvenio> listConvenio) {
		this.listConvenio = listConvenio;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public ContratoConvenio getContratoConvenio() {
		return contratoConvenio;
	}

	public void setContratoConvenio(ContratoConvenio contratoConvenio) {
		this.contratoConvenio = contratoConvenio;
	}

	public List<TipoProjeto> getListTipoProjeto() {
		return listTipoProjeto;
	}

	public void setListTipoProjeto(List<TipoProjeto> listTipoProjeto) {
		this.listTipoProjeto = listTipoProjeto;
	}

	public List<GestorFinanceiro> getListGestorFinanceiro() {
		return listGestorFinanceiro;
	}

	public void setListGestorFinanceiro(List<GestorFinanceiro> listGestorFinanceiro) {
		this.listGestorFinanceiro = listGestorFinanceiro;
	}

	public boolean isConvenioSalvo() {
		return convenioSalvo;
	}

	public void setConvenioSalvo(boolean convenioSalvo) {
		this.convenioSalvo = convenioSalvo;
	}

	public boolean isDesabilitar() {
		return desabilitar;
	}

	public void setDesabilidar(boolean desabilitar) {
		this.desabilitar = desabilitar;
	}

	public PessoaFisicaContratoConvenio getPessoaFisicaContratoConvenio() {
		return pessoaFisicaContratoConvenio;
	}

	public void setPessoaFisicaContratoConvenio(PessoaFisicaContratoConvenio pessoaFisicaContratoConvenio) {
		this.pessoaFisicaContratoConvenio = pessoaFisicaContratoConvenio;
	}

	public List<PessoaFisicaContratoConvenio> getListPessoaFisicaContratoConvenio() {
		return listPessoaFisicaContratoConvenio;
	}

	public void setListPessoaFisicaContratoConvenio(
			List<PessoaFisicaContratoConvenio> listPessoaFisicaContratoConvenio) {
		this.listPessoaFisicaContratoConvenio = listPessoaFisicaContratoConvenio;
	}

	public String getNumCpf() {
		return numCpf;
	}

	public void setNumCpf(String numCpf) {
		this.numCpf = numCpf;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public String getNumCnpjFormatado() {
		return numCnpjFormatado;
	}

	public void setNumCnpjFormatado(String numCnpjFormatado) {
		this.numCnpjFormatado = numCnpjFormatado;
	}

	public String getNumCpfFormatado() {
		return numCpfFormatado;
	}

	public void setNumCpfFormatado(String numCpfFormatado) {
		this.numCpfFormatado = numCpfFormatado;
	}

	public boolean isAdicionarUsuario() {
		return adicionarUsuario;
	}

	public void setAdicionarUsuario(boolean adicionarUsuario) {
		this.adicionarUsuario = adicionarUsuario;
	}

	public ContratoConvenio getContratoConvenioFiltro() {
		return contratoConvenioFiltro;
	}

	public void setContratoConvenioFiltro(ContratoConvenio contratoConvenioFiltro) {
		this.contratoConvenioFiltro = contratoConvenioFiltro;
	}

}
