package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.EfetuarEnquadramentoServiceFacade;
import br.gov.ba.seia.facade.FluxoAlternativoFacade;
import br.gov.ba.seia.facade.ProcessoReenquadramentoHistAtoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioReequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ReenquadramentoProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;


@Named("enquadramentoController")
@ViewScoped
public class EnquadramentoController {

	private static final String TIPO_AUTO_OUTORGA = "OUTORGA";
	
	private Collection<TipoAto> tiposAto;

	private Collection<AtoAmbiental> atosAmbientais;

	private Collection<Tipologia> tipologias;

	private Collection<DocumentoAto> documentosAtos;
	
	private Collection<TipoFinalidadeUsoAgua> finalidades;

	private Enquadramento enquadramento;

	private Boolean outrosAtos;

	private boolean isPassivelOutorga;

	private EnquadramentoAtoAmbiental enquadramentoAtoAmbiental;
	
	private EnquadramentoAtoAmbiental enquadramentoAtoAmbientalARemover;
	
	private Requerimento requerimento;
	
	private boolean adicionandoDocumento;

	private boolean  dla;
	
	private boolean enquadramentoJaIniciado;
	
	private String step;
	
	private TramitacaoRequerimento tramitacaoRequerimento;
	
	private boolean concluirSemAlterar;
	
	@EJB
	private AtoAmbientalService ambientalService;
	
	@EJB
	private EnquadramentoService enquadramentoService;

	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private TipoAtoService tipoAtoService;

	@EJB
	private AtoAmbientalService atoAmbientalService;

	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;

	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	@EJB
	private PessoaService pessoaService;

	@EJB
	private StatusRequerimentoService statusRequerimentoService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	
	@EJB
	private EfetuarEnquadramentoServiceFacade efetuarEnquadramentoServiceFacade;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private FluxoAlternativoFacade fluxoAlternativoFacade;
	
	@EJB
	private DocumentoObrigatorioReequerimentoService documentoObrigatorioReequerimentoService;
	
	@EJB
	private ProcessoReenquadramentoHistAtoServiceFacade processoReenquadramentoHistAtoServiceFacade;
	
	boolean isTla;
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO; 
	
	private String cameFrom = StringUtils.EMPTY;
	
	private Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection;
	
	private EnquadramentoAtoAmbiental enquadramentoAtoAmbientalSelecionado;
	
	private Collection<DocumentoObrigatorioRequerimento> listaDocumentoObrigatorioReenquadramento;
	
	
	
	
	@PostConstruct
	public void init() {
		this.step = "atos";
		this.limpar();
	}

