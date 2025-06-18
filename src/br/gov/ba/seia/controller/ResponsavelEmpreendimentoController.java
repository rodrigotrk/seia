package br.gov.ba.seia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EscolaridadeService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PaisService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("responsavelEmpreendimentoController")
@ViewScoped
public class ResponsavelEmpreendimentoController extends PessoaEndereco {

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private PaisService paisService;
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	@EJB
	private TipoTelefoneService tipoTelefoneService;
	@EJB
	private TelefoneService telefoneService;
	@EJB
	private EscolaridadeService escolaridadeService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	private PessoaFisica responsavel;
	private Ocupacao ocupacao;
	private Collection<Pais> listaPais;
	private Pais pais;
	private Empreendimento empreendimento;
	private TipoTelefone tipoTelefone;
	private Collection<TipoTelefone> listaTelefone;
	private Telefone telefone;
	private Collection<ResponsavelEmpreendimento> listaResponsaveis;
	private ResponsavelEmpreendimento responsavelEmpreendimento;
	private List<TipoIdentificacao> identificacaoResponsavel;
	private List<Escolaridade> listaEscolaridade;
	private String lblBtnSalvarResponsavelTecnico;

	private Boolean visualizaResponsavel;
	private Boolean editaResponsavel;
	private String numTelefone;
	private Boolean temPessoaSelecionada;

	private Date dataAtual = new Date();
	
	private boolean showGridResponsaveisCadastrados;
	private boolean showCPFDoNovoResponsavel;
	private boolean showDadosDoNovoResponsavel;
	private boolean showDocumentoTelefoneEndereco;
	private boolean bloqueiaBotoesDadosDoResponavel;
	
	private boolean disabledDadosResponsavel;
	
	private DataModel<Telefone> modelTelefone;
	
	private static final int AGUARDANDOENQUADRAMENTO  = 1;
	private static final int PENDENCIADEENQUADRAMENTO = 10;
	private static final int REQUERIMENTOINCOMPLETO = 14;
	private static final int CANCELADO              = 9 ;
	private static final int NAOVINCULADO          = -1;
	
