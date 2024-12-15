package br.gov.ba.seia.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.enumerator.StatusFinanceiro;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.RelatorioFinanceiroService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class RelatorioFinanceiroController {
	
	private Date dataPagamentoInicial;
	
	private Date dataPagamentoFinal;

	private Date dataVencimentoInicial;
	
	private Date dataVencimentoFinal;
	
	private String numDocumento;
	
	private String nomeRequerente;
	
	private List<StatusFinanceiro> listStatusFinanceiroEnum;
	
	private boolean vizualizarDtPag;
	
	@Inject
	private RelatorioFinanceiroService relatorioFinanceiroService;

	private List<Object[]> relatorioFinanceiroList;

	public Date getDataPagamentoInicial() {
		return dataPagamentoInicial;
	}

	public void setDataPagamentoInicial(Date dataPagamentoInicial) {
		this.dataPagamentoInicial = dataPagamentoInicial;
	}

	public Date getDataPagamentoFinal() {
		return dataPagamentoFinal;
	}

	public void setDataPagamentoFinal(Date dataPagamentoFinal) {
		this.dataPagamentoFinal = dataPagamentoFinal;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	
	public StreamedContent getGerarRelatorio() {
		String dataInicialVencimento = null;
		String dataFinalVencimento = null;
		String dataPagamentoInicial = null;
		String dataPagamentoFinal = null;
		
		if(!Util.isNullOuVazio(dataVencimentoInicial)) {
			dataInicialVencimento = new SimpleDateFormat("dd/MM/yyyy").format(dataVencimentoInicial);
		}
		if(!Util.isNullOuVazio(dataVencimentoFinal)) {
			dataFinalVencimento = new SimpleDateFormat("dd/MM/yyyy").format(dataVencimentoFinal);
		}
		
		if(!Util.isNullOuVazio(this.dataPagamentoInicial)) {
			dataPagamentoInicial = new SimpleDateFormat("dd/MM/yyyy").format(this.dataPagamentoInicial);
		}
		if(!Util.isNullOuVazio(this.dataPagamentoFinal)) {
			dataPagamentoFinal = new SimpleDateFormat("dd/MM/yyyy").format(this.dataPagamentoFinal);
		}
		
		setRelatorioFinanceiroList(relatorioFinanceiroService.obterRelatorioFinanceiro(numDocumento, nomeRequerente, dataInicialVencimento, dataFinalVencimento, dataPagamentoInicial, dataPagamentoFinal, listStatusFinanceiroEnum));
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		RelatorioUtil lRelatorio = new RelatorioUtil("relatorioFinanceiro.jrxml", params);
		DefaultStreamedContent relatorio = null;
		try {
			relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorioComDataSource(RelatorioUtil.RELATORIO_PDF, true, getRelatorioFinanceiroList());
			relatorio.setContentType("application/pdf");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return relatorio;
	}

	public void limpar() {
		setNomeRequerente(null);
		setDataPagamentoInicial(null);
		setDataPagamentoFinal(null);
		setNumDocumento(null);
		setRelatorioFinanceiroList(null);
		setListStatusFinanceiroEnum(new ArrayList<StatusFinanceiro>());
		setVizualizarDtPag(false);
	}
	
	public void vizualizarDataPagamento(){
		vizualizarDtPag = false;
		for (StatusFinanceiro statusFinanceiro : listStatusFinanceiroEnum) {
			if (statusFinanceiro.equals(StatusFinanceiro.BOLETO_PAGO)){
				vizualizarDtPag = true;
			}
		}
		
		if (!vizualizarDtPag){
			setDataPagamentoInicial(null);
			setDataPagamentoFinal(null);
		}
	}

	public String getNomeRequerente() {
		return nomeRequerente;
	}

	public void setNomeRequerente(String nomeRequerente) {
		this.nomeRequerente = nomeRequerente;
	}

	public List<Object[]> getRelatorioFinanceiroList() {
		return relatorioFinanceiroList;
	}

	public void setRelatorioFinanceiroList(List<Object[]> relatorioFinanceiroList) {
		this.relatorioFinanceiroList = relatorioFinanceiroList;
	}

	public List<StatusFinanceiro> getListStatusFinanceiroEnum() {
		return listStatusFinanceiroEnum;
	}

	public void setListStatusFinanceiroEnum(List<StatusFinanceiro> listStatusFinanceiroEnum) {
		this.listStatusFinanceiroEnum = listStatusFinanceiroEnum;
	}
	
	public List<StatusFinanceiro> getListaStatusFinanceiro() {
		List<StatusFinanceiro> listaStatusFinanceiro = Arrays.asList(StatusFinanceiro.values());
		List<StatusFinanceiro> listaTemp = new ArrayList<StatusFinanceiro>();
		for (StatusFinanceiro statusFinanceiro : listaStatusFinanceiro) {
			if(!statusFinanceiro.equals(StatusFinanceiro.DECLARACAO_EMITIDA)){
				listaTemp.add(statusFinanceiro);
			}
		}
		listaStatusFinanceiro = listaTemp;
		
		return listaStatusFinanceiro;
	}

	public Date getDataVencimentoInicial() {
		return dataVencimentoInicial;
	}

	public void setDataVencimentoInicial(Date dataVencimentoInicial) {
		this.dataVencimentoInicial = dataVencimentoInicial;
	}

	public Date getDataVencimentoFinal() {
		return dataVencimentoFinal;
	}

	public void setDataVencimentoFinal(Date dataVencimentoFinal) {
		this.dataVencimentoFinal = dataVencimentoFinal;
	}

	public boolean isVizualizarDtPag() {
		return vizualizarDtPag;
	}

	public void setVizualizarDtPag(boolean vizualizarDtPag) {
		this.vizualizarDtPag = vizualizarDtPag;
	}
}
