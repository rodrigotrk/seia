package br.gov.ba.seia.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Deprecated
public class ReportUtil {

	private String path ;
	
	public static final Locale REPORT_LOCALE = new Locale("pt", "BR");
	
	public ReportUtil() {
		
	}
	
	public StreamedContent imprimir(String nomeRelatorio, Map parametros) throws Exception {
		
		ByteArrayOutputStream outputStream = null;
		Connection conexao = null;
		
		Exception erro = null;
		try {
			
			this.loadResources();
			this.loadParams(parametros);
			
			outputStream = new ByteArrayOutputStream();
			JasperReport jasperReport = this.carregarJasperReport(nomeRelatorio);
	
			conexao = BancoUtil.obterConexao();
	
			JasperPrint jasperPrint  = this.carregarJasperPrint(parametros, jasperReport, conexao);
	
			JRExporter exporter = this.carregarExporter(outputStream, jasperPrint);
			exporter.exportReport();
			
			String nome = this.formatar(nomeRelatorio);
			return new DefaultStreamedContent(new ByteArrayInputStream(outputStream.toByteArray()), "", nome);
		
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			    this.fechar(outputStream, conexao);
		}
		return null;
		
	}

	public void loadResources() {
		
		this.path =	this.getRealPath(Uri.URL_RELATORIOS_NOVO_REQUERIMENTO);
		
	}

	public void loadParams(Map<String, Object> params){
		params.put("SUBREPORT_DIR", this.path);
		
		String resources = this.getRealPath("resources/imagens");		
		params.put("LOGO_INEMA",resources+"logoInemaCabecalho.png");
		params.put("LOGO_SEIA", resources+"sema_vertical.jpg");
		params.put("REPORT_LOCALE", REPORT_LOCALE);
	}

	public String getRealPath(String folder) {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return sessao.getServletContext().getRealPath( File.separator + folder)+ File.separator;
	}
	
	public JasperReport carregarJasperReport(final String nomeRelatorio) throws JRException {
		if (nomeRelatorio.endsWith(".jasper")) {
			if(path.endsWith("\\"))
				return (JasperReport) JRLoader.loadObjectFromFile(path + nomeRelatorio);
			else
				return (JasperReport) JRLoader.loadObjectFromFile(path + File.separator +nomeRelatorio);
		} else {
			return JasperCompileManager.compileReport((path + File.separator + nomeRelatorio));
		}
	}

	public JasperPrint carregarJasperPrint(final Map parametros, JasperReport jasperReport, Connection conexao)
			throws JRException {
		if (!Util.isNullOuVazio(jasperReport.getQuery()) && !Util.isNullOuVazio(jasperReport.getQuery().getText())) {
			return JasperFillManager.fillReport(jasperReport, parametros, conexao);
		} else {
			return  JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
		}
	}

	public JRExporter carregarExporter(ByteArrayOutputStream outputStream, JasperPrint jasperPrint) {
		JRExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		return exporter;
	}

	private String formatar(String nomeRelatorio) {
		return nomeRelatorio.replace(".jasper", "").replace(".jrxml", "") + new Date().getTime() + ".pdf";
	}
	
	public void fechar(ByteArrayOutputStream outputStream, Connection conexao) throws IOException, SQLException {
		if (!Util.isNull(outputStream)){
			outputStream.close();
		}
			conexao.close();
	}

	
	private static JdbcTemplate dataSource;

	@Autowired
	public void setDataSource(DataSource pDataSource) {
		dataSource = new JdbcTemplate(pDataSource);
	}

	public static DataSource getDataSource() {
		return dataSource.getDataSource();
	}

	
	public String salvar(String diretorio, String nomePdf, String nomeRelatorio, Map parametros) throws Exception {
		
		ByteArrayOutputStream outputStream = null;
		Connection conexao = null;
		
		Exception erro = null;
		try {
			
			this.loadResources();
			this.loadParams(parametros);
			
			outputStream = new ByteArrayOutputStream();
			JasperReport jasperReport = this.carregarJasperReport(nomeRelatorio);
	
			conexao = BancoUtil.obterConexao();
	
			JasperPrint jasperPrint  = this.carregarJasperPrint(parametros, jasperReport, conexao);
	
			JRExporter exporter = new JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, diretorio.concat(nomePdf));
			
			
			exporter.exportReport();
			
			//String nome = this.formatar(nomeRelatorio);
			//new DefaultStreamedContent(new ByteArrayInputStream(outputStream.toByteArray()), "", nomePdf);
			return diretorio.concat("/").concat(nomePdf);
		
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			    this.fechar(outputStream, conexao);
		}
		return null;
		
	}
}