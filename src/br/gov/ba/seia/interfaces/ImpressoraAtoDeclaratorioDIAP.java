package br.gov.ba.seia.interfaces;

import static br.gov.ba.seia.util.AtoDeclaratorioUtil.retornarCaminhoJasper;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.RelatorioUtil;

/**
 * @author eduardo.fernandes 
 * @since 22/02/2017
 *
 */
public class ImpressoraAtoDeclaratorioDIAP extends ImpressoraAtoDeclaratorio {

	private String representantes;
	
	public ImpressoraAtoDeclaratorioDIAP(String representantes) {
		this.representantes = representantes;
	}

	@Override
	public StreamedContent imprimirCertificado(Integer ideRequerimento, DocumentoObrigatorioEnum docObrigatorioEnum) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornarCaminhoJasper(docObrigatorioEnum));
		lParametros.put("ide_requerimento", ideRequerimento);
		lParametros.put("representantes", representantes);
		return new RelatorioUtil("certificado.jasper", lParametros, null).gerar();
	}

}