	public void load(Requerimento requerimento) {
		try {

			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = this.requerimentoService.carregarDadosBasicos(ideRequerimento);

			this.limpar();
			EmpreendimentoRequerimento empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);

			this.verificarPorte(empreendimentoRequerimento);
			this.verificarDispensaLicencaAmbiental(empreendimentoRequerimento);

			this.vincularOperador();

			this.isPassivelOutorga = this.outorgaLocalizacaoGeograficaService.isPassivelDispensaOutorga(ideRequerimento);

			this.enquadramento.setIdeRequerimento(requerimento);					
			
			isTla = solicitacaoAdministrativoService.existeSolicitacaoAdminstrativaByRequerimento(requerimento, TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);
			
			if(isTla){	
				tiposAto = this.tipoAtoService.listarTiposAtoJuridicos();					
			}
			else{
				
				if (isReenquadramento()) {
					tiposAto = tipoAtoService.listarTiposAtoComAtoAmbiental();

					for (TipoAto item : tiposAto) {
						if (TIPO_AUTO_OUTORGA.equalsIgnoreCase(item.getNomTipoAto())) {
							tiposAto.remove(item);

							break;
						}
					}
				
			}else {
					
					tiposAto= this.tipoAtoService.listarTiposAtoNaoJuridicos(empreendimentoRequerimento.getIndDla());

			
			}	
				}
			
			try{
				this.iniciarEnquadramento(requerimento);
				this.enquadramentoJaIniciado = false;
			}
			catch(SeiaTramitacaoRequerimentoRuntimeException e) {
				this.enquadramentoJaIniciado = true;
				JsfUtil.addWarnMessage(e.getMessage());
			}
			
			tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(ideRequerimento);
			
		} 
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void loadReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO){
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
		load(processoReenquadramentoDTO.getRequerimento());
		carregarEnquadramentro(processoReenquadramentoDTO);
	}
	
	private void removerEnquadramentoAtoAmbientalAlterado() throws Exception{
		if (isReenquadramento()){
			
			ProcessoReenquadramento processoReenquadramento = enquadramentoService.obterPenultimoProcessoReenquadramentoPorProcesso(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso());
			
			if (!Util.isNull(processoReenquadramento)){
				Iterator<EnquadramentoAtoAmbiental> itr = this.enquadramento.getEnquadramentoAtoAmbientalCollection().iterator();
				while(itr.hasNext()){
					EnquadramentoAtoAmbiental eaa = itr.next();
					
					ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = processoReenquadramentoHistAtoServiceFacade.obterProReenHistAtoAlteradoPorProcessoReenquadramento(processoReenquadramento, eaa.getAtoAmbiental(), eaa.getTipologia());
					
					if (!Util.isNull(processoReenquadramentoHistAto)){
						itr.remove();
					}
				}
			}
		}
	}

	public void carregarEnquadramentro(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		try {
			enquadramento = enquadramentoService.carregarEnquadramentoBy(processoReenquadramentoDTO.getRequerimento());
			enquadramento.setProcessoReenquadramentoDTO(processoReenquadramentoDTO);
			removerEnquadramentoAtoAmbientalAlterado();
			
			Collection<EnquadramentoDocumentoAto> enqDocumentoAtoCollection = enquadramentoService.listarAtosEnquadradosByRequerimento(enquadramento.getIdeRequerimento().getIdeRequerimento());
			for (EnquadramentoAtoAmbiental eaa : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
				carregarDocs(eaa);
				if (!Util.isNull(eaa.getListaDocumentosAtos())){
					for (DocumentoAto documentoAto : eaa.getListaDocumentosAtos()) {
						for (EnquadramentoDocumentoAto enquadramentoDocumentoAto : enqDocumentoAtoCollection) {
							if(enquadramentoDocumentoAto.getDocumentoAto().equals(documentoAto)){
								documentoAto.setChecked(true);
								documentoAto.setEnquadramentoAtoAmbiental(this.enquadramentoAtoAmbiental);
							}
						}
					}
				}
			}
			setEnquadramentoAtoAmbientalCollection(enquadramento.getEnquadramentoAtoAmbientalCollection());
			voltar();
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public MetodoUtil getMetodoSelecionarEnquadramentoAtoAmbiental() {
		return new MetodoUtil(this, "selecionarEnquadramentoAtoAmbiental", EnquadramentoAtoAmbiental.class);
	}
	
	public void selecionarEnquadramentoAtoAmbiental(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental){
		this.enquadramentoAtoAmbientalSelecionado = enquadramentoAtoAmbiental;
	}
	
	public MetodoUtil getMetodoSelecionarAtoAmbiental() {
		return new MetodoUtil(this, "selecionarAtoAmbiental", AtoAmbiental.class);
	}
	
	public void selecionarAtoAmbiental(AtoAmbiental atoAmbiental) {
		enquadramentoService.selecionarAtoAmbiental(this, atoAmbiental);
		Html.atualizar("enquadramento:lstAtosCadastrados");
	}
	
	private void limpar() {
		this.enquadramento = new Enquadramento();
		this.enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental();
		this.enquadramentoAtoAmbiental.setAtoAmbiental(new AtoAmbiental());
		this.tiposAto = new ArrayList<TipoAto>();
		this.atosAmbientais = new ArrayList<AtoAmbiental>();
		this.tipologias = new ArrayList<Tipologia>();
		this.enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());
		this.adicionandoDocumento = false;
		this.outrosAtos = null;
		this.enquadramentoAtoAmbientalCollection = null;
		this.enquadramentoAtoAmbientalSelecionado = null;
	}

	private void verificarPorte(EmpreendimentoRequerimento empreendimentoRequerimento) {
		if (!Util.isNull(empreendimentoRequerimento)) {
			Porte idePorte = empreendimentoRequerimento.getIdePorte();
			if(!Util.isNull(idePorte))
				this.enquadramento.setIndPassivelEiarima(idePorte.isNi());
		}
	}

	private void verificarDispensaLicencaAmbiental(EmpreendimentoRequerimento empreendimentoRequerimento) {

		if (!Util.isNull(empreendimentoRequerimento) && empreendimentoRequerimento.getIndDla() != null) {
			this.dla = empreendimentoRequerimento.getIndDla();
		} else {
			this.dla = false;
		}

		if (!this.dla) {
			this.outrosAtos = true;
		}

	}
	
	public void exibirTextoEmailReprovado() {
		if(enquadramento.getIndEnquadramentoAprovado()) {
			enquadramento.setDscJustificativa("");
		}
		else {
			StringBuilder texto = new StringBuilder();
			
			if (isReenquadramento()) {
				texto.append("Prezado(a),\n\n");
				texto.append("Não foi possível reenquadrar o Processo de nº "+processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getNumProcesso()+" "+"devido a seguinte justificativa:\n\n\n\n");
				texto.append("Para que o processo de reenquadramento seja concluído, é necessário que se resolvam as pendências solicitadas. \n\n");
				texto.append("Para tanto, acesse o SEIA, no menu lateral à esquerda acione a aba "+"\"PROCESSO"+"\", em seguida "+"\"REENQUADRAMENTO"+"\" e siga os seguintes passos:\n\n");
				texto.append(" Passo 1: Informe o número do processo mencionado acima no campo "+"\"Nº do processo:"+"\" e acione o botão "+"\"Consultar"+"\";\n\n");
				texto.append(" Passo 2: Na coluna "+"\"Ações"+"\" acione o ícone  "+"\"Editar requerimento"+"\";\n\n");
				texto.append(" Passo 3: Realize as alterações necessárias conforme orientação acima.\n\n");
				texto.append(" Passo 4: Acione o botão \"Finalizar requerimento\" e aguarde novas orientações.\n\n" );
				texto.append("Atenciosamente,\n");
				texto.append("Central de Atendimento/INEMA.");
			}
			else {
				texto.append("Prezado(a), Não foi possível enquadrar o Requerimento de nº "+ enquadramento.getIdeRequerimento().getNumRequerimento() + ".\n\n");
				texto.append("Segue justificativa:\n\n");
				texto.append("Para que o processo de requerimento seja concluído, é necessária que se resolvam as pendências. \n");
				texto.append("Para tal, entre no SEIA com seu login e senha, acesse a funcionalidade de consulta de Requerimentos (item de menu Requerimento, subitem Consultar)");
				texto.append(" e altere o requerimento indicado conforme justificativa acima.\n");
				texto.append("At.te,\n");
				texto.append("Central de Atendimento/INEMA.");
			}
			
			enquadramento.setDscJustificativa(texto.toString());
		}
	}

	public void iniciarEnquadramento(Requerimento requerimento) throws Exception {
		Pessoa pessoa = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
		if(!isReenquadramento()){
			this.enquadramentoService.iniciarEnquadramento(requerimento, pessoa);
		}
	}

	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}
	
	public boolean isRequerimentoUnico() {
		if (!Util.isNull(processoReenquadramentoDTO) && !Util.isNull(processoReenquadramentoDTO.getRequerimento())){
			Requerimento req = requerimentoService.buscarEntidadeCarregadaPorId(processoReenquadramentoDTO.getRequerimento());
			
			if(!Util.isNull(req.getRequerimentoUnico())){
				return true;
			}
		}
		return false;
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}
	
	public void listarAtos() {
		
		TipoAto tipoAto = enquadramentoAtoAmbiental.getAtoAmbiental().getIdeTipoAto();
		
		if (Util.isNull(tipoAto)) {
			atosAmbientais = new ArrayList<AtoAmbiental>();
		}
		else if (isReenquadramento()) {	
			atosAmbientais = atoAmbientalService.listarAtoAmbientalPorTipoAtoParaReenquadramento(tipoAto.getIdeTipoAto(), isTla);
		}
		else {
			atosAmbientais = atoAmbientalService.listarAtoAmbientalPorTipoAtoAtivo(tipoAto.getIdeTipoAto(), isTla);
		}	
		
		finalidades = new ArrayList<TipoFinalidadeUsoAgua>();
		tipologias = new ArrayList<Tipologia>();
		
	}

	public void listarTipologias() {
		enquadramentoAtoAmbiental.setTipologia(null);
		this.finalidades = new ArrayList<TipoFinalidadeUsoAgua>();
		AtoAmbiental atoAmbiental = this.enquadramentoAtoAmbiental.getAtoAmbiental();
		this.tipologias = this.tipologiaService.listarByAto(atoAmbiental);
	}
	
	@Deprecated
	public void listarFinalidades() {
		finalidades = tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologiaAndAto(enquadramentoAtoAmbiental);
	}
	
	public void concluir() {
		try{
			efetuarEnquadramentoServiceFacade.efetuarEnquadramento(this);			
		}
		catch (SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			if(e.getMessage().contains("Erro: Já existe Enquadramento para o Requerimento")) {
				JsfUtil.addWarnMessage("Já existe Enquadramento para o Requerimento.");
			}
			else{
				Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi adicionado nenhum ato ambiental. Adicione ao menos um ato ambiental para concluir o enquadramento.");
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void concluirSemAlterar(){
		setConcluirSemAlterar(true);
		concluir();
	}
	
	public void finalizarRequerimentoDLA() {
		try {
			this.requerimentoService.finalizarDLA(this.requerimento, this.getOperador());
			ContextoUtil.getContexto().setObject("Requerimento finalizado com sucesso!");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/novo-requerimento/consulta.xhtml");
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param enquadramentoAtoAmbiental
	 */
	public boolean hasFormalarioNaLista(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
			if (documentoAto.getIdeDocumentoObrigatorio().getIndFormulario()) {
				return true;
			}
		}
		return false;
	}

	private void vincularOperador() {
		Pessoa pessoa = this.getOperador();
		this.enquadramento.setIdePessoa(pessoa);
	}

	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return this.pessoaService.carregarGet(usuario.getIdePessoaFisica());
	}

	public void manipularArquvio(FileUploadEvent event) {
		
		Exception erro = null;
		try {
			String caminhoArquivo = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
			this.enquadramento.setDscCaminhoArquivoRima(caminhoArquivo);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro =e ;
			JsfUtil.addErrorMessage("Erro ao enviar arquivo.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public StreamedContent getFileDownload() {
		StreamedContent file = null;
		
		Exception erro = null;
		try {
			String caminhoArquivo = this.enquadramento.getDscCaminhoArquivoRima().trim();
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo
					.lastIndexOf(File.separator) + 1));
		} catch (FileNotFoundException e) {
			erro = e;
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return file;
	}

	public void excluirArquivoEIA() {
		FileUploadUtil.deletarArquivo(this.enquadramento.getDscCaminhoArquivoRima());
		this.enquadramento.setDscCaminhoArquivoRima(null);
	}

	public void adicionar() {

		
		if(isPossivelAdicionar(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental())){

			try {

				if (!this.validarAtoTipologia()) {
					return;
				} else {
					this.enquadramentoAtoAmbiental.setEnquadramento(this.enquadramento);

					Tipologia tipologia = null;
					Tipologia ideTipologia = enquadramentoAtoAmbiental.getTipologia();
					
					if (!Util.isNullOuVazio(ideTipologia)) {
						tipologia = this.tipologiaService.carregarTipologiaPorIde(ideTipologia.getIdeTipologia());
					}
					
					EnquadramentoAtoAmbiental ea = new EnquadramentoAtoAmbiental(enquadramento, this.enquadramentoAtoAmbiental.getAtoAmbiental(), tipologia);
					this.enquadramento.getEnquadramentoAtoAmbientalCollection().add(ea);
					setEnquadramentoAtoAmbientalCollection(this.enquadramento.getEnquadramentoAtoAmbientalCollection());
					
					if(isReenquadramento() && !Util.isNullOuVazio(enquadramentoAtoAmbiental.getAtoAmbiental())){
							Collection<DocumentoObrigatorioRequerimento> listaDocumentos = 
									this.documentoObrigatorioRequerimentoService.buscarDocumentosObrigatoriosByAto(
											processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getIdeRequerimento().getIdeRequerimento(), 
											enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental(),
											enquadramentoAtoAmbiental.getAtoAmbiental().getTipologia());
							if(!Util.isNullOuVazio(listaDocumentos)){
									listaDocumentoObrigatorioReenquadramento = listaDocumentos;
							}
						}
					
					
					this.enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental();
					atosAmbientais = null;
					tipologias = null;
					RequestContext.getCurrentInstance().addPartialUpdateTarget("enquadramento:aprovado");
				}
			} catch (Exception e) {
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		
		}
	}

	
	private boolean isPossivelAdicionar(Integer ideAto){
		
		boolean atoExigeTipologia = true;
		boolean retorno           = false;
		
		try{
			atoExigeTipologia = atoAmbientalService.isAtoExigeTipologia(ideAto);	
		}		
		catch(Exception e){
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
		
		if ((atoExigeTipologia && Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia()))){		
				JsfUtil.addErrorMessage("Para esse Ato Ambiental é obrigatoria a escolha de uma Tipologia");
		}else{
			retorno = true;
		}
		
		
		
		return retorno;
	}
	
	private boolean verificarAtoJaAdd(){
		for (EnquadramentoAtoAmbiental eaa : getEnquadramentoAtoAmbientalCollection()) {
			if (!Util.isNullOuVazio(eaa.getTipologia())){
				if (eaa.getAtoAmbiental().equals(this.enquadramentoAtoAmbiental.getAtoAmbiental()) &&
						eaa.getTipologia().equals(this.enquadramentoAtoAmbiental.getTipologia())){
					JsfUtil.addErrorMessage("O tipologia já foi cadastrado.");
					return false;
				}
			} else {
				if (eaa.getAtoAmbiental().equals(this.enquadramentoAtoAmbiental.getAtoAmbiental())){
					JsfUtil.addErrorMessage("O Ato Ambiental já foi cadastrado.");
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean validarAtoTipologia() {
		Collection<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = getEnquadramentoAtoAmbientalCollection();
		if (Util.isNullOuVazio(listaEnquadramentoAtoAmbiental)){
			setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());
		}
		
		
		boolean valido = verificarAtoJaAdd();

		enquadramento.setEnquadramentoAtoAmbientalCollection(getEnquadramentoAtoAmbientalCollection());
		
		if (enquadramento.getEnquadramentoAtoAmbientalCollection().contains(this.enquadramentoAtoAmbiental)) {
			JsfUtil.addErrorMessage("O Ato Ambiental já foi cadastrado.");
			valido = false;
		}

		if (Util.isNullOuVazio(this.enquadramentoAtoAmbiental.getAtoAmbiental())) {
			JsfUtil.addErrorMessage("O campo Ato Ambiental é de preenchimento obrigatório");
			valido = false;
			return valido;
		}
		
		if (Util.isNull(this.enquadramentoAtoAmbiental.getAtoAmbiental().getIdeTipoAto())) {
			JsfUtil.addErrorMessage("O campo Categoria é de preenchimento obrigatório");
			valido = false;
		}

		if (this.enquadramentoAtoAmbiental.getAtoAmbiental().isOutorga()
				&& Util.isNull(this.enquadramentoAtoAmbiental.getTipologia())) {
			JsfUtil.addErrorMessage("O campo Tipologia é de preenchimento obrigatório");
			valido = false;
		}

		return valido;
	}

	public void excluirAto() {
		this.enquadramento.getEnquadramentoAtoAmbientalCollection().remove(this.enquadramentoAtoAmbientalARemover);
		getEnquadramentoAtoAmbientalCollection().remove(this.enquadramentoAtoAmbientalARemover);
	}

	public void carregarDocs(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {

		Exception erro = null;
		
		try {
			this.adicionandoDocumento = true;

			if (Util.isNullOuVazio(enquadramentoAtoAmbiental.getListaDocumentosAtos())) {
				AtoAmbiental atoAmbiental = enquadramentoAtoAmbiental.getAtoAmbiental();
				Tipologia tipologia = enquadramentoAtoAmbiental.getTipologia();
				
				Collection<Integer> listTipologiasDoReq = requerimentoTipologiaService.buscarIdesTipologiasLicenca(getRequerimento());
				
				if(!Util.isNullOuVazio(tipologia)){
					if(Util.isNullOuVazio(listTipologiasDoReq)){
						listTipologiasDoReq = new ArrayList<Integer>();						
					}
					listTipologiasDoReq.add(tipologia.getIdeTipologia());
				}
					
				List<DocumentoAto> listaDocumentosAtos = (List<DocumentoAto>) this.documentoObrigatorioService.listarDocumentosObrigatoriosByAtoAndTipologia(atoAmbiental, listTipologiasDoReq);
				
				
				List<DocumentoObrigatorioRequerimento> listaDocObgInterface = (List<DocumentoObrigatorioRequerimento>) this.documentoObrigatorioRequerimentoService.buscarDocumentosObrigatoriosRequerimentoEnquadramentoByIdeRequerimento(getRequerimento().getIdeRequerimento());
				
				if(listaDocObgInterface != null && !listaDocObgInterface.isEmpty()){
					for (DocumentoObrigatorioInterface itemDocObgInterface : listaDocObgInterface) {
						
						if(atoAmbiental.getIdeAtoAmbiental().equals(itemDocObgInterface.getIdeDocumentoAto().getIdeAtoAmbiental().getIdeAtoAmbiental())){
							listaDocumentosAtos.add(itemDocObgInterface.getIdeDocumentoAto());
						}
					}
				}
				
				if (isReenquadramento()){
					List<DocumentoObrigatorioReenquadramento> listaDocObgInterfaceReenq =  this.documentoObrigatorioReequerimentoService.buscarDocumentosObrigatoriosReenquadramentoPorProcessoReenquadramento(
							 processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getIdeProcesso(), processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(), enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental());
					
					if(listaDocObgInterfaceReenq != null && !listaDocObgInterfaceReenq.isEmpty()){
						for (DocumentoObrigatorioInterface itemDocObgInterface : listaDocObgInterfaceReenq) {
							DocumentoAto documentoAto = itemDocObgInterface.getIdeDocumentoAto();
							
							documentoAto.setChecked(true);
							listaDocumentosAtos.add(documentoAto);
						}
					}
				}
			
				enquadramentoAtoAmbiental.setListaDocumentosAtos(listaDocumentosAtos);
				
				if (Util.isNullOuVazio(enquadramentoAtoAmbiental.getProcessoReenquadramentoHistAtoTransient()) && isReenquadramento()){
					ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = new ProcessoReenquadramentoHistAto();
					processoReenquadramentoHistAto.setIdeProcessoReenquadramento(getProcessoReenquadramentoDTO().getProcessoReenquadramento());
					processoReenquadramentoHistAto.setDtcReenquadramento(new Date());
					processoReenquadramentoHistAto.setIdeAtoAmbientalOriginal(null);
					processoReenquadramentoHistAto.setIdeTipologiaOriginal(null);
					processoReenquadramentoHistAto.setIdeEnquadramentoAtoAmbientalReenquadrado(enquadramentoAtoAmbiental);
					
					enquadramentoAtoAmbiental.setProcessoReenquadramentoHistAtoTransient(processoReenquadramentoHistAto);
				}
				
				enquadramentoAtoAmbiental.setAlteracao(isAlteracaoTipoAto(enquadramentoAtoAmbiental));
			}

			this.enquadramentoAtoAmbiental = enquadramentoAtoAmbiental;

			this.step = "docs";

		} catch (Exception e) {
			erro = e;
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void armazenarDocAtoReenquadramentoSelecionado(DocumentoAto documentoAto){
		if(documentoAto.isCheckedReenquadramento()){
			documentoAto.setReenquadramentoAtoAmbiental(this.enquadramentoAtoAmbiental);
		} 
		else {
			documentoAto.setReenquadramentoAtoAmbiental(null);
		}
	}
	
	public void armazenarDocAtoSelecionado(DocumentoAto documentoAto){
		if(documentoAto.isChecked()){
			documentoAto.setEnquadramentoAtoAmbiental(this.enquadramentoAtoAmbiental);	
		} else {
			documentoAto.setEnquadramentoAtoAmbiental(null);	
		}
	}
	
	public void voltar() {
		this.adicionandoDocumento = false;
		this.step = "atos";
		this.enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental();
	}
	
	public String countDocsAtos(Collection<DocumentoAto> lista) {
		Integer numDocs = 0;
		if(!Util.isNullOuVazio(lista)){
			for (DocumentoAto documentoAto : lista) {
				if(documentoAto.isChecked()){
					numDocs = numDocs + 1;
				} 
			}
			return numDocs.toString();
		}else {
			return StringUtils.EMPTY;
		}
	}
	
	public String countDocsAtosReenquadramento(Collection<DocumentoAto> lista) {
		Integer numDocs = 0;
		if (!Util.isNullOuVazio(lista)) {
			for (DocumentoAto documentoAto : lista) {
				if (documentoAto.isCheckedReenquadramento()) {
					numDocs = numDocs + 1;
				}
			}
			return numDocs.toString();
		} else {
			return StringUtils.EMPTY;
		}
	}

	public boolean isAlteracaoTipoAto(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) throws Exception{
		if (!Util.isNullOuVazio(processoReenquadramentoDTO)){
			ReenquadramentoProcessoAto reenquadramentoProcessoAto = enquadramentoService.isAlteracaoTipoAto(enquadramentoAtoAmbiental, processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getIdeProcesso());
			return !Util.isNullOuVazio(reenquadramentoProcessoAto);
		}
		return false;
	}

	public boolean isSomenteDLA() {
		return this.dla && !Util.isNull(this.outrosAtos) && !outrosAtos;
	}
	
	public boolean isRenderedPnlAtoTipologia() {
		if(isEnquadramentoAprovado()) {
			return isReenquadramento() || Boolean.TRUE.equals(outrosAtos);
		}
		return false;		
	}

	public boolean isEnquadramentoAprovado() {
		return !Util.isNull(enquadramento) && Boolean.TRUE.equals(enquadramento.getIndEnquadramentoAprovado());
	}

	public Collection<TipoAto> getTiposAto() {
		return tiposAto;
	}

	public void setTiposAto(Collection<TipoAto> tiposAto) {
		this.tiposAto = tiposAto;
	}

	public Collection<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public Collection<Tipologia> getTipologias() {
		return tipologias;
	}

	public void setTipologias(Collection<Tipologia> tipologias) {
		this.tipologias = tipologias;
	}

	public Enquadramento getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = enquadramento;
	}

	public Boolean getOutrosAtos() {
		return outrosAtos;
	}

	public void setOutrosAtos(Boolean outrosAtos) {
		this.outrosAtos = outrosAtos;
	}

	public EnquadramentoAtoAmbiental getEnquadramentoAtoAmbiental() {
		return enquadramentoAtoAmbiental;
	}

	public void setEnquadramentoAtoAmbiental(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		this.enquadramentoAtoAmbiental = enquadramentoAtoAmbiental;
	}

	public boolean isAdicionandoDocumento() {
		return adicionandoDocumento;
	}

	public void setAdicionandoDocumento(boolean adicionandoDocumento) {
		this.adicionandoDocumento = adicionandoDocumento;
	}

	public Collection<DocumentoAto> getDocumentosAtos() {
		return documentosAtos;
	}

	public void setDocumentosAtos(Collection<DocumentoAto> documentosAtos) {
		this.documentosAtos = documentosAtos;
	}

	public boolean isDla() {
		return dla;
	}

	public void setDla(boolean dla) {
		this.dla = dla;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public EnquadramentoAtoAmbiental getEnquadramentoAtoAmbientalARemover() {
		return enquadramentoAtoAmbientalARemover;
	}

	public void setEnquadramentoAtoAmbientalARemover(EnquadramentoAtoAmbiental enquadramentoAtoAmbientalARemover) {
		this.enquadramentoAtoAmbientalARemover = enquadramentoAtoAmbientalARemover;
	}

	public boolean isEnquadramentoJaIniciado() {
		return enquadramentoJaIniciado;
	}

	public void setEnquadramentoJaIniciado(boolean enquadramentoJaIniciado) {
		this.enquadramentoJaIniciado = enquadramentoJaIniciado;
	}

	public Boolean getExibirAtosCadastados() {
		return !Util.isNullOuVazio(this.enquadramento.getEnquadramentoAtoAmbientalCollection()) || !Util.isNullOuVazio(getEnquadramentoAtoAmbientalCollection());
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public boolean isPassivelOutorga() {
		return isPassivelOutorga;
	}

	public void setPassivelOutorga(boolean isPassivelOutorga) {
		this.isPassivelOutorga = isPassivelOutorga;
	}

	public Collection<TipoFinalidadeUsoAgua> getFinalidades() {
		return finalidades;
	}

	public void setFinalidades(Collection<TipoFinalidadeUsoAgua> finalidades) {
		this.finalidades = finalidades;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(
			TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

	public String getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(String cameFrom) {
		this.cameFrom = cameFrom;
	}

	public Collection<EnquadramentoAtoAmbiental> getEnquadramentoAtoAmbientalCollection() {
		return enquadramentoAtoAmbientalCollection;
	}

	public void setEnquadramentoAtoAmbientalCollection(
			Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection) {
		this.enquadramentoAtoAmbientalCollection = enquadramentoAtoAmbientalCollection;
	}

	public EnquadramentoAtoAmbiental getEnquadramentoAtoAmbientalSelecionado() {
		return enquadramentoAtoAmbientalSelecionado;
	}

	public void setEnquadramentoAtoAmbientalSelecionado(
			EnquadramentoAtoAmbiental enquadramentoAtoAmbientalSelecionado) {
		this.enquadramentoAtoAmbientalSelecionado = enquadramentoAtoAmbientalSelecionado;
	}

	public boolean isConcluirSemAlterar() {
		return concluirSemAlterar;
	}

	public void setConcluirSemAlterar(boolean concluirSemAlterar) {
		this.concluirSemAlterar = concluirSemAlterar;
	}

	public Collection<DocumentoObrigatorioRequerimento> getListaDocumentoObrigatorioReenquadramento() {
		return listaDocumentoObrigatorioReenquadramento;
	}

	public void setListaDocumentoAtosReenquadramento(
		Collection<DocumentoObrigatorioRequerimento> listaDocumentoObrigatorioReenquadramento) {
		this.listaDocumentoObrigatorioReenquadramento = listaDocumentoObrigatorioReenquadramento;
	}

}
