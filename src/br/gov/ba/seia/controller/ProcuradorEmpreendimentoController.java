package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPfEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;


@Named("procuradorEmpreendimentoController")
@ViewScoped
public class ProcuradorEmpreendimentoController extends SeiaControllerAb {
	
	private String caminhoArquivoProc;
	private String caminhoArquivoProcPj;
	private List<String> listaArquivoProc;
	private List<String> listaArquivoProcPj;
	private Boolean temArquivoProc;
	private Boolean temArquivoProcPj;
	private StreamedContent arquivoBaixarProc;
	private StreamedContent arquivoBaixarProcPj;
	private ProcuradorPessoaFisica procuradorPessoaFisica;
	private ProcuradorRepresentante procuradorRepresentante;
	private ProcuradorPfEmpreendimento procPfEmpreendimento;
	private ProcuradorPfEmpreendimento procPfEmpreendimentoSelecionado;
	private ProcuradorRepEmpreendimento procRepEmpreendimento;
	private ProcuradorRepEmpreendimento procRepEmpreendimentoSelecionado;
	private List<ProcuradorRepresentante> procRepresentantes;
	private List<ProcuradorPessoaFisica> procPessoasFisicas;
	private DataModel<ProcuradorPfEmpreendimento> modelProcPf;
	private DataModel<ProcuradorRepEmpreendimento> modelProcRep;
	private Pessoa requerente;
	private Boolean isProcuradorPf;
	private Empreendimento empreendimento;
	private Boolean mostrarTabelaProcuradoresPf;
	private Boolean mostrarTabelaProcuradoresPj;
	private Boolean disableFormProcEmpPf;
	private Boolean habilitaLista;
	private boolean isVisualizar;
	private boolean bloquarEdicao;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private ProcuradorRepresentanteService procRepService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@EJB
	private PessoaJuridicaService   pessoaJuridicaService;
	
	
	@EJB
	private ProcuradorPfEmpreendimentoService procuradorPfEmpreendimentoService;
	
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	
	@PostConstruct
	public void init() {
		habilitaLista = false;
		verificarTipoProcurador();
		carregarEmpreendimentoSessao();
		carregarListaProcuradores();
		carregarModelProcPf();
		carregarModelProcPj();
		listaArquivoProc = new ArrayList<String>();
		listaArquivoProcPj = new ArrayList<String>();
		procPfEmpreendimento = new ProcuradorPfEmpreendimento();
		procRepEmpreendimento = new ProcuradorRepEmpreendimento();
		disableFormProcEmpPf = Boolean.TRUE;
		temArquivoProc = Boolean.FALSE;
		temArquivoProcPj = Boolean.FALSE;
		bloquarEdicao = false;
		isVisualizar = false;
				
	}
	
	
	private void carregarEmpreendimentoSessao(){
		if(Util.isNullOuVazio(empreendimento)){
			this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
		}
	}
	
	
	public void trataArquivoProc(FileUploadEvent event) {
		caminhoArquivoProc = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.EMPREENDIMENTO.toString());
		temArquivoProc = true;
		listaArquivoProc.clear();
		listaArquivoProc.add(FileUploadUtil.getFileName(caminhoArquivoProc));
		procPfEmpreendimento.setDscCaminhoProcuracao(caminhoArquivoProc);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}
	
	public void trataArquivoProcPj(FileUploadEvent event) {
		caminhoArquivoProcPj = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.EMPREENDIMENTO.toString());
		temArquivoProcPj = true;
		listaArquivoProcPj.clear();
		listaArquivoProcPj.add(FileUploadUtil.getFileName(caminhoArquivoProcPj));
		procRepEmpreendimento.setDscCaminhoProcuracao(caminhoArquivoProcPj);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}


	private void carregarListaProcuradores() {
		if (isProcuradorPf) {
			carregarListaProcPessoasFisicas();
		} else {
			carregarListaProcRepresentantes();
		}
	}
	
	private void carregarListaProcRepresentantes() {
		try {
			procRepresentantes = new ArrayList<ProcuradorRepresentante>();
			if (!Util.isNull(requerente)) {
				ProcuradorRepresentante procuradorRepresentante = new ProcuradorRepresentante();
				procuradorRepresentante.setIdePessoaJuridica(new PessoaJuridica(requerente.getIdePessoa()));
				procRepresentantes = (List<ProcuradorRepresentante>) procRepService.getListaProcuradorRepresentante(procuradorRepresentante);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	private void carregarListaProcPessoasFisicas() {
		try {
			procPessoasFisicas = new ArrayList<ProcuradorPessoaFisica>();
			if (!Util.isNull(requerente)) {
				ProcuradorPessoaFisica procuradorPessoaFisica = new ProcuradorPessoaFisica();
				procuradorPessoaFisica.setIdePessoaFisica(new PessoaFisica(requerente.getIdePessoa()));
				procPessoasFisicas = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(procuradorPessoaFisica);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboProcuradores() {
		if (!Util.isNull(requerente) && !Util.isNull(requerente.getPessoaFisica())) {
			return getValuesComboProcuradoresPf();
		} else {
			return getValuesComboProcuradoresRep();
		}
	}
	
	public Collection<SelectItem> getValuesComboProcuradoresPf() {
		compararListaComboPf();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
        Iterator<ProcuradorPessoaFisica> i = procPessoasFisicas.iterator();
        while (i.hasNext()) {
        	ProcuradorPessoaFisica procuradorPessoaFisica = i.next();
        	toReturn.add(new SelectItem(procuradorPessoaFisica, procuradorPessoaFisica.getIdeProcurador().getNomPessoa()));
        }
        return toReturn;
	}
	
	public Collection<SelectItem> getValuesComboProcuradoresRep() {
		compararListaComboRep();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
        Iterator<ProcuradorRepresentante> i = procRepresentantes.iterator();
        while (i.hasNext()) {
        	ProcuradorRepresentante procuradorRepresentante = i.next();
        	toReturn.add(new SelectItem(procuradorRepresentante, procuradorRepresentante.getIdeProcurador().getNomPessoa()));
        }
        return toReturn;
	}
	
	private void verificarTipoProcurador() {
		
		requerente = (Pessoa) getAtributoSession("REQUERENTE");

		if (!Util.isNull(requerente) && !Util.isNull(requerente.getPessoaFisica())) {
			isProcuradorPf = Boolean.TRUE;
		} else {
			isProcuradorPf = Boolean.FALSE;
		}
	}
	
	public void salvarProcPf() {
		if(!temArquivoProc) {
			JsfUtil.addErrorMessage(BUNDLE.getString("documentoMsgErroUpload"));
		} else {
			try {
				procPfEmpreendimento.setIdeEmpreendimento(empreendimento);
				procPfEmpreendimento.setIdeProcuradorPessoaFisica(procuradorPessoaFisica);
				procPfEmpreendimento.setIndExcluido(Boolean.FALSE);
				procPfEmpreendimento.setDtcCriacao(new Date());
				procuradorPfEmpreendimentoService.salvarOuAtualizar(procPfEmpreendimento);
				carregarModelProcPf();
				compararListaComboPf();
				limparObjetosPf();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void editarProcuracaoPF(ActionEvent evt) {
		carregarProcuracaoPF(evt);
		bloquarEdicao = false;
		
	}

	public void editarProcuracaoPJ(ActionEvent evt) {
		carregarProcuracaoPJ(evt);
		bloquarEdicao = false;
		
	}
	
	public void visualizarProcuracaoPF(ActionEvent evt) {
		carregarProcuracaoPF(evt);
		bloquarEdicao = true;
		
	}
	
	public void visualizarProcuracaoPJ(ActionEvent evt) {
		carregarProcuracaoPJ(evt);
		bloquarEdicao = true;
		
	}
	
	private void carregarProcuracaoPJ(ActionEvent evt) {
		procRepEmpreendimento = (ProcuradorRepEmpreendimento) evt.getComponent().getAttributes().get("procRepEmp");
		procuradorRepresentante = procRepEmpreendimento.getIdeProcuradorRepresentante();
		listaArquivoProcPj = new ArrayList<String>();
		listaArquivoProcPj.add(procRepEmpreendimento.getDscCaminhoProcuracao());
		procRepresentantes.add(procuradorRepresentante);
		temArquivoProcPj = true;
		isVisualizar = true;
	}
	
	private void carregarProcuracaoPF(ActionEvent evt) {
		procPfEmpreendimento = (ProcuradorPfEmpreendimento) evt.getComponent().getAttributes().get("procPfEmp");
		procuradorPessoaFisica = procPfEmpreendimento.getIdeProcuradorPessoaFisica();
		listaArquivoProc = new ArrayList<String>();
		listaArquivoProc.add(procPfEmpreendimento.getDscCaminhoProcuracao());
		procPessoasFisicas.add(procuradorPessoaFisica);
		temArquivoProc = true;
		isVisualizar = true;
	}
	
	public void salvarProcRep() {
		if(!temArquivoProcPj) {
			JsfUtil.addErrorMessage(BUNDLE.getString("documentoMsgErroUpload"));
		} else {
			try {
				procRepEmpreendimento.setIdeEmpreendimento(empreendimento);
				procRepEmpreendimento.setIdeProcuradorRepresentante(procuradorRepresentante);
				procRepEmpreendimento.setIndExcluido(Boolean.FALSE);
				procRepEmpreendimento.setDtcCriacao(new Date());
				procuradorRepEmpreendimentoService.salvarOuAtualizar(procRepEmpreendimento);
				carregarModelProcPj();
				compararListaComboRep();
				limparObjetosRep();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void carregarModelProcPf() {
		try {
			if (!Util.isNull(empreendimento)) {		
				modelProcPf = Util.castListToDataModel(procuradorPfEmpreendimentoService.listarByEmpreendimento(empreendimento));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarModelProcPj() {
		try {
			if (!Util.isNull(empreendimento)) {		
				modelProcRep = Util.castListToDataModel(procuradorRepEmpreendimentoService.listarByEmpreendimento(empreendimento));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	

	public String incluirProcurador() {
		try {
			PessoaFisica pessoaFisica = this.pessoaFisicaService.filtrarPessoaFisicaByCpf(this.empreendimento.getIdePessoa().getPessoaFisica());
			addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
			ContextoUtil.getContexto().setPessoaFisica(pessoaFisica);
			ContextoUtil.getContexto().setVisualizarPessoaFisica(Boolean.FALSE);
			return "/paginas/manter-pessoafisica/cadastro.xhtml";//Retorna na aba de procuradores
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao inserir procurador");
			return null;
		}
	}
	
	public String incluirProcuradorPJ() {
		try {
			PessoaJuridica  pessoaJuridica  =  this.pessoaJuridicaService.filtrarPessoaFisicaByCnpj(this.empreendimento.getIdePessoa().getPessoaJuridica());
			addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
			ContextoUtil.getContexto().setPessoaJuridica(pessoaJuridica);
			ContextoUtil.getContexto().setVisualizarPessoaJuridica(Boolean.FALSE);
			return "/paginas/manter-pessoajuridica/cadastro.xhtml";//Retorna na aba de procuradores
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao inserir procurador");
			return null;
		}
	}
	
	public void compararListaComboPf() {
		try {
			List<ProcuradorPfEmpreendimento> lista = new ArrayList<ProcuradorPfEmpreendimento>(); 
			if (!Util.isNull(empreendimento)) {		
				lista = procuradorPfEmpreendimentoService.listarByEmpreendimento(empreendimento);
			}
			if (!lista.isEmpty()) {
				List<ProcuradorPessoaFisica> newProcPessoasFisicas = new ArrayList<ProcuradorPessoaFisica>();
				newProcPessoasFisicas.addAll(procPessoasFisicas);
				for (ProcuradorPfEmpreendimento procuradorPfEmpreendimento : lista) {
					for (ProcuradorPessoaFisica procuradorPessoaFisica : procPessoasFisicas) {
						if (!isVisualizar && procuradorPessoaFisica.getIdeProcuradorPessoaFisica().equals(procuradorPfEmpreendimento.getIdeProcuradorPessoaFisica().getIdeProcuradorPessoaFisica())) {
							newProcPessoasFisicas.remove(procuradorPessoaFisica);
						}
					}
				}
				procPessoasFisicas = newProcPessoasFisicas;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void compararListaComboRep() {
		try {
			List<ProcuradorRepEmpreendimento> lista = new ArrayList<ProcuradorRepEmpreendimento>(); 
			if (!Util.isNull(empreendimento)) {		
				lista = procuradorRepEmpreendimentoService.listarByEmpreendimento(empreendimento);
			}
			if (!lista.isEmpty()) {
				List<ProcuradorRepresentante> newProcPessoasJuridicas = new ArrayList<ProcuradorRepresentante>();
				newProcPessoasJuridicas.addAll(procRepresentantes);
				for (ProcuradorRepEmpreendimento procuradorRepEmpreendimento : lista) {
					for (ProcuradorRepresentante procuradorRepresentante : procRepresentantes) {
						if (!isVisualizar && procuradorRepresentante.getIdeProcuradorRepresentante().equals(procuradorRepEmpreendimento.getIdeProcuradorRepresentante().getIdeProcuradorRepresentante())) {
							newProcPessoasJuridicas.remove(procuradorRepresentante);
						}
					}
				}
				procRepresentantes = newProcPessoasJuridicas;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirProcEmpPf() {
		try {
			procuradorPfEmpreendimentoService.excluir(procPfEmpreendimentoSelecionado);
			carregarListaProcuradores();
			compararListaComboPf();
			carregarModelProcPf();
			limparObjetosPf();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirProcEmpPJ() {
		try {
			procuradorRepEmpreendimentoService.excluir(procRepEmpreendimentoSelecionado);
			carregarListaProcuradores();
			compararListaComboRep();
			carregarModelProcPj();
			limparObjetosRep();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void limparObjetosPf() {
		procPfEmpreendimento = new ProcuradorPfEmpreendimento();
		listaArquivoProc = new ArrayList<String>();
		arquivoBaixarProc = null;
		procuradorPessoaFisica = new ProcuradorPessoaFisica();
		temArquivoProc = Boolean.FALSE;
		isVisualizar = false;
		bloquarEdicao = false;
	}
	
	public void limparObjetosRep() {
		procRepEmpreendimento = new ProcuradorRepEmpreendimento();
		listaArquivoProcPj = new ArrayList<String>();
		arquivoBaixarProcPj = null;
		procuradorRepresentante = new ProcuradorRepresentante();
		temArquivoProcPj = Boolean.FALSE;
		isVisualizar = false;
		bloquarEdicao = false;
	}
	
	
	
	// GET E SET
	public List<ProcuradorRepresentante> getProcRepresentantes() {
		return procRepresentantes;
	}

	public void setProcRepresentantes(
			List<ProcuradorRepresentante> procRepresentantes) {
		this.procRepresentantes = procRepresentantes;
	}

	public List<ProcuradorPessoaFisica> getProcPessoasFisicas() {
		return procPessoasFisicas;
	}

	public void setProcPessoasFisicas(
			List<ProcuradorPessoaFisica> procPessoasFisicas) {
		this.procPessoasFisicas = procPessoasFisicas;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
		verificarTipoProcurador();
		carregarListaProcuradores();
		listaArquivoProc = new ArrayList<String>();
		procPfEmpreendimento = new ProcuradorPfEmpreendimento();
		procRepEmpreendimento = new ProcuradorRepEmpreendimento();
		carregarModelProcPf();
		disableFormProcEmpPf = Boolean.TRUE;
		temArquivoProc = Boolean.FALSE;
	}
	
	
	public ProcuradorPfEmpreendimento getProcPfEmpreendimento() {
		return procPfEmpreendimento;
	}

	public void setProcPfEmpreendimento(
			ProcuradorPfEmpreendimento procPfEmpreendimento) {
		this.procPfEmpreendimento = procPfEmpreendimento;
	}

	public ProcuradorRepEmpreendimento getProcRepEmpreendimento() {
		return procRepEmpreendimento;
	}

	public void setProcRepEmpreendimento(
			ProcuradorRepEmpreendimento procRepEmpreendimento) {
		this.procRepEmpreendimento = procRepEmpreendimento;
	}
	
	public String getCaminhoArquivoProc() {
		return caminhoArquivoProc;
	}

	public void setCaminhoArquivoProc(String caminhoArquivoProc) {
		this.caminhoArquivoProc = caminhoArquivoProc;
	}

	public List<String> getListaArquivoProc() {
		return listaArquivoProc;
	}

	public void setListaArquivoProc(List<String> listaArquivoProc) {
		this.listaArquivoProc = listaArquivoProc;
	}

	public Boolean getTemArquivoProc() {
		return temArquivoProc;
	}

	public void setTemArquivoProc(Boolean temArquivoProc) {
		this.temArquivoProc = temArquivoProc;
	}


	public ProcuradorPessoaFisica getProcuradorPessoaFisica() {
		return procuradorPessoaFisica;
	}


	public void setProcuradorPessoaFisica(
			ProcuradorPessoaFisica procuradorPessoaFisica) {
		this.procuradorPessoaFisica = procuradorPessoaFisica;
	}


	public ProcuradorRepresentante getProcuradorRepresentante() {
		return procuradorRepresentante;
	}


	public void setProcuradorRepresentante(
			ProcuradorRepresentante procuradorRepresentante) {
		this.procuradorRepresentante = procuradorRepresentante;
	}


	public Boolean getIsProcuradorPf() {
		return isProcuradorPf;
	}


	public void setIsProcuradorPf(Boolean isProcuradorPf) {
		this.isProcuradorPf = isProcuradorPf;
	}


	public DataModel<ProcuradorPfEmpreendimento> getModelProcPf() {
		return modelProcPf;
	}


	public void setModelProcPf(DataModel<ProcuradorPfEmpreendimento> modelProcPf) {
		this.modelProcPf = modelProcPf;
	}


	public DataModel<ProcuradorRepEmpreendimento> getModelProcRep() {
		return modelProcRep;
	}


	public void setModelProcRep(DataModel<ProcuradorRepEmpreendimento> modelProcRep) {
		this.modelProcRep = modelProcRep;
	}


	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}


	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	public Boolean getMostrarTabelaProcuradoresPf() {
		if(!Util.isNullOuVazio(modelProcPf) && modelProcPf.getRowCount()>0) {
			mostrarTabelaProcuradoresPf = Boolean.TRUE;
		}
		else {
			mostrarTabelaProcuradoresPf = Boolean.FALSE;
		}
		return mostrarTabelaProcuradoresPf;
	}

	public void setMostrarTabelaProcuradoresPf(Boolean mostrarTabelaProcuradoresPf) {
		this.mostrarTabelaProcuradoresPf = mostrarTabelaProcuradoresPf;
	}


	public ProcuradorPfEmpreendimento getProcPfEmpreendimentoSelecionado() {
		return procPfEmpreendimentoSelecionado;
	}


	public void setProcPfEmpreendimentoSelecionado(
			ProcuradorPfEmpreendimento procPfEmpreendimentoSelecionado) {
		this.procPfEmpreendimentoSelecionado = procPfEmpreendimentoSelecionado;
	}


	public Boolean getDisableFormProcEmpPf() {
		return disableFormProcEmpPf;
	}


	public void setDisableFormProcEmpPf(Boolean disableFormProcEmpPf) {
		this.disableFormProcEmpPf = disableFormProcEmpPf;
	}


	public Boolean getMostrarTabelaProcuradoresPj() {
		if(!Util.isNullOuVazio(modelProcRep) && modelProcRep.getRowCount()>0) {
			mostrarTabelaProcuradoresPj = Boolean.TRUE;
		}
		else {
			mostrarTabelaProcuradoresPj = Boolean.FALSE;
		}
		return mostrarTabelaProcuradoresPj;
	}


	public void setMostrarTabelaProcuradoresPj(Boolean mostrarTabelaProcuradoresPj) {
		this.mostrarTabelaProcuradoresPj = mostrarTabelaProcuradoresPj;
	}


	public Boolean getTemArquivoProcPj() {
		return temArquivoProcPj;
	}


	public void setTemArquivoProcPj(Boolean temArquivoProcPj) {
		this.temArquivoProcPj = temArquivoProcPj;
	}


	public List<String> getListaArquivoProcPj() {
		return listaArquivoProcPj;
	}


	public void setListaArquivoProcPj(List<String> listaArquivoProcPj) {
		this.listaArquivoProcPj = listaArquivoProcPj;
	}

	public String getCaminhoArquivoProcPj() {
		return caminhoArquivoProcPj;
	}


	public void setCaminhoArquivoProcPj(String caminhoArquivoProcPj) {
		this.caminhoArquivoProcPj = caminhoArquivoProcPj;
	}
	
	
	public StreamedContent getArquivoBaixarProc() {
		if(!listaArquivoProc.isEmpty()) {			
			try {
				arquivoBaixarProc = gerenciaArquivoService.getContentFile(procPfEmpreendimento.getDscCaminhoProcuracao());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return arquivoBaixarProc;
	}
	
	public StreamedContent getArquivoBaixarProcPj() {
		if(!listaArquivoProcPj.isEmpty()) {			
			try {
				arquivoBaixarProcPj = gerenciaArquivoService.getContentFile(procRepEmpreendimento.getDscCaminhoProcuracao());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return arquivoBaixarProcPj;
	}

	public void setArquivoBaixarProc(StreamedContent arquivoBaixarProc) {
		this.arquivoBaixarProc = arquivoBaixarProc;
	}
	
	public void setArquivoBaixarProcPj(StreamedContent arquivoBaixarProcPj) {
		this.arquivoBaixarProcPj = arquivoBaixarProcPj;
	}


	public ProcuradorRepEmpreendimento getProcRepEmpreendimentoSelecionado() {
		return procRepEmpreendimentoSelecionado;
	}


	public void setProcRepEmpreendimentoSelecionado(
			ProcuradorRepEmpreendimento procRepEmpreendimentoSelecionado) {
		this.procRepEmpreendimentoSelecionado = procRepEmpreendimentoSelecionado;
	}


	public Boolean getHabilitaLista() {
		if(!getValuesComboProcuradores().isEmpty()){
			habilitaLista  = true;
		}
		else {
			habilitaLista = false;
		}
		return habilitaLista;
	}


	public void setHabilitaLista(Boolean habilitaLista) {
		this.habilitaLista = habilitaLista;
	}


	public boolean getBloquearEdicao() {
		return bloquarEdicao;
	}


	public void setBloquearEdicao(boolean modoEdicao) {
		this.bloquarEdicao = modoEdicao;
	}
	
	public boolean getIsVisualizar() {
		return isVisualizar;
	}
	
	
	public void setIsVisualizar(boolean isVisualizar) {
		this.isVisualizar = isVisualizar;
	}
	
	
	
	
	

}
