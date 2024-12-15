package br.gov.ba.seia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.exception.SeiaLoteBoletoException;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.middleware.seia.model.BaixaArquivoRetornoDTO;
import br.gov.ba.seia.middleware.seia.model.LeituraArquivoRetornoDTO;
import br.gov.ba.seia.middleware.seia.model.Pessoa;
import br.gov.ba.seia.middleware.seia.service.BaixaBoletoService;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LoteBoletoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;


@Named("consultaLoteBoletoController")
@ViewScoped
public class ConsultaLoteBoletoController {

	// SERVICES
	@EJB
	private LoteBoletoService loteBoletoService;
	@EJB
	private BaixaBoletoService baixaBoletoService;
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;
	@EJB
 	private GerenciaArquivoService gerenciaArquivoService;

	

	// OBJETOS DA TELA
	private String numeroLote;
	private String numeroRequerimento;
	private DataTable dataTableLote;
	private LazyDataModel<LoteBoleto> listaLotes;
	private List<BoletoPagamentoRequerimento> listaBoletos;
	
	private LoteBoleto loteSelecionado; //lote selecionado ao clicar no icone de confirmacao de processamento de remessa
	private String caminhoArquivoRetorno; //caminho do arquivo de retorno importado no servidor

	@PostConstruct
	public void load() {
		limpar();
	}

	public void limpar() {
		numeroLote = null;
		numeroRequerimento = null;
		dataTableLote = null;
	}
	
	public void consultar(){
		try{
			this.dataTableLote.setFirst(0);
			this.dataTableLote.setPage(1);
			carregarPaginacaoLotes();
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarPaginacaoLotes() {

		listaLotes = new LazyDataModel<LoteBoleto>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<LoteBoleto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

				List<LoteBoleto> lista = null;

				try {
					Map<String, Object> params = carregarParametros();

					params.put("first", first);
					params.put("pageSize", pageSize);
					params.put("isPagination", true);
					
					lista = loteBoletoService.listarLoteBoleto(params);
					return lista;

				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação.");
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		
		listaLotes.setRowCount(count());
	}

	private Integer count() {
		try{
			Map<String, Object> params = carregarParametros();
			params.put("isCount", true);
			return loteBoletoService.countListarLoteBoleto(params);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private Map<String, Object> carregarParametros() {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("numLote", numeroLote);
		params.put("numRequerimento", numeroRequerimento);

		return params;
	}
	
	public void visualizarBoletos(LoteBoleto lote)  {
		listaBoletos = boletoPagamentoRequerimentoService.listarBoletosPorLote(lote);
	}
	
	public void getLoteRemessa(){
	
		try {
			
			StreamedContent remessa = loteBoletoService.gerarLoteRemessaBoleto();

			SessaoUtil.adicionarObjetoSessao("arquivo", remessa.getStream());
			SessaoUtil.adicionarObjetoSessao("nomeArquivo", remessa.getName());

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String url =  request.getScheme() + "://" + request.getServerName();
			
			if (request.getServerPort()!=80){
				url += ":" + request.getServerPort();
			}
			
			Html.executarJS("window.open('"+url+"/download/')");
			JsfUtil.addSuccessMessage("Lote de remessa nº "+StringUtils.substring(remessa.getName(), 8, 14)+" gerado com sucesso.");
			carregarPaginacaoLotes();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		finally {
			Html.atualizar("formBoleto:panelGrid");
			Html.atualizar("formBoleto:panelGrid:tabelaLotes");
		}
	}
	
	public void selecionarLoteProcessamento(LoteBoleto lote){
		this.loteSelecionado = lote;
	}
	
	public void limparCaminhoArquivoRetorno(){
		this.caminhoArquivoRetorno = null;
	}
	
	public void uploadRetorno(FileUploadEvent event) {
		try {
			setCaminhoArquivoRetorno(loteBoletoService.uploadArquivoRetorno(event.getFile()));
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public StreamedContent getRetornoDownload() {
		try {
			return gerenciaArquivoService.getContentFile(caminhoArquivoRetorno);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void salvarArquivoRetorno(){
		try {
			if(StringUtils.isEmpty(caminhoArquivoRetorno)) {
				MensagemUtil.alerta("É obrigatório o upload do arquivo.");
			}
			else {
				Pessoa usuarioLogado = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
				LeituraArquivoRetornoDTO leituraArquivoRetornoDTO = new LeituraArquivoRetornoDTO(usuarioLogado, caminhoArquivoRetorno);
				
				loteBoletoService.lerArquivoRetorno(leituraArquivoRetornoDTO);
				BaixaArquivoRetornoDTO baixaArquivoRetornoDTO = baixaBoletoService.registrarBaixa(leituraArquivoRetornoDTO);
				loteBoletoService.enviarEmailLoteBoleto(baixaArquivoRetornoDTO.getListaBaixaBoleto());
				
				if(baixaArquivoRetornoDTO.getListaBaixaBoletoErro().isEmpty()) {
					MensagemUtil.sucesso("Arquivo de retorno processado com sucesso.");
				} 
				else {
					MensagemUtil.alerta("Alguns boletos não foram processados.");
				}
			}
		}
		catch (SeiaLoteBoletoException e) {
			MensagemUtil.erro(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro("Ocorreu um erro ao importar o arquivo de retorno");
		}
	}

	

	
	public void confirmarProcessamentoRemessa(){
		try{
			listaBoletos = boletoPagamentoRequerimentoService.listarBoletosPorLote(loteSelecionado);
			loteBoletoService.confirmarLoteRemessa(loteSelecionado, listaBoletos);
			JsfUtil.addSuccessMessage("Remessa confirmada com sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public StreamedContent getRemessaJaGerada() {
		try {
			listaBoletos = boletoPagamentoRequerimentoService.listarBoletosPorLote(loteSelecionado);
			return loteBoletoService.getLoteRemessa(loteSelecionado, listaBoletos);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public String getNumeroRequerimento() {
		return numeroRequerimento;
	}

	public void setNumeroRequerimento(String numeroRequerimento) {
		this.numeroRequerimento = numeroRequerimento;
	}

	public DataTable getDataTableLote() {
		return dataTableLote;
	}

	public void setDataTableLote(DataTable dataTableLote) {
		this.dataTableLote = dataTableLote;
	}

	public LazyDataModel<LoteBoleto> getlistaLotes() {
		return listaLotes;
	}

	public void setListaLotes(LazyDataModel<LoteBoleto> listaLotes) {
		this.listaLotes = listaLotes;
	}

	public LazyDataModel<LoteBoleto> getListaLotes() {
		return listaLotes;
	}

	public List<BoletoPagamentoRequerimento> getListaBoletos() {
		return listaBoletos;
	}

	public void setListaBoletos(List<BoletoPagamentoRequerimento> listaBoletos) {
		this.listaBoletos = listaBoletos;
	}

	public LoteBoleto getLoteSelecionado() {
		return loteSelecionado;
	}

	public void setLoteSelecionado(LoteBoleto loteSelecionado) {
		this.loteSelecionado = loteSelecionado;
	}

	public String getCaminhoArquivoRetorno() {
		return caminhoArquivoRetorno;
	}

	public void setCaminhoArquivoRetorno(String caminhoArquivoRetorno) {
		this.caminhoArquivoRetorno = caminhoArquivoRetorno;
	}
}