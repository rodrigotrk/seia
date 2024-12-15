package br.gov.ba.seia.interfaces;

import static br.gov.ba.seia.util.AtoDeclaratorioUtil.retornarCaminhoJasper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 22/02/2017
 *
 */
public class ImpressoraAtoDeclaratorio implements ImpressoraAtoDeclaratorioIF {

	@Override
	public StreamedContent imprimirCertificado(Integer ideRequerimento, DocumentoObrigatorioEnum docObrigatorioEnum) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornarCaminhoJasper(docObrigatorioEnum));
		lParametros.put("ide_requerimento", ideRequerimento);
		lParametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		return new RelatorioUtil("certificado.jasper", lParametros, null).gerar();
	}

	@Override
	public StreamedContent imprimirRelatorio(AtoDeclaratorio atoDeclaratorio) throws Exception {
		try {
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("SUBREPORT_DIR", retornarCaminhoJasper(DocumentoObrigatorioEnum.getEnum(atoDeclaratorio.getIdeDocumentoObrigatorio())));
			lParametros.put("ide_ato_declaratorio", atoDeclaratorio.getIdeAtoDeclaratorio());
			lParametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			return new RelatorioUtil("relatorio.jasper", lParametros, null).gerar();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível gerar o relatório.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

}
