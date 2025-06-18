package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;

public class DocumentoObrigatorioEnquadramentoDTO {
	
	private EnquadramentoAtoAmbiental enquadramentoAtoAmbiental;
	private List<DocumentoObrigatorioInterface> listaDocumentoObrigatorio;
	
	
	public List<DocumentoObrigatorioInterface> getListaDocumentoObrigatorio() {
		return listaDocumentoObrigatorio;
	}
	public void setListaDocumentoObrigatorio(
			List<DocumentoObrigatorioInterface> listaDocumentoObrigatorio) {
		this.listaDocumentoObrigatorio = listaDocumentoObrigatorio;
	}
	public EnquadramentoAtoAmbiental getEnquadramentoAtoAmbiental() {
		return enquadramentoAtoAmbiental;
	}
	public void setEnquadramentoAtoAmbiental(
			EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		this.enquadramentoAtoAmbiental = enquadramentoAtoAmbiental;
	}
}