	@PostConstruct
	public void init() {
		try {
			responsavel = new PessoaFisica();
			setPessoa(new Pessoa());
			modelTelefone = null;
			tipoTelefone = new TipoTelefone();
			telefone = new Telefone();
			responsavelEmpreendimento = new ResponsavelEmpreendimento();
			listaResponsaveis = new ArrayList<ResponsavelEmpreendimento>();
			listaEscolaridade = new ArrayList<Escolaridade>();
			
			lblBtnSalvarResponsavelTecnico = ResourceBundle.getBundle("/Bundle").getString("geral_lbl_salvar");
			empreendimento = (Empreendimento) SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO");
			listaPais = paisService.listar();
			listaTelefone = tipoTelefoneService.listarTipoTelefone();
			
			carregarResponsaveis();
			carregarEscolaridade();
			preparaIdentificacao();
	
			visualizaResponsavel = false;
			editaResponsavel = false;
			showGridResponsaveisCadastrados = false;
			showCPFDoNovoResponsavel = false;
			showDadosDoNovoResponsavel = false;
			showDocumentoTelefoneEndereco = false;
			bloqueiaBotoesDadosDoResponavel = true;

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void preparaIdentificacao() {
		identificacaoResponsavel = new ArrayList<TipoIdentificacao>();
		identificacaoResponsavel.add(new TipoIdentificacao(5, "Carteira de identidade de conselho regional ou federação trabalhista"));
	}

	public void editarResponsavel() {
		try {
			responsavelEmpreendimento = responsavelEmpreendimentoService.buscarResponsavelEmpreendimentoPorId(responsavelEmpreendimento.getIdeResponsavelEmpreendimento());
			responsavelEmpreendimento.setIdeEmpreendimento(empreendimento);
			responsavelEmpreendimento.setPossuiART(!Util.isNullOuVazio(responsavelEmpreendimento.getNumART()));
			
			responsavel = responsavelEmpreendimento.getIdePessoaFisica();
			pessoa = responsavel.getPessoa();
			
			carregarTelefones();

			visualizaResponsavel = false;
			editaResponsavel = true;
			showCPFDoNovoResponsavel = true;
			showDadosDoNovoResponsavel = true;
			showDocumentoTelefoneEndereco = true;
			bloqueiaBotoesDadosDoResponavel = false;
			disabledDadosResponsavel = false;
			super.prepararEnderecoGenericoController();
			super.enderecoGenericoController.setVisualizacao(false);
			
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao buscar o responsavel do empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void consultarResponsavelByCPF() {
		
		String cpf;
		
		try {
			
			cpf = responsavel.getNumCpf(); // preenchido na consulta
			
			responsavel = pessoaFisicaService.filtrarPessoaFisicaByCpf(responsavel);
			
			if (!Util.isNullOuVazio(responsavel) && !Util.isNullOuVazio(responsavel.getNumCpf())) {
				this.setPessoa(this.responsavel.getPessoa());
				pais = responsavel.getIdePais();
				lblBtnSalvarResponsavelTecnico = ResourceBundle.getBundle("/Bundle").getString("lbl_btn_associar_responsavel_tecnico_empreedimento");

				carregarTelefones();
				
				visualizaResponsavel = true;
				disabledDadosResponsavel = true;
			} else {
				responsavel = new PessoaFisica();
				responsavel.setNumCpf(cpf);
				visualizaResponsavel = false;
			}
			
			showDadosDoNovoResponsavel = true;
			showDocumentoTelefoneEndereco = false;
			bloqueiaBotoesDadosDoResponavel = false;
		} catch (Exception e) {
			visualizaResponsavel = true;
			showDadosDoNovoResponsavel = false;
			showDocumentoTelefoneEndereco = false;
			bloqueiaBotoesDadosDoResponavel = true;
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarTelefones() {

		try {
			this.modelTelefone = Util.castListToDataModel(telefoneService.filtrarTelefonesNaoExcluidosPorPessoa(this.responsavel.getPessoa()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		// this.telefone =
		// ((List<Telefone>)this.responsavel.getPessoa().getTelefoneCollection()).get(0);
	}

	public void carregarResponsaveis() {
		if (!Util.isNullOuVazio(this.empreendimento)) {
			try {
				this.listaResponsaveis = responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(this.empreendimento);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void carregarEscolaridade() {
		try {
			listaEscolaridade = escolaridadeService.listarEscolaridadeResponsavelTecnico();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean validaResponsavelTecnico() {
		
		if(Util.isNullOuVazio(responsavel.getNumCpf())) {
			MensagemUtil.erro("O campo CPF é de preenchimento obrigatório.");
			showDocumentoTelefoneEndereco = false;
			return false;
		}
		
		if (existeResponsavelNaLista() && visualizaResponsavel ) {
			MensagemUtil.erro("Já existe pessoa cadastrada.");
			showDocumentoTelefoneEndereco = false;
			return false;
		}
		
		if(!Util.isNullOuVazio(responsavel)) {
			if(Util.isNullOuVazio(responsavel.getNomPessoa())) {
				MensagemUtil.erro("O campo Nome é de preenchimento obrigatório");
				showDocumentoTelefoneEndereco = false;
				visualizaResponsavel = false;
				return false;
			}
			
			if(Util.isNullOuVazio(responsavel.getIdePais())) {
				MensagemUtil.erro("O campo Nacionalidade é de preenchimento obrigatório");
				showDocumentoTelefoneEndereco = false;
				visualizaResponsavel = false;
				return false;
			}
			
			if(Util.isNullOuVazio(responsavel.getIdeEscolaridade())) {
				MensagemUtil.erro("O campo Escolaridade é de preenchimento obrigatório");
				showDocumentoTelefoneEndereco = false;
				visualizaResponsavel = false;
				return false;
			}
			
			if(!Util.isNull(responsavel.getPessoa()) && Util.isNullOuVazio(responsavel.getPessoa().getDesEmail())) {
				MensagemUtil.erro("O campo E-mail é de preenchimento obrigatório");
				showDocumentoTelefoneEndereco = false;
				visualizaResponsavel = false;
				return false;
			}
		}
		
		return true;
	}

	public void salvarResponsavel() {
		
		if(!validaResponsavelTecnico()) {
			return;
		} else {
		
			try {
				
				if(!Util.isNull(this.responsavel) && !Util.isNull(this.responsavel.getIdePessoaFisica()) ){ 
					salvarOuAtualizarPessoaResponsavel();
				}
				
				if (editaResponsavel) {
					responsavelEmpreendimentoService.salvarOuAtualizarResponsavelEmpreendimento(responsavelEmpreendimento);
					MensagemUtil.sucesso("Atualização efetuada com sucesso!");
				} else {
					this.salvaResponsavelEmpreendimento();
					MensagemUtil.sucesso("Inclusão efetuada com sucesso!");
				}
				
				this.carregarResponsaveis();
				
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabAbas:formGridResponsaveisCadastrados");
				lblBtnSalvarResponsavelTecnico=ResourceBundle.getBundle("/Bundle").getString("geral_lbl_salvar");
				
				temPessoaSelecionada=true;
				editaResponsavel=true;
				visualizaResponsavel = true;
				showDocumentoTelefoneEndereco = true;
				bloqueiaBotoesDadosDoResponavel = true;
				this.setPessoa(this.responsavel.getPessoa());
				super.prepararEnderecoGenericoController();
				super.enderecoGenericoController.setVisualizacao(disabledDadosResponsavel);
				
			} catch (Exception e) {				
				MensagemUtil.erro("Erro ao incluir responsável.");
				temPessoaSelecionada = false;
				editaResponsavel = false;
				temPessoaSelecionada = false;
				showDocumentoTelefoneEndereco = false;
				bloqueiaBotoesDadosDoResponavel = true;
				visualizaResponsavel = true;
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void salvaResponsavelEmpreendimento() throws Exception {
		ResponsavelEmpreendimento resp = new ResponsavelEmpreendimento();
		resp.setDtcCriacao(new Date());
		resp.setDtcExclusao(null);
		resp.setIdeEmpreendimento(this.empreendimento);
		resp.setIdePessoaFisica(this.responsavel);
		
		try {
			if (empreendimento.getResponsavelEmpreendimentoCollection() == null) {
				empreendimento.setResponsavelEmpreendimentoCollection(new ArrayList<ResponsavelEmpreendimento>());
			}
			empreendimento.getResponsavelEmpreendimentoCollection().add(resp);
			
		}
		catch(LazyInitializationException e ){
			//todo erro de lazy inicialization
			empreendimento = empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
			empreendimento.getResponsavelEmpreendimentoCollection().add(resp);
		}
		
		responsavelEmpreendimentoService.salvarOuAtualizarResponsavelEmpreendimento(resp);
		
		responsavelEmpreendimento = resp;
	}

	public void salvarOuAtualizarPessoaResponsavel() {
		try {
			
			if(Util.isNullOuVazio(this.responsavel.getPessoa().getDtcCriacao())){
				this.responsavel.getPessoa().setDtcCriacao(new Date());				
			}
			pessoaFisicaService.salvarOuAtualizarPessoaFisica(this.responsavel);
		} catch (Exception e) {
			temPessoaSelecionada = Boolean.FALSE;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public Boolean existeResponsavelNaLista() {
		for (ResponsavelEmpreendimento resp : listaResponsaveis) {
			if (!Util.isNullOuVazio(resp) && resp.getIdePessoaFisica().getNumCpf().equals(this.responsavel.getNumCpf())) {
				return true;
			}
		}
		return false;
	}

	public void adicionaTelefone() {
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(responsavel.getPessoa());
		telefone.setPessoaCollection(lista);

		try {
			telefoneService.salvarTelefone(telefone);
			carregarTelefones();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void limparTelefone() {
		System.out.println("shall clear");
	}

	public void limpar() {
		init();
		showCPFDoNovoResponsavel = true;
		showDadosDoNovoResponsavel = false;
		showDocumentoTelefoneEndereco = false;
		disabledDadosResponsavel = false;
	}
	
	public void incluirNovoResponsavelTecnico() {
		limpar();
		showCPFDoNovoResponsavel = true;
		showDadosDoNovoResponsavel = false;
		showDocumentoTelefoneEndereco = false;
		disabledDadosResponsavel = false;
	}
	
	public void esconderFormulario() {
		limpar();
		showCPFDoNovoResponsavel = false;
		showDadosDoNovoResponsavel = false;
		showDocumentoTelefoneEndereco = false;
	}

	public void removerTelefone() {
		try {
			telefoneService.excluirTelefone(this.telefone);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void novoTelefone() {
		this.telefone = new Telefone();
	}

	public void removerResponsavel(){
		try {
			
			if (podeExcluirRepresentante()){
				responsavelEmpreendimentoService.removerResponsavel(this.responsavelEmpreendimento);
				carregarResponsaveis();
				
				this.responsavel = null;
				this.responsavel = new PessoaFisica();
				listaTelefone = new ArrayList<TipoTelefone>();
				
				showCPFDoNovoResponsavel = false;
				showDadosDoNovoResponsavel = false;
				showDocumentoTelefoneEndereco = false;
				
				MensagemUtil.sucesso("Exclusão efetuada com sucesso!");		
			} else {
				RequestContext.getCurrentInstance().execute("dlgResponsavel.hide()");
				MensagemUtil.sucesso("Esse empreendimento possui pelo menos um requerimento associado e por isso o representante legal não pode ser desvinculado.");
			}
		} catch (AppExceptionError e) {
			MensagemUtil.erro("Erro ao remover responsável: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao remover responsável: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean podeExcluirRepresentante(){
		
		int ideRequerimentoEmpreendimento = 0 ;
		int statusRequerimento = 0;
		boolean podeExcluir = false;
		boolean responsavelVinculadoAoRequerimento = true;
		
		try {
		
			ideRequerimentoEmpreendimento = empreendimentoService.requerimentoVinculadoAoEmpreendimento(empreendimento.getIdeEmpreendimento());
			responsavelVinculadoAoRequerimento = requerimentoPessoaService.isPessoaAssociadoRequerimento(responsavelEmpreendimento.getIdePessoaFisica().getIdePessoaFisica(), ideRequerimentoEmpreendimento);
			statusRequerimento = empreendimentoService.statusEmpreedinmentoRequerimento(empreendimento.getIdeEmpreendimento());
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if(ideRequerimentoEmpreendimento== -1){
			return true;
		}
		
		if(!responsavelVinculadoAoRequerimento){
			return true;
		}
		else{
			
			if(!Util.isNullOuVazio(statusRequerimento)){
				
				if ((statusRequerimento == AGUARDANDOENQUADRAMENTO )||
					(statusRequerimento == PENDENCIADEENQUADRAMENTO)||
					(statusRequerimento == REQUERIMENTOINCOMPLETO)  ||
					(statusRequerimento == CANCELADO) ||
					(statusRequerimento == NAOVINCULADO)){
	
					podeExcluir = true;
				}
				
			}
		
		}
		return podeExcluir;
	}

	public Boolean getTemPessoaSelecionada() {
		if (!Util.isNullOuVazio(responsavel) && !Util.isNullOuVazio(responsavel.getIdePessoaFisica())) {
			temPessoaSelecionada = true;
		} else {
			temPessoaSelecionada = false;
		}
		return temPessoaSelecionada;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
		carregarResponsaveis();
	}

	public boolean isShowGridResponsaveisCadastrados() {
		
		if(listaResponsaveis.isEmpty()) showGridResponsaveisCadastrados = false;
		else showGridResponsaveisCadastrados = true;
	
		return showGridResponsaveisCadastrados;
	}

	@Override
	public void enviarId() {
		super.enviarId("formDadosEndereco");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		super.enderecoGenericoController.setVisualizacao(false);
	}
	
	public void tratarArquivo(FileUploadEvent event) {
		String caminhoArquivo = null; 
		
		if(!Util.isNullOuVazio(responsavelEmpreendimento) && !Util.isNullOuVazio(responsavelEmpreendimento.getIdePessoaFisica())) {
			caminhoArquivo = FileUploadUtil.Enviar(event, 
					DiretorioArquivoEnum.REPRESENTANTE_LEGAL.toString() + responsavelEmpreendimento.getIdePessoaFisica().getIdePessoaFisica() + File.separator);
		}
		
		if(!Util.isNullOuVazio(caminhoArquivo)) {
			responsavelEmpreendimento.setDscCaminhoArquivoART(caminhoArquivo);
			MensagemUtil.sucesso("Arquivo enviado com sucesso.");
			
		} else {
			MensagemUtil.erro("Houve um erro ao tentar enviar o arquivo.");			
		}
	}
	
	public String getFileNameART() {
		return FileUploadUtil.getFileName(responsavelEmpreendimento.getDscCaminhoArquivoART());
	}
	
	public boolean isDisableUploadART() {
		return !Util.isNullOuVazio(responsavelEmpreendimento.getDscCaminhoArquivoART());
	}
	
	public StreamedContent getDownloadART() {
		StreamedContent arquivoBaixar;
		
		try {
			arquivoBaixar = gerenciaArquivoService.getContentFile(responsavelEmpreendimento.getDscCaminhoArquivoART());
		} catch (Exception e) {
			MensagemUtil.erro("Arquivo não encontrado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		return arquivoBaixar;
	}
	
	public void salvarART() {
		try {
			if(!responsavelEmpreendimento.isPossuiART()) {
				excluirART();
			} else {
				if(Util.isNullOuVazio(responsavelEmpreendimento.getNumART())) {
					MensagemUtil.msg0003("Número da ART");
					return;
				}
				
				if(Util.isNullOuVazio(responsavelEmpreendimento.getDscCaminhoArquivoART())) {
					MensagemUtil.msg0003("Upload da ART");
					return;
				}
			}
			
			responsavelEmpreendimentoService.salvarOuAtualizarResponsavelEmpreendimento(responsavelEmpreendimento);
			MensagemUtil.sucesso("Atualização efetuada com sucesso!");
			
			if(!Util.isNullOuVazio(responsavelEmpreendimento.getListaCaminhoArquivoParaExcluir())){
				for (String arq : responsavelEmpreendimento.getListaCaminhoArquivoParaExcluir()) {
					gerenciaArquivoService.deletarArquivo(arq);
				}
				
				responsavelEmpreendimento.setListaCaminhoArquivoParaExcluir(null);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirART() {
		responsavelEmpreendimento.setNumART(null);
		
		if(!Util.isNullOuVazio(responsavelEmpreendimento.getDscCaminhoArquivoART())){
			gerenciaArquivoService.deletarArquivo(responsavelEmpreendimento.getDscCaminhoArquivoART());
			responsavelEmpreendimento.setDscCaminhoArquivoART(null);
		}
	}
	
	public void limparDocumentoART() {
		if(Util.isNullOuVazio(responsavelEmpreendimento.getListaCaminhoArquivoParaExcluir())){
			responsavelEmpreendimento.setListaCaminhoArquivoParaExcluir(new ArrayList<String>());
		}
		
		responsavelEmpreendimento.getListaCaminhoArquivoParaExcluir().add(responsavelEmpreendimento.getDscCaminhoArquivoART());
		
		responsavelEmpreendimento.setDscCaminhoArquivoART(null);
	}
	
	/************************
	 *						* 
	 * XXX: GETS AND SETS	*
	 * 				 		*
	 ************************/
	
	public PessoaFisica getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(PessoaFisica responsavel) {
		this.responsavel = responsavel;
	}

	public Ocupacao getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(Ocupacao ocupacao) {
		this.ocupacao = ocupacao;
	}

	public Collection<Pais> getListaPais() {
		return listaPais;
	}

	public void setListaPais(Collection<Pais> listaPais) {
		this.listaPais = listaPais;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public Boolean getVisualizaResponsavel() {
		return visualizaResponsavel;
	}

	public void setVisualizaResponsavel(Boolean visualizaResponsavel) {
		this.visualizaResponsavel = visualizaResponsavel;
	}

	public Boolean getEditaResponsavel() {
		return editaResponsavel;
	}

	public void setEditaResponsavel(Boolean editaResponsavel) {
		this.editaResponsavel = editaResponsavel;
	}

	public DataModel<Telefone> getModelTelefone() {
		return modelTelefone;
	}

	public void setModelTelefone(DataModel<Telefone> modelTelefone) {
		this.modelTelefone = modelTelefone;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public Collection<TipoTelefone> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(Collection<TipoTelefone> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}

	public String getNumTelefone() {
		return numTelefone;
	}

	public void setNumTelefone(String numTelefone) {
		this.numTelefone = numTelefone;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Collection<ResponsavelEmpreendimento> getListaResponsaveis() {
		return listaResponsaveis;
	}

	public void setListaResponsaveis(Collection<ResponsavelEmpreendimento> listaResponsaveis) {
		this.listaResponsaveis = listaResponsaveis;
	}

	public ResponsavelEmpreendimento getResponsavelEmpreendimento() {
		return responsavelEmpreendimento;
	}

	public void setResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento) {
		this.responsavelEmpreendimento = responsavelEmpreendimento;
	}

	public List<TipoIdentificacao> getIdentificacaoResponsavel() {
		return identificacaoResponsavel;
	}

	public void setIdentificacaoResponsavel(List<TipoIdentificacao> identificacaoResponsavel) {
		this.identificacaoResponsavel = identificacaoResponsavel;
	}

	public void setTemPessoaSelecionada(Boolean temPessoaSelecionada) {
		this.temPessoaSelecionada = temPessoaSelecionada;
	}

	public List<Escolaridade> getListaEscolaridade() {
		return listaEscolaridade;
	}

	public void setListaEscolaridade(List<Escolaridade> listaEscolaridade) {
		this.listaEscolaridade = listaEscolaridade;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public String getLblBtnSalvarResponsavelTecnico() {
		return lblBtnSalvarResponsavelTecnico;
	}

	public void setLblBtnSalvarResponsavelTecnico(String lblBtnSalvarResponsavelTecnico) {
		this.lblBtnSalvarResponsavelTecnico = lblBtnSalvarResponsavelTecnico;
	}
	
	public void setShowGridResponsaveisCadastrados(boolean showGridResponsaveisCadastrados) {
		this.showGridResponsaveisCadastrados = showGridResponsaveisCadastrados;
	}
	
	public boolean isShowDadosDoNovoResponsavel() {
		return showDadosDoNovoResponsavel;
	}
	
	public void setShowDadosDoNovoResponsavel(boolean showDadosDoNovoResponsavel) {
		this.showDadosDoNovoResponsavel = showDadosDoNovoResponsavel;
	}

	public boolean isShowDocumentoTelefoneEndereco() {
		return showDocumentoTelefoneEndereco;
	}

	public void setShowDocumentoTelefoneEndereco(boolean showDocumentoTelefoneEndereco) {
		this.showDocumentoTelefoneEndereco = showDocumentoTelefoneEndereco;
	}

	public boolean isShowCPFDoNovoResponsavel() {
		return showCPFDoNovoResponsavel;
	}

	public void setShowCPFDoNovoResponsavel(boolean showCPFDoNovoResponsavel) {
		this.showCPFDoNovoResponsavel = showCPFDoNovoResponsavel;
	}

	public boolean isBloqueiaBotoesDadosDoResponavel() {
		return bloqueiaBotoesDadosDoResponavel;
	}

	public void setBloqueiaBotoesDadosDoResponavel(boolean bloqueiaBotoesDadosDoResponavel) {
		this.bloqueiaBotoesDadosDoResponavel = bloqueiaBotoesDadosDoResponavel;
	}

	public boolean isDisabledResponsavelEscolaridade() {
		if(!Util.isNull(responsavel) && Util.isNull(responsavel.getIdeEscolaridade())) {
			return false;
		}
		return disabledDadosResponsavel;
	}
	
	public boolean isDisabledDadosResponsavel() {
		return disabledDadosResponsavel;
	}

	public void setDisabledDadosResponsavel(boolean disabledDadosResponsavel) {
		this.disabledDadosResponsavel = disabledDadosResponsavel;
	}
}