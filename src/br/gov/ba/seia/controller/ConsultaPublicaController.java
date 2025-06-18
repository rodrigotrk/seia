package br.gov.ba.seia.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.gov.ba.seia.dto.ConsultaPublicaDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacTransporteProduto;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.facade.LacPostoServiceFacade;
import br.gov.ba.seia.facade.ProcessoRequerimentoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ConsultaPublicaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LacService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.ReportUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.validators.CnpjValidator;
import br.gov.ba.seia.validators.CpfValidator;


@Named("consultaPublicaController")
@ViewScoped
public class ConsultaPublicaController {

	@EJB
	private ProcessoService processoService;

	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ConsultaPublicaService consultaPublicaService;
	
	@EJB
	private LacService lacService;
	
	@EJB
	private LacPostoServiceFacade lacPostoServiceFacade;
	
	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;
	
	@EJB
	private LacTransporteService lacTransporteService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratoriosFacade;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private FormarEquipeServiceFacade formarEquipeServiceFacade;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	private LazyDataModel<ConsultaPublicaDTO> dataModelConsultaPublica;

	private VwConsultaProcesso vwProcesso;
	
	private Processo processo;
	private String nome;
	private String numDocumento;
	private String numeroProcesso;
	private String desTipologiaAto;
	private String nomEmpreendimento;
	
	private List<Municipio> listaMunicipios;
	private Municipio municipioSelecionado;
	
	private final String ARQUIVO_MARCA_DAGUA = "/resources/imagens/visualizacao.png";
	private final String NOVO_NOME = "(somenteVisualizacao).";
	private final String PONTO  = ".";
	private final String BARRA  = "/";
	
	private Boolean showProcessos;
	
	public ConsultaPublicaController() {}

	@PostConstruct
	public void init() {
		this.showProcessos = false;
		carregarMunicipios();
	}

	public void consultar() throws Exception {
		if (validarFiltros()) {
			carregarPaginacao();
		}
	}
	
	public void limparTela() {
		nome = null;
		numeroProcesso = null;
		numDocumento = null;
		municipioSelecionado = null;
		nomEmpreendimento = null;
		dataModelConsultaPublica = null;
		showProcessos = false;
	}

	private boolean validarFiltros() {
		try{			
			if (!existeFiltro()) {
				throw new Exception("Informe um filtro para realizar a consulta.");
			}
			
			if(!verificarNumeroValidoDeCaracteresDoFiltro()){
				throw new Exception("O filtro informado deve conter o mínimo de 5 caracteres.");										
			}

			if (!Util.isNullOuVazio(numDocumento)) {
				boolean documentoValido = CpfValidator.validaCPF(numDocumento) || CnpjValidator.validarCNPJ(numDocumento);
				if (!documentoValido) {
					throw new Exception("O campo CPF/CNPJ está inválido.");					
				}
			}
			return true;
		}
		catch(Exception e){
			showProcessos = false;
			JsfUtil.addErrorMessage(e.getMessage());			
			return false;		
		}
	}

