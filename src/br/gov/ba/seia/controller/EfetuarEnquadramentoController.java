/**
 * 
 */
package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.AtosSemFormularioEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.facade.EquadramentoAtoAmbientalServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.NumeroDocumentoFormatterUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author MJunior
 */
@Named("efetuarEnquadramentoController")
@ViewScoped
public class EfetuarEnquadramentoController {

	private ResourceBundle bundle = ResourceBundle.getBundle("/Bundle");
	private Boolean isEnquadramentoRendered;
	private Boolean isFormularioRendered;
	private Boolean isJustificativaRendered;
	private Boolean isConfirmarEnquadramento;
	private Boolean isEmpreendimentoPassivel;
	private Boolean isConfirmaActionRendered;
	private Boolean confirmarEnquadramento;
	private String confirmarEnquadramentoMsg;
	private String nomePessoaRequerimento;
	private String numerCpfCnpj;
	private String nomeEmpreendimento;
	private String empreendimentoPassivel;
	private String dscJustificativa;
	private List<SelectItem> listAtoAmbiental;
	private List<SelectItem> selectedAtoAmbiental;
	private List<SelectItem> formularios;
	private List<SelectItem> selectedFormulario;
	private List<AtoAmbiental> listDocsAtoAmbiental;
	private Usuario usuario;
	private Enquadramento enquadramento;
	private List<RequerimentoUnicoDTO> requerimentosDTO;
	private List<String> listaArquivo;
	private RequerimentoUnico requerimentoUnico;
	private List<DocumentoObrigatorio> docsObrigs;
	private List<AtoAmbiental> atosAmbi;
	private List<DocumentoAto> listaDocumentoAto;
	@EJB
	private ParametroService parametroService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private EquadramentoAtoAmbientalServiceFacade enquadramentoAtoAmbientalService;
	
	@EJB
	private EmailService emailService;
	
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	/**
	 * 
	 */
	public EfetuarEnquadramentoController() {
	}

	public void limparTela() {
		this.isEnquadramentoRendered = null;
		this.isFormularioRendered = Boolean.FALSE;
		this.isJustificativaRendered = Boolean.FALSE;
		isEmpreendimentoPassivel = null;
		isConfirmarEnquadramento = null;
		empreendimentoPassivel = null;
		listAtoAmbiental = null;
		selectedAtoAmbiental = null;
		formularios = null;
		selectedFormulario = null;
		enquadramento = null;
		requerimentosDTO = null;
		requerimentoUnico = null;
		nomePessoaRequerimento = "";
		numerCpfCnpj = "";
		nomeEmpreendimento = "";
		dscJustificativa = "";
		this.listaArquivo = new ArrayList<String>();
	}

