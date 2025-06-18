package br.gov.ba.seia.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RelatorioDTO;
import br.gov.ba.seia.enumerator.TipoArquivoEnum;
import br.gov.ba.seia.enumerator.TipoRelatorioEnum;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperUtil {

	private Map<String, Object> parametros;
	private RelatorioDTO relatorioDTO;

	public JasperUtil() {}

	private void prepararRelatorio(RelatorioDTO relatorioDTO, Map<String,Object> parametros){
		this.relatorioDTO = relatorioDTO;
		this.parametros = new HashMap<String, Object>();
		this.parametros.put("REPORT_LOCALE", 	new Locale("pt", "BR"));
		this.parametros.put("LOGO_INEMA", 		RecursosUtil.retornaCaminho(TipoArquivoEnum.IMAGEM, Uri.LOGO_INEMA));
		this.parametros.put("LOGO_SEIA", 		RecursosUtil.retornaCaminho(TipoArquivoEnum.IMAGEM, Uri.LOGO_SEMA));
		this.parametros.put("LOGO_GOVERNO", 	RelatorioUtil.retornaCaminhoImagem("LOGO_GOVERNO.png"));

		if(parametros.get("PASTA")==null && parametros.get("NOME_RELATORIO")==null){
			this.parametros.put("SUBREPORT_DIR", RecursosUtil.retornaCaminho(TipoArquivoEnum.RELATORIO,"") + File.separator);
		}

		else if (parametros.get("NOME_RELATORIO")==null){
			this.parametros.put("SUBREPORT_DIR", RecursosUtil.retornaCaminho(TipoArquivoEnum.RELATORIO,"")  + File.separator);
		}

		else if (parametros.get("NOME_RELATORIO")!=null){
			this.parametros.put("SUBREPORT_DIR", RecursosUtil.retornaCaminho(TipoArquivoEnum.RELATORIO,"") + File.separator + parametros.get("PASTA") + File.separator);
		}

		if(parametros.get("NOME_RELATORIO")==null){
			this.parametros.put("NOME_COMPLETO", this.parametros.get("SUBREPORT_DIR")+ "relatorio_padrao.jasper");
			this.parametros.put("NOME_RELATORIO","relatorio_padrao.jasper");
		}
		else{
			this.parametros.put("NOME_COMPLETO", this.parametros.get("SUBREPORT_DIR").toString() + parametros.get("NOME_RELATORIO").toString());
		}

		if (parametros.containsKey("MARCA_DAGUA")) {
			parametros.put("MARCA_DAGUA", RecursosUtil.retornaCaminho(TipoArquivoEnum.IMAGEM,Uri.MARCA_DAGUA));
		}

		if(relatorioDTO!=null){

			if(relatorioDTO.getTitulo()!=null){
				this.parametros.put("TITULO", relatorioDTO.getTitulo());
			}

			if(relatorioDTO.getIdeEmpreendimento()!=null){
				this.parametros.put("IDE_EMPREENDIMENTO", relatorioDTO.getIdeEmpreendimento());
			}

			if(relatorioDTO.getIdeRequerente()!=null){
				this.parametros.put("IDE_REQUERENTE", relatorioDTO.getIdeRequerente());
			}

			if(relatorioDTO.getNumero()!=null){
				this.parametros.put("NUM_RELATORIO", relatorioDTO.getNumero());
			}

			if(relatorioDTO.getTituloNumero()!=null){
				this.parametros.put("TITULO_NUM_RELATORIO", relatorioDTO.getTituloNumero());
			}

			if(relatorioDTO.getData()!=null){
				this.parametros.put("DATA_RELATORIO", relatorioDTO.getData());
			}

			if(relatorioDTO.getTituloData()!=null){
				this.parametros.put("TITULO_DATA_RELATORIO", relatorioDTO.getTituloData());
			}

			this.parametros.put("IS_EXIBIR_DECLARACAO", relatorioDTO.getIsExibirDeclaracao());
			this.parametros.put("IS_EXIBIR_RODAPE", 	relatorioDTO.getIsExibirRodape());
		}


		if(parametros.get("PASTA")!=null && !ExpressaoRegularUtil.matches(parametros.get("PASTA").toString(), ExpressaoRegularUtil.terminaComBarra)){
			parametros.put("PASTA",parametros.get("PASTA")+File.separator);
		}

		if(relatorioDTO.getTipoRelatorioEnum()==null || relatorioDTO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.PDF)){
			this.parametros.put("TIPO_RELATORIO", "PDF");
		}

		this.parametros.putAll(parametros);
	}


	public StreamedContent gerar(RelatorioDTO relatorioDTO, Map<String,Object> parametros, TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
		if(relatorioDTO == null){
			relatorioDTO = new RelatorioDTO();
		}

		if(tipoRelatorioEnum != null){
			relatorioDTO.setTipoRelatorioEnum(tipoRelatorioEnum);
		}

		prepararRelatorio(relatorioDTO,parametros );
		return gerar();
	}

	private StreamedContent gerar() throws Exception {
		ByteArrayOutputStream outputStream = null;
		Connection conexao = null;
		JasperReport jasperReport = null;

		try {

				outputStream = new ByteArrayOutputStream();

				jasperReport = (JasperReport) JRLoader.loadObjectFromFile(this.parametros.get("NOME_COMPLETO").toString());

				conexao = BancoUtil.obterConexao();
				String ext = "";


				JRExporter relatorio = null;

				if(relatorioDTO==null || relatorioDTO.getTipoRelatorioEnum()==null || relatorioDTO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.PDF)){
					relatorio =  new JRPdfExporter();
					ext = ".pdf";
				}

				else if(relatorioDTO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.XLS)){
					relatorio = new JRXlsExporter();
					ext = ".xls";
				}


				relatorio.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
				relatorio.setParameter(JRExporterParameter.JASPER_PRINT, JasperFillManager.fillReport(jasperReport, parametros, conexao));
				relatorio.exportReport();

			return new DefaultStreamedContent(new ByteArrayInputStream(
					outputStream.toByteArray()),"",
					getNomeRelatorioFormatado(this.parametros.get("NOME_RELATORIO" ).toString())+ "_"+new Date().getTime()+ext
				);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw new Exception("Não foi possível gerar o relatório.", e);
		} finally {

			if(outputStream != null){
				outputStream.close();
			}

			if(conexao != null){
				conexao.close();
			}

		}

	}

	/***
	 * Gets e Settes
	 **/

	private String getNomeRelatorioFormatado(String nomeRelatorio) {
		return nomeRelatorio.replace(".jasper", "").replace(".jrxml", "");
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

}