package br.gov.ba.seia.interfaces;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

/**
 * @author eduardo.fernandes 
 * @since 22/02/2017
 *
 */
public interface ImpressoraAtoDeclaratorioIF {

	public StreamedContent imprimirCertificado(Integer ideRequerimento, DocumentoObrigatorioEnum docObrigatorioEnum) throws Exception;
	
	public StreamedContent imprimirRelatorio(AtoDeclaratorio atoDeclaratorio) throws Exception;
	
}