	public void carregarTela(RequerimentoUnico requerimento) {
		Exception erro = null;
		try {
			if(Util.isLazyInitExcepOuNull(requerimento.getEnquadramento())){
				Collection<Enquadramento> enquadColl = enquadramentoService.buscarEnquadramentoPorRequerimentoUnico(requerimento.getRequerimento());
				for (Enquadramento enquadramento : enquadColl) {
					this.enquadramento = enquadramento;
					break;
				}
				this.enquadramento = new Enquadramento();
			}else{
				for (Enquadramento enquadramentoIter : requerimento.getEnquadramento()) {
					enquadramento = enquadramentoIter;
					break;
				}
			}
			if (!Util.isNull(requerimento)) {
				requerimentoUnico = requerimento;
				this.isEnquadramentoRendered = null;
				this.isFormularioRendered = Boolean.FALSE;
				this.isJustificativaRendered = Boolean.FALSE;
				usuario = ContextoUtil.getContexto().getUsuarioLogado();
				enquadramento.setIdePessoa(pessoaService.carregarGet(usuario.getIdePessoaFisica()));
				enquadramento.setIdeRequerimentoUnico(requerimentoUnico);
				enquadramento.setIdeRequerimento(requerimento.getRequerimento());
				this.buscarRequerimantoEmpreendimento();
				for (RequerimentoUnicoDTO requerimentoUnicoDTO : requerimentosDTO) {
					nomeEmpreendimento = requerimentoUnicoDTO.getEmpreendimento().getNomEmpreendimento();
					if (requerimentoUnicoDTO.getPessoa().getPessoaFisica() != null) {
						nomePessoaRequerimento = requerimentoUnicoDTO.getPessoa().getPessoaFisica().getNomPessoa();
						numerCpfCnpj = NumeroDocumentoFormatterUtil.formatarCpf(requerimentoUnicoDTO.getPessoa().getPessoaFisica().getNumCpf());
					} else {
						nomePessoaRequerimento = requerimentoUnicoDTO.getPessoa().getPessoaJuridica().getNomRazaoSocial();
						numerCpfCnpj = NumeroDocumentoFormatterUtil.formatarCnpj(requerimentoUnicoDTO.getPessoa().getPessoaJuridica().getNumCnpj());
					}
					break;
				}
				if (this.isEnquadramentoInicializado()) {
					if (this.isEnquadramentoInicializadoPeloUsuario()) {
						this.confirmarEnquadramentoMsg = bundle.getString("enquadramento_msg_possivel_realizar_enquadramento");
						this.isConfirmarEnquadramento = Boolean.TRUE;
						confirmarEnquadramento = null;
						isEnquadramentoRendered = null;
						this.obterAtoAmbiental();
					} else {
						this.isConfirmarEnquadramento = Boolean.FALSE;
						this.confirmarEnquadramentoMsg = bundle.getString("enquadramento_msg_enquadramento_requerimento_iniciado");
					}
				} else {
					this.confirmarEnquadramentoMsg = bundle.getString("enquadramento_msg_possivel_realizar_enquadramento");
					this.isConfirmarEnquadramento = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void trataArquivo(FileUploadEvent event) {
		
		Exception erro = null;
		
		try {
			if (!Util.isNull(listaArquivo) && !listaArquivo.isEmpty()) {
				JsfUtil.addWarnMessage("Um arquivo já foi enviado.");
				return;
			}
			String caminhoArquivo = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
			//this.enquadramento.setDscCaminhoArquivoRIMA(caminhoArquivo);
			this.adicionarArquivoNaLista(caminhoArquivo);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro = e;
			JsfUtil.addErrorMessage("Erro ao enviar arquivo.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void adicionarArquivoNaLista(String caminhoArquivo) {
		if (!Util.isNullOuVazio(listaArquivo)) {
			listaArquivo.clear();
		} else {
			listaArquivo = new ArrayList<String>();
		}
		listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
	}

	/**
	 * 
	 */
	private void buscarRequerimantoEmpreendimento() {
		Exception erro = null;
		try {
			requerimentosDTO = requerimentoUnicoService.filtrarRequerimentoUnicoByConsulta(requerimentoUnico, null, null, null, null, null, new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	@SuppressWarnings("unchecked")
	public void atoAmbientalChanged(ValueChangeEvent event) {
		Exception erro = null;
		try {
			if (event.getNewValue() != null && event.getNewValue() instanceof ArrayList<?>) {
				selectedAtoAmbiental = ((ArrayList<SelectItem>) event.getNewValue());
				List<AtoAmbiental> listaAtosAmbientais = ((ArrayList<AtoAmbiental>) event.getNewValue());
				if (selectedAtoAmbiental.isEmpty() || listaTemApenasAtoSemFormulario(listaAtosAmbientais)) {
					isFormularioRendered = Boolean.FALSE;
					selectedFormulario = null;
				} else {
					this.obterAtoFormularios();
					if (!Util.isNullOuVazio(formularios)) {
						isFormularioRendered = Boolean.TRUE;
					}
				}
			} else {
				isFormularioRendered = Boolean.FALSE;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public boolean listaTemApenasAtoSemFormulario(List<AtoAmbiental> lista) {
		boolean retorno = false;
		for (AtoAmbiental atoAmbiental : lista) {
			for (AtosSemFormularioEnum atoSemFormulario : AtosSemFormularioEnum.values()) {
				if (atoAmbiental.getIdeAtoAmbiental().intValue() == atoSemFormulario.getIdeAtoAmbiental().intValue()) {
					retorno = true;
					break;
				} else {
					retorno = false;
				}
			}
			if (!retorno) {
				break;
			}
		}
		return retorno;
	}

	public void isPossivelRealizarEnquadramentoChanged(ValueChangeEvent event) throws Exception {
		if (event.getNewValue() != null) {
			Boolean newValue = (Boolean) event.getNewValue();
			if (newValue) {
				// sim
				isEmpreendimentoPassivel = Boolean.TRUE;
				isEnquadramentoRendered = Boolean.TRUE;
				this.isJustificativaRendered = Boolean.FALSE;
				enquadramento.setIndEnquadramentoAprovado(Boolean.TRUE);
				this.iniciarEnquadramento();
				this.obterAtoAmbiental();
			} else {
				// não
				enquadramento.setIndEnquadramentoAprovado(Boolean.FALSE);
				isEmpreendimentoPassivel = Boolean.FALSE;
				isEnquadramentoRendered = Boolean.FALSE;
				this.isJustificativaRendered = Boolean.TRUE;

			}
			isConfirmaActionRendered = Boolean.TRUE;
		}
	}

	public void confirmarAction() {
		
		carregarListaDocumentoAtoSelecionado();
		
		if (!Util.isNullOuVazio(confirmarEnquadramento) && confirmarEnquadramento) {
			if (!this.isEnquadramentoValido()) {
				JsfUtil.addErrorMessage("Favor selecionar apenas um tipo de Licença e Adesão e Compromisso.");
				return;
			}
			if (Util.isNullOuVazio(listaDocumentoAto)) {
				JsfUtil.addErrorMessage("Favor selecionar pelo menos um dos Documentos Obrigatórios.");
				return;
			}
			if (isFormularioRendered && (!Util.isNullOuVazio(formularios) && Util.isNullOuVazio(selectedFormulario))) {
				JsfUtil.addErrorMessage("O campo Formulário(s) é de preenchimento obrigatório.");
				return;
			}
			if (Util.isNullOuVazio(this.empreendimentoPassivel)) {
				JsfUtil.addErrorMessage("O campo Passível de EIA/RIMA é de preenchimento obrigatório.");
				return;
			}
			if (selectedAtoAmbiental == null || selectedAtoAmbiental.isEmpty()) {
				JsfUtil.addErrorMessage("Favor selecionar pelo menos um dos Ato Ambiental.");
				return;
			}
			if (selectedAtoAmbiental != null && !selectedAtoAmbiental.isEmpty() && !Util.isNullOuVazio(this.empreendimentoPassivel)) {
				enquadramento.setIndEnquadramentoAprovado(Boolean.TRUE);
				enquadramento.setDscJustificativa("");
				
				if (this.empreendimentoPassivel.equals("1"))
					enquadramento.setIndPassivelEiarima(Boolean.TRUE);
				else
					enquadramento.setIndPassivelEiarima(Boolean.FALSE);
				
				try {
					inserirEnquadramento();
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage("Erro ao efetuar o enquadramento!");
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		} 
		else if (!Util.isNullOuVazio(confirmarEnquadramento) && !confirmarEnquadramento) {
			if (!getDscJustificativa().trim().isEmpty()) {
				enquadramento.setDscJustificativa(getDscJustificativa().trim());
				enquadramento.setIndEnquadramentoAprovado(Boolean.FALSE);
				if (!Util.isNull(selectedAtoAmbiental)) {
					selectedAtoAmbiental.clear();
				}
				if (!Util.isNull(selectedFormulario)) {
					selectedFormulario.clear();
				}
				try{
					String textoEmail = gerarTextoEmailRequerimentoNaoAprovado();
					salvarComunicacao(enquadramento.getIdeRequerimento(),textoEmail);
					emailService.enviarEmailsAoRequerente(enquadramento.getIdeRequerimento(), "SEIA - Comunicado - Requerimento número " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento(), textoEmail);
					limparTela();
					String msg = "Um e-mail contendo a justificativa do não enquadramento do requerimento foi enviado à todos os interessados. Aguarde as alterações cabíveis para efetuar uma nova tentativa de enquadramento!";
					ContextoUtil.getContexto().setObject(msg);
					RequestContext.getCurrentInstance().execute("$(window.document.location).attr('href','/paginas/novo-requerimento/consulta.xhtml');");
				}
				catch(Exception e){
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage("Erro ao tentar enviar e-mail.");
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			} else {
				JsfUtil.addErrorMessage("O campo Justificativa é de preenchimento obrigatório.");
			}
		} else {
			JsfUtil.addErrorMessage("O campo Confirmar Enquadramento é de preenchimento obrigatório.");
		}
	}

	private void carregarListaDocumentoAtoSelecionado() {
		
		listaDocumentoAto = new ArrayList<DocumentoAto>();
		
		if (!Util.isNullOuVazio(listDocsAtoAmbiental)) {
			for (AtoAmbiental ato : listDocsAtoAmbiental) {
				if (!Util.isNullOuVazio(ato.getListaDocumentoAtoSelecionado())) {
					listaDocumentoAto.addAll(ato.getListaDocumentoAtoSelecionado());
				}
			}
		}
	}

	private String gerarTextoEmailRequerimentoNaoAprovado() {
		StringBuilder textoEmail = new StringBuilder();
		textoEmail.append("Prezado(a), Não foi possível enquadrar o Requerimento de nº " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento() + ". Segue justificativa:\n");
		textoEmail.append(enquadramento.getDscJustificativa() + "\n");
		textoEmail.append("Para que o processo de requerimento seja concluí­do, é necessário que \n");
		textoEmail.append("se resolvam as pendências. Para tal, entre no SEIA com seu login e \n");
		textoEmail.append("senha, acesse a funcionalidade de consulta de Requerimentos (item de menu Requerimento, subitem Consultar)");
		textoEmail.append(" e altere o requerimento indicado conforme justificativa acima.");
		textoEmail.append("Atte.,\n");
		textoEmail.append("Central de Atendimento/INEMA.");
		return textoEmail.toString();
	}
	
	private void salvarComunicacao(Requerimento req, String texto)  {
		ComunicacaoRequerimento comunicacao = new ComunicacaoRequerimento();
		comunicacao.setIdeRequerimento(req);
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setDesMensagem(texto);
		comunicacaoRequerimentoService.salvar(comunicacao);
	}

	private boolean isEnquadramentoValido() {
		if (!Util.isNullOuVazio(selectedAtoAmbiental)) {
			AtoAmbiental atoAmbiental = new AtoAmbiental(AtoAmbientalEnum.LAC.getId());
			DocumentoObrigatorio erb = getDocumentoErb();
			DocumentoObrigatorio posto = getDocumentoPosto();
			if (selectedAtoAmbiental.contains(atoAmbiental)) {
				if (this.selectedFormulario.contains(erb) && this.selectedFormulario.contains(posto)) {
					return false;
				};
			}
		}
		return true;
	}

	public void removerArquivo() {
		this.listaArquivo = new ArrayList<String>();
	}

	private DocumentoObrigatorio getDocumentoPosto() {
		DocumentoObrigatorio posto = null;
		Exception erro =null;
		try {
			posto = new DocumentoObrigatorio(Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_POSTO).getDscValor()));
		} catch (NumberFormatException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return posto;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return posto;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return posto;
	}

	private DocumentoObrigatorio getDocumentoErb() {
		DocumentoObrigatorio erb = null;
		
		Exception erro = null;
		try {
			erb = new DocumentoObrigatorio(Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_ERB).getDscValor()));
		} catch (NumberFormatException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return erb;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return erb;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return erb;
	}

	private void inserirEnquadramento() throws Exception {

		if (this.isEnquadramentoValido()) {
			
			gerarListaEnquadramentoAtoAmbiental();
			gerarListaEnquadramentoDocumentoAto();
			
			enquadramentoService.salvar(enquadramento);
			enquadramentoService.salvarComunicacao(enquadramento);

			if (enquadramento.getIndEnquadramentoAprovado()) {
				ContextoUtil.getContexto().setObject("Enquadramento efetuado com sucesso!");
			} else {
				ContextoUtil
						.getContexto()
						.setObject("Um e-mail contendo a justificativa do não enquadramento do requerimento foi enviado à todos os interessados."
								 + " Aguarde as alterações cabíveis para efetuar uma nova tentativa de enquadramento!");
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/novo-requerimento/consulta.xhtml");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void gerarListaEnquadramentoAtoAmbiental() throws Exception {
		
		if (!Util.isNullOuVazio(selectedAtoAmbiental)) {
			List<AtoAmbiental> listaAtos = (ArrayList<AtoAmbiental>) (List<?>) selectedAtoAmbiental;
			List<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = new ArrayList<EnquadramentoAtoAmbiental>();
			for(AtoAmbiental ato : listaAtos) {
				EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental();
				enquadramentoAtoAmbiental.setEnquadramento(enquadramento);
				enquadramentoAtoAmbiental.setAtoAmbiental(ato);
				
				for(DocumentoAto docAto : listaDocumentoAto) {
					docAto.setIdeAtoAmbiental(documentoAtoAmbientalService.carregarDocumentoAto(docAto).getIdeAtoAmbiental());
					if(docAto.getIdeAtoAmbiental().equals(ato)){
						docAto.setEnquadramentoAtoAmbiental(enquadramentoAtoAmbiental);
					}
				}
				
				listaEnquadramentoAtoAmbiental.add(enquadramentoAtoAmbiental);
			}
			enquadramento.setEnquadramentoAtoAmbientalCollection(listaEnquadramentoAtoAmbiental);
		}		
	}
	
	private void gerarListaEnquadramentoDocumentoAto() {
		
		if (!Util.isNullOuVazio(listaDocumentoAto)) {
			List<EnquadramentoDocumentoAto> listaEnquadramentoDocumentoAto = new ArrayList<EnquadramentoDocumentoAto>();
			for(DocumentoAto documentoAto : listaDocumentoAto) {
				EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
				enquadramentoDocumentoAto.setEnquadramento(enquadramento);
				enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
				
				listaEnquadramentoDocumentoAto.add(enquadramentoDocumentoAto);
			}
			enquadramento.setEnquadramentoDocumentoAtoCollection(listaEnquadramentoDocumentoAto);
		}		
	}

	private void iniciarEnquadramento() {
		
		Exception erro = null;
		try {
			if (enquadramentoService.buscarUltimoEnquadramentoRequerimento(enquadramento.getIdeRequerimento()) == null) {
				enquadramento.setIdeRequerimento(enquadramento.getIdeRequerimento());
				enquadramentoAtoAmbientalService.iniciarEnquadramento(enquadramento);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void confirmarEnquadramentoChanged(ValueChangeEvent event) {
		Exception erro = null;
		try {
			if (event.getNewValue() != null && !event.getNewValue().toString().trim().isEmpty()) {
				String newValue = event.getNewValue().toString().trim();
				if (newValue.equals("1")) {
					this.obterAtoAmbiental();
					if (!listAtoAmbiental.isEmpty()) {
						isEnquadramentoRendered = Boolean.TRUE;
					} else {
						isEnquadramentoRendered = Boolean.FALSE;
					}
					isJustificativaRendered = Boolean.FALSE;
				} else {
					isEnquadramentoRendered = Boolean.FALSE;
					isJustificativaRendered = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private boolean isEnquadramentoInicializado() {
		if (enquadramento != null && enquadramento.getIdeRequerimento() != null && enquadramento.getIdeRequerimento().getIdeRequerimento() != null) {
			Exception erro = null;
			try {
				StatusRequerimento statusReq = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(enquadramento);
				if (statusReq != null && statusReq.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus())) {
					return true;
				}
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage(e.getMessage());
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
		}
		return false;
	}

	private boolean isEnquadramentoInicializadoPeloUsuario() {
		if (enquadramento != null && enquadramento.getIdeRequerimento().getIdeRequerimento() != null) {
			Exception erro = null;
			try {
				if (enquadramentoService.buscarEnquadramentoInicializadoPeloUsuario(enquadramento) != null) {
					return true;
				}
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage(e.getMessage());
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
		}
		return false;
	}

	public void obterAtoAmbiental() {
		
		Exception erro = null;
		try {
			listAtoAmbiental = new ArrayList<SelectItem>();
			for (AtoAmbiental atoAmbiental : atoAmbientalService.listarAtoAmbientalDLA(requerimentoUnico)) {
				listAtoAmbiental.add(new SelectItem(atoAmbiental, atoAmbiental.getNomAtoAmbiental()));
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	@SuppressWarnings("unchecked")
	private void obterAtoFormularios() {
		Exception erro = null;
		try {
			this.formularios = new ArrayList<SelectItem>();
			for (DocumentoObrigatorio docObrigatorio : documentoAtoAmbientalService.listarFormulariosAtoWhereIn((Collection<AtoAmbiental>) (Collection<?>) selectedAtoAmbiental)) {
				formularios.add(new SelectItem(docObrigatorio, docObrigatorio.getNomDocumentoObrigatorio(), docObrigatorio.getNomDocumentoObrigatorio()));
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	@SuppressWarnings("unchecked")
	public void montarDocsObrigatorios() {
		Exception erro = null;
		try {
			if (!Util.isNullOuVazio(selectedAtoAmbiental)) {
				listDocsAtoAmbiental = (List<AtoAmbiental>) atoAmbientalService.listarDocumentosAtoAmbientalWhereIn((Collection<AtoAmbiental>) (List<?>) selectedAtoAmbiental);
				RequestContext.getCurrentInstance().execute("dialogDocumentos.show()");
				RequestContext.getCurrentInstance().addPartialUpdateTarget("efetuarEnquadramento:dialogDocumentos");
			} else {
				JsfUtil.addWarnMessage("Selecione pelo menos um Ato Ambiental da lista acima");
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}


	public void isEmpreendimentoPassivelChanged(ValueChangeEvent event) {
		String valor = (String) event.getNewValue();
		if (valor.equals("1")) {
			this.enquadramento.setIndPassivelEiarima(Boolean.TRUE);
		} else {
			this.removerArquivo();
			this.enquadramento.setIndPassivelEiarima(Boolean.FALSE);
		}
	}

	public Boolean getExibirListaArquivo() {
		return !Util.isNullOuVazio(this.listaArquivo) && !this.listaArquivo.isEmpty();
	}

	/**
	 * @return the isEnquadramentoRendered
	 */
	public Boolean getIsEnquadramentoRendered() {
		return isEnquadramentoRendered;
	}

	/**
	 * @param isEnquadramentoRendered
	 * the isEnquadramentoRendered to set
	 */
	public void setIsEnquadramentoRendered(Boolean isEnquadramentoRendered) {
		this.isEnquadramentoRendered = isEnquadramentoRendered;
	}

	/**
	 * @return the isFormularioRendered
	 */
	public Boolean getIsFormularioRendered() {
		return isFormularioRendered;
	}

	/**
	 * @param isFormularioRendered
	 * the isFormularioRendered to set
	 */
	public void setIsFormularioRendered(Boolean isFormularioRendered) {
		this.isFormularioRendered = isFormularioRendered;
	}

	/**
	 * @return the isJustificativaRendered
	 */
	public Boolean getIsJustificativaRendered() {
		return isJustificativaRendered;
	}

	/**
	 * @param isJustificativaRendered
	 * the isJustificativaRendered to set
	 */
	public void setIsJustificativaRendered(Boolean isJustificativaRendered) {
		this.isJustificativaRendered = isJustificativaRendered;
	}

	/**
	 * @return the isConfirmarEnquadramento
	 */
	public Boolean getIsConfirmarEnquadramento() {
		return isConfirmarEnquadramento;
	}

	/**
	 * @param isConfirmarEnquadramento
	 * the isConfirmarEnquadramento to set
	 */
	public void setIsConfirmarEnquadramento(Boolean isConfirmarEnquadramento) {
		this.isConfirmarEnquadramento = isConfirmarEnquadramento;
	}

	/**
	 * @return the confirmarEnquadramento
	 */
	public Boolean getConfirmarEnquadramento() {
		return confirmarEnquadramento;
	}

	/**
	 * @param confirmarEnquadramento
	 * the confirmarEnquadramento to set
	 */
	public void setConfirmarEnquadramento(Boolean confirmarEnquadramento) {
		this.confirmarEnquadramento = confirmarEnquadramento;
	}

	/**
	 * @return the confirmarEnquadramentoMsg
	 */
	public String getConfirmarEnquadramentoMsg() {
		return confirmarEnquadramentoMsg;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	/**
	 * @param confirmarEnquadramentoMsg
	 * the confirmarEnquadramentoMsg to set
	 */
	public void setConfirmarEnquadramentoMsg(String confirmarEnquadramentoMsg) {
		this.confirmarEnquadramentoMsg = confirmarEnquadramentoMsg;
	}

	/**
	 * @return the listAtoAmbiental
	 */
	public List<SelectItem> getListAtoAmbiental() {
		return listAtoAmbiental;
	}

	/**
	 * @param listAtoAmbiental
	 * the listAtoAmbiental to set
	 */
	public void setListAtoAmbiental(List<SelectItem> listAtoAmbiental) {
		this.listAtoAmbiental = listAtoAmbiental;
	}

	/**
	 * @return the selectedAtoAmbiental
	 */
	public List<SelectItem> getSelectedAtoAmbiental() {
		return selectedAtoAmbiental;
	}

	/**
	 * @param selectedAtoAmbiental
	 * the selectedAtoAmbiental to set
	 */
	public void setSelectedAtoAmbiental(List<SelectItem> selectedAtoAmbiental) {
		this.selectedAtoAmbiental = selectedAtoAmbiental;
	}

	/**
	 * @return the formularios
	 */
	public List<SelectItem> getFormularios() {
		return formularios;
	}

	/**
	 * @param formularios
	 * the formularios to set
	 */
	public void setFormularios(List<SelectItem> formularios) {
		this.formularios = formularios;
	}

	/**
	 * @return the selectedFormulario
	 */
	public List<SelectItem> getSelectedFormulario() {
		return selectedFormulario;
	}

	/**
	 * @param selectedFormulario
	 * the selectedFormulario to set
	 */
	public void setSelectedFormulario(List<SelectItem> selectedFormulario) {
		this.selectedFormulario = selectedFormulario;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 * the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the enquadramento
	 */
	public Enquadramento getEnquadramento() {
		return enquadramento;
	}

	/**
	 * @param enquadramento
	 * the enquadramento to set
	 */
	public void setEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = enquadramento;
	}

	/**
	 * @return the nomePessoaRequerimento
	 */
	public String getNomePessoaRequerimento() {
		return nomePessoaRequerimento;
	}

	/**
	 * @param nomePessoaRequerimento
	 * the nomePessoaRequerimento to set
	 */
	public void setNomePessoaRequerimento(String nomePessoaRequerimento) {
		this.nomePessoaRequerimento = nomePessoaRequerimento;
	}

	/**
	 * @return the numerCpfCnpj
	 */
	public String getNumerCpfCnpj() {
		return numerCpfCnpj;
	}

	/**
	 * @param numerCpfCnpj
	 * the numerCpfCnpj to set
	 */
	public void setNumerCpfCnpj(String numerCpfCnpj) {
		this.numerCpfCnpj = numerCpfCnpj;
	}

	/**
	 * @return the nomeEmpreendimento
	 */
	public String getNomeEmpreendimento() {
		return nomeEmpreendimento;
	}

	/**
	 * @param nomeEmpreendimento
	 * the nomeEmpreendimento to set
	 */
	public void setNomeEmpreendimento(String nomeEmpreendimento) {
		this.nomeEmpreendimento = nomeEmpreendimento;
	}

	/**
	 * @return the isEmpreendimentoPassivel
	 */
	public Boolean getIsEmpreendimentoPassivel() {
		return isEmpreendimentoPassivel;
	}

	/**
	 * @param isEmpreendimentoPassivel
	 * the isEmpreendimentoPassivel to set
	 */
	public void setIsEmpreendimentoPassivel(Boolean isEmpreendimentoPassivel) {
		this.isEmpreendimentoPassivel = isEmpreendimentoPassivel;
	}

	/**
	 * @return the empreendimentoPassivel
	 */
	public String getEmpreendimentoPassivel() {
		return empreendimentoPassivel;
	}

	/**
	 * @param empreendimentoPassivel
	 * the empreendimentoPassivel to set
	 */
	public void setEmpreendimentoPassivel(String empreendimentoPassivel) {
		this.empreendimentoPassivel = empreendimentoPassivel;
	}

	/**
	 * @return the requerimentoUnico
	 */
	public RequerimentoUnico getRequerimentoUnico() {
		return requerimentoUnico;
	}

	/**
	 * @param requerimentoUnico
	 * the requerimentoUnico to set
	 */
	public void setRequerimentoUnico(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
		carregarTela(this.requerimentoUnico);
	}

	public List<RequerimentoUnicoDTO> getRequerimentosDTO() {
		return requerimentosDTO;
	}

	public void setRequerimentosDTO(List<RequerimentoUnicoDTO> requerimentosDTO) {
		this.requerimentosDTO = requerimentosDTO;
	}

	public String getDscJustificativa() {
		return dscJustificativa;
	}

	public void setDscJustificativa(String dscJustificativa) {
		this.dscJustificativa = dscJustificativa;
	}

	public List<DocumentoObrigatorio> getDocsObrigs() {
		return docsObrigs;
	}

	public void setDocsObrigs(List<DocumentoObrigatorio> docsObrigs) {
		this.docsObrigs = docsObrigs;
	}

	public List<AtoAmbiental> getAtosAmbi() {
		return atosAmbi;
	}

	public void setAtosAmbi(List<AtoAmbiental> atosAmbi) {
		this.atosAmbi = atosAmbi;
	}

	public List<AtoAmbiental> getListDocsAtoAmbiental() {
		return listDocsAtoAmbiental;
	}

	public void setListDocsAtoAmbiental(List<AtoAmbiental> listDocsAtoAmbiental) {
		this.listDocsAtoAmbiental = listDocsAtoAmbiental;
	}

	public Boolean getIsConfirmaActionRendered() {
		return isConfirmaActionRendered;
	}

	public void setIsConfirmaActionRendered(Boolean isConfirmaActionRendered) {
		this.isConfirmaActionRendered = isConfirmaActionRendered;
	}

	/**
	 * @return the listaDeDocsObrigSelecionadosNoAto
	 */
	public List<DocumentoAto> getListaDocumentoAto() {
		return listaDocumentoAto;
	}

	/**
	 * @param listaDocumentoAto the listaDeDocsObrigSelecionadosNoAto to set
	 */
	public void setListaDocumentoAto(List<DocumentoAto> listaDocumentoAto) {
		this.listaDocumentoAto	= listaDocumentoAto;	
	}
}
