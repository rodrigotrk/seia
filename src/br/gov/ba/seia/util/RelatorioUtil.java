package br.gov.ba.seia.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.enumerator.FuncionalidadeEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class RelatorioUtil {

	public static final int RELATORIO_PDF = 1;
	public static final int RELATORIO_XLS = 2;
	public static final int RELATORIO_HTML= 3;
	public static final int RELATORIO_TXT = 4;
	public static final int RELATORIO_RTF = 5;
	public static final int RELATORIO_CSV = 6;

	public static final Locale REPORT_LOCALE = new Locale("pt", "BR");
	private static final String URL_IMAGEM = "resources" + File.separator + "imagens";

	private String nomeRelatorio;
	private Map<String, Object> parametros;

	public RelatorioUtil(){}
	
	public RelatorioUtil(Map<String, Object> parametros) {
		try {
			this.parametros = parametros;
			this.nomeRelatorio = parametros.get("NOME_RELATORIO").toString();

			this.parametros.put("LOGO_INEMA", retornaCaminhoImagem("logoInemaCabecalho.png"));
			this.parametros.put("LOGO_SEIA", retornaCaminhoImagem("sema_vertical.jpg"));
			this.parametros.put("REPORT_LOCALE", REPORT_LOCALE);
			this.parametros.put("LOGO_GOVERNO", retornaCaminhoImagem("LOGO_GOVERNO.png"));
			
			if(parametros.containsKey("FUNCIONALIDADE")){
				 if (parametros.get("FUNCIONALIDADE").equals(FuncionalidadeEnum.ATIVIDADES_DE_EXPLORACAO_E_PRODUCAO_DE_OLEO_E_GAS)) {
					 this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioBy(FuncionalidadeEnum.ATIVIDADES_DE_EXPLORACAO_E_PRODUCAO_DE_OLEO_E_GAS));
				 }
			}else {
				if(parametros.containsKey("TIPO_ATIVIDADE")){
					/*this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioBy((TipoAtividadeNaoSujeitaLicenciamento) parametros.get("TIPO_ATIVIDADE")));*/
				}
				else {
					if(!this.parametros.containsKey("SUBREPORT_DIR") || Util.isNullOuVazio(this.parametros.get("SUBREPORT_DIR"))) {
						this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());
					}
				}
			}

			if(parametros.containsKey("TIPO_ATIVIDADE")){
				this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioBy(new TipoAtividadeNaoSujeitaLicenciamento((TipoAtividadeNaoSujeitaLicenciamentoEnum) parametros.get("TIPO_ATIVIDADE"))));
			} 

			else if(parametros.containsKey("CERH") && (Boolean) parametros.get("CERH")){
				this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioCerh());
				
			} else {
				if(Util.isNullOuVazio(this.parametros.get("SUBREPORT_DIR"))){
					this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());
				}
			}

			if (parametros.containsKey("TEM_MARCA_DAGUA")) {
				this.parametros.put("MARCA_DAGUA", retornaCaminhoImagem("marca.png"));
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	public RelatorioUtil(String pnomeRelatorio, Map<String, Object> parametros) {
		try {
			this.parametros = parametros;
			this.nomeRelatorio = pnomeRelatorio;
			this.parametros.put("LOGO_INEMA", retornaCaminhoImagem("logoInemaCabecalho.png"));
			this.parametros.put("LOGO_SEIA", retornaCaminhoImagem("sema_vertical.jpg"));
			this.parametros.put("REPORT_LOCALE", REPORT_LOCALE);
			this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	public RelatorioUtil(String pnomeRelatorio, Map<String, Object> pParametros, String nomeArquivoLogoInema, String nomeArquivoLogoSema) {
		try {
			this.nomeRelatorio = pnomeRelatorio;
			this.parametros = pParametros;
			this.parametros.put("LOGO_INEMA", retornaCaminhoImagem(nomeArquivoLogoInema));
			this.parametros.put("LOGO_SEIA", retornaCaminhoImagem(nomeArquivoLogoSema));
			this.parametros.put("REPORT_LOCALE", REPORT_LOCALE);
			this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	public RelatorioUtil(String pnomeRelatorio, Map<String, Object> pParametros, boolean hasMarcaDagua) {

		try {
			this.nomeRelatorio = pnomeRelatorio;
			this.parametros = pParametros;

			this.parametros.put("LOGO_INEMA", retornaCaminhoImagem("logoInemaCabecalho.png"));
			this.parametros.put("LOGO_SEIA", retornaCaminhoImagem("sema_vertical.jpg"));
			this.parametros.put("REPORT_LOCALE", REPORT_LOCALE);
			this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());

			if (hasMarcaDagua) {
				this.parametros.put("MARCA_DAGUA", retornaCaminhoImagem("marca.png"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public RelatorioUtil(String pnomeRelatorio, Map<String, Object> parametros, Boolean comMarcaDagua) {
		try {
			this.parametros = parametros;
			this.nomeRelatorio = pnomeRelatorio;
			this.parametros.put("LOGO_INEMA", retornaCaminhoImagem("logoInemaCabecalho.png"));
			this.parametros.put("LOGO_SEIA", retornaCaminhoImagem("sema_vertical.jpg"));
			this.parametros.put("REPORT_LOCALE", REPORT_LOCALE);
			this.parametros.put("NOME_RELATORIO", pnomeRelatorio);
			if(Util.isNullOuVazio(this.parametros.get("SUBREPORT_DIR"))){
				this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio());
			}
			else {
				this.parametros.put("SUBREPORT_DIR", retornaCaminhoRelatorio(this.parametros.get("SUBREPORT_DIR").toString()));
			}
			if(!Util.isNullOuVazio(comMarcaDagua) && comMarcaDagua){
				this.parametros.put("MARCA_DAGUA", retornaCaminhoImagem("marca.png"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public String retornaCaminhoRelatorio() {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		File file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS));
		return file.getAbsolutePath() + File.separator;
	}

	public String retornaCaminhoRelatorio(String dscCaminhoRelatorio) {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		File file = new File(sessao.getServletContext().getRealPath(File.separator + dscCaminhoRelatorio));
		return file.getAbsolutePath() + File.separator;
	}

	public static String retornaCaminhoImagem(String imagem) {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		File file = new File(sessao.getServletContext().getRealPath(File.separator + URL_IMAGEM));
		return file.getAbsolutePath() + File.separator + imagem;
	}

	public String retornaCaminhoRelatorioBy(FuncionalidadeEnum funcionalidade) {

		String caminho = "";

		if(funcionalidade.equals(FuncionalidadeEnum.ATIVIDADES_DE_EXPLORACAO_E_PRODUCAO_DE_OLEO_E_GAS)){

			HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			File file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_CAEPOG));
			caminho = file.getAbsolutePath() + File.separator;

		}

		return caminho;
	}

	public String retornaCaminhoRelatorioBy(TipoAtividadeNaoSujeitaLicenciamento tipoAtividade) {
		File file = null;
		
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		if(tipoAtividade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS.getIde())){
			file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_CAEPOG));
		}
		else if (tipoAtividade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL.getIde())){
			file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_PESQUISA_MINERAL));
		}
		else if(tipoAtividade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS.getIde())){
			file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_SILOS_ARMAZENS));
		}
		else if(tipoAtividade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES.getIde())){
			file = new File(sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_INSTALACAO_DE_TORRES));
		}
		return file.getAbsolutePath() + File.separator;
	}

	public String retornaCaminhoRelatorioCerh(){
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return sessao.getServletContext().getRealPath( File.separator + Uri.URL_RELATORIOS_CADASTRO_ESTADUAL_USUARIO_RECURSO_HIDRICO)+ File.separator;
	}

	public StreamedContent gerarRelatorio(int pTipoRelatorio, boolean pUtilizaBancoDados) throws Exception {
		ByteArrayOutputStream lOutputStream = null;
		Connection lConexao = null;
		JasperReport lJasperReport = null;
		
		if (!Util.isNullOuVazio(getNomeRelatorio())) {
			try {
				lOutputStream = new ByteArrayOutputStream();

				if (getNomeRelatorio().endsWith(".jasper")) {
					lJasperReport = (JasperReport) JRLoader.loadObjectFromFile(retornaCaminhoRelatorio() + getNomeRelatorio());
				}
				else if (getNomeRelatorio().endsWith(".jrxml")) {
					lJasperReport = JasperCompileManager.compileReport((retornaCaminhoRelatorio() + getNomeRelatorio()));
				}

				JasperPrint lJasperPrint = null;

				if (!Util.isNullOuVazio(lJasperReport.getQuery()) && !Util.isNullOuVazio(lJasperReport.getQuery().getText())) {
					lConexao = BancoUtil.obterConexao();
					lJasperPrint = JasperFillManager.fillReport(lJasperReport, getParametros(), lConexao);
				}
				else {
					lJasperPrint = JasperFillManager.fillReport(lJasperReport, getParametros(), new JREmptyDataSource());
				}

				JRExporter lJRExporter = null;
				String lExtensaoArquivoExportado = null;
				String mimeType = null;

				switch (pTipoRelatorio) {
					case RelatorioUtil.RELATORIO_PDF:
						lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
						lExtensaoArquivoExportado = ".pdf";
						mimeType = "application/pdf";
						break;
					case RelatorioUtil.RELATORIO_XLS:
						lJRExporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
						lJRExporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65000);
						lExtensaoArquivoExportado = ".xls";
						mimeType = "application/vnd.ms-excel";
						break;
					case RelatorioUtil.RELATORIO_HTML:
						lJRExporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
						lJRExporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
						lExtensaoArquivoExportado = ".html";
						mimeType = "text/html";
						break;
					case RelatorioUtil.RELATORIO_TXT:
						lJRExporter = new net.sf.jasperreports.engine.export.JRTextExporter();
						lJRExporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(lJasperPrint.getPageWidth()));
						lJRExporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Integer(lJasperPrint.getPageHeight()));
						lExtensaoArquivoExportado = ".txt";
						mimeType = "text/plain";
						break;
					case RelatorioUtil.RELATORIO_RTF:
						lJRExporter = new net.sf.jasperreports.engine.export.JRRtfExporter();
						lExtensaoArquivoExportado = ".rtf";
						mimeType = "application/rtf";
						break;
					case RelatorioUtil.RELATORIO_CSV:
						lJRExporter = new net.sf.jasperreports.engine.export.JRCsvExporter();
						lExtensaoArquivoExportado = ".csv";
						mimeType = "text/csv";
						break;
					default:
						lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
						lExtensaoArquivoExportado = ".pdf";
						mimeType = "application/pdf";
						break;
				}

				lJRExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, lOutputStream);
				lJRExporter.setParameter(JRExporterParameter.JASPER_PRINT, lJasperPrint);
				lJRExporter.exportReport();

				String lNomeArquivo = getNomeRelatorioFormatado() + "_" + new Date().getTime() + lExtensaoArquivoExportado;

				return new DefaultStreamedContent(new ByteArrayInputStream(lOutputStream.toByteArray()), mimeType, lNomeArquivo);
			} catch (Exception lException) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, lException);
				throw new Exception("Não foi possível gerar o relatório.", lException);
			} finally {
				if (!Util.isNullOuVazio(lOutputStream)) lOutputStream.close();
				if (!Util.isNullOuVazio(lJasperReport) && !Util.isNullOuVazio(lJasperReport.getQuery()) && !Util.isNullOuVazio(lJasperReport.getQuery().getText()) && !Util.isNullOuVazio(lConexao)) lConexao.close();
			}
		} else {
			throw new Exception("Não foi possível gerar o relatório em função da falta do nome do relatório.");
		}
	}

	public StreamedContent gerar() throws Exception {

		ByteArrayOutputStream lOutputStream = null;
		Connection conexao = null;
		JasperReport jasperReport = null;

		try {

			lOutputStream = new ByteArrayOutputStream();

			jasperReport = (JasperReport) JRLoader.loadObjectFromFile(parametros.get("SUBREPORT_DIR").toString() + parametros.get("NOME_RELATORIO").toString());

			conexao = BancoUtil.obterConexao();
			JasperPrint lJasperPrint = JasperFillManager.fillReport(jasperReport, getParametros(), conexao);

			JRExporter jRExporter =  new net.sf.jasperreports.engine.export.JRPdfExporter();
			jRExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, lOutputStream);
			jRExporter.setParameter(JRExporterParameter.JASPER_PRINT, lJasperPrint);
			jRExporter.exportReport();



			return new DefaultStreamedContent(new ByteArrayInputStream(lOutputStream.toByteArray()),"",getNomeRelatorioFormatado(this.nomeRelatorio) + "_" + new Date().getTime() + ".pdf");

		} catch (Exception e) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw new Exception("Não foi possível gerar o relatório.", e);

		} finally {

			if (!Util.isNullOuVazio(lOutputStream)) {
				lOutputStream.close();
			}

			if (!Util.isNullOuVazio(jasperReport.getQuery()) && !Util.isNullOuVazio(jasperReport.getQuery().getText()) && !Util.isNullOuVazio(conexao)) {
				conexao.close();
			}
		}

	}

	public StreamedContent gerarRelatorioComDataSource(int pTipoRelatorio,	boolean pUtilizaBancoDados, List<?> dataSource) throws Exception {
		ByteArrayOutputStream lOutputStream = null;
		JasperReport lJasperReport = null;
		if (!Util.isNullOuVazio(getNomeRelatorio())) {
			try {
				lOutputStream = new ByteArrayOutputStream();
				if (getNomeRelatorio().endsWith(".jasper")) {
					lJasperReport = (JasperReport) JRLoader
							.loadObjectFromFile(retornaCaminhoRelatorio()
									+ getNomeRelatorio());
				} else if (getNomeRelatorio().endsWith(".jrxml")) {
					lJasperReport = JasperCompileManager
							.compileReport((retornaCaminhoRelatorio() + getNomeRelatorio()));
				}
				JasperPrint lJasperPrint = JasperFillManager.fillReport(lJasperReport,
							getParametros(), new JRBeanCollectionDataSource(dataSource));

				JRExporter lJRExporter = null;
				String lExtensaoArquivoExportado = null;
				switch (pTipoRelatorio) {
				case RelatorioUtil.RELATORIO_PDF:
					lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
					lExtensaoArquivoExportado = ".pdf";
					break;
				case RelatorioUtil.RELATORIO_XLS:
					lJRExporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
					lJRExporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 65000);
					lExtensaoArquivoExportado = ".xls";
					break;
				case RelatorioUtil.RELATORIO_HTML:
					lJRExporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
					lJRExporter.setParameter(
							JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
							false);
					lExtensaoArquivoExportado = ".html";
					break;
				case RelatorioUtil.RELATORIO_TXT:
					lJRExporter = new net.sf.jasperreports.engine.export.JRTextExporter();
					lJRExporter.setParameter(
							JRTextExporterParameter.PAGE_WIDTH, new Integer(
									lJasperPrint.getPageWidth()));
					lJRExporter.setParameter(
							JRTextExporterParameter.PAGE_HEIGHT, new Integer(
									lJasperPrint.getPageHeight()));
					lExtensaoArquivoExportado = ".txt";
					break;
				case RelatorioUtil.RELATORIO_RTF:
					lJRExporter = new net.sf.jasperreports.engine.export.JRRtfExporter();
					lExtensaoArquivoExportado = ".rtf";
					break;
				case RelatorioUtil.RELATORIO_CSV:
					lJRExporter = new net.sf.jasperreports.engine.export.JRCsvExporter();
					lExtensaoArquivoExportado = ".csv";
					break;
				default:
					lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
					lExtensaoArquivoExportado = ".pdf";
					break;
				}
				lJRExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						lOutputStream);
				lJRExporter.setParameter(JRExporterParameter.JASPER_PRINT,
						lJasperPrint);
				lJRExporter.exportReport();
				String lNomeArquivo = getNomeRelatorioFormatado() + "_"
						+ new Date().getTime() + lExtensaoArquivoExportado;
				return new DefaultStreamedContent(new ByteArrayInputStream(
						lOutputStream.toByteArray()), "", lNomeArquivo);
			} catch (Exception lException) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, lException);
				throw new Exception("Não foi possível gerar o relatório.",
						lException);
			} finally {
				if (!Util.isNullOuVazio(lOutputStream))
					lOutputStream.close();
			}
		} else {
			throw new Exception(
					"Não foi possível gerar o relatório em função da falta do nome do relatório.");
		}
	}


	public static String formatarTermoDeCompromisso(ImovelRural imovelRural,TipoVinculoImovel tipoVinculoImovelRural, Collection<Pessoa> listPessoa,Collection<RepresentanteLegal> representantesLegais, List<PessoaFisica> listAssentado){
		StringBuilder termoDeCompromisso = new StringBuilder();
		termoDeCompromisso.append("<br>Pelo presente instrumento, nos termos do Art. 5º, § 6º da Lei Federal nº 7.347, de 24 de julho de 1985, com a redação que lhe foi dada pela Lei Federal n. º 8.078, ");
		termoDeCompromisso.append("de 11 de setembro de 1990, dos Art. 50 e 191 da Lei Estadual nº 10.431/2006 e do Art. 291 do Decreto Estadual nº 14.024/2012, de um lado o <b>INSTITUTO DO MEIO AMBIENTE E ");
		termoDeCompromisso.append("RECURSOS HÍDRICOS - INEMA</b>, Autarquia vinculada à Secretaria de Meio Ambiente – SEMA, criada pela Lei n° 12.212, de 04 de maio de 2011, inscrita no CGC/MF sob o n. º 13.700.575/0001-69,");
		termoDeCompromisso.append(" com sede na Avenida Luis Viana Filho, 6.º Avenida nº 600 – CAB – CEP: 41745-900, Salvador-BA, adiante denominado");
		if(!imovelRural.isImovelINCRA()) {
			termoDeCompromisso.append("s ");
		}
		termoDeCompromisso.append(" <b>COMPROMITENTE</b> ou <b>INEMA</b>; e de outro lado, ");


		int qtdProprietarios = 0;
		String numCpfCnpj = "";
		StringBuilder tipoVinculoImovel = new StringBuilder();

		if((!Util.isNullOuVazio(listPessoa) || !listPessoa.isEmpty())){
			for (Pessoa proprietario : listPessoa) {
				if(qtdProprietarios > 0){
					termoDeCompromisso.append(", ");
				}

				if(proprietario.getCpfCnpj().length() == 11){
					numCpfCnpj = "CPF";
				}else{
					numCpfCnpj = "CNPJ";
				}

				termoDeCompromisso.append("<i>" + proprietario.getNomeRazao() + "</i>");
				if(!Util.isNullOuVazio(proprietario.getEndereco())) {
					termoDeCompromisso.append(" com endereço em <i>" + proprietario.getEndereco().getEnderecoFormatado() + "</i>, CEP <i>" + proprietario.getEndereco().getIdeLogradouro().getNumCepFormatado()
							+ "</i>, município de <i>" + proprietario.getEndereco().getIdeLogradouro().getIdeBairro().getIdeMunicipio().getNomMunicipio() + "</i>");
				}
				termoDeCompromisso.append(", inscrito(a) no " + numCpfCnpj + " sob o n° <i>" + proprietario.getCpfCnpjFormatado() + "</i>");

				qtdProprietarios++;

			}
		}

		if(imovelRural.isImovelINCRA()) {
			qtdProprietarios = 0;
			if(!Util.isNullOuVazio(representantesLegais)){
				termoDeCompromisso.append(", neste ato representado por ");
				for (RepresentanteLegal representante : representantesLegais) {
					if(qtdProprietarios > 0){
						termoDeCompromisso.append(", <i>");
					}
					termoDeCompromisso.append(representante.getIdePessoaFisica().getNomPessoa());
					termoDeCompromisso.append("</i>, de nacionalidade <i>" + representante.getIdePessoaFisica().getIdePais().getNomPais());
					termoDeCompromisso.append("</i>, CPF nº <i>" + representante.getIdePessoaFisica().getNumCpf() + "</i>");
					qtdProprietarios++;
				}
			}
			termoDeCompromisso.append(" e o(s) assentado(s) ");
		}

		if(!Util.isNullOuVazio(listAssentado)) {
			for (PessoaFisica pessoaFisica : listAssentado) {
				termoDeCompromisso.append(" <i>" + pessoaFisica.getNomPessoa() + "</i> inscrito(a) no CPF sob o n° <i>" + pessoaFisica.getNumCpf() + ",</i>");
				qtdProprietarios++;
			}
		}

		if(tipoVinculoImovelRural.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
			if(qtdProprietarios > 1)
				tipoVinculoImovel.append("justo(s) possuidor(es)");
			else
				tipoVinculoImovel.append("justo possuidor");
		} else {
			if(!imovelRural.isImovelINCRA()) {
				if(qtdProprietarios > 1)
					tipoVinculoImovel.append("proprietário(s)");
				else
					tipoVinculoImovel.append("proprietário");
			}
		}

		String labelDenominado = qtdProprietarios > 1 ? "denominados" : "denominado";

		termoDeCompromisso.append(" adiante " + labelDenominado + " <b>COMPROMISSADO</b>");
		if(qtdProprietarios > 1) {
			termoDeCompromisso.append("<b>S</b>");
		}

		//
		if(!imovelRural.isImovelINCRA()) {
			qtdProprietarios = 0;
			if(!Util.isNullOuVazio(representantesLegais)){
				termoDeCompromisso.append(", neste ato representado por ");
				for (RepresentanteLegal representante : representantesLegais) {
					if(qtdProprietarios > 0){
						termoDeCompromisso.append(", <i>");
					}
					termoDeCompromisso.append(representante.getIdePessoaFisica().getNomPessoa());
					termoDeCompromisso.append("</i>, de nacionalidade <i>" + representante.getIdePessoaFisica().getIdePais().getNomPais());
					termoDeCompromisso.append("</i>, CPF nº <i>" + representante.getIdePessoaFisica().getNumCpf() + "</i>");
					qtdProprietarios++;
				}
			}
		}
		Municipio municipio = imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio();

		termoDeCompromisso.append(", " + tipoVinculoImovel.toString());

		if(!imovelRural.isImovelINCRA()) {
			termoDeCompromisso.append(", justo(s) possuidor(es)");
		}
		termoDeCompromisso.append(", do imóvel rural situado no município de <i>" + municipio.getNomMunicipio() + "</i>, Estado da Bahia, ");


		if(!Util.isNullOuVazio(imovelRural.getRegistroMatricula())) {
			if(tipoVinculoImovelRural.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
				termoDeCompromisso.append(" registrado sob o nº do documento de posse <i>" + imovelRural.getRegistroMatricula() +"</i>, ");
			} else {
				termoDeCompromisso.append(" registrado sob Matrícula <i>" + imovelRural.getRegistroMatricula() +"</i>, ");
			}

			termoDeCompromisso.append((!Util.isNullOuVazio(imovelRural.getDesCartorio()) ? "cartório <i>" + imovelRural.getDesCartorio() + "</i>, " : ""));
		}

		termoDeCompromisso.append("inscrito no Cadastro Estadual Florestal de Imóveis Rurais - CEFIR, têm entre si certo e ajustado o presente Termo, o qual se regerá pelas cláusulas e condições ora estipuladas, com inteira submissão às disposições legais aplicáveis à espécie.<br>");

		return termoDeCompromisso.toString();
	}

	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	private String getNomeRelatorioFormatado(String nomeRelatorio) {
		return nomeRelatorio.replace(".jasper", "").replace(".jrxml", "");
	}

	private String getNomeRelatorioFormatado() {
		return getNomeRelatorio().replace(".jasper", "").replace(".jrxml", "");
	}

	public void setNomeRelatorio(String nomeRelatorio) {
		this.nomeRelatorio = nomeRelatorio;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}


	public String getRealPath(String folder) {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return sessao.getServletContext().getRealPath( File.separator + folder)+ File.separator;
	}

	public String salvar(int pTipoRelatorio, boolean pUtilizaBancoDados, boolean isRequerimentoAntigo, String destinoCaminhoArquivo, String nomeArquivo) throws Exception {
		ByteArrayOutputStream lOutputStream = null;
		Connection lConexao = null;
		JasperReport lJasperReport = null;
		String caminhoRelatorio = null;
		String diretorio = null;

		if (!Util.isNullOuVazio(getNomeRelatorio())) {
			try {
				diretorio = destinoCaminhoArquivo.concat(nomeArquivo);
				File file = new File(destinoCaminhoArquivo);
				file.mkdirs();

				lOutputStream = new ByteArrayOutputStream();

				if (isRequerimentoAntigo){
					caminhoRelatorio = retornaCaminhoRelatorio();
				} else {
					caminhoRelatorio = getRealPath(Uri.URL_RELATORIOS_NOVO_REQUERIMENTO);
					getParametros().put("SUBREPORT_DIR", caminhoRelatorio);

					String resources = getRealPath("resources/imagens");
					getParametros().put("LOGO_INEMA",resources+"logoInemaCabecalho.png");
					getParametros().put("LOGO_SEIA", resources+"sema_vertical.jpg");
					getParametros().put("REPORT_LOCALE", REPORT_LOCALE);
				}

				if (getNomeRelatorio().endsWith(".jasper")) {
					lJasperReport = (JasperReport) JRLoader.loadObjectFromFile(caminhoRelatorio + getNomeRelatorio());
				}
				else if (getNomeRelatorio().endsWith(".jrxml")) {
					lJasperReport = JasperCompileManager.compileReport((caminhoRelatorio + getNomeRelatorio()));
				}

				JasperPrint lJasperPrint = null;

				if (!Util.isNullOuVazio(lJasperReport.getQuery()) && !Util.isNullOuVazio(lJasperReport.getQuery().getText())) {
					lConexao = BancoUtil.obterConexao();
					lJasperPrint = JasperFillManager.fillReport(lJasperReport, getParametros(), lConexao);
				}
				else {
					lJasperPrint = JasperFillManager.fillReport(lJasperReport, getParametros(), new JREmptyDataSource());
				}

				JRExporter lJRExporter = null;

				switch (pTipoRelatorio) {
					case RelatorioUtil.RELATORIO_PDF:
						lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
						break;
					case RelatorioUtil.RELATORIO_XLS:
						lJRExporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
						break;
					case RelatorioUtil.RELATORIO_HTML:
						lJRExporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
						lJRExporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
						break;
					case RelatorioUtil.RELATORIO_TXT:
						lJRExporter = new net.sf.jasperreports.engine.export.JRTextExporter();
						lJRExporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(lJasperPrint.getPageWidth()));
						lJRExporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Integer(lJasperPrint.getPageHeight()));
						break;
					case RelatorioUtil.RELATORIO_RTF:
						lJRExporter = new net.sf.jasperreports.engine.export.JRRtfExporter();
						break;
					case RelatorioUtil.RELATORIO_CSV:
						lJRExporter = new net.sf.jasperreports.engine.export.JRCsvExporter();
						break;
					default:
						lJRExporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
						break;
				}

				lJRExporter.setParameter(JRExporterParameter.JASPER_PRINT, lJasperPrint);
				lJRExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, diretorio);
				lJRExporter.exportReport();

				return diretorio;
			} catch (Exception lException) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, lException);
				throw new Exception("Não foi possível gerar o relatório.", lException);
			} finally {
				if (!Util.isNullOuVazio(lOutputStream)) lOutputStream.close();
				if (!Util.isNullOuVazio(lJasperReport.getQuery()) && !Util.isNullOuVazio(lJasperReport.getQuery().getText()) && !Util.isNullOuVazio(lConexao)) lConexao.close();
			}
		} else {
			throw new Exception("Não foi possível gerar o relatório em função da falta do nome do relatório.");
		}
	}

	public StreamedContent downloadArquivoRelatorio(String caminho) throws FileNotFoundException{
		File tempFile = new File(caminho);

		String[] caminhoColunas = caminho.split("/");
		String nomeArquivo = caminhoColunas[caminhoColunas.length - 1];

		StreamedContent dFile = new DefaultStreamedContent(new FileInputStream(tempFile), new MimetypesFileTypeMap().getContentType(tempFile), nomeArquivo);

		return dFile;
	}
}