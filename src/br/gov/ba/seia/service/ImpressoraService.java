package br.gov.ba.seia.service;

import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.enumerator.TipoRelatorioEnum;

public class ImpressoraService {
	
	@Inject
	private ImpressoraCerhService impressoraCerhService;
	
	public StreamedContent resumoQuantitativoCerh(Map<String, Object> parametros, TipoRelatorioEnum tipoRelatorioEnum) {
		return impressoraCerhService.resumoQuantitativoCerh(parametros, tipoRelatorioEnum );
	}
	
	public StreamedContent resumoCerh(Cerh cerh	) {
		return impressoraCerhService.resumoCerh(cerh);
	}

	public StreamedContent imprimirCertificado(Cerh cerh) {
		return impressoraCerhService.imprimirCertificado(cerh);
	}

}