	private void carregarPaginacao() {
		dataModelConsultaPublica = new LazyDataModel<ConsultaPublicaDTO>() {

			private static final long serialVersionUID = -9150943634752597868L;

			@Override
			public List<ConsultaPublicaDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<ConsultaPublicaDTO> lConsultaPublica = null;
				
				try {
					setPageSize(pageSize);
					lConsultaPublica = listarConsultaPublica(first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage("Ocorreu um erro inesperado, favor entrar em contato com a administração do sistema.");
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
				
				return lConsultaPublica;
			}

		};
		
		dataModelConsultaPublica.setRowCount(getRowCountProcesso());
	}
	
	private int getRowCountProcesso() {
		int totalRowCount = 0;
		
		Exception erro = null;
		try {
			totalRowCount = consultaPublicaService.countListarFiltrandoPorParametros(carregarParametros());			
			if (totalRowCount != 0) {
				this.showProcessos = true;
			} else {
				this.showProcessos = false;
				JsfUtil.addSuccessMessage("Não Foram encontrados Registros.");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return totalRowCount;
	}
	
	private boolean existeFiltro() {		
		return !Util.isNullOuVazio(numeroProcesso) || 
			   !Util.isNullOuVazio(numDocumento) || 
			   !Util.isNullOuVazio(nome) || 
			   !Util.isNullOuVazio(municipioSelecionado) || 
			   !Util.isNullOuVazio(nomEmpreendimento);
	}
	
	private boolean verificarNumeroValidoDeCaracteresDoFiltro() {
		return !Util.isNullOuVazio(numeroProcesso) 
			|| !Util.isNullOuVazio(numDocumento)
			|| (!Util.isNullOuVazio(nome) && nome.length() >= 5) 
			|| (!Util.isNullOuVazio(nomEmpreendimento) &&  nomEmpreendimento.length() >= 5)
			|| !Util.isNullOuVazio(municipioSelecionado);
	}
	
	private List<ConsultaPublicaDTO> listarConsultaPublica(int first, int pageSize) {
		
		try{
			Map<String, Object> params = carregarParametros();
			return consultaPublicaService.listarFiltrandoPorParametros(first, pageSize, params);			
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	private Map<String, Object> carregarParametros() {
		Map<String,Object> params = new HashMap<String, Object>();
		
		if(nome != null && !nome.equals("")){
			params.put("nomPessoaOuNomRazaoSocialOunomeFantasia", "%"+nome+"%");			
		}
		
		if(numeroProcesso != null && !numeroProcesso.equals("")){
			params.put("numeroProcesso", "%"+numeroProcesso.trim()+"%");
		}
		
		if(numDocumento != null && !numDocumento.equals("")){
			params.put("numCpfOuNumCnpj", numDocumento);
		}
		
		if(municipioSelecionado != null){
			params.put("municipio", municipioSelecionado.getIdeMunicipio());
		}
		
		if(nomEmpreendimento != null && !nomEmpreendimento.equals("")){
			params.put("nomEmpreendimento", "%"+nomEmpreendimento+"%");
		}
		
		return params;
	}	
	
	public void visualizarProcesso(ActionEvent event){
		
		Processo processo = (Processo) event.getComponent().getAttributes().get("ideProcesso");
		
		try {
			this.vwProcesso = this.vwConsultaProcessoService.getProcessoCompletoLAI(processo.getIdeProcesso());
			DetalharProcessoController detalharProcessoController = (DetalharProcessoController) SessaoUtil.recuperarManagedBean("#{detalharProcessoController}", DetalharProcessoController.class);
			
			this.vwProcesso.setListProcessoAto(detalharProcessoController.carregaProcessoAto(vwProcesso.getListProcessoAto()));
			this.vwProcesso.setListProcessoAtoReequadramento(detalharProcessoController.carregaProcessoAto(vwProcesso.getListProcessoAtoReequadramento()));
			
			Collection<ProcessoAto> listProcessoAto = formarEquipeServiceFacade.ajustarProcessoAto(vwProcesso.getListProcessoAto());
			this.vwProcesso.setListProcessoAto(listProcessoAto);
			
			detalharProcessoController.setVwProcesso(vwProcesso);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public Collection<AtoAmbiental> obterAtosProcesso(Processo processo) {
		
		Exception erro = null;
		
		try {
			return this.processoService.obterListaAtosAmbientais(processo.getIdeProcesso());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return null;
	}
	
	public Collection<Municipio> obterMunicipios(String municipio)  {
		return this.municipioService.obterMunicipiosBahiaByNome(municipio);
	}
	
	private void carregarMunicipios() {
		try {
			listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoLac(Requerimento requerimento) throws Exception {
		gerarCertificado(requerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	private void gerarCertificado(Requerimento requerimento) throws Exception {
		List<Certificado> certificados = this.certificadoService.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento()));
		for (AtoAmbiental atoAmbiental : requerimento.getAtosAmbientais()) {
			Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
			if (!certificados.contains(certificado)) {
				String numeroCertificado = this.certificadoService.gerarNumeroCertificado(certificado);
				certificado.setNumCertificado(numeroCertificado);
				this.certificadoService.salvar(certificado);
			}
		}
	}

	public StreamedContent gerarRfp(Requerimento requerimento) throws Exception {
		gerarCertificado(requerimento);
		return new ImpressoraAtoDeclaratorio().imprimirCertificado(requerimento.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_RFP);
	}	
	

	public StreamedContent gerarRcfp(Requerimento requerimento) throws Exception {
		gerarCertificado(requerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		return new RelatorioUtil("relatorio_rcfp.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public StreamedContent gerarDocumentoDeclaracaoPdf(Requerimento requerimento) {
		try {
			gerarCertificado(requerimento);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			RelatorioUtil lRelatorio = new RelatorioUtil("documentoDeclaracao.jasper", params, true);
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getGerarDetalhesNotificacaoPdf(Notificacao notificacaoSelecionada) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_notificacao", notificacaoSelecionada.getIdeNotificacao());
			
			if(notificacaoSelecionada.getTipo()==1){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);				
			}
			else if(notificacaoSelecionada.getTipo()==2){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacaoComunicacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);	
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}
	
	public StreamedContent getArquivoProcessoSC(ArquivoProcesso arquivoProcesso) {
		try {			
			return gerenciaArquivoService.getContentFile(arquivoProcesso.getDscCaminhoArquivo());
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return null;
	}
	
	public boolean isAtoDeclaratorio(Processo processo) {
		try{
			if(processo == null) {
				throw new Exception("Não existe processo.");
			}
			
			ControleTramitacao ultimaTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(processo);
			if(!ultimaTramitacao.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus())) {
				throw new Exception("O processo não está concluído.");								
			}
			
			boolean existeAtoDeclaratorio = false;
			for(AtoAmbiental ato : processo.getAtosAmbientais()){
				if(ato.getIndDeclaratorio()){
					existeAtoDeclaratorio = true;
				}
			}
			
			if(!existeAtoDeclaratorio){
				throw new Exception("Não existe ato declaratorio");
			}			
		}
		catch(Exception e) {
			return false;
		}
		
		return true && !isRfp(processo) && !isConversaoTcraLac(processo) && !isLac(processo) && !isAtoRegistroCorte(processo) && !isAtoRegistroFlorestaProducao(processo);
	}
	
	public boolean isConversaoTcraLac(Processo processo) {
		try{
			if(processo != null){
				Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorProcesso(processo.getIdeProcesso());
				return empreendimento.getIndConversaoTcraLac();				
			}
			return false;
		}
		catch(Exception e){
			return false;
		}
		
	}
	
	private boolean isAtoRegistroCorte(Processo processo) {
		if(processo != null && processo.getAtosAmbientais()!=null){
			for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
				if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId())) {
					return true;
				}
			}			
		}
		return false;
	}
	
	private boolean isAtoRegistroFlorestaProducao(Processo processo) {
		if(processo != null && processo.getAtosAmbientais()!=null){
			for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
				if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isLac(Processo processo) {
		if(processo != null && processo.getAtosAmbientais() != null){
			for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
				if (atoAmbiental.isLac()) {
					return true;
				}
			}			
		}
		return false;
	}

	public boolean isRfp(Processo processo) {
		if(processo != null && processo.getAtosAmbientais() != null){
			for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
				if (atoAmbiental.isRfp()) {
					return true;
				}
			}			
		}
		return false;
	}

	public boolean isRcfp(Processo processo) {
		if(processo != null && processo.getAtosAmbientais() != null){
			for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
				if (atoAmbiental.isRcfp()) {
					return true;
				}
			}			
		}
		return false;
	}
	
	public boolean isLacErb(Processo processo){
		if(processo!=null) {
			try{
				Lac lac = lacService.buscarLacPorProcesso(processo.getIdeProcesso());
				if(lac != null && lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.ERB.getId())){					
					return true;
				}
			}
			catch(Exception e){
			}
		}
		return false;
	}
	
	public boolean isLacPosto(Processo processo){
		if(processo!=null) {
			try{
				Lac lac = lacService.buscarLacPorProcesso(processo.getIdeProcesso());
				if(lac != null && lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.POSTO.getId())){					
					return true;
				}
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getSimpleName(), Level.ERROR, e);
			}
		}
		return false;
	}
	
	public boolean isLacTransportadora(Processo processo){
		
		if(processo!=null) {
			try{
				Lac lac = lacService.buscarLacPorProcesso(processo.getIdeProcesso());
				if(lac != null && lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.TRANSPORTADORA.getId())){					
					return true;
				}
			} catch (Exception e) {
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
		return false;
	}
	
	public boolean isDla(Requerimento requerimento){
		
		if(requerimento!=null) {
			try{
				RequerimentoUnico reqUnico = requerimentoUnicoService.recuperarRequerimentoUnicoPorId(new RequerimentoUnico(requerimento.getIdeRequerimento()));
				if(reqUnico != null && reqUnico.getIndDla()){
					return true;
				}
			} catch (Exception e) {
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
		return false;
	}
	
	public StreamedContent imprimirCertificado(Requerimento requerimento) throws Exception {
		gerarCertificado(requerimento);
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_req", requerimento.getIdeRequerimento());
		lParametros.put("classes", tratarClasseRisco(requerimento));
		return new RelatorioUtil("certificado_lac_transporte_2.jasper", lParametros,true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public String tratarClasseRisco(Requerimento requerimento){
		List<LacTransporteProduto> listaTransporteProduto;
		String str = "";
		
		Exception erro = null;
		
		try {
				listaTransporteProduto = lacTransporteService.buscarClasseRisco(requerimento);
			//Divide as classes no formato padrão de texto.
			for (int x = 0; x < listaTransporteProduto.size(); x++) {
				if(listaTransporteProduto.size() - x == 2 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+" e ";
				}else if(listaTransporteProduto.size() - x == 1 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1);
				}else if(!str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+", ";
				}
			}
			//Caso haja apenas uma classe de risco remove a virgula do fim
			if(str.endsWith(", ")){
				str = str.substring(0, str.length()-2);
			}
			//Caso a String esteja no formato 1, 2 ou 1,2, 3 e assim sucessivamente adiciona o "e" ficando 1 e 2 ou 1, 2 e 3
			if(!str.contains("e") && str.length() > 4){
				str = str.substring(0, str.length()-3) + str.substring(str.length()-3).replaceAll(",", " e");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return str;
	}
	
	public StreamedContent gerarDocumentoDLA(Requerimento requerimento) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			RelatorioUtil lRelatorio = new RelatorioUtil("dla.jasper", params);
			DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			relatorio.setContentType("application/pdf");
			return relatorio;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}
	
	
	/**
	 *@author Alexandre.Queiroz
	 *@since v2 09/06/2014
	 *@param Caminho do arquivo Original, e Caminho do Arquivo Final 
	 */	
	public static void copyFile(File source, File destination) throws IOException {  
		
	     if (destination.exists())
	    	 destination.delete();  
	  
	     FileChannel sourceChannel = null;  
	     FileChannel destinationChannel = null;  
	  
	     try {  
	         sourceChannel = new FileInputStream(source).getChannel();  
	         destinationChannel = new FileOutputStream(destination).getChannel();  
	         sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);  
	     } finally {  
	         
	    	 if (sourceChannel != null && sourceChannel.isOpen())  
	             sourceChannel.close();  
	    
	         if (destinationChannel != null && destinationChannel.isOpen())  
	             destinationChannel.close();  
	    }
	    System.out.println("["+source.getName()+"]" + "Copiado para "+"["+ destination.getName()+"]");    
	}  

	/**
	 *@author Alexandre.Queiroz
	 *@since v2 09/06/2014
	 *@param dscCaminho 
	 *@return Arquivo em PDf ou imagem com a WaterMark- Somente Visualização
	 */
	public StreamedContent getDocumentoObrigatorioSCMarcaDagua(String dscCaminho) {
		
		File fileOld = new File(dscCaminho);
        File fileNio = null;

        String name = fileOld.getName().substring(0,fileOld.getName().indexOf(PONTO)).toString();
        String exte  = fileOld.getName().substring(fileOld.getName().lastIndexOf(PONTO) + 1).toLowerCase();

        fileNio = new File (fileOld.getParent() + BARRA +   name +  NOVO_NOME + exte);
            
        try {
        
        	copyFile(fileOld, fileNio);
        
        } catch (IOException ex) {
        	Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
        }
	
        
		try {
			
			String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath(ARQUIVO_MARCA_DAGUA);
	        Image image = Image.getInstance(caminho);
	        image.setAbsolutePosition(0, 0);
			
			if( (fileNio!= null) && (!"".equals(fileNio)) )
			{	
			    	
			String ext = fileNio.getName().substring(fileNio.getName().lastIndexOf('.') + 1).toLowerCase();
			 
			//pdf
			if(ext.equals("pdf")){
			  PdfReader reader = new PdfReader(new FileInputStream(fileNio)); 
			     PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(fileNio));
			        int totalPaginas = reader.getNumberOfPages() + 1;

			        for (int i = 1; i < totalPaginas; i++) {
			            PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);      
			            pdfContentByte. addImage(image, image.getWidth(), 0,0, image.getHeight(), 0 ,0);
			        }
			        
			        pdfStamper.close();
			        reader.close();
			        
			   return gerenciaArquivoService.getContentFile(fileNio.getPath());		
			 }
			 
			
			 //imagens
			 else if( (ext.equals("jpeg")) || (ext.equals("jpg") || (ext.equals("png"))  )) {

			       BufferedImage imageOri = ImageIO.read(fileNio);   
			       BufferedImage image2 = ImageIO.read(new File(ARQUIVO_MARCA_DAGUA));   
			  
			       int w = image2.getWidth();   
			       int h = image2.getHeight();   
			  
			       Graphics2D graphics = imageOri.createGraphics();  
			       graphics.drawImage(image2, 0, 0, w, h, null);  
			       graphics.dispose(); 
			       
			       try {
			         BufferedImage b = imageOri;
			         File outputfile = new File(fileNio.getAbsolutePath());
			         ImageIO.write(b, "png", outputfile);
			         
			     	return gerenciaArquivoService.getContentFile(fileNio.getPath());		
			         
			      } catch (IOException e) {
			          Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			      }	 
				 	 
			}else{
				System.out.println("Arquivo Não reconhecido ou não qualificado ");
				return null;
			}	
			 		
			}
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado! ");
		}
	
		return null;	
	}
	
	public StreamedContent getDocumentoObrigatorioSC(String dscCaminho) {
		try {
			return gerenciaArquivoService.getContentFile(dscCaminho);
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent gerarResumoRequerimentoPdf(Requerimento requerimento) {
		try {
			Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimentoRetornandoImoveis(requerimento.getIdeRequerimento());
			List<Imovel> listaImovel = (List<Imovel>) empreendimento.getImovelCollection();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
			
			if(this.requerimentoService.isRequerimentoAntigo(requerimento.getIdeRequerimento())){
				RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			}else{
				return  new ReportUtil().imprimir("resumo_requerimento.jasper", params);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		return null;
	}
	
	public boolean isAPE(Processo processo) {
			
		try {
			if (!Util.isNull(processo) && !Util.isNull(processo.getIdeRequerimento()) && !Util.isNull(processo.getIdeRequerimento().getIdeRequerimento())) {
				for (AtoAmbiental ato: processo.getAtosAmbientais()) {
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.APE.getId())) {
						
						VwConsultaProcesso consultaProcesso = this.vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(processo.getIdeProcesso(), true);	
						
						if(consultaProcesso.getStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus())) {
							if(processoAtoService.listarAtosPorProcessoComOuSemStatus(consultaProcesso.getIdeProcesso(), true, StatusProcessoAtoEnum.EMITIDO).size() > 0)return true;
							
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	 }
	
	
	public StreamedContent gerarResumoAPEPdf(Requerimento requerimento) {
		try {
			Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimentoRetornandoImoveis(requerimento.getIdeRequerimento());
					
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			
			if(this.requerimentoService.isRequerimentoAntigo(requerimento.getIdeRequerimento())){
		 		RelatorioUtil lRelatorio = new RelatorioUtil("certificado_ape.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			}else{				
				return new RelatorioUtil("certificado_ape.jasper", params).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		return null;
	}
	
	
	
	public StreamedContent getRegistroCorteFlorestaProducaoPlantada(Integer ideRequerimento) {
		try {
			return new ImpressoraAtoDeclaratorio().imprimirCertificado(ideRequerimento, DocumentoObrigatorioEnum.FORMULARIO_RFP);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public boolean isDocLac(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento){
		return this.isDocLacErb(documentoObrigatorioRequerimento) || this.isDocLacPosto(documentoObrigatorioRequerimento);
	}
	
	public boolean isDocLacErb(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		Boolean isValido = Boolean.FALSE;
		DocumentoObrigatorio documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio();
		
		Exception erro= null;
		
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo()) && documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(
						ParametroEnum.IDE_LAC_ERB).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return isValido;
	}

	public boolean isDocLacPosto(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		Boolean isValido = Boolean.FALSE;
		DocumentoObrigatorio documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio();
		
		Exception erro = null;
		
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo()) && documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(
						ParametroEnum.IDE_LAC_POSTO).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return isValido;
	}
	
	public boolean isRLAC(ConsultaPublicaDTO pConsultaPublicaDTO) {
		
		try {
			if (!Util.isNull(pConsultaPublicaDTO) && !Util.isNull(pConsultaPublicaDTO.getRequerimento()) && !Util.isNull(pConsultaPublicaDTO.getRequerimento().getIdeRequerimento())) {
				List<AtoAmbiental> listaAtosEnq = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(pConsultaPublicaDTO.getRequerimento().getIdeRequerimento());
				for (AtoAmbiental ato : listaAtosEnq) {
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLAC.getId())) {
						return true;
					}
				}
			}			
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoRLAC(Requerimento requerimento) throws Exception {
		
		gerarCertificado(requerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		lParametros.put("classes", tratarClasseRiscoTransporte(requerimento));
		return new RelatorioUtil("certificado_rlac.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	private String tratarClasseRiscoTransporte(Requerimento requerimento) {
		List<LacTransporteProduto> listaTransporteProduto = new ArrayList<LacTransporteProduto>();
		String str = "";
		Exception erro = null;
		
		try {
			
			Licenca licenca = licencaService.getLicencaByIdeRequerimentoSimples(requerimento);
			
			if(!Util.isNull(licenca) && !Util.isNullOuVazio(licenca.getNumProcessoLicenca())) {
				
				Processo processo = processoService.buscarProcessoPorCriteria(licenca.getNumProcessoLicenca());
				
				if(!Util.isNullOuVazio(processo) && !Util.isNull(processo.getIdeRequerimento())) {
					
					listaTransporteProduto = lacTransporteService.buscarClasseRisco(processo.getIdeRequerimento());
				}
			}
			
			//Divide as classes no formato padrão de texto.
			for (int x = 0; x < listaTransporteProduto.size(); x++) {
				if(listaTransporteProduto.size() - x == 2 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+" e ";
				}else if(listaTransporteProduto.size() - x == 1 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1);
				}else if(!str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+", ";
				}
			}
			
			//Caso haja apenas uma classe de risco remove a virgula do fim
			if(str.endsWith(", ")){
				str = str.substring(0, str.length()-2);
			}
			
			//Caso a String esteja no formato 1, 2 ou 1,2, 3 e assim sucessivamente adiciona o "e" ficando 1 e 2 ou 1, 2 e 3
			if(!str.contains("e") && str.length() > 4){
				str = str.substring(0, str.length()-3) + str.substring(str.length()-3).replaceAll(",", " e");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		} finally {
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return str;
	}
	
	public boolean isRequerimentoAntigo(Processo processo) {
		try {
			return requerimentoService.isRequerimentoAntigo(processo.getIdeRequerimento().getIdeRequerimento());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRequerimentoPago(ConsultaPublicaDTO pConsultaPublicaDTO) {
		return pConsultaPublicaDTO.getStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus());
	}
	
	public String getDesTipologiaAto() {
		return desTipologiaAto;
	}
	
	public LazyDataModel<ConsultaPublicaDTO> getDataModelConsultaPublica() {
		return dataModelConsultaPublica;
	}

	public void setDataModelPublica(LazyDataModel<ConsultaPublicaDTO> dataModelConsultaPublica) {
		this.dataModelConsultaPublica = dataModelConsultaPublica;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public Boolean getShowProcessos() {
		return showProcessos;
	}

	public void setShowProcessos(Boolean showProcessos) {
		this.showProcessos = showProcessos;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	
	public String getNomEmpreendimento() {
		return nomEmpreendimento;
	}

	public void setNomEmpreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento;
	}

	
	public List<Municipio> getListaMunicipios() {
	
		return listaMunicipios;
	}

	
	public void setListaMunicipios(List<Municipio> listaMunicipios) {
	
		this.listaMunicipios = listaMunicipios;
	}

	
	public Municipio getMunicipioSelecionado() {
	
		return municipioSelecionado;
	}

	
	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
	
		this.municipioSelecionado = municipioSelecionado;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
}
